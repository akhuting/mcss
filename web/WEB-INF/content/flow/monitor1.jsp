<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 11-5-18
  Time: 下午5:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
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
<link href="<%=basePath%>styles/layout.css" rel="stylesheet" type="text/css"/>
<link href="<%=basePath%>styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="<%=basePath%>themes/base/jquery.ui.all.css">
<!-- Theme Start -->
<link href="<%=basePath%>themes/blue/styles.css" rel="stylesheet" type="text/css"/>
<!-- Theme End -->
<SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery-1.4.4.min.js"></SCRIPT>
<script type="text/javascript" src="<%=basePath%>scripts/FusionCharts.js"></script>
<SCRIPT type="text/javascript" src="<%=basePath%>scripts/util.js"></SCRIPT>
<script src="<%=basePath%>ui/jquery.ui.core.js"></script>
<script src="<%=basePath%>ui/jquery.ui.widget.js"></script>
<script src="<%=basePath%>ui/jquery.ui.mouse.js"></script>
<script src="<%=basePath%>ui/jquery.ui.draggable.js"></script>
<script src="<%=basePath%>ui/jquery.ui.position.js"></script>
<script src="<%=basePath%>ui/jquery.ui.resizable.js"></script>
<script src="<%=basePath%>ui/jquery.ui.dialog.js"></script>
<script type="text/javascript">
var current = 1;
function showList(id, srcobj) {
    $("#" + id).slideToggle("slow");
    $("#" + id + "Count").slideToggle("slow");
    $("#" + id + "span").slideToggle("slow");
    var src = srcobj.src;
    if (src.indexOf("down") != -1) {
        src = src.replace("down", "up");
    } else {
        src = src.replace("up", "down");
    }
    srcobj.src = src;
}
function add(obj) {
    $("." + obj).toggle();
}
function hideAll(obj) {
    $("#totalContent").hide();
    $("#webContent").hide();
    $("#mediaContent").hide();
    $("#" + obj + "Content").show();
}
function ajaxchart(html, render) {
    $("#dialog").dialog("destroy");
    var arr = html.split("|");
    $("#" + render + "p").html("当前条数：" + arr[3] + "条");
    if (FusionCharts("speed") != undefined) {
        FusionCharts("speed").dispose();
    }
    var speedbit = new FusionCharts("<%=basePath%>swf/FCBar.swf", "speed", "49%", "300", "0", "0");
    speedbit.addEventListener('BeforeLinkedItemOpen', function(e, a) {
        var dialog = $('#dialog').dialog({
            autoOpen: false,
            width: 850, height: 320, title : '域名详情'
        });
        dialog.dialog("open");
    });
    speedbit.configureLink({
        width : '100%',
        height: '100%',
        renderAt : "dialog",
        overlayButton: { show : false }
    }, 0);

    speedbit.setDataXML(arr[0]);
    speedbit.setTransparent(true);
    speedbit.render(render + "sb");
    if (FusionCharts("flow") != undefined) {
        FusionCharts("flow").dispose();
    }
    var flow = new FusionCharts("<%=basePath%>swf/FCBar.swf", "flow", "50%", "300", "0", "0");
    flow.configureLink({
        width : '100%',
        height: '100%',
        renderAt : "dialog",
        overlayButton: { show : false }
    }, 0);
    flow.addEventListener('BeforeLinkedItemOpen', function(e, a) {
        var dialog = $('#dialog').dialog({
            autoOpen: false,
            width: 850, height: 320, title : '域名详情'

        });
        dialog.dialog("open");
    });

    flow.setDataXML(arr[1]);
    flow.setTransparent(true);
    flow.render(render + "fl");

    if (arr.length > 2) {
        var chart = document.getElementById(render);
        if (chart.children.length == 1) {
            var span = document.createElement("span");
            span.style.paddingLeft = "70px";
            span.id = render + "Listspan";
            span.style.display = "none";
            span.innerHTML = arr[2];
            chart.appendChild(span);
        } else {
            chart.children[1].innerHTML = arr[2];
        }
    }
    hideAll(render);
}
var times = 0;
function referrer() {
    if (current == 1) {
        $.ajax({
            url: "<%=basePath%>flow/flow!ajaxWebUser1.action?page=" + webpage,
            cache: false,
            dataType:"html",
            async:true,
            success: function(html) {
                ajaxchart(html, "web");
                if (times == 0) {
                    times = 1;
                } else if (times == 1) {
                    times = 2;
                } else if (times == 2) {
                    times = 3;
                } else if (times == 3) {
                    current = 2;
                    times = 0;
                }
            }
        });
    }
    if (current == 2) {
        $.ajax({
            url: "<%=basePath%>flow/flow!ajaxMediaUser1.action?page=" + mediapage,
            cache: false,
            dataType:"html",
            async:true,
            success: function(html) {
                ajaxchart(html, "media");
                if (times == 0) {
                    times = 1;
                } else if (times == 1) {
                    times = 2;
                } else if (times == 2) {
                    times = 3;
                } else if (times == 3) {
                    current = 3;
                    times = 0;
                }
            }
        });
    }
    if (current == 3) {

        $.ajax({
            url: "<%=basePath%>flow/flow!ajaxTotalUser1.action?page=" + totalpage,
            cache: false,
            dataType:"html",
            async:true,
            success: function(html) {
                ajaxchart(html, "total");
                if (times == 0) {
                    times = 1;
                } else if (times == 1) {
                    times = 2;
                } else if (times == 2) {
                    times = 3;
                } else if (times == 3) {
                    current = 1;
                    times = 0;
                }
            }
        });
    }

}
var intervalId = window.setInterval(referrer, 10000);
$(document).ready(function() {
    referrer();
    initSelectCount("webList");
    initSelectCount("totalList");
    initSelectCount("mediaList");
});

function initSelectCount(obj) {
    var obj = $("#" + obj + "Count");
    obj.html("");
    obj.append("<option value='0'>选择显示条数</option>");
    for (var i = 1; i <= 20; i++) {
        obj.append("<option value='" + i + "'>" + i + "</option>");
    }
}
var webpage = 0;
var mediapage = 0;
var totalpage = 0;
function selectCount(value, obj) {
    clearInterval(intervalId);
    if (obj == "Web") {
        webpage = value;
        $("#webp").html("当前条数：" + value + "条");
    }
    if (obj == "Media") {
        mediapage = value;
        $("#mediap").html("当前条数：" + value + "条");
    }
    if (obj == "Total") {
        totalpage = value;
        $("#totalp").html("当前条数：" + value + "条");
    }
    $.ajax({
        url: "<%=basePath%>flow/flow!ajax" + obj + "User1.action?page=" + value,
        cache: false,
        dataType:"html",
        async:false,
        success: function(html) {
            $("#dialog").dialog("destroy");
            var arr = html.split("|");
            if (FusionCharts("speed") != undefined) {
                FusionCharts("speed").dispose();
            }
            var speedbit = new FusionCharts("<%=basePath%>swf/FCBar.swf", "speed", "49%", "300", "0", "0");
            speedbit.setDataXML(arr[0]);
            speedbit.setTransparent(true);
            speedbit.render(obj.toLowerCase() + "sb");
            speedbit.addEventListener('BeforeLinkedItemOpen', function(e, a) {
                var dialog = $('#dialog').dialog({
                    autoOpen: false,
                    width: 850, height: 320, title : '域名详情'

                });
                dialog.dialog("open");
            });
            speedbit.configureLink({
                width : '100%',
                height: '100%',
                renderAt : "dialog",
                overlayButton: { show : false }
            }, 0);
            if (FusionCharts("flow") != undefined) {
                FusionCharts("flow").dispose();
            }
            var flow = new FusionCharts("<%=basePath%>swf/FCBar.swf", "flow", "50%", "300", "0", "0");
            flow.setDataXML(arr[1]);
            flow.setTransparent(true);
            flow.render(obj.toLowerCase() + "fl");
            flow.configureLink({
                width : '100%',
                height: '100%',
                renderAt : "dialog",
                overlayButton: { show : false }
            }, 0);
            flow.addEventListener('BeforeLinkedItemOpen', function(e, a) {
                var dialog = $('#dialog').dialog({
                    autoOpen: false,
                    width: 850, height: 320, title : '域名详情'

                });
                dialog.dialog("open");
            });
        }
    });
    intervalId = window.setInterval(referrer, 10000);
}
</script>
</head>
<body style="background-image: url('');">
<div id="dialog"></div>
<!-- Right Side/Main Content Start -->
<div id="rightside" style="margin-left:25px;margin-top: 0px;">
    <div class="headings alt" style="text-align: center;height: 50px;">
        <h2 style="font-size: 35px;padding-top: 5px;">索贝MDN客户加速数据监控平台</h2>
    </div>
    <div class="contentcontainer" style="margin-bottom:25px;display: none;" id="totalContent">
        <div class="contentbox" id="total">
        </div>
        <img src="<%=basePath%>img/icons/down.png" alt="" onclick="showList('totalList',this)"
             style="float: right;margin-top: 5px;cursor: pointer">
        <script type="text/javascript">
            var myChart = new FusionCharts("<%=basePath%>swf/RealTimeLine.swf", "myChart", "100%", "350", "0", "0");
            myChart.setDataXML("<%=map.get("totalXml")%>");
            myChart.setTransparent(true);
            myChart.render("total");
        </script>
        <div class="contentbox" id="totalList">
            <span id="totalsb"></span>
            <span id="totalfl"></span>
        </div>
        <div style="margin-top: 10px;">
            <select id="totalListCount" onchange="selectCount(this.value,'Total');">
                <option>选择显示条数</option>
            </select>
            <span style="font-size: 13px;" id="totalp"></span>
        </div>
    </div>
    <!-- Alternative Content Box Start -->
    <div class="contentcontainer" style="margin-bottom:25px;display: none;" id="webContent">
        <div class="contentbox" id="web"></div>
        <img src="<%=basePath%>img/icons/down.png" alt="" onclick="showList('webList',this)"
             style="float: right;margin-top: 5px;cursor: pointer">
        <script type="text/javascript">
            var myChart1 = new FusionCharts("<%=basePath%>swf/RealTimeLine.swf", "myChart1", "100%", "350", "0", "0");
            myChart1.setDataXML("<%=map.get("webXml")%>");
            myChart1.setTransparent(true);
            myChart1.render("web");
        </script>
        <div class="contentbox" id="webList">
            <span id="websb"></span>
            <span id="webfl"></span>
        </div>
        <div style="margin-top: 10px;">
            <select id="webListCount" onchange="selectCount(this.value,'Web');">
                <option>选择显示条数</option>
            </select>
            <span style="font-size: 13px;" id="webp"></span>
        </div>
    </div>
    <!-- Alternative Content Box End -->
    <div class="contentcontainer" style="margin-bottom:25px;display: none;" id="mediaContent">
        <div class="contentbox" id="media"></div>
        <img src="<%=basePath%>img/icons/down.png" alt="" onclick="showList('mediaList',this)"
             style="float: right;margin-top: 5px;cursor: pointer">
        <script type="text/javascript">
            var myChart2 = new FusionCharts("<%=basePath%>swf/RealTimeLine.swf", "myChart2", "100%", "350", "0", "0");
            myChart2.setDataXML("<%=map.get("mediaXml")%>");
            myChart2.setTransparent(true);
            myChart2.render("media");
        </script>
        <div class="contentbox" id="mediaList">
            <span id="mediasb"></span>
            <span id="mediafl"></span>
        </div>
        <div style="margin-top: 10px;">
            <select id="mediaListCount" onchange="selectCount(this.value,'Media');">
                <option>选择显示条数</option>
            </select>
            <span style="font-size: 13px;" id="mediap"></span>
        </div>
    </div>

    <div style="clear:both;"></div>

</div>
</body>
</html>

