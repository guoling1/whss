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
  <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
  <title>好收收-钱包</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="wallet">
  <div class="wallet">
    <a class="operation touch_op" href="/sqb/collection">
      <div class="logo collection"></div>
      <div class="operation-title">收款</div>
    </a>

    <div class="operation" id="tt">
      <div class="logo wallet"></div>
      <div class="operation-title">余额</div>
      <div class="operation-small">${avaliable}</div>
    </div>
    <div class="operation">
      <div class="logo card"></div>
      <div class="operation-title">提现</div>
    </div>
  </div>
  <div class="record">
    <a href="/sqb/tradeRecord" class="group touch_gr"><span class="record"></span>收款记录</a>
    <a href="/sqb/bank" class="group touch_gr"><span class="bank"></span>银行卡</a>
    <a href="javascript:void(0)" class="group"><span class="user"></span>用户认证</a>
    <a href="http://mp.weixin.qq.com/s/-GYEGM7PAboICupcETBPhw" class="group touch_gr"><span class="help"></span>使用帮助</a>
  </div>
  <div class="flexBox flex-box-column">
    <div class="advertisement">
      <img src="http://static.jinkaimen.cn/hss/assets/banner.png" alt="">
    </div>
    <div class="application">
      <a class="group touch_gr" href="/sqb/myRecommend">
        <div class="logo friend"></div>
        <div class="text">推荐好友</div>
      </a>
      <a class="group touch_gr" href="/sqb/upgradeMax">
        <div class="logo upgrade"></div>
        <div class="text">我要升级</div>
      </a>
      <a class="group touch_gr" href="/sqb/ticket">
        <div class="logo train"></div>
        <div class="text">火车票</div>
      </a>
      <!--<div class="group">-->
      <!--<div class="logo movie"></div>-->
      <!--<div class="text">电影票</div>-->
      <!--</div>-->
      <a class="group touch_gr" href="https://loan.rongba.com/H5tuiguang/kff?ref=hd_11015964&sid=haoshouyin001">
        <div class="logo loan"></div>
        <div class="text">贷款申请</div>
      </a>
      <a class="group touch_gr" href="http://channel.haodai.com/Mobile/creditcard/ref/hd_11015964/sid/haoshouyin001">
        <div class="logo credit"></div>
        <div class="text">信用卡申请</div>
      </a>
      <%--<div class="group touch_gr">--%>
      <%--<div class="logo more"></div>--%>
      <%--<div class="text">更多</div>--%>
      <%--</div>--%>
    </div>
  </div>
</div>
</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.0.1/wallet.min.js"></script>
</html>