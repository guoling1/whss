<template>
  <div id="storeList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">订单查询</h3>
        </div>
        <div class="box-body">
          <el-tabs class="tab" v-model="activeName" type="card">
            <el-tab-pane label="好收收" name="first">
              <!--筛选-->
              <ul class="search">
                <li class="same">
                  <label>订单号:</label>
                  <el-input style="width: 188px" v-model="queryHss.orderNo" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>订单创建时间:</label>
                  <el-date-picker :clearable="false" style="width: 188px" v-model="dateHss" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small" @change="datetimeSelect">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>交易时间:</label>
                  <el-date-picker :clearable="false" style="width: 188px" v-model="dateHss1" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>交易单号:</label>
                  <el-input style="width: 188px" v-model="queryHss.tradeOrderNo" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>商户名称:</label>
                  <el-input style="width: 188px" v-model="queryHss.merchantName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>一级代理:</label>
                  <el-input style="width: 188px" v-model="queryHss.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>二级代理:</label>
                  <el-input style="width: 188px" v-model="queryHss.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>支付方式：</label>
                  <el-select style="width: 188px" v-model="queryHss.paymentMethod" size="small">
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="微信支付" value="1"></el-option>
                    <el-option label="支付宝支付" value="2"></el-option>
                    <el-option label="快捷支付" value="3"></el-option>
                    <el-option label="QQ钱包" value="4"></el-option>
                  </el-select>
                </li>
                <li class="same">
                  <label>订单状态：</label>
                  <el-select style="width: 188px" clearable v-model="queryHss.status" size="small">
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="待支付" value="1"></el-option>
                    <el-option label="支付成功" value="3"></el-option>
                    <el-option label="支付失败" value="2"></el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search('hss')">筛选</div>
                  <div class="btn btn-primary" @click="reset('hss')">重置</div>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom:15px" :data="recordsHss" border>
                <el-table-column type="index" width="62" label="序号"></el-table-column>
                <el-table-column prop="orderNo" label="订单号">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.orderNo" style="cursor: pointer" title="点击复制">{{scope.row.orderNo|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="createTimes" label="订单创建时间"></el-table-column>
                <el-table-column prop="merchantName" label="收款商户"></el-table-column>
                <!--<el-table-column prop="proxyName1" label="分公司"></el-table-column>-->
                <el-table-column prop="proxyName" label="所属一级"></el-table-column>
                <el-table-column prop="proxyName1" label="所属二级"></el-table-column>
                <el-table-column prop="tradeOrderNo" label="交易订单号">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.tradeOrderNo" style="cursor: pointer" title="点击复制">{{scope.row.tradeOrderNo|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="sn" label="支付流水号">
                  <template scope="scope">
                    <span class="td" :data-clipboard-text="scope.row.sn" style="cursor: pointer" title="点击复制">{{scope.row.sn|changeHide}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="tradeAmount" label="订单金额" align="right"></el-table-column>
                <el-table-column prop="payRate" label="费率" align="right"></el-table-column>
                <el-table-column prop="poundage" label="手续费" align="right"></el-table-column>
                <el-table-column prop="statusValue" label="订单状态"></el-table-column>
                <el-table-column prop="paymentMethod" label="支付方式"></el-table-column>
                <el-table-column prop="paySuccessTimes" label="交易时间"></el-table-column>
              </el-table>
              <ul style="float: left;margin-top: 5px">
                <li>
                  <label style="margin-right: 10px;">支付金额</label>
                  <span>当页总额：{{pageTotalHss}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
                  <span>统计总额：{{addTotalHss}}&nbsp;元</span>
                </li>
                <li>
                  <label style="margin-right: 10px;">手续费</label>
                  <span>当页总额：{{pageTotalHss1}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
                  <span>统计总额：{{addTotalHss1}}&nbsp;元</span>
                </li>
              </ul>
              <div class="block" style="text-align: right">
                <el-pagination @size-change="handleSizeChangeHss"
                               @current-change="handleCurrentChangeHss"
                               :current-page="queryHss.page"
                               :page-sizes="[10, 20, 50]"
                               :page-size="queryHss.size"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="countHss">
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="好收银" name="second">
              <!--<ul class="search">-->
                <!--<li class="same">-->
                  <!--<label>商户编号:</label>-->
                  <!--<el-input style="width: 188px" v-model="queryHsy.globalID" placeholder="请输入内容" size="small"></el-input>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<label>商户名称:</label>-->
                  <!--<el-input style="width: 188px" v-model="queryHsy.shortName" placeholder="请输入内容" size="small"></el-input>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<label>注册手机号:</label>-->
                  <!--<el-input style="width: 188px" v-model="queryHsy.cellphone" placeholder="请输入内容" size="small"></el-input>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<label>注册时间:</label>-->
                  <!--<el-date-picker style="width: 188px" v-model="dateHsy" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">-->
                  <!--</el-date-picker>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<label>审核时间:</label>-->
                  <!--<el-date-picker style="width: 188px" v-model="dateHsy1" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">-->
                  <!--</el-date-picker>-->
                <!--</li>-->
                <!--&lt;!&ndash;<li class="same">-->
                  <!--<label>所属分公司:</label>-->
                  <!--<el-input style="width: 188px" v-model="queryHsy.proxyName1" placeholder="请输入内容" size="small"></el-input>-->
                <!--</li>&ndash;&gt;-->
                <!--<li class="same">-->
                  <!--<label>所属一级代理:</label>-->
                  <!--<el-input style="width: 188px" v-model="queryHsy.proxyName" placeholder="请输入内容" size="small"></el-input>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<label>所属二级代理:</label>-->
                  <!--<el-input style="width: 188px" v-model="queryHsy.proxyName1" placeholder="请输入内容" size="small"></el-input>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<label>报单员:</label>-->
                  <!--<el-input style="width: 188px" v-model="queryHsy.username" placeholder="请输入内容" size="small"></el-input>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<label>姓名:</label>-->
                  <!--<el-input style="width: 188px" v-model="queryHsy.realname" placeholder="请输入内容" size="small"></el-input>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<label>审核状态:</label>-->
                  <!--<el-select style="width: 188px" v-model="queryHsy.status" clearable placeholder="请选择" size="small">-->
                    <!--<el-option label="全部" value="">全部</el-option>-->
                    <!--<el-option label="已注册" value="0">已注册</el-option>-->
                    <!--<el-option label="已提交基本资料" value="1">已提交基本资料</el-option>-->
                    <!--<el-option label="待审核" value="2">待审核</el-option>-->
                    <!--<el-option label="审核通过" value="3">审核通过</el-option>-->
                    <!--<el-option label="审核未通过" value="4">审核未通过</el-option>-->
                  <!--</el-select>-->
                <!--</li>-->
                <!--<li class="same">-->
                  <!--<div class="btn btn-primary" @click="search('hsy')">筛选</div>-->
                  <!--<div class="btn btn-primary" @click="reset('hsy')">重置</div>-->
                <!--</li>-->
                <!--<li class="same" style="float: right">-->
                  <!--<span @click="_$power(onload,'boss_merchant_export')" download="商户列表" class="btn btn-primary">导出</span>-->
                <!--</li>-->
              <!--</ul>-->
              <!--<el-table v-loading.body="loading" style="font-size: 12px;margin-bottom:15px" :data="recordsHsy" border>-->
                <!--<el-table-column type="index" width="62" label="序号"></el-table-column>-->
                <!--<el-table-column prop="globalID" label="商户编号"></el-table-column>-->
                <!--<el-table-column prop="shortName" label="商户名称"></el-table-column>-->
                <!--&lt;!&ndash;<el-table-column prop="proxyNames" label="所属分公司"></el-table-column>&ndash;&gt;-->
                <!--<el-table-column prop="proxyName" label="所属一级代理"></el-table-column>-->
                <!--<el-table-column prop="proxyName1" label="所属二级代理"></el-table-column>-->
                <!--<el-table-column prop="username" label="报单员"></el-table-column>-->
                <!--<el-table-column prop="realname" label="姓名"></el-table-column>-->
                <!--&lt;!&ndash;<el-table-column prop="proxyNames" label="所属代理商"></el-table-column>&ndash;&gt;-->
                <!--<el-table-column label="注册时间">-->
                  <!--<template scope="scope">-->
                    <!--<span>{{scope.row.createTime|changeTime}}</span>-->
                  <!--</template>-->
                <!--</el-table-column>-->
                <!--<el-table-column label="提交时间">-->
                  <!--<template scope="scope">-->
                    <!--<span>{{scope.row.commitTime|changeTime}}</span>-->
                  <!--</template>-->
                <!--</el-table-column>-->
                <!--<el-table-column label="审核时间">-->
                  <!--<template scope="scope">-->
                    <!--<span>{{scope.row.auditTime|changeTime}}</span>-->
                  <!--</template>-->
                <!--</el-table-column>-->
                <!--<el-table-column prop="cellphone" label="注册手机号"></el-table-column>-->
                <!--<el-table-column prop="districtCode" label="省市"></el-table-column>-->
                <!--<el-table-column prop="industryCode" label="行业"></el-table-column>-->
                <!--<el-table-column prop="stat" label="状态"></el-table-column>-->
                <!--<el-table-column label="操作" width="100">-->
                  <!--<template scope="scope">-->
                    <!--<el-button @click="_$power(scope.row.id,scope.row.status,auditHsy,'boss_merchant_check')" v-if="recordsHsy[scope.$index].stat=='待审核'" type="text" size="small">审核</el-button>-->
                    <!--<el-button @click="_$power(scope.row.id,scope.row.status,auditHsy,'boss_merchant_detail')" v-if="recordsHsy[scope.$index].stat!='待审核'" type="text" size="small">查看详情</el-button>-->
                  <!--</template>-->
                <!--</el-table-column>-->
              <!--</el-table>-->
              <!--<div class="block" style="text-align: right">-->
                <!--<el-pagination @size-change="handleSizeChangeHsy"-->
                               <!--@current-change="handleCurrentChangeHsy"-->
                               <!--:current-page="queryHsy.pageNo"-->
                               <!--:page-sizes="[10, 20, 50]"-->
                               <!--:page-size="queryHsy.pageSize"-->
                               <!--layout="total, sizes, prev, pager, next, jumper"-->
                               <!--:total="countHsy">-->
                <!--</el-pagination>-->
              <!--</div>-->
            </el-tab-pane>
          </el-tabs>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
  </div>
</template>

<script lang="babel">
  import Clipboard from "clipboard"
  export default{
    name: 'storeList',
    data(){
      return {
        isMask: false,
        activeName: 'first', //选项卡选中第一个
        url:'/admin/queryOrder/queryOrderList',
        fromName:'',
        queryHss:{
          page:1,
          size:10,
          orderNo:'',
          tradeOrderNo:"",
          merchantName:'',
          proxyName:'',
          proxyName1:'',
          paymentMethod:'',
          status:'',
          startTime:'',
          endTime:'',
          paySuccessTime:'',
          paySuccessTime1:''
        },
        pageTotalHss:0,
        pageTotalHss1:0,
        addTotalHss:0,
        addTotalHss1:0,
        dateHss:'',
        dateHss1:'',
        queryHsy:{
          pageNo:1,
          pageSize:10,
        },
        recordsHss: [],
        recordsHsy: [],
        countHss: 0,
        countHsy: 0,
        currentPage: 1,
        loading: false,
      }
    },
//    beforeRouteEnter (to, from, next) {
//      next(vm=>{
//        vm.$data.fromName = from.name;
//        if(vm.$data.fromName=='StoreAuditHSY'){
//          vm.$data.activeName='second';
//          vm.$data.url='/admin/hsyMerchantList/getMerchantList'
//        }else {
//          vm.$data.activeName='first';
//          vm.$data.url='/admin/query/getAll'
//        }
//        vm.$data.records = '';
//        vm.$data.total = 0;
//        vm.$data.count = 0;
//      })
//    },
    created: function () {
      var clipboard = new Clipboard('.td');
      // 复制成功执行的回调，可选
      clipboard.on('success', (e) => {
        this.$message({
          showClose: true,
          message: "复制成功  内容为：" + e.text,
          type: 'success'
        });
      });
      let time = new Date();
      this.dateHss = [time,time];
      for (var j = 0; j < this.dateHss.length; j++) {
        var str = this.dateHss[j];
        var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
        for (var i = 0, len = ary.length; i < len; i++) {
          if (ary[i] < 10) {
            ary[i] = '0' + ary[i];
          }
        }
        str = ary[0] + '-' + ary[1] + '-' + ary[2];
        if (j == 0) {
          this.queryHss.startTime = str;
        } else {
          this.queryHss.endTime = str;
        }
      }
      this.getDataHss();
//      this.getDataHsy()
      this.getAddTotalHss();
    },
    methods: {
      datetimeSelect: function (val) {
        let format = val.split(' - ');
        this.queryHss.startTime = format[0];
        this.queryHss.endTime = format[1];
      },
      reset: function (val) {
        if(val == 'hss'){
          this.queryHss = {
            page:1,
            size:10,
            orderNo:'',
            tradeOrderNo:"",
            merchantName:'',
            proxyName:'',
            proxyName1:'',
            paymentMethod:'',
            status:''
          }
        }else if(val == 'hsy'){
          this.dateHsy='';
          this.dateHsy1='';
          this.queryHsy = {
            page:1,
            size:10,
            shortName:'',
            globalID:'',
            proxyName:'',
            proxyName1:'',
            cellphone:'',
            username:'',
            status:'',
            startTime:'',
            endTime:'',
            auditTime:'',
            auditTime1:'',
            realname:''
          }
        }
      },
      auditHsy: function (id,status) {
//        this.$router.push({path:'/admin/record/StoreAuditHSY',query:{id:id,status:status}})
        window.open('http://admin.qianbaojiajia.com/admin/details/StoreAuditHSY?id='+id+'&status='+status);
      },
      audit: function (id,status) {
        window.open('http://admin.qianbaojiajia.com/admin/details/StoreAudit?id='+id+'&status='+status);
//        this.$router.push({path:'/admin/record/StoreAudit',query:{id:id,status:status}})
      },
      getDataHss: function () {
        this.loading = true;
        this.$http.post('/admin/queryOrder/queryOrderList',this.queryHss)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.recordsHss = res.data.records;
              var toFix = function (val) {
                return parseFloat(val).toFixed(2)
              };
              var total=0,total1=0;
              for (let i = 0; i < res.data.records.length; i++) {
                if(res.data.records[i].payRate!=null){
                  res.data.records[i].payRate = toFix(res.data.records[i].payRate*100)+"%";
                }
                if(res.data.records[i].tradeAmount!=null){
                  total = toFix(parseFloat(total)+parseFloat(res.data.records[i].tradeAmount));
                }
                if(res.data.records[i].poundage!=null){
                  total1 = toFix(parseFloat(total1)+parseFloat(res.data.records[i].poundage))

                }
              }
              this.pageTotalHss = total;
              this.pageTotalHss1 = total1;
            },1000)
            this.countHss = res.data.count;
          }, function (err) {
            setTimeout(()=>{
              this.loading = false;
            },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      getAddTotalHss(){
        this.$http.post('/admin/queryOrder/getOrderCount',this.queryHss)
          .then(res=>{
            this.addTotalHss = res.data.totalPayment;
            this.addTotalHss1 = res.data.totalPoundage;
          })
          .catch(err=>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      getDataHsy: function () {
//        this.loading = true;
//        this.$http.post('/admin/hsyMerchantList/getMerchantList',this.queryHsy)
//          .then(function (res) {
//            setTimeout(()=>{
//              this.loading = false;
//              this.$data.recordsHsy = res.data.records;
//            },1000)
//            this.$data.countHsy = res.data.count;
//          }, function (err) {
//            setTimeout(()=>{
//              this.loading = false;
//            },1000)
//            this.$message({
//              showClose: true,
//              message: err.statusMessage,
//              type: 'error'
//            })
//          })
      },
      onload: function () {
        if(this.activeName == 'first'){
          this.$data.loadUrl = this.loadUrlHss;
        }else if(this.activeName == 'second'){
          this.$data.loadUrl = this.loadUrlHsy;
        }
        this.$data.isMask = true;
      },
      search(val){
        if(val == 'hss'){
          this.queryHss.page = 1;
          this.getDataHss();
          this.getAddTotalHss();
        }else if(val == 'hsy'){
          this.queryHsy.page = 1;
          this.getDataHsy();
        }
      },
      //当前页改变时
      handleCurrentChangeHss(val) {
        this.queryHss.page = val;
        this.getDataHss()
      },
      handleCurrentChangeHsy(val) {
        this.queryHsy.page = val;
        this.getDataHsy()
      },
      handleSizeChangeHss: function (val) {
        this.queryHss.page = 1;
        this.queryHss.size = val;
        this.getDataHss()
      },
      handleSizeChangeHsy: function (val) {
        this.queryHsy.page = 1;
        this.queryHsy.size = val;
        this.getDataHsy()
      }
    },
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
  ul {
    padding: 0;
    margin:0;
  }
  .search{
    margin-bottom:0;
    label{
      display: block;
      margin-bottom: 0;
    }
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }
</style>
