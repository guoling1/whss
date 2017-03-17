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
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.1.css">--%>
  <link rel="stylesheet" href="/css/hss/style.2.2.1.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="againUnionPay">
  <div class="space pd">
    <div class="small-list">
      <div class="left">支付金额</div>
      <div class="right or">¥${amount}</div>
    </div>
    <div class="small-list">
      <div class="left">收款商户</div>
      <div class="right">${merchantName}</div>
    </div>
  </div>
  <div class="space">
    <div class="list">
      <div class="left">付款银行</div>
      <div class="val" id="chooseBank">${bankName} 尾号${shortNo}</div>
    </div>
    <div class="list">
      <div class="left">CVV2</div>
      <input id="cvv2" class="ipt" type="text" placeholder="信用卡背面最后3个数字">
      <div class="mpt" id="check_cvv">查看示例</div>
    </div>
    <div class="list">
      <div class="left">开户手机号</div>
      <div class="val">${mobile}</div>
    </div>
    <div class="list">
      <div class="left">短信验证码</div>
      <input id="code" class="ipt" type="text" placeholder="输入短信验证码">
      <div class="mpt" id="sendCode">点击获取</div>
    </div>
  </div>
  <div class="btn-box">
    <div class="btn" id="submit">立即支付</div>
  </div>
  <div class="message-space" id="example_cvv">
    <div class="message-box">
      <div class="message-box-head">CVV2图例</div>
      <div class="message-box-body">
        <img class="img" src="../../assets/CVV.png" alt="">
      </div>
      <div class="message-box-foot">
        <div class="message-enter" id="cancel_cvv">
          知道了
        </div>
      </div>
    </div>
  </div>
</div>
<div class="choose-space" id="layer">
  <div class="choose-box">
    <div class="choose-box-head">
      请选择银行
      <div class="choose-x" id="layer-x"></div>
    </div>
    <div class="choose-box-body" id="layer-body" style="height: 200px">
      <%-- 动态加载 --%>
    </div>
    <div class="choose-box-foot" id="addNew">
      <span class="icon"></span> 添加信用卡
    </div>
  </div>
</div>

</body>
<script>
  var pageData = {
    creditCardId: '${creditCardId}'
  }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.1.min.js"></script>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.1/firstUnionPay.min.js"></script>--%>
<script src="/js/hss/2.2.1/againUnionPay.min.js"></script>
</html>
