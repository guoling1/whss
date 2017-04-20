// import Crumbs from '../../Crumbs.vue';
// 后台管理 交易 流水 组件
// 订单
const OrderQuery = r => require.ensure([], () => r(require('components/OrderQuery')), 'group-record');
const OrderDetail = r => require.ensure([], () => r(require('components/OrderDetail')), 'group-record');
// 交易
const DealQuery = r => require.ensure([], () => r(require('components/DealQuery')), 'group-record');
const DealDet = r => require.ensure([], () => r(require('components/DealDet')), 'group-record');
const NewDealQuery = r => require.ensure([], () => r(require('components/newVersion/NewDealQuery')), 'group-record');
const NewDealDet = r => require.ensure([], () => r(require('components/newVersion/NewDealDet')), 'group-record');
const Retrieval = r => require.ensure([], () => r(require('components/Retrieval')), 'group-record');
const RetrievalDet = r => require.ensure([], () => r(require('components/RetrievalDet')), 'group-record');
//分润
const ProfitCom = r => require.ensure([], () => r(require('components/ProfitCom')), 'group-record');
const ProfitFir = r => require.ensure([], () => r(require('components/ProfitFir')), 'group-record');
const ProfitSec = r => require.ensure([], () => r(require('components/ProfitSec')), 'group-record');
const ProfitCount = r => require.ensure([], () => r(require('components/ProfitCount')), 'group-record');
const ProfitComDet = r => require.ensure([], () => r(require('components/ProfitDet')), 'group-record');
const ProfitDet = r => require.ensure([], () => r(require('components/ProfitDet')), 'group-record');
const ProfitFirDet = r => require.ensure([], () => r(require('components/ProfitDet')), 'group-record');
const ProfitSecDet = r => require.ensure([], () => r(require('components/ProfitDet')), 'group-record');
const ProfitAccount = r => require.ensure([], () => r(require('components/ProfitAccount')), 'group-record');
const ProfitAccountDet = r => require.ensure([], () => r(require('components/ProfitAccountDet')), 'group-record');
//结算
const T1Audit = r => require.ensure([], () => r(require('components/T1Audit')), 'group-record');
const TAuditDealer = r => require.ensure([], () => r(require('components/TAuditDealer')), 'group-record');
const TAuditStore = r => require.ensure([], () => r(require('components/TAuditStore')), 'group-record');
// 渠道交易
const PayQuery = r => require.ensure([], () => r(require('components/PayQuery')), 'group-record');
const NewWithdrawalQuery = r => require.ensure([], () => r(require('components/newVersion/NewWithdrawalQuery')), 'group-record');
const WithdrawalQuery = r => require.ensure([], () => r(require('components/WithdrawalQuery')), 'group-record');
const WithdrawalDet = r => require.ensure([], () => r(require('components/WithdrawalDet')), 'group-record');
const WithdrawalAudit = r => require.ensure([], () => r(require('components/WithdrawalAudit')), 'group-record');
// 商户
const StoreList = r => require.ensure([], () => r(require('components/StoreList')), 'group-record');
const StoreAuditList = r => require.ensure([], () => r(require('components/StoreAuditList')),'group-record');
const StoreAudit = r => require.ensure([], () => r(require('components/StoreAudit')), 'group-record');
const StoreNotice = r => require.ensure([], () => r(require('components/StoreNotice')), 'group-record');
const StoreNoticeDet = r => require.ensure([], () => r(require('components/StoreNoticeDet')), 'group-record');
const StoreAuditHSY = r => require.ensure([], () => r(require('components/StoreAuditHSY')), 'group-record');
//代理
const AgentListSec = r => require.ensure([], () => r(require('components/AgentListSec')), 'group-record');
const AgentListFir = r => require.ensure([], () => r(require('components/AgentListFir')), 'group-record');
const AgentAdd = r => require.ensure([], () => r(require('components/AgentAdd')), 'group-record');
const AgentAddPro = r => require.ensure([], () => r(require('components/AgentAddPro')), 'group-record');
const AgentAddBase = r => require.ensure([], () => r(require('components/AgentAddBase')), 'group-record');
const AgentPersonnel = r => require.ensure([], () => r(require('components/AgentPersonnel')), 'group-record');
const AgentPersonnelDet = r => require.ensure([], () => r(require('components/AgentPersonnelDet')), 'group-record');
const AgentRole = r => require.ensure([], () => r(require('components/AgentRole')), 'group-record');
const AgentRoleAdd = r => require.ensure([], () => r(require('components/AgentRoleAdd')), 'group-record');
//设备
const Issue = r => require.ensure([], () => r(require('components/Issue')), 'group-record');
const IssueRecord = r => require.ensure([], () => r(require('components/IssueRecord')), 'group-record');
const CodeStatus = r => require.ensure([], () => r(require('components/CodeStatus')), 'group-record');
const CodeProRecord = r => require.ensure([], () => r(require('components/CodeProRecord')), 'group-record');
const CodeProduct = r => require.ensure([], () => r(require('components/CodeProduct')), 'group-record');
const CodeAll = r => require.ensure([], () => r(require('components/CodeAll')), 'group-record');
const CodeDet = r => require.ensure([], () => r(require('components/CodeDet')), 'group-record');
//产品
const ProductAdd = r => require.ensure([], () => r(require('components/ProductAdd')), 'group-record');
const ProductList = r => require.ensure([], () => r(require('components/ProductList')), 'group-record');
const Invite = r => require.ensure([], () => r(require('components/Invite')), 'group-record');
const Gateway = r => require.ensure([], () => r(require('components/Gateway')), 'group-record');
const GatewayAdd = r => require.ensure([], () => r(require('components/GatewayAdd')), 'group-record');
const Template = r => require.ensure([], () => r(require('components/Template')), 'group-record');
const TemplateAdd = r => require.ensure([], () => r(require('components/TemplateAdd')), 'group-record');
//通道
const PassAdd = r => require.ensure([], () => r(require('components/PassAdd')), 'group-record');
const PassList = r => require.ensure([], () => r(require('components/PassList')), 'group-record');
const LimitList = r => require.ensure([], () => r(require('components/LimitList')), 'group-record');
//员工
const PersonnelList = r => require.ensure([], () => r(require('components/PersonnelList')), 'group-record');
const PersonnelAdd = r => require.ensure([], () => r(require('components/PersonnelAdd')), 'group-record');
const Role = r => require.ensure([], () => r(require('components/Role')), 'group-record');
const RoleAdd = r => require.ensure([], () => r(require('components/RoleAdd')), 'group-record');
const AccountSystem = r => require.ensure([], () => r(require('components/accountSystem')), 'group-record');
const AccountErr = r => require.ensure([], () => r(require('components/accountErr')), 'group-record');
const AccountData = r => require.ensure([], () => r(require('components/accountData')), 'group-record');
//合作意向
const Application = r => require.ensure([], () => r(require('components/Application')), 'group-record');
const Channel = r => require.ensure([], () => r(require('components/Channel')), 'group-record');

const Test = r => require.ensure([], () => r(require('components/Test')), 'group-record');

const AAHome = r => require.ensure([], () => r(require('components/AAHome')), 'group-record');
const Home = r => require.ensure([], () => r(require('components/Home')), 'group-record');
const Login = r => require.ensure([], () => r(require('components/Login')), 'group-record');
let routes = [
  {
    path: '/',
    redirect: '/admin/login'
  },
  {
    path: '/admin/login',
    component: Login
  },
  {
    path: '/admin/newDealDet',
    component: NewDealDet
  },
  {
    path: '/admin/record',
    redirect: '/admin/record/home',
    component: AAHome,
    children: [
      {
        path: 'template',
        name: 'Template',
        component: Template
      },
      {
        path: 'templateAdd',
        name: 'TemplateAdd',
        component: TemplateAdd
      },
      {
        path: 'gateway',
        name: 'Gateway',
        component: Gateway
      },
      {
        path: 'gatewayAdd',
        name: 'GatewayAdd',
        component: GatewayAdd
      },
      {
        path: 'home',
        name: 'Home',
        component: Home
      },
      {
        path: 'accountData',
        name: 'AccountData',
        component: AccountData
      },
      {
        path: 'accountSystem',
        name: 'AccountSystem',
        component: AccountSystem
      },
      {
        path: 'accountErr',
        name: 'AccountErr',
        component: AccountErr
      },
      {
        path: 'agentPersonnel',
        name: 'AgentPersonnel',
        component: AgentPersonnel
      },
      {
        path: 'agentPersonnelDet',
        name: 'AgentPersonnelDet',
        component: AgentPersonnelDet
      },
      {
        path: 'agentRole',
        name: 'AgentRole',
        component: AgentRole
      },
      {
        path: 'agentRoleAdd',
        name: 'AgentRoleAdd',
        component: AgentRoleAdd
      },
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
        path: 'tAuditDealer',
        name: 'TAuditDealer',
        component: TAuditDealer
      },
      {
        path: 'tAuditStore',
        name: 'TAuditStore',
        component: TAuditStore
      },
      {
        path: 'newDeal',
        name: 'NewDealQuery',
        component: NewDealQuery
      },
      // {
      //   path: 'newDealDet',
      //   name: 'NewDealDet',
      //   component: NewDealDet
      // },
      {
        path: 'retrieval',
        name: 'Retrieval',
        component: Retrieval
      },
      {
        path: 'retrievalDet',
        name: 'RetrievalDet',
        component: RetrievalDet
      },
      {
        path: 'newWithdrawalQuery',
        name: 'NewWithdrawalQuery',
        component: NewWithdrawalQuery
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
        path: 'storeList',
        name: 'StoreList',
        component: StoreList
      },
      {
        path: 'storeNotice',
        name: 'StoreNotice',
        component: StoreNotice
      },
      {
        path: 'storeNoticeDet',
        name: 'StoreNoticeDet',
        component: StoreNoticeDet
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
        path: 'limitList',
        name: 'LimitList',
        component: LimitList
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
        path: 'codeProRecord',
        name: 'CodeProRecord',
        component: CodeProRecord
      },
      {
        path: 'invite',
        name: 'Invite',
        component: Invite
      },
      {
        path: 'codeStatus',
        name: 'CodeStatus',
        component: CodeStatus
      },
      {
        path: 'codeAll',
        name: 'CodeAll',
        component: CodeAll
      },
      {
        path: 'codeDet',
        name: 'CodeDet',
        component: CodeDet
      },
      {
        path: 'codeProduct',
        name: 'CodeProduct',
        component: CodeProduct
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
        path: 'profitCount',
        name: 'ProfitCount',
        component: ProfitCount
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
        path: 'role',
        name: 'Role',
        component: Role
      },
      {
        path: 'roleAdd',
        name: 'RoleAdd',
        component: RoleAdd
      },
      {
        path: 'test',
        name: 'Test',
        component: Test
      },
      {
        path: 'application',
        name: 'Application',
        component: Application
      },
      {
        path: 'channel',
        name: 'Channel',
        component: Channel
      },
    ]
  },
];

export default routes;
