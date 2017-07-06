import App from '../../App';
// 应用 火车票 流程组件
const Login = r => require.ensure([], () => r(require('../../components/Login')), 'group-ticket');
const Index = r => require.ensure([], () => r(require('../../components/hsy-dealer/Index')), 'group-ticket');
const IssueCord = r => require.ensure([], () => r(require('../../components/hsy-dealer/IssueCord')), 'group-ticket');
const Issue = r => require.ensure([], () => r(require('../../components/hsy-dealer/Issue')), 'group-ticket');
const Record = r => require.ensure([], () => r(require('../../components/hsy-dealer/Record')), 'group-ticket');
const RecordOwn = r => require.ensure([], () => r(require('../../components/hsy-dealer/RecordOwn')), 'group-ticket');
const AddDealer = r => require.ensure([], () => r(require('../../components/hsy-dealer/AddDealer')), 'group-ticket');
const Success = r => require.ensure([], () => r(require('../../components/hsy-dealer/Success')), 'group-ticket');
const AginDetail = r => require.ensure([], () => r(require('../../components/hsy-dealer/AginDetail')), 'group-ticket');
const MyDealer = r => require.ensure([], () => r(require('../../components/hsy-dealer/MyDealer')), 'group-ticket');
const IssueOwn = r => require.ensure([], () => r(require('../../components/hsy-dealer/IssueOwn')), 'group-ticket');
const MyMsg = r => require.ensure([], () => r(require('../../components/hsy-dealer/MyMsg')), 'group-ticket');
const MyCard = r => require.ensure([], () => r(require('../../components/hsy-dealer/MyCard')), 'group-ticket');
const MyStore = r => require.ensure([], () => r(require('../../components/hsy-dealer/MyStore')), 'group-ticket');
const Follow = r => require.ensure([], () => r(require('../../components/hsy-dealer/Follow')), 'group-ticket');
const Prompt = r => require.ensure([], () => r(require('../../components/hsy-dealer/Prompt')), 'group-ticket');
export default {
  path: '/dealer',
  redirect: '/dealer/login',
  component: App,
  children: [
    {
      path: 'login',
      name: 'Login',
      component: Login
    },
    {
      path: 'index',
      name: 'Index',
      component: Index
    },
    {
      path: 'issueCord',
      redirect: '/dealer/issueCord/issue',
      component: IssueCord,
      children: [
        {
          path: 'issue',
          name: 'Issue',
          component: Issue
        },
        {
          path: 'record',
          name: 'Record',
          component: Record
        },
        {
          path: 'recordOwn',
          name: 'RecordOwn',
          component: RecordOwn
        },
        {
          path: 'issueOwn',
          name: 'IssueOwn',
          component: IssueOwn
        },
        {
          path: 'success',
          name: 'Success',
          component: Success
        }
      ]
    },
    {
      path: 'addDealer',
      name: 'AddDealer',
      component: AddDealer
    },

    {
      path: 'aginDetail',
      name: 'AginDetail',
      component: AginDetail
    },
    {
      path: 'MyDealer',
      name: 'MyDealer',
      component: MyDealer
    },
    {
      path: 'myCard',
      name: 'MyCard',
      component: MyCard
    },
    {
      path: 'myMsg',
      name: 'MyCard',
      component: MyMsg
    },
    {
      path: 'myStore',
      name: 'MyStore',
      component: MyStore
    },
    {
      path: 'follow',
      name: 'Follow',
      component: Follow
    },
    {
      path: 'prompt',
      name: 'Prompt',
      component: Prompt
     }
  ]
}
