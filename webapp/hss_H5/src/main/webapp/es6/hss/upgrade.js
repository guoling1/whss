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
let upType = 0;

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
const layer = document.getElementById('layer');
layer.addEventListener('click', function () {
  layer.style.display = 'none';
});

// 获取升级
http.post('/wx/toUpgrade', {}, function (data) {
  nowLevel = data.currentLevel;
  new QRCode(qr, {
    text: data.shareUrl,
    colorDark: "#000000",
    colorLight: "#ffffff",
    correctLevel: QRCode.CorrectLevel.H
  });
  for (let i = 2; i > nowLevel - 1; i--) {
    moveLength++;
    switch (data.upgradeRules[i].type) {
      case 3:
        var boss_banner_list = document.createElement('div');
        boss_banner_list.className = 'list active';
        var boss_banner_head = document.createElement('div');
        boss_banner_head.className = 'head boss';
        var boss_banner_text = document.createElement('div');
        boss_banner_text.className = 'text';
        boss_banner_text.innerHTML = '老板';
        var boss_banner_type = document.createElement('ul');
        boss_banner_type.className = 'upType';
        //li 1
        var boss_banner_type_li1 = document.createElement('li');
        boss_banner_type_li1.addEventListener('click', function () {
          boss_banner_type_li1_span1.className = 'check active';
          boss_banner_type_li2_span1.className = 'check';
          boss_banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
          upType = 0;
        });
        var boss_banner_type_li1_left = document.createElement('div');
        boss_banner_type_li1_left.className = 'left';
        var boss_banner_type_li1_span1 = document.createElement('span');
        boss_banner_type_li1_span1.className = 'check active';
        var boss_banner_type_li1_span2 = document.createElement('span');
        boss_banner_type_li1_span2.innerHTML = '方式一：付费升级';
        boss_banner_type_li1_left.appendChild(boss_banner_type_li1_span1);
        boss_banner_type_li1_left.appendChild(boss_banner_type_li1_span2);
        var boss_banner_type_li1_right = document.createElement('div');
        boss_banner_type_li1_right.className = 'right';
        boss_banner_type_li1_right.innerHTML = '￥' + data.upgradeRules[i].needMoney;
        var boss_banner_type_li1_small = document.createElement('s');
        boss_banner_type_li1_small.className = 'small';
        boss_banner_type_li1_small.innerHTML = '￥' + data.upgradeRules[i].upgradeCost;
        boss_banner_type_li1.appendChild(boss_banner_type_li1_left);
        boss_banner_type_li1.appendChild(boss_banner_type_li1_right);
        boss_banner_type_li1.appendChild(boss_banner_type_li1_small);
        //li 2
        var boss_banner_type_li2 = document.createElement('li');
        boss_banner_type_li2.addEventListener('click', function () {
          boss_banner_type_li1_span1.className = 'check';
          boss_banner_type_li2_span1.className = 'check active';
          boss_banner_bottom.innerHTML = '去推广';
          upType = 1;
        });
        var boss_banner_type_li2_left = document.createElement('div');
        boss_banner_type_li2_left.className = 'left';
        var boss_banner_type_li2_span1 = document.createElement('span');
        boss_banner_type_li2_span1.className = 'check';
        var boss_banner_type_li2_span2 = document.createElement('span');
        boss_banner_type_li2_span2.innerHTML = '方式二：推广' + data.upgradeRules[i].needCount + '个好友';
        boss_banner_type_li2_left.appendChild(boss_banner_type_li2_span1);
        boss_banner_type_li2_left.appendChild(boss_banner_type_li2_span2);
        var boss_banner_type_li2_right = document.createElement('div');
        boss_banner_type_li2_right.className = 'right';
        boss_banner_type_li2_right.innerHTML = '还差' + data.upgradeRules[i].restCount + '个';
        var boss_banner_type_li2_small = document.createElement('div');
        boss_banner_type_li2_small.className = 'small';
        boss_banner_type_li2_small.innerHTML = '达到推广好友数量，系统将自动为您升级';
        boss_banner_type_li2.appendChild(boss_banner_type_li2_left);
        boss_banner_type_li2.appendChild(boss_banner_type_li2_right);
        boss_banner_type_li2.appendChild(boss_banner_type_li2_small);
        boss_banner_type.appendChild(boss_banner_type_li1);
        boss_banner_type.appendChild(boss_banner_type_li2);
        var boss_banner_bottom = document.createElement('div');
        boss_banner_bottom.className = 'bottom';
        boss_banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
        boss_banner_bottom.addEventListener('click', function () {
          if (upType) {
            layer.style.display = 'block';
          } else {
            window.location.replace('/sqb/toBuy/' + data.upgradeRules[i].id);
          }
        });
        boss_banner_list.appendChild(boss_banner_head);
        boss_banner_list.appendChild(boss_banner_text);
        boss_banner_list.appendChild(boss_banner_type);
        boss_banner_list.appendChild(boss_banner_bottom);
        banner.appendChild(boss_banner_list);
        break;
      case 2:
        var manager_banner_list = document.createElement('div');
        manager_banner_list.className = 'list left';
        var manager_banner_head = document.createElement('div');
        manager_banner_head.className = 'head manager';
        var manager_banner_text = document.createElement('div');
        manager_banner_text.className = 'text';
        manager_banner_text.innerHTML = '店长';
        var manager_banner_type = document.createElement('ul');
        manager_banner_type.className = 'upType';
        //li 1
        var manager_banner_type_li1 = document.createElement('li');
        manager_banner_type_li1.addEventListener('click', function () {
          manager_banner_type_li1_span1.className = 'check active';
          manager_banner_type_li2_span1.className = 'check';
          manager_banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
          upType = 0;
        });
        var manager_banner_type_li1_left = document.createElement('div');
        manager_banner_type_li1_left.className = 'left';
        var manager_banner_type_li1_span1 = document.createElement('span');
        manager_banner_type_li1_span1.className = 'check active';
        var manager_banner_type_li1_span2 = document.createElement('span');
        manager_banner_type_li1_span2.innerHTML = '方式一：付费升级';
        manager_banner_type_li1_left.appendChild(manager_banner_type_li1_span1);
        manager_banner_type_li1_left.appendChild(manager_banner_type_li1_span2);
        var manager_banner_type_li1_right = document.createElement('div');
        manager_banner_type_li1_right.className = 'right';
        manager_banner_type_li1_right.innerHTML = '￥' + data.upgradeRules[i].needMoney;
        var manager_banner_type_li1_small = document.createElement('s');
        manager_banner_type_li1_small.className = 'small';
        manager_banner_type_li1_small.innerHTML = '￥' + data.upgradeRules[i].upgradeCost;
        manager_banner_type_li1.appendChild(manager_banner_type_li1_left);
        manager_banner_type_li1.appendChild(manager_banner_type_li1_right);
        manager_banner_type_li1.appendChild(manager_banner_type_li1_small);
        //li 2
        var manager_banner_type_li2 = document.createElement('li');
        manager_banner_type_li2.addEventListener('click', function () {
          manager_banner_type_li1_span1.className = 'check';
          manager_banner_type_li2_span1.className = 'check active';
          manager_banner_bottom.innerHTML = '去推广';
          upType = 1;
        });
        var manager_banner_type_li2_left = document.createElement('div');
        manager_banner_type_li2_left.className = 'left';
        var manager_banner_type_li2_span1 = document.createElement('span');
        manager_banner_type_li2_span1.className = 'check';
        var manager_banner_type_li2_span2 = document.createElement('span');
        manager_banner_type_li2_span2.innerHTML = '方式二：推广' + data.upgradeRules[i].needCount + '个好友';
        manager_banner_type_li2_left.appendChild(manager_banner_type_li2_span1);
        manager_banner_type_li2_left.appendChild(manager_banner_type_li2_span2);
        var manager_banner_type_li2_right = document.createElement('div');
        manager_banner_type_li2_right.className = 'right';
        manager_banner_type_li2_right.innerHTML = '还差' + data.upgradeRules[i].restCount + '个';
        var manager_banner_type_li2_small = document.createElement('div');
        manager_banner_type_li2_small.className = 'small';
        manager_banner_type_li2_small.innerHTML = '达到推广好友数量，系统将自动为您升级';
        manager_banner_type_li2.appendChild(manager_banner_type_li2_left);
        manager_banner_type_li2.appendChild(manager_banner_type_li2_right);
        manager_banner_type_li2.appendChild(manager_banner_type_li2_small);
        manager_banner_type.appendChild(manager_banner_type_li1);
        manager_banner_type.appendChild(manager_banner_type_li2);
        var manager_banner_bottom = document.createElement('div');
        manager_banner_bottom.className = 'bottom';
        manager_banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
        manager_banner_bottom.addEventListener('click', function () {
          if (upType) {
            layer.style.display = 'block';
          } else {
            window.location.replace('/sqb/toBuy/' + data.upgradeRules[i].id);
          }
        });
        manager_banner_list.appendChild(manager_banner_head);
        manager_banner_list.appendChild(manager_banner_text);
        manager_banner_list.appendChild(manager_banner_type);
        manager_banner_list.appendChild(manager_banner_bottom);
        banner.appendChild(manager_banner_list);
        break;
      case 1:
        var assistant_banner_list = document.createElement('div');
        assistant_banner_list.className = 'list left none';
        var assistant_banner_head = document.createElement('div');
        assistant_banner_head.className = 'head assistant';
        var assistant_banner_text = document.createElement('div');
        assistant_banner_text.className = 'text';
        assistant_banner_text.innerHTML = '店员';
        var assistant_banner_type = document.createElement('ul');
        assistant_banner_type.className = 'upType';
        //li 1
        var assistant_banner_type_li1 = document.createElement('li');
        assistant_banner_type_li1.addEventListener('click', function () {
          assistant_banner_type_li1_span1.className = 'check active';
          assistant_banner_type_li2_span1.className = 'check';
          assistant_banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
          upType = 0;
        });
        var assistant_banner_type_li1_left = document.createElement('div');
        assistant_banner_type_li1_left.className = 'left';
        var assistant_banner_type_li1_span1 = document.createElement('span');
        assistant_banner_type_li1_span1.className = 'check active';
        var assistant_banner_type_li1_span2 = document.createElement('span');
        assistant_banner_type_li1_span2.innerHTML = '方式一：付费升级';
        assistant_banner_type_li1_left.appendChild(assistant_banner_type_li1_span1);
        assistant_banner_type_li1_left.appendChild(assistant_banner_type_li1_span2);
        var assistant_banner_type_li1_right = document.createElement('div');
        assistant_banner_type_li1_right.className = 'right';
        assistant_banner_type_li1_right.innerHTML = '￥' + data.upgradeRules[i].needMoney;
        var assistant_banner_type_li1_small = document.createElement('s');
        assistant_banner_type_li1_small.className = 'small';
        assistant_banner_type_li1_small.innerHTML = '￥' + data.upgradeRules[i].upgradeCost;
        assistant_banner_type_li1.appendChild(assistant_banner_type_li1_left);
        assistant_banner_type_li1.appendChild(assistant_banner_type_li1_right);
        assistant_banner_type_li1.appendChild(assistant_banner_type_li1_small);
        //li 2
        var assistant_banner_type_li2 = document.createElement('li');
        assistant_banner_type_li2.addEventListener('click', function () {
          assistant_banner_type_li1_span1.className = 'check';
          assistant_banner_type_li2_span1.className = 'check active';
          assistant_banner_bottom.innerHTML = '去推广';
          upType = 1;
        });
        var assistant_banner_type_li2_left = document.createElement('div');
        assistant_banner_type_li2_left.className = 'left';
        var assistant_banner_type_li2_span1 = document.createElement('span');
        assistant_banner_type_li2_span1.className = 'check';
        var assistant_banner_type_li2_span2 = document.createElement('span');
        assistant_banner_type_li2_span2.innerHTML = '方式二：推广' + data.upgradeRules[i].needCount + '个好友';
        assistant_banner_type_li2_left.appendChild(assistant_banner_type_li2_span1);
        assistant_banner_type_li2_left.appendChild(assistant_banner_type_li2_span2);
        var assistant_banner_type_li2_right = document.createElement('div');
        assistant_banner_type_li2_right.className = 'right';
        assistant_banner_type_li2_right.innerHTML = '还差' + data.upgradeRules[i].restCount + '个';
        var assistant_banner_type_li2_small = document.createElement('div');
        assistant_banner_type_li2_small.className = 'small';
        assistant_banner_type_li2_small.innerHTML = '达到推广好友数量，系统将自动为您升级';
        assistant_banner_type_li2.appendChild(assistant_banner_type_li2_left);
        assistant_banner_type_li2.appendChild(assistant_banner_type_li2_right);
        assistant_banner_type_li2.appendChild(assistant_banner_type_li2_small);
        assistant_banner_type.appendChild(assistant_banner_type_li1);
        assistant_banner_type.appendChild(assistant_banner_type_li2);
        var assistant_banner_bottom = document.createElement('div');
        assistant_banner_bottom.className = 'bottom';
        assistant_banner_bottom.innerHTML = '微信立即支付 ￥' + data.upgradeRules[i].needMoney;
        assistant_banner_bottom.addEventListener('click', function () {
          if (upType) {
            layer.style.display = 'block';
          } else {
            window.location.replace('/sqb/toBuy/' + data.upgradeRules[i].id);
          }
        });
        assistant_banner_list.appendChild(assistant_banner_head);
        assistant_banner_list.appendChild(assistant_banner_text);
        assistant_banner_list.appendChild(assistant_banner_type);
        assistant_banner_list.appendChild(assistant_banner_bottom);
        banner.appendChild(assistant_banner_list);
        break;
    }
  }
});