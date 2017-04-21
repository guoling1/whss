<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.11.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="prompt">
  <img src="http://static.jinkaimen.cn/hss/assets/book.png" alt="">

  <p>您的审核未通过,请重新提交审核资料</p>

  <p class="small">${res}</p>
  <a href="/sqb/repeatAddInfo/${id}" class="btn">重新提交资料</a>
</div>
</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.9.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.17/common.min.js"></script>
</html>