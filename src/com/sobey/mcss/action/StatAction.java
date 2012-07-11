package com.sobey.mcss.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sobey.common.util.PropertiesUtil;
import com.sobey.common.util.R;
import com.sobey.common.util.StringUtil;
import com.sobey.common.util.HttpUtil;
import com.sobey.mcss.service.BroadBandStatService;
import com.sobey.mcss.service.DayStatItemService;
import com.sobey.mcss.service.HourStatItemService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yanggang.
 * Date: 11-1-19
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"deprecation"})
@Controller
@Results(@Result(name = "inputStream", type = "stream", params = {"contentType", "text/html;charset=utf-8", "inputName", "inputStream"}))
public class StatAction extends ActionSupport {
    private String cpStr;
    private String typeStr;
    private String item;

    private InputStream inputStream;

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Autowired
    private DayStatItemService dayStatItemService;

    @Autowired
    private HourStatItemService hourStatItemService;

    @Autowired
    private BroadBandStatService broadBandStatService;

    public void setCpStr(String cpStr) {
        this.cpStr = cpStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String refresh() {
        String message = "刷新失败，请检查刷新地址格式是否正确";
        int invalid = 0;
        boolean success = false;
        try {
            String http[] = item.split("\r\n");
            for (int i = 0; i < http.length; i++) {
                if (http[i].startsWith("http")) {
                    HttpUtil.sendGet(http[i], null);
                    success = true;
                } else {
                    invalid++;
                }
            }
            if (success) {
                message = "刷新成功";
                if (invalid != 0) {
                    message += "，有" + invalid + "条无效的地址未刷新，请检查后重试";
                }
            }
            inputStream = new ByteArrayInputStream(java.net.URLEncoder.encode(message, "UTF-8").getBytes());
        } catch (Exception e) {
            try {
                inputStream = new ByteArrayInputStream(java.net.URLEncoder.encode(message, "UTF-8").getBytes());
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return "inputStream";
    }

    public String getLog() {
        try {
            String url = PropertiesUtil.getString("LOGFILE.URL");
            File file = new File(url);
            if (!file.exists()) {
                file.mkdir();
            }
            JSONArray jsonArray = new JSONArray();
            for (File log : file.listFiles()) {
                if (log.isFile()) {
                    String[] str = log.getName().split("_");
                    if (str.length == 5) {
                        if (str[0].equals(cpStr) && str[3].equals(typeStr)) {
                            String cp = str[0];
                            String type = str[3];
                            String date = str[4].split("\\.")[0];
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("date", date);
                            jsonObject.put("url", "/log/" + log.getName());
                            jsonObject.put("name", log.getName());
                            jsonArray.add(jsonObject);
                        }
                    }
                }
            }
            inputStream = new ByteArrayInputStream(jsonArray.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }

    public String getLogCp() {
        try {
            String url = PropertiesUtil.getString("LOGFILE.URL");
            StringBuffer result = null;
            File file = new File(url);
            if (!file.exists()) {
                file.mkdir();
            }
            for (File cp : file.listFiles()) {
                if (cp.isFile()) {
                    String[] str = cp.getName().split("_");
                    if (result != null) {
                        result.append("|").append(str[0]);
                    } else {
                        result = new StringBuffer();
                        result.append(str[0]);
                    }
                }
            }
            if (result == null) {
                result = new StringBuffer();
            }
            inputStream = new ByteArrayInputStream(result.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
             inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }
}
