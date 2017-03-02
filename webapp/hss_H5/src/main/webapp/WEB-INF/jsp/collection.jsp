<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/5
  Time: 20:04
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
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.1.3.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="payment-wx">
  <div class="space">
    <div class="prompt"><span class="collection"></span>${merchantName}</div>
    <div class="key-ipt">
      <span class="key-title">收款金额</span>
      <span class="key-cursor animate"></span>
      <span class="key-span" id="key-span"></span>
      <span class="key-icon">￥</span>
      <input type="hidden" id="key-input" value="">
    </div>
  </div>
  <div class="prompt-deep">收款实时到账;无卡快捷单笔限额2万</div>
  <div class="keyboard" id="keyboard">
    <div class="copyright flexBox">
      <span class="line"></span>
      <span class="word">由好收收提供技术支持</span>
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
      <div class="qr">
        <div class="delete" keyCtrl="delete" touch="true"></div>
        <div class="quick" keyCtrl="quick">
          <div class="logo"></div>
          <div class="word">无卡快捷</div>
        </div>
        <div class="wx-pay" keyCtrl="wx-ss">
          <div class="wx-logo"></div>
          <div class="word">微信</div>
        </div>
        <div class="ali-pay" keyCtrl="ali-ss">
          <div class="ali-logo"></div>
          <div class="word">支付宝</div>
        </div>
      </div>
    </div>
  </div>

  <div class="layer" id="layerBK">
    <div class="space">
      <div class="space-title">
        提示
        <div class="xx" id="xxBK"></div>
      </div>
      <div class="space-cont">
        <div class="cont-detail">使用该通道前<br>您需要补全结算卡的支行信息</div>
        <div class="operation">
          <div class="cancel" id="cancelBK">待会儿再说</div>
          <div class="submit" id="toBK">去补全</div>
        </div>
      </div>
    </div>
  </div>

  <div class="layer" id="layerB">
    <div class="space">
      <div class="space-title">
        提示
        <div class="xx" id="xxB"></div>
      </div>
      <div class="space-cont">
        <div class="cont-detail">使用该通道前<br>您需要补全结算卡的支行信息</div>
        <div class="operation">
          <div class="cancel" id="cancelB">待会儿再说</div>
          <div class="submit" id="toB">去补全</div>
        </div>
      </div>
    </div>
  </div>

  <div class="layer" id="layerC">
    <div class="space">
      <div class="space-title">
        提示
        <div class="xx" id="xxC"></div>
      </div>
      <div class="space-cont">
        <div class="cont-detail">补全信用卡信息可以提高您的支付额度</div>
        <div class="operation">
          <div class="cancel" id="cancelC">待会儿再说</div>
          <div class="submit" id="toC">去补全</div>
        </div>
      </div>
    </div>
  </div>

  <div class="layer" id="layer">
    <div class="space">
      <div class="space-title">
        提示
        <div class="xx" id="xx"></div>
      </div>
      <div class="space-cont">
        <div class="cont-detail">该通道正在支付公司注册中<br>请明天再使用该通道</div>
        <div class="operation">
          <div class="layer-submit" id="cancel">确定</div>
        </div>
      </div>
    </div>
  </div>

  <div class="layer" id="layerE">
    <div class="space">
      <div class="space-title">
        提示
        <div class="xx" id="xxE"></div>
      </div>
      <div class="space-cont">
        <div class="cont-detail" id="textE"></div>
        <div class="operation">
          <div class="layer-submit" id="cancelE">确定</div>
        </div>
      </div>
    </div>
  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.3.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.1.1/collection.min.js"></script>
</html>