<template>
  <div id="profitCount">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">分润统计</h3>
        </div>
        <div class="box-body">
          <el-tabs class="tab" v-model="activeName" type="card" @tab-click="handleClick">
            <el-tab-pane label="公司分润" name="first">
              <!--筛选-->
              <ul class="search">
                <li class="same">
                  <label>交易日期:</label>
                  <el-date-picker style="width: 190px" v-model="date" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>收益类型:</label>
                  <el-select style="width: 190px" clearable v-model="query.businessType" size="small" >
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="好收收-收款" value="hssPay">好收收-收款</el-option>
                    <el-option label="好收收-提现" value="hssWithdraw">好收收-提现</el-option>
                    <el-option label="好收收-升级费" value="hssUpgrade">好收收-升级费</el-option>
                    <el-option label="好收银-收款" value="hsyPay">好收银-收款</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search">筛选</div>
                </li>
                <li class="same" style="color: #fff;float: right">
                  <span @click="_$power(onload,'boss_company_share_export')" download="公司分润" class="btn btn-primary">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px;" :data="records" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    <div v-if="records[scope.$index].businessType!='当页总额'&&records[scope.$index].businessType!='筛选条件统计'">{{scope.$index+1}}</div>
                  </template>
                </el-table-column>
                <el-table-column label="收益日期">
                  <template scope="scope">
                    <span>{{scope.row.splitDate|changeDate}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="businessType" label="收益类型"></el-table-column>
                <el-table-column prop="splitAmount" label="收益金额" align="right" header-align="left"></el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <router-link :to="{path:'/admin/record/profitComDet',query:{type:records[scope.$index].businessType,time:records[scope.$index].splitDate}}" v-if="records[scope.$index].splitAmount!=0&&records[scope.$index].businessType!='当页总额'&&records[scope.$index].businessType!='筛选条件统计'" type="text" size="small">明细
                    </router-link>
                    <a v-if="records[scope.$index].businessType=='筛选条件统计'" @click="add">点击统计</a>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="一级代理商分润" name="second">
              <!--筛选-->
              <ul class="search">
                <li class="same">
                  <label>交易日期:</label>
                  <el-date-picker style="width: 190px" v-model="date" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>代理商名称:</label>
                  <el-input style="width: 190px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>代理商编号:</label>
                  <el-input style="width: 190px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>收益类型:</label>
                  <el-select style="width: 190px" clearable v-model="query.businessType" size="small" >
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="好收收-收款" value="hssPay">好收收-收款</el-option>
                    <el-option label="好收收-提现" value="hssWithdraw">好收收-提现</el-option>
                    <el-option label="好收收-升级费" value="hssUpgrade">好收收-升级费</el-option>
                    <el-option label="好收银-收款" value="hsyPay">好收银-收款</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search">筛选</div>
                </li>
                <li class="same" style="color: #fff;float: right">
                  <span @click="_$power(onload,'boss_first_share_export')" download="公司分润" class="btn btn-primary" style="color: #fff;float: right">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    <div v-if="records[scope.$index].businessType!='当页总额'&&records[scope.$index].businessType!='筛选条件统计'">{{scope.$index+1}}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="proxyName" label="代理商名称"></el-table-column>
                <el-table-column prop="markCode" label="代理商编号"></el-table-column>
                <el-table-column label="收益日期">
                  <template scope="scope">
                    <span>{{scope.row.splitDate|changeDate}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="businessType" label="收益类型"></el-table-column>
                <el-table-column prop="splitAmount" label="收益金额" align="right" header-align="left"></el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <router-link :to="{path:'/admin/record/profitFirDet',query:{type:records[scope.$index].businessType,id:records[scope.$index].receiptMoneyAccountId,time:records[scope.$index].splitDate}}" v-if="records[scope.$index].splitAmount!=0&&records[scope.$index].businessType!='当页总额'&&records[scope.$index].businessType!='筛选条件统计'" type="text" size="small">明细
                    </router-link>
                    <a v-if="records[scope.$index].businessType=='筛选条件统计'" @click="add">点击统计</a>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="二级代理商分润" name="third">
              <ul class="search">
                <li class="same">
                  <label>交易日期:</label>
                  <el-date-picker style="width: 190px" v-model="date" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>上级代理名称:</label>
                  <el-input style="width: 190px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>代理商名称:</label>
                  <el-input style="width: 190px" v-model="query.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>代理商编号:</label>
                  <el-input style="width: 190px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>收益类型:</label>
                  <el-select style="width: 190px" clearable v-model="query.businessType" size="small" >
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="好收收-收款" value="hssPay">好收收-收款</el-option>
                    <el-option label="好收收-提现" value="hssWithdraw">好收收-提现</el-option>
                    <el-option label="好收收-升级费" value="hssUpgrade">好收收-升级费</el-option>
                    <el-option label="好收银-收款" value="hsyPay">好收银-收款</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search">筛选</div>
                </li>
                <li class="same" style="color: #fff;float: right">
                  <span @click="_$power(onload,'boss_seconde_share_export')" download="公司分润" class="btn btn-primary">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    <div v-if="records[scope.$index].businessType!='当页总额'&&records[scope.$index].businessType!='筛选条件统计'">{{scope.$index+1}}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="proxyName" label="上级代理商名称"></el-table-column>
                <el-table-column prop="proxyName1" label="二级代理商名称"></el-table-column>
                <el-table-column prop="markCode" label="代理编号"></el-table-column>
                <el-table-column label="收益日期">
                  <template scope="scope">
                    <span>{{scope.row.splitDate|changeDate}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="businessType" label="收益类型"></el-table-column>
                <el-table-column prop="splitAmount" label="收益金额" align="right" header-align="left"></el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <router-link
                      :to="{path:'/admin/record/profitSecDet',query:{type:records[scope.$index].businessType,id:records[scope.$index].receiptMoneyAccountId,time:records[scope.$index].splitDate}}"
                      v-if="records[scope.$index].splitAmount!=0&&records[scope.$index].businessType!='当页总额'&&records[scope.$index].businessType!='筛选条件统计'" type="text" size="small">明细
                    </router-link>
                    <a v-if="records[scope.$index].businessType=='筛选条件统计'" @click="add">点击统计</a>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
          <ul style="float: left;margin-top: 5px">
            <li>
              <label style="margin-right: 10px;">收益金额</label>
              <span>当页总额：{{pageTotal}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span>筛选条件统计：{{addTotal}}&nbsp;元</span>
            </li>
          </ul>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.pageNo"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
          <div class="box box-info mask el-message-box" v-if="isMask">
            <div class="maskCon">
              <div class="head">
                <div class="title">消息</div>
                <i class="el-icon-close" @click="isMask=false"></i>
              </div>
              <div class="body">
                <div>确定导出列表吗？</div>
              </div>
              <div class="foot">
                <a href="javascript:void(0)" @click="isMask=false" class="el-button el-button--default">取消</a>
                <a :href="'http://'+loadUrl" @click="isMask=false" class="el-button el-button-default el-button--primary ">下载</a>
              </div>
            </div>
          </div>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name: 'profitCount',
    data(){
      return {
        isMask: false,
        activeName: 'first', //选项卡选中第一个
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        date:'',
        date1:'',
        date2:'',
        url:'',
        loadUrl:'',
        loadUrl1:'',
        totalUrl:"",
        fromName:'',
        query:{
          pageNo:1,
          pageSize:10,
          startTime:'',
          endTime:'',
          businessType:'',
          proxyName:'',
          markCode:'',
          proxyName1:'',
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    beforeRouteEnter (to, from, next) {
      next(vm=>{
        vm.$data.fromName = from.name;
        console.log(from)
        if(vm.$data.fromName=='ProfitFirDet'){
          vm.$data.activeName='second';
          vm.$data.url='/admin/allProfit/firstProfit';
          vm.$data.totalUrl='/admin/allProfit/firstAmount'
        }else if(vm.$data.fromName=='ProfitSecDet'){
          vm.$data.activeName='third';
          vm.$data.url='/admin/allProfit/secondProfit';
          vm.$data.totalUrl='/admin/allProfit/secondAmount'
        }else {
          vm.$data.activeName='first';
          vm.$data.url='/admin/allProfit/companyProfit';
          vm.$data.totalUrl='/admin/allProfit/companyAmount'
        }
        vm.$data.records = [];
        vm.$data.total = 0;
        vm.$data.count = 0;
        vm.$http.post(vm.$data.url,vm.query)
          .then(function (res) {
            vm.$data.loading = false;
            vm.$data.records = res.data.records;
            vm.$data.count = res.data.count;
            vm.$data.loadUrl1 = res.data.ext;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            var total=0;
            for (let i = 0; i < vm.$data.records.length; i++) {
              vm.$data.records[i].splitAmount = toFix(vm.$data.records[i].splitAmount);
              total = toFix(parseFloat(total)+parseFloat(vm.$data.records[i].splitAmount))
            }
            vm.pageTotal = total;
          }, function (err) {
            vm.$data.loading = false;
            vm.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
        vm.$http.post(vm.totalUrl,vm.query)
          .then(res=>{
            vm.addTotal = res.data;
          })
          .catch(err=>{
            vm.$data.loading = false;
            vm.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      })
    },
    created: function () {
      let time = new Date();
      this.date = [time,time];
      for (var j = 0; j < this.date.length; j++) {
        var str = this.date[j];
        var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
        for (var i = 0, len = ary.length; i < len; i++) {
          if (ary[i] < 10) {
            ary[i] = '0' + ary[i];
          }
        }
        str = ary[0] + '-' + ary[1] + '-' + ary[2];
        if (j == 0) {
          this.$data.query.startTime = str;
        } else {
          this.$data.query.endTime = str;
        }
      }
    },
    methods: {
      onload: function () {
        this.$data.loadUrl = this.loadUrl1;
        this.$data.isMask = true;
      },
      getData: function () {
        this.loading = true;
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.loadUrl1 = res.data.ext;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            var total=0;
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].splitAmount = toFix(this.$data.records[i].splitAmount);
              total = toFix(parseFloat(total)+parseFloat(this.$data.records[i].splitAmount))
            }
            this.pageTotal = total;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      //选项卡改变时
      handleClick: function (tab,event) {
        this.$data.query={
          pageNo:1,
          pageSize:10,
          startTime:'',
          endTime:'',
          businessType:'',
          proxyName:'',
          markCode:'',
          proxyName1:'',
        };
        let time = new Date();
        this.date = [time,time];
        for (var j = 0; j < this.date.length; j++) {
          var str = this.date[j];
          var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
          for (var i = 0, len = ary.length; i < len; i++) {
            if (ary[i] < 10) {
              ary[i] = '0' + ary[i];
            }
          }
          str = ary[0] + '-' + ary[1] + '-' + ary[2];
          if (j == 0) {
            this.$data.query.startTime = str;
          } else {
            this.$data.query.endTime = str;
          }
        }
        this.records=[];
        if(event.target.innerHTML=="公司分润"){
          this.$data.url='/admin/allProfit/companyProfit';
          this.$data.totalUrl='/admin/allProfit/companyAmount'
        }else if(event.target.innerHTML=="一级代理商分润"){
          this.$data.url='/admin/allProfit/firstProfit';
          this.$data.totalUrl='/admin/allProfit/firstAmount'
        }else if(event.target.innerHTML=="二级代理商分润"){
          this.$data.url='/admin/allProfit/secondProfit';
          this.$data.totalUrl='/admin/allProfit/secondAmount'
        }
        this.getData();
        this.getAddTotal();
      },
      search(){
        this.$data.query.pageNo = 1;
        this.getData();
        this.getAddTotal();
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.getData()
      },
      handleSizeChange: function (val) {
        this.query.pageNo = 1;
        this.query.pageSize = val;
        this.loading = true;
        this.getData()
        this.getAddTotal();
      },
      onload: function () {
        this.$data.loadUrl = this.loadUrl1;
        this.$data.isMask = true;
      },
      getAddTotal(){
        this.$http.post(this.totalUrl,this.query)
          .then(res=>{
            this.addTotal = res.data;
          })
          .catch(err=>{
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
    },
    watch:{
      date:function (val,oldVal) {
        if(val!=undefined&&val[0]!=null){
          for(var j=0;j<val.length;j++){
            var str = val[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for(var i = 0, len = ary.length; i < len; i ++) {
              if(ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if(j==0){
              this.$data.query.startTime = str;
            }else {
              this.$data.query.endTime = str;
            }
          }
        }else {
          this.$data.query.startTime = '';
          this.$data.query.endTime = '';
        }
      },
      date1:function (val,oldVal) {
        if(val!=undefined&&val[0]!=null){
          for(var j=0;j<val.length;j++){
            var str = val[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for(var i = 0, len = ary.length; i < len; i ++) {
              if(ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if(j==0){
              this.$data.query.startTime1 = str;
            }else {
              this.$data.query.endTime1 = str;
            }
          }
        }else {
          this.$data.query.startTime1 = '';
          this.$data.query.endTime1 = '';
        }
      },
      date2:function (val,oldVal) {
        if(val!=undefined&&val[0]!=null){
          for(var j=0;j<val.length;j++){
            var str = val[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for(var i = 0, len = ary.length; i < len; i ++) {
              if(ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if(j==0){
              this.$data.query.startTime2 = str;
            }else {
              this.$data.query.endTime2 = str;
            }
          }
        }else {
          this.$data.query.startTime2 = '';
          this.$data.query.endTime2 = '';
        }
      },
    },
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
  ul {
    padding: 0;
    margin:0;
  }
  .search{
    margin-bottom: 0;
    label{
      display: block;
      margin-bottom: 0;
    }
  }

  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }

  .mask {
    z-index: 2020;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.45);
    .maskCon {
      margin: 250px auto;
      text-align: left;
      vertical-align: middle;
      background-color: #fff;
      width: 420px;
      border-radius: 3px;
      font-size: 16px;
      overflow: hidden;
      -webkit-backface-visibility: hidden;
      backface-visibility: hidden;
      .head {
        position: relative;
        padding: 20px 20px 0;
        .title {
          padding-left: 0;
          margin-bottom: 0;
          font-size: 16px;
          font-weight: 700;
          height: 18px;
          color: #333;
        }
        i {
          font-family: element-icons !important;
          speak: none;
          font-style: normal;
          font-weight: 400;
          font-variant: normal;
          text-transform: none;
          vertical-align: baseline;
          display: inline-block;
          -webkit-font-smoothing: antialiased;
          position: absolute;
          top: 19px;
          right: 20px;
          color: #999;
          cursor: pointer;
          line-height: 20px;
          text-align: center;
        }
      }
      .body {
        padding: 30px 20px;
        color: #48576a;
        font-size: 14px;
        position: relative;
        div {
          margin: 0;
          line-height: 1.4;
          font-size: 14px;
          color: #48576a;
          font-weight: 400;
        }
      }
      .foot {
        padding: 10px 20px 15px;
        text-align: right;
      }
    }

  }
</style>
