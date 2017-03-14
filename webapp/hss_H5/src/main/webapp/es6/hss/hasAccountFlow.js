/**
 * Created by administrator on 2016/12/8.
 */

// 引入动画模版 处理验证码
const AnimationCountdown = _require('art-countdown');
let countdown = new AnimationCountdown('sendCode', '重新获取');
// 引入http message
const validate = _require('validate');
const message = _require('message');
const http = _require('http');

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
  http.post('/account/flowDetails', {
    pageNo: nowPage,
    pageSize: 5
  }, function (res) {
    console.log(res.records);
    for (let i = 0; i < res.records.length; i++) {
      let div_list = document.createElement('div');
      div_list.className = 'flow-group';
      let div_left = document.createElement('div');
      div_left.className = 'flow-left';
      let div_top = document.createElement('div');
      div_top.className = 'flow-top';
      let div_span = document.createElement('span');
      if (res.records[i].incomeAmount > 0) {
        div_span.innerHTML = '收入';
      } else {
        div_span.innerHTML = '提现';
      }
      let div_spanS = document.createElement('span');
      div_spanS.className = 's';
      div_spanS.innerHTML = res.records[i].remark;
      div_top.appendChild(div_span);
      div_top.appendChild(div_spanS);
      let div_bottom = document.createElement('div');
      div_bottom.className = 'flow-bottom';
      div_bottom.innerHTML = res.records[i].createTime;
      div_left.appendChild(div_top);
      div_left.appendChild(div_bottom);
      let div_right = document.createElement('div');
      if (res.records[i].incomeAmount > 0) {
        div_right.className = 'flow-right green';
        div_right.innerHTML = '+' + res.records[i].incomeAmount;
      } else {
        div_right.className = 'flow-right red';
        div_right.innerHTML = '-' + res.records[i].outAmount;
      }
      div_list.appendChild(div_left);
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