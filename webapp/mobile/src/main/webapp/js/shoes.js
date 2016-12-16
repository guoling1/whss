'use strict';

/*
 * 以下为模块化加载的基础代码
 * copy 自 阮一峰 教程
 * 第三方包 直接标签引入
 * */

/* 提供全局变量
 * 内置的全局变量必须加前缀 __ */
var global = {
  __allow: true // 全局允许 主要用于防止二次点击
};

function _require(p) {
  var path = _require.resolve(p);
  var mod = _require.modules[path];
  if (!mod) throw new Error('failed to require "' + p + '"');
  if (!mod.exports) {
    mod.exports = {};
    mod.call(mod.exports, mod, mod.exports, _require.relative(path), global);
  }
  return mod.exports;
}

_require.modules = {};

_require.resolve = function (path) {
  var orig = path;
  var reg = path + '.js';
  var index = path + '/index.js';
  return _require.modules[reg] && reg || _require.modules[index] && index || orig;
};

_require.register = function (path, fn) {
  _require.modules[path] = fn;
};

_require.relative = function (parent) {
  return function (p) {
    if ('.' != p.charAt(0)) return _require(p);
    var path = parent.split('/');
    var segs = p.split('/');
    path.pop();

    for (var i = 0; i < segs.length; i++) {
      var seg = segs[i];
      if ('..' == seg) path.pop();else if ('.' != seg) path.push(seg);
    }

    return _require(path.join('/'));
  };
};
'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/13.
 */

_require.register("tools", function (module, exports, _require, global) {

  /* Tools 基础工具类*/
  var Tools = function () {
    function Tools() {
      _classCallCheck(this, Tools);

      console.log('tools 构建成功...');
    }

    //转换时间格式 默认输出中文


    _createClass(Tools, [{
      key: 'dateFormat',
      value: function dateFormat(format, date, language) {
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
      }
    }]);

    return Tools;
  }();

  module.exports = new Tools();
});
'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/8.
 */

_require.register("message", function (module, exports, _require, global) {
  /*
   * 全局的提示信息类
   * 每个页面必须引用 兼职数据重置的方法*/
  var Message = function () {
    function Message() {
      _classCallCheck(this, Message);

      // 重置数据
      global.__allow = true;
      // 创建提示信息div prompt
      var body = document.body;
      this.prompt = document.createElement('div');
      this.prompt.id = 'prompt';
      this.prompt.style.display = 'none';
      this.prompt.style.width = 'auto';
      this.prompt.style.height = 'auto';
      this.prompt.style.fontSize = '16px';
      this.prompt.style.color = '#FFF';
      this.prompt.style.position = 'fixed';
      this.prompt.style.top = '40%';
      this.prompt.style.left = '50%';
      this.prompt.style.zIndex = '19999';
      this.prompt.style.transform = 'translate3d(-50%,-50%,0)';
      this.prompt.style.padding = '5px 10px';
      this.prompt.style.backgroundColor = '#373737';
      this.prompt.style.borderRadius = '5px';
      body.appendChild(this.prompt);
      // 创建微信load提示 weui
      this.load = document.createElement('div');
      this.load.id = 'loadingToast';
      this.load.style.opacity = '0';
      this.load.style.display = 'none';
      var load_msak = document.createElement('div');
      load_msak.className = 'weui-mask_transparent';
      var load_toast = document.createElement('div');
      load_toast.className = 'weui-toast';
      var load_i = document.createElement('i');
      load_i.className = 'weui-loading weui-icon_toast';
      this.load_p = document.createElement('p');
      this.load_p.className = 'weui-toast__content';
      this.load_p.innerHTML = '正在加载';
      load_toast.appendChild(load_i);
      load_toast.appendChild(this.load_p);
      this.load.appendChild(load_msak);
      this.load.appendChild(load_toast);
      body.appendChild(this.load);
      // 创建微信toast提示
      this.toast = document.createElement('div');
      this.toast.id = 'toast';
      this.toast.style.opacity = '0';
      this.toast.style.display = 'none';
      var toast_msak = document.createElement('div');
      toast_msak.className = 'weui-mask_transparent';
      var toast_toast = document.createElement('div');
      toast_toast.className = 'weui-toast';
      var toast_i = document.createElement('i');
      toast_i.className = 'weui-icon-success-no-circle weui-icon_toast';
      this.toast_p = document.createElement('p');
      this.toast_p.className = 'weui-toast__content';
      this.toast_p.innerHTML = '已完成';
      toast_toast.appendChild(toast_i);
      toast_toast.appendChild(this.toast_p);
      this.toast.appendChild(toast_msak);
      this.toast.appendChild(toast_toast);
      body.appendChild(this.toast);
      // 定义 定时器
      this.timer = '';
      console.log('message 构造成功...');
    }

    _createClass(Message, [{
      key: 'prompt_show',
      value: function prompt_show(text) {
        var _this = this;

        clearTimeout(this.timer);
        this.prompt.innerHTML = text;
        this.prompt.style.display = 'block';
        this.timer = setTimeout(function () {
          _this.prompt.style.display = 'none';
        }, 1000);
      }

      // 微信 weui 样式

    }, {
      key: 'load_show',
      value: function load_show(text) {
        this.load_p.innerHTML = text || '正在加载';
        this.load.style.opacity = '1';
        this.load.style.display = 'block';
      }
    }, {
      key: 'load_hide',
      value: function load_hide() {
        this.load.style.opacity = '0';
        this.load.style.display = 'none';
      }
    }, {
      key: 'toast_show',
      value: function toast_show(text) {
        this.toast_p.innerHTML = text || '已完成';
        this.toast.style.opacity = '1';
        this.toast.style.display = 'block';
      }
    }]);

    return Message;
  }();

  module.exports = new Message();
});
"use strict";

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/*
 * 基础 模块化 加载 end*/

_require.register("http", function (module, exports, _require, global) {
  // 调用message
  var message = _require('message');
  /* http类
   * 任意请求完成时 都会将 global.__allow 设置为 true
   * 任意请求开始时 都会将 global.__allow 设置为 false
   *
   * 异步请求 全局统一错误处理 只有code为1时正确
   * 同步请求 需要单独 转换json 处理错误*/

  var Http = function () {
    function Http() {
      _classCallCheck(this, Http);

      this.onchange = function (xmlhttp, fun) {
        xmlhttp.onreadystatechange = function () {
          if (this.readyState == 4) {
            // 收到请求时 设置全局允许
            global.__allow = true;
            if (this.status == 200) {
              try {
                var response = eval("(" + this.responseText + ")");
                if (response.code == -2) {
                  window.location.href = '/sqb/reg';
                  return;
                } else if (response.code != 1) {
                  message.load_hide();
                  message.prompt_show(response.msg || '抱歉!服务器异常');
                  return;
                }
                fun(response.result);
              } catch (e) {
                fun(this.responseText);
              }
            } else if (this.status == 500) {
              message.prompt_show("抱歉!服务器异常");
            }
          }
        };
      };
      this.creathttp = function () {
        if (window.XMLHttpRequest) {
          return new XMLHttpRequest();
        } else {
          return new ActiveXObject("Microsoft.XMLHTTP");
        }
      };
    }

    _createClass(Http, [{
      key: "get",
      value: function get(url, fun) {
        if (global.__allow) {
          global.__allow = false;
          var xmlhttp = this.creathttp();
          this.onchange(xmlhttp, fun);
          xmlhttp.open("get", url, true);
          xmlhttp.send();
        }
      }
    }, {
      key: "post",
      value: function post(url, data, fun) {
        if (global.__allow) {
          global.__allow = false;
          var xmlhttp = this.creathttp();
          this.onchange(xmlhttp, fun);
          xmlhttp.open("post", url, true);
          xmlhttp.setRequestHeader("Content-type", "application/json");
          xmlhttp.send(JSON.stringify(data));
        }
      }
    }, {
      key: "syncGet",
      value: function syncGet(url) {
        var xmlhttp = this.creathttp();
        xmlhttp.open("get", url, false);
        xmlhttp.send();
        return xmlhttp.responseText;
      }
    }, {
      key: "syncPost",
      value: function syncPost(url, data) {
        var xmlhttp = this.creathttp();
        xmlhttp.open("post", url, false);
        xmlhttp.setRequestHeader("Content-type", "application/json");
        xmlhttp.send(JSON.stringify(data));
        return xmlhttp.responseText;
      }
    }, {
      key: "formatData",
      value: function formatData(data) {
        var rs = "";
        for (var m in data) {
          rs += m + "=" + data[m] + "&";
        }
        return rs.substring(0, rs.length - 1);
      }
    }]);

    return Http;
  }();

  module.exports = new Http();
});
'use strict';

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/6.
 */

_require.register("validate", function (module, exports, _require, global) {
  // 调用message
  var message = _require('message');
  var http = _require('http');
  // 表单校验类

  var Validate = function () {
    // 构造方法
    function Validate() {
      _classCallCheck(this, Validate);

      // 定义卡bin存储队列 仅在当前页面生效
      this.cardBin = {};

      console.log('validate 构造成功...');
    }

    _createClass(Validate, [{
      key: 'joint',
      value: function joint(obj) {
        // 联合校验 {方法名:值}
        for (var method in obj) {
          // 多值数组校验
          if (_typeof(obj[method]) == 'object') {
            for (var m = 0; m < obj[method].length; m++) {
              if (!this[method](obj[method][m])) {
                return false;
                break;
              }
            }
          } else {
            // 单值直接校验
            if (!this[method](obj[method])) {
              return false;
              break;
            }
          }
        }
        return true;
      }
    }, {
      key: 'empty',
      value: function empty(val, promptWords) {
        if (!val || val == '') {
          message.prompt_show(promptWords + '不能为空');
          return false;
        }
        return true;
      }
    }, {
      key: 'name',
      value: function name(val, promptWords) {
        // 商户名称 和 用户姓名 做长度校验 最多15个字
        if (!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,15}$/.test(val)) {
          message.prompt_show((promptWords ? promptWords : '名称') + '长度限制1-15个字');
          return false;
        }
        return true;
      }
    }, {
      key: 'address',
      value: function address(val, promptWords) {
        // 地址 做长度校验 最多35个字
        if (!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,35}$/.test(val)) {
          message.prompt_show((promptWords ? promptWords : '名称') + '长度限制1-35个字');
          return false;
        }
        return true;
      }
    }, {
      key: 'phone',
      value: function phone(_phone) {
        if (!/^1(3|4|5|7|8)\d{9}$/.test(_phone)) {
          message.prompt_show('请输入正确的手机号码');
          return false;
        }
        return true;
      }
    }, {
      key: 'code',
      value: function code(_code) {
        if (_code.length != 6) {
          message.prompt_show('请输入正确的验证码');
          return false;
        }
        return true;
      }
    }, {
      key: 'bankNo',
      value: function bankNo(_bankNo) {
        if (this.cardBin[_bankNo]) {
          return true;
        }
        if (!/^(\d{16}|\d{19})$/.test(_bankNo)) {
          message.prompt_show('请输入正确的银行卡号');
          return false;
        }
        var res = http.syncPost('/merchantInfo/queryBank', { bankNo: _bankNo });
        res = JSON.parse(res);
        if (res.code != 1 || res.result != 0) {
          message.prompt_show('仅支持绑定储蓄卡');
          return false;
        }
        this.cardBin[_bankNo] = true;
        return true;
      }
    }, {
      key: 'idCard',
      value: function idCard(_idCard) {
        //15位和18位身份证号码的正则表达式
        var regIdCard = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;
        //如果通过该验证，说明身份证格式正确，但准确性还需计算
        if (regIdCard.test(_idCard)) {
          if (_idCard.length == 18) {
            var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); //将前17位加权因子保存在数组里
            var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); //这是除以11后，可能产生的11位余数、验证码，也保存成数组
            var idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
            for (var i = 0; i < 17; i++) {
              idCardWiSum += _idCard.substring(i, i + 1) * idCardWi[i];
            }
            var idCardMod = idCardWiSum % 11; //计算出校验码所在数组的位置
            var idCardLast = _idCard.substring(17); //得到最后一位身份证号码
            //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
            if (idCardMod == 2) {
              if (idCardLast == "X" || idCardLast == "x") {
                return true;
              } else {
                message.prompt_show('请输入正确的身份证号');
              }
            } else {
              //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
              if (idCardLast == idCardY[idCardMod]) {
                return true;
              } else {
                message.prompt_show('请输入正确的身份证号');
              }
            }
          }
        } else {
          message.prompt_show('请输入正确的身份证号');
        }
      }
    }]);

    return Validate;
  }();

  module.exports = new Validate();
});
'use strict';

/*
 * 基础 模块化 加载 end*/

_require.register("fastclick", function (module, exports, _require, global) {
  /**
   * @preserve FastClick: polyfill to remove click delays on browsers with touch UIs.
   *
   * @codingstandard ftlabs-jsv2
   * @copyright The Financial Times Limited [All Rights Reserved]
   * @license MIT License (see LICENSE.txt)
   */

  /*jslint browser:true, node:true*/
  /*global define, Event, Node*/

  /**
   * Instantiate fast-clicking listeners on the specified layer.
   *
   * @constructor
   * @param {Element} layer The layer to listen on
   * @param {Object} [options={}] The options to override the defaults
   */
  function FastClick(layer, options) {
    var oldOnClick;

    options = options || {};

    /**
     * Whether a click is currently being tracked.
     *
     * @type boolean
     */
    this.trackingClick = false;

    /**
     * Timestamp for when click tracking started.
     *
     * @type number
     */
    this.trackingClickStart = 0;

    /**
     * The element being tracked for a click.
     *
     * @type EventTarget
     */
    this.targetElement = null;

    /**
     * X-coordinate of touch start event.
     *
     * @type number
     */
    this.touchStartX = 0;

    /**
     * Y-coordinate of touch start event.
     *
     * @type number
     */
    this.touchStartY = 0;

    /**
     * ID of the last touch, retrieved from Touch.identifier.
     *
     * @type number
     */
    this.lastTouchIdentifier = 0;

    /**
     * Touchmove boundary, beyond which a click will be cancelled.
     *
     * @type number
     */
    this.touchBoundary = options.touchBoundary || 10;

    /**
     * The FastClick layer.
     *
     * @type Element
     */
    this.layer = layer;

    /**
     * The minimum time between tap(touchstart and touchend) events
     *
     * @type number
     */
    this.tapDelay = options.tapDelay || 200;

    /**
     * The maximum time for a tap
     *
     * @type number
     */
    this.tapTimeout = options.tapTimeout || 700;

    if (FastClick.notNeeded(layer)) {
      return;
    }

    // Some old versions of Android don't have Function.prototype.bind
    function bind(method, context) {
      return function () {
        return method.apply(context, arguments);
      };
    }

    var methods = ['onMouse', 'onClick', 'onTouchStart', 'onTouchMove', 'onTouchEnd', 'onTouchCancel'];
    var context = this;
    for (var i = 0, l = methods.length; i < l; i++) {
      context[methods[i]] = bind(context[methods[i]], context);
    }

    // Set up event handlers as required
    if (deviceIsAndroid) {
      layer.addEventListener('mouseover', this.onMouse, true);
      layer.addEventListener('mousedown', this.onMouse, true);
      layer.addEventListener('mouseup', this.onMouse, true);
    }

    layer.addEventListener('click', this.onClick, true);
    layer.addEventListener('touchstart', this.onTouchStart, false);
    layer.addEventListener('touchmove', this.onTouchMove, false);
    layer.addEventListener('touchend', this.onTouchEnd, false);
    layer.addEventListener('touchcancel', this.onTouchCancel, false);

    // Hack is required for browsers that don't support Event#stopImmediatePropagation (e.g. Android 2)
    // which is how FastClick normally stops click events bubbling to callbacks registered on the FastClick
    // layer when they are cancelled.
    if (!Event.prototype.stopImmediatePropagation) {
      layer.removeEventListener = function (type, callback, capture) {
        var rmv = Node.prototype.removeEventListener;
        if (type === 'click') {
          rmv.call(layer, type, callback.hijacked || callback, capture);
        } else {
          rmv.call(layer, type, callback, capture);
        }
      };

      layer.addEventListener = function (type, callback, capture) {
        var adv = Node.prototype.addEventListener;
        if (type === 'click') {
          adv.call(layer, type, callback.hijacked || (callback.hijacked = function (event) {
            if (!event.propagationStopped) {
              callback(event);
            }
          }), capture);
        } else {
          adv.call(layer, type, callback, capture);
        }
      };
    }

    // If a handler is already declared in the element's onclick attribute, it will be fired before
    // FastClick's onClick handler. Fix this by pulling out the user-defined handler function and
    // adding it as listener.
    if (typeof layer.onclick === 'function') {

      // Android browser on at least 3.2 requires a new reference to the function in layer.onclick
      // - the old one won't work if passed to addEventListener directly.
      oldOnClick = layer.onclick;
      layer.addEventListener('click', function (event) {
        oldOnClick(event);
      }, false);
      layer.onclick = null;
    }
  }

  /**
   * Windows Phone 8.1 fakes user agent string to look like Android and iPhone.
   *
   * @type boolean
   */
  var deviceIsWindowsPhone = navigator.userAgent.indexOf("Windows Phone") >= 0;

  /**
   * Android requires exceptions.
   *
   * @type boolean
   */
  var deviceIsAndroid = navigator.userAgent.indexOf('Android') > 0 && !deviceIsWindowsPhone;

  /**
   * iOS requires exceptions.
   *
   * @type boolean
   */
  var deviceIsIOS = /iP(ad|hone|od)/.test(navigator.userAgent) && !deviceIsWindowsPhone;

  /**
   * iOS 4 requires an exception for select elements.
   *
   * @type boolean
   */
  var deviceIsIOS4 = deviceIsIOS && /OS 4_\d(_\d)?/.test(navigator.userAgent);

  /**
   * iOS 6.0-7.* requires the target element to be manually derived
   *
   * @type boolean
   */
  var deviceIsIOSWithBadTarget = deviceIsIOS && /OS [6-7]_\d/.test(navigator.userAgent);

  /**
   * BlackBerry requires exceptions.
   *
   * @type boolean
   */
  var deviceIsBlackBerry10 = navigator.userAgent.indexOf('BB10') > 0;

  /**
   * Determine whether a given element requires a native click.
   *
   * @param {EventTarget|Element} target Target DOM element
   * @returns {boolean} Returns true if the element needs a native click
   */
  FastClick.prototype.needsClick = function (target) {
    switch (target.nodeName.toLowerCase()) {

      // Don't send a synthetic click to disabled inputs (issue #62)
      case 'button':
      case 'select':
      case 'textarea':
        if (target.disabled) {
          return true;
        }

        break;
      case 'input':

        // File inputs need real clicks on iOS 6 due to a browser bug (issue #68)
        if (deviceIsIOS && target.type === 'file' || target.disabled) {
          return true;
        }

        break;
      case 'label':
      case 'iframe': // iOS8 homescreen apps can prevent events bubbling into frames
      case 'video':
        return true;
    }

    return (/\bneedsclick\b/.test(target.className)
    );
  };

  /**
   * Determine whether a given element requires a call to focus to simulate click into element.
   *
   * @param {EventTarget|Element} target Target DOM element
   * @returns {boolean} Returns true if the element requires a call to focus to simulate native click.
   */
  FastClick.prototype.needsFocus = function (target) {
    switch (target.nodeName.toLowerCase()) {
      case 'textarea':
        return true;
      case 'select':
        return !deviceIsAndroid;
      case 'input':
        switch (target.type) {
          case 'button':
          case 'checkbox':
          case 'file':
          case 'image':
          case 'radio':
          case 'submit':
            return false;
        }

        // No point in attempting to focus disabled inputs
        return !target.disabled && !target.readOnly;
      default:
        return (/\bneedsfocus\b/.test(target.className)
        );
    }
  };

  /**
   * Send a click event to the specified element.
   *
   * @param {EventTarget|Element} targetElement
   * @param {Event} event
   */
  FastClick.prototype.sendClick = function (targetElement, event) {
    var clickEvent, touch;

    // On some Android devices activeElement needs to be blurred otherwise the synthetic click will have no effect (#24)
    if (document.activeElement && document.activeElement !== targetElement) {
      document.activeElement.blur();
    }

    touch = event.changedTouches[0];

    // Synthesise a click event, with an extra attribute so it can be tracked
    clickEvent = document.createEvent('MouseEvents');
    clickEvent.initMouseEvent(this.determineEventType(targetElement), true, true, window, 1, touch.screenX, touch.screenY, touch.clientX, touch.clientY, false, false, false, false, 0, null);
    clickEvent.forwardedTouchEvent = true;
    targetElement.dispatchEvent(clickEvent);
  };

  FastClick.prototype.determineEventType = function (targetElement) {

    //Issue #159: Android Chrome Select Box does not open with a synthetic click event
    if (deviceIsAndroid && targetElement.tagName.toLowerCase() === 'select') {
      return 'mousedown';
    }

    return 'click';
  };

  /**
   * @param {EventTarget|Element} targetElement
   */
  FastClick.prototype.focus = function (targetElement) {
    var length;

    // Issue #160: on iOS 7, some input elements (e.g. date datetime month) throw a vague TypeError on setSelectionRange. These elements don't have an integer value for the selectionStart and selectionEnd properties, but unfortunately that can't be used for detection because accessing the properties also throws a TypeError. Just check the type instead. Filed as Apple bug #15122724.
    if (deviceIsIOS && targetElement.setSelectionRange && targetElement.type.indexOf('date') !== 0 && targetElement.type !== 'time' && targetElement.type !== 'month') {
      length = targetElement.value.length;
      targetElement.setSelectionRange(length, length);
    } else {
      targetElement.focus();
    }
  };

  /**
   * Check whether the given target element is a child of a scrollable layer and if so, set a flag on it.
   *
   * @param {EventTarget|Element} targetElement
   */
  FastClick.prototype.updateScrollParent = function (targetElement) {
    var scrollParent, parentElement;

    scrollParent = targetElement.fastClickScrollParent;

    // Attempt to discover whether the target element is contained within a scrollable layer. Re-check if the
    // target element was moved to another parent.
    if (!scrollParent || !scrollParent.contains(targetElement)) {
      parentElement = targetElement;
      do {
        if (parentElement.scrollHeight > parentElement.offsetHeight) {
          scrollParent = parentElement;
          targetElement.fastClickScrollParent = parentElement;
          break;
        }

        parentElement = parentElement.parentElement;
      } while (parentElement);
    }

    // Always update the scroll top tracker if possible.
    if (scrollParent) {
      scrollParent.fastClickLastScrollTop = scrollParent.scrollTop;
    }
  };

  /**
   * @param {EventTarget} targetElement
   * @returns {Element|EventTarget}
   */
  FastClick.prototype.getTargetElementFromEventTarget = function (eventTarget) {

    // On some older browsers (notably Safari on iOS 4.1 - see issue #56) the event target may be a text node.
    if (eventTarget.nodeType === Node.TEXT_NODE) {
      return eventTarget.parentNode;
    }

    return eventTarget;
  };

  /**
   * On touch start, record the position and scroll offset.
   *
   * @param {Event} event
   * @returns {boolean}
   */
  FastClick.prototype.onTouchStart = function (event) {
    var targetElement, touch, selection;

    // Ignore multiple touches, otherwise pinch-to-zoom is prevented if both fingers are on the FastClick element (issue #111).
    if (event.targetTouches.length > 1) {
      return true;
    }

    targetElement = this.getTargetElementFromEventTarget(event.target);
    touch = event.targetTouches[0];

    if (deviceIsIOS) {

      // Only trusted events will deselect text on iOS (issue #49)
      selection = window.getSelection();
      if (selection.rangeCount && !selection.isCollapsed) {
        return true;
      }

      if (!deviceIsIOS4) {

        // Weird things happen on iOS when an alert or confirm dialog is opened from a click event callback (issue #23):
        // when the user next taps anywhere else on the page, new touchstart and touchend events are dispatched
        // with the same identifier as the touch event that previously triggered the click that triggered the alert.
        // Sadly, there is an issue on iOS 4 that causes some normal touch events to have the same identifier as an
        // immediately preceeding touch event (issue #52), so this fix is unavailable on that platform.
        // Issue 120: touch.identifier is 0 when Chrome dev tools 'Emulate touch events' is set with an iOS device UA string,
        // which causes all touch events to be ignored. As this block only applies to iOS, and iOS identifiers are always long,
        // random integers, it's safe to to continue if the identifier is 0 here.
        if (touch.identifier && touch.identifier === this.lastTouchIdentifier) {
          event.preventDefault();
          return false;
        }

        this.lastTouchIdentifier = touch.identifier;

        // If the target element is a child of a scrollable layer (using -webkit-overflow-scrolling: touch) and:
        // 1) the user does a fling scroll on the scrollable layer
        // 2) the user stops the fling scroll with another tap
        // then the event.target of the last 'touchend' event will be the element that was under the user's finger
        // when the fling scroll was started, causing FastClick to send a click event to that layer - unless a check
        // is made to ensure that a parent layer was not scrolled before sending a synthetic click (issue #42).
        this.updateScrollParent(targetElement);
      }
    }

    this.trackingClick = true;
    this.trackingClickStart = event.timeStamp;
    this.targetElement = targetElement;

    this.touchStartX = touch.pageX;
    this.touchStartY = touch.pageY;

    // Prevent phantom clicks on fast double-tap (issue #36)
    if (event.timeStamp - this.lastClickTime < this.tapDelay) {
      event.preventDefault();
    }

    return true;
  };

  /**
   * Based on a touchmove event object, check whether the touch has moved past a boundary since it started.
   *
   * @param {Event} event
   * @returns {boolean}
   */
  FastClick.prototype.touchHasMoved = function (event) {
    var touch = event.changedTouches[0],
        boundary = this.touchBoundary;

    if (Math.abs(touch.pageX - this.touchStartX) > boundary || Math.abs(touch.pageY - this.touchStartY) > boundary) {
      return true;
    }

    return false;
  };

  /**
   * Update the last position.
   *
   * @param {Event} event
   * @returns {boolean}
   */
  FastClick.prototype.onTouchMove = function (event) {
    if (!this.trackingClick) {
      return true;
    }

    // If the touch has moved, cancel the click tracking
    if (this.targetElement !== this.getTargetElementFromEventTarget(event.target) || this.touchHasMoved(event)) {
      this.trackingClick = false;
      this.targetElement = null;
    }

    return true;
  };

  /**
   * Attempt to find the labelled control for the given label element.
   *
   * @param {EventTarget|HTMLLabelElement} labelElement
   * @returns {Element|null}
   */
  FastClick.prototype.findControl = function (labelElement) {

    // Fast path for newer browsers supporting the HTML5 control attribute
    if (labelElement.control !== undefined) {
      return labelElement.control;
    }

    // All browsers under test that support touch events also support the HTML5 htmlFor attribute
    if (labelElement.htmlFor) {
      return document.getElementById(labelElement.htmlFor);
    }

    // If no for attribute exists, attempt to retrieve the first labellable descendant element
    // the list of which is defined here: http://www.w3.org/TR/html5/forms.html#category-label
    return labelElement.querySelector('button, input:not([type=hidden]), keygen, meter, output, progress, select, textarea');
  };

  /**
   * On touch end, determine whether to send a click event at once.
   *
   * @param {Event} event
   * @returns {boolean}
   */
  FastClick.prototype.onTouchEnd = function (event) {
    var forElement,
        trackingClickStart,
        targetTagName,
        scrollParent,
        touch,
        targetElement = this.targetElement;

    if (!this.trackingClick) {
      return true;
    }

    // Prevent phantom clicks on fast double-tap (issue #36)
    if (event.timeStamp - this.lastClickTime < this.tapDelay) {
      this.cancelNextClick = true;
      return true;
    }

    if (event.timeStamp - this.trackingClickStart > this.tapTimeout) {
      return true;
    }

    // Reset to prevent wrong click cancel on input (issue #156).
    this.cancelNextClick = false;

    this.lastClickTime = event.timeStamp;

    trackingClickStart = this.trackingClickStart;
    this.trackingClick = false;
    this.trackingClickStart = 0;

    // On some iOS devices, the targetElement supplied with the event is invalid if the layer
    // is performing a transition or scroll, and has to be re-detected manually. Note that
    // for this to function correctly, it must be called *after* the event target is checked!
    // See issue #57; also filed as rdar://13048589 .
    if (deviceIsIOSWithBadTarget) {
      touch = event.changedTouches[0];

      // In certain cases arguments of elementFromPoint can be negative, so prevent setting targetElement to null
      targetElement = document.elementFromPoint(touch.pageX - window.pageXOffset, touch.pageY - window.pageYOffset) || targetElement;
      targetElement.fastClickScrollParent = this.targetElement.fastClickScrollParent;
    }

    targetTagName = targetElement.tagName.toLowerCase();
    if (targetTagName === 'label') {
      forElement = this.findControl(targetElement);
      if (forElement) {
        this.focus(targetElement);
        if (deviceIsAndroid) {
          return false;
        }

        targetElement = forElement;
      }
    } else if (this.needsFocus(targetElement)) {

      // Case 1: If the touch started a while ago (best guess is 100ms based on tests for issue #36) then focus will be triggered anyway. Return early and unset the target element reference so that the subsequent click will be allowed through.
      // Case 2: Without this exception for input elements tapped when the document is contained in an iframe, then any inputted text won't be visible even though the value attribute is updated as the user types (issue #37).
      if (event.timeStamp - trackingClickStart > 100 || deviceIsIOS && window.top !== window && targetTagName === 'input') {
        this.targetElement = null;
        return false;
      }

      this.focus(targetElement);
      this.sendClick(targetElement, event);

      // Select elements need the event to go through on iOS 4, otherwise the selector menu won't open.
      // Also this breaks opening selects when VoiceOver is active on iOS6, iOS7 (and possibly others)
      if (!deviceIsIOS || targetTagName !== 'select') {
        this.targetElement = null;
        event.preventDefault();
      }

      return false;
    }

    if (deviceIsIOS && !deviceIsIOS4) {

      // Don't send a synthetic click event if the target element is contained within a parent layer that was scrolled
      // and this tap is being used to stop the scrolling (usually initiated by a fling - issue #42).
      scrollParent = targetElement.fastClickScrollParent;
      if (scrollParent && scrollParent.fastClickLastScrollTop !== scrollParent.scrollTop) {
        return true;
      }
    }

    // Prevent the actual click from going though - unless the target node is marked as requiring
    // real clicks or if it is in the whitelist in which case only non-programmatic clicks are permitted.
    if (!this.needsClick(targetElement)) {
      event.preventDefault();
      this.sendClick(targetElement, event);
    }

    return false;
  };

  /**
   * On touch cancel, stop tracking the click.
   *
   * @returns {void}
   */
  FastClick.prototype.onTouchCancel = function () {
    this.trackingClick = false;
    this.targetElement = null;
  };

  /**
   * Determine mouse events which should be permitted.
   *
   * @param {Event} event
   * @returns {boolean}
   */
  FastClick.prototype.onMouse = function (event) {

    // If a target element was never set (because a touch event was never fired) allow the event
    if (!this.targetElement) {
      return true;
    }

    if (event.forwardedTouchEvent) {
      return true;
    }

    // Programmatically generated events targeting a specific element should be permitted
    if (!event.cancelable) {
      return true;
    }

    // Derive and check the target element to see whether the mouse event needs to be permitted;
    // unless explicitly enabled, prevent non-touch click events from triggering actions,
    // to prevent ghost/doubleclicks.
    if (!this.needsClick(this.targetElement) || this.cancelNextClick) {

      // Prevent any user-added listeners declared on FastClick element from being fired.
      if (event.stopImmediatePropagation) {
        event.stopImmediatePropagation();
      } else {

        // Part of the hack for browsers that don't support Event#stopImmediatePropagation (e.g. Android 2)
        event.propagationStopped = true;
      }

      // Cancel the event
      event.stopPropagation();
      event.preventDefault();

      return false;
    }

    // If the mouse event is permitted, return true for the action to go through.
    return true;
  };

  /**
   * On actual clicks, determine whether this is a touch-generated click, a click action occurring
   * naturally after a delay after a touch (which needs to be cancelled to avoid duplication), or
   * an actual click which should be permitted.
   *
   * @param {Event} event
   * @returns {boolean}
   */
  FastClick.prototype.onClick = function (event) {
    var permitted;

    // It's possible for another FastClick-like library delivered with third-party code to fire a click event before FastClick does (issue #44). In that case, set the click-tracking flag back to false and return early. This will cause onTouchEnd to return early.
    if (this.trackingClick) {
      this.targetElement = null;
      this.trackingClick = false;
      return true;
    }

    // Very odd behaviour on iOS (issue #18): if a submit element is present inside a form and the user hits enter in the iOS simulator or clicks the Go button on the pop-up OS keyboard the a kind of 'fake' click event will be triggered with the submit-type input element as the target.
    if (event.target.type === 'submit' && event.detail === 0) {
      return true;
    }

    permitted = this.onMouse(event);

    // Only unset targetElement if the click is not permitted. This will ensure that the check for !targetElement in onMouse fails and the browser's click doesn't go through.
    if (!permitted) {
      this.targetElement = null;
    }

    // If clicks are permitted, return true for the action to go through.
    return permitted;
  };

  /**
   * Remove all FastClick's event listeners.
   *
   * @returns {void}
   */
  FastClick.prototype.destroy = function () {
    var layer = this.layer;

    if (deviceIsAndroid) {
      layer.removeEventListener('mouseover', this.onMouse, true);
      layer.removeEventListener('mousedown', this.onMouse, true);
      layer.removeEventListener('mouseup', this.onMouse, true);
    }

    layer.removeEventListener('click', this.onClick, true);
    layer.removeEventListener('touchstart', this.onTouchStart, false);
    layer.removeEventListener('touchmove', this.onTouchMove, false);
    layer.removeEventListener('touchend', this.onTouchEnd, false);
    layer.removeEventListener('touchcancel', this.onTouchCancel, false);
  };

  /**
   * Check whether FastClick is needed.
   *
   * @param {Element} layer The layer to listen on
   */
  FastClick.notNeeded = function (layer) {
    var metaViewport;
    var chromeVersion;
    var blackberryVersion;
    var firefoxVersion;

    // Devices that don't support touch don't need FastClick
    if (typeof window.ontouchstart === 'undefined') {
      return true;
    }

    // Chrome version - zero for other browsers
    chromeVersion = +(/Chrome\/([0-9]+)/.exec(navigator.userAgent) || [, 0])[1];

    if (chromeVersion) {

      if (deviceIsAndroid) {
        metaViewport = document.querySelector('meta[name=viewport]');

        if (metaViewport) {
          // Chrome on Android with user-scalable="no" doesn't need FastClick (issue #89)
          if (metaViewport.content.indexOf('user-scalable=no') !== -1) {
            return true;
          }
          // Chrome 32 and above with width=device-width or less don't need FastClick
          if (chromeVersion > 31 && document.documentElement.scrollWidth <= window.outerWidth) {
            return true;
          }
        }

        // Chrome desktop doesn't need FastClick (issue #15)
      } else {
        return true;
      }
    }

    if (deviceIsBlackBerry10) {
      blackberryVersion = navigator.userAgent.match(/Version\/([0-9]*)\.([0-9]*)/);

      // BlackBerry 10.3+ does not require Fastclick library.
      // https://github.com/ftlabs/fastclick/issues/251
      if (blackberryVersion[1] >= 10 && blackberryVersion[2] >= 3) {
        metaViewport = document.querySelector('meta[name=viewport]');

        if (metaViewport) {
          // user-scalable=no eliminates click delay.
          if (metaViewport.content.indexOf('user-scalable=no') !== -1) {
            return true;
          }
          // width=device-width (or less than device-width) eliminates click delay.
          if (document.documentElement.scrollWidth <= window.outerWidth) {
            return true;
          }
        }
      }
    }

    // IE10 with -ms-touch-action: none or manipulation, which disables double-tap-to-zoom (issue #97)
    if (layer.style.msTouchAction === 'none' || layer.style.touchAction === 'manipulation') {
      return true;
    }

    // Firefox version - zero for other browsers
    firefoxVersion = +(/Firefox\/([0-9]+)/.exec(navigator.userAgent) || [, 0])[1];

    if (firefoxVersion >= 27) {
      // Firefox 27+ does not have tap delay if the content is not zoomable - https://bugzilla.mozilla.org/show_bug.cgi?id=922896

      metaViewport = document.querySelector('meta[name=viewport]');
      if (metaViewport && (metaViewport.content.indexOf('user-scalable=no') !== -1 || document.documentElement.scrollWidth <= window.outerWidth)) {
        return true;
      }
    }

    // IE11: prefixed -ms-touch-action is no longer supported and it's recomended to use non-prefixed version
    // http://msdn.microsoft.com/en-us/library/windows/apps/Hh767313.aspx
    if (layer.style.touchAction === 'none' || layer.style.touchAction === 'manipulation') {
      return true;
    }

    return false;
  };

  /**
   * Factory method for creating a FastClick object
   *
   * @param {Element} layer The layer to listen on
   * @param {Object} [options={}] The options to override the defaults
   */
  FastClick.attach = function (layer, options) {
    return new FastClick(layer, options);
  };
  module.exports = FastClick;
});
'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/8.
 */

_require.register("animation", function (module, exports, _require, global) {
  // 调用validate http message
  var validate = _require('validate');
  var message = _require('message');
  var http = _require('http');
  // 动画类
  /*
   * 动画类提供 验证码倒计时功能
   * 动画类提供 按钮防重复点击功能*/

  var Animation = function () {
    // 构造方法
    function Animation() {
      _classCallCheck(this, Animation);

      // 构造定时器
      this.timer = '';
      this.time = 60;
      this.allow = true;

      console.log('animation 构造成功...');
    }

    _createClass(Animation, [{
      key: 'fourelement',
      value: function fourelement(object) {
        var _this = this;

        var url = object.url;
        var bankName = object.bankName;
        var bankVal = document.getElementById(object.bankVal);
        var nameName = object.nameName;
        var nameVal = document.getElementById(object.nameVal);
        var identityName = object.identityName;
        var identityVal = document.getElementById(object.identityVal);
        var phoneName = object.phoneName;
        var phoneVal = document.getElementById(object.phoneVal);
        var btn = document.getElementById(object.btn);
        btn.addEventListener('click', function () {
          if (_this.allow) {
            if (validate.joint({
              bankNo: bankVal.value,
              idCard: identityVal.value,
              phone: phoneVal.value
            })) {
              var _http$post;

              // 发送请求
              http.post(url, (_http$post = {}, _defineProperty(_http$post, bankName, bankVal.value), _defineProperty(_http$post, nameName, nameVal.value), _defineProperty(_http$post, identityName, identityVal.value), _defineProperty(_http$post, phoneName, phoneVal.value), _http$post), function () {
                // 局部拦截
                _this.allow = false;
                // 定时器
                _this.timer = setInterval(function () {
                  btn.innerHTML = _this.time--;
                  if (_this.time < 0) {
                    clearInterval(_this.timer);
                    _this.time = 60;
                    _this.allow = true;
                    btn.innerHTML = '重新获取';
                  }
                }, 1000);
                message.prompt_show('验证码发送成功');
              });
            }
          }
        });
      }
    }, {
      key: 'validcode',
      value: function validcode(object) {
        var _this2 = this;

        var url = object.url;
        var phoneName = object.phoneName;
        var phoneVal = document.getElementById(object.phoneVal);
        var btn = document.getElementById(object.btn);
        btn.addEventListener('click', function () {
          if (_this2.allow) {
            if (validate.phone(phoneVal.value)) {
              // 发送请求
              http.post(url, _defineProperty({}, phoneName, phoneVal.value), function () {
                // 局部拦截
                _this2.allow = false;
                // 定时器
                _this2.timer = setInterval(function () {
                  btn.innerHTML = _this2.time--;
                  if (_this2.time < 0) {
                    clearInterval(_this2.timer);
                    _this2.time = 60;
                    _this2.allow = true;
                    btn.innerHTML = '重新获取';
                  }
                }, 1000);
                message.prompt_show('验证码发送成功');
              });
            }
          }
        });
      }
    }, {
      key: 'sendcode',
      value: function sendcode(object) {
        var _this3 = this;

        var url = object.url;
        var btn = document.getElementById(object.btn);
        btn.addEventListener('click', function () {
          if (_this3.allow) {
            // 发送请求
            http.post(url, {}, function () {
              // 局部拦截
              _this3.allow = false;
              // 定时器
              _this3.timer = setInterval(function () {
                btn.innerHTML = _this3.time--;
                if (_this3.time < 0) {
                  clearInterval(_this3.timer);
                  _this3.time = 60;
                  _this3.allow = true;
                  btn.innerHTML = '重新获取';
                }
              }, 1000);
              message.prompt_show('验证码发送成功');
            });
          }
        });
      }
    }]);

    return Animation;
  }();

  module.exports = Animation;
});
'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/15.
 */

/*
 * 基础 模块化 加载 end*/

_require.register("browser", function (module, exports, _require, global) {
  /* browser 类
   * 提供浏览器特性解决方案
   * -webkit-overflow-scrolling: touch;
   * 抛弃body滚动 自定义层级div 使用上述属性优化滑动
   * ！！！ 仅支持单模块啊滑动 */
  var Browser = function () {
    function Browser() {
      _classCallCheck(this, Browser);

      console.log('browser 构建完成...');
    }

    _createClass(Browser, [{
      key: 'elastic_touch',
      value: function elastic_touch(allowClassName) {
        var lastY = void 0,
            touchY = void 0,
            scroll = 0; // 记录Y轴坐标点
        document.body.addEventListener('touchstart', function (event) {
          lastY = event.touches[0].clientY;
        });
        document.body.addEventListener('touchmove', function (event) {
          touchY = event.touches[0].clientY;
          if (allowClassName) {
            scroll = document.getElementsByClassName(allowClassName)[0].scrollTop;
          }
          if (touchY >= lastY && scroll <= 5) {
            lastY = touchY;
            event.preventDefault();
          }
        });
      }
    }]);

    return Browser;
  }();

  module.exports = new Browser();
});
'use strict';

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/7.
 */

_require.register("keyboard", function (module, exports, _require, global) {
  // 调用message http
  var message = _require('message');
  var http = _require('http');
  // 启用fastclick处理点击事件
  var fastclick = _require('fastclick');
  fastclick.attach(document.body);
  // 键盘类

  var Keyboard =
  /*
   * 自定义支付键盘
   * 不提供样式 不提供标签
   * 用户输入值 span标签内展示 隐藏input存储
   * 键盘事件 监听键盘最外层标签的 touchEnd 事件*/

  /*
   * 键盘初始化构造事件*/
  function Keyboard(object) {
    var _this = this;

    _classCallCheck(this, Keyboard);

    // 初始化value值
    this.value = '';
    // 展示span id
    this.span = document.getElementById(object.spanId);
    // 隐藏input id
    this.input = document.getElementById(object.inputId);
    // 键盘最外层div id
    this.keyboard = document.getElementById(object.keyboardId);

    // 获取keyNum函数
    var getKeyValue = function getKeyValue(ev, key) {
      var a = ev.getAttribute(key);
      while (!a && ev.id != "keyboard") {
        ev = ev.parentNode;
        a = ev.getAttribute(key);
      }
      return a;
    };

    // copy来的 函数
    var zeroNum = function zeroNum(oldNum, inputNum) {
      var num = '';
      if (oldNum === '') {
        oldNum = '0';
      }
      if (oldNum.indexOf('.') != -1) {
        if (oldNum.length - oldNum.indexOf('.') > 2) return oldNum;
        if (inputNum === '.') return oldNum;
      }
      if (oldNum === '0' && inputNum !== '.') {
        num = inputNum;
      } else {
        num = oldNum + inputNum;
      }
      //大于俩万
      if (num >= 20000) {
        num = 20000;
        message.prompt_show('收款金额不能超过20000');
      }
      return num;
    };

    /*
     * keyboard 监听 touchstart 事件*/
    var touchStart = function touchStart(e) {
      var ev = e.target;
      if (ev.getAttribute('touch') == 'true') {
        ev.style.backgroundColor = "#f4f5f8";
      }
    };

    /*
     * keyboard 监听 touchend 事件*/
    var touchEnd = function touchEnd(e) {
      var ev = e.target;
      // 判断条件 还原点击前的白色
      if (ev.getAttribute('touch') == 'true') {
        ev.style.backgroundColor = "#FFF";
      }

      // 获取上一次的值
      var oldValue = _this.input.value;
      // 获取新输入的值
      var keyNum = getKeyValue(ev, 'keyNum');
      if (keyNum) {
        var a = zeroNum(oldValue, keyNum);
        _this.input.value = a;
        _this.span.innerHTML = a;
        return;
      }
      // 获取输入的功能键 delete quick wx-zfb
      var keyCtrl = getKeyValue(ev, 'keyCtrl');
      if (keyCtrl) {
        switch (keyCtrl) {
          case 'delete':
            var _a = oldValue.substr(0, oldValue.length - 1);
            _this.input.value = _a;
            _this.span.innerHTML = _a;
            break;
          case 'quick':
            if (oldValue > 0) {
              http.post('/wx/receipt', {
                totalFee: oldValue,
                payChannel: '103'
              }, function (data) {
                window.location.href = data.payUrl;
              });
            } else {
              message.prompt_show('请输入正确的收款金额');
            }
            break;
          case 'wx-zfb':
            if (oldValue > 0) {
              http.post('/wx/receipt', {
                totalFee: oldValue,
                payChannel: '101'
              }, function (data) {
                window.location.href = "/sqb/charge?qrCode=" + encodeURIComponent(data.payUrl) + "&name=" + data.subMerName + "&money=" + data.amount;
              });
            } else {
              message.prompt_show('请输入正确的收款金额');
            }
            break;
          case 'wx-pay':
            if (oldValue > 0) {
              http.post('/wx/receiptByCode', {
                totalFee: oldValue,
                payChannel: '101',
                merchantId: pageDate.merchantId
              }, function (data) {
                window.location.href = data.payUrl;
              });
            } else {
              message.prompt_show('请输入正确的支付金额');
            }
            break;
          case 'ali-pay':
            if (oldValue > 0) {
              http.post('/wx/receiptByCode', {
                totalFee: oldValue,
                payChannel: '102',
                merchantId: pageDate.merchantId
              }, function (data) {
                window.location.href = data.payUrl;
              });
            } else {
              message.prompt_show('请输入正确的支付金额');
            }
            break;
        }
      }
    };

    // 注册监听事件
    this.keyboard.addEventListener('touchstart', touchStart);
    this.keyboard.addEventListener('touchend', touchEnd);
  };

  module.exports = Keyboard;
});
'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/13.
 */

_require.register("paging", function (module, exports, _require, global) {

  // 引入http tools
  var tools = _require('tools');
  var http = _require('http');
  /* 数据列表分页
   * 支持加载更多分页 */

  var Paging = function () {
    function Paging(type, config) {
      _classCallCheck(this, Paging);

      this[type](config);
      console.log('Paging 构造成功...');
    }

    // 调用以下方法处理数据

    // 商户端交易记录的查询


    _createClass(Paging, [{
      key: 'trade_record',
      value: function trade_record(config) {
        // 定义变量
        var content = document.getElementById(config.content);
        var list = document.createElement('div');
        // 定义加载更多
        var nowPage = 1;
        var more = document.createElement('div');
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
        var startDate = document.getElementById(config.startDate);
        var endDate = document.getElementById(config.endDate);
        // 定义支付结果
        var payResult = {
          N: '等待支付',
          H: '支付中',
          A: '支付已受理',
          S: '支付成功',
          F: '支付失败'
        };
        // 定义提现结果
        var walResult = {
          N: '提现中',
          H: '提现中',
          A: '提现中',
          S: '提现成功',
          F: '提现失败',
          W: '请求已受理',
          E: '提现异常'
        };
        // 定义支付方式
        var payChannel = {
          101: '扫码支付',
          103: '快捷支付'
        };
        // 定义ajax事件
        var getData = function getData(e, page) {
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
            for (var i = 0; i < res.records.length; i++) {
              var group = document.createElement('a');
              if (res.records[i].tradeType == 0) {
                group.href = '/sqb/tradeDetail/' + res.records[i].id;
              } else {
                group.href = '/sqb/otherTradeDetail/' + res.records[i].id;
              }
              group.className = 'group';
              var top = document.createElement('div');
              top.className = 'top';
              var topLeft = document.createElement('div');
              topLeft.className = 'left';
              if (res.records[i].tradeType == 0) {
                topLeft.innerHTML = res.records[i].body + '-' + payChannel[res.records[i].payChannel];
              } else {
                topLeft.innerHTML = res.records[i].body;
              }
              var topRight = document.createElement('div');
              topRight.className = 'right';
              if (res.records[i].tradeType == 0) {
                topRight.innerHTML = '+' + res.records[i].totalFee + '元';
              } else {
                topRight.innerHTML = '-' + res.records[i].totalFee + '元';
              }
              top.appendChild(topLeft);
              top.appendChild(topRight);
              var bottom = document.createElement('div');
              bottom.className = 'bottom';
              var bottomLeft = document.createElement('div');
              bottomLeft.className = 'left';
              bottomLeft.innerHTML = res.records[i].errorMessage;
              var bottomRight = document.createElement('div');
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
          });
        };
        // 初始化数据
        getData();
        startDate.addEventListener('change', getData);
        endDate.addEventListener('change', getData);
      }
    }]);

    return Paging;
  }();

  module.exports = Paging;
});
'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/13.
 */

_require.register("touch", function (module, exports, _require, global) {

  /* touch 类处理点击的效果优化*/
  var Touch = function () {
    /* 构建函数
     * class 必须为touch_xx
     * 参数 当前背景色 加深or变浅 等级*/
    function Touch(name, bgc, type, level) {
      _classCallCheck(this, Touch);

      this.level = level;

      /* 初始化时判断touch类型
       * 存储 新颜色*/
      var newColor = '';
      switch (type) {
        case 'deep':
          newColor = this.deepColor(bgc);
          break;
        case 'light':
          break;
      }

      // 循环绑定事件
      var body = document.getElementsByClassName(name);

      var _loop = function _loop(i) {
        body[i].addEventListener('touchstart', function () {
          body[i].style.backgroundColor = newColor;
        });
        body[i].addEventListener('touchend', function () {
          body[i].style.backgroundColor = bgc;
        });
      };

      for (var i = 0; i < body.length; i++) {
        _loop(i);
      }
      console.log('touch 构建成功...');
    }

    // 得到较深的颜色 唯一参数 等级 默认 0.2


    _createClass(Touch, [{
      key: 'deepColor',
      value: function deepColor(oldColor) {
        var str = oldColor.replace('#', '');
        var hexs = [];
        if (str.length == 6) {
          hexs = str.match(/../g);
        } else if (str.length == 3) {
          hexs = str.match(/./g);
          for (var i = 0; i < hexs.length; i++) {
            hexs[i] = hexs[i] + hexs[i];
          }
        } else {
          console.log('不是正确的颜色格式');
        }
        var rgbs = this.hex2Rgb(hexs);
        for (var _i = 0; _i < rgbs.length; _i++) {
          rgbs[_i] = Math.floor(rgbs[_i] * (1 - this.level));
        }
        var colors = this.rgb2Hex(rgbs);
        return '#' + colors[0] + colors[1] + colors[2];
      }

      // 得到较浅的颜色 唯一参数 等级 默认 0.2

    }, {
      key: 'lightColor',
      value: function lightColor(oldColor) {
        console.log(oldColor);
        return '#000';
      }

      // 将 rgb 转 hex 带#号传入

    }, {
      key: 'rgb2Hex',
      value: function rgb2Hex(rgbs) {
        for (var i = 0; i < rgbs.length; i++) {
          rgbs[i] = rgbs[i].toString(16);
          if (rgbs[i].length == 1) {
            rgbs[i] = '0' + rgbs[i];
          }
        }
        return rgbs;
      }

      // 将 hex 转 rgb

    }, {
      key: 'hex2Rgb',
      value: function hex2Rgb(hexs) {
        for (var i = 0; i < hexs.length; i++) {
          hexs[i] = parseInt(hexs[i], 16);
        }
        return hexs;
      }
    }]);

    return Touch;
  }();

  module.exports = Touch;
});
'use strict';

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/13.
 */

_require.register("timePicker", function (module, exports, _require, global) {

  /* 时间选择器
   * 利用weui实现
   * 不提供weui样式*/
  var TimePicker = function TimePicker(btnId) {
    var _this = this;

    var inputId = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : btnId;
    var format = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : 'YYYY-MM-DD';

    _classCallCheck(this, TimePicker);

    // 创建时间选择器 html
    this.btn = document.getElementById(btnId);
    this.input = document.getElementById(inputId);
    // 注册时间监听
    var evt = document.createEvent('Event');
    evt.initEvent('change', true, true);
    this.btn.addEventListener('click', function () {
      weui.datePicker({
        start: 2016,
        end: new Date().getFullYear(),
        onChange: function onChange(result) {
          console.log(result);
        },
        onConfirm: function onConfirm(result) {
          format = 'YYYY-MM-DD';
          format = format.replace('YYYY', result[0]);
          format = format.replace('MM', result[1] + 1);
          format = format.replace('DD', result[2]);
          _this.input.value = format;
          _this.input.dispatchEvent(evt);
        }
      });
    });
    console.log('timePicker 构造成功...');
    console.log('触发按钮id:' + btnId);
    console.log('填充时间id:' + inputId);
  };

  module.exports = TimePicker;
});
'use strict';

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

/**
 * Created by administrator on 2016/12/9.
 */

_require.register("upload", function (module, exports, _require, global) {

  /* 图片上传类
   * 默认已经做好 config 校验
   * 仅针对单张图片*/
  var Upload = function () {
    function Upload(btnId, fun) {
      var _this = this;

      _classCallCheck(this, Upload);

      this.localId = '';
      this.serverId = '';
      this.btn = document.getElementById(btnId);
      this.callback = fun;
      wx.ready(function () {
        console.log('upload 构造成功...');

        _this.btn.addEventListener('click', function () {
          wx.chooseImage({
            count: 1,
            sizeType: ['original', 'compressed'],
            sourceType: ['album', 'camera'],
            success: function success(res) {
              _this.localId = res.localIds[0];
              // 图片选择完成后 延迟100ms调用上传 (解决安卓6.2以下的版本会出现的bug)
              setTimeout(function () {
                wx.uploadImage({
                  localId: _this.localId, // 需要上传的图片的本地ID，由chooseImage接口获得
                  isShowProgressTips: 1, // 默认为1，显示进度提示
                  success: function success(res) {
                    _this.serverId = res.serverId; // 返回图片的服务器端ID
                    _this.callback(_this.localId, _this.serverId);
                  }
                });
              }, 100);
            }
          });
        });
      });
    }

    _createClass(Upload, [{
      key: 'getServerId',
      value: function getServerId() {
        return this.serverId;
      }
    }]);

    return Upload;
  }();

  module.exports = Upload;
});
//# sourceMappingURL=shoes.js.map
