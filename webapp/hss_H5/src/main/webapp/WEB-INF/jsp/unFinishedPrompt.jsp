<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>${oemName}</title>
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
        function aysnLoadcb () {
            var script = document.createElement('script');
            script.src = "http://static.jinkaimen.cn/hss/2.2.30/common.min.js";
            script.type = "text/javascript";
            document.head.appendChild(script);
            script.onerror = function () {
                var script = document.createElement('script');
                script.src = '/js/hss/2.2.30/common.min.js';
                script.type = "text/javascript";
                document.head.appendChild(script);
            }
        }
        function aysnLoad(url,cburl) {
            var script = document.createElement('script');
            script.src = url;
            script.type = "text/javascript";
            document.head.appendChild(script);
            script.onload = function () {
                aysnLoadcb()
            }
            script.onerror = function () {
                var script = document.createElement('script');
                script.src = cburl;
                script.type = "text/javascript";
                document.head.appendChild(script);
                script.onload = function () {
                    aysnLoadcb()
                }
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
<div id="prompt">
    <img src="http://static.jinkaimen.cn/hss/assets/book.png" alt="">
    <p>该商户资料正在审核</p>
    <p>请耐心等待</p>
</div>
</body>
<%--<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>--%>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.31/common.min.js"></script>--%>
</html>