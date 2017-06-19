<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<html>
<head>
    <title>开卡成功</title>
</head>
<body>
<h1 style="font-size: 40px;">
    您已成功开通会员卡啦
    <a href="<%=basePath%>membership/memberInfo?mid=${mid}">会员详情</a>
    </h1>
</body>
</html>
