<template>
  <div id="storeList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户列表</h3>
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
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
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
              </ul>
              <!--表格-->
              <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
                <el-table-column type="index" width="100" label="序号"></el-table-column>
                <el-table-column prop="globalID" label="商户编号"></el-table-column>
                <el-table-column prop="shortName" label="商户名称"></el-table-column>
                <el-table-column prop="shortName" label="所属代理商"></el-table-column>
                <el-table-column prop="createTime" label="注册时间" :formatter="changeTime"></el-table-column>
                <el-table-column prop="cellphone" label="注册手机号"></el-table-column>
                <el-table-column prop="districtCode" label="省市"></el-table-column>
                <el-table-column prop="industryCode" label="行业"></el-table-column>
                <el-table-column prop="stat" label="状态"></el-table-column>
                <!--<el-table-column label="状态">
                  <template scope="scope">
                    <span v-if="records[scope.$index].status==1">正常</span>
                    <span v-if="records[scope.$index].status==2">待审核</span>
                    <span v-if="records[scope.$index].status==3">审核未通过</span>
                  </template>
                </el-table-column>-->
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
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="count">
            </el-pagination>
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
        fromName:'',
        query:{
          pageNo:1,
          pageSize:10,
          shortName:'',
          globalID:'',
          proxyName:'',
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
    /*created:function () {
      if(this.$data.activeName=='second'){
        this.$data.url='/admin/hsyMerchantList/getMerchantList'
      }else {
        this.$data.url='/admin/query/getAll'
      }
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
    },*/
    methods: {
      toDet: function (val) {

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
    },
    watch:{
      date:function (val,oldVal) {
        if(val[0]!=null){
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
        if(val[0]!=null){
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
        if(val[0]!=null){
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
<style scoped lang="less">
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
</style>
