<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>消费记录</title>
    <link rel="stylesheet" href="../../css/style.1.0.0.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="record">
    <div class="weui-cells" id="weui-cells">
        <div class="weui-cell">
            <div class="weui-cell__bd">
                <p id="title">会员卡充值</p>
                <p id="date" class="time">2017-02-14 16：38</p>
            </div>
            <div id="context" class="weui-cell__ft">￥<span class="price">55.21</span></div>
        </div>
    </div>
</div>
<%--<a href="<%=basePath%>membership/consumeListByPage?mid=${mid}&currentPage=1">test</a>--%>
</body>
<script src="../../js/jquery-2.2.3.min.js"></script>
<script src="http://static.jinkaimen.cn/hsy/js/vendor.2.0.1.3.min.js"></script>
<script src="../../js/2.0.1.1/consumeList.min.js"></script>
</html>
