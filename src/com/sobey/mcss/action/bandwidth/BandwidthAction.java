package com.sobey.mcss.action.bandwidth;

import com.opensymphony.xwork2.ActionSupport;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.StringUtil;
import com.sobey.mcss.domain.Hourstatitem;
import com.sobey.mcss.service.BroadBandStatService;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import com.sobey.mcss.service.DayStatItemService;
import com.sobey.mcss.service.HourStatItemService;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Results({@org.apache.struts2.convention.annotation.Result(name = "Web", location = "webSpeedUp.jsp"), @org.apache.struts2.convention.annotation.Result(name = "Media", location = "mediaSpeedUp.jsp"), @org.apache.struts2.convention.annotation.Result(name = "Upload", location = "uploadSpeedUp.jsp")})
public class BandwidthAction extends ActionSupport {
    private String cp = "t.video.sobeycache.com";
    private String beginTime;
    private String endTime;
    private Map result;
    @Autowired
    private BroadBandStatService broadBandStatService;

    @Autowired
    private HourStatItemService hourStatItemService;

    public BandwidthAction() {
        this.result = new HashMap();
    }

    public Map getResult() {
        return this.result;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCp() {
        return this.cp;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String webSpeedUp() {
        return SpeedUp("Web");
    }

    public String mediaSpeedUp() {
        return SpeedUp("Media");
    }

    public String uploadSpeedUp() {
        return SpeedUp("Upload");
    }

    public String SpeedUp(String type) {
        String capton = "";
        if (type.equals("Web")) {
            capton = "网页加速带宽统计";
        }
        if (type.equals("Media")) {
            capton = "流媒体加速带宽统计";
        }
        if (type.equals("Upload")) {
            capton = "上传加速带宽统计";
        }
        StringBuffer sb = new StringBuffer();
        if (StringUtil.checkEmpty(beginTime)) {
            beginTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        if (StringUtil.checkEmpty(endTime)) {
            endTime = DateUtil.getCurrentTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD));
        }
        List list = null;
        if (DateUtil.checkDay(this.beginTime, this.endTime)) {
            boolean find = false;
            StringBuffer categories = new StringBuffer();
            StringBuffer values = new StringBuffer();
            sb.append("<chart  caption='").append(capton).append("' formatNumberScale='0' btnSwitchToPinModeTooltext='Pin模式可以把某块区域锁定下来，便于比较' btnSwitchtoZoomModeTitle='切换成缩放模式' btnSwitchToZoomModeTooltext='缩放模式可以任意缩放图表大小来查看数据' btnSwitchToPinModeTitle='切换成Pin模式' btnResetChartTitle='重置图表' btnResetChartTooltext='重置图表到初始状态' btnZoomOutTooltext='返回至上一个缩放级别' btnZoomOutTitle='返回'>");
            categories.append("<categories>");
            values.append("<dataset seriesName='MDN带宽'>");
            long count = 0;
            String maxTime = this.beginTime;
            int year = DateUtil.getSpecificTime(this.beginTime, DateUtil.YEAR);
            int montn = DateUtil.getSpecificTime(this.beginTime, DateUtil.MONTH) + 1;
            int day = DateUtil.getSpecificTime(this.beginTime, DateUtil.DAY_OF_MONTH);
            int begin = DateUtil.getSpecificTime(this.beginTime, DateUtil.HOUR_OF_DAY);
            int end;
            try {
                if (this.endTime.split(" ")[1].startsWith("24"))
                    end = 24;
                else
                    end = DateUtil.getSpecificTime(this.endTime, DateUtil.HOUR_OF_DAY);
            } catch (Exception e) {
                end = DateUtil.getSpecificTime(this.endTime, DateUtil.HOUR_OF_DAY);
            }
            if (end == 0) {
                end = 24;
            }

            StringBuffer parameter_one = new StringBuffer();
            StringBuffer parameter_two = new StringBuffer();
            if (beginTime.split(" ").length == 1) {
                parameter_one.append(year).append("-");
                if (String.valueOf(montn).length() == 1) {
                    parameter_one.append("0").append(montn);
                } else {
                    parameter_one.append(montn);
                }
                parameter_one.append("-");
                if (String.valueOf(day).length() == 1) {
                    parameter_one.append("0").append(day);
                } else {
                    parameter_one.append(day);
                }
                parameter_one.append(" ").append("00:00:00");
            } else {
                parameter_one.append(beginTime);
            }
            if (endTime.split(" ").length == 1) {
                parameter_two.append(year).append("-");
                if (String.valueOf(montn).length() == 1) {
                    parameter_two.append("0").append(montn);
                } else {
                    parameter_two.append(montn);
                }
                parameter_two.append("-");
                if (String.valueOf(day).length() == 1) {
                    parameter_two.append("0").append(day);
                } else {
                    parameter_two.append(day);
                }
                parameter_two.append(" ").append("24:00:00");
            } else {
                parameter_two.append(endTime);
            }
            list = this.broadBandStatService.getBroadbandstatListByHour(this.cp, type, parameter_one.toString(), parameter_two.toString());


            for (int i = 0; i < list.size(); ++i) {
                if (!find) {
                    find = true;
                }
                Object[] objects = (Object[]) list.get(i);
                String datetime = objects[3].toString().split("\\.")[0];
                categories.append("<category label='").append(datetime.split(" ")[1]).append("'/>");
                int broadband = Integer.parseInt(objects[4].toString());
                values.append(" <set value='").append(broadband).append("'/>");
            }
            if (!find) {
                for (int j = 0; j < 24; j++) {
                    String temp = "";
                    if (String.valueOf(j).length() == 1) {
                        temp = "0" + j;
                    } else {
                        temp = String.valueOf(j);
                    }
                    categories.append("<category label='").append(temp + ":00:00").append("'/>");
                    values.append(" <set value='").append("0").append("'/>");
                }

                maxTime = "NaN";
            }
            categories.append("</categories>");
            values.append("</dataset>");
            String max = "";
            String avg = "";
            StringBuffer date = new StringBuffer();
            date.append(year);
            if (montn < 10 && String.valueOf(montn).length() == 1) {
                date.append("0").append(montn);
            } else {
                date.append(montn);
            }
            if (day < 10 && String.valueOf(day).length() == 1) {
                date.append("0").append(day);
            } else {
                date.append(day);
            }
            List _list = hourStatItemService.getHourstatitemList(cp, "BroadBand", "Stat", type, date.toString());
            if (_list != null && _list.size() > 0) {
                Hourstatitem hourstatitem = (Hourstatitem) _list.get(0);
                max = hourstatitem.getExpend2();
                maxTime = hourstatitem.getExpend1();
                for (int i = 1; i <= 24; i++) {
                    count += Long.parseLong(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + i).toString());
                }
                avg = String.valueOf(count / 24);
            }
            NumberFormat formatter = new DecimalFormat("0");
            this.result.put("max", max);
            this.result.put("avg", avg);
            this.result.put("maxtime", maxTime);
            this.result.put("charts", "ZoomLine");
            sb.append(categories);
            sb.append(values);
        } else {
            StringBuffer categories = new StringBuffer();
            StringBuffer values = new StringBuffer();
            StringBuffer values1 = new StringBuffer();

            sb.append("<chart palette='4' caption='带宽统计图' yAxisName='带宽' xAxisName='日期' numdivlines='4' lineThickness='2' showValues='0' formatNumberScale='0' decimals='1' anchorRadius='2'  yAxisMinValue='800000' shadowAlpha='50'>");
            categories.append("<categories>");
            values.append("<dataset seriesName='峰值带宽'>");
            values1.append("<dataset seriesName='均值带宽'>");
            int begin = DateUtil.getSpecificTime(this.beginTime, 5);
            int end;
            try {
                if (this.endTime.split(" ")[1].startsWith("24"))
                    end = DateUtil.getSpecificTime(this.endTime, 5) - 1;
                else
                    end = DateUtil.getSpecificTime(this.endTime, 5);
            } catch (Exception e) {
                end = DateUtil.getSpecificTime(this.endTime, 5);
            }
            int year = DateUtil.getSpecificTime(this.beginTime, 1);
            int montn = DateUtil.getSpecificTime(this.beginTime, 2) + 1;
            int endMonth = DateUtil.getSpecificTime(this.endTime, 2) + 1;
            Calendar calendar = Calendar.getInstance();
            Calendar compareCalendar = Calendar.getInstance();
            compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            calendar.set(year, montn - 1, begin);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Calendar temp = Calendar.getInstance();
            temp.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
            temp.add(Calendar.DAY_OF_MONTH, 1);
//            list = this.broadBandStatService.getBroadbandstatListByDay(this.cp, type, beginTime, DateUtil.getSpecificTime(temp.getTime(), DateUtil._YY_MM_DD));
            list = this.hourStatItemService.getHourstatitemList(cp, "BroadBand", "Stat", type);
            List result = new ArrayList();
//            if (list.size() == 0) {
//                return type;
//            }
            long max = 0;
            String maxTime = "";
            while (calendar.before(compareCalendar)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                boolean find = false;
                categories.append("<category label='").append(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D)).append("'/>");
                for (int j = 0; j < list.size(); ++j) {
                    Hourstatitem hourstatitem = (Hourstatitem) list.get(j);
                    long count = 0;
                    String datetime = hourstatitem.getExpend1();
                    int day = DateUtil.getSpecificTime(datetime, 5);
                    int a = calendar.get(Calendar.DAY_OF_MONTH);
                    if (a == day) {
                        try {
                            for (int i = 1; i <= 24; i++) {
                                count += Long.parseLong(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + i).toString());
                            }
                            String _broadband = hourstatitem.getExpend2();
                            long broadband =Long.parseLong(_broadband);
                            if(broadband > max){
                                max = broadband;
                            }
                            values.append(" <set value='").append(_broadband).append("'/>");
                            values1.append(" <set value='").append(count / 24).append("'/>");
                            result.add(hourstatitem);
                            find = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                }
                if (!find) {
                    values.append("<set value=''/>");
                    values1.append("<set value=''/>");
                }
            }
            categories.append("</categories>");
            values.append("</dataset>");
            values1.append("</dataset>");
            sb.append(categories);
            sb.append(values);
            sb.append(values1);
            this.result.put("charts", "MSLine");
            this.result.put("list", result);
        }
        sb.append("</chart>");
        this.result.put("xml", sb.toString());
        return type;
    }
}