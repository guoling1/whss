<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>会员卡充值</title>
    <link rel="stylesheet" href="../../css/style.2.0.2.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="toRecharge">
    <div class="top">
        <div class="card">
            <span class="name">${appPolicyMember.membershipShopName}</span>
            <span class="type">${appPolicyMember.membershipName}</span>
            <span class="discount"><span>${discountInt}</span>.${discountFloat}折</span>
        </div>
        <div class="phone">手机号：${appPolicyMember.consumerCellphone}</div>
        <div class="title">余额</div>
        <div class="price">￥<span>${appPolicyMember.remainingSum}</span></div>
    </div>
    <div class="weui-cells weui-cells_form">
        <div class="weui-cell">
            <div class="weui-cell__hd"><label class="weui-label">充值金额</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="number" placeholder="请输入金额" id="price">
            </div>
        </div>
    </div>

    <c:if test="${appPolicyMember.isPresentedViaRecharge==1}">
        <p>会员卡充值每满<span>${appPolicyMember.rechargeLimitAmount}</span>元送<span>${appPolicyMember.rechargePresentAmount}</span>元</p>
    </c:if>

    <div id="recharge" class="weui-btn weui-btn_primary" style="margin: 30px 15px 0">充值</div>
    <a href="<%=basePath%>membership/toRechargeList?mid=${appPolicyMember.id}" class="toRecord">点击查看充值记录</a>
    <div class="js_dialog" id="iosDialog2" style="display: none">
        <div class="weui-mask"></div>
        <div class="weui-dialog">
            <div class="weui-dialog__bd">您已成功开通会员卡</div>
            <div class="weui-dialog__ft">
                <a href="<%=basePath%>membership/memberInfo?mid=${appPolicyMember.id}&source=${source}"  class="weui-dialog__btn weui-dialog__btn_primary">确定</a>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    var pageData = {
        type : '${type}',
        source : '${source}',
        mid　: '${appPolicyMember.id}'
    }
</script>
<script src="http://static.jinkaimen.cn/hsy/js/jquery-2.2.3.min.js"></script>
<script src="https://a.alipayobjects.com/g/h5-lib/alipayjsapi/0.2.4/alipayjsapi.inc.min.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.2.0.2.min.js"></script>
<script src="http://static.jinkaimen.cn/hsy/js/2.0.2.1/toRecharge.min.js"></script>
</html>
