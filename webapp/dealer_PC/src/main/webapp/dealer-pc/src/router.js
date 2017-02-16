// router
import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import App from './App'
import index from './index'
import login from './components/login.vue'
import home from './components/home.vue'

import development from './components/development.vue'

import profits_detail from './components/profits_detail.vue'
import dealer_list from './components/dealer_list.vue'
import dealer_add from './components/dealer_add.vue'
import product_add from './components/product_add.vue'
import distribution_qrcode from './components/distribution_qrcode.vue'
import qrcode_distribution from './components/qrcode_distribution.vue'
import balance_withdrawal from './components/balance_withdrawal.vue'
import withdrawal from './components/withdrawal.vue'

import store from './store/index'

// 添加 全局 directive
import './directives'

// 添加 全局 filter
import './filters'

const pop = index;

Vue.use(VueRouter);
Vue.use(VueResource);

// 0. 如果使用模块化机制编程， 要调用 Vue.use(VueRouter)

// 1. 定义（路由）组件。
// 可以从其他文件 import 进来
// 2. 定义路由
// 每个路由应该映射一个组件。 其中"component" 可以是
// 通过 Vue.extend() 创建的组件构造器，
// 或者，只是一个组件配置对象。
// 我们晚点在讨论嵌套路由。
const routes = [
  {path: '/daili/', redirect: '/daili/login'},
  {path: '/daili/login', name: "login", component: login},
  {
    path: '/daili/app', redirect: '/daili/app/home', component: index,
    children: [
      {path: 'development', name: "development", component: development},
      {path: 'home', name: "home", component: home},
      {path: 'profits_detail', name: "profits_detail", component: profits_detail},
      {path: 'dealer_list', name: "dealer_list", component: dealer_list},
      {path: 'dealer_add', name: "dealer_add", component: dealer_add},
      {path: 'product_add', name: "product_add", component: product_add},
      {path: 'distribution_qrcode', name: "distribution_qrcode", component: distribution_qrcode},
      {path: 'qrcode_distribution', name: "qrcode_distribution", component: qrcode_distribution},
      {path: 'balance_withdrawal', name: "balance_withdrawal", component: balance_withdrawal},
      {path: 'withdrawal', name: "withdrawal", component: withdrawal},
    ]
  }
];

// 3. 创建 router 实例，然后传 `routes` 配置
// 你还可以传别的配置参数, 不过先这么简单着吧。
const router = new VueRouter({
  mode: 'history',
  routes // （缩写）相当于 routes: routes
});

// ajax 请求的全局拦截器
Vue.http.interceptors.push((request, next) => {
  next((response) => {
    let {status, body} = response;
    if (status == 200) {
      // 这里判断 header 是否设置为文件下载
      // 如果返回的 body 为 blob 则直接下载该文件
      if (response.headers.map['Content-Disposition']) {
        let fileNameArray = response.headers.map['Content-Disposition'][0].split('=');
        let fileName = decodeURIComponent(fileNameArray[1]);
        let links = document.createElement("a");
        document.body.appendChild(links);
        links.style = "display: none";
        let url = window.URL.createObjectURL(body);
        links.href = url;
        links.download = fileName;
        links.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(links);
      } else if (body.code == -2) {
        router.push('/login');
      } else if (body.code != 1) {
        response.status = 500;
        response.statusMessage = body.message || '系统异常';
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

// 添加第三方插件
import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-default/index.css'

Vue.use(ElementUI);

// 添加自定义插件
import filePost from './plugin-vue/file-post'
Vue.use(filePost);

/* eslint-disable no-new */
// new Vue({
//   el: '#app',
//   render: h => h(App)
// })

// 4. 创建和挂载根实例。
// 记得要通过 router 配置参数注入路由，
// 从而让整个应用都有路由功能
const app = new Vue({
  store,
  router,
  render: h => h(App)
  //render: h => h(index)
}).$mount('#app');

// 现在，应用已经启动了！
