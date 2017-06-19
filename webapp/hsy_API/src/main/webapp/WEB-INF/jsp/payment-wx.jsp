<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta name="format-detection" content="telephone=no"/>
  <title>付款给商户</title>
  <link rel="stylesheet" href="/css/style.1.0.0.css">
  <link rel="stylesheet" href="/css/weui/weui.css">
</head>
<body>

<div id="payment-wx">
  <div class="space">
    <div class="prompt">
      <span class="pay"></span>付款给${merchantName}
      <div class="word">和商家确定好金额后输入</div>
    </div>
    <div class="key-ipt">
      <span class="key-title">消费金额</span>
      <span class="key-cursor animate"></span>
      <span class="key-span" id="key-span"></span>
      <span class="key-icon">￥</span>
      <input type="hidden" id="key-input" value="">
    </div>
  </div>
  <div class="safe">
    <img src="/assets/wx-gray.png" alt="">
    微信安全支付
  </div>
  <div class="keyboard" id="keyboard">
    <div class="copyright flexBox">
      <span class="line"></span>
      <span class="word">由钱包加加提供技术支持</span>
      <span class="line"></span>
    </div>
    <div class="keys">
      <div class="left">
        <div class="key" keyNum="1" touch="true">1</div>
        <div class="key" keyNum="2" touch="true">2</div>
        <div class="key" keyNum="3" touch="true">3</div>
        <div class="key" keyNum="4" touch="true">4</div>
        <div class="key" keyNum="5" touch="true">5</div>
        <div class="key" keyNum="6" touch="true">6</div>
        <div class="key" keyNum="7" touch="true">7</div>
        <div class="key" keyNum="8" touch="true">8</div>
        <div class="key" keyNum="9" touch="true">9</div>
        <div class="key gray"></div>
        <div class="key" keyNum="0" touch="true">0</div>
        <div class="key gray" keyNum=".">.</div>
      </div>
      <div class="wx">
        <div class="delete" keyCtrl="delete" touch="true"></div>
        <div class="pay" keyCtrl="wx-pay">
          付款
        </div>
      </div>
    </div>
  </div>
</div>

</body>
<script>
  var pageData = {
    hsyOrderId: '${hsyOrderId}'
  }
</script>
<script src="https://a.alipayobjects.com/g/h5-lib/alipayjsapi/0.2.4/alipayjsapi.inc.min.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/js/vendor.2.0.1.3.min.js"></script>
<script src="/js/2.0.1.1/payment.min.js"></script>
</html>