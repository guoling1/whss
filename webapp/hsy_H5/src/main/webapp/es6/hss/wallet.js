/**
 * Created by administrator on 2016/12/9.
 */

// 引入http message
const message = _require('message');
// 引入 touch 动画
const Touch = _require('touch');
new Touch('touch_op', '#4b65a8', 'deep', '0.2');
new Touch('touch_gr', '#FFF', 'deep', '0.1');
// 引入浏览器特性处理
const browser = _require('browser');
browser.elastic_touch();