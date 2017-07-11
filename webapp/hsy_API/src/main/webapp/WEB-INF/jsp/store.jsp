<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>商家信息</title>
    <link rel="stylesheet" href="../../css/style.1.0.0.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="record">
    <div class="weui-cells">
        <c:forEach items="${appBizShopList}" var="shop">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p><c:out value="${shop.shortName}"></c:out></p>
                    <p class="time"><c:out value="${shop.address}"></c:out></p>
                </div>
                <div class="weui-cell__ft"><img src="../../assets/member/phone.png" alt="<c:out value="${shop.contactCellphone}"></c:out>"></div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
