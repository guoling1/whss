<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/6
  Time: 16:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收收</title>
    <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.8.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="success">
    <div class="space">
        <div class="info"></div>
        <div class="text">付款成功</div>
    </div>
    <div class="group">
        <div class="left">交易流水号</div>
        <div class="right">${firstSn}<span>${secondSn}</span></div>
    </div>
    <div class="group">
        <div class="left">付款金额</div>
        <div class="right">${money}元</div>
    </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.7.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.14/common.min.js"></script>
</html>
