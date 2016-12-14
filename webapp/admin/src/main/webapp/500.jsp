<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/8
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <link rel="stylesheet" href="/css/not-fund.css">
    <title>500</title>
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
<div class="not-fund-wrap">
    <div class="not-fund-img">
        <span>错误信息： ${errorMsg}</span>
    </div>
    <p>您访问的页面出现了问题</p>
    <a href="/index" class="go-index">去首页看看</a>
</div>
</body>
</html>
