<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>提示</title>
    <link rel="stylesheet" href="../../css/style.2.0.2.css">
</head>
<body>

    <div id="rechargeResults">
        <img src="../assets/member/fail.png" alt="">
        <p class="result">提示</p>
        <p class="errCode">${tips}</p>
    </div>
</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js"></script>
<script>
    const http = _require('http');
    http.post('/merchantApi/code/jsapi', {
              trxType: 'WX_SCANCODE_JSAPI',
              merchantNo: '110000000093',
              orderNum: 'q1234',
              amount: '10.00',
                goodsName: '1234',
                callbackUrl: '12345',
                              orderIp: '110000000093',
                              sign: '110000000093'
            }, function (data) {
              message.load_hide();
              if (data.errorCode == 1) {
                window.location.replace('/trade/unionPaySuccess/' + data.orderId);
              } else {
                window.location.replace('/trade/unionPay2Error/' + data.orderId);
              }
            });
</script>
</html>