<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/8
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收银</title>
    <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
    <script>
        var _hmt = _hmt || [];
        (function () {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?ef1d541ee1b0d474088be5f1760a9cc8";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
    </script>
</head>
<body>

<div id="prompt">
    <img src="http://static.jinkaimen.cn/hss/assets/hss-prompt.png" alt="">

    <p>${errorMsg}</p>

    <div class="btn" onclick="WeixinJSBridge.call('closeWindow')">返回</div>
</div>

</body>
<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
<script src="http://static.jinkaimen.cn/hss/2.0.1/common.min.js"></script>
</html>
