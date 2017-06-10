<template lang="html">
  <div id="agentAdd">
    <div class="col-xs-12">
      <div class="box">
        <div class="box-header">
          <h3 class="box-title">产品列表</h3>
          <a @click="_$power(issue,'boss_product_add')" class="pull-right btn btn-primary">
            新增产品
          </a>
        </div>
        <div class="box-body">
          <el-table max-height="637" style="font-size: 12px;margin-bottom: 15px;width: 50%" :data="products" border>
            <el-table-column prop="productName" label="产品名称"></el-table-column>
            <el-table-column label="通道配置" min-width="100">
              <template scope="scope">
                <el-button type="text" @click="detail(scope.$index)" v-if="scope.row.type=='hss'">查看详情</el-button>
                <el-button type="text" @click="detailHsy(scope.$index)" v-if="scope.row.type=='hsy'">查看详情</el-button>
              </template>
            </el-table-column>
            <el-table-column label="网关配置" min-width="100">
              <template scope="scope">
                <el-button type="text" @click="setup(scope.$index)" v-if="scope.row.type=='hss'">配置</el-button>
              </template>
            </el-table-column>
          </el-table>
          </el-table>
          <!--<div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div class="row">
              <div class="col-sm-6"></div>
              <div class="col-sm-6"></div>
            </div>
            <div class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                  <thead>
                  <tr role="row">
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">
                      项目类型
                    </th>
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">
                      产品名称
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">支付手续费加价限额
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">提现手续费加价限额
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">商户提现模式
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">代理商结算模式
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作
                    </th>
                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" class="odd" v-for="(product,index) in products">
                    <td class="sorting_1">{{product.type|changeType}}</td>
                    <td class="sorting_1">{{product.productName}}</td>
                    <td>{{product.limitPayFeeRate}}</td>
                    <td>{{product.limitWithdrawFeeRate}}</td>
                    <td>{{product.merchantWithdrawType|changeDrawType}}</td>
                    <td>{{product.dealerBalanceType|changeBalanceType}}</td>
                    <td><a @click="detail(index)">查看详情</a></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>-->
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'dale',
    data () {
      return {
        mobile: '',
        name: '',
        belongArea: '',
        bankCard: '',
        bankAccountName: '',
        bankReserveMobile: '',
        product: {
          channels: []
        },
        products: [],//所有产品
        id: 0
      }
    },
    created: function () {
      this.$http.post('/admin/product/list')
        .then(function (res) {
          this.$data.products = res.data;
          for (let i = 0; i < this.$data.products.length; i++) {
            for (let j = 0; j < this.$data.products[i].list.length; j++) {
              this.$data.products[i].list[j].paymentSettleRate = '';
              (this.$data.products[i].list)[j].withdrawSettleFee = '';
              (this.$data.products[i].list)[j].merchantSettleRate = '';
              (this.$data.products[i].list)[j].merchantWithdrawFee = '';
            }
          }
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      issue: function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/productAdd')
//        this.$router.push('/admin/record/productAdd')
      },
      setup:function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/gateway')
//        this.$router.push('/admin/record/gateway')
      },
      create: function () {
        this.$data.product.channels = [];
        this.$data.product.productId = this.$data.products[this.$data.id].productId;
        for (let i = 0; i < this.$data.products[this.$data.id].list.length; i++) {
          this.$data.product.channels.push({
            channelType: this.$data.products[this.$data.id].list[i].channelTypeSign,
            paymentSettleRate: this.$data.products[this.$data.id].list[i].paymentSettleRate,
            settleType: this.$data.products[this.$data.id].list[i].productBalanceType,
            withdrawSettleFee: this.$data.products[this.$data.id].list[i].withdrawSettleFee,
            merchantSettleRate: this.$data.products[this.$data.id].list[i].merchantSettleRate,
            merchantWithdrawFee: this.$data.products[this.$data.id].list[i].merchantWithdrawFee
          })
        }
        let query = {
          mobile: this.$data.mobile,
          name: this.$data.name,
          belongArea: this.$data.belongArea,
          bankCard: this.$data.bankCard,
          bankAccountName: this.$data.bankAccountName,
          bankReserveMobile: this.$data.bankReserveMobile,
          product: this.$data.product
        };
        this.$http.post('/admin/user/addFirstDealer', query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: "添加成功"
            })
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      },
      detail: function (val) {
        window.open('http://admin.qianbaojiajia.com/admin/details/productAdd?id='+val);
//        this.$router.push({path:'/admin/record/productAdd',query:{id:val}})
      },
      detailHsy: function (val) {
        window.open('http://admin.qianbaojiajia.com/admin/details/productAddHsy?id='+val);
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
      changeType: function (val) {
        if(val=='hss'){
          val='好收收'
        }else if(val=='hsy'){
          val = '好收银'
        }
        return val;
      },
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

<!-- Add "scoped" attribute to limit CSS to this component only -->
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

  .btn{
    font-size: 12px;
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
</style>
