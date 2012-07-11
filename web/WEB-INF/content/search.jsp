<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 11-5-20
  Time: 下午4:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        sendAjaxRequestPost("cp!field.action",obj.value);
    }
    function search() {
        var start = document.getElementById("beginTime").value;
        var end = document.getElementById("endTime").value;
        if (start != "" && end != "") {
            try {
                if (countTimeLength("D", start, end) > 31) {
                    alert("只能查询一个月的数据");
                    return;
                }
            } catch(e) {
                alert("日期格式有误");
                return;
            }
        }
        var sf = document.getElementById("sform");
        sf.submit();
    }
    function toDay() {
        document.getElementById("beginTime").value = getDateStr(0);
        document.getElementById("endTime").value = getDateStr(0);
        var sf = document.getElementById("sform");
        sf.submit();
    }
    function lastDay() {
        document.getElementById("beginTime").value = getDateStr(-1);
        document.getElementById("endTime").value = getDateStr(-1);
        var sf = document.getElementById("sform");
        sf.submit();
    }
    function week() {
        document.getElementById("beginTime").value = getWeekStartDate();
        document.getElementById("endTime").value = getWeekEndDate();
        var sf = document.getElementById("sform");
        sf.submit();
    }
    function month() {
        document.getElementById("beginTime").value = getMonthStartDate();
        document.getElementById("endTime").value = getMonthEndDate();
        var sf = document.getElementById("sform");
        sf.submit();
    }
    $(function() {
        $('#beginTime').datetimepicker({
                    prevText:"上一页",
                    nextText:"下一页",
                    currentText:"当前时间",
                    closeText:"关闭",
                    timeText:"时间",
                    hourText:"小时",
                    minuteText:"分钟",
                    monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                    dayNamesMin: ['日','一','二','三','四','五','六'],
                    hourGrid: 4,
                    minuteGrid: 10,
                    dateFormat:"yy-mm-dd"
                });

        $('#endTime').datetimepicker({
                    prevText:"上一页",
                    nextText:"下一页",
                    currentText:"当前时间",
                    closeText:"关闭",
                    timeText:"时间",
                    hourText:"小时",
                    minuteText:"分钟",
                    monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                    dayNamesMin: ['日','一','二','三','四','五','六'],
                    hourGrid: 4,
                    minuteGrid: 10 ,
                    dateFormat:"yy-mm-dd"
                });
        getCP();
        initCp('<%=request.getAttribute("cp")%>');
        parent.$("#detail").attr("scrolling", "no");
        $("body").css({overflow:"hidden"});
    });
</script>
<form action="" method="post" id="sform">
    <div class="extrabottom">
        <ul style="margin-top:6px;">
            <li>从 <input style="width: 110px;" type="text" name="beginTime" id="beginTime" value="<%=beginTime%>"/></li>
            <li>到 <input style="width: 110px;" type="text" name="endTime" id="endTime" value="<%=endTime%>"/></li>
            <li style="padding-top: 2px;"><a href="javascript:toDay();" style="text-decoration: none;color: #666;"><img
                    src="<%=basePath%>img/icons/3.png" width="16" height="16" align="middle"/> 今天</a></li>
            <li style="padding-top: 2px;"><a href="javascript:lastDay();" style="text-decoration: none;color: #666"><img
                    src="<%=basePath%>img/icons/3.png" width="16" height="16" align="middle"/> 昨天</a></li>
            <li style="padding-top: 2px;"><a href="javascript:week();" style="text-decoration: none;color: #666"><img
                    src="<%=basePath%>img/icons/3.png" width="16" height="16" align="middle"/> 一周</a></li>
            <li style="padding-top: 2px;"><a href="javascript:month();" style="text-decoration: none;color: #666"><img
                    src="<%=basePath%>img/icons/3.png" width="16" height="16" align="middle"/> 一月</a></li>
            <li style="padding-top: 2px;"><a href="javascript:void(0);" style="text-decoration: none;color: #666"><img
                    src="<%=basePath%>img/icons/exDB.gif" width="16" height="16" align="middle"
                    title="导出Excel"/></a>
            </li>
            <li style="padding-top: 2px;"><a href="javascript:void(0);" style="text-decoration: none"><img
                    src="<%=basePath%>img/icons/Printer.png" width="16" height="16" onclick="print();" align="middle"
                    title="打印"/></a></li>
        </ul>
        <div class="bulkactions">
            <select id="cp" name="cp">
                <option>选择域名.</option>
            </select>
            <select id="channel" name="channel" style="display: none;">
                <option value="all"
                        selected="selected">所有频道
                </option>
            </select>
            <input type="button" onclick="search();" value="查询" class="btn"/>
        </div>
    </div>
</form>