package com.sobey.mcss.action;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.sobey.common.util.DateUtil;
import com.sobey.common.util.HttpUtil;
import com.sobey.common.util.PasswordEncoder;
import com.sobey.mcss.domain.UserCp;
import com.sobey.mcss.domain.Userinfo;
import com.sobey.mcss.service.UserCpService;
import com.sobey.mcss.service.UserService;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Yanggang.
 * Date: 11-1-11
 * Time: 下午7:05
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Results({@Result(name = Action.SUCCESS, location = "index.jsp"),
        @Result(name = "userManager", location = "userManager.jsp"),
        @Result(name = "main", location = "/user!index.action", type = "redirect"),
        @Result(name = "reg", location = "reg.jsp"),
        @Result(name = "edit", location = "edit.jsp"),
        @Result(name = "inputStream", type = "stream", params = {"contentType", "text/html;charset=utf-8", "inputName", "inputStream"})})
public class UserAction extends ActionSupport {
    private String loginName;
    private String loginpassword;
    private String newPassword;
    private InputStream inputStream;
    private String email;
    private String phone;
    private String name;
    private String cp;
    private String result;
    private int id;
    private int rank = 99;
    private String range;
    private List<Userinfo> users;
    @Autowired
    private UserService userService;

    @Autowired
    private UserCpService userCpService;

    public String index() {
        if (ActionContext.getContext().getSession().get("user") == null) {
            return "login";
        }
        return SUCCESS;
    }

    public String login() {
        loginName = loginName.trim().toLowerCase();
        loginpassword = loginpassword.trim().toLowerCase();
        ActionContext ac = ActionContext.getContext();
        loginpassword = PasswordEncoder.encode(loginName, loginpassword);
        Userinfo userinfo = userService.getUser(loginName, loginpassword);
        if (loginName == null && loginpassword == null) {
            addActionError("登录名或密码不能为空! ");
            return "login";
        }
        if (userinfo == null) {
            addActionError("登录名或密码不正确! ");
            return "login";
        }
        userinfo.setLastLogin(DateUtil.getCurrentTime(DateUtil._YY_MM_DD_TIME));
        userService.updateUser(userinfo);
        ac.getSession().put("user", userinfo);
        return "main";
    }


    public void setRank(int rank) {
        this.rank = rank;
    }


    public void setRange(String range) {
        this.range = range;
    }

    public List<Userinfo> getUsers() {
        return users;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginpassword(String loginpassword) {

        this.loginpassword = loginpassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void prepare() throws Exception {
        clearErrorsAndMessages();
    }

    public String getResult() {
        return result;
    }

    public String modify() {

        try {
            loginName = loginName.trim().toLowerCase();
            loginpassword = loginpassword.trim().toLowerCase();
            newPassword = newPassword.trim().toLowerCase();
            loginpassword = PasswordEncoder.encode(loginName, loginpassword);
            newPassword = PasswordEncoder.encode(loginName, newPassword);
            userService.updatePassword(loginName, loginpassword, newPassword);
            HttpServletRequest request = ServletActionContext.getRequest();
            inputStream = new ByteArrayInputStream(("ok('" + request.getContextPath() + "/login!index.action?d=" + System.currentTimeMillis() + "'); ").getBytes("UTF-8"));
            ActionContext ac = ActionContext.getContext();
            ac.getSession().remove("user");
        } catch (Exception e) {
            try {
                inputStream = new ByteArrayInputStream(("$(\"#error\").slideDown(\"slow\");").getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return "inputStream";
    }

    public String forget() {

        try {
            HttpServletRequest request = ServletActionContext.getRequest();
            Userinfo userinfo = userService.getUserByEmail(loginName, email);
            if (userinfo != null) {
                userService.updatePassword(userinfo.getUsername(), userinfo.getPassword(), "sobey");
                inputStream = new ByteArrayInputStream(("art.dialog({ id:\"forget\", skin: 'aero', icon:'succeed', content: '验证成功!密码已重置为sobey,请尽快登陆修改密码', closeFn:function() {window.top.location='" + request.getContextPath() + "/login!index.action?d=" + System.currentTimeMillis() + "';} }); ").getBytes("UTF-8"));
            } else {
                inputStream = new ByteArrayInputStream(("art.dialog({ id:\"forget\", skin: 'aero', icon: 'error', content: '验证失败,请重新输入' });").getBytes("UTF-8"));
            }
        } catch (Exception e) {
            try {
                inputStream = new ByteArrayInputStream(("art.dialog({ id:\"forget\", skin: 'aero', icon: 'error', content:'" + e.getMessage() + "'});").getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return "inputStream";
    }

    public String logout() {
        ActionContext ac = ActionContext.getContext();
        ac.getSession().remove("user");
        return "login";
    }

    public String execute() {
        return "login";
    }

    public String regUser() {
        loginName = loginName.trim().toLowerCase();
        loginpassword = loginpassword.trim().toLowerCase();
        HttpServletRequest request = ServletActionContext.getRequest();
        range = request.getParameter("smin") + "-" + request.getParameter("smax") +
                "-" + request.getParameter("fmin") + "-" + request.getParameter("fmax");
        Userinfo userinfo = new Userinfo();
        userinfo.setCellphone(phone);
        userinfo.setEmail(email);
        userinfo.setPassword(PasswordEncoder.encode(loginName, loginpassword));
        userinfo.setUsername(loginName);
        userinfo.setUserStatus(0);
        userinfo.setUserTruename(name);
        userinfo.setUserRank(rank);
        userinfo.setUserRange(range);
        userService.saveUser(userinfo);
        userinfo = userService.getUser(loginName, loginpassword);
        List list = new ArrayList();
        for (int i = 0; i < cp.split(",").length; i++) {
            list.add(cp.split(",")[i]);
        }
        try {
            userCpService.saveUsercp(list, userinfo.getUserid());
            result = "1";
        } catch (Exception e) {
            e.printStackTrace();
            result = "2";
        }
        return userManager();
    }

    public String userManager() {
        users = userService.getUsers();
        return "userManager";
    }

    public String reg() {

        return "reg";
    }

    public String edit() {
        try {
            Userinfo userinfo = null;
            List<Userinfo> list = userService.getUserById(id);
            if (list != null && list.size() > 0) {
                userinfo = list.get(0);
                if (userinfo.getUserStatus() == 1) {
                    return "userManager";
                }
            }
            List<UserCp> cps = userCpService.getUserCp(id);
            String cp = "";
            for (int i = 0; i < cps.size(); i++) {
                if (i != 0) {
                    cp += "," + cps.get(i).getCpId();
                } else {
                    cp += cps.get(i).getCpId();
                }
            }
            userinfo.setUserCp(cp);
            users = new ArrayList<Userinfo>();
            users.add(userinfo);
            result = "1";
        } catch (Exception e) {
            e.printStackTrace();
            result = "2";
        }

        return "edit";
    }

    public String editUser() {
        try {
            loginName = loginName.trim().toLowerCase();
            loginpassword = loginpassword.trim().toLowerCase();
            HttpServletRequest request = ServletActionContext.getRequest();
            range = request.getParameter("smin") + "-" + request.getParameter("smax") +
                    "-" + request.getParameter("fmin") + "-" + request.getParameter("fmax");
            Userinfo userinfo = userService.getUserById(id).get(0);
            userinfo.setCellphone(phone);
            userinfo.setEmail(email);
            if (!loginpassword.toUpperCase().trim().equals("_SOBEY_PASSWORD")) {
                userinfo.setPassword(PasswordEncoder.encode(loginName, loginpassword));
            }
            userinfo.setUsername(loginName);
            userinfo.setUserTruename(name);
            userinfo.setUserRank(rank);
            userinfo.setUserRange(range);
            userService.updateUser(userinfo);
            List list = new ArrayList();
            for (int i = 0; i < cp.split(",").length; i++) {
                list.add(cp.split(",")[i]);
            }
            userCpService.deleteAll(id);
            userCpService.saveUsercp(list, id);
            result = "1";
        } catch (Exception e) {
            e.printStackTrace();
            result = "2";
        }
        return userManager();
    }

    public String deleteUser() {
        try {
            userService.deleteUser(id);
            result = "1";
        } catch (Exception e) {
            e.printStackTrace();
            result = "2";
        }
        return userManager();
    }
}
