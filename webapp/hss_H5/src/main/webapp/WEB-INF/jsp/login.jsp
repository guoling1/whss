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
    <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
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
    </ul>
    <div style="padding:0 15px;">
        <button class="reg" style="width:100%;margin:0;" id="submit">登录</button>
    </div>
</div>

</body>
<script>
    var pageData = {
        mobile: '${mobile}'
    }
</script>
<script>
    var mobileInput = document.getElementById('mobile');
    if (!pageData.mobile || pageData.mobile == '') {
        mobileInput.value=pageData.mobile;
    }
</script>
<script src="/js/hss/2.0.1/vendor.1.0.1.min.js"></script>
<script src="/js/hss/2.0.1/login.min.js"></script>
</html>
