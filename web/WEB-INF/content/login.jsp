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
    <title>MCSS - Login</title>
    <link href="<%=basePath%>styles/layout.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/login.css" rel="stylesheet" type="text/css"/>
    <!-- Theme Start -->
    <link href="<%=basePath%>themes/blue/styles.css" rel="stylesheet" type="text/css"/>
    <!-- Theme End -->
    <script type="text/javascript" src="<%=basePath%>scripts/dialog/artDialog.min.js"></script>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery.min.js"></SCRIPT>
    <%--<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6/jquery.min.js"></script>--%>
    <link href="<%=basePath%>scripts/dialog/skin/aero.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>css/jquery.motionCaptcha.0.2.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        $(document).ready(function (){
            if(parent.document.getElementById("detail")!=null){
                parent.document.location.href = "<%=basePath%>";
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
                var nameDialog = art.dialog({
                            lock: true,
                            id:"id1",
                            follow: document.getElementById("loginName"),
                            skin: 'aero',
                            icon: 'error',
                            content: '用户名不能为空',
                            closeFn:function() {
                                document.getElementById("loginName").focus();
                            }
                        });
                return;
            }
            if (document.getElementById("loginpassword").value == "") {
                var passwordDialog = art.dialog({
                            lock: true,
                            id:"id2",
                            follow:document.getElementById("loginpassword"),
                            skin: 'aero',
                            icon: 'error',
                            content: '密码不能为空',
                            closeFn:function() { document.getElementById("loginpassword").focus(); }
                        });
                return;
            }
            if (document.getElementById("code").value == "false") {
                var passwordDialog = art.dialog({
                    lock: true,
                    id:"id2",
                    follow:document.getElementById("loginpassword"),
                    skin: 'aero',
                    icon: 'error',
                    content: '请绘制验证码',
                    closeFn:function() { }
                });
                return;
            }
            $("#frm").attr("action", "<%=basePath%>user!login.action");
            $("#frm").submit();
            <%--var frm = document.getElementById("frm");--%>
            <%--frm.action = "<%=basePath%>user!login.action";--%>
            <%--frm.submit();--%>
        }
        function forget() {
            var userName = document.getElementById("fn");
            var email = document.getElementById("fe");
            if (userName.value == "" && userName.value.length == 0) {
//                alert("用户名不能为空");
                userName.focus();
                return;
            }
            if (email.value == "" && email.value.length == 0) {
//                alert("邮箱不能为空");
                email.focus();
                return;
            }
            $("#bModal").css("z-index","1980");
            $("#notificationsbox").css("z-index","1981");
            $.ajax({
                        url: "<%=basePath%>user!forget.action",
                        data:{loginName:userName.value,email:email.value},
                        cache: false,
                        dataType:"script",
                        success: function(html) {
                        }
                    });
        }
    </script>
</head>
<body onkeypress="enter(event);">
<div id="logincontainer">
    <div id="loginbox">
        <div id="loginheader">
            <img src="<%=basePath%>img/cp_logo_login.png" alt="Control Panel Login"/>
        </div>
        <div id="innerlogin">
            <form action="" method="post" name="frm" id="frm">
                <p>用户名:</p>
                <input type="text" id="loginName" name="loginName" class="logininput" value="<%=request.getAttribute("loginName")==null ?"":request.getAttribute("loginName")%>"/>

                <p><font color="red" style="font-size: 13px;"><s:actionerror/></font></p>

                <p>密码:</p>
                <input type="password" name="loginpassword" id="loginpassword" class="logininput"/>

                <div id="mc">
                    <p>请在框中绘制形状通过验证:</p>
                    <canvas id="mc-canvas" style="margin-top: 10px;"></canvas>
                </div>
                <input disabled="disabled"  id="sb" type="button" value="提交" onclick="submitFrm();">
                 <input type="hidden" id="code" value="false">
                   <%--<a href="" title="忘记密码" class="notifypop">忘记密码?</a>--%>
            </form>
        </div>
    </div>
    <img src="<%=basePath%>img/login_fade.png" alt="Fade"/>
</div>

<div id="notificationsbox">
    <h4>忘记密码</h4>
    <ul>
        <li>
            <p style="font-weight:700;font-size: 13px;">用户名:</p>
            <input class="logininput" id="fn" style="padding-top: 0px;padding-bottom: 0px;" type="text"/>
        </li>
        <li>
            <p style="font-weight:700;font-size: 13px;">注册邮箱:</p>
            <input class="logininput" id="fe" style="padding-top: 0px;padding-bottom: 0px;" type="text"/>
        </li>
    </ul>

    <div id="innerlogin">
        <div>
            <p><a href="javascript:forget();" title="忘记密码">提交</a></p>
        </div>
    </div>


</div>
<script type="text/javascript" src='<%=basePath%>scripts/functions.js'></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.motionCaptcha.0.2.js"></script>
</body>
</html>