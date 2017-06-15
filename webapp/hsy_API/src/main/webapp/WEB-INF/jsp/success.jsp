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
  <title>付款成功</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hsy/css/style.1.0.0.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="success">
  <div class="space">
    <div class="info"></div>
    <div class="text">付款成功</div>
  </div>
  <div class="group">
    <div class="left">确认码</div>
    <div class="right">${code}</div>
  </div>
  <div class="group">
    <div class="left">交易流水号</div>
    <div class="right">${sn}</div>
  </div>
  <div class="group">
    <div class="left">付款金额</div>
    <div class="right">${money}元</div>
  </div>
  <a href="https://www.yumilc.com/motherDay/html/motherDay.html" class="ad">
    <img src="http://static.jinkaimen.cn/hsy/assets/ym-ad.jpeg" alt="">
  </a>
</div>

</body>
</html>
