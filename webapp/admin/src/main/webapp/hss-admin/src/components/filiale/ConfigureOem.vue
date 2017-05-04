<template lang="html">
  <div id="agentAddBase">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title">配置O单</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="">
          <div class="box-header">
            <h3 class="box-title title2">基本设置：</h3>
          </div>
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">品牌名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.brandName" placeholder="限制4个汉字" maxlength="4"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">微信公众号名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.wechatName" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">微信号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.wechatCode" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">微信AppID:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.appId" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">微信AppSecret:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input type="password" size="small" v-model="query.appSecret" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
          </div>
        </div>
        <div>
          <div class="box-header">
            <h3 class="box-title title2">模板消息设置：</h3>
          </div>
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight"></div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-table :data="query.templateInfos" style="width: 100%">
                    <el-table-column prop="templateName" label="消息名称"></el-table-column>
                    <el-table-column label="模板ID">
                      <template scope="scope">
                        <input v-model="scope.row.templateId" type="text" style="border: none;border-bottom: 1px solid #D1D1D1">
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
          </div>
        </div>
        <div>
          <div class="box-header">
            <h3 class="box-title title2">个性化设置：</h3>
          </div>
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">公众号二维码:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" id="phone1">
                  <el-upload id="upload" style="position: relative" action="/upload/picUpload" type="drag"
                             :thumbnail-mode="true"
                             :on-progress="handleProgress" :on-preview="handlePreview" :on-remove="handleRemove1"
                             :on-success="handleSuccess1" :default-file-list="fileList1">
                    <i class="el-icon-upload"></i>
                    <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                    <!--<div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>-->
                  </el-upload>
                  <div
                    style="position: absolute;top: 127px;margin-left:1px;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div
                    style="position: absolute;top: 3px;margin-left:1px;width: 200px;height: 30px;background: #fbfdff"></div>
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
              <!--<div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">-->
                <!--返回-->
              <!--</div>-->
              <div class="btn btn-primary" @click="create" style="width: 45%;margin: 20px 0 100px;">配置O单</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light"></div>
          </el-col>
        </el-row>
      </div>
      <div class="mask" id="mask" v-show="isMask" @click="isMask = false">
        <p @click="isMask = false">×</p>
        <img src="" alt="">
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  import Message from '../Message.vue'
  export default {
    name: 'agentAddBase',
    data () {
      return {
        dialogFormVisible: false,
        password:'',
        provinces: '',
        citys: '',
        province: '',
        city: '',
        level: '',
        query: {},
        tableData:[],
        id: 0,
        productId: '',
        fileList1: []
      }
    },
    created: function () {
      //若为查看详情
      this.$http.get('/admin/dealer/oemDetail/' + this.$route.query.dealerId)
        .then(function (res) {
          this.query = res.data;
        })
        .catch(err =>{
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
    },
    methods: {
      create: function () {
        this.query.dealerId = this.$route.query.dealerId;
        this.$http.post('/admin/dealer/addOrUpdateOem', this.query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '配置成功'
            })
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      //修改
      change: function () {
        this.$data.query.dealerId = this.$data.query.id;
        this.$http.post('/admin/user/updateDealer2', this.query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '修改成功'
            })
//            }
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      handleSuccess1: function (response, file, fileList) {
        this.query.qrCode = response.result.url
        //移除继续上传按钮
        setTimeout(function () {
          var aSpan = document.getElementById('phone1').getElementsByTagName('span')[0];
          if (document.getElementsByClassName('el-draggeer__cover__btns').length == 1) {
            document.getElementsByClassName('el-draggeer__cover__btns')[0].removeChild(aSpan)
          } else {
            document.getElementsByClassName('el-draggeer__cover__btns')[1].removeChild(aSpan)
          }
        }, 300)
      },
      handleRemove1: function () {
        this.query.identityOppositePic = ''
      },
      handleErr:function (err) {
        this.$message({
          showClose: true,
          message: err.statusMessage,
          type: 'error'
        });
      },
      //查看照片
      handlePreview: function (file) {
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        img.src = file.url;
        this.isMask = true
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

  .title2 {
    margin-left: 10%;
  }
  .mask {
    background: rgba(0, 0, 0, 0.8);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
  p {
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

  img {
    display: inherit;
    max-height: 100%;
    max-width: 100%;
    margin: 0 auto;
  }
  }
</style>
