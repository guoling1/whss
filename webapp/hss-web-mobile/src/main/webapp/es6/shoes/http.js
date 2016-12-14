/*
 * 基础 模块化 加载 end*/

_require.register("http", (module, exports, _require, global)=> {
  // 调用message
  const message = _require('message');
  /* http类
   * 任意请求完成时 都会将 global.__allow 设置为 true
   * 任意请求开始时 都会将 global.__allow 设置为 false
   *
   * 异步请求 全局统一错误处理 只有code为1时正确
   * 同步请求 需要单独 转换json 处理错误*/
  class Http {
    constructor() {
      this.onchange = (xmlhttp, fun)=> {
        xmlhttp.onreadystatechange = function () {
          if (this.readyState == 4) {
            // 收到请求时 设置全局允许
            global.__allow = true;
            if (this.status == 200) {
              try {
                let response = eval("(" + this.responseText + ")");
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
        }
      };
      this.creathttp = ()=> {
        if (window.XMLHttpRequest) {
          return new XMLHttpRequest();
        } else {
          return new ActiveXObject("Microsoft.XMLHTTP");
        }
      }
    }

    get(url, fun) {
      if (global.__allow) {
        global.__allow = false;
        var xmlhttp = this.creathttp();
        this.onchange(xmlhttp, fun);
        xmlhttp.open("get", url, true);
        xmlhttp.send();
      }
    }

    post(url, data, fun) {
      if (global.__allow) {
        global.__allow = false;
        var xmlhttp = this.creathttp();
        this.onchange(xmlhttp, fun);
        xmlhttp.open("post", url, true);
        xmlhttp.setRequestHeader("Content-type", "application/json");
        xmlhttp.send(JSON.stringify(data));
      }
    }

    syncGet(url) {
      var xmlhttp = this.creathttp();
      xmlhttp.open("get", url, false);
      xmlhttp.send();
      return xmlhttp.responseText;
    }

    syncPost(url, data) {
      var xmlhttp = this.creathttp();
      xmlhttp.open("post", url, false);
      xmlhttp.setRequestHeader("Content-type", "application/json");
      xmlhttp.send(JSON.stringify(data));
      return xmlhttp.responseText;
    }

    formatData(data) {
      var rs = "";
      for (var m in data) {
        rs += (m + "=" + data[m] + "&");
      }
      return rs.substring(0, rs.length - 1);
    }
  }

  module.exports = new Http();
});