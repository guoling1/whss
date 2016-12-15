/**
 * Created by administrator on 2016/12/13.
 */

_require.register("touch", (module, exports, _require, global)=> {

  /* touch 类处理点击的效果优化*/
  class Touch {
    /* 构建函数
     * class 必须为touch_xx
     * 参数 当前背景色 加深or变浅 等级*/
    constructor(name, bgc, type, level) {
      this.level = level;

      /* 初始化时判断touch类型
       * 存储 新颜色*/
      let newColor = '';
      switch (type) {
        case 'deep':
          newColor = this.deepColor(bgc);
          break;
        case 'light':
          break;
      }

      // 循环绑定事件
      let body = document.getElementsByClassName(name);
      for (let i = 0; i < body.length; i++) {
        body[i].addEventListener('touchstart', ()=> {
          body[i].style.backgroundColor = newColor;
        });
        body[i].addEventListener('touchend', ()=> {
          body[i].style.backgroundColor = bgc;
        });
      }
      console.log('touch 构建成功...');
    }

    // 得到较深的颜色 唯一参数 等级 默认 0.2
    deepColor(oldColor) {
      let str = oldColor.replace('#', '');
      let hexs = [];
      if (str.length == 6) {
        hexs = str.match(/../g);
      } else if (str.length == 3) {
        hexs = str.match(/./g);
        for (let i = 0; i < hexs.length; i++) {
          hexs[i] = hexs[i] + hexs[i];
        }
      } else {
        console.log('不是正确的颜色格式');
      }
      let rgbs = this.hex2Rgb(hexs);
      for (let i = 0; i < rgbs.length; i++) {
        rgbs[i] = Math.floor(rgbs[i] * (1 - this.level));
      }
      let colors = this.rgb2Hex(rgbs);
      return '#' + colors[0] + colors[1] + colors[2];
    }

    // 得到较浅的颜色 唯一参数 等级 默认 0.2

    lightColor(oldColor) {
      console.log(oldColor);
      return '#000';
    }

    // 将 rgb 转 hex 带#号传入
    rgb2Hex(rgbs) {
      for (let i = 0; i < rgbs.length; i++) {
        rgbs[i] = rgbs[i].toString(16);
        if (rgbs[i].length == 1) {
          rgbs[i] = '0' + rgbs[i];
        }
      }
      return rgbs;
    }

    // 将 hex 转 rgb
    hex2Rgb(hexs) {
      for (let i = 0; i < hexs.length; i++) {
        hexs[i] = parseInt(hexs[i], 16);
      }
      return hexs;
    }
  }
  module.exports = Touch;
});
