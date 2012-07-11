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
    <script type="text/javascript">

    </script>
</head>
<body style="background-image: url('');">
<!-- Right Side/Main Content Start -->
<div id="rightside" style="margin-left:25px;margin-top: 0px;">
    <div class="headings alt" style="text-align: center;">
        <h2>WHY TEST</h2>
    </div>
    <div class="contentcontainer" style="margin-bottom:25px;height: 50px;">
        <div class="contentbox" id="list" style="height: 50px;">

            <form action="" method="post" id="sform">
                <input type="hidden" name="isTime" id="isTime">

                <div>
                    <ul style="margin-top:6px;">
                        <li><input style="width: 110px;" type="text" name="time" id="time" value=""/>
                            是否按时间查询<input type="checkbox" name="it" id="it">

                            <div class="bulkactions">
                                <input type="button" onclick="ok();" value="查询" class="btn" id="btn"/>
                            </div>
                        </li>

                    </ul>

                </div>
            </form>
            <div style="clear: both;"></div>
        </div>

    </div>
    <div id="content" style="padding-top: 20px;">

    </div>
    <div style="clear:both;"></div>

</div>
</body>
</html>
