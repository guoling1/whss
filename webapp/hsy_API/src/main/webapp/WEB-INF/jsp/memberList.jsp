<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>会员卡列表</title>
</head>
<body>
<h1 style="font-size: 40px;">
<c:forEach items="${memberList}" var="member">
    <a href="<%=basePath%>membership/memberInfo?mid=${member.id}&source=${source}">会员卡名称<c:out value="${member.membershipName}"></c:out></a>
</c:forEach>
</h1>
</body>
</html>
