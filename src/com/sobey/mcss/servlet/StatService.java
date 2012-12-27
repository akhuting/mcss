package com.sobey.mcss.servlet;

import com.sobey.common.util.DateUtil;
import com.sobey.common.util.MirrorUtil;
import com.sobey.common.util.StringUtil;
import com.sobey.mcss.domain.Daystatitem;
import com.sobey.mcss.domain.Hourstatitem;
import com.sobey.mcss.service.BroadBandStatService;
import com.sobey.mcss.service.DayStatItemService;
import com.sobey.mcss.service.HourStatItemService;
import com.sobey.mcss.service.UploadStatService;
import org.apache.log4j.Logger;
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
import java.util.ArrayList;
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

    Logger logger = Logger.getLogger(getClass());
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
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("收到Xml信息：" + sb.toString());
        }
        if (sb.toString().equals("")) {
            logger.error("xml数据为空");
            this.printString(resp, "xml数据为空");
        }
        Document document = null;
        try {
            document = DocumentHelper.parseText(sb.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error("解析xml错误：" + sb.toString());
            return;
        }
        String ip = req.getRemoteHost();
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
                saveBroadBand(broadband, cp, date, ip);

                for (Object object : channels) {
                    Element element = (Element) object;
                    //频道名称
                    String name = element.elementText("name");

                    //当前在线人数
                    String onlineCount = element.elementText("onliveCount");

                    String s[] = DateUtil.getBeginEnd(DateUtil.getDateTime(date)).split(",");

                    List liveTemp = broadBandStatService.getBroadbandstatListBySql("SELECT * FROM livetemp WHERE cp = ? AND dt = ? AND ip = ? AND channel = ?", cp, s[1], ip, name);
                    if (liveTemp != null && liveTemp.size() > 0) {
                        for (Object aLiveTemp : liveTemp) {
                            Object[] objects = (Object[]) aLiveTemp;
                            int id = Integer.parseInt(objects[0].toString());
                            broadBandStatService.executeSql("UPDATE livetemp SET online = ? WHERE id = ?", onlineCount, id);
                        }
                    } else {
                        broadBandStatService.executeSql("INSERT INTO livetemp(cp,dt,ip,online,channel)  VALUES(?,?,?,?,?)", cp, s[1], ip, onlineCount, name);
                    }
                    List<String> dt = new ArrayList();
                    List<String> ips = new ArrayList();
                    liveTemp = broadBandStatService.getBroadbandstatListBySql("SELECT * FROM livetemp WHERE cp = ? AND HOUR(dt) = ?  AND channel = ?", cp, hour - 1, name);
                    for (Object aLiveTemp : liveTemp) {
                        Object[] objects = (Object[]) aLiveTemp;
                        if (!dt.contains(objects[2].toString())) {
                            dt.add(objects[2].toString());
                        }
                        if (!ips.contains(objects[3].toString())) {
                            ips.add(objects[3].toString());
                        }
                    }

                    int max = 0;
                    int min = 0;
                    for (String d : dt) {
                        int online = 0;
                        for (Object aLiveTemp : liveTemp) {
                            Object[] objects = (Object[]) aLiveTemp;
                            if (objects[2].toString().equals(d)) {
                                for (String i : ips) {
                                    if (i.equals(objects[3].toString())) {
                                        online += Integer.parseInt(objects[4].toString());
                                    }
                                }
                            }
                        }
                        if (online > max) {
                            max = online;
                        }
                        if (online < min) {
                            min = online;
                        }
                    }

                    //保存小时数据
                    Map maxMap = new HashMap();
                    Map minMap = new HashMap();
                    maxMap.put("count" + hour, max);
                    minMap.put("count" + hour, min);

                    try {
                        hourStatItemService.saveHourStatItem(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), maxMap);
                        hourStatItemService.saveHourStatItem(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), minMap);
                    } catch (Exception e) {  //异常表示数据库已经有数据，更新数据即可

                        //从数据查出当前小时的峰值在线人数
                        Hourstatitem hourstatitem = hourStatItemService.getHourstatitemByPk(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                        String high = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + hour));

                        //如果当前在线人数大于数据库在线人数则更新数据库
                        if (max > Integer.parseInt(high)) {
                            hourStatItemService.updateHourStatItem(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), maxMap);
                        }

                        //和上面同理，保存最低在线人数
                        hourstatitem = hourStatItemService.getHourstatitemByPk(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                        String low = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + hour));
                        if (min < Integer.parseInt(low)) {
                            hourStatItemService.updateHourStatItem(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), minMap);
                        }
//                        StringUtil.println(String.format("Object %s already exists in the database . Because primary key constraint violation , So the object will be updated", Hourstatitem.class.getSimpleName()));
                    }

                    max = 0;
                    min = 0;

                    Hourstatitem maxStatItem = hourStatItemService.getHourstatitemByPk(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                    Hourstatitem minStatItem = hourStatItemService.getHourstatitemByPk(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));

                    for (int i = 1; i <= 24; i++) {
                        int maxValue = Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, maxStatItem, "count" + (i)).toString());
                        int minValue = Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, minStatItem, "count" + (i)).toString());
                        if (maxValue > max) {
                            max = maxValue;
                        }
                        if (minValue < min) {
                            min = minValue;
                        }
                    }
                    //保存天的数据
                    int day = DateUtil.getSpecificTime(date, DateUtil.DAY_OF_MONTH);
                    maxMap.clear();
                    minMap.clear();
                    maxMap.put("count" + day, max);
                    minMap.put("count" + day, min);
                    try {
                        dayStatItemService.saveDayStatItem(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM), maxMap);
                        dayStatItemService.saveDayStatItem(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM), minMap);
                    } catch (Exception e) { //异常表示数据库已经有数据，更新数据即可
                        Daystatitem daystatitem = dayStatItemService.getDaystatitemByPk(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM));

                        String high = String.valueOf(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + day));
                        if (max > Integer.parseInt(high)) {
                            dayStatItemService.updateDayStatItem(cp, "LiveWatch", "Highest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM), maxMap);
                        }
                        daystatitem = dayStatItemService.getDaystatitemByPk(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM));

                        String low = String.valueOf(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + hour));
                        if (min < Integer.parseInt(low)) {
                            dayStatItemService.updateDayStatItem(cp, "LiveWatch", "Lowest", name, DateUtil.getSpecificTime(date, DateUtil.YY_MM), minMap);
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
                int hour = DateUtil.getSpecificTime(date, DateUtil.HOUR_OF_DAY) + 1;
                double broadband = Double.parseDouble(stat.elementText("broadband"));
                saveBroadBand(broadband, cp, date, ip);
                String playCount = stat.elementText("playCount");

                String s[] = DateUtil.getBeginEnd(DateUtil.getDateTime(date)).split(",");

                List vodTemp = broadBandStatService.getBroadbandstatListBySql("SELECT * FROM vodtemp WHERE cp = ? AND dt = ? AND ip = ? ", cp, s[1], ip);
                if (vodTemp != null && vodTemp.size() > 0) {
                    for (Object aLiveTemp : vodTemp) {
                        Object[] objects = (Object[]) aLiveTemp;
                        int id = Integer.parseInt(objects[0].toString());
                        broadBandStatService.executeSql("UPDATE vodtemp SET online = ? WHERE id = ?", playCount, id);
                    }
                } else {
                    broadBandStatService.executeSql("INSERT INTO vodtemp(cp,dt,ip,online)  VALUES(?,?,?,?)", cp, s[1], ip, playCount);
                }
                List<String> dt = new ArrayList();
                List<String> ips = new ArrayList();
                vodTemp = broadBandStatService.getBroadbandstatListBySql("SELECT * FROM vodtemp WHERE cp = ? AND HOUR(dt) = ? ", cp, hour - 1);
                for (Object aLiveTemp : vodTemp) {
                    Object[] objects = (Object[]) aLiveTemp;
                    if (!dt.contains(objects[2].toString())) {
                        dt.add(objects[2].toString());
                    }
                    if (!ips.contains(objects[3].toString())) {
                        ips.add(objects[3].toString());
                    }
                }

                int max = 0;
                int min = 0;
                for (String d : dt) {
                    int online = 0;
                    for (Object aLiveTemp : vodTemp) {
                        Object[] objects = (Object[]) aLiveTemp;
                        if (objects[2].toString().equals(d)) {
                            for (String i : ips) {
                                if (i.equals(objects[3].toString())) {
                                    online += Integer.parseInt(objects[4].toString());
                                }
                            }
                        }
                    }
                    if (online > max) {
                        max = online;
                    }
                    if (online < min) {
                        min = online;
                    }
                }

                //保存小时数据
                Map maxMap = new HashMap();
                Map minMap = new HashMap();
                maxMap.put("count" + hour, max);
                minMap.put("count" + hour, min);


                try {
                    hourStatItemService.saveHourStatItem(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), maxMap);
                    hourStatItemService.saveHourStatItem(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), minMap);
                } catch (Exception e) {  //异常表示数据库已经有数据，更新数据即可


                    //从数据查出当前小时的峰值在线人数
                    Hourstatitem hourstatitem = hourStatItemService.getHourstatitemByPk(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                    String high = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + hour));

                    //如果当前在线人数大于数据库在线人数则更新数据库
                    if (max > Integer.parseInt(high)) {
                        hourStatItemService.updateHourStatItem(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), maxMap);
                    }

                    //和上面同理，保存最低在线人数
                    hourstatitem = hourStatItemService.getHourstatitemByPk(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                    String low = String.valueOf(MirrorUtil.getValue(Hourstatitem.class, hourstatitem, "count" + hour));
                    if (min < Integer.parseInt(low)) {
                        hourStatItemService.updateHourStatItem(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D), minMap);
                    }
                }

                max = 0;
                min = 0;

                Hourstatitem maxStatItem = hourStatItemService.getHourstatitemByPk(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));
                Hourstatitem minStatItem = hourStatItemService.getHourstatitemByPk(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM_D));

                for (int i = 1; i <= 24; i++) {
                    int maxValue = Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, maxStatItem, "count" + (i)).toString());
                    int minValue = Integer.parseInt(MirrorUtil.getValue(Hourstatitem.class, minStatItem, "count" + (i)).toString());
                    if (maxValue > max) {
                        max = maxValue;
                    }
                    if (minValue < min) {
                        min = minValue;
                    }
                }
                //保存天的数据
                int day = DateUtil.getSpecificTime(date, DateUtil.DAY_OF_MONTH);
                maxMap.clear();
                minMap.clear();
                maxMap.put("count" + day, max);
                minMap.put("count" + day, min);
                try {
                    dayStatItemService.saveDayStatItem(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM), maxMap);
                    dayStatItemService.saveDayStatItem(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM), minMap);
                } catch (Exception e) { //异常表示数据库已经有数据，更新数据即可
                    Daystatitem daystatitem = dayStatItemService.getDaystatitemByPk(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM));

                    String high = String.valueOf(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + day));
                    if (max > Integer.parseInt(high)) {
                        dayStatItemService.updateDayStatItem(cp, "Analysis", "VodPlay", "Highest", DateUtil.getSpecificTime(date, DateUtil.YY_MM), maxMap);
                    }
                    daystatitem = dayStatItemService.getDaystatitemByPk(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM));

                    String low = String.valueOf(MirrorUtil.getValue(Daystatitem.class, daystatitem, "count" + hour));
                    if (min < Integer.parseInt(low)) {
                        dayStatItemService.updateDayStatItem(cp, "Analysis", "VodPlay", "Lowest", DateUtil.getSpecificTime(date, DateUtil.YY_MM), minMap);
                    }
//                        StringUtil.println(String.format("Object %s already exists in the database . Because primary key constraint violation , So the object will be updated", Daystatitem.class.getSimpleName()));
                }

            }

//                if ("UploadStatService".equals(method)) {
//                    String cp = stat.elementText("cpid");
//                    String taskId = stat.elementText("taskId");
//                    String begin = stat.elementText("beginDate");
//                    String end = stat.elementText("endDate");
//                    String ip = stat.elementText("ip");
//                    String dataFlow = stat.elementText("dataFlow");
//                    uploadStatService.saveUploadStat(cp, taskId, begin, end, ip, dataFlow);
//                }
        }
//        String batch = "DELETE FROM broadbandtemp WHERE DAYOFMONTH(dt) <> DAYOFMONTH(NOW())";
//        broadBandStatService.executeSql(batch);
//        batch = "DELETE FROM livetemp WHERE DAYOFMONTH(dt) <> DAYOFMONTH(NOW())";
//        broadBandStatService.executeSql(batch);
//        batch = "DELETE FROM vodtemp WHERE DAYOFMONTH(dt) <> DAYOFMONTH(NOW())";
//        broadBandStatService.executeSql(batch);
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

    private void saveBroadBand(double broadband, String cp, String date, String ip) {
        try {
            broadband = StringUtil.div(broadband, 1048576d, 4);

            //保存带宽统计数据
//                if (broadband != 0)
            broadBandStatService.executeSql("INSERT INTO broadbandtemp(cp,dt,ip,bb)  VALUES(?,?,?,?)", new Object[]{cp, date, ip, broadband});
            String s[] = DateUtil.getBeginEnd().split(",");
            List list = broadBandStatService.getBroadbandstatListBySql("SELECT ip,SUM(bb),COUNT(bb)  FROM broadbandtemp WHERE cp = ? and (dt BETWEEN ? AND ? ) GROUP BY ip", cp, s[0], s[1]);
            double count = 0;
            if (list != null && list.size() > 0) {
                for (Object aList : list) {
                    Object[] objects = (Object[]) aList;
                    String value = objects[1].toString();
                    String i = objects[2].toString();
                    count += StringUtil.div(Double.valueOf(value), Double.valueOf(i), 4);
                }
            }
            broadBandStatService.saveBroadbandstat(cp, "Media", s[1], count);
            List l = broadBandStatService.getBroadbandstatListBySql("SELECT BroadBand ,DateTime FROM broadbandstat WHERE (DateTime  BETWEEN ? AND ?) AND cp = ? AND type='Media'", DateUtil.getCurrentTime(DateUtil._YY_MM_DD) + " 00:00:00", DateUtil.getCurrentTime(DateUtil._YY_MM_DD) + " 23:59:59", cp);
            /**
             * 每天的带宽信息还要另外保存到HourStatItem表里，加快查询带宽统计跨几天甚至一个月的数据查询速度
             */
            List lis = broadBandStatService.getBroadbandstatListBySql("SELECT * FROM broadbanddaystat WHERE  cp = ? and period =? and type = ?", new Object[]{cp, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), "Media"});
            Object[] broad = null;
            if (lis != null && lis.size() > 0) {
                broad = (Object[]) lis.get(0);
            }
            double dayMax = 0;
            double dayAvg = 0;
            double dayTotal = 0;
            String dayMaxTime = null;
            if (l != null && l.size() > 0) {
                for (Object aL : l) {
                    Object[] objects = (Object[]) aL;
                    double value = Double.parseDouble(objects[0].toString());
                    dayTotal += value;
                    if (value > dayMax) {
                        dayMax = value;
                        dayMaxTime = objects[1].toString();
                    }
                }
                dayAvg = StringUtil.div(dayTotal, (double) l.size(), 4);
            }
            if (dayMaxTime != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug(dayMaxTime + " 发现最大值：" + dayMax);
                }
                dayMaxTime = dayMaxTime.replace(".0", "");
                dayMaxTime = dayMaxTime.split(" ")[1];
            } else {
                dayMaxTime = "";
            }

            if (broad == null) {
                broadBandStatService.executeSql("INSERT INTO broadbanddaystat VALUES(?,?,?,?,?,?,?,?)", cp, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), "Media", dayMaxTime, dayMax, dayAvg, dayTotal, 1);
            } else {
                broadBandStatService.executeSql("UPDATE broadbanddaystat SET max_time = ?, max_value = ?, avg_value = ? ,  total = ? , total_add = ? WHERE period = ? AND type='Media' AND cp=? ", dayMaxTime, dayMax, dayAvg, dayTotal, 1, DateUtil.getSpecificTime(date, DateUtil._YY_MM_DD), cp);
            }
        } catch (Exception e) {
            logger.error("saveBroadBand Error", e);
        }
    }
}
