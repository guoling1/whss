/**
 * Created by administrator on 2016/12/6.
 */

_require.register("validate", (module, exports, _require, global)=> {
  // 调用message
  const message = _require('message');
  const http = _require('http');
  // 表单校验类
  class Validate {
    // 构造方法
    constructor() {

      // 定义卡bin存储队列 仅在当前页面生效
      this.cardBin = {};

      console.log('validate 构造成功...');
    }

    joint(obj) {
      // 联合校验 {方法名:值}
      for (var method in obj) {
        // 多值数组校验
        if (typeof (obj[method]) == 'object') {
          for (var m = 0; m < obj[method].length; m++) {
            // 联合校验时 名称校验必须用data来传
            if (method == 'length') {
              if (!this[method](obj[method][m]['data'], obj[method][m]['min'], obj[method][m]['max'], obj[method][m]['text'])) {
                return false;
                break;
              }
            } else if (method == 'empty') {
              if (!this[method](obj[method][m]['data'], obj[method][m]['text'])) {
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
    }

    empty(val, promptWords) {
      if (!val || val == '') {
        message.prompt_show(promptWords + '不能为空');
        return false;
      }
      return true;
    }

    length(val, min, max, promptWords) {
      // 商户名称 和 用户姓名 做长度校验 最多15个字
      if (!("/^[\u4e00-\u9fa5_a-zA-Z0-9]{" + min + "," + max + "}$/".test(val))) {
        message.prompt_show((promptWords ? promptWords : '名称') + '长度限制' + min + '-' + max + '个字');
        return false;
      }
      return true;
    }

    phone(phone) {
      if (!(/^1(3|4|5|7|8)\d{9}$/.test(phone))) {
        message.prompt_show('请输入正确的手机号码');
        return false;
      }
      return true;
    }

    code(code) {
      if (code.length != 6) {
        message.prompt_show('请输入正确的验证码');
        return false;
      }
      return true;
    }

    bankNo(bankNo) {
      if (this.cardBin[bankNo]) {
        return true;
      }
      if (!(/^(\d{16}|\d{19})$/.test(bankNo))) {
        message.prompt_show('请输入正确的银行卡号');
        return false;
      }
      let res = http.syncPost('/merchantInfo/queryBank', {bankNo: bankNo});
      res = JSON.parse(res);
      if (res.code != 1 || res.result != 0) {
        message.prompt_show('仅支持绑定储蓄卡');
        return false;
      }
      this.cardBin[bankNo] = true;
      return true;
    }

    idCard(idCard) {
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
              message.prompt_show('请输入正确的身份证号');
            }
          } else {
            //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
            if (idCardLast == idCardY[idCardMod]) {
              return true;
            } else {
              message.prompt_show('请输入正确的身份证号');
            }
          }
        }
      } else {
        message.prompt_show('请输入正确的身份证号');
      }
    }
  }
  module.exports = new Validate();
});