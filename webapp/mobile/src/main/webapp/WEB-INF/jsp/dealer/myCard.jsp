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
    <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/dealer/css/style.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="myCard">
    <ul>
        <li>
            <span>姓名</span>
            <span class="right">${data.bankAccountName}</span>
        </li>
        <li>
            <span>结算卡</span>
            <span class="right">${data.bankCard}</span>
        </li>
        <li>
            <span>手机号</span>
            <span class="right">${data.mobile}</span>
        </li>
    </ul>
    <div class="submit" onclick="(function(){window.history.go(-1)})()">
        确定
    </div>
</div>
</body>
<script src="http://img.jinkaimen.cn/hss/js/qrcode.min.js"></script>
</html>