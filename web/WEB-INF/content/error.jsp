<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 11-5-18
  Time: 下午5:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.sobey.common.util.StringUtil" %>
<%@ page import="java.util.Date" %>
<%@ page import="org.htmlparser.Parser" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="org.htmlparser.NodeFilter" %>
<%@ page import="org.htmlparser.filters.TagNameFilter" %>
<%@ page import="org.htmlparser.util.NodeList" %>
<%@ page import="org.htmlparser.Node" %>
<%@ page import="org.htmlparser.Tag" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 11-5-18
  Time: 下午5:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    String message = "系统发生位置错误，请重试或重新登录";
    if(request.getAttribute("message")!=null){
        message = request.getAttribute("message").toString();
    }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>MCSS</title>
    <link href="<%=basePath%>styles/jquery-ui-1.8.6.custom.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/jquery-ui-timepicker-addon.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/layout.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
    <!-- Theme Start -->
    <link href="<%=basePath%>themes/blue/styles.css" rel="stylesheet" type="text/css"/>
    <!-- Theme End -->
</head>
<body style="background-image: url('');">
<!-- Right Side/Main Content Start -->
<div id="rightside" style="margin-left:25px;margin-top: 0px;">
    <div class="contentcontainer sml right" id="tabs" style="margin-right:auto;margin-left:auto;float: none;padding-top:200px;">
        <div class="headings">
            <h2 class="left">错误</h2>
        </div>
        <div class="contentbox" id="tabs-1">

                <span ><img src="<%=basePath%>images/warning_blue.png" alt="Warning" align="middle"/></span>
                <span><%=message%></span>

        </div>

    </div>
</div>
</body>
</html>
