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
    <title>Title</title>
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
