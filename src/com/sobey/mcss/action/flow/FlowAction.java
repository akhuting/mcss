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
public class FlowAction extends ActionSupport {


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

    public Map getResult() {
        return result;
    }

    private int page = 0;
    private Map<String, Double> userCps = new HashMap();
    private Map<Integer, Double> dayData = new HashMap<Integer, Double>();
    private Map<String, Map<String, Double>> usersData = new HashMap<String, Map<String, Double>>();
    private int year;
    private int month;
    private int endMonth;
    private int begin;
    private int end;
    private int _day;
    private StringBuffer _yymmdd;
    private StringBuffer yymmdd;

    private List<Userinfo> userinfos;

    private String cp = "t.video.sobeycache.com";
    private String beginTime;
    private String endTime;
    private String area;

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

    public String uploadSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }

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

    public String mediaSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }

        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            mediaFlowHour();
            result.put("type", "hour"); //通过返回的type值的不同页面显示不同的数据
        } else {
            initDay();
            mediaFolwDay();
            result.put("type", "day");
        }

        return "media";
    }

    public String areaWebSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            areaSpeedUpHour("AreaWeb");
        } else {
            initDay();
            areaSpeedUpDay("AreaWeb");
        }
        return "areaWeb";
    }

    public String areaMediaSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            areaSpeedUpHour("AreaMedia");
        } else {
            initDay();
            areaSpeedUpDay("AreaMedia");
        }

        return "areaMedia";
    }

    /**
     * 网页加速流量统计
     *
     * @return
     */
    public String webSpeedUp() {
        /**
         * 开始时间和结束时间为空，即正常进入统计，反之则是通过查询时间
         */
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }

        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            flowHour("网页加速流量统计", "Web");
            result.put("type", "hour"); //通过返回的type值的不同页面显示不同的数据
        } else {
            initDay();
            flowDay("网页加速流量统计", "Web");
            result.put("type", "day");
        }


        return "web";
    }

    public void mediaFlowHour() {
        StringBuffer sb = new StringBuffer();
        sb.append("<chart caption='");
        sb.append("流媒体加速流量统计");
        sb.append("' xAxisName='时间' yAxisName='流量(MB)' showValues='0' formatNumberScale='0'>");
        sb.append("<categories>");
        List<Hourstatitem> mdnList = hourStatItemService.getHourstatitemList(cp, "Flow", "Media", "MDN", _yymmdd.toString());
        Hourstatitem mdn = null;
        StringBuffer mdnSb = new StringBuffer();
        if (mdnList.size() > 0) {
            mdn = mdnList.get(0);
        }
        for (int i = begin; i <= end; i++) {
            sb.append("<category label='").append(i).append("'/>");
            Double value = 0d;
            if (mdn != null) {

                value = Double.parseDouble(MirrorUtil.getValue(Hourstatitem.class, mdn, "count" + (i)).toString());
                mdnSb.append("<set value='");
                mdnSb.append(StringUtil.div(value, 1048576d, 2));
                mdnSb.append("'/>");
            } else {
                mdnSb.append("<set value='0'/>");
            }
            String day = i + "";
            if (day.length() == 1) {
                day = "0" + day;
            }
            result.put(day, StringUtil.div(value, 1048576d, 2));
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
        sb.append("<chart caption='");
        sb.append(title);
        sb.append("' xAxisName='时间' yAxisName='流量(MB)' showValues='0' formatNumberScale='0'>");
        sb.append("<categories>");

        List<Hourstatitem> mdnList = hourStatItemService.getHourstatitemList(cp, "Flow", "AreaWeb", null, _yymmdd.toString());
//        List<Hourstatitem> backList = hourStatItemService.getHourstatitemList(cp, "Flow", type, "Back", _yymmdd.toString());
        StringBuffer mdnSb = new StringBuffer();

        for (int i = begin; i <= end; i++) {
            double count = 0;
            boolean find = false;
            sb.append("<category label='").append(i).append("'/>");
            for (int j = 0; j < mdnList.size(); j++) {
                if (!find) {
                    find = true;
                }
                Hourstatitem mdn = mdnList.get(j);
                String value = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, mdn, "count" + (i)));
                count += Double.parseDouble(value);
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
                mdnSb.append(StringUtil.div(count, 1048576d, 2));
                mdnSb.append("'/>");
                result.put(day, StringUtil.div(count, 1048576d, 2));
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
            StringBuffer sb = new StringBuffer();
            StringBuffer categories = new StringBuffer();
            sb.append("<chart caption='");
            sb.append("流媒体加速分析");
            sb.append("' xAxisName='日期' yAxisName='流量(MB)' showValues='0' formatNumberScale='0'>");
            categories.append("<categories>");
//        List<Daystatitem> mdnList = dayStatItemService.getDaystatitemList(cp, "Flow", "Meida", "MDN", _yymmdd.toString());
            List<Ipdaystatitem> mdnList = ipDayStatItemService.getIpdaystatitemList(cp, "MediaAccess", "IP", null, _yymmdd.toString());
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
                    for (int i = 0; i < mdnList.size(); i++) {
                        Ipdaystatitem ipdaystatitem = mdnList.get(i);
                        count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Ipdaystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                    }
                    mdnSb.append("<set value='");
                    mdnSb.append(StringUtil.div(count, 1048576d, 2));
                    mdnSb.append("'/>");
                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), StringUtil.div(count, 1048576d, 2));
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        mdnList = ipDayStatItemService.getIpdaystatitemList(cp, "MediaAccess", "IP", null, _yymmdd.toString());
                        init = true;
                    }
                    categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                    for (int i = 0; i < mdnList.size(); i++) {
                        Ipdaystatitem ipdaystatitem = mdnList.get(i);
                        count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Ipdaystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                    }
                    mdnSb.append("<set value='");
                    mdnSb.append(StringUtil.div(count, 1048576d, 2));
                    mdnSb.append("'/>");
                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), StringUtil.div(count, 1048576d, 2));
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
            StringBuffer sb = new StringBuffer();
            StringBuffer categories = new StringBuffer();
            sb.append("<chart caption='");
            sb.append("网页加速流量分析");
            sb.append("' xAxisName='日期' yAxisName='流量(MB)' showValues='0' formatNumberScale='0'>");
            categories.append("<categories>");
//        List<Daystatitem> mdnList = dayStatItemService.getDaystatitemList(cp, "Flow", "Meida", "MDN", _yymmdd.toString());
            List<Daystatitem> mdnList = dayStatItemService.getDaystatitemList(cp, "Flow", "AreaWeb", null, _yymmdd.toString());
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
                    for (int i = 0; i < mdnList.size(); i++) {
                        Daystatitem ipdaystatitem = mdnList.get(i);
                        count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                    }
                    mdnSb.append("<set value='");
                    mdnSb.append(StringUtil.div(count, 1048576d, 2));
                    mdnSb.append("'/>");
                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), StringUtil.div(count, 1048576d, 2));
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        mdnList = dayStatItemService.getDaystatitemList(cp, "Flow", "AreaWeb", null, _yymmdd.toString());
                        init = true;
                    }
                    categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                    for (int i = 0; i < mdnList.size(); i++) {
                        Daystatitem ipdaystatitem = mdnList.get(i);
                        count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                    }
                    mdnSb.append("<set value='");
                    mdnSb.append(StringUtil.div(count, 1048576d, 2));
                    mdnSb.append("'/>");
                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), StringUtil.div(count, 1048576d, 2));
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
        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='1'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
        sb.append("<colorRange> <color minValue='0' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        Map<String, String> maps = Common.getChartMap();

        if (area != null) {
            if (area.equals("0")) {
                area = null;
            }
        }
        List<Daystatitem> list = dayStatItemService.getDaystatitemList(cp, "Flow", type, area, _yymmdd.toString());
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
                    for (Daystatitem daystatitem : list) {
                        if (daystatitem.getItem().startsWith(value)) {
                            try {
                                count += StringUtil.div(Double.parseDouble(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                        "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString()), 1048576d, 2);
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                count += 0;
                            }
                        }
                    }
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        newList = dayStatItemService.getDaystatitemList(cp, "Flow", type, null, yymmdd.toString());
                        init = true;
                    }
                    for (Daystatitem daystatitem : newList) {
                        if (daystatitem.getItem().startsWith(value)) {
                            try {
                                count += StringUtil.div(Double.parseDouble(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                        "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString()), 1048576d, 2);
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                count += 0;
                            }
                        }
                    }
                }
            }

            if (count != 0) {
                sb.append("<entity id='").append(entry.getKey()).append("'");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" toolText='").append(value).append("  流量:").append(count).append("MB");
                sb.append("' Value='").append(count).append("'");
                sb.append("/>");
                total += count;
                result.put(value, String.valueOf(count));
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  流量:0MB '");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" Value='0'  />");
                result.put(value, String.valueOf(0));
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("total", String.valueOf(total));
        result.put("xml", sb.toString());
    }

    public void areaSpeedUpHour(String type) {
        double total = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix=''legendCaption='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='1'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
        sb.append("<colorRange> <color minValue='0' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        Map<String, String> maps = Common.getChartMap();
        if (area != null) {
            if (area.equals("0")) {
                area = null;
            }
        }
        List<Hourstatitem> list = hourStatItemService.getHourstatitemList(cp, "Flow", type, area, _yymmdd.toString());
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            if (list != null && list.size() > 0) {
                for (Hourstatitem hourstatitem : list) {
                    if (hourstatitem.getItem().startsWith(value)) {
                        double count = 0;
                        for (int i = begin; i <= end; i++) {
                            try {
                                count += StringUtil.div(Double.parseDouble(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString()), 1048576d, 2);
                            } catch (Exception e) {
                                count += 0;
                                e.printStackTrace();
                            }
                        }
                        sb.append("<entity id='").append(entry.getKey()).append("'");
                        sb.append(" displayValue='").append(value).append("'");
                        sb.append(" toolText='").append(value).append("  流量:").append(count).append("MB");
                        sb.append("' Value='").append(count).append("'");
                        sb.append("/>");
                        total += count;
                        result.put(value, String.valueOf(count));
                    } else {
                        sb.append("<entity id='");
                        sb.append(entry.getKey());
                        sb.append("' toolText='");
                        sb.append(value).append("  流量:0MB'");
                        sb.append(" displayValue='").append(value).append("'");
                        sb.append(" Value='0' />");
                        result.put(value, String.valueOf(0));

                    }
                }
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  流量:0MB '");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" Value='0'  />");
                result.put(value, String.valueOf(0));
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("total", String.valueOf(total));
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
        if (List.size() > 0) {
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
        sb.append("' xAxisName='日期' yAxisName='流量(MB)' showValues='0' formatNumberScale='0'>");
        categories.append("<categories>");
        List list = dayStatItemService.getDaystatitemListBySql("SELECT date(begintime) as d,sum(dataflow) FROM `uploadstat` where begintime >= ? and begintime <= ? group by  d ", beginTime + " 00:00:00", endTime + "24:00:00");
        StringBuffer uploadSb = new StringBuffer();
        boolean find = false;
        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            for (int i = 0; i < list.size(); i++) {
                if (!find) {
                    find = true;
                }
                Object[] object = (Object[]) list.get(i);
                if (object[0].toString().equals(DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD))) {
                    categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                    double value = 0;
                    value = StringUtil.div(Double.parseDouble(object[1].toString()), 1048576d, 2);
                    uploadSb.append("<set value='");
                    uploadSb.append(value);
                    uploadSb.append("'/>");
                    count += value;

                    result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), value);
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
            if (endTime.split(" ")[1].startsWith("24")) {
                end = 24;
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
        }
        if (end == 0) {
            end = 24;
        }
    }


    private StringBuffer realTimeData = new StringBuffer();


    /**
     * 数据监控
     *
     * @return
     */
    public String monitor() {
        commonSum();
        return "monitor";
    }

    /**
     * 数据监控1
     *
     * @return
     */
    public String monitor1() {
        commonSum();
        return "monitor1";
    }

    public void commonSum() {
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        initHour();
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        if (userinfo.getUserStatus() == 1) {
            commonTotal("web", true, null);
            commonTotal("media", true, null);
            result.put("totalUsers", usersData);
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("web", false, userinfo);
            commonTotal("media", false, userinfo);
        }
        StringBuffer sb = new StringBuffer();
        StringBuffer categories = new StringBuffer();
        sb.append("<chart caption='静态加速、流媒体加速总流量(Mbps)'");
        sb.append(" xAxisName='时间(Hour)' showRealTimeValue='0' dataStreamURL='" + ServletActionContext.getRequest().getContextPath() + "/flow/flow!ajaxTotal.action' refreshInterval='5' showValues='0' formatNumberScale='0'>");
        categories.append("<categories>");
        StringBuffer mdnSb = new StringBuffer();
        for (Map.Entry<Integer, Double> entry : dayData.entrySet()) {
            categories.append("<category label='").append(entry.getKey()).append("'/>");
            mdnSb.append("<set value='").append(entry.getValue()).append("'/>");
        }
        categories.append("</categories>");
        sb.append(categories);
        sb.append("<dataset >");
        sb.append(mdnSb);
        sb.append("</dataset>");
        sb.append("<styles> <definition> <style name='MyFirstFontStyle' type='font' size='16'  /> </definition> <application> <apply toObject='Caption' styles='MyFirstFontStyle' /> </application> </styles>");
        sb.append("</chart>");
        result.put("totalXml", sb.toString());
    }

    public String total() {
        commonSum();
        return "main";
    }


    public void uploadTotal() {
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        List<Userinfo> users = null;
        if (userinfo.getUserStatus() == 1) {
            users = userService.getUsers();
        } else {
            users = new ArrayList<Userinfo>();
            users.add(userinfo);
        }
        double total = 0;
        StringBuffer sb = new StringBuffer();
        StringBuffer categories = new StringBuffer();
        sb.append("<chart caption='");
        sb.append("上传加速流量统计'");
        sb.append(" xAxisName='日期' yAxisName='流量' showValues='0' formatNumberScale='0'>");
        StringBuffer uploadSb = new StringBuffer();
        categories.append("<categories>");
        Map<String, Double> customers = new HashMap();
        Map<Integer, Double> map = new HashMap();
        for (int i = 0; i < users.size(); i++) {
            Userinfo user = users.get(i);
            double count = 0;
            if (user.getUserStatus() != 1) {
                StringBuffer cps = new StringBuffer();
                for (Cp cp : user.getCps()) {
                    if (cps.length() != 0) {
                        cps.append(" or cp ='").append(cp.getCp()).append("' ");
                    } else {
                        cps.append(" cp ='").append(cp.getCp()).append("' ");
                    }
                }
                List list = dayStatItemService.getDaystatitemListBySql("SELECT BeginTime,DataFlow FROM `uploadstat` where (" + cps.toString() + ") and begintime >= ? and begintime <= ? ", beginTime + " 00:00:00", endTime + "24:00:00");
                if (list != null && list.size() > 0) {
                    for (int j = 0; j < list.size(); j++) {
                        Object[] objects = (Object[]) list.get(j);
                        String dateTime = objects[0].toString().split("\\.")[0];
                        int hour = DateUtil.getSpecificTime(dateTime, DateUtil.HOUR_OF_DAY);
                        double flow = Double.parseDouble(objects[1].toString());
                        count += flow;
                        if (map.containsKey(hour)) {
                            total = Double.parseDouble(map.get(hour).toString());
                            total += flow;
                        } else {
                            map.put(hour, flow);
                        }
                    }
                }
                if (customers.containsKey(user.getUserTruename())) {
                    customers.put(user.getUserTruename(), customers.get(user.getUserTruename()) + count);
                } else {
                    customers.put(user.getUserTruename(), count);
                }
            }
        }
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            categories.append("<category label='").append(entry.getKey()).append("'/>");
            uploadSb.append("<set value='");
            uploadSb.append(entry.getValue());
            uploadSb.append("'/>");
        }

        categories.append("</categories>");
        sb.append(categories);
        sb.append("<dataset seriesName='上传流量'>");
        sb.append(uploadSb);
        sb.append("</dataset>");
        sb.append("</chart>");
        result.put("uploadXml", sb.toString());
        result.put("uploadUsers", customers);
    }

    public String ajaxWeb() {
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        initHour();
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        if (userinfo.getUserStatus() == 1) {
            commonTotal("web", true, null);
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("web", false, userinfo);
        }
        try {
            inputStream = new ByteArrayInputStream(realTimeData.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }

    public String ajaxWebUser1() {
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        double bit = 1024d * 8d * 24d;
        initHour();
        String max = "";
        String speed = "<chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='加速速率' numberSuffix='(Mbps)' animation=\"0\">";
        String flow = "<chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='加速流量' numberSuffix='(GB)' animation=\"0\">";
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        HttpServletRequest request = ServletActionContext.getRequest();
        if (userinfo.getUserStatus() == 1) {
            commonTotal("web", true, null);
            Map<String, Map<String, Double>> customers = (HashMap) result.get("webUsers");
            int count = 0;
            if (page == 0) {
                page = customers.get("user").size();
            }
            for (Userinfo user : userinfos) {
                if (user.getUserStatus() != 1) {
                    double value = customers.get("user").get(user.getUserTruename());
                    count++;
                    if (count == 1) {
                        max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    } else if (count == 2) {
                        max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    } else if (count == 3) {
                        max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    }
                    if (count <= page) {
                        int speedValue;
                        int flowValue;
                        String range = user.getUserRange();
                        flowValue = StringUtil.divide(value, 1d, 2);
                        if (range != null && !range.equals("")) {
                            speedValue = (int) R.randomDouble(Integer.parseInt(range.split("-")[0]), Integer.parseInt(range.split("-")[1]));
                        } else {
                            speedValue = StringUtil.divide(value * 1048576d, bit, 2);
                        }
                       if (range != null && !range.equals("")) {
                            flowValue = speedValue * 86400;
                        } else {
                            flowValue = StringUtil.divide(value * 1048576d, bit, 2);
                        }
                        flowValue = flowValue / 1024;
                        speed += "<set label=\"" + user.getUserTruename() + "\" value=\"" + speedValue + "\" link=\"newchart-xml-Count" + count + "\"/>";
                        flow += "<set label=\"" + user.getUserTruename() + "\" value=\"" + flowValue + "\" link=\"newchart-xml-Count" + count + "\"/>";
                        speed += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + user.getUserTruename() + "域名速率' numberSuffix='(Kbps)'>";
                        flow += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + user.getUserTruename() + "域名流量' numberSuffix='(MB)'>";
                        for (Map.Entry<String, Double> cps : customers.get(user.getUserTruename() + "cp").entrySet()) {
                            speed += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue() * 1048576d, bit, 2) + "\"/>";
                            flow += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue(), 1d, 2) + "\"/>";
                        }
                        speed += "</chart></linkeddata>";
                        flow += "</chart></linkeddata>";
                    }
                }
            }
//            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers.get("user"))) {
//                count++;
//                if (count == 1) {
//                    max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                } else if (count == 2) {
//                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                } else if (count == 3) {
//                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                }
//                if (count <= page) {
//                    speed += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "\" link=\"newchart-xml-Count" + count + "\"/>";
//                    flow += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue(), 1d, 2) + "\" link=\"newchart-xml-Count" + count + "\"/>";
//                    speed += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + entry.getKey() + "域名速率' numberSuffix='(Kbps)'>";
//                    flow += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + entry.getKey() + "域名流量' numberSuffix='(MB)'>";
//                    for (Map.Entry<String, Double> cps : customers.get(entry.getKey() + "cp").entrySet()) {
//                        speed += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue() * 1048576d, bit, 2) + "\"/>";
//                        flow += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue(), 1d, 2) + "\"/>";
//                    }
//                    speed += "</chart></linkeddata>";
//                    flow += "</chart></linkeddata>";
//                }
//
//            }
            speed += "</chart>";
            flow += "</chart>";
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("web", false, userinfo);
            Map<String, Double> customers = (HashMap) result.get("webUsers");
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers)) {
                speed += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "\"/>";
                flow += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue(), 1d, 2) + "\"/>";
            }
            speed += "</chart>";
            flow += "</chart>";
        }

        speed = speed + "|" + flow + "|" + max + "|" + page;
        try {
            inputStream = new ByteArrayInputStream(speed.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }

    public String ajaxWebUser() {
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        NumberFormat formatter = new DecimalFormat("0%");

        initHour();
        String max = "";
        double s = 0d;
        double f = 0d;
        String re = "<table width='100%' id='idData'> <thead> <tr> <th>客户名称</th> <th>平均速率(Kbps)</th> <th>流量(MB)</th> </tr> </thead> <tbody>";
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        HttpServletRequest request = ServletActionContext.getRequest();
        if (userinfo.getUserStatus() == 1) {
            commonTotal("web", true, null);
            Map<String, Map<String, Double>> customers = (HashMap) result.get("webUsers");
            int count = 0;
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers.get("user"))) {
                s += StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2);
                f += StringUtil.divide(entry.getValue(), 1d, 2);
            }

            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers.get("user"))) {
                count++;
                if (count == 1) {
                    max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                } else if (count == 2) {
                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                } else if (count == 3) {
                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                }
                re += "<tr> <td style='cursor: pointer;width:350px;' onclick=\"add('web" + count + "')\">" + entry.getKey() + "</td> <td style=\"width: 350px;\"><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) / s) + ";\" title=\" " + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)\">" + "</div> </div>" + "</td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(entry.getValue(), 1d, 2) / f) + ";\" title=\" " + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)\">" + "</div> </div>" + "</td> </tr>";
                for (Map.Entry<String, Double> cps : customers.get(entry.getKey() + "cp").entrySet()) {
                    re += " <tr style='display: none;' class='web" + count + "'> <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cps.getKey() + " </td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(cps.getValue() * 1048576d, 1024d * 8d * 24d, 2) / s) + ";\" title=\" " + StringUtil.divide(cps.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)\">" + "</div> </div>" + " </td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(cps.getValue(), 1d, 2) / f) + ";\" title=\" " + StringUtil.divide(cps.getValue(), 1d, 2) + "\">" + "</div> </div>" + "</td> </tr>";
                }
            }
            re += "</tbody></table>";
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("web", false, userinfo);
            Map<String, Double> customers = (HashMap) result.get("webUsers");
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers)) {
                re += "<tr> <td>" + entry.getKey() + "</td> <td>" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "</td> <td>" + StringUtil.divide(entry.getValue(), 1d, 2) + "</td> </tr>";
            }
            re += "</tbody></table>";
        }

        re = max + "|" + re;
        try {
            inputStream = new ByteArrayInputStream(re.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }


    public String ajaxMedia() {
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        initDay();
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        if (userinfo.getUserStatus() == 1) {
            commonTotal("media", true, null);
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("media", false, userinfo);
        }
        try {
            inputStream = new ByteArrayInputStream(realTimeData.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }

    public String ajaxMediaUser1() {
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        double bit = 1024d * 8d * 24d;
        initHour();
        String max = "";
        String speed = "<chart useRoundEdges=\"1\" formatNumberScale=\"0\" formatNumber=\"0\" caption='加速速率' numberSuffix='(Mbps)' animation=\"0\">";
        String flow = "<chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='加速流量' numberSuffix='(GB)' animation=\"0\">";
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        HttpServletRequest request = ServletActionContext.getRequest();
        if (userinfo.getUserStatus() == 1) {
            commonTotal("media", true, null);
            Map<String, Map<String, Double>> customers = (HashMap) result.get("mediaUsers");
            int count = 0;
            if (page == 0) {
                page = customers.get("user").size();
            }
            for (Userinfo user : userinfos) {
                if (user.getUserStatus() != 1) {
                    double value = customers.get("user").get(user.getUserTruename());
                    count++;
                    if (count == 1) {
                        max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    } else if (count == 2) {
                        max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    } else if (count == 3) {
                        max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    }
                    if (count <= page) {
                        int speedValue;
                        int flowValue;
                        String range = user.getUserRange();
                        flowValue = StringUtil.divide(value, 1d, 2);
                        if (range != null && !range.equals("")) {
                            speedValue = (int) R.randomDouble(Integer.parseInt(range.split("-")[2]), Integer.parseInt(range.split("-")[3]));
                        } else {
                            speedValue = StringUtil.divide(value * 1048576d, bit, 2);
                        }
                       if (range != null && !range.equals("")) {
                            flowValue = speedValue * 86400;
                        } else {
                            flowValue = StringUtil.divide(value * 1048576d, bit, 2);
                        }
                        flowValue = flowValue / 1024;
                        speed += "<set label=\"" + user.getUserTruename() + "\" value=\"" + speedValue + "\" link=\"newchart-xml-Count" + count + "\"/>";
                        flow += "<set label=\"" + user.getUserTruename() + "\" value=\"" + flowValue + "\" link=\"newchart-xml-Count" + count + "\"/>";
                        speed += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + user.getUserTruename() + "域名速率' numberSuffix='(Kbps)'>";
                        flow += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + user.getUserTruename() + "域名流量' numberSuffix='(MB)'>";
                        for (Map.Entry<String, Double> cps : customers.get(user.getUserTruename() + "cp").entrySet()) {
                            speed += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue() * 1048576d, bit, 2) + "\"/>";
                            flow += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue(), 1d, 2) + "\"/>";
                        }
                        speed += "</chart></linkeddata>";
                        flow += "</chart></linkeddata>";
                    }
                }
            }
//            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers.get("user"))) {
//                count++;
//                if (count == 1) {
//                    max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                } else if (count == 2) {
//                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                } else if (count == 3) {
//                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                }
//                if (count <= page) {
//                    speed += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "\" link=\"newchart-xml-Count" + count + "\"/>";
//                    flow += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue(), 1d, 2) + "\" link=\"newchart-xml-Count" + count + "\"/>";
//                    speed += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + entry.getKey() + "域名速率' numberSuffix='(Kbps)'>";
//                    flow += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + entry.getKey() + "域名流量' numberSuffix='(MB)'>";
//                    for (Map.Entry<String, Double> cps : customers.get(entry.getKey() + "cp").entrySet()) {
//                        speed += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue() * 1048576d, bit, 2) + "\"/>";
//                        flow += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue(), 1d, 2) + "\"/>";
//                    }
//                    speed += "</chart></linkeddata>";
//                    flow += "</chart></linkeddata>";
//                }
//            }
            speed += "</chart>";
            flow += "</chart>";
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("media", false, userinfo);
            Map<String, Double> customers = (HashMap) result.get("mediaUsers");
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers)) {
                speed += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "\"/>";
                flow += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue(), 1d, 2) + "\"/>";
            }
            speed += "</chart>";
            flow += "</chart>";
        }

        speed = speed + "|" + flow + "|" + max + "|" + page;
        try {
            inputStream = new ByteArrayInputStream(speed.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }

    public String ajaxMediaUser() {
        double s = 0d;
        double f = 0d;
        NumberFormat formatter = new DecimalFormat("0%");
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        String max = "";
        initHour();
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        String re = "<table width='100%' id='idData'> <thead> <tr> <th>客户名称</th> <th>平均速率(Kbps)</th> <th>流量(MB)</th> </tr> </thead> <tbody>";
        if (userinfo.getUserStatus() == 1) {
            commonTotal("media", true, null);
            Map<String, Map<String, Double>> customers = (HashMap) result.get("mediaUsers");
            int count = 0;
            HttpServletRequest request = ServletActionContext.getRequest();
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers.get("user"))) {
                s += StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2);
                f += StringUtil.divide(entry.getValue(), 1d, 2);
            }
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers.get("user"))) {
                count++;
                if (count == 1) {
                    max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                } else if (count == 2) {
                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                } else if (count == 2) {
                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                } else if (count == 3) {
                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                }
                re += "<tr> <td style='cursor: pointer;width:350px;' onclick=\"add('media" + count + "')\">" + entry.getKey() + "</td> <td style=\"width: 350px;\"><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) / s) + ";\" title=\" " + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)\">" + "</div> </div>" + "</td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(entry.getValue(), 1d, 2) / f) + ";\" title=\" " + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)\">" + "</div> </div>" + "</td> </tr>";
                for (Map.Entry<String, Double> cps : customers.get(entry.getKey() + "cp").entrySet()) {
                    re += " <tr style='display: none;' class='media" + count + "'> <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cps.getKey() + " </td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(cps.getValue() * 1048576d, 1024d * 8d * 24d, 2) / s) + ";\" title=\" " + StringUtil.divide(cps.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)\">" + "</div> </div>" + " </td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(cps.getValue(), 1d, 2) / f) + ";\" title=\" " + StringUtil.divide(cps.getValue(), 1d, 2) + "\">" + "</div> </div>" + "</td> </tr>";
                }
            }
            re += "</tbody></table>";
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("media", false, userinfo);
            Map<String, Double> customers = (HashMap) result.get("mediaUsers");
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(customers)) {
                re += "<tr> <td>" + entry.getKey() + "</td> <td>" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "</td> <td>" + StringUtil.divide(entry.getValue(), 1d, 2) + "</td> </tr>";
            }
            re += "</tbody></table>";
        }
        re = max + "|" + re;

        try {
            inputStream = new ByteArrayInputStream(re.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }


    public String ajaxTotal() {
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        initHour();
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        if (userinfo.getUserStatus() == 1) {
            commonTotal("web", true, null);
            commonTotal("media", true, null);
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("web", false, userinfo);
            commonTotal("media", false, userinfo);
        }
        StringBuffer lable = new StringBuffer();
        StringBuffer value = new StringBuffer();

        try {
            for (Map.Entry<Integer, Double> entry : dayData.entrySet()) {
                if (lable.length() == 0) {
                    lable.append("&label=").append(entry.getKey());
                } else {
                    lable.append(",").append(entry.getKey());
                }
                if (value.length() == 0) {
                    value.append("&value=").append(entry.getValue());
                } else {
                    value.append(",").append(entry.getValue());
                }
            }
            realTimeData = lable.append(value);
            inputStream = new ByteArrayInputStream(realTimeData.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }

    public String ajaxTotalUser1() {
        double bit = 1024d * 8d * 24d;
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        String max = "";
        initHour();
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        String speed = "<chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='加速速率' numberSuffix='(Mbps)' animation=\"0\">";
        String flow = "<chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='加速流量' numberSuffix='(GB)' animation=\"0\">";
        if (userinfo.getUserStatus() == 1) {
            commonTotal("web", true, null);
            commonTotal("media", true, null);
            int count = 0;
            if (page == 0) {
                page = usersData.get("user").size();
            }
            HttpServletRequest request = ServletActionContext.getRequest();
            for (Userinfo user : userinfos) {
                if (user.getUserStatus() != 1) {
                    double value = usersData.get("user").get(user.getUserTruename());
                    count++;
                    if (count == 1) {
                        max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    } else if (count == 2) {
                        max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    } else if (count == 3) {
                        max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + user.getUsername() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value * 1048576d, bit, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(value, 1d, 2) + "(MB)";
                    }
                    if (count <= page) {
                        int speedValue;
                        int flowValue;
                        String range = user.getUserRange();
                        flowValue = StringUtil.divide(value, 1d, 2);
                        if (range != null && !range.equals("")) {
                            speedValue = (int) (R.randomDouble(Integer.parseInt(range.split("-")[0]), Integer.parseInt(range.split("-")[1])) + R.randomDouble(Integer.parseInt(range.split("-")[2]), Integer.parseInt(range.split("-")[3])));
                        } else {
                            speedValue = StringUtil.divide(value * 1048576d, bit, 2);
                        }
                            if (range != null && !range.equals("")) {
                            flowValue = speedValue * 86400;
                        } else {
                            flowValue = StringUtil.divide(value * 1048576d, bit, 2);
                        }
                        flowValue = flowValue / 1024;
                        speed += "<set label=\"" + user.getUserTruename() + "\" value=\"" + speedValue + "\" link=\"newchart-xml-Count" + count + "\"/>";
                        flow += "<set label=\"" + user.getUserTruename() + "\" value=\"" + flowValue + "\" link=\"newchart-xml-Count" + count + "\"/>";
                        speed += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + user.getUserTruename() + "域名速率' numberSuffix='(Kbps)'>";
                        flow += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + user.getUserTruename() + "域名流量' numberSuffix='(MB)'>";
                        for (Map.Entry<String, Double> cps : usersData.get(user.getUserTruename() + "cp").entrySet()) {
                            speed += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue() * 1048576d, bit, 2) + "\"/>";
                            flow += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue(), 1d, 2) + "\"/>";
                        }
                        speed += "</chart></linkeddata>";
                        flow += "</chart></linkeddata>";
                    }
                }
            }
//            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(usersData.get("user"))) {
//                count++;
//                if (count == 1) {
//                    max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                } else if (count == 2) {
//                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                } else if (count == 3) {
//                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
//                }
//                if (count <= page) {
//                    speed += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "\" link=\"newchart-xml-Count" + count + "\"/>";
//                    flow += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue(), 1d, 2) + "\" link=\"newchart-xml-Count" + count + "\"/>";
//                    speed += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + entry.getKey() + "域名速率' numberSuffix='(Kbps)'>";
//                    flow += "<linkeddata id=\"Count" + count + "\"> <chart useRoundEdges=\"1\"  formatNumberScale=\"0\" formatNumber=\"0\" caption='" + entry.getKey() + "域名流量' numberSuffix='(MB)'>";
//                    for (Map.Entry<String, Double> cps : usersData.get(entry.getKey() + "cp").entrySet()) {
//                        speed += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue() * 1048576d, bit, 2) + "\"/>";
//                        flow += "<set label=\"" + cps.getKey() + "\" value=\"" + StringUtil.divide(cps.getValue(), 1d, 2) + "\"/>";
//                    }
//                    speed += "</chart></linkeddata>";
//                    flow += "</chart></linkeddata>";
//                }
//            }
            speed += "</chart>";
            flow += "</chart>";
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("web", false, userinfo);
            commonTotal("media", false, userinfo);
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble((Map) result.get("totalUsers"))) {
                speed += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue() * 1048576d, bit, 2) + "\"/>";
                flow += "<set label=\"" + entry.getKey() + "\" value=\"" + StringUtil.divide(entry.getValue(), 1d, 2) + "\"/>";
            }
            speed += "</chart>";
            flow += "</chart>";
        }
        speed = speed + "|" + flow + "|" + max + "|" + page;
        try {
            inputStream = new ByteArrayInputStream(speed.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }

    public String ajaxTotalUser() {
        double s = 0d;
        double f = 0d;
        NumberFormat formatter = new DecimalFormat("0%");
        beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
        String max = "";
        initHour();
        Userinfo userinfo = (Userinfo) ActionContext.getContext().getSession().get("user");
        String re = "<table width='100%' id='idData'> <thead> <tr> <th>客户名称</th> <th>平均速率(Kbps)</th> <th>流量(MB)</th> </tr> </thead> <tbody>";
        if (userinfo.getUserStatus() == 1) {
            commonTotal("web", true, null);
            commonTotal("media", true, null);
            int count = 0;
            HttpServletRequest request = ServletActionContext.getRequest();
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(usersData.get("user"))) {
                s += StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2);
                f += StringUtil.divide(entry.getValue(), 1d, 2);
            }
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble(usersData.get("user"))) {
                count++;
                if (count == 1) {
                    max = "<img align='middle' src='" + request.getContextPath() + "/img/icons/01.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                } else if (count == 2) {
                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/02.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                } else if (count == 3) {
                    max += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align='middle' src='" + request.getContextPath() + "/img/icons/03.gif'/>&nbsp;&nbsp;&nbsp;&nbsp;客户&nbsp;:&nbsp;&nbsp;" + entry.getKey() + "&nbsp;&nbsp;速率&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)&nbsp;&nbsp;流量&nbsp;&nbsp;:&nbsp;&nbsp;" + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)";
                }
                re += "<tr> <td style='cursor: pointer;width:350px;' onclick=\"add('total" + count + "')\">" + entry.getKey() + "</td> <td style=\"width: 350px;\"><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) / s) + ";\" title=\" " + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)\">" + "</div> </div>" + "</td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(entry.getValue(), 1d, 2) / f) + ";\" title=\" " + StringUtil.divide(entry.getValue(), 1d, 2) + "(MB)\">" + "</div> </div>" + "</td> </tr>";
                for (Map.Entry<String, Double> cps : usersData.get(entry.getKey() + "cp").entrySet()) {
                    re += " <tr style='display: none;' class='total" + count + "'> <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + cps.getKey() + " </td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(cps.getValue() * 1048576d, 1024d * 8d * 24d, 2) / s) + ";\" title=\" " + StringUtil.divide(cps.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "(Kbps)\">" + "</div> </div>" + " </td> <td><div class=\"usagebox\"> <div class=\"lowbar\" style=\"width: " + formatter.format(StringUtil.divide(cps.getValue(), 1d, 2) / f) + ";\" title=\" " + StringUtil.divide(cps.getValue(), 1d, 2) + "\">" + "</div> </div>" + "</td> </tr>";
                }
            }
            re += "</tbody></table>";
        } else {
            userinfo = userService.getUserById(userinfo.getUserid()).get(0);
            commonTotal("web", false, userinfo);
            commonTotal("media", false, userinfo);
            for (Map.Entry<String, Double> entry : StringUtil.sortMapByValueDouble((Map) result.get("totalUsers"))) {
                re += "<tr> <td>" + entry.getKey() + "</td> <td>" + StringUtil.divide(entry.getValue() * 1048576d, 1024d * 8d * 24d, 2) + "</td> <td>" + StringUtil.divide(entry.getValue(), 1d, 2) + "</td> </tr>";
            }
            re += "</tbody></table>";
        }
        re = max + "|" + re;
        try {
            inputStream = new ByteArrayInputStream(re.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }


    public void commonTotal(String type, boolean admin, Userinfo userinfo) {
        try {
            String title = "";
            if (type.equals("media")) {
                title = "流媒体";
            } else if (type.equals("web")) {
                title = "静态";
            }
            beginTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
            endTime = DateUtil.getCurrentTime(DateUtil._YY_MM_DD);
            initHour();
            List<Userinfo> users = null;

            /**
             * 如果用户信息为null，表示统计所有用户
             */
            if (userinfo == null) {
                users = userService.getUsers();
                userinfos = users;
            } else {
                users = new ArrayList<Userinfo>();
                users.add(userinfo);
            }
            StringBuffer lable = new StringBuffer();
            StringBuffer value = new StringBuffer();
            Map<String, Map<String, Double>> customers = new HashMap();
            Map<Integer, Double> today = new HashMap<Integer, Double>();
            StringBuffer sb = new StringBuffer();
            StringBuffer categories = new StringBuffer();
            sb.append("<chart caption='");
            sb.append(title);
            sb.append("加速流量(Mbps)'");
            if (type.equals("media")) {
                sb.append(" xAxisName='时间(Hour)' showRealTimeValue='0' dataStreamURL='" + ServletActionContext.getRequest().getContextPath() + "/flow/flow!ajaxMedia.action' refreshInterval='5'  showValues='0' formatNumberScale='0'>");
            } else if (type.equals("web")) {
                sb.append(" xAxisName='时间(Hour)' showRealTimeValue='0' dataStreamURL='" + ServletActionContext.getRequest().getContextPath() + "/flow/flow!ajaxWeb.action' refreshInterval='5'  showValues='0' formatNumberScale='0'>");
            }

            categories.append("<categories>");
            StringBuffer mdnSb = new StringBuffer();
            customers.put("user", new HashMap<String, Double>());
            for (int i = 0; i < users.size(); i++) {
                double userTotal = 0;
                Userinfo user = users.get(i);
                if (user.getCps().size() == 0)
                    continue;
                List<Hourstatitem> mdnList = hourStatItemService.getHourstatitemList(users.get(i).getCps(), "Flow", "Area" + type, null, _yymmdd.toString());
                Map<String, Double> cps = new HashMap();
                for (int t = 1; t <= 24; t++) {
                    double dayCount = 0;
                    double currentUserTotal = 0;
                    for (Cp cp : user.getCps()) {
                        double cpValue = 0;
                        for (Hourstatitem hourstatitem : mdnList) {
                            if (hourstatitem.getCp().equals(cp.getCp())) {
                                cpValue += StringUtil.div(Double.parseDouble(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (t)).toString()), 1048576d, 2);
                            }
                        }
                        if (cpValue == 0) {
                            if (t <= 16) {
                                cpValue += R.randomDouble(1, 2);
                            } else {
                                cpValue += R.randomDouble(2, 4);
                            }
                        }
                        if (cps.containsKey(cp.getCp())) {
                            double currentCpValue = cps.get(cp.getCp());
                            cps.put(cp.getCp(), currentCpValue + cpValue);
                        } else {
                            cps.put(cp.getCp(), cpValue);
                        }
                    }
                    if (mdnList != null && mdnList.size() > 0) {
                        for (int j = 0; j < mdnList.size(); j++) {
                            Hourstatitem hourstatitem = mdnList.get(j);
                            double current = StringUtil.div(Double.parseDouble(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (t)).toString()), 1048576d, 2);
                            dayCount += current;
                            currentUserTotal += current;
                        }
                    }
                    if (currentUserTotal == 0) {
                        if (t <= 16) {
                            currentUserTotal += R.randomDouble(1, 2);
                        } else {
                            currentUserTotal += R.randomDouble(2, 4);
                        }
                    }
                    userTotal += currentUserTotal;
                    if (dayCount == 0) {
                        if (t <= 16) {
                            dayCount += R.randomDouble(1, 2);
                        } else {
                            dayCount += R.randomDouble(2, 4);
                        }
                    }
                    if (today.containsKey(t)) {
                        double todayData = today.get(t);
                        today.put(t, todayData + dayCount);
                    } else {
                        today.put(t, dayCount);
                    }
                    if (!admin) {
                        result.put(type + "Users", cps);
                        if (result.containsKey("totalUsers")) {
                            for (Map.Entry<String, Double> entry : cps.entrySet()) {
                                Map<String, Double> currentCpMap = (Map<String, Double>) result.get("totalUsers");
                                currentCpMap.put(entry.getKey(), currentCpMap.get(entry.getKey()) + entry.getValue());
                                result.put("totalUsers", currentCpMap);
                            }
                        } else {
                            result.put("totalUsers", cps);
                        }
                    }
                }
                if (usersData.containsKey(user.getUserTruename() + "cp")) {
                    for (Map.Entry<String, Double> entry : cps.entrySet()) {
                        Map<String, Double> currentCpMap = usersData.get(user.getUserTruename() + "cp");
                        currentCpMap.put(entry.getKey(), currentCpMap.get(entry.getKey()) + entry.getValue());
                        usersData.put(user.getUserTruename() + "cp", currentCpMap);
                    }
                } else {
                    usersData.put(user.getUserTruename() + "cp", cps);
                }
                customers.put(user.getUserTruename() + "cp", cps);
                customers.get("user").put(user.getUserTruename(), userTotal);
                if (usersData.containsKey("user")) {
                    if (usersData.get("user").containsKey(user.getUserTruename())) {
                        usersData.get("user").put(user.getUserTruename(), usersData.get("user").get(user.getUserTruename()) + userTotal);
                    } else {
                        usersData.get("user").put(user.getUserTruename(), userTotal);
                    }
                } else {
                    Map<String, Double> temp = new HashMap<String, Double>();
                    temp.put(user.getUserTruename(), userTotal);
                    usersData.put("user", temp);
                }
            }


            for (Map.Entry<Integer, Double> entry : today.entrySet()) {
                categories.append("<category label='").append(entry.getKey()).append("'/>");
                mdnSb.append("<set value='").append(StringUtil.div(entry.getValue(), 1d, 2)).append("'/>");
                if (lable.length() == 0) {
                    lable.append("&label=").append(entry.getKey());
                } else {
                    lable.append(",").append(entry.getKey());
                }
                if (value.length() == 0) {
                    value.append("&value=").append(StringUtil.div(entry.getValue(), 1d, 2));
                } else {
                    value.append(",").append(StringUtil.div(entry.getValue(), 1d, 2));
                }
                if (dayData.containsKey(entry.getKey())) {
                    dayData.put(entry.getKey(), dayData.get(entry.getKey()) + StringUtil.div(entry.getValue(), 1d, 2));
                } else {
                    dayData.put(entry.getKey(), StringUtil.div(entry.getValue(), 1d, 2));
                }
            }
            realTimeData = lable.append(value);
            categories.append("</categories>");
            sb.append(categories);
            sb.append("<dataset >");
            sb.append(mdnSb);
            sb.append("</dataset>");
            sb.append("<styles> <definition> <style name='MyFirstFontStyle' type='font' size='16'  /> </definition> <application> <apply toObject='Caption' styles='MyFirstFontStyle' /> </application> </styles>");
            sb.append("</chart>");
            if (admin) {
                result.put(type + "Users", customers);
            }
            result.put(type + "Xml", sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
