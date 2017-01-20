import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
Vue.use(ElementUI)
// 使用 vuex
import store from './store'
// 使用 vue-router
import router from './routers'
// 使用 vue-resource
import VueResource from 'vue-resource'
Vue.use(VueResource);

// ajax 请求的全局拦截器
Vue.http.interceptors.push((request, next) => {
  next((response) => {
    let {status,body} = response;
      if (status == 200) {
        if (body.code == -100 || body.code == -200 || body.code == -201) {
          store.commit('LOGIN_SHOW');
          router.push('/admin/login');
        } else if (body.code != 1) {
          response.status = 500;
          response.statusMessage = body.msg || '系统异常';
          response.statusText = 'Internal Server Error';
          response.ok = false;
        } else {
          response.data = body.result;
        }
      } else {
        response.statusMessage = '系统异常';
      }
    return response;
  })
});

// 添加 全局 directive
import './directives'

// 添加 全局 filter
import './filters'

// 添加自定义 插件
import keyboard from './plugin/keyboard'
Vue.use(keyboard);
import validate from './plugin/validate'
Vue.use(validate);

// 添加路由变化监听
router.beforeEach((to, from, next) => {
  next();
});

// 注册 adminLTE 模版
import Head from './components/AppHead.vue';
import Menu from './components/AppMenu.vue';
import Content from './components/AppContent.vue';
import Foot from './components/AppFoot.vue';
import Aside from './components/AppAside.vue';
import AsideBg from './components/AppAsideBg.vue';

Vue.component('app-header', Head);
Vue.component('app-menu', Menu);
Vue.component('app-content', Content);
Vue.component('app-footer', Foot);
Vue.component('app-aside', Aside);
Vue.component('app-aside-bg', AsideBg);


// 4. 创建和挂载根实例。
// 记得要通过 router 配置参数注入路由，
// 从而让整个应用都有路由功能
const app = new Vue({
  store,
  router
}).$mount('#app');
