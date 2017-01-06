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
            <c:if test="${empty headimgUrl}"><img src="http://static.jinkaimen.cn/hss/assets/hss-default-head.png" alt=""></c:if>
            <c:if test="${not empty headimgUrl}"><img src="${headimgUrl}" alt=""></c:if>
        </div>
        <div class="status">${mobile} <span>普通会员</span></div>
    </div>
    <div class="upgrade">
        <div class="small">升级合伙人，不仅能降费率，推广好友还能拿分润</div>
        <div class="table">
            <div class="t-head">
                <div class="li w34 t">合伙人等级</div>
                <div class="li w22 t">微信费率</div>
                <div class="li w22 t">支付宝费率</div>
                <div class="li w22 t">无卡费率</div>
            </div>
            <c:forEach items="${upgradeArray}" var="item" varStatus="status">
                <c:if test="${status.index==0}">
                    <div class="t-body">
                        <c:if test="${level==item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1}"></span>

                                <div class="up1">
                                    <p>${item.name}</p>

                                    <div class="mes">我的费率</div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${level<item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1}"></span>

                                <div class="up1">
                                    <p>${item.name}</p>

                                    <div class="ups" onclick="javascript:location.href='/sqb/toUpgrade/${item.id}'">立即升级</div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${level>item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1} upn"></span>
                                    ${item.name}
                            </div>
                        </c:if>
                        <div class="li w22 w">${item.weixinRate*100}% <br> 无分佣</div>
                        <div class="li w22 w">${item.alipayRate*100}% <br> 无分佣</div>
                        <div class="li w22 w">${item.fastRate*100}% <br> 无分佣</div>
                    </div>
                </c:if>
                <c:if test="${status.index==1}">
                    <div class="t-body">
                        <c:if test="${level==item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1}"></span>

                                <div class="up1">
                                    <p>${item.name}</p>

                                    <div class="mes">我的费率</div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${level<item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1}"></span>

                                <div class="up1">
                                    <p>${item.name}</p>

                                    <div class="ups" onclick="javascript:location.href='/sqb/toUpgrade/${item.id}'">立即升级</div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${level>item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1} upn"></span>
                                    ${item.name}
                            </div>
                        </c:if>
                        <div class="li w22 w">${item.weixinRate*100}% <br> 万三分佣</div>
                        <div class="li w22 w">${item.alipayRate*100}% <br> 万三分佣</div>
                        <div class="li w22 w">${item.fastRate*100}% <br> 万三分佣</div>
                    </div>
                </c:if>

                <c:if test="${status.index==2}">
                    <div class="t-body">
                        <c:if test="${level==item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1}"></span>

                                <div class="up1">
                                    <p>${item.name}</p>

                                    <div class="mes">我的费率</div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${level<item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1}"></span>

                                <div class="up1">
                                    <p>${item.name}</p>

                                    <div class="ups" onclick="javascript:location.href='/sqb/toUpgrade/${item.id}'">立即升级</div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${level>item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1} upn"></span>
                                    ${item.name}
                            </div>
                        </c:if>
                        <div class="li w22 w">${item.weixinRate*100}% <br> 万五分佣</div>
                        <div class="li w22 w">${item.alipayRate*100}% <br> 万五分佣</div>
                        <div class="li w22 w">${item.fastRate*100}% <br> 万五分佣</div>
                    </div>
                </c:if>

                <c:if test="${status.index==3}">
                    <div class="t-body">
                        <c:if test="${level==item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1}"></span>

                                <div class="up1">
                                    <p>${item.name}</p>

                                    <div class="mes">我的费率</div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${level<item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1}"></span>

                                <div class="up1">
                                    <p>${item.name}</p>

                                    <div class="ups" onclick="javascript:location.href='/sqb/toUpgrade/${item.id}'">立即升级</div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${level>item.type}">
                            <div class="li w34 p">
                                <span class="star${status.index+1} upn"></span>
                                    ${item.name}
                            </div>
                        </c:if>
                        <div class="li w22 w">${item.weixinRate*100}% <br> 万七分佣</div>
                        <div class="li w22 w">${item.alipayRate*100}% <br> 万七分佣</div>
                        <div class="li w22 w">${item.fastRate*100}% <br> 万七分佣</div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
        <div class="btn">
            <div onclick="javascript:location.href='/sqb/suansuan'">算算能挣多少钱</div>
        </div>
    </div>
    <div class="rocket"></div>
</div>

</body>

<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
</html>