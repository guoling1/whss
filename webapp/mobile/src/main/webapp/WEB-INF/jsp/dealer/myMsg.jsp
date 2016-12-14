<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/5
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收银</title>
    <!--<link rel="stylesheet" href="http://img.jinkaimen.cn/hsy/css/reset.css">-->
    <!--<link rel="stylesheet" href="http://img.jinkaimen.cn/hsy/css/style.css">-->
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div id="myMsg">
    <ul>
        <li>
            <span>手机号</span>
            <span class="right">${data.mobile}</span>
        </li>
        <li>
            <span>名称</span>
            <span class="right">${data.proxyName}</span>
        </li>
        <li>
            <span>所在地</span>
            <span class="right">${data.belongArea}</span>
        </li>
        <li>
            <span>支付宝收单结算价</span>
            <span class="right">${data.alipaySettleRate}%</span>
        </li>
        <li>
            <span>微信收单结算价</span>
            <span class="right">${data.weixinSettleRate}%</span>
        </li>
        <li>
            <span>快捷收单结算价</span>
            <span class="right">${data.quickSettleRate}%</span>
        </li>
        <li>
            <span>提现结算价</span>
            <span class="right">${data.withdrawSettleFee}元</span>
        </li>
    </ul>
    <div class="submit" onclick="(function(){window.history.go(-1)})()">
        确定
    </div>
</div>
</body>
<script src="http://img.jinkaimen.cn/hsy/js/qrcode.min.js"></script>
</html>