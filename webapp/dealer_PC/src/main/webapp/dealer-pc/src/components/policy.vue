<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">代理政策</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">
                <el-tab-pane label="好收收" name="first">好收收</el-tab-pane>
                <el-tab-pane label="好收银" name="second">好收银</el-tab-pane>
              </el-tabs>
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
        activeName: 'first',
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
//this.getData();
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
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
