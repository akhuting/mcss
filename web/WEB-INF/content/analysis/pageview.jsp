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
            sendAjaxRequest("cp!field.action?type=Analysis&subType=Viewed&item=PV&field=cp&d=" + d.getMilliseconds());
        }
    </SCRIPT>
</head>
<body id="homepage" onLoad="goPage(1,12);">
<!-- Right Side/Main Content Start -->
<div id="rightside">

    <!-- Alternative Content Box Start -->
    <div class="contentcontainer">
        <div class="headings alt">
            <h2>浏览量分析</h2>
        </div>
        <%@include file="../search.jsp" %>
        <div class="contentbox" id="graphs"></div>
        <script type="text/javascript">
            var myChart1 = new FusionCharts("<%=basePath%>swf/StackedColumn2D.swf", "myChartId2", "100%", "300", "0", "0");
            myChart1.setDataXML("<%=map.get("xml")%>");
            myChart1.setTransparent(true);
            myChart1.render("graphs");
        </script>
        <div class="contentbox">
            <table width="100%" id="idData">
                <thead>
                <tr>
                    <th>时间</th>
                    <th>独立IP</th>
                    <th>浏览量(PV值)</th>
                    <th>人均浏览页数</th>
                </tr>
                </thead>
                <tbody>
                <%
                    NumberFormat formatter = new DecimalFormat("0.00");

                    Object[] key_arr = map.keySet().toArray();
                    Arrays.sort(key_arr);
                    String last = "";
                    String type = "";
                    if (map.get("type") != null) {
                        type = map.get("type").toString();
                    }
                    for (Object key : key_arr) {
                        if (!key.equals("xml") && !key.equals("type")) {
                            Object value = map.get(key);
                            double mdn;
                            double back;
                            if (type.equals("hour")) {
                                mdn = Double.parseDouble(value.toString().split(";")[0]);
                                back = Double.parseDouble(value.toString().split(";")[1]);
                                if (key.equals("01")) {

                %>
                <tr class=table-row>
                    <td>00:00-<%=key%>:00</td>
                    <td><%=mdn%>
                    </td>
                    <td><%=back%>
                    </td>
                    <%
                        if (mdn == 0) {
                            mdn = 1;
                        }
                    %>
                    <td><%=formatter.format(Double.parseDouble(String.valueOf(back)) / Double.parseDouble(String.valueOf(mdn)))%>
                    </td>
                </tr>
                <%
                } else {

                %>
                <tr class=table-row>
                    <td><%=last%>:00-<%=key%>:00</td>
                    <td><%=mdn%>
                    </td>
                    <td><%=back%>
                    </td>
                    <%
                        if (mdn == 0) {
                            mdn = 1;
                        }
                    %>
                    <td><%=formatter.format(Double.parseDouble(String.valueOf(back)) / Double.parseDouble(String.valueOf(mdn)))%>
                    </td>
                </tr>
                <%
                    }
                    last = key.toString();
                } else {
                    mdn =Double.parseDouble(value.toString().split(";")[0]);
                    back = Double.parseDouble(value.toString().split(";")[1]);
                %>
                <tr class=table-row>
                    <td><%=key%>
                    </td>
                    <td><%=mdn%>
                    </td>
                    <td><%=back%>
                    </td>
                    <%
                        if (mdn == 0) {
                            mdn = 1;
                        }
                    %>
                    <td><%=formatter.format(Double.parseDouble(String.valueOf(back)) / Double.parseDouble(String.valueOf(mdn)))%>
                    </td>
                </tr>
                <%
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


    <!--
       <div id="footer">
        	Copyright &copy; 1997 - 2011 Sobey Digital Technology Co.Ltd
        </div>
        -->
</div>
</body>
</html>
