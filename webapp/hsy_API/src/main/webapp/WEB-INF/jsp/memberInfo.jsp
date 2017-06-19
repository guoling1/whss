<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>会员卡</title>
</head>
<body>
    <h1 style="font-size: 40px;">
        会员卡页面<br>
        会员卡名称:${appPolicyMember.membershipName}<br>
        会员卡上显示的店铺名称:${appPolicyMember.membershipShopName}<br>
        手机号:${appPolicyMember.consumerCellphone}<br>
        会员卡号:${appPolicyMember.memberCardNO}<br>
        会员卡折扣(折):${appPolicyMember.discount}<br>
        <c:choose>
            <c:when test="${appPolicyMember.userID!=null&&appPolicyMember.userID!=''}">
                支付宝已绑定
            </c:when>
            <c:otherwise>
                支付宝未绑定
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${appPolicyMember.openID!=null&&appPolicyMember.openID!=''}">
                微信已绑定
            </c:when>
            <c:otherwise>
                微信未绑定
            </c:otherwise>
        </c:choose>
    </h1>
</body>
</html>
