<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 11-5-24
  Time: 下午1:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    String result = request.getParameter("result");
    if (result == null) {
        result = "";
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>Admin Template</title>
<link href="styles/layout.css" rel="stylesheet" type="text/css"/>
<link href="styles/wysiwyg.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="styles/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="styles/demoStyle/demo.css" type="text/css">
<link href="styles/jquery.bubblepopup.v2.3.1.css" rel="stylesheet" type="text/css"/>
<!-- Theme Start -->
<link href="themes/blue/styles.css" rel="stylesheet" type="text/css"/>
<!-- Theme End -->
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.ztree-2.6.min.js"></script>
<script src="js/jquery.bubblepopup.v2.3.1.min.js" type="text/javascript"></script>
<script src="js/jquery.json-2.3.min.js" type="text/javascript"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
var zTree1;
var setting;

setting = {
//    checkable : true,
    expandSpeed : "slow",
    callback: {
        beforeClick: zTreeBeforeClick,
        click:    zTreeOnClick,
        remove:removeTreeNode
    }
};
setting.editable = true;
setting.edit_renameBtn = true;
setting.edit_removeBtn = true;
setting.dragCopy = false;
setting.dragMove = false;

var tr;
$(document).ready(function() {
    $.ajax({
                url: "<%=basePath%>cp!allCp.action",
                cache: false,
                async:false,
                success: function(html) {
                    tr = eval(html);
                }
            });
    reloadTree();
    initCopyEvent();
    //create bubble popups for each element with class "button"
    $('.IphoneAddButton').CreateBubblePopup();

    //set customized mouseover event for each button
    $('.IphoneAddButton').mouseover(function() {

        //show the bubble popup with new options
        $(this).ShowBubblePopup({
                    innerHtml: '点我可以添加CP哦!',
                    innerHtmlStyle: {
                        color: '#666',
                        'text-align':'center'
                    },
                    themeName:     $(this).attr('id'),
                    themePath:     'styles/jquerybubblepopup-theme'
                });
    });


    $('.IphoneDoneButton').CreateBubblePopup();

    //set customized mouseover event for each button
    $('.IphoneDoneButton').mouseover(function() {

        //show the bubble popup with new options
        $(this).ShowBubblePopup({
                    innerHtml: '点我可以保存CP哦!',
                    innerHtmlStyle: {
                        color: '#666',
                        'text-align':'center'
                    },
                    themeName:     $(this).attr('id'),
                    themePath:     'styles/jquerybubblepopup-theme'
                });
    });
});


function zTreeOnClick(event, treeId, treeNode) {
    $("#renameTxt").attr("value", treeNode.name);
}
function zTreeBeforeClick(treeId, treeNode) {
    var canClick = (treeNode != zTree1.getSelectedNode());
    if (!canClick) {
        zTree1.cancelSelectedNode();
    }
    return canClick;
}


function expandAll(expandSign) {
    zTree1.expandAll(expandSign);
}

function expandNode(expandSign) {
    var srcNode = zTree1.getSelectedNode();
    if (srcNode) {
        zTree1.expandNode(srcNode, expandSign, $("#sonChk").attr("checked"));
    }
}

var addCount = 1;
function addNode(type) {
    var srcNode = zTree1.getSelectedNode();
    var newNode = [
        { name:"cp" + (addCount++),id :0}
    ];
    if(srcNode){
        if(srcNode.isParent || srcNode.level == 0){
            zTree1.addNodes(srcNode, newNode);
        }else{
            zTree1.addNodes(null, newNode);
        }
    }else{
        zTree1.addNodes(null, newNode);
    }

}
var del = "0";
function removeTreeNode(event, treeId, treeNode){
    var srcNode = zTree1.getSelectedNode();

    if (srcNode) {
        if (srcNode.id != 0) {
            if (srcNode.nodes && srcNode.nodes.length > 0) {
                for(var i = 0; i < srcNode.nodes.length; i++){
                    var node = srcNode.nodes[i];
                    del += "," + node.id;
                }

            } else {
                del += "," + srcNode.id;

            }
        }
        zTree1.removeNode(srcNode);
    }
    if (treeNode) {
        if (treeNode.id != 0) {
            if (treeNode.nodes && treeNode.nodes.length > 0) {
                for(var i = 0; i < treeNode.nodes.length; i++){
                    var node = treeNode.nodes[i];
                    del += "," + node.id;
                }

            } else {
                del += "," + treeNode.id;

            }
        }
        zTree1.removeNode(treeNode);
    }
}

function getPreTreeNode(treeNode) {
    if (treeNode.isFirstNode) return null;
    var nodes = treeNode.parentNode ? treeNode.parentNode.nodes : zTree1.getNodes();
    var preNode;
    for (var i = 0; i < nodes.length; i++) {
        if (nodes[i] == treeNode) {
            return preNode;
        }
        preNode = nodes[i];
    }
}
function getNextTreeNode(treeNode) {
    if (treeNode.isLastNode) return null;
    var nodes = treeNode.parentNode ? treeNode.parentNode.nodes : zTree1.getNodes();
    for (var i = 0; i < nodes.length; i++) {
        if (nodes[i] == treeNode) {
            return nodes[i + 1];
        }
    }
}


function makeListName(node) {
    var space = "";
    for (var s = 0; s < node.level; s++) {
        space = space + "--";
    }
    return space + node.name + "(" + node.tId + ")";
}

function renameTreeNode(txtObj) {
    var srcNode = zTree1.getSelectedNode();
    if (!srcNode) {
        alert("请先选中一个节点");
        $("#renameTxt").attr("value", "");
        return;
    }
    if (srcNode.name != txtObj.value) {
        srcNode.name = txtObj.value;
        zTree1.updateNode(srcNode);
        zTree1.selectNode(srcNode);
    }

}
function checkTreeNode(checked) {
    var srcNode = zTree1.getSelectedNode();
    if (!srcNode) {
        alert("请先选中一个节点");
        return;
    }
    var oldNodes = clone(zTree1.getNodes());
    var oldcheckNum = zTree1.getCheckedNodes(checked).length;
    var connFlag = $("#connTrue").attr("checked");
    srcNode.checked = checked;
    zTree1.updateNode(srcNode, connFlag);
    var newcheckNum = zTree1.getCheckedNodes(checked).length;
}
function checkAllTreeNode(checked) {
    var oldNodes = clone(zTree1.getNodes());
    var oldcheckNum = zTree1.getCheckedNodes(checked).length;
    zTree1.checkAllNodes(checked);
    var newcheckNum = zTree1.getCheckedNodes(checked).length;
}
function zShowChk(flag) {
    var srcNode = zTree1.getSelectedNode();
    if (!srcNode) {
        alert("请先选中一个节点");
        return;
    }
    if (!srcNode.nocheck === flag) return;
    var oldNodes = clone(zTree1.getNodes());
    srcNode.nocheck = !flag;
    zTree1.updateNode(srcNode);
}
function reloadTree(node) {
    zTree1 = $("#treeDemo").zTree(setting, node ? node : tr);
    $("#renameTxt").attr("value", "");
}


function initCopyEvent() {
    $("#jRollback").bind("dblclick", noSelection);
    $("#jRedo").bind("dblclick", noSelection);
}
function noSelection(event) {
    event.target.blur();
    window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
    return false;
}


var opreatePool = [];
var lastNode = null;
var poolCursor = -1;
var poolMax = 20;
function clone(jsonObj, newName) {
    var buf;
    if (jsonObj instanceof Array) {
        buf = [];
        var i = jsonObj.length;
        while (i--) {
            buf[i] = clone(jsonObj[i], newName);
        }
        return buf;
    } else if (typeof jsonObj == "function") {
        return jsonObj;
    } else if (jsonObj instanceof Object) {
        buf = {};
        for (var k in jsonObj) {
            if (k != "parentNode") {
                buf[k] = clone(jsonObj[k], newName);
                if (newName && k == "name") buf[k] += newName;
            }
        }
        return buf;
    } else {
        return jsonObj;
    }
}


function ok() {

    var node = zTree1.getNodes();
    var arr = new Array();
    for (var i = 0; i < node.length; i++) {
        var obj;
        if(node[i].nodes && node[i].nodes.length > 0){
            var a = new Array();
            for(var j = 0; j < node[i].nodes.length; j++){
                var temp = {name:node[i].nodes[j].name , id:node[i].nodes[j].id};
                a.push(temp);
            }
            obj = {name:node[i].name , id:node[i].id , nodes:a};
        }else{
            obj = {name:node[i].name , id:node[i].id};
        }
        arr.push(obj);
    }
    $.ajax({
                url: "<%=basePath%>cp!saveCp.action",
                data:{item:jQuery.toJSON(arr),field:del},
                type:"POST",
                cache: false,
                success: function(html) {
                    if (html == "1") {
                        window.location.href = "<%=basePath%>cpManager.jsp?result=1";
                    } else {
                        window.location.href = "<%=basePath%>cpManager.jsp?result=2";

                    }
                }
            });
}
function showOrClose(obj) {
    var addTop = new Number(($(".IphoneAddButton").css("top").split("px")[0])) - 60;
    $(".IphoneAddButton").css("top", addTop + "px");
    var doneTop = new Number(($(".IphoneDoneButton").css("top").split("px")[0])) - 60;
    $(".IphoneDoneButton").css("top", doneTop + "px");
    $(obj).slideUp("slow");
}
  $(document).ready(function(){
            parent.$("#detail").attr("scrolling","no");
      $("body").css({overflow:"hidden"});
        });
//-->
</SCRIPT>
</head>
<body id="homepage">

<!-- Top Breadcrumb End -->

<!-- Right Side/Main Content Start -->
<div id="rightside">
    <input type="hidden" id="result" value="<%=result%>"/>

    <div class="status error" style="display: none;" id="errorInfo">
        <p class="closestatus"><a href="javascript:showOrClose('#errorInfo')" title="Close">x</a></p>

        <p id="err"><img src="img/icons/icon_error.png" alt="Error"/><span>CP操作失败!</span></p>
    </div>

    <div class="status success" id="success" style="display: none;">
        <p class="closestatus"><a href="javascript:showOrClose('#success')" title="Close">x</a></p>

        <p><img src="img/icons/icon_success.png" alt="Success"/><span>成功!</span>操作成功.</p>
    </div>

    <!-- Content Box Start -->
    <div class="contentcontainer">
        <div class="headings">
            <h2>域名管理</h2>
        </div>
        <TABLE border=0 width="700" class="tb1">
            <TR>
                <TD width=340px align=center valign=top>
                    <div class="zTreeDemoBackground">
                        <ul id="treeDemo" class="tree"></ul>
                    </div>
                    <img src="styles/zTreeStyle/img/add.png" alt=""
                         style="position: absolute;top: 473px;left:315px;cursor: pointer" onclick="addNode();"
                         class="IphoneAddButton" id="azure">
                    <img src="styles/zTreeStyle/img/done.png" alt=""
                         style="position: absolute;top: 473px;left:505px;cursor: pointer" class="IphoneDoneButton"
                         id="azure" onclick="ok();">
                </TD>
                <script>
                    var result = $("#result").val();

                    if (result == "1") {
                        $("#success").slideDown("slow");
                        var addTop = new Number(($(".IphoneAddButton").css("top").split("px")[0])) + 60;
                        $(".IphoneAddButton").css("top", addTop + "px");
                        var doneTop = new Number(($(".IphoneDoneButton").css("top").split("px")[0])) + 60;
                        $(".IphoneDoneButton").css("top", doneTop + "px");
                    } else if (result == "2") {
                        $("#errorInfo").slideDown("slow");
                    }

                </script>
                <TD width=360px align=left valign=top>
                    <div class="demoContent">
                        <li class="title focus">
                            <button class="ico star" onfocus="this.blur();"></button>
                            操作指示(也可点击域名列表下方按钮进行操作哦！<img src="img/icons/new.gif" alt="" style="margin-bottom: 4px;"/>)
                            <%--<ul class="operate">--%>
                            <%--[<a id="jRollback" class="disabled" onclick="operateRollback(); return false;">撤销</a>] [<a--%>
                            <%--id="jRedo" class="disabled" onclick="operateRedo(); return false;">重做</a>]--%>
                            <%--&nbsp;&nbsp;&nbsp;&nbsp;<span id="jOperatePool" class="info"></span>--%>
                            <%--</ul>--%>
                            <ul class="operate">
                                修改节点名称: <input type="text" value="" id="renameTxt"
                                               onchange="renameTreeNode(this);"/></button>
                            </ul>
                            <ul class="operate">
                                增加节点:
                                <button class="ico addNode" onfocus="this.blur();" title="增加叶子节点"
                                        onclick="addNode();"></button>
                                删除节点:
                                <button class="ico removeNode" onfocus="this.blur();" title="删除节点"
                                        onclick="removeTreeNode();"></button>
                            </ul>
                            <ul class="operate">
                                保存
                                <button class="ico checkNode" onfocus="this.blur();" title="保存"
                                        onclick="ok();"></button>
                            </ul>
                        </li>

                    </div>
                </TD>
            </TR>
        </TABLE>
    </div>
    <!-- Content Box End -->
    <!--
    <div id="footer">
        Copyright &copy; 1997 - 2011 Sobey Digital Technology Co.Ltd
    </div>
     -->
</div>
<!-- Right Side/Main Content End -->


<!-- Left Dark Bar End -->

<!-- Notifications Box/Pop-Up Start -->

<!-- Notifications Box/Pop-Up End -->


<!--[if IE 6]>
<script type='text/javascript' src='scripts/png_fix.js'></script>
<script type='text/javascript'>
    DD_belatedPNG.fix('img, .notifycount, .selected');
</script>
<![endif]-->
</body>
</html>
