<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/jsp/path.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>会员卡</title>
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
                                location.href="<%=basePath%>membership/needRecharge?mid="+data.mid+"&cellphone="+$("#consumerCellphone").val();
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
</html>
