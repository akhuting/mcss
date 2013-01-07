<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 11-5-20
  Time: 下午4:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="<%=basePath%>styles/jquery.loadmask.css" type="text/css">
<link rel="stylesheet" href="<%=basePath%>styles/jquery.bubblepopup.v2.3.1.css" type="text/css">
<script src="<%=basePath%>js/jquery.bubblepopup.v2.3.1.min.js" type="text/javascript"></script>
<%
    String beginTime = "";
    String endTime = "";
    if (request.getAttribute("beginTime") != null) {
        beginTime = request.getAttribute("beginTime").toString();
    }
    if (request.getAttribute("endTime") != null) {
        endTime = request.getAttribute("endTime").toString();
    }
%>
<SCRIPT type="text/javascript" src="<%=basePath%>scripts/util.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=basePath%>scripts/dateUtil.js"></SCRIPT>
<SCRIPT type="text/javascript" src="<%=basePath%>scripts/jquery.loadmask.min.js"></SCRIPT>
<link rel="stylesheet" type="text/css" href="<%=basePath%>styles/easyui/easyui.css">


<script type="text/javascript" src="<%=basePath%>scripts/jquery.easyui.min.js"></script>
<script type="text/javascript">
function print() {
    var obj = new Object();
    obj.name = document.getElementsByTagName("h2")[0].innerHTML;
    obj.value = document.getElementById("idData").outerHTML;
    var sheight = screen.height - 70;
    var swidth = screen.width - 10;
    var winoption = "dialogHeight:" + sheight + "px;dialogWidth:" + swidth + "px;status:yes;scroll:yes;resizable:yes;center:yes";

    window.showModalDialog("../print.jsp", obj, winoption);
}
function excel() {
    var obj = new Object();
    obj.name = document.getElementsByTagName("h2")[0].innerHTML;
    obj.value = document.getElementById("idData").outerHTML;
    var sheight = screen.height - 70;
    var swidth = screen.width - 10;
    var winoption = "dialogHeight:" + sheight + "px;dialogWidth:" + swidth + "px;status:yes;scroll:yes;resizable:yes;center:yes";
//
//        window.open("../toExcel.jsp?tab=");
    var d = new Date();
    sendAjaxRequestPost("cp!field.action", obj.value);
}
function search() {
    window.parent.showMask();
    var start = document.getElementById("beginTime").value;
    var end = document.getElementById("endTime").value;
    if (start != "" && end != "") {
        try {
            if (countTimeLength("D", start, end) > 31) {
                alert("只能查询一个月的数据");
                window.parent.hideMask();
                return;
            }
        } catch (e) {
            alert("日期格式有误");
            window.parent.hideMask();
            return;
        }
    }
    var sf = document.getElementById("sform");
    sf.submit();
}
function toDay() {
    window.parent.showMask();
    document.getElementById("beginTime").value = getDateStr(0);
    document.getElementById("endTime").value = getDateStr(0);
    var sf = document.getElementById("sform");
    sf.submit();
}
function lastDay() {
    window.parent.showMask();
    document.getElementById("beginTime").value = getDateStr(-1);
    document.getElementById("endTime").value = getDateStr(-1);
    var sf = document.getElementById("sform");
    sf.submit();
}
function week() {
    window.parent.showMask();
    document.getElementById("beginTime").value = getWeekStartDate();
    document.getElementById("endTime").value = getWeekEndDate();
    var sf = document.getElementById("sform");
    sf.submit();
}
function month() {
    window.parent.showMask();
    document.getElementById("beginTime").value = getMonthStartDate();
    document.getElementById("endTime").value = getMonthEndDate();
    var sf = document.getElementById("sform");
    sf.submit();
}

//检查当前选择日期是今天，昨天，还是一周，一月
function checkCurrentDate() {
    var input_date = $("#beginTime").val();
    if (input_date.split(" ").length == 2) {
        input_date = input_date.split(" ")[0];
    }
    var input_end = $("#endTime").val();
    if (input_end.split(" ").length == 2) {
        input_end = input_end.split(" ")[0];
    }
    if (input_date == input_end) {
        if (input_date == getDateStr(0)) {
            setSelectEffects("today");
        }
        if (input_date == getDateStr(-1)) {
            setSelectEffects("yestday");
        }
    } else {
        if (input_date == getWeekStartDate() && input_end == getWeekEndDate()) {
            setSelectEffects("week");
        }
        if (input_date == getMonthStartDate() && input_end == getMonthEndDate()) {
            setSelectEffects("month");
        }
    }
}

function setSelectEffects(id) {
    var $effect = $("#" + id);
    $effect.css('opacity', '1.0');
    $effect.find("img").attr("src","<%=basePath%>img/icons/clock.png");
    setInterval(function () {
        if ($effect.css('opacity') == 1) {
            $effect.animate({opacity: 0.1});
        } else {
            $effect.animate({opacity: 1.0});
        }
    }, 800);
}
$(function () {
    checkCurrentDate();

    window.parent.hideMask();
    $('#beginTime').datetimepicker({
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

    $('#endTime').datetimepicker({
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
    getCP();
    <%--initCp('<%=request.getAttribute("cp")%>');--%>
    parent.$("#detail").attr("scrolling", "no");
    $("body").css({overflow: "hidden"});
    $('#today').CreateBubblePopup();
    $('#yestday').CreateBubblePopup();
    $('#week').CreateBubblePopup();
    $('#month').CreateBubblePopup();
    $('#excel').CreateBubblePopup();
    $('#print').CreateBubblePopup();

    $('#today').mouseover(function () {
        //show the bubble popup with new options
        $(this).ShowBubblePopup({
            innerHtml: '查看今天一天的信息！',
            innerHtmlStyle: {
                color: '#666',
                'text-align': 'center'
            },
            themeName: 'azure',
            themePath: '<%=basePath%>styles/jquerybubblepopup-theme'
        });
    });


    $('#yestday').mouseover(function () {
        //show the bubble popup with new options
        $(this).ShowBubblePopup({
            innerHtml: '查看昨天一天的信息！',
            innerHtmlStyle: {
                color: '#666',
                'text-align': 'center'
            },
            themeName: 'azure',
            themePath: '<%=basePath%>styles/jquerybubblepopup-theme'
        });
    });

    $('#week').mouseover(function () {
        //show the bubble popup with new options
        $(this).ShowBubblePopup({
            innerHtml: '查看最近一周的信息！',
            innerHtmlStyle: {
                color: '#666',
                'text-align': 'center'
            },
            themeName: 'azure',
            themePath: '<%=basePath%>styles/jquerybubblepopup-theme'
        });
    });

    $('#month').mouseover(function () {
        //show the bubble popup with new options
        $(this).ShowBubblePopup({
            innerHtml: '查看最近一月的信息！',
            innerHtmlStyle: {
                color: '#666',
                'text-align': 'center'
            },
            themeName: 'azure',
            themePath: '<%=basePath%>styles/jquerybubblepopup-theme'
        });
    });

    $('#excel').mouseover(function () {
        //show the bubble popup with new options
        $(this).ShowBubblePopup({
            innerHtml: '导出Excel！',
            innerHtmlStyle: {
                color: '#666',
                'text-align': 'center'
            },
            themeName: 'azure',
            themePath: '<%=basePath%>styles/jquerybubblepopup-theme'
        });
    });

    $('#print').mouseover(function () {
        //show the bubble popup with new options
        $(this).ShowBubblePopup({
            innerHtml: '打印列表信息！',
            innerHtmlStyle: {
                color: '#666',
                'text-align': 'center'
            },
            themeName: 'azure',
            themePath: '<%=basePath%>styles/jquerybubblepopup-theme'
        });
    });

});
</script>
<form action="" method="post" id="sform">
    <div class="extrabottom">
        <ul style="margin-top:6px;">
            <li>从 <input style="width: 110px;" type="text" name="beginTime" id="beginTime" value="<%=beginTime%>"/></li>
            <li>到 <input style="width: 110px;" type="text" name="endTime" id="endTime" value="<%=endTime%>"/></li>
            <li id="today" style="padding-top: 2px;"><a href="javascript:toDay();"
                                                        style="text-decoration: none;color: #666;"><img
                    src="<%=basePath%>img/icons/3.png" width="16" height="16" align="middle" id="todayi"/> 今天</a></li>
            <li id="yestday" style="padding-top: 2px;"><a href="javascript:lastDay();"
                                                          style="text-decoration: none;color: #666"><img
                    src="<%=basePath%>img/icons/3.png" width="16" height="16" align="middle"/> 昨天</a></li>
            <li id="week" style="padding-top: 2px;"><a href="javascript:week();"
                                                       style="text-decoration: none;color: #666"><img
                    src="<%=basePath%>img/icons/3.png" width="16" height="16" align="middle"/> 一周</a></li>
            <li id="month" style="padding-top: 2px;"><a href="javascript:month();"
                                                        style="text-decoration: none;color: #666"><img
                    src="<%=basePath%>img/icons/3.png" width="16" height="16" align="middle"/> 一月</a></li>
            <%--<li id="excel" style="padding-top: 2px;"><a href="javascript:void(0);"--%>
                                                        <%--style="text-decoration: none;color: #666"><img--%>
                    <%--src="<%=basePath%>img/icons/exDB.gif" width="16" height="16" align="middle"--%>
                    <%--title="导出Excel"/></a>--%>
            <%--</li>--%>
            <li id="print" style="padding-top: 2px;"><a href="javascript:void(0);" style="text-decoration: none"><img
                    src="<%=basePath%>img/icons/Printer.png" width="16" height="16" onclick="print();"
                    align="middle"
                    title="打印"/></a></li>
        </ul>
        <div class="bulkactions">
            <input id="cc" class="easyui-combotree"
                   value="<%=request.getAttribute("cp")==null ?"请选择域名" :request.getAttribute("cp")%>"
                   style="width:200px;" name="cp">
            <select id="channel" name="channel" style="display: none;">
                <option value="all"
                        selected="selected">所有频道
                </option>
            </select>
            <input type="button" onclick="search();" value="查询" class="btn"/>
        </div>
    </div>
</form>