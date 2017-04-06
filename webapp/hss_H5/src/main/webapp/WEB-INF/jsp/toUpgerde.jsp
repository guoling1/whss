<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>我的推广</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.3.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="upgradeWay">
  <div class="space flexBox flex-box-column">
    <div class="cont">
      <div class="info">
          <c:if test="${upgradeRules.type==1}">
              <div class="assistant"></div>
          </c:if>
          <c:if test="${upgradeRules.type==2}">
              <div class="manager"></div>
          </c:if>
          <c:if test="${upgradeRules.type==3}">
              <div class="boss"></div>
          </c:if>
        <div class="name">${upgradeRules.name}</div>
      </div>
      <div class="payType">
        <div class="li">
          <span class="wx"></span>
          费率${upgradeRules.weixinRate}%
        </div>
        <div class="li">
          <span class="ali"></span>
          费率${upgradeRules.alipayRate}%
        </div>
        <div class="li">
          <span class="quick"></span>
          费率${upgradeRules.fastRate}%
        </div>
      </div>
      <ul class="upType">
        <li id="payToUp">
          <div class="left"><span id="payToCheck" class="check active"></span>方式一：付费升级</div>
          <div class="right">￥${needMoney}</div>
          <s class="small">￥${upgradeRules.upgradeCost}</s>
        </li>
        <li id="pushToUp">
          <div class="left"><span id="pushToCheck" class="check"></span>方式二：推广${upgradeRules.promotionNum}个好友</div>
          <div class="right">还差${restCount}个</div>
          <div class="small">达到推广好友数量，系统将自动为您升级</div>
        </li>
      </ul>
    </div>
    <div class="pay" id="toUp">微信立即支付 ￥${needMoney}</div>
  </div>
  <div class="layer" id="layer">
    <div class="space flexBox flex-box-column">
      <div class="content flexBox flex-box-column">
        <div class="logo"></div>
        <div class="qr-code">
          <div id="qr"></div>
        </div>
        <div class="word">微信识别二维码，即刻开通收款</div>
      </div>
      <div class="divider"></div>
      <div class="describe">
        <p>使用方法</p>

        <p>1. 好友扫描二维码完成注册，即可成为您推广的好友；</p>

        <p>2. 推广二维码永久有效，您可以截图保存图片发给好友。</p>
      </div>
    </div>
  </div>
</div>

</body>
<script>
  var pageData = {
    level: ${upgradeRules.id},
    shareUrl: '${shareUrl}'
  }
</script>
<script src="http://static.jinkaimen.cn/qrcode/qrcode.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.7.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.10/toUpgerde.min.js"></script>
</html>
