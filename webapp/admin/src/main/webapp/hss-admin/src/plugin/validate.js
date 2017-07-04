/**
 * Created by administrator on 16/11/22.
 */

import Store from '../store';

export default{
  install(Vue){
    const validate = {
      joint: function (obj) {
        // 联合校验 {方法名:值}
        for (let method in obj) {
          // 多值数组校验
          if (typeof (obj[method]) == 'object') {
            for (let m = 0; m < obj[method].length; m++) {
              // 联合校验时 名称校验必须用data来传
              if (method == 'name') {
                if (!this[method](obj[method][m]['data'],obj[method][m]['text'])) {
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
          Store.commit('MESSAGE_ACCORD_SHOW', {
            text: (promptWords ? promptWords : '名称') + '长度限制1-15个字'
          });
          return false;
        }
        return true;
      },
      address: function (name) {
        // 详细地址 做长度校验 最多35个字
        if (!(/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,35}$/.test(name))) {
          Store.commit('MESSAGE_ACCORD_SHOW', {
            text: '地址长度限制1-35个字'
          });
          return false;
        }
        return true;
      },
      phone: function (phone) {
        if (!(/^1(3|4|5|7|8)\d{9}$/.test(phone))) {
          Store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请输入正确的手机号码'
          });
          return false;
        }
        return true;
      },
      code: function (code) {
        if (code.length != 6) {
          Store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请输入正确的验证码'
          });
          return false;
        }
        return true;
      },
      bankNo: function (bankNo) {
        if (!(/^(\d{16}|\d{19})$/.test(bankNo))) {
          Store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请输入正确的银行卡号'
          });
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
                Store.commit('MESSAGE_ACCORD_SHOW', {
                  text: '请输入正确的身份证号'
                });
              }
            } else {
              //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
              if (idCardLast == idCardY[idCardMod]) {
                return true;
              } else {
                Store.commit('MESSAGE_ACCORD_SHOW', {
                  text: '请输入正确的身份证号'
                });
              }
            }
          }
        } else {
          Store.commit('MESSAGE_ACCORD_SHOW', {
            text: '请输入正确的身份证号'
          });
        }
      }
    };

    Vue.__validate = validate;
    Vue.prototype._$validate = validate;
  }
}
