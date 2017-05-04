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
              <el-col :span="8">
                <div class="grid-content bg-purple-light" style="margin: 0 15px;">
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">产品名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.productName" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">例如：快收银2.0</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="3">
                <div class="alignRight">添加通道:</div>
              </el-col>
              <el-col :span="14">
                <div class="grid-content bg-purple-light tableSel">
                  <el-table :data="channels" @selection-change="handleSelectionChange" border v-if="tableHas"
                            style="font-size: 12px">
                    <el-table-column label="通道名称">
                      <template scope="scope">
                        <span>{{channels[scope.$index].channelName}}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="通道编码">
                      <template scope="scope">
                        <span>{{channels[scope.$index].channelCode}}</span>
                      </template>
                    </el-table-column>
                    <el-table-column label="支付结算手续费">
                      <template scope="scope">
                        <input type="text" v-model="channels[scope.$index].productTradeRate">
                        <b>%</b>
                      </template>
                    </el-table-column>
                    <el-table-column label="结算时间" min-width="100">
                      <template scope="scope">
                        <!--<el-select style="width: 100%" v-model="channels[scope.$index].productBalanceType" clearable placeholder="请选择" size="small">
                          <el-option label="D0" value="D0">D0</el-option>
                          <el-option label="T1" value="T1">T1</el-option>
                        </el-select>-->
                        <select class="form-control select2 select2-hidden-accessible" tabindex="-1" aria-hidden="true" v-model="channels[scope.$index].productBalanceType" style="padding: 0">
                          <option value="">请选择</option>
                          <option value="D0">D0</option>
                          <option value="T1">T1</option>
                        </select>
                      </template>
                    </el-table-column>
                    <el-table-column label="提现结算费">
                      <template scope="scope">
                        <input type="text" v-model="channels[scope.$index].productWithdrawFee">
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
                    <el-table-column label="操作" width="72">
                      <template scope="scope">
                        <el-button @click.native.prevent="deleteRow(scope.$index, channels)" type="text" size="small">移除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <span class="btn btn-primary" style="margin: 15px 0" @click="passSelect">添加通道</span>
                </div>
              </el-col>
              <el-col :span="1"></el-col>
            </el-row>

            <el-dialog title="选择通道" v-model="dialogTableVisible">
              <el-table :data="gridData" @selection-change="handleSelectionChange" border style="font-size: 12px">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="通道名称">
                  <template scope="scope">
                    <span >{{gridData[scope.$index].channelName}}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="channelCode" label="通道编码"></el-table-column>
                <el-table-column prop="thirdCompany" label="收单机构"></el-table-column>
                <el-table-column prop="channelSource" label="渠道来源"></el-table-column>
                <el-table-column label="支付费率">
                  <template scope="scope">
                    <span>{{gridData[scope.$index].basicTradeRate}}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="basicBalanceType" label="结算时间"></el-table-column>
                <el-table-column label="结算类型">
                  <template scope="scope">
                    <span v-if="gridData[scope.$index].basicSettleType=='AUTO'">通道自动结算</span>
                    <span v-if="gridData[scope.$index].basicSettleType=='SELF'">自主打款结算</span>
                  </template>
                </el-table-column>
                <el-table-column prop="supportWay" label="支付方式">
                  <template scope="scope">
                    <span v-if="gridData[scope.$index].supportWay=='1'">公众号</span>
                    <span v-if="gridData[scope.$index].supportWay=='2'">扫码</span>
                    <span v-if="gridData[scope.$index].supportWay=='3'">公众号、扫码</span>
                  </template>
                </el-table-column>
              </el-table>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogTableVisible = false">取 消</el-button>
                <el-button type="primary" @click="select">确 定</el-button>
              </div>
            </el-dialog>

            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">支付手续费加价额:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" style="position: relative">
                  <el-input size="small" v-model="query.limitPayFeeRate" placeholder="请输入内容"></el-input>
                  <b>%</b>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">允许一级代理商提高商户的手续费最高限制，例如：0.05%</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">提现手续费加价限额:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" style="position: relative">
                  <el-input size="small" v-model="query.limitWithdrawFeeRate" placeholder="请输入内容"></el-input>
                  <b>元/笔</b>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">允许一级代理商提高商户的手续费最高限制，例如：0.5元／笔</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">商户提现模式:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio class="radio" v-model="query.merchantWithdrawType" label="HAND">手动提现</el-radio>
                  <el-radio class="radio" v-model="query.merchantWithdrawType" label="AUTO">逐笔自动提现</el-radio>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">代理商结算模式:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio class="radio" v-model="query.dealerBalanceType" label="D0">D0</el-radio>
                  <el-radio class="radio" v-model="query.dealerBalanceType" label="D1">日结</el-radio>
                  <el-radio class="radio" v-model="query.dealerBalanceType" label="M1">月结</el-radio>
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
              <!--<div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">返回</div>-->
              <div class="btn btn-primary" @click="create" v-if="isShow" style="width: 45%;margin: 20px 0 100px;">
                创 建 产 品
              </div>
              <div class="btn btn-primary" @click="_$power(change,'boss_product_update')" v-if="!isShow" style="width: 45%;margin: 20px 0 100px;">
                修 改
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
      if (this.channels.length == 0) {
        this.tableHas = false;
      }else {
        this.tableHas = true;
      }
      //获取通道列表
      this.$http.post('/admin/channel/list')
        .then(function (res) {
          this.loading = false;
          this.$data.gridData = res.data;
          this.$data.gridData1 = [].concat(res.data);
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
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      select: function () {
        this.channels = this.channels.concat(this.multipleSelection)
        this.gridData = [].concat(this.gridData1);
        for(var i=0;i<this.multipleSelection.length;i++){
          for (var j=0;j<this.gridData.length;j++){
            if(this.multipleSelection[i].channelName==this.gridData[j].channelName){
              this.gridData.splice(j, 1);
            }
          }
        }
        this.query.channels = this.channels
        this.dialogTableVisible = false
        this.tableHas = true;
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
//              this.$router.push('/admin/record/productList')
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
//              this.$router.push('/admin/record/productList')
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
          this.$router.push('/admin/record/productList')
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
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '修改成功'
            })
//            this.$router.push('/admin/record/productList')
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
