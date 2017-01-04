<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
  <style>
    .reg:disabled {
      background-color: #b0c3f2 !important;
    }
  </style>
</head>
<body>

<div id='reg'>
  <h1>好收收-商户</h1>

  <h2>HAOSHOUSHOU</h2>
  <ul>
    <li class="top flexBox">
      <div class="logo phone"></div>
      <input type="number" placeholder="请输入手机号" id="mobile">

      <div class="btn" id="sendCode">发送验证码</div>
    </li>
    <li class="bottom flexBox">
      <div class="logo message"></div>
      <input type="number" placeholder="请输入验证码" id="code">
    </li>
    <li class="bottom flexBox">
      <div class="logo"
           style="background:url(http://static.jinkaimen.cn/hss/assets/code2.png) center no-repeat;background-size: 21px 15px;"></div>
      <input type="text" placeholder="请通过扫码注册获取邀请码" id="qrcode" value="${qrCode}" readonly>
    </li>
  </ul>
  <div style="padding:0 15px;">
    <button class="reg" style="width:100%;margin:0;" id="submit">注册</button>
  </div>
</div>

</body>
<script>
  var pageData = {
    qrCode: '${qrCode}'
  }
</script>
<script>
  if (!pageData.qrCode || pageData.qrCode == '') {
    var dis = document.getElementById('submit');
    dis.setAttribute('disabled', 'true');
  }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/0.1.19/reg.min.js"></script>
</html>
