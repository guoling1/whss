<template>
  <div id="gateway">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">网关模板</h3>
          <router-link to="/admin/record/gatewayAdd" class="btn btn-primary" style="float: right;">新增网关通道</router-link>
        </div>
        <div class="box-body">
          <ul>
            <li class="same">
              <label class="title">网关模板:</label>
              <el-table max-height="637" style="font-size:12px;width:80%;display: inline-table;vertical-align: top" :data="records" border>
                <el-table-column type="index" width="70" label="序号"></el-table-column>
                <el-table-column prop="viewChannelName" label="展示名称"></el-table-column>
                <el-table-column prop="channelShortName" label="通道名称"></el-table-column>
                <el-table-column label="操作" min-width="100">
                  <template scope="scope">
                    <el-button type="text" @click="detail(scope.row.productId,scope.$index)">修改</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </li>
            <!--<li class="same">-->
              <!--<label class="title"></label>-->
              <!--<el-button type="primary" @click="submit">保存更改</el-button>-->
            <!--</li>-->
          </ul>
        </div>
        </div>
      </div>
    </div>
</template>
<script lang="babel">
  export default{
    name: 'gateway',
    data(){
      return {
        records: [],
      }
    },
    created: function () {
      this.$http.post('/admin/product/listGateway',{"productType":"hss"})
        .then(res => {
          this.records = res.data;
        })
        .catch(err => {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
    },
    methods: {
      submit:function () {
        var list = JSON.parse(JSON.stringify(this.tableData));
        for (var i = 0; i < list.length; i++) {
          for (var j = 0; j < list[i].children.length; j++) {
            for (var k = 0; k < list[i].children[j].opts.length; k++) {
              list[i].children[j].opts[k].isSelected = Number(list[i].children[j].opts[k].isSelected);
            }
            list[i].children[j].isSelected = Number(list[i].children[j].isSelected);
          }
          list[i].isSelected = Number(list[i].isSelected);
        }
        let id=0;
        if(this.$route.query.id!=undefined){
          id = this.$route.query.id;
          this.isAdd = false;
        }
        this.$http.post('/admin/user/saveRole',{
          roleId:id,
          roleName:this.roleName,
          list:list
        }).then(res => {
          this.$message({
            showClose: true,
            message: '添加成功',
            type: 'success'
          });
          this.$router.push('/admin/record/role')
        }).catch(err =>{
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
      },
      detail: function (productId, index) {
        this.$router.push({path:'/admin/record/gatewayAdd',query:{productId:productId,index:index}})
      }
    }
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    margin: 0 15px 15px 0;
    .title{
      width: 80px;
      margin-right: 15px;
      text-align: right;
    }
  }
  .btn{
    font-size: 12px;
  }
</style>
