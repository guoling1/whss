<template lang="html">
  <div id="companyProfitDet">
    <h1>公司分润详情</h1>
    <div class="search">
      <span>日期：</span>
      <input type="datetime-local">
      <span>至</span>
      <input type="datetime-local">
      <div class="btn btn-primary" @click="lookup">筛选</div>
    </div>
    <div class="box">
      <div class="box-header">
        <h3 class="box-title">交易记录</h3>
        <!--<button @click="load" class="btn btn-primary" style="float: right">导出</button>-->
      </div>
      <!-- /.box-header -->
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
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">序号
                  </th>
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                      aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">流水号
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">业务类型
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">交易日期
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">结算周期
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">商户名称
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">上级代理名称
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">交易金额
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">商户手续费
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">分润金额
                  </th>
                </tr>
                </thead>
                <tbody id="content">
                <tr role="row" v-for="(order,index) in orders">
                  <td>{{index+1}}</td>
                  <td>{{order.updateTime|changeTime}}</td>
                  <td>{{order.merchantId}}</td>
                  <td>{{order.subName}}</td>
                  <td>{{order.mdMobile}}</td>
                  <td>{{index+1}}</td>
                  <td>{{order.updateTime|changeTime}}</td>
                  <td>{{order.merchantId}}</td>
                  <td>{{order.subName}}</td>
                  <td>{{order.mdMobile}}</td>
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
                  <!--<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a>
                  </li>
                  <li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">1</a></li>
                  <li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a></li>-->
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- /.box-body -->
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'CompanyProfitDet',
    data () {
      return {

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

  .search{
    margin:20px 0;
    span{
      font-size: 18px;
    }
  }
  .box-title {
    display: inline-block;
    font-size: 20px;
    line-height: 34px;
    height: 34px;
  }
</style>
