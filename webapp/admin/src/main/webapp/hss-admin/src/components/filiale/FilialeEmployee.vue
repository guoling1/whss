<template>
  <div id="filialeEmployee">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">分公司员工</h3>
          <a @click="issue" class="pull-right">分公司员工权限</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>分公司:</label>
              <el-input style="width: 188px" v-model="query.dealerName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>员工编号:</label>
              <el-input style="width: 188px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>姓名:</label>
              <el-input style="width: 188px" v-model="query.realname" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>手机号:</label>
              <el-input style="width: 188px" v-model="query.mobile" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="markCode" label="员工编号"></el-table-column>
            <el-table-column prop="username" label="登录名"></el-table-column>
            <el-table-column prop="realname" label="姓名"></el-table-column>
            <el-table-column prop="belongDealer" label="所属分公司"></el-table-column>
            <el-table-column prop="mobile" label="手机号"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column prop="roleName" label="角色"></el-table-column>
            <el-table-column prop="createTime" label="注册日期">
              <template scope="scope">
                <span>{{scope.row.createTime|changeDate}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
              <template scope="scope">
                <span v-if="records[scope.$index].status==2" type="text" size="small">禁用</span>
                <span v-if="records[scope.$index].status==1" type="text" size="small">正常</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template scope="scope">
                <a @click="_$power(scope.row.id,issue1,'boss_oem_staff_update')" type="text" size="small" v-if="scope.row.isMaster!=1">编辑</a>
                <a @click="_$power(scope.row.id,open,'boss_oem_staff_disable')" v-if="records[scope.$index].status==2&&scope.row.isMaster!=1" type="text" size="small">开启</a>
                <a @click="_$power(scope.row.id,close,'boss_oem_staff_disable')" v-if="records[scope.$index].status==1&&scope.row.isMaster!=1" type="text" size="small">禁用</a>
              </template>
            </el-table-column>
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
    name: 'filialeEmployee',
    data(){
      return {
        query:{
          dealerName:'',
          pageNo:1,
          pageSize:10,
          markCode: "",
          realname: "",
          mobile: ""
        },
        records: [],
        count: 0,
        total: 0,
        loading: true
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      reset: function () {
        this.query={
          dealerName:'',
          pageNo:1,
          pageSize:10,
          markCode: "",
          realname: "",
          mobile: ""
        }
      },
      issue: function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/filialeRole');
//        this.$router.push('/admin/details/filialeRole')
      },
      issue1: function (id) {
        window.open('http://admin.qianbaojiajia.com/admin/details/filialeRole?id='+id)
//        this.$router.push({path:'/admin/details/filialeRole',query:{id:id}})
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/dealer/oemList', this.query)
          .then(function (res) {
            this.loading = false;
            this.$data.records = res.data.records;
            this.$data.total = res.data.totalPage;
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
        this.$http.post('/admin/user/activeUser',{id:val})
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
        this.$http.post('/admin/user/disableUser',{id:val})
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
        this.query.pageNo = 1;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.query.pageNo = 1;
        this.query.pageSize = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.pageNo = val;
        this.getData()
      },
    }
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
