<template>
  <div id="issueProRecord">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">产码记录</h3>
          <!--<router-link to="/admin/record/codeProduct" class="pull-right btn btn-primary" style="margin-left: 20px">生产二维码</router-link>-->
          <a @click="_$power(issue,'boss_qr_code_product')" class="pull-right btn btn-primary" style="margin-left: 20px">生产二维码</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>产品类型:</label>
              <el-select style="width: 188px" v-model="query.sysType" clearable placeholder="请选择" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收收" value="hss">好收收</el-option>
                <el-option label="好收银" value="hsy">好收银</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>二维码类型:</label>
              <el-select style="width: 188px" v-model="query.qrType" clearable placeholder="请选择" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="实体码" value="1">实体码</el-option>
                <el-option label="电子码" value="2">电子码</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column label="产生时间">
              <template scope="scope">
                <span type="text" size="small">{{records[scope.$index].createTime|changeTime}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="startCode" label="起始号码"></el-table-column>
            <el-table-column prop="endCode" label="终止号码"></el-table-column>
            <el-table-column prop="count" label="产生个数"></el-table-column>
            <el-table-column label="二维码类型">
              <template scope="scope">
                <span v-if="records[scope.$index].qrType==1">实体码</span>
                <span v-if="records[scope.$index].qrType==2">电子码</span>
              </template>
            </el-table-column>
            <el-table-column prop="sysType" label="产品名称">
              <template scope="scope">
                <span v-if="records[scope.$index].sysType=='hss'">好收收</span>
                <span v-if="records[scope.$index].sysType=='hsy'">好收银</span>
              </template>
            </el-table-column>
            <el-table-column prop="operatorName" label="操作人"></el-table-column>
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
    name: 'issueProRecord',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          sysType:'',
          qrType:''
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      reset: function () {
        this.query = {
          pageNo:1,
          pageSize:10,
          sysType:'',
          qrType:''
        };
      },
      issue: function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/codeProduct')
//        this.$router.push('/admin/record/codeProduct')
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/code/productionList',this.query)
          .then(function (res) {
            this.count = res.data.count;
            setTimeout(()=>{
              this.loading = false;
              this.records = res.data.records;
          },1000)
          },function (err) {
            setTimeout(()=>{
              this.loading = false;
          },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      //格式化创建时间
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
        this.getData();
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
    filters: {
      changeTime: function (val) {
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
          return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
        }
      }
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
