/**
 * Created by administrator on 2016/12/13.
 */

// 引入 message http
const message = _require('message');
const http = _require('http');
// 引入时间选择器
const TimePicker = _require('timePicker');
new TimePicker('dateFrom');
new TimePicker('dateTo');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch('list');
// 引入分页插件
const Paging = _require('paging');
new Paging('trade_record', {
  content: 'content',
  url: '/trade/queryMerchantPayOrders',
  size: 5,
  startDate: 'dateFrom',
  endDate: 'dateTo'
});