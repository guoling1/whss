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