/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 引入http message
const http = _require('http');
const message = _require('message');
const Keyboard = _require('keyboard');
new Keyboard({
  spanId: 'key-span',
  inputId: 'key-input',
  keyboardId: 'keyboard'
});

let know = document.getElementById('know');
let layer = document.getElementById('layer');
let layer_x = document.getElementById('layer-x');
let submit = document.getElementById('submit');

know.addEventListener('click', function () {
  layer.style.display = 'block';
});

layer_x.addEventListener('click', function () {
  layer.style.display = 'none';
});

submit.addEventListener('click', function () {
  layer.style.display = 'none';
});

// 多通道获取
http.post('/channel/list', {}, function (data) {
  console.log(data);
  let group = document.createElement('group');
});