<template lang="html">
  <div id="issue">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title">分配二维码</h3>
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
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">代理名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <input @keyup="find()" class="inp" v-model="name" placeholder="输入代理手机号或名称">
                  <div style="position: relative">
                    <ul class="col-sm-12 list" v-if="listIsShow">
                      <li v-for="(findDealer,index) in findDealers" @click='handleSelect(index)'>
                        <span>{{findDealer.name}}</span>
                        <span>{{findDealer.mobile}}</span>
                      </li>
                    </ul>
                  </div>
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
                <div>未分配二维码：98个</div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 15px" type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight" style="line-height: 42px">分配方式:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio class="radio" v-model="query.distributeType" label="1">按码段</el-radio>
                  <el-radio class="radio" v-model="query.distributeType" label="2">按个数</el-radio>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 15px" type="flex" class="row-bg" justify="center" v-if="query.distributeType==1">
              <el-col :span="4">
                <div class="alignRight">起始码:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.startCode" placeholder="请输入二维码号"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 15px" type="flex" class="row-bg" justify="center" v-if="query.distributeType==1">
              <el-col :span="4">
                <div class="alignRight">终止码:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.endCode" placeholder="请输入二维码号"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 15px" type="flex" class="row-bg" justify="center" v-if="query.distributeType==2">
              <el-col :span="4">
                <div class="alignRight">个数:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.count" placeholder="请输入分配个数"></el-input>
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
                立即分配
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light"></div>
          </el-col>
        </el-row>
        <!--分配成功-->
        <div v-if="isShow">
          <el-dialog :show-close="false" title="分配成功" v-model="isShow">
            <div class="maskCon">
              <span>商户名称：</span>
              <span>{{name}}</span>
            </div>
            <div class="maskCon">
              <span>代理商手机号：</span>
              <span>{{mobile}}</span>
            </div>
            <div class="maskCon">
              <span>分配时间：</span>
              <span>{{issueSuss[0].distributeTime|changeTime}}</span>
            </div>
            <div class="maskCon">
              <span>类型：</span>
              <span v-if="query.type==1">实体码</span>
              <span v-if="query.type==2">电子码</span>
            </div>
            <div class="maskCon">
              <span>分配个数：</span>
              <span>{{totalCount}}笔</span>
            </div>
            <div class="maskCon">
              <span>分配号段：</span>
              <ul class="succ" >
                <li v-for="cord in issueSuss">{{cord.startCode}}至{{cord.endCode}}</li>
              </ul>
            </div>
            <div slot="footer" class="dialog-footer" style="text-align: center;">
              <el-button @click="goBack">返回</el-button>
              <!--<el-button @click="settle(2,records[index].id)">结算已对账部分</el-button>-->
              <!--<el-button @click="settle(3,records[index].id)">强制结算全部</el-button>-->
            </div>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'issue',
    data () {
      return {
        query: {
          sysType: "",//hss或hsy
          dealerId:'',//一级代理商编码
          type:'',//1实体码 2电子码
          distributeType:"1",//分配方式(1按码段 2按个数)
          startCode:"",//开始码段
          endCode:"",//终止码段
          count:''//分配数量
        },
        name:'',
        mobile:'',
        totalCount:'',
        listIsShow:false,
        findDealers:'',
        isShow:false,
        issueSuss:'',
      }
    },
    created: function () {

    },
    watch: {

    },
    filter:{
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
      }
    },
    methods: {
      find: function () {
        if(this.$data.name!=''){
          this.$http.post('/admin/dealer/find',{condition:this.$data.name})
            .then(function(res){
              this.$data.findDealers=res.data;
              if(this.$data.findDealers.length!=0){
                this.$data.listIsShow = true;
              }else {
                this.$data.listIsShow = false;
              }
            },function(err){
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              });
            })
        }else{
          this.$data.listIsShow = false;
        }
      },
      //选中代理时
      handleSelect(index) {
        this.$data.name = this.$data.findDealers[index].name;
        this.$data.mobile = this.$data.findDealers[index].mobile;
        this.$data.query.dealerId = this.$data.findDealers[index].dealerId;
        this.$data.listIsShow = false;
      },
      //创建
      create: function () {
        console.log(this.$data.query)
        this.$http.post('/admin/user/distributeQrCodeToDealer', this.$data.query)
          .then(function (res) {
            this.$data.issueSuss= res.data;
            this.$data.isShow= true;
            this.$data.totalCount='';
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
        this.$router.push('/admin/record/issueRecord')
      },
    },
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
    max-height: 150px;
  }

  li {
    padding: 5px 10px;
  }
  li:hover{
    background: #3ea0d8;
  }
  .inp{
    height: 30px;
    background-color: #fff;
    border-radius: 4px;
    border: 1px solid #bfcbd9;
    color: #1f2d3d;
    display: block;
    padding: 3px 10px;
    transition: border-color .2s cubic-bezier(.645,.045,.355,1);
    width: 100%;
  }
  .inp:focus {
    outline: 0;
    border-color: #20a0ff;
  }
  .maskCon{
    margin:0 0 15px 50px
  }
  .succ{
    border: none;
    position: inherit;
    display: inline-table;
    margin-top: -11px;
  }
  .succ li:hover{
    background: #fff;
  }
</style>
