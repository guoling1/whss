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
        <div class="left"><span class="active" id="left">会员卡</span></div>
        <div class="right"><span id="right">体验卡</span></div>
    </div>
    <ul class="leftList" id="leftList">
        <a>
            <span class="name">江湖味道总店</span>
            <span class="type">尊贵金卡</span>
            <span class="discount"><span>8</span>.0折</span>
        </a>
    </ul>
    <ul class="rightList hide" id="rightList">
        <a>
            <span class="name">江湖味道总店</span>
            <span class="type">体验卡</span>
            <span class="discount"><span>8</span>.0折</span>
        </a>
    </ul>
</div>
<h1 style="font-size: 40px;">
<c:forEach items="${memberList}" var="member">
    <a href="<%=basePath%>membership/memberInfo?mid=${member.id}&source=${source}">会员卡名称<c:out value="${member.membershipName}"></c:out></a>
</c:forEach>
</h1>
</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js"></script>
<script src="../../js/2.0.1.1/memberList.min.js"></script>
</html>
