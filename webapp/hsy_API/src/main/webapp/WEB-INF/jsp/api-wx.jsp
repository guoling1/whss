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
  <title>付款给商户</title>
  <link rel="stylesheet" href="../../css/style.2.0.3.css">
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hsy/css/style.2.0.2.css">--%>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="api">
  <div class="space">
    <div class="head">订单金额</div>
    <div class="amount"><span class="money">¥ </span> <span id="amount">0.00</span></div>
    <div class="group">
      <div class="info">
        <span class="babel">商家名称</span>
        <span id="merchantName"></span>
      </div>
    </div>
    <div class="group">
      <div class="info">
        <span class="babel">商家描述</span>
        <span id="subject"></span>
      </div>
    </div>
  </div>
  <div class="submit" id="submit">立即支付</div>
</div>

</body>
<script>
  var pageData = {
    payUrl: '${payUrl}',
    status: '${status}',
    amount: '${amount}',
    merchantName: '${merchantName}',
    subject: '${subject}',
    pageCallBackUrl: '${pageCallBackUrl}'
  };
</script>
<script src="https://a.alipayobjects.com/g/h5-lib/alipayjsapi/0.2.4/alipayjsapi.inc.min.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="http://static.jinkaimen.cn/hsy/js/vendor.2.0.2.min.js"></script>
<script>
  document.getElementById('amount').innerHTML = pageData.amount;
  document.getElementById('merchantName').innerHTML = pageData.merchantName;
  document.getElementById('subject').innerHTML = pageData.subject;
</script>
<script>
  const message = _require('message');
  var jsonData = JSON.parse(pageData.payUrl);
  var pay = function () {
    if (pageData.status == 'success') {
      WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
          "appId": jsonData.appId, //公众号名称，由商户传入
          "timeStamp": jsonData.timeStamp, //时间戳，自 1970 年以来的 秒数
          "nonceStr": jsonData.nonceStr, //随机串
          "package": jsonData.package,
          "signType": jsonData.signType, //微信签名方式:
          "paySign": jsonData.paySign //微信签名
        },
        // 使用以上方式判断前端返回,微信团队郑重提示:res.err_msg 将在用户支付成功后返回ok，但并不保证它绝对可靠。
        function (res) {
          if (res.err_msg == "get_brand_wcpay_request:cancel") {
            console.log('取消支付')
          } else if (res.err_msg == "get_brand_wcpay_request:ok") {
           window.location.replace(pageData.pageCallBackUrl);
          } else {
            alert(res.err_code + res.err_desc + res.err_msg);
          }
        }
      );
    } else {
      message.prompt_show('网络异常');
    }
  }
  document.getElementById('submit').onclick = pay;
  if (typeof WeixinJSBridge === "undefined") {
    if (document.addEventListener) {
      document.addEventListener('WeixinJSBridgeReady', pay, false);
    }
  } else {
    pay();
  }
</script>
</html>
