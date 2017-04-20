<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收银</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="prompt">
  <img src="http://static.jinkaimen.cn/hss/assets/hss-prompt.png" alt="">

  <p>${message}</p>

  <div class="btn" onclick="history.go(-1)">返回</div>
</div>

</body>
</html>