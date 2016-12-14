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
// 引入分页插件
const Paging = _require('paging');
new Paging('trade_record', {
  content: 'content',
  url: '/wx/getOrderRecord',
  size: 5,
  startDate: 'dateFrom',
  endDate: 'dateTo'
});