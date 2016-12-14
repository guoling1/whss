/**
 * Created by wangl on 2016/12/12.
 */
function toFix(val) {
    return parseFloat(val).toFixed(2)
}
var aSpan = document.getElementsByClassName("decimal");
for(var i = 0; i <aSpan.length; i++){
    aSpan[i].innerHTML = toFix(aSpan[i].innerHTML)
}