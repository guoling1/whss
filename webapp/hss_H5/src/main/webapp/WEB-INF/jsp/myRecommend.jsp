<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/9
  Time: 19:42
  To change this template use File | Settings | File Templates.
--%>
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
  <title>我的推广</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.1.7.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="promote" class="flexBox flex-box-column">
  <div class="top">
    <div class="promote-title">累计分润(元)</div>
    <div class="amount">${totalProfit}</div>
    <div class="qr-code" id="shareShow"><span></span>我的推广二维码</div>
  </div>
  <div class="bottom flexBox flex-box-column">
    <div class="teb">
      <div class="space shadow-right" id="profitsBtn">我的分润</div>
      <div class="space shadow-left disabled" id="friendsBtn">我推广的好友</div>
    </div>
    <div class="cont">
      <ul class="window" id="windows">
        <li id="profits">
          <div class="total">只有您的费率比邀请的好友低的时候才有分润哦</div>
          <%-- 以下列表内容动态添加 --%>
        </li>
        <li id="friends">
          <div class="total">我推广的好友(直接:<span id="directCount"></span>人 间接:<span id="indirectCount"></span>人)</div>
          <%-- 以下列表内容动态添加 --%>
        </li>
      </ul>
    </div>
  </div>
  <div class="rules" id="rules">
    <span></span>
    规则
  </div>
  <div class="notice flexBox flex-box-column miss" id="notice">
    <div class="cont">
      <h5>推广规则：</h5>

      <h6>1. 通过您的推广二维码注册，或者在注册时填写您的邀请码，才能算作您推广的好友</h6>

      <h6>2. 指导好友提交资料通过审核后，收款达到2000元即可激活</h6>

      <h6>3. 每笔分润金额 = 好友每笔收款金额 × (您的费率 - 好友费率)。如果好友的费率低于或等于您的费率，则好友在收款时您不享受分佣。</h6>

      <h6>4. 好友2年内产生的收款参与分润</h6>
    </div>
    <div class="xx" id="xx"></div>
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
  //分享链接
  var pageData = {
    shareUrl: '${shareUrl}'
  }
</script>
<script src="http://static.jinkaimen.cn/qrcode/qrcode.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.3.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.1.4/myRecommend.min.js"></script>
</html>