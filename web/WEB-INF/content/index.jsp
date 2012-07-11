<%@ page import="com.sobey.mcss.domain.Userinfo" %>
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

    <link href="<%=basePath%>styles/layout.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>css/style.css" rel="stylesheet" type="text/css"/>
    <!-- Theme Start -->
    <link href="<%=basePath%>themes/blue/styles.css" rel="stylesheet" type="text/css"/>
    <!-- Theme End -->
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-1.4.4.min.js"></SCRIPT>
    <script type="text/javascript">
        var temp = "";
        function changeSelect(obj) {
//            var height = new Number($("#nav").css("height").split("px")[0]) + 500;
//            $("#detail").css("height", height + "px");
            var nav = $(".navigation");
            for (var i = 0; i < nav.length; i++) {
                var nodes = nav[i].children;
                for (var j = 0; j < nodes.length; j++) {
                    if (nodes[j].className == "selected") {
                        nodes[j].className = "";
                        nodes[j].innerHTML = temp;
                        nodes[j].children[0].onclick = function () {
                            changeSelect(this);
                        };
                    }
                }
            }
            var li = obj.parentNode;
            li.className = "selected";
            temp = li.innerHTML;
            li.innerHTML = obj.innerHTML;
            li.onclick = function () {
                document.getElementById("detail").src = obj.href;

            }
        }
        $(document).ready(function () {

            var nav = $(".navigation");
            for (var i = 0; i < nav.length; i++) {
                var nodes = nav[i].children;
                for (var j = 0; j < nodes.length; j++) {
                    var obj = nodes[j].children[0];
                    if (obj.tagName == "A") {
                        obj.onclick = function () {
                            changeSelect(this);
                        };
                        obj.title = obj.innerHTML;
                        obj.target = "detail";
                    }
                }
            }
        });
        function onLogout() {
            window.location = "<%=basePath + "user!logout.action?d=" + System.currentTimeMillis()%>";
        }
    </script>
</head>
<body id="homepage" style="overflow-y: auto;overflow-x: hidden;background-image: url('')">
<div id="header">
    <a href="" title=""><img src="<%=basePath%>img/cp_logo_login.png" alt="Control Panel" class="logo"/></a>

    <div class=menu1 id=sl1>
        <div id=slider-nav style="Z-INDEX: 1; POSITION: relative">
            <a class=slider-link-1 href="<%=basePath%>index.jsp">首　　页</a>
            <a class=slider-link-2 href="<%=basePath%>modify.jsp" target="detail">账户管理</a>
            <a class=slider-link-3 href="<%=basePath%>content.jsp" target="detail">内容管理</a>
            <a class=slider-link-4 href="<%=basePath%>monitor1.jsp" target="_self">数据监控</a>
            <a class=slider-link-5 href="<%=basePath%>log.jsp" target="detail">日志管理</a>
            <a class=slider-link-6 href="<%=basePath%>about.jsp" target="detail">客户中心</a>
            <a class=slider-link-7 href="javascript:onLogout();" target="_self">退　　出</a>
        </div>
    </div>
    <script type="text/javascript">
        $(".slider-link-1, .slider-link-2, .slider-link-3, .slider-link-4, .slider-link-5, .slider-link-6,.slider-link-7").hover(function() {
            $(this).animate({
                marginTop: "8px"
            }, 250);
        }, function() {
            $(this).animate({
                marginTop: "0px"
            }, 250);
        });
    </script>
</div>
<iframe src="<%=basePath%>main.jsp" width="100%" height="100%" name="detail" id="detail"></iframe>
<!-- Left Dark Bar Start -->
<div id="leftside">
    <div class="user">
        <img src="<%=basePath%>img/avatar.png" width="44" height="44" class="hoverimg" alt="Avatar"/>

        <p>您好:</p>

        <p class="username">${sessionScope.user.userTruename}</p>


        <p class="userbtn"><a href="javascript:onLogout();" title="">注销</a></p>

        <p style="float: left;">上次登录:${sessionScope.user.lastLogin}</p>
    </div>
    <ul id="nav">
        <li>
            <a class="collapsed heading">流量查看</a>
            <ul class="navigation">
                <li><a href="<%=basePath%>flow/flow!webSpeedUp.action">网页加速流量</a>
                </li>
                <li><a href="<%=basePath%>flow/flow!mediaSpeedUp.action">流媒体加速流量</a>
                </li>
                <li><a href="<%=basePath%>flow/flow!uploadSpeedUp.action">上传加速流量</a></li>
                <li><a href="<%=basePath%>flow/flow!areaWebSpeedUp.action">各地区网页加速流量</a></li>
                <li><a href="<%=basePath%>flow/flow!areaMediaSpeedUp.action">各地区流媒体加速流量</a></li>
            </ul>
        </li>
        <li>
            <a class="collapsed heading">带宽统计</a>
            <ul class="navigation">
                <li><a href="<%=basePath%>bandwidth/bandwidth!webSpeedUp.action">网页带宽统计</a></li>
                <li><a href="<%=basePath%>bandwidth/bandwidth!mediaSpeedUp.action">流媒体带宽统计</a></li>
                <li><a href="<%=basePath%>bandwidth/bandwidth!uploadSpeedUp.action">上传带宽统计</a></li>
            </ul>
        </li>
        <li><a class="collapsed heading">数据分析</a>
            <ul class="navigation">
                <li>
                    <ul id="nav">
                        <li>
                            <a class="collapsed heading"
                               style="font-size: 13px;background-image: url('<%=basePath%>themes/blue/img/bg_navigation1.png');border-bottom-color: #666;border-top-color:#666"
                               id="sub">网页加速数据分析</a>
                            <ul class="navigation">
                                <li><a href="<%=basePath%>analysis/analysis!webAccess.action">&nbsp;&nbsp;&nbsp;&nbsp;访问人数分析</a>
                                </li>
                                <li><a href="<%=basePath%>analysis/analysis!visitorsSource.action">&nbsp;&nbsp;&nbsp;&nbsp;访问来源分析</a>
                                </li>
                                <li><a href="<%=basePath%>analysis/analysis!webView.action">&nbsp;&nbsp;&nbsp;&nbsp;浏览量分析</a>
                                </li>
                                <li><a href="<%=basePath%>analysis/analysis!webArea.action">&nbsp;&nbsp;&nbsp;&nbsp;来访地区分析</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                <li>
                    <ul id="nav">
                        <li>
                            <a class="collapsed heading"
                               style="font-size: 13px;background-image: url('<%=basePath%>themes/blue/img/bg_navigation1.png');border-bottom-color: #666;border-top-color:#666"
                               id="sub">流媒体直播加速数据分析</a>
                            <ul class="navigation">
                                <li><a href="<%=basePath%>analysis/analysis!liveWatch.action">&nbsp;&nbsp;&nbsp;&nbsp;观看人数分析</a>
                                </li>
                                <li><a href="<%=basePath%>analysis/analysis!liveStay.action">&nbsp;&nbsp;&nbsp;&nbsp;停留时间分析</a>
                                </li>
                                <li><a href="<%=basePath%>analysis/analysis!liveArea.action">&nbsp;&nbsp;&nbsp;&nbsp;来访地区分析</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
                <li>
                    <ul id="nav">
                        <li>
                            <a class="collapsed heading"
                               style="font-size: 13px;background-image: url('<%=basePath%>themes/blue/img/bg_navigation1.png');border-bottom-color: #666;border-top-color:#666"
                               id="sub">流媒体点播加速数据分析</a>
                            <ul class="navigation">
                                <li><a href="<%=basePath%>analysis/analysis!vodPlay.action">&nbsp;&nbsp;&nbsp;&nbsp;总播放量分析</a>
                                </li>
                                <li><a href="<%=basePath%>analysis/analysis!vodStay.action">&nbsp;&nbsp;&nbsp;&nbsp;停留时间分析</a>
                                </li>
                                <li><a href="<%=basePath%>analysis/analysis!vodArea.action">&nbsp;&nbsp;&nbsp;&nbsp;来访地区分析</a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
                <li><a href="<%=basePath%>upload/upload!uploadStat.action" style="font-size: 13px;font-weight: bold;">上传带宽统计</a>
                </li>
            </ul>
        </li>
        <li>
            <a class="collapsed heading">内容管理</a>
            <ul class="navigation">
                <li><a href="<%=basePath%>content.jsp">内容管理</a>
                </li>
            </ul>
        </li>
        <li>
            <a class="collapsed heading">日志管理</a>
            <ul class="navigation">
                <li><a href="<%=basePath%>log.jsp">日志下载</a>
                </li>
            </ul>
        </li>
        <li>
            <a class="collapsed heading">账户管理</a>
            <ul class="navigation">
                <li><a href="<%=basePath%>modify.jsp">账户管理</a>
                </li>
                <%
                    Userinfo userinfo = (Userinfo) request.getSession().getAttribute("user");
                    if (userinfo.getUserStatus() == 1) {
                %>
                <li><a href="<%=basePath%>user!userManager.action">CP管理</a>
                </li>
                <li><a href="<%=basePath%>cpManager.jsp">域名管理</a>
                </li>
                <%
                    }
                %>
            </ul>
        </li>
        <li>
            <a class="collapsed heading">客户中心</a>
            <ul class="navigation">
                <li><a href="<%=basePath%>about.jsp">客户中心</a>
                </li>
            </ul>
        </li>
    </ul>
</div>

<!-- Left Dark Bar End -->

<script type='text/javascript' src='<%=basePath%>scripts/jquery.wysiwyg.js'></script>
<script type='text/javascript' src='<%=basePath%>scripts/visualize.jQuery.js'></script>
<script type="text/javascript" src='<%=basePath%>scripts/functions.js'></script>

<!--[if IE 6]>
<script type='text/javascript' src='<%=basePath%>scripts/png_fix.js'></script>
<script type='text/javascript'>
    DD_belatedPNG.fix('img, .notifycount, .selected');
</script>
<![endif]-->
<%--<div id="footer">--%>
<%--Copyright &copy; 1997 - 2011 Sobey Digital Technology Co.Ltd--%>
<%--</div>--%>
</body>
</html>
