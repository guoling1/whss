<template lang="html">
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        钱包++代理商系统
        <small>Version 1.0</small>
      </h1>
      <ol class="breadcrumb">
        <li>
          <router-link to="/app/home"><i class="glyphicon glyphicon-home"></i> 主页</router-link>
        </li>
        <!--<li class="active">Dashboard</li>-->
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">余额提现</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <label class="form-label">账户信息</label>
              <el-table :data="accountData" border>
                <el-table-column prop="totalAmount" label="总金额(元)"></el-table-column>
                <el-table-column prop="dueSettleAmount" label="待结算余额(元)"></el-table-column>
                <el-table-column prop="available" label="可提现余额(元)"></el-table-column>
                <el-table-column label="操作">
                  <template scope="scope">
                    <el-button type="primary" size="small"
                               @click="withdrawal($event,scope.row)">提现
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="box-body screen-top">
              <label class="form-label">账户流水</label>
              <div class="screen-item">
                <span class="screen-title">流水号</span>
                <el-input v-model="flowSn" placeholder="流水号" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">收支类别</span>
                <el-select v-model="type" size="small" clearable placeholder="请选择">
                  <el-option v-for="item in item_type"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">发生时间</span>
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
              <el-table :data="tableData" border>
                <el-table-column prop="flowSn" label="流水号" sortable></el-table-column>
                <el-table-column label="时间" width="180" sortable>
                  <template scope="scope">
                    {{ scope.row.createTime }}
                  </template>
                </el-table-column>
                <el-table-column prop="beforeAmount" label="发生前余额(元)" align="right"></el-table-column>
                <el-table-column prop="incomeAmount" label="收入金额(元)" align="right"></el-table-column>
                <el-table-column prop="outAmount" label="支出金额(元)" align="right"></el-table-column>
                <el-table-column prop="afterAmount" label="发生后余额(元)" align="right"></el-table-column>
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
        accountData: [],
        total: 0,
        pageSize: 20,
        pageNo: 1,
        tableData: [],
        flowSn: '',
        type: '',
        item_type: [
          {
            value: '0',
            label: '全部'
          },
          {
            value: '1',
            label: '收入'
          },
          {
            value: '2',
            label: '支出'
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
      this.$http.post('/daili/account/info').then(res => {
        this.accountData[0] = res.data;
      }, err => {
        this.$message({
          showClose: true,
          message: err.data.msg,
          type: 'error'
        });
      });
    },
    methods: {
      withdrawal: function (event, item) {
        this.$router.push({path: '/daili/app/withdrawal', query: item});
      },
      datetimeSelect: function (val) {
        let format = val.split(' - ');
        this.beginDate = format[0];
        this.endDate = format[1];
      },
      screen: function () {
        this.getData();
      },
      getData: function () {
        this.$http.post('/daili/account/flowDetails', {
          pageSize: this.pageSize,
          pageNo: this.pageNo,
          flowSn: this.flowSn,
          type: this.type,
          beginDate: this.beginDate,
          endDate: this.endDate
        }).then(res => {
          this.total = res.data.count;
          this.tableData = res.data.records;
        }, err => {
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
  .form-label {
    display: block;
  }

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
