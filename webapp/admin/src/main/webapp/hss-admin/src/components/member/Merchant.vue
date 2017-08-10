<template>
  <div id="Merchant">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户列表</h3>
        </div>
        <div class="box-body">
          <ul class="search">
            <li class="same">
              <label>商户名:</label>
              <el-input style="width: 193px" v-model="query.realname" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <el-button type="primary" size="small" @click="search">筛选</el-button>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px;" :data="records" border>
            <el-table-column width="62" label="序号" type="index"></el-table-column>
            <el-table-column prop="globalID" label="商户编号"></el-table-column>
            <el-table-column prop="realname" label="商户名称"></el-table-column>
            <el-table-column prop="cardCount" label="会员卡数" align="right"></el-table-column>
            <el-table-column prop="total" label="储值额" align="right"></el-table-column>
            <el-table-column prop="branchName" label="操作">
              <template scope="scope">
                <el-button type="text" @click="toDetail(scope.row.id)">查看会员卡</el-button>
              </template>
            </el-table-column>
          </el-table>
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
    name: 'Merchant',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          realname:''
        },
        records: [],
        count: 0,
        loading: true,
      }
    },
    created: function () {
      this.getData();
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/merchantMember/getMerchantMemberList',this.query)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.records = res.data.records;
            },1000)
            this.count = res.data.count;
          },function (err) {
            setTimeout(()=>{
              this.loading = false;
            },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      toDetail: function (id) {
        window.open('http://admin.qianbaojiajia.com/admin/details/merchantDetail?id='+id)
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
      }
    }
  }
</script>

<style scoped lang="less" rel="stylesheet/less">
  .search {
    label{
      display: block;
      margin-bottom: 0;
    }
  }
  ul{
    padding: 0;
    margin: 0;
  }
  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
</style>
