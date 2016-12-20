<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/6
  Time: 18:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="record">
    <div class="title">
        <span class="active">分配给二级代理</span>
        <span>分配给自己</span>
    </div>
    <div class="head">
        <span class="w30">起始号码</span>
        <span class="w30">终止号码</span>
        <span class="w12">个数</span>
        <span>日期</span>
    </div>
    <!--分配给二级-->
    <ul class="show">
        <li>
            <c:forEach items="${toSecond}" var="second">
                <p>${second.secondLevelDealerName}</p>
                <span class="w30">${second.startCode}</span>
                <span class="w30">${second.endCode}</span>
                <span class="w12">${second.count}</span>
                <span>${second.distributeDate}</span>
            </c:forEach>
        </li>
    </ul>
    <!--分配给自己-->
    <ul>
        <li>
            <c:forEach items="${toSelf}" var="self">
                <span class="w30">${self.startCode}</span>
                <span class="w30">${self.endCode}</span>
                <span class="w12">${self.count}</span>
                <span>${self.distributeDate}</span>
            </c:forEach>
        </li>
    </ul>

</div>
<jsp:include page="../message.jsp"></jsp:include>
</body>
<script src="http://img.jinkaimen.cn/hss/js/qrcode.min.js"></script>
<script src="/js/jQuery-2.1.4.min.js"></script>
<script src="/js/utility.js"></script>
<script>
    $('.title span').click(function () {
        $(this).addClass('active').siblings('span').removeClass('active');
        $('#record ul').eq($(this).index()).addClass('show').siblings('ul').removeClass('show');
    })
</script>
</html>
