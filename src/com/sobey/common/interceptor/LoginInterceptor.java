package com.sobey.common.interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.struts2.ServletActionContext;

/**
 * Created by Yanggang.
 * Date: 11-1-11
 * Time: 下午7:01
 * To change this template use File | Settings | File Templates.
 */
public class LoginInterceptor extends MethodFilterInterceptor {
    @Override
    protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
        if (ServletActionContext.getRequest().getSession().getAttribute("user") != null) {
            return actionInvocation.invoke();
        } else {
            return Action.LOGIN;
        }
    }
}
