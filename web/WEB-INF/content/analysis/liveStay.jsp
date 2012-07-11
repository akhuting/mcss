<%@ page import="java.util.Map" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Arrays" %>
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
    Map<String, String> map = (Map) request.getAttribute("result");

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
            sendAjaxRequest("cp!field.action?type=Live&subType=5Min,10Min,30Min,60Min,NMin&field=cp&d=" + d.getMilliseconds());
        }
        function getChannel() {
            var d = new Date();
            sendAjax("cp!channel.action?type=Live&subType=5Min,10Min,30Min,60Min,NMin&field=item&d=" + d.getMilliseconds());
        }
    </SCRIPT>
</head>
<body id="homepage">
<!-- Right Side/Main Content Start -->
<div id="rightside">

    <!-- Alternative Content Box Start -->
    <div class="contentcontainer">
        <div class="headings alt">
            <h2>停留时间分析</h2>
        </div>
        <%@include file="../search.jsp" %>
        <div class="contentbox" id="graphs"></div>
        <script type="text/javascript">
            var myChart1 = new FusionCharts("<%=basePath%>swf/Pie3D.swf", "myChartId2", "100%", "300", "0", "0");
            myChart1.setDataXML("<%=map.get("xml")%>");
            myChart1.setTransparent(true);
            myChart1.render("graphs");
            $("#channel").show();
            getChannel();
            initChannel('<%=request.getAttribute("channel")%>');
        </script>
        <div class="contentbox">
            <table width="100%" id="idData">
                <thead>
                <tr>
                    <th>停留时间段</th>
                    <th>观看人数</th>
                    <th>人数百分比</th>
                </tr>
                </thead>
                <tbody>
                <%
                    NumberFormat formatter = new DecimalFormat("0%");
                    double count = 0;
                    if (map.get("count") != null) {
                        count = Double.parseDouble(map.get("count"));
                    }
                    if (count == 0) {
                        count = 1;
                    }
                    if (map.get("five") != null) {
                %>
                <tr class=table-row>
                    <td>0-5 min
                    </td>
                    <td><%=map.get("five")%>
                    </td>

                    <td><%=formatter.format(Double.parseDouble(String.valueOf(map.get("five"))) / count)%>
                    </td>
                </tr>
                <%
                    }
                %>

                <%
                    if (map.get("ten") != null) {
                %>
                <tr class=table-row>
                    <td>5-10 min
                    </td>
                    <td><%=Integer.parseInt(map.get("ten"))%>
                    </td>
                    <td><%=formatter.format(Double.parseDouble(String.valueOf(map.get("ten"))) / count)%>
                    </td>
                </tr>
                <%
                    }
                %>

                <%
                    if (map.get("thirty") != null) {
                %>
                <tr class=table-row>
                    <td>10-30 min
                    </td>
                    <td><%=map.get("thirty")%>
                    </td>
                    <td><%=formatter.format(Double.parseDouble(String.valueOf(map.get("thirty"))) / count)%>
                    </td>
                </tr>
                <%
                    }
                %>

                <%
                    if (map.get("sixty") != null) {
                %>
                <tr class=table-row>
                    <td>30-60 min
                    </td>
                    <td><%=map.get("sixty")%>
                    </td>
                    <td><%=formatter.format(Double.parseDouble(String.valueOf(map.get("sixty"))) / count)%>
                    </td>
                </tr>
                <%
                    }
                %>

                <%
                    if (map.get("n") != null) {
                %>
                <tr class=table-row>
                    <td>60以上 min
                    </td>
                    <td><%=map.get("n")%>
                    </td>
                    <td><%=formatter.format(Double.parseDouble(String.valueOf(map.get("n"))) / count)%>
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
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
