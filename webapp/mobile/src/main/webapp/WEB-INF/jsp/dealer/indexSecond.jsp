<%--
  Created by IntelliJ IDEA.
  User: xiang.yu
  Date: 2016/12/5
  Time: 20:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收收</title>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/hss/css/style.css">
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>
<div id="dealerIndexSec">
    <div class="header">
        <div class="top">
            <!--头像-->
            <!--<div class="photo">-->
            <!--&lt;!&ndash; <img src="" alt="" /> &ndash;&gt;-->
            <!--</div>-->
            <span>您好，${data.mobile}</span>

            <div class="right">
                <a href="/dealer/get"><img src="http://img.jinkaimen.cn/hss/assets/tx.png" alt=""/>
                    我的信息</a>
                <a href="/dealer/getCard"><img src="http://img.jinkaimen.cn/hss/assets/yhcard.png" alt=""/>
                    我的结算卡</a>

            </div>
        </div>
        <div class="bottom">
            <div class="left">
                <p><span class="decimal">${data.yesterdayProfitMoney}</span>元</p>

                <p>昨日分润总额</p>
            </div>
            <div class="middle">
                <p><span class="decimal">${data.yesterdayDealMoney}</span>元</p>

                <p>昨日门店交易总额</p>
            </div>
            <div class="right">
                <p><span>${data.secondLevelDealerCodeInfo.lastDayActivateCount}</span>家</p>

                <p>昨日激活二维码</p>
            </div>
        </div>
    </div>
    <ul class="secDiff">
        <li>
            <p>待结算分润:
                <span class="decimal">${data.waitBalanceMoney}</span>元 月底结算</p>
        </li>
        <li>
            <p>已结分润:
                <span class="decimal">${data.alreadyBalanceMoney}</span>元</p>
        </li>
        <li>
            <p>我的二维码:
                <span>${data.secondLevelDealerCodeInfo.codeCount}</span>个</p>
        </li>
        <li>
            <p>未激活二维码:
                <span>${data.secondLevelDealerCodeInfo.unActivateCount}</span>个</p>
        </li>
    </ul>
    <!-- 都需要展示的信息代理商要展示的信息 -->
    <div class="myGain same">
        <div class="top">
            <i></i>
            <span class="title">我的分润</span>
            <span class="level">直接店铺</span>
            <span class="details"><a href="/dealer/profit/toMerchant">详情</a></span>
        </div>
        <div class="bottom">
            <div class="left">
                <p class="price decimal">${data.yesterdayProfitMoney}</p>

                <p>昨日分润金额(元)</p>
            </div>
            <div class="right">
                <p class="price decimal">${data.historyProfitMoney}</p>

                <p>历史分润总额(元)</p>
            </div>
        </div>
    </div>
    <div class="myStore same">
        <div class="top">
            <i></i>
            <span class="title">我发展的店铺</span>
            <span class="details"><a href="/dealer/getMyMerchants">详情</a></span>
        </div>
        <div class="bottom">
            <div class="left">
                <p class="price">${data.myMerchantCount.currentWeekActivateMerchantCount}</p>

                <p>本周激活店铺</p>
            </div>
            <div class="right">
                <p class="price">${data.myMerchantCount.activateMerchantCount}</p>

                <p>已激活店铺总数(家)</p>
            </div>
        </div>
    </div>
</div>
</body>
<script src="http://img.jinkaimen.cn/hss/js/qrcode.min.js"></script>
<script src="../js/dealerIndex.js"></script>
</html>