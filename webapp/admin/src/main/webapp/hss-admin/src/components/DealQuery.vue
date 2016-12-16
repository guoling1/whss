<template lang="html">
  <div id="dale">
    <h1>交易查询</h1>
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
            <div class="number">
              <label for="orderId">交易流水号：</label>
              <input id="orderId" type="text" name="number" v-model="$$data.orderId">
            </div>
            <div class="date">
              <label for="date">交易日期：</label>
              <input id="date" type="text" name="date" value="" v-model="$$data.startTime" placeholder="如：20161001">至
              <input type="text" name="date" value="" v-model="$$data.endTime" placeholder="如：20161001">
            </div>
            <div class="number">
              <label for="merchantId">商户编号：</label>
              <input type="text" name="merchantId" v-model="$$data.merchantId">
            </div>
            <div class="name">
              <label for="reserveMobile">商户名称：</label>
              <input type="text" name="name" v-model="$$data.subName">
            </div>
            <div class="phone">
              <label for="phone">商户手机号：</label>
              <input type="text" name="phone" v-model="$$data.mdMobile">
            </div>
            <div class="price">
              <label for="price">交易金额：</label>
              <input type="text" name="price" value="" v-model="$$data.lessTotalFee">至
              <input type="text" name="price" value="" v-model="$$data.moreTotalFee">
            </div>
            <!--<div class="pay">
              <label for="">
              订单状态：</label>
              <input type="radio" name="pay" value="0" v-model="$$data.status">已删除
              <input type="radio" name="pay" value="1" v-model="$$data.status">正常

            </div>-->
            <div class="assount">
              <label for="">
              结算状态：</label>
              <input type="radio" name="payResult" value="N" v-model="$$data.payResult">未结算
              <input type="radio" name="payResult" value="S" v-model="$$data.payResult">已结算
              <input type="radio" name="payResult" value="F" v-model="$$data.payResult">失败
              </label>
            </div>
            <div class="fun">
              <label for="">业务：
                <select class="fun" name="" v-model="$$data.payChannel">
                  <option value="">全部</option>
                  <option value="101">微信</option>
                  <option value="102">支付宝</option>
                  <option value="103">快捷</option>
                </select>
              </label>
            </div>
            <div class="btn btn-primary" @click="lookup">
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
        <h3 class="box-title">交易记录</h3>
        <button @click="load" class="btn btn-primary" style="float: right">导出</button>
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
                      aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">交易流水号
                  </th>
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                      aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">交易日期
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">商户编号
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">商户名称
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">商户手机号
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">交易金额
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">订单状态
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">结算状态
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">业务
                  </th>
                  <!--<th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">详情-->
                  <!--</th>-->
                </tr>
                </thead>
                <tbody id="content">
                <tr role="row" v-for="order in orders">
                  <td>{{order.orderId}}</td>
                  <td>{{order.updateTime|changeTime}}</td>
                  <td>{{order.merchantId}}</td>
                  <td>{{order.subName}}</td>
                  <td>{{order.mdMobile}}</td>
                  <td>{{order.totalFee}}</td>
                  <td>{{order.status|changeStatus}}</td>
                  <td>{{order.payResult|changePayResult}}</td>
                  <td>{{order.payChannel|changeChannel}}</td>
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
    <div class="login" v-if="isLogin">
      <form class="" action="index.html" method="post">
        <label for="phone">
          手机号:
          <input type="text" name="phone" placeholder="请输入手机号" v-model="phone">
          <!-- <span class='btn btn-primary'>获取验证码</span> -->
        </label>
        <label for="phone">
          密码:
          <input type="password" name="password" placeholder="请输入密码" v-model="password">
        </label>
        <div class="btn btn-primary sub" @click="login">登 录</div>
      </form>

    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'deal',
    data () {
      return {
        phone: '',
        password: '',
        isLogin: false,
        msg:{
          orderId:'',
          startTime: '',
          endTime: '',
          merchantId: '',
          subName: '',
          lessTotalFee: '',
          moreTotalFee: '',
          status:2,
          payResult: '',
          payChannel: '',
          reserveMobile:'',
          pageNo:1,
          pageSize:10,
          saveUrl:'D:'
        },
        orders:[],
        total:''
      }
    },
    created:function(){
      this.$http.post('/queryOrderRecord/selectOrderRecordByConditions',this.$data.msg)
        .then(function (res) {
          this.$data.orders=res.data.records;
          this.$data.total=res.data.totalPage;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a></li>'
          for (var i=1; i<=this.$data.total;i++){
            if(i==res.data.pageNO){
              str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
              continue;
            }
            str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
          }
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a></li>'
          page.innerHTML=str;
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      //分页器
      bindEvent: function (e) {
        e = e||window.event;
        var tar = e.target||e.srcElement,
          tarInn = tar.innerHTML,
          n = this.$data.msg.pageNo;
        if(tarInn == 'Previous'){
          if(n == 1){
            tar.parentNode.className+=' disabled'
            return;
          }
          n--;
        }
        if(tarInn == 'Next'){
          if(n == this.$data.total){
            tar.parentNode.className+=' disabled'
            return;
          }
          n++;
        }
        if(Number(tarInn)){
          if(n == Number(tarInn)){
            return;
          }
          tar.parentNode.className+=' active'
          n = Number(tarInn);
        }
        this.$data.msg.pageNo = n;
        this.$http.post('/queryOrderRecord/selectOrderRecordByConditions',this.$data.msg)
          .then(function (res) {
            this.$data.orders=res.data.records;
            this.$data.total=res.data.totalPage;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==res.data.pageNO){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a></li>'
            page.innerHTML=str;
          },function (err) {
            console.log(err)
          })
      },
      login: function () {
        this.$http.post('/admin/login', {
          mobile: this.$data.phone,
          password: this.$data.password
        }).then(function () {
          this.$data.isLogin = false;
        }, function () {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.body.message
          })
        })
      },
      //筛选
      lookup: function () {
        var time = this.$data.msg.startTime;
        if(this.$data.msg.startTime!='' && (/\d{8}/.test(time))){
          this.$data.msg.startTime = time.substring(4,0)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" 00:00:00";
        }
        var time = this.$data.msg.endTime;
        if(this.$data.msg.endTime!=''&& (/\d{8}/.test(time))){
          this.$data.msg.endTime = time.substring(4,0)+"-"+time.substring(4,6)+"-"+time.substring(6,8)+" 24:00:00";
        }
        this.$http.post('/queryOrderRecord/selectOrderRecordByConditions',this.$data.msg)
          .then(function (res) {
            this.$data.orders=res.data.records;
            this.$data.total=res.data.totalPage;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==res.data.pageNO){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
            page.innerHTML=str;
          },function (err) {
            console.log(err)
          })
      },
      load: function () {
        this.$http.post('/export/exportOrderRecord',this.$data.msg)
          .then(function (res) {
            console.log(res)
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '已成功导出到D盘'
            })
          },function (err) {
            console.log(err)
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.body.message
            })
          })
      }
    },
    computed: {
      $$data: function () {
        return this.$data.msg
      }
    },
    filters: {
      changeStatus: function (val) {
        if(val == 0){
          return '正常'
        }else if(val == 1){
          return '已删除'
        }
      },
      changePayResult: function (val) {
        if(val == 'S'){
          return '已结算'
        }else if(val == 'N'){
          return '未结算'
        }else if(val =='F'){
          return '失败'
        }
      },
      changeChannel: function (val) {
        if(val == 101){
          return '微信'
        }else if(val == 102){
          return '支付宝'
        }else if(val == 103){
          return '快捷'
        }
      },
      changeTime: function (val) {
        if(val!=''){
          val = new Date(val)
          var year=val.getFullYear();
          var month=val.getMonth()+1;
          var date=val.getDate();
          var hour=val.getHours();
          var minute=val.getMinutes();
          var second=val.getSeconds();
          return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
        }
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

  .dale {
    float: right;
    width: 80%;
  }

  .search div {
    float: left;
    height: 34px;
    margin-right: 22px;
    margin-top: 10px;

  &
  .date input,

  &
  .price input,

  &
  .card input {
    width: 50px;
  }

  &
  .assount input,

  &
  .pay input {
    width: 20px;
  }

  &
  .btn {
    margin: 0 0 5px 0;
    width: 80px;
    float: right;
  }

  }
  .table {
    overflow: hidden;

  td, th {
    text-align: center;
    width: 10%;
  }

  tr:nth-child(2) {
    background: #ccc;
  }

  }
  .login {
    z-index: 1000;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: #fff;

  form {
    margin: 0 auto;
    margin-top: 200px;
    padding: 50px;
    width: 450px;
    height: 400px;
    border: 2px solid #ccc;

  label {
    font-size: 18px;
    height: 50px;
    line-height: 30px;
  }

  }
  .sub {
    margin-top: 50px;
    width: 100%;
    height: 50px;
    line-height: 35px;
    font-size: 20px;
  }

  }
</style>
