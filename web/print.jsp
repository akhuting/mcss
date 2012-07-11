<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 11-5-17
  Time: 下午1:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Simple jsp page</title>
    <link href="styles/jquery-ui-1.8.6.custom.css" rel="stylesheet" type="text/css"/>
    <link href="styles/jquery-ui-timepicker-addon.css" rel="stylesheet" type="text/css"/>
    <link href="styles/layout.css" rel="stylesheet" type="text/css"/>
    <link href="styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
    <!-- Theme Start -->
    <link href="themes/blue/styles.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var getElementsByClassName = function(searchClass, node, tag) {
            if (document.getElementsByClassName) {
                return document.getElementsByClassName(searchClass)
            } else {
                node = node || document;
                tag = tag || "*";
                var classes = searchClass.split(" "),
                        elements = (tag === "*" && node.all) ? node.all : node.getElementsByTagName(tag),
                        patterns = [],
                        returnElements = [],
                        current,
                        match;
                var i = classes.length;
                while (--i >= 0) {
                    patterns.push(new RegExp("(^|\\s)" + classes[i] + "(\\s|$)"));
                }
                var j = elements.length;
                while (--j >= 0) {
                    current = elements[j];
                    match = false;
                    for (var k = 0, kl = patterns.length; k < kl; k++) {
                        match = patterns[k].test(current.className);
                        if (!match) break;
                    }
                    if (match) returnElements.push(current);
                }
                return returnElements;
            }
        }
        var obj = window.dialogArguments
    </script>

</head>
<body style="background-image: none;">
<span>
<span ></span> <b>
    <a href="###" onclick="this.style.visibility='hidden';window.print();this.style.visibility='visible'">[打印本页]</a></b></span>
<br/> <br/>

<div class="contentcontainer">
        <div class="headings alt">
            <h2 id="title"></h2>
        </div>

        <div id="contentbox">

        </div>

    </div>
<script>
    document.getElementById("title").innerHTML = obj.name;
    document.getElementById("contentbox").innerHTML = obj.value;
    var trs = document.getElementsByTagName("tr");
    for(var i = 0; i < trs.length; i++){
       if(trs[i].style.display == "none"){
           trs[i].style.display = "block";
       }
    }
</script>
</body>
</html>