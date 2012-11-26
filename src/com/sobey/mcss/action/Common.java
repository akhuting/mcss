package com.sobey.mcss.action;

import com.sobey.mcss.domain.Userinfo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-4-7
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
public class Common {

    public static Map<String, String> getChartMap() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("CN.SC", "四川");
        maps.put("CN.QH", "青海");
        maps.put("CN.SH", "上海");
        maps.put("CN.JS", "江苏");
        maps.put("CN.GD", "广东");
        maps.put("CN.BJ", "北京");
        maps.put("CN.LN", "辽宁");
        maps.put("CN.XJ", "新疆");
        maps.put("CN.GS", "甘肃");
        maps.put("CN.XZ", "西藏");
        maps.put("CN.NM", "内蒙");
        maps.put("CN.YN", "云南");
        maps.put("CN.NX", "宁夏");
        maps.put("CN.SA", "陕西");
        maps.put("CN.CQ", "重庆");
        maps.put("CN.GZ", "贵州");
        maps.put("CN.SX", "山西");
        maps.put("CN.HE", "河南");
        maps.put("CN.HU", "湖北");
        maps.put("CN.HN", "湖南");
        maps.put("CN.GX", "广西");
        maps.put("CN.HB", "河北");
        maps.put("CN.TJ", "天津");
        maps.put("CN.SD", "山东");
        maps.put("CN.AH", "安徽");
        maps.put("CN.JX", "江西");
        maps.put("CN.GD", "广东");
        maps.put("CN.HA", "海南");
        maps.put("CN.ZJ", "浙江");
        maps.put("CN.FJ", "福建");
        maps.put("CN.TA", "台湾");
        maps.put("CN.HL", "黑龙江");
        maps.put("CN.JL", "吉林");
        maps.put("CN.MA", "澳门");
        maps.put("CN.HK", "香港");
        return maps;
    }

    public static int getCN(HttpServletRequest request) {
        Userinfo userinfo = (Userinfo) request.getSession().getAttribute("user");
        if (userinfo != null) {
            return userinfo.getUserCn();
        } else {
            return 1;
        }
    }
}
