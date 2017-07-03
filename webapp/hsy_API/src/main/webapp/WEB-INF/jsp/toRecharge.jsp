<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<html>
<head>
    <title>会员卡充值</title>
</head>
<body>
    <h1 style="font-size: 40px;">
        会员卡页面<br>
        会员卡名称:${appPolicyMember.membershipName}<br>
        会员卡上显示的店铺名称:${appPolicyMember.membershipShopName}<br>
        手机号:${appPolicyMember.consumerCellphone}<br>
        会员卡号:${appPolicyMember.memberCardNO}<br>
        会员卡折扣(折):${appPolicyMember.discount}<br>

        <c:if test="${appPolicyMember.isPresentedViaRecharge==1}">
            会员卡充值每满${appPolicyMember.rechargeLimitAmount}送${appPolicyMember.rechargePresentAmount}
        </c:if>

        <%--临时写的应该用ajax--%>
        <form action="<%=basePath %>membership/recharge" method="post">
            <input type="number" name="amount"  />
            <input type="hidden" name="type"  value="${type}"/>
            <input type="hidden" name="source" value="${source}" />
            <input type="hidden" name="mid" value="${appPolicyMember.id}" />
            <button id="recharge">充值</button>
        </form>
    </h1>
</body>
</html>
