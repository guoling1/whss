/**
 * Created by administrator on 16/11/2.
 */

 import sqb from './modules/sqb';
 import dealer from './modules/dealer';

// 2. 定义路由
// 1级作为顶部路由,2级作为底部路由,3级作为主内容区路由
export default [
  {
    path: '/',
    redirect: '/sqb'
  },
  sqb,
  dealer
];
