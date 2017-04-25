<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/include.inc.jsp" %>
<meta charset="utf-8">
<meta name="renderer" content="webkit">
<meta name="viewport"
      content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=1.0">
<meta name="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no,email=no,adress=no">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.1.min.js"></script>
<style type="text/css">
  * {
    margin: 0px;
    padding: 0px;
  }

  body {
    background-color: #F9F9F9;
  }

  /* 输入框圆角阴影样式*/
  .czy_box .input:focus {
    box-shadow: inset 0 0 10px #e5e5e5;
  }

  .czy_box .uname {
    border-radius: 5px 5px 0 0;
  }

  .czy_box .ss {
    height: 26px;
    padding: 2px 10px;
    outline: none;
    color: #999;
    border: 1px solid #eaeaea;
  }

  .czy_box .pw {
    margin-top: -1px;
    border-radius: 0 0 5px 5px;
  }

  /* 输入框样式*/
  .czy_box .c_text {
    margin: 10px auto;
    width: 320px;
    height: 20px;
  }

  .czy_box .c_text .input {
    height: 20px;
    padding: 12px 10px;
    outline: none;
    color: #999;
    border: 1px solid #eaeaea;
  }

  /* 登录按钮样式cursor:pointer;*/
  .czy_box .c_btn {
    margin-top: 15px;
  }

  .czy_box .c_btn .btn {
    width: 320px;
    height: 44px;
    background: #FF7A4D;
    color: #fff;
    font-size: 16px;
    text-align: center:
    line-height: 44px;
    border-radius: 5px;
    border: 0px;
  }

  .czy_box .c_text .c_btn .btn:hover {
    background: #111;
    transition: all 0.3s ease-in-out;
    cursor: pointer;
  }

</style>

<div class="czy_box">

  <div class="c_text">
    <table>
      <tr>
        <td>API地址</td>
        <td><input type="text" name="apiUrl" Id="apiUrl" class="input uname"
                   value="https://test.grapefs.com/gpay/H5Pay" size="30"/></td>
      </tr>
      <tr>
        <td>交易渠道</td>
        <td>
          <select name="payMethod" id="payMethod" class="ss">
            <!-- <option value="">请选择</option> -->
            <option value="ALIPAY">ALIPAY</option>
            <option value="WXPAY">WXPAY</option>
          </select></td>
      </tr>
      <tr>
        <td>平台商户号</td>
        <td><input type="text" name="merchantNo" class="input uname" id="merchantNo" value="201703161000015" size="30"/>
        </td>
      </tr>
      <tr>
        <td>商户密钥</td>
        <td><input type="text" name="md5Key" class="input uname" id="md5Key" value="8ccf0edf000941d8839811a6025b6666"
                   size="30"/></td>
      </tr>
      <tr>
        <td>交易金额</td>
        <td><input type="text" name="amount" id="amount" class="input uname" size="30" value="0.01"
                   class="required number" min='0'/></td>
      </tr>

      <tr>
        <td>主题</td>
        <td><input type="text" name="subject" id="subject" class="input uname" value="测试商户"
                   size="30"/></td>
      </tr>

      <tr>
        <td>交易订单号</td>
        <td><input type="text" name="orderNo" id="orderNo" class="input uname" size="30"/></td>
      </tr>
      <tr>
        <td>通知地址</td>
        <td><input type="text" name="notifyUrl" id="notifyUrl" class="input uname"
                   value="https://test.grapefs.com/gpay/xmhx/payNotify" size="30"/></td>
      </tr>
      <tr>
        <td>openId</td>
        <td><input type="text" name="openId" id="openId"
                   value="oaTp-wLkvBgFw0jMQkmAL6nlqpCY" size="30" class="input uname"/></td>
      </tr>
      <tr>
        <td>buyerId</td>
        <td><input type="text" name="buyerId" id="buyerId"
                   value="2088802705557085" size="30" class="input uname"/></td>
      </tr>
      <tr>
        <td>信用卡付款：</td>
        <td><select name="payByCredit" id="payByCredit" class=ss>
          <!-- <option value="">请选择</option> -->
          <option value="TRUE">可以</option>
          <option value="FALSE">不能</option>
        </select></td>
      </tr>
      <tr>
        <td>订单失效时间</td>
        <td><input type="text" name="expireTime" id="expireTime"
                   value="6" size="30" class="input uname"/></td>
      </tr>
    </table>
    <p class="c_btn"><input onclick="doSubmit()" class="btn" type="button" value="支付"/></p>
  </div>
</div>
<script src="https://a.alipayobjects.com/g/h5-lib/alipayjsapi/0.2.4/alipayjsapi.inc.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
  function doSubmit() {
    AlipayJSBridge.call("tradePay", {tradeNO: 2017042521001004500231812603},
      function (result) {
        alert(JSON.stringify(result));
      });
  }
</script>


<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html lang="en">--%>
<%--<head>--%>
<%--<meta charset="UTF-8">--%>
<%--<meta name="viewport"--%>
<%--content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">--%>
<%--<meta name="format-detection" content="telephone=no"/>--%>
<%--<title>钱包加加</title>--%>
<%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">--%>
<%--<link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">--%>
<%--</head>--%>
<%--<body>--%>

<%--<div id="payment-zfb">--%>
<%--<div class="space">--%>
<%--<div class="prompt">--%>
<%--<span class="pay"></span>付款给${merchantName}--%>
<%--<div class="word">和商家确定好金额后输入</div>--%>
<%--</div>--%>
<%--<div class="key-ipt">--%>
<%--<span class="key-title">消费金额</span>--%>
<%--<span class="key-cursor animate"></span>--%>
<%--<span class="key-span" id="key-span"></span>--%>
<%--<span class="key-icon">￥</span>--%>
<%--<input type="hidden" id="key-input" value="">--%>
<%--</div>--%>
<%--</div>--%>
<%--<div class="safe">--%>
<%--<img src="http://static.jinkaimen.cn/hss/assets/zfb-gray.png" alt="">--%>
<%--支付宝安全支付--%>
<%--</div>--%>
<%--<div class="keyboard" id="keyboard">--%>
<%--<div class="copyright flexBox">--%>
<%--<span class="line"></span>--%>
<%--<span class="word">由钱包加加提供技术支持</span>--%>
<%--<span class="line"></span>--%>
<%--</div>--%>
<%--<div class="keys">--%>
<%--<div class="left">--%>
<%--<div class="key" keyNum="1" touch="true">1</div>--%>
<%--<div class="key" keyNum="2" touch="true">2</div>--%>
<%--<div class="key" keyNum="3" touch="true">3</div>--%>
<%--<div class="key" keyNum="4" touch="true">4</div>--%>
<%--<div class="key" keyNum="5" touch="true">5</div>--%>
<%--<div class="key" keyNum="6" touch="true">6</div>--%>
<%--<div class="key" keyNum="7" touch="true">7</div>--%>
<%--<div class="key" keyNum="8" touch="true">8</div>--%>
<%--<div class="key" keyNum="9" touch="true">9</div>--%>
<%--<div class="key gray"></div>--%>
<%--<div class="key" keyNum="0" touch="true">0</div>--%>
<%--<div class="key gray" keyNum=".">.</div>--%>
<%--</div>--%>
<%--<div class="zfb">--%>
<%--<div class="delete" keyCtrl="delete" touch="true"></div>--%>
<%--<div class="pay" keyCtrl="ali-pay">--%>
<%--付款--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>

<%--</body>--%>
<%--<script>--%>
<%--var pageData = {--%>
<%--memberId: '${openId}',--%>
<%--merchantId: '${mid}'--%>
<%--};--%>
<%--</script>--%>
<%--<script src="https://a.alipayobjects.com/g/h5-lib/alipayjsapi/0.2.4/alipayjsapi.inc.min.js"></script>--%>
<%--<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>--%>
<%--<script src="/js/2.0.1/payment.2.0.1.min.js"></script>--%>
<%--</html>--%>