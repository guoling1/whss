<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/16
  Time: 10:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>交易详情</title>
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
            script.src = "http://static.jinkaimen.cn/hss/2.2.31/common.min.js";
            script.type = "text/javascript";
            document.head.appendChild(script);
            script.onerror = function () {
                var script = document.createElement('script');
                script.src = '/js/hss/2.2.31/common.min.js';
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
            aysnLoad('http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js','/js/hss/vendor.1.0.9.7.min.js');
            aysnLoadCss('http://static.jinkaimen.cn/hss/css/style.2.2.21.css','/css/hss/style.2.2.21.css');
            aysnLoadCss('http://static.jinkaimen.cn/weui/weui.css','/css/hss/weui.css');
        };
    </script>
</head>
<body>

<div class="flexBox flex-box-column" id="transactionDetail">
    <div class="space">
        <div class="amount">
            <div class="left">金额</div>
            <div class="right">￥ ${totalMoney}</div>
        </div>
    </div>
    <div class="space">
        <div class="detail">
            <div class="left">商品名称</div>
            <div class="right">${goodsName}</div>
        </div>
        <div class="detail">
            <div class="left">商品描述</div>
            <div class="right">${goodsDescribe}</div>
        </div>
        <div class="detail">
            <div class="left">时间</div>
            <div class="right">${createTime}</div>
        </div>
        <div class="detail">
            <div class="left">状态</div>
            <div class="right">${status}</div>
        </div>
        <div class="detail">
            <div class="left">付款方式</div>
            <div class="right">${payType}</div>
        </div>
        <div class="detail">
            <div class="left">商户名称</div>
            <div class="right">${merchantName}</div>
        </div>
        <div class="detail">
            <div class="left">商户订单号</div>
            <div class="right">${orderNo}</div>
        </div>
        <div class="detail">
            <div class="left">交易流水号</div>
            <div class="right">${sn}</div>
        </div>
        <div class="detail">
            <div class="left">结算状态</div>
            <div class="right">${settleStatus}</div>
        </div>
    </div>
</div>

</body>
<%--<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.9.13.min.js"></script>--%>
<%--<script src="http://static.jinkaimen.cn/hss/2.2.31/common.min.js"></script>--%>
</html>