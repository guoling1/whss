/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  openId: null,
  status: null
};

// mutations
const mutations = {
  USERS_STATUS (state, obj) {
    state.openId = obj.openId;
    state.status = obj.status;
    window.localStorage.setItem('openId', obj.openId);
  }
};

export default {
  state,
  mutations
}
