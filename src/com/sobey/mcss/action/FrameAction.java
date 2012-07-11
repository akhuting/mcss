package com.sobey.mcss.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * Created by Yanggang.
 * Date: 11-1-11
 * Time: 下午7:55
 * To change this template use File | Settings | File Templates.
 */

@Results({@Result(name = "top", location = "banner.jsp"), @Result(name = "left", location = "navigator.jsp"), @Result(name = "right", location = "welcome.jsp")})
public class FrameAction extends ActionSupport {

    public String top() {
        return "top";
    }

    public String left() {
        return "left";
    }

    public String right() {
        return "right";
    }
}
