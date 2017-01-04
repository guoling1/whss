/**
 * Created by administrator on 16/11/2.
 */

import Vue from 'vue'
import Vuex from 'vuex'
//import * as actions from './actions'
//import * as getters from './getters'
import title from './modules/title'
import message from './modules/message'
import upload from './modules/upload'
import users from './modules/users'

Vue.use(Vuex);

export default new Vuex.Store({
  //actions,
  //getters,
  modules: {
    title,
    message,
    upload,
    users
  }
})
