<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 11-5-24
  Time: 上午11:34
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
        function urls() {
            var url = $("#url").val();
            if (url == "" && url.length == 0) {
                $("#err").html("<img src='img/icons/icon_error.png' alt='Error'/><span>错误!</span>URL必须填写.</p>");
                $("#errorInfo").show();
            } else {
                $("#success").hide();
                $("#errorInfo").hide();
                $.ajax({
                            url: "<%=basePath%>/stat!refresh.action",
                            data:{item:url},
                            cache: false,
                            dataType:"text",
                            success: function(html) {
                                if (window.decodeURI(html) == "刷新成功") {
                                    $("#success").show();
                                } else {
                                    $("#err").html("<img src='<%=basePath%>img/icons/icon_error.png' alt='Error'/><span>错误!</span>" + window.decodeURI(html) + "</p>");
                                    $("#errorInfo").show();
                                }
                            }
                        });
            }
        }

        function dirs() {
            var dir = $("#dir").val();
            if (dir == "" && dir.length == 0) {
                $("#err").html("<img src='img/icons/icon_error.png' alt='Error'/><span>错误!</span>URL必须填写.</p>");
                $("#errorInfo").show();
            } else {
                $("#success").hide();
                $("#errorInfo").hide();
                $.ajax({
                            url: "<%=basePath%>/stat!refresh.action",
                            data:{item:dir},
                            cache: false,
                            dataType:"text",
                            success: function(html) {
                                if (window.decodeURI(html) == "刷新成功") {
                                    $("#success").slideDown("slow");
                                } else {
                                    $("#err").html("<img src='<%=basePath%>img/icons/icon_error.png' alt='Error'/><span>错误!</span>" + window.decodeURI(html) + "</p>");
                                    $("#errorInfo").slideDown("slow");
                                }
                            }
                        });
            }
        }
        $(document).ready(function(){
            parent.$("#detail").attr("scrolling","no");
            $("body").css({overflow:"hidden"});
        });
    </script>
</head>
<body id="homepage">


<!-- Right Side/Main Content Start -->
<div id="rightside">

    <!-- Blue Status Bar Start -->
    <div class="status info" id="dirInfo">
        <p class="closestatus"><a href="javascript:showOrClose('#dirInfo')" title="Close">x</a></p>

        <p><img src="img/icons/icon_info.png" alt="Information"/><span>目录刷新:</span>刷新目录以/结尾，或包含*字符，必须以协议://开头
            若目录下URL数量很大，不建议使用目录刷新</p>
    </div>

    <div class="status info" id="urlInfo">
        <p class="closestatus"><a href="javascript:showOrClose('#urlInfo')" title="Close">x</a></p>

        <p><img src="img/icons/icon_info.png" alt="Information"/><span>URL刷新:</span>每个URL一行，必须以协议://开头，如(http://)开头
            例如：http://www.sobey.com/chinese/mews.asp
            URL刷新每日推送任务上限为500
            提交刷新后5分钟生效
        </p>
    </div>
    <div class="status error" style="display: none;" id="errorInfo">
        <p class="closestatus"><a href="javascript:showOrClose('#errorInfo')" title="Close">x</a></p>

        <p id="err"><img src="img/icons/icon_error.png" alt="Error"/><span>错误!</span></p>
    </div>
    <div class="status success" id="success" style="display: none;">
        <p class="closestatus"><a href="javascript:showOrClose('#success')" title="Close">x</a></p>

        <p><img src="img/icons/icon_success.png" alt="Success"/><span>成功!</span> 刷新成功.</p>
    </div>

    <div class="contentcontainer med left" style="width: 50%;">
        <div class="headings alt">
            <h2>目录刷新</h2>
        </div>
        <div class="contentbox">

            <textarea class="text-input textarea" id="dir" name="textfield" rows="10" cols="75"></textarea>

            <input type="button" value="提交" onclick="dirs();" class="btn"/>
        </div>
    </div>

    <div class="contentcontainer sml right" id="tabs" style="width: 45%;">
        <div class="headings">
            <h2 class="left">URL刷新</h2>

        </div>
        <div class="contentbox">

            <textarea class="text-input textarea" id="url" name="textfield" rows="10" cols="68"></textarea>

            <input type="button" value="提交" onclick="urls();" class="btn"/>
        </div>

    </div>

    <div style="clear:both;"></div>
     <!--
    <div id="footer">
        Copyright &copy; 1997 - 2011 Sobey Digital Technology Co.Ltd
    </div>
     -->
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
