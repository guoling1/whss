/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
const http = _require('http');
const message = _require('message');
const browser = _require('browser');
browser.elastic_touch('layer-w-list');
browser.elastic_touch('layer-b-list');
// 定义变量
const color = document.getElementById('color');
const logo = document.getElementById('logo');
// 获取屏幕高度 确定搜索栏位置
let client_h = document.body.clientHeight;
let layer_w = document.getElementById('layer-w');
let layer_w_list = document.getElementById('layer-w-list');
let layer_b = document.getElementById('layer-b');
let layer_b_list = document.getElementById('layer-b-list');
// 定义输入框
let submit = document.getElementById('submit');
let world = document.getElementById('world');
let branch = document.getElementById('branch');
let match_ipt = document.getElementById('match_ipt');
// 定义选择框
let p = document.getElementById('p');
let c = document.getElementById('c');
let ct = document.getElementById('ct');

let Provinces = false;
let Citys = false;
let Countrys = false;

let layer = document.getElementById('layer');
let cancel = document.getElementById('cancel');

cancel.addEventListener('click', function () {
  window.location.href = '/sqb/wallet';
});

function getQueryString(name) {
  let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
}

function getLocationString() {
  let path = window.location.href;
  let index = path.lastIndexOf("\/");
  return path.substring(index + 1, path.length);
}

// 处理 初始化 的过程
if (pageData.provinceCode != '' &&
  pageData.provinceName != '' &&
  pageData.cityCode != '' &&
  pageData.cityName != '' &&
  pageData.countyCode != '' &&
  pageData.countyName != '' &&
  pageData.branchCode != '' &&
  pageData.branchName != '') {
  world.value = pageData.provinceName + pageData.cityName + pageData.countyName;
  branch.value = pageData.branchName;
}


submit.onclick = function () {
  http.post('/wx/branchInfo', {
    bankId: getLocationString(),
    branchCode: pageData.branchCode,
    branchName: pageData.branchName,
    provinceCode: pageData.provinceCode,
    provinceName: pageData.provinceName,
    cityCode: pageData.cityCode,
    cityName: pageData.cityName,
    countyCode: pageData.countyCode,
    countyName: pageData.countyName
  }, function () {
    if (getQueryString('card')) {
      window.location.href = '/sqb/creditCardAuthen?card=true';
    } else if (getQueryString('branch')) {
      layer.style.display = 'block';
    } else {
      window.location.href = '/sqb/bank';
    }
  })
};

branch.onclick = function () {
  if (!pageData.countyName || pageData.countyName == '') {
    message.prompt_show('请先选择支行地区');
    return false;
  }
  layer_b.style.display = 'block';
  let rect_b = layer_b.getBoundingClientRect();
  layer_b.style.height = (client_h - rect_b.top) + 'px';
  layer_b_list.style.height = (client_h - rect_b.top - 50) + 'px';
  match_ipt.focus();
};

match_ipt.addEventListener('input', function (e) {
  let ev = e.target;
  if (/[\u4e00-\u9fa5]{2,50}/.test(ev.value)) {
    http.post('/bankBranch/getBankBranch', {
      contions: ev.value,
      provinceName: pageData.provinceName,
      cityName: pageData.cityName
    }, function (data) {
      layer_b_list.innerHTML = '';
      console.log(data);
      for (let i = 0; i < data.length; i++) {
        let div_branch = document.createElement('div');
        div_branch.className = 't';
        div_branch.innerHTML = data[i].branchName;
        div_branch.onclick = function () {
          branch.value = data[i].branchName;
          pageData.branchCode = data[i].branchCode;
          pageData.branchName = data[i].branchName;
          layer_b.style.display = 'none';
        };
        layer_b_list.appendChild(div_branch);
      }
    })
  }
});

world.onclick = function () {
  layer_b.style.display = 'none';
  if (!Provinces) {
    // 如果不存在 则去获取数据
    http.post('/district/findAllProvinces', {}, function (data) {
      Provinces = data;
      ProvincesSet();
    })
  } else {
    ProvincesSet();
  }
  branch.value = '';
  layer_b_list.innerHTML = '';
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
      pageData.provinceCode = Provinces[i].code;
      pageData.provinceName = Provinces[i].aname;
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
      pageData.cityCode = Citys[code][i].code;
      pageData.cityName = Citys[code][i].aname;
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
      pageData.countyCode = Countrys[code][i].code;
      pageData.countyName = Countrys[code][i].aname;
      world.value = pageData.provinceName + pageData.cityName + pageData.countyName;
      layer_w.style.display = 'none';
    };
    layer_w_list.appendChild(div_Countrys);
  }
  ct.className = 'choose';
  ct.style.display = 'inline-block';
};

p.onclick = function () {
  c.style.display = 'none';
  ct.style.display = 'none';
  ProvincesSet();
};

c.onclick = function () {
  ct.style.display = 'none';
  CitysSet(pageData.provinceCode);
};

ct.onclick = function () {
  CitysSet(pageData.cityCode);
};

// 卡bin和颜色一一对应
let bankCardBin = ['ABC', 'BANKCOMM', 'BOC', 'CCB', 'CEB', 'CGB', 'CIB', 'CITIC', 'CMB', 'CMBC', 'ICBC', 'PINGAN', 'PSBC', 'SHBANK'];
let bankLogoColor = ['green', 'blue', 'red', 'blue', 'red', 'red', 'blue', 'red', 'red', 'red', 'red', 'red', 'green', 'red'];

let HasBank = () => {
  for (let i = 0; i < bankCardBin.length; i++) {
    if (bankCardBin[i] == pageData.bin) {
      return i;
    }
  }
  return -1;
};

let index = HasBank();

if (index != -1) {
  color.className = 'group ' + bankLogoColor[index];
  logo.className = 'logo ' + pageData.bin;
} else {
  color.className = 'group red';
  logo.className = 'logo DEFAULT';
}