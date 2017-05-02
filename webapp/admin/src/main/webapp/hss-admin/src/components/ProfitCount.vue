<template>
  <div id="profitCount">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">分润统计</h3>
        </div>
        <div class="box-body">
          <el-tabs class="tab" v-model="activeName" type="card" >
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
                  <el-select style="width: 190px" clearable v-model="queryCom.businessType" size="small" >
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="好收收-收款" value="hssPay">好收收-收款</el-option>
                    <el-option label="好收收-提现" value="hssWithdraw">好收收-提现</el-option>
                    <el-option label="好收收-升级费" value="hssUpgrade">好收收-升级费</el-option>
                    <el-option label="好收银-收款" value="hsyPay">好收银-收款</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search('Com')">筛选</div>
                  <div class="btn btn-primary" @click="reset('Com')">重置</div>
                </li>
                <li class="same" style="color: #fff;float: right">
                  <span @click="_$power(onload,'boss_company_share_export')" download="公司分润" class="btn btn-primary">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px;" :data="recordsCom" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    <span>{{scope.$index+1}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="收益日期">
                  <template scope="scope">
                    <span>{{scope.row.splitDate|changeDate}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="businessType" label="收益类型"></el-table-column>
                <el-table-column prop="splitAmount" label="收益金额" align="right" header-align="left">
                  <template scope="scope">
                    <span>{{scope.row.splitAmount|toFix}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <router-link target="_blank" :to="{path:'/admin/details/profitComDet',query:{type:recordsCom[scope.$index].businessType,time:recordsCom[scope.$index].splitDate}}" v-if="recordsCom[scope.$index].splitAmount!=0" type="text" size="small">明细
                    </router-link>
                  </template>
                </el-table-column>
              </el-table>
              <div class="block" style="text-align: right">
                <el-pagination @size-change="handleSizeChangeCom"
                               @current-change="handleCurrentChangeCom"
                               :current-page="queryCom.pageNo"
                               :page-sizes="[10, 20, 50]"
                               :page-size="queryCom.pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="countCom">
                </el-pagination>
              </div>
              <ul style="float: left;margin-top: 5px">
                <li>
                  <label style="margin-right: 10px;">收益金额</label>
                  <span>当页总额：{{pageTotalCom}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
                  <span>筛选条件统计：{{addTotalCom}}&nbsp;元</span>
                </li>
              </ul>
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
                  <el-input style="width: 190px" v-model="queryFir.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>代理商编号:</label>
                  <el-input style="width: 190px" v-model="queryFir.markCode" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>收益类型:</label>
                  <el-select style="width: 190px" clearable v-model="queryFir.businessType" size="small" >
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="好收收-收款" value="hssPay">好收收-收款</el-option>
                    <el-option label="好收收-提现" value="hssWithdraw">好收收-提现</el-option>
                    <el-option label="好收收-升级费" value="hssUpgrade">好收收-升级费</el-option>
                    <el-option label="好收银-收款" value="hsyPay">好收银-收款</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search('Fir')">筛选</div>
                  <div class="btn btn-primary" @click="reset('Fir')">重置</div>
                </li>
                <li class="same" style="color: #fff;float: right">
                  <span @click="_$power(onload,'boss_first_share_export')" download="公司分润" class="btn btn-primary" style="color: #fff;float: right">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="recordsFir" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    <div>{{scope.$index+1}}</div>
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
                <el-table-column prop="splitAmount" label="收益金额" align="right" header-align="left">
                  <template scope="scope">
                    <span>{{scope.row.splitAmount|toFix}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <router-link target="_blank" :to="{path:'/admin/details/profitFirDet',query:{type:recordsFir[scope.$index].businessType,id:recordsFir[scope.$index].receiptMoneyAccountId,time:recordsFir[scope.$index].splitDate}}" v-if="recordsFir[scope.$index].splitAmount!=0" type="text" size="small">明细
                    </router-link>
                  </template>
                </el-table-column>
              </el-table>
              <div class="block" style="text-align: right">
                <el-pagination @size-change="handleSizeChangeFir"
                               @current-change="handleCurrentChangeFir"
                               :current-page="queryFir.pageNo"
                               :page-sizes="[10, 20, 50]"
                               :page-size="queryFir.pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="countFir">
                </el-pagination>
              </div>
              <ul style="float: left;margin-top: 5px">
                <li>
                  <label style="margin-right: 10px;">收益金额</label>
                  <span>当页总额：{{pageTotalFir}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
                  <span>筛选条件统计：{{addTotalFir}}&nbsp;元</span>
                </li>
              </ul>
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
                  <el-input style="width: 190px" v-model="querySec.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>代理商名称:</label>
                  <el-input style="width: 190px" v-model="querySec.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>代理商编号:</label>
                  <el-input style="width: 190px" v-model="querySec.markCode" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>收益类型:</label>
                  <el-select style="width: 190px" clearable v-model="querySec.businessType" size="small" >
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="好收收-收款" value="hssPay">好收收-收款</el-option>
                    <el-option label="好收收-提现" value="hssWithdraw">好收收-提现</el-option>
                    <el-option label="好收收-升级费" value="hssUpgrade">好收收-升级费</el-option>
                    <el-option label="好收银-收款" value="hsyPay">好收银-收款</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search('Sec')">筛选</div>
                  <div class="btn btn-primary" @click="reset('Sec')">重置</div>
                </li>
                <li class="same" style="color: #fff;float: right">
                  <span @click="_$power(onload,'boss_seconde_share_export')" download="公司分润" class="btn btn-primary">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="recordsSec" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    <div>{{scope.$index+1}}</div>
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
                <el-table-column prop="splitAmount" label="收益金额" align="right" header-align="left">
                  <template scope="scope">
                    <span>{{scope.row.splitAmount|toFix}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <router-link target="_blank" :to="{path:'/admin/details/profitSecDet',query:{type:recordsSec[scope.$index].businessType,id:recordsSec[scope.$index].receiptMoneyAccountId,time:recordsSec[scope.$index].splitDate}}" v-if="recordsSec[scope.$index].splitAmount!=0" type="text" size="small">明细
                    </router-link>
                  </template>
                </el-table-column>
              </el-table>
              <div class="block" style="text-align: right">
                <el-pagination @size-change="handleSizeChangeSec"
                               @current-change="handleCurrentChangeSec"
                               :current-page="querySec.pageNo"
                               :page-sizes="[10, 20, 50]"
                               :page-size="querySec.pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="countSec">
                </el-pagination>
              </div>
              <ul style="float: left;margin-top: 5px">
                <li>
                  <label style="margin-right: 10px;">收益金额</label>
                  <span>当页总额：{{pageTotalSec}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
                  <span>筛选条件统计：{{addTotalSec}}&nbsp;元</span>
                </li>
              </ul>
            </el-tab-pane>
          </el-tabs>

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
        url:'/admin/allProfit/companyProfit',
        totalUrl:'/admin/allProfit/companyAmount',
        loadUrl:'',
        loadUrl1:'',
        loadUrlCom:'',
        fromName:'',
        queryCom:{
          pageNo:1,
          pageSize:10,
          startTime:'',
          endTime:'',
          businessType:''
        },
        queryFir:{
          pageNo:1,
          pageSize:10,
          startTime:'',
          endTime:'',
          businessType:'',
          proxyName:'',
          markCode:'',
        },
        querySec:{
          pageNo:1,
          pageSize:10,
          startTime:'',
          endTime:'',
          businessType:'',
          proxyName:'',
          markCode:'',
          proxyName1:'',
        },
        startTime:'',
        endTime:'',
        records: [],
        recordsCom: [],
        recordsFir: [],
        recordsSec: [],
        count: 0,
        countCom: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    beforeRouteEnter (to, from, next) {
      next(vm=>{
        vm.$data.fromName = from.name;
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
          this.startTime = str;
        } else {
          this.endTime = str;
        }
      }
      this.queryCom.startTime = this.startTime;
      this.queryCom.endTime = this.endTime;
      this.queryFir.startTime = this.startTime;
      this.queryFir.endTime = this.endTime;
      this.querySec.startTime = this.startTime;
      this.querySecZ.endTime = this.endTime;
      this.getDataCom();
      this.getDataFir();
      this.getDataSec();
      this.getAddTotalCom();
      this.getAddTotalFir();
      this.getAddTotalSec();
    },
    methods: {
      reset: function (val) {
        if(val == 'Com'){
          this.queryCom = {
            pageNo:1,
            pageSize:10,
            startTime:this.startTime,
            endTime:this.endTime,
            businessType:''
          }
        }else if(val == 'Fir'){
          this.queryFir = {
            pageNo:1,
            pageSize:10,
            startTime:this.startTime,
            endTime:this.endTime,
            businessType:'',
            proxyName:'',
            markCode:'',
          }
        }else if(val == 'Sec'){
          this.querySec = {
            pageNo:1,
            pageSize:10,
            startTime:this.startTime,
            endTime:this.endTime,
            businessType:'',
            proxyName:'',
            markCode:'',
            proxyName1:''
          }
        }
      },
      onload: function () {
        this.$data.loadUrl = this.loadUrl1;
        this.$data.isMask = true;
      },
      getDataCom: function () {
        this.loading = true;
        this.$http.post('/admin/allProfit/companyProfit',this.queryCom)
          .then(function (res) {
            this.loading = false;
            this.recordsCom = res.data.records;
            this.countCom = res.data.count;
            this.loadUrlCom = res.data.ext;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            var total=0;
            for (let i = 0; i < this.$data.recordsCom.length; i++) {
//              this.$data.records[i].splitAmount = toFix(this.$data.records[i].splitAmount);
              total = toFix(parseFloat(total)+parseFloat(this.$data.recordsCom[i].splitAmount))
            }
            this.pageTotalCom = total;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      getDataFir: function () {
        this.loading = true;
        this.$http.post('/admin/allProfit/firstProfit',this.queryFir)
          .then(function (res) {
            this.loading = false;
            this.recordsFir = res.data.records;
            this.countFir = res.data.count;
            this.loadUrlFir = res.data.ext;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            var total=0;
            for (let i = 0; i < this.$data.recordsFir.length; i++) {
//              this.$data.records[i].splitAmount = toFix(this.$data.records[i].splitAmount);
              total = toFix(parseFloat(total)+parseFloat(this.$data.recordsFir[i].splitAmount))
            }
            this.pageTotalFir = total;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      getDataSec: function () {
        this.loading = true;
        this.$http.post('/admin/allProfit/secondProfit',this.querySec)
          .then(function (res) {
            this.loading = false;
            this.recordsSec = res.data.records;
            this.countSec = res.data.count;
            this.loadUrlSec = res.data.ext;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            var total=0;
            for (let i = 0; i < this.$data.recordsSec.length; i++) {
//              this.$data.records[i].splitAmount = toFix(this.$data.records[i].splitAmount);
              total = toFix(parseFloat(total)+parseFloat(this.$data.recordsSec[i].splitAmount))
            }
            this.pageTotalSec = total;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      /*getData: function () {
        this.loading = true;
        this.$http.post(this.url,this.query)
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
      },*/
      //选项卡改变时
      /*handleClick: function (tab,event) {
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
      },*/
      search(val){
        if(val == 'Com'){
          this.queryCom.pageNo = 1;
          this.getDataCom();
          this.getAddTotalCom();
        }else if(val == 'Fir'){
          this.queryFir.pageNo = 1;
          this.getDataFir();
          this.getAddTotalFir();
        }else if(val == 'Sec'){
          this.querySec.pageNo = 1;
          this.getDataSec();
          this.getAddTotalSec();
        }
      },
      //当前页改变时
      handleCurrentChangeCom(val) {
        this.queryCom.pageNo = val;
        this.getDataCom()
      },
      handleCurrentChangeFir(val) {
        this.queryFir.pageNo = val;
        this.getDataFir()
      },
      handleCurrentChangeSec(val) {
        this.querySec.pageNo = val;
        this.getDataSec()
      },
      handleSizeChangeCom: function (val) {
        this.queryCom.pageNo = 1;
        this.queryCom.pageSize = val;
        this.loading = true;
        this.getDataCom()
        this.getAddTotalCom();
      },
      handleSizeChangeFir: function (val) {
        this.queryFir.pageNo = 1;
        this.queryFir.pageSize = val;
        this.loading = true;
        this.getDataFir()
        this.getAddTotalFir();
      },
      handleSizeChangeSec: function (val) {
        this.querySec.pageNo = 1;
        this.querySec.pageSize = val;
        this.loading = true;
        this.getDataSec()
        this.getAddTotalSec();
      },
      onload: function () {
        this.$data.loadUrl = this.loadUrl1;
        this.$data.isMask = true;
      },
      getAddTotalCom(){
        this.$http.post('/admin/allProfit/companyAmount',this.queryCom)
          .then(res=>{
            this.addTotalCom = res.data;
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
      getAddTotalFir(){
        this.$http.post('/admin/allProfit/firstAmount',this.queryFir)
          .then(res=>{
            this.addTotalFir = res.data;
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
      getAddTotalSec(){
        this.$http.post('/admin/allProfit/secondAmount',this.querySec)
          .then(res=>{
            this.addTotalSec = res.data;
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
      /*getAddTotal(){
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
      },*/
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
              this.$data.startTime = str;
            }else {
              this.$data.endTime = str;
            }
          }
        }else {
          this.$data.startTime = '';
          this.$data.endTime = '';
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
