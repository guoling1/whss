/**
 * Created by administrator on 16/11/17.
 */

import Vue from 'vue'

Vue.filter('changeTime', function (val) {
    if (val == '' || val == null) {
      return ''
    } else {
      val = new Date(val)
      let year = val.getFullYear();
      let month = val.getMonth() + 1;
      let date = val.getDate();
      let hour = val.getHours();
      let minute = val.getMinutes();
      let second = val.getSeconds();

      function tod(a) {
        if (a < 10) {
          a = "0" + a
        }
        return a;
      }

      return year + "-" + tod(month) + "-" + tod(date) + " " + tod(hour) + ":" + tod(minute) + ":" + tod(second);
    }
});
Vue.filter('changeDate', function (val) {
  if (val == '' || val == null) {
    return ''
  } else {
    val = new Date(val)
    let year = val.getFullYear();
    let month = val.getMonth() + 1;
    let date = val.getDate();

    function tod(a) {
      if (a < 10) {
        a = "0" + a
      }
      return a;
    }

    return year + "-" + tod(month) + "-" + tod(date) ;
  }
});


Vue.filter('toFix', function (val) {
  if (val == '' || val == null) {
    return ''
  } else {
    return parseFloat(val).toFixed(2);
  }
});

Vue.filter('changeHide', function (val) {
  if(val!=""&&val!=null){
    val = val.replace(val.substring(3,val.length-6),"…");
  }
  return val
});
