<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/5
  Time: 20:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta name="format-detection" content="telephone=no"/>
  <title>收款</title>
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.21.css">--%>
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">--%>
  <script>
      function aysnLoadCss(url,cburl) {
          var link = document.createElement('link');
          link.href = url;
          link.rel = "stylesheet";
          document.head.appendChild(link);
          link.onerror = function () {
              var link = document.createElement('link');
              link.href = cburl;
              link.rel = "stylesheet";
              document.head.appendChild(link);
          }
          link.onload = function () {
              document.getElementById('payment-wx').style.opacity='1'
          }
      }
      function aysnLoadcb () {
          var script = document.createElement('script');
          script.src = "http://static.jinkaimen.cn/hss/2.2.33/collection.min.js";
          script.type = "text/javascript";
          document.head.appendChild(script);
          script.onerror = function () {
              var script = document.createElement('script');
              script.src = '/js/hss/2.2.33/collection.min.js';
              script.type = "text/javascript";
              document.head.appendChild(script);
          }
      }
      function aysnLoad(url,cburl) {
          var script = document.createElement('script');
          script.src = url;
          script.type = "text/javascript";
          document.head.appendChild(script);
          script.onload = function () {
              aysnLoadcb()
          }
          script.onerror = function () {
              var script = document.createElement('script');
              script.src = cburl;
              script.type = "text/javascript";
              document.head.appendChild(script);
              script.onload = function () {
                  aysnLoadcb()
              }
          }
      }
      window.onload = function () {
          aysnLoad('http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js','/js/hss/vendor.1.0.9.7.min.js');
          aysnLoadCss('http://static.jinkaimen.cn/hss/css/style.2.2.21.css','/css/hss/style.2.2.21.css');
          aysnLoadCss('http://static.jinkaimen.cn/weui/weui.css','/css/hss/weui.css');
      };
  </script>
</head>
<body>

<div id="payment-wx" style="opacity: 0">
  <div class="space">
    <div class="prompt"><span class="collection"></span>${merchantName}</div>
    <div class="key-ipt">
      <span class="key-title">收款金额</span>
      <span class="key-cursor animate"></span>
      <span class="key-span" id="key-span"></span>
      <span class="key-icon">￥</span>
      <input type="hidden" id="key-input" value="">
    </div>
  </div>
  <%--<div class="prompt-deep">支付宝与微信收款实时到账，无卡快捷T1到账<span id="know"></span>--%>
    <%--<br>支付宝与微信限额1万，无卡快捷限额2万--%>
  <%--</div>--%>
  <div class="keyboard" id="keyboard">
    <div class="copyright flexBox">
      <span class="line"></span>
      <span class="word">由好收收提供技术支持</span>
      <span class="line"></span>
    </div>
    <div class="keys">
      <div class="left">
        <div class="key" keyNum="1" touch="true">1</div>
        <div class="key" keyNum="2" touch="true">2</div>
        <div class="key" keyNum="3" touch="true">3</div>
        <div class="key" keyNum="4" touch="true">4</div>
        <div class="key" keyNum="5" touch="true">5</div>
        <div class="key" keyNum="6" touch="true">6</div>
        <div class="key" keyNum="7" touch="true">7</div>
        <div class="key" keyNum="8" touch="true">8</div>
        <div class="key" keyNum="9" touch="true">9</div>
        <div class="key gray"></div>
        <div class="key" keyNum="0" touch="true">0</div>
        <div class="key gray" keyNum=".">.</div>
      </div>
      <div class="qr">
        <div class="delete" keyCtrl="delete" touch="true"></div>
        <%--<div class="quick" keyCtrl="quick">--%>
        <%--<div class="logo"></div>--%>
        <%--<div class="word">无卡快捷</div>--%>
        <%--</div>--%>
        <%--<div class="wx-pay" keyCtrl="wx-ss">--%>
        <%--<div class="wx-logo"></div>--%>
        <%--<div class="word">微信</div>--%>
        <%--</div>--%>
        <%--<div class="ali-pay" keyCtrl="ali-ss">--%>
        <%--<div class="ali-logo"></div>--%>
        <%--<div class="word">支付宝</div>--%>
        <%--</div>--%>
        <div class="all-pay" keyCtrl="pay" pay="true">
          收款
        </div>
      </div>
    </div>
  </div>
</div>


<div class="message-space" id="layer">
  <div class="message-box">
    <div class="message-box-head">
      T1到账说明
      <div class="message-x" id="layer-x"></div>
    </div>
    <div class="message-box-body">
      “T1”是指下一个工作日<br>遇到周末及法定节假日顺延<br>
      <span>22:56分以后支付成功的，算到下一个工作日</span>
    </div>
    <div class="message-box-foot">
      <div class="message-enter" id="submit">
        确认
      </div>
    </div>
  </div>
</div>

</body>
<script>
  var pageData = {
    bankId: '${bankId}',
    oemNo: '${oemNo}',
      source:'2'
  }
</script>
<%--<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.14.min.js"></script>--%>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.33/collection.min.js"></script>--%>
</html>