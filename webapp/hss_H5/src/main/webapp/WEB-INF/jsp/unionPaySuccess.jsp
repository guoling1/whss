<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2017/3/15
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta name="format-detection" content="telephone=no"/>
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.17.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="success">
  <div class="space">
    <div class="info"></div>
    <div class="text">付款成功</div>
  </div>
  <div class="group">
    <div class="left">支付流水号</div>
    <div class="right">${sn}</div>
  </div>
  <div class="group">
    <div class="left">付款银行</div>
    <div class="right">${bankName} 尾号${shortNo}</div>
  </div>
  <div class="group">
    <div class="left">付款金额</div>
    <div class="right">${amount}元</div>
  </div>
</div>
</body>
</html>
