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
    <link rel="stylesheet" href="http://img.jinkaimen.cn/hsy/css/reset.css">
    <link rel="stylesheet" href="http://img.jinkaimen.cn/hsy/css/style.css">

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
    <div id="err">
    <img src="http://img.jinkaimen.cn/hsy/assets/book.png" alt="">
    <p>错误信息： ${errorMsg}</p>
    <p class="small"></p>
    <div class="btn" onclick="(function(){window.history.go(-1)})()">返回</div>
    </div>
    </body>
    <script src="http://img.jinkaimen.cn/hsy/js/qrcode.min.js"></script>
    </html>
