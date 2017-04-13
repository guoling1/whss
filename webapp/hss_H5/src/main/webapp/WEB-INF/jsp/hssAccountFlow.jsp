<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>账户流水</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.8.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="accountFlow">
  <div id="content">
    <%-- 动态加载 --%>
  </div>
  <div id="empty" class="empty">
    <div class="img"></div>
    <div class="text">没有数据</div>
  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.7.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.14/hasAccountFlow.min.js"></script>
</html>
