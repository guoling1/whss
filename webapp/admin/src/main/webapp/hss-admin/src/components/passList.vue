<template>
  <div id="passList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">通道列表</h3>
          <!--<router-link to="/admin/record/passAdd" class="pull-right btn btn-primary" style="margin-left: 20px">新增通道-->
          <!--</router-link>-->
          <a @click="_$power(issue,'boss_channel_add')" class="pull-right btn btn-primary">
            新增通道
          </a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <!--<ul>
            <li class="same">
              <label>通道名称:</label>
              <el-input style="width: 130px" v-model="query.orderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>通道编码:</label>
              <el-input style="width: 130px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>-->
          <!--表格-->
          <el-table v-loading.body="loading" height="583" style="font-size: 12px;margin-bottom:15px" :data="records" border>
            <el-table-column label="通道名称">
              <template scope="scope">
                <router-link :to="{path:'/admin/record/passAdd',query:{id:scope.$index}}"
                             type="text" size="small">{{records[scope.$index].channelShortName}}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column label="原始名称">
              <template scope="scope">
                <router-link :to="{path:'/admin/record/passAdd',query:{id:scope.$index}}"
                             type="text" size="small">{{records[scope.$index].channelName}}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column prop="channelCode" label="通道编码"></el-table-column>
            <el-table-column prop="thirdCompany" label="收单机构"></el-table-column>
            <el-table-column prop="channelSource" label="渠道来源"></el-table-column>
            <el-table-column label="支付费率">
              <template scope="scope">
                <span>{{records[scope.$index].basicTradeRate}}%</span>
              </template>
            </el-table-column>
            <el-table-column prop="basicBalanceType" label="结算时间"></el-table-column>
            <el-table-column label="结算类型">
              <template scope="scope">
                <span v-if="records[scope.$index].basicSettleType=='AUTO'">通道自动结算</span>
                <span v-if="records[scope.$index].basicSettleType=='SELF'">自主打款结算</span>
              </template>
            </el-table-column>
            <el-table-column prop="thirdCompany" label="支付方式"></el-table-column>
            <el-table-column prop="remarks" label="备注信息"></el-table-column>
          </el-table>
          <!--分页
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.page"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.size"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>-->
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name: 'passList',
    data(){
      return {
        query: {
          page: 1,
          size: 10,
          orderNo: '',
          merchantName: '',
          startTime: '',
          endTime: '',
          lessTotalFee: '',
          moreTotalFee: '',
          status: '',
          settleStatus: '',
          payType: '',
          proxyName: '',
          proxyName1: ''
        },
        date: '',
        records: [],
        count: 0,
        total: 0,
        loading: true,
        url: ''
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      issue: function () {
        this.$router.push('/admin/record/passAdd')
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/channel/list', this.$data.query)
          .then(function (res) {
            this.loading = false;
            this.$data.records = res.data;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      //格式化hss创建时间
      changeTime: function (row, column) {
        var val = row.basicBalanceType;
        if (val == '' || val == null) {
          return ''
        } else {
          val = new Date(val)
          var year = val.getFullYear();
          var month = val.getMonth() + 1;
          var date = val.getDate();
          var hour = val.getHours();
          var minute = val.getMinutes();
          var second = val.getSeconds();

          function tod(a) {
            if (a < 10) {
              a = "0" + a
            }
            return a;
          }

          return year + "-" + tod(month) + "-" + tod(date) + " " + tod(hour) + ":" + tod(minute) + ":" + tod(second);
        }
      },
      changeNum: function (row, column) {
        var val = row.tradeAmount;
        return parseFloat(val).toFixed(2);
      },
      search(){
        this.$data.query.page = 1;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.page = 1;
        this.$data.query.size = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.page = val;
        this.getData()
      },
    },
  }
</script>

<style scoped lang="less" rel="stylesheet/less">
  ul {
    padding: 0;
  }

  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }

  .btn {
    font-size: 12px;
  }

  .price {
    display: inline-block;
    width: 210px;
    height: 30px;
    border-radius: 4px;
    border-color: #bfcbd9;
    position: relative;
    top: 6px;
    input {
      border: none;
      display: inline-block;
      width: 45%;
      height: 25px;
      position: relative;
      top: -3px;
    }
  }

  .price:hover {
    border-color: #20a0ff;
  }

</style>
