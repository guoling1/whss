<%--
  Created by IntelliJ IDEA.
  User: Thinkpad
  Date: 2016/12/29
  Time: 21:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.13.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="prompt">
  <img src="http://static.jinkaimen.cn/hss/assets/hss-prompt.png" alt="">

  <p>${message}</p>

  <div class="btn" onclick="WeixinJSBridge.call('closeWindow')">返回</div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.9.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.20/common.min.js"></script>
</html>
