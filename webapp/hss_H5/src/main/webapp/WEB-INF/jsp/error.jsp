<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/6
  Time: 16:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>支付结果</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.17.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="error">
  <div class="space">
    <div class="info"></div>
    <div class="title">很遗憾，支付失败</div>
    <div class="text">${errorMsg}</div>
    <div class="btn-box">
      <a href="/sqb/collection" class="btn">重新收款</a>
    </div>
  </div>
  <div class="qa">
    <div class="qa-title">常见报错原因</div>
    <div class="qa-group">
      <div class="q"><span></span>“您的银行卡上余额不足”</div>
      <div class="a">您信用卡额度不足，或者您限制了快捷支付的额度。</div>
    </div>
    <div class="qa-group">
      <div class="q"><span></span>“您的卡暂不支持该业务”，“银行卡未开通认证支付”或“发卡行交易权限受限”</div>
      <div class="a">您关闭了该信用卡的快捷支付功能，或者未开通。具体可以询问您的发卡银行“为什么我不能使用快捷支付”</div>
    </div>
    <div class="qa-group">
      <div class="q"><span></span>“手机号或CVN输入不正确”</div>
      <div class="a">您必须输入开通此银行卡时预留在银行的手机号。CVN码是信用卡背面印刷的数字的最后3位，请参考示例图输入。</div>
    </div>
  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.28/common.min.js"></script>
</html>
