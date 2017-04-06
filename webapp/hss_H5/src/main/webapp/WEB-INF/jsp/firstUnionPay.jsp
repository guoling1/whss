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
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.6.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="firstUnionPay">
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
      <div class="left">开户名</div>
      <div class="val">${bankAccountName}</div>
      <div class="small">只能使用您名下银行卡</div>
    </div>
    <div class="list">
      <div class="left">身份证号</div>
      <div class="val">${idCard}</div>
    </div>
  </div>
  <div class="space">
    <div class="list">
      <div class="left">所属银行</div>
      <input id="chooseBank" class="ipt" type="text" placeholder="点击选择" readonly>
    </div>
    <div class="list">
      <div class="left">银行卡号</div>
      <input id="bankCode" class="ipt" type="number" placeholder="请输入信用卡号" readonly>
    </div>
  </div>
  <div class="space">
    <div class="list">
      <div class="left">有效期</div>
      <input id="expireDate" class="ipt" type="text" placeholder="请选择信用卡有效期" readonly>
      <div class="mpt" id="check_validity">查看示例</div>
    </div>
    <div class="list">
      <div class="left">CVV2</div>
      <input id="cvv2" class="ipt" type="text" placeholder="信用卡背面最后3个数字">
      <div class="mpt" id="check_cvv">查看示例</div>
    </div>
    <div class="list">
      <div class="left">开户手机号</div>
      <input id="mobile" class="ipt" type="text" placeholder="银行预留手机号">
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
<script src="https://res.wx.qq.com/open/libs/weuijs/1.0.0/weui.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.5.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.8/firstUnionPay.min.js"></script>
</html>
