<template lang="html">
  <div id="withDrawal">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">打款查询
      <div class="btn btn-primary pull-right" @click="refresh()">刷新</div>
    </div>
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
          <div class="row">
            <div class="col-md-3">
              <div class="form-group">
                <label for="date">订单号：</label>
                <input type="text" class="form-control" name="date" value="" v-model="$$data.query.orderId">
              </div>
            </div>
            <div class="col-md-3">
              <div class="form-group">
                <label for="codeNumber">用户名称：</label>
                <input type="text" name="codeNumber" class="form-control" v-model="$$data.query.name">
              </div>
            </div>
            <div class="col-md-2">
              <div class="form-group">
                <label for="number">收款银行卡后四位：</label>
                <input type="text" class="form-control" name="number" v-model="$$data.query.bankNoShort">
              </div>
            </div>
            <div class="col-md-3">
              <div class="form-group">
                <label>状态：</label>
                <select class="form-control select2 select2-hidden-accessible" style="width: 100%;" tabindex="-1" aria-hidden="true" v-model="$$data.query.payResult">
                  <option value="">全部</option>
                  <option value="N">申请</option>
                  <option value="H">提现中</option>
                  <!--<option value="S">驳回</option>-->
                  <option value="S">提现成功</option>
                  <option value="F">提现失败</option>
                </select>
              </div>
            </div>
            <div class="col-md-1">
              <div class="form-group">
                <div class="btn btn-primary" @click="lookup">筛选</div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
    <div style="margin: 0 15px">
      <div class="box" style="overflow-x: hidden;">
        <div class="box-header">
          <h3 class="box-title">提现记录</h3>
        </div>
        <!-- /.box-header -->
        <div class="box-body">
          <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                  <thead>
                  <tr role="row">
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">订单号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">打款时间</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">用户名称</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">收款银行账号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付金额</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">实际所得</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">服务费</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">支付状态</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">错误信息</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr role="row" v-for="(record,index) in $$records">
                    <td><router-link :to="{ path: '/admin/record/withdrawalDet', query: {id: record.id}}">{{record.orderId|changeHide}}</router-link></td>
                    <td>{{record.payTime|changeTime}}</td>
                    <td>{{record.name}}</td>
                    <td>{{record.bankNo}}</td>
                    <td>{{record.totalFee}}</td>
                    <td>{{record.realFee}}</td>
                    <td>{{record.serviceFee}}</td>
                    <td>{{record.payResult|changeStatus}}</td>
                    <td>{{record.errorMessage}}</td>

                    <!--<td v-if="record.payResult=='O'">{{record.errorMessage||changeMes}}</td>
                    <td v-if="record.payResult!='O'"></td>
                    <td>
                      <p class="btn btn-success" v-if="record.payResult=='N'" @click="audit(index)">审核</p>
                      <p class="btn btn-success" v-if="record.payResult=='F'" @click="audit(index)">结果审核</p>
                    </td>-->
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-if="records.length==0" class="row" style="text-align: center;color: red;font-size: 16px;">
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
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.box-body -->
      </div>
    </div>
    <!--审核页-->
    <!--<div id="cashAudit" v-if="isShow">
      <div class="box box-solid content col-sm-6"  >
        <div class="box-header with-border">
          <h3 class="box-title">申请审核</h3>
          <i style="float: right" @click="(function(){return isShow=!isShow})()">X</i>
        </div>
        &lt;!&ndash; /.box-header &ndash;&gt;
        <div class="box-body">
          <dl class="dl-horizontal">
            <dt>商户名称：</dt>
            <dd>{{$$records[this.$$data.index].merchantName}}</dd>
            <dt>打款流水号：</dt>
            <dd>{{$$records[this.$$data.index].orderId}}</dd>
            <dt>提现申请金额：</dt>
            <dd>{{$$records[this.$$data.index].totalFee}}</dd>
            <dt>实际申请金额：</dt>
            <dd>{{$$records[this.$$data.index].realFee}}</dd>
            <dt>打款通道：</dt>
            <dd>{{$$records[this.$$data.index].payChannel|changeChannel}}</dd>
            <dt v-if="$$records[this.$$data.index].payResult=='N'">审核备注：</dt>
            <dd v-if="$$records[this.$$data.index].payResult=='N'"><input type="text" placeholder="必填" v-model="remark"></dd>
            <dt v-if="$$records[this.$$data.index].payResult=='F'">审核备注：</dt>
            <dd v-if="$$records[this.$$data.index].payResult=='F'">{{$$records[this.$$data.index].errorMessage}}</dd>
          </dl>
          <dt class="btn btn-success dl-horizontal" @click="pass()" v-if="$$records[this.$$data.index].payResult=='N'">通过</dt>
          <dd class="btn btn-danger dl-horizontal" @click="unPass()" v-if="$$records[this.$$data.index].payResult=='N'">不通过</dd>
          <dt class="btn btn-success dl-horizontal" @click="freeze()" v-if="$$records[this.$$data.index].payResult=='F'">确认</dt>
          <dd class="btn btn-danger dl-horizontal" @click="unfreeze()" v-if="$$records[this.$$data.index].payResult=='F'">取消</dd>
        </div>
        &lt;!&ndash; /.box-body &ndash;&gt;
      </div>
    </div>-->
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
          page:1,
          size:10,
          orderId:'',
          name:'',
          bankNoShort:'',
          payResult:''
        },
        total:'',
        records:[],
        isShow:false,
        index:0,
        remark:''
      }
    },
    created:function () {
      this.$http.post('/admin/withdraw/withdrawListByContions',this.$data.query)
        .then(function (res) {
          this.$data.records = res.data.records;
          this.$data.total = res.data.totalPage;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
          for (var i=1; i<=this.$data.total;i++){
            if(i==this.$data.query.page){
              str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
              continue;
            }
            str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
          }
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
          var aLi = page.getElementsByTagName('li');
          for(var i=0;i<aLi.length;i++){
            if(this.$data.query.page<6&&i>10){
              aLi[i].style.display='none'
            }else if(this.$data.query.page>(this.$data.total-6)&&i<(this.$data.total-11)){
              aLi[i].style.display='none'
            }else if((i!=0&&i<this.$data.query.page-5)||(i!=this.$data.total+1&&i>this.$data.query.page+4)){
              aLi[i].style.display='none'
            }
          }
        },function (err) {
          console.log(err)
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
        this.$http.post('/admin/withdraw/withdrawListByContions',this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.total=res.data.totalPage;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==this.$data.query.page){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
            page.innerHTML=str;
            var aLi = page.getElementsByTagName('li');
            for(var i=0;i<aLi.length;i++){
              if(this.$data.query.page<6&&i>10){
                aLi[i].style.display='none'
              }else if(this.$data.query.page>(this.$data.total-6)&&i<(this.$data.total-11)){
                aLi[i].style.display='none'
              }else if((i!=0&&i<this.$data.query.page-5)||(i!=this.$data.total+1&&i>this.$data.query.page+4)){
                aLi[i].style.display='none'
              }
            }
          },function (err) {
            console.log(err)
          })
      },
      //筛选
      lookup: function () {
        this.$data.query.page = 1;
        this.$http.post('/admin/withdraw/withdrawListByContions',this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.total=res.data.totalPage;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==this.$data.query.page){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
            page.innerHTML=str;
            var aLi = page.getElementsByTagName('li');
            for(var i=0;i<aLi.length;i++){
              if(this.$data.query.page<6&&i>10){
                aLi[i].style.display='none'
              }else if(this.$data.query.page>(this.$data.total-6)&&i<(this.$data.total-11)){
                aLi[i].style.display='none'
              }else if((i!=0&&i<this.$data.query.page-5)||(i!=this.$data.total+1&&i>this.$data.query.page+4)){
                aLi[i].style.display='none'
              }
            }
          },function (err) {
            console.log(err)
          })
      },
      /*audit: function (index) {
        this.$data.isShow=!this.$data.isShow
        this.$data.index = index;
      },*/
      /*pass: function () {
        this.$http.post('/admin/withdraw/checkWithdraw',{
          id:this.$data.records[this.$data.index].id
        }).then(function (res) {
          this.$data.isShow=!this.$data.isShow;
          location.reload()
        },function (err) {
          console.log(err)
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
          this.$data.isShow=!this.$data.isShow;
        })
      },*/
      /*unPass: function () {
        this.$http.post('/admin/withdraw/unPass',{
          id:this.$data.records[this.$data.index].id,
          message:this.$data.remark
        }).then(function (res) {
          this.$data.isShow=!this.$data.isShow;
          location.reload()
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
          this.$data.isShow=!this.$data.isShow;
        })
      },*/
      /*freeze: function () {
        this.$http.post('/admin/withdraw/unfreeze',{
          id:this.$data.records[this.$data.index].id,
        }).then(function (res) {
          this.$data.isShow=!this.$data.isShow;
          location.reload()
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.msg
          })
          this.$data.isShow=!this.$data.isShow;
          location.reload()
        })
      },
      unfreeze: function () {
        this.$data.isShow=!this.$data.isShow
      }*/
    },
    computed:{
      $$data:function () {
        return this.$data
      },
      $$records: function () {
        return this.$data.records
      }
    },
    filters: {
      changeStatus: function (val) {
        if(val == "N"){
          return '待审核'
        }else if(val == "H"){
          return '提现中'
        }else if(val == "S"){
          return '提现成功'
        }else if(val == "F"){
          return '提现失败'
        }else if(val == "O"){
          return '审核未通过'
        }else if(val == "D"){
          return '交易关闭'
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
          return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
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
        val = val.replace(val.substring(3,19),"…");
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

  #cashAudit{
    width: 100%;
    height:100%;
    background: #cccccc;
    position: fixed;
    top:0;
    z-index: 100;
  .content{
    margin:0 auto;
    position: absolute;
    top:20%;
    left:10%;
    width: 50%;
  }
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
</style>
