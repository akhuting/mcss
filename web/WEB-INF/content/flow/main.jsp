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
    <script type="text/javascript">
        function showList(id,srcobj) {
            $("#" + id).slideToggle("slow");
            var src = srcobj.src;
            if(src.indexOf("down")!=-1){
               src = src.replace("down","up");
            }else{
                src = src.replace("up","down");
            }
            srcobj.src = src;
        }
        function add(obj) {
            $("." + obj).toggle();
        }
        function referrer() {
            $.ajax({
                        url: "<%=basePath%>flow/flow!ajaxWebUser.action",
                        cache: false,
                        dataType:"html",
                        async:true,
                        success: function(html) {
                            var arr = html.split("|");
                            if (arr.length == 1) {
                                document.getElementById("webList").innerHTML = html;
                            } else {
                                document.getElementById("webList").innerHTML = arr[1];
                                var chart = document.getElementById("web");
                                if (chart.children.length == 1) {
                                    var span = document.createElement("span");
                                    span.innerHTML = arr[0];
                                    chart.appendChild(span);
                                } else {
                                    chart.children[1].innerHTML = arr[0];
                                }
                            }
                        }
                    });
            $.ajax({
                        url: "<%=basePath%>flow/flow!ajaxMediaUser.action",
                        cache: false,
                        dataType:"html",
                        async:true,
                        success: function(html) {
                            var arr = html.split("|");
                            if (arr.length == 1) {
                                document.getElementById("mediaList").innerHTML = html;
                            } else {
                                document.getElementById("mediaList").innerHTML = arr[1];
                                var chart = document.getElementById("media");
                                if (chart.children.length == 1) {
                                    var span = document.createElement("span");
                                    span.innerHTML = arr[0];
                                    chart.appendChild(span);
                                } else {
                                    chart.children[1].innerHTML = arr[0];
                                }
                            }
                        }
                    });
            $.ajax({
                        url: "<%=basePath%>flow/flow!ajaxTotalUser.action",
                        cache: false,
                        dataType:"html",
                        async:true,
                        success: function(html) {
                            var arr = html.split("|");
                            if (arr.length == 1) {
                                document.getElementById("totalList").innerHTML = html;
                            } else {
                                document.getElementById("totalList").innerHTML = arr[1];
                                var chart = document.getElementById("total");
                                if (chart.children.length == 1) {
                                    var span = document.createElement("span");
                                    span.innerHTML = arr[0];
                                    chart.appendChild(span);
                                } else {
                                    chart.children[1].innerHTML = arr[0];
                                }
                            }
                        }
                    });
        }
        window.setInterval(referrer, 10000);
         $(document).ready(function(){
            referrer();
        });
    </script>
</head>
<body>
<!-- Right Side/Main Content Start -->
<div id="rightside">
    <div class="contentcontainer" style="margin-bottom:25px;">
        <div class="contentbox" id="total"></div>
        <img src="<%=basePath%>img/icons/down.png" alt="" onclick="showList('totalList',this)"
             style="float: right;margin-top: 5px;cursor: pointer">
        <script type="text/javascript">
            var myChart = new FusionCharts("<%=basePath%>swf/RealTimeLine.swf", "myChart", "100%", "180", "0", "0");
            myChart.setDataXML("<%=map.get("totalXml")%>");
            myChart.setTransparent(true);
            myChart.render("total");
        </script>
        <div class="contentbox" style="display: none;" id="totalList">

        </div>

    </div>
    <!-- Alternative Content Box Start -->
    <div class="contentcontainer" style="margin-bottom:25px;">
        <div class="contentbox" id="web"></div>
        <img src="<%=basePath%>img/icons/down.png" alt="" onclick="showList('webList',this)"
             style="float: right;margin-top: 5px;cursor: pointer">
        <script type="text/javascript">
            var myChart1 = new FusionCharts("<%=basePath%>swf/RealTimeLine.swf", "myChart1", "100%", "180", "0", "0");
            myChart1.setDataXML("<%=map.get("webXml")%>");
            myChart1.setTransparent(true);
            myChart1.render("web");
        </script>
        <div class="contentbox" style="display: none;" id="webList">

        </div>
    </div>
    <!-- Alternative Content Box End -->
    <div class="contentcontainer" style="margin-bottom:25px;">
        <div class="contentbox" id="media"></div>
        <img src="<%=basePath%>img/icons/down.png" alt="" onclick="showList('mediaList',this)"
             style="float: right;margin-top: 5px;cursor: pointer">
        <script type="text/javascript">
            var myChart2 = new FusionCharts("<%=basePath%>swf/RealTimeLine.swf", "myChart2", "100%", "180", "0", "0");
            myChart2.setDataXML("<%=map.get("mediaXml")%>");
            myChart2.setTransparent(true);
            myChart2.render("media");
        </script>
        <div class="contentbox" style="display: none;" id="mediaList">

        </div>

    </div>

    <div style="clear:both;"></div>
</div>
</body>
</html>

