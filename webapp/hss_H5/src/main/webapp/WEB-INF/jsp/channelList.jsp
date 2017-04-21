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
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.10.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div class="channel">
  <div class="channel-box" id="channelBox">
    <div class="channel-space top" style="margin-bottom: 3px">
      <div class="channel-group" style="border: none">
        <div class="channel-li big">渠道名称</div>
        <div class="channel-li">结算时间<span id="know-channel"></span></div>
        <div class="channel-li" style="width: 16%">费率</div>
        <div class="channel-li" style="float: right">单笔限额</div>
      </div>
    </div>
    <div class="channel-space" id="channel">
      <%-- 动态添加 --%>
    </div>
    <div class="channel-small"><span></span>各通道限额动态变化，一次不通过可换其他通道重试</div>
  </div>
</div>

</body>
</html>
