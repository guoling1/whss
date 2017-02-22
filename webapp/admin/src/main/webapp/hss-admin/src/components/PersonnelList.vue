<template>
  <div id="personnelList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">员工管理</h3>
          <router-link to="/admin/record/personnelAdd" class="btn btn-primary" style="float: right;">新增</router-link>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>员工编号:</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>姓名:</label>
              <el-input style="width: 120px" v-model="query.realname" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>手机号:</label>
              <el-input style="width: 120px" v-model="query.mobile" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="markCode" label="员工编号"></el-table-column>
            <el-table-column prop="username" label="登录名"></el-table-column>
            <el-table-column prop="realname" label="姓名"></el-table-column>
            <el-table-column prop="companyName" label="所属分公司"></el-table-column>
            <el-table-column prop="deptName" label="所属部门"></el-table-column>
            <el-table-column prop="mobile" label="手机号"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column prop="roleName" label="角色"></el-table-column>
            <el-table-column prop="createTime" label="注册日期"></el-table-column>
            <el-table-column prop="status" label="状态"></el-table-column>
            <el-table-column label="操作" width="100">
              <template scope="scope">
                <router-link :to="{path:'/admin/record/profitComDet',query:{id:records[scope.$index].id}}" type="text"
                             size="small">编辑
                </router-link>
                <router-link @click="open(records[scope.$index].id)" v-if="records[scope.$index].status==2" type="text" size="small">开启</router-link>
                <router-link v-if="records[scope.$index].status==1" type="text" size="small">禁用</router-link>
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
    name: 'personnelList',
    data(){
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
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/user/userList', this.$data.query)
          .then(function (res) {
            this.loading = false;
            this.$data.records = res.data.records;
            this.$data.total = res.data.totalPage;
            this.$data.url = res.data.ext;
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
      //格式化hss创建时间
      changeTime: function (row, column) {
        var val=row.createTime;
        if(val==''||val==null){
          return ''
        }else {
          val = new Date(val)
          var year=val.getFullYear();
          var month=val.getMonth()+1;
          var date=val.getDate();
          var hour=val.getHours();
          var minute=val.getMinutes();
          var second=val.getSeconds();
          function tod(a) {
            if(a<10){
              a = "0"+a
            }
            return a;
          }
          return year+"-"+tod(month)+"-"+tod(date)+" "+tod(hour)+":"+tod(minute)+":"+tod(second);
        }
      },
      open: function (val) {
        this.loading = true;
        this.$http.post('/admin/user/activeUser')
          .then((res)=>{
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
        this.$http.post('/admin/user/disableUser')
          .then((res)=>{
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
  ul{
    padding: 0;
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
