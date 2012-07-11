<%@ page import="com.sobey.common.util.StringUtil" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
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
    String unit ="Mbps";
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
            sendAjaxRequest("cp!field.action?type=Web&field=cp&table=broadbandstat&d=" + d.getMilliseconds());
        }
    </SCRIPT>
</head>
<body id="homepage" onLoad="goPage(1,12);">
<!-- Right Side/Main Content Start -->
<div id="rightside">

    <!-- Alternative Content Box Start -->
    <div class="contentcontainer">
        <div class="headings alt">
            <h2>网页加速带宽统计</h2>
        </div>
        <%@include file="../search.jsp" %>
        <div class="contentbox" id="graphs"></div>
        <%
            String charts = "ZoomLine";
            if (map.get("charts") != null) {
                charts = map.get("charts").toString();
            }
        %>
        <script type="text/javascript">
            var myChart1 = new FusionCharts("<%=basePath%>swf/<%=charts%>.swf", "myChartId2", "100%", "300", "0", "0");
            myChart1.setDataXML("<%=map.get("xml")%>");
            myChart1.setTransparent(true);
            myChart1.render("graphs");
        </script>
        <div class="contentbox">
            <table width="100%" id="idData">
                <thead>
                <tr>
                    <th>时间</th>
                    <th>峰值带宽</th>
                    <th>峰值时间点</th>
                    <th>峰值平均带宽</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (map.get("charts") != null) {
                        if (map.get("charts").equals("ZoomLine")) {
                %>
                <tr class=table-row>
                    <td><%=request.getAttribute("beginTime")%>
                        — <%=request.getAttribute("endTime")%>
                    </td>
                    <td><%=map.get("max")%>(<%=unit%>)</td>
                    <td><%=map.get("maxtime")%>
                    </td>
                    <td><%=map.get("avg")%>(<%=unit%>)</td>
                </tr>
                <%
                } else {
                    ArrayList list = (ArrayList) map.get("list");
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            Object []  objects= (Object[]) list.get(i);
                %>
                <tr class=table-row>
                    <td><%=objects[1]%>
                    </td>
                    <td><%=objects[4].toString()%>(<%=unit%>)</td>
                    <td><%=objects[1] + " " + objects[3]%>
                    </td>
                    <td><%=objects[5].toString()%>(<%=unit%>)</td>
                </tr>
                <%
                                }
                            }
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


    <%--<div id="footer">--%>
        <%--Copyright &copy; 1997 - 2011 Sobey Digital Technology Co.Ltd--%>
    <%--</div>--%>

</div>
</body>
</html>
