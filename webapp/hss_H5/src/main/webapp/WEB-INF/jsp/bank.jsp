<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/9
  Time: 19:40
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
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.0.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="bankList">
  <%-- 动态添加 --%>
</div>

<div class="message-space" id="layer">
  <div class="message-box">
    <div class="message-box-head">提示</div>
    <div class="message-box-body">
      确认删除该银行卡吗？<br>本系统银行卡数据为加密存储，请放心使用
    </div>
    <div class="message-box-foot">
      <div class="message-cancel" id="cancel">
        取消
      </div>
      <div class="message-line"></div>
      <div class="message-submit" id="enter">
        确定
      </div>
    </div>
  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.6.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.0/bank.min.js"></script>
</html>