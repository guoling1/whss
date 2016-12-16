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
  <link rel="stylesheet" href="/css/reset.css">
  <link rel="stylesheet" href="/css/style.css">
  <link rel="stylesheet" href="/css/weui.css">
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
  </ul>
  <div class="reg" id="submit">注册</div>
</div>

</body>
<script>
  var pageData = {
    qrCode: '${qrCode}'
  }
</script>
<script src="/js/shoes.js"></script>
<script src="/js/reg.js"></script>
</html>
