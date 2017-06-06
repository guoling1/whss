<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>升级降费率</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.4.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="_upgrade">
  <div class="info">
    <div class="head">
      <c:if test="${empty headimgUrl}"><img src="http://static.jinkaimen.cn/hss/assets/hss-default-head.png"
                                            alt=""></c:if>
      <c:if test="${not empty headimgUrl}"><img src="${headimgUrl}" alt=""></c:if>
    </div>
    <div class="status">${mobile} <span>${name}</span></div>
  </div>
  <div class="upgrade">
    <div class="small" onclick="javascript:location.href='/sqb/suansuan'">升级合伙人，不仅能降费率，推广好友还能拿分润</div>

    <div class="table">
      <div class="t-head">
        <div class="li t m">合伙人等级</div>
        <div class="li t"><span class="star2"></span>店员</div>
        <div class="li t"><span class="star3"></span>店长</div>
        <div class="li t"><span class="star4"></span>老板</div>
      </div>
      <c:forEach items="${upgradeArray}" var="item">
        <div class="t-body">
          <div class="li p b">
            <span class="name">${item.channelName}</span>
            <span class="name-small">原价 <fmt:formatNumber value="${item.commonRate}" pattern="0.00#"/>%</span>
          </div>
          <div class="li w"><fmt:formatNumber value="${item.clerkRate}" pattern="0.00#"/>%</div>
          <div class="li w"><fmt:formatNumber value="${item.shopownerRate}" pattern="0.00#"/>%</div>
          <div class="li w"><fmt:formatNumber value="${item.bossRate}" pattern="0.00#"/>%</div>
        </div>
      </c:forEach>
    </div>
    <div class="btn" id="isUpBtn">
      <div id="upBtn">立即升级</div>
    </div>
    <div class="ss" onclick="javascript:location.href='/sqb/suansuan'">算算能挣多少钱?</div>
  </div>
  <div class="up" id="up">
    <div class="banner" id="banner">
      <%-- 动态添加 --%>
    </div>
    <div class="point" id="point"></div>
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
  <div class="rocket" id="rocket"></div>
  <div class="notice flexBox flex-box-column miss" id="notice">
    <div class="cont">
      <h5>升级说明：</h5>

      <h6>Q：会员升级后，何时生效？</h6>

      <p>购买成功后立即生效，升级后的收款有效按新费率执行。</p>

      <h6>Q：升级后，如何拿分润？</h6>

      <p>升级成功后，您需要邀请好友注册成商户，您的好友每收一笔款项，您就可以获得相应分润。资金存入您的账户余额。</p>

      <h6>Q：如何查看我的分润？</h6>

      <p>您可以前往“邀请好友”页面查看分润情况。</p>

      <h6>Q：升级有什么好处？</h6>

      <p>升级后您自己使用“好收收”收款，将按照新的费率执行。您推广的好友以及好友再次推广的好友收款，您还能拿费率差额的分润，您等级越高，分润越多。</p>

      <h6>Q：通过您的推广二维码注册，或者在注册时填写您的邀请码，才能算作您推广的好友</h6>

      <h6>Q：指导好友提交资料通过审核后，收款达到2000元即可激活</h6>

      <h6>Q：每笔分润金额 = 好友每笔收款金额 × (您的费率 - 好友费率)。如果好友的费率低于或等于您的费率，则好友在收款时您不享受分佣。</h6>

      <h6>Q：好友2年内产生的收款参与分润</h6>
    </div>
    <div class="xx" id="xx"></div>
  </div>
</div>

</body>

<script src="http://static.jinkaimen.cn/qrcode/qrcode.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.25/upgrade.min.js"></script>
</html>