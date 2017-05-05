<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>账户详情</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.15.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="account">
  <div class="account">
    <div class="logo"></div>
    <div class="amount"><span class="s">¥</span><span id="amount"></span></div>
    <div class="useAmount">待结算金额: ¥<span id="settleAmount"></span></div>
    <div class="useAmount">可提现金额: ¥<span id="available"></span></div>
    <a class="btn" href="/account/toWithdraw">提现</a>
    <a class="check" href="/account/toHssAccountFlow"><span></span>查看余额明细</a>
  </div>
  <div class="qa">
    <div class="qa-title"><span></span>常见问题</div>
    <div class="qa-group">
      <div class="q"><span></span>我为什么没有余额？</div>
      <div class="a">收款以外的收入结算到账户余额，收款资金实时结算到您注册时绑定的结算银行卡。如果没有其他收入，就不会产生余额。</div>
    </div>
    <div class="qa-group">
      <div class="q"><span></span>余额如何提现？</div>
      <div class="a">点击“提现”，输入短信验证码即可提现到绑定银行卡。短信验证码会发送到您银行开户绑定的手机号，请注意查收。</div>
    </div>
    <div class="qa-group">
      <div class="q"><span></span>为什么存在余额与可提现金额不相同的情况？</div>
      <div class="a">当资金还未结算给您，以及提现资金还未到账之前。都会出现余额与可提现金额不相等的情况。</div>
    </div>
    <div class="qa-group">
      <div class="q"><span></span>提现多久到账？</div>
      <div class="a">一般情况10分钟内到账，各别小银行在下个工作日到账。如未到账，请联系客服400-622-6233查询。</div>
    </div>
  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.10.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.22/hasAccount.min.js"></script>
</html>
