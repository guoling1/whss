/**
 * Created by administrator on 2016/12/13.
 */

// 引入 message http
const message = _require('message');
const http = _require('http');
const tools = _require('tools');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch('noticeDetails');
// 定义ajax事件
let query = tools.GetUrlArg();
let title = document.getElementById('title');
let date = document.getElementById('date');
let context = document.getElementById('context');

let changeTitle = function (title) {
  let body = document.getElementsByTagName('body')[0];
  if (!title) {
    document.title = '好收收';
  } else {
    document.title = title;
  }
  let iframe = document.createElement("iframe");
  iframe.className = 'none';
  iframe.setAttribute("src", "/app/imgs/bingo.png");
  iframe.addEventListener('load', function () {
    $timeout(function () {
      iframe.removeEventListener('load', function () {
      });
      document.body.removeChild(iframe);
    }, 0);
  });
  document.body.appendChild(iframe);
};

http.post('/notice/noticeDetails', {
  id: query.id
}, function (data) {
  changeTitle(data.title);
  title.innerHTML = data.title;
  date.innerHTML = tools.dateFormat('YYYY-MM-DD HH:mm:ss', data.createTime);
  context.innerHTML = data.text;
});