<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>会员卡</title>
    <link rel="stylesheet" href="../../css/style.2.0.2.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="memberInfo">
    <div class="top">
        <div class="card">
            <span class="name">${appPolicyMember.membershipShopName}</span>
            <span class="type">${appPolicyMember.membershipName}</span>
            <span class="discount" <c:if test="${discountInt==10}">style="display:none;"</c:if> ><span>${discountInt}</span>.${discountFloat}折</span>
        </div>
        <div class="phone">手机号：${appPolicyMember.consumerCellphone}</div>
        <c:if test="${appPolicyMember.isDeposited==1}">
        <div class="title">余额</div>
        <div class="price">￥<span>${appPolicyMember.remainingSum}</span></div>
        </c:if>
    </div>
    <c:if test="${appPolicyMember.canRecharge==1}">
    <a class="weui-btn weui-btn_primary" style="margin: 14px 15px 0" href="<%=basePath%>sqb/toRecharge?mid=${appPolicyMember.id}&source=${source}">充值</a>
    </c:if>
    <div class="weui-cells">
        <%--<a class="weui-cell weui-cell_access" href="<%=basePath%>membership/consumeListByPage?mid=${appPolicyMember.id}&currentPage=1">--%>
        <a class="weui-cell weui-cell_access" href="<%=basePath%>membership/toConsumeList?mid=${appPolicyMember.id}">
            <div class="weui-cell__bd">
                <p>消费记录</p>
            </div>
            <div class="weui-cell__ft">${appPolicyMember.consumeTotalAmount}</div>
        </a>
        <%--<a class="weui-cell weui-cell_access" href="<%=basePath%>membership/rechargeListByPage?mid=${appPolicyMember.id}&currentPage=1">--%>
        <a class="weui-cell weui-cell_access" href="<%=basePath%>membership/toRechargeList?mid=${appPolicyMember.id}">
            <div class="weui-cell__bd">
                <p>充值记录</p>
            </div>
            <div class="weui-cell__ft">${appPolicyMember.rechargeTotalAmount}</div>
        </a>

    </div>

    <div class="weui-cells">
        <a class="weui-cell weui-cell_access" href="<%=basePath%>membership/toShopList?mcid=${appPolicyMember.mcid}">
            <div class="weui-cell__bd">
                <p>商家信息</p>
            </div>
            <div class="weui-cell__ft" >查看全部</div>
        </a>
    </div>

    <div class="bottom">
        <div>
            <c:choose>
                <c:when test="${appPolicyMember.openID!=null&&appPolicyMember.openID!=''}">
                    <span style="display: inline-block;position: relative;">
                        <img src="../assets/member/wx.png" alt="" class="icon">
                    </span>
                    <p class="type">已绑定</p>
                </c:when>
                <c:otherwise>
                    <span style="display: inline-block;position: relative;">
                        <img src="../assets/member/wxno.png" alt="" class="icon">
                    </span>
                    <p class="type">未绑定</p>
                </c:otherwise>
            </c:choose>
        </div>
        <div>
            <c:choose>
                <c:when test="${appPolicyMember.userID!=null&&appPolicyMember.userID!=''}">
                    <span style="display: inline-block;position: relative;">
                        <img src="../assets/member/zfb.png" alt="" class="icon">
                    </span>
                    <p class="type">已绑定</p>
                </c:when>
                <c:otherwise>
                    <span style="display: inline-block;position: relative;">
                        <img src="../assets/member/zfbno.png" alt="" class="icon">
                    </span>
                    <p class="type">未绑定</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
</body>
</html>
