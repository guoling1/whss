/**
 * Created by administrator on 2016/12/9.
 */

// 引入http message
const message = _require('message');
const http = _require('http');
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