<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>会员卡充值</title>
    <link rel="stylesheet" href="../../css/style.1.0.0.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="toRecharge">
    <div class="top">
        <div class="card">
            <span class="name">江湖味道总店</span>
            <span class="type">尊贵金卡</span>
            <span class="discount"><span>8</span>.0折</span>
        </div>
        <div class="phone">手机号：13833212332</div>
        <div class="title">余额：</div>
        <div class="price">￥<span>566.00</span></div>
    </div>
    <div class="weui-cells weui-cells_form">
        <div class="weui-cell">
            <div class="weui-cell__hd"><label class="weui-label">充值金额</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入金额">
            </div>
        </div>
    </div>
    <div class="weui-btn weui-btn_primary" style="margin: 30px 15px 0">充值</div>
</div>
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
