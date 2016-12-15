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
  <title>好收收</title>
  <link rel="stylesheet" href="/css/reset.css">
  <link rel="stylesheet" href="/css/style.css">
  <link rel="stylesheet" href="/css/weui.css">
</head>
<body>

<div id="payment-wx">
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
        <div class="quick" keyCtrl="quick">
          <div class="logo"></div>
          <div class="word">无卡快捷</div>
          <div class="date">(D0)</div>
        </div>
        <div class="wx-zfb" keyCtrl="wx-zfb">
          <div class="wx-zfb"></div>
          <div class="word">微信/支付宝</div>
          <div class="date">(D0)</div>
        </div>
      </div>
    </div>
  </div>
</div>

</body>
<script src="../js/shoes.js"></script>
<script src="../js/collection.js"></script>
</html>