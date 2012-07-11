/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-3-1
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
function getDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate() + AddDayCount);
    var y = dd.getFullYear();
    var m = dd.getMonth() + 1;
    var d = dd.getDate();
    return formatDate(dd);
}
function StringToDate(string) {
    return new Date(Date.parse(string.replace(/-/g, "/")));
}
function countTimeLength(interval, date1, date2) {
    var objInterval = {'D' : 1000 * 60 * 60 * 24, 'H' : 1000 * 60 * 60, 'M' : 1000 * 60, 'S' : 1000, 'T' : 1};
    interval = interval.toUpperCase();
    var dt1 = Date.parse(StringToDate(date1));
    var dt2 = Date.parse(StringToDate(date2));
    try {
        return ((dt2 - dt1) / (objInterval[interval]));
    }
    catch (e) {
        return e.message;
    }
}

var now = new Date();                    //当前日期
var nowDayOfWeek = now.getDay();         //今天本周的第几天
var nowDay = now.getDate();              //当前日
var nowMonth = now.getMonth();           //当前月
var nowYear = now.getFullYear();             //当前年
nowYear += (nowYear < 2000) ? 1900 : 0;  //

//格式化日期：yyyy-MM-dd
function formatDate(date) {
    var myyear = date.getFullYear();
    var mymonth = date.getMonth() + 1;
    var myweekday = date.getDate();

    if (mymonth < 10) {
        mymonth = "0" + mymonth;
    }
    if (myweekday < 10) {
        myweekday = "0" + myweekday;
    }
    return (myyear + "-" + mymonth + "-" + myweekday);
}

//获得某月的天数
function getMonthDays(myMonth) {
    var monthStartDate = new Date(nowYear, myMonth, 1);
    var monthEndDate = new Date(nowYear, myMonth + 1, 1);
    var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
    return   days;
}

//获得本季度的开始月份
function getQuarterStartMonth() {
    var quarterStartMonth = 0;
    if (nowMonth < 3) {
        quarterStartMonth = 0;
    }
    if (2 < nowMonth && nowMonth < 6) {
        quarterStartMonth = 3;
    }
    if (5 < nowMonth && nowMonth < 9) {
        quarterStartMonth = 6;
    }
    if (nowMonth > 8) {
        quarterStartMonth = 9;
    }
    return quarterStartMonth;
}

//获得本周的开始日期
function getWeekStartDate() {
    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
    weekStartDate.setDate(weekStartDate.getDate() + 1);
    return formatDate(weekStartDate);
}

//获得本周的结束日期
function getWeekEndDate() {
    var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
    weekEndDate.setDate(weekEndDate.getDate() + 1);
    return formatDate(weekEndDate);
}

//获得本月的开始日期
function getMonthStartDate() {
    var monthStartDate = new Date(nowYear, nowMonth, 1);
    return formatDate(monthStartDate);
}

//获得本月的结束日期
function getMonthEndDate() {
    var monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));
    return formatDate(monthEndDate);
}

//获得本季度的开始日期
function getQuarterStartDate() {

    var quarterStartDate = new Date(nowYear, getQuarterStartMonth(), 1);
    return formatDate(quarterStartDate);
}

//或的本季度的结束日期
function getQuarterEndDate() {
    var quarterEndMonth = getQuarterStartMonth() + 2;
    var quarterStartDate = new Date(nowYear, quarterEndMonth, getMonthDays(quarterEndMonth));
    return formatDate(quarterStartDate);
}

