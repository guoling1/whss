/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 引入http message
const message = _require('message');
const Keyboard = _require('keyboard');
new Keyboard({
  spanId: 'key-span',
  inputId: 'key-input',
  keyboardId: 'keyboard'
});

// 定义变量
let layerB = document.getElementById('layerB');
let xxB = document.getElementById('xxB');
let toB = document.getElementById('toB');
let cancel = document.getElementById('cancel');

layerB.addEventListener('click', function () {
  layerB.style.display = 'block';
});
