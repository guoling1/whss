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
        <a style="color:red;" href="<%=basePath%>membership/toRecharge?mid=${mid}">继续开卡</a>
    </h1>
</body>
</html>
