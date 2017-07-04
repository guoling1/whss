<%--
  Created by IntelliJ IDEA.
  User: Thinkpad
  Date: 2016/12/29
  Time: 21:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>请选择通道</title>
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
          script.src = "http://static.jinkaimen.cn/hss/2.2.30/channelList.min.js";
          script.type = "text/javascript";
          document.head.appendChild(script);
          script.onerror = function () {
              var script = document.createElement('script');
              script.src = '/js/hss/2.2.30/channelList.min.js';
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

<div class="channel">
  <div class="channel-box" id="channelBox">
    <div class="channel-space top" style="margin-bottom: 3px">
      <div class="channel-group" style="border: none">
        <div class="channel-li big">渠道名称</div>
        <div class="channel-li">结算时间<span id="know-channel"></span></div>
        <div class="channel-li" style="width: 16%">费率</div>
        <div class="channel-li right" style="float: right">单笔限额</div>
      </div>
    </div>
    <div class="channel-space" id="channel">
      <%-- 动态添加 --%>
    </div>
    <div class="channel-small"><span></span>各通道限额动态变化，一次不通过可换其他通道重试</div>

    <div class="channel-mpt-box">
      <div class="channel-mpt">
        <div class="channel-mpt-title">结算时间说明</div>
        <div class="channel-mpt-group"><span class="channel-mpt-li"></span>D0：结算的通道，当天到账，一般在十分钟内</div>
        <div class="channel-mpt-group"><span class="channel-mpt-li"></span>T1：是指下一个工作日，遇到周末及法定假日顺延</div>
        <div class="channel-mpt-group-small">“摩宝”通道在22:56分以后支付成功的，算到下一个工作日</div>
      </div>
    </div>
  </div>

  <div class="message-space" id="layer-channel">
    <div class="message-box">
      <div class="message-box-head">
        结算时间说明
        <div class="message-x" id="layer-x-channel"></div>
      </div>
      <div class="message-box-body">
        <div>“D0”是指当日<br>一般在十分钟内到账结算卡<br></div>
        <div style="margin-top:10px">“T1”是指下一个工作日<br>遇到周末及法定节假日顺延<br></div>
        <span>22:56分以后支付成功的，算到下一个工作日</span>
      </div>
      <div class="message-box-foot">
        <div class="message-enter" id="submit-channel">
          确认
        </div>
      </div>
    </div>
  </div>
</div>

</body>
<%--<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js"></script>--%>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.30/channelList.min.js"></script>--%>
<%--<script src="/js/hss/2.2.30/channelList.min.js"></script>--%>
</html>
