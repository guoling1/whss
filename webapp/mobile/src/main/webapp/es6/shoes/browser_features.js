/**
 * Created by administrator on 2016/12/15.
 */

/*
 * 基础 模块化 加载 end*/

_require.register("browser", (module, exports, _require, global)=> {
  /* browser 类
   * 提供浏览器特性解决方案
   * -webkit-overflow-scrolling: touch;
   * 抛弃body滚动 自定义层级div 使用上述属性优化滑动
   * ！！！ 仅支持单模块啊滑动 */
  class Browser {
    constructor() {
      console.log('browser 构建完成...')
    }

    elastic_touch(allowClassName) {
      let lastY, touchY, scroll = 0; // 记录Y轴坐标点
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
  }

  module.exports = new Browser();
});