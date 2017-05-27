/**
 * Created by guoling on 2017/3/30.
 */
export default{
  install(Vue){
    //查询权限
    /*
     倒数第二参：按钮的方法
     最后一个参：按钮事件的参数,即权限query
     */
    const power = function () {
      this.$http.post('/admin/user/havePermission', {descr: arguments[arguments.length-1]})
        .then(res => {
          arguments[arguments.length-2](arguments[0],arguments[1],arguments[2],arguments[3],arguments[3])
        })
        .catch(err => {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        });
    };
    Vue.__power = power;
    Vue.prototype._$power = power;
    }


}
