<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">员工管理</h3>
            </div>
            <div class="box-body screen-top">
              <el-button type="primary" icon="plus" size="small" @click="_$power(add,'dealer_staff_add')">新增员工</el-button>
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">员工编号</span>
                <el-input v-model="query.markCode" placeholder="员工编号" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">姓名</span>
                <el-input v-model="query.realname" placeholder="姓名" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">手机号</span>
                <el-input v-model="query.mobile" placeholder="手机号" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title"></span>
                <el-button type="primary" size="small" @click="search">筛选</el-button>
              </div>
            </div>
            <div class="box-body">
              <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
                <el-table-column type="index" width="70" label="序号"></el-table-column>
                <el-table-column prop="markCode" label="员工编号"></el-table-column>
                <el-table-column prop="username" label="登录名"></el-table-column>
                <el-table-column prop="realname" label="姓名"></el-table-column>
                <el-table-column prop="mobile" label="手机号"></el-table-column>
                <el-table-column prop="email" label="邮箱"></el-table-column>
                <el-table-column prop="roleName" label="角色权限">
                  <template scope="scope">
                    <el-button type="text" @click="roleDet(scope.row.roleId)">{{scope.row.roleName}}</el-button>
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" label="注册日期">
                  <template scope="scope">
                    <span>{{scope.row.createTime|datetime}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="status" label="状态">
                  <template scope="scope">
                    <span v-if="records[scope.$index].status==2" type="text" size="small">禁用</span>
                    <span v-if="records[scope.$index].status==1" type="text" size="small">正常</span>
                  </template>
                </el-table-column>
                </el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <a @click="_$power(scope.row.id,audit,'dealer_staff_update')" v-if="scope.row.isMaster!=1">编辑</a>
                    <a @click="_$power(scope.row.id,open,'dealer_staff_disable')" v-if="records[scope.$index].status==2&&scope.row.isMaster!=1" type="text" size="small">开启</a>
                    <a @click="_$power(scope.row.id,close,'dealer_staff_disable')" v-if="records[scope.$index].status==1&&scope.row.isMaster!=1" type="text" size="small">禁用</a>
                  </template>
                </el-table-column>
              </el-table>
              </el-table>
            </div>
            <div class="box-body">
              <el-pagination style="float:right"
                             @size-change="handleSizeChange"
                             @current-change="handleCurrentChange"
                             :current-page="query.pageNo"
                             :page-sizes="[20, 100, 200, 500]"
                             :page-size="query.pageSize"
                             layout="total, sizes, prev, pager, next, jumper"
                             :total="count">
              </el-pagination>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
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
          markCode: "",
          realname: "",
          mobile: ""
        },
        records: [],
        count: 0,
        total: 0,
        loading: true,
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      roleDet: function (id) {
        this.$router.push({path:'/daili/app/roles_add',query:{id:id}});
      },
      audit:function (id) {
        this.$router.push({path:'/daili/app/employees_add',query:{id:id}});
      },
      add: function () {
        this.$router.push('/daili/app/employees_add');
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/daili/privilege/userList', this.query)
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
        this.$http.post('/daili/privilege/activeUser',{id:val})
          .then((res)=>{
          for(var i=0;i<this.records.length;i++){
            if(this.records[i].id == val){
              this.records[i].status = '1'
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
              this.records[i].status = '2'
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
