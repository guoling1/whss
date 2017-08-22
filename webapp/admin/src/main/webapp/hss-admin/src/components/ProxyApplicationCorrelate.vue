<template lang="html">
  <div id="productAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border" style="margin-bottom: 15px">
          <h3 class="box-title">关联代理商</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="">
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">代理商类型:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="level" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">代理商编号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="form.markCode" placeholder="请输入内容" @blur="query"></el-input>
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">代理商登录名:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="loginName" placeholder="请输入代理商编号查询" disabled></el-input>
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">代理商名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="proxyName" placeholder="请输入代理商编号查询" disabled></el-input>
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
              <div class="btn btn-primary" @click="submit" style="width: 45%;margin: 20px 0 100px;">
                关 联
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
        level: '',
        loginName: '',
        proxyName: '',
        form: {
          markCode: '',
          id: ''
        }
      }
    },
    created: function () {
      let query = this.$route.query;
      this.form.id = query.id;
      switch (query.level) {
        case '1':
          this.level = '一级代理商';
          break;
        case '2':
          this.level = '二级代理商';
          break;
      }
    },
    methods: {
      query: function () {
        this.$http.post('/admin/dealer/getDealerInfoByMarkCode', {
          markCode: this.form.markCode
        }).then(res => {
          this.loginName = res.data.loginName;
          this.proxyName = res.data.proxyName;
        })
      },
      submit: function () {
        console.log(this.form);
        this.$http.post('/admin/dealer/relation', this.form).then(res => {
          this.$message({
            showClose: true,
            message: '关联成功',
            type: 'success'
          });
        })
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
