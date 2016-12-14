<template lang="html">
  <div id="agentAdd">
    <h1>通道列表</h1>

    <div class="col-xs-12">
      <div class="box">
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
                    <td>{{product.basicTradeRate}}</td>
                    <td>{{product.basicWithdrawFee}}</td>
                    <td>{{product.basicBalanceType|changeBalanceType}}</td>
                    <td><div class="btn btn-primary" @click="detail(index)">修改</div></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="row">
              <div class="col-sm-5">
              </div>
              <div class="col-sm-7">
                <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                  <ul class="pagination" id="page">
                    <li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a>
                    </li>
                    <li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">1</a></li>
                    </li>
                    <li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a></li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>

    <div class="btn btn-primary" @click="create">
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

  a {
    color: #42b983;
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
</style>
