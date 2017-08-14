<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta name="format-detection" content="telephone=no"/>
  <title>付款给商户</title>
  <link rel="stylesheet" href="../../css/style.2.0.2.css">
  <%--<link rel="stylesheet" href="http://static.jinkaimen.cn/hsy/css/style.2.0.2.css">--%>
  <link rel="stylesheet" href="http://static.jinkaimen.cn/weui/weui.css">
</head>
<body>

<div id="payment-wx">
  <div class="space">
    <div class="prompt">
      <span class="pay"></span>付款给${merchantName}
      <%--<div class="word">和商家确定好金额后输入</div>--%>
    </div>
    <div class="key-ipt">
      <span class="key-title">支付金额</span>
      <span class="key-cursor animate"></span>
      <span class="key-span" id="key-span"></span>
      <span class="key-icon">￥</span>
      <input type="hidden" id="key-input" value="">
    </div>
    <div class="sale" <c:if test="${appPolicyMember==null}">style="display:none;"</c:if> >
      <img src="../assets/member/zhekou.png" alt="">
      <span><span id="rebate">${appPolicyMember.discountInt}.${appPolicyMember.discountFloat}</span>折</span>
      <span class="right">-￥<span id="minus">0.00</span></span>
      <%--${appPolicyMember.cid},${appPolicyMember.mcid},${appPolicyMember.id}
      isMemberCardPay 0 否 1是  discountFee--%>
    </div>
  </div>
  <%--<div class="safe">
    <img src="http://static.jinkaimen.cn/hss/assets/wx-gray.png" alt="">
    微信安全支付
  </div>--%>
  <div class="keyboard" id="keyboard">
    <div class="copyright">
      <div <c:if test="${appPolicyMember==null}">style="display:none;"</c:if> >
        <img src="../assets/member/vip.png" alt="">
        <span class="word">卡内余额：</span>
        <span>${appPolicyMember.remainingSum}</span>
        <c:if test="${appPolicyMember.canRecharge==1}">
        <a class="weui-btn weui-btn_mini weui-btn_primary" href="/sqb/toRecharge?mid=${appPolicyMember.id}&source=WX">充值</a>
        </c:if>
      </div>
    </div>
    <%--<div class="copyright flexBox">
      <span class="line"></span>
      <span class="word">由钱包加加提供技术支持</span>
      <span class="line"></span>
    </div>--%>
    <div class="keys">
      <div class="left">
        <div class="key" keyNum="1" touch="true">1</div>
        <div class="key" keyNum="2" touch="true">2</div>
        <div class="key" keyNum="3" touch="true">3</div>
        <div class="key" keyNum="4" touch="true">4</div>
        <div class="key" keyNum="5" touch="true">5</div>
        <div class="key" keyNum="6" touch="true">6</div>
        <div class="key" keyNum="7" touch="true">7</div>
        <div class="key" keyNum="8" touch="true">8</div>
        <div class="key" keyNum="9" touch="true">9</div>
        <div class="key gray"></div>
        <div class="key" keyNum="0" touch="true">0</div>
        <div class="key gray" keyNum=".">.</div>
      </div>
      <div class="wx">
        <div class="delete" keyCtrl="delete" touch="true"></div>
        <%--会员--%>
        <div class="pay" id="choose" <c:if test="${appPolicyMember==null}">style="display:none;"</c:if>>
          <p>￥<span id="realNum">0.00</span></p>
          <p>付款</p>
        </div>
        <%--非会员--%>
        <div class="pay" keyCtrl="wx-pay" <c:if test="${appPolicyMember!=null}">style="display:none;"</c:if>>
          付款
        </div>
      </div>
    </div>
    <div class="js_dialog" id="iosDialog1" style="opacity: 0; display: none;">
      <div class="weui-mask"></div>
      <div class="weui-dialog" style="top: 35%;">
        <div class="weui-dialog__hd">
          <img id="close" src="../../assets/member/return.png" alt="">
          <strong class="weui-dialog__title">选择支付方式</strong>
        </div>
        <div class="weui-dialog__bd">
          <div class="weui-cells">

            <a class="weui-cell weui-cell_access" href="javascript:;" id="memberPay">
              <div class="weui-cell__hd"><img src="../../assets/member/vip.png" alt="" style="width:20px;margin-right:5px;display:block"></div>
              <div class="weui-cell__bd">
                <p>会员卡支付(余额￥${appPolicyMember.remainingSum})</p>
              </div>
              <div class="weui-cell__ft"></div>
            </a>
            <a class="weui-cell weui-cell_access" href="javascript:;" class="pay" keyCtrl="wx-pay">
              <div class="weui-cell__hd"><img class="wx" src="../../assets/member/wxv.png" alt="" style="width:20px;margin-right:5px;display:block"></div>
              <div class="weui-cell__bd">
                <p>微信支付</p>
              </div>
              <div class="weui-cell__ft"></div>
            </a>
          </div>
        </div>
      </div>
    </div>
    <div class="js_dialog2" id="iosDialog2" style="opacity: 0; display: none;">
      <div class="weui-mask"></div>
      <div class="weui-dialog" style="top: 35%;">
        <div class="weui-dialog__hd">
          <img src="../../assets/member/cha.png" alt="" id="closePay">
          <strong class="weui-dialog__title">支付</strong>
        </div>
        <div class="weui-dialog__price" id="paymenyPrice">￥5.55</div>
        <div class="weui-dialog__bd">
          <div class="weui-cells">
            <a class="weui-cell weui-cell_access" href="javascript:;">
              <div class="weui-cell__hd"></div>
              <div class="weui-cell__bd">
                <img src="../../assets/member/vip.png" alt="" style="">
                <span>会员卡支付</span>
              </div>
              <div class="weui-cell__ft"></div>
            </a>
          </div>
          <div class="wrap" id="wrap">
              <p>请输入您开卡手机号后六位数字</p>
              <div class="inputBoxContainer" id="inputBoxContainer">
                <input type="number" class="realInput" maxlength="6" style="margin-left: -100%"/>
                <div class="bogusInput">
                  <input type="password" maxlength="6" disabled/>
                  <input type="password" maxlength="6" disabled/>
                  <input type="password" maxlength="6" disabled/>
                  <input type="password" maxlength="6" disabled/>
                  <input type="password" maxlength="6" disabled/>
                  <input type="password" maxlength="6" disabled/>
                </div>
              </div>
              <%--<button id="confirmButton" class="confirmButton">查看</button>
              <p class="showValue" id="showValue"></p>--%>
            </div>
        </div>
      </div>
    </div>
  </div>

</div>

</body>
<script>
  var pageData = {
      hsyOrderId: '${hsyOrderId}',
      remainingSum:'${appPolicyMember.remainingSum}',
      type:'',
      cid: '${appPolicyMember.cid}',
      mcid:'${appPolicyMember.mcid}',
      id:'${appPolicyMember.id}'
  };
  console.log('${appPolicyMember.id}')
  if(${appPolicyMember==null}){
      pageData.type='noMember'  // 无会员卡
  }else {
      pageData.type ='member'
  }

</script>
<script src="https://a.alipayobjects.com/g/h5-lib/alipayjsapi/0.2.4/alipayjsapi.inc.min.js"></script>
<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="http://static.jinkaimen.cn/hsy/js/vendor.2.0.2.min.js"></script>
<script src="http://static.jinkaimen.cn/hsy/js/2.0.2/payment.min.js"></script>
<script>
    var jsonData =     JSON.parse(${payUrl});

    WeixinJSBridge.invoke(
                'getBrandWCPayRequest', {
                  "appId": 'wxf784d28e5aab3b2a', //公众号名称，由商户传入
                  "timeStamp": '1502630170', //时间戳，自 1970 年以来的 秒数
                  "nonceStr": 'a6f6a6c131a850ecb0e3f11b08189', //随机串
                  "package": 'prepay_id=wx20170813211605ae15a1fc2d0177508692',
                  "signType": 'MD5', //微信签名方式:
                  "paySign": '3FCB6B15FEE4DC1E9D30285F2B3A7847' //微信签名
                },
                // 使用以上方式判断前端返回,微信团队郑重提示:res.err_msg 将在用户支付成功后返回ok，但并不保证它绝对可靠。
                function (res) {
                  if (res.err_msg == "get_brand_wcpay_request:cancel") {
                    console.log('取消支付')
                  } else if (res.err_msg == "get_brand_wcpay_request:ok") {
                    window.location.href = '/trade/success/' + jsonData.orderId;
                    alert(res.err_code + res.err_desc + res.err_msg);
                  }
                }
              );
</script>
</html>
