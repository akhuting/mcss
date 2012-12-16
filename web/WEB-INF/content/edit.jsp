<%@ page import="com.sobey.mcss.domain.Userinfo" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 11-5-24
  Time: 下午12:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%

    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    List<Userinfo> list = (List<Userinfo>) request.getAttribute("users");
    Userinfo user = list.get(0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Admin Template</title>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-1.4.4.min.js"></SCRIPT>
    <script type="text/javascript" src="<%=basePath%>sliding.form.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/jquery.ztree-2.6.min.js"></script>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/common.js"></SCRIPT>
    <link href="<%=basePath%>styles/layout.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>styles/zTreeStyle/zTreeStyle.css">
    <!-- Theme Start -->
    <link href="<%=basePath%>themes/blue/styles.css" rel="stylesheet" type="text/css"/>
    <!-- Theme End -->
    <link rel="stylesheet" href="<%=basePath%>css/reg.css" type="text/css" media="screen"/>
    <link rel="stylesheet" href="<%=basePath%>themes/dark/jquery.ui.all.css">
    <script src="<%=basePath%>ui/jquery.ui.core.js"></script>
    <script src="<%=basePath%>ui/jquery.ui.widget.js"></script>
    <script src="<%=basePath%>ui/jquery.ui.mouse.js"></script>
    <script src="<%=basePath%>ui/jquery.ui.draggable.js"></script>
    <script src="<%=basePath%>ui/jquery.ui.position.js"></script>
    <script src="<%=basePath%>ui/jquery.ui.resizable.js"></script>
    <script src="<%=basePath%>ui/jquery.ui.dialog.js"></script>
    <script src="<%=basePath%>ui/jquery.ui.slider.js"></script>
    <style>
        span.reference {
            position: fixed;
            left: 5px;
            top: 5px;
            font-size: 10px;
            text-shadow: 1px 1px 1px #fff;
        }

        span.reference a {
            color: #555;
            text-decoration: none;
            text-transform: uppercase;
        }

        span.reference a:hover {
            color: #000;
        }

        h1 {
            color: #ccc;
            font-size: 36px;
            text-shadow: 1px 1px 1px #fff;
            padding: 20px;
        }
    </style>
    <script type="text/javascript">

        function checkdig(obj) {
            var value = $("#" + obj).val();
            $("#" + obj).val(value.replace(/[^\d]/g, ''));
        }
        var setting = {
            showLine: true,
            checkable: true,
            checkType: { "Y": "s", "N": "s" }
        };
        var zTree;
        $(document).ready(function () {

            var zTreeNodes;
            $.ajax({
                url: "<%=basePath%>cp!allCp.action",
                cache: false,
                async: false,
                success: function (html) {
                    zTreeNodes = eval(html);
                }
            });
            var cps = $("#cp").val().split(",");
            for (var i = 0; i < zTreeNodes.length; i++) {

                for (var j = 0; j < cps.length; j++) {
                    if (zTreeNodes[i].id == cps[j]) {
                        zTreeNodes[i].checked = true;
                    }
                }
                if (zTreeNodes[i].nodes != undefined) {
                    for (var j = 0; j < zTreeNodes[i].nodes.length; j++) {
                        for (var l = 0; l < cps.length; l++) {
                            if (zTreeNodes[i].nodes[j].id == cps[l]) {
                                zTreeNodes[i].nodes[j].checked = true;
                            }
                        }
                    }
                }
            }
            zTree = $("#tree").zTree(setting, zTreeNodes);

        });
        function reg() {
            var cks = zTree.getCheckedNodes();
            var cps = "";
            for (var i = 0; i < cks.length; i++) {
                if (i != 0) {
                    cps += "," + cks[i].id;
                } else {
                    cps += cks[i].id;
                }
            }
            $("#cp").val(cps);
        }
    </script>
</head>
<body id="homepage">


<!-- Right Side/Main Content Start -->
<div id="rightside">
    <!-- Alternative Content Box Start -->
    <div class="contentcontainer">
        <div class="headings alt">
            <h2>CP管理</h2>
        </div>
        <div id="content">
            <div id="wrapper">
                <div id="steps">
                    <form id="formElem" name="formElem" action="<%=basePath%>user!editUser.action" method="post">
                        <fieldset class="step">
                            <legend>账户</legend>
                            <p>
                                <label for="username">用户名</label>
                                <input id="username" name="loginName"
                                       value="<%=user.getUsername()%>"/>
                            </p>

                            <p>
                                <label for="email">邮箱</label>
                                <input id="email" name="email" placeholder="sobey@sobey.com"
                                       value="<%=user.getEmail()%>" type="email"
                                       AUTOCOMPLETE=OFF/>
                            </p>

                            <p>
                                <label for="password">密码</label>
                                <input id="password" name="loginpassword" type="password"
                                       value="_SOBEY_PASSWORD" AUTOCOMPLETE=OFF/>
                            </p>
                        </fieldset>
                        <fieldset class="step">
                            <legend>个人资料</legend>
                            <p>
                                <label for="name">昵称</label>
                                <input id="name" name="name" type="text" value="<%=user.getUserTruename()%>"
                                       AUTOCOMPLETE=OFF/>
                            </p>

                            <p>
                                <label for="phone">电话</label>
                                <input id="phone" name="phone" placeholder="123456789" value="<%=user.getCellphone()%>"
                                       type="tel"
                                       AUTOCOMPLETE=OFF/>
                            </p>

                            <p>
                                <label for="rank">排名</label>
                                <input id="rank" name="rank" value="<%=user.getUserRank()%>"/>
                            </p>

                        </fieldset>
                        <fieldset class="step">
                            <legend>域名设置</legend>
                            <ul id="tree" class="tree" style="width:300px;height: 200px; overflow-y: scroll;"></ul>
                        </fieldset>
                        <fieldset class="step">
                            <legend>演示流量设置</legend>
                            <p>
                                <label>静态加速</label>
                                <%
                                    String[] range = user.getUserRange().split("-");
                                %>
                                <input style="width: 50px;" onkeyup="checkdig('smin')" id="smin" name="smin"
                                       value="<%=range[0]%>"><input
                                    style="width: 50px;" id="smax" onkeyup="checkdig('smax')" name="smax"
                                    value="<%=range[1]%>">
                            </p>

                            <p>
                                <label>流媒体加速</label>
                                <input style="width: 50px;" onkeyup="checkdig('fmin')" id="fmin" name="fmin"
                                       value="<%=range[2]%>">&nbsp;<input
                                    style="width: 50px;" id="fmax" onkeyup="checkdig('fmax')" name="fmax"
                                    value="<%=range[3]%>">
                            </p>
                        </fieldset>
                        <fieldset class="step">
                            <legend>完成</legend>
                            <p> 如果所有的步骤有一个绿色的对号图标表明所有选项填写正确，
                                一个红色的对号图标表明，有些选项未填写。最后一切填写正确，请点击按钮完成修改 </p>

                            <p class="submit">
                                <button id="registerButton" type="submit" class="regButton">修改</button>
                            </p>
                            <input type="hidden" id="cp" name="cp" value="<%=user.getUserCp()%>"/>
                            <input type="hidden" id="id" name="id" value="<%=user.getUserid()%>"/>
                        </fieldset>
                    </form>
                </div>
                <div id="navigation" style="display:none;">
                    <ul>
                        <li class="selected"><a href="reg.jsp#">账户</a></li>
                        <li><a href="reg.jsp#">个人资料</a></li>
                        <li><a href="reg.jsp#">域名设置</a></li>
                        <li><a href="reg.jsp#">演示流量设置</a></li>
                        <li><a href="reg.jsp#">完成</a></li>
                    </ul>
                </div>
            </div>
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


<script type='text/javascript' src='../../scripts/jquery.wysiwyg.js'></script>
<script type='text/javascript' src='../../scripts/visualize.jQuery.js'></script>
<script type="text/javascript" src='../../scripts/functions.js'></script>

<!--[if IE 6]>
<script type='text/javascript' src='scripts/png_fix.js'></script>
<script type='text/javascript'>
    DD_belatedPNG.fix('img, .notifycount, .selected');
</script>
<![endif]-->
</body>
</html>
