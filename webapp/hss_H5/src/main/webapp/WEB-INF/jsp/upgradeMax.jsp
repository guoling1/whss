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
      <c:if test="${empty headimgUrl}"><img src="http://static.jinkaimen.cn/hss/assets/hss-default-head.png"
                                            alt=""></c:if>
      <c:if test="${not empty headimgUrl}"><img src="${headimgUrl}" alt=""></c:if>
    </div>
    <div class="status">${mobile} <span>${name}</span></div>
  </div>
  <div class="upgrade">
    <div class="small" onclick="javascript:location.href='/sqb/suansuan'">升级合伙人，不仅能降费率，推广好友还能拿分润</div>
    <div class="table">
      <div class="t-head">
        <div class="li t">合伙人等级</div>
        <div class="li t">微信费率</div>
        <div class="li t">支付宝费率</div>
        <div class="li t">无卡费率</div>
        <div class="li t">升级</div>
      </div>
      <c:forEach items="${upgradeArray}" var="item" varStatus="status">
        <c:if test="${status.index==0}">
          <div class="t-body">
            <div class="li p">
              <span class="star${status.index+1} upn"></span>
                ${item.name}
            </div>
            <div class="li w"><fmt:formatNumber value="${item.weixinRate*100}" pattern="0.00#"/>%</div>
            <div class="li w"><fmt:formatNumber value="${item.alipayRate*100}" pattern="0.00#"/>%</div>
            <div class="li w"><fmt:formatNumber value="${item.fastRate*100}" pattern="0.00#"/>%</div>
            <c:if test="${level==item.type}">
              <div class="li w b">
                <div class="me">&nbsp;我的等级</div>
              </div>
            </c:if>
            <c:if test="${level<item.type}">
              <div class="li w b">
                <div class="up" onclick="javascript:location.href='/sqb/toUpgrade/${item.id}'">立即升级</div>
              </div>
            </c:if>
            <c:if test="${level>item.type}">
              <div class="li w b">- -</div>
            </c:if>
          </div>
        </c:if>
        <c:if test="${status.index==1}">
          <div class="t-body">
            <div class="li p">
              <span class="star${status.index+1} upn"></span>
                ${item.name}
            </div>
            <div class="li w"><fmt:formatNumber value="${item.weixinRate*100}" pattern="0.00#"/>%</div>
            <div class="li w"><fmt:formatNumber value="${item.alipayRate*100}" pattern="0.00#"/>%</div>
            <div class="li w"><fmt:formatNumber value="${item.fastRate*100}" pattern="0.00#"/>%</div>
            <c:if test="${level==item.type}">
              <div class="li w b">
                <div class="me">&nbsp;我的等级</div>
              </div>
            </c:if>
            <c:if test="${level<item.type}">
              <div class="li w b">
                <div class="up" onclick="javascript:location.href='/sqb/toUpgrade/${item.id}'">立即升级</div>
              </div>
            </c:if>
            <c:if test="${level>item.type}">
              <div class="li w b">- -</div>
            </c:if>
          </div>
        </c:if>
        <c:if test="${status.index==2}">
          <div class="t-body">
            <div class="li p">
              <span class="star${status.index+1} upn"></span>
                ${item.name}
            </div>
            <div class="li w"><fmt:formatNumber value="${item.weixinRate*100}" pattern="0.00#"/>%</div>
            <div class="li w"><fmt:formatNumber value="${item.alipayRate*100}" pattern="0.00#"/>%</div>
            <div class="li w"><fmt:formatNumber value="${item.fastRate*100}" pattern="0.00#"/>%</div>
            <c:if test="${level==item.type}">
              <div class="li w b">
                <div class="me">&nbsp;我的等级</div>
              </div>
            </c:if>
            <c:if test="${level<item.type}">
              <div class="li w b">
                <div class="up" onclick="javascript:location.href='/sqb/toUpgrade/${item.id}'">立即升级</div>
              </div>
            </c:if>
            <c:if test="${level>item.type}">
              <div class="li w b">- -</div>
            </c:if>
          </div>
        </c:if>
        <c:if test="${status.index==3}">
          <div class="t-body">
            <div class="li p">
              <span class="star${status.index+1} upn"></span>
                ${item.name}
            </div>
            <div class="li w"><fmt:formatNumber value="${item.weixinRate*100}" pattern="0.00#"/>%</div>
            <div class="li w"><fmt:formatNumber value="${item.alipayRate*100}" pattern="0.00#"/>%</div>
            <div class="li w"><fmt:formatNumber value="${item.fastRate*100}" pattern="0.00#"/>%</div>
            <c:if test="${level==item.type}">
              <div class="li w b">
                <div class="me">&nbsp;我的等级</div>
              </div>
            </c:if>
            <c:if test="${level<item.type}">
              <div class="li w b">
                <div class="up" onclick="javascript:location.href='/sqb/toUpgrade/${item.id}'">立即升级</div>
              </div>
            </c:if>
            <c:if test="${level>item.type}">
              <div class="li w b">- -</div>
            </c:if>
          </div>
        </c:if>
      </c:forEach>
    </div>
    <div class="btn">
      <div onclick="javascript:location.href='/sqb/suansuan'">算算能挣多少钱</div>
    </div>
  </div>
  <div class="rocket" id="rocket"></div>
  <div class="notice flexBox flex-box-column miss" id="notice">
    <div class="cont">
      <h5>升级说明：</h5>

      <h6>Q：会员升级后，何时生效？</h6>

      <p>购买成功后立即生效，升级后的收款有效按新费率执行。</p>

      <h6>Q：升级后，如何拿分润？</h6>

      <p>升级成功后，您需要邀请好友注册成商户，您的好友每收一笔款项，您就可以获得相应分润。资金存入您的账户余额。</p>

      <h6>Q：如何查看我的分润？</h6>

      <p>您可以前往“邀请好友”页面查看分润情况。</p>

      <h6>Q：升级有什么好处？</h6>

      <p>升级后您自己使用“好收收”收款，将按照新的费率执行。您推广的好友以及好友再次推广的好友收款，您还能拿费率差额的分润，您等级越高，分润越多。</p>

      <h6>Q：通过您的推广二维码注册，或者在注册时填写您的邀请码，才能算作您推广的好友</h6>

      <h6>Q：指导好友提交资料通过审核后，收款达到2000元即可激活</h6>

      <h6>Q：每笔分润金额 = 好友每笔收款金额 × (您的费率 - 好友费率)。如果好友的费率低于或等于您的费率，则好友在收款时您不享受分佣。</h6>

      <h6>Q：好友2年内产生的收款参与分润</h6>
    </div>
    <div class="xx" id="xx"></div>
  </div>
</div>

</body>

<script src="http://static.jinkaimen.cn/vendor/vendor.1.0.1.min.js"></script>
<script>
  var notice = document.getElementById('notice');
  var rocket = document.getElementById('rocket');
  var xx = document.getElementById('xx');
  rocket.addEventListener('click', function () {
    notice.className = 'notice flexBox flex-box-column';
    setTimeout(function () {
      notice.style.opacity = 1;
    }, 0);
  });
  xx.addEventListener('click', function () {
    notice.style.opacity = 0;
    setTimeout(function () {
      notice.className = 'notice flexBox flex-box-column miss';
    }, 400);
  })
</script>
</html>