/**
 * Created by administrator on 2017/1/9.
 */

// 引入http message
const message = _require('message');
const http = _require('http');
// 引入浏览器特性处理
const browser = _require('browser');
browser.set_html_size();
// 定义变量
let notice = document.getElementById('notice');
let rocket = document.getElementById('rocket');
let xx = document.getElementById('xx');
rocket.addEventListener('click', function () {
  notice.className = 'notice flexBox flex-box-column';
  setTimeout(function () {
    notice.style.opacity = 1;
  }, 0);
});
xx.addEventListener('click', function () {
  notice.style.opacity = 0;
  setTimeout(function () {
    notice.className = 'notice flexBox flex-box-column miss';
  }, 400);
});