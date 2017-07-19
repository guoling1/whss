<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>用户认证</title>
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
              document.getElementById('authentication').style.opacity='1'
          }
      }
      window.onload = function () {
          aysnLoadCss('http://static.jinkaimen.cn/weui/weui.css','/css/hss/weui.css');
      };
  </script>
  <style>
    html, body {
      width: 100%;
      height: 100%;
    }

    #authentication {
      width: 100%;
      height: 100%;
      background-color: #f4f5f8;
    }

    .space {
      padding: 15px;
      background-color: #FFF;
      margin-bottom: 8px;
    }

    .mark {
      width: 85px;
      height: 21px;
      border-radius: 3px;
      background-color: #f8ac74;
      font-size: 12px;
      color: #FFF;
      line-height: 21px;
      margin-bottom: 5px;
      text-align: center;
    }

    .mark.green {
      background-color: #4ecab1;
    }

    .store {
      display: inline-block;
      width: 14px;
      height: 12px;
      background: url("../../assets/store.png") center;
      background-size: 14px 12px;
      margin-right: 5px;
      transform: translate3d(0, 2px, 0);
    }

    .people {
      display: inline-block;
      width: 14px;
      height: 14px;
      background: url("../../assets/people.png") center;
      background-size: 14px 14px;
      margin-right: 5px;
      transform: translate3d(0, 2px, 0);
    }

    .icard {
      display: inline-block;
      width: 14px;
      height: 12px;
      background: url("../../assets/icard.png") center;
      background-size: 14px 12px;
      margin-right: 5px;
      transform: translate3d(0, 2px, 0);
    }

    .icard.green {
      background: url("../../assets/icard-green.png") center;
      background-size: 14px 12px;
    }

    .list {
      overflow: hidden;
      line-height: 35px;
    }

    .left {
      float: left;
      font-size: 14px;
      color: #9ba1bb;
    }

    .right {
      float: right;
      font-size: 14px;
      color: #35405b;
    }

    .btn {
      float: right;
      font-size: 14px;
      color: #36c493;
    }

    .small {
      font-size: 12px;
      color: #aab0c6;
    }
  </style>
</head>
<body>
<div id="authentication" style="opacity: 0">
  <div class="space">
    <div class="mark"><span class="store"></span>已认证</div>
    <div class="list">
      <div class="left">店铺名:</div>
      <div class="right">${merchantName}</div>
    </div>
    <div class="list">
      <div class="left">店铺所在地:</div>
      <c:choose>
        <c:when test="${district == ''}">
          <div class="right">--</div>
        </c:when>
        <c:otherwise>
          <div class="right">${district}</div>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="list">
      <div class="left">详细地址:</div>
      <div class="right">${address}</div>
    </div>
    <div class="list">
      <div class="left">注册时间:</div>
      <div class="right">${createTime}</div>
    </div>
  </div>

  <div class="space">
    <div class="mark"><span class="people"></span>已认证</div>
    <div class="list">
      <div class="left">店主姓名:</div>
      <div class="right">${name}</div>
    </div>
    <div class="list">
      <div class="left">身份证号:</div>
      <div class="right">${idCard}</div>
    </div>
  </div>

  <div class="space">
    <c:choose>
      <c:when test="${isAuthen == '1'}">
        <div class="mark"><span class="icard"></span>已认证</div>
      </c:when>
      <c:otherwise>
        <div class="mark green"><span class="icard green"></span>信用卡</div>
      </c:otherwise>
    </c:choose>
    <div class="list">
      <div class="left">信用卡号:</div>
      <c:choose>
        <c:when test="${isAuthen == '1'}">
          <div class="right">${creditCardName} 尾号${creditCardShort}</div>
        </c:when>
        <c:otherwise>
          <a href="/sqb/creditCardAuthen?oemNo=${oemNo}" class="btn">立即认证</a>
        </c:otherwise>
      </c:choose>
    </div>
    <div class="list">
      <span class="small">认证通过后可使用大额收款</span>
    </div>
  </div>
</div>
</body>
<script>
  var _hmt = _hmt || [];
  (function () {
    var hm = document.createElement("script");
    hm.src = "https://hm.baidu.com/hm.js?a50541c927a563018bb64f5d6c996869";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
  })();
</script>
</html>
