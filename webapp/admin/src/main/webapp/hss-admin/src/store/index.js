/**
 * Created by administrator on 16/11/2.
 */

import Vue from 'vue'
import Vuex from 'vuex'
//import * as actions from './actions'
//import * as getters from './getters'
import title from './modules/title'
import message from './modules/message'
import login from './modules/login'

Vue.use(Vuex);

export default new Vuex.Store({
  //actions,
  //getters,
  modules: {
    title,
    message,
    login
  }
})
