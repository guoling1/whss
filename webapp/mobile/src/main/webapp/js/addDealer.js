/**
 * Created by yulong.zhang on 2016/12/6.
 */

var validate = validate();

function addSecondDealer() {
    var mobile = document.getElementById('mobile').value;
    var name = document.getElementById('name').value;
    var belongArea = document.getElementById('belongArea').value;
    var bankAccountName = document.getElementById('bankAccountName').value;
    var bankCard = document.getElementById('bankCard').value;
    var bankReserveMobile = document.getElementById('bankReserveMobile').value;
    var weixinSettleRate = document.getElementById('weixinSettleRate').value;
    var alipaySettleRate = document.getElementById('alipaySettleRate').value;
    var quickSettleRate = document.getElementById('quickSettleRate').value;
    var withdrawSettleFee = document.getElementById('withdrawSettleFee').value;
    if(weixinSettleRate==''||alipaySettleRate==""||quickSettleRate==""||withdrawSettleFee==''){
        prompt_show('请填写结算价');
    }
    var dealerData = JSON.stringify({mobile: mobile, name:name, belongArea:belongArea,
        bankAccountName:bankAccountName, bankCard:bankCard,
        bankReserveMobile:bankReserveMobile, weixinSettleRate:weixinSettleRate,
        alipaySettleRate:alipaySettleRate, quickSettleRate:quickSettleRate,
        withdrawSettleFee:withdrawSettleFee});
    if (validate.phone(mobile) &&
        validate.phone(bankReserveMobile) &&
        validate.name(name) &&
        validate.address(belongArea) &&
        validate.bankNo(bankCard)) {
        $.ajax({
            type: "post",
            url: "/dealer/addSecondDealer",
            data: dealerData,
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                if (data.code == 1) {
                    window.location.href="/dealer/indexInfo";
                } else {
                    console.log(data)
                    prompt_show(data.msg);
                }
            },
            error: function () {}
        });
    }
}