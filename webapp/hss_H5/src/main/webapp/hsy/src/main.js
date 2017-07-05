import Vue from 'vue'
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
      if (body.code != 1) {
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
import validate from './plugin/validate'
Vue.use(validate);
import keyboard from './plugin/keyboard'
Vue.use(keyboard);
import upload from './plugin/upload'
Vue.use(upload);
import QRCode from './plugin/QRCode'
Vue.use(QRCode);

// 添加路由变化监听

// 拦截 用户状态 跳转链接
const interceptor = function (to, status, next) {
  if (to.name == 'Wallet') {
    next();
  } else {
    if (status == '-1' && to.name == 'Login') {
      next();
      return;
    } else if (status == '-1') {
      next('/sqb/login');
      return;
    }

    if (status == '0' && to.name == 'Material') {
      next();
      return;
    } else if (status == '0') {
      next('/sqb/material');
      return;
    }

    if (status == '1' && to.name == 'Upload') {
      next();
      return;
    } else if (status == '1') {
      next('/sqb/upload');
      return;
    }

    if (to.name == 'Login' || to.name == 'Material' || to.name == 'Upload') {
      next('/sqb/wallet');
      return;
    }

    next();
  }
};

router.beforeEach((to, from, next) => {
  if (to.path.indexOf('/dealer') != -1 || to.name == 'Wallet') {
    next();
  } else {
    // 获取链接上的 openId
    if (!store.state.users.status) {
      Vue.http.post('/wx/getStatus').then(function (res) {
        store.commit('USERS_STATUS', {
          status: res.data.status
        });
        interceptor(to, res.data.status, next);
      }, function () {
        store.commit('MESSAGE_ACCORD_SHOW', {
          text: '获取用户状态失败'
        });
      });
      return;
    }
    interceptor(to, store.state.users.status, next);
  }
});

router.afterEach(route => {
  Vue.__init(route);
});

// 4. 创建和挂载根实例。
// 记得要通过 router 配置参数注入路由，
// 从而让整个应用都有路由功能
const app = new Vue({
  store,
  router
}).$mount('#app');
