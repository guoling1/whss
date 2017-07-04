<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>会员卡充值</title>
</head>
<body>
    <h1 style="font-size: 40px;">
        会员卡页面<br>
        会员卡名称:${appPolicyMember.membershipName}<br>
        会员卡上显示的店铺名称:${appPolicyMember.membershipShopName}<br>
        手机号:${appPolicyMember.consumerCellphone}<br>
        会员卡号:${appPolicyMember.memberCardNO}<br>
        会员卡折扣(折):${appPolicyMember.discount}<br>
        <input type="hidden" name="" value="${appPolicyMember.openID}"/>
        <input type="number" name=""  /><button id="recharge">充值</button>
    </h1>
</body>
</html>
