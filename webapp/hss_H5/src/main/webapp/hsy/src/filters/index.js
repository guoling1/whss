/**
 * Created by administrator on 16/11/17.
 */

import Vue from 'vue'

Vue.filter('test', function (val) {
  return (val == 1);
});
