/**
 * Created by administrator on 16/11/2.
 */

import deal from './modules/deal';
import Login from '../components/Login.vue'

export default [
  {
    path: '/admin',
    redirect: '/admin/record'
  },
  {
    path: '/admin/login',
    component: Login
  },
  deal
];
