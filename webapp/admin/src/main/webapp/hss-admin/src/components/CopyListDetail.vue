<template lang="html">
  <div id="productAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border" style="margin-bottom: 15px">
          <h3 class="box-title" v-if="isShow">新增产品</h3>
          <h3 class="box-title" v-if="!isShow">产品详情</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="">
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">标题:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="form.title" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">内容:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input type="textarea" size="small" v-model="form.content" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">上传照片:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-upload class="upload-demo"
                             action="/upload/picUpload"
                             :on-success="handleSuccess"
                             :file-list="form.centerImages"
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
          <el-col :span="4">
            <div class="alignRight"></div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple-light" style="width: 100%">
              <!--<div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">返回</div>-->
              <div class="btn btn-primary" @click="create" v-if="isShow" style="width: 45%;margin: 20px 0 100px;">
                创 建 文 案
              </div>
              <div class="btn btn-primary" @click="_$power(change,'boss_product_update')" v-if="!isShow"
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
        form: {
          title: '',
          content: '',
          centerImages: []
        }
      }
    },
    created: function () {

    },
    methods: {
      handleSuccess: function (response, file, fileList) {
        console.log(response.result.url);
      },
      create: function () {
        console.log(this.form);
      }
    },
    computed: {
      $channels: function () {
        return this.channels
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
