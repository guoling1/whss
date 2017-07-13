<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>充值成功</title>
  <link rel="stylesheet" href="../../css/style.2.0.2.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="rechargeResults">
  <img src="../assets/member/success.png" alt="">
  <!--<img src="../assets/member/fail.png" alt="">-->
  <p class="result">充值成功</p>
  <%--<p class="errCode">错误码</p>--%>
  <a href="javascript:;" class="toRecord" href="<%=basePath%>membership/toRechargeList?mid=${appPolicyMember.id}" >查看充值记录</a>
  <a class="weui-btn weui-btn_plain-primary back" href="javascript:" onclick="self.location=document.referrer;">返回</a>
</div>

</body>
</html>
