<%--
  Created by IntelliJ IDEA.
  User: yulong.zhang
  Date: 2016/12/5
  Time: 20:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>好收银</title>
    <link rel="stylesheet" href="/css/reset.css">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div id="addDealer">
    <div class="basic">
        <p class="title">基本资料</p>
        <label for="">
            <span>代理人手机</span>
            <input type="number" id="mobile" placeholder="二级代理手机号，用于登录">
        </label>
        <label for="">
            <span>代理姓名</span>
            <input type="text" id="name" placeholder="输入代理名字">
        </label>
        <label for="">
            <span>所在地</span>
            <input type="text" id="belongArea" placeholder="省/市/区/街道详情">
        </label>
    </div>
    <div class="basic">
        <p class="title">结算信息</p>
        <label for="">
            <span>结算卡</span>
            <input type="number" id="bankCard" placeholder="输入分润结算银行卡号">
        </label>
        <label for="">
            <span>开户名</span>
            <input type="text" id="bankAccountName" placeholder="银行开户名称">
        </label>
        <label for="">
            <span>预留手机号</span>
            <input type="number" id="bankReserveMobile" placeholder="银行开户预留手机号">
        </label>
    </div>
    <div class="set">
        <p class="title">分润设置</p>
        <ul>
            <li>
                <div class="top">
                    <img src="http://img.jinkaimen.cn/hsy/assets/zfb.png" alt="" />
                    <span>支付宝(D0)</span>
                    <div class="">
                        <p>我的收单结算价：${alipaySettleRate}%</p>
                        <p>商户收单手续费：${alipayMerchantSettleRate}%</p>
                    </div>
                </div>
                <div class="bottom">
                    <label for="">
                        结算价
                        <input type="number" id="alipaySettleRate" value="">%
                    </label>
                </div>
            </li>
            <li>
                <div class="top">
                    <img src="http://img.jinkaimen.cn/hsy/assets/wx.png" alt="" />
                    <span>微信(D0)</span>
                    <div class="">
                        <p>我的收单结算价：${weixinSettleRate}%</p>
                        <p>商户收单手续费：${weixinMerchantSettleRate}%</p>
                    </div>
                </div>
                <div class="bottom">
                    <label for="">
                        结算价
                        <input type="number" id="weixinSettleRate" value="">%
                    </label>
                </div>
            </li>
            <li>
                <div class="top">
                    <img src="http://img.jinkaimen.cn/hsy/assets/card.png" alt="" />
                    <span>无卡快捷(D0)</span>
                    <div class="">
                        <p>我的收单结算价：${quickSettleRate}%</p>
                        <p>商户收单手续费：${quickMerchantSettleRate}%</p>
                    </div>
                </div>
                <div class="bottom">
                    <label for="">
                        结算价
                        <input type="number" id="quickSettleRate" value="">%
                    </label>
                </div>
            </li>
        </ul>
    </div>
    <div class="price">
        <div class="top">
            <span>提现结算价</span>
            <div class="">
                <input type="number" id="withdrawSettleFee" value="">
                <span>元/笔</span>
            </div>
        </div>
        <div class="bottom">
            <span>我的提现结算价：${withdrawSettleFee}元/笔</span>
            <span>商户提现手续费：${withdrawMerchantSettleFee}元/笔</span>
        </div>
    </div>
    <div class="submit" onclick="addSecondDealer()">
        提交
    </div>
</div>
<jsp:include page="../message.jsp"></jsp:include>
</body>

<script src="http://img.jinkaimen.cn/hsy/js/qrcode.min.js"></script>
<script src="/js/jQuery-2.1.4.min.js"></script>
<script src="/js/utility.js"></script>
<script src="/js/addDealer.js"></script>
</html>