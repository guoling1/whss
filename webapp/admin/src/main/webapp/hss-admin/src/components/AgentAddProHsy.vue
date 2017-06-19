<template lang="html">
  <div id="agentAddProHsy">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title">好收银/代理结算政策</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="box-body">
          <el-table style="width: 90%;display: inline-block;vertical-align: top;" :data="channels" border>
            <el-table-column label="支付方式" width="100px">
              <template scope="scope">
                <span v-if="scope.row.policyType=='wechat'">微信</span>
                <span v-if="scope.row.policyType=='alipay'">支付宝</span>
                <span v-if="scope.row.policyType=='withdraw'">提现手续费</span>
              </template>
            </el-table-column>
            <el-table-column label="T1代理商结算价" width="160px">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.dealerTradeRateT1" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.dealerTradeRateT1">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="T1商户费率">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.merchantMinRateT1" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.merchantMinRateT1">
                      <template slot="prepend">最小值</template>
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                  <el-form-item prop="row.merchantMaxRateT1" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.merchantMaxRateT1">
                      <template slot="prepend">最大值</template>
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="D1代理商结算价" width="160px">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.dealerTradeRateD1" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.dealerTradeRateD1">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="D1商户费率">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.merchantMinRateD1" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.merchantMinRateD1">
                      <template slot="prepend">最小值</template>
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                  <el-form-item prop="row.merchantMaxRateD1" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.merchantMaxRateD1">
                      <template slot="prepend">最大值</template>
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="D0代理商结算价" width="160px">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.dealerTradeRateD0" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.dealerTradeRateD0">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="D0商户费率">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.merchantMinRateD0" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.merchantMinRateD0">
                      <template slot="prepend">最小值</template>
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                  <el-form-item prop="row.merchantMaxRateD0" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.merchantMaxRateD0">
                      <template slot="prepend">最大值</template>
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="box-body">
          <el-button type="primary" size="small" @click="_$power(submit,'boss_first_product__update')" style="margin-bottom: 20px"  v-if="level==1&&!isAdd">修改</el-button>
          <el-button type="primary" size="small" @click="_$power(submit,'boss_merchant_check')" v-if="level==1&&isAdd" style="margin-bottom: 20px">开通</el-button>
        </div>
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
            });
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
  input {
    width: 70%;
    border: none;
    border-bottom: 1px solid #d0d0d0;
  }
</style>
