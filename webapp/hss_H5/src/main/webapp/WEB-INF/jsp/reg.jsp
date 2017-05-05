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
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.15.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id='reg'>
  <h1>好收收-商户</h1>

  <h2>HAOSHOUSHOU</h2>
  <ul>
    <li class="top fly_wing_item">
      <div class="main">
        <input class="inner" type="number" placeholder="请输入手机号" id="mobile">
      </div>
      <div class="logo phone left"></div>

      <div class="btn right" id="sendCode">发送验证码</div>
    </li>
    <li class="bottom fly_wing_item_nr">
      <div class="main">
        <input class="inner" type="number" placeholder="请输入验证码" id="code">
      </div>
      <div class="logo message left"></div>
    </li>
    <li class="bottom fly_wing_item_nr" id="invite" style="display:none;">
      <div class="main">
        <input class="inner" type="text" placeholder="邀请人注册手机号或邀请码" id="inviteCode" value="${inviteCode}">
      </div>
      <div class="logo invite left"></div>
    </li>
  </ul>
  <div class="reg">
    <button class="reg-btn" id="submit">注册</button>
  </div>
  <a class="login" href="/sqb/login">已有账号，直接登录</a>

  <div class="message-space" id="layer">
    <div class="message-box">
      <div class="message-box-head">手机号已注册</div>
      <div class="message-box-body">
        手机号已经注册<br>确认使用该手机号登录？
      </div>
      <div class="message-box-foot">
        <div class="message-cancel" id="cancel">
          取消
        </div>
        <div class="message-line"></div>
        <div class="message-submit" id="login">
          登录
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
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.10.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.22/reg.min.js"></script>
</html>
