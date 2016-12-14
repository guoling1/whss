/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  ctrl: true
};

// mutations
const mutations = {
  LOGIN_SHOW (state) {
    state.ctrl = true;
  },
  LOGIN_HIDE (state) {
    state.ctrl = false;
  }
};

export default {
  state,
  mutations
}
