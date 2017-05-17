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
                <div class="alignRight">分公司名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="name" placeholder="最多5个字"></el-input>
                </div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">产品名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="productType" disabled placeholder="请选择" size="small">
                    <el-option v-for="product in products" :label="product.productName" :value="product.type"></el-option>
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
                  <el-input size="small" v-model="viewChannelName" placeholder="最多5个字"></el-input>
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
                  <el-select style="width: 100%" v-model="channelSign" clearable placeholder="请选择" size="small">
                    <el-option v-for="channel in channels" :label="channel.channelName" :value="channel.channelTypeSign"></el-option>
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
        isShow:true,
        productType:'',
        channelSign:'',
        viewChannelName:'',
        products:[],
        channels:[],
        productId:'',
        id:'',
        name:''
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
      this.$http.post('/admin/channel/list')
        .then(function (res) {
          this.channels = res.data;
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
      //若为查看详情
      if (this.$route.query.index != undefined) {
        this.isShow = false;
        this.$http.post('/admin/product/listGateway',{"productType":"hss"})
          .then(res => {
            this.productType = res.data[this.$route.query.index].productType;
            this.viewChannelName = res.data[this.$route.query.index].viewChannelName;
            this.channelSign = res.data[this.$route.query.index].channelSign;
            this.id = res.data[this.$route.query.index].id;
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
      create: function () {
        this.$http.post('/admin/product/addGateway', {
          productType:this.productType,
          channelSign:this.channelSign,
          viewChannelName:this.viewChannelName,
        })
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '创建成功',
              type: 'success'
            });
            this.$router.push('/admin/details/configureGateway')
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      goBack: function () {
        this.$router.push('/admin/details/configureGateway')
      },
      change: function () {
        if(this.productType=='hss'){
          this.productId = 6;
        }else if(this.productType=='hsy'){
          this.productId = 7;
        }
        this.$http.post('/admin/product/updateGateway', {
          productType:this.productType,
          channelSign:this.channelSign,
          viewChannelName:this.viewChannelName,
          productId:this.productId,
          id: this.id
        })
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '创建成功',
              type: 'success'
            });
            this.$router.push('/admin/details/configureGateway')
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
