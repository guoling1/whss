/**
 * Created by wangl on 2016/12/16.
 */

// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();
// teb切换
let title = document.getElementById('title');
let ownTitle = document.getElementById('ownTitle');
let content = document.getElementById('content');
let ownContent = document.getElementById('ownContent');

title.addEventListener('click', function () {
  title.className = 'active';
  ownTitle.className = '';
  content.className = 'show';
  ownContent.className = '';
});

ownTitle.addEventListener('click', function () {
  title.className = '';
  ownTitle.className = 'active';
  content.className = '';
  ownContent.className = 'show';
});