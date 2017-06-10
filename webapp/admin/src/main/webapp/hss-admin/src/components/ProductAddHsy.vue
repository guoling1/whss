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
              <el-col :span="3">
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
              <el-col :span="3">
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
              <el-col :span="2">
                <div class="alignRight">添加通道:</div>
              </el-col>
              <el-col :span="15">
                <div class="grid-content bg-purple-light tableSel">
                  <table class="table table-bordered table-hover dataTable">
                    <tbody>
                    <tr>
                      <th>支付方式</th>
                      <th>T1产品结算价</th>
                      <th>T1商户默认费率</th>
                      <th>D1产品结算价</th>
                      <th>D1商户默认费率</th>
                      <th>D0产品结算价</th>
                      <th>D0商户默认费率</th>
                    </tr>
                    <tr v-for="channel in channels">
                      <td style="width: 90px">
                        <span v-if="channel.policyType=='wechat'">微信</span>
                        <span v-if="channel.policyType=='alipay'">支付宝</span>
                        <span v-if="channel.policyType=='withdraw'">提现手续费</span>
                      </td>
                      <td>
                        <input class="input" type="text" name="name" v-model="channel.productTradeRateT1">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.merchantMinRateT1" style="border-bottom: 1px solid #d0d0d0 !important;width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'"></span>
                        &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" name="name" v-model="channel.merchantMaxRateT1" style="border-bottom: 1px solid #d0d0d0 !important;width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input class="input" type="text" name="name" v-model="channel.productTradeRateD1">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.merchantMinRateD1" style="border-bottom: 1px solid #d0d0d0 !important;width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'"></span>
                        &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" name="name" v-model="channel.merchantMaxRateD1" style="border-bottom: 1px solid #d0d0d0 !important;width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input class="input" type="text" name="name" v-model="channel.productTradeRateD0">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'">元/笔</span>
                      </td>
                      <td>
                        <input type="text" name="name" v-model="channel.merchantMinRateD0" style="border-bottom: 1px solid #d0d0d0 !important;width: 33%">
                        <span v-if="channel.policyType!='withdraw'">%</span>
                        <span v-if="channel.policyType=='withdraw'"></span>
                        &nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" name="name" v-model="channel.merchantMaxRateD0" style="border-bottom: 1px solid #d0d0d0 !important;width: 33%">
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
        query: {},
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
        this.$http.post('/admin/product/list')
          .then(function (res) {
            this.query = JSON.parse(JSON.stringify(res.data[this.$route.query.id]));
            this.channels = JSON.parse(JSON.stringify(res.data[this.$route.query.id].list));
            for(let i=0;i<this.channels.length-1;i++){
              this.channels[i].merchantMaxRateD0 = (this.channels[i].merchantMaxRateD0*100).toFixed(2);
              this.channels[i].merchantMaxRateD1 = (this.channels[i].merchantMaxRateD1*100).toFixed(2);
              this.channels[i].merchantMaxRateT1 = (this.channels[i].merchantMaxRateT1*100).toFixed(2);
              this.channels[i].merchantMinRateD0 = (this.channels[i].merchantMinRateD0*100).toFixed(2);
              this.channels[i].merchantMinRateD1 = (this.channels[i].merchantMinRateD1*100).toFixed(2);
              this.channels[i].merchantMinRateT1 = (this.channels[i].merchantMinRateT1*100).toFixed(2);
              this.channels[i].productTradeRateD0 = (this.channels[i].productTradeRateD0*100).toFixed(2);
              this.channels[i].productTradeRateD1 = (this.channels[i].productTradeRateD1*100).toFixed(2);
              this.channels[i].productTradeRateT1 = (this.channels[i].productTradeRateT1*100).toFixed(2);
            }
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
        this.query.list = JSON.parse(JSON.stringify(this.channels));
        for(let i=0;i<this.query.list.length-1;i++){
          this.query.list[i].merchantMaxRateD0 = (this.query.list[i].merchantMaxRateD0/100).toFixed(4);
          this.query.list[i].merchantMaxRateD1 = (this.query.list[i].merchantMaxRateD1/100).toFixed(4);
          this.query.list[i].merchantMaxRateT1 = (this.query.list[i].merchantMaxRateT1/100).toFixed(4);
          this.query.list[i].merchantMinRateD0 = (this.query.list[i].merchantMinRateD0/100).toFixed(4);
          this.query.list[i].merchantMinRateD1 = (this.query.list[i].merchantMinRateD1/100).toFixed(4);
          this.query.list[i].merchantMinRateT1 = (this.query.list[i].merchantMinRateT1/100).toFixed(4);
          this.query.list[i].productTradeRateD0 = (this.query.list[i].productTradeRateD0/100).toFixed(4);
          this.query.list[i].productTradeRateD1 = (this.query.list[i].productTradeRateD1/100).toFixed(4);
          this.query.list[i].productTradeRateT1 = (this.query.list[i].productTradeRateT1/100).toFixed(4);
        }
        this.$http.post('/admin/product/updateHsy',this.query)
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
  .input {
    width: 70% !important;
    border: none;
    border-bottom: 1px solid #d0d0d0 !important;
  }
</style>
