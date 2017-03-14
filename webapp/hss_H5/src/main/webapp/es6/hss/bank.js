/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
const http = _require('http');
const message = _require('message');
const browser = _require('browser');
browser.elastic_touch();

// 卡bin和颜色一一对应
let bankCardBin = ['ABC', 'BANKCOMM', 'BOC', 'CCB', 'CEB', 'CGB', 'CIB', 'CITIC', 'CMB', 'CMBC', 'ICBC', 'PINGAN', 'PSBC', 'SHBANK'];
let bankLogoColor = ['green', 'blue', 'red', 'blue', 'red', 'red', 'blue', 'red', 'red', 'red', 'red', 'red', 'green', 'red'];

let HasBank = (bin) => {
  for (let i = 0; i < bankCardBin.length; i++) {
    if (bankCardBin[i] == bin) {
      return i;
    }
  }
  return -1;
};

let bankList = document.getElementById('bankList');
let layer = document.getElementById('layer');
let cancel = document.getElementById('cancel');
let enter = document.getElementById('enter');
let bankId = '';

cancel.addEventListener('click', function () {
  layer.style.display = 'none';
});

enter.addEventListener('click', function () {
  http.post('/wx/deleteCreditCard', {
    bankId: bankId
  }, function () {
    getBankList();
    layer.style.display = 'none';
  })
});
// 获取银行卡列表
let getBankList = function () {
  http.post('/wx/myCardList', {}, function (data) {
    bankList.innerHTML = '';
    for (let i = 0; i < data.length; i++) {
      let group = document.createElement('div');
      let index = HasBank(data[i].bankBin);
      if (index != -1) {
        group.className = 'group ' + bankLogoColor[index];
      } else {
        group.className = 'group red';
      }
      // group top-start
      let top = document.createElement('div');
      top.className = 'top';
      let logo = document.createElement('div');
      if (index != -1) {
        logo.className = 'logo ' + data[i].bankBin;
      } else {
        logo.className = 'logo DEFAULT';
      }
      let info = document.createElement('div');
      info.className = 'info';
      let name = document.createElement('div');
      name.className = 'name';
      let span = document.createElement('span');
      span.innerHTML = data[i].bankName;
      name.appendChild(span);
      if (data[i].cardType == 0) {
        let a = document.createElement('a');
        if (index != -1) {
          a.className = 'btn ' + bankLogoColor[index];
        } else {
          a.className = 'btn red';
        }
        if (data[i].hasBranch == 0) {
          a.innerHTML = '补充支行信息';
        } else {
          a.innerHTML = '修改支行信息';
        }
        a.href = '/sqb/bankBranch/' + data[i].bankId;
        name.appendChild(a);
      } else {
        let b = document.createElement('div');
        if (index != -1) {
          b.className = 'btn ' + bankLogoColor[index];
        } else {
          b.className = 'btn red';
        }
        b.innerHTML = '删除';
        b.onclick = function () {
          layer.style.display = 'block';
          bankId = data[i].bankId;
        };
        name.appendChild(b);
      }
      let type = document.createElement('div');
      type.className = 'type';
      if (data[i].cardType == 0) {
        if (data[i].hasBranch == 0) {
          type.innerHTML = '储蓄卡';
        } else {
          type.innerHTML = '储蓄卡 ' + ' | ' + data[i].branchName;
        }
      } else {
        type.innerHTML = '信用卡';
      }
      info.appendChild(name);
      info.appendChild(type);
      top.appendChild(logo);
      top.appendChild(info);
      // group top-end
      // group bottom-start
      let bottom = document.createElement('div');
      bottom.className = 'bottom';
      let XP1 = document.createElement('div');
      XP1.className = 'p';
      XP1.innerHTML = '<div class="bank_x"></div><div class="bank_x"></div><div class="bank_x"></div><div class="bank_x"></div>';
      let XP2 = document.createElement('div');
      XP2.className = 'p';
      XP2.innerHTML = '<div class="bank_x"></div><div class="bank_x"></div><div class="bank_x"></div><div class="bank_x"></div>';
      let XP3 = document.createElement('div');
      XP3.className = 'p';
      XP3.innerHTML = '<div class="bank_x"></div><div class="bank_x"></div><div class="bank_x"></div><div class="bank_x"></div>';
      let p = document.createElement('div');
      p.className = 'p';
      let word = document.createElement('div');
      word.className = 'word';
      word.innerHTML = data[i].bankNo;
      p.appendChild(word);
      let s = document.createElement('div');
      s.className = 's';
      s.innerHTML = data[i].reserveMobile;
      bottom.appendChild(XP1);
      bottom.appendChild(XP2);
      bottom.appendChild(XP3);
      bottom.appendChild(p);
      bottom.appendChild(s);
      // group bottom-end
      group.appendChild(top);
      group.appendChild(bottom);
      if (data[i].cardType == 0) {
        // group small-start
        let small = document.createElement('div');
        small.className = 'small';
        small.innerHTML = '该卡用于收款后的自动结算与余额提现';
        // group small-end
        group.appendChild(small);
      }
      bankList.appendChild(group);
    }
  });
};
getBankList();
