<template lang="html">
  <div id="agentAddProHsy">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title">好收银/代理结算政策</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="">
          <form class="form-horizontal">
            <div class="box-body">
              <div class="form-group">
                <div class="product">
                  <div class="col-xs-11" style="margin-left: 2%">
                        <div class="box box1">
                          <div class="box-body table-responsive no-padding">
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
                          <!-- /.box-body -->
                        </div>
                        <!-- /.box -->
                      </div>
                </div>
              </div>
            </div>
          </form>
          <!--<template>
            <el-table :data="channels" border style="width:96%;margin: 0 auto;font-size: 12px;">
              <el-table-column prop="name" label="支付方式" width="94">
                <template scope="scope">
                  <span v-if="scope.row.policyType=='wechat'">微信</span>
                  <span v-if="scope.row.policyType=='alipay'">支付宝</span>
                  <span v-if="scope.row.policyType=='withdraw'">提现手续费</span>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="T1代理商结算价" width="135">
                <template scope="scope">
                  <el-input v-model="scope.row.dealerTradeRateT1" size="small">
                    <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                    <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="T1商户费率">
                <template scope="scope">
                  <div style="display: inline-block;width: 45%">
                    <el-input v-model="scope.row.merchantMinRateT1" size="small">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                  </el-input>
                  </div>
                  <span>-</span>
                  <div style="display: inline-block;width: 45%">
                    <el-input v-model="scope.row.merchantMaxRateT1" size="small">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                  </el-input>
                  </div>

                </template>
              </el-table-column>
              <el-table-column prop="name" label="D1代理商结算价" width="135">
                <template scope="scope">
                  <el-input v-model="scope.row.dealerTradeRateD1" size="small">
                    <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                    <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="D1商户费率">
                <template scope="scope">
                  <div style="display: inline-block;width: 45%">
                    <el-input v-model="scope.row.merchantMinRateD1" size="small">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </div>
                  <span>-</span>
                  <div style="display: inline-block;width: 45%">
                    <el-input v-model="scope.row.merchantMaxRateD1" size="small">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </div>

                </template>
              </el-table-column>
              <el-table-column prop="name" label="D0代理商结算价" width="135">
                <template scope="scope">
                  <el-input v-model="scope.row.dealerTradeRateD0" size="small" >
                    <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                    <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                  </el-input>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="D0商户费率">
                <template scope="scope">
                  <div style="display: inline-block;width: 45%">
                    <el-input v-model="scope.row.merchantMinRateD0" size="small">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </div>
                  <span>-</span>
                  <div style="display: inline-block;width: 45%">
                    <el-input v-model="scope.row.merchantMaxRateD0" size="small">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </template>-->
        </div>
        <!--<div class="btn btn-primary" @click="goBack" style="margin: 20px 20px 100px 40px;">返回</div>-->
        <div class="btn btn-primary" @click="_$power(submit,'boss_first_product__update')" style="margin: 20px 0 100px;" v-if="level==1&&!isAdd">修改</div>
        <div class="btn btn-primary" @click="_$power(submit,'boss_merchant_check')" style="margin: 20px 0 100px;" v-if="level==1&&isAdd">开 通</div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
  export default {
    name: 'AgentAddProHsy',
    data () {
      return {
        isAdd:true,
        records:{},
        channels:[{
          id:0,
          dealerId:this.$route.query.dealerId,
          sn:1,
          policyType:'wechat',
          dealerTradeRateT1:'',
          merchantMinRateT1:'',
          merchantMaxRateT1:'',
          dealerTradeRateD1:'',
          merchantMinRateD1:'',
          merchantMaxRateD1:'',
          dealerTradeRateD0:'',
          merchantMinRateD0:'',
          merchantMaxRateD0:''
        },{
          id:0,
          dealerId:this.$route.query.dealerId,
          sn:2,
          policyType:'alipay',
          dealerTradeRateT1:'',
          merchantMinRateT1:'',
          merchantMaxRateT1:'',
          dealerTradeRateD1:'',
          merchantMinRateD1:'',
          merchantMaxRateD1:'',
          dealerTradeRateD0:'',
          merchantMinRateD0:'',
          merchantMaxRateD0:''
        },{
          id:0,
          dealerId:this.$route.query.dealerId,
          sn:3,
          policyType:'withdraw',
          dealerTradeRateT1:'',
          merchantMinRateT1:'',
          merchantMaxRateT1:'',
          dealerTradeRateD1:'',
          merchantMinRateD1:'',
          merchantMaxRateD1:'',
          dealerTradeRateD0:'',
          merchantMinRateD0:'',
          merchantMaxRateD0:''
        }],
        dealerUpgerdeRate1:'',
        dealerUpgerdeRate2:'',
        level:1,
        url:''
      }
    },
    created: function () {
      if(this.$route.query.productId!=0){
          this.$data.isAdd = false;
      }
      if(this.$route.query.level==2){
        this.$data.level = 2;
      }
      this.$http.get('/admin/dealer/dealerRatePolicyDetail/'+this.$route.query.dealerId)
        .then(function (res) {
          if(res.data.length!=0){
            this.$data.channels = res.data;
          }
        })
        .catch(function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        });
    },
    methods: {
      goBack: function () {
        this.$router.go(-1)
      },
      //修改
      submit: function () {
        this.records.dealerId = this.$route.query.dealerId;
        this.records.dealerRatePolicies = this.channels;
        this.$http.post('/admin/dealer/saveOrUpdateDealerRatePolicy', this.records)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '设置成功'
            })
//            this.$message({
//              showClose: true,
//              message: '设置成功',
//              type: 'success'
//            });
//            this.$router.go(-1)
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

  .title2{
    margin-left: 20px;
  }
  input {
    width: 70%;
    border: none;
    border-bottom: 1px solid #d0d0d0;
  }
</style>
