/**
 * Created by administrator on 2016/12/13.
 */

// 引入 message http
const message = _require('message');
const http = _require('http');
// 引入时间选择器
const TimePicker = _require('timePicker');
new TimePicker('dateFrom');
new TimePicker('dateTo');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch('list');
// 定义变量
let startDate = document.getElementById('dateFrom');
let endDate = document.getElementById('dateTo');
// 定义变量
let query = document.getElementById('query');
let unfold = document.getElementById('unfold');
let cancel = document.getElementById('cancel');
let submit = document.getElementById('submit');
// 定义初始变量
let nowPage = 1;
let payStatusCode = ['4'];
let payTypeCode = ['H', 'Z', 'B'];
// 定义加载更多
let content = document.getElementById('content');
let list = document.createElement('div');
let more = document.createElement('div');
more.className = 'touch_more';
more.innerHTML = '加载更多';
more.style.display = 'none';
more.style.height = '50px';
more.style.lineHeight = '50px';
more.style.fontSize = '16px';
more.style.color = '#999';
more.style.backgroundColor = '#f0eff5';
more.addEventListener('touchstart', function () {
  more.style.backgroundColor = '#d8d7dc';
});
more.addEventListener('touchend', function () {
  more.style.backgroundColor = '#f0eff5';
});
more.addEventListener('click', function () {
  getData(null, nowPage + 1);
});
content.appendChild(more);
// 定义支付
const payStatus = {
  1: '待支付',
  3: '支付失败',
  4: '支付成功'
};
// 定义支付方式
const payType = {
  S: '微信扫码',
  N: '微信二维码',
  H: '微信扫码', //微信H5收银台
  B: '快捷收款',
  Z: '支付宝扫码'
};
// 定义ajax事件
let getData = function (e, page) {
  // 检查page 如果page为1 清空content数据
  nowPage = page || 1;
  if (nowPage == 1) {
    list.innerHTML = '';
  }
  http.post('/trade/queryMerchantPayOrders', {
    pageNo: nowPage,
    pageSize: 20,
    orderNo: '',  //交易订单号
    payStatus: payStatusCode,   //支付状态（1：待支付，3：支付失败，4：支付成功）
    payType: payTypeCode,    //支付方式
    startDate: startDate.value,
    endDate: endDate.value
  }, function (res) {
    for (let i = 0; i < res.records.length; i++) {
      let group = document.createElement('a');
      group.href = '/sqb/tradeDetail/' + res.records[i].orderId;
      group.className = 'group';
      let top = document.createElement('div');
      top.className = 'top';
      let topLeft = document.createElement('div');
      topLeft.className = 'left';
      if (res.records[i].payStatus == '4') {
        topLeft.innerHTML = payStatus[res.records[i].payStatus] + '-' + payType[res.records[i].payType]
      } else {
        topLeft.innerHTML = payStatus[res.records[i].payStatus];
      }
      let topRight = document.createElement('div');
      topRight.className = 'right';
      topRight.innerHTML = '+' + res.records[i].amount + '元';
      top.appendChild(topLeft);
      top.appendChild(topRight);
      let bottom = document.createElement('div');
      bottom.className = 'bottom';
      let bottomLeft = document.createElement('div');
      bottomLeft.className = 'left';
      bottomLeft.innerHTML = res.records[i].payStatusValue;
      let bottomRight = document.createElement('div');
      bottomRight.className = 'right';
      bottomRight.innerHTML = tools.dateFormat('YYYY-MM-DD HH:mm:ss', res.records[i].datetime);
      bottom.appendChild(bottomLeft);
      bottom.appendChild(bottomRight);
      group.appendChild(top);
      group.appendChild(bottom);
      list.appendChild(group);
    }
    // 判断是否还有下一页
    if (res.hasNextPage) {
      more.style.display = 'block';
    } else {
      more.style.display = 'none';
    }
    content.insertBefore(list, content.childNodes[0]);
    // 根据 e 判断是否为 时间改变的change事件
    if (!e) {
      query.className = 'query';
    }
  })
};
// 初始化数据
getData();
startDate.addEventListener('change', getData);
endDate.addEventListener('change', getData);

unfold.addEventListener('click', function () {
  query.className = 'query flexBox flex-box-column active';
});
cancel.addEventListener('click', function () {
  query.className = 'query';
});
// 定义筛选条件
let payVariable = {
  paySuccess: {
    status: true,
    value: 4
  },
  payError: {
    status: false,
    value: 3
  },
  payWait: {
    status: false,
    value: 1
  },
  payWx: {
    status: true,
    value: 'H'
  },
  payAli: {
    status: true,
    value: 'Z'
  },
  payQuick: {
    status: true,
    value: 'B'
  }
};
let li = document.getElementsByClassName('li');
for (let i = 0; i < li.length; i++) {
  li[i].addEventListener('click', function () {
    // 处理变量
    let variable = this.getAttribute('y-variable');
    payVariable[variable]['status'] = !payVariable[variable]['status'];
    // 处理变量唯一性
    if (!payVariable.paySuccess.status && !payVariable.payError.status && !payVariable.payError.status) {
      message.prompt_show('至少选择一种支付状态');
      payVariable[variable]['status'] = !payVariable[variable]['status'];
      return;
    }
    if (!payVariable.payWx.status && !payVariable.payAli.status && !payVariable.payQuick.status) {
      message.prompt_show('至少选择一种支付方式');
      payVariable[variable]['status'] = !payVariable[variable]['status'];
      return;
    }
    // 处理样式
    if (this.className.indexOf('active') > -1) {
      this.className = 'li';
    } else {
      this.className = 'li active';
    }
  })
}
submit.addEventListener('click', function () {
  // 处理请求数据
  payStatusCode = [];
  payTypeCode = [];
  for (let i in payVariable) {
    if (payVariable[i]['status']) {
      if (typeof (payVariable[i]['value']) == 'number') {
        payStatusCode.push(payVariable[i]['value'])
      } else if (typeof (payVariable[i]['value']) == 'string') {
        payTypeCode.push(payVariable[i]['value'])
      }
    }
  }
  // 唤起请求事件
  getData();
});
