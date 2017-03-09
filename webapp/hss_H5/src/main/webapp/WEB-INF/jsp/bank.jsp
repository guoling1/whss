<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/9
  Time: 19:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.1.3.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="bankList">
  <div class="group red" id="color">
    <div class="top">
      <div class="logo" id="logo"></div>
      <div class="info">
        <div class="name">
          ${bankName}
          <a href="/sqb/bankBranch" class="btn red" id="btn">补充支行信息</a></div>
        <div class="type">储蓄卡 | ${branchName}</div>
      </div>

    </div>
    <div class="bottom">
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
        <div class="bank_x"></div>
      </div>
      <div class="p">
        <div class="word">${bankNo}</div>
      </div>
      <div class="s">${mobile}</div>
    </div>
    <div class="small">
      该卡用于收款后的自动结算与余额提现
    </div>
  </div>
</div>

</body>
<script>
  var pageData = {
    bin: '${bankBin}',
    hasBranch:'${hasBranch}'
  }
</script>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.3.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.1.3/bank.min.js"></script>
</html>