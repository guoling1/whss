/**
 * Created by administrator on 2016/12/7.
 */

_require.register("keyboard", (module, exports, _require, global) => {
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

      // 改变微信title
      const changeTitle = function (title) {
        let body = document.getElementsByTagName('body')[0];
        if (!title) {
          document.title = '好收收';
        } else {
          document.title = title;
        }
        let iframe = document.createElement("iframe");
        iframe.className = 'none';
        iframe.setAttribute("src", "../assets/channel-i.png");
        iframe.addEventListener('load', function () {
          setTimeout(function () {
            iframe.removeEventListener('load', function () {
            });
            document.body.removeChild(iframe);
          }, 0);
        });
        document.body.appendChild(iframe);
      };

      // 获取keyNum函数
      const getKeyValue = (ev, key) => {
        let a = ev.getAttribute(key);
        while (!a && ev.id != "keyboard") {
          ev = ev.parentNode;
          a = ev.getAttribute(key);
        }
        return a;
      };

      // copy来的 函数
      const zeroNum = (oldNum, inputNum) => {
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
        if (num >= 10000) {
          num = 10000;
          message.prompt_show('收款金额不能超过10000');
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
       * 商户入网拦截校验*/
      const checkBusinessRegistration = (code) => {
        return new Promise(function (resolve, reject) {
          http.post('/wx/checkMerchantInfo1', {
            channelTypeSign: code
          }, function () {
            resolve(true);
          });
        });
      };

      /*
       * keyboard 监听 touchend 事件*/
      const touchEnd = (e) => {
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

        // 获取输入的功能键 delete quick wx-zfb
        let keyCtrl = getKeyValue(ev, 'keyCtrl');
        if (keyCtrl) {
          switch (keyCtrl) {
            case 'delete':
              let a = oldValue.substr(0, oldValue.length - 1);
              this.input.value = a;
              this.span.innerHTML = a;
              break;
            case 'wx-pay':
              if (oldValue > 0) {
                message.load_show('正在支付');
                http.post('/trade/scReceipt', {
                  totalFee: oldValue,
                  payChannel: '801',
                  memberId: pageData.memberId,
                  merchantId: pageData.merchantId
                }, function (data) {
                  http.post(data.payUrl, {}, function (data) {
                    message.load_hide();
                    onWeixinJSBridge(data);
                  });
                });
              } else {
                message.prompt_show('请输入正确的支付金额');
              }
              break;
            case 'ali-pay':
              if (oldValue > 0) {
                message.load_show('正在支付');
                http.post('/trade/scReceipt', {
                  totalFee: oldValue,
                  payChannel: '802',
                  memberId: pageData.memberId,
                  merchantId: pageData.merchantId
                }, function (data) {
                  http.post(data.payUrl, {}, function (data) {
                    message.load_hide();
                    onAlipayJSBridge(data);
                  });
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