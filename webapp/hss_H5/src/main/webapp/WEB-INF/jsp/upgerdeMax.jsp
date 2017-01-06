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

<div id="upgrade">
    <div class="info">
        <div class="head">

            <img src="http://static.jinkaimen.cn/hss/assets/hss-default-head.png" alt="">
        </div>
        <div class="status">${mobile} <span>普通会员</span></div>
    </div>
    <div class="upgrade">
        <div class="small">升级合伙人，不仅能降费率，推广好友还能拿分润</div>
        <div class="table">
            <div class="t-head">
                <div class="li t">合伙人等级</div>
                <div class="li t">微信费率</div>
                <div class="li t">支付宝费率</div>
                <div class="li t">无卡费率</div>
                <div class="li t">升级</div>
            </div>
            <div class="t-body">
                <div class="li p">
                    <span class="star1 upn"></span>
                    普通
                </div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">- -</div>
            </div>
            <div class="t-body">
                <div class="li p">
                    <span class="star2 upn"></span>
                    店员
                </div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">
                    <div class="me">&nbsp;我的费率</div>
                </div>
            </div>
            <div class="t-body">
                <div class="li p">
                    <span class="star3 upn"></span>
                    店长
                </div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">0.49% <br> 无分佣</div>
                <div class="li w">
                    <div class="up">立即升级</div>
                </div>
            </div>
            <div class="t-body">
                <div class="li p">
                    <span class="star4 upn"></span>
                    老板
                </div>
                <div class="li w n">0.49% <br> 无分佣</div>
                <div class="li w n">0.49% <br> 无分佣</div>
                <div class="li w n">0.49% <br> 无分佣</div>
                <div class="li w n">
                    <div class="up">立即升级</div>
                </div>
            </div>
        </div>
        <div class="btn">
            <div>算算能挣多少钱</div>
        </div>
    </div>
    <div class="rocket"></div>
</div>

</body>

<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
</html>