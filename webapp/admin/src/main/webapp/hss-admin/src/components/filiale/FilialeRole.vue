<template>
  <div id="role">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">分公司员工权限管理</h3>
          <a @click="_$power(issue,'boss_oem_role_add')" class="btn btn-primary" style="float: right;">新增角色</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>角色名称:</label>
              <el-input style="width: 188px" v-model="query.roleName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="roleName" label="角色名称"></el-table-column>
            <el-table-column prop="updateTime" label="最后编辑时间">
              <template scope="scope">
                {{scope.row.updateTime|changeTime}}
              </template>
            </el-table-column>
            <el-table-column prop="statusName" label="状态"></el-table-column>
            <el-table-column label="操作" width="100">
              <template scope="scope">
                <a @click="_$power(scope.row.id,issue1,'boss_role_update')" type="text" size="small">编辑</a>
                <a @click="_$power(scope.row.id,open,'boss_role_disable')"  v-if="records[scope.$index].statusName=='禁用'" type="text" size="small">开启</a>
                <a @click="_$power(scope.row.id,close,'boss_role_disable')" v-if="records[scope.$index].statusName=='正常'" type="text" size="small">禁用</a>
              </template>
            </el-table-column>
          </el-table>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.pageNo"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'role',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          roleName: "",
        },
        records: [],
        count: 0,
        loading: true,
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      issue: function () {
//        window.open('http://admin.qianbaojiajia.com/admin/details/FilialeRoleAdd')
        this.$router.push('/admin/details/FilialeRoleAdd')
      },
      issue1: function (id) {
//        window.open('http://admin.qianbaojiajia.com/admin/details/FilialeRoleAdd?id='+id)
        this.$router.push({path:'/admin/details/FilialeRoleAdd',query:{id:id}})
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/dealer/roleOemListByPage', this.query)
          .then(function (res) {
            this.loading = false;
            this.records = res.data.records;
            this.count = res.data.count;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      open: function (val) {
        this.loading = true;
        this.$http.post('/admin/user/activeRole',{id:val})
          .then((res)=>{
            for(var i=0;i<this.records.length;i++){
              if(this.records[i].id == val){
                this.records[i].statusName = '正常'
              }
            }
            this.loading = false;
            this.$message({
              showClose: true,
              message: '开启成功',
              type: 'success'
            });
          },(err)=>{
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      close: function (val) {
        this.loading = true;
        this.$http.post('/admin/user/disableRole',{id:val})
          .then((res)=>{
            for(var i=0;i<this.records.length;i++){
              if(this.records[i].id == val){
                this.records[i].statusName = '禁用'
              }
            }
            this.loading = false;
            this.$message({
              showClose: true,
              message: '禁用成功',
              type: 'success'
            });
          },(err)=>{
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      search(){
        this.$data.query.pageNo = 1;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.pageNo = 1;
        this.$data.query.pageSize = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.getData()
      },
    },
    watch:{

    },
  }
</script>
<style scoped lang="less">
  ul {
    padding: 0;
    margin:0;
  }
  .search{
    margin-bottom:0;
  label{
    display: block;
    margin-bottom: 0;
  }
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }
</style>
