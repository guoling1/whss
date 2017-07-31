<%--
  Created by IntelliJ IDEA.
  User: xiang.yu
  Date: 2016/12/5
  Time: 14:28
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
  <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no">
  <title>${oemName}</title>
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.21.css">--%>
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">--%>
  <link rel="stylesheet" href="">
  <script>
      function aysnLoadCss(url,cburl) {
          var link = document.createElement('link');
          link.href = url;
          link.rel = "stylesheet";
          document.head.appendChild(link);
          link.onerror = function () {
              var link1 = document.createElement('link');
              link1.href = cburl;
              link1.rel = "stylesheet";
              document.head.appendChild(link1);
          }
          link.onload = function () {
              document.getElementById('wallet').style.opacity='1'
          }
      }
      function aysnLoadcb () {
          var script = document.createElement('script');
          script.src = "http://static.jinkaimen.cn/hss/2.2.31/wallet.min.js";
          script.type = "text/javascript";
          document.head.appendChild(script);
          script.onerror = function () {
              var script = document.createElement('script');
              script.src = '/js/hss/2.2.31/wallet.min.js';
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
        aysnLoad('http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js','/js/hss/vendor.1.0.9.7.min.js');
        aysnLoadCss('http://static.jinkaimen.cn/hss/css/style.2.2.21.css','/css/hss/style.2.2.21.css');
        aysnLoadCss('http://static.jinkaimen.cn/weui/weui.css','/css/hss/weui.css');
      };
  </script>
</head>
<body>

<div id="wallet" style="opacity: 0">
  <div class="wallet">
    <a class="operation touch_op" href="/sqb/collection?oemNo=${oemNo}">
      <div class="logo collection"></div>
      <div class="operation-title">收款</div>
    </a>
    <a class="operation" href="/account/toHssAccount?oemNo=${oemNo}">
      <div class="logo wallet"></div>
      <div class="operation-title">余额</div>
      <div class="operation-small">${avaliable}</div>
    </a>
    <a class="operation" href="/account/toWithdraw?oemNo=${oemNo}">
      <div class="logo card"></div>
      <div class="operation-title">提现</div>
    </a>
  </div>
  <div class="announcement" id="announcement">
    <span class="announcement-icon"></span>
    <div class="announcement-text-box" id="announcement-text-box">
      <span class="announcement-text" id="announcement-text"></span>
    </div>
  </div>
  <div class="record">
    <a href="/sqb/tradeRecord?oemNo=${oemNo}" class="group touch_gr"><span class="record"></span>收款记录</a>
    <a href="/sqb/bank?oemNo=${oemNo}" class="group touch_gr"><span class="bank"></span>银行卡</a>
    <a href="/sqb/authentication?oemNo=${oemNo}" class="group touch_gr"><span class="user"></span>用户认证</a>
    <a href="/merchantInfo/toHelp" class="group touch_gr"><span class="help"></span>使用帮助</a>
  </div>
  <div>
    <div class="advertisement">
      <img src="http://static.jinkaimen.cn/hss/assets/banner.png" alt="">
    </div>
    <div class="application">
      <a class="group touch_gr" id="show_recommend" href="/sqb/myRecommend?oemNo=${oemNo}">
        <div class="logo friend"></div>
        <div class="text">推荐好友</div>
      </a>

      <div class="group touch_gr" id="show_upgrade">
        <div class="logo upgrade"></div>
        <div class="text">我要升级</div>
      </div>
      <a class="group touch_gr" href="/sqb/ticket">
        <div class="logo train"></div>
        <div class="text">火车票</div>
      </a>
      <!--<div class="group">-->
      <!--<div class="logo movie"></div>-->
      <!--<div class="text">电影票</div>-->
      <!--</div>-->
      <a class="group touch_gr" href="https://loan.rongba.com/H5tuiguang/kff?ref=hd_11015964&sid=haoshouyin001">
        <div class="logo loan"></div>
        <div class="text">贷款申请</div>
      </a>
      <a class="group touch_gr" href="http://channel.haodai.com/Mobile/creditcard/ref/hd_11015964/sid/haoshouyin001">
        <div class="logo credit"></div>
        <div class="text">信用卡申请</div>
      </a>
      <div class="group touch_gr" id="announcement-link">
        <div class="logo announcement1"></div>
        <div class="text">公告信息</div>
      </div>
      <div class="group touch_gr" id="unbundling">
        <div class="logo unbundling"></div>
        <div class="text">解绑微信</div>
      </div>
      <%--<div class="group touch_gr">--%>
      <%--<div class="logo more"></div>--%>
      <%--<div class="text">更多</div>--%>
      <%--</div>--%>
    </div>
  </div>
  <div class="layer" id="layer">
    <div class="space">
      <div class="space-title">
        解绑微信
        <div class="xx" id="xx"></div>
      </div>
      <div class="space-cont">
        <div class="cont-detail">解绑后，再次进入需要使用手机验证码登录并绑定微信。</div>
        <div class="operation">
          <div class="cancel" id="cancel">取消</div>
          <div class="submit" id="unbundlingSubmit">解绑</div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
<script>
  var pageData = {
    showRecommend: '${showRecommend}',//1显示升级 2不显示升级
    oemNo: '${oemNo}'
  };
</script>
<%--<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.32/wallet.min.js"></script>--%>
</html>