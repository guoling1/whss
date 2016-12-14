<%--
  Created by IntelliJ IDEA.
  User: xiang.yu
  Date: 2016/12/5
  Time: 14:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
  <script src="../js/jQuery-2.1.4.min.js"></script>
</head>
<body>

<div id="wallet">
  <div class="wallet">
    <a class="operation" href="/sqb/collection">
      <div class="logo collection"></div>
      <div class="operation-title">收款</div>
    </a>

    <div class="operation" id="tt">
      <div class="logo wallet"></div>
      <div class="operation-title">余额</div>
      <div class="operation-small">${avaliable}</div>
    </div>
    <a class="operation" href="/sqb/drawCash">
      <div class="logo card"></div>
      <div class="operation-title">提现</div>
    </a>
  </div>
  <div class="record">
    <div class="group" id="trade_reord"><span class="money"></span>交易记录</div>
    <div class="group" id="bank_record"><span class="card"></span>银行卡</div>
  </div>
  <div class="flexBox flex-box-column">
    <div class="advertisement">

    </div>
    <div class="application">
      <div class="group">
        <div class="ing"></div>
        <%--<img src='../assets/shengjizhong.png'/>--%>
        <div class="logo friend"></div>
        <div class="text">推荐好友</div>
      </div>
      <div class="group">
        <div class="logo upgrade"></div>
        <div class="text">我要升级</div>
      </div>
      <a class="group" href="/sqb/ticket">
        <div class="logo train"></div>
        <div class="text">火车票</div>
      </a>
      <!--<div class="group">-->
      <!--<div class="logo movie"></div>-->
      <!--<div class="text">电影票</div>-->
      <!--</div>-->
      <a class="group" href="https://loan.rongba.com/H5tuiguang/kff?ref=hd_11015964&sid=haoshouyin001">
        <div class="logo loan"></div>
        <div class="text">贷款申请</div>
      </a>
      <a class="group" href="http://channel.haodai.com/Mobile/creditcard/ref/hd_11015964/sid/haoshouyin001">
        <div class="logo credit"></div>
        <div class="text">信用卡申请</div>
      </a>

      <div class="group">
        <div class="logo more"></div>
        <div class="text">更多</div>
      </div>
    </div>
  </div>
</div>
</body>
<script src="/js/shoes.js"></script>
<script src="/js/wallet.js"></script>
<script>
  var oper = document.getElementsByClassName('operation');
  for (var i = 0; i < oper.length; i++) {
    oper[i].addEventListener('touchstart', function () {
      this.style.background = "#495f97"
    });
    oper[i].addEventListener('touchend', function () {
      this.style.background = "#4b65a8"
    })
  }
  var group = document.getElementsByClassName('group');
  for (var i = 0; i < group.length; i++) {
    group[i].addEventListener('touchstart', function () {
      this.style.background = "#e2e3e5"
    })
    group[i].addEventListener('touchend', function () {
      this.style.background = "#fff"
    })
  }


  $("#trade_reord").click(function () {
    window.location.href = "/sqb/tradeRecord";
  });
  $("#bank_record").click(function () {
    window.location.href = "/sqb/bank";
  });
</script>
</html>