<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>提现成功</title>
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.21.css">--%>
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
      }
      function aysnLoadcb () {
          var script = document.createElement('script');
          script.src = "http://static.jinkaimen.cn/hss/2.2.30/withdrawSuccess.min.js";
          script.type = "text/javascript";
          document.head.appendChild(script);
          script.onerror = function () {
              var script = document.createElement('script');
              script.src = '/js/hss/2.2.30/withdrawSuccess.min.js';
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
          aysnLoad('http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js','/js/hss/vendor.1.0.9.7.min.js');
          aysnLoadCss('http://static.jinkaimen.cn/hss/css/style.2.2.21.css','/css/hss/style.2.2.21.css');
          aysnLoadCss('http://static.jinkaimen.cn/weui/weui.css','/css/hss/weui.css');
      };
  </script>
</head>
<body>
<div id="withdrawSuccess">
  <div class="cont">
    <div class="detail">
      <div class="logo"></div>
      <div class="text">提现申请成功</div>
      <div class="small">预计在2小时内到账</div>
    </div>
    <div class="list-box">
      <div class="list-group">
        <div class="left">到账银行卡</div>
        <div class="right" id="bank"></div>
      </div>
      <div class="list-group">
        <div class="left">提现金额</div>
        <div class="right" id="amount"></div>
      </div>
      <div class="list-group">
        <div class="left">手续费</div>
        <div class="right" id="fee"></div>
      </div>
      <div class="list-group">
        <div class="left">到账金额</div>
        <div class="right" id="come"></div>
      </div>
    </div>
  </div>
  <div class="btn-box">
    <div class="box" id="complete">完成</div>
  </div>
</div>

</body>
<%--<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js"></script>--%>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.30/withdrawSuccess.min.js"></script>--%>
</html>
