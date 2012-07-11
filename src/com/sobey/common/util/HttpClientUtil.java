package com.sobey.common.util;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class HttpClientUtil {

    private static final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

    public static String doPost(String url, String method, String data) throws HttpException,
            IOException {
        HttpMethod post = getPostMethod(url, method, data);
        String str = null;
        if (post == null) {
            return "error";
        }
        HttpClient httpclient = new HttpClient(connectionManager);
        int result;
        result = httpclient.executeMethod(post);
        if (result == HttpStatus.SC_OK) {
            str = post.getResponseBodyAsString();
        }
        post.releaseConnection();
        return str;
    }

    private static HttpMethod getPostMethod(String url, String method, String data) {
        // RequestEntity entity = new StringRequestEntity(data, "text/xml",
        // "utf-8");
        PostMethod post = new UTF8PostMethod(url);
        // 设置默认恢复策略，发生异常时自动重试3次
        post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        // post.setRequestHeader("Content-type", "text/xml;charset=UTF-8");
//		post.setRequestHeader("Cookie", headerValue)
//		post.setRequestHeader("Cookie", "name=admin; uid=4; usertoken=Dou++CmCkp86OPlEfrJJYjPdfMqbNJmJBmUiP0U3HvHMa9D6UlbkNoA2jvmxBjaYg+BD4EykEciuYTe64hlAm/rKWTZJvCu9");

        post.setRequestHeader("Content-type",
                "application/x-www-form-urlencoded;charset=UTF-8");
        // post.setRequestEntity(entity);
        NameValuePair n1 = new NameValuePair("method", method);
        NameValuePair n2 = new NameValuePair("LiveStatRequest", data);

        NameValuePair[] parametersBody = new NameValuePair[2];
        parametersBody[0] = n1;
        parametersBody[1] = n2;

        post.setRequestBody(parametersBody);
        return post;
    }

    public static class UTF8PostMethod extends PostMethod {
        public UTF8PostMethod(String url) {
            super(url);
        }

        @Override
        public String getRequestCharSet() {
            // return super.getRequestCharSet();
            return "UTF-8";
        }
    }

    public static void main(String args[]) {
//		String str = "{'ChannelId':0,'IsGetNew':0,'Limit':0,'Name':null,'Type':0}";
//		String str = "{'NotGetAllData':0,'ParentId':0}";	// ListActsc
//		String str = "{'Name':'hello','Passwd':'hello'}"; //MemberLogServers
//		String str = "{'MemberId':21,'ChannelId':531368099,'Type':1}";
//		GetActsData
//		String str = "{'Result':1}";

        String str = "<root> <stat> <cpid>10.10.10.99</cpid> <date>2011-02-24 16:11:19</date> <type>live</type> <channels> <channel> <name>419000fdaf</name> <onliveCount>1</onliveCount> </channel> </channels> <broadband>319752</broadband></stat>";  //GetActsData

//		String str = "'{'Id':15}'"; //MemberLogServers
        String back;
        try {
            back = HttpClientUtil.doPost(
                    "http://127.0.0.1:8080/StatService", "LiveStatService", str);
            System.out.println(back);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
