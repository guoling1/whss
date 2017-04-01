<template lang="html">
  <div id="gatewayAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border" style="margin-bottom: 15px">
          <h3 class="box-title" v-if="isShow">新增网关配置</h3>
          <h3 class="box-title" v-if="!isShow">网关配置详情</h3>
        </div>
        <div class="">
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">产品名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="product" clearable placeholder="请选择" size="small">
                    <el-option v-for="product in products" :label="product.productName" value="微信">微信</el-option>
                  </el-select>
                </div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">展示名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="showName" placeholder="最多5个字"></el-input>
                </div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">选择通道:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="channel" clearable placeholder="请选择" size="small">
                    <el-option label="微信" value="微信">微信</el-option>
                  </el-select>
                </div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
          </div>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="4">
            <div class="alignRight"></div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple-light" style="width: 100%">
              <div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">
                返回
              </div>
              <div class="btn btn-primary" @click="create" v-if="isShow"
                   style="width: 45%;float: right;margin: 20px 0 100px;">
                确定
              </div>
              <div class="btn btn-primary" @click="change()" v-if="!isShow"
                   style="width: 45%;float: right;margin: 20px 0 100px;">
                修改
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
  export default {
    name: 'gatewayAdd',
    data () {
      return {
        channel:'',
        showName:'',
        product:'',
        products:[],
        channels:[]
      }
    },
    created: function () {
      this.$http.post('/admin/product/list')
        .then(function (res) {
          this.products = res.data;
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
      //若为查看详情
      if (this.$route.query.id != undefined) {
        this.$data.isShow = false;
        this.$http.post('/admin/channel/list')
          .then(function (res) {
            this.query = res.data[this.$route.query.id];
            this.name = res.data[this.$route.query.id].channelName;
            this.query.isNeed = res.data[this.$route.query.id].isNeed;
            this.query.id = res.data[this.$route.query.id].id;
            this.query.status = res.data[this.$route.query.id].status;
            this.query.accountId = res.data[this.$route.query.id].accountId;
          })
          .catch(err => {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      }
    },
    methods: {
      //创建一级代理
      create: function () {
        this.query.channelName = this.name;
        var isFalse = false;
        for( var i in this.query){
          if(this.query[i]==''){
            isFalse = true;
            break;
          }else {
            isFalse = false
          }
        }
        this.$http.post('/admin/paymentChannel/add', this.$data.query)
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '创建成功',
              type: 'success'
            });
            this.$router.push('/admin/record/passList')
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      goBack: function () {
        this.$router.push('/admin/record/passList')
      },
      //修改
      change: function () {
        this.$http.post('/admin/channel/update', this.$data.query)
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '修改成功',
              type: 'success'
            });
            this.$router.push('/admin/record/passList')
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
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
</style>
