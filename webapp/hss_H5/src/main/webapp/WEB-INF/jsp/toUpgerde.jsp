<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>我的推广</title>
    <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.2.0.1.css">
    <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="upgradeWay">
    <div class="space flexBox flex-box-column">
        <div class="cont">
            <div class="info">
                <div class="boss"></div>
                <div class="name">${upgradeRules.name}</div>
            </div>
            <div class="payType">
                <div class="li">
                    <span class="wx"></span>
                    费率${upgradeRules.weixinRate}%
                </div>
                <div class="li">
                    <span class="ali"></span>
                    费率${upgradeRules.alipayRate}%
                </div>
                <div class="li">
                    <span class="quick"></span>
                    费率${upgradeRules.fastRate}%
                </div>
            </div>
            <ul class="upType">
                <li>
                    <div class="left"><span class="check active"></span>方式一：付费升级</div>
                    <div class="right">￥${upgradeRules.upgradeCost}</div>
                </li>
                <li>
                    <div class="left"><span class="check"></span>方式二：推广${upgradeRules.promotionNum}个好友</div>
                    <div class="right">还差${restCount}个</div>
                    <div class="small">达到推广好友数量，系统将自动为您升级</div>
                </li>
            </ul>
        </div>
        <div class="pay">微信立即支付 ￥128</div>
    </div>
</div>

</body>
<script type="text/javascript">
    var cssEl = document.createElement('style');
    document.documentElement.firstElementChild.appendChild(cssEl);

    function setPxPerRem() {
        var dpr = 1;
        //把viewport分成10份的rem，html标签的font-size设置为1rem的大小;
        var pxPerRem = document.documentElement.clientWidth * dpr / 10;
        cssEl.innerHTML = 'html{font-size:' + pxPerRem + 'px!important;}';
    }
    setPxPerRem();
</script>

<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
</html>
