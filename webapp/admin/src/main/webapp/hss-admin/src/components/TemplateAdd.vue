<template lang="html">
  <div id="templateAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border" style="margin-bottom: 15px">
          <h3 class="box-title" v-if="isShow">新增政策模版</h3>
          <h3 class="box-title" v-if="!isShow">政策模版详情</h3>
        </div>
        <div class="">
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">项目类型:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="query.type" clearable placeholder="请选择" size="small">
                    <el-option label="好收收" value="hss">好收收</el-option>
                    <el-option label="好收银" value="hsy">好收银</el-option>
                  </el-select>
                </div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">模板名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.productName" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="3">
                <div class="alignRight">费率模板:</div>
              </el-col>
              <el-col :span="14">
                <div class="grid-content bg-purple-light tableSel">
                  <el-table :data="channels" border style="font-size: 12px;margin-bottom: 15px">
                    <el-table-column label="通道名称">
                      <template scope="scope">
                        <span>{{channels[scope.$index].channelName}}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="支付结算手续费">
                      <template scope="scope">
                        <input type="text" v-model="channels[scope.$index].settleType">
                        <b>%</b>
                      </template>
                    </el-table-column>
                    <el-table-column label="结算时间" min-width="100">
                      <template scope="scope">
                        <select class="form-control select2 select2-hidden-accessible" tabindex="-1" aria-hidden="true" v-model="channels[scope.$index].channelSign" style="padding: 0">
                          <option value="">请选择</option>
                          <option value="D0">D0</option>
                          <option value="T1">T1</option>
                        </select>
                      </template>
                    </el-table-column>
                    <el-table-column label="提现结算费">
                      <template scope="scope">
                        <input type="text" v-model="channels[scope.$index].channelRate">
                        <b>元/笔</b>
                      </template>
                    </el-table-column>
                    <el-table-column prop="basicBalanceType" label="商户支付手续费">
                      <template scope="scope">
                        <input type="text" v-model="channels[scope.$index].productMerchantPayRate">
                        <b>%</b>
                      </template>
                    </el-table-column>
                    <el-table-column prop="basicBalanceType" label="商户提现手续费">
                      <template scope="scope">
                        <input type="text" v-model="channels[scope.$index].productMerchantWithdrawFee">
                        <b>元/笔</b>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-col>
              <el-col :span="1"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="3">
                <div class="alignRight">网关模板:</div>
              </el-col>
              <el-col :span="14">
                <div class="grid-content bg-purple-light tableSel">
                  <el-table max-height="637" style="font-size: 12px;" :data="products" border>
                    <el-table-column prop="productName" label="展示名称"></el-table-column>
                    <el-table-column prop="productName" label="通道名称"></el-table-column>
                    <el-table-column label="操作" min-width="100">
                      <template scope="scope">
                        <el-button type="text" @click="change(scope.$index)">切换</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </el-col>
              <el-col :span="1"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">更新策略:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio class="radio" v-model="query.merchantWithdrawType" label="HAND">对新应用模版代理商有效</el-radio>
                  <el-radio class="radio" v-model="query.merchantWithdrawType" label="AUTO">对所有应用该模版的代理商有效</el-radio>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">修改锁定:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio class="radio" v-model="query.dealerBalanceType" label="D0">锁定</el-radio>
                  <el-radio class="radio" v-model="query.dealerBalanceType" label="D1">不锁定</el-radio>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">锁定后，不可修改应用该模版的代理商通道及费率信息</div>
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
              <div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">
                返回
              </div>
              <div class="btn btn-primary" @click="create" v-if="isShow" style="width: 45%;float: right;margin: 20px 0 100px;">
                创建模板
              </div>
              <div class="btn btn-primary" @click="change()" v-if="!isShow" style="width: 45%;float: right;margin: 20px 0 100px;">
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
    name: 'templateAdd',
    data () {
      return {
        channelData:[],
        tableHas:false,
        dialogTableVisible: false, // 选择通道层
        gridData: [], //选择通道时的列表
        gridData1: [],
        multipleSelection:[],
        password:'',
        query: {
          type: '',
          productName: '',
          limitPayFeeRate:'',
          limitWithdrawFeeRate:'',
          merchantWithdrawType:'',
          dealerBalanceType:'',
          channels:[]
        },
        channels:[],
        id: 0,
        isShow: true,
        productId: ''
      }
    },
    created: function () {
      //获取通道列表
      this.$http.post('/channel/list')
        .then(function (res) {
          this.loading = false;
          this.channelData = res.data;
        }, function (err) {
          this.$data.loading = false;
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
      //若为查看详情
      if(this.$route.query.id!=undefined) {
        this.$data.isShow = false;
        this.$data.tableHas = true;
        this.$http.post('/admin/product/list')
          .then(function (res) {
            this.$data.query = res.data[this.$route.query.id];
            this.$data.productId = res.data[this.$route.query.id].productId;
            this.$data.accountId = res.data[this.$route.query.id].accountId;/*
             for (var i=0;i<data.list.length;i++){
             this.$data.channels[i].productTradeRate=data.list[i].productTradeRate;
             this.$data.channels[i].productWithdrawFee=data.list[i].productWithdrawFee;
             this.$data.channels[i].productMerchantPayRate=data.list[i].productMerchantPayRate;
             this.$data.channels[i].productMerchantWithdrawFee=data.list[i].productMerchantWithdrawFee;
             this.$data.channels[i].id=data.list[i].id;
             this.$data.channels[i].status=data.list[i].status;
             }*/
            for(var i=0;i<this.query.list.length;i++){
              for (var j=0;j<this.gridData.length;j++){
                if(this.query.list[i].basicChannelId==this.gridData[j].id){
                  this.gridData[j].basicChannelId = this.gridData[j].id;
                  this.gridData[j].productTradeRate = this.query.list[i].productTradeRate;
                  this.gridData[j].productBalanceType = this.query.list[i].productBalanceType;
                  this.gridData[j].productWithdrawFee = this.query.list[i].productWithdrawFee;
                  this.gridData[j].productMerchantPayRate = this.query.list[i].productMerchantPayRate;
                  this.gridData[j].productMerchantWithdrawFee = this.query.list[i].productMerchantWithdrawFee;
                  this.gridData[j].id1 = this.query.list[i].id

                  this.channels.push(this.gridData[j]);
                  this.gridData.splice(j, 1);
                  j--;
                }
              }
            }
            this.$data.query.channels = this.$data.channels
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      }

    },
    methods: {
      deleteRow(index, rows) {
        rows.splice(index, 1);
        this.gridData = [].concat(this.gridData1);
        for(var i=0;i<rows.length;i++){
          for (var j=0;j<this.gridData.length;j++){
            if(rows[i].id==this.gridData[j].id){
              this.gridData.splice(j, 1);
            }
          }
        }
      },
      // 添加通道
      passSelect: function () {
        this.dialogTableVisible = true;
      },
      //创建一级代理
      create: function () {
        for(let i= 0;i<this.$data.channels.length;i++){
          this.channels[i].basicChannelId = this.$data.channels[i].id;
          this.channels[i].productBalanceType = this.$data.channels[i].basicBalanceType;
        }
        this.query.channels = this.channels;
        if(this.$data.isShow==true){
          this.$http.post('/admin/product/add',this.query)
            .then(function(res){
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: '创建成功'
              })
              this.$router.push('/admin/record/productList')
            },function(err){
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              });
            })
        }else {
          this.query.productId=this.$data.productId;
          this.query.accountId = this.$data.accountId;
          this.query.list=this.query.channels
          this.$http.post('/admin/product/update',this.query)
            .then(function(res){
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: '修改成功'
              })
              this.$router.push('/admin/record/productList')
            },function(err){
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              });
            })
        }
      },
      goBack: function () {
        this.$router.push('/admin/record/template')
      },
      //修改
      change: function () {
        for(let i= 0;i<this.$data.channels.length;i++){
          this.channels[i].basicChannelId = this.$data.channels[i].id;
          this.channels[i].productBalanceType = this.$data.channels[i].basicBalanceType;
          this.channels[i].id = this.$data.channels[i].id1;
        }
        this.query.channels = this.channels;
        this.query.productId=this.$data.productId;
        this.query.accountId = this.$data.accountId;
        this.query.list=this.query.channels
        this.$http.post('/admin/product/update',this.query)
          .then(function(res){
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
            this.$router.push('/admin/record/productList')
          },function(err){
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      }
    },
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

  .right{
    height: 30px;
    line-height: 30px;
    margin-left: 15px;
    color: #999999;
  }
  b{
    height: 30px;
    line-height: 30px;
    margin-right: 15px;
    position: absolute;
    top:0;
    right: 0;
  }

  .tableSel {
    input {
      width: 100%;
      position: relative;
      border: none;
    }
    b{
      position: absolute;
      top:7px;
      right: 0;
    }
  }
</style>
