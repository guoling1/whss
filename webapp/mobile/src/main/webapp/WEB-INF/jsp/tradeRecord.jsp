<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/9
  Time: 19:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div class="flexBox flex-box-column" id="transaction">
  <div class="query flexBox">
    <div class="logo"></div>
    <div class="input flexBox">
      <input type="text" readonly id="dateFrom">
      <span>至</span>
      <input type="text" readonly id="dateTo">
    </div>
  </div>
  <div class="list" id="content">

  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/weui/weui.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/0.1.19/tradeRecord.min.js"></script>
</html>