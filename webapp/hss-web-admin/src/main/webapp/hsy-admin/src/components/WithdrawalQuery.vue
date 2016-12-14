<template lang="html">
  <div id="dale">
    <h1>提现查询</h1>
    <div class="col-md-12">
      <div class="box box-success box-solid">
        <div class="box-header with-border">
          <h3 class="box-title">筛选条件</h3>

          <div class="box-tools pull-right">
            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
            </button>
          </div>
          <!-- /.box-tools -->
        </div>
        <!-- /.box-header -->
        <div class="box-body">
          <div class="search">
            <div class="date">
              <label for="date">提现日期：</label>
              <input type="text" name="date" value="">至
              <input type="text" name="date" value="">
            </div>
            <div class="codeNumber">
              <label for="codeNumber">设备编号：</label>
              <input type="text" name="codeNumber" placeholder="例如二维码编号">
            </div>
            <div class="number">
              <label for="number">商户编号：</label>
              <input type="text" name="number">
            </div>
            <div class="name">
              <label for="name">商户名称：</label>
              <input type="text" name="name">
            </div>
            <div class="phone">
              <label for="phone">商户手机号：</label>
              <input type="text" name="phone">
            </div>
            <div class="price">
              <label for="price">提现金额：</label>
              <input type="text" name="price" value="">至
              <input type="text" name="price" value="">
            </div>
            <div class="pay">
              <label for="pay">订单状态：
                <input type="checkbox" name="pay" value="">待支付
                <input type="checkbox" name="pay" value="">支付成功</label>
            </div>
            <div class="assount">
              <label for="assount">结算状态：
                <input type="checkbox" name="assount" value="">未结算
                <input type="checkbox" name="assount" value="">已结算</label>
            </div>
            <div class="fun">
              <label for="">业务：
                <select class="fun" name="">
                  <option value="all">全部</option>
                  <option value="wx">微信</option>
                  <option value="zfb">支付宝</option>
                  <option value="kj">快捷</option>
                </select>
              </label>
            </div>
            <div class="card">
              <label for="card">卡号后四位：</label>
              <input type="text" name="name">
            </div>
            <div class="btn btn-primary">
              筛选
            </div>
          </div>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
    <div class="box" style="overflow: hidden">
      <div class="box-header">
        <h3 class="box-title">提现记录</h3>
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
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                      aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">提现日期
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">设备编号
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">商户编号
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">商户名称
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">商户手机号
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">提现金额
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">订单状态
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">结算状态
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">业务
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">卡号
                  </th>
                </tr>
                </thead>
                <tbody>
                <tr role="row" class="odd">
                  <td>2016.11.28</td>
                  <td>10000000</td>
                  <td>10000000</td>
                  <td>大润发</td>
                  <td>13011111111</td>
                  <td>100.25</td>
                  <td>未支付</td>
                  <td>已结算</td>
                  <td>微信</td>
                  <td>62230000000000</td>
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
                <ul class="pagination">
                  <li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a>
                  </li>
                  <li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">1</a></li>
                  <li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a></li>
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
  name: 'dale',
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

a {
  color: #42b983;
}
.dale{
  float: right;
  width: 80%;
}
.search div{
  float: left;
  height: 34px;
  margin-right: 22px;
  margin-top: 10px;
  input{
    width: 100px
  }
  &.date input,&.price input,&.card input{
    width: 50px;
  }
  &.assount input,&.pay input{
    width: 20px;
  }
  &.btn{
    margin: 0 0 5px 0;
    width: 80px;
    float: right;
  }
}
.table{
  overflow: hidden;
  td,th{
    text-align: center;
    width: 10%;
  }
  tr:nth-child(2){
    background: #ccc;
  }
}
</style>
