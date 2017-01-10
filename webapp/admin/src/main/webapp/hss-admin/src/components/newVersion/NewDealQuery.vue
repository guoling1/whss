<template lang="html">
  <div id="dale">
    <div class="col-md-12">

      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">交易查询</h3>
          <router-link to="/admin/record/deal" class="pull-right" style="margin-left: 20px">切换旧版</router-link>
        </div>
      <div class="box-body">
        <div class="row">
          <div class="col-md-3">
            <div class="form-group">
              <label>订单号：</label>
              <input type="text" class="form-control" v-model="$$query.orderNo">
            </div>
            <div class="form-group">
              <label>商户名称</label>
              <input type="text" class="form-control" v-model="$$query.merchantName">
            </div>
          </div>
          <div class="col-md-3">
            <div class="form-group">
              <label>交易日期：</label>
              <div class="form-control">
                <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="$$query.startTime">至
                <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="$$query.endTime">
              </div>
            </div>
            <div class="form-group">
              <label>交易金额：</label>
              <div class="form-control">
                <input type="text" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="$$query.lessTotalFee">至
                <input type="text" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="$$query.moreTotalFee">
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="form-group">
              <label>订单状态：</label>
              <select class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true" v-model="$$query.status">
                <option value="">全部</option>
                <option value="1">待支付</option>
                <option value="4">支付成功</option>
                <option value="3">支付失败</option>
              </select>
            </div>
            <div class="form-group">
              <label>结算状态：</label>
              <select class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true" v-model="$$query.settleStatus">
                <option value="">全部</option>
                <option value="1">未结算</option>
                <option value="2">结算中</option>
                <option value="3">已结算</option>
              </select>
              </select>
            </div>
          </div>
          <div class="col-md-3">
            <div class="form-group">
              <label>支付方式：</label>
              <select class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true" v-model="$$query.payType">
                <option value="">全部</option>
                <option value="S">微信扫码</option>
                <option value="N">微信二维码</option>
                <option value="H">微信H5收银台</option>
                <option value="B">快捷收款</option>
                <option value="Z">支付宝扫码</option>
              </select>
            </div>
            <div class="form-group" style="overflow: hidden;margin: 0;margin-top: 36px;">
              <a :href="'http://'+this.$data.url" download="交易记录" class="btn btn-primary" style="float: right;color: #fff">导出</a>
              <div class="btn btn-primary" @click="lookup" style="margin-right:15px;float: right">筛选</div>
            </div>
          </div>
        </div>
        <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
          <div class="row">
            <div class="col-sm-12">
              <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                <thead>
                <tr role="row">
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">序号</th>
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">订单号</th>
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">交易日期</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">商户名称</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">所属一级</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">所属二级</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">支付金额</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">手续费率</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">订单状态</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">结算状态</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付方式</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付渠道</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">渠道信息</th>
                </tr>
                </thead>
                <tbody id="content">
                <tr role="row" v-for="(order,index) in orders">
                  <td>{{(query.page-1)*10+(index+1)}}</td>
                  <td><router-link :to="{ path: '/admin/record/newDealDet', query: {orderNo: order.orderNo}}">{{order.orderNo|changeHide}}</router-link></td>
                  <td>{{order.createTime|changeTime}}</td>
                  <td>{{order.merchantName}}</td>
                  <td>{{order.proxyName}}</td>
                  <td>{{order.proxyName1}}</td>
                  <td style="text-align: right">{{order.tradeAmount|toFix}}</td>
                  <td>{{order.payRate}}</td>
                  <td>{{order.status|changeStatus}}<!--<a href="javascript:;">(补发)</a>--></td>
                  <td>{{order.settleStatus|changeSettleStatus}}</td>
                  <td>{{order.payType|changePayType}}</td>
                  <td>{{order.payChannelSign|changePayChannel}}</td>
                  <td>{{order.remark}}</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div v-if="orders.length==0" class="row" style="text-align: center;color: red;font-size: 16px;">
            <div class="col-sm-12">无此数据</div>
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
                <span class="count">共{{count}}条</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
      <!--登录页面-->
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
        query:{
          page:1,
          size:10,
          orderNo:'',
          merchantName: '',
          startTime: '',
          endTime: '',
          lessTotalFee: '',
          moreTotalFee: '',
          status: '',
          settleStatus:'',
          payType:''
        },
        orders:[],
        total:'',
        url:'',
        count:0
      }
    },
    created:function(){
      this.$http.post('/admin/queryOrder/orderList',this.$data.query)
        .then(function (res) {
          this.$data.orders=res.data.records;
          this.$data.total=res.data.totalPage;
          this.$data.url=res.data.ext;
          this.$data.count = res.data.count;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
          if(this.$data.total<=10){
            for (var i = 1; i <= this.$data.total; i++){
              if(i == this.$data.query.page){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
          }else {
            if(this.$data.query.page<6){
              for (var i = 1; i <= 10; i++){
                if(i == this.$data.query.page){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else if(this.$data.query.page>=6&&this.$data.query.page<=(this.$data.total-5)){
              console.log(this.$data.query.page)
              for (var i = this.$data.query.page-4; i <= this.$data.query.page+5; i++){
                if(i == this.$data.query.page){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else if(this.$data.query.page>=6&&this.$data.query.page>this.$data.total-5){
              console.log(2)
              for (var i = this.$data.total-9; i <= this.$data.total; i++){
                if(i == this.$data.query.page){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }}
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      refresh: function () {
        location.reload()
      },
      //分页器
      bindEvent: function (e) {
        e = e||window.event;
        var tar = e.target||e.srcElement,
          tarInn = tar.innerHTML,
          n = this.$data.query.page;
        if(tarInn == '上一页'){
          if(n == 1){
            tar.parentNode.className+=' disabled'
            return;
          }
          n--;
        }
        if(tarInn == '下一页'){
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
        this.$data.query.page = n;
        this.$http.post('/admin/queryOrder/orderList',this.$data.query)
          .then(function (res) {
            this.$data.orders=res.data.records;
            this.$data.total=res.data.totalPage;
            this.$data.url=res.data.ext;
            this.$data.count = res.data.count;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            if(this.$data.total<=10){
              for (var i = 1; i <= this.$data.total; i++){
                if(i == this.$data.query.page){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else {
              if(this.$data.query.page<6){
                for (var i = 1; i <= 10; i++){
                  if(i == this.$data.query.page){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.page>=6&&this.$data.query.page<=(this.$data.total-5)){
                console.log(this.$data.query.page)
                for (var i = this.$data.query.page-4; i <= this.$data.query.page+5; i++){
                  if(i == this.$data.query.page){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.page>=6&&this.$data.query.page>this.$data.total-5){
                console.log(2)
                for (var i = this.$data.total-9; i <= this.$data.total; i++){
                  if(i == this.$data.query.page){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }}
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
            page.innerHTML=str;
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
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
        this.$data.query.page = 1;
        this.$http.post('/admin/queryOrder/orderList',this.$data.query)
          .then(function (res) {
            this.$data.orders=res.data.records;
            this.$data.total=res.data.totalPage;
            this.$data.url=res.data.ext;
            this.$data.count = res.data.count;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            if(this.$data.total<=10){
              for (var i = 1; i <= this.$data.total; i++){
                if(i == this.$data.query.page){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else {
              if(this.$data.query.page<6){
                for (var i = 1; i <= 10; i++){
                  if(i == this.$data.query.page){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.page>=6&&this.$data.query.page<=(this.$data.total-5)){
                console.log(this.$data.query.page)
                for (var i = this.$data.query.page-4; i <= this.$data.query.page+5; i++){
                  if(i == this.$data.query.page){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.page>=6&&this.$data.query.page>this.$data.total-5){
                console.log(2)
                for (var i = this.$data.total-9; i <= this.$data.total; i++){
                  if(i == this.$data.query.page){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }}
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
            page.innerHTML=str;
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      }
    },
    computed: {
      $$query: function () {
        return this.$data.query
      }
    },
    filters: {
      changePayType: function (val) {
        if(val == "S"){
          return "微信扫码"
        }else if(val == "N"){
          return "微信二维码"
        }else if(val == "H"){
          return "微信H5收银台"
        }else if(val == "B"){
          return "快捷收款"
        }else if(val == "Z"){
          return "支付宝扫码"
        }
      },
      changeStatus: function (val) {
        if(val == 1){
          return "待支付"
        }else if(val == 3){
          return "支付失败"
        }else if(val == 4){
          return "支付成功"
        }else if(val == 5){
          return "提现中"
        }else if(val == 6){
          return "提现成功"
        }else if(val == 7){
          return "充值成功"
        }else if(val == 6){
          return "充值失败"
        }
      },
      changeSettleStatus: function (val) {
        if(val == 2){
          return '结算中'
        }else if(val == 1){
          return '待结算'
        }else if(val == 3){
          return '已结算'
        }
      },
      changePayChannel: function (val) {
        if(val == 101){
          return '阳光微信扫码'
        }else if(val == 102){
          return '阳光支付宝扫码'
        }else if(val == 103){
          return '阳光银联支付'
        }
      },
      changeTime: function (val) {
        if(val==''||val==null){
          return ''
        }else {
          val = new Date(val)
          var year=val.getFullYear();
          var month=val.getMonth()+1;
          var date=val.getDate();
          var hour=val.getHours();
          var minute=val.getMinutes();
          var second=val.getSeconds();
          function tod(a) {
            if(a<10){
              a = "0"+a
            }
            return a;
          }
          return year+"-"+tod(month)+"-"+tod(date)+" "+tod(hour)+":"+tod(minute)+":"+tod(second);
        }
      },
      changeHide: function (val) {
        val = val.replace(val.substring(3,19),"…");
        return val
      },
      toFix: function (val) {
        return parseFloat(val).toFixed(2);
      },
      changeName: function (val) {
        if(val==null){
          return "无"
        }else {
          return val
        }
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
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
  .form-control{
    height: 29px;
    line-height: 25px;
    font-size: 12px;
    padding: 0 6px;
  }
  .count{
    display: inline-block;
    vertical-align: top;
    margin: 28px 10px;
  }
  .table td[data-v-497723e2], .table th[data-v-497723e2]{
    width: inherit;
  }
  .btn{
    font-size: 12px;
  }
</style>
