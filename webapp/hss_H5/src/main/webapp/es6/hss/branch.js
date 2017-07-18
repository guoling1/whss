/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
const http = _require('http');
const message = _require('message');
const validate = _require('validate');
const browser = _require('browser');
browser.elastic_touch('layer-w-list');
browser.elastic_touch('layer-b-list');
// 定义变量
// const color = document.getElementById('color');
// const logo = document.getElementById('logo');
// 获取屏幕高度 确定搜索栏位置
let client_h = document.body.clientHeight;
// let layer_w = document.getElementById('layer-w');
// let layer_w_list = document.getElementById('layer-w-list');
// let layer_b = document.getElementById('layer-b');
let layer_b_list = document.getElementById('layer-b-list');
// 定义输入框
// let submit = document.getElementById('submit');
// let world = document.getElementById('world');
// let branch = document.getElementById('branch');
let match_ipt = document.getElementById('match_ipt');
// 定义选择框
let p = document.getElementById('p');
let c = document.getElementById('c');
let ct = document.getElementById('ct');

let Provinces = false;
let Citys = false;
let Countrys = false;
let bankList = document.getElementById('cellList');

function getQueryString(name) {
  let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  let r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]);
  return null;
}

function getLocationString() {
  let path = window.location.pathname;
  let index = path.lastIndexOf("\/");
  return path.substring(index + 1, path.length);
}

// 处理 初始化 的过程
let dataStorage = JSON.parse(localStorage.getItem('branch'));
let search = document.getElementById('search');
let searchFun = function (currentPage) {
    if (nowPage == 1) {
        bankList.innerHTML = '';
    }
    http.post('/bankBranch/getBankBranch', {
        contions: document.getElementById('match_ipt').value,
        bankNo: localStorage.getItem('cardNo'),
        districtCode: dataStorage.cityCode,
        pageNo:currentPage,
        pageSize:10
    }, function (data) {
        console.log(data)
        data = JSON.parse(data)
        for (let i = 0; i < data.result.records.length; i++) {
            let weuiCell = document.createElement('div');
            weuiCell.className = 'weui-cell';
            let cellBd = document.createElement('div');
            cellBd.className = 'weui-cell__bd';
            weuiCell.appendChild(cellBd);
            let p = document.createElement('p');
            p.innerHTML = data.result.records[i].branchName
            cellBd.appendChild(p);
            weuiCell.onclick = function () {
                dataStorage.branchCode = data.result.records[i].branchCode;
                dataStorage.branchName = data.result.records[i].branchName;
                localStorage.setItem('branch',JSON.stringify(dataStorage))
                window.location.href = '/sqb/collection'
            }
            /*let div_branch = document.createElement('div');
             div_branch.className = 't';
             div_branch.innerHTML = data[i].branchName;
             div_branch.onclick = function () {
             branch.value = data[i].branchName;
             pageData.branchCode = data[i].branchCode;
             pageData.branchName = data[i].branchName;
             layer_b.style.display = 'none';
             };*/
            // weuiCell.appendChild(bankList);
            bankList.appendChild(weuiCell)
        }
        if (data.result.totalPage != nowPage&&data.result.totalPage!=0) {
            more.style.display = 'block';
        } else {
            more.style.display = 'none';
        }
    })
}
search.addEventListener('click',function (ev) {
    nowPage = 1
    searchFun(1)
})
let writeBranch = document.getElementById('writeBranch');
let nowPage = 1;
let more = document.createElement('div');
more.className = 'touch_more';
more.innerHTML = '加载更多';
more.style.display = 'none';
more.style.textAlign = 'center';
more.style.height = '50px';
more.style.lineHeight = '50px';
more.style.fontSize = '16px';
more.style.color = '#999';
more.style.backgroundColor = '#f4f5f8';
let pathName = window.location.pathname;
more.addEventListener('touchstart', function () {
    more.style.backgroundColor = '#d8d7dc';
});
more.addEventListener('touchend', function () {
    more.style.backgroundColor = '#f4f5f8';
});
more.addEventListener('click', function () {
    nowPage = nowPage+1
    console.log(nowPage)
    searchFun(nowPage);
});
writeBranch.appendChild(more);
