package com.sobey.mcss.action.bandwidth;

import com.opensymphony.xwork2.ActionSupport;
import com.sobey.common.util.CookieUtil;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.StringUtil;
import com.sobey.mcss.domain.Cp;
import com.sobey.mcss.domain.Userinfo;
import com.sobey.mcss.service.BroadBandStatService;
import com.sobey.mcss.service.CpService;
import com.sobey.mcss.service.DayStatItemService;
import com.sobey.mcss.service.UserService;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@Results({@org.apache.struts2.convention.annotation.Result(name = "Web", location = "webSpeedUp.jsp"), @org.apache.struts2.convention.annotation.Result(name = "Media", location = "mediaSpeedUp.jsp"), @org.apache.struts2.convention.annotation.Result(name = "Upload", location = "uploadSpeedUp.jsp")})
public class BandwidthAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    private String cp;
    private String beginTime;
    private String endTime;
    private Map result;

    private String userCps;

    @Autowired
    private DayStatItemService dayStatItemService;

    @Autowired
    private BroadBandStatService broadBandStatService;

    @Autowired
    private UserService userService;


    private HttpServletResponse response;
    private HttpServletRequest request;

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

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
        try {
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
            String unit = "";
            if (StringUtil.checkEmpty(beginTime)) {
                beginTime = DateUtil.getFirstDayOfWeek();
            }
            if (StringUtil.checkEmpty(endTime)) {
                endTime = DateUtil.getLastDayOfWeek();
            }
            List list = null;
            if (DateUtil.checkDay(this.beginTime, this.endTime)) {
                if (beginTime.split(" ").length == 1) {
                    beginTime = beginTime + " 00:00";
                }
                if (endTime.split(" ").length == 1) {
                    endTime = endTime + " 00:00";
                }
                initCp();
                if (cp == null) {
                    getMaxCp(type);
                }
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
                    if (this.endTime.split(" ")[1].startsWith("23"))
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
                    parameter_one.append(" ").append("00:00");
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
                    parameter_two.append(" ").append("23:59");
                } else {
                    if (endTime.endsWith(beginTime)) {
                        endTime = endTime.replace("00:00", "23:59");
                    }
                    parameter_two.append(endTime);
                }
                setCp();
                list = this.broadBandStatService.getBroadbandstatListByHour(this.userCps, type, parameter_one.toString(), parameter_two.toString());

                if (list != null) {
                    for (int i = 0; i < list.size(); ++i) {
                        if (!find) {
                            find = true;
                        }
                        Object[] objects = (Object[]) list.get(i);
                        String datetime = objects[3].toString().split("\\.")[0];
                        categories.append("<category label='").append(datetime.split(" ")[1]).append("'/>");
                        float broadband = Float.parseFloat(objects[4].toString());
                        values.append(" <set value='").append(broadband).append("'/>");
                    }
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
                String max = "0";
                String avg = "0";
                StringBuffer date = new StringBuffer();
                StringBuffer newDate = new StringBuffer();
                date.append(year);
                newDate.append(year);
                if (montn < 10 && String.valueOf(montn).length() == 1) {
                    date.append("0").append(montn);
                    newDate.append("-").append("0").append(montn);
                } else {
                    date.append(montn);
                    newDate.append("-").append(montn);
                }
                if (day < 10 && String.valueOf(day).length() == 1) {
                    date.append("0").append(day);
                    newDate.append("-").append("0").append(day);
                } else {
                    date.append(day);
                    newDate.append("-").append(day);
                }
                String sql = "SELECT * FROM broadbanddaystat where period = '" + newDate + "' and type = '" + type + "' and " + userCps;
                List _list = this.dayStatItemService.getDaystatitemListBySql(sql, null);
                if (_list != null && _list.size() > 0) {
                    Object[] objects = (Object[]) _list.get(0);
                    max = objects[4].toString();
                    maxTime = objects[1].toString() + " " + objects[3];
                    avg = objects[5].toString();
                }
                String[] temp = new String[1];
//                StringUtil.byteToUnit(Double.parseDouble(max), temp, null);
                unit = temp[0];
                this.result.put("max", max);
                this.result.put("avg", avg);
//                this.result.put("max",  StringUtil.byteToUnit(Double.parseDouble(max), temp, unit));
//                this.result.put("avg", StringUtil.byteToUnit(Double.parseDouble(avg), temp, unit));
                this.result.put("maxtime", maxTime);
                this.result.put("charts", "ZoomLine");
                sb.append(categories);
                sb.append(values);
            } else {
                float min = 0;
                initCp();
                if (cp == null) {
                    getMaxCp(type);
                }
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
                setCp();
                String sql = "SELECT * FROM broadbanddaystat where period BETWEEN '" + beginTime + "' and '" + endTime + "' and " + userCps + " and type = '" + type + "'";
                list = this.dayStatItemService.getDaystatitemListBySql(sql, null);
                List result = new ArrayList();

                while (calendar.before(compareCalendar)) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    boolean find = false;
                    categories.append("<category label='").append(DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM_D)).append("'/>");
                    if (list != null) {
                        for (int j = 0; j < list.size(); ++j) {
                            Object[] objects = (Object[]) list.get(j);
                            String datetime = objects[1] + " " + objects[3];
                            int day = DateUtil.getSpecificTime(datetime, 5);
                            int a = calendar.get(Calendar.DAY_OF_MONTH);
                            float maxValue = Float.parseFloat(objects[4].toString());
                            if (min > maxValue) {
                                min = maxValue;
                            }
                            if (a == day) {
                                values.append(" <set value='").append(objects[4]).append("'/>");
                                values1.append(" <set value='").append(objects[5]).append("'/>");
                                result.add(objects);
                                find = true;
                                break;
                            }
                        }
                    }
                    if (!find) {
                        values.append("<set value=''/>");
                        values1.append("<set value=''/>");
                    }
                }
                String[] t = new String[1];
//                StringUtil.byteToUnit(Double.parseDouble(String.valueOf(min)), t, null);
                unit = t[0];
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
            this.result.put("unit", unit);
            if (cp != null) {
                CookieUtil.addCookie(response, type, cp, 2592000);
            }
            return type;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Cookie中保存的CP
     *
     * @param name cookie name
     */
    private void getMaxCp(String name) {
        this.cp = null;
        Cookie cookie = CookieUtil.getCookieByName(request, name);
        if (cookie != null) {
            String temp = cookie.getValue();
            for (Cp _cp : getAllCp()) {
                if (temp.equals(_cp.getCp())) {
                    this.cp = temp;
                }
            }
        }
        if (cp == null || cp.equals("")) {
            for (Cp _cp : getAllCp()) {
                this.cp = _cp.getCp();
                break;
            }
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
                        if (subCp.getPid() == _cp.getId())
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

    private void setCp() {
        if (cp != null && userCps == null) {
            userCps = " cp = '" + this.cp + "'";
        }
    }

    @Autowired
    private CpService cpService;

    private List<Cp> getAllCp() {
        Userinfo userinfo = (Userinfo) request.getSession().getAttribute("user");
        if (userinfo.getUserStatus() == 1) {
            return cpService.getAll();
        }
        return userService.getUserById(userinfo.getUserid()).get(0).getCps();
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.response = httpServletResponse;
    }
}