/**
 * Created by wangl on 2016/12/16.
 */

// 引入http message validate
const validate = _require('validate');
const message = _require('message');
const http = _require('http');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();

// 定义变量
let number1 = document.getElementById('number1');
let number2 = document.getElementById('number2');
let number3 = document.getElementById('number3');
let number4 = document.getElementById('number4');
let count1 = document.getElementById('count1');
let count3 = document.getElementById('count3');

// 输入框校验
const check1 = function () {
  return (number1.value.length == 12 && number2.value.length == 12);
};
const check3 = function () {
  return (number3.value.length == 12 && number4.value.length == 12);
};

// 查询有效二维码个数
const query1 = function () {
  if (check1()) {
    http.post('/dealer/findCodeCount', {
      startCode: number1.value,
      endCode: number2.value
    }, function (data) {
      count1.innerHTML = data.count;
    })
  }
};
const query3 = function () {
  if (check3()) {
    http.post('/dealer/findCodeCount', {
      startCode: number3.value,
      endCode: number4.value
    }, function (data) {
      count3.innerHTML = data.count;
    })
  }
};

// 查询剩余个数
number1.addEventListener('input', query1);
number2.addEventListener('input', query1);
number3.addEventListener('input', query3);
number4.addEventListener('input', query3);

// teb切换
let title = document.getElementById('title');
let ownTitle = document.getElementById('ownTitle');
let content = document.getElementById('content');
let ownContent = document.getElementById('ownContent');

title.addEventListener('click', function () {
  title.className = 'active';
  ownTitle.className = '';
  content.className = 'content show';
  ownContent.className = 'ownContent';
});

ownTitle.addEventListener('click', function () {
  title.className = '';
  ownTitle.className = 'active';
  content.className = 'content';
  ownContent.className = 'ownContent show';
});

// 二级代理模糊匹配
let mobile = document.getElementById('mobile');
let listId = document.getElementById('listId');
let dealerId1 = document.getElementById('dealerId1');
mobile.addEventListener('input', function () {
  let val = mobile.value;
  if (validate.empty(val)) {
    http.post_form('/dealer/find', {
      condition: val
    }, function (data) {
      listId.innerHTML = '';
      if (data.length != 0) {
        listId.style.display = "block";
        for (let i = 0; i < data.length; i++) {
          let li = document.createElement('li');
          li.addEventListener('click', function () {
            listId.style.display = "none";
            mobile.value = data[i].name + ' ' + data[i].mobile;
            dealerId1.value = data[i].dealerId;
          });
          let c_name = document.createElement('span');
          c_name.innerHTML = data[i].name;
          let c_mobile = document.createElement('span');
          c_mobile.innerHTML = data[i].mobile;
          li.appendChild(c_name);
          li.appendChild(c_mobile);
          listId.appendChild(li);
        }
      } else {
        listId.style.display = "none";
      }
    })
  }
});

// 提交前的校验
let submit1 = document.getElementById('submit1');
let form1 = document.getElementById('form1');
submit1.addEventListener('click', function () {
  if (!validate.empty(dealerId1.value, '二级代理商')) {
    return false;
  }
  if (count1.innerHTML > 0) {
    form1.submit();
  } else {
    message.prompt_show('请输入正确的分配码段');
    return false;
  }
});

let submit2 = document.getElementById('submit2');
let form2 = document.getElementById('form2');
submit2.addEventListener('click', function () {
  if (count3.innerHTML > 0) {
    form2.submit();
  } else {
    message.prompt_show('请输入正确的分配码段');
    return false;
  }
});