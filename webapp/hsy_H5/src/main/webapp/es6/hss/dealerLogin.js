// 引入动画模版 处理验证码
const Animation = _require('animation');
const animation = new Animation();
// 引入http message
const vali = _require('validate');
const message = _require('message');
const http = _require('http');
// 定义变量
const mobile = document.getElementById('mobile');
const code = document.getElementById('code');
const submit = document.getElementById('submit');

// 定义验证码
animation.validcode({
    url: '/dealer/sendVerifyCode',
    phoneName: 'mobile',
    phoneVal: 'mobile',
    btn: 'sendCode'
});

// 注册
submit.addEventListener('click', ()=> {
    if (vali.joint({
            phone: mobile.value,
            code: code.value
        })) {
        http.post('/dealer/ajaxLogin', {
            mobile: mobile.value,
            code: code.value,
        }, (data)=> {
            window.location.href = data.url;
        })
    }
});