/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  title: '好收银',
  router: {
    'Login': '注册'
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
