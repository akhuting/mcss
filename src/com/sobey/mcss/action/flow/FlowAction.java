package com.sobey.mcss.action.flow;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.R;
import com.sobey.common.util.StringUtil;
import com.sobey.mcss.action.Common;
import com.sobey.mcss.action.McssService;
import com.sobey.mcss.domain.*;
import com.sobey.mcss.service.DayStatItemService;
import com.sobey.mcss.service.HourStatItemService;
import com.sobey.mcss.service.IpDayStatItemService;
import com.sobey.mcss.service.UserService;
import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Yanggang.
 * Date: 11-1-20
 * Time: 下午4:24
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Results({@Result(name = "web", location = "webSpeedUp.jsp"),
        @Result(name = "media", location = "mediaSpeedUp.jsp"),
        @Result(name = "main", location = "main.jsp"),
        @Result(name = "main1", location = "main1.jsp"),
        @Result(name = "monitor", location = "monitor.jsp"),
        @Result(name = "monitor1", location = "monitor1.jsp"),
        @Result(name = "upload", location = "uploadSpeedUp.jsp"),
        @Result(name = "areaWeb", location = "areaWebSpeedUp.jsp"),
        @Result(name = "areaMedia", location = "areaMediaSpeedUp.jsp"),
        @Result(name = "inputStream", type = "stream", params = {"contentType", "text/html;charset=utf-8", "inputName", "inputStream"})})
public class FlowAction extends ActionSupport  implements ServletRequestAware {


    @Autowired
    private IpDayStatItemService ipDayStatItemService;

    @Autowired
    private DayStatItemService dayStatItemService;

    @Autowired
    private HourStatItemService hourStatItemService;

    @Autowired
    private UserService userService;

    private InputStream inputStream;
    private Map result = new HashMap();

    private int page = 0;
    private String userCps;
    private Map<Integer, Double> dayData = new HashMap<Integer, Double>();
    private Map<String, Map<String, Double>> usersData = new HashMap<String, Map<String, Double>>();
    private int year;
    private int month;
    private int endMonth;
    private int begin;
    private int end;
    private int _day;
    private String unit_lable;
    private StringBuffer _yymmdd;
    private StringBuffer yymmdd;

    private List<Userinfo> userinfos;

    private String cp;
    private String beginTime;
    private String endTime;
    private String area;

    private HttpServletRequest request;

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCp() {
        return cp;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUnit_lable() {
        return unit_lable;
    }

    public Map getResult() {
        return result;
    }
    /**
     *  =====私有变量结束========
     */

    /**
     * 网页加速流量
     *
     * @return
     */
    public String webSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getFirstDayOfWeek();
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getLastDayOfWeek();
        }
        initCp();
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            flowHour("网页加速流量统计", "Web");
            result.put("type", "hour"); //通过返回的type值的不同页面显示不同的数据
        } else {
            initDay();
            if (cp == null) {
                getMaxCp(" from daystatitem where type ='Flow' and subtype ='AreaWeb' ");
                initCp();
            }
            flowDay("网页加速流量统计", "Web");
            result.put("type", "day");
        }


        return "web";
    }

    /**
     * 流媒体加速流量
     *
     * @return
     */
    public String mediaSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getFirstDayOfWeek();
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getLastDayOfWeek();
        }
        initCp();
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            mediaFlowHour();
            result.put("type", "hour"); //通过返回的type值的不同页面显示不同的数据
        } else {
            initDay();
            if (cp == null) {
                getMaxCp("  from daystatitem where type ='Flow' and subtype ='AreaMedia' ");
                initCp();
            }
            mediaFolwDay();
            result.put("type", "day");
        }

        return "media";
    }

    /**
     * 上传加速流量
     *
     * @return
     */
    public String uploadSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getFirstDayOfWeek();
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getLastDayOfWeek();
        }
        initCp();
        if (DateUtil.checkDay(beginTime, endTime)) {
            initDay();
            uploadDay();
            result.put("type", "day");
        } else {
            initDay();
            uploadDay();
            result.put("type", "day");
        }

        return "upload";
    }

    /**
     * 各地区网页加速流量
     *
     * @return
     */
    public String areaWebSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getFirstDayOfWeek();
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getLastDayOfWeek();
        }
        initCp();
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            areaSpeedUpHour("AreaWeb");
        } else {
            initDay();
            if (cp == null) {
                getMaxCp(" from daystatitem where type ='Flow' and subtype ='AreaWeb' ");
                initCp();
            }
            areaSpeedUpDay("AreaWeb");
        }
        return "areaWeb";
    }

    /**
     * 各地区流媒体加速流量
     *
     * @return
     */
    public String areaMediaSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getFirstDayOfWeek();
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getLastDayOfWeek();
        }
        initCp();
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            areaSpeedUpHour("AreaMedia");
        } else {
            initDay();
            if (cp == null) {
                getMaxCp("  from daystatitem where type ='Flow' and subtype ='AreaMedia' ");
                initCp();
            }

            areaSpeedUpDay("AreaMedia");
        }

        return "areaMedia";
    }


    public void mediaFlowHour() {
        setCp();
        List<Hourstatitem> mdnList = hourStatItemService.getHourstatitemList(userCps, "Flow", "AreaMedia", null, _yymmdd.toString());
//        List<Hourstatitem> mdnList = mcssService.getHourstatitemList(cp, "Flow", "Media", "MDN", _yymmdd.toString());
        Hourstatitem mdn = null;


        if (mdnList != null && mdnList.size() > 0) {
            mdn = mdnList.get(0);
        }
        double min = 0;
        for (int i = begin; i <= end; i++) {
            Double value = 0d;
            if (mdn != null) {
                for (int j = 0; j < mdnList.size(); j++) {
                    mdn = mdnList.get(j);
                    value += Double.parseDouble(MirrorUtil.getValue(Hourstatitem.class, mdn, "count" + (i)).toString());
                }
                if (min == 0) {
                    min = value;
                } else {
                    if (value < min && value > 0) {
                        min = value;
                    }
                }
            }

        }
        String[] unit = new String[1];
        StringUtil.byteToUnit(min, unit, null);
        unit_lable = unit[0];
        StringBuffer sb = new StringBuffer();
        StringBuffer mdnSb = new StringBuffer();
        sb.append("<chart caption='");
        sb.append("流媒体加速流量统计");
        sb.append("' xAxisName='时间' yAxisName='流量(" + unit[0] + ")' showValues='0' formatNumberScale='0'>");
        sb.append("<categories>");
        for (int i = begin; i <= end; i++) {
            sb.append("<category label='").append(i).append("'/>");
            Double value = 0d;
            if (mdn != null) {
                for (int j = 0; j < mdnList.size(); j++) {
                    mdn = mdnList.get(j);
                    value += Double.parseDouble(MirrorUtil.getValue(Hourstatitem.class, mdn, "count" + (i)).toString());
                }

                mdnSb.append("<set value='");
                mdnSb.append(StringUtil.byteToUnit(value, unit, unit[0]));
                mdnSb.append("'/>");
            } else {
                mdnSb.append("<set value='0'/>");
            }
            String day = i + "";
            if (day.length() == 1) {
                day = "0" + day;
            }
            result.put(day, StringUtil.byteToUnit(value, unit, unit[0]));
        }
        sb.append("</categories>");
        sb.append("<dataset seriesName='索贝MDN'>");
        sb.append(mdnSb);
        sb.append("</dataset>");
        sb.append("</chart>");
        result.put("xml", sb.toString());
    }

    /**
     * 加速流量小时统计
     */
    public void flowHour(String title, String type) {
        StringBuffer sb = new StringBuffer();
        setCp();
        List<Hourstatitem> mdnList = hourStatItemService.getHourstatitemList(userCps, "Flow", "AreaWeb", null, _yymmdd.toString());
//        List<Hourstatitem> backList = hourStatItemService.getHourstatitemList(cp, "Flow", type, "Back", _yymmdd.toString());
        double min = 0;
        for (int i = begin; i <= end; i++) {
            double count = 0;
            boolean find = false;
            if (mdnList != null) {
                for (int j = 0; j < mdnList.size(); j++) {
                    if (!find) {
                        find = true;
                    }
                    Hourstatitem mdn = mdnList.get(j);
                    String value = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, mdn, "count" + (i)));
                    count += Double.parseDouble(value);
                }
                if (min == 0) {
                    min = count;
                } else {
                    if (count < min && count > 0) {
                        min = count;
                    }
                }
            }
        }
        String[] unit = new String[1];
        StringUtil.byteToUnit(min, unit, null);
        unit_lable = unit[0];
        sb.append("<chart caption='");
        sb.append(title);
        sb.append("' xAxisName='时间' yAxisName='流量(" + unit[0] + ")' showValues='0' formatNumberScale='0'>");
        sb.append("<categories>");

        StringBuffer mdnSb = new StringBuffer();
        for (int i = begin; i <= end; i++) {
            double count = 0;
            boolean find = false;
            sb.append("<category label='").append(i).append("'/>");
            if (mdnList != null) {
                for (int j = 0; j < mdnList.size(); j++) {
                    if (!find) {
                        find = true;
                    }
                    Hourstatitem mdn = mdnList.get(j);
                    String value = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, mdn, "count" + (i)));
                    count += Double.parseDouble(value);
                }
            }
            String day = i + "";
            if (day.length() == 1) {
                day = "0" + day;
            }
            if (!find) {
                mdnSb.append("<set value='0'/>");
                result.put(day, 0);
            } else {
                mdnSb.append("<set value='");
                mdnSb.append(StringUtil.byteToUnit(count, unit, unit[0]));
                mdnSb.append("'/>");
                result.put(day, StringUtil.byteToUnit(count, unit, unit[0]));
            }
        }
        sb.append("</categories>");
        sb.append("<dataset seriesName='索贝MDN'>");
        sb.append(mdnSb);
        sb.append("</dataset>");
        sb.append("</chart>");
        result.put("xml", sb.toString());
    }

    public void mediaFolwDay() {
        try {
            boolean init = false;
            setCp();
            List<Daystatitem> mdnList = dayStatItemService.getDaystatitemList(userCps, "Flow", "AreaMedia", null, _yymmdd.toString());
//            List<Ipdaystatitem> mdnList = mcssService.getIpdaystatitemList(cp, "MediaAccess", "IP", null, _yymmdd.toString());
            double min = 0;
            if (min == 0) {
                Calendar calendar = Calendar.getInstance();
                Calendar compareCalendar = Calendar.getInstance();
                compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
                calendar.set(year, month - 1, begin);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                while (calendar.before(compareCalendar)) {
                    double count = 0;
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                        if (mdnList != null) {
                            for (int i = 0; i < mdnList.size(); i++) {
                                Daystatitem ipdaystatitem = mdnList.get(i);
                                count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                            }
                        }
                        if (min == 0) {
                            min = count;
                        } else {
                            if (count < min && count > 0) {
                                min = count;
                            }
                        }
                    } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                        if (!init) {
                            mdnList = dayStatItemService.getDaystatitemList(userCps, "Flow", "AreaMedia", null, _yymmdd.toString());
                            init = true;
                        }
                        if (mdnList != null) {
                            for (int i = 0; i < mdnList.size(); i++) {
                                Daystatitem ipdaystatitem = mdnList.get(i);
                                count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                            }
                        }
                        if (min == 0) {
                            min = count;
                        } else {
                            if (count < min && count > 0) {
                                min = count;
                            }
                        }
                    }
                }
            }
            init = false;
            String unit[] = new String[1];
            StringUtil.byteToUnit(min, unit, null);
            unit_lable = unit[0];
            StringBuffer sb = new StringBuffer();
            StringBuffer categories = new StringBuffer();
            sb.append("<chart caption='");
            sb.append("流媒体加速分析");
            sb.append("' xAxisName='日期' yAxisName='流量(" + unit[0] + ")' showValues='0' formatNumberScale='0'>");
            categories.append("<categories>");
            StringBuffer mdnSb = new StringBuffer();

            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, month - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            while (calendar.before(compareCalendar)) {
                double count = 0;
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                    categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                    if (mdnList != null) {
                        for (int i = 0; i < mdnList.size(); i++) {
                            Daystatitem ipdaystatitem = mdnList.get(i);
                            count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                        }
                    }
                    mdnSb.append("<set value='");
                    mdnSb.append(StringUtil.byteToUnit(count, null, unit[0]));
                    mdnSb.append("'/>");
                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), StringUtil.byteToUnit(count, null, unit[0]));
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        mdnList = dayStatItemService.getDaystatitemList(userCps, "Flow", "AreaMedia", null, _yymmdd.toString());
                        init = true;
                    }
                    categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                    if (mdnList != null) {
                        for (int i = 0; i < mdnList.size(); i++) {
                            Daystatitem ipdaystatitem = mdnList.get(i);
                            count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                        }
                    }
                    mdnSb.append("<set value='");
                    mdnSb.append(StringUtil.byteToUnit(count, null, unit[0]));
                    mdnSb.append("'/>");
                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), StringUtil.byteToUnit(count, null, unit[0]));
                }

            }
            categories.append("</categories>");
            sb.append(categories);
            sb.append("<dataset seriesName='索贝MDN'>");
            sb.append(mdnSb);
            sb.append("</dataset>");
            sb.append("</chart>");
            result.put("xml", sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加速流量天统计
     */
    public void flowDay(String title, String type) {
        try {
            boolean init = false;
//        List<Daystatitem> mdnList = dayStatItemService.getDaystatitemList(cp, "Flow", "Meida", "MDN", _yymmdd.toString());

            setCp();
            List<Daystatitem> mdnList = dayStatItemService.getDaystatitemList(userCps, "Flow", "AreaWeb", null, _yymmdd.toString());
            StringBuffer mdnSb = new StringBuffer();
            double min = 0;
            if (min == 0) {
                Calendar calendar = Calendar.getInstance();
                Calendar compareCalendar = Calendar.getInstance();
                compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
                calendar.set(year, month - 1, begin);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                while (calendar.before(compareCalendar)) {
                    double count = 0;
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                        if (mdnList != null) {
                            for (int i = 0; i < mdnList.size(); i++) {
                                Daystatitem ipdaystatitem = mdnList.get(i);
                                count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                            }
                            if (min == 0) {
                                min = count;
                            } else {
                                if (count < min && count > 0)
                                    min = count;
                            }
                        }
                    } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                        if (!init) {
                            mdnList = dayStatItemService.getDaystatitemList(userCps, "Flow", "AreaWeb", null, _yymmdd.toString());
                            init = true;
                        }
                        if (mdnList != null) {
                            for (int i = 0; i < mdnList.size(); i++) {
                                Daystatitem ipdaystatitem = mdnList.get(i);
                                count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                            }
                            if (min == 0) {
                                min = count;
                            } else {
                                if (count < min)
                                    min = count;
                            }
                        }
                    }
                }
            }
            String[] unit = new String[1];
            StringUtil.byteToUnit(min, unit, null);
            unit_lable = unit[0];
            init = false;
            StringBuffer sb = new StringBuffer();
            StringBuffer categories = new StringBuffer();
            sb.append("<chart caption='");
            sb.append("网页加速流量分析");
            sb.append("' xAxisName='日期' yAxisName='流量(" + unit[0] + ")' showValues='0' formatNumberScale='0'>");
            categories.append("<categories>");
            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, month - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            while (calendar.before(compareCalendar)) {
                double count = 0;
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                    categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                    if (mdnList != null) {
                        for (int i = 0; i < mdnList.size(); i++) {
                            Daystatitem ipdaystatitem = mdnList.get(i);
                            count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                        }
                    }
                    mdnSb.append("<set value='");
                    mdnSb.append(StringUtil.byteToUnit(count, unit, unit[0]));
                    mdnSb.append("'/>");
                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), StringUtil.byteToUnit(count, unit, unit[0]));
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        mdnList = dayStatItemService.getDaystatitemList(userCps, "Flow", "AreaWeb", null, _yymmdd.toString());
                        init = true;
                    }
                    categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                    if (mdnList != null) {
                        for (int i = 0; i < mdnList.size(); i++) {
                            Daystatitem ipdaystatitem = mdnList.get(i);
                            count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                        }
                    }
                    mdnSb.append("<set value='");
                    mdnSb.append(StringUtil.byteToUnit(count, unit, unit[0]));
                    mdnSb.append("'/>");
                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), StringUtil.byteToUnit(count, unit, unit[0]));
                }

            }
            categories.append("</categories>");
            sb.append(categories);
            sb.append("<dataset seriesName='索贝MDN'>");
            sb.append(mdnSb);
            sb.append("</dataset>");
            sb.append("</chart>");
            result.put("xml", sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void areaSpeedUpDay(String type) {
        boolean init = false;
        double total = 0;

        Map<String, String> maps = Common.getChartMap();

        if (area != null) {
            if (area.equals("0")) {
                area = null;
            }
        }
        double min = 0;
        setCp();
        List<Daystatitem> list = dayStatItemService.getDaystatitemList(userCps, "Flow", type, area, _yymmdd.toString());
        List<Daystatitem> newList = null;
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            double count = 0;
            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, month - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            while (calendar.before(compareCalendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                    if (list != null) {
                        for (Daystatitem daystatitem : list) {
                            if (daystatitem.getItem().startsWith(value)) {
                                try {
                                    count += Double.parseDouble(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                            "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    count += 0;
                                }
                            }
                        }
                    }
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        newList = dayStatItemService.getDaystatitemList(userCps, "Flow", type, null, yymmdd.toString());
                        init = true;
                    }
                    if (newList != null) {
                        for (Daystatitem daystatitem : newList) {
                            if (daystatitem.getItem().startsWith(value)) {
                                try {
                                    count += Double.parseDouble(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                            "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    count += 0;
                                }
                            }
                        }
                    }
                }
            }
            if (count != 0) {
                if (min == 0) {
                    min = count;
                } else {
                    if (count < min && count > 0) {
                        min = count;
                    }
                }
            }
        }
        String[] unit = new String[1];
        StringUtil.byteToUnit(min, unit, null);
        unit_lable = unit[0];
        init = false;
        newList = null;
        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='1'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
        sb.append("<colorRange> <color minValue='1' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            double count = 0;
            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, month - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            while (calendar.before(compareCalendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                    if (list != null) {
                        for (Daystatitem daystatitem : list) {
                            if (daystatitem.getItem().startsWith(value)) {
                                try {
                                    count += Double.parseDouble(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                            "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    count += 0;
                                }
                            }
                        }
                    }
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        newList = dayStatItemService.getDaystatitemList(userCps, "Flow", type, null, yymmdd.toString());
                        init = true;
                    }
                    if (newList != null) {
                        for (Daystatitem daystatitem : newList) {
                            if (daystatitem.getItem().startsWith(value)) {
                                try {
                                    count += Double.parseDouble(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                            "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    count += 0;
                                }
                            }
                        }
                    }
                }
            }

            if (count != 0) {
                sb.append("<entity id='").append(entry.getKey()).append("'");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" toolText='").append(value).append("  流量:").append(StringUtil.byteToUnit(count, null, unit[0])).append(unit[0]);
                sb.append("' Value='").append(StringUtil.byteToUnit(count, null, unit[0])).append("'");
                sb.append("/>");
                total += count;
                result.put(value, StringUtil.byteToUnit(count, null, unit[0]));
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  流量:0" + unit[0] + " '");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" Value='0'  />");
                result.put(value, String.valueOf(0));
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("total", StringUtil.byteToUnit(total, null, unit[0]));
        result.put("xml", sb.toString());
    }

    public void areaSpeedUpHour(String type) {
        double total = 0;
        Map<String, String> maps = Common.getChartMap();
        if (area != null) {
            if (area.equals("0")) {
                area = null;
            }
        }
        setCp();
        List<Hourstatitem> list = hourStatItemService.getHourstatitemList(userCps, "Flow", type, area, _yymmdd.toString());
        double min = 0;
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            if (list != null && list.size() > 0) {
                for (Hourstatitem hourstatitem : list) {
                    if (hourstatitem.getItem().startsWith(value)) {
                        double count = 0;
                        for (int i = begin; i <= end; i++) {
                            try {
                                count += Double.parseDouble(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                            } catch (Exception e) {
                                count += 0;
                                e.printStackTrace();
                            }
                        }
                        if (min == 0) {
                            min = count;
                        } else {
                            if (count < min && count > 0) {
                                min = count;
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }

        String unit[] = new String[1];
        StringUtil.byteToUnit(min, unit, null);
        unit_lable = unit[0];
        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix=''legendCaption='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='1'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
        sb.append("<colorRange> <color minValue='1' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            boolean find = false;
            if (list != null && list.size() > 0) {
                for (Hourstatitem hourstatitem : list) {
                    if (hourstatitem.getItem().startsWith(value)) {
                        double count = 0;
                        for (int i = begin; i <= end; i++) {
                            try {
                                count += Double.parseDouble(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                            } catch (Exception e) {
                                count += 0;
                                e.printStackTrace();
                            }
                        }
                        sb.append("<entity id='").append(entry.getKey()).append("'");
                        sb.append(" displayValue='").append(value).append("'");
                        sb.append(" toolText='").append(value).append("  流量:").append(StringUtil.byteToUnit(count, null, unit[0])).append(unit[0]);
                        sb.append("' Value='").append(StringUtil.byteToUnit(count, null, unit[0])).append("'");
                        sb.append("/>");
                        total += count;
                        result.put(value, StringUtil.byteToUnit(count, null, unit[0]));
                        find = true;
                    }
                    if (!find) {
                        sb.append("<entity id='");
                        sb.append(entry.getKey());
                        sb.append("' toolText='");
                        sb.append(value).append("  流量:0" + unit[0] + "'");
                        sb.append(" displayValue='").append(value).append("'");
                        sb.append(" Value='0' />");
                        result.put(value, String.valueOf(0));
                    }
                }
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  流量:0" + unit[0] + " '");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" Value='0'  />");
                result.put(value, String.valueOf(0));
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("total", StringUtil.byteToUnit(total, null, unit[0]));
        result.put("xml", sb.toString());
    }

    public void uploadHour() {
        int count = 0;
        StringBuffer sb = new StringBuffer();
        StringBuffer categories = new StringBuffer();
        StringBuffer dataset = new StringBuffer();
        sb.append("<chart caption='上传加速流量统计' xAxisName='时间' yAxisName='流量' showValues='0' formatNumberScale='0'>");
        categories.append("<categories>");
        dataset.append("<dataset seriesName='上传流量'>");
        List<Hourstatitem> List = hourStatItemService.getHourstatitemList(cp, "Flow", "Upload", "Upload", _yymmdd.toString());
        Hourstatitem upload = null;
        if (List != null && List.size() > 0) {
            upload = List.get(0);
        } else {
            return;
        }
        for (int i = begin; i <= end; i++) {
            categories.append("<category label='").append(i).append("'/>");
            String value = "";
            if (upload != null) {
                value = MirrorUtil.getValue(Hourstatitem.class, upload, "count" + (i)).toString();
                dataset.append("<set value='");
                dataset.append(value);
                dataset.append("'/>");
            } else {
                value += "0";
                dataset.append("<set value='0'/>");
            }
            String day = i + "";
            if (day.length() == 1) {
                day = "0" + day;
            }
            count += Integer.parseInt(value);
            result.put(day, value);

        }
        categories.append("</categories>");
        dataset.append("</dataset>");
        sb.append(categories);
        sb.append(dataset);
        sb.append("</chart>");
        result.put("count", count);
        result.put("xml", sb.toString());
    }

    public void uploadDay() {
        double count = 0;
        boolean init = false;
        StringBuffer sb = new StringBuffer();
        StringBuffer categories = new StringBuffer();
        sb.append("<chart caption='");
        sb.append("上传加速流量统计");
        sb.append("' xAxisName='日期' yAxisName='流量(GB)' showValues='0' formatNumberScale='0'>");
        categories.append("<categories>");
        List list = null;
        if (userCps == null) {
            userCps = " cp = ''";
        }

        list = dayStatItemService.getDaystatitemListBySql("SELECT date(begintime) as d,sum(dataflow) FROM `uploadstat` where 1=1 and " + userCps + "  and begintime >= ? and begintime <= ? group by  d ", beginTime + " 00:00:00", endTime + "24:00:00");
        StringBuffer uploadSb = new StringBuffer();
        boolean find = false;
        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (!find) {
                        find = true;
                    }
                    Object[] object = (Object[]) list.get(i);
                    if (object[0].toString().equals(DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD))) {
                        categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                        double value = 0;
                        value = Double.parseDouble(object[1].toString());
                        uploadSb.append("<set value='");
                        uploadSb.append(value);
                        uploadSb.append("'/>");
                        count += value;

                        result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), value);
                    }

                }
            }
        }
        result.put("count", count);
        if (!find) {
            categories.append("<category label='").append(DateUtil.getSpecificTime(beginTime, DateUtil.YY_MM_D)).append("'/>");
            uploadSb.append("<set value='");
            uploadSb.append("0");
            uploadSb.append("'/>");
        }
        categories.append("</categories>");
        sb.append(categories);
        sb.append("<dataset seriesName='上传流量'>");
        sb.append(uploadSb);
        sb.append("</dataset>");
        sb.append("</chart>");
        result.put("xml", sb.toString());
    }

    private void initDay() {
        year = DateUtil.getSpecificTime(beginTime, DateUtil.YEAR);
        month = DateUtil.getSpecificTime(beginTime, DateUtil.MONTH) + 1;
        endMonth = DateUtil.getSpecificTime(endTime, DateUtil.MONTH) + 1;
        begin = DateUtil.getSpecificTime(beginTime, DateUtil.DAY_OF_MONTH);
        try {
            if (endTime.split(" ")[1].startsWith("24")) {
                end = DateUtil.getSpecificTime(endTime, DateUtil.DAY_OF_MONTH) - 1;
            } else {
                end = DateUtil.getSpecificTime(endTime, DateUtil.DAY_OF_MONTH);
            }
        } catch (Exception e) {
            end = DateUtil.getSpecificTime(endTime, DateUtil.DAY_OF_MONTH);
        }
        _yymmdd = new StringBuffer();
        _yymmdd.append(year);
        if (String.valueOf(month).length() == 1) {
            _yymmdd.append("0").append(month);
        } else {
            _yymmdd.append(month);
        }
        yymmdd = new StringBuffer();
        if (month != endMonth) {
            yymmdd.append(year);
            if (String.valueOf(month).length() == 1) {
                yymmdd.append("0").append(endMonth);
            } else {
                yymmdd.append(endMonth);
            }
        }

    }

    private void initHour() {
        year = DateUtil.getSpecificTime(beginTime, DateUtil.YEAR);
        month = DateUtil.getSpecificTime(beginTime, DateUtil.MONTH) + 1;
        _day = DateUtil.getSpecificTime(beginTime, DateUtil.DAY_OF_MONTH);
        begin = DateUtil.getSpecificTime(beginTime, DateUtil.HOUR_OF_DAY);
        try {
            if (endTime.split(" ")[1].startsWith("23")) {
//                end = 24;
            } else {
                end = DateUtil.getSpecificTime(endTime, DateUtil.HOUR_OF_DAY);
            }
        } catch (Exception e) {
            end = DateUtil.getSpecificTime(beginTime, DateUtil.HOUR_OF_DAY);
        }
        _yymmdd = new StringBuffer();
        _yymmdd.append(year);
        if (String.valueOf(month).length() == 1) {
            _yymmdd.append("0").append(month);
        } else {
            _yymmdd.append(month);
        }
        if (String.valueOf(_day).length() == 1) {
            _yymmdd.append("0").append(_day);
        } else {
            _yymmdd.append(_day);
        }
        /**
         * begin,end等于0表示是查询某一天24小时的数据
         */
        if (begin == 0) {
            begin = 1;
        }else{
            begin = begin + 1;
        }
        if (end == 0) {
            end = 24;
        }else{
            end = end + 1;
        }


    }




    /**
     * 查询本周值最大的CP
     *
     * @param sql
     */
    private void getMaxCp(String sql) {
        StringBuffer sum = new StringBuffer();

        //本周结束日期小于本周开始日期，表示结束日期已是下月
        if (end < begin) {
            /**
             * 如果结束日期在下月，先查出本月本周开始日期到月底最大的CP
             * 再查出下月月初到本周结束最大的CP，返回两者最大
             */
            int lastDayOfMonth = DateUtil.getLastDayOfMonth();//本月最后有一天
            for (int i = begin; i <= lastDayOfMonth; i++) {
                if (sum.length() == 0) {
                    sum.append("count" + i);
                } else {
                    sum.append("+count" + i);
                }
            }
            List list = dayStatItemService.getDaystatitemListBySql("select cp , " + sum.toString() + sql + " and Period='" + _yymmdd.toString() + "'  ORDER BY (" + sum.toString() + ") desc limit 0 , 1", null);
            if (list != null && list.size() > 0) {
                Object[] objects = (Object[]) list.get(0);
                sum.setLength(0);
                for (int i = 1; i <= end; i++) {
                    if (sum.length() == 0) {
                        sum.append("count" + i);
                    } else {
                        sum.append("+count" + i);
                    }
                }
                list = dayStatItemService.getDaystatitemListBySql("select cp , " + sum.toString() + sql + " and Period='" + yymmdd.toString() + "'  ORDER BY (" + sum.toString() + ") desc limit 0 , 1", null);
                if (list != null && list.size() > 0) {
                    Object[] _objects = (Object[]) list.get(0);
                    double one = Double.parseDouble(objects[1].toString());
                    double two = Double.parseDouble(_objects[1].toString());
                    if (!objects[1].equals("0") && !_objects[1].equals("0")) {
                        if (two > one) {
                            this.cp = _objects[0].toString();
                        } else {
                            this.cp = objects[0].toString();
                        }
                    }
                } else {
                    if (!objects[1].toString().equals("0")) {
                        this.cp = objects[0].toString();
                    }
                }
            }
        } else {
            //如果本周开始和结束都是当月，那直接累加出最大值即可！
            for (int i = begin; i <= end; i++) {
                if (sum.length() == 0) {
                    sum.append("count" + i);
                } else {
                    sum.append("+count" + i);
                }
            }
            List list = dayStatItemService.getDaystatitemListBySql("select cp , " + sum.toString() + sql + " and Period='" + _yymmdd.toString() + "'  ORDER BY (" + sum.toString() + ") desc limit 0 , 1", null);
            if (list != null && list.size() > 0) {
                Object[] _objects = (Object[]) list.get(0);
                this.cp = _objects[0].toString();
            }
        }
        if(cp ==null)
            return;
        boolean find = false;


        for(Cp _cp : getAllCp()){
            if(cp.equalsIgnoreCase(_cp.getCp())){
                find = true;
                break;
            }
        }
        if(!find){
            this.cp = null;
        }
    }

    private void initCp() {
        if (cp != null) {
            List<Cp> cps = getAllCp();
            StringBuffer temp = null;
            for (Cp _cp : cps) {
                if (_cp.getCp().equals(this.cp) && _cp.getPid() == 0) {
                    if (temp == null) {
                        temp = new StringBuffer();
                    }
                    temp.append("( cp = '").append(_cp.getCp()).append("' ");
                    for (Cp subCp : cps) {
                        if(subCp.getPid() == _cp.getId())
                            temp.append(" or cp = '").append(subCp.getCp()).append("'");
                    }
                    temp.append(")");
                }
            }
            if (temp != null) {
                userCps = temp.toString();
            }
        }
    }

    /**
     *
     */
    private void setCp() {
        if (cp != null && userCps == null) {
            userCps = " cp = '" + this.cp + "'";
        }
    }

    private List<Cp> getAllCp(){
        Userinfo userinfo = (Userinfo) request.getSession().getAttribute("user");
        return  userService.getUserById(userinfo.getUserid()).get(0).getCps();
    }
}
