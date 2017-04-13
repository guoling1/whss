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
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.8.css">--%>
  <link rel="stylesheet" href="/css/hss/style.2.2.8.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
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
  <div class="prompt-deep">支付宝与微信收款实时到账，无卡快捷T1到账<span id="know"></span>
    <br>支付宝与微信限额1万，无卡快捷限额2万
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
        <div class="all-pay" keyCtrl="pay">
          <div class="word big">收款</div>
        </div>
      </div>
    </div>
    <div class="channel-box" id="channelBox">
      <div class="channel-space top">
        <div class="channel-group">
          <div class="channel-li big">渠道名称</div>
          <div class="channel-li">结算时间<span id="know-channel"></span></div>
          <div class="channel-li small">费率</div>
          <div class="channel-li">单笔限额</div>
        </div>
      </div>
      <div class="channel-small">各通道限额动态变化，一次不通过可换其他通道重试</div>
      <div class="channel-space" id="channel">
        <%-- 动态添加 --%>
      </div>
    </div>
  </div>
</div>

<div class="message-space" id="layer-channel">
  <div class="message-box">
    <div class="message-box-head">
      结算时间说明
      <div class="message-x" id="layer-x-channel"></div>
    </div>
    <div class="message-box-body">
      <div>“D0”是指当日<br>一般在十分钟内到账结算卡<br></div>
      <div style="margin-top:10px">“T1”是指下一个工作日<br>遇到周末及法定节假日顺延<br></div>
      <span>22:56分以后支付成功的，算到下一个工作日</span>
    </div>
    <div class="message-box-foot">
      <div class="message-enter" id="submit-channel">
        确认
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
    bankId: '${bankId}'
  }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.7.min.js"></script>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.12/collection.min.js"></script>--%>
<script src="/js/hss/2.2.12/collection.min.js"></script>
</html>