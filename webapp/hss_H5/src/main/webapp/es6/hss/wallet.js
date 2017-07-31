/**
 * Created by administrator on 2016/12/9.
 */

// 引入http message
const message = _require('message');
const http = _require('http');
// 引入 touch 动画
const Touch = _require('touch');
new Touch('touch_op', '#4b65a8', 'deep', '0.2');
new Touch('touch_gr', '#FFF', 'deep', '0.1');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// 定义变量
const layer = document.getElementById('layer');
const xx = document.getElementById('xx');
const cancel = document.getElementById('cancel');
const unbundling = document.getElementById('unbundling');
const unbundlingSubmit = document.getElementById('unbundlingSubmit');
const upgrade = document.getElementById('show_upgrade');
const announcement_link = document.getElementById('announcement-link');
announcement_link.addEventListener('click', function () {
  window.location.href = "/notice/noticeListJSP";
});

// 公告横向的滚动
let announcement = document.getElementById('announcement');
let announcement_text_box = document.getElementById('announcement-text-box');
let announcement_text = document.getElementById('announcement-text');

const pxPerRem = document.documentElement.clientWidth;
upgrade.addEventListener('click', function () {
  window.location.href = '/sqb/upgradeMax?oemNo=' + pageData.oemNo;
});
// 是否展示升级功能
if (pageData.showRecommend == 2) {
  document.getElementById('show_recommend').style.display = "none";
  document.getElementById('show_upgrade').style.display = "none";
}

// 获取公告数据
http.post('/notice/pageAnnouncement', {
  productType: 'hss',
}, function (data) {
  if (data) {
    announcement.style.display = 'block';
    announcement_text.innerHTML = data.title.replace(/<[^>]+>/g, "");
    let announcement_text_box_ClientRect = announcement_text_box.getBoundingClientRect();
    let initLeft = announcement_text_box_ClientRect.width;
    let announcement_text_ClientRect = announcement_text.getBoundingClientRect();
    let runLeft = announcement_text_ClientRect.width;
    setInterval(function () {
      let nowLeft = initLeft--;
      announcement_text.style.left = nowLeft + 'px';
      if (nowLeft <= -runLeft) {
        initLeft = announcement_text_box_ClientRect.width;
      }
    }, 15);
    announcement.onclick = function () {
      window.location.href = '/notice/noticeDetailsJSP?id=' + data.id;
    };
  }
});

// 解绑微信
unbundling.addEventListener('click', function () {
  layer.style.display = 'block';
});

xx.addEventListener('click', function () {
  layer.style.display = 'none';
});

cancel.addEventListener('click', function () {
  layer.style.display = 'none';
});

unbundlingSubmit.addEventListener('click', function () {
  http.post('/wx/unbundling', {}, function (res) {
    message.prompt_show('解绑成功');
    layer.style.display = 'none';
    window.location.href = '/sqb/login?oemNo=' + pageData.oemNo;
  })
});