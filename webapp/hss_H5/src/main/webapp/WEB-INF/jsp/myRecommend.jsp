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
  <title>我的推广</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
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
          <div class="list">
            <div class="name">张伟<span class="z">直接</span></div>
            <div class="date">2016/10/26</div>
            <div class="amount">2.00元</div>
          </div>
          <div class="list">
            <div class="name">张伟<span class="j">间接</span></div>
            <div class="date">2016/10/26</div>
            <div class="amount">2.00元</div>
          </div>
        </li>
        <li id="friends">
          <div class="total">我推广的好友(直接:<span id="directCount"></span>人 间接:<span id="indirectCount"></span>人)</div>
          <%-- 以下列表内容动态添加 --%>
        </li>
      </ul>
    </div>
  </div>
  <div class="rules">
    <span></span>
    规则
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
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
<%--<script src="http://static.jinkaimen.cn/hss/2.0.1/myRecommend.min.js"></script>--%>
<script src="/js/hss/2.0.1/myRecommend.min.js"></script>
</html>