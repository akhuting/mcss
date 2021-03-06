package com.sobey;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.dom4j.DocumentException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


/**
 * Created by Yanggang.
 * Date: 11-1-8
 * Time: 下午11:53
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String args[]) throws DocumentException {
//        boolean b = true;
//        if (b) {
//            HttpUtil.sendPost("http://localhost:8080/StatService", "<root>\n" +
//                    "    <stat>\n" +
//                    "        <cpid>vodhyw.sobeycache.com</cpid>\n" +
//                    "        <date>2012-12-05 17:29:52</date>\n" +
//                    "        <type>vod</type>\n" +
//                    "        <broadband>6078512</broadband>\n" +
//                    "        <playCount>2</playCount>\n" +
//                    "    </stat>\n" +
//                    "</root>");
//        } else {
//            HttpUtil.sendPost("http://localhost:8080/StatService", "<root>\n" +
//                    "    <stat>\n" +
//                    "        <cpid>vodhyw.sobeycache.com</cpid>\n" +
//                    "        <date>2012-12-12 17:53:52</date>\n" +
//                    "        <type>live</type>\n" +
//                    "        <channels>\n" +
//                    "            <channel>\n" +
//                    "                <name>1</name>\n" +
//                    "                <onliveCount>1</onliveCount>\n" +
//                    "            </channel>\n" +
//                    "            <channel>\n" +
//                    "                <name>XJTV-10</name>\n" +
//                    "                <onliveCount>1</onliveCount>\n" +
//                    "            </channel>\n" +
//                    "        </channels>\n" +
//                    "        <broadband>3078512</broadband>\n" +
//                    "    </stat>    \n" +
//                    "</root>");
//        }
//         socket("113.142.30.222",80);
//        System.out.println(StringUtil.getURI("http://cn.bing.com/aaa.jso"));
//        System.out.println(IPUtil.getInstance().getAddress("61.136.5.7"));
//        String date = "20121103.log";
//        String begin = "2012-11-01";
//        String end = "2012-12-30";
//
//        date = date.replace(".log", "");
//        int year = Integer.parseInt(date.substring(0, 4));
//        int month = Integer.parseInt(date.substring(4, 6));
//        int day = Integer.parseInt(date.substring(6, 8));
//
//        Calendar beginCalendar = Calendar.getInstance();
//        Calendar endCalendar = Calendar.getInstance();
//        Calendar compareCalendar = Calendar.getInstance();
//        compareCalendar.set(year, month-1, day);
//        beginCalendar.set(DateUtil.getSpecificTime(begin, DateUtil.YEAR), DateUtil.getSpecificTime(begin, DateUtil.MONTH), DateUtil.getSpecificTime(begin, DateUtil.DAY_OF_MONTH));
//        endCalendar.set(DateUtil.getSpecificTime(end, DateUtil.YEAR), DateUtil.getSpecificTime(end, DateUtil.MONTH), DateUtil.getSpecificTime(end, DateUtil.DAY_OF_MONTH));
//
//        long add = (endCalendar.getTime().getTime()-beginCalendar.getTime().getTime())/(24*60*60*1000);
//        if(beginCalendar.before(compareCalendar)&&endCalendar.after(compareCalendar)){
//            System.out.println("yes");
//        }else{
//            System.out.println("no");
//        }

        Client client = Client.create();
        String url = "http://localhost:8080/api/cp";
        WebResource resource = client.resource(url);
        MultivaluedMap<String, String> param = new MultivaluedMapImpl();
        param.add("name", "cctv.v.sobeycache.com");
        param.add("alias", "cdtv");
        param.add("domain", "v.sobeycache.com");
//        String result = resource.queryParams(param).delete(String.class);
        String result = resource.queryParams(param).type(MediaType.APPLICATION_FORM_URLENCODED).post(String.class);
        System.out.println(result);
    }

    private static String encoding = "GBK";

    public static void socket(String ip, int port) {
        try {
            Socket s = new Socket(ip, port);
            OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append("PURGE /E-WebTVyszd/upload/Image/default/2012/11/28/89a4302582a74d6c9f385379809ffd18/ HTTP/1.0\r\n");
            sb.append("Host: image.sobeycache.com\r\n");
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

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
