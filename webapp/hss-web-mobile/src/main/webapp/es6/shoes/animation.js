/**
 * Created by administrator on 2016/12/8.
 */

_require.register("animation", (module, exports, _require, global)=> {
  // 调用validate http message
  const validate = _require('validate');
  const message = _require('message');
  const http = _require('http');
  // 动画类
  /*
   * 动画类提供 验证码倒计时功能
   * 动画类提供 按钮防重复点击功能*/
  class Animation {
    // 构造方法
    constructor() {
      // 构造定时器
      this.timer = '';
      this.time = 60;
      this.allow = true;

      console.log('animation 构造成功...');
    }

    fourelement(object) {
      const url = object.url;
      let bankName = object.bankName;
      let bankVal = document.getElementById(object.bankVal);
      let nameName = object.nameName;
      let nameVal = document.getElementById(object.nameVal);
      let identityName = object.identityName;
      let identityVal = document.getElementById(object.identityVal);
      let phoneName = object.phoneName;
      let phoneVal = document.getElementById(object.phoneVal);
      let btn = document.getElementById(object.btn);
      btn.addEventListener('click', ()=> {
        if (this.allow) {
          if (validate.joint({
              bankNo: bankVal.value,
              idCard: identityVal.value,
              phone: phoneVal.value
            })) {
            // 发送请求
            http.post(url, {
              [bankName]: bankVal.value,
              [nameName]: nameVal.value,
              [identityName]: identityVal.value,
              [phoneName]: phoneVal.value
            }, ()=> {
              // 局部拦截
              this.allow = false;
              // 定时器
              this.timer = setInterval(()=> {
                btn.innerHTML = this.time--;
                if (this.time < 0) {
                  clearInterval(this.timer);
                  this.time = 60;
                  this.allow = true;
                  btn.innerHTML = '重新获取';
                }
              }, 1000);
              message.prompt_show('验证码发送成功');
            })
          }
        }
      })
    }

    validcode(object) {
      const url = object.url;
      let phoneName = object.phoneName;
      let phoneVal = document.getElementById(object.phoneVal);
      let btn = document.getElementById(object.btn);
      btn.addEventListener('click', ()=> {
        if (this.allow) {
          if (validate.phone(phoneVal.value)) {
            // 发送请求
            http.post(url, {
              [phoneName]: phoneVal.value
            }, ()=> {
              // 局部拦截
              this.allow = false;
              // 定时器
              this.timer = setInterval(()=> {
                btn.innerHTML = this.time--;
                if (this.time < 0) {
                  clearInterval(this.timer);
                  this.time = 60;
                  this.allow = true;
                  btn.innerHTML = '重新获取';
                }
              }, 1000);
              message.prompt_show('验证码发送成功');
            })
          }
        }
      })
    }

    sendcode(object) {
      const url = object.url;
      let btn = document.getElementById(object.btn);
      btn.addEventListener('click', ()=> {
        if (this.allow) {
          // 发送请求
          http.post(url, {}, ()=> {
            // 局部拦截
            this.allow = false;
            // 定时器
            this.timer = setInterval(()=> {
              btn.innerHTML = this.time--;
              if (this.time < 0) {
                clearInterval(this.timer);
                this.time = 60;
                this.allow = true;
                btn.innerHTML = '重新获取';
              }
            }, 1000);
            message.prompt_show('验证码发送成功');
          })
        }
      })
    }
  }
  module.exports = Animation;
});