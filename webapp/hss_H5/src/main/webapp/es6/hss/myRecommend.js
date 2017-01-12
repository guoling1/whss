/**
 * Created by administrator on 2016/12/9.
 */

// 引入http message
const message = _require('message');
const http = _require('http');
const tools = _require('tools');
// 定义变量
const qr = document.getElementById('qr');
const shareShow = document.getElementById('shareShow');
const layer = document.getElementById('layer');
const profits = document.getElementById('profits');
const profitsBtn = document.getElementById('profitsBtn');
const friends = document.getElementById('friends');
const friendsBtn = document.getElementById('friendsBtn');
const directCount = document.getElementById('directCount');
const indirectCount = document.getElementById('indirectCount');
const windows = document.getElementById('windows');
let notice = document.getElementById('notice');
let rules = document.getElementById('rules');
let xx = document.getElementById('xx');
rules.addEventListener('click', function () {
  notice.className = 'notice flexBox flex-box-column';
  setTimeout(function () {
    notice.style.opacity = 1;
  }, 0);
});
xx.addEventListener('click', function () {
  notice.style.opacity = 0;
  setTimeout(function () {
    notice.className = 'notice flexBox flex-box-column miss';
  }, 400);
});
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();

shareShow.addEventListener('click', function () {
  layer.style.display = 'block';
});

layer.addEventListener('click', function () {
  layer.style.display = 'none';
});

// 分润点击
profitsBtn.addEventListener('click', function () {
  windows.style.left = '0';
  profitsBtn.className = 'space shadow-right';
  friendsBtn.className = 'space shadow-left disabled';
});
// 分润滑动
let profitsLastX, profitsTouchX;
profits.addEventListener('touchstart', function (event) {
  profitsLastX = event.touches[0].clientX;
});
profits.addEventListener('touchmove', function (event) {
  profitsTouchX = event.touches[0].clientX;
  if (profitsTouchX <= profitsLastX - 100) {
    windows.style.left = '-100%';
    profitsBtn.className = 'space shadow-right disabled';
    friendsBtn.className = 'space shadow-left';
    event.preventDefault();
  }
});
// 分润的数据获取以及分页
let box = document.getElementById('profits');
let pag_No = 1;
let pag_tag = '';
let pag_box = document.createElement('div');
let pag_more = document.createElement('div');
pag_more.innerHTML = '加载更多';
pag_more.style.height = '40px';
pag_more.style.lineHeight = '40px';
pag_more.style.fontSize = '16px';
pag_more.style.color = '#999';
pag_more.style.backgroundColor = '#FFF';
pag_more.addEventListener('touchstart', function () {
  pag_more.style.backgroundColor = '#f0eff5';
});
pag_more.addEventListener('touchend', function () {
  pag_more.style.backgroundColor = '#FFF';
});
let g = function (pageNO, pageSize, shallId) {
  return new Promise((resolve, reject) => {
    http.post('/merchantInfo/queryShall', {
      pageNo: pageNO,
      shallId: shallId,
      pageSize: pageSize
    }, function (res) {
      resolve(res.pageModel);
    });
  });
};
let c = function (name, type, date, money) {
  let div_list = document.createElement('div');
  div_list.className = 'list';
  let div_name = document.createElement('div');
  div_name.className = 'name';
  div_name.innerHTML = name;
  if (type == 1) {
    let span_z = document.createElement('span');
    span_z.className = 'z';
    span_z.innerHTML = '直接';
    div_name.appendChild(span_z);
  } else {
    let span_j = document.createElement('span');
    span_j.className = 'j';
    span_j.innerHTML = '间接';
    div_name.appendChild(span_j);
  }
  let div_date = document.createElement('div');
  div_date.className = 'date';
  div_date.innerHTML = tools.dateFormat('YYYY/MM/DD', date);
  let div_amount = document.createElement('div');
  div_amount.className = 'amount';
  div_amount.innerHTML = money + '元';
  div_list.appendChild(div_name);
  div_list.appendChild(div_date);
  div_list.appendChild(div_amount);
  return div_list;
};
// 初始化数据
g(pag_No, 5, pag_tag).then(function (data) {
  // 循环添加数据
  for (let i = 0; i < data.records.length; i++) {
    pag_box.appendChild(c(data.records[i].name, data.records[i].type, data.records[i].date, data.records[i].money));
    // 重置下一页
    pag_No++;
    pag_tag = data.records[i].shallId;
  }
  // 判断是否需要加载更多
  if (!data.hasNextPage) {
    console.log('下一页消失');
    pag_more.style.display = 'none';
  }
}, function (err) {
  console.log(err);
});
// 加载更多
pag_more.addEventListener('click', function () {
  g(pag_No, 5, pag_tag).then(function (data) {
    // 循环添加数据
    for (let i = 0; i < data.records.length; i++) {
      pag_box.appendChild(c(data.records[i].name, data.records[i].type, data.records[i].date, data.records[i].money));
      // 重置下一页
      pag_No++;
      pag_tag = data.records[i].shallId;
    }
    // 判断是否需要加载更多
    if (!data.hasNextPage) {
      console.log('下一页消失');
      pag_more.style.display = 'none';
    }
  }, function (err) {
    console.log(err);
  });
});
box.appendChild(pag_box);
box.appendChild(pag_more);

// 推广的好友点击
friendsBtn.addEventListener('click', function () {
  windows.style.left = '-100%';
  profitsBtn.className = 'space shadow-right disabled';
  friendsBtn.className = 'space shadow-left';
});
// 推广的好友滑动
let friendsLastX, friendsTouchX;
friends.addEventListener('touchstart', function (event) {
  friendsLastX = event.touches[0].clientX;
});
friends.addEventListener('touchmove', function (event) {
  friendsTouchX = event.touches[0].clientX;
  if (friendsTouchX >= friendsLastX + 100) {
    windows.style.left = '0';
    profitsBtn.className = 'space shadow-right';
    friendsBtn.className = 'space shadow-left disabled';
    event.preventDefault();
  }
});
// 推广的好友的数据获取
http.post('/wx/myRecommend', {}, function (data) {
  console.log(data);
  directCount.innerHTML = data.directCount;
  indirectCount.innerHTML = data.indirectCount;
  for (let i = 0; i < data.recommends.length; i++) {
    let list = document.createElement('div');
    list.className = 'list';
    let name = document.createElement('div');
    name.className = 'name';
    name.innerHTML = data.recommends[i].name;
    let span = document.createElement('span');
    if (data.recommends[i].type == 1) {
      span.className = 'z';
      span.innerHTML = '直接';
    } else {
      span.className = 'j';
      span.innerHTML = '间接';
    }
    name.appendChild(span);
    let amount = document.createElement('amount');
    amount.className = 'amount';
    amount.innerHTML = data.recommends[i].statusName;
    list.appendChild(name);
    list.appendChild(amount);
    friends.appendChild(list);
  }
});

new QRCode(qr, {
  text: pageData.shareUrl,
  width: 210,
  height: 210,
  colorDark: "#000000",
  colorLight: "#ffffff",
  correctLevel: QRCode.CorrectLevel.H
});