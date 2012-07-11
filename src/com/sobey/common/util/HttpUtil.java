package com.sobey.common.util;

import sun.rmi.runtime.NewThreadAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AlexYang
 * Date: 11-3-18
 * Time: 下午3:47
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtil {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */

    public static String sendGet(String url, String param) throws Exception {
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        try {
            String urlName = url + "?" + param;
            URL realUrl = new URL(urlName);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; SV1)");
            //建立实际的连接
            conn.connect();
            //获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            //遍历所有的响应头字段
            for(Iterator e = map.keySet().iterator();e.hasNext();){
               System.out.println(e.next().toString() + "--->" + map.get(e.next().toString()));
            }
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append("n" + line);
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
            throw new Exception(e.getMessage());

        }
        //使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C)");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            out.print(param);
            //flush输出流的缓冲
            out.flush();
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    //提供主方法，测试发送GET请求和POST请求
    public static void main(String args[]) {
        //发送GET请求
//        String s = HttpUtil.sendGet("http://172.16.145.21:8088/vms-cms", null);
//        System.out.println(s);

        HttpUtil.sendPost("http://localhost:8080/StatService", "<root><stat><cpid>10.10.10.99</cpid> <date>2011-03-29 23:59:00</date> <type>live</type> <channels> <channel> <name>测试频道</name> <onliveCount>2</onliveCount> </channel> </channels> <broadband>319752</broadband></stat><stat>        <cpid>t.video.sobeycache.com</cpid>        <date>2011-04-02 12:52:02</date>        <type>live</type>        <channels>        </channels>        <broadband>0</broadband></stat></root>");
//        HttpUtil.sendPost("http://localhost:8080/StatService","<root><stat><cpid>10.10.10.99</cpid> <date>2011-03-29 16:11:19</date> <type>vod</type> <broadband>599096</broadband> <playCount>1</playCount></stat></root>");
    }
}
