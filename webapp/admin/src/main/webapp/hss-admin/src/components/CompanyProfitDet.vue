<template lang="html">
  <div id="companyProfitDet">
    <div style="margin: 15px;">
      <div class="box">
        <div class="box-header">
          <h3 class="box-title">分润详情</h3>
        </div>
        <!-- /.box-header -->
        <div class="box-body">
          <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div class="search">
              <div class="form-group" style="margin-bottom: 15px">
                <label>日期：</label>
                <div class="form-control" style="margin-right: 15px;padding: 0;width: 248px;height: 30px;line-height: 26px;">
                  <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="query.beginProfitDate">至
                  <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="query.endProfitDate">
                </div>
              </div>
              <div class="form-group" style="margin-bottom: 15px">
                <label>代理商名称：</label>
                <input style="height: 30px;margin-right: 15px" type="text" class="form-control" v-model="query.dealerName">
              </div>
              <div class="btn btn-primary" @click="search()" style="margin-top: -15px">筛选</div>
            </div>

            <div class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                  <thead>
                  <tr role="row">
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">序号
                    </th>
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">订单号</th>
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">交易类型</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">交易日期</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">结算周期</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">商户名称</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">上级代理名称</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">交易金额</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">商户手续费</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">分润金额</th>
                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" v-for="(record,index) in records">
                    <td>{{index+1}}</td>
                    <td>{{record.paymentSn}}</td>
                    <td>{{record.profitType|changeProfitType}}</td>
                    <td>{{record.profitDate}}</td>
                    <td>{{record.settleDate}}</td>
                    <td>{{record.merchantName}}</td>
                    <td>{{record.dealerName}}</td>
                    <td style="text-align: right">{{record.totalFee|toFix}}</td>
                    <td style="text-align: right">{{record.merchantFee|toFix}}</td>
                    <td style="text-align: right">{{record.shallMoney|toFix}}</td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="row">
              <div class="col-sm-5">
                <div class="dataTables_info" id="example2_info" role="status" aria-live="polite">
                </div>
              </div>
              <div class="col-sm-7">
                <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                  <ul class="pagination" id="page" @click="bindEvent($event)">

                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.box-body -->
      </div>
    </div>
    <message></message>
  </div>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
  export default {
    name: 'companyProfitDet',
    components: {
      Message
    },
    data: function () {
      return {
        query:{
          dailyProfitId:'',
          beginProfitDate:'',
          endProfitDate:'',
          dealerName:''
        },
        records:[],
        path:''
      }
    },
    created: function(){
      this.$data.query.dailyProfitId = this.$route.query.id;
      if(this.$route.path=="/admin/record/companyProfitDet"){
        this.$data.path = '/admin/profit/companyProfit/detail'
      }else if(this.$route.path=="/admin/record/firProfitDet"){
        this.$data.path = '/admin/profit/firstDealer/detail'
      }else if(this.$route.path=="/admin/record/secProfitDet"){
        this.$data.path = '/admin/profit/secondDealer/detail'
      }
      this.$http.post(this.$data.path,this.$data.query)
        .then(function (res) {
          this.$data.records = res.data;
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      search: function () {
        this.$http.post(this.$data.path,this.$data.query)
          .then(function (res) {
            this.$data.record = res.data;
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      }
    },
    filters: {
      changeProfitType: function (val) {
        if(val == 1){
          return '支付'
        }else if(val == 2){
          return '提现'
        }
      },
      toFix: function (val) {
        return parseFloat(val).toFixed(2)
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  .flexBox {
    display: box;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    display: -webkit-flex;
  }
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }
  .btn{
    font-size: 12px;
  }
</style>
