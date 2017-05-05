<template lang="html">
  <div id="codeRevoke">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title">撤回二维码</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
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
            <el-row style="margin-bottom: 15px" type="flex" class="row-bg" justify="center">
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
          </div>
        </div>
        <el-row type="flex" class="row-bg" justify="center" style="height: 0px">
          <el-col :span="4"></el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple-light" style="width: 100%">注意：只能撤回未注册的二维码</div>
          </el-col>
          <el-col :span="8"></el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="4">
            <div class="alignRight"></div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple-light" style="width: 100%">
              <div class="btn btn-primary" @click="_$power(create,'boss_qr_code_right_distribute')" style="width: 100%;float: right;margin: 20px 0 100px;">
                立即撤回
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light"></div>
          </el-col>
        </el-row>
        <!--分配成功-->
        <div v-if="isShow">
          <el-dialog :show-close="false" title="撤回结果" v-model="isShow">
            <div class="maskCon">
              <span>撤回号段：</span>
              <span>{{result.startCode}}至{{result.endCode}}</span>
              <!--<ul class="succ" >-->
                <!--<li v-for="cord in issueSuss">{{cord.startCode}}至{{cord.endCode}}</li>-->
              <!--</ul>-->
            </div>
            <div class="maskCon">
              <span>成功撤回：</span>
              <span>{{result.successCount}}个</span>
            </div>
            <div class="maskCon">
              <span>撤回失败：</span>
              <span>{{result.failCount}}个</span>
            </div>
            <div slot="footer" class="dialog-footer" style="text-align: center;">
              <el-button @click="goBack">确定</el-button>
            </div>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'codeRevoke',
    data () {
      return {
        query: {
          sysType: "hss",//hss或hsy
          startCode:"",//开始码段
          endCode:"",//终止码段
        },
        result:{},
        isShow:false,
      }
    },
    methods: {
      //创建
      create: function () {
        this.$http.post('/admin/code/revokeQrCode', this.query)
          .then(function (res) {
            this.result= res.data;
            this.isShow= true;
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      goBack: function () {
        this.isShow= false;
        this.query = {
          sysType: "hss",
          startCode:"",
          endCode:"",
        }
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
  .maskCon{
    margin:0 0 15px 50px
  }
</style>
