/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  title: '钱包++',
  router: {
    'ticketReserve': '车票预定',
    'ticketRob': '抢票',
    'ticketRobOrder': '确认订单',
    'ticketRobDetail': '抢票详情',
    'ticketPrivate': '私人定制',
    'ticketOrder': '我的订单',
    'ticketSubmitOrder': '提交订单',
    'ticketSureOrder': '确认订单',
    'ticketPayOrder': '订单详情',
    'ticketTrain': '北京 → 深圳',
    'ticketContacts': '常用联系人',
    'ticketLogin': '12306登录',
    'firstAdd': '确认订单',
    'ticketRefundDetail': '订单详情',
    'secondAdd': '确认订单',
    'ticketAddChild': '常用联系人',
    'ticketRefundSuccess': '出票成功'
  }
};

// mutations
const mutations = {
  TITLE_CHANGE (state, obj) {
    if (obj.name == 'ticketTrain') {
      state.router.ticketTrain = obj.formName + ' → ' + obj.toName;
    }
    state.title = state.router[obj.name];
  }
};

export default {
  state,
  mutations
}
