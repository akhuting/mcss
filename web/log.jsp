<%@ page import="com.sobey.common.util.DateUtil" %>
<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 11-5-24
  Time: 下午12:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/user!index.action?d=" + System.currentTimeMillis());
    }
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Admin Template</title>
    <SCRIPT type="text/javascript" src="scripts/jquery-1.4.4.min.js"></SCRIPT>
    <SCRIPT type="text/javascript" src="scripts/common.js"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-ui-1.8.6.custom.min.js"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-ui-timepicker-addon.js"></SCRIPT>
    <link href="styles/layout.css" rel="stylesheet" type="text/css"/>
    <link href="styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/jquery-ui-1.8.6.custom.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/jquery-ui-timepicker-addon.css" rel="stylesheet" type="text/css"/>
    <!-- Theme Start -->
    <link href="themes/blue/styles.css" rel="stylesheet" type="text/css"/>
    <!-- Theme End -->
    <script type="text/javascript" language="JavaScript" charset="UTF-8">
        function getFile() {


            var cp = document.getElementById("cp").value;
            if (cp == "0") {
                $("#war").slideDown("slow");
                return;

            }
            var type = document.getElementById("type").value;
            var begin = $("#beginTime").val();
            var end = $("#endTime").val();

            $.ajax({
                url: "<%=basePath%>/stat!getLog.action",
                data: {cpStr: cp, typeStr: type, begin: begin, end: end},
                cache: false,
                dataType: "json",
                success: function (obj) {
                    if (obj == "0") {
                        $("#war").slideDown("slow");
                    } else {
                        $("#war").slideUp("slow");
                        var str = "<table width='100%'> <thead> <tr> <th>日期</th> <th>日志名称</th> <th>大小</th><th>操作</th> </tr> </thead> <tbody>";
                        for (var i = 0; i < obj.length; i++) {
                            str += "<tr class='alt'>";
                            str += "<td>";
                            str += obj[i]["date"];
                            str += "</td>";
                            str += "<td>";
                            str += obj[i]["name"];
                            str += "</td><td>";
                            str += obj[i]["size"];
                            str += "</td>";
                            str += "<td>";
                            str += "<a href='<%=basePath%>";
                            str += obj[i]["url"] + "' title=''>";
                            str += " <img src='img/icons/save_download_32.png' alt='下载'/></a></td>";
                            str += "</tr>";
                        }
                        str += "</tbody> </table>";
                        document.getElementById("file").innerHTML = str;
                    }
                }
            });
        }
        $(document).ready(function () {
            parent.$("#detail").attr("scrolling", "no");
            $("body").css({overflow: "hidden"});
            $('#endTime').datepicker({
                prevText: "上一页",
                nextText: "下一页",
                currentText: "当前时间",
                closeText: "关闭",
                timeText: "时间",
                hourText: "小时",
                minuteText: "分钟",
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
                hourGrid: 4,
                minuteGrid: 10,
                dateFormat: "yy-mm-dd"
            });

            $('#beginTime').datepicker({
                prevText: "上一页",
                nextText: "下一页",
                currentText: "当前时间",
                closeText: "关闭",
                timeText: "时间",
                hourText: "小时",
                minuteText: "分钟",
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
                hourGrid: 4,
                minuteGrid: 10,
                dateFormat: "yy-mm-dd"
            });
        });
    </script>
</head>
<body id="homepage">


<!-- Top Breadcrumb Start -->

<!-- Top Breadcrumb End -->

<!-- Right Side/Main Content Start -->
<div id="rightside">


    <div class="status warning" id="war" style="display: none;">
        <p class="closestatus"><a href="javascript:showOrClose('#war');" title="Close">x</a></p>

        <p><img src="img/icons/icon_warning.png" alt="Warning"/><span>警告!</span>&nbsp;暂无可提供下载的日志文件</p>
    </div>


    <!-- Alternative Content Box Start -->
    <div class="contentcontainer">
        <div class="headings alt">
            <h2>日志下载</h2>
        </div>
        <div class="extrabottom">
            <ul>
                <li><select id="type">
                    <option value="access">web</option>
                    <option value="media">media</option>
                    <option value="upload">upload</option>
                </select></li>
                <li><select id="cp" name="cp">
                    <option value="0">选择域名</option>
                </select></li>
                <li><input style="width: 110px;" class="logDate" type="text" name="endTime" id="beginTime" value="<%=DateUtil.getFirstDayOfWeek()%>"/>
                </li>
                <li><input style="width: 110px;" class="logDate" type="text" name="endTime" id="endTime" value="<%=DateUtil.getLastDayOfWeek()%>"/></li>
                <li><input type="button" onclick="getFile();" value="查询" class="btn"/></li>
            </ul>
            <script type="text/javascript">
                $.ajax({
                    url: "<%=basePath%>stat!getLogCp.action",
                    cache: false,
                    dataType: "text",
                    success: function (html) {
                        if (html != "0") {
                            $("#war").slideUp("slow");
                            var cps = html.split("|");
                            var cp = document.getElementById("cp");
                            cp.innerHTML = "";
                            for (var i = 0; i < cps.length; i++) {
                                var obj = document.createElement("option");
                                obj.text = cps[i];
                                obj.value = cps[i];
                                cp.options.add(obj);
                            }
                        } else {
                            $("#war").slideDown("slow");
                        }
                    }
                });
            </script>
        </div>

        <div class="contentbox" id="file">
            <table width="100%">
                <thead>
                <tr>
                    <th>日期</th>
                    <th>日志名称</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>


                </tbody>
            </table>

            <ul class="pagination">
                <li class="text">上一页</li>
                <li><a href="#" title="">1</a></li>
                <li class="text"><a href="#" title="">下一页</a></li>
            </ul>
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


<script type='text/javascript' src='scripts/jquery.wysiwyg.js'></script>
<script type='text/javascript' src='scripts/visualize.jQuery.js'></script>
<script type="text/javascript" src='scripts/functions.js'></script>

<!--[if IE 6]>
<script type='text/javascript' src='scripts/png_fix.js'></script>
<script type='text/javascript'>
    DD_belatedPNG.fix('img, .notifycount, .selected');
</script>
<![endif]-->
</body>
</html>
