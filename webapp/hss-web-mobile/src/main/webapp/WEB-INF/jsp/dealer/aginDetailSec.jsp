<%--
  Created by IntelliJ IDEA.
  User: xiang.yu
  Date: 2016/12/5
  Time: 20:06
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
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div id="aginDetailSec">
    <div class="header">
        <p class="w33">商户名称</p>

        <p class="w21">收单分润</p>

        <p class="w21">提现分润</p>

        <p class="w21">分润总额</p>
    </div>
    <ul>
    <c:forEach items="${data}" var="everyDay">
        <li>
            <div class="total">
                <p class="w33">${everyDay.profitDate}</p>

                <p class="w21 decimal">${everyDay.dayCollectTotalMoney}</p>

                <p class="w21 decimal">${everyDay.daywithdrawTotalMoney}</p>

                <p class="red w21 decimal">${everyDay.dayTotalMoney}</p>
            </div>
            <c:forEach items="${everyDay.list}" var="detail">
            <div class="" v-for="list in info.list">
                <p class="w33">${detail.merchantName}</p>

                <p class="w21 decimal">${detail.collectMoney}</p>

                <p class="w21 decimal">${detail.withdrawMoney}</p>

                <p class="w21 decimal">${detail.totalMoney}</p>
            </div>
             </c:forEach>
        </li>
        </c:forEach>
    </ul>
</div>
</body>
<script src="http://img.jinkaimen.cn/hsy/js/qrcode.min.js"></script>
<script src="../js/dealerIndex.js"></script>
</html>