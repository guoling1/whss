<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/6
  Time: 18:15
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
    <!--<link rel="stylesheet" href="http://img.jinkaimen.cn/hsy/css/reset.css">-->
    <!--<link rel="stylesheet" href="http://img.jinkaimen.cn/hsy/css/style.css">-->
    <link rel="stylesheet" href="../css/reset.css">
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
<div id="issueSuccess">
    <div class="success">
        <img src="http://img.jinkaimen.cn/hsy/assets/succ.png" alt="" />
        <p>分配成功</p>
    </div>
    <ul>
        <li>
            <span>代理商名称</span>
            <span class="right">${data.name}</span>
        </li>
        <li>
            <span>代理商手机</span>
            <span class="right">${data.mobile}</span>
        </li>
        <li>
            <span>分配时间</span>
            <span class="right">${data.distributeDate}</span>
        </li>
        <li>
            <span>分配个数</span>
            <span class="right">${data.count}个</span>
        </li>
        <li>
            <span>分配号段</span>
            <span class="right">${data.startCode}至${data.endCode}</span>
        </li>
    </ul>
    <div class="submit" onclick="window.location.replace('/dealer/toDistributeCode')">
        确定
    </div>
</div>
</body>
<script src="http://img.jinkaimen.cn/hsy/js/qrcode.min.js"></script>
</html>