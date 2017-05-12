/**
 * Created by administrator on 2017/1/9.
 */

// 引入http message
const message = _require('message');
const http = _require('http');
// 引入浏览器特性处理
const browser = _require('browser');
browser.set_html_size();
// 定义变量
let notice = document.getElementById('notice');
let rocket = document.getElementById('rocket');
let xx = document.getElementById('xx');
rocket.addEventListener('click', function () {
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

let banner = document.getElementById('banner');
let point = document.getElementById('point');
let nowLevel = 0;

let up = document.getElementById('up');
let upBtn = document.getElementById('upBtn');
let touchS = 0;
let touchE = 0;
let move = false;
let moveLength = 0;
up.addEventListener('touchstart', function (e) {
  touchS = e.touches[0].clientX;
});
up.addEventListener('touchmove', function (e) {
  touchE = e.touches[0].clientX;
  let banner_child = banner.getElementsByClassName('list');
  if (touchE - 50 > touchS && !move) {
    if (banner_child[moveLength - 1].className == 'list active')return;
    move = true;
    for (let b = 0; b < banner_child.length; b++) {
      switch (banner_child[b].className) {
        case 'list active':
          banner_child[b].className = 'list right';
          break;
        case 'list left':
          banner_child[b].className = 'list active';
          break;
        case 'list right':
          banner_child[b].className = 'list right none';
          break;
        case 'list left none':
          banner_child[b].className = 'list left';
          break;
      }
    }
    setTimeout(function () {
      move = false;
    }, 300)
  } else if (touchS - 50 > touchE && !move) {
    if (banner_child[0].className == 'list active')return;
    move = true;
    for (let b = 0; b < banner_child.length; b++) {
      switch (banner_child[b].className) {
        case 'list active':
          banner_child[b].className = 'list left';
          break;
        case 'list left':
          banner_child[b].className = 'list left none';
          break;
        case 'list right':
          banner_child[b].className = 'list active';
          break;
        case 'list right none':
          banner_child[b].className = 'list right';
          break;
      }
    }
    setTimeout(function () {
      move = false;
    }, 300)
  }
});
upBtn.addEventListener('click', function () {
  up.style.display = 'block';
});

// 获取升级
http.post('/wx/toUpgrade', {}, function (data) {
  nowLevel = data.currentLevel;
  for (let i = 2; i > nowLevel - 1; i--) {
    moveLength++;
    switch (data.upgradeRules[i].type) {
      case 3:
        var banner_list = document.createElement('div');
        banner_list.className = 'list active';
        var banner_head = document.createElement('div');
        banner_head.className = 'head boss';
        var banner_text = document.createElement('div');
        banner_text.className = 'text';
        banner_text.innerHTML = '老板';
        var banner_type = document.createElement('ul');
        banner_type.className = 'upType';
        //li 1
        var banner_type_li1 = document.createElement('li');
        var banner_type_li1_left = document.createElement('div');
        banner_type_li1_left.className = 'left';
        var banner_type_li1_span1 = document.createElement('span');
        banner_type_li1_span1.className = 'check active';
        var banner_type_li1_span2 = document.createElement('span');
        banner_type_li1_span2.innerHTML = '方式一：付费升级';
        banner_type_li1_left.appendChild(banner_type_li1_span1);
        banner_type_li1_left.appendChild(banner_type_li1_span2);
        var banner_type_li1_right = document.createElement('div');
        banner_type_li1_right.className = 'right';
        banner_type_li1_right.innerHTML = '￥' + data.upgradeRules[i].needMoney;
        var banner_type_li1_small = document.createElement('div');
        banner_type_li1_small.className = 'small';
        banner_type_li1_small.innerHTML = '￥';
        banner_type_li1.appendChild(banner_type_li1_left);
        banner_type_li1.appendChild(banner_type_li1_right);
        banner_type_li1.appendChild(banner_type_li1_small);
        //li 2
        var banner_type_li2 = document.createElement('li');
        var banner_type_li2_left = document.createElement('div');
        banner_type_li2_left.className = 'left';
        var banner_type_li2_span1 = document.createElement('span');
        banner_type_li2_span1.className = 'check';
        var banner_type_li2_span2 = document.createElement('span');
        banner_type_li2_span2.innerHTML = '方式二：推广' + data.upgradeRules[i].needCount + '个好友';
        banner_type_li2_left.appendChild(banner_type_li2_span1);
        banner_type_li2_left.appendChild(banner_type_li2_span2);
        var banner_type_li2_right = document.createElement('div');
        banner_type_li2_right.className = 'right';
        banner_type_li2_right.innerHTML = '还差' + data.upgradeRules[i].restCount + '个';
        var banner_type_li2_small = document.createElement('div');
        banner_type_li2_small.className = 'small';
        banner_type_li2_small.innerHTML = '达到推广好友数量，系统将自动为您升级';
        banner_type_li2.appendChild(banner_type_li2_left);
        banner_type_li2.appendChild(banner_type_li2_right);
        banner_type_li2.appendChild(banner_type_li2_small);
        banner_type.appendChild(banner_type_li1);
        banner_type.appendChild(banner_type_li2);
        var banner_bottom = document.createElement('div');
        banner_bottom.className = 'bottom';
        banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
        banner_list.appendChild(banner_head);
        banner_list.appendChild(banner_text);
        banner_list.appendChild(banner_type);
        banner_list.appendChild(banner_bottom);
        banner.appendChild(banner_list);
        break;
      case 2:
        var banner_list = document.createElement('div');
        banner_list.className = 'list left';
        var banner_head = document.createElement('div');
        banner_head.className = 'head manager';
        var banner_text = document.createElement('div');
        banner_text.className = 'text';
        banner_text.innerHTML = '店长';
        var banner_type = document.createElement('ul');
        banner_type.className = 'upType';
        //li 1
        var banner_type_li1 = document.createElement('li');
        var banner_type_li1_left = document.createElement('div');
        banner_type_li1_left.className = 'left';
        var banner_type_li1_span1 = document.createElement('span');
        banner_type_li1_span1.className = 'check active';
        var banner_type_li1_span2 = document.createElement('span');
        banner_type_li1_span2.innerHTML = '方式一：付费升级';
        banner_type_li1_left.appendChild(banner_type_li1_span1);
        banner_type_li1_left.appendChild(banner_type_li1_span2);
        var banner_type_li1_right = document.createElement('div');
        banner_type_li1_right.className = 'right';
        banner_type_li1_right.innerHTML = '￥' + data.upgradeRules[i].needMoney;
        var banner_type_li1_small = document.createElement('div');
        banner_type_li1_small.className = 'small';
        banner_type_li1_small.innerHTML = '￥';
        banner_type_li1.appendChild(banner_type_li1_left);
        banner_type_li1.appendChild(banner_type_li1_right);
        banner_type_li1.appendChild(banner_type_li1_small);
        //li 2
        var banner_type_li2 = document.createElement('li');
        var banner_type_li2_left = document.createElement('div');
        banner_type_li2_left.className = 'left';
        var banner_type_li2_span1 = document.createElement('span');
        banner_type_li2_span1.className = 'check';
        var banner_type_li2_span2 = document.createElement('span');
        banner_type_li2_span2.innerHTML = '方式二：推广' + data.upgradeRules[i].needCount + '个好友';
        banner_type_li2_left.appendChild(banner_type_li2_span1);
        banner_type_li2_left.appendChild(banner_type_li2_span2);
        var banner_type_li2_right = document.createElement('div');
        banner_type_li2_right.className = 'right';
        banner_type_li2_right.innerHTML = '还差' + data.upgradeRules[i].restCount + '个';
        var banner_type_li2_small = document.createElement('div');
        banner_type_li2_small.className = 'small';
        banner_type_li2_small.innerHTML = '达到推广好友数量，系统将自动为您升级';
        banner_type_li2.appendChild(banner_type_li2_left);
        banner_type_li2.appendChild(banner_type_li2_right);
        banner_type_li2.appendChild(banner_type_li2_small);
        banner_type.appendChild(banner_type_li1);
        banner_type.appendChild(banner_type_li2);
        var banner_bottom = document.createElement('div');
        banner_bottom.className = 'bottom';
        banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
        banner_list.appendChild(banner_head);
        banner_list.appendChild(banner_text);
        banner_list.appendChild(banner_type);
        banner_list.appendChild(banner_bottom);
        banner.appendChild(banner_list);
        break;
      case 1:
        var banner_list = document.createElement('div');
        banner_list.className = 'list left none';
        var banner_head = document.createElement('div');
        banner_head.className = 'head assistant';
        var banner_text = document.createElement('div');
        banner_text.className = 'text';
        banner_text.innerHTML = '店员';
        var banner_type = document.createElement('ul');
        banner_type.className = 'upType';
        //li 1
        var banner_type_li1 = document.createElement('li');
        var banner_type_li1_left = document.createElement('div');
        banner_type_li1_left.className = 'left';
        var banner_type_li1_span1 = document.createElement('span');
        banner_type_li1_span1.className = 'check active';
        var banner_type_li1_span2 = document.createElement('span');
        banner_type_li1_span2.innerHTML = '方式一：付费升级';
        banner_type_li1_left.appendChild(banner_type_li1_span1);
        banner_type_li1_left.appendChild(banner_type_li1_span2);
        var banner_type_li1_right = document.createElement('div');
        banner_type_li1_right.className = 'right';
        banner_type_li1_right.innerHTML = '￥' + data.upgradeRules[i].needMoney;
        var banner_type_li1_small = document.createElement('div');
        banner_type_li1_small.className = 'small';
        banner_type_li1_small.innerHTML = '￥';
        banner_type_li1.appendChild(banner_type_li1_left);
        banner_type_li1.appendChild(banner_type_li1_right);
        banner_type_li1.appendChild(banner_type_li1_small);
        //li 2
        var banner_type_li2 = document.createElement('li');
        var banner_type_li2_left = document.createElement('div');
        banner_type_li2_left.className = 'left';
        var banner_type_li2_span1 = document.createElement('span');
        banner_type_li2_span1.className = 'check';
        var banner_type_li2_span2 = document.createElement('span');
        banner_type_li2_span2.innerHTML = '方式二：推广' + data.upgradeRules[i].needCount + '个好友';
        banner_type_li2_left.appendChild(banner_type_li2_span1);
        banner_type_li2_left.appendChild(banner_type_li2_span2);
        var banner_type_li2_right = document.createElement('div');
        banner_type_li2_right.className = 'right';
        banner_type_li2_right.innerHTML = '还差' + data.upgradeRules[i].restCount + '个';
        var banner_type_li2_small = document.createElement('div');
        banner_type_li2_small.className = 'small';
        banner_type_li2_small.innerHTML = '达到推广好友数量，系统将自动为您升级';
        banner_type_li2.appendChild(banner_type_li2_left);
        banner_type_li2.appendChild(banner_type_li2_right);
        banner_type_li2.appendChild(banner_type_li2_small);
        banner_type.appendChild(banner_type_li1);
        banner_type.appendChild(banner_type_li2);
        var banner_bottom = document.createElement('div');
        banner_bottom.className = 'bottom';
        banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
        banner_list.appendChild(banner_head);
        banner_list.appendChild(banner_text);
        banner_list.appendChild(banner_type);
        banner_list.appendChild(banner_bottom);
        banner.appendChild(banner_list);
        break;
    }
  }
});