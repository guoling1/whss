/**
 * Created by administrator on 2016/12/8.
 */

// 引入浏览器特性处理
const http = _require('http');
const message = _require('message');
const validate = _require('validate');
const browser = _require('browser');

const submit = document.getElementById('submit');
const branch = document.getElementById('branch');
const branchStorage = JSON.parse(localStorage.getItem('branch'));
submit.addEventListener('click',function () {
    if(branch.value==''){
        message.prompt_show('请填写支行名称');
    }else {
        branchStorage.branchCode="";
        branchStorage.branchName = branch.value;
        localStorage.setItem('branch', JSON.stringify(branchStorage));
        window.location.href = '/sqb/collection?oemNo='+pageData.oemNo;
    }
})


// 处理 初始化 的过程
let dataStorage = JSON.parse(localStorage.getItem('branch'));


