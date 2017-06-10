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
                  <table class="table table-bordered table-hover dataTable">
                    <tbody>
                    <tr>
                      <th>支付方式</th>
                      <th>T1代理商结算价</th>
                      <th>T1商户费率</th>
                      <th>D1代理商结算价</th>
                      <th>D1商户费率</th>
                      <th>D0代理商结算价</th>
                      <th>D0商户费率</th>
                    </tr>
                    <tr v-for="channel in channels">
                      <td style="width: 90px">
                        <span v-if="channel.policyType=='wechat'">微信</span>
                        <span v-if="channel.policyType=='alipay'">支付宝</span>
                        <span v-if="channel.policyType=='withdraw'">提现手续费</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.dealerTradeRateT1">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.merchantMinRateT1" style="width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'"></span>
                        &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" name="name" v-model="channel.merchantMaxRateT1" style="width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.dealerTradeRateD1">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.merchantMinRateD1" style="width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'"></span>
                        &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" name="name" v-model="channel.merchantMaxRateD1" style="width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.dealerTradeRateD0">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.merchantMinRateD0" style="width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'"></span>
                        &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" name="name" v-model="channel.merchantMaxRateD0" style="width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                    </tr>
                    </tbody></table>
                </div>
              </el-col>
              <el-col :span="1"></el-col>
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
      //若为查看详情
      if(this.$route.query.id!=undefined) {
        this.$data.isShow = false;
        this.$data.tableHas = true;
        this.$http.post('/admin/product/list')
          .then(function (res) {
            this.$data.query = res.data[this.$route.query.id];
            this.$data.productId = res.data[this.$route.query.id].productId;
            this.$data.accountId = res.data[this.$route.query.id].accountId;
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
