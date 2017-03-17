/**
 * Created by administrator on 2016/12/6.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 引入http message
const http = _require('http');
const validate = _require('validate');
const message = _require('message');

// 获取支持的银行卡列表
http.post('/channel/queryChannelSupportBank', {}, function (data) {
  console.log(data);
});