<template lang="html">
  <div id="codeProduct">
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
        <!--生产成功-->
        <div v-if="isShow">
          <el-dialog :show-close="true" title="分配成功" v-model="isShow">
            <div class="maskCon">
              <span>产品名称：</span>
              <span>{{query.sysType}}</span>
              <span v-if="query.sysType=='hss'">好收收</span>
              <span v-if="query.sysType=='hsy'">好收银</span>
            </div>
            <div class="maskCon">
              <span>类型：</span>
              <span v-if="query.type==1">实体码</span>
              <span v-if="query.type==2">电子码</span>
            </div>
            <div class="maskCon">
              <span>生产个数：</span>
              <span>{{query.count}}个</span>
            </div>
            <div class="maskCon">
              <span>生产时间：</span>
              <span>{{productionTime|changeTime}}</span>
            </div>
            <div class="maskCon">
              <span>分配号段：</span>
              <ul class="succ">
                <li>{{startCode}}至{{endCode}}</li>
              </ul>
            </div>
            <div class="text" v-if="query.type==2">二维码文件10分钟内有效</div>
            <div class="text" v-if="query.type==2">实体码请务必及时下载Excel文件</div>
            <div slot="footer" class="dialog-footer" style="text-align: center;">
              <a :href="'http://'+this.url" v-if="query.type==2">下载文件</a>
              <el-button @click="this.isShow = false" v-if="query.type==1">确定</el-button>
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
          sysType: "hss",//hss或hsy
          type:'1',//1实体码 2电子码
          count:''//分配数量
        },
        isShow:false,
        url:'',
        startCode:'',
        endCode: ''
      }
    },
    methods: {
      //创建
      create: function () {
        this.$http.post('/admin/code/productionQrCode', this.query)
          .then(function (res) {
            this.isShow = true;
            this.url= res.data.downloadUrl;
            this.startCode= res.data.startCode;
            this.endCode= res.data.endCode;
            this.productionTime= res.data.productionTime;
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      }
    },
    filters: {
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
      }
    }
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
</style>
