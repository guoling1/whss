<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>会员卡列表</title>
    <link rel="stylesheet" href="../../css/style.1.0.0.css">
</head>
<body>
<div id="memberList">
    <div class="navbar">
        <div class="left"><span class="active">会员卡</span></div>
        <div class="right"><span>体验卡</span></div>
    </div>
    <ul>
        <li>
            <span class="name">江湖味道总店</span>
            <span class="type">尊贵金卡</span>
            <span class="discount">8.0折</span>
        </li>
    </ul>
</div>
<h1 style="font-size: 40px;">
<c:forEach items="${memberList}" var="member">
    <a href="<%=basePath%>membership/memberInfo?mid=${member.id}&source=${source}">会员卡名称<c:out value="${member.membershipName}"></c:out></a>
</c:forEach>
</h1>
</body>
</html>
