<template>
  <div id="storeList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户列表</h3>
          <span @click="synchro" class="btn btn-primary" style="color: #fff;float: right;" v-if="activeName=='first'">同步商户报备状态</span>
        </div>
        <div class="box-body">
          <el-tabs class="tab" v-model="activeName" type="card">
            <el-tab-pane label="好收收" name="first">
              <!--筛选-->
              <ul class="search">
                <li class="same">
                  <label>商户编号:</label>
                  <el-input style="width: 188px" v-model="queryHss.markCode" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>商户名称:</label>
                  <el-input style="width: 188px" v-model="queryHss.merchantName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>所属分公司:</label>
                  <el-input style="width: 188px" v-model="queryHss.branchCompany" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>所属一级代理:</label>
                  <el-input style="width: 188px" v-model="queryHss.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>所属二级代理:</label>
                  <el-input style="width: 188px" v-model="queryHss.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>注册手机号:</label>
                  <el-input style="width: 188px" v-model="queryHss.mobile" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>注册时间:</label>
                  <el-date-picker style="width: 188px" v-model="dateHss" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>认证时间:</label>
                  <el-date-picker style="width: 188px" v-model="dateHss1" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>审核时间:</label>
                  <el-date-picker style="width: 188px" v-model="dateHss2" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>审核状态:</label>
                  <el-select style="width: 188px" v-model="queryHss.status" clearable placeholder="请选择" size="small">
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="已注册" value="0">已注册</el-option>
                    <el-option label="已提交基本资料" value="1">已提交基本资料</el-option>
                    <el-option label="待审核" value="2">待审核</el-option>
                    <el-option label="审核通过" value="3">审核通过</el-option>
                    <el-option label="审核未通过" value="4">审核未通过</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search('hss')">筛选</div>
                  <div class="btn btn-primary" @click="reset('hss')">重置</div>
                </li>
                <li class="same" style="float: right">
                  <span @click="_$power(onload,'boss_merchant_export')" download="商户列表" class="btn btn-primary">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom:15px" :data="recordsHss" border>
                <el-table-column type="index" width="62" label="序号"></el-table-column>
                <el-table-column prop="markCode" label="商户编号"></el-table-column>
                <el-table-column prop="merchantName" label="商户名称"></el-table-column>
                <el-table-column prop="branchCompany" label="所属分公司"></el-table-column>
                <el-table-column prop="proxyName" label="所属一级代理商"></el-table-column>
                <el-table-column prop="proxyName1" label="所属二级代理"></el-table-column>
                <el-table-column label="注册时间">
                  <template scope="scope">
                    <span>{{scope.row.createTime|changeTime}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="mobile" label="注册手机号"></el-table-column>
                <el-table-column label="注册方式">
                  <template scope="scope">
                    <span v-if="recordsHss[scope.$index].source==0">扫码注册</span>
                    <span v-if="recordsHss[scope.$index].source==1">商户推荐注册</span>
                    <span v-if="recordsHss[scope.$index].source==2">代理商推荐注册</span>
                  </template>
                </el-table-column>
                <el-table-column prop="authenticationTime" label="认证时间"></el-table-column>
                <el-table-column prop="checkedTime" label="审核时间"></el-table-column>
                <el-table-column label="状态">
                  <template scope="scope">
                    <span v-if="recordsHss[scope.$index].status==0">已注册</span>
                    <span v-if="recordsHss[scope.$index].status==1">已提交基本资料</span>
                    <span v-if="recordsHss[scope.$index].status==2">待审核</span>
                    <span v-if="recordsHss[scope.$index].status==3||recordsHss[scope.$index].status==6">审核通过</span>
                    <span v-if="recordsHss[scope.$index].status==4">审核未通过</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <el-button @click="_$power(scope.row.id,scope.row.status,audit,'boss_merchant_check')" v-if="recordsHss[scope.$index].status==2" type="text" size="small">审核</el-button>
                    <el-button @click="_$power(scope.row.id,scope.row.status,audit,'boss_merchant_detail')" v-if="recordsHss[scope.$index].status!=2" type="text" size="small">查看详情</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div class="block" style="text-align: right">
                <el-pagination @size-change="handleSizeChangeHss"
                               @current-change="handleCurrentChangeHss"
                               :current-page="queryHss.pageNo"
                               :page-sizes="[10, 20, 50]"
                               :page-size="queryHss.pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="countHss">
                </el-pagination>
              </div>
            </el-tab-pane>
            <el-tab-pane label="好收银" name="second">
              <!--筛选-->
              <ul class="search">
                <li class="same">
                  <label>商户编号:</label>
                  <el-input style="width: 188px" v-model="queryHsy.globalID" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>商户名称:</label>
                  <el-input style="width: 188px" v-model="queryHsy.shortName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>注册手机号:</label>
                  <el-input style="width: 188px" v-model="queryHsy.cellphone" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>注册时间:</label>
                  <el-date-picker style="width: 188px" v-model="dateHsy" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>审核时间:</label>
                  <el-date-picker style="width: 188px" v-model="dateHsy1" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <!--<li class="same">
                  <label>所属分公司:</label>
                  <el-input style="width: 188px" v-model="queryHsy.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>-->
                <li class="same">
                  <label>所属一级代理:</label>
                  <el-input style="width: 188px" v-model="queryHsy.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>所属二级代理:</label>
                  <el-input style="width: 188px" v-model="queryHsy.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>报单员:</label>
                  <el-input style="width: 188px" v-model="queryHsy.username" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>姓名:</label>
                  <el-input style="width: 188px" v-model="queryHsy.realname" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>审核状态:</label>
                  <el-select style="width: 188px" v-model="queryHsy.status" clearable placeholder="请选择" size="small">
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="已注册" value="0">已注册</el-option>
                    <el-option label="已提交基本资料" value="1">已提交基本资料</el-option>
                    <el-option label="待审核" value="2">待审核</el-option>
                    <el-option label="审核通过" value="3">审核通过</el-option>
                    <el-option label="审核未通过" value="4">审核未通过</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search('hsy')">筛选</div>
                  <div class="btn btn-primary" @click="reset('hsy')">重置</div>
                </li>
                <li class="same" style="float: right">
                  <span @click="_$power(onload,'boss_merchant_export')" download="商户列表" class="btn btn-primary">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom:15px" :data="recordsHsy" border>
                <el-table-column type="index" width="62" label="序号"></el-table-column>
                <el-table-column prop="globalID" label="商户编号"></el-table-column>
                <el-table-column prop="shortName" label="商户名称"></el-table-column>
                <!--<el-table-column prop="proxyNames" label="所属分公司"></el-table-column>-->
                <el-table-column prop="proxyName" label="所属一级代理"></el-table-column>
                <el-table-column prop="proxyName1" label="所属二级代理"></el-table-column>
                <el-table-column prop="username" label="报单员"></el-table-column>
                <el-table-column prop="realname" label="姓名"></el-table-column>
                <!--<el-table-column prop="proxyNames" label="所属代理商"></el-table-column>-->
                <el-table-column label="注册时间">
                  <template scope="scope">
                    <span>{{scope.row.createTime|changeTime}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="提交时间">
                  <template scope="scope">
                    <span>{{scope.row.commitTime|changeTime}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="审核时间">
                  <template scope="scope">
                    <span>{{scope.row.auditTime|changeTime}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="cellphone" label="注册手机号"></el-table-column>
                <el-table-column prop="districtCode" label="省市"></el-table-column>
                <el-table-column prop="industryCode" label="行业"></el-table-column>
                <el-table-column prop="stat" label="状态"></el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <el-button @click="_$power(scope.row.id,scope.row.status,auditHsy,'boss_merchant_check')" v-if="recordsHsy[scope.$index].stat=='待审核'" type="text" size="small">审核</el-button>
                    <el-button @click="_$power(scope.row.id,scope.row.status,auditHsy,'boss_merchant_detail')" v-if="recordsHsy[scope.$index].stat!='待审核'" type="text" size="small">查看详情</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div class="block" style="text-align: right">
                <el-pagination @size-change="handleSizeChangeHsy"
                               @current-change="handleCurrentChangeHsy"
                               :current-page="queryHsy.pageNo"
                               :page-sizes="[10, 20, 50]"
                               :page-size="queryHsy.pageSize"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="countHsy">
                </el-pagination>
              </div>
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
    name: 'storeList',
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
        dateHss:'',
        dateHss1:'',
        dateHss2:'',
        dateHsy:'',
        dateHsy1:'',
        url:'/admin/query/getAll',
        loadUrl:'',
        loadUrlHss:'',
        loadUrlHsy:'',
        fromName:'',
        queryHss:{
          pageNo:1,
          pageSize:10,
          shortName:'',
          globalID:'',
          proxyName:'',
          mobile:'',
          proxyName1:'',
          startTime:'',
          endTime:'',
          startTime1:'',
          endTime1:'',
          startTime2:'',
          endTime2:'',
          status:''
        },
        queryHsy:{
          pageNo:1,
          pageSize:10,
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
        },
        recordsHss: [],
        recordsHsy: [],
        countHss: 0,
        countHsy: 0,
        currentPage: 1,
        loading: false,
      }
    },
    beforeRouteEnter (to, from, next) {
      next(vm=>{
        vm.$data.fromName = from.name;
        if(vm.$data.fromName=='StoreAuditHSY'){
          vm.$data.activeName='second';
          vm.$data.url='/admin/hsyMerchantList/getMerchantList'
        }else {
          vm.$data.activeName='first';
          vm.$data.url='/admin/query/getAll'
        }
        vm.$data.records = '';
        vm.$data.total = 0;
        vm.$data.count = 0;
      })
    },
    created: function () {
      /*this.getDataHss()
      this.getDataHsy()*/
    },
    methods: {
      reset: function (val) {
        if(val == 'hss'){
          this.dateHss='';
          this.dateHss1='';
          this.dateHss2='';
          this.queryHss = {
            pageNo:1,
            pageSize:10,
            shortName:'',
            globalID:'',
            proxyName:'',
            mobile:'',
            proxyName1:'',
            startTime:'',
            endTime:'',
            startTime1:'',
            endTime1:'',
            startTime2:'',
            endTime2:'',
            status:''
          }
        }else if(val == 'hsy'){
          this.dateHsy='';
          this.dateHsy1='';
          this.queryHsy = {
            pageNo:1,
            pageSize:10,
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
        this.$http.post('/admin/query/getAll',this.queryHss)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.$data.recordsHss = res.data.records;
            },1000)
            this.$data.countHss = res.data.count;
            this.$data.totalHss = res.data.totalPage;
            this.$data.loadUrlHss = res.data.ext;
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
      getDataHsy: function () {
        this.loading = true;
        this.$http.post('/admin/hsyMerchantList/getMerchantList',this.queryHsy)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.$data.recordsHsy = res.data.records;
            },1000)
            this.$data.countHsy = res.data.count;
            this.$data.totalHsy = res.data.totalPage;
            this.$data.loadUrlHsy = res.data.ext;
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
      //同步
      synchro: function () {
        this.loading = true;
        this.$http.get('/admin/merchantIn/update')
          .then(res => {
            setTimeout(()=>{
              this.loading = false;
            },1000)
            this.$message({
              showClose: true,
              message: '同步成功',
              type: 'success'
            });
          })
          .catch(err => {
            setTimeout(()=>{
              this.loading = false;
            },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
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
          this.queryHss.pageNo = 1;
          this.getDataHss();
        }else if(val == 'hsy'){
          this.queryHsy.pageNo = 1;
          this.getDataHsy();
        }
      },
      //当前页改变时
      handleCurrentChangeHss(val) {
        this.queryHss.pageNo = val;
        this.getDataHss()
      },
      handleCurrentChangeHsy(val) {
        this.queryHsy.pageNo = val;
        this.getDataHsy()
      },
      handleSizeChangeHss: function (val) {
        this.queryHss.pageNo = 1;
        this.queryHss.pageSize = val;
        this.getDataHss()
      },
      handleSizeChangeHsy: function (val) {
        this.queryHsy.pageNo = 1;
        this.queryHsy.pageSize = val;
        this.getDataHsy()
      }
    },
    watch:{
      dateHss:function (val,oldVal) {
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
              this.$data.queryHss.startTime = str;
            }else {
              this.$data.queryHss.endTime = str;
            }
          }
        }else {
          this.$data.queryHss.startTime = '';
          this.$data.queryHss.endTime = '';
        }
      },
      dateHss1:function (val,oldVal) {
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
              this.$data.queryHss.startTime1 = str;
            }else {
              this.$data.queryHss.endTime1 = str;
            }
          }
        }else {
          this.$data.queryHss.startTime1 = '';
          this.$data.queryHss.endTime1 = '';
        }
      },
      dateHss2:function (val,oldVal) {
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
              this.$data.queryHss.startTime2 = str;
            }else {
              this.$data.queryHss.endTime2 = str;
            }
          }
        }else {
          this.$data.queryHss.startTime2 = '';
          this.$data.queryHss.endTime2 = '';
        }
      },
      dateHsy:function (val,oldVal) {
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
              this.queryHsy.startTime = str;
            }else {
              this.queryHsy.endTime = str;
            }
          }
        }else {
          this.queryHsy.startTime = '';
          this.queryHsy.endTime = '';
        }
      },
      dateHsy1:function (val,oldVal) {
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
              this.queryHsy.auditTime = str;
            }else {
              this.queryHsy.auditTime1 = str;
            }
          }
        }else {
          this.queryHsy.auditTime = '';
          this.queryHsy.auditTime1 = '';
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
