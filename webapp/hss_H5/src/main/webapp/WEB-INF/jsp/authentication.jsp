<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>我的推广</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">

  <style>
    html, body {
      width: 100%;
      height: 100%;
    }

    #authentication {
      width: 100%;
      height: 100%;
      background-color: #f4f5f8;
    }

    .space {
      padding: 15px;
      background-color: #FFF;
      margin-bottom: 8px;
    }

    .mark {
      width: 85px;
      height: 21px;
      border-radius: 3px;
      background-color: #f8ac74;
      font-size: 12px;
      color: #FFF;
      line-height: 21px;
      margin-bottom: 5px;
      text-align: center;
    }

    .mark.green {
      background-color: #4ecab1;
    }

    .store {
      display: inline-block;
      width: 14px;
      height: 12px;
      background: url("../../assets/store.png") center;
      background-size: 14px 12px;
      margin-right: 5px;
      transform: translate3d(0, 2px, 0);
    }

    .people {
      display: inline-block;
      width: 14px;
      height: 14px;
      background: url("../../assets/people.png") center;
      background-size: 14px 14px;
      margin-right: 5px;
      transform: translate3d(0, 2px, 0);
    }

    .icard {
      display: inline-block;
      width: 14px;
      height: 12px;
      background: url("../../assets/icard.png") center;
      background-size: 14px 12px;
      margin-right: 5px;
      transform: translate3d(0, 2px, 0);
    }

    .list {
      overflow: hidden;
      line-height: 35px;
    }

    .left {
      float: left;
      font-size: 14px;
      color: #9ba1bb;
    }

    .right {
      float: right;
      font-size: 14px;
      color: #35405b;
    }

    .btn {
      float: right;
      font-size: 14px;
      color: #36c493;
    }

    .small {
      font-size: 12px;
      color: #aab0c6;
    }
  </style>
</head>
<body>
<div id="authentication">

  <div class="space">
    <div class="mark"><span class="store"></span>已认证</div>
    <div class="list">
      <div class="left">店铺名:</div>
      <div class="right">${merchantName}</div>
    </div>
    <div class="list">
      <div class="left">店铺所在地:</div>
      <div class="right">${address}</div>
    </div>
    <div class="list">
      <div class="left">详细地址:</div>
      <div class="right">东城区********7号</div>
    </div>
    <div class="list">
      <div class="left">注册时间:</div>
      <div class="right">${createTime}</div>
    </div>
  </div>

  <div class="space">
    <div class="mark"><span class="people"></span>已认证</div>
    <div class="list">
      <div class="left">店主姓名:</div>
      <div class="right">${name}</div>
    </div>
    <div class="list">
      <div class="left">身份证号:</div>
      <div class="right">420*************01</div>
    </div>
  </div>

  <div class="space">
    <div class="mark green"><span class="icard"></span>信用卡</div>
    <div class="list">
      <div class="left">信用卡号:</div>
      <%--<div class="right">招商银行 尾号9098</div>--%>
      <div class="btn">立即认证</div>
    </div>
    <div class="list">
      <span class="small">认证通过后可使用大额收款</span>
    </div>
  </div>
</div>
</body>
</html>
