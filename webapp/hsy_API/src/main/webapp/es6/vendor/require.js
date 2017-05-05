/*
 * 以下为模块化加载的基础代码
 * copy 自 阮一峰 教程
 * 第三方包 直接标签引入
 * */

/* 提供全局变量
 * 内置的全局变量必须加前缀 __ */
var global = {
  __allow: true, // 全局允许 主要用于防止二次点击
  __lockUrl: [],
  __lockCheck: function (url) {
    var result = true;
    for (var i = 0; i < this.__lockUrl.length; i++) {
      if (url == this.__lockUrl[i]) {
        result = false;
      }
    }
    return result;
  },
  __lockNum: function (url) {
    var num = -1;
    for (var i = 0; i < this.__lockUrl.length; i++) {
      if (url.indexOf(this.__lockUrl[i]) > 1) {
        num = i;
      }
    }
    return num;
  },
  __lockRemove: function (dx) {
    if (isNaN(dx) || dx > this.__lockUrl.length) {
      return false;
    }
    for (var i = 0, n = 0; i < this.__lockUrl.length; i++) {
      if (this.__lockUrl[i] != this.__lockUrl[dx]) {
        this.__lockUrl[n++] = this.__lockUrl[i]
      }
    }
    this.__lockUrl.length -= 1
  }
};

function require(p) {
  var path = require.resolve(p);
  var mod = require.modules[path];
  if (!mod) throw new Error('failed to require "' + p + '"');
  if (!mod.exports) {
    mod.exports = {};
    mod.call(mod.exports, mod, mod.exports, require.relative(path), global);
  }
  return mod.exports;
}

require.modules = {};

require.resolve = function (path) {
  var orig = path;
  var reg = path + '.js';
  var index = path + '/index.js';
  return require.modules[reg] && reg
    || require.modules[index] && index
    || orig;
};

require.register = function (path, fn) {
  require.modules[path] = fn;
};

require.relative = function (parent) {
  return function (p) {
    if ('.' != p.charAt(0)) return require(p);
    var path = parent.split('/');
    var segs = p.split('/');
    path.pop();

    for (var i = 0; i < segs.length; i++) {
      var seg = segs[i];
      if ('..' == seg) path.pop();
      else if ('.' != seg) path.push(seg);
    }

    return require(path.join('/'));
  };
};