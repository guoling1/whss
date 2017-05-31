<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2017/3/15
  Time: 11:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta name="format-detection" content="telephone=no"/>
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.19.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="_againUnionPay">
  <div class="space-title">确认订单信息</div>
  <div class="order-info">
    <div class="left">${merchantName}</div>
    <div class="right">¥${amount}</div>
  </div>
  <div class="space-title">请填写银行卡信息</div>
  <div class="space">
    <div class="group right" id="chooseBank">
      <div class="left">银行</div>
      <c:if test="${status==0}">
        <div class="adaptive text" id="bank">${bankName} 尾号${shortNo} (本通道不支持)</div>
      </c:if>
      <c:if test="${status==1}">
        <div class="adaptive text active" id="bank">${bankName} 尾号${shortNo}</div>
      </c:if>
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
      <div class="adaptive text active" id="mobile">${mobile}</div>
    </div>
    <div class="group">
      <div class="left">验证码</div>
      <input id="code" class="adaptive ipt" type="text" placeholder="请输入短信验证码">
      <div class="btn" id="sendCode">点击获取</div>
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
    creditCardId: '${creditCardId}',
    status: '${status}',
    canPay: false,
    showExpireDate: '${showExpireDate}',
    showCvv: '${showCvv}'
  }
</script>
<script src="https://res.wx.qq.com/open/libs/weuijs/1.0.0/weui.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.11.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.30/againUnionPay.min.js"></script>
</html>
