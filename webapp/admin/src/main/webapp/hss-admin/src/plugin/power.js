/**
 * Created by guoling on 2017/3/30.
 */
export default{
  install(Vue){
    const power = function (fun,val) {
      if(val==undefined){
        this.$message({
          showClose: true,
          message: '无访问权限',
          type: 'error'
        })
        return;
      }else {
        fun()
      }

      // this.$http.post('',{id:val})
      //   .then(res =>{
      //     this.$message({
      //       showClose: true,
      //       message: err.statusMessage,
      //       type: 'error'
      //     })
      //   })
      //   .catch( err =>{
      //     this.$message({
      //       showClose: true,
      //       message: err.statusMessage,
      //       type: 'error'
      //     })
      //   })
    }
    Vue.__power = power;
    Vue.prototype._$power = power;
    }


}
