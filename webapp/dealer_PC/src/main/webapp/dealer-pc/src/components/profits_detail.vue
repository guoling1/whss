<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">分润详情</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">交易订单号</span>
                <el-input v-model="orderNo" placeholder="交易订单号" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">业务类型</span>
                <el-select v-model="businessType" size="small" clearable placeholder="请选择">
                  <el-option v-for="item in item_businessType"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">分润日期</span>
                <el-date-picker size="small"
                                v-model="datetime"
                                type="daterange"
                                align="right"
                                @change="datetimeSelect"
                                placeholder="选择日期范围"
                                :picker-options="pickerOptions2">
                </el-date-picker>
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
                <el-table-column type="index" label="序号"></el-table-column>
                <el-table-column prop="splitOrderNo" label="分润流水号"></el-table-column>
                <el-table-column label="业务类型">
                  <template scope="scope">
                    {{ scope.row.businessType | filter_businessType }}
                  </template>
                </el-table-column>
                <el-table-column label="分润时间" width="180">
                  <template scope="scope">
                    {{ scope.row.splitCreateTime | datetime }}
                  </template>
                </el-table-column>
                <el-table-column prop="orderNo" label="交易订单号"></el-table-column>
                <el-table-column prop="splitSettlePeriod" label="分润结算周期"></el-table-column>
                <el-table-column label="结算时间">
                  <template scope="scope">
                    {{ scope.row.settleTime | datetime }}
                  </template>
                </el-table-column>
                <el-table-column prop="dealerName" label="代理商名称"></el-table-column>
                <el-table-column prop="splitAmount" label="分润金额" align="right"></el-table-column>
                <el-table-column prop="remark" label="备注信息"></el-table-column>
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
        businessType: '',
        item_businessType: [
          {
            value: 'hssPay',
            label: '好收收-收款'
          },
          {
            value: 'hssWithdraw',
            label: '好收收-提现'
          },
          {
            value: 'hssPromote',
            label: '好收收-升级费'
          },
          {
            value: 'hsyPay',
            label: '好收银-收款'
          }
        ],
        datetime: '',
        beginDate: '',
        endDate: '',
        pickerOptions2: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        }
      }
    },
    created() {
      this.getData();
    },
    methods: {
      datetimeSelect: function (val) {
        let format = val.split(' - ');
        this.beginDate = format[0];
        this.endDate = format[1];
      },
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
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.getData();
      },
      handleCurrentChange(val) {
        this.pageNo = val;
        this.getData();
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
