<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/6
  Time: 14:42
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
  <link rel="stylesheet" href="http://static.jinkaimen.cn/dealer/css/style.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="myStore">
    <div class="header">

        <p class="w33">店铺名</p>

        <p class="w21">二维码编号</p>

        <p class="w21">注册时间</p>

        <p class="w21">商户状态</p>
    </div>
    <ul>
        <li>
            <c:forEach items="${merchants}" var="merchant">
                <div>
                    <p class="w33">${merchant.merchantName}</p>

                    <p class="w21">${merchant.code}</p>

                    <p class="w21">${merchant.registerDate}</p>

                    <p class="w21">${merchant.status}</p>
                </div>
            </c:forEach>
        </li>
    </ul>
</div>
<jsp:include page="../message.jsp"></jsp:include>
</body>
<script src="http://img.jinkaimen.cn/hss/js/qrcode.min.js"></script>
<script src="/js/jQuery-2.1.4.min.js"></script>
<script src="/js/utility.js"></script>
</html>
