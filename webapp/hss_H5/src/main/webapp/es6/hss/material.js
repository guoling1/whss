/**
 * Created by administrator on 2016/12/9.
 */

// 引入动画模版 处理验证码
const Animation = _require('animation');
const animation = new Animation();
// 引入http message validate
const validate = _require('validate');
const message = _require('message');
const http = _require('http');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch('layer-w-list');
// 引入wx_upload
const Upload = _require('upload');
// 定义变量
const merchantName = document.getElementById('merchantName');
const address = document.getElementById('address');
const bankNo = document.getElementById('bankNo');
const sampleShow = document.getElementById('sampleShow');
const sampleHide = document.getElementById('sampleHide');
const bank = document.getElementById('bank');
const bankShow = document.getElementById('bankShow');
const bankHide = document.getElementById('bankHide');
const bankEnter = document.getElementById('bankEnter');
const username = document.getElementById('username');
const identity = document.getElementById('identity');
const reserveMobile = document.getElementById('reserveMobile');
const code = document.getElementById('code');
const submit = document.getElementById('submit');
const bankText = document.getElementById('bankText');
const skip = document.getElementById('skip');
const branch = document.getElementById('branch');

skip.addEventListener('click', function () {
  window.location.href = '/sqb/wallet?oemNo=' + pageData.oemNo;
});

branch.addEventListener('click',function () {
    if(validate.bankNo(bankNo.value)){
        localStorage.setItem('cardNo',bankNo.value)
        window.location.href = "/sqb/districtSelect?oemNo=" + pageData.oemNo;
    }
})

let branchStorage = JSON.parse(localStorage.getItem('branch'));
if(branchStorage!=null){
    if(branchStorage.branchName!=''){
        document.getElementById('branch').innerHTML = branchStorage.branchName;
        document.getElementById('branch').style.fontSize = '14px';
        document.getElementById('branch').style.color = '#4a5171';
    }
}

let world = document.getElementById('world');
let layer_w = document.getElementById('layer-w');
let layer_w_list = document.getElementById('layer-w-list');
let p = document.getElementById('p');
let c = document.getElementById('c');
let ct = document.getElementById('ct');
let Provinces = false;
let Citys = false;
let Countrys = false;
let provinceCode = '';
let provinceName = '';
let cityCode = '';
let cityName = '';
let countyCode = '';
let countyName = '';
let client_h = document.body.clientHeight;

world.onclick = function () {
  if (!Provinces) {
    // 如果不存在 则去获取数据
    http.post('/district/findAllProvinces', {}, function (data) {
      Provinces = data;
      ProvincesSet();
    })
  } else {
    ProvincesSet();
  }
  layer_w.style.display = 'block';
  let rect_w = layer_w.getBoundingClientRect();
  layer_w.style.height = (client_h - rect_w.top) + 'px';
  layer_w_list.style.height = (client_h - rect_w.top - 100) + 'px';
};

// Provinces 部署事件
let ProvincesSet = function () {
  layer_w_list.innerHTML = '';
  for (let i = 0; i < Provinces.length; i++) {
    let div_Provinces = document.createElement('div');
    div_Provinces.innerHTML = Provinces[i].aname;
    div_Provinces.onclick = function () {
      p.className = '';
      p.innerHTML = Provinces[i].aname;
      provinceCode = Provinces[i].code;
      provinceName = Provinces[i].aname;
      if (!Citys || !Citys[Provinces[i].code]) {
        http.post('/district/findAllCity', {code: Provinces[i].code}, function (data) {
          Citys = {};
          Citys[Provinces[i].code] = data;
          CitysSet(Provinces[i].code);
        })
      } else {
        CitysSet(Provinces[i].code);
      }
    };
    layer_w_list.appendChild(div_Provinces);
  }
  p.className = 'choose';
  p.style.display = 'inline-block';
};

// Citys 部署事件
let CitysSet = function (code) {
  layer_w_list.innerHTML = '';
  for (let i = 0; i < Citys[code].length; i++) {
    let div_Citys = document.createElement('div');
    div_Citys.innerHTML = Citys[code][i].aname;
    div_Citys.onclick = function () {
      c.className = '';
      c.innerHTML = Citys[code][i].aname;
      cityCode = Citys[code][i].code;
      cityName = Citys[code][i].aname;
      if (!Countrys || !Countrys[Citys[code][i].code]) {
        http.post('/district/findAllCounty', {code: Citys[code][i].code}, function (data) {
          Countrys = {};
          Countrys[Citys[code][i].code] = data;
          CountrysSet(Citys[code][i].code);
        })
      } else {
        CountrysSet(Citys[code][i].code);
      }
    };
    layer_w_list.appendChild(div_Citys);
  }
  c.className = 'choose';
  c.style.display = 'inline-block';
};

// Countrys 部署事件
let CountrysSet = function (code) {
  layer_w_list.innerHTML = '';
  for (let i = 0; i < Countrys[code].length; i++) {
    let div_Countrys = document.createElement('div');
    div_Countrys.innerHTML = Countrys[code][i].aname;
    div_Countrys.onclick = function () {
      ct.className = '';
      ct.innerHTML = Countrys[code][i].aname;
      countyCode = Countrys[code][i].code;
      countyName = Countrys[code][i].aname;
      world.value = provinceName + cityName + countyName;
      layer_w.style.display = 'none';
      recordQ();
    };
    layer_w_list.appendChild(div_Countrys);
  }
  ct.className = 'choose';
  ct.style.display = 'inline-block';
};

let upload_bank = new Upload('chooseImage', function () {
  bankText.innerHTML = '图片已上传';
});

// 定义验证码
animation.fourelement({
  url: '/merchantInfo/sendVerifyCode',
  bankName: 'bankNo',
  bankVal: 'bankNo',
  nameName: 'name',
  nameVal: 'username',
  identityName: 'identity',
  identityVal: 'identity',
  phoneName: 'reserveMobile',
  phoneVal: 'reserveMobile',
  btn: 'sendCode'
});

sampleShow.addEventListener('click', function () {
  sampleHide.style.display = 'block';
});

sampleHide.addEventListener('click', function () {
  sampleHide.style.display = 'none';
});

bankShow.addEventListener('click', function () {
  bank.style.display = 'block';
});

bankHide.addEventListener('click', function () {
  bank.style.display = 'none';
});

bankEnter.addEventListener('click', function () {
  bank.style.display = 'none';
});

bankNo.addEventListener('blur', function () {
  validate.bankNo(bankNo.value);
});

submit.addEventListener('click', function () {
  let bankPic = upload_bank.getServerId();
  if (validate.name(merchantName.value, '店铺名称') &&
    validate.empty(provinceCode, '所在地区') &&
    validate.empty(provinceName, '所在地区') &&
    validate.empty(cityCode, '所在地区') &&
    validate.empty(cityName, '所在地区') &&
    validate.empty(countyCode, '所在地区') &&
    validate.empty(countyName, '所在地区') &&
    validate.address(address.value, '详细地址') &&
    validate.joint({
      bankNo: bankNo.value,
      idCard: identity.value,
      phone: reserveMobile.value,
      code: code.value
    }) && validate.empty(bankPic, '银行卡图片')
    && validate.name(username.value, '开户姓名')) {
    http.post('/merchantInfo/save', {
      merchantName: merchantName.value,
      address: address.value,
      bankNo: bankNo.value,
      name: username.value,
      identity: identity.value,
      reserveMobile: reserveMobile.value,
      code: code.value,
      bankPic: bankPic,
      provinceCode: provinceCode,
      provinceName: provinceName,
      cityCode: cityCode,
      cityName: cityName,
      countyCode: countyCode,
      countyName: countyName,
      branchCode:branchStorage.branchCode,
      branchName:branchStorage.branchName,
      districtCode:branchStorage.countyCode
    }, function () {
      localStorage.removeItem('branch');
      window.location.replace("/sqb/addNext?oemNo=" + pageData.oemNo);
    })
  }
});

// 部署 输入框记录事件

merchantName.addEventListener('change', function () {
  localStorage.setItem('merchantName', JSON.stringify({
    markCode: pageData.markCode,
    merchantName: merchantName.value
  }));
});

let recordQ = function () {
  localStorage.setItem('pathQ', JSON.stringify({
    markCode: pageData.markCode,
    provinceCode: provinceCode,
    provinceName: provinceName,
    cityCode: cityCode,
    cityName: cityName,
    countyCode: countyCode,
    countyName: countyName
  }));
};

address.addEventListener('change', function () {
  localStorage.setItem('address', JSON.stringify({
    markCode: pageData.markCode,
    address: address.value
  }));
});

bankNo.addEventListener('change', function () {
  localStorage.setItem('bankNo', JSON.stringify({
    markCode: pageData.markCode,
    bankNo: bankNo.value
  }));
});

username.addEventListener('change', function () {
  localStorage.setItem('username', JSON.stringify({
    markCode: pageData.markCode,
    username: username.value
  }));
});

identity.addEventListener('change', function () {
  localStorage.setItem('identity', JSON.stringify({
    markCode: pageData.markCode,
    identity: identity.value
  }));
});

reserveMobile.addEventListener('change', function () {
  localStorage.setItem('reserveMobile', JSON.stringify({
    markCode: pageData.markCode,
    reserveMobile: reserveMobile.value
  }));
});

// 部署 输入框数据获取事件

if (localStorage.getItem('merchantName')) {
  if (JSON.parse(localStorage.getItem('merchantName')).markCode == pageData.markCode) {
    merchantName.value = JSON.parse(localStorage.getItem('merchantName')).merchantName;
  }
}

if (localStorage.getItem('pathQ')) {
  if (JSON.parse(localStorage.getItem('pathQ')).markCode == pageData.markCode) {
    provinceCode = JSON.parse(localStorage.getItem('pathQ')).provinceCode;
    provinceName = JSON.parse(localStorage.getItem('pathQ')).provinceName;
    cityCode = JSON.parse(localStorage.getItem('pathQ')).cityCode;
    cityName = JSON.parse(localStorage.getItem('pathQ')).cityName;
    countyCode = JSON.parse(localStorage.getItem('pathQ')).countyCode;
    countyName = JSON.parse(localStorage.getItem('pathQ')).countyName;
    world.value = provinceName + cityName + countyName;
  }
}

if (localStorage.getItem('address')) {
  if (JSON.parse(localStorage.getItem('address')).markCode == pageData.markCode) {
    address.value = JSON.parse(localStorage.getItem('address')).address;
  }
}

if (localStorage.getItem('bankNo')) {
  if (JSON.parse(localStorage.getItem('bankNo')).markCode == pageData.markCode) {
    bankNo.value = JSON.parse(localStorage.getItem('bankNo')).bankNo;
  }
}

if (localStorage.getItem('username')) {
  if (JSON.parse(localStorage.getItem('username')).markCode == pageData.markCode) {
    username.value = JSON.parse(localStorage.getItem('username')).username;
  }
}

if (localStorage.getItem('identity')) {
  if (JSON.parse(localStorage.getItem('identity')).markCode == pageData.markCode) {
    identity.value = JSON.parse(localStorage.getItem('identity')).identity;
  }
}

if (localStorage.getItem('reserveMobile')) {
  if (JSON.parse(localStorage.getItem('reserveMobile')).markCode == pageData.markCode) {
    reserveMobile.value = JSON.parse(localStorage.getItem('reserveMobile')).reserveMobile;
  }
}