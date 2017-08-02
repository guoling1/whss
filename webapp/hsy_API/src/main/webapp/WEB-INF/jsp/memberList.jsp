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
    <link rel="stylesheet" href="../../css/style.2.0.2.css">
</head>
<body>
<div id="memberList">
    <div class="navbar">
        <div class="left"><span class="active" id="left">会员卡</span></div>
        <%--<div class="right"><span id="right">体验卡</span></div>--%>
    </div>
        <%--<a href="<%=basePath%>membership/memberInfo?mid=${member.id}&source=${source}">会员卡名称<c:out value="${member.membershipName}"></c:out></a>--%>
        <ul class="leftList" id="leftList" <c:if test="${memberList.size()<1}">style="display: none" </c:if>>
            <c:forEach items="${memberList}" var="member">
            <li>
                <a href="<%=basePath%>membership/memberInfo?mid=${member.id}&source=${source}" style="display: inline-block;width: 100%;height: 100%;">
                <span class="name"><c:out value="${member.membershipShopName}"></c:out></span>
                <span class="type"><c:out value="${member.membershipName}"></c:out></span>
                <span class="discount" <c:if test="${member.discountInt==10}">style="display:none;"</c:if> ><span>${member.discountInt}</span>.${member.discountFloat}折</span>
                </a>
            </li>
            </c:forEach>
        </ul>
    <div id="empty" class="empty" <c:if test="${memberList.size()<1}">style="display: block" </c:if>>
        <div class="img"></div>
        <div class="text">还没有会员卡</div>
    </div>
</div>
</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.2.0.2.min.js"></script>
<script src="http://static.jinkaimen.cn/hsy/js/2.0.2.1/memberList.min.js"></script>
</html>
