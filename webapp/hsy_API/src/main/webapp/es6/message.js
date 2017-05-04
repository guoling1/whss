/**
 * Created by administrator on 2016/12/8.
 */

_require.register("message", (module, exports, _require, global)=> {
  /*
   * 全局的提示信息类
   * 每个页面必须引用 兼职数据重置的方法*/
  class Message {
    constructor() {
      // 重置数据
      global.__allow = true;
      // 创建提示信息div prompt
      let body = document.body;
      this.prompt = document.createElement('div');
      this.prompt.id = 'prompt';
      this.prompt.style.display = 'none';
      this.prompt.style.width = 'auto';
      this.prompt.style.minWidth = '200px';
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
      this.prompt.style.whiteSpace = 'normal';
      body.appendChild(this.prompt);
      // 创建微信load提示 weui
      this.load = document.createElement('div');
      this.load.id = 'loadingToast';
      this.load.style.opacity = '0';
      this.load.style.display = 'none';
      let load_msak = document.createElement('div');
      load_msak.className = 'weui-mask_transparent';
      let load_toast = document.createElement('div');
      load_toast.className = 'weui-toast';
      let load_i = document.createElement('i');
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
      let toast_msak = document.createElement('div');
      toast_msak.className = 'weui-mask_transparent';
      let toast_toast = document.createElement('div');
      toast_toast.className = 'weui-toast';
      let toast_i = document.createElement('i');
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

    prompt_show(text) {
      clearTimeout(this.timer);
      this.prompt.innerHTML = text;
      this.prompt.style.display = 'block';
      this.timer = setTimeout(()=> {
        this.prompt.style.display = 'none';
      }, 1000);
    }

    // 微信 weui 样式
    load_show(text) {
      this.load_p.innerHTML = text || '正在加载';
      this.load.style.opacity = '1';
      this.load.style.display = 'block';
    }

    load_hide() {
      this.load.style.opacity = '0';
      this.load.style.display = 'none';
    }

    toast_show(text) {
      this.toast_p.innerHTML = text || '已完成';
      this.toast.style.opacity = '1';
      this.toast.style.display = 'block';
    }
  }
  module.exports = new Message();
});