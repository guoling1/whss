<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">系统对账</h3>
        </div>
        <div class="box-body">
          <ul>
            <li class="same">
              <label>对账渠道:</label>
              <el-input v-model="query.channelName" placeholder="请输入内容" size="small" style="width: 193px"></el-input>
            </li>
            <li class="same">
              <label>交易时间:</label>
              <el-date-picker
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions" size="small" :clearable="false" :editable="false">
              </el-date-picker>
            </li>
            <li class="same">
              <label>交易类型:</label>
              <el-select clearable v-model="query.tradeType" size="small" style="width: 193px">
                <el-option label="交易" value="1"></el-option>
                <el-option label="提现" value="3"></el-option>
              </el-select>
            </li>
            <li class="same">
              <label>对账状态:</label>
              <el-select clearable v-model="query.status" size="small" style="width: 193px">
                <el-option label="未对账" value="0"></el-option>
                <el-option label="已对账" value="1"></el-option>
                <el-option label="已取消" value="2"></el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="$records" border :row-style="tableFoot">
            <el-table-column type="index" width="62" label="序号" fixed="left"></el-table-column>
            <el-table-column prop="channelName" label="通道名称" ></el-table-column>
            <el-table-column label="交易类型" >
              <template scope="scope">
                <span v-if="scope.row.tradeType==1">交易</span>
                <span v-if="scope.row.tradeType==3">提现</span>
              </template>
            </el-table-column>
            <el-table-column label="交易时间">
              <template scope="scope">
                {{scope.row.tradeDate|strSplit}}
              </template>
            </el-table-column>
            <el-table-column prop="balanceCount" label="帐平笔数" align="right" ></el-table-column>
            <el-table-column label="帐平金额（元）" align="right" min-width="90">
              <template scope="scope">{{scope.row.balanceSum|toFix}}</template>
            </el-table-column>
            <el-table-column prop="exCount" label="对方单边笔数"  align="right"></el-table-column>
            <el-table-column label="对方单边金额（元）"  align="right" min-width="90">
              <template scope="scope">{{scope.row.exSum|toFix}}</template>
            </el-table-column>
            <el-table-column prop="inCountWD" label="己方单边笔数"  align="right"></el-table-column>
            <el-table-column label="己方单边金额（元）"  align="right" min-width="90">
              <template scope="scope">{{scope.row.inSumWD|toFix}}</template>
            </el-table-column>
            <el-table-column label="对账状态" align="right" >
              <template scope="scope">
                <span v-if="scope.row.status==0">未对账</span>
                <span v-if="scope.row.status==1">已对账</span>
                <span v-if="scope.row.status==2">已取消</span>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template scope="scope">
                <el-button @click="downloadAcc(scope.row.id)" type="text" size="small" v-if="scope.row.handleType==1">联网对账</el-button>
                <el-button @click="handle(scope.row.id,scope.row.tradeDate,scope.row.tradeType,scope.row.channelName)" type="text" size="small" v-if="scope.row.batchNO==undefined&&scope.row.status!=2&&scope.row.handleType!=1">上传文件</el-button>
                <el-button @click="load(scope.row.batchNO)" type="text" size="small" v-if="scope.row.batchNO!=undefined&&scope.row.status!=2&&scope.row.handleType!=1">下载文件</el-button>
                <el-button @click="handle(scope.row.id,scope.row.tradeDate,scope.row.tradeType,scope.row.channelName)" type="text" size="small" v-if="scope.row.batchNO!=undefined&&scope.row.status!=2&&scope.row.handleType!=1">重新上传</el-button>
                <el-button @click="cancelAcc(scope.row.id)" type="text" size="small" v-if="scope.row.status==0&&scope.row.status!=2">取消对账</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.currentPage"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
        </div>
        <el-dialog title="上传对账文件" v-model="isShow">
          <el-form :label-position="right" label-width="150px">
            <el-form-item label="对账日期：" width="120" style="margin-bottom: 0">
              {{tradeDate|strSplit}}
            </el-form-item>
            <el-form-item label="对账类型：" width="120" style="margin-bottom: 0">
              <span v-if="tradeType==1">交易</span>
              <span v-if="tradeType==3">提现</span>
            </el-form-item>
            <el-form-item label="渠道名称：" width="120" style="margin-bottom: 0">
              {{channelName}}
            </el-form-item>
            <el-form-item label="上传文件：" width="120" style="margin-bottom: 0">
              <el-upload
                class="upload-demo"
                :action="uploadURL"
                :data={id:id}
                :on-preview="handlePreview"
                :on-success="handleSuccess"
                :on-remove="handleRemove"
                :file-list="fileList">
                <el-button id="btn" size="small" type="primary">点击上传</el-button>
                <div style="position: absolute;top: 36px;left:-1px;width: 220px;height: 30px;background: #fff"></div>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-dialog>
        <div class="box box-info mask el-message-box" v-if="isMask">
          <div class="maskCon">
            <div class="head">
              <div class="title">消息</div>
              <i class="el-icon-close" @click="isMask=false"></i>
            </div>
            <div class="body">
              <div>确定下载文件吗？</div>
            </div>
            <div class="foot">
              <a href="javascript:void(0)" @click="isMask=false" class="el-button el-button--default">取消</a>
              <a :href="loadURL+loadURL1" @click="isMask=false" class="el-button el-button-default el-button--primary ">下载</a>
            </div>
          </div>
        </div>
        <div class="box box-info mask el-message-box" v-if="isCancel">
          <div class="maskCon">
            <div class="head">
              <div class="title">取消对账</div>
              <i class="el-icon-close" @click="isCancel=false"></i>
            </div>
            <div class="body">
              <div>确定取消对账吗？</div>
            </div>
            <div class="foot">
              <a href="javascript:void(0)" @click="isCancel=false" class="el-button el-button--default">取消</a>
              <a @click="cancel()" class="el-button el-button-default el-button--primary ">确定</a>
            </div>
          </div>
        </div>
        <div class="box box-info mask el-message-box" v-if="isDownload">
          <div class="maskCon">
            <div class="head">
              <div class="title">联网对账</div>
              <i class="el-icon-close" @click="isDownload=false"></i>
            </div>
            <div class="body">
              <div>确定联网对账吗？</div>
            </div>
            <div class="foot">
              <a href="javascript:void(0)" @click="isDownload=false" class="el-button el-button--default">取消</a>
              <el-button @click="download()" class="el-button el-button-default el-button--primary" :disabled="downloadClick">确定</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'tAuditStore',
    data(){
      return{
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 8.64e7*30||time.getTime() > Date.now();
          }
        },
        downloadClick:false,
        date:'',
        fileList: [],
        records:[],
        count:0,
        tradeDate:'',
        tradeType:'',
        query:{
          pageSize:10,
          currentPage:1,
          channelName:"",
          tradeType:"",
          startDateStr:"",
          endDateStr:"",
          status:""
        },
        id:'',
        cancelId:"",
        isCancel:false,
        loading:true,
        isShow:false,
        isMask:false,
        loadURL1:'',
        isDownload:false,
        downloadId:"",
        //正式
        /*loadURL:'http://checking.qianbaojiajia.com/external/downloadxlsx/',
        url:'http://checking.qianbaojiajia.com/external/statisticList',
        uploadURL:'http://checking.qianbaojiajia.com/external/banlanceAccount',
        cancelUrl:'http://checking.qianbaojiajia.com/external/cancelBalance',
        downloadUrl:'http://checking.qianbaojiajia.com/external/banlanceAccountViaDownload'*/
        //测试
        loadURL:'http://192.168.0.110:8080/balance/external/downloadxlsx/',
        url:'http://192.168.0.110:8080/balance/external/statisticList',
        uploadURL:'http://192.168.0.110:8080/balance/external/banlanceAccount',
        cancelUrl:'http://192.168.0.110:8080/balance/external/cancelBalance',
        downloadUrl:'http://192.168.0.110:8080/balance/external/banlanceAccountViaDownload'
      }
    },
    created: function () {
      this.currentDate();
      this.getData()
    },
    methods: {
      currentDate: function () {
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
            this.query.startDateStr = str;
          } else {
            this.query.endDateStr = str;
          }
        }
      },
      reset: function () {
        this.query = {
          pageSize:10,
          currentPage:1,
          channelName:"",
          tradeType:"",
          startDateStr:"",
          endDateStr:"",
          status:""
        }
        this.currentDate()
      },
      tableFoot(row, index) {
        console.log(row)
        if (row.status == '2') {
          return {background:'#eef1f6'}
        }
        return '';
      },
      downloadAcc:function (id) {
        this.isDownload = true;
        this.downloadId = id;
      },
      download:function () {
        this.downloadClick = true
        this.$http.post(this.downloadUrl,{id:this.downloadId},{emulateJSON: true})
          .then(res => {
          if(res.data.result=='操作成功'){
          this.isDownload = false;
          sessionStorage.setItem('data',JSON.stringify(res.data.jsonPayResult))
//          window.open("http://admin.qianbaojiajia.com/admin/details/accountData");
          this.$router.push('/admin/details/accountData')
            this.downloadClick = false
          }else{
          this.isDownload = false;
          this.$message({
            showClose: true,
            message: res.data.result,
            type: 'warning'
          });
            this.downloadClick = false
          }

          })
          .catch(err =>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      cancelAcc:function (id) {
        this.isCancel = true;
        this.cancelId = id;
      },
      cancel:function () {
        this.$http.post(this.cancelUrl,{id:this.cancelId},{emulateJSON: true})
          .then(res => {
            this.$message({
              showClose: true,
              message: '取消成功',
              type: 'success'
            });
            this.isCancel = false;
            this.getData();
          })
          .catch(err =>{
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      handle: function(id,tradeDate,tradeType,channelName){
        this.isShow=true;
        this.tradeDate=tradeDate;
        this.tradeType=tradeType;
        this.channelName=channelName;
        this.id=id;
      },
      load:function (url) {
        this.isMask = true;
        this.loadURL1 = url;
      },
      handleSuccess(file){
        this.$message({
          showClose: true,
          message: file.result.result,
          type: 'info'
        });
        if(file.result.result=='操作成功'){
          sessionStorage.setItem('data',JSON.stringify(file.result.jsonPayResult));
          this.$router.push('/admin/details/accountData');
//          window.open("http://admin.qianbaojiajia.com/admin/details/accountData");
        }
      },
      getData: function () {
        this.loading = true;
        this.$http.post(this.url,this.query,{emulateJSON: true})
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.$data.records = res.data.list;
          },1000)
            this.$data.count = res.data.page.totalRecord;
          },function (err) {
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
      search: function () {
        this.query.currentPage = 1;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.query.currentPage = 1;
        this.$data.query.pageSize = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.currentPage = val;
        this.getData()
      },
    },
    watch: {
      date: function (val, oldVal) {
        if (val[0] != null) {
          for (var j = 0; j < val.length; j++) {
            var str = val[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for (var i = 0, len = ary.length; i < len; i++) {
              if (ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if (j == 0) {
              this.$data.query.startDateStr = str;
            } else {
              this.$data.query.endDateStr = str;
            }
          }
        } else {
          this.$data.query.startDateStr = '';
          this.$data.query.endDateStr = '';
        }
      }
    },
    computed:{
      $records:function () {
        return this.records
      }
    },
    filters:{
      strSplit:function (val) {
        let a = val.toString().split(' ')
        return a[0]
      }
    }
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
  ul{
    padding: 0;
    margin: 0;
  }
  label{
    display: block;
    margin-bottom: 0;
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
