package com.sobey.mcss.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.PropertiesUtil;
import com.sobey.common.util.HttpUtil;
import com.sobey.common.util.StringUtil;
import com.sobey.mcss.domain.Cp;
import com.sobey.mcss.domain.Userinfo;
import com.sobey.mcss.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Yanggang.
 * Date: 11-1-19
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"deprecation"})
@Controller
@Results(@Result(name = "inputStream", type = "stream", params = {"contentType", "text/html;charset=utf-8", "inputName", "inputStream"}))
public class StatAction extends ActionSupport implements ServletRequestAware {
    private String cpStr;
    private String typeStr;
    private String item;
    private String begin;
    private String end;

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

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String refresh() {
        String message = "刷新失败，请检查刷新地址格式是否正确";
        int invalid = 0;
        boolean success = false;
        try {
            String http[] = item.split("\r\n");
            for (String aHttp : http) {
                if (aHttp.startsWith("http")) {
                    String host = StringUtil.getHost(aHttp);
                    String url = StringUtil.getURI(aHttp);
                    success = HttpUtil.varnishPurge(host, url);
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
            ArrayList list = new ArrayList();
            for (File log : file.listFiles()) {
                if (log.isFile()) {
                    String[] str = log.getName().split("_");
                    if (str.length == 3) {
                        boolean hasCp = false;
                        boolean validate = false;
                        try {
                            validate = validate(str[2]);
                            hasCp = hasCp(str[0]);
                        } catch (Exception ignored) {
                        }
                        if (hasCp && validate && str[1].equals(typeStr)) {
                            String date = str[2].replace(".log", "");
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("date", date);
                            jsonObject.put("url", "/log/" + log.getName());
                            jsonObject.put("name", log.getName());
                            jsonObject.put("size", StringUtil.FormetFileSize(StringUtil.getFileSizes(log)));
//                            jsonArray.add(jsonObject);
                            list.add(jsonObject);
                        }
                    }
                }
            }
            Collections.sort(list, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    JSONObject j1 = (JSONObject) o1;
                    JSONObject j2 = (JSONObject) o2;
                    int year = Integer.parseInt(j1.get("date").toString().substring(0, 4));
                    int month = Integer.parseInt(j1.get("date").toString().substring(4, 6));
                    int day = Integer.parseInt(j1.get("date").toString().substring(6, 8));
                    Calendar j1Calendar = Calendar.getInstance();
                    j1Calendar.set(year, month - 1, day);

                    year = Integer.parseInt(j2.get("date").toString().substring(0, 4));
                    month = Integer.parseInt(j2.get("date").toString().substring(4, 6));
                    day = Integer.parseInt(j2.get("date").toString().substring(6, 8));
                    Calendar j2Calendar = Calendar.getInstance();
                    j2Calendar.set(year, month - 1, day);

                    long diff = j1Calendar.getTime().getTime() - j2Calendar.getTime().getTime();
                    if (diff > 0)
                        return 1;
                    else if (diff == 0)
                        return 0;
                    else
                        return -1;
                }
            });
            jsonArray = JSONArray.fromObject(list);
            inputStream = new ByteArrayInputStream(jsonArray.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }

    private boolean validate(String date) {

        /*
          判断日志是否在查询之内
         */
        date = date.replace(".log", "");
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));

        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.set(year, month - 1, day);
        beginCalendar.set(DateUtil.getSpecificTime(begin, DateUtil.YEAR), DateUtil.getSpecificTime(begin, DateUtil.MONTH), DateUtil.getSpecificTime(begin, DateUtil.DAY_OF_MONTH));
        endCalendar.set(DateUtil.getSpecificTime(end, DateUtil.YEAR), DateUtil.getSpecificTime(end, DateUtil.MONTH), DateUtil.getSpecificTime(end, DateUtil.DAY_OF_MONTH));

        long add = (endCalendar.getTime().getTime() - beginCalendar.getTime().getTime()) / (24 * 60 * 60 * 1000);
        return beginCalendar.before(compareCalendar) && endCalendar.after(compareCalendar) || compareCalendar.equals(beginCalendar) || compareCalendar.equals(endCalendar);
    }

    private boolean hasCp(String cp) {


        /*
         1，判断查询的cp是否是根域名，如果是，循环根域名下的所有子域名判断有没有和当前查到文件cp相等的，有就返回true
         2，如果不是，直接循环所有域名，查找有没有和当前找到的文件cp相等的，有就返回true
         3，都没返回的话，直接返回false
         */
        List<Cp> cps = getAllCp();
        for (Cp _cp : cps) {
            if (_cp.getCp().equals(cpStr) && _cp.getPid() == 0) {
                for (Cp subCp : cps) {
                    if (subCp.getPid() == _cp.getId()) {
                        if (subCp.getCp().equals(cp)) {
                            return true;
                        }
                    }
                }
            }
            if (_cp.getCp().equals(cp) && cp.equals(cpStr)) {
                return true;
            }
        }
        return false;
    }

    public String getLogCp() {
        try {

            StringBuffer result = null;

            for (Cp cp : getAllCp()) {
                if (result != null) {
                    result.append("|").append(cp.getCp());
                } else {
                    result = new StringBuffer();
                    result.append(cp.getCp());
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

    private List<Cp> getAllCp() {
        Userinfo userinfo = (Userinfo) request.getSession().getAttribute("user");
        if (userinfo.getUserStatus() == 1) {
            return cpService.getAll();
        }
        return userService.getUserById(userinfo.getUserid()).get(0).getCps();
    }

    @Autowired
    private UserService userService;

    @Autowired
    private CpService cpService;


    private HttpServletRequest request;

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }
}
