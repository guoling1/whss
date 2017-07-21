<template>
  <div id="Merchant">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户会员卡详情</h3>
          <i class="el-icon-circle-cross pull-right" style="color: #8f9092;font-size: 18px;cursor: pointer" @click="close"></i>
        </div>

        <div class="box-body">

          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px;" :data="records" border>
            <el-table-column width="62" label="序号" type="index"></el-table-column>
            <el-table-column prop="branchCode" label="会员卡名"></el-table-column>
            <el-table-column prop="province" label="生效店铺"></el-table-column>
            <el-table-column prop="city" label="开卡储值额" align="right"></el-table-column>
            <el-table-column prop="bankName" label="折扣"></el-table-column>
            <el-table-column prop="bankName" label="开卡送额" align="right"></el-table-column>
            <el-table-column prop="bankName" label="充值送" align="right"></el-table-column>
            <el-table-column prop="bankName" label="可分享"></el-table-column>
            <el-table-column prop="bankName" label="办理数量" align="right"></el-table-column>
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
        records: [],
        count: 0,
        loading: true,
        query:{
          uid:'',
          pageSize:10,
          pageNo:1
        }
      }
    },
    created: function () {
      this.getData();
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.query.uid = this.$route.query.id;
        this.$http.post('/admin/merchantMember/getMemberShipList',this.query)
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
      close: function () {
        window.close();
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
