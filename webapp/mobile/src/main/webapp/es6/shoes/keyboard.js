/**
 * Created by administrator on 2016/12/7.
 */

_require.register("keyboard", (module, exports, _require, global)=> {
  // 调用message http
  const message = _require('message');
  const http = _require('http');
  // 启用fastclick处理点击事件
  const fastclick = _require('fastclick');
  fastclick.attach(document.body);
  // 键盘类
  class Keyboard {
    /*
     * 自定义支付键盘
     * 不提供样式 不提供标签
     * 用户输入值 span标签内展示 隐藏input存储
     * 键盘事件 监听键盘最外层标签的 touchEnd 事件*/

    /*
     * 键盘初始化构造事件*/
    constructor(object) {
      // 初始化value值
      this.value = '';
      // 展示span id
      this.span = document.getElementById(object.spanId);
      // 隐藏input id
      this.input = document.getElementById(object.inputId);
      // 键盘最外层div id
      this.keyboard = document.getElementById(object.keyboardId);

      // 获取keyNum函数
      const getKeyValue = (ev, key)=> {
        let a = ev.getAttribute(key);
        while (!a && ev.id != "keyboard") {
          ev = ev.parentNode;
          a = ev.getAttribute(key);
        }
        return a;
      };

      // copy来的 函数
      const zeroNum = (oldNum, inputNum)=> {
        var num = '';
        if (oldNum === '') {
          oldNum = '0';
        }
        if (oldNum.indexOf('.') != -1) {
          if (oldNum.length - oldNum.indexOf('.') > 2)return oldNum;
          if (inputNum === '.')return oldNum;
        }
        if (oldNum === '0' && inputNum !== '.') {
          num = inputNum;
        } else {
          num = oldNum + inputNum;
        }
        //大于俩万
        if (num >= 20000) {
          num = 20000;
          message.prompt_show('收款金额不能超过20000');
        }
        return num;
      };

      /*
       * keyboard 监听 touchstart 事件*/
      const touchStart = (e) => {
        let ev = e.target;
        if (ev.getAttribute('touch') == 'true') {
          ev.style.backgroundColor = "#f4f5f8";
        }
      };

      /*
       * keyboard 监听 touchend 事件*/
      const touchEnd = (e)=> {
        let ev = e.target;
        // 判断条件 还原点击前的白色
        if (ev.getAttribute('touch') == 'true') {
          ev.style.backgroundColor = "#FFF";
        }

        // 获取上一次的值
        let oldValue = this.input.value;
        // 获取新输入的值
        let keyNum = getKeyValue(ev, 'keyNum');
        if (keyNum) {
          let a = zeroNum(oldValue, keyNum);
          this.input.value = a;
          this.span.innerHTML = a;
          return;
        }
        // 获取输入的功能键 delete quick wx-zfb
        let keyCtrl = getKeyValue(ev, 'keyCtrl');
        if (keyCtrl) {
          switch (keyCtrl) {
            case 'delete':
              let a = oldValue.substr(0, oldValue.length - 1);
              this.input.value = a;
              this.span.innerHTML = a;
              break;
            case 'quick':
              if (oldValue > 0) {
                http.post('/wx/receipt', {
                  totalFee: oldValue,
                  payChannel: '103'
                }, function (data) {
                  window.location.href = data.payUrl;
                });
              } else {
                message.prompt_show('请输入正确的收款金额');
              }
              break;
            case 'wx-zfb':
              if (oldValue > 0) {
                http.post('/wx/receipt', {
                  totalFee: oldValue,
                  payChannel: '101'
                }, function (data) {
                  window.location.href = "/sqb/charge?qrCode=" + encodeURIComponent(data.payUrl) + "&name=" + data.subMerName + "&money=" + data.amount;
                });
              } else {
                message.prompt_show('请输入正确的收款金额');
              }
              break;
            case 'wx-pay':
              if (oldValue > 0) {
                http.post('/wx/receiptByCode', {
                  totalFee: oldValue,
                  payChannel: '101',
                  merchantId: pageDate.merchantId
                }, function (data) {
                  window.location.href = data.payUrl;
                });
              } else {
                message.prompt_show('请输入正确的支付金额');
              }
              break;
            case 'ali-pay':
              if (oldValue > 0) {
                http.post('/wx/receiptByCode', {
                  totalFee: oldValue,
                  payChannel: '102',
                  merchantId: pageDate.merchantId
                }, function (data) {
                  window.location.href = data.payUrl;
                });
              } else {
                message.prompt_show('请输入正确的支付金额');
              }
              break;
          }
        }
      };

      // 注册监听事件
      this.keyboard.addEventListener('touchstart', touchStart);
      this.keyboard.addEventListener('touchend', touchEnd);
    }
  }
  module.exports = Keyboard;
});