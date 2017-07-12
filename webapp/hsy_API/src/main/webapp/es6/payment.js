/**
 * Created by administrator on 2016/12/8.
 */
const message = _require('message');
const Keyboard = _require('keyboard');
new Keyboard({
  spanId: 'key-span',
  inputId: 'key-input',
  keyboardId: 'keyboard'
});
let choose = document.getElementById('choose');
let close = document.getElementById('close');
let mask = document.getElementById('iosDialog1');
let maskPay = document.getElementById('iosDialog2');
let val = document.getElementById('key-input');
let memberPay = document.getElementById('memberPay');
let wrap = document.getElementById('wrap');
let closePay = document.getElementById('closePay');

    var container = document.getElementById("inputBoxContainer");
    var boxInput = {
        maxLength:"",
        realInput:"",
        bogusInput:"",
        bogusInputArr:"",
        callback:"",
        init:function(fun){
            var that = this;
            this.callback = fun;
            that.realInput = container.children[0];
            that.bogusInput = container.children[1];
            that.bogusInputArr = that.bogusInput.children;
            that.maxLength = that.bogusInputArr[0].getAttribute("maxlength");
            that.realInput.oninput = function(){
                that.setValue();
            }
            that.realInput.onpropertychange = function(){
                that.setValue();
            }
        },
        setValue:function(){
            this.realInput.value = this.realInput.value.replace(/\D/g,"");
            console.log(this.realInput.value.replace(/\D/g,""))
            var real_str = this.realInput.value;
            for(var i = 0 ; i < this.maxLength ; i++){
                this.bogusInputArr[i].value = real_str[i]?real_str[i]:"";
            }
            if(real_str.length >= this.maxLength){
                this.realInput.value = '';
                this.callback();
            }
        },
        getBoxInputValue:function(){
            var realValue = "";
            for(var i in this.bogusInputArr){
                if(!this.bogusInputArr[i].value){
                    break;
                }
                realValue += this.bogusInputArr[i].value;
            }
            return realValue;
        }
    }

boxInput.init(function(){
    getValue();
});
// document.getElementById("confirmButton").onclick = function(){
//     getValue();
// }
function getValue(){
    var val = boxInput.getBoxInputValue();
    boxInput.setValue();
    maskPay.style.opacity = 0;
    maskPay.style.display = 'none';
}
choose.addEventListener('click',function () {
    if(val.value > 0){
        mask.style.opacity = 1;
        mask.style.display = 'block';
    }else {
        message.prompt_show('请输入正确的支付金额');
    }
});
close.addEventListener('click',function () {
    mask.style.opacity = 0;
    mask.style.display = 'none';
});
memberPay.addEventListener('click',function () {
    if(val.value > 0){
        mask.style.opacity = 0;
        mask.style.display = 'none';
        maskPay.style.opacity = 1;
        maskPay.style.display = 'block';
        document.getElementsByClassName('realInput')[0].focus();
    }else {
        message.prompt_show('请输入正确的支付金额');
    }
});
closePay.addEventListener('click',function () {
    maskPay.style.opacity = 0;
    maskPay.style.display = 'none';
});