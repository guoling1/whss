<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">所有商户</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">商户编号</span>
                <el-input v-model="orderNo" placeholder="商户编号" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">商户名称</span>
                <el-input v-model="orderNo" placeholder="商户名称" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title"></span>
                <el-button type="primary" size="small" @click="screen">筛选</el-button>
              </div>
            </div>
            <div class="box-body">
              <el-table :data="tableData" border
                        v-loading="tableLoading"
                        element-loading-text="数据加载中">
                <el-table-column prop="splitOrderNo" label="商户编号"></el-table-column>
                <el-table-column prop="splitOrderNo" label="商户名称"></el-table-column>
                <el-table-column label="注册时间">
                  <template scope="scope">
                    {{ scope.row.businessType | filter_businessType }}
                  </template>
                </el-table-column>
                <el-table-column label="注册手机号">
                  <template scope="scope">
                    {{ scope.row.splitCreateTime | datetime }}
                  </template>
                </el-table-column>
                <el-table-column prop="orderNo" label="所属代理商"></el-table-column>
                <el-table-column prop="splitSettlePeriod" label="所属代理商编号"></el-table-column>
                <el-table-column prop="splitSettlePeriod" label="注册业务来源"></el-table-column>
                <el-table-column prop="dealerName" label="注册入口"></el-table-column>
                <el-table-column prop="splitAmount" label="已开通业务"></el-table-column>
              </el-table>
            </div>
            <div class="box-body">
              <el-pagination style="float:right"
                             @size-change="handleSizeChange"
                             @current-change="handleCurrentChange"
                             :current-page="pageNo"
                             :page-sizes="[20, 100, 200, 500]"
                             :page-size="pageSize"
                             layout="total, sizes, prev, pager, next, jumper"
                             :total="total">
              </el-pagination>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
  </div>
</template>
<script lang="babel">
  export default {
    name: 'app',
    data() {
      return {
        total: 0,
        pageSize: 20,
        pageNo: 1,
        tableData: [],
        tableLoading: false,
        orderNo: '',
      }
    },
    created() {
      //this.getData();
    },
    methods: {
      screen: function () {
        this.getData();
      },
      getData: function () {
        this.tableLoading = true;
        this.$http.post('/daili/profit/details', {
          pageSize: this.pageSize,
          pageNo: this.pageNo,
          orderNo: this.orderNo,
          businessType: this.businessType,
          beginDate: this.beginDate,
          endDate: this.endDate
        }).then(res => {
          this.tableLoading = false;
          this.total = res.data.count;
          this.tableData = res.data.records;
        }, err => {
          this.tableLoading = false;
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        });
      }
    }
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
