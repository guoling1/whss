/**
 * Created by administrator on 2016/12/13.
 */

// 引入 message http
const message = _require('message');
const http = _require('http');
const tools = _require('tools');
// 定义ajax事件
let query = tools.GetUrlArg();
let title = document.getElementById('title');
let date = document.getElementById('date');
let context = document.getElementById('context');
http.post('/notice/noticeDetails', {
  id: query.noticeId
}, function (data) {
  title.innerHTML = data.title;
  date.innerHTML = tools.dateFormat('YYYY-MM-DD HH:mm:ss', data.createTime);
  context.innerHTML = data.text;
});