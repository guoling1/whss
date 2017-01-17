<template lang="html">
  <div id="payQuery">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">支付查询</h3>
          <span @click="onload()" download="交易记录" class="btn btn-primary" style="float: right;color: #fff">导出</span>
        </div>
      <div class="box-body">
        <div class="row">
          <div class="col-md-3">
            <div class="form-group">
              <label>支付创建日期：</label>
              <div class="form-control">
                <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="$$query.startCreateTime">至
                <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="$$query.endCreateTime">
              </div>
            </div>
            <div class="form-group">
              <label>支付完成日期：</label>
              <div class="form-control">
                <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="$$query.startFinishTime">至
                <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="$$query.endFinishTime">
              </div>
            </div>
          </div>
          <div class="col-md-3">
            <div class="form-group">
              <label>交易单号：</label>
              <input type="text" class="form-control" v-model="$$query.orderNo">
            </div>
            <div class="form-group">
              <label>支付状态：</label>
              <select class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true" v-model="$$query.status">
                <option value="">全部</option>
                <option value="1">待支付</option>
                <option value="2">支付中</option>
                <option value="4">支付成功</option>
                <option value="5">支付失败</option>
              </select>
            </div>
          </div>
          <div class="col-md-3">
            <div class="form-group">
              <label>支付流水号：</label>
              <input type="text" class="form-control" v-model="$$query.sn">
            </div>
          </div>
          <div class="col-md-3">
            <div class="btn btn-primary" @click="lookup" style="margin-top: 22px">筛选</div>
          </div>
        </div>
        <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
          <div class="row">
            <div class="col-sm-12">
              <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                <thead>
                <tr role="row">
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">序号</th>
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">支付流水号</th>
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">订单号</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">支付金额</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">创建时间</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">支付发起时间</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付完成时间</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付渠道</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付方式</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">渠道方</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付账号</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付状态</th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">渠道信息</th>
                  <!--<th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">备注信息</th>-->
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作</th>
                </tr>
                </thead>
                <tbody id="content">
                <tr role="row" v-for="(order,index) in $$orders">
                  <td>{{(query.pageNo-1)*10+(index+1)}}</td>
                  <td>{{order.sn|changeHide}}</td>
                  <td>{{order.orderNo|changeHide}}</td>
                  <td style="text-align: right;">{{order.payAmount}}</td>
                  <td>{{order.createTime|changeTime}}</td>
                  <td>{{order.requestTime|changeTime}}</td>
                  <td>{{order.finishTime|changeTime}}</td>
                  <td>{{order.payChannel}}</td>
                  <td>{{order.payType}}</td>
                  <td>{{order.upperChannel}}</td>
                  <td>{{order.payAccount}}</td>
                  <td>{{order.statusValue}}</td>
                  <td>{{order.message}}</td>
                  <!--<td>{{order.remark}}</td>-->
                  <td><a href="javascript:;" @click="synchro(order.sn)">补单</a></td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div v-if="isShow">
            <img src="http://img.jinkaimen.cn/admin/common/dist/img/ICBCLoading.gif" alt="">
          </div>
          <div v-if="orders.length==0&&!isShow" class="row" style="text-align: center;color: red;font-size: 16px;">
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
                <span class="count">共{{total}}页 {{count}}条</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
    <!--下载-->
    <div class="box box-info mask" v-if="isMask">
      <div class="box-body" style="text-align: center;font-size: 20px;">
        确认下载吗？
      </div>
      <div class="box-footer clearfix" style="border-top: none">
        <a :href="'http://'+$$url" @click="close()" class="btn btn-sm btn-info btn-flat pull-left">下载</a>
        <a href="javascript:void(0)" @click="close()" class="btn btn-sm btn-default btn-flat pull-right">取消</a>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'payQuery',
    data () {
      return {
        phone: '',
        password: '',
        query:{
          pageNo:1,
          pageSize:10,
          sn:'',
          orderNo:'',
          status: '',
          startCreateTime: '',
          endCreateTime: '',
          startFinishTime: '',
          endFinishTime: ''
        },
        orders:[],
        total:0,
        isMask: false,
        url: '',
        count:0,
        isShow:false,
        //正式
        queryUrl:'http://pay.qianbaojiajia.com/order/pay/listOrder',
        excelUrl:'http://pay.qianbaojiajia.com/order/pay/exportExcel',
        syncUrl:'http://pay.qianbaojiajia.com/order/syncPayOrder',
        //测试
        /*queryUrl:'http://192.168.1.20:8076/order/pay/listOrder',
        excelUrl:'http://192.168.1.20:8076/order/pay/exportExcel',
        syncUrl:'http://192.168.1.20:8076/order/syncPayOrder',*/
      }
    },
    created:function(){
      this.$data.isShow = true;
      this.$data.orders='';
      this.$data.total=0;
      this.$data.count = 0;
      this.$http.post(this.$data.queryUrl,this.$data.query)
        .then(function (res) {
          this.$data.isShow = false;
          this.$data.orders=res.data.records;
          this.$data.total=res.data.totalPage;
          this.$data.url=res.data.ext;
          this.$data.count = res.data.count;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
          if(this.$data.total<=10){
            for (var i = 1; i <= this.$data.total; i++){
              if(i == this.$data.query.pageNo){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
          }else {
            if(this.$data.query.pageNo<6){
              for (var i = 1; i <= 10; i++){
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo<=(this.$data.total-5)){
              console.log(this.$data.query.pageNo)
              for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
              console.log(2)
              for (var i = this.$data.total-9; i <= this.$data.total; i++){
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }}
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
        },function (err) {
          this.$data.isShow = false;
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      onload:function () {
        this.$data.isMask = true;
        this.$http.post(this.$data.excelUrl,this.$data.query)
          .then(function (res) {
            this.$data.url = res.data.url;
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
            this.$data.isMask = false;
          })
      },
      close: function () {
        this.$data.isMask = false;
      },
      refresh: function () {
        location.reload()
      },
      //分页器
      bindEvent: function (e) {
        e = e||window.event;
        var tar = e.target||e.srcElement,
          tarInn = tar.innerHTML,
          n = this.$data.query.pageNo;
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
        this.$data.query.pageNo = n;
        this.$http.post(this.$data.queryUrl,this.$data.query)
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
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else {
              if(this.$data.query.pageNo<6){
                for (var i = 1; i <= 10; i++){
                  if(i == this.$data.query.pageNo){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo<=(this.$data.total-5)){
                console.log(this.$data.query.pageNo)
                for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
                  if(i == this.$data.query.pageNo){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
                console.log(2)
                for (var i = this.$data.total-9; i <= this.$data.total; i++){
                  if(i == this.$data.query.pageNo){
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
      //筛选
      lookup: function () {
        this.$data.query.pageNo = 1;
        if(this.$data.query.startCreateTime!=""&&this.$data.query.endCreateTime==""){
          var date = new Date();
          var month = date.getMonth() + 1;
          var strDate = date.getDate();
          if (month >= 1 && month <= 9) {
            month = "0" + month;
          }
          if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
          }
          this.$data.query.endCreateTime = date.getFullYear() + "-" + month + "-" + strDate;
        }
        if(this.$data.query.startFinishTime!=""&&this.$data.query.endFinishTime==""){
          var date = new Date();
          var month = date.getMonth() + 1;
          var strDate = date.getDate();
          if (month >= 1 && month <= 9) {
            month = "0" + month;
          }
          if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
          }
          this.$data.query.endFinishTime = date.getFullYear() + "-" + month + "-" + strDate;
        }
        if((this.$data.query.startCreateTime==""&&this.$data.query.endCreateTime!="")||(this.$data.query.startFinishTime==""&&this.$data.query.endFinishTime!="")){
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: "请输入开始时间"
          })
        }else {
          this.$data.isShow = true;
          this.$data.orders='';
          this.$data.total=0;
          this.$data.count = 0;
          this.$http.post(this.$data.queryUrl,this.$data.query)
            .then(function (res) {
              this.$data.isShow = false;
              this.$data.orders=res.data.records;
              this.$data.total=res.data.totalPage;
              this.$data.url=res.data.ext;
              this.$data.count = res.data.count;
              var str='',
                page=document.getElementById('page');
              str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
              if(this.$data.total<=10){
                for (var i = 1; i <= this.$data.total; i++){
                  if(i == this.$data.query.pageNo){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else {
                if(this.$data.query.pageNo<6){
                  for (var i = 1; i <= 10; i++){
                    if(i == this.$data.query.pageNo){
                      str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                      continue;
                    }
                    str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                  }
                }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo<=(this.$data.total-5)){
                  console.log(this.$data.query.pageNo)
                  for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
                    if(i == this.$data.query.pageNo){
                      str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                      continue;
                    }
                    str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                  }
                }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
                  console.log(2)
                  for (var i = this.$data.total-9; i <= this.$data.total; i++){
                    if(i == this.$data.query.pageNo){
                      str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                      continue;
                    }
                    str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                  }
                }}
              str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
              page.innerHTML=str;
            },function (err) {
              this.$data.isShow = false;
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        }
      },
      //补单
      synchro: function (val) {
        console.log(val)
        this.$http.post(this.$data.syncUrl,{sn:val})
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: res.msg
            })
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
      },
      $$orders: function () {
        return this.$data.orders
      },
      $$url: function () {
        return this.$data.url
      }
    },
    filters: {
      changeStatus: function (val) {
        if(val == 1){
          return '待支付'
        }else if(val == 1){
          return '支付中'
        }else if(val == 4){
          return '支付成功'
        }else if(val == 5){
          return '支付失败'
        }
      },
      changePayChannel: function (val) {
        if(val == 101){
          return '微信'
        }else if(val == 102){
          return '支付宝'
        }else if(val == 103){
          return '快捷'
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
        if(val!=""&&val!=null){
          val = val.replace(val.substring(3,val.length-6),"…");
        }
        return val
      },
      toFix: function (val) {
        return parseFloat(val).toFixed(2);
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
  .mask{
    width: 20%;
    position: fixed;
    top: 30%;
    left: 46%;
    box-shadow: 0 0 15px #000;
  }
  .form-control{
    height: 29px;
    line-height: 25px;
    font-size: 12px;
    padding: 0 6px;
  }
  .table td[data-v-497723e2], .table th[data-v-497723e2]{
    width: inherit;
  }
  .count{
    display: inline-block;
    vertical-align: top;
    margin: 28px 10px;
  }
  .btn{
    font-size: 12px;
  }
  img{
    width: 8%;
    margin: 0 auto;
    display: inherit;
  }
</style>
