<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2017/3/15
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html  lang="en">
<head>
    <title>Title</title>
</head>
<body>

<div>
    <div>
        收款成功
    </div>
    <div>
        <div>支付流水号</div>
        <div>${sn}</div>
    </div>
    <div>
        <div>付款银行</div>
        <div>${bankName}   尾号${shortNo}</div>
    </div>
    <div>
        <div>付款金额</div>
        <div>${amount}元</div>
    </div>
</div>
</body>
</html>
