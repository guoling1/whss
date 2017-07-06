<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <h4 class="text-center top">
        您好，{{dealerInfo}}
      </h4>
      <h2 class="text-center bottom">
        代理商账户 正在开发中,敬请期待
      </h2>
    </section>
  </div>
</template>
<script lang="babel">
  import store from '../store'
  export default {
    data () {
      return {
        dealerInfo: ''
      }
    },
    mounted (){
      this.$store.dispatch('actions_users_getInfo').then(data => {
        if (data.status === 1) {
          this.dealerInfo = data.dealerInfo;
        }
      });
    },
    methods: {}
  }
</script>
<style scoped lang="less">
  .top {
    margin-top: 200px;
  }

  .bottom {
    margin-bottom: 200px;
  }
</style>

<!--<template lang="html">-->
  <!--<div class="content-wrapper">-->
    <!--&lt;!&ndash; Main content &ndash;&gt;-->
    <!--<section class="content">-->
      <!--<div class="row">-->
        <!--<div class="col-xs-12">-->
          <!--<div class="box">-->
            <!--<div class="box-header">-->
              <!--<h3 class="box-title">代理商账户</h3>-->
            <!--</div>-->
            <!--&lt;!&ndash; /.box-header &ndash;&gt;-->
            <!--<div class="box-body screen-top">-->
              <!--<div class="screen-item">-->
                <!--<span class="screen-title">代理商编号</span>-->
                <!--<el-input v-model="orderNo" placeholder="代理商编号" size="small" style="width:240px"></el-input>-->
              <!--</div>-->
              <!--<div class="screen-item">-->
                <!--<span class="screen-title">代理商名称</span>-->
                <!--<el-input v-model="orderNo" placeholder="代理商名称" size="small" style="width:240px"></el-input>-->
              <!--</div>-->
              <!--<div class="screen-item">-->
                <!--<span class="screen-title"></span>-->
                <!--<el-button type="primary" size="small" @click="screen">筛选</el-button>-->
              <!--</div>-->
            <!--</div>-->
            <!--<div class="box-body">-->
              <!--<el-table :data="tableData" border-->
                        <!--v-loading="tableLoading"-->
                        <!--element-loading-text="数据加载中">-->
                <!--<el-table-column prop="splitOrderNo" label="代理商编号"></el-table-column>-->
                <!--<el-table-column prop="splitOrderNo" label="代理商名称"></el-table-column>-->
                <!--<el-table-column prop="businessType" label="账户总额(元)"></el-table-column>-->
                <!--<el-table-column prop="businessType" label="可用余额(元)"></el-table-column>-->
                <!--<el-table-column prop="orderNo" label="待结算金额(元)"></el-table-column>-->
                <!--<el-table-column prop="splitSettlePeriod" label="操作"></el-table-column>-->
              <!--</el-table>-->
            <!--</div>-->
            <!--<div class="box-body">-->
              <!--<el-pagination style="float:right"-->
                             <!--@size-change="handleSizeChange"-->
                             <!--@current-change="handleCurrentChange"-->
                             <!--:current-page="pageNo"-->
                             <!--:page-sizes="[20, 100, 200, 500]"-->
                             <!--:page-size="pageSize"-->
                             <!--layout="total, sizes, prev, pager, next, jumper"-->
                             <!--:total="total">-->
              <!--</el-pagination>-->
            <!--</div>-->
            <!--&lt;!&ndash; /.box-body &ndash;&gt;-->
          <!--</div>-->
          <!--&lt;!&ndash; /.box &ndash;&gt;-->
        <!--</div>-->
        <!--&lt;!&ndash; /.col &ndash;&gt;-->
      <!--</div>-->
      <!--&lt;!&ndash; /.row &ndash;&gt;-->
    <!--</section>-->
  <!--</div>-->
<!--</template>-->
<!--<script lang="babel">-->
  <!--export default {-->
    <!--name: 'app',-->
    <!--data() {-->
      <!--return {-->
        <!--total: 0,-->
        <!--pageSize: 20,-->
        <!--pageNo: 1,-->
        <!--tableData: [],-->
        <!--tableLoading: false,-->
        <!--orderNo: ''-->
      <!--}-->
    <!--},-->
    <!--created() {-->
      <!--//this.getData();-->
    <!--},-->
    <!--methods: {-->
      <!--screen: function () {-->
        <!--this.getData();-->
      <!--},-->
      <!--getData: function () {-->
        <!--this.tableLoading = true;-->
        <!--this.$http.post('/daili/profit/details', {-->
          <!--pageSize: this.pageSize,-->
          <!--pageNo: this.pageNo,-->
          <!--orderNo: this.orderNo,-->
          <!--businessType: this.businessType,-->
          <!--beginDate: this.beginDate,-->
          <!--endDate: this.endDate-->
        <!--}).then(res => {-->
          <!--this.tableLoading = false;-->
          <!--this.total = res.data.count;-->
          <!--this.tableData = res.data.records;-->
        <!--}, err => {-->
          <!--this.tableLoading = false;-->
          <!--this.$message({-->
            <!--showClose: true,-->
            <!--message: err.data.msg,-->
            <!--type: 'error'-->
          <!--});-->
        <!--});-->
      <!--},-->
      <!--handleSizeChange(val) {-->
        <!--this.pageSize = val;-->
        <!--this.getData();-->
      <!--},-->
      <!--handleCurrentChange(val) {-->
        <!--this.pageNo = val;-->
        <!--this.getData();-->
      <!--}-->
    <!--}-->
  <!--}-->
<!--</script>-->
<!--<style scoped lang="less">-->
  <!--.screen-top {-->
    <!--padding-top: 0 !important;-->
  <!--}-->

  <!--.screen-item {-->
    <!--float: left;-->
    <!--margin-right: 10px;-->
  <!--}-->

  <!--.screen-title {-->
    <!--display: block;-->
    <!--height: 24px;-->
    <!--line-height: 24px;-->
  <!--}-->
<!--</style>-->
