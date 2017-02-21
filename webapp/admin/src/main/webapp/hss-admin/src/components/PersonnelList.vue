<template>
  <div id="personnelList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">员工管理</h3>
          <router-link to="/admin/record/personnelAdd" class="btn btn-primary" style="float: right;">导出</router-link>
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
              <el-input style="width: 120px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>手机号:</label>
              <el-input style="width: 120px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="markCode" label="员工编号"></el-table-column>
            <el-table-column prop="merchantName" label="登录名"></el-table-column>
            <el-table-column prop="proxyName" label="姓名"></el-table-column>
            <el-table-column prop="proxyName1" label="所属分公司"></el-table-column>
            <el-table-column prop="proxyName1" label="所属部门"></el-table-column>
            <el-table-column prop="proxyName1" label="手机号"></el-table-column>
            <el-table-column prop="proxyName1" label="邮箱"></el-table-column>
            <el-table-column prop="proxyName1" label="角色"></el-table-column>
            <el-table-column prop="proxyName1" label="注册日期"></el-table-column>
            <el-table-column prop="proxyName1" label="状态"></el-table-column>
            <el-table-column prop="proxyName1" label="操作"></el-table-column>
            <el-table-column label="操作" width="100">
              <template scope="scope">
                <router-link :to="{path:'/admin/record/profitComDet',query:{id:records[scope.$index].id}}" v-if="records[scope.$index].totalMoney!=0" type="text" size="small">编辑</router-link>
                <router-link :to="{path:'/admin/record/profitComDet',query:{id:records[scope.$index].id}}" v-if="records[scope.$index].totalMoney!=0" type="text" size="small">开启</router-link>
              </template>
            </el-table-column>
          </el-table>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="count">
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
          shortName:'',
          globalID:'',
          proxyName:'',
          proxyName1:'',
          startTime:'',
          endTime:'',
          startTime1:'',
          endTime1:'',
          startTime2:'',
          endTime2:'',
          status:''
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {

    },
    methods: {
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
      search(){
        this.$data.query.pageNo = 1;
        this.$data.loading = true;
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.loading = true;
        this.$data.records = '';
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
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
