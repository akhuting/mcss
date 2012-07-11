package com.sobey.mcss.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sobey.common.util.R;
import com.sobey.mcss.domain.Cp;
import com.sobey.mcss.domain.Userinfo;
import com.sobey.mcss.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-2-28
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"deprecation"})
@Controller
@Results(@Result(name = "inputStream", type = "stream", params = {"contentType", "text/html;charset=utf-8", "inputName", "inputStream"}))
public class CpAction extends ActionSupport {
    private InputStream inputStream;
    private String type;
    private String subType;
    private String item;
    private String field;
    private String table = "daystatitem";

    public void setTable(String table) {
        this.table = table;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setSubType(String subType) {
        this.subType = subType;
    }


    public void setItem(String item) {
        this.item = item;
    }


    @Autowired
    private DayStatItemService dayStatItemService;

    @Autowired
    private CpService cpService;

    public String field() {
        try {
            List list = null;
            StringBuffer sql = new StringBuffer("SELECT cp from cp ");
            ActionContext ac = ActionContext.getContext();
            Userinfo user = (Userinfo) ac.getSession().get("user");
            if (user.getUserStatus() != 1) {
                sql.append(" where id in(select cpid from user_cp where userid=").append(user.getUserid()).append(") ");
            }
            list = dayStatItemService.getDaystatitemListBySql(sql.toString());
            StringBuffer result = new StringBuffer();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        result.append("|").append(list.get(i));
                    } else {
                        result.append(list.get(i));
                    }
                }
            }
            inputStream = new ByteArrayInputStream(java.net.URLEncoder.encode(result.toString(), "UTF-8").getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            inputStream = new ByteArrayInputStream(e.getMessage().getBytes());
        }
        return "inputStream";
    }

    public String allCp() {
        try {
            String result = "0";
            JSONArray jsonArray = new JSONArray();
            List<Cp> cps = cpService.getAll();
            if (cps != null && cps.size() > 0) {
                for (int i = 0; i < cps.size(); i++) {
                    Cp cp = cps.get(i);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", cp.getCp());
                    jsonObject.put("id", cp.getId());
                    jsonArray.add(jsonObject);
                }
                result = jsonArray.toString();
            }
            inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "inputStream";
    }

    public String saveCp() {

        try {
            String dels[] = field.split(",");
            if (dels.length > 1) {
                List<Cp> list = new ArrayList<Cp>();
                for (int i = 0; i < dels.length; i++) {
                    Cp cp = new Cp();
                    if (!dels[i].equals("0")) {
                        cp.setId(Integer.parseInt(dels[i]));
                    }
                    list.add(cp);
                }
                cpService.delete(list);
            }
            List<Cp> list = new ArrayList<Cp>();
            String cps[] = item.split(",");
            for (int i = 0; i < cps.length; i++) {
                String str[] = cps[i].split("_");
                Cp cp = new Cp();
                if (!str[1].equals("0")) {
                    cp.setId(Integer.parseInt(str[1]));
                }
                cp.setCp(str[0]);
                list.add(cp);
            }

            cpService.saveCp(list);
            inputStream = new ByteArrayInputStream("1".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            inputStream = new ByteArrayInputStream("0".getBytes());
        }
        return "inputStream";
    }


        public String channel() {
        try {
            List list = null;
            StringBuffer sql = new StringBuffer("select ");
            sql.append(field);
            sql.append(" from ");
            sql.append(table);
            sql.append(" where 1=1 ");
            if (type != null) {
                sql.append(" and type = '").append(type).append("'");
            }
            if (subType != null) {
                String[] subtypes = subType.split(",");
                if (subtypes.length > 1) {
                    sql.append(" and (");
                    for (int i = 0; i < subtypes.length; i++) {
                        if (i == 0) {
                            sql.append("subtype = '").append(subtypes[i]).append("' ");
                        } else {
                            sql.append(" or subtype = '").append(subtypes[i]).append("' ");
                        }
                    }
                    sql.append(")");
                } else {
                    sql.append(" and subtype = '").append(subType).append("'");
                }
            }
            if (item != null) {
                String[] items = item.split(",");
                if (items.length > 1) {
                    sql.append(" and (");
                    for (int i = 0; i < items.length; i++) {
                        if (i == 0) {
                            sql.append("item = '").append(items[i]).append("' ");
                        } else {
                            sql.append(" or item = '").append(items[i]).append("' ");
                        }
                    }
                    sql.append(")");
                } else {
                    sql.append(" and item = '").append(item).append("'");
                }
            }
            sql.append(" group by ");
            sql.append(field);
            list = dayStatItemService.getDaystatitemListBySql(sql.toString());
            StringBuffer result = new StringBuffer();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        result.append("|").append(list.get(i));
                    } else {
                        result.append(list.get(i));
                    }
                }
            }
            inputStream = new ByteArrayInputStream(java.net.URLEncoder.encode(result.toString(), "UTF-8").getBytes());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            inputStream = new ByteArrayInputStream(e.getMessage().getBytes());
        }
        return "inputStream";
    }

}
