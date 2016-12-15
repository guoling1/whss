/**
 * Created by administrator on 2016/12/13.
 */

_require.register("timePicker", (module, exports, _require, global)=> {

  /* 时间选择器
   * 利用weui实现
   * 不提供weui样式*/
  class TimePicker {
    constructor(btnId, inputId = btnId, format = 'YYYY-MM-DD') {
      // 创建时间选择器 html
      this.btn = document.getElementById(btnId);
      this.input = document.getElementById(inputId);
      // 注册时间监听
      let evt = document.createEvent('Event');
      evt.initEvent('change', true, true);
      this.btn.addEventListener('click', ()=> {
        weui.datePicker({
          start: 2016,
          end: new Date().getFullYear(),
          onChange: (result)=> {
            console.log(result);
          },
          onConfirm: (result)=> {
            format = 'YYYY-MM-DD';
            format = format.replace('YYYY', result[0]);
            format = format.replace('MM', result[1] + 1);
            format = format.replace('DD', result[2]);
            this.input.value = format;
            this.input.dispatchEvent(evt);
          }
        });
      });
      console.log('timePicker 构造成功...');
      console.log('触发按钮id:' + btnId);
      console.log('填充时间id:' + inputId);
    }
  }
  module.exports = TimePicker;
});