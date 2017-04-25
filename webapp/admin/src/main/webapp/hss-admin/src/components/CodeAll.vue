<template>
  <div id="cordAll">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">所有二维码</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>二维码编号:</label>
              <el-input style="width: 188px" v-model="query.code" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>商户名称:</label>
              <el-input style="width: 188px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>一代名称:</label>
              <el-input style="width: 188px" v-model="query.firstDealerName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>二代名称:</label>
              <el-input style="width: 188px" v-model="query.secondDealerName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>产品类型:</label>
              <el-select style="width: 188px" v-model="query.sysType" clearable placeholder="请选择" size="small">
                <el-option label="好收收" value="hss">好收收</el-option>
                <el-option label="好收银" value="hsy">好收银</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="sysTypeName" label="产品类型"></el-table-column>
            <el-table-column prop="code" label="二维码编号">
              <template scope="scope">
                <el-button type="text" @click="checkDetail($event,scope.row.code)">{{scope.row.code}}</el-button>
              </template>
            </el-table-column>
            <el-table-column prop="activateTime" :formatter="changeTime" label="激活时间"></el-table-column>
            <el-table-column prop="firstDealerName" label="一代名称"></el-table-column>
            <el-table-column prop="secondDealerName" label="二代名称"></el-table-column>
            <el-table-column prop="merchantName" label="商户名称"></el-table-column>
            <el-table-column prop="merchantMarkCode" label="商户编号"></el-table-column>
            <el-table-column prop="merchantStatus" label="商户状态"></el-table-column>
            <el-table-column prop="shopName" label="店铺名称"></el-table-column>
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
    name: 'issueAll',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          code:'',
          merchantName:'',
          firstDealerName:'',
          secondDealerName:'',
          sysType:'hss'
        },
        records: [],
        count: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      checkDetail: function (event, code) {
        this.$router.push({path: '/admin/record/codeDet', query: {code: code}});
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/code/selectQrCodeList', this.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.loading = false;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      changeTime: function (row, column) {
        var val=row.activateTime;
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
        this.query.pageNo = 1;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.pageNo = val;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.query.pageNo = 1;
        this.$data.query.pageSize = val;
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
