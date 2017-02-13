/**
 * Created by administrator on 16/11/2.
 */

import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter);

import routes from './router'

export default new VueRouter({
  mode: 'history',
  routes, // （缩写）相当于 routes: routes
  scrollBehavior (to, from, savedPosition) {
    // return 期望滚动到哪个的位置
  }
});
