<%--
  Created by IntelliJ IDEA.
  User: Thinkpad
  Date: 2017/1/6
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>算算能挣多少钱</title>
    <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.2.21.css">--%>
    <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">--%>
    <script>
        function aysnLoadCss(url,cburl) {
            var link = document.createElement('link');
            link.href = url;
            link.rel = "stylesheet";
            document.head.appendChild(link);
            link.onerror = function () {
                var link = document.createElement('link');
                link.href = cburl;
                link.rel = "stylesheet";
                document.head.appendChild(link);
            }
        }
        function aysnLoad(url,cburl) {
            var script = document.createElement('script');
            script.src = url;
            script.type = "text/javascript";
            document.head.appendChild(script);
            script.onerror = function () {
                var script = document.createElement('script');
                script.src = cburl;
                script.type = "text/javascript";
                document.head.appendChild(script);
            }
        }
        window.onload = function () {
            aysnLoad('http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js','/js/hss/vendor.1.0.9.7.min.js');
            aysnLoadCss('http://static.jinkaimen.cn/hss/css/style.2.2.21.css','/css/hss/style.2.2.21.css');
            aysnLoadCss('http://static.jinkaimen.cn/weui/weui.css','/css/hss/weui.css');
        };
    </script>
</head>
<body>

<div id="ad">
    <img src="http://static.jinkaimen.cn/hss/assets/hss-up-ad.png" alt="">
</div>

</body>

<%--<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.12.min.js"></script>--%>
</html>
