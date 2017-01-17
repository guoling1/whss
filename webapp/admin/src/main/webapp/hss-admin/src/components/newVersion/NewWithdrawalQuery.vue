<template lang="html">
  <div id="withDrawal">
    <div style="margin: 15px">
      <div class="box" style="overflow-x: hidden;">
        <div class="box-header">
          <h3 class="box-title">打款查询</h3>
          <router-link to="/admin/record/withdrawal" class="  pull-right" style="margin-left: 20px">切换旧版</router-link>
        </div>
        <div class="box-body">
          <div class="row">
            <div class="col-md-2">
              <div class="form-group">
                <label for="date">打款流水号：</label>
                <input type="text" class="form-control" name="date" value="" v-model="$$data.query.sn">
              </div>
            </div>
            <div class="col-md-2">
              <div class="form-group">
                <label for="number">交易单号：</label>
                <input type="text" class="form-control" name="number" v-model="$$data.query.orderNo">
              </div>
            </div>
            <div class="col-md-2">
              <div class="form-group">
                <label for="codeNumber">收款账户名：</label>
                <input type="text" name="codeNumber" class="form-control" v-model="$$data.query.userName">
              </div>
            </div>
            <div class="col-md-2">
              <div class="form-group">
                <label>状态：</label>
                <select class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true" v-model="$$data.query.status">
                  <option value="">全部</option>
                  <option value="1">待提现</option>
                  <option value="2">打款中</option>
                  <option value="4">打款成功</option>
                  <option value="5">打款失败</option>
                </select>
              </div>
            </div>
            <div class="col-md-4" style="margin-top: 20px;">
              <div class="form-group">
                <div class="btn btn-primary" @click="lookup">筛选</div>
                <span @click="onload()" download="交易记录" class="btn btn-primary" style="float: right;color: #fff">导出</span>
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
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">打款流水号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">交易单号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">打款时间</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">用户名</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">收款银行账号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">打款金额</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付状态</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">打款通道</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">渠道信息</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">备注信息</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr role="row" v-for="(record,index) in $$records">
                    <td>{{(query.pageNo-1)*10+(index+1)}}</td>
                    <td id="td" @mouseover.self="mouseover($event)" @mouseout.self="mouseout($event)">{{record.sn|changeHide}}
                      <span style="display: none">{{record.sn}}</span>
                    </td>
                    <td>{{record.orderNo|changeHide}}</td>
                    <td>{{record.requestTime|changeTime}}</td>
                    <td>{{record.receiptUserName}}</td>
                    <td>{{record.bankCard|changeHide}}</td>
                    <td style="text-align: right;">{{record.amount}}</td>
                    <td>{{record.statusValue}}</td>
                    <td>{{record.playMoneyChannel}}</td>
                    <td>{{record.message}}</td>
                    <td>{{record.remark|changeHide}}</td>
                    <td>
                      <router-link :to="{path:'/admin/record/withdrawalAudit',query:{orderNo:record.orderNo,sn:record.sn,requestTime:record.requestTime,amount:record.amount,receiptUserName:record.receiptUserName,playMoneyChannel:record.playMoneyChannel,status:record.status,bankCard:record.bankCard,message:record.message}}" id="audit" v-if="(record.status=='2'||record.status=='3'||record.status=='5')&&record.auditStatus==0" >审核</router-link>
                      <span v-if="record.status=='5'&&record.auditStatus!=0">{{record.auditStatusValue}}</span>
                      <a href="javascript:;" @click="updata(record.sn)">同步</a>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-if="isShow">
              <img src="http://img.jinkaimen.cn/admin/common/dist/img/ICBCLoading.gif" alt="">
            </div>
            <div v-if="records.length==0&&!isShow" class="row" style="text-align: center;color: red;font-size: 16px;">
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
    name: 'withDrawal',
    data () {
      return {
        msg:{
          page:1,//页数
          size:10,//条数
          name:'',
          orderId:'',//流水号
          bankNoShort:'',
          payResult:'',//支付状态
          merchantId:'',
          mobile:'',
          startDate:'',
          endDate:'',
          payStartDate:'',
          payEndDate:''
        },
        query:{
          pageNo:"1",
          pageSize:"10",
          orderNo:'',
          sn:'',
          userName:'',
          status:''
        },
        total:'',
        records:[],
        isShow:false,
        index:0,
        remark:'',
        isMask: false,
        url: '',
        count:0,
        //正式
        queryUrl:'http://pay.qianbaojiajia.com/order/withdraw/listOrder',
         excelUrl:'http://pay.qianbaojiajia.com/order/withdraw/exportExcel',
         syncUrl:'http://pay.qianbaojiajia.com/order/syncWithdrawOrder',
        //测试
        /*queryUrl:'http://192.168.1.20:8076/order/withdraw/listOrder',
        excelUrl:'http://192.168.1.20:8076/order/withdraw/exportExcel',
        syncUrl:'http://192.168.1.20:8076/order/syncWithdrawOrder',
        syncUrl:'http://192.168.1.20:8076/order/syncWithdrawOrder',*/
      }
    },
    created:function () {
      this.$data.isShow = true;
      this.$http.post(this.$data.queryUrl,this.$data.query)
        .then(function (res) {
          this.$data.isShow = false;
          this.$data.records=res.data.records;
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
              for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
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
      mouseover: function (e) {
        /*var obj = e.target;
        obj.style.position='relative'
        var span = e.target.lastElementChild;
        span.style.display="block";
        span.style.position="absolute";
        span.style.bottom='25px';
        span.style.left= '21px';
        span.style.background='#fff';
        span.style.boxShadow= '0 0 10px';
        span.style.padding= '5px';
        span.style.borderRadius= '4px';*/
      },
      mouseout: function (e) {
        /*var obj = e.target;
        e.target.lastElementChild.style.display="none"*/
      },
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
            this.$data.isShow = false;
            this.$data.records=res.data.records;
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
                for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
                  if(i == this.$data.query.pageNo){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
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
        this.$data.records="";
        this.$data.total=0;
        this.$data.count = 0;
        this.$data.isShow = true;
        this.$data.query.pageNo = 1;
        this.$http.post(this.$data.queryUrl,this.$data.query)
          .then(function (res) {
            this.$data.isShow = false;
            this.$data.records=res.data.records;
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
                for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
                  if(i == this.$data.query.pageNo){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
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
      updata: function (val) {
        this.$http.post(this.$data.syncUrl,{sn:val})
          .then(function (res) {
            this.$store.commit('MESSAGE_DELAY_SHOW', {
              text: "同步成功"
            })
          },function (err) {
            this.$store.commit('MESSAGE_DELAY_SHOW', {
              text: err.statusMessage
            })
          })
      }
    },
    computed:{
      $$data:function () {
        return this.$data
      },
      $$records: function () {
        return this.$data.records
      },
      $$url: function () {
        return this.$data.url
      }
    },
    filters: {
      changeStatus: function (val) {
        if(val == "1"){
          return '待提现'
        }else if(val == "2"){
          return '准备打款'
        }else if(val == "3"){
          return '请求成功'
        }else if(val == "4"){
          return '打款成功'
        }else if(val == "5"){
          return '打款失败'
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
      changeType: function (val) {
        if(val==0){
          return '商户'
        }else if(val==1){
          return '代理商'
        }
      },
      changeHide: function (val) {
        if(val!=""&&val!=null){
          val = val.replace(val.substring(3,val.length-6),"…");
        }
        return val
      },
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
  img{
    width: 8%;
    margin: 0 auto;
    display: inherit;
  }
</style>
