/**
 * Created by wangl on 2016/12/12.
 */


// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch('dealerIndex');
// 引入浏览器特性处理
const tools = _require('tools');
let aSpan = document.getElementsByClassName("decimal");
for (var i = 0; i < aSpan.length; i++) {
  aSpan[i].innerHTML = tools.NumToFix(aSpan[i].innerHTML);
}