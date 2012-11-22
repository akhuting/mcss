package com.sobey.mcss.servlet;

import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.StringUtil;
import com.sobey.mcss.domain.Daystatitem;
import com.sobey.mcss.service.DayStatItemService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Yanggang
 * Date: 12-11-7
 * Time: 下午1:25
 * To change this template use File | Settings | File Templates.
 */
@Component("FlowService")
public class FlowService extends HttpServlet {
    //    private int currentHour = DateUtil.getSpecificTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD_TIME), DateUtil.HOUR_OF_DAY);
    @Autowired
    private DayStatItemService dayStatItemService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        if (sb.toString().equals("")) {
            System.out.println("xml数据为空");
            this.printString(resp, "xml数据为空");
        }
        JSONObject parameter = JSONObject.fromObject(sb.toString());
        JSONArray array = new JSONArray();
        int year;
        int month;
        int endMonth;
        int begin;
        int end;
        int _day;
        String unit_lable;
        StringBuffer _yymmdd;
        StringBuffer yymmdd;
        JSONObject result = new JSONObject();
        String cp;
        String beginTime;
        String endTime;
        beginTime = parameter.getString("begin");
        endTime = parameter.getString("end");
        cp = "cp='" + parameter.getString("cp") + "'";
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
        List<Daystatitem> ip_list = dayStatItemService.getDaystatitemList(cp, "Analysis", "Viewed", "IP", _yymmdd.toString());
        List<Daystatitem> pv_list = dayStatItemService.getDaystatitemList(cp, "Analysis", "Viewed", "PV", _yymmdd.toString());
        List<Daystatitem> flow_list = dayStatItemService.getDaystatitemList(cp, "Flow", "AreaWeb", null, _yymmdd.toString());


        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            double count = 0;
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                if (ip_list != null) {
                    for (int i = 0; i < ip_list.size(); i++) {
                        Daystatitem ipdaystatitem = ip_list.get(i);
                        count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                    }
                }
                JSONObject object = new JSONObject();
                object.put("ip", count);
                JSONObject dateObject = new JSONObject();
                dateObject.put("date", DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD));
                dateObject.put("value", object);
                array.add(dateObject);
            } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
            }
        }


        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            double count = 0;
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                if (pv_list != null) {
                    for (int i = 0; i < pv_list.size(); i++) {
                        Daystatitem ipdaystatitem = pv_list.get(i);
                        count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                    }
                }
                for(int i = 0; i < array.size() ; i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.getString("date").equals(DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD))){
                        object.put("pv", count);
                    }
                }
//                JSONObject object = array.getJSONObject(DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD));
//                object.put("pv", count);
//                array.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD), object);
            } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
            }
        }

        compareCalendar.set(year, DateUtil.getSpecificTime(endTime, DateUtil.MONTH), end);
        calendar.set(year, month - 1, begin);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        while (calendar.before(compareCalendar)) {
            double count = 0;
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(_yymmdd.toString())) {
                if (flow_list != null) {
                    for (int i = 0; i < flow_list.size(); i++) {
                        Daystatitem ipdaystatitem = flow_list.get(i);
                        count += Double.parseDouble(String.valueOf(MirrorUtil.getValue(Daystatitem.class, ipdaystatitem, "count" + (calendar.get(Calendar.DAY_OF_MONTH)))));
                    }
                }
                for(int i = 0; i < array.size() ; i++){
                    JSONObject object = array.getJSONObject(i);
                    if(object.getString("date").equals(DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD))){
                        object.put("flow", count);
                    }
                }
//                JSONObject object = array.getJSONObject(DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD));
//                object.put("flow", count);
//                array.put(DateUtil.getSpecificTime(calendar.getTime(), DateUtil._YY_MM_DD), object);
            } else if (DateUtil.getSpecificTime(calendar.getTime(), DateUtil.YY_MM).equals(yymmdd.toString())) {
            }
        }
        result.put("status" , 1);
        result.put("result" , array);
        System.out.println(result.toString());
    }

    protected void printString(HttpServletResponse response, String string) {
        response.setContentType("text/xml; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(string);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
