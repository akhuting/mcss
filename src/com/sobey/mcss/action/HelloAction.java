package com.sobey.mcss.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sobey.mcss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/**
 * Created by Yanggang.
 * Date: 11-1-10
 * Time: 下午5:16
 * To change this template use File | Settings | File Templates.
 */
public class HelloAction extends ActionSupport {

    @Autowired
    private UserService userService;

    @Override
    public String execute() throws Exception {
        System.out.println(userService);
        return super.execute();
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
