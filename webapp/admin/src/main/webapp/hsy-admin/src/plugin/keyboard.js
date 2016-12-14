/**
 * Created by administrator on 16/11/23.
 */

export default{
  install(Vue){
    Vue.directive('keyboard', {
      inserted: function (el, binding) {
        // 初始化指令设置,获取配置参数
        // arg 为input样式
        console.log(binding);
        let arg = binding.arg;
        // 创建input
        let input = document.createElement('input');
        input.type = 'text';
        // 设置输入框不可 readonly
        input.setAttribute("readOnly", 'true');
        // 设置input的样式 如果存在style,则设置class,如果不存在,采用默认设置
        if (arg && arg.input) {
          input.className = arg.input;
        } else {
          // 此处设置input的默认样式
          input.style.border = '1px solid #000'
        }
        // 设置input的点击事件
        input.onclick = function () {
          console.log('唤起键盘');
        };
        // input value 的设置函数
        const setValue = function (val) {
          if (val == 'delete') {
            // 删除
            input.value = input.value.substr(0, input.value.length - 1);
          } else {
            // 校验小数位 小数点
            if (input.value.indexOf('.') != -1) {
              let pointPosition = input.value.indexOf('.');
              let strLength = input.value.length;
              // 小数
              if (strLength - pointPosition > 2) {
                return false;
              }
            }
            // 添加
            input.value += val;
          }
        };
        // 将input添加到el下
        el.appendChild(input);

        // 创建键盘div
        let keyboard = document.createElement('div');
        // 设置keyboard的样式 如果存在style,则设置class,如果不存在,采用默认设置
        if (arg && arg.keyboard) {
          keyboard.className = arg.keyboard;
        } else {
          // 此处设置input的默认样式
          keyboard.style.width = '100%';
          keyboard.style.height = '280px';
          keyboard.style.position = 'fixed';
          keyboard.style.left = '0';
          keyboard.style.bottom = '0';
          keyboard.style.borderTop = '1px solid #000'
        }
        /* 设置点击唤起自定义键盘
         * 键盘默认状态 唤起/隐藏
         * 键盘是否可隐藏 是/否*/

        // 定义按钮默认样式
        let btnStyleText = 'width:33.33%;height:70px;float:left;text-align:center;line-height:70px;';

        // 定义键盘左侧数字按钮
        let keyLeft = document.createElement('div');
        // 定义数字按键 点击事件
        let key1 = document.createElement('div');
        key1.innerHTML = '1';
        key1.style.cssText = btnStyleText;
        key1.onclick = function () {
          setValue('1');
        };
        let key2 = document.createElement('div');
        key2.innerHTML = '2';
        key2.style.cssText = btnStyleText;
        key2.onclick = function () {
          setValue('2');
        };
        let key3 = document.createElement('div');
        key3.innerHTML = '3';
        key3.style.cssText = btnStyleText;
        key3.onclick = function () {
          setValue('3');
        };
        let key4 = document.createElement('div');
        key4.innerHTML = '4';
        key4.style.cssText = btnStyleText;
        key4.onclick = function () {
          setValue('4');
        };
        let key5 = document.createElement('div');
        key5.innerHTML = '5';
        key5.style.cssText = btnStyleText;
        key5.onclick = function () {
          setValue('5');
        };
        let key6 = document.createElement('div');
        key6.innerHTML = '6';
        key6.style.cssText = btnStyleText;
        key6.onclick = function () {
          setValue('6');
        };
        let key7 = document.createElement('div');
        key7.innerHTML = '7';
        key7.style.cssText = btnStyleText;
        key7.onclick = function () {
          setValue('7');
        };
        let key8 = document.createElement('div');
        key8.innerHTML = '8';
        key8.style.cssText = btnStyleText;
        key8.onclick = function () {
          setValue('8');
        };
        let key9 = document.createElement('div');
        key9.innerHTML = '9';
        key9.style.cssText = btnStyleText;
        key9.onclick = function () {
          setValue('9');
        };
        let keyPoint = document.createElement('div');
        keyPoint.innerHTML = '.';
        keyPoint.style.cssText = btnStyleText;
        keyPoint.onclick = function () {
          setValue('.');
        };
        let key0 = document.createElement('div');
        key0.innerHTML = '0';
        key0.style.cssText = btnStyleText;
        key0.onclick = function () {
          setValue('0');
        };
        let keyDelete = document.createElement('div');
        keyDelete.innerHTML = 'x';
        keyDelete.style.cssText = btnStyleText;
        keyDelete.onclick = function () {
          setValue('delete');
        };
        // 按需求顺序添加键盘按键
        keyboard.appendChild(key1);
        keyboard.appendChild(key2);
        keyboard.appendChild(key3);
        keyboard.appendChild(key4);
        keyboard.appendChild(key5);
        keyboard.appendChild(key6);
        keyboard.appendChild(key7);
        keyboard.appendChild(key8);
        keyboard.appendChild(key9);
        keyboard.appendChild(keyPoint);
        keyboard.appendChild(key0);
        keyboard.appendChild(keyDelete);
        // 将左侧键盘添加进keyboard
        keyboard.appendChild(keyLeft);

        // 定键盘右侧选择按钮
        let keyRight = document.createElement('div');

        // 将左侧键盘添加进keyboard
        keyboard.appendChild(keyRight);

        // 将keyboard添加到el下
        el.appendChild(keyboard);
      }
    })
  }
}
