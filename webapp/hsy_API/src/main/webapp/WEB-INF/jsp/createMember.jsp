<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta name="format-detection" content="telephone=no"/>
    <title>会员卡</title>
    <link rel="stylesheet" href="../../css/style.1.0.0.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
    <link rel="stylesheet" href="../../css/swiper-3.4.2.min.css">
    <script src="<%=basePath %>js/jquery-2.2.3.min.js"></script>
    <script>
        $(function(){
            $(":radio").click(function(){
                var isDeposited=$(this).attr("isDeposited");
                if(isDeposited==0)
                {
                    $("#depositAmount").parent().hide();
                }
                else
                {
                    $("#depositAmount").parent().show();
                    $("#depositAmount").text($(this).attr("depositAmount"));
                }
            });

            $(":radio:first").click();

            $("#sendVcode").click(function(){
                $.ajax({
                    type: "post",
                    url: "<%=basePath%>membership/sendVcode",
                    dataType: "json",
                    data: {
                        "cellphone"          :   $("#consumerCellphone").val()
                    },
                    error: function () {
                        alert("请求失败")
                    },
                    success: function (data) {
                        alert(data.result);
                    }
                });
            });

            $("#createMember").click(function(){
                $.ajax({
                    type: "post",
                    url: "<%=basePath%>membership/createMember",
                    dataType: "json",
                    data: {
                        "consumerCellphone"          :   $("#consumerCellphone").val(),
                        "vcode"                        :   $("#vcode").val(),
                        "openID"                       :   $("#openID").val(),
                        "userID"                       :   $("#userID").val(),
                        "source"                       :   $("#source").val(),
                        "cid"                          :   $("#cid").val(),
                        "mcid"                         :   $(":radio:checked").val(),
                        "isDeposited"                  :  $(":radio:checked").attr("isDeposited")

                    },
                    error: function () {
                        alert("请求失败")
                    },
                    success: function (data) {
                        if(data.flag=="success")
                        {
                            if(data.status==1)
                                location.href="<%=basePath%>membership/createMemberSuccess?mid="+data.mid;
                            else
                                location.href="<%=basePath%>membership/needRecharge?mid="+data.mid+"&cellphone="+$("#consumerCellphone").val()+"&source="+$("#source").val();
                        }
                        else
                            alert(data.result);
                    }
                });
            });
        });
    </script>
</head>
<body>
<div id="createMem">
    <div class="swiper-container top">
        <div class="swiper-wrapper">
            <div class="swiper-slide">
                <div class="card">
                    <span class="name">江湖味道总店</span>
                    <span class="type">尊贵金卡</span>
                    <span class="discount"><span>8</span>.0折</span>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="card">
                    <span class="name">江湖味道总店</span>
                    <span class="type">尊贵金卡</span>
                    <span class="discount"><span>8</span>.0折</span>
                </div>
            </div>
            <div class="swiper-slide">
                <div class="card">
                    <span class="name">江湖味道总店</span>
                    <span class="type">尊贵金卡</span>
                    <span class="discount"><span>8</span>.0折</span>
                </div>
            </div>
        </div>
    </div>
    <div class="weui-cells weui-cells_form">
        <div class="weui-cell">
            <div class="weui-cell__hd"><label class="weui-label">手机号</label></div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="number" pattern="[0-9]*" placeholder="请输入手机号">
            </div>
        </div>
        <div class="weui-cell weui-cell_vcode">
            <div class="weui-cell__hd">
                <label class="weui-label">验证码</label>
            </div>
            <div class="weui-cell__bd">
                <input class="weui-input" type="tel" placeholder="请输入短信验证码">
            </div>
            <div class="weui-cell__ft">
                <button class="weui-btn weui-btn_plain-primary btnCode">获取验证码</button>
            </div>
        </div>
    </div>
    <p>开通会员卡需要支付1元</p>
    <div class="weui-btn weui-btn_primary open">开通</div>
</div>
    <h1 style="font-size: 40px;">
        <c:forEach items="${cardList }" var="entity" varStatus="status">
        <input type="radio" isDeposited="${entity.isDeposited}" depositAmount="${entity.depositAmount}" name="mcid" value="${entity.id}" <c:if test="${status.index==0 }">checked</c:if>/><label>${entity.membershipName}</label>
        </c:forEach>
        <br>
    手机号<input type="text" id="consumerCellphone" name="consumerCellphone"/><br>
    验证码<input type="text" id="vcode" name="vcode">
        <input type="hidden" id="openID" name="openID" value="${authInfo.openID}"/>
        <input type="hidden" id="userID" name="userID" value="${authInfo.userID}"/>
        <input type="hidden" id="source" name="source" value="${authInfo.source}"/>
        <input type="hidden" id="cid" name="cid" value="${appPolicyConsumer.id}">

        <br>
        <label>开通会员卡需要支付<a id="depositAmount"></a>元</label>
    </h1>
        <button id="createMember">开通</button>
        <button id="sendVcode">发送验证码</button>
</body>
<script src="../../js/swiper-3.4.2.min.js"></script>
<script>
    var mySwiper = new Swiper('.swiper-container', {
        effect : 'coverflow',
        slidesPerView: 1.25,
        centeredSlides: true,
        coverflow: {
            rotate: 30,
            stretch: 10,
            depth: 60,
            modifier: 2,
            slideShadows : false
        }

    })

</script>
</html>
