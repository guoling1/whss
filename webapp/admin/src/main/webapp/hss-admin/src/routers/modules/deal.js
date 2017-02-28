/**
 * Created by administrator on 16/11/2.
 */

import Crumbs from '../../Crumbs.vue';
// 后台管理 交易 流水 组件
const DealQuery = r => require.ensure([], () => r(require('../../components/DealQuery')), 'group-record');
const DealDet = r => require.ensure([], () => r(require('../../components/DealDet')), 'group-record');
const WithdrawalQuery = r => require.ensure([], () => r(require('../../components/WithdrawalQuery')), 'group-record');
const WithdrawalDet = r => require.ensure([], () => r(require('../../components/WithdrawalDet')), 'group-record');
const WithdrawalAudit = r => require.ensure([], () => r(require('../../components/WithdrawalAudit')), 'group-record');
const PayQuery = r => require.ensure([], () => r(require('../../components/PayQuery')), 'group-record');
const StoreAccount = r => require.ensure([], () => r(require('../../components/StoreAccount')), 'group-record');
const StoreList = r => require.ensure([], () => r(require('../../components/StoreList')), 'group-record');
const StoreAudit = r => require.ensure([], () => r(require('../../components/StoreAudit')), 'group-record');
const StoreAuditList = r => require.ensure([], () => r(require('../../components/StoreAuditList')), 'group-record');
const AgentList = r => require.ensure([], () => r(require('../../components/AgentList')), 'group-record');
const AgentListSec = r => require.ensure([], () => r(require('../../components/AgentListSec')), 'group-record');
const AgentListFir = r => require.ensure([], () => r(require('../../components/AgentListFir')), 'group-record');
const AgentAccount = r => require.ensure([], () => r(require('../../components/AgentAccount')), 'group-record');
const AgentAdd = r => require.ensure([], () => r(require('../../components/AgentAdd')), 'group-record');
const AgentAddPro = r => require.ensure([], () => r(require('../../components/AgentAddPro')), 'group-record');
const AgentAddBase = r => require.ensure([], () => r(require('../../components/AgentAddBase')), 'group-record');
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
const IssueRecord = r => require.ensure([], () => r(require('../../components/IssueRecord')), 'group-record');
const IssueSuccess = r => require.ensure([], () => r(require('../../components/IssueSuccess')), 'group-record');
const Invite = r => require.ensure([], () => r(require('../../components/Invite')), 'group-record');
const Upgrade = r => require.ensure([], () => r(require('../../components/Upgrade')), 'group-record');
const CodeStatus = r => require.ensure([], () => r(require('../../components/CodeStatus')), 'group-record');
const StoreAuditHSY = r => require.ensure([], () => r(require('../../components/StoreAuditHSY')), 'group-record');
const T1Audit = r => require.ensure([], () => r(require('../../components/T1Audit')), 'group-record');
const PersonnelList = r => require.ensure([], () => r(require('../../components/PersonnelList')), 'group-record');
const PersonnelAdd = r => require.ensure([], () => r(require('../../components/PersonnelAdd')), 'group-record');

const NewDealQuery = r => require.ensure([], () => r(require('../../components/newVersion/NewDealQuery')), 'group-record');
const NewDealDet = r => require.ensure([], () => r(require('../../components/newVersion/NewDealDet')), 'group-record');
const NewWithdrawalQuery = r => require.ensure([], () => r(require('../../components/newVersion/NewWithdrawalQuery')), 'group-record');
const NewWithdrawalDet = r => require.ensure([], () => r(require('../../components/newVersion/NewWithdrawalDet')), 'group-record');

const OrderQuery = r => require.ensure([], () => r(require('../../components/OrderQuery')), 'group-record');
const OrderDetail = r => require.ensure([], () => r(require('../../components/OrderDetail')), 'group-record');
const TradeQuery = r => require.ensure([], () => r(require('../../components/TradeQuery')), 'group-record');
const ProfitAccount = r => require.ensure([], () => r(require('../../components/ProfitAccount')), 'group-record');
const ProfitAccountDet = r => require.ensure([], () => r(require('../../components/ProfitAccountDet')), 'group-record');
const ProfitCom = r => require.ensure([], () => r(require('../../components/ProfitCom')), 'group-record');
const ProfitFir = r => require.ensure([], () => r(require('../../components/ProfitFir')), 'group-record');
const ProfitSec = r => require.ensure([], () => r(require('../../components/ProfitSec')), 'group-record');
const ProfitComDet = r => require.ensure([], () => r(require('../../components/ProfitDet')), 'group-record');
const ProfitDet = r => require.ensure([], () => r(require('../../components/ProfitDet')), 'group-record');
const ProfitFirDet = r => require.ensure([], () => r(require('../../components/ProfitDet')), 'group-record');
const ProfitSecDet = r => require.ensure([], () => r(require('../../components/ProfitDet')), 'group-record');

const Test = r => require.ensure([], () => r(require('../../components/Test')), 'group-record');

export default {
  path: '/admin/record',
  redirect: '/admin/record/newDeal',
  component: Crumbs,
  children: [
    {
      path: 'storeAuditHSY',
      name: 'StoreAuditHSY',
      component: StoreAuditHSY
    },
    {
      path: 't1Audit',
      name: 'T1Audit',
      component: T1Audit
    },
    {
      path: 'newDeal',
      name: 'NewDealQuery',
      component: NewDealQuery
    },
    {
      path: 'newDealDet',
      name: 'NewDealDet',
      component: NewDealDet
    },
    {
      path: 'newWithdrawalQuery',
      name: 'NewWithdrawalQuery',
      component: NewWithdrawalQuery
    },
    {
      path: 'newWithdrawalDet',
      name: 'NewWithdrawalDet',
      component: NewWithdrawalDet
    },
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
      path: 'withdrawalAudit',
      name: 'WithdrawalAudit',
      component: WithdrawalAudit
    },
    {
      path: 'withdrawalDet',
      name: 'WithdrawalDet',
      component: WithdrawalDet
    },
    {
      path: 'payQuery',
      name: 'PayQuery',
      component: PayQuery
    },
    {
      path: 'storeAccount',
      name: 'StoreAccount',
      component: StoreAccount
    },
    {
      path: 'storeList',
      name: 'StoreList',
      component: StoreList
    },
    {
      path: 'storeAuditList',
      name: 'StoreAuditList',
      component: StoreAuditList
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
      path: 'agentListFir',
      name: 'AgentListFir',
      component: AgentListFir
    },
    {
      path: 'agentListSec',
      name: 'AgentListSec',
      component: AgentListSec
    },
    {
      path: 'agentAccount',
      name: 'AgentAccount',
      component: AgentAccount
    },
    {
      path: 'agentAdd',
      name: 'AgentAdd',
      component: AgentAdd
    },
    {
      path: 'agentAddPro',
      name: 'AgentAddPro',
      component: AgentAddPro
    },
    {
      path: 'agentAddBase',
      name: 'AgentAddBase',
      component: AgentAddBase
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
      path: 'issueRecord',
      name: 'IssueRecord',
      component: IssueRecord
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
    },
    {
      path: 'orderQuery',
      name: 'OrderQuery',
      component: OrderQuery
    },
    {
      path: 'orderDetail',
      name: 'OrderDetail',
      component: OrderDetail
    },
    {
      path: 'tradeQuery',
      name: 'TradeQuery',
      component: TradeQuery
    },
    {
      path: 'profitAccount',
      name: 'ProfitAccount',
      component: ProfitAccount
    },
    {
      path: 'profitAccountDet',
      name: 'ProfitAccountDet',
      component: ProfitAccountDet
    },
    {
      path: 'profitCom',
      name: 'ProfitCom',
      component: ProfitCom
    },
    {
      path: 'profitFir',
      name: 'ProfitFir',
      component: ProfitFir
    },
    {
      path: 'profitSec',
      name: 'ProfitSec',
      component: ProfitSec
    },
    {
      path: 'profitDet',
      name: 'ProfitDet',
      component: ProfitDet
    },
    {
      path: 'profitComDet',
      name: 'ProfitComDet',
      component: ProfitComDet
    },
    {
      path: 'profitFirDet',
      name: 'ProfitFirDet',
      component: ProfitFirDet
    },
    {
      path: 'profitSecDet',
      name: 'ProfitSecDet',
      component: ProfitSecDet
    },
    {
      path: 'personnelList',
      name: 'PersonnelList',
      component: PersonnelList
    },
    {
      path: 'personnelAdd',
      name: 'PersonnelAdd',
      component: PersonnelAdd
    },
    {
      path: 'test',
      name: 'Test',
      component: Test
    },
  ]
}
