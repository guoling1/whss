/**
 * Created by administrator on 2016/12/13.
 */

_require.register("paging", (module, exports, _require, global)=> {

  // 引入http tools
  const tools = _require('tools');
  const http = _require('http');
  /* 数据列表分页
   * 支持加载更多分页 */
  class Paging {
    constructor(type, config) {
      this[type](config);
      console.log('Paging 构造成功...');
    }

    // 调用以下方法处理数据

    // 商户端交易记录的查询
    trade_record(config) {
      // 定义变量
      let content = document.getElementById(config.content);
      let list = document.createElement('div');
      // 定义加载更多
      let nowPage = 1;
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
      let startDate = document.getElementById(config.startDate);
      let endDate = document.getElementById(config.endDate);
      // 定义支付结果
      const payResult = {
        N: '等待支付',
        H: '支付中',
        A: '支付已受理',
        S: '支付成功',
        F: '支付失败'
      };
      // 定义提现结果
      const walResult = {
        N: '提现中',
        H: '提现中',
        A: '提现中',
        S: '提现成功',
        F: '提现失败',
        W: '请求已受理',
        E: '提现异常'
      };
      // 定义支付方式
      const payChannel = {
        101: '扫码支付',
        103: '快捷支付'
      };
      // 定义ajax事件
      let getData = function (e, page) {
        // 检查page 如果page为1 清空content数据
        nowPage = page || 1;
        if (nowPage == 1) {
          list.innerHTML = '';
        }
        http.post(config.url, {
          page: nowPage,
          size: config.size,
          startDate: startDate.value,
          endDate: endDate.value
        }, function (res) {
          for (let i = 0; i < res.records.length; i++) {
            let group = document.createElement('a');
            if (res.records[i].tradeType == 0) {
              group.href = '/sqb/tradeDetail/' + res.records[i].id;
            } else {
              group.href = '/sqb/otherTradeDetail/' + res.records[i].id;
            }
            group.className = 'group';
            let top = document.createElement('div');
            top.className = 'top';
            let topLeft = document.createElement('div');
            topLeft.className = 'left';
            if (res.records[i].tradeType == 0) {
              topLeft.innerHTML = res.records[i].body + '-' + payChannel[res.records[i].payChannel];
            } else {
              topLeft.innerHTML = res.records[i].body;
            }
            let topRight = document.createElement('div');
            topRight.className = 'right';
            if (res.records[i].tradeType == 0) {
              topRight.innerHTML = '+' + res.records[i].totalFee + '元';
            } else {
              topRight.innerHTML = '-' + res.records[i].totalFee + '元';
            }
            top.appendChild(topLeft);
            top.appendChild(topRight);
            let bottom = document.createElement('div');
            bottom.className = 'bottom';
            let bottomLeft = document.createElement('div');
            bottomLeft.className = 'left';
            bottomLeft.innerHTML = res.records[i].errorMessage;
            let bottomRight = document.createElement('div');
            bottomRight.className = 'right';
            bottomRight.innerHTML = tools.dateFormat('YYYY-MM-DD HH:mm:ss', res.records[i].createTime);
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
        })
      };
      // 初始化数据
      getData();
      startDate.addEventListener('change', getData);
      endDate.addEventListener('change', getData);
    }
  }
  module.exports = Paging;
});