<%@ page import="java.util.Map" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.sobey.common.util.StringUtil" %>
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
    Map<String, Object> map = (Map) request.getAttribute("result");

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
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-1.4.4.min.js"></SCRIPT>
    <script type="text/javascript" src="<%=basePath%>scripts/FusionCharts.js"></script>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-ui-1.8.6.custom.min.js"></SCRIPT>
    <script type="text/javascript" src="<%=basePath%>scripts/page.js"></script>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-ui-timepicker-addon.js"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/util.js"></SCRIPT>
    <SCRIPT type="text/javascript">
        function getCP() {
            var d = new Date();
            sendAjaxRequest("cp!field.action?type=Flow&subType=URL&field=cp&table=urldaystatitem&d=" + d.getMilliseconds());
        }
    </SCRIPT>
</head>
<body id="homepage" onload="goPage(1,12)">
<!-- Right Side/Main Content Start -->
<div id="rightside">

    <!-- Alternative Content Box Start -->
    <div class="contentcontainer">
        <div class="headings alt">
            <h2>访问来源分析</h2>
        </div>
        <%@include file="../search.jsp" %>

        <div class="contentbox">
            <table width="100%"  id="idData">
                <thead>
                <tr>
                    <th>&nbsp;</th>
                    <th>来访URL</th>
                    <th>流量百分比</th>
                </tr>
                </thead>
                <tbody>
                <%
                    NumberFormat formatter = new DecimalFormat("0.00%");
                    double count = 0;
                    int index = 0;
                    if (map.get("count") != null) {
                        count = Double.parseDouble(map.get("count").toString());
                    }
                    if (count == 0) {
                        count = 1;
                    }
                    for (Map.Entry<String, String> entry : StringUtil.sortMapByValueDouble(map)) {
                        if (!entry.getKey().equals("xml") && !entry.getKey().equals("count")) {
                %>
                <tr class=table-row>
                    <td><%=++index%>
                    </td>
                    <td><%=entry.getKey()%>
                    </td>
                    <td><%=formatter.format(Double.parseDouble(String.valueOf(entry.getValue())) / count)%>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>

            <div id="barcon"></div>
            <div style="clear: both;"></div>
        </div>

    </div>
    <!-- Alternative Content Box End -->

    <div style="clear:both;"></div>


     <!--
       <div id="footer">
        	Copyright &copy; 1997 - 2011 Sobey Digital Technology Co.Ltd
        </div>
        -->

</div>
</body>
</html>
