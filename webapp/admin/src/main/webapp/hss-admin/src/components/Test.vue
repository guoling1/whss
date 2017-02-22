<!--生产二维码-->
<!--<template lang="html">
  <div id="issueProduct">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title">生产二维码</h3>
        </div>
        <div class="">
          <div class="table-responsive">
            <el-row style="margin-bottom: 15px" type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight" style="line-height: 42px">产品:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio class="radio" v-model="query.sysType" label="hss">好收收</el-radio>
                  <el-radio class="radio" v-model="query.sysType" label="hsy">好收银</el-radio>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 15px" type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight" style="line-height: 42px">类型:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio class="radio" v-model="query.type" label="1">实体码</el-radio>
                  <el-radio class="radio" v-model="query.type" label="2">电子码</el-radio>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 15px" type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">个数:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.count" placeholder="请输入产码个数"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
          </div>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="4">
            <div class="alignRight"></div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple-light" style="width: 100%">
              <div class="btn btn-primary" @click="create" style="width: 100%;float: right;margin: 20px 0 100px;">
                立即生产
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light"></div>
          </el-col>
        </el-row>
        &lt;!&ndash;生产成功&ndash;&gt;
        <div v-if="isShow">
          <el-dialog :show-close="false" title="分配成功" v-model="isShow">
            <div class="maskCon">
              <span>产品名称：</span>
              <span>{{query.sysType}}</span>
            </div>
            <div class="maskCon">
              <span>类型：</span>
              <span v-if="query.type==1">实体码</span>
              <span v-if="query.type==2">电子码</span>
            </div>
            <div class="maskCon">
              <span>生产个数：</span>
              <span>{{query.count}}</span>
            </div>
            <div class="maskCon">
              <span>生产时间：</span>
              <span>{{}}</span>
            </div>
            <div class="maskCon">
              <span>分配号段：</span>
              <ul class="succ">
                <li>123至3131</li>
              </ul>
            </div>
            <div class="text" v-if="query.type==2">二维码文件10分钟内有效</div>
            <div class="text" v-if="query.type==2">实体码请务必及时下载Excel文件</div>
            <div slot="footer" class="dialog-footer" style="text-align: center;">
              <el-button @click="goBack" v-if="query.type==2">下载文件</el-button>
              &lt;!&ndash;<el-button @click="goBack" v-if="query.type==1">确定</el-button>&ndash;&gt;
            </div>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'issueProduct',
    data () {
      return {
        query: {
          sysType: "",//hss或hsy
          type:'',//1实体码 2电子码
          count:''//分配数量
        },
        isShow:true,
        issueSuss:'',
      }
    },
    created: function () {

    },
    watch: {

    },
    methods: {
      //创建
      create: function () {
          console.log(this.$data.query)
        this.$http.post('/admin/user/distributeQrCodeToDealer', this.$data.query)
          .then(function (res) {
            this.$data.issueSuss= res.data;
            for(var i=0; i<this.$data.issueSuss.length; i++){
              this.$data.totalCount = Number(this.$data.totalCount) + Number(this.$data.issueSuss[i].count)
            }
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      goBack: function () {
        this.$router.push('/admin/record/agentListSec')
      },
    },
  }
</script>

&lt;!&ndash; Add "scoped" attribute to limit CSS to this component only &ndash;&gt;
<style scoped lang="less">
  .alignRight {
    margin-right: 15px;
    text-align: right;
    height: 30px;
    line-height: 30px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  ul {
    list-style-type: none;
    background: #fff;
    border: 1px solid #d0d0d0;
    padding: 5px 0;
    position: absolute;
    z-index: 10;
    overflow: auto;
    max-height: 115px;
  }

  li {
    padding: 5px 10px;
  }
  li:hover{
    background: #3ea0d8;
  }
  .text{
    text-align: center;
    font-size: 20px;
    color: #888;
  }
  .maskCon{
    margin:0 0 15px 50px
  }
  .succ{
    display: inline-table;
    border: none;
    margin-top: -11px;
    position: inherit;
  }
</style>-->

<!--二维码分配记录-->
<!--<template>
  <div id="issueRecord">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">二维码分配记录</h3>
        </div>
        <div class="box-body">
          &lt;!&ndash;筛选&ndash;&gt;
          <ul>
            <li class="same">
              <label>代理商编号:</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>代理商名称:</label>
              <el-input style="width: 120px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>上级代理名称:</label>
              <el-input style="width: 120px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>上级代理编号:</label>
              <el-input style="width: 120px" v-model="query.proxyName1" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>分配方:</label>
              <el-select style="width: 160px" v-model="query.status" clearable placeholder="请选择" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="金开门" value="0">金开门</el-option>
                <el-option label="一级代理" value="1">一级代理</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          &lt;!&ndash;表格&ndash;&gt;
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="markCode" label="分配时间"></el-table-column>
            <el-table-column prop="merchantName" label="代理名称"></el-table-column>
            <el-table-column prop="proxyName" label="代理商编号"></el-table-column>
            <el-table-column prop="proxyName1" label="上级名称"></el-table-column>
            <el-table-column prop="proxyName1" label="上级编号"></el-table-column>
            <el-table-column prop="proxyName1" label="分配个数"></el-table-column>
            <el-table-column prop="proxyName1" label="起始码"></el-table-column>
            <el-table-column prop="proxyName1" label="终止码"></el-table-column>
            <el-table-column prop="proxyName1" label="操作人"></el-table-column>
          </el-table>
          &lt;!&ndash;分页&ndash;&gt;
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'issueRecord',
    data(){
      return {
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
    created: function () {

    },
    methods: {
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
</style>-->

<!--所有二维码-->
<!--<template>
  <div id="issueAll">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">所有二维码</h3>
        </div>
        <div class="box-body">
          &lt;!&ndash;筛选&ndash;&gt;
          <ul>
            <li class="same">
              <label>二维码编号:</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>商户名称:</label>
              <el-input style="width: 120px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>一代名称:</label>
              <el-input style="width: 120px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>二代名称:</label>
              <el-input style="width: 120px" v-model="query.proxyName1" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>产品类型:</label>
              <el-select style="width: 160px" v-model="query.status" clearable placeholder="请选择" size="small">
                <el-option label="好收收" value="hss">好收收</el-option>
                <el-option label="好收银" value="hsy">好收银</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          &lt;!&ndash;表格&ndash;&gt;
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="markCode" label="产品类型"></el-table-column>
            <el-table-column prop="merchantName" label="二维码编号"></el-table-column>
            <el-table-column prop="proxyName" label="分配时间"></el-table-column>
            <el-table-column prop="proxyName1" label="激活时间"></el-table-column>
            <el-table-column prop="proxyName1" label="一代名称"></el-table-column>
            <el-table-column prop="proxyName1" label="二代名称"></el-table-column>
            <el-table-column prop="proxyName1" label="商户名称"></el-table-column>
            <el-table-column prop="proxyName1" label="商户编号"></el-table-column>
            <el-table-column prop="proxyName1" label="店铺名称"></el-table-column>
          </el-table>
          &lt;!&ndash;分页&ndash;&gt;
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'issueAll',
    data(){
      return {
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
    created: function () {

    },
    methods: {
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
</style>-->

<!--产码记录-->
<!--<template>
  <div id="issueProRecord">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">产码记录</h3>

        </div>
        <div class="box-body">
          &lt;!&ndash;筛选&ndash;&gt;
          <ul>
            <li class="same">
              <label>产品类型:</label>
              <el-select style="width: 160px" v-model="query.sysType" clearable placeholder="请选择" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收收" value="hss">好收收</el-option>
                <el-option label="好收银" value="hsy">好收银</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>二维码类型:</label>
              <el-select style="width: 160px" v-model="query.type" clearable placeholder="请选择" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="实体码" value="1">实体码</el-option>
                <el-option label="电子码" value="2">电子码</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          &lt;!&ndash;表格&ndash;&gt;
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="markCode" label="产生时间"></el-table-column>
            <el-table-column prop="merchantName" label="起始号码"></el-table-column>
            <el-table-column prop="proxyName" label="终止号码"></el-table-column>
            <el-table-column prop="proxyName1" label="产生个数"></el-table-column>
            <el-table-column prop="proxyName1" label="二维码类型"></el-table-column>
            <el-table-column prop="proxyName1" label="产品名称"></el-table-column>
            <el-table-column prop="proxyName1" label="操作人"></el-table-column>
          </el-table>
          &lt;!&ndash;分页&ndash;&gt;
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'issueProRecord',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          sysType:'',
          type:''
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {

    },
    methods: {
      //格式化创建时间
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
</style>-->

<!--hss商户详情-->
<!--<template>
  <div id="storeList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户详情</h3>
        </div>
        <div class="box-body">
          <el-tabs class="tab" v-model="activeName" type="card" @tab-click="handleClick">
            <el-tab-pane label="商户注册信息" name="first">
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">注册手机：<span>{{msg.mobile}}</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">注册时间：<span>{{msg.createTime|changeTime}}</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">注册方式：<span>{{msg.registered}}</span></div>
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">一级代理编号：<span>{{msg.firstDealerId}}</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">一级代理名称：<span>{{msg.proxyName}}</span></div>
                </el-col>
                <el-col :span="5"><div class="label"><span></span></div></el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">二级代理编号：<span>{{msg.secondDealerId}}</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">二级代理名称：<span>{{msg.proxyName1}}</span></div>
                </el-col>
                <el-col :span="5"><div class="label"><span></span></div></el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">推荐人编号：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">推荐人名称：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">推荐人注册手机号<span>——</span></div>
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">推荐所属一级代理名：<span>{{msg.proxyNameYq}}</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">推荐所属二级代理名：<span>{{msg.proxyNameYq1}}</span></div>
                </el-col>
                <el-col :span="5"></el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="商户认证信息" name="second">
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">商铺名称（全称）：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">商铺简称：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">商户营业执照号：<span>——</span></div>
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">店主（法人）实名：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">店主身份证号：<span>——</span></div>
                </el-col>
                <el-col :span="5"></el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">商铺上报名称：<span>——</span></div>
                </el-col>
                <el-col :span="5"></el-col>
                <el-col :span="5"></el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">经营种类：<span>——</span></div>
                </el-col>
                <el-col :span="5"></el-col>
                <el-col :span="5"></el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">省市区：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">商铺详细地址：<span></span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label"><span></span></div>
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">资料提交时间：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label">资料审核状态：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                  <div class="label"><span></span></div>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="商户认证资料" name="third">
              <div class="table-responsive">
                <table class="table">
                  <tbody>
                  <tr class="row">
                    <th class="col-md-3" style="text-align: center;">身份证正面:</th>
                    <th class="col-md-3" style="text-align: center;">身份证反面:</th>
                    <th class="col-md-3" style="text-align: center;">手持身份证:</th>
                    <th class="col-md-3" style="text-align: center;">银行卡正面:</th>
                    <th class="col-md-3" style="text-align: center;">手持结算卡:</th>
                  </tr>
                  <tr class="row">
                    <td class="col-md-3" style="text-align: center;border: none;">
                      <img style="width: 200px" @click="changeBig()" :src="msg.identityFacePic" alt=""/>
                    </td>
                    <td class="col-md-3" style="text-align: center;border: none;">
                      <img style="width: 200px"  @click="changeBig()" :src="msg.identityOppositePic" alt=""/>
                    </td>
                    <td class="col-md-3" style="text-align: center;border: none;">
                      <img style="width: 200px"  @click="changeBig()" :src="msg.identityHandPic" alt=""/>
                    </td>
                    <td class="col-md-3" style="text-align: center;border: none;">
                      <img style="width: 200px"  @click="changeBig()" :src="msg.bankPic" alt=""/>
                    </td>
                    <td class="col-md-3" style="text-align: center;border: none;">
                      <img style="width: 200px"  @click="changeBig()" :src="msg.bankHandPic" alt=""/>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </el-tab-pane>
            <el-tab-pane label="商户结算信息" name="fourth">
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">结算卡类型：<span><el-radio-group v-model="msg.isPublic">
                        <el-radio :label="1">对公</el-radio>
                        <el-radio :label="0">对私</el-radio>
                      </el-radio-group></span></div>
                </el-col>
                <el-col :span="5">
                </el-col>
                <el-col :span="5">
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">结算卡开户名：<span>{{msg.cardAccountName}}</span></div>
                </el-col>
                <el-col :span="5">
                </el-col>
                <el-col :span="5">
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px" v-if="msg.isPublic==0">
                <el-col :span="5">
                  <div class="label">身份证号：<span>——</span></div>
                </el-col>
                <el-col :span="5">
                </el-col>
                <el-col :span="5">
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">商户结算卡号：<span>{{msg.cardNO}}</span></div>
                </el-col>
                <el-col :span="5">
                </el-col>
                <el-col :span="5">
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">结算卡所属银行：<span>{{msg.cardBank}}</span></div>
                </el-col>
                <el-col :span="5">
                </el-col>
                <el-col :span="5">
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">支行信息：<span>{{msg.bankAddress}}</span></div>
                </el-col>
                <el-col :span="5">
                </el-col>
                <el-col :span="5">
                </el-col>
              </el-row>
              <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
                <el-col :span="5">
                  <div class="label">结算方式：<span><el-radio-group>
                        <el-radio :label="3">T1结算到卡</el-radio>
                        <el-radio :label="4">T1结算到账户余额</el-radio>
                      </el-radio-group></span></div>
                </el-col>
                <el-col :span="5">
                </el-col>
                <el-col :span="5">
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="商户费率信息" name="fifth">
              <div style="width: 70%;margin: 0 0 15px 15px;overflow: hidden;">
                <template>
                  <el-table :data="tableData" border style="width: 100%">
                    <el-table-column prop="name" label="通道名称" ></el-table-column>
                    <el-table-column prop="rate" label="支付结算手续费"></el-table-column>
                    <el-table-column prop="time" label="结算时间" ></el-table-column>
                    <el-table-column prop="money" label="提现手续费" ></el-table-column>
                  </el-table>
                </template>
              </div>
            </el-tab-pane>
            <el-tab-pane label="商户审核信息" name="sixth">
              <div class="table-responsive">
                <div class="col-sm-12">
                  <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                    <thead>
                    <tr role="row">
                      <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">资料审核状态</th>
                      <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">审核时间</th>
                      <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">审核人</th>
                      <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">批复信息</th>
                    </tr>
                    </thead>
                    <tbody id="content">
                    <tr role="row" class="odd" v-for="re in this.$data.res">
                      <td class="sorting_1">{{re.status|status}}</td>
                      <td>{{re.createTime|changeTime}}</td>
                      <td>—</td>
                      <td>{{re.descr}}</td>
                    </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
        &lt;!&ndash; /.box-body &ndash;&gt;
        <div class="mask" id="mask" style="display: none" @click="isNo()">
          <p @click="isNo">×</p>
          <img src="" alt="">
        </div>
        <div class="" v-if="isShow" style="padding-top: 30px">
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
              <el-col :span="2">
                <div class="label"><span></span></div>
              </el-col>
              <el-col :span="12">
                <div class="label">审核意见：<span>
                  <el-input style="height: 35px;line-height: 35px;width: 50%;" v-model="reason" placeholder="不通过必填" ></el-input>
                  </span></div>
              </el-col>
              <el-col :span="1">
                <div class="label"><span></span></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
              <el-col :span="3">
                <div class="label"><span></span></div>
              </el-col>
              <el-col :span="8">
                <div class="label"><span>
                  <div style="margin-right: 30px" class="btn btn-danger" @click="unAudit">不 通 过</div>
                  <div class="btn btn-success" @click="audit($event)">通 过</div>
                  </span></div>
              </el-col>
              <el-col :span="1">
                <div class="label"><span></span></div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
      &lt;!&ndash; /.box &ndash;&gt;
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name: 'storeList',
    data(){
      return {
        id: '',
        msg:{
          id:'',
          merchantName:'',
          identity:'',
          address:'',
          bankNo:'',
          mobile:'',
          identityFacePic: '',
          identityOppositePic: '',
          identityHandPic: '',
          bankHandPic: '',
          proxyName1:'',
          proxyName: '',
          reserveMobile:'',
          createTime:'',
          proxyNameYQ:'',
          proxyNameYQ1:'',
        },
        reason:'',
        status:'',
        isShow:true,
        res: [],
        activeName:'first',
        tableData:[{
          name:'支付宝',
          rate:'',
          time:'',
          money:''
        },{
          name:'微信',
          rate:'',
          time:'',
          money:''
        },{
          name:'快捷',
          rate:'',
          time:'',
          money:''
        }]
      }
    },
    created:function () {
      this.$data.id = this.$route.query.id;
      if(this.$route.query.status !=2){
        this.$data.isShow = false;
      }
      this.$http.post('/admin/QueryMerchantInfoRecord/getAll',{id:this.$data.id})
        .then(function (res) {
          this.$data.msg = res.data.list[0];
          this.$data.res = res.data.res;
          this.$data.tableData[0].rate = res.data.weixinRate;
          this.$data.tableData[1].rate = res.data.alipayRate;
          this.$data.tableData[2].rate = res.data.fastRate;
        },function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
     },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      },
      audit: function (event) {
        this.$http.post('/admin/merchantInfoCheckRecord/record', {
          merchantId: this.$data.id
        }).then(function (res) {
          this.$router.push('/admin/record/storeList')
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
      },
      unAudit: function () {
        this.$http.post('/admin/merchantInfoCheckRecord/auditFailure',{merchantId: this.$data.id,descr:this.$data.reason})
          .then(function (res) {
            this.$router.push('/admin/record/storeList')
          },function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      changeBig: function (e) {
        e = e||window.event;
        var obj = e.srcElement||e.target;
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        img.src = obj.src;
        mask.style.display= 'block'
      },
      isNo: function () {
        document.getElementById('mask').style.display='none'
      }
    },
    filters: {
      status: function (val) {
        val = Number(val)
        if(val == 0){
          val = "已注册"
        }else if(val == 1){
          val = "已提交基本资料"
        }else if(val == 2){
          val = "待审核"
        }else if(val == 3){
          val = "审核通过"
        }else if(val == 4){
          val="审核未通过"
        }
        return val;
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
      changeDeal: function (val) {
        return val=val?val:'无'
      }
    }
  }
</script>
<style scoped lang="less">
  ul{
    padding: 0;
  }
  .btn{
    font-size: 12px;
  }
  .mask{
    background: rgba(0,0,0,0.8);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top:0;
    left: 0;
  p{
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 1200;
    width: 65px;
    height: 65px;
    line-height: 55px;
    font-size: 65px;
    color: #d2d1d1;
    text-align: center;
    border: 6px solid #adaaaa;
    border-radius: 50%;
    box-shadow: 0 0 16px #000;
    text-shadow: 0 0 16px #000;
  }

  img{
    display: inherit;
    height: 100%;
    margin: 0 auto;
  }
  }
  .label {
    font-weight: bold;
    color: #333;

  span {
    font-weight: normal;
  }
  }
</style>-->

<!--交易查询-->
<template>
  <div id="personnelList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">交易查询</h3>
          <router-link to="/admin/record/deal" class="pull-right btn btn-primary" style="margin-left: 20px">切换旧版</router-link>
          <a :href="'http://'+this.$data.url" download="交易记录" class="btn btn-primary" style="float: right;color: #fff">导出</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>订单号:</label>
              <el-input style="width: 130px" v-model="query.orderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>商户名称:</label>
              <el-input style="width: 130px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>所属一级代理:</label>
              <el-input style="width: 130px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>所属二级代理:</label>
              <el-input style="width: 130px" v-model="query.proxyName1" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>交易日期:</label>
              <el-date-picker
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions2" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <label>交易金额:</label>
              <div class="form-control price">
                <input type="text" name="date" value="" v-model="query.lessTotalFee">至
                <input type="text" name="date" value="" v-model="query.moreTotalFee">
              </div>
            </li>
            <li class="same">
              <label>订单状态:</label>
              <el-select style="width: 120px" clearable v-model="query.status" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待支付" value="1">待支付</el-option>
                <el-option label="支付成功" value="4">支付成功</el-option>
                <el-option label="支付失败" value="3">支付失败</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>结算状态:</label>
              <el-select style="width: 120px" clearable v-model="query.settleStatus" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待结算" value="1">待结算</el-option>
                <el-option label="结算中" value="2">结算中</el-option>
                <el-option label="已结算" value="3">已结算</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>支付方式：</label>
              <el-select style="width: 140px" clearable v-model="query.payType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="微信扫码" value="S">微信扫码</el-option>
                <el-option label="微信二维码" value="N">微信二维码</el-option>
                <el-option label="微信H5收银台" value="H">微信H5收银台</el-option>
                <el-option label="快捷收款" value="B">快捷收款</el-option>
                <el-option label="支付宝扫码" value="Z">支付宝扫码</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" height="640" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="62" label="序号"></el-table-column>
            <el-table-column  label="订单号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].orderNo" type="text" size="small">{{records[scope.$index].orderNo|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column  label="交易流水号" min-width="112">
              <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].sn" type="text" size="small">{{records[scope.$index].sn|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :formatter="changeTime" label="交易日期" width="162"></el-table-column>
            <el-table-column prop="merchantName" label="商户名称" min-width="90"></el-table-column>
            <el-table-column prop="proxyName" label="所属一级" min-width="90"></el-table-column>
            <el-table-column prop="proxyName1" label="所属二级" min-width="90"></el-table-column>
            <el-table-column prop="tradeAmount" :formatter="changeNum" label="支付金额" min-width="90" align="right"></el-table-column>
            <el-table-column prop="payRate" label="手续费率" min-width="90" align="right"></el-table-column>
            <el-table-column prop="appId" label="业务方" min-width="85"></el-table-column>
            <el-table-column prop="status" :formatter="changeStatus" label="订单状态" min-width="90"></el-table-column>
            <el-table-column prop="settleStatus" :formatter="changeSettleStatus" label="结算状态" min-width="90"></el-table-column>
            <el-table-column prop="payType" :formatter="changePayType" label="支付方式" min-width="90"></el-table-column>
            <el-table-column prop="payChannelSign" :formatter="changePayChannel" label="支付渠道" min-width="90"></el-table-column>
            <el-table-column prop="remark" label="渠道信息" min-width="90"></el-table-column>
            <el-table-column label="操作" width="90" fixed="right">
              <template scope="scope">
                <router-link :to="{path:'/admin/record/newDealDet',query:{orderNo:records[scope.$index].orderNo}}" type="text" size="small">详情
                </router-link>
              </template>
            </el-table-column>
          </el-table>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.page"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.size"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  import Clipboard from "clipboard"
  export default{
    name: 'deal',
    data(){
      return {
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
          payType:'',
          proxyName:'',
          proxyName1:''
        },
        date:'',
        records: [],
        count: 0,
        total: 0,
        loading: true,
        url:''
      }
    },
    created: function () {
      var clipboard = new Clipboard('.td');
      //复制成功执行的回调，可选
      clipboard.on('success', (e)=> {
        this.$message({
          showClose: true,
          message: "复制成功  内容为："+e.text,
          type: 'success'
        });
      });
      this.getData()
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/queryOrder/orderList',this.$data.query)
          .then(function (res) {
            this.loading = false;
            this.$data.records =res.data.records;
            this.$data.total=res.data.totalPage;
            this.$data.url=res.data.ext;
            this.$data.count = res.data.count;
            for(var i=0;i<this.records.length;i++){
              if(this.records[i].payRate!=null){
                this.records[i].payRate = (parseFloat(this.records[i].payRate)*100).toFixed(2)+'%';
              }
            }
          },function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
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
      changeNum: function (row, column) {
        var val = row.tradeAmount;
        return parseFloat(val).toFixed(2);
      },
      changePayType: function (row, column) {
        var val = row.payType;
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
      changeStatus: function (row, column) {
        var val = row.status;
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
      changeSettleStatus: function (row, column) {
        var val = row.settleStatus;
        if(val == 2){
          return '结算中'
        }else if(val == 1){
          return '待结算'
        }else if(val == 3){
          return '已结算'
        }
      },
      changePayChannel: function (row, column) {
        var val = row.payChannelSign;
        if(val == 101){
          return '阳光微信扫码'
        }else if(val == 102){
          return '阳光支付宝扫码'
        }else if(val == 103){
          return '阳光银联支付'
        }
      },
      changeHide: function (row, column) {
        var val = row.sn;
        if(val!=""&&val!=null){
          val = val.replace(val.substring(3,val.length-6),"…");
        }
        return val
      },
      changeHide1: function (row, column) {
        var val = row.orderNo;
        if(val!=""&&val!=null){
          val = val.replace(val.substring(3,val.length-6),"…");
        }
        return val
      },
      search(){
        this.$data.query.page = 1;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.page = 1;
        this.$data.query.size = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.page = val;
        this.getData()
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
      }
    },
    filters: {
      changeHide: function (val) {
        if(val!=""&&val!=null){
          val = val.replace(val.substring(3,val.length-6),"…");
        }
        return val
      },
    }
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
  .price{
    display:inline-block;
    width: 210px;
    height:30px;
    /*line-height: 30px;*/
    border-radius: 4px;
    border-color: #bfcbd9;
    position: relative;
    top: 6px;
    input{
      border: none;
      display:inline-block;
      width: 45%;
      height: 25px;
      position: relative;
      top: -3px;
    }
  }
  .price:hover {
    border-color: #20a0ff;
  }

</style>

