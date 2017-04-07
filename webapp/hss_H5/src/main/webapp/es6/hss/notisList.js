/**
 * Created by administrator on 2016/12/13.
 */

// 引入 message http
const message = _require('message');
const http = _require('http');
const tools = _require('tools');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch('list-box');
// 定义ajax事件
let content = document.getElementById('content');
let list = document.createElement('list');
let nowPage = 1;
let more = document.createElement('div');
more.className = 'touch_more';
more.innerHTML = '加载更多';
more.style.display = 'none';
more.style.height = '50px';
more.style.lineHeight = '50px';
more.style.fontSize = '16px';
more.style.color = '#999';
more.style.backgroundColor = '#f4f5f8';
more.addEventListener('touchstart', function () {
  more.style.backgroundColor = '#d8d7dc';
});
more.addEventListener('touchend', function () {
  more.style.backgroundColor = '#f4f5f8';
});
more.addEventListener('click', function () {
  getData(null, nowPage + 1);
});
content.appendChild(more);

let getData = function (e, page) {
  // 检查page 如果page为1 清空content数据
  nowPage = page || 1;
  if (nowPage == 1) {
    list.innerHTML = '';
  }
  http.post('/notice/noticeList', {
    pageNo: nowPage,
    pageSize: 20
  }, function (res) {
    console.log(res.records);
    for (let i = 0; i < res.records.length; i++) {
      let div_list = document.createElement('div');
      div_list.className = 'list-li';
      div_list.onclick = function () {
        window.location.href = '/notice/noticeDetailsJSP?id=' + res.records[i].id;
      };
      let div_icon = document.createElement('div');
      if (res.records[i].type == 1) {
        div_icon.className = 'list-icon notice2';
      } else {
        div_icon.className = 'list-icon notice1';
      }
      let div_info = document.createElement('div');
      div_info.className = 'list-info';
      let div_top = document.createElement('div');
      div_top.className = 'top';
      div_top.innerHTML = res.records[i].title;
      let div_bottom = document.createElement('div');
      div_bottom.className = 'bottom';
      div_bottom.innerHTML = res.records[i].dates;
      div_info.appendChild(div_top);
      div_info.appendChild(div_bottom);
      let div_right = document.createElement('div');
      div_right.className = 'list-right';
      div_list.appendChild(div_icon);
      div_list.appendChild(div_info);
      div_list.appendChild(div_right);
      list.appendChild(div_list);
    }
    // 判断是否还有下一页
    if (res.hasNextPage) {
      more.style.display = 'block';
    } else {
      more.style.display = 'none';
    }
    content.insertBefore(list, content.childNodes[0]);
  })
};
// 初始化数据
getData();
