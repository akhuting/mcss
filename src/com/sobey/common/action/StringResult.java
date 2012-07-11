package com.sobey.common.action;

import com.opensymphony.xwork2.ActionInvocation;
import org.apache.struts2.dispatcher.ServletRedirectResult;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Yanggang.
 * Date: 11-1-20
 * Time: 下午5:00
 * To change this template use File | Settings | File Templates.
 */
public class StringResult extends ServletRedirectResult {

    private static final long serialVersionUID = 1L;
    private String contentTypeName;
    private String stringName = "";

    public StringResult() {
        super();
    }

    public String getContentTypeName() {
        return contentTypeName;
    }

    public void setContentTypeName(String contentTypeName) {
        this.contentTypeName = contentTypeName;
    }

    public String getStringName() {
        return stringName;
    }

    public void setStringName(String stringName) {
        this.stringName = stringName;
    }

    public StringResult(String location) {
        super(location);
    }

    protected void doExecute(String finalLocation, ActionInvocation invocation)
            throws Exception {
        HttpServletResponse response = (HttpServletResponse) invocation
                .getInvocationContext().get(HTTP_RESPONSE);

        String contentType = conditionalParse(contentTypeName, invocation);
        if (contentType == null) {
            contentType = "text/plain; charset=UTF-8";
        }
        response.setContentType(contentType);
        PrintWriter out = response.getWriter();

        String result = (String) invocation.getStack().findValue(stringName);
        out.println(result);
        out.flush();
        out.close();
    }

}