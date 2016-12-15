'use strict';

/**
 * Created by administrator on 2016/12/13.
 */

// 引入 message http
var message = _require('message');
var http = _require('http');
// 引入时间选择器
var TimePicker = _require('timePicker');
new TimePicker('dateFrom');
new TimePicker('dateTo');
// 引入浏览器特性处理
var browser = _require('browser');
browser.elastic_touch('list');
// 引入分页插件
var Paging = _require('paging');
new Paging('trade_record', {
  content: 'content',
  url: '/wx/getOrderRecord',
  size: 5,
  startDate: 'dateFrom',
  endDate: 'dateTo'
});
//# sourceMappingURL=tradeRecord.js.map
