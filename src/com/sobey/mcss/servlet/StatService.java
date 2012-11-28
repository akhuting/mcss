package com.sobey.mcss.servlet;

import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.mcss.domain.Daystatitem;
import com.sobey.mcss.domain.Hourstatitem;
import com.sobey.mcss.service.BroadBandStatService;
import com.sobey.mcss.service.DayStatItemService;
import com.sobey.mcss.service.HourStatItemService;
import com.sobey.mcss.service.UploadStatService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yanggang.
 * Date: 11-2-12
 * Time: 上午11:12
 * To change this template use File | Settings | File Templates.
 */
@Component("StatService")
public class StatService extends HttpServlet {

    //    private int currentHour = DateUtil.getSpecificTime(DateUtil.getCurrentTime(DateUtil._YY_MM_DD_TIME), DateUtil.HOUR_OF_DAY);
    @Autowired
    private HourStatItemService hourStatItemService;

    @Autowired
    private BroadBandStatService broadBandStatService;

    @Autowired
    private DayStatItemService dayStatItemService;

    @Autowired
    private UploadStatService uploadStatService;

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
        Document document = null;
        try {
            document = DocumentHelper.parseText(sb.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
            System.out.println("解析xml错误");
            return;
        }
        for (Object stats : document.getRootElement().elements("stat")) {
            Element stat = (Element) stats;

            String method = stat.elementText("type");
            /**
             * 直播统计
             */
            if ("live".equals(method)) {

                String cp = stat.elementText("cpid");
                try {
                    if (cp.indexOf(":") != -1) {
                        cp = cp.split(":")[0];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String date = stat.elementText("date");
                int hour = DateUtil.getSpecificTime(date, DateUtil.HOUR_OF_DAY) + 1;

                List channels = stat.element("channels").elements("channel");
                double broadband = Long.parseLong(stat.elementText("broadband"));

                broadband = broadband / 1048576d;

                //保存带宽统计数据
//                if (broadband != 0)

                broadBandStatService.saveBroadbandstat(cp, "Media", date, broadband);

                /**
                 * 每天的带宽信息还要另外保存到HourStatItem表里，加快查询带宽统计跨几天甚至一个月的数据查询速度
                 */
                List lis = broadBandStatService.getBroadbandstatListBySql("SELECT * FROM broadbanddaystat WHERE  cp = ? and period =? and type = ?", new Object[]{cp, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), "Media"});
                Object[] broad = null;
                if (lis != null && lis.size() > 0) {
                    broad = (Object[]) lis.get(0);
                }
//                Hourstatitem broad = mcssStreamerService.getHourstatitemByPk(cp, "BroadBand", "Stat", "Media", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                if (broad == null) {
                    String time = date.split(" ")[1];
                    broadBandStatService.executeSql("INSERT INTO broadbanddaystat VALUES(?,?,?,?,?,?,?)", new Object[]{cp, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), "Media", time, broadband, 0, broadband});
                } else {
                    double max = Double.parseDouble(broad[4].toString());
                    double total = Double.parseDouble(broad[6].toString()) + broadband;
                    float total_add = Float.parseFloat(broad[7].toString()) + 1;
                    double avg = total / total_add;

                    if (broadband > max) {
                        String time = date.split(" ")[1];
                        broadBandStatService.executeSql("UPDATE broadbanddaystat SET max_time = ?, max_value = ?, avg_value = ? ,  total = ? WHERE period = ? AND type='Media' AND cp=? AND total_add = ?", new Object[]{time, broadband, avg, total, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), cp , total_add});
                    } else {
                        broadBandStatService.executeSql("UPDATE broadbanddaystat SET avg_value = ? ,  total = ? WHERE period = ?AND type='Media' AND cp=? AND total_add = ?", new Object[]{avg, total, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), cp , total_add});
                    }
                }
                for (Object object : channels) {
                    Element element = (Element) object;
                    //频道名称
                    String name = element.elementText("name");

                    //当前在线人数
                    String onlineCount = element.elementText("onliveCount");

                    //保存小时数据
                    Map map = new HashMap();
                    map.put("count" + hour, Integer.parseInt(onlineCount));
                    try {
                        hourStatItemService.saveHourStatItem(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), map);
                        hourStatItemService.saveHourStatItem(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), map);
                    } catch (Exception e) {  //异常表示数据库已经有数据，更新数据即可

                        //从数据查出当前小时的峰值在线人数
                        Hourstatitem hourstatitem = hourStatItemService.getHourstatitemByPk(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                        String high = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + hour));

                        //如果当前在线人数大于数据库在线人数则更新数据库
                        if (Integer.parseInt(onlineCount) > Integer.parseInt(high)) {
                            hourStatItemService.updateHourStatItem(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), map);
                        }

                        //和上面同理，保存最低在线人数
                        hourstatitem = hourStatItemService.getHourstatitemByPk(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                        String low = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + hour));
                        if (Integer.parseInt(onlineCount) < Integer.parseInt(low) || low.equals("0")) {
                            hourStatItemService.updateHourStatItem(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), map);
                        }
//                        StringUtil.println(String.format("Object %s already exists in the database . Because primary key constraint violation , So the object will be updated", Hourstatitem.class.getSimpleName()));
                    }

                    //保存天的数据
                    int day = DateUtil.getSpecificTime(date, DateUtil.DAY_OF_MONTH);
                    map.clear();
                    map.put("count" + day, Integer.parseInt(onlineCount));
                    try {
                        dayStatItemService.saveDayStatItem(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM), map);
                        dayStatItemService.saveDayStatItem(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM), map);
                    } catch (Exception e) { //异常表示数据库已经有数据，更新数据即可
                        Daystatitem daystatitem = dayStatItemService.getDaystatitemByPk(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM));

                        String high = String.valueOf(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + day));
                        if (Integer.parseInt(onlineCount) > Integer.parseInt(high)) {
                            dayStatItemService.updateDayStatItem(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM), map);
                        }
                        daystatitem = dayStatItemService.getDaystatitemByPk(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM));

                        String low = String.valueOf(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + hour));
                        if (Integer.parseInt(onlineCount) < Integer.parseInt(low) || low.equals("0")) {
                            dayStatItemService.updateDayStatItem(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM), map);
                        }
//                        StringUtil.println(String.format("Object %s already exists in the database . Because primary key constraint violation , So the object will be updated", Daystatitem.class.getSimpleName()));
                    }

                }

            }

            /**
             * 点播统计
             */
            if ("vod".equals(method)) {

                String cp = stat.elementText("cpid");
                try {
                    if (cp.indexOf(":") != -1) {
                        cp = cp.split(":")[0];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String date = stat.elementText("date");
                int hour = DateUtil.getSpecificTime(date, DateUtil.HOUR_OF_DAY);
                double broadband = Double.parseDouble(stat.elementText("broadband"));
                broadband = broadband / 1048576d;
                String playCount = stat.elementText("playCount");

                //保存带宽统计数据
//                if (broadband != 0)
                broadBandStatService.saveBroadbandstat(cp, "Media", date, broadband);

                /**
                 * 每天的带宽信息还要另外保存到HourStatItem表里，加快查询带宽统计跨几天甚至一个月的数据查询速度
                 */
                List lis = broadBandStatService.getBroadbandstatListBySql("SELECT * FROM broadbanddaystat WHERE  cp = ? and period =? and type = ?", new Object[]{cp, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), "Media"});
                Object[] broad = null;
                if (lis != null && lis.size() > 0) {
                    broad = (Object[]) lis.get(0);
                }
//                Hourstatitem broad = mcssStreamerService.getHourstatitemByPk(cp, "BroadBand", "Stat", "Media", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                if (broad == null) {
                    String time = date.split(" ")[1];
                    broadBandStatService.executeSql("INSERT INTO broadbanddaystat VALUES(?,?,?,?,?,?)", new Object[]{cp, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), "Media", time, broadband, 0});
                } else {
                    double max = Double.parseDouble(broad[4].toString());
                    double total = Double.parseDouble(broad[6].toString()) + broadband;
                    float total_add = Float.parseFloat(broad[7].toString()) + 1;
                    double avg = total / total_add;

                    if (broadband > max) {
                        String time = date.split(" ")[1];
                        broadBandStatService.executeSql("UPDATE broadbanddaystat SET max_time = ?, max_value = ?, avg_value = ? ,  total = ? WHERE period = ? AND type='Media' AND cp=? AND total_add = ?", new Object[]{time, broadband, avg, total, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), cp , total_add});
                    } else {
                        broadBandStatService.executeSql("UPDATE broadbanddaystat SET avg_value = ? ,  total = ? WHERE period = ?AND type='Media' AND cp=? AND total_add = ?", new Object[]{avg, total, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), cp , total_add});
                    }
                }
                //保存小时数据
                Map map = new HashMap();
                map.put("count" + hour, Integer.parseInt(playCount));
                try {
                    hourStatItemService.saveHourStatItem(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), map);
                    hourStatItemService.saveHourStatItem(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), map);
                } catch (Exception e) {  //异常表示数据库已经有数据，更新数据即可
                    Hourstatitem hourstatitem = hourStatItemService.getHourstatitemByPk(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));

                    String high = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + hour));
                    if (Integer.parseInt(playCount) > Integer.parseInt(high)) {
                        hourStatItemService.updateHourStatItem(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), map);
                    }
                    hourstatitem = hourStatItemService.getHourstatitemByPk(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));

                    String low = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + hour));
                    if (Integer.parseInt(playCount) < Integer.parseInt(low) || low.equals("0")) {
                        hourStatItemService.updateHourStatItem(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), map);
                    }
//                    StringUtil.println(String.format("Object %s already exists in the database . Because primary key constraint violation , So the object will be updated", Hourstatitem.class.getSimpleName()));
                }

                //保存天的数据
                int day = DateUtil.getSpecificTime(date, DateUtil.DAY_OF_MONTH);
                map.clear();
                map.put("count" + day, Integer.parseInt(playCount));
                try {
                    dayStatItemService.saveDayStatItem(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM), map);
                    dayStatItemService.saveDayStatItem(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM), map);
                } catch (Exception e) { //异常表示数据库已经有数据，更新数据即可
                    Daystatitem daystatitem = dayStatItemService.getDaystatitemByPk(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM));

                    String high = String.valueOf(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + day));
                    if (Integer.parseInt(playCount) > Integer.parseInt(high)) {
                        dayStatItemService.updateDayStatItem(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM), map);
                    }
                    daystatitem = dayStatItemService.getDaystatitemByPk(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM));

                    String low = String.valueOf(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + hour));
                    if (Integer.parseInt(playCount) < Integer.parseInt(low) || low.equals("0")) {
                        dayStatItemService.updateDayStatItem(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM), map);
                    }
//                    StringUtil.println(String.format("Object %s already exists in the database . Because primary key constraint violation , So the object will be updated", Daystatitem.class.getSimpleName()));
                }

            }

            if ("UploadStatService".equals(method)) {
                String cp = stat.elementText("cpid");
                String taskId = stat.elementText("taskId");
                String begin = stat.elementText("beginDate");
                String end = stat.elementText("endDate");
                String ip = stat.elementText("ip");
                String dataFlow = stat.elementText("dataFlow");
                uploadStatService.saveUploadStat(cp, taskId, begin, end, ip, dataFlow);
            }
        }
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
