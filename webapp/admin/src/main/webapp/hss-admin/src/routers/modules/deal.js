/**
 * Created by administrator on 16/11/2.
 */

import Crumbs from '../../Crumbs.vue';
// 后台管理 交易 流水 组件
const DealQuery = r => require.ensure([], () => r(require('../../components/DealQuery')), 'group-record');
const DealDet = r => require.ensure([], () => r(require('../../components/DealDet')), 'group-record');
const WithdrawalQuery = r => require.ensure([], () => r(require('../../components/WithdrawalQuery')), 'group-record');
const WithdrawalDet = r => require.ensure([], () => r(require('../../components/WithdrawalDet')), 'group-record');
const StoreList = r => require.ensure([], () => r(require('../../components/StoreList')), 'group-record');
const StoreAudit = r => require.ensure([], () => r(require('../../components/StoreAudit')), 'group-record');
const AgentList = r => require.ensure([], () => r(require('../../components/AgentList')), 'group-record');
const AgentAdd = r => require.ensure([], () => r(require('../../components/AgentAdd')), 'group-record');
const CompanyProfit = r => require.ensure([], () => r(require('../../components/CompanyProfit')), 'group-record');
const CompanyProfitDet = r => require.ensure([], () => r(require('../../components/CompanyProfitDet')), 'group-record');
const FirProfit = r => require.ensure([], () => r(require('../../components/FirProfit')), 'group-record');
const FirProfitDet = r => require.ensure([], () => r(require('../../components/CompanyProfitDet')), 'group-record');
const SecProfit = r => require.ensure([], () => r(require('../../components/SecProfit')), 'group-record');
const SecProfitDet = r => require.ensure([], () => r(require('../../components/CompanyProfitDet')), 'group-record');
const PassAdd = r => require.ensure([], () => r(require('../../components/PassAdd')), 'group-record');
const PassList = r => require.ensure([], () => r(require('../../components/PassList')), 'group-record');
const ProductAdd = r => require.ensure([], () => r(require('../../components/ProductAdd')), 'group-record');
const ProductList = r => require.ensure([], () => r(require('../../components/ProductList')), 'group-record');
const Issue = r => require.ensure([], () => r(require('../../components/Issue')), 'group-record');
const Issue1 = r => require.ensure([], () => r(require('../../components/Issue1')), 'group-record');
const IssueSuccess = r => require.ensure([], () => r(require('../../components/IssueSuccess')), 'group-record');
const Invite = r => require.ensure([], () => r(require('../../components/Invite')), 'group-record');
const Upgrade = r => require.ensure([], () => r(require('../../components/Upgrade')), 'group-record');
const CodeStatus = r => require.ensure([], () => r(require('../../components/CodeStatus')), 'group-record');

export default {
  path: '/admin/record',
  redirect: '/admin/record/deal',
  component: Crumbs,
  children: [
    {
      path: 'deal',
      name: 'DealQuery',
      component: DealQuery
    },
    {
      path: 'dealDet',
      name: 'DealDet',
      component: DealDet
    },
    {
      path: 'withdrawal',
      name: 'WithdrawalQuery',
      component: WithdrawalQuery
    },
    {
      path: 'withdrawalDet',
      name: 'WithdrawalDet',
      component: WithdrawalDet
    },
    {
      path: 'storeList',
      name: 'StoreList',
      component: StoreList
    },
    {
      path: 'storeAudit',
      name: 'StoreAudit',
      component: StoreAudit
    },
    {
      path: 'agentList',
      name: 'AgentList',
      component: AgentList
    },
    {
      path: 'agentAdd',
      name: 'AgentAdd',
      component: AgentAdd
    },
    {
      path: 'companyProfit',
      name: 'CompanyProfit',
      component: CompanyProfit
    },
    {
      path: 'companyProfitDet',
      name: 'CompanyProfitDet',
      component: CompanyProfitDet
    },
    {
      path: 'firProfit',
      name: 'FirProfit',
      component: FirProfit
    },
    {
      path: 'firProfitDet',
      name: 'FirProfitDet',
      component: FirProfitDet
    },
    {
      path: 'secProfit',
      name: 'SecProfit',
      component: SecProfit
    },
    {
      path: 'secProfitDet',
      name: 'SecProfitDet',
      component: SecProfitDet
    },
    {
      path: 'passAdd',
      name: 'PassAdd',
      component: PassAdd
    },
    {
      path: 'passList',
      name: 'PassList',
      component: PassList
    },
    {
      path: 'productAdd',
      name: 'ProductAdd',
      component: ProductAdd
    },
    {
      path: 'productList',
      name: 'ProductList',
      component: ProductList
    },
    {
      path: 'issue',
      name: 'Issue',
      component: Issue
    },
    {
      path: 'issue1',
      name: 'Issue1',
      component: Issue1
    },
    {
      path: 'issueSuccess',
      name: 'IssueSuccess',
      component: IssueSuccess
    },
    {
      path: 'invite',
      name: 'Invite',
      component: Invite
    },
    {
      path: 'upgrade',
      name: 'Upgrade',
      component: Upgrade
    },
    {
      path: 'codeStatus',
      name: 'CodeStatus',
      component: CodeStatus
    }
  ]
}
