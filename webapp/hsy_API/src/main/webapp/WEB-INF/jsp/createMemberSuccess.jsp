<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>开卡成功</title>
</head>
<body>
<h1 style="font-size: 40px;">
    您已成功开通会员卡啦
    <c:if test="${appPolicyMember.isDeposited==1}">
        余额${appPolicyMember.remainingSum}
    </c:if>
    <a href="<%=basePath%>membership/memberInfo?mid=${mid}&source=${source}">会员详情</a>
    </h1>
</body>
</html>
