<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收-登录</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.5.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id='login'>
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
  <div class="reg">
    <button class="reg-btn" id="submit">登录</button>
  </div>
</div>

</body>
<script>
  var _hmt = _hmt || [];
  (function () {
    var hm = document.createElement("script");
    hm.src = "https://hm.baidu.com/hm.js?a50541c927a563018bb64f5d6c996869";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
  })();
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.5.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.9/login.min.js"></script>
</html>
