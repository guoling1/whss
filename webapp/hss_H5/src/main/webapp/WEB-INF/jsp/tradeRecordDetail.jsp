<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/16
  Time: 10:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.1.8.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div class="flexBox flex-box-column" id="transactionDetail">
    <div class="space">
        <div class="amount">
            <div class="left">金额</div>
            <div class="right">￥ ${totalMoney}</div>
        </div>
    </div>
    <div class="space">
        <div class="detail">
            <div class="left">商品名称</div>
            <div class="right">${goodsName}</div>
        </div>
        <div class="detail">
            <div class="left">商品描述</div>
            <div class="right">${goodsDescribe}</div>
        </div>
        <div class="detail">
            <div class="left">时间</div>
            <div class="right">${createTime}</div>
        </div>
        <div class="detail">
            <div class="left">状态</div>
            <div class="right">${status}</div>
        </div>
        <div class="detail">
            <div class="left">付款方式</div>
            <div class="right">${payType}</div>
        </div>
        <div class="detail">
            <div class="left">商户名称</div>
            <div class="right">${merchantName}</div>
        </div>
        <div class="detail">
            <div class="left">商户订单号</div>
            <div class="right">${orderNo}</div>
        </div>
        <div class="detail">
            <div class="left">交易流水号</div>
            <div class="right">${sn}</div>
        </div>
        <div class="detail">
            <div class="left">结算状态</div>
            <div class="right">${settleStatus}</div>
        </div>
    </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.6.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.1.6/common.min.js"></script>
</html>