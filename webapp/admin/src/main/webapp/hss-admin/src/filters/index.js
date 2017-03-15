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
