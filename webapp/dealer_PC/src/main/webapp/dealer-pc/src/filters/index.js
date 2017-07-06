/**
 * Created by administrator on 16/11/17.
 */

import Vue from 'vue'

Vue.filter('datetime', function (date) {
  let format = 'YYYY-MM-DD HH:mm:ss';
  // 若时间为空,则返回当前时间
  if (!date || date == '') {
    return '--';
  }
  date = date / 1;
  date = new Date(date);
  // 匹配年份
  if (/(Y+)/i.test(format)) {
    format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  // 匹配小时
  let dateHours = '';
  if (/(H+)/.test(format)) {
    dateHours = date.getHours();
    format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? dateHours : ("00" + dateHours).substr(("" + dateHours).length));
  }
  // 匹配 月份 日期 分钟 秒钟 季度 毫秒
  const getDate = {
    "M+": date.getMonth() + 1,
    "D+": date.getDate(),
    "m+": date.getMinutes(),
    "s+": date.getSeconds(),
    "q+": Math.floor((date.getMonth() + 3) / 3),
    "S+": date.getMilliseconds()
  };
  for (let k in getDate) {
    if (new RegExp("(" + k + ")").test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? getDate[k] : ("00" + getDate[k]).substr(("" + getDate[k]).length));
    }
  }
  return format;
});
Vue.filter('changeDate', function (val) {
  if (val == '' || val == null) {
    return ''
  } else {
    val = new Date(val);
    let year = val.getFullYear();
    let month = val.getMonth() + 1;
    let date = val.getDate();

    function tod(a) {
      if (a < 10) {
        a = "0" + a
      }
      return a;
    }

    return year + "-" + tod(month) + "-" + tod(date) ;
  }
});

Vue.filter('filter_businessType', function (value) {
  const encryption = {
    hssPay: '好收收-收款',
    hssWithdraw: '好收收-提现',
    hssPromote: '好收收-升级费',
    hsyPay: '好收银-收款',
  };
  return encryption[value];
});

Vue.filter('filter_qrcodeType', function (value) {
  const encryption = {
    1: '实体码',
    2: '电子码'
  };
  return encryption[value];
});

Vue.filter('filter_amount', function (value) {
  if (value == 0) {
    return '--'
  }
  return value;
});

Vue.filter('filter_toFix', function (value) {
  if (value != null) {
    return parseFloat(value).toFixed(2);
  } else {
    return value
  }
});
