<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2017/3/15
  Time: 11:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta name="format-detection" content="telephone=no"/>
  <title>填写银行卡号</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.19.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="_firstUnionPay">
  <div class="space-title">确认订单信息</div>
  <div class="order-info">
    <div class="left">${merchantName}</div>
    <div class="right">¥${amount}</div>
  </div>
  <div class="space-title">请填写银行卡信息</div>
  <div class="space">
    <div class="group">
      <div class="left">持卡人</div>
      <div class="adaptive text active">${bankAccountName}</div>
      <div class="i" id="know_channel"></div>
    </div>
    <div class="group right" id="showChooseBank">
      <div class="left">银行</div>
      <div id="chooseBank" class="adaptive text">点击选择发卡银行</div>
    </div>
    <div class="group">
      <div class="magnifying" id="magnifying"></div>
      <div class="left">卡号</div>
      <input id="bankCode" class="adaptive ipt" type="number" placeholder="请输入本人银行卡号" readonly>
      <div class="del" id="del"></div>
    </div>
    <div class="group" id="showExpireDate" style="display: none;">
      <div class="left">有效期</div>
      <div id="expireDate" class="adaptive text">请选择月份/年份</div>
      <div class="i" id="check_validity"></div>
    </div>
    <div class="group" id="showCvv" style="display: none;">
      <div class="left">CVV2</div>
      <input id="cvv2" class="adaptive ipt" type="text" placeholder="卡背面末三位">
      <div class="i" id="check_cvv"></div>
    </div>
    <div class="group">
      <div class="left">手机号</div>
      <input id="mobile" class="adaptive ipt" type="text" placeholder="请输入银行预留手机号">
    </div>
    <div class="group">
      <div class="left">验证码</div>
      <input id="code" class="adaptive ipt" type="text" placeholder="请输入短信验证码">
      <div class="btn" id="sendCode">点击获取</div>
    </div>
  </div>

  <div class="security-info"><span class="shield"></span>银行卡号由合作银行及持牌支付机构存储，并采用国际认可的Https金融级数据传输加密方式</div>

  <div class="btn-box">
    <div class="btn" id="submit">立即支付</div>
  </div>


  <div class="message-space" id="layer-channel">
    <div class="message-box">
      <div class="message-box-head">
        说明
        <div class="message-x" id="layer-x-channel"></div>
      </div>
      <div class="message-box-body">
        <div>为了资金安全<br>只能绑定当前持卡人的银行卡</div>
      </div>
      <div class="message-box-foot">
        <div class="message-enter" id="submit-channel">
          确认
        </div>
      </div>
    </div>
  </div>
  <div class="message-space" id="example_validity">
    <div class="message-box">
      <div class="message-box-head">有效期图例</div>
      <div class="message-box-body">
        <img class="img" src="../../assets/validity.png" alt="">
      </div>
      <div class="message-box-foot">
        <div class="message-enter" id="cancel_validity">
          知道了
        </div>
      </div>
    </div>
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
</body>
<script>
  var pageData = {
    showExpireDate: '${showExpireDate}',
    showCvv: '${showCvv}'
  }
</script>
<script src="https://res.wx.qq.com/open/libs/weuijs/1.0.0/weui.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.30/firstUnionPay.min.js"></script>
</html>
