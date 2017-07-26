<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>开卡成功</title>
    <link rel="stylesheet" href="../../css/style.2.0.2.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="createMemberSuccess">
    <img src="../assets/member/success.png" alt="">
    <p class="result">您已成功开通会员卡啦</p>
    <c:if test="${appPolicyMember.isDeposited==1}">
    <a href="javascript:;" class="toRecord">会员卡余额:￥${appPolicyMember.remainingSum}</a>
    </c:if>
    <a class="weui-btn weui-btn_plain-primary back" href="<%=basePath%>membership/memberInfo?mid=${mid}&source=${source}">会员卡详情</a>
</div>
</body>
</html>
