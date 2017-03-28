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
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.4.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="withdrawal">
  <div class="info">
    <div class="small">可提现金额</div>
    <div class="amount"><span class="s">￥</span><span id="amount"></span></div>
    <div class="group">
      <span class="label">到账银行</span>
      <span class="important" id="bank"></span>
    </div>
    <div class="group">
      <span class="label">提现金额</span>
      ¥ <input type="text" id="ipt" placeholder="请输入提现金额">
    </div>
    <div class="group">
      <div class="mpt">手续费: <span id="fee"></span>元 到账金额: <span id="come">0.00</span>元</div>
    </div>
  </div>
  <div class="detail">提现申请成功后，将在2小时内到账，请注意查收</div>
  <button class="btn" id="next">立即提现</button>

  <div class="message-space" id="layer">
    <div class="message-box">
      <div class="message-box-head">
        短信已发送至 <span id="mobile"></span>
        <div class="message-x" id="layer-x"></div>
      </div>
      <div class="message-box-body">
        <div class="code-title">验证码</div>
        <input type="text" class="code-ipt" id="code" placeholder="输入短信验证码">
        <div class="code-line"></div>
        <div class="code-re" id="sendCode">重新获取</div>
      </div>
      <div class="message-box-foot">
        <div class="message-enter" id="submit">
          确认提现
        </div>
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
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.4.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.8/withdrawal.min.js"></script>
</html>
