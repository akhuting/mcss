package com.sobey;

import com.sobey.common.util.HttpUtil;
import com.sobey.common.util.IPUtil;
import org.dom4j.DocumentException;


/**
 * Created by Yanggang.
 * Date: 11-1-8
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String args[]) throws DocumentException {
        HttpUtil.sendPost("http://localhost:8080/StatService", "<root>\n" +
                "    <stat>\n" +
                "        <cpid>vodhyw.sobeycache.com</cpid>\n" +
                "        <date>2012-11-27 11:10:52</date>\n" +
                "        <type>vod</type>\n" +
                "        <broadband>6078512</broadband>\n" +
                "        <playCount>2</playCount>\n" +
                "    </stat>\n" +
                "</root>");
//        System.out.println(IPUtil.getInstance().getAddress("216.252.128.1"));

    }

    public static long ipToLong(String ip) {
        long result = 0;
        try {
            String[] strs = ip.split("\\.");
            if (strs.length != 4)
                throw new Exception();
            int[] ips = new int[4];
            for (int i = 0; i < 4; i++) {
                ips[i] = Integer.parseInt(strs[i]);
            }
            for (int i = 0; i < 4; i++) {
                result += ips[i] * Math.pow(256, 3 - i);
            }
        } catch (Exception e) {
        }
        return result;
    }

    /*把long型转换成字符串形式的IP地址*/
    public static String longToIP(long ip) {
        String str = "";
        str += ip % 256;
        ip /= 256;
        str = ip % 256 + "." + str;
        ip /= 256;
        str = ip % 256 + "." + str;
        ip /= 256;
        str = ip % 256 + "." + str;
        return str;
    }
}
