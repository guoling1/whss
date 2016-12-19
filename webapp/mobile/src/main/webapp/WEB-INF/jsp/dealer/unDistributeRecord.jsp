<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/19
  Time: 14:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收收</title>
    <link rel="stylesheet" href="../css/reset.css">
    <link rel="stylesheet" href="../css/style.css">
</head>

<body>
<div id="record">
    <div class="head">
        <span class="w30" style="width: 38%;">起始号码</span>
        <span class="w30" style="width: 38%;">终止号码</span>
        <span class="w12">个数</span>
    </div>
    <ul style="display:block">
        <li style="height: 34px;line-height: 34px;">
            <c:forEach items="${codes}" var="code">
                <span class="w30" style="width: 38%;">${code.startCode}</span>
                <span class="w30" style="width: 38%;">${code.endCode}</span>
                <span class="w12">${code.count}</span>
            </c:forEach>
        </li>
    </ul>
</div>
<jsp:include page="../message.jsp"></jsp:include>
</body>
</html>
