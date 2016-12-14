
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

function getFormatDate(date, pattern) {
    if (date == undefined) {
        date = new Date();
    }
    if (pattern == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    }
    return date.format(pattern);
}
function getFormatDateByLong(l, pattern) {
    return getFormatDate(new Date(l), pattern);
}

$(document).ready(function(){
    record();
});
function record(){
    var param = {"page":1,"size":20};
    var newParam=JSON.stringify(param);
    $.ajax({
        type: "post",
        url: "/wx/getOrderRecord",
        data:newParam,
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            var _html="";
           if(data.code==1){
                if(data.result.records.length>0){
                    for(var i=0;i<data.result.records.length;i++){

                       if(data.result.records[i].tradeType==0){
                           _html+='<div class="group">';
                           _html+='<div class="top">';
                           _html+='<div class="left">收款</div>';
                           _html+='<div class="right">+'+data.result.records[i].totalFee+'元</div>';
                           _html+='</div>';
                           _html+='<div class="bottom">';
                           if("N"==data.result.records[i].payResult){
                               _html+='<div class="left">等待支付</div>';
                           }
                           if("H"==data.result.records[i].payResult){
                               _html+='<div class="left">支付中</div>';
                           }
                           if("A"==data.result.records[i].payResult){
                               _html+='<div class="left">支付已受理</div>';
                           }
                           if("S"==data.result.records[i].payResult){
                               _html+='<div class="left">支付成功</div>';
                           }
                           if("F"==data.result.records[i].payResult){
                               _html+='<div class="left">支付失败</div>';
                           }
                           _html+='<div class="right">'+getFormatDateByLong(data.result.records[i].createTime,"yyyy-MM-dd hh:mm")+'</div>';
                           _html+='</div>';
                           _html+='</div>';

                       }
                        if(data.result.records[i].tradeType==1){
                            _html+='<div class="group">';
                            _html+='<div class="top">';
                            _html+='<div class="left">提现</div>';
                            _html+='<div class="right">-'+data.result.records[i].totalFee+'元</div>';
                            _html+='</div>';
                            _html+='<div class="bottom">';
                            if("N"==data.result.records[i].payResult){
                                _html+='<div class="left">待提现</div>';
                            }else if("H"==data.result.records[i].payResult){
                                _html+='<div class="left">提现中</div>';
                            }else if("A"==data.result.records[i].payResult){
                                _html+='<div class="left">请求已受理</div>';
                            }else if("S"==data.result.records[i].payResult){
                                _html+='<div class="left">提现成功</div>';
                            }else if("F"==data.result.records[i].payResult){
                                _html+='<div class="left">提现失败</div>';
                            }else if("W"==data.result.records[i].payResult){
                                _html+='<div class="left">请求已受理</div>';
                            }else if("E"==data.result.records[i].payResult){
                                _html+='<div class="left">提现异常</div>';
                            }
                            _html+='<div class="right">'+getFormatDateByLong(data.result.records[i].createTime,"yyyy-MM-dd hh:mm")+'</div>';
                            _html+='</div>';
                            _html+='</div>';
                        }


                    }
                }
               $("#content").html(_html);
           }else{
            alert(data.msg);
           }

        },
        error: function () {}
    });
}
