<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">交易查询</h3>
              <!--<a :href="'http://'+this.$data.url" download="交易记录" class="btn btn-primary" style="float: right;color: #fff">导出</a>-->
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">业务订单号</span>
                <el-input v-model="query.businessOrderNo" placeholder="业务订单号" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">交易订单号</span>
                <el-input v-model="query.orderNo" placeholder="交易订单号" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">支付流水号</span>
                <el-input v-model="query.sn" placeholder="支付流水号" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">收款商户名称</span>
                <el-input v-model="query.merchantName" placeholder="收款商户名称" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">上级代理名称</span>
                <el-input v-model="query.proxyName" placeholder="上级代理名称" size="small" style="width:220px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">交易日期</span>
                <el-date-picker size="small"
                                v-model="date"
                                type="daterange"
                                align="right"
                                @change="datetimeSelect"
                                placeholder="选择日期范围"
                                :picker-options="pickerOptions2">
                </el-date-picker>
              </div>
              <div class="screen-item">
                <span class="screen-title">交易状态</span>
                <el-select v-model="query.status" size="small" clearable placeholder="请选择" style="width: 220px">
                  <el-option v-for="item in item_settlementStatus"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">结算状态</span>
                <el-select v-model="query.settleStatus" size="small" clearable placeholder="请选择" style="width: 220px">
                  <el-option v-for="item in item_settlementStatus"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">支付方式</span>
                <el-select v-model="query.payType" size="small" clearable placeholder="请选择" style="width: 220px">
                  <el-option v-for="item in item_settlementStatus"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title"></span>
                <el-button type="primary" size="small" @click="screen">筛选</el-button>
              </div>
            </div>
            <div class="box-body">
              <el-table v-loading.body="loading" height="583" style="font-size: 12px;margin:15px 0" :data="records" border :row-style="tableFoot">
                <el-table-column width="62" label="序号" fixed="left">
                  <template scope="scope">
                    <div v-if="records[scope.$index].proxyName1!='当页总额'&&records[scope.$index].proxyName1!='筛选条件统计'">{{scope.$index+1}}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="appId" label="业务方" min-width="85"></el-table-column>
                <el-table-column label="业务订单号" min-width="112">
                  <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].businessOrderNo" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{records[scope.$index].businessOrderNo|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="交易订单号" min-width="112">
                  <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].orderNo" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{records[scope.$index].orderNo|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="支付流水号" min-width="112">
                  <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].sn" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{records[scope.$index].sn|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" :formatter="changeTime" label="交易日期" width="162"></el-table-column>
                <el-table-column prop="merchantName" label="收款商户名称" min-width="120"></el-table-column>
                <el-table-column prop="proxyName" label="所属一级" min-width="90"></el-table-column>
                <el-table-column prop="proxyName1" label="所属二级" min-width="110"></el-table-column>
                <el-table-column prop="tradeAmount" :formatter="changeNum" label="支付金额" min-width="120" align="right"></el-table-column>
                <el-table-column label="手续费率" min-width="90" align="right">
                  <template scope="scope">
                    <div v-if="records[scope.$index].proxyName1!='当页总额'&&records[scope.$index].proxyName1!='筛选条件统计'">{{records[scope.$index].payRate}}</div>
                    <a v-if="records[scope.$index].proxyName1=='筛选条件统计'" @click="add">点击统计</a>
                  </template>
                </el-table-column>
                <el-table-column label="订单状态" min-width="90">
                  <template scope="scope">
                    <div v-if="records[scope.$index].proxyName1!='当页总额'&&records[scope.$index].proxyName1!='筛选条件统计'">{{records[scope.$index].status|changeStatus}}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="settleStatus" :formatter="changeSettleStatus" label="结算状态" min-width="90"></el-table-column>
                <el-table-column prop="payType" label="支付方式" min-width="115"></el-table-column>
                <el-table-column prop="payChannelSigns" label="支付渠道" min-width="115"></el-table-column>
                <el-table-column prop="remark" label="渠道信息" min-width="90"></el-table-column>
                <el-table-column label="操作" width="90" fixed="right">
                  <template scope="scope">
                    <router-link :to="{path:'/admin/record/newDealDet',query:{orderNo:records[scope.$index].orderNo}}" v-if="records[scope.$index].proxyName1!='当页总额'&&records[scope.$index].proxyName1!='筛选条件统计'" type="text" size="small">详情
                    </router-link>
                  </template>
                </el-table-column>
              </el-table>
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
                             :total="count">
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
  import store from '../store'
  export default {
    data () {
      return {
        query:{
          page:1,
          size:10,
          orderNo:'',
          businessOrderNo:'',
          sn:'',
          merchantName: '',
          startTime: '',
          endTime: '',
          lessTotalFee: '',
          moreTotalFee: '',
          status: '',
          settleStatus:'',
          payType:'',
          proxyName:'',
          proxyName1:''
        },
      }
    },
    created(){
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
