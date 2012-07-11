package com.sobey;

import com.sobey.common.util.*;
import com.sobey.mcss.domain.Daystatitem;
import com.sobey.mcss.service.*;
import net.sf.json.JSONArray;
import org.dom4j.DocumentException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;


/**
 * Created by Yanggang.
 * Date: 11-1-8
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    static ApplicationContext content = new ClassPathXmlApplicationContext("spring/spring-config.xml");
    static DayStatItemService dayStatItemService = (DayStatItemService) content.
            getBean("dayStatItemService");
    static HourStatItemService hourStatItemService = (HourStatItemService) content.getBean("hourStatItemService");

    static UploadStatService uploadStatService = (UploadStatService) content.getBean("uploadStatService");
    static IpDayStatItemService ipDayStatItemService = (IpDayStatItemService) content.getBean("ipDayStatItemService");
    static UrlDayStatItemService urlDayStatItemService = (UrlDayStatItemService) content.getBean("urlDayStatItemService");
    static BroadBandStatService broadBandStatService = (BroadBandStatService) content.getBean("broadBandStatService");

    public static void main(String args[]) throws DocumentException {


        ArrayList<String> list = new ArrayList<String>();
        list.add("t.video.sobeycache.com");
        list.add("livexj.sobeycache.com");
        list.add("vodxj.sobeycache.com");

//                            saveHourStatItem("t.video.sobeycache.com","Flow","AreaWeb","MDN","20111112");    //网页加速流量
//                            saveHourStatItem("livexj.sobeycache.com","Flow","Media","MDN","20111111");    //网页加速流量
//                            saveHourStatItem("t.video.sobeycache.com","Flow","Media","四川省","20111112");    //网页加速流量
//             saveHourStatItem("livexj.sobeycache.com","LiveWatch","Highest","测试频道","20111112");
//             saveHourStatItem("livexj.sobeycache.com","LiveWatch","Lowest","测试频道","20111112");
//             writeDayStatItem("livexj.sobeycache.com","Live","5Min","成都直播流","201111");
//             writeDayStatItem("livexj.sobeycache.com","Live","10Min","成都直播流","201111");
//             writeDayStatItem("livexj.sobeycache.com","Live","30Min","成都直播流","201111");
//             writeDayStatItem("livexj.sobeycache.com","Live","60Min","成都直播流","201111");
//             writeDayStatItem("livexj.sobeycache.com","Live","NMin","成都直播流","201111");
//            writeDayStatItem("livexj.sobeycache.com","LiveArea","成都直播流" , "新疆省","201111");
//
//             saveHourStatItem("vodxj.sobeycache.com","Analysis","VodPlay","Highest","20111112");
//             saveHourStatItem("vodxj.sobeycache.com","Analysis","VodPlay","Lowest","20111112");
//            writeDayStatItem("vodxj.sobeycache.com","Analysis","Vod" , "5Min","201111");
//            writeDayStatItem("vodxj.sobeycache.com","Analysis","Vod" , "10Min","201111");
//            writeDayStatItem("vodxj.sobeycache.com","Analysis","Vod" , "30Min","201111");
//            writeDayStatItem("vodxj.sobeycache.com","Analysis","Vod" , "60Min","201111");
//            writeDayStatItem("vodxj.sobeycache.com","Analysis","Vod" , "NMin","201111");
            writeDayStatItem("vodxj.sobeycache.com","Analysis","VodArea" , "新疆省","201111");


    }

    public static void webBroad() {
        //流媒体:Media 网页:Web 上传:Upload
        Random random = new Random();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(2011, 10, 12, 0, 0, 0);
        while (compareCalendar.get(Calendar.DATE) == 12) {
            broadBandStatService.saveBroadbandstat("t.video.sobeycache.com", "Media", DateUtil.getSpecificTime(compareCalendar.getTime(), DateUtil._YY_MM_DD_TIME), Math.abs(random.nextInt()) % 55);
            compareCalendar.add(Calendar.HOUR, 1);
        }

    }

    public static void modifyDayStatItem(String cp, String type, String subType, String item, String date) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < 31; i++) {
            map.put("count" + (i + 1),
                    R.randomInt(R.randomInt(1)));
        }
        for (Map.Entry<String, Integer> m : map.entrySet()) {
            System.out.println("key:" + m.getKey() + ",value:" + m.getValue());
        }
        dayStatItemService.updateDayStatItem(cp, type, subType, item, date);
    }

    public static void writeDayStatItem(String cp, String type, String subType, String item) {
        dayStatItemService.saveDayStatItem(cp, type, subType, item);
    }

    public static void writeDayStatItem(String cp, String type, String subType, String item, String date) {
        dayStatItemService.saveDayStatItem(cp, type, subType, item, date);
    }

    public static List<Daystatitem> get(String cp, String type, String subType, String item, String... date) {
        return dayStatItemService.getDaystatitemList(cp, type, subType, item, date);
    }

    public static void saveHourStatItem(String cp, String type, String subType, String item, String date) {
        hourStatItemService.saveHourStatItem(cp, type, subType, item, date);
    }
}
