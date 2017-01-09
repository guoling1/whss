<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收-注册</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
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
    <li class="bottom flexBox" id="invite" style="display:none;">
      <div class="logo invite"></div>
      <input style="height:49px;" type="text" placeholder="邀请人注册手机号" id="inviteCode" value="${inviteCode}">
    </li>
  </ul>
  <div class="reg">
    <button class="reg-btn" id="submit">注册</button>
  </div>
  <a class="login" href="/sqb/login">已有账号，直接登录</a>

  <div class="layer" id="layer">
    <div class="space">
      <div class="space-title">
        手机号已注册
        <div class="xx" id="xx"></div>
      </div>
      <div class="space-cont">
        <div class="cont-detail">手机号已经注册，确认使用该手机号登录？</div>
        <div class="operation">
          <div class="cancel" id="cancel">取消</div>
          <div class="submit" id="login">登录</div>
        </div>
      </div>
    </div>
  </div>
</div>

</body>
<script>
  var pageData = {
    qrCode: '${qrCode}',
    inviteCode: '${inviteCode}'
  }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.0.1/reg.min.js"></script>
</html>
