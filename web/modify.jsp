<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 11-5-24
  Time: 下午1:27
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
    <link href="styles/layout.css" rel="stylesheet" type="text/css"/>
    <link href="styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
    <!-- Theme Start -->
    <link href="themes/blue/styles.css" rel="stylesheet" type="text/css"/>
    <!-- Theme End -->
    <script type="text/javascript">
        function ok(url) {
            $("#success").slideDown("slow");
            var secs = 5;
            for (var i = secs; i >= 0; i--) {
                window.setTimeout('doUpdate(' + i + ',"' + url + '")', (secs - i) * 1000);
            }
        }
        function doUpdate(num, url) {
            document.getElementById('succ').innerHTML = '<img src="img/icons/icon_success.png" alt="Success"/><span>密码修改成功!</span>将在' + num + '秒后自动跳转';
            if (num == 0) {
                window.top.location = url;
            }
        }
        $(document).ready(function() {
            parent.$("#detail").css({overflow:"hidden"});
            $("body").css({overflow:"hidden"});
        });
    </script>
</head>
<body id="homepage">
<!-- Right Side/Main Content Start -->
<div id="rightside">


    <!-- Green Status Bar Start -->
    <div class="status error" style="display: none" id="error">
        <p class="closestatus"><a href="javascript:showOrClose('#error')" title="Close">x</a></p>

        <p><img src="img/icons/icon_error.png" alt="Error"/><span>错误!</span> 密码不正确，请从新输入</p>
    </div>
    <!-- Green Status Bar End -->

    <div class="status success" id="success" style="display: none;">
        <p class="closestatus"><a href="javascript:showOrClose('#success')" title="Close">x</a></p>

        <p id="succ"><img src="img/icons/icon_success.png" alt="Success"/><span>成功!</span> 刷新成功.</p>
    </div>
    <script type="text/javascript">
        function submitFrm() {
            if ($(".correctbox").length == 4) {
                $.ajax({
                            url: "<%=basePath%>user!modify.action",
                            data:{loginName:$("#loginName").val(),loginpassword:$("#loginpassword").val(),newPassword:$("#newPassword").val()},
                            cache: false,
                            dataType:"script",
                            success: function(html) {
                            }
                        });
            }
        }
        function check(obj) {
            var p = $(obj);
            var span = p.parent().find("span");
            if (p.val() == "") {
                span.show();
                p.removeClass("correctbox");
                span.removeClass("green");
                p.addClass("errorbox");
                span.addClass("red");
                if (p.parent().find("img").length == 0) {
                    p.after("<img src='img/icons/icon_missing.png' alt='Error'/>");
                } else {
                    p.parent().find("img").attr("src", "img/icons/icon_missing.png");
                }
            } else {
                span.removeClass("red");
                p.removeClass("errorbox");
                p.addClass("correctbox");
                span.addClass("green");
                if (p.parent().find("img").length == 0) {
                    p.after("<img src='img/icons/icon_approve.png' alt='Error'/>");
                } else {
                    p.parent().find("img").attr("src", "img/icons/icon_approve.png");
                }
                $(span.get(1)).hide();

            }
        }

        function checkPassword(obj) {
            var p = $(obj);
            var password = $("#newPassword").val();
            var span = p.parent().find("span");
            if (p.val() == "") {
                span.show();
                p.removeClass("correctbox");
                span.removeClass("green");
                p.addClass("errorbox");
                span.addClass("red");
                if (p.parent().find("img").length == 0) {
                    p.after("<img src='img/icons/icon_missing.png' alt='Error'/>");
                } else {
                    p.parent().find("img").attr("src", "img/icons/icon_missing.png");
                }
            } else if (p.val() != password) {
                span.show();
                p.removeClass("correctbox");
                span.removeClass("green");
                p.addClass("errorbox");
                span.addClass("red");
                if (p.parent().find("img").length == 0) {
                    p.after("<img src='img/icons/icon_missing.png' alt='Error'/>");
                } else {
                    p.parent().find("img").attr("src", "img/icons/icon_missing.png");
                }
                $(span.get(1)).html("(两次密码不一致)");
            } else {
                span.removeClass("red");
                p.removeClass("errorbox");
                p.addClass("correctbox");
                span.addClass("green");
                $(span.get(1)).hide();
                if (p.parent().find("img").length == 0) {
                    p.after("<img src='img/icons/icon_approve.png' alt='Error'/>");
                } else {
                    p.parent().find("img").attr("src", "img/icons/icon_approve.png");
                }
            }
        }
    </script>
    <div class="contentcontainer med left">
        <div class="headings alt">
            <h2>账户管理</h2>
        </div>
        <div class="contentbox">
            <form action="#">
                <p>
                    <label for="loginName"><span><strong>用户名:</strong></span></label>
                    <input type="text" id="loginName" value="${sessionScope.user.username}" class="inputbox"
                           onblur="check(this)"/> <br/>
                    <span class="smltxt">(输入您要修改的用户名)</span>
                </p>
                <script type="text/javascript">
                    $("#loginName").blur();
                </script>
                <p>
                    <label for="loginpassword"><span><strong>密码:</strong></span></label>
                    <input type="password" id="loginpassword" class="inputbox" onblur="check(this)"/> <br/>
                    <span class="smltxt">(输入您要修改的密码)</span>
                </p>

                <p>
                    <label for="newPassword"><span><strong>新密码:</strong></span></label>
                    <input type="password" id="newPassword" class="inputbox" onblur="check(this)"/> <br/>
                    <span class="smltxt">(输入您要修改的新密码)</span>
                </p>

                <p>
                    <label for="confirmPassword"><span><strong>确认新密码:</strong></span></label>
                    <input type="password" id="confirmPassword" class="inputbox" onblur="checkPassword(this);"/> <br/>
                    <span class="smltxt">(再次输入您要修改的新密码)</span>
                </p>

                <p>
            </form>
            <%--<textarea class="=textarea" name="textfield" rows="10" cols="75"></textarea>--%>

            <%--<p><br/><br/>备注</p>--%>
            <input type="submit" value="提交" class="btn" onclick="submitFrm();"/>
        </div>
    </div>


    <div style="clear:both;"></div>



</div>
<!-- Right Side/Main Content End -->

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
