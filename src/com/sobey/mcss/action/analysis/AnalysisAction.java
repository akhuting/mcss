package com.sobey.mcss.action.analysis;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.StringUtil;
import com.sobey.mcss.action.Common;
import com.sobey.mcss.domain.Daystatitem;
import com.sobey.mcss.domain.Hourstatitem;
import com.sobey.mcss.service.DayStatItemService;
import com.sobey.mcss.service.HourStatItemService;
import com.sobey.mcss.service.IpDayStatItemService;
import com.sobey.mcss.service.UrlDayStatItemService;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * Created by Yanggang.
 * Date: 11-2-14
 * Time: 下午3:58
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"NullArgumentToVariableArgMethod"})
@Controller
@Results({@Result(name = "viewed", location = "pageview.jsp"),
        @Result(name = "area", location = "accessArea.jsp"),
        @Result(name = "watch", location = "liveWatch.jsp"),
        @Result(name = "visitors", location = "visitors.jsp"),
        @Result(name = "visitorsSource", location = "visitorsSource.jsp"),
        @Result(name = "liveStay", location = "liveStay.jsp"),
        @Result(name = "liveArea", location = "liveArea.jsp"),
        @Result(name = "vodPlay", location = "vodPlay.jsp"),
        @Result(name = "vodStay", location = "vodStay.jsp"),
        @Result(name = "vodArea", location = "vodArea.jsp")})
public class AnalysisAction extends ActionSupport {

    private int year;
    private int month;
    private int endMonth;
    private int begin;
    private int end;
    private int _day;
    private StringBuffer _yymmdd;
    private StringBuffer yymmdd;

    private String cp = "t.video.sobeycache.com";
    private String channel;
    private String beginTime;
    private String endTime;
    private Map result = new HashMap();

    //区域
    private String area;

    @Autowired
    private HourStatItemService hourStatItemService;

    @Autowired
    private DayStatItemService dayStatItemService;

    @Autowired
    private IpDayStatItemService ipDayStatItemService;

    @Autowired
    private UrlDayStatItemService urlDayStatItemService;

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Map getResult() {
        return result;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getCp() {
        return cp;
    }

    public String getChannel() {
        return channel;
    }

    public String visitorsSource() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        initDay();
        if (DateUtil.checkDay(beginTime, endTime)) {
            visitorsSourceDay();
        } else {
            visitorsSourceDay();
        }
        return "visitorsSource";
    }

    public String webAccess() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        initDay();
        if (DateUtil.checkDay(beginTime, endTime)) {
            webAccessDay();
        } else {
            webAccessDay();
        }
        return "visitors";
    }

    public String webView() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            webViewHour();
            result.put("type", "hour");
        } else {
            initDay();
            webViewDay();
            result.put("type", "day");
        }
        return "viewed";
    }

    public String webArea() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            webAreaHour();
        } else {
            initDay();
            webAreaDay();
        }
        return "area";
    }

    public String liveWatch() {
        if (StringUtil.checkEmpty(beginTime)) {
            channel = "all";
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (channel != null && channel.equals("all")) {
            if (DateUtil.checkDay(beginTime, endTime)) {
                initHour();
                liveWatchChannelHour();
            } else {
                initDay();
                liveWatchChannelDay();
            }
            result.put("type", "day");
            result.put("data", "频道");
        } else {
            if (DateUtil.checkDay(beginTime, endTime)) {
                initHour();
                liveWatchHour();
                result.put("type", "hour");
            } else {
                initDay();
                liveWatchDay();
                result.put("type", "day");
            }
        }

        return "watch";
    }

    public String liveStay() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        initDay();
        if (channel != null && channel.equals("all")) {
            if (DateUtil.checkDay(beginTime, endTime)) {
                liveStayChannelDay();
            } else {
                liveStayChannelDay();
            }
        } else {
            if (DateUtil.checkDay(beginTime, endTime)) {
                liveStayDay();
            } else {
                liveStayDay();
            }
        }
        return "liveStay";
    }

    public String liveArea() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        initDay();
        if (channel != null && channel.equals("all")) {
            if (DateUtil.checkDay(beginTime, endTime)) {
                liveAreaChannelDay();
            } else {
                liveAreaChannelDay();
            }
        } else {
            if (DateUtil.checkDay(beginTime, endTime)) {
                liveAreaDay();
            } else {
                liveAreaDay();
            }
        }

        return "liveArea";
    }

    public String vodPlay() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (DateUtil.checkDay(beginTime, endTime)) {
            initHour();
            vodPlayHour();
            result.put("type", "hour");
        } else {
            initDay();
            vodPlayDay();
            result.put("type", "day");
        }
        return "vodPlay";
    }

    public String vodStay() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        initDay();
        if (DateUtil.checkDay(beginTime, endTime)) {
            vodStayDay();
        } else {
            vodStayDay();
        }
        return "vodStay";
    }

    public String vodArea() {
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        initDay();
        if (DateUtil.checkDay(beginTime, endTime)) {
            vodAreaDay();
        } else {
            vodAreaDay();
        }
        return "vodArea";
    }

    public void webViewHour() {

        StringBuffer sb = new StringBuffer();
        sb.append("<chart caption='");
        sb.append("浏览量统计");
        sb.append("' xAxisName='时间' yAxisName='PV值' showValues='0' formatNumberScale='0'>");
        sb.append("<categories>");
        List<Hourstatitem> ipList = hourStatItemService.getHourstatitemList(cp, "Analysis", "Viewed", "IP", _yymmdd.toString());
        List<Hourstatitem> pvList = hourStatItemService.getHourstatitemList(cp, "Analysis", "Viewed", "PV", _yymmdd.toString());
        Hourstatitem ip = null;
        Hourstatitem pv = null;
        if (ipList != null && ipList.size() > 0) {
            ip = ipList.get(0);
        }
        if (pvList != null && pvList.size() > 0) {
            pv = pvList.get(0);
        }
        StringBuffer pvSb = new StringBuffer();
        /**
         * begin,end等于0表示是查询某一天24小时的数据
         */
        if (begin == 0) {
            begin = 1;
        }
        if (end == 0) {
            end = 24;
        }
        for (int i = begin; i <= end; i++) {
            sb.append("<category label='").append(i).append("'/>");
            String value = "";
            String ipValue = "";
            String pvValue = "";
            if (ip == null) {
                ipValue = "0";
            } else {
                ipValue += MirrorUtil.getValue(Hourstatitem.class, ip, "count" + (i));
            }
            if (pv == null) {
                pvValue = "0";
            } else {
                pvValue += MirrorUtil.getValue(Hourstatitem.class, pv, "count" + (i));
            }
            value = ipValue + ";" + pvValue;
            pvSb.append("<set value='");
            pvSb.append(pvValue);
            pvSb.append("'/>");
            String day = i + "";
            if (day.length() == 1) {
                day = "0" + day;
            }
            result.put(day, value);
        }
        sb.append("</categories>");
        sb.append("<dataset seriesName='PV值'>");
        sb.append(pvSb);
        sb.append("</dataset>");
        sb.append("</chart>");
        result.put("xml", sb.toString());
    }

    public void webViewDay() {
        boolean init = false;

        StringBuffer sb = new StringBuffer();
        StringBuffer categories = new StringBuffer();
        sb.append("<chart caption='");
        sb.append("浏览量统计");
        sb.append("' xAxisName='日期' yAxisName='PV值' showValues='0' formatNumberScale='0'>");
        categories.append("<categories>");
        List<Daystatitem> ipList = dayStatItemService.getDaystatitemList(cp, "Analysis", "Viewed", "IP", _yymmdd.toString());
        List<Daystatitem> pvList = dayStatItemService.getDaystatitemList(cp, "Analysis", "Viewed", "PV", _yymmdd.toString());
        Daystatitem ip = null;
        Daystatitem pv = null;
        StringBuffer pvSb = new StringBuffer();
        if (ipList != null && ipList.size() > 0) {
            ip = ipList.get(0);
        }
        if (pvList != null && pvList.size() > 0) {
            pv = pvList.get(0);
        }

        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                String value = "";
                String ipValue = "";
                String pvValue = "";
                if (ip == null) {
                    ipValue = "0";
                } else {
                    ipValue += MirrorUtil.getValue(Daystatitem.class, ip, "count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                }
                if (pv == null) {
                    pvValue = "0";
                } else {
                    pvValue += MirrorUtil.getValue(Daystatitem.class, pv, "count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                }
                value = ipValue + ";" + pvValue;
                pvSb.append("<set value='");
                pvSb.append(pvValue);
                pvSb.append("'/>");

                result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), value);
            } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                if (!init) {
                    ipList = dayStatItemService.getDaystatitemList(cp, "Analysis", "Viewed", "IP", yymmdd.toString());
                    pvList = dayStatItemService.getDaystatitemList(cp, "Analysis", "Viewed", "PV", yymmdd.toString());
                    if (ipList != null && ipList.size() > 0) {
                        ip = ipList.get(0);
                    }
                    if (pvList != null && pvList.size() > 0) {
                        pv = pvList.get(0);
                    }
                    init = true;
                }
                categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                String value = "";
                String ipValue = "";
                String pvValue = "";
                if (ip == null) {
                    ipValue = "0";
                } else {
                    ipValue += MirrorUtil.getValue(Daystatitem.class, ip, "count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                }
                if (pv == null) {
                    pvValue = "0";
                } else {
                    pvValue += MirrorUtil.getValue(Daystatitem.class, pv, "count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                }
                value = ipValue + ";" + pvValue;
                pvSb.append("<set value='");
                pvSb.append(pvValue);
                pvSb.append("'/>");

                result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), value);
            }
        }
        categories.append("</categories>");
        sb.append(categories);
        sb.append("<dataset seriesName='PV值'>");
        sb.append(pvSb);
        sb.append("</dataset>");
        sb.append("</chart>");
        result.put("xml", sb.toString());
    }

    public void webAccessHour() {
        int year = DateUtil.getSpecificTime(beginTime, DateUtil.YEAR);
        int montn = DateUtil.getSpecificTime(beginTime, DateUtil.MONTH) + 1;
        int _day = DateUtil.getSpecificTime(beginTime, DateUtil.DAY_OF_MONTH);
        int begin = DateUtil.getSpecificTime(beginTime, DateUtil.HOUR_OF_DAY);
        double count = 0;
        int end;
        try {
            if (endTime.split(" ")[1].startsWith("24")) {
                end = 24;
            } else {
                end = DateUtil.getSpecificTime(endTime, DateUtil.HOUR_OF_DAY);
            }
        } catch (Exception e) {
            end = DateUtil.getSpecificTime(beginTime, DateUtil.HOUR_OF_DAY);
        }
        StringBuffer _yymmdd = new StringBuffer();
        _yymmdd.append(year);
        if (String.valueOf(montn).length() == 1) {
            _yymmdd.append("0").append(montn);
        } else {
            _yymmdd.append(montn);
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
        List<Hourstatitem> list = hourStatItemService.getHourstatitemList(cp, "Analysis", "Viewed", "IP", _yymmdd.toString());
        Hourstatitem hourstatitem = null;
        if (list != null && list.size() > 0) {
            hourstatitem = list.get(0);
        } else {
            return;
        }
        StringBuffer dataset = new StringBuffer();
        StringBuffer chart = new StringBuffer();
        StringBuffer categories = new StringBuffer();
        chart.append("<chart rotateYAxisName='0' caption='访问人数统计图' xAxisName='时间' yAxisName='人数(IP数)' showValues='0' formatNumberScale='0'>");
        categories.append("<categories>");
        dataset.append("<dataset seriesName='独立IP数'>");
        for (int i = begin; i <= end; i++) {
            categories.append("<category label='").append(i).append("'/>");
            String value = MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString();
            dataset.append("<set value='");
            dataset.append(value);
            dataset.append("'/>");
        }
        String hql = "from Hourstatitem  where cp = ? and period = ? and type = ? and subtype = ? order by total";
        list = hourStatItemService.getHourstatitemListByHql(hql, 0, 10, cp, _yymmdd.toString(), "WebAccess", "IP");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                hourstatitem = list.get(i);
                count += Double.parseDouble(String.valueOf(hourstatitem.getTotal()));
                result.put(hourstatitem.getItem(), hourstatitem.getTotal());
            }
        }
        categories.append("</categories>");
        dataset.append("</dataset>");
        chart.append(categories);
        chart.append(dataset);
        chart.append("</chart>");
        result.put("count", String.valueOf(count));
        result.put("xml", chart.toString());
    }

    @SuppressWarnings({"NullArgumentToVariableArgMethod"})
    public void webAccessDay() {
        double count = 0;
        boolean init = false;

        List<Daystatitem> list = dayStatItemService.getDaystatitemList(cp, "Analysis", "Viewed", "IP", _yymmdd.toString());
        Daystatitem daystatitem = null;
        if (list != null && list.size() > 0) {
            daystatitem = list.get(0);
        }
        StringBuffer total = new StringBuffer();
        StringBuffer nextTotal = new StringBuffer();
        StringBuffer dataset = new StringBuffer();
        StringBuffer chart = new StringBuffer();
        StringBuffer categories = new StringBuffer();
        chart.append("<chart rotateYAxisName='0' caption='访问人数统计图' xAxisName='日期' yAxisName='人数(IP数)' showValues='0' formatNumberScale='0'>");
        categories.append("<categories>");
        dataset.append("<dataset seriesName='独立IP数'>");
        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            categories.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
            if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                String value = "";
                if (daystatitem == null) {
                    value = "0";
                } else {
                    value = MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }

                dataset.append("<set value='");
                dataset.append(value);
                dataset.append("'/>");
                if (total.length() != 0) {
                    total.append("+count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                } else {
                    total.append("count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                }

            } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                if (!init) {
                    list = dayStatItemService.getDaystatitemList(cp, "Analysis", "Viewed", "IP", yymmdd.toString());
                    if (list.size() > 0) {
                        daystatitem = list.get(0);
                    }
                    init = true;
                }
                String value = "";
                if (daystatitem == null) {
                    value = "0";
                } else {
                    value = MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }

                dataset.append("<set value='");
                dataset.append(value);
                dataset.append("'/>");
                if (total.length() != 0) {
                    total.append("+count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                } else {
                    total.append("count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                }
            }

        }
        StringBuffer sql = new StringBuffer("Select (");
        sql.append(total);
        if (total.toString().indexOf("+") != -1) {
            total.setLength(0);
            total.append("total");
        }
        sql.append(") as ").append(total).append(" , item From ipdaystatitem");
        sql.append(" Where 1=1");
        sql.append(" And cp = '").append(cp).append("'");
        sql.append(" And period = '").append(_yymmdd).append("'");
        sql.append(" And type = 'WebAccess' And subtype = 'IP'");
        List ips = null;
        try {
            ips = ipDayStatItemService.getIpdaystatitemListBySql(sql.toString(), 0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List nextIps = null;
        if (ips != null && ips.size() > 0) {
            for (int i = 0; i < ips.size(); i++) {
                Object[] object = (Object[]) ips.get(i);
                int num = Integer.parseInt(object[0].toString());
                count += Double.parseDouble(object[0].toString());
                if (nextTotal.length() != 0) {
                    sql.setLength(0);
                    sql.append("Select (");
                    sql.append(nextTotal);
                    if (nextTotal.toString().indexOf("+") != -1) {
                        total.setLength(0);
                        total.append("total");
                    }
                    sql.append(") as ").append(nextTotal).append(" , item From ipdaystatitem");
                    sql.append(" Where 1=1");
                    sql.append(" And cp = '").append(cp).append("'");
                    sql.append(" And period = '").append(yymmdd).append("'");
                    sql.append(" And type = 'WebAccess' And subtype = 'IP'");
                    nextIps = ipDayStatItemService.getIpdaystatitemListBySql(sql.toString(), 0, 10, null);
                    if (nextIps != null && nextIps.size() > 0) {
                        for (int j = 0; j < nextIps.size(); j++) {
                            Object[] next = (Object[]) nextIps.get(j);
                            if (next[1].toString().equals(object[1].toString())) {
                                count += Double.parseDouble(next[0].toString());
                                num += Integer.parseInt(next[0].toString());
                                break;
                            }
                        }
                    }
                }
                result.put(object[1].toString(), String.valueOf(num));
            }
        }

        categories.append("</categories>");
        dataset.append("</dataset>");
        chart.append(categories);
        chart.append(dataset);
        chart.append("</chart>");
        result.put("count", String.valueOf(count));
        result.put("xml", chart.toString());
    }

    public void webAreaHour() {

        double total = 0;

        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix=''legendCaption='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='1'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
           sb.append("<colorRange> <color minValue='0' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        Map<String, String> maps = Common.getChartMap();
        List<Hourstatitem> list = hourStatItemService.getHourstatitemList(cp, "Analysis", "WebArea", null, _yymmdd.toString());
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            boolean find = false;
            if (list != null && list.size() > 0) {
                for (Hourstatitem hourstatitem : list) {
                    if (hourstatitem.getItem().startsWith(value)) {
                        int count = 0;
                        for (int i = begin; i <= end; i++) {
                            try {
                                count += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                            } catch (Exception e) {
                                count += 0;
                                e.printStackTrace();
                            }
                        }
                        sb.append("<entity id='").append(entry.getKey()).append("'");
                        sb.append(" displayValue='").append(value).append("'");
                        sb.append(" toolText='").append(value).append("  访客数:").append(count);
                        sb.append("' Value='").append(count).append("'");
                        sb.append("/>");
                        result.put(value, String.valueOf(count));
                        total += count;
                        find = true;
                        break;
                    }

                }
                if (!find) {
                    sb.append("<entity id='");
                    sb.append(entry.getKey());
                    sb.append("' toolText='");
                    sb.append(value).append("  访客数:0 '");
                    sb.append(" displayValue='").append(value).append("'");
                    sb.append(" Value='0' />");
                    result.put(value, String.valueOf(0));
                }
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  访客数:0 '");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" Value='0' />");
                result.put(value, String.valueOf(0));
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("xml", sb.toString());
        result.put("total", total);
    }

    public void webAreaDay() {
        int total = 0;
        boolean init = false;

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
        List<Daystatitem> list = dayStatItemService.getDaystatitemList(cp, "Analysis", "WebArea", area, _yymmdd.toString());
        List<Daystatitem> newList = null;
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            int count = 0;
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
                                count += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                        "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                count += 0;
                            }
                        }
                    }
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        newList = dayStatItemService.getDaystatitemList(cp, "Analysis", "WebArea", area, yymmdd.toString());
                        init = true;
                    }
                    for (Daystatitem daystatitem : list) {
                        if (daystatitem.getItem().startsWith(value)) {
                            try {
                                count += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem,
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
            sb.append("<entity id='").append(entry.getKey()).append("'");
            sb.append(" displayValue='").append(value).append("'");
            sb.append(" toolText='").append(value).append("  访客数:").append(count);
            sb.append("' Value='").append(count).append("'");
            sb.append("/>");
            total += count;
            if (count != 0) {
                result.put(value, String.valueOf(count));
            } else {
                result.put(value, String.valueOf(0));
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("total", total);
        result.put("xml", sb.toString());
    }

    public void liveWatchHour() {
        List<Hourstatitem> highList = hourStatItemService.getHourstatitemList(cp, "LiveWatch", "Highest", channel, _yymmdd.toString());
        List<Hourstatitem> lowList = hourStatItemService.getHourstatitemList(cp, "LiveWatch", "Lowest", channel, _yymmdd.toString());
        Hourstatitem high = null;
        Hourstatitem low = null;
        if (highList != null && highList.size() > 0) {
            high = highList.get(0);
        }
        if (lowList != null && lowList.size() > 0) {
            low = lowList.get(0);
        }
        StringBuffer chart = new StringBuffer("<chart palette='4' caption='").append(channel).append("观看人数统计' yAxisName='人数(IP数)' xAxisName='时间' numdivlines='4'  lineThickness='2' showValues='0' formatNumberScale='0' decimals='1' anchorRadius='2' yAxisMinValue='800000' shadowAlpha='50'>");
        chart.append("<categories>");
        StringBuffer sbOne = new StringBuffer();
        StringBuffer sbTwo = new StringBuffer();
        sbOne.append("<dataset seriesName='峰值人数'>");
        sbTwo.append("<dataset seriesName='最低人数'>");
        for (int i = begin; i <= end; i++) {
            chart.append("<category label='").append(i).append("'/>");
            String highValue = "";
            if (high == null) {
                highValue = "0";
            } else {
                highValue = MirrorUtil.getValue(Hourstatitem.class, high, "count" + (i)).toString();
            }

            String lowValue = "";
            if (low == null) {
                lowValue = "0";
            } else {
                lowValue = MirrorUtil.getValue(Hourstatitem.class, low, "count" + (i)).toString();
            }
            sbOne.append("<set value='").append(highValue).append("'/>");
            sbTwo.append("<set value='").append(lowValue).append("'/>");
            String day = i + "";
            if (day.length() == 1) {
                day = "0" + day;
            }
            result.put(day, highValue + ";" + lowValue);
        }
        sbOne.append("</dataset>");
        sbTwo.append("</dataset>");
        chart.append("</categories>");
        chart.append(sbOne);
        chart.append(sbTwo);
        chart.append("</chart>");
        result.put("xml", chart.toString());
    }

    public void liveWatchDay() {
        boolean init = false;
        List<Daystatitem> highList = dayStatItemService.getDaystatitemList(cp, "LiveWatch", "Highest", channel, _yymmdd.toString());
        List<Daystatitem> lowList = dayStatItemService.getDaystatitemList(cp, "LiveWatch", "Lowest", channel, _yymmdd.toString());
        Daystatitem high = null;
        Daystatitem low = null;
        if (highList != null && highList.size() > 0) {
            high = highList.get(0);
        }
        if (lowList != null && lowList.size() > 0) {
            low = lowList.get(0);
        }
        StringBuffer chart = new StringBuffer("<chart palette='4' caption='").append(channel).append("观看人数统计' yAxisName='人数(IP数)' xAxisName='日期' numdivlines='4'  lineThickness='2' showValues='0' formatNumberScale='0' decimals='1' anchorRadius='2' yAxisMinValue='800000' shadowAlpha='50'>");
        chart.append("<categories>");
        StringBuffer sbOne = new StringBuffer();
        StringBuffer sbTwo = new StringBuffer();
        sbOne.append("<dataset seriesName='峰值人数'>");
        sbTwo.append("<dataset seriesName='最低人数'>");
        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                chart.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                String highValue = "";
                if (high == null) {
                    highValue = "0";
                } else {
                    highValue = MirrorUtil.getValue(Daystatitem.class, high, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }

                String lowValue = "";
                if (low == null) {
                    lowValue = "0";
                } else {
                    lowValue = MirrorUtil.getValue(Daystatitem.class, low, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }

                sbOne.append("<set value='").append(highValue).append("'/>");
                sbTwo.append("<set value='").append(lowValue).append("'/>");
                result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), highValue + ";" + lowValue);
            } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                if (!init) {
                    highList = dayStatItemService.getDaystatitemList(cp, "LiveWatch", "Highest", channel, yymmdd.toString());
                    lowList = dayStatItemService.getDaystatitemList(cp, "LiveWatch", "Lowest", channel, yymmdd.toString());
                    if (highList != null && highList.size() > 0) {
                        high = highList.get(0);
                    }
                    if (lowList != null && lowList.size() > 0) {
                        low = lowList.get(0);
                    }
                }
                chart.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                String highValue = "";
                if (high == null) {
                    highValue = "0";
                } else {
                    highValue = MirrorUtil.getValue(Daystatitem.class, high, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }

                String lowValue = "";
                if (low == null) {
                    lowValue = "0";
                } else {
                    lowValue = MirrorUtil.getValue(Daystatitem.class, low, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }

                sbOne.append("<set value='").append(highValue).append("'/>");
                sbTwo.append("<set value='").append(lowValue).append("'/>");
                result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), highValue + ";" + lowValue);
            }
        }
        sbOne.append("</dataset>");
        sbTwo.append("</dataset>");
        chart.append("</categories>");
        chart.append(sbOne);
        chart.append(sbTwo);
        chart.append("</chart>");
        result.put("xml", chart.toString());
    }

    public void liveStayHour() {
        try {
            List<Hourstatitem> five_min = hourStatItemService.getHourstatitemList(cp, "Live", "5Min", null, _yymmdd.toString());
            List<Hourstatitem> ten_min = hourStatItemService.getHourstatitemList(cp, "Live", "10Min", null, _yymmdd.toString());
            List<Hourstatitem> thirty_min = hourStatItemService.getHourstatitemList(cp, "Live", "30Min", null, _yymmdd.toString());
            List<Hourstatitem> sixty_min = hourStatItemService.getHourstatitemList(cp, "Live", "60Min", null, _yymmdd.toString());
            List<Hourstatitem> n_min = hourStatItemService.getHourstatitemList(cp, "Live", "NMin", null, _yymmdd.toString());
            int five = 0;
            int ten = 0;
            int thirty = 0;
            int sixty = 0;
            int n = 0;

            StringBuffer charts = new StringBuffer();
            charts.append("<chart caption='").append(channel).append("停留时间统计图' palette='2' animation='1' YAxisName='Sales Achieved' showValues='0'  formatNumberScale='0' showPercentInToolTip='0' showLabels='0' showLegend='1'>");
            for (int i = begin; i <= end; i++) {

                if (five_min.size() > 0) {
                    five += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, five_min.get(0), "count" + (i)).toString());
                }

                if (ten_min.size() > 0) {
                    ten += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, ten_min.get(0), "count" + (i)).toString());
                }

                if (thirty_min.size() > 0) {
                    thirty += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, thirty_min.get(0), "count" + (i)).toString());
                }

                if (sixty_min.size() > 0) {
                    sixty += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, sixty_min.get(0), "count" + (i)).toString());
                }

                if (n_min.size() > 0) {
                    n += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, n_min.get(0), "count" + (i)).toString());
                }
            }
            charts.append("<set label='0-5 min' value='").append(five).append("' isSliced='0'/>");
            charts.append("<set label='5-10 min' value='").append(ten).append("' isSliced='0'/>");
            charts.append("<set label='10-30 min' value='").append(thirty).append("' isSliced='0'/>");
            charts.append("<set label='30-60 min' value='").append(sixty).append("' isSliced='0'/>");
            charts.append("<set label='60以上 min' value='").append(n).append("' isSliced='0'/>");
            charts.append("</chart>");
            result.put("xml", charts.toString());
            result.put("ten", String.valueOf(ten));
            result.put("five", String.valueOf(five));
            result.put("thirty", String.valueOf(thirty));
            result.put("sixty", String.valueOf(sixty));
            result.put("n", String.valueOf(n));
            result.put("count", String.valueOf(five + ten + thirty + sixty + n));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liveStayChannelHour() {
        try {
            List<Hourstatitem> five_min = hourStatItemService.getHourstatitemList(cp, "Live", "5Min", null, _yymmdd.toString());
            List<Hourstatitem> ten_min = hourStatItemService.getHourstatitemList(cp, "Live", "10Min", null, _yymmdd.toString());
            List<Hourstatitem> thirty_min = hourStatItemService.getHourstatitemList(cp, "Live", "30Min", null, _yymmdd.toString());
            List<Hourstatitem> sixty_min = hourStatItemService.getHourstatitemList(cp, "Live", "60Min", null, _yymmdd.toString());
            List<Hourstatitem> n_min = hourStatItemService.getHourstatitemList(cp, "Live", "NMin", null, _yymmdd.toString());

            int five = 0;
            int ten = 0;
            int thirty = 0;
            int sixty = 0;
            int n = 0;

            StringBuffer charts = new StringBuffer();
            charts.append("<chart caption='所有频道停留时间统计图' palette='2' animation='1' YAxisName='Sales Achieved' showValues='0'  formatNumberScale='0' showPercentInToolTip='0' showLabels='0' showLegend='1'>");
            for (int i = begin; i <= end; i++) {
                for (Hourstatitem hourstatitem : five_min) {
                    five += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                }
                for (Hourstatitem hourstatitem : ten_min) {
                    ten += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                }
                for (Hourstatitem hourstatitem : thirty_min) {
                    thirty += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                }

                for (Hourstatitem hourstatitem : sixty_min) {
                    sixty += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                }
                for (Hourstatitem hourstatitem : n_min) {
                    n += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                }

            }
            charts.append("<set label='0-5 min' value='").append(five).append("' isSliced='0'/>");
            charts.append("<set label='5-10 min' value='").append(ten).append("' isSliced='0'/>");
            charts.append("<set label='10-30 min' value='").append(thirty).append("' isSliced='0'/>");
            charts.append("<set label='30-60 min' value='").append(sixty).append("' isSliced='0'/>");
            charts.append("<set label='60以上 min' value='").append(n).append("' isSliced='0'/>");
            charts.append("</chart>");
            result.put("xml", charts.toString());
            result.put("ten", String.valueOf(ten));
            result.put("five", String.valueOf(five));
            result.put("thirty", String.valueOf(thirty));
            result.put("sixty", String.valueOf(sixty));
            result.put("n", String.valueOf(n));
            result.put("count", String.valueOf(five + ten + thirty + sixty + n));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liveStayChannelDay() {
        boolean init = false;
        try {
            List<Daystatitem> five_min = dayStatItemService.getDaystatitemList(cp, "Live", "5Min", null, _yymmdd.toString());
            List<Daystatitem> ten_min = dayStatItemService.getDaystatitemList(cp, "Live", "10Min", null, _yymmdd.toString());
            List<Daystatitem> thirty_min = dayStatItemService.getDaystatitemList(cp, "Live", "30Min", null, _yymmdd.toString());
            List<Daystatitem> sixty_min = dayStatItemService.getDaystatitemList(cp, "Live", "60Min", null, _yymmdd.toString());
            List<Daystatitem> n_min = dayStatItemService.getDaystatitemList(cp, "Live", "NMin", null, _yymmdd.toString());

            int five = 0;
            int ten = 0;
            int thirty = 0;
            int sixty = 0;
            int n = 0;

            StringBuffer charts = new StringBuffer();
            charts.append("<chart caption='所有频道停留时间统计图' palette='2' animation='1' YAxisName='Sales Achieved' showValues='0'  formatNumberScale='0' showPercentInToolTip='0' showLabels='0' showLegend='1'>");

            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, month - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            while (calendar.before(compareCalendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                int i = calendar.get(Calendar.DAY_OF_MONTH);
                if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {

                    for (Daystatitem daystatitem : five_min) {
                        five += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : ten_min) {
                        ten += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : thirty_min) {
                        thirty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }

                    for (Daystatitem daystatitem : sixty_min) {
                        sixty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : n_min) {
                        n += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        five_min = dayStatItemService.getDaystatitemList(cp, "Live", "5Min", channel, yymmdd.toString());
                        ten_min = dayStatItemService.getDaystatitemList(cp, "Live", "10Min", channel, yymmdd.toString());
                        thirty_min = dayStatItemService.getDaystatitemList(cp, "Live", "30Min", channel, yymmdd.toString());
                        sixty_min = dayStatItemService.getDaystatitemList(cp, "Live", "60Min", channel, yymmdd.toString());
                        n_min = dayStatItemService.getDaystatitemList(cp, "Live", "NMin", channel, yymmdd.toString());
                        init = true;
                    }
                    for (Daystatitem daystatitem : five_min) {
                        five += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : ten_min) {
                        ten += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : thirty_min) {
                        thirty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }

                    for (Daystatitem daystatitem : sixty_min) {
                        sixty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : n_min) {
                        n += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                }
            }
            charts.append("<set label='0-5 min' value='").append(five).append("' isSliced='0'/>");
            charts.append("<set label='5-10 min' value='").append(ten).append("' isSliced='0'/>");
            charts.append("<set label='10-30 min' value='").append(thirty).append("' isSliced='0'/>");
            charts.append("<set label='30-60 min' value='").append(sixty).append("' isSliced='0'/>");
            charts.append("<set label='60以上 min' value='").append(n).append("' isSliced='0'/>");
            charts.append("</chart>");
            result.put("xml", charts.toString());
            result.put("ten", String.valueOf(ten));
            result.put("five", String.valueOf(five));
            result.put("thirty", String.valueOf(thirty));
            result.put("sixty", String.valueOf(sixty));
            result.put("n", String.valueOf(n));
            result.put("count", String.valueOf(five + ten + thirty + sixty + n));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liveStayDay() {
        boolean init = false;
        try {
            List<Daystatitem> five_min = dayStatItemService.getDaystatitemList(cp, "Live", "5Min", channel, _yymmdd.toString());
            List<Daystatitem> ten_min = dayStatItemService.getDaystatitemList(cp, "Live", "10Min", channel, _yymmdd.toString());
            List<Daystatitem> thirty_min = dayStatItemService.getDaystatitemList(cp, "Live", "30Min", channel, _yymmdd.toString());
            List<Daystatitem> sixty_min = dayStatItemService.getDaystatitemList(cp, "Live", "60Min", channel, _yymmdd.toString());
            List<Daystatitem> n_min = dayStatItemService.getDaystatitemList(cp, "Live", "NMin", channel, _yymmdd.toString());

            int five = 0;
            int ten = 0;
            int thirty = 0;
            int sixty = 0;
            int n = 0;

            StringBuffer charts = new StringBuffer();
            charts.append("<chart caption='").append(channel).append("停留时间统计图' palette='2' animation='1' YAxisName='Sales Achieved' showValues='0'  formatNumberScale='0' showPercentInToolTip='0' showLabels='0' showLegend='1'>");

            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, month - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            while (calendar.before(compareCalendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                int i = calendar.get(Calendar.DAY_OF_MONTH);

                if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {

                    for (Daystatitem daystatitem : five_min) {
                        five += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : ten_min) {
                        ten += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : thirty_min) {
                        thirty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }

                    for (Daystatitem daystatitem : sixty_min) {
                        sixty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : n_min) {
                        n += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                } else {
                    if (!init) {
                        five_min = dayStatItemService.getDaystatitemList(cp, "Live", "5Min", channel, yymmdd.toString());
                        ten_min = dayStatItemService.getDaystatitemList(cp, "Live", "10Min", channel, yymmdd.toString());
                        thirty_min = dayStatItemService.getDaystatitemList(cp, "Live", "30Min", channel, yymmdd.toString());
                        sixty_min = dayStatItemService.getDaystatitemList(cp, "Live", "60Min", channel, yymmdd.toString());
                        n_min = dayStatItemService.getDaystatitemList(cp, "Live", "NMin", channel, yymmdd.toString());
                        init = true;
                    }
                    for (Daystatitem daystatitem : five_min) {
                        five += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : ten_min) {
                        ten += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : thirty_min) {
                        thirty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }

                    for (Daystatitem daystatitem : sixty_min) {
                        sixty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                    for (Daystatitem daystatitem : n_min) {
                        n += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                    }
                }
            }
            charts.append("<set label='0-5 min' value='").append(five).append("' isSliced='0'/>");
            charts.append("<set label='5-10 min' value='").append(ten).append("' isSliced='0'/>");
            charts.append("<set label='10-30 min' value='").append(thirty).append("' isSliced='0'/>");
            charts.append("<set label='30-60 min' value='").append(sixty).append("' isSliced='0'/>");
            charts.append("<set label='60以上 min' value='").append(n).append("' isSliced='0'/>");
            charts.append("</chart>");
            result.put("xml", charts.toString());
            result.put("ten", String.valueOf(ten));
            result.put("five", String.valueOf(five));
            result.put("thirty", String.valueOf(thirty));
            result.put("sixty", String.valueOf(sixty));
            result.put("n", String.valueOf(n));
            result.put("count", String.valueOf(five + ten + thirty + sixty + n));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void liveAreaHour() {
        double total = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix=''legendCaption='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='0'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
           sb.append("<colorRange> <color minValue='0' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        Map<String, String> maps = Common.getChartMap();
        if (area != null) {
            if (area.equals("0")) {
                area = null;
            }
        }
        List<Hourstatitem> list = hourStatItemService.getHourstatitemList(cp, "LiveArea", channel, area, _yymmdd.toString());
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            boolean find = false;
            if (list != null && list.size() > 0) {
                for (Hourstatitem hourstatitem : list) {
                    if (hourstatitem.getItem().startsWith(value)) {
                        int count = 0;
                        for (int i = begin; i <= end; i++) {
                            try {
                                count += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                            } catch (Exception e) {
                                count += 0;
                                e.printStackTrace();
                            }
                        }
                        sb.append("<entity id='").append(entry.getKey()).append("'");
                        sb.append(" toolText='").append(value).append("  流量:").append(count);
                        sb.append("' Value='").append(count).append("'");
                        sb.append("/>");
                        result.put(value, String.valueOf(count));
                        total += count;
                        find = true;
                        break;
                    }

                }
                if (!find) {
                    sb.append("<entity id='");
                    sb.append(entry.getKey());
                    sb.append("' toolText='");
                    sb.append(value).append("  流量:0");
                    sb.append("' Value='0' />");
                }
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  流量:0");
                sb.append("' Value='0' />");
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("xml", sb.toString());
        result.put("total", total);
    }

    public void liveAreaChannelHour() {
        double total = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix=''legendCaption='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='0'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
        sb.append("<colorRange> <color minValue='0' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        Map<String, String> maps = Common.getChartMap();
        if (area != null) {
            if (area.equals("0")) {
                area = null;
            }
        }
        List<Hourstatitem> list = hourStatItemService.getHourstatitemList(cp, "LiveArea", null, area, _yymmdd.toString());
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            boolean find = false;
            if (list != null && list.size() > 0) {
                int count = 0;
                for (Hourstatitem hourstatitem : list) {
                    if (hourstatitem.getItem().startsWith(value)) {
                        for (int i = begin; i <= end; i++) {
                            try {
                                count += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                                find = true;
                            } catch (Exception e) {
                                count += 0;
                                e.printStackTrace();
                            }
                        }
                    }
                }
                sb.append("<entity id='").append(entry.getKey()).append("'");
                sb.append(" toolText='").append(value).append("  流量:").append(count);
                sb.append("' Value='").append(count).append("'");
                sb.append("/>");
                if (find) {
                    result.put(value, String.valueOf(count));
                }
                total += count;
                if (!find) {
                    sb.append("<entity id='");
                    sb.append(entry.getKey());
                    sb.append("' toolText='");
                    sb.append(value).append("  流量:0");
                    sb.append("' Value='0' />");
                }
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  流量:0");
                sb.append("' Value='0' />");
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("xml", sb.toString());
        result.put("total", total);
    }

    public void liveAreaDay() {
        boolean init = false;
        int total = 0;
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
        List<Daystatitem> list = dayStatItemService.getDaystatitemList(cp, "LiveArea", channel, area, _yymmdd.toString());
        List<Daystatitem> newList = null;
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            int count = 0;
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
                                count += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                        "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                count += 0;
                            }
                        }
                    }
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        newList = dayStatItemService.getDaystatitemList(cp, "LiveArea", channel, area, yymmdd.toString());
                        init = true;
                    }
                    for (Daystatitem daystatitem : newList) {
                        if (daystatitem.getItem().startsWith(value)) {
                            try {
                                count += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem,
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

            if (count != 0) {
                sb.append("<entity id='").append(entry.getKey()).append("'");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" toolText='").append(value).append("  访客数:").append(count);
                sb.append("' Value='").append(count).append("'");
                sb.append("/>");
                total += count;
                result.put(value, String.valueOf(count));
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  访客数:0 '");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" Value='0' />");
                  result.put(value, String.valueOf(0));
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("total", total);
        result.put("xml", sb.toString());
    }

    public void liveAreaChannelDay() {
        boolean init = false;
        int total = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix=''legendCaption='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='1'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
         sb.append("<colorRange> <color minValue='0' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        Map<String, String> maps = Common.getChartMap();
        List<Daystatitem> list = null;
        List<Daystatitem> newList = null;
        if (area != null) {
            if (area.equals("0")) {
                area = null;
            }
        }
        list = dayStatItemService.getDaystatitemList(cp, "LiveArea", null, area, _yymmdd.toString());
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            int count = 0;
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
                                count += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                        "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                                count += 0;
                            }
                        }
                    }
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        newList = dayStatItemService.getDaystatitemList(cp, "LiveArea", null, area, yymmdd.toString());
                        init = true;
                    }
                    for (Daystatitem daystatitem : newList) {
                        if (daystatitem.getItem().startsWith(value)) {
                            try {
                                count += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                        "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());

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
                sb.append(" toolText='").append(value).append("  访客数:").append(count);
                sb.append("' Value='").append(count).append("'");
                sb.append("/>");
                total += count;
                result.put(value, String.valueOf(count));
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  访客数:0 '");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" Value='0' />");
                  result.put(value, String.valueOf(0));
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("total", total);
        result.put("xml", sb.toString());
    }

    public void vodPlayHour() {
        List<Hourstatitem> highList = hourStatItemService.getHourstatitemList(cp, "Analysis", "VodPlay", "Highest", _yymmdd.toString());
        List<Hourstatitem> lowList = hourStatItemService.getHourstatitemList(cp, "Analysis", "VodPlay", "Lowest", _yymmdd.toString());
        Hourstatitem high = null;
        Hourstatitem low = null;
        if (highList != null && highList.size() > 0) {
            high = highList.get(0);
        }
        if (lowList != null && lowList.size() > 0) {
            low = lowList.get(0);
        }
        StringBuffer chart = new StringBuffer("<chart palette='4' caption='总播放量统计' yAxisName='点播数' xAxisName='时间' numdivlines='4'  lineThickness='2' showValues='0' formatNumberScale='0' decimals='1' anchorRadius='2' yAxisMinValue='800000' shadowAlpha='50'>");
        chart.append("<categories>");
        StringBuffer sbOne = new StringBuffer();
        StringBuffer sbTwo = new StringBuffer();
        sbOne.append("<dataset seriesName='峰值点播数'>");
        sbTwo.append("<dataset seriesName='最低点播数'>");
        for (int i = begin; i <= end; i++) {
            chart.append("<category label='").append(i).append("'/>");
            String highValue = "";
            if (high == null) {
                highValue = "0";
            } else {
                highValue = MirrorUtil.getValue(Hourstatitem.class, high, "count" + (i)).toString();
            }
            String lowValue = "";
            if (low == null) {
                lowValue = "0";
            } else {
                lowValue = MirrorUtil.getValue(Hourstatitem.class, low, "count" + (i)).toString();
            }
            sbOne.append("<set value='").append(highValue).append("'/>");
            sbTwo.append("<set value='").append(lowValue).append("'/>");
            String day = i + "";
            if (day.length() == 1) {
                day = "0" + day;
            }
            result.put(day, highValue + ";" + lowValue);
        }
        sbOne.append("</dataset>");
        sbTwo.append("</dataset>");
        chart.append("</categories>");
        chart.append(sbOne);
        chart.append(sbTwo);
        chart.append("</chart>");
        result.put("xml", chart.toString());
    }

    public void vodPlayDay() {
        boolean init = false;
        List<Daystatitem> highList = dayStatItemService.getDaystatitemList(cp, "Analysis", "VodPlay", "Highest", _yymmdd.toString());
        List<Daystatitem> lowList = dayStatItemService.getDaystatitemList(cp, "Analysis", "VodPlay", "Lowest", _yymmdd.toString());
        Daystatitem high = null;
        Daystatitem low = null;
        if (highList != null && highList.size() > 0) {
            high = highList.get(0);
        }
        if (lowList != null && lowList.size() > 0) {
            low = lowList.get(0);
        }
        StringBuffer chart = new StringBuffer("<chart palette='4' caption='总播放量统计' yAxisName='点播数' xAxisName='日期' numdivlines='4'  lineThickness='2' showValues='0' formatNumberScale='0' decimals='1' anchorRadius='2' yAxisMinValue='800000' shadowAlpha='50'>");
        chart.append("<categories>");
        StringBuffer sbOne = new StringBuffer();
        StringBuffer sbTwo = new StringBuffer();
        sbOne.append("<dataset seriesName='峰值点播数'>");
        sbTwo.append("<dataset seriesName='最低点播数'>");
        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                chart.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                String highValue = "";
                if (high == null) {
                    highValue = "0";
                } else {
                    highValue = MirrorUtil.getValue(Daystatitem.class, high, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }
                String lowValue = "";
                if (low == null) {
                    lowValue = "0";
                } else {
                    lowValue = MirrorUtil.getValue(Daystatitem.class, low, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }

                sbOne.append("<set value='").append(highValue).append("'/>");
                sbTwo.append("<set value='").append(lowValue).append("'/>");
                result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), highValue + ";" + lowValue);
            } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                if (!init) {
                    highList = dayStatItemService.getDaystatitemList(cp, "Analysis", "VodPlay", "Highest", yymmdd.toString());
                    lowList = dayStatItemService.getDaystatitemList(cp, "Analysis", "VodPlay", "Lowest", yymmdd.toString());
                    if (highList != null && highList.size() > 0) {
                        high = highList.get(0);
                    }
                    if (lowList != null && lowList.size() > 0) {
                        low = lowList.get(0);
                    }
                }
                chart.append("<category label='").append(calendar.get(Calendar.DAY_OF_MONTH)).append("'/>");
                String highValue = "";
                if (high == null) {
                    highValue = "0";
                } else {
                    highValue = MirrorUtil.getValue(Daystatitem.class, high, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }
                String lowValue = "";
                if (low == null) {
                    lowValue = "0";
                } else {
                    lowValue = MirrorUtil.getValue(Daystatitem.class, low, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString();
                }
                sbOne.append("<set value='").append(highValue).append("'/>");
                sbTwo.append("<set value='").append(lowValue).append("'/>");
                result.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D), highValue + ";" + lowValue);
            }
        }
        sbOne.append("</dataset>");
        sbTwo.append("</dataset>");
        chart.append("</categories>");
        chart.append(sbOne);
        chart.append(sbTwo);
        chart.append("</chart>");
        result.put("xml", chart.toString());
    }

    public void vodStayHour() {

        try {
            List<Hourstatitem> temp = hourStatItemService.getHourstatitemList(cp, "Analysis", "Vod", "5Min", _yymmdd.toString());
            Hourstatitem five_min = null;
            if (temp.size() > 0) {
                five_min = temp.get(0);
            }
            temp = hourStatItemService.getHourstatitemList(cp, "Analysis", "Vod", "10Min", _yymmdd.toString());
            Hourstatitem ten_min = null;
            if (temp.size() > 0) {
                ten_min = temp.get(0);
            }
            Hourstatitem thirty_min = null;
            temp = hourStatItemService.getHourstatitemList(cp, "Analysis", "Vod", "30Min", _yymmdd.toString());
            if (temp.size() > 0) {
                thirty_min = temp.get(0);
            }
            Hourstatitem sixty_min = null;
            temp = hourStatItemService.getHourstatitemList(cp, "Analysis", "Vod", "60Min", _yymmdd.toString());
            if (temp.size() > 0) {
                sixty_min = temp.get(0);
            }
            Hourstatitem n_min = null;
            temp = hourStatItemService.getHourstatitemList(cp, "Analysis", "Vod", "NMin", _yymmdd.toString());
            if (temp.size() > 0) {
                n_min = temp.get(0);
            }

            int five = 0;
            int ten = 0;
            int thirty = 0;
            int sixty = 0;
            int n = 0;

            StringBuffer charts = new StringBuffer();
            charts.append("<chart caption='停留时间统计图' palette='2' animation='1' YAxisName='Sales Achieved' showValues='0'  formatNumberScale='0' showPercentInToolTip='0' showLabels='0' showLegend='1'>");
            for (int i = begin; i <= end; i++) {

                if (five_min != null) {
                    five += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, five_min, "count" + (i)).toString());
                }

                if (ten_min != null) {
                    ten += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, ten_min, "count" + (i)).toString());
                }


                if (thirty_min != null) {
                    thirty += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, thirty_min, "count" + (i)).toString());
                }

                if (sixty_min != null) {
                    sixty += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, sixty_min, "count" + (i)).toString());
                }

                if (n_min != null) {
                    n += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, n_min, "count" + (i)).toString());
                }
            }
            charts.append("<set label='0-5 min' value='").append(five).append("' isSliced='0'/>");
            charts.append("<set label='5-10 min' value='").append(ten).append("' isSliced='0'/>");
            charts.append("<set label='10-30 min' value='").append(thirty).append("' isSliced='0'/>");
            charts.append("<set label='30-60 min' value='").append(sixty).append("' isSliced='0'/>");
            charts.append("<set label='60以上 min' value='").append(n).append("' isSliced='0'/>");
            charts.append("</chart>");
            result.put("xml", charts.toString());
            result.put("ten", String.valueOf(ten));
            result.put("five", String.valueOf(five));
            result.put("thirty", String.valueOf(thirty));
            result.put("sixty", String.valueOf(sixty));
            result.put("n", String.valueOf(n));
            result.put("count", String.valueOf(five + ten + thirty + sixty + n));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void vodStayDay() {
        boolean init = false;

        try {
            List<Daystatitem> five_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "5Min", _yymmdd.toString());
            List<Daystatitem> ten_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "10Min", _yymmdd.toString());
            List<Daystatitem> thirty_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "30Min", _yymmdd.toString());
            List<Daystatitem> sixty_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "60Min", _yymmdd.toString());
            List<Daystatitem> n_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "NMin", _yymmdd.toString());

            int five = 0;
            int ten = 0;
            int thirty = 0;
            int sixty = 0;
            int n = 0;

            StringBuffer charts = new StringBuffer();
            charts.append("<chart caption='").append("停留时间统计图' palette='2' animation='1' YAxisName='Sales Achieved' showValues='0'  formatNumberScale='0' showPercentInToolTip='0' showLabels='0' showLegend='1'>");

            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, month - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            while (calendar.before(compareCalendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                int i = calendar.get(Calendar.DAY_OF_MONTH);
                if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                    if (five_min != null) {
                        for (Daystatitem daystatitem : five_min) {
                            five += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }
                    if (ten_min != null) {
                        for (Daystatitem daystatitem : ten_min) {
                            ten += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }
                    if (thirty_min != null) {

                        for (Daystatitem daystatitem : thirty_min) {
                            thirty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }

                    if (sixty_min != null) {
                        for (Daystatitem daystatitem : sixty_min) {
                            sixty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }

                    if (n_min != null) {
                        for (Daystatitem daystatitem : n_min) {
                            n += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }
                } else {
                    if (!init) {
                        five_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "5Min", _yymmdd.toString());
                        ten_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "10Min", _yymmdd.toString());
                        thirty_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "30Min", _yymmdd.toString());
                        sixty_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "60Min", _yymmdd.toString());
                        n_min = dayStatItemService.getDaystatitemList(cp, "Analysis", "Vod", "NMin", _yymmdd.toString());

                        init = true;
                    }
                    if (five_min != null) {
                        for (Daystatitem daystatitem : five_min) {
                            five += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }
                    if (ten_min != null) {
                        for (Daystatitem daystatitem : ten_min) {
                            ten += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }
                    if (thirty_min != null) {

                        for (Daystatitem daystatitem : thirty_min) {
                            thirty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }

                    if (sixty_min != null) {
                        for (Daystatitem daystatitem : sixty_min) {
                            sixty += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }

                    if (n_min != null) {
                        for (Daystatitem daystatitem : n_min) {
                            n += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + (i)).toString());
                        }
                    }
                }
            }
            charts.append("<set label='0-5 min' value='").append(five).append("' isSliced='0'/>");
            charts.append("<set label='5-10 min' value='").append(ten).append("' isSliced='0'/>");
            charts.append("<set label='10-30 min' value='").append(thirty).append("' isSliced='0'/>");
            charts.append("<set label='30-60 min' value='").append(sixty).append("' isSliced='0'/>");
            charts.append("<set label='60以上 min' value='").append(n).append("' isSliced='0'/>");
            charts.append("</chart>");
            result.put("xml", charts.toString());
            result.put("ten", String.valueOf(ten));
            result.put("five", String.valueOf(five));
            result.put("thirty", String.valueOf(thirty));
            result.put("sixty", String.valueOf(sixty));
            result.put("n", String.valueOf(n));
            result.put("count", String.valueOf(five + ten + thirty + sixty + n));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void vodAreaHour() {
        double total = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("<map  showFCMenuItem='0' numberPrefix=''legendCaption='' baseFontSize='12' animation='1' legendShadow='1'  canvasBorderThickness='0' canvasBorderAlpha='0' showBorder='0' showShadow='1' showBevel='0' showLabels='0'  fillAlpha='100' hoverColor='639ACE' bgColor='f3fbff' chartRightMargin='0' fillColor='ffffff' chartTopMargin='0' showlegend='1' chartLeftMargin='0' chartBottomMargin='0'>");
         sb.append("<colorRange> <color minValue='0' maxValue='20' color='ffff00' /> <color minValue='20' maxValue='40' color='ffcc00' /> <color minValue='40' maxValue='65' color='ff9900' /> <color minValue='65' maxValue='85' color='ff6600' /> <color minValue='85' maxValue='100' color='ff3300' /> <color minValue='100' maxValue='999999999' color='ff0000' /> </colorRange>");
        sb.append("<data>");
        Map<String, String> maps = Common.getChartMap();
        if (area != null) {
            if (area.equals("0")) {
                area = null;
            }
        }
        List<Hourstatitem> list = hourStatItemService.getHourstatitemList(cp, "VodArea", "sobey", area, _yymmdd.toString());
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            boolean find = false;
            if (list != null && list.size() > 0) {
                for (Hourstatitem hourstatitem : list) {
                    if (hourstatitem.getItem().startsWith(value)) {
                        int count = 0;
                        for (int i = begin; i <= end; i++) {
                            try {
                                count += Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString());
                            } catch (Exception e) {
                                count += 0;
                                e.printStackTrace();
                            }
                        }
                        sb.append("<entity id='").append(entry.getKey()).append("'");
                        sb.append(" toolText='").append(value).append("  流量:").append(count);
                        sb.append("' Value='").append(count).append("'");
                        sb.append("/>");
                        result.put(value, String.valueOf(count));
                        total += count;
                        find = true;
                        break;
                    }

                }
                if (!find) {
                    sb.append("<entity id='");
                    sb.append(entry.getKey());
                    sb.append("' toolText='");
                    sb.append(value).append("  流量:0");
                    sb.append("' Value='0' />");
                }
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  流量:0");
                sb.append("' Value='0' />");
            }
        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("xml", sb.toString());
        result.put("total", total);
    }

    public void vodAreaDay() {
        boolean init = false;
        int total = 0;
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
        List<Daystatitem> list = dayStatItemService.getDaystatitemList(cp, "Analysis", "VodArea", area, _yymmdd.toString());
        List<Daystatitem> newList = null;
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            String value = entry.getValue();
            int count = 0;
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
                                count += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem,
                                        "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                count += 0;
                            }
                        }
                    }
                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        newList = dayStatItemService.getDaystatitemList(cp, "Analysis", "VodArea", area, yymmdd.toString());
                        init = true;
                    }
                    for (Daystatitem daystatitem : newList) {
                        if (daystatitem.getItem().startsWith(value)) {
                            try {
                                count += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, daystatitem,
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

            if (count != 0) {
                sb.append("<entity id='").append(entry.getKey()).append("'");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" toolText='").append(value).append("  流量:").append(count);
                sb.append("' Value='").append(count).append("'");
                sb.append("/>");
                total += count;
                result.put(value, String.valueOf(count));
            } else {
                sb.append("<entity id='");
                sb.append(entry.getKey());
                sb.append("' toolText='");
                sb.append(value).append("  流量:0 '");
                sb.append(" displayValue='").append(value).append("'");
                sb.append(" Value='0' />");
                 result.put(value, String.valueOf(0));
            }

        }
        sb.append("</data>");
        sb.append("<styles> <definition> <style name='CaptionFont' type='font' size='12'/> <style name='datasetFont' type='font' size='12'/> <style name='myHTMLFont' type='font' isHTML='1' /> </definition> <application> <apply toObject='CAPTION' styles='CaptionFont' /> <apply toObject='seriesName' styles='datasetFont' /> <apply toObject='TOOLTIP' styles='myHTMLFont' /> </application> </styles>");
        sb.append("</map>");
        result.put("total", total);
        result.put("xml", sb.toString());
    }

    @SuppressWarnings({"NullArgumentToVariableArgMethod"})
    public void visitorsSourceDay() {
        double count = 0;
        StringBuffer total = new StringBuffer();
        StringBuffer nextTotal = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                if (total.length() != 0) {
                    total.append("+count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                } else {
                    total.append("count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                }
            } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                if (nextTotal.length() != 0) {
                    nextTotal.append("+count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                } else {
                    nextTotal.append("count" + (calendar.get(Calendar.DAY_OF_MONTH)));
                }
            }
        }

        StringBuffer sql = new StringBuffer("Select (");
        sql.append(total);
        if (total.toString().indexOf("+") != -1) {
            total.setLength(0);
            total.append("total");
        }
        sql.append(") as ").append(total).append(" , item From urldaystatitem");
        sql.append(" Where 1=1");
        sql.append(" And cp = '").append(cp).append("'");
        sql.append(" And period = '").append(_yymmdd).append("'");
        sql.append(" And type = 'Flow' And subtype = 'URL' Order By ").append(total).append(" Desc");
        List ips = urlDayStatItemService.getUrldaystatitemListBySql(sql.toString(), 0, 10);
        List nextIps = null;
        if (ips != null && ips.size() > 0) {
            for (int i = 0; i < ips.size(); i++) {
                Object[] object = (Object[]) ips.get(i);
                int num = Integer.parseInt(object[0].toString());
                count += Double.parseDouble(object[0].toString());
                if (nextTotal.length() != 0) {
                    sql.setLength(0);
                    sql.append("Select (");
                    sql.append(nextTotal);
                    if (nextTotal.toString().indexOf("+") != -1) {
                        nextTotal.setLength(0);
                        nextTotal.append("total");
                    }
                    sql.append(") as ").append(nextTotal).append(" , item From urldaystatitem");
                    sql.append(" Where 1=1");
                    sql.append(" And cp = '").append(cp).append("'");
                    sql.append(" And period = '").append(yymmdd).append("'");
                    sql.append(" And type = 'Flow' And subtype = 'URL' Order By ").append(nextTotal).append(" Desc");
                    nextIps = urlDayStatItemService.getUrldaystatitemListBySql(sql.toString(), 0, 10);
                    if (nextIps != null && nextIps.size() > 0) {
                        for (int j = 0; j < nextIps.size(); j++) {
                            Object[] next = (Object[]) nextIps.get(j);
                            if (object[1].toString().equals(next[1].toString())) {
                                count += Double.parseDouble(next[0].toString());
                                num += Integer.parseInt(next[0].toString());
                                break;
                            }
                        }
                    }
                }
                result.put(object[1].toString(), String.valueOf(num));
            }
        }
        result.put("count", String.valueOf(count));
    }

    public void liveWatchChannelHour() {

        boolean find = false;
        List<Hourstatitem> highList = hourStatItemService.getHourstatitemList(cp, "LiveWatch", "Highest", null, _yymmdd.toString());
        List<Hourstatitem> lowList = hourStatItemService.getHourstatitemList(cp, "LiveWatch", "Lowest", null, _yymmdd.toString());
        StringBuffer chart = new StringBuffer("<chart palette='4' caption='").append("全频道").append("观看人数统计' yAxisName='人数(IP数)' xAxisName='频道' numdivlines='4'  lineThickness='2' showValues='0' formatNumberScale='0' decimals='1' anchorRadius='2' yAxisMinValue='800000' shadowAlpha='50'>");
        chart.append("<categories>");
        StringBuffer sbOne = new StringBuffer();
        StringBuffer sbTwo = new StringBuffer();
        sbOne.append("<dataset seriesName='峰值人数'>");
        sbTwo.append("<dataset seriesName='最低人数'>");
        for (Hourstatitem hourstatitem : highList) {
            int higthNum = 0;
            int lowNum = 0;
            if (!find) {
                find = true;
            }
            chart.append("<category label='").append(hourstatitem.getItem()).append("'/>");
            String highValue = null;
            String lowValue = null;
            for (int i = begin; i <= end; i++) {
                highValue = MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + (i)).toString();
                for (Hourstatitem low : lowList) {
                    if (low.getItem().equalsIgnoreCase(hourstatitem.getItem())) {
                        lowValue = MirrorUtil.getValue(Hourstatitem.class, low, "count" + (i)).toString();
                        break;
                    }
                }
                if (highValue == null) {
                    highValue = "0";
                }
                if (lowValue == null) {
                    lowValue = "0";
                }

                if (Integer.parseInt(highValue) > higthNum) {
                    higthNum = Integer.parseInt(highValue);
                }
                if (Integer.parseInt(lowValue) > lowNum) {
                    lowNum = Integer.parseInt(lowValue);
                }
//                higthNum += Integer.parseInt(highValue);
//                lowNum += Integer.parseInt(lowValue);
            }
            sbOne.append("<set value='").append(higthNum).append("'/>");
            sbTwo.append("<set value='").append(lowNum).append("'/>");
            result.put(hourstatitem.getItem(), higthNum + ";" + lowNum);
        }
        if (!find) {
            chart.append("<category label='").append("").append("'/>");
            sbOne.append("<set value='").append("0").append("'/>");
            sbTwo.append("<set value='").append("0").append("'/>");
        }
        sbOne.append("</dataset>");
        sbTwo.append("</dataset>");
        chart.append("</categories>");
        chart.append(sbOne);
        chart.append(sbTwo);
        chart.append("</chart>");
        result.put("xml", chart.toString());
    }

    public void liveWatchChannelDay() {
        boolean init = false;
        boolean find = false;
        List<Daystatitem> highList = dayStatItemService.getDaystatitemList(cp, "LiveWatch", "Highest", null, _yymmdd.toString());
        List<Daystatitem> lowList = dayStatItemService.getDaystatitemList(cp, "LiveWatch", "Lowest", null, _yymmdd.toString());
        List<Daystatitem> nextHighList = null;
        List<Daystatitem> nextLowList = null;
//        if (highList.size() != lowList.size()) {
//            return;
//        }
        Map<String, String> map = new HashMap<String, String>();
        for (Daystatitem daystatitem : highList) {
            if (!find) {
                find = true;
            }
            map.put(daystatitem.getItem(), daystatitem.getItem());
        }

        StringBuffer chart = new StringBuffer("<chart palette='4' caption='").append("全频道").append("观看人数统计' yAxisName='人数(IP数)' xAxisName='日期' numdivlines='4'  lineThickness='2' showValues='0' formatNumberScale='0' decimals='1' anchorRadius='2' yAxisMinValue='800000' shadowAlpha='50'>");
        chart.append("<categories>");
        StringBuffer sbOne = new StringBuffer();
        StringBuffer sbTwo = new StringBuffer();
        sbOne.append("<dataset seriesName='峰值人数'>");
        sbTwo.append("<dataset seriesName='最低人数'>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = entry.getValue();
            int highValue = 0;
            int lowValue = 0;
            chart.append("<category label='").append(value).append("'/>");
            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, month - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            while (calendar.before(compareCalendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                    for (Daystatitem high : highList) {
                        if (high.getItem().equals(value)) {
                            highValue += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, high, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                        }
                    }
                    for (Daystatitem low : lowList) {
                        if (low.getItem().equals(value)) {
                            lowValue += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, low, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                        }
                    }

                } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
                    if (!init) {
                        nextHighList = dayStatItemService.getDaystatitemList(cp, "LiveWatch", "Highest", null, yymmdd.toString());
                        nextLowList = dayStatItemService.getDaystatitemList(cp, "LiveWatch", "Lowest", null, yymmdd.toString());
                        if (nextHighList.size() != nextLowList.size()) {
                            break;
                        }
                    }
                    if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                        for (Daystatitem high : nextHighList) {
                            if (high.getItem().equals(value)) {
                                highValue += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, high, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                            }
                        }
                        for (Daystatitem low : nextLowList) {
                            if (low.getItem().equals(value)) {
                                lowValue += Integer.parseInt(MirrorUtil.getValue(Daystatitem.class, low, "count" + (calendar.get(Calendar.DAY_OF_MONTH))).toString());
                            }
                        }
                    }
                }
            }
            sbOne.append("<set value='").append(highValue).append("'/>");
            sbTwo.append("<set value='").append(lowValue).append("'/>");
            result.put(value, highValue + ";" + lowValue);
        }
        if (!find) {
            chart.append("<category label='").append("").append("'/>");
            sbOne.append("<set value='").append("0").append("'/>");
            sbTwo.append("<set value='").append("0").append("'/>");
        }
        sbOne.append("</dataset>");
        sbTwo.append("</dataset>");
        chart.append("</categories>");
        chart.append(sbOne);
        chart.append(sbTwo);
        chart.append("</chart>");
        result.put("xml", chart.toString());
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
}
