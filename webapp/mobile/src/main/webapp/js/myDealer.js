/**
 * Created by yulong.zhang on 2016/12/6.
 */


function showMyDetail(dealerId, distributeCount, activateCount) {
    if (dealerId > 0) {
        $.ajax({
            type: "post",
            url: "/dealer/getMyDealerDetail",
            data: JSON.stringify({dealerId: dealerId}),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                if (data.code == 1) {
                    var dealerDetailHtml = document.getElementById('dealerDetail');
                    document.getElementById('proxyName').innerHTML = data.result.proxyName;
                    document.getElementById('mobile').innerHTML = data.result.mobile;
                    document.getElementById('distributeCount').innerHTML = distributeCount;
                    document.getElementById('activateCount').innerHTML = activateCount;
                    var qrCodeHtml = document.getElementById('qrcode');
                    var qrCodeRecords = data.result.codeRecords;
                    for (var qrcode in qrCodeRecords) {
                        var div = document.createElement('div');
                        div.className = 'row';
                        var p1 = document.createElement('p');
                        p1.className = 'w35';
                        p1.innerHTML = qrCodeRecords[qrcode].distributeDate;
                        div.appendChild(p1);
                        var p2 = document.createElement('p');
                        p2.className = 'w50 middle';
                        p2.innerHTML = qrCodeRecords[qrcode].startCode + '至' + qrCodeRecords[qrcode].endCode;
                        div.appendChild(p2);
                        var p3 = document.createElement('p');
                        p3.innerHTML = qrCodeRecords[qrcode].count;
                        div.appendChild(p3);
                        qrCodeHtml.appendChild(div);
                    }
                    dealerDetailHtml.style.display='block';
                }
            },
            error: function () {}
        });
    }
}