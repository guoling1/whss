/**
 * Created by administrator on 2016/12/5.
 */

function validate() {
  return {
    joint: function (obj) {
      // 联合校验 {方法名:值}
      for (var method in obj) {
        // 多值数组校验
        if (typeof (obj[method]) == 'object') {
          for (var m = 0; m < obj[method].length; m++) {
            // 联合校验时 长度校验必须用data来传
            if (method == 'length') {
              if (!this[method](obj[method][m]['data'], obj[method][m]['min'], obj[method][m]['max'], obj[method][m]['text'])) {
                return false;
                break;
              }
            } else {
              if (!this[method](obj[method][m])) {
                return false;
                break;
              }
            }
          }
        } else {
          // 单值直接校验
          if (!this[method](obj[method])) {
            return false;
            break;
          }
        }
      }
      return true;
    },
    name: function (name, promptWords) {
      // 商户名称 和 用户姓名 做长度校验 最多15个字
      if (!(/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,15}$/.test(name))) {
        prompt_show((promptWords ? promptWords : '名称') + '长度限制1-15个字');
        return false;
      }
      return true;
    },
    address: function (name) {
      // 详细地址 做长度校验 最多35个字
      if (!(/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,35}$/.test(name))) {
        prompt_show('地址长度限制1-35个字');
        return false;
      }
      return true;
    },
    phone: function (phone) {
      if (!(/^1(3|4|5|7|8)\d{9}$/.test(phone))) {
        prompt_show('请输入正确的手机号码');
        return false;
      }
      return true;
    },
    code: function (code) {
      if (code.length != 6) {
        prompt_show('请输入正确的验证码');
        return false;
      }
      return true;
    },
    bankNo: function (bankNo) {
      if (!(/^(\d{16}|\d{19})$/.test(bankNo))) {
        prompt_show('请输入正确的银行卡号');
        return false;
      }
      return true;
    },
    idCard: function validateIdCard(idCard) {
      //15位和18位身份证号码的正则表达式
      var regIdCard = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
      //如果通过该验证，说明身份证格式正确，但准确性还需计算
      if (regIdCard.test(idCard)) {
        if (idCard.length == 18) {
          var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); //将前17位加权因子保存在数组里
          var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); //这是除以11后，可能产生的11位余数、验证码，也保存成数组
          var idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
          for (var i = 0; i < 17; i++) {
            idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];
          }
          var idCardMod = idCardWiSum % 11;//计算出校验码所在数组的位置
          var idCardLast = idCard.substring(17);//得到最后一位身份证号码
          //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
          if (idCardMod == 2) {
            if (idCardLast == "X" || idCardLast == "x") {
              return true;
            } else {
              prompt_show('请输入正确的身份证号');
            }
          } else {
            //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
            if (idCardLast == idCardY[idCardMod]) {
              return true;
            } else {
              prompt_show('请输入正确的身份证号');
            }
          }
        }
      } else {
        prompt_show('请输入正确的身份证号');
      }
    }
  }
}

function keyboard(el, type) {
  // 初始化指令设置,获取配置参数
  // 创建input
  var input = document.createElement('input');
  input.type = 'text';
  input.style.textAlign = 'right';
  // 设置输入框不可 readonly
  input.setAttribute("readOnly", 'true');
  // 此处设置input的默认样式
  input.style.width = '100%';
  input.style.padding = '5px 5px';
  input.style.border = '1px solid #41c573';
  input.style.borderRadius = '5px';
  input.style.fontSize = '45px';
  input.style.fontWeight = 'bold';
  input.style.color = '#4b526d';
  // 设置input的点击事件
  input.onclick = function () {
    console.log('唤起键盘');
  };
  // input value 的设置函数 ¥
  input.value = '¥';
  var setValue = function (val) {
    if (val == 'delete') {
      // 删除
      if (input.value.length > 1) {
        input.value = input.value.substr(0, input.value.length - 1);
      }
    } else {
      // 输入金额超过5万 提示最大金额为50000
      var valiAmount = input.value.substr(1) + val;
      if (valiAmount > 50000) {
        input.value = '¥50000';
        prompt_show('付款金额不能超过50000');
        return false;
      }
      // 校验小数位 小数点
      if (input.value.indexOf('.') != -1) {
        var pointPosition = input.value.indexOf('.');
        var strLength = input.value.length;
        // 小数位数最多2位
        if (strLength - pointPosition > 2) {
          return false;
        }
        // 已经有小数的禁止输入小数点
        if (val == '.') {
          return false;
        }
      }
      // 第一位直接输入小数点或者0 直接填充为 0.
      if (input.value.length == 1 && (val == '0' || val == '.')) {
        input.value += '0.';
        return false;
      }
      // 添加
      input.value += val;
    }
  };
  // 将input添加到el下
  el.appendChild(input);

  // 创建键盘div
  var keyboard = document.createElement('div');
  // 此处设置keyboard的默认样式
  keyboard.style.width = '100%';
  keyboard.style.height = '280px';
  keyboard.style.position = 'fixed';
  keyboard.style.left = '0';
  keyboard.style.bottom = '0';
  keyboard.style.borderTop = '1px solid #e8eaf3';
  keyboard.style.backgroundColor = '#FFF';
  /* 设置点击唤起自定义键盘
   * 键盘默认状态 唤起/隐藏
   * 键盘是否可隐藏 是/否*/

  // 定义按钮默认样式
  var btnStyleText = 'width:33.3%;height:70px;font-size:27px;color:#5a5d6b;float:left;text-align:center;line-height:70px;border-bottom:1px solid #e8eaf3;border-right:1px solid #e8eaf3';

  // 定义键盘左侧数字按钮
  var keyLeft = document.createElement('div');
  keyLeft.style.float = 'left';
  keyLeft.style.width = '75%';
  // 定义数字按键 点击事件
  var key1 = document.createElement('div');
  key1.innerHTML = '1';
  key1.style.cssText = btnStyleText;
  key1.addEventListener('touchend', function () {
    setValue('1');
  });
  var key2 = document.createElement('div');
  key2.innerHTML = '2';
  key2.style.cssText = btnStyleText;
  key2.addEventListener('touchend', function () {
    setValue('2');
  });
  var key3 = document.createElement('div');
  key3.innerHTML = '3';
  key3.style.cssText = btnStyleText;
  key3.addEventListener('touchend', function () {
    setValue('3');
  });
  var key4 = document.createElement('div');
  key4.innerHTML = '4';
  key4.style.cssText = btnStyleText;
  key4.addEventListener('touchend', function () {
    setValue('4');
  });
  var key5 = document.createElement('div');
  key5.innerHTML = '5';
  key5.style.cssText = btnStyleText;
  key5.addEventListener('touchend', function () {
    setValue('5');
  });
  var key6 = document.createElement('div');
  key6.innerHTML = '6';
  key6.style.cssText = btnStyleText;
  key6.addEventListener('touchend', function () {
    setValue('6');
  });
  var key7 = document.createElement('div');
  key7.innerHTML = '7';
  key7.style.cssText = btnStyleText;
  key7.addEventListener('touchend', function () {
    setValue('7');
  });
  var key8 = document.createElement('div');
  key8.innerHTML = '8';
  key8.style.cssText = btnStyleText;
  key8.addEventListener('touchend', function () {
    setValue('8');
  });
  var key9 = document.createElement('div');
  key9.innerHTML = '9';
  key9.style.cssText = btnStyleText;
  key9.addEventListener('touchend', function () {
    setValue('9');
  });
  var keyPoint = document.createElement('div');
  keyPoint.innerHTML = '.';
  keyPoint.style.cssText = btnStyleText;
  keyPoint.style.backgroundColor = '#f4f5f8';
  keyPoint.addEventListener('touchend', function () {
    setValue('.');
  });
  var key0 = document.createElement('div');
  key0.innerHTML = '0';
  key0.style.cssText = btnStyleText;
  key0.addEventListener('touchend', function () {
    setValue('0');
  });
  var keyDelete = document.createElement('div');
  keyDelete.style.cssText = btnStyleText;
  keyDelete.style.backgroundImage = 'url(/static/del.png)';
  keyDelete.style.backgroundRepeat = 'no-repeat';
  keyDelete.style.backgroundPosition = 'center';
  keyDelete.style.backgroundSize = '27px 20px';
  keyDelete.style.backgroundColor = '#f4f5f8';
  keyDelete.addEventListener('touchend', function () {
    setValue('delete');
  });
  // 按需求顺序添加键盘按键
  keyLeft.appendChild(key1);
  keyLeft.appendChild(key2);
  keyLeft.appendChild(key3);
  keyLeft.appendChild(key4);
  keyLeft.appendChild(key5);
  keyLeft.appendChild(key6);
  keyLeft.appendChild(key7);
  keyLeft.appendChild(key8);
  keyLeft.appendChild(key9);
  keyLeft.appendChild(keyPoint);
  keyLeft.appendChild(key0);
  keyLeft.appendChild(keyDelete);
  // 将左侧键盘添加进keyboard
  keyboard.appendChild(keyLeft);

  // 定键盘右侧选择按钮
  var keyRight = document.createElement('div');
  keyRight.style.float = 'right';
  keyRight.style.width = '25%';

  switch (type) {
    case 'QR':
      // 定义按钮默认样式
      var payStyleText = 'width:100%;height:45px;text-align:center;line-height:45px;font-size:12px;color:#5a5d6b';

      // 定义支付按钮
      var payQR1 = document.createElement('div');
      payQR1.style.height = '140px';
      payQR1.style.overflow = 'hidden';
      payQR1.style.borderBottom = '1px solid #e8eaf3';
      payQR1.onclick = function () {
        var amount = input.value.substr(1);
        if (!!amount && amount != 0) {
          var param = {
            totalFee: amount,//收款金额（小数点后两位，单位元）
            payChannel: '103'//101.微信扫码102.支付宝扫码103.银联
          };
          var newParam = JSON.stringify(param);
          $.ajax({
            url: "/wx/receipt",
            type: "post",
            contentType: "application/json",
            dataType: "json",
            data: newParam,
            success: function (r) {
              if (r.code == 1) {
                window.location.href = r.result.payUrl;
              } else if (r.code == -2) {
                window.location.href = "/sqb/reg";
              } else {
                prompt_show(r.msg);
              }
            },
            error: function () {
              prompt_show("系统异常");
            }
          });
        } else {
          prompt_show("请输入正确的付款金额");
        }
      };
      // 定义pay logo
      var payQR1Logo = document.createElement('div');
      payQR1Logo.style.width = '26px';
      payQR1Logo.style.height = '20px';
      payQR1Logo.style.margin = '40px auto 0';
      payQR1Logo.style.backgroundImage = 'url(/static/card.png)';
      payQR1Logo.style.backgroundRepeat = 'no-repeat';
      payQR1Logo.style.backgroundPosition = 'center';
      payQR1Logo.style.backgroundSize = '26px 20px';
      payQR1.appendChild(payQR1Logo);
      // 定义pay word
      var payQRText = document.createElement('div');
      payQRText.innerHTML = "无卡快捷";
      payQRText.style.cssText = payStyleText;
      payQR1.appendChild(payQRText);
      var payQR2 = document.createElement('div');
      payQR2.style.height = '140px';
      payQR2.onclick = function () {
        var amount = input.value.substr(1);
        if (!!amount && amount != 0) {
          var param = {
            totalFee: amount,//收款金额（小数点后两位，单位元）
            payChannel: '101'//101.微信扫码102.支付宝扫码103.银联
          };
          var newParam = JSON.stringify(param);
          $.ajax({
            url: "/wx/receipt",
            type: "post",
            contentType: "application/json",
            dataType: "json",
            data: newParam,
            success: function (r) {
              if (r.code == 1) {
                window.location.href = "/sqb/charge?qrCode=" + encodeURIComponent(r.result.payUrl) + "&name=" + r.result.subMerName + "&money=" + r.result.amount;
              } else if (r.code == -2) {
                window.location.href = "/sqb/reg";
              } else {
                prompt_show(r.msg);
              }
            },
            error: function () {
              prompt_show("系统异常");
            }
          });
        } else {
          prompt_show("请输入正确的付款金额");
        }
      };
      // 定义pay logo
      var payQR2Logo1 = document.createElement('div');
      payQR2Logo1.style.width = '26px';
      payQR2Logo1.style.height = '24px';
      payQR2Logo1.style.margin = '20px auto 0';
      payQR2Logo1.style.backgroundImage = 'url(/static/wx.png)';
      payQR2Logo1.style.backgroundRepeat = 'no-repeat';
      payQR2Logo1.style.backgroundPosition = 'center';
      payQR2Logo1.style.backgroundSize = '26px 24px';
      var payQR2Logo2 = document.createElement('div');
      payQR2Logo2.style.width = '25px';
      payQR2Logo2.style.height = '25px';
      payQR2Logo2.style.margin = '10px auto 0';
      payQR2Logo2.style.backgroundImage = 'url(/static/zfb.png)';
      payQR2Logo2.style.backgroundRepeat = 'no-repeat';
      payQR2Logo2.style.backgroundPosition = 'center';
      payQR2Logo2.style.backgroundSize = '25px 25px';
      payQR2.appendChild(payQR2Logo1);
      payQR2.appendChild(payQR2Logo2);
      // 定义pay word
      var payQR2Text = document.createElement('div');
      payQR2Text.innerHTML = "收款码";
      payQR2Text.style.cssText = payStyleText;
      payQR2.appendChild(payQR2Text);

      keyRight.appendChild(payQR1);
      keyRight.appendChild(payQR2);
      break;
    case 'weixinpay':
      var payWX1 = document.createElement('div');
      payWX1.style.height = '100px';
      payWX1.style.overflow = 'hidden';
      payWX1.style.borderBottom = '1px solid #e8eaf3';
      // 定义pay logo
      var payWX1Logo = document.createElement('div');
      payWX1Logo.style.width = '26px';
      payWX1Logo.style.height = '24px';
      payWX1Logo.style.margin = '30px auto 10px';
      payWX1Logo.style.backgroundImage = 'url(/static/wx.png)';
      payWX1Logo.style.backgroundRepeat = 'no-repeat';
      payWX1Logo.style.backgroundPosition = 'center';
      payWX1Logo.style.backgroundSize = '26px 24px';
      payWX1.appendChild(payWX1Logo);
      // 定义pay word
      var payWX1Text = document.createElement('div');
      payWX1Text.innerHTML = "微信支付";
      payWX1Text.style.fontSize = "12px";
      payWX1Text.style.color = "#52c67e";
      payWX1.appendChild(payWX1Text);

      var payWX2 = document.createElement('div');
      payWX2.style.height = '180px';
      payWX2.style.lineHeight = '180px';
      payWX2.style.overflow = 'hidden';
      payWX2.onclick = function () {
        var amount = input.value.substr(1);
        var mid = $("#mid").text().trim();
        if (!!amount && amount != 0) {
          $.ajax({
            type: "post",
            url: "/wx/receiptByCode",
            data: JSON.stringify({totalFee: amount, payChannel: "101", merchantId: mid}),
            dataType: "json",
            contentType: "application/json",
            success: function (r) {
              if (r.code == 1) {
                window.location.href = r.result.payUrl;
              } else if (r.code == -2) {
                window.location.href = "/sqb/reg";
              } else {
                prompt_show(r.msg);
              }
            },
            error: function () {
              prompt_show("系统异常");
            }
          });
          //Vue.http.post('/wx/receiptByCode', {
          //  totalFee: amount, //收款金额（小数点后两位，单位元）
          //  merchantId: '101' //101.微信扫码102.支付宝扫码103.银联
          //})
        } else {
          prompt_show("请输入正确的付款金额");
        }
      };
      // 定义pay word
      var payWX2Text = document.createElement('div');
      payWX2Text.innerHTML = "付款";
      payWX2Text.style.height = '180px';
      payWX2Text.style.lineHeight = '180px';
      payWX2Text.style.fontSize = "16px";
      payWX2Text.style.color = "#FFF";
      payWX2Text.style.backgroundColor = "#5fd68c";
      payWX2.appendChild(payWX2Text);

      keyRight.appendChild(payWX1);
      keyRight.appendChild(payWX2);
      break;
    case 'alipay':
      var payZFB1 = document.createElement('div');
      payZFB1.style.height = '100px';
      payZFB1.style.overflow = 'hidden';
      payZFB1.style.borderBottom = '1px solid #e8eaf3';
      // 定义pay logo
      var payZFB1Logo = document.createElement('div');
      payZFB1Logo.style.width = '25px';
      payZFB1Logo.style.height = '25px';
      payZFB1Logo.style.margin = '30px auto 10px';
      payZFB1Logo.style.backgroundImage = 'url(/static/zfb.png)';
      payZFB1Logo.style.backgroundRepeat = 'no-repeat';
      payZFB1Logo.style.backgroundPosition = 'center';
      payZFB1Logo.style.backgroundSize = '25px 25px';
      payZFB1.appendChild(payZFB1Logo);
      // 定义pay word
      var payZFB1Text = document.createElement('div');
      payZFB1Text.innerHTML = "支付宝支付";
      payZFB1Text.style.fontSize = "12px";
      payZFB1Text.style.color = "#4e9bf9";
      payZFB1.appendChild(payZFB1Text);

      var payZFB2 = document.createElement('div');
      payZFB2.style.height = '180px';
      payZFB2.style.lineHeight = '180px';
      payZFB2.style.overflow = 'hidden';
      payZFB2.onclick = function () {
        var amount = input.value.substr(1);
        var mid = $("#mid").text().trim();
        if (!!amount && amount != 0) {
          $.ajax({
            type: "post",
            url: "/wx/receiptByCode",
            data: JSON.stringify({totalFee: amount, payChannel: "102", merchantId: mid}),
            dataType: "json",
            contentType: "application/json",
            success: function (r) {
              if (r.code == 1) {
                window.location.href = r.result.payUrl;
              } else if (r.code == -2) {
                window.location.href = "/sqb/reg";
              } else {
                prompt_show(r.msg);
              }
            },
            error: function () {
              prompt_show("系统异常");
            }
          });
          //Vue.http.post('/wx/receiptByCode', {
          //  totalFee: amount, //收款金额（小数点后两位，单位元）
          //  merchantId: '101' //101.微信扫码102.支付宝扫码103.银联
          //})
        } else {
          prompt_show("请输入正确的付款金额");
        }
      };
      // 定义pay word
      var payZFB2Text = document.createElement('div');
      payZFB2Text.innerHTML = "付款";
      payZFB2Text.style.height = '180px';
      payZFB2Text.style.lineHeight = '180px';
      payZFB2Text.style.fontSize = "16px";
      payZFB2Text.style.color = "#FFF";
      payZFB2Text.style.backgroundColor = "#4e9bf9";
      payZFB2.appendChild(payZFB2Text);

      keyRight.appendChild(payZFB1);
      keyRight.appendChild(payZFB2);
      break;
  }

  // 将左侧键盘添加进keyboard
  keyboard.appendChild(keyRight);

  // 将keyboard添加到el下
  el.appendChild(keyboard);
}

