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
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div id="dealerIndex">
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
                <p><span>${data.firstLevelDealerCodeInfos.lastDayActivateCount}</span>家</p>

                <p>昨日激活二维码</p>
            </div>
        </div>
    </div>
    <ul class="list">
        <li>
            <img src="http://img.jinkaimen.cn/hss/assets/mar.png" alt=""/>

            <div class="">
                <p>
                    <a href="/dealer/unDistributeCode" style="color: #272727;font-weight: bold;">${data.firstLevelDealerCodeInfos.residueCount}个</a>
                </p>
                <p>剩余二维码</p>
            </div>
        </li>
        <li>
            <img src="http://img.jinkaimen.cn/hss/assets/jt.png" alt=""/>

            <div class="">
                <p>${data.firstLevelDealerCodeInfos.distributeCount}个</p>

                <p>已配二维码</p>
            </div>
        </li>
        <li>
            <img src="http://img.jinkaimen.cn/hss/assets/time.png" alt=""/>

            <div class="">
                <p>${data.firstLevelDealerCodeInfos.unActivateCount}个</p>

                <p>未激活二维码</p>
            </div>
        </li>
        <li>
            <img src="http://img.jinkaimen.cn/hss/assets/lamp.png" alt=""/>

            <div class="">
                <p>${data.firstLevelDealerCodeInfos.activateCount}个</p>

                <p>已激活二维码</p>
            </div>
        </li>
    </ul>
    <div class="btn">
       <span><a href="/dealer/toDistributeCode">分配二维码</a></span>
        <span><a href="/dealer/addSecondDealerPage">新增二级代理</a></span>
    </div>
    <div class="secGain same">
        <div class="top">
            <i></i>
            <span class="title">我的分润</span>
            <span class="level">二级代理</span>
            <span class="details"><a href="/dealer/profit/toDealer">详情</a></span>
        </div>
        <div class="bottom">
            <div class="left">
                <p class="price decimal">${data.secondYesterdayProfitMoney}</p>

                <p>昨日分润金额(元)</p>
            </div>
            <div class="right">
                <p class="price decimal">${data.secondHistoryProfitMoney}</p>

                <p>历史分润总额(元)</p>
            </div>
        </div>
    </div>
    <div class="dealer same">
        <div class="top">
            <i></i>
            <span class="title">我发展的二级代理</span>
            <span class="details"><a href="/dealer/getMyDealers">详情</a></span>
        </div>
        <div class="bottom">
            <div class="left">
                <p class="price">${data.mySecondDealerCount}</p>

                <p>二级代理个数</p>
            </div>
            <div class="right">
                <p class="price">${data.distributeToSecondDealerCodeCount}</p>

                <p>已分配二维码(个)</p>
            </div>
        </div>
    </div>
    <div class="myGain same">
        <div class="top">
            <i></i>
            <span class="title">我的分润</span>
            <span class="level">直接店铺</span>
            <span class="details"><a href="/dealer/profit/toMerchant">详情</a></span>
        </div>
        <div class="bottom">
            <div class="left">
                <p class="price decimal">${data.merchantYesterdayProfitMoney}</p>

                <p>昨日分润金额(元)</p>
            </div>
            <div class="right">
                <p class="price decimal">${data.merchantHistoryProfitMoney}</p>

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
    <div class="code">
        直客二维码：${data.firstLevelDealerCodeInfos.distributeToSelfCount}个
    </div>
</div>
</body>
<script src="http://img.jinkaimen.cn/hss/js/qrcode.min.js"></script>
<script src="../js/dealerIndex.js"></script>
</html>