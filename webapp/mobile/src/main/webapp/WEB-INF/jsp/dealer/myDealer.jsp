<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/6
  Time: 15:22
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
<div id="myDealer">
    <div class="header">
        <p class="w1">二级代理</p>

        <p class="w3">配码数</p>

        <p class="w3">已激活</p>
    </div>
    <ul>
        <li>
            <c:forEach items="${dealers}" var="dealer">
                <div class="">
                    <p class="w1">${dealer.proxyName}</p>

                    <p class="w3 blue" onclick="showMyDetail(${dealer.dealerId}, ${dealer.distributeCount}, ${dealer.activeCount})">${dealer.distributeCount}</p>

                    <p class="w3 blue">${dealer.activeCount}</p>
                </div>
            </c:forEach>
        </li>
    </ul>
    <!--配码详情-->
    <div id="dealerDetail" class="mask" style="display: none">
        <div class="content">
            <h3 id="proxyName"></h3>
            <span class="X">×</span>

            <div class="total">
                <p>手机号:<span id="mobile"></span></p>

                <p>总数:<span id="distributeCount"></span></p>

                <p>已激活:<span id="activateCount"></span></p>
            </div>
            <div class="detail">
                <div class="title">
                    <p class="w35">配码日期</p>

                    <p class="w50">码段</p>

                    <p>个数</p>
                </div>
                <div id="qrcode" class="content">
                    <%--<div class="row">--%>
                        <%--<p class="w35"></p>--%>

                        <%--<p class="w50 middle">-</p>--%>

                        <%--<p></p>--%>
                    <%--</div>--%>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../message.jsp"></jsp:include>
</body>

<script src="http://img.jinkaimen.cn/hss/js/qrcode.min.js"></script>
<script src="/js/jQuery-2.1.4.min.js"></script>
<script src="/js/utility.js"></script>
<script src="/js/myDealer.js"></script>
<script>
    $('.X').click(function () {
        $('.mask').hide()
    })
</script>
</html>
