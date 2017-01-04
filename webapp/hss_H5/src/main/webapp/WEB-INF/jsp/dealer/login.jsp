<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/5
  Time: 20:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/dealer/css/style.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id='login'>
  <h1>好收收-代理商</h1>

  <h2>HAOSHOUSHOU</h2>
  <ul>
    <li class="top flexBox">
      <div class="logo phone"></div>
      <input type="number" id="mobile" placeholder="请输入手机号">

      <div class="btn" id="sendCode" style="background: #fff">发送验证码</div>
    </li>
    <li class="bottom flexBox">
      <div class="logo message"></div>
      <input type="number" id="code" placeholder="请输入验证码">
    </li>
  </ul>
  <div class="reg" id="submit">登录</div>
</div>
</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
<script src="http://static.jinkaimen.cn/dealer/0.1.1/dealerLogin.min.js"></script>
</html>
