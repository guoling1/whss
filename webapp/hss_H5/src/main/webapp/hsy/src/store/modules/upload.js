/**
 * Created by administrator on 16/11/2.
 */

// initial state
const state = {
  change: 0,
  localId: [],
  serverId: []
};

// mutations
const mutations = {
  UPLOAD_CHANGE (state, obj) {
    if (obj.localId) {
      state.change += 1;
      state.localId = obj.localId;
    }
    if (obj.serverId) {
      state.change += 1;
      state.serverId = obj.serverId;
    }
  }
};

export default {
  state,
  mutations
}
