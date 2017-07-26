<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>继续开卡</title>
    <link rel="stylesheet" href="../../css/style.2.0.2.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="needRecharge">
    <img src="../../assets/member/jixu.png" alt="">
    <p class="title">尊敬的 ${cellphone}准会员：</p>
    <p class="content">您的会员卡还差一步即可开通成功，请您点击下方的【继续开卡】</p>
    <div id="recharge" class="weui-btn weui-btn_primary">继续开卡</div>
    <div class="js_dialog" id="iosDialog2" style="display: none">
        <div class="weui-mask"></div>
        <div class="weui-dialog">
            <div class="weui-dialog__bd">您已成功开通会员卡</div>
            <div class="weui-dialog__ft">
                <a href="<%=basePath%>membership/memberInfo?mid=${mid}&source=${source}"  class="weui-dialog__btn weui-dialog__btn_primary">确定</a>
            </div>
        </div>
    </div>
</div>
    <%--<h1 style="font-size: 40px;">
        尊敬的 ${cellphone}准会员：
        您的会员卡还差一步即可开通成功
        请您点击下方的【继续开卡】
        <br>

        &lt;%&ndash;临时写的应该用ajax&ndash;%&gt;
        <a style="color:red;" id="recharge">继续开卡</a>
        &lt;%&ndash;<a style="color:red;" href="<%=basePath%>membership/recharge?mid=${mid}&type=activate&source=${source}">继续开卡</a>&ndash;%&gt;
    </h1>--%>
</body>
<script>
    var pageData = {
        type : 'activate',
        source : '${source}',
        mid　: '${mid}'
    }
</script>
<script src="../../js/jquery-2.2.3.min.js"></script>
<script src="https://a.alipayobjects.com/g/h5-lib/alipayjsapi/0.2.4/alipayjsapi.inc.min.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="http://static.jinkaimen.cn/vendor/vendor.2.0.2.min.js"></script>
<script src="http://static.jinkaimen.cn/hsy/js/2.0.2/needRecharge.min.js"></script>
</html>
