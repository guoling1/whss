<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/9
  Time: 19:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>${oemName}</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.21.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div class="flexBox flex-box-column" id="transaction">
  <div class="query" id="query">
    <div class="top flexBox">
      <div class="searchBox">
        <div class="input flexBox">
          <input type="text" readonly id="dateFrom">
          <span>至</span>
          <input type="text" readonly id="dateTo">
        </div>
      </div>
      <div class="unfold" id="unfold">筛选</div>
    </div>
    <div class="bottom">
      <div class="screen">
        <div class="group">
          <div class="g-title">支付状态</div>
          <div class="g-list">
            <div class="li active" y-variable="paySuccess">支付成功</div>
            <div class="li" y-variable="payError">支付失败</div>
            <div class="li" y-variable="payWait">待支付</div>
          </div>
        </div>
        <div class="group">
          <div class="g-title">支付方式</div>
          <div class="g-list">
            <div class="li active" y-variable="payWx">微信扫码</div>
            <div class="li active" y-variable="payAli">支付宝扫码</div>
            <div class="li active" y-variable="payQuick">快捷支付</div>
            <div class="li active" y-variable="payQQ">QQ钱包</div>
          </div>
        </div>
        <div class="btn">
          <div class="cancel" id="cancel">取消</div>
          <div class="submit" id="submit">确定</div>
        </div>
      </div>
    </div>
  </div>
  <div class="list" id="content">

  </div>
</div>

</body>
<%--<script src="http://static.jinkaimen.cn/weui/weui.min.js"></script>--%>
<script src="https://res.wx.qq.com/open/libs/weuijs/1.0.0/weui.min.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.2.30/tradeRecord.min.js"></script>
</html>