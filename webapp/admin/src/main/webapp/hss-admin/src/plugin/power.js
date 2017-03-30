/**
 * Created by guoling on 2017/3/30.
 */
export default{
  install(Vue){
    const power = function (x) {
      console.log(x)
    }
    Vue.__power = power;
    Vue.prototype._$power = power;
    }


}
