<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/5
  Time: 21:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.3.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="chargeCode" class="flexBox flex-box-column">
  <div class="content">
    <div class="top">
      <p class="shop">${subMerName}</p>

      <p class="price">￥<span>${amount}</span></p>
      <i class="left"></i>
      <i class="right"></i>
    </div>
    <div class="bottom">
      <div class="qr" id="qr"></div>

      <div class="red_mpt">请勿在微信内长按二维码识别</div>
      <div class="bar">
        <img src="http://static.jinkaimen.cn/hss/assets/wxb.png" alt=""/>
        <img src="http://static.jinkaimen.cn/hss/assets/zfbb.png" alt=""/>
        <span>截图保存后识别二维码付款</span>
      </div>
    </div>
  </div>
  <div class="explain">
    <p class="refresh">二维码仅支持扫描一次，<span class="reload" id="refresh">
      <img src="http://static.jinkaimen.cn/hss/assets/reload.png"/>点击刷新</span></p>

    <p class="server">由好收收提供服务</p>
  </div>
</div>
</body>
<script type="text/javascript">
  var pageData = {
    payUrl: '${payUrl}',
    amount: '${amount}'
  }
</script>
<script src="http://static.jinkaimen.cn/qrcode/qrcode.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.8.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.15/charge.min.js"></script>
</html>
