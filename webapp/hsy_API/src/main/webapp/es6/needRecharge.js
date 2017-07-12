// 引入http message
const http = _require('http');
const message = _require('message');
let recharge = document.getElementById('recharge');
// 唤起微信支付
let onWeixinJSBridge = function (jsonData) {
    WeixinJSBridge.invoke(
        'getBrandWCPayRequest', {
            "appId": jsonData.appId, //公众号名称，由商户传入
            "timeStamp": jsonData.timeStamp, //时间戳，自 1970 年以来的 秒数
            "nonceStr": jsonData.nonceStr, //随机串
            "package": jsonData.package,
            "signType": jsonData.signType, //微信签名方式:
            "paySign": jsonData.paySign //微信签名
        },
        // 使用以上方式判断前端返回,微信团队郑重提示:res.err_msg 将在用户支付成功后返回ok，但并不保证它绝对可靠。
        function (res) {
            if (res.err_msg == "get_brand_wcpay_request:cancel") {
                console.log('取消支付')
            } else if (res.err_msg == "get_brand_wcpay_request:ok") {
                window.location.href = '/trade/success/' + jsonData.orderId;
            } else {
                alert(res.err_code + res.err_desc + res.err_msg);
            }
        }
    );
};
// 唤起支付宝支付
let onAlipayJSBridge = function (jsonData) {
    //jsonData.channelNo
    AlipayJSBridge.call("tradePay", {tradeNO: jsonData.tradeNO},
        function (result) {
            if (result.resultCode == 9000 || result.resultCode == 8000) {
                window.location.href = '/trade/success/' + jsonData.orderId;
            }
        });
};
recharge.addEventListener('click',function () {
    let keyCtrl = pageData.source;
    if ( keyCtrl ) {
        switch ( keyCtrl ) {
            case 'wx':
                message.load_show('正在支付');
                http.post_form('/membership/recharge',{
                    type: pageData.type,
                    source: pageData.source,
                    mid: pageData.mid,
                },function (data) {
                    http.post(data.payUrl, {}, function (data) {
                        message.load_hide();
                        onWeixinJSBridge(data);
                    });
                })
                break;
            case 'zfb':
                message.load_show('正在支付');
                http.post_form('/membership/recharge',{
                    type: pageData.type,
                    source: pageData.source,
                    mid: pageData.mid,
                },function (data) {
                    http.post(data.payUrl, {}, function (data) {
                        message.load_hide();
                        onAlipayJSBridge(data);
                    });
                })
                break;
        }
    }
})

