<template>
  <div id="issueRecord">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">二维码分配记录</h3>
          <a @click="_$power(issue,'boss_qr_code_distribute')" class="pull-right btn btn-primary" style="margin-left: 20px">分配二维码</a>
          <a @click="revoke" class="pull-right btn btn-primary" style="margin-left: 20px">撤回二维码</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>代理商编号:</label>
              <el-input style="width: 188px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>代理商名称:</label>
              <el-input style="width: 188px" v-model="query.name" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>上级代理名称:</label>
              <el-input style="width: 188px" v-model="query.firstName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>上级代理编号:</label>
              <el-input style="width: 188px" v-model="query.firstMarkCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>分配方:</label>
              <el-select style="width: 188px" v-model="query.type" clearable placeholder="请选择" size="small">
                <el-option label="全部" value="0">全部</el-option>
                <el-option label="金开门" value="1">金开门</el-option>
                <el-option label="一级代理" value="2">一级代理</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="distributeTime" :formatter="distributeTime" label="分配时间"></el-table-column>
            <el-table-column prop="proxyName" label="代理名称"></el-table-column>
            <el-table-column prop="markCode" label="代理商编号"></el-table-column>
            <el-table-column prop="fristProxyName" label="上级名称"></el-table-column>
            <el-table-column prop="firstMarkCode" label="上级编号"></el-table-column>
            <el-table-column prop="count" label="分配个数"></el-table-column>
            <el-table-column prop="startCode" label="起始码"></el-table-column>
            <el-table-column prop="endCode" label="终止码"></el-table-column>
            <el-table-column label="类型">
              <template scope="scope">
                <span v-if="records[scope.$index].type==1">实体码</span>
                <span v-if="records[scope.$index].type==2">电子码</span>
              </template>
            </el-table-column>
            <el-table-column prop="operateUser" label="操作人"></el-table-column>
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
    name: 'issueRecord',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          markCode:"",
          name:"",
          firstMarkCode:"",
          firstName:"",
          type:"0"//0全部 1boss 2代理商
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {
      this.$http.post('/admin/user/distributeRecord',this.$data.query)
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
    methods: {
      revoke: function () {
        this.$router.push('/admin/record/codeRevoke')
      },
      issue: function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/issue')
//        this.$router.push('/admin/record/issue')
      },
      //格式化时间
      distributeTime: function (row, column) {
        var val=row.distributeTime;
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
        this.$http.post('/admin/user/distributeRecord',this.$data.query)
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
        this.$http.post('/admin/user/distributeRecord',this.$data.query)
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
