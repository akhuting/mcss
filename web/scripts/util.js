/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 11-5-20
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
var XMLHttpReq;
function createXMLHttpRequest() {
    try {
        XMLHttpReq = new ActiveXObject("Msxml2.XMLHTTP");//IE高版本创建XMLHTTP
    }
    catch(E) {
        try {
            XMLHttpReq = new ActiveXObject("Microsoft.XMLHTTP");//IE低版本创建XMLHTTP
        }
        catch(E) {
            XMLHttpReq = new XMLHttpRequest();//兼容非IE浏览器，直接创建XMLHTTP对象
        }
    }

}
function initChannel(obj) {
    if (obj == "null" || obj == "无") {
        return;
    }
    var cp = document.getElementById("channel");
    var options = cp.options;
    for (var i = 0; i < options.length; i++) {
        var option = options[i];
        if (option.value == obj) {
            option.selected = "selected";
        }
    }
}
function initCp(obj) {
    if (obj == "null" || obj == "无") {
        return;
    }
    var cp = document.getElementById("cp");
    var options = cp.options;
    for (var i = 0; i < options.length; i++) {
        var option = options[i];
        if (option.value == obj) {
            option.selected = "selected";
        }
    }
}
function sendAjax(url) {
    createXMLHttpRequest();                                //创建XMLHttpRequest对象
    XMLHttpReq.open("post", url, false);
    XMLHttpReq.onreadystatechange = channelCallback; //指定响应函数
    XMLHttpReq.send(null);
}
function sendAjaxRequest(url) {
    createXMLHttpRequest();                                //创建XMLHttpRequest对象
    XMLHttpReq.open("post", url, false);
    XMLHttpReq.onreadystatechange = processResponse; //指定响应函数
    XMLHttpReq.send(null);
}
function sendAjaxRequestPost(url,obj) {
    createXMLHttpRequest();                                //创建XMLHttpRequest对象
    XMLHttpReq.open("post", url, false);
    XMLHttpReq.onreadystatechange = function (){

    }; //指定响应函数
    XMLHttpReq.send("type=" + obj);
}
//回调函数
function processResponse() {
    if (XMLHttpReq.readyState == 4) {
        if (XMLHttpReq.status == 200) {
            var text = XMLHttpReq.responseText;

            /**
             *实现回调
             */
            text = window.decodeURI(text);
            if (text != "" && text.length > 0) {
                var cp = document.getElementById("cp");
                cp.innerHTML = "";
                var values = text.split("|");
                for (var i = 0; i < values.length; i++) {
                    var temp = document.createElement("option");
                    temp.text = values[i];
                    temp.value = values[i];
                    cp.options.add(temp);
                }
            }
        }
    }

}

function channelCallback() {
    if (XMLHttpReq.readyState == 4) {
        if (XMLHttpReq.status == 200) {
            var text = XMLHttpReq.responseText;
            /**
             *实现回调
             */
            text = window.decodeURI(text);
            if (text != "" && text.length > 0) {
                var cp = document.getElementById("channel");
                cp.innerHTML = "";
                var all = document.createElement("option");
                all.text = "所有频道";
                all.value = "all";
                cp.options.add(all);
                var values = text.split("|");
                for (var i = 0; i < values.length; i++) {
                    var temp = document.createElement("option");
                    temp.text = values[i];
                    temp.value = values[i];
                    cp.options.add(temp);
                }
            }

        }
    }
}

