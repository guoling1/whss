<template lang="html">
  <div id="productAdd">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">二维码状态查询</div>
    <div style="margin: 0 15px;width: inherit" class="box box-info">
      <form class="form-horizontal">
        <div class="box-body">
          <div class="form-group">
            <label for="productName" class="col-sm-2 control-label">二维码编号</label>
            <div class="col-sm-4">
              <input type="text" class="form-control" id="productName" v-model="code">
            </div>
            <div class="btn btn-primary" @click="search">查询</div>
          </div>
          <div class="form-group">
            <table class="table table-bordered" >
              <thead>
              <tr>
                <th>一级代理商编号</th>
                <th>一级代理商名称</th>
                <th>二级代理商编号</th>
                <th>二级代理商名称</th>
                <th>商户编号</th>
                <th>商户名称</th>
                <th>分配状态</th>
                <th>激活状态</th>
                <th>二维码类型</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="form-group">
            <div class="col-sm-12" style="text-align: center">
              此二维码未激活，可分配
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name:'productAdd',
    data(){
      return{
        code:''
      }
    },
    created: function () {

    },
    methods:{
      search: function(){
        this.$http.post('/admin/user/getCode',{code:this.$data.code})
          .then(function(res){
            console.log(res)
          },function(err){
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      }
    },
    computed: {

    },
    filtersd: {
      status: function (val) {
        if(val == 1){
          return "关注公众号生成"
        }else if(val == 2){
          return "扫码"
        }
      }
    }
  }
</script>
<style scoped lang="less">
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #fff;
  }

  table{
    width: 90%;
    margin:20px auto;
  }
  .middle{
    position: relative;
  i{
    position: absolute;
    right: 23px;
    top: 8px;
  }
  }
  .right{
    padding-top: 7px;
  }
</style>
