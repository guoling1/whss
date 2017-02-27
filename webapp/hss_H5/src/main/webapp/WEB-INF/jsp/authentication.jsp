<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>我的认证</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="authentication">
  <div class="group">
    <div class="name">店铺名</div>
    <div class="value">${merchantName}</div>
  </div>
  <div class="group">
    <div class="name">店铺地址</div>
    <div class="value">${address}</div>
  </div>
  <div class="group">
    <div class="name">注册日期</div>
    <div class="value">${createTime}</div>
  </div>
  <div class="group">
    <div class="name">店主姓名</div>
    <div class="value">${name}</div>
  </div>
  <div class="group">
    <div class="name">认证状态</div>
    <div class="value">已认证</div>
  </div>
  <div class="group">
    <div class="name">认证日期</div>
    <div class="value">${authenticationTime}</div>
  </div>
  <div class="group">
    <div class="name">店铺所在地</div>
    <div class="value">${district}</div>
  </div>
  <div class="group">
    <div class="name">身份证号</div>
    <div class="value">${idCard}</div>
  </div>
  <div class="group">
    <div class="name">是否认证</div>
    <div class="value">${isAuthen}</div>
  </div>
  <div class="group">
    <div class="name">信用卡号所属银行</div>
    <div class="value">${creditCardName}</div>
  </div>
  <div class="group">
    <div class="name">信用卡后四位</div>
    <div class="value">${creditCardShort}</div>
  </div>
</div>
</body>
</html>
