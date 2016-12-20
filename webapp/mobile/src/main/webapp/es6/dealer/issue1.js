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

$('#issueHead span').click(function () {
  $(this).addClass('active').siblings('span').removeClass('active');
  $('.box div').eq($(this).index()).addClass('show').siblings('div').removeClass('show');
});
$("#mobile").keyup(function () {
  var mobi1e = $("#mobile").val();
  $.post("/dealer/find", { condition: mobi1e }, function (data) {
    var str = '';
    if (data.result.length != 0) {
      document.getElementById('listId').style.display = "block";
    }
    for (var i = 0; i < data.result.length; i++) {
      str += '<li>' + '<span>' + data.result[i].name + '</span>' + '<span>' + data.result[i].mobile + '</span>' + '</li>';
    }
    document.getElementById('listId').innerHTML = str;
    var aLi = document.getElementById('listId').getElementsByTagName('li');
    for (var i = 0; i < aLi.length; i++) {
      aLi[i].index = i;
      aLi[i].onclick = function () {
        $('.content input')[0].value = data.result[this.index].name + " " + data.result[this.index].mobile;
        $('.list').hide();
        $("#dealerId1").val(data.result[this.index].dealerId);
      };
    }
  });
});
$("#mobile").blur(function () {
  var mobi1e = $("#mobile").val();
  $.post("/dealer/find", { condition: mobi1e }, function (data) {
    if (data.result.length != 0) {
      document.getElementById('listId').style.display = "none";
      $('.content input')[0].value = data.result[0].name + " " + data.result[0].mobile;
      $('.list').hide();
      $("#dealerId1").val(data.result[0].dealerId);
    }
  });
});
$("#submit1").click(function () {
  var number1 = $("#number1").val();
  var data = $("#data").val();
  var dealerId1 = $("#dealerId1").val();
  if (parseInt(dealerId1) == 0) {
    prompt_show("请输入二级代理");
    return false;
  }
  if (parseInt(data) >= parseInt(number1) && parseInt(number1) > 0) {
    return true;
  } else {
    prompt_show("剩余可分配个数:" + data + "个");
    return false;
  }
});
$("#submit2").click(function () {
  var number2 = $("#number2").val();
  var data = $("#data").val();
  if (parseInt(data) >= parseInt(number2) && parseInt(number2) > 0) {
    return true;
  } else {
    prompt_show("剩余可分配个数:" + data + "个");
    return false;
  }
});