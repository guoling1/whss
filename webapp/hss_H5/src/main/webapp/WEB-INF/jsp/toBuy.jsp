<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/5
  Time: 21:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>收款码</title>
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.5.css">--%>
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">--%>
  <script>
      function aysnLoadCss(url,cburl) {
          var link = document.createElement('link');
          link.href = url;
          link.rel = "stylesheet";
          document.head.appendChild(link);
          link.onerror = function () {
              var link = document.createElement('link');
              link.href = cburl;
              link.rel = "stylesheet";
              document.head.appendChild(link);
          }
          link.onload = function () {
              document.getElementById('chargeCode').style.opacity='1'
          }
      }
      function aysnLoadcb () {
          var script = document.createElement('script');
          script.src = "http://static.jinkaimen.cn/hss/2.2.33/charge.min.js";
          script.type = "text/javascript";
          document.head.appendChild(script);
          script.onerror = function () {
              var script = document.createElement('script');
              script.src = '/js/hss/2.2.33/charge.min.js';
              script.type = "text/javascript";
              document.head.appendChild(script);
          }
      }
      function aysnLoad(url,cburl) {
          var script = document.createElement('script');
          script.src = url;
          script.type = "text/javascript";
          document.head.appendChild(script);
          script.onload = function () {
              aysnLoadcb()
          }
          script.onerror = function () {
              var script = document.createElement('script');
              script.src = cburl;
              script.type = "text/javascript";
              document.head.appendChild(script);
              script.onload = function () {
                  aysnLoadcb()
              }
          }
      }
      window.onload = function () {
          var script = document.createElement('script');
          script.src = "http://static.jinkaimen.cn/qrcode/qrcode.min.js";
          script.type = "text/javascript";
          document.head.appendChild(script);
          script.onload = function () {
              aysnLoad('http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js','/js/hss/vendor.1.0.9.7.min.js');
          }
          script.onerror = function () {
              var script = document.createElement('script');
              script.src = "/js/hss/qrcode.min.js";
              script.type = "text/javascript";
              document.head.appendChild(script);
              script.onload = function () {
                  aysnLoad('http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js','/js/hss/vendor.1.0.9.7.min.js');
              }
          }
          aysnLoadCss('http://static.jinkaimen.cn/hss/css/style.2.2.21.css','/css/hss/style.2.0.5.css');
          aysnLoadCss('http://static.jinkaimen.cn/weui/weui.css','/css/hss/weui.css');
      };
  </script>
</head>
<body>

<div id="chargeCode" class="flexBox flex-box-column" style="opacity: 0">
  <div class="content">
    <div class="top">
      <p class="shop">升级费</p>

      <p class="price">￥<span>${amount}</span></p>
      <i class="left"></i>
      <i class="right"></i>
    </div>
    <div class="bottom">
      <div class="qr" id="qr"></div>

      <div class="red_mpt">请勿在微信内长按二维码识别</div>
      <div class="bar">
        <img src="http://static.jinkaimen.cn/hss/assets/wxb.png" alt=""/>
        <%--<img src="http://static.jinkaimen.cn/hss/assets/zfbb.png" alt=""/>--%>
        <span>截图保存后识别二维码付款</span>
      </div>
    </div>
  </div>
  <div class="explain"  style="display: none">
    <p class="refresh">二维码仅支持扫描一次，<span class="reload" id="refresh">
      <img src="http://static.jinkaimen.cn/hss/assets/reload.png"/>点击刷新</span></p>

    <p class="server">由好收收提供服务</p>
  </div>
</div>
</body>
<script type="text/javascript">
  var pageData = {
    payUrl: '${payUrl}',
    amount: '${amount}'
  }
</script>
<%--<script src="http://static.jinkaimen.cn/qrcode/qrcode.min.js"></script>--%>
<%--<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>--%>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.33/charge.min.js"></script>--%>
</html>
