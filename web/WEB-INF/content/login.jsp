<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 11-5-24
  Time: 下午4:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>mcss登录</title>
<style type="text/css">
    * {
        margin: 0;
        padding: 0;
        border: 0;
    }

    body {
        font: 12px/24px "Microsoft YaHei";
        background-color: #eeeef0
    }

    p, ul, li, dt, dd, dl, img, body, h1, h2, h3, h4 {
        list-style: none;
    }

    a {
        text-decoration: none;
        outline: none
    }

    .of {
        overflow: hidden;
        _zoom: 1;
    }

    .fl {
        float: left;
        display: inline
    }

    .fr {
        float: right;
        display: inline
    }

    input, select, textarea {
        outline: none
    }

    select {
        -moz-border-radius: 2px;
        -webkit-border-radius: 2px;
        -ms-border-radius: 2px;
        border-radius: 2px;
    }

    .cl {
        clear: both;
        line-height: 0;
        height: 0;
        font-size: 0;
    }

    .mcss_top {
        height: 99px;
        background: url(images/mcss_bg.gif) 0 0 repeat-x;
        padding-top: 12px;
        border-top: 2px #e5e5e5 solid
    }

    .mcss_top .logo {
        width: 324px;
        height: 37px;
        background: url(images/mcss_logo.png) 0 0 no-repeat;
        margin-left: 34px;
    }

    .mcss_cent {
        width: 538px;
        margin: 0 auto;
        padding-top: 25px;
    }

    .mcss_cent .icon {
        width: 404px;
        height: 75px;
        background: url(images/mcss_icon.gif) no-repeat;
        margin: 0 0 20px 6px;
    }

    .login_form {
        width: 437px;
        height: 443px;
        background: url(images/mcss_form_bg.png) no-repeat;
        padding: 35px 0 0 101px;
    }

    .login_form h2 {
        width: 116px;
        height: 35px;
        background: url(images/mcss_icon.gif) 0 -77px no-repeat;
        margin-bottom: 24px;
        text-indent: -99em
    }

    .login_form li {
        height: 39px;
        margin-bottom: 20px;
        overflow: hidden;
        vertical-align: top
    }

    .login_form li.yzm {
        height: 160px;
        overflow: hidden;
        margin-bottom: 0;
    }

    .login_form .bt {
        width: 58px;
        height: 39px;
        line-height: 39px;
        font-size: 15px;
        float: left
    }

    .login_form .ipt {
        float: left;
        width: 240px;
        height: 13px;
        line-height: 14px;
        padding: 12px 13px;
        overflow: hidden;
        background: url(images/mcss_ipt.gif) 0 -39px no-repeat;
        color: #7f8b97
    }

    .login_form input.focus {
        background-position: 0 0;
        color: #333
    }

    .login_form .ipt1 {
        float: left;
        width: 220px;
        height: 154px;
        padding: 5px;
        background: url(images/mcss_ipt.gif) 0 -240px no-repeat;
    }

    .login_form .ipt1.focus {
        background-position: 0 -80px;
        color: #333
    }

    .login_form .paint_area {
        width: 230px;
        height: 164px;
        overflow: hidden;
    }

    .login_form .tishi {
        clear: both;
        height: 18px;
        line-height: 18px;
        overflow: hidden;
        color: #606060;
        padding-left: 59px;
        margin-bottom: 14px;
    }

    .login_form .submit_btn {
        padding-left: 58px;
    }

    .login_form .submit_btn a {
        display: block;
        width: 93px;
        height: 38px;
        background: url(images/mcss_icon.gif) -141px -77px no-repeat
    }

    .mcss_bot {
        width: 100%;
        height: 43px;
        line-height: 43px;
        padding-top: 15px;
        margin-top: 85px;
        text-align: center;
        background: url(images/mcss_bot_bg.gif) 0 0 repeat-x;
        color: #fff;
    }
</style>
<link href="<%=basePath%>css/jquery.motionCaptcha.0.2.css" rel="stylesheet" type="text/css"/>
<SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery.min.js"></SCRIPT>
<script type="text/javascript">
    $(document).ready(function () {
        if (parent.document.getElementById("detail") != null) {
            parent.document.location.href = "<%=basePath%>";
        }
        var html = $("#msg").html();
        if (html.length != 0) {
            alert($(".errorMessage li span").html());
        }

        $('#frm').motionCaptcha({
            shapes: ['triangle', 'x', 'rectangle', 'circle', 'check', 'caret', 'zigzag', 'arrow', 'leftbracket', 'rightbracket', 'v', 'delete', 'star', 'pigtail'],
            errorMsg: '再试一次.',
            successMsg: '验证通过!',
            submitId: "sb",
            noCanvasMsg: "Your browser doesn't support <canvas> - try Chrome, FF4, Safari or IE9."
        });
    });
    function enter(e) {
        var myEvent = e || window.event;
        if (myEvent.keyCode == 13) {
            submitFrm();
        }
    }
    function submitFrm() {

        if (document.getElementById("loginName").value == "") {
            alert("用户名不能为空");
            return;
        }
        if (document.getElementById("loginpassword").value == "") {
            alert("密码不能为空");
            return;
        }
        if (document.getElementById("code").value == "false") {
            alert("请绘制验证码");
            return;
        }
        $("#frm").attr("action", "<%=basePath%>user!login.action");
        $("#frm").submit();
        <%--var frm = document.getElementById("frm");--%>
        <%--frm.action = "<%=basePath%>user!login.action";--%>
        <%--frm.submit();--%>
    }
</script>
</head>

<body onkeypress="enter(event);">
<div class="mcss_top">
    <div class="logo"></div>
</div>
<div class="mcss_cent">
    <div class="icon"></div>
    <form action="" method="post" name="frm" id="frm" class="login_form">
        <h2></h2>
        <ul>
            <li>
                <div class="bt">用户名</div>
                <input class="ipt" type="text" id="loginName" name="loginName">

            </li>
            <li>
                <div class="bt">密　码</div>
                <input class="ipt" type="password" name="loginpassword" id="loginpassword">

            </li>
            <li class="yzm">
                <div class="bt">验证码</div>
                <div id="mc">
                    <canvas id="mc-canvas"></canvas>
                </div>
            </li>
        </ul>
        <div class="tishi">请在框中绘制形状通过验证</div>
        <div class="submit_btn">
            <a href="javascript:void();" onclick="submitFrm();"></a>
        </div>
        <input type="hidden" id="code" value="false">

    </form>
</div>
<div class="mcss_bot">© 2011 Sobey Digital Technology Co., Ltd.</div>
<div id="msg" style="display: none;"><s:actionerror/></div>
</body>
<script type="text/javascript" src="<%=basePath%>js/jquery.motionCaptcha.0.2.js"></script>
<script type="text/javascript">
    $("#login_form").find(".ipt").focusin(function () {
        $(this).addClass("focus");
    }).focusout(function () {
                $(this).removeClass("focus");
            })

    $("#login_form").find(".ipt1").hover(function () {
        $(this).addClass("focus");
    }, function () {
        $(this).removeClass("focus");
    })

    /*底部块定位*/
    function positonBot() {
        if ($(window).height() < 855) {
            $(".mcss_bot").css("position", "relative")
        } else {
            $(".mcss_bot").css("position", "absolute")
        }
    }
    positonBot();
    $(window).resize(function () {
        positonBot();
    })
</script>
</html>
