/**
 * Created by administrator on 2016/12/8.
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
  keyboardId: 'keyboard',
  isSelf: pageData.isSelf,
  selfId: 'layerSelf',
  notSelfId: 'layerNotSelf',
  amount: '2000'
});

if (pageData.isSelf == 1) {
  let layerSelf = document.getElementById('layerSelf');
  let cancelSelf = document.getElementById('cancelSelf');
  let submitSelf = document.getElementById('submitSelf');
  cancelSelf.addEventListener('click', function () {
    layerSelf.style.display = 'none';
  });
  submitSelf.addEventListener('click', function () {
    window.location.href = '/sqb/collection';
  })
}
let layerNotSelf = document.getElementById('layerNotSelf');
let cancelNotSelf = document.getElementById('cancelNotSelf');
let submitNotSelf = document.getElementById('submitNotSelf');
cancelNotSelf.addEventListener('click', function () {
  layerNotSelf.style.display = 'none';
});
submitNotSelf.addEventListener('click', function () {
  if (WeixinJSBridge) {
    WeixinJSBridge.call('closeWindow');
  } else {
    window.close();
  }
});