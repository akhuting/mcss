<%@ page import="java.util.List" %>
<%@ page import="com.sobey.mcss.domain.Userinfo" %>
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
    String result = "0";
    if (request.getAttribute("result") != null) {
        result = request.getAttribute("result").toString();
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Admin Template</title>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-1.4.4.min.js"></SCRIPT>
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/common.js"></SCRIPT>
    <link href="<%=basePath%>styles/layout.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
    <!-- Theme Start -->
    <link href="<%=basePath%>themes/blue/styles.css" rel="stylesheet" type="text/css"/>
    <!-- Theme End -->
    <SCRIPT type="text/javascript" src="<%=basePath%>scripts/common.js"></SCRIPT>
    <script type="text/javascript">
        function reg() {
            window.location = "<%=basePath%>user!reg.action";
        }
        function edit(id) {
            window.location = "<%=basePath%>user!edit.action?id=" + id;
        }
        function del(id) {
            window.location = "<%=basePath%>user!deleteUser.action?id=" + id;
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

    <input type="hidden" id="result" value="<%=result%>"/>

    <div class="status error" style="display: none;" id="errorInfo">
        <p class="closestatus"><a href="javascript:showOrClose('#errorInfo')" title="Close">x</a></p>

        <p id="err"><img src="<%=basePath%>img/icons/icon_error.png" alt="Error"/><span>操作失败!</span></p>
    </div>

    <div class="status success" id="success" style="display: none;">
        <p class="closestatus"><a href="javascript:showOrClose('#success')" title="Close">x</a></p>

        <p><img src="<%=basePath%>img/icons/icon_success.png" alt="Success"/><span>成功!</span>操作成功.</p>
    </div>
    <script>
        var result = $("#result").val();

        if (result == "1") {
            $("#success").slideDown("slow");
        } else if (result == "2") {
            $("#errorInfo").slideDown("slow");
        }

    </script>

    <!-- Alternative Content Box Start -->
    <div class="contentcontainer">
        <div class="headings alt">
            <h2>CP管理</h2>
        </div>
        <div class="extrabottom" style="height: 20px;">
            <ul>

                <li><img src="<%=basePath%>styles/demoStyle/img/addNode.png" onclick="reg();" alt="新增用户" title="新增用户"
                         style="cursor:pointer" align="middle"></li>
            </ul>

        </div>

        <div class="contentbox" id="file">
            <table width="100%">
                <thead>
                <tr>
                    <th>用户名</th>
                    <th>昵称</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            Userinfo userinfo = list.get(i);
                            if (userinfo.getUserStatus() != 1) {
                %>
                <tr class="alt">
                    <td><%=userinfo.getUsername()%>
                    </td>
                    <td><%=userinfo.getUserTruename()%>
                    </td>
                    <td><a href="#" title=""><img src="<%=basePath%>img/icons/icon_edit.png" alt="Edit"
                                                  onclick="edit(<%=userinfo.getUserid()%>)" title="修改"/></a>
                        <a href="#" title=""><img src="<%=basePath%>img/icons/icon_delete.png" alt="Delete"
                                                  onclick="del(<%=userinfo.getUserid()%>)" title="删除"/></a>
                    </td>
                </tr>
                <%
                            }
                        }
                    }
                %>
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


<script type='text/javascript' src='<%=basePath%>scripts/jquery.wysiwyg.js'></script>
<script type='text/javascript' src='<%=basePath%>scripts/visualize.jQuery.js'></script>
<script type="text/javascript" src='<%=basePath%>scripts/functions.js'></script>

<!--[if IE 6]>
<script type='text/javascript' src='<%=basePath%>scripts/png_fix.js'></script>
<script type='text/javascript'>
    DD_belatedPNG.fix('img, .notifycount, .selected');
</script>
<![endif]-->
</body>
</html>
