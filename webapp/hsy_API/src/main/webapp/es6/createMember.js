/**
 * Created by administrator on 2016/12/8.
 */

// 引入动画模版 处理验证码
const AnimationCountdown = _require('art-countdown');
let countdown = new AnimationCountdown('sendCode', '重新获取');
// 引入http message
const validate = _require('validate');
const message = _require('message');
const http = _require('http');
// 定义变量
const mobile = document.getElementById('mobile');
const sendCode = document.getElementById('sendCode');
const code = document.getElementById('code');
const submit = document.getElementById('submit');
const layer = document.getElementById('layer');
const cancel = document.getElementById('cancel');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();

var mcid,isDeposited;
var mySwiper = new Swiper('.swiper-container', {
    effect : 'coverflow',
    slidesPerView: 1.35,
    centeredSlides: true,
    coverflow: {
        rotate: 0,
        stretch: -50,
        depth: 200,
        modifier: 1,
        slideShadows : false
    },
    onInit: function(swiper){
        var prompt = document.getElementById('prompt').getElementsByTagName('p');
        var swip = document.getElementsByClassName('swiperInp');
        for(var i=0;i<prompt.length;i++){
            if(i==swiper.activeIndex){
                prompt[i].style.display='block';
                mcid = swip[i].value;
                isDeposited = swip[i].getAttribute('isDeposited');
                console.log(mcid,isDeposited)
            }else {
                prompt[i].style.display='none'
            }
        }
    },
    onSlideChangeStart:function(swiper){ console.log(swiper.activeIndex);
        var prompt = document.getElementById('prompt').getElementsByTagName('p');
        var swip = document.getElementsByClassName('swiperInp');
        for(var i=0;i<prompt.length;i++){
            if(i==swiper.activeIndex){
                prompt[i].style.display='block';
                mcid = swip[i].value;
                isDeposited = swip[i].getAttribute('isDeposited');
                console.log(mcid,isDeposited)
            }else {
                prompt[i].style.display='none'
            }
        }
    }
})

if(pageData.consumeCellphone!=''){
    mobile.value = pageData.consumeCellphone;
    mobile.readOnly = 'true'
}else {
    mobile.value = '';
    // mobile.removeAttribute('readOnly')
    // mobile.readOnly = 'false'
}

sendCode.addEventListener('click', function () {
    if (validate.phone(mobile.value)) {
        if (countdown.check()) {
            $.ajax({
                type: "post",
                url: "/membership/sendVcode",
                dataType: "json",
                data: {
                    "cellphone"          :   mobile.value
                },
                error: function () {
                    alert("请求失败")
                },
                success: function (data) {
                    message.prompt_show('验证码发送成功');
                    countdown.submit_start();
                }
            });
        }
    }
});

submit.addEventListener('click', ()=> {
        if (validate.joint({
                phone: mobile.value,
                code: code.value
            })) {
            $.ajax({
                type: "post",
                url: "/membership/createMember",
                dataType: "json",
                data: {
                    "consumerCellphone" : mobile.value,
                    "vcode" : code.value,
                    "openID" : $("#openID").val(),
                    "userID" : $("#userID").val(),
                    "source" : $("#source").val(),
                    "cid" : $("#cid").val(),
                    "mcid" : mcid,
                    "isDeposited" : isDeposited
                },
                error: function () {
                    alert("请求失败")
                },
                success: function (data) {
                    if(data.flag=="success")
                    {
                        if(data.status==1)
                            location.href="/membership/createMemberSuccess?mid="+data.mid;
                        else
                            location.href="/sqb/needRecharge?mid="+data.mid+"&cellphone="+mobile.value+"&source="+$("#source").val();
                    }
                    else
                        alert(data.result);
                }
            });
        }
});