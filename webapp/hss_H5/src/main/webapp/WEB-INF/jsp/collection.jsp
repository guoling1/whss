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
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.1.8.css">
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
  <div class="prompt-deep">支付宝收款限额8千，微信收款限额1万</div>
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

  <div class="message-space" id="layerBK">
    <div class="message-box">
      <div class="message-box-head">提示</div>
      <div class="message-box-body">
        使用该通道前，您需要补全结算卡的支行信息<br>
        以及提供一张您本人的信用卡号增加收款额度<br>
        信息报备有大约1个工作日的审核时间
      </div>
      <div class="message-box-foot">
        <div class="message-cancel" id="cancelBK">
          待会儿再说
        </div>
        <div class="message-line"></div>
        <div class="message-submit" id="toBK">
          去补全
        </div>
      </div>
    </div>
  </div>

  <div class="message-space" id="layerB">
    <div class="message-box">
      <div class="message-box-head">提示</div>
      <div class="message-box-body">
        使用该通道前，您需要补全结算卡的支行信息<br>
        以及提供一张您本人的信用卡号增加收款额度<br>
        信息报备有大约1个工作日的审核时间
      </div>
      <div class="message-box-foot">
        <div class="message-cancel" id="cancelB">
          待会儿再说
        </div>
        <div class="message-line"></div>
        <div class="message-submit" id="toB">
          去补全
        </div>
      </div>
    </div>
  </div>

  <div class="message-space" id="layerC">
    <div class="message-box">
      <div class="message-box-head">提示</div>
      <div class="message-box-body">
        使用该通道前，您需要<br>
        提供一张您本人的信用卡号增加收款额度<br>
        信息报备有大约1个工作日的审核时间
      </div>
      <div class="message-box-foot">
        <div class="message-cancel" id="cancelC">
          待会儿再说
        </div>
        <div class="message-line"></div>
        <div class="message-submit" id="toC">
          去补全
        </div>
      </div>
    </div>
  </div>

  <div class="message-space" id="layer">
    <div class="message-box">
      <div class="message-box-head">提示</div>
      <div class="message-box-body">
        商户信息报备中，大约1个工作日完成<br>请耐心等待
      </div>
      <div class="message-box-foot">
        <div class="message-enter" id="cancel">
          确定
        </div>
      </div>
    </div>
  </div>

  <div class="message-space" id="layerE">
    <div class="message-box">
      <div class="message-box-head">提示</div>
      <div class="message-box-body" id="textE"></div>
      <div class="message-box-foot">
        <div class="message-enter" id="cancelE">
          确定
        </div>
      </div>
    </div>
  </div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.6.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.1.6/collection.min.js"></script>
</html>