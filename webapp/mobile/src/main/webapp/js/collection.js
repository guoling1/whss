'use strict';

/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
var browser = _require('browser');
browser.elastic_touch();
// 引入http message
var message = _require('message');
var Keyboard = _require('keyboard');
new Keyboard({
  spanId: 'key-span',
  inputId: 'key-input',
  keyboardId: 'keyboard'
});
//# sourceMappingURL=collection.js.map
