<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>会员卡</title>
    <link rel="stylesheet" href="http://static.jinkaimen.cn/hsy/css/style.2.0.2.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/hsy/css/swiper-3.4.2.min.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="createMem">
    <div class="swiper-container top" id="swiper">
        <div class="swiper-wrapper" style="width: 100%">
            <c:forEach items="${cardList }" var="entity" varStatus="status">
                <div class="swiper-slide" style="width: 100px">
                    <div class="card">
                        <span class="name">${entity.membershipShopName}</span>
                        <span class="type">${entity.membershipName}</span>
                        <span class="discount" <c:if test="${entity.discountInt==10}">style="display:none;"</c:if> ><span><c:out value="${entity.discountInt}"/></span>.${entity.discountFloat}折</span>
                    </div>
                    <input type="hidden" isDeposited="${entity.isDeposited}" depositAmount="${entity.depositAmount}" name="mcid" value="${entity.id}" class="swiperInp"/>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="weui-cells weui-cells_form">
        <div class="weui-cell">
            <div class="weui-cell__hd"><label class="weui-label">手机号</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入手机号" id="mobile"/>
            </div>
        </div>
        <div class="weui-cell weui-cell_vcode">
            <div class="weui-cell__hd">
                <label class="weui-label">验证码</label>
            </div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="tel" placeholder="请输入短信验证码" id="code"/>
            </div>
            <div class="weui-cell__ft">
                <button class="weui-btn weui-btn_plain-primary btnCode" id="sendCode" style="width: 100px">获取验证码</button>
            </div>
        </div>
    </div>
    <div class="prompt" id="prompt">
        <c:forEach items="${cardList }" var="entity" varStatus="status" >
            <p>开通会员卡需要支付${entity.depositAmount}元</p>
        </c:forEach>
    </div>
    <div class="weui-btn weui-btn_primary open" id="submit">开通</div>
    <input type="hidden" id="openID" name="openID" value="${authInfo.openID}"/>
    <input type="hidden" id="userID" name="userID" value="${authInfo.userID}"/>
    <input type="hidden" id="source" name="source" value="${authInfo.source}"/>
    <input type="hidden" id="cid" name="cid" value="${appPolicyConsumer.id}">
</div>
</body>
<script>
    var pageData={
        consumeCellphone:'${appPolicyConsumer.consumerCellphone}'
    }
</script>
<script src="http://static.jinkaimen.cn/hsy/js/jquery-2.2.3.min.js"></script>
<script src="../../js/swiper-3.4.2.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>
<script src="http://static.jinkaimen.cn/hsy/js/2.0.2.1/createMember.min.js"></script>
</html>