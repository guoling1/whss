/**
 * Created by administrator on 2016/12/13.
 */

_require.register("tools", (module, exports, _require, global)=> {

  /* Tools 基础工具类*/
  class Tools {
    constructor() {
      console.log('tools 构建成功...')
    }

    //转换时间格式 默认输出中文
    dateFormat(format, date, language) {
      // 若时间为空,则返回当前时间
      date = date ? new Date(date) : new Date();
      // 匹配年份
      if (/(Y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
      }
      // 匹配星期? 支持 中文 英文 英文缩写
      var wkArray = {
        'CHN': ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
        'ENG': ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        'AB_ENG': ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
      };
      if (/(wk)/.test(format)) {
        format = format.replace(RegExp.$1, wkArray[language][date.getDay()]);
      }
      /* 匹配小时
       * 支持 12/24 小时制
       * 支持 中文 英文 英文缩写
       * */
      var apArray = {
        'CHN': ['上午', '下午'],
        'ENG': ['AM', 'PM'],
        'AB_ENG': ['AM', 'PM']
      };
      var dateHours = '',
        apKey = '';
      if (/(H+)/.test(format)) {
        dateHours = date.getHours();
        format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? dateHours : ("00" + dateHours).substr(("" + dateHours).length));
      }
      if (/(h+)/.test(format)) {
        dateHours = date.getHours() >= 12 ? date.getHours() - 12 : date.getHours();
        apKey = date.getHours() >= 12 ? 1 : 0;
        format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? dateHours : ("00" + dateHours).substr(("" + dateHours).length));
        if (/(ap)/.test(format)) {
          format = format.replace(RegExp.$1, apArray[language][apKey]);
        }
      }
      // 匹配 月份 日期 分钟 秒钟 季度 毫秒
      var getDate = {
        "M+": date.getMonth() + 1,
        "D+": date.getDate(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
        "q+": Math.floor((date.getMonth() + 3) / 3),
        "S+": date.getMilliseconds()
      };
      for (var k in getDate) {
        if (new RegExp("(" + k + ")").test(format)) {
          format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? getDate[k] : ("00" + getDate[k]).substr(("" + getDate[k]).length));
        }
      }
      return format;
    };

    NumToFix(num, f = 2) {
      return parseFloat(num).toFixed(f);
    }

    GetUrlArg() {
      let url = location.search; //获取url中"?"符后的字串
      let theRequest = {};
      if (url.indexOf("?") != -1) {
        let str = url.substr(1);
        let strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
          theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
        }
      }
      return theRequest;
    }
  }
  module.exports = new Tools();
});