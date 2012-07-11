/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 11-5-31
 * Time: 下午5:53
 * To change this template use File | Settings | File Templates.
 */
function showOrClose(obj) {
    $(obj).slideUp("slow");
}
function reset() {
    var height = new Number($(".contentcontainer").css("height").split("px")[0]);
    parent.$("#detail").css("height", height + "px");
}