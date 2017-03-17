<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2017/3/15
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta name="format-detection" content="telephone=no"/>
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.1.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

    银行卡号ID：${creditCardId}

    <div>支付金额</div> <div>¥ ${amount}</div>
    <div>收款商户</div> <div>${merchantName}</div>

    <div>付款银行</div> <div>${bankName}  尾号${shortNo}</div>
    <div>CVV2</div> <div></div>
    <div>开户手机号</div> <div>${mobile}</div>
    <div>短信验证码</div> <div></div>
</body>
</html>
