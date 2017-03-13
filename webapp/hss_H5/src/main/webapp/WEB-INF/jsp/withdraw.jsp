<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/7
  Time: 20:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.1.6.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="withdrawal">
  <div class="info">
    <div class="small">可提现金额</div>
    <div class="amount"><span>￥</span>${avaMoney}</div>
    <div class="group">
      <span class="label">到账银行</span>
      <span class="important">${bankName} (${bankNo})</span>
    </div>
    <div class="group">
      <span class="label">到账金额</span>
      <span class="important">${realMoney}元</span>
      <span class="small">(手续费:${channelFee}元/笔)</span>
    </div>
  </div>
  <div class="detail">实时到账</div>
  <button class="btn" id="next">立即提现</button>

  <div class="layer" id="layer">
    <div class="space">
      <div class="space-title">
        短信已发送至 ${phone_01}****${phone_02}
        <div class="xx" id="xx"></div>
      </div>
      <div class="space-cont">
        <div class="input flexBox">
          <div>验证码</div>
          <input type="text" id="code" placeholder="输入短信验证码">
          <div class="re" id="sendCode">重新获取</div>
        </div>
        <div class="submit" id="submit">确认提现</div>
      </div>
    </div>
  </div>
</div>

</body>
<script>
  var pageData = {
    bookValue: '${realMoney}'
  }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.3.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.1.4/withdrawal.min.js"></script>
</html>
