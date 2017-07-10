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
            <span class="name">${appPolicyMember.membershipShopName}</span>
            <span class="type">${appPolicyMember.membershipName}</span>
            <span class="discount"><span>${discountInt}</span>.${discountFloat}折</span>
        </div>
        <div class="phone">手机号：${appPolicyMember.consumerCellphone}</div>
        <c:if test="${appPolicyMember.isDeposited==1}">
        <div class="title">余额</div>
        <div class="price">￥<span>${appPolicyMember.remainingSum}</span></div>
        </c:if>
    </div>
    <a class="weui-btn weui-btn_primary" style="margin: 14px 15px 0" href="<%=basePath%>membership/toRecharge?mid=${appPolicyMember.id}&source=${source}">充值</a>
    <div class="weui-cells">
        <a class="weui-cell weui-cell_access" href="javascript:;">
            <div class="weui-cell__bd">
                <p>消费记录</p>
            </div>
            <div class="weui-cell__ft">${appPolicyMember.consumeTotalAmount}</div>
        </a>
        <a class="weui-cell weui-cell_access" href="javascript:;">
            <div class="weui-cell__bd">
                <p>充值记录</p>
            </div>
            <div class="weui-cell__ft">${appPolicyMember.rechargeTotalAmount}</div>
        </a>

    </div>

    <div class="weui-cells">
        <a class="weui-cell weui-cell_access" href="javascript:;">
            <div class="weui-cell__bd">
                <p>商家信息</p>
            </div>
            <a class="weui-cell__ft" href="<%=basePath%>membership/toShopList?mcid=${appPolicyMember.mcid}">查看全部</a>
        </a>
    </div>

    <div class="bottom">
        <div>
        <span style="display: inline-block;position: relative;">
          <img src="../assets/member/wx.png" alt="" class="icon">
        </span>
            <c:choose>
                <c:when test="${appPolicyMember.openID!=null&&appPolicyMember.openID!=''}">
                    <p class="type">已绑定</p>
                </c:when>
                <c:otherwise>
                    <p class="type">未绑定</p>
                </c:otherwise>
            </c:choose>
        </div>
        <div>
        <span style="display: inline-block;position: relative;">
          <img src="../assets/member/zfb.png" alt="" class="icon">
        </span>
            <c:choose>
                <c:when test="${appPolicyMember.userID!=null&&appPolicyMember.userID!=''}">
                    <p class="type">已绑定</p>
                </c:when>
                <c:otherwise>
                    <p class="type">未绑定</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
   <%-- <h1 style="font-size: 40px;">
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
    </h1>--%>
</body>
</html>
