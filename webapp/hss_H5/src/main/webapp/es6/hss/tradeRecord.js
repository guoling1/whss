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
// 引入分页插件
const Paging = _require('paging');
new Paging('trade_record', {
  content: 'content',
  url: '/trade/queryMerchantPayOrders',
  size: 20,
  startDate: 'dateFrom',
  endDate: 'dateTo'
});
// 定义变量
let query = document.getElementById('query');
let unfold = document.getElementById('unfold');
let cancel = document.getElementById('cancel');
let submit = document.getElementById('submit');
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
      console.log(payVariable);
      return;
    }
    if (!payVariable.payWx.status && !payVariable.payAli.status && !payVariable.payQuick.status) {
      message.prompt_show('至少选择一种支付方式');
      payVariable[variable]['status'] = !payVariable[variable]['status'];
      console.log(payVariable);
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
// 定义传输变量
let payStatus = ['4'];
let payType = ['H', 'Z', 'B'];
submit.addEventListener('click', function () {
  // 处理请求数据
  payStatus = [];
  payType = [];
  for (let i in payVariable) {
    if (payVariable[i]['status']) {
      if (typeof (payVariable[i]['value']) == 'number') {
        payStatus.push(payVariable[i]['value'])
      } else if (typeof (payVariable[i]['value']) == 'string') {
        payType.push(payVariable[i]['value'])
      }
    }
  }
  // 唤起请求事件
  http.post('/trade/queryMerchantPayOrders', {
    pageNo: 1,
    pageSize: 20,
    orderNo: '',  //交易订单号
    payStatus: payStatus,   //支付状态（1：待支付，3：支付失败，4：支付成功）
    payType: payType,    //支付方式
    startDate: '',
    endDate: ''
  }, function (data) {
    console.log(data);
  })
});
