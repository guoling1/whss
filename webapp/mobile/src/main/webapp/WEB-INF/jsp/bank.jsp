<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/9
  Time: 19:40
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
  <link rel="stylesheet" href="/css/reset.css">
  <link rel="stylesheet" href="/css/style.css">
  <link rel="stylesheet" href="/css/weui.css">
</head>
<body>

<div id="bankList">
  <div class="group red" id="color">
    <div class="top">
      <div class="logo" id="logo"></div>
      <div class="info">
        <div class="name">${bankName}</div>
        <div class="type">储蓄卡</div>
      </div>
    </div>
    <div class="bottom">
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="word">${bankNo}</div>
      </div>
    </div>
  </div>
</div>

</body>
<script>
  var pageData = {
    bin: '${bankBin}'
  }
</script>
<script src="/js/shoes.js"></script>
<script src="/js/bank.js"></script>
</html>