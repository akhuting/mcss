<%@ page import="java.util.Map" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.sobey.mcss.domain.Uploadstat" %>
<%@ page import="com.sobey.common.dao.QueryResult" %>
<%@ page import="com.sobey.common.pager.Pager" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sobey.common.util.IPUtil" %>
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
            sendAjaxRequest("cp!field.action?field=cp&table=uploadstat&d=" + d.getMilliseconds());
        }
    </SCRIPT>
</head>
<body id="homepage" onLoad="goPage(1,12);">
<!-- Right Side/Main Content Start -->
<div id="rightside">

    <!-- Alternative Content Box Start -->
    <div class="contentcontainer">
        <div class="headings alt">
            <h2>上传加速数据分析</h2>
        </div>
        <%@include file="../search.jsp" %>
        <div class="contentbox" id="graphs"></div>
        <script type="text/javascript">
            var myChart1 = new FusionCharts("<%=basePath%>swf/MSCombi2D.swf", "myChartId2", "100%", "300", "0", "0");
            myChart1.setDataXML("<%=map.get("xml")%>");
            myChart1.setTransparent(true);
            myChart1.render("graphs");
        </script>
        <div class="contentbox">
            <table width="100%" id="idData">
                <thead>
                <tr>
                    <th>上传时间</th>
                    <th>上传地址</th>
                    <th>上传地区</th>
                    <th>上传均值带宽</th>
                    <th>上传流量</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Uploadstat> list = null;
                    if (map.get("result") != null) {
                        list = (List<Uploadstat>) map.get("result");
                    }
                    if (list != null && list.size() > 0) {
                        for (Uploadstat uploadstat : list) {
                %>
                <tr class=table-row>
                    <td><%=uploadstat.getBegintime().replace(".0", "")%>
                        — <%=uploadstat.getEndtime().replace(".0", "")%>
                    </td>
                    <td><%=uploadstat.getClient()%>
                    </td>
                    <td><%=IPUtil.getInstance().getAddress(uploadstat.getClient())%>
                    </td>
                    <td><%="100MB"%>
                    </td>
                    <td><%=uploadstat.getDataflow()%>MB
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
