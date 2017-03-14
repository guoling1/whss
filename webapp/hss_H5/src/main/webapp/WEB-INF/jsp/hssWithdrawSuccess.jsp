<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>提现成功</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.1.7.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="withdrawSuccess">
  <div class="cont">
    <div class="detail">
      <div class="logo"></div>
      <div class="text">提现申请成功</div>
      <div class="small">预计在2小时内到账</div>
    </div>
    <div class="list-box">
      <div class="list-group">
        <div class="left">到账银行卡</div>
        <div class="right" id="bank"></div>
      </div>
      <div class="list-group">
        <div class="left">提现金额</div>
        <div class="right" id="amount"></div>
      </div>
      <div class="list-group">
        <div class="left">手续费</div>
        <div class="right" id="fee"></div>
      </div>
      <div class="list-group">
        <div class="left">到账金额</div>
        <div class="right" id="come"></div>
      </div>
    </div>
  </div>
  <div class="btn-box">
    <div class="box">完成</div>
  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.5.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.1.5/withdrawSuccess.min.js"></script>
</html>
