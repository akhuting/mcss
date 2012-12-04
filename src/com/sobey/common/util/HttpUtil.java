package com.sobey.common.util;

import sun.rmi.runtime.NewThreadAction;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
            for (Iterator e = map.keySet().iterator(); e.hasNext(); ) {
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
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
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

    public static boolean varnishPurge(String host, String url) {
        boolean success = false;
        try {
            String ip = PropertiesUtil.getString("VARNISH.IP");
            String port = PropertiesUtil.getString("VARNISH.PORT");
            Socket s = new Socket(ip, Integer.parseInt(port));
            OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append("PURGE " + url + " HTTP/1.0\r\n");
            sb.append("Host: " + host + "\r\n");
            sb.append("Connection: close\r\n");
            //注，这是关键的关键，忘了这里让我搞了半个小时。这里一定要一个回车换行，表示消息头完，不然服务器会等待
            sb.append("\r\n");
            osw.write(sb.toString());
            osw.flush();

            //--输出服务器传回的消息的头信息
            InputStream is = s.getInputStream();
            String line = null;
            int contentLength = 0;//服务器发送回来的消息长度
            // 读取所有服务器发送过来的请求参数头部信息
            do {
                line = readLine(is, 0);
                //如果有Content-Length消息头时取出
                if (line.startsWith("Content-Length")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
                //打印请求部信息
                System.out.print(line);
                //如果遇到了一个单独的回车换行，则表示请求头结束
            } while (!line.equals("\r\n"));

            //--输消息的体
            System.out.print(readLine(is, contentLength));

            //关闭流
            is.close();
            success = true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * 这里我们自己模拟读取一行，因为如果使用API中的BufferedReader时，它是读取到一个回车换行后
     * 才返回，否则如果没有读取，则一直阻塞，直接服务器超时自动关闭为止，如果此时还使用BufferedReader
     * 来读时，因为读到最后一行时，最后一行后不会有回车换行符，所以就会等待。如果使用服务器发送回来的
     * 消息头里的Content-Length来截取消息体，这样就不会阻塞
     *
     * contentLe 参数 如果为0时，表示读头，读时我们还是一行一行的返回；如果不为0，表示读消息体，
     * 时我们根据消息体的长度来读完消息体后，客户端自动关闭流，这样不用先到服务器超时来关闭。
     */
    private static String readLine(InputStream is, int contentLe) throws IOException {
        ArrayList lineByteList = new ArrayList();
        byte readByte;
        int total = 0;
        if (contentLe != 0) {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
                total++;
            } while (total < contentLe);//消息体读还未读完
        } else {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
            } while (readByte != 10);
        }

        byte[] tmpByteArr = new byte[lineByteList.size()];
        for (int i = 0; i < lineByteList.size(); i++) {
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();
        }
        lineByteList.clear();

        return new String(tmpByteArr, encoding);
    }

    private static String encoding = "GBK";

    //提供主方法，测试发送GET请求和POST请求
    public static void main(String args[]) {
        //发送GET请求
//        String s = HttpUtil.sendGet("http://172.16.145.21:8088/vms-cms", null);
//        System.out.println(s);

        HttpUtil.sendPost("http://localhost:8080/StatService", "<root><stat><cpid>10.10.10.99</cpid> <date>2011-03-29 23:59:00</date> <type>live</type> <channels> <channel> <name>测试频道</name> <onliveCount>2</onliveCount> </channel> </channels> <broadband>319752</broadband></stat><stat>        <cpid>t.video.sobeycache.com</cpid>        <date>2011-04-02 12:52:02</date>        <type>live</type>        <channels>        </channels>        <broadband>0</broadband></stat></root>");
//        HttpUtil.sendPost("http://localhost:8080/StatService","<root><stat><cpid>10.10.10.99</cpid> <date>2011-03-29 16:11:19</date> <type>vod</type> <broadband>599096</broadband> <playCount>1</playCount></stat></root>");
    }
}
