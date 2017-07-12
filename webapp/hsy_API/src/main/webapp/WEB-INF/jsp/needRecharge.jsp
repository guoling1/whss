<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<html>
<head>
    <title>继续开卡</title>
</head>
<body>
    <h1 style="font-size: 40px;">
        尊敬的 ${cellphone}准会员：
        您的会员卡还差一步即可开通成功
        请您点击下方的【继续开卡】
        <br>

        <%--临时写的应该用ajax--%>
        <a style="color:red;" id="recharge">继续开卡</a>
        <%--<a style="color:red;" href="<%=basePath%>membership/recharge?mid=${mid}&type=activate&source=${source}">继续开卡</a>--%>
    </h1>
</body>
<script>
    var pageData = {
        type : 'activate',
        source : '${source}',
        mid　: '${appPolicyMember.id}'
    }
</script>
<script src="https://a.alipayobjects.com/g/h5-lib/alipayjsapi/0.2.4/alipayjsapi.inc.min.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.2.0.2.min.js"></script>
<script src="../../js/2.0.2/needRecharge.min.js"></script>
</html>
