<template>
  <div id="storeList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户列表</h3>
          <span @click="synchro" class="btn btn-primary" style="color: #fff;float: right;" v-if="activeName=='first'">同步商户报备状态</span>
        </div>
        <div class="box-body">
          <el-tabs class="tab" v-model="activeName" type="card" @tab-click="handleClick">
            <el-tab-pane label="好收收" name="first">
              <!--筛选-->
              <ul>
                <li class="same">
                  <label>商户编号:</label>
                  <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>商户名称:</label>
                  <el-input style="width: 120px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>所属一级代理:</label>
                  <el-input style="width: 120px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>所属二级代理:</label>
                  <el-input style="width: 120px" v-model="query.proxyName1" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>注册手机号:</label>
                  <el-input style="width: 120px" v-model="query.mobile" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>注册时间:</label>
                  <el-date-picker style="width: 190px" v-model="date" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>认证时间:</label>
                  <el-date-picker style="width: 190px" v-model="date1" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>审核时间:</label>
                  <el-date-picker style="width: 190px" v-model="date2" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" size="small">
                  </el-date-picker>
                </li>
                <li class="same">
                  <label>审核状态:</label>
                  <el-select style="width: 160px" v-model="query.status" clearable placeholder="请选择" size="small">
                    <el-option label="全部" value="">全部</el-option>
                    <el-option label="已注册" value="0">已注册</el-option>
                    <el-option label="已提交基本资料" value="1">已提交基本资料</el-option>
                    <el-option label="待审核" value="2">待审核</el-option>
                    <el-option label="审核通过" value="3">审核通过</el-option>
                    <el-option label="审核未通过" value="4">审核未通过</el-option>
                  </el-select>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search">筛选</div>
                </li>
                <li class="same" style="float: right">
                  <span @click="onload()" download="商户列表" class="btn btn-primary" style="color: #fff">导出</span>
                </li>
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom:15px" :data="records" border>
                <el-table-column type="index" width="70" label="序号"></el-table-column>
                <el-table-column prop="markCode" label="商户编号"></el-table-column>
                <el-table-column prop="merchantName" label="商户名称"></el-table-column>
                <el-table-column prop="proxyName" label="所属一级代理商"></el-table-column>
                <el-table-column prop="proxyName1" label="所属二级代理"></el-table-column>
                <el-table-column prop="createTime" :formatter="changeTime" label="注册时间"></el-table-column>
                <el-table-column prop="mobile" label="注册手机号"></el-table-column>
                <el-table-column label="注册方式">
                  <template scope="scope">
                    <span v-if="records[scope.$index].source==0">扫码注册</span>
                    <span v-if="records[scope.$index].source==1">商户推荐注册</span>
                    <span v-if="records[scope.$index].source==2">代理商推荐注册</span>
                  </template>
                </el-table-column>
                <el-table-column prop="authenticationTime" label="认证时间"></el-table-column>
                <el-table-column prop="checkedTime" label="审核时间"></el-table-column>
                <el-table-column label="状态">
                  <template scope="scope">
                    <span v-if="records[scope.$index].status==0">已注册</span>
                    <span v-if="records[scope.$index].status==1">已提交基本资料</span>
                    <span v-if="records[scope.$index].status==2">待审核</span>
                    <span v-if="records[scope.$index].status==3||records[scope.$index].status==6">审核通过</span>
                    <span v-if="records[scope.$index].status==4">审核未通过</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <router-link :to="{path:'/admin/record/StoreAudit',query:{id:records[scope.$index].id,status:records[scope.$index].status}}" v-if="records[scope.$index].status==2" type="text" size="small">审核</router-link>
                    <router-link :to="{path:'/admin/record/StoreAudit',query:{id:records[scope.$index].id,status:records[scope.$index].status}}" v-if="records[scope.$index].status!=2" type="text" size="small">查看详情</router-link>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="好收银" name="second">
              <!--筛选-->
              <ul>
                <li class="same">
                  <label>商户编号:</label>
                  <el-input style="width: 120px" v-model="query.globalID" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <label>商户名称:</label>
                  <el-input style="width: 120px" v-model="query.shortName" placeholder="请输入内容" size="small"></el-input>
                </li>
                <li class="same">
                  <div class="btn btn-primary" @click="search">筛选</div>
                </li>
                <!--<li class="same" style="float: right">
                  <span @click="onload()" download="商户列表" class="btn btn-primary" style="color: #fff">导出</span>
                </li>-->
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom:15px" :data="records" border>
                <el-table-column type="index" width="100" label="序号"></el-table-column>
                <el-table-column prop="globalID" label="商户编号"></el-table-column>
                <el-table-column prop="shortName" label="商户名称"></el-table-column>
                <el-table-column prop="proxyNames" label="所属代理商"></el-table-column>
                <el-table-column prop="createTime" label="注册时间" :formatter="changeTime"></el-table-column>
                <el-table-column prop="cellphone" label="注册手机号"></el-table-column>
                <el-table-column prop="districtCode" label="省市"></el-table-column>
                <el-table-column prop="industryCode" label="行业"></el-table-column>
                <el-table-column prop="stat" label="状态"></el-table-column>
                <el-table-column label="操作" width="100">
                  <template scope="scope">
                    <router-link :to="{path:'/admin/record/StoreAuditHSY',query:{id:records[scope.$index].id,status:records[scope.$index].status}}" v-if="records[scope.$index].stat=='待审核'" type="text" size="small">审核</router-link>
                    <router-link :to="{path:'/admin/record/StoreAuditHSY',query:{id:records[scope.$index].id,status:records[scope.$index].status}}" v-if="records[scope.$index].stat!='待审核'" type="text" size="small">查看详情</router-link>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
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
        date:'',
        date1:'',
        date2:'',
        url:'',
        loadUrl:'',
        loadUrl1:'',
        fromName:'',
        query:{
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
        vm.$http.post(vm.$data.url,vm.$data.query)
          .then(function (res) {
            vm.$data.loading = false;
            vm.$data.records = res.data.records;
            vm.$data.total = res.data.totalPage;
            vm.$data.count = res.data.count;
            vm.$data.loadUrl1 = res.data.ext;
          }, function (err) {
            vm.$data.loading = false;
            vm.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
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
      //同步
      synchro: function () {
        this.loading = true;
        this.$http.get('/admin/merchantIn/update')
          .then(res => {
            this.loading = false;
            this.$message({
              showClose: true,
              message: '同步成功',
              type: 'success'
            });
          })
          .catch(err => {
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      onload: function () {
         this.$data.loadUrl = this.loadUrl1;
         this.$data.isMask = true;
//        this.$http.post(this.$data.excelUrl, this.$data.query)
//          .then(function (res) {
//            this.$data.isMask = true;
//            this.$data.url = res.data.url;
//          }, function (err) {
//            this.$message({
//              showClose: true,
//              message: err.statusMessage,
//              type: 'error'
//            });
//            this.$data.isMask = false;
//          })
      },
      //格式化hss创建时间
      changeTime: function (row, column) {
        var val=row.createTime;
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
      //选项卡改变时
      handleClick: function (tab,event) {
        this.$data.query={
          pageNo:1,
          pageSize:10,
          shortName:'',
          globalID:'',
          proxyName:'',
          proxyName1:'',
          mobile:'',
          startTime:'',
          endTime:'',
          startTime1:'',
          endTime1:'',
          startTime2:'',
          endTime2:'',
          status:''
        };
        if(event.target.innerHTML=="好收收"){
          this.$data.url='/admin/query/getAll'
        }else if(event.target.innerHTML=="好收银"){
          this.$data.url='/admin/hsyMerchantList/getMerchantList'
        }
        this.$data.records='';
        this.$data.loading = true;
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loadUrl1 = res.data.ext;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      search(){
        this.$data.query.pageNo = 1;
        this.$data.loading = true;
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.loading = true;
        this.$data.records = '';
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      handleSizeChange: function (val) {
        this.query.pageNo = 1;
        this.query.pageSize = val;
        this.loading = true;
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      }
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
