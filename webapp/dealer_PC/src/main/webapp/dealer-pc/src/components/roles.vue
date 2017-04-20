<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">角色管理</h3>
        </div>
        <div class="box-body screen-top">
          <el-button type="primary" icon="plus" size="small" @click="_$power(add,'dealer_role_add')">新增角色</el-button>
        </div>
        <div class="box-body screen-top">
          <div class="screen-item">
            <span class="screen-title">角色名称</span>
            <el-input v-model="query.markCode" placeholder="角色名称" size="small" style="width:240px"></el-input>
          </div>
          <div class="screen-item">
            <span class="screen-title"></span>
            <el-button type="primary" size="small" @click="search">筛选</el-button>
          </div>


        </div>
        <div class="box-body screen-top">
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="roleName" label="角色名称"></el-table-column>
            <el-table-column prop="updateTime" label="最后编辑时间">
              <template scope="scope">
                {{scope.row.updateTime|datetime}}
              </template>
            </el-table-column>
            <el-table-column prop="statusName" label="状态"></el-table-column>
            <el-table-column label="操作" width="100">
              <template scope="scope">
                <a @click="_$power(scope.row.id,audit,'dealer_role_update')" :to="{path:'/daili/app/roles_add',query:{id:records[scope.$index].id}}" type="text" size="small">编辑
                </a>
                <a @click="_$power(scope.row.id,open,'dealer_role_disable')" v-if="records[scope.$index].statusName=='禁用'" type="text" size="small">开启</a>
                <a @click="_$power(scope.row.id,close,'dealer_role_disable')" v-if="records[scope.$index].statusName=='正常'" type="text" size="small">禁用</a>
              </template>
            </el-table-column>
          </el-table>
          </el-table>
        </div>
        <div class="box-body screen-top">
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.pageNo"
                           :page-sizes="[20, 50, 100]"
                           :page-size="query.pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>
<script lang="babel">
  import store from '../store'
  export default {
    data () {
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
      audit:function (id) {
        this.$router.push({path:'/daili/app/roles_add',query:{id:id}});
      },
      add: function () {
        this.$router.push('/daili/app/roles_add');
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/daili/privilege/roleListByPage', this.query)
          .then(function (res) {
            this.loading = false;
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
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
        this.$http.post('/daili/privilege/activeUser',{id:val})
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
        this.$http.post('/daili/privilege/disableUser',{id:val})
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
  }
</script>
<style scoped lang="less">
  .screen-top {
    padding-top: 0 !important;
  }

  .screen-item {
    float: left;
    margin-right: 10px;
  }

  .screen-title {
    display: block;
    height: 24px;
    line-height: 24px;
  }
</style>
