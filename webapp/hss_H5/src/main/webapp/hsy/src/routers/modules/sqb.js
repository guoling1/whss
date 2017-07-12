/**
 * Created by administrator on 16/11/2.
 */

import App from '../../App';
// 应用 火车票 流程组件
const Login = r => require.ensure([], () => r(require('../../components/Login')), 'group-ticket');
const Material = r => require.ensure([], () => r(require('../../components/hsy-wx/Material')), 'group-ticket');
const Upload = r => require.ensure([], () => r(require('../../components/hsy-wx/Upload')), 'group-ticket');
const Status = r => require.ensure([], () => r(require('../../components/hsy-wx/Status')), 'group-ticket');
const Wallet = r => require.ensure([], () => r(require('../../components/hsy-wx/Wallet')), 'group-ticket');
const Collection = r => require.ensure([], () => r(require('../../components/hsy-wx/Collection')), 'group-ticket');
const Payment = r => require.ensure([], () => r(require('../../components/hsy-wx/Payment')), 'group-ticket');
const ChargeCode = r => require.ensure([], () => r(require('../../components/hsy-wx/ChargeCode')), 'group-ticket');
const Success = r => require.ensure([], () => r(require('../../components/hsy-wx/Success')), 'group-ticket');
const Follow = r => require.ensure([], () => r(require('../../components/hsy-wx/Follow')), 'group-ticket');

export default {
  path: '/sqb',
  redirect: '/sqb/login',
  component: App,
  children: [
    {
      path: 'login',
      name: 'Login',
      component: Login
    },
    {
      path: 'material',
      name: 'Material',
      component: Material
    },
    {
      path: 'upload',
      name: 'Upload',
      component: Upload
    },
    {
      path: 'status',
      name: 'Status',
      component: Status
    },
    {
      path: 'wallet',
      name: 'Wallet',
      component: Wallet
    },
    {
      path: 'collection',
      name: 'Collection',
      component: Collection
    },
    {
      path: 'payment',
      name: 'Payment',
      component: Payment
    },
    {
      path: 'charge',
      name: 'ChargeCode',
      component: ChargeCode
    },
    {
      path: 'success/:amount/:id',
      name: 'Success',
      component: Success
    },
    {
      path: 'follow',
      name: 'Follow',
      component: Follow
    }
  ]
}
