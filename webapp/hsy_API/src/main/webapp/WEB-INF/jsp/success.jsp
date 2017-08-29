<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>付款成功</title>
  <link rel="stylesheet" href="../../css/style.2.0.3.css">
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hsy/css/style.1.0.0.css">--%>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="success">
  <div class="space">
    <div class="info"></div>
    <div class="text">付款成功 ${money}元</div>
  </div>
  <div class="group">
    <div class="left">确认码</div>
    <div class="right red">${code}</div>
  </div>
  <div class="group">
    <div class="left">交易流水号</div>
    <div class="right">${sn}</div>
  </div>
  <a href="https://click.haodai.com/to.php?type=loan&pid=2108" class="ad">
    <img src="http://static.jinkaimen.cn/hsy/assets/ad-success-1.png" alt="">
  </a>
  <a href="http://openapi.haodai.com/wap/sdwap?ref=sd_h5001_qbjj001" class="ad">
    <img src="http://static.jinkaimen.cn/hsy/assets/ad-success-2.jpeg" alt="">
  </a>
</div>
<script>
  var _hmt = _hmt || [];
  (function () {
    var hm = document.createElement("script");
    hm.src = "https://hm.baidu.com/hm.js?7089764f9496288c924af64a3297a93a";
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(hm, s);
  })();
</script>

</body>
</html>
