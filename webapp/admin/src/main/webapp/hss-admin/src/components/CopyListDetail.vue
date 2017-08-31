<template lang="html">
  <div id="productAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border" style="margin-bottom: 15px">
          <h3 class="box-title" v-if="isShow">发布中央文案</h3>
          <h3 class="box-title" v-if="!isShow">修改中央文案</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="">
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="2">
                <div class="alignRight">标题:</div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="form.title" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="2">
                <div class="alignRight">内容:</div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">
                  <el-input type="textarea" size="small" v-model="form.content" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center" style="margin-top:10px">
              <el-col :span="2">
                <div class="alignRight">上传照片:</div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">
                  <el-upload class="upload-demo"
                             action="/upload/picUpload"
                             :on-success="handleSuccess"
                             :on-remove="handleRemove"
                             :file-list="fileList"
                             list-type="picture" style="margin-bottom: 10px">
                    <el-button v-if="hasButton" style="float: left;margin-right:250px;" size="small" type="primary">
                      点击上传
                    </el-button>
                  </el-upload>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="2">
            <div class="alignRight"></div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light" style="width: 100%">
              <!--<div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">返回</div>-->
              <div class="btn btn-primary" @click="create" v-if="isShow" style="width: 45%;margin: 20px 0 100px;">
                创 建 文 案
              </div>
              <div class="btn btn-primary" @click="amend" v-if="!isShow"
                   style="width: 45%;margin: 20px 0 100px;">
                修 改 文 案
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light"></div>
          </el-col>
        </el-row>
      </div>
    </div>
    <el-dialog title="提示" v-model="closeWindow" size="tiny" :show-close="false" :close-on-click-modal="false">
      <span>操作完成</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeAll">关闭窗口</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
  export default {
    name: 'productAdd',
    data () {
      return {
        hasButton: true,
        isShow: true,
        closeWindow: false,
        fileList: [],
        form: {
          id: null,
          title: '',
          content: '',
          centerImages: []
        }
      }
    },
    created: function () {
      let query = this.$route.query;
      if (query.type == 2) {
        this.isShow = false;
        this.form.id = query.id;
        this.$http.get('/admin/center/detail/' + query.id).then(res => {
          this.form.title = res.data.title;
          this.form.content = res.data.content;
          for (let i = 0; i < res.data.centerImages.length; i++) {
            this.form.centerImages[i] = [];
            this.form.centerImages[i].imgUrl = res.data.centerImages[i].imgUrl;
            res.data.centerImages[i].url = res.data.centerImages[i].showImgUrl;
          }
          this.fileList = res.data.centerImages;
        })
      }
    },
    methods: {
      fileDeal: function (fileList) {
        this.form.centerImages = [];
        for (let i = 0; i < fileList.length; i++) {
          let obj = {};
          if (!!fileList[i].response) {
            obj.imgUrl = fileList[i].response.result.url
          } else {
            obj.imgUrl = fileList[i].imgUrl
          }
          this.form.centerImages.push(obj);
        }
      },
      handleSuccess: function (response, file, fileList) {
        this.fileDeal(fileList)
      },
      handleRemove(file, fileList) {
        this.fileDeal(fileList)
      },
      create: function () {
        this.$http.post('/admin/center/publish', this.form).then(res => {
          this.closeWindow = true;
        })
      },
      amend: function () {
        this.$http.post('/admin/center/update', this.form).then(res => {
          this.closeWindow = true;
        })
      },
      closeAll: function () {
        window.close()
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less" rel="stylesheet/less">
  .alignRight {
    margin-right: 15px;
    text-align: right;
    height: 30px;
    line-height: 30px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  .right {
    height: 30px;
    line-height: 30px;
    margin-left: 15px;
    color: #999999;
  }

  b {
    height: 30px;
    line-height: 30px;
    margin-right: 15px;
    position: absolute;
    top: 0;
    right: 0;
  }

  .tableSel {
    input {
      width: 100%;
      position: relative;
      border: none;
    }
    b {
      position: absolute;
      top: 7px;
      right: 0;
    }
  }
</style>
