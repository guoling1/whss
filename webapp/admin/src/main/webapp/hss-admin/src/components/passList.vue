<!--<template lang="html">
  <div id="agentAdd">
    <div class="col-xs-12" style="margin-top: 15px">
      <div class="box">
        <div class="box-header">
          <h3 class="box-title">通道列表</h3>
        </div>
        <div class="box-body">
          <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div class="row">
              <div class="col-sm-6"></div>
              <div class="col-sm-6"></div>
            </div>
            <div class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                  <thead>
                  <tr role="row">
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                        aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">
                      通道名称
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">收单机构
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">渠道来源
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">支付费率
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">提现费用
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">结算时间
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作
                    </th>
                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" class="odd" v-for="(product,index) in products">
                    <td class="sorting_1">{{product.channelName}}</td>
                    <td>{{product.thirdCompany}}</td>
                    <td>{{product.channelSource}}</td>
                    <td>{{product.basicTradeRate}}%</td>
                    <td>{{product.basicWithdrawFee}}</td>
                    <td>{{product.basicBalanceType|changeBalanceType}}</td>
                    <td><a @click="detail(index)">修改</a></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
        &lt;!&ndash; /.box-body &ndash;&gt;
      </div>
      &lt;!&ndash; /.box &ndash;&gt;
    </div>

    <div class="btn btn-primary" @click="create" style="margin: 0 15px">
      新增通道
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'dale',
    data () {
      return {
        products: [],//所有通道
        id: 0
      }
    },
    created: function () {
      this.$http.post('/admin/channel/list')
        .then(function (res) {
          this.$data.products = res.data;
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      create: function () {
        this.$router.push('/admin/record/passAdd')
      },
      detail: function (val) {
        console.log(val)
        this.$router.push({path:'/admin/record/passAdd',query:{id:val}})
      }
    },
    computed: {
      $$products: function () {
        return this.$data.products
      },
      $$data: function () {
        return this.$data;
      }
    },
    filters: {
      changeDrawType: function(val){
        if(val=='HAND'){
          val='手动提现'
        }else if(val=='AUTO'){
          val = '自动提现'
        }
        return val;
      },
      changeBalanceType: function (val) {
        if(val=='D0'){
          val='D0'
        }else if(val=='D1'){
          val = '日结'
        }else if(val == 'M1'){
          val = '月结'
        }
        return val;
      }
    }
  }
</script>

&lt;!&ndash; Add "scoped" attribute to limit CSS to this component only &ndash;&gt;
<style scoped lang="less">
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }
  .agentAdd {
    float: right;
    width: 80%;
  }

  .add {
    border: 1px solid #ccc;
    width: 800px;
    border-top: none;
    font-size: 16px;

  th {
    width: 200px;
    vertical-align: middle;
    text-align: center;
  }

  td input {
    border: none;
    width: 600px;
  }

  }

  .product1 {
    display: inline-block;
    width: 90%;
  }

  input.check {
    position: relative;
    top: -50px;
  }

  .product .table {
    display: inline-block;
    font-size: 12px;
    width: 90%;

  thead tr, caption {
    font-size: 8px;
    color: #777;
    background: #ccc
  }

  td {
    width: 16.5%;

  input {
    width: 77%;
    border: none;
  }

  }
  }
  .btn{
    font-size: 12px;
  }
</style>-->
<template>
  <div id="passList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">通道列表</h3>
          <router-link to="/admin/record/passAdd" class="pull-right btn btn-primary" style="margin-left: 20px">新增通道
          </router-link>
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
