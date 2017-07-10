<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>会员卡</title>
    <link rel="stylesheet" href="../../css/style.1.0.0.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="memberInfo">
    <div class="top">
        <div class="card">
            <span class="name">江湖味道总店</span>
            <span class="type">尊贵金卡</span>
            <span class="discount"><span>8</span>.0折</span>
        </div>
        <div class="phone">手机号：13833212332</div>
        <div class="title">余额：</div>
        <div class="price">￥<span>566.00</span></div>
    </div>
    <div class="weui-btn weui-btn_primary" style="margin: 14px 15px 0">充值</div>
    <div class="weui-cells">
        <a class="weui-cell weui-cell_access" href="javascript:;">
            <div class="weui-cell__bd">
                <p>消费记录</p>
            </div>
            <div class="weui-cell__ft">1笔</div>
        </a>
        <a class="weui-cell weui-cell_access" href="javascript:;">
            <div class="weui-cell__bd">
                <p>充值记录</p>
            </div>
            <div class="weui-cell__ft">￥10781.12</div>
        </a>

    </div>

    <div class="weui-cells">
        <a class="weui-cell weui-cell_access" href="javascript:;">
            <div class="weui-cell__bd">
                <p>商家信息</p>
            </div>
            <div class="weui-cell__ft">查看全部</div>
        </a>

    </div>

    <div class="bottom">
        <div>
        <span style="display: inline-block;position: relative;">
          <img src="../assets/member/wx.png" alt="" class="icon">
        </span>
            <p class="type">已绑定</p>
        </div>
        <div>
        <span style="display: inline-block;position: relative;">
          <img src="../assets/member/zfb.png" alt="" class="icon">
        </span>
            <p class="type">未绑定</p>
        </div>
    </div>
</div>
    <h1 style="font-size: 40px;">
        会员卡页面<br>
        会员卡名称:${appPolicyMember.membershipName}<br>
        会员卡上显示的店铺名称:${appPolicyMember.membershipShopName}<br>
        手机号:${appPolicyMember.consumerCellphone}<br>
        会员卡号:${appPolicyMember.memberCardNO}<br>
        会员卡折扣(折):${appPolicyMember.discount}<br>
        <c:if test="${appPolicyMember.isDeposited==1}">
            余额：${appPolicyMember.remainingSum}<br>
            <a href="<%=basePath%>membership/consumeListByPage?mid=${appPolicyMember.id}&currentPage=1">累计消费：${appPolicyMember.consumeTotalAmount}</a><br>
            <a href="<%=basePath%>membership/rechargeListByPage?mid=${appPolicyMember.id}&currentPage=1">累计充值：${appPolicyMember.rechargeTotalAmount}</a><br>
            <a href="<%=basePath%>membership/toRecharge?mid=${appPolicyMember.id}&source=${source}">充值</a><br>
        </c:if>

        <c:forEach items="${appBizShopList}" var="shop">
            <c:out value="${shop.shortName}"></c:out><br>
        </c:forEach>

        <c:choose>
            <c:when test="${appPolicyMember.userID!=null&&appPolicyMember.userID!=''}">
                支付宝已绑定
            </c:when>
            <c:otherwise>
                支付宝未绑定
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${appPolicyMember.openID!=null&&appPolicyMember.openID!=''}">
                微信已绑定
            </c:when>
            <c:otherwise>
                微信未绑定
            </c:otherwise>
        </c:choose>
    </h1>
</body>
</html>
