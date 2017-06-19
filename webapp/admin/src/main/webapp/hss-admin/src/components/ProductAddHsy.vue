<template lang="html">
  <div id="productAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border" style="margin-bottom: 15px">
          <h3 class="box-title" v-if="isShow">新增产品</h3>
          <h3 class="box-title" v-if="!isShow">产品详情</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="box-body">
          <label class="form-label">项目类型:</label>
          <el-select style="width: 20%" v-model="query.type" clearable placeholder="请选择" size="small">
            <el-option label="好收收" value="hss">好收收</el-option>
            <el-option label="好收银" value="hsy">好收银</el-option>
          </el-select>
        </div>
        <div class="box-body">
          <label class="form-label">产品名称:</label>
          <el-input style="width: 20%" size="small" v-model="query.productName" placeholder="请输入内容"></el-input>
          <span>例如：快收银2.0</span>
        </div>
        <div class="box-body">
          <label class="form-label">添加通道:</label>
          <el-table style="width: 90%;display: inline-block;vertical-align: top;" :data="channels" border>
            <el-table-column label="支付方式" width="100px">
              <template scope="scope">
                <span v-if="scope.row.policyType=='wechat'">微信</span>
                <span v-if="scope.row.policyType=='alipay'">支付宝</span>
                <span v-if="scope.row.policyType=='withdraw'">提现手续费</span>
              </template>
            </el-table-column>
            <el-table-column label="T1产品结算价" width="160px">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.productTradeRateT1" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.productTradeRateT1">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="T1商户默认费率">
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
            <el-table-column label="D1产品结算价" width="160px">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.productTradeRateD1" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.productTradeRateD1">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="D1商户默认费率">
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
            <el-table-column label="D0产品结算价" width="160px">
              <template scope="scope">
                <el-form ref="form" :model="scope" label-width="0px" class="demo-ruleForm">
                  <el-form-item prop="row.productTradeRateD0" style="margin:10px 0 20px 0"
                                :rules="{required:true,pattern:/^[0-9]{1,4}([.][0-9]{1,2})?$/,message:'该输入框必填',trigger:'blur'}">
                    <el-input size="small" placeholder="必填"
                              v-model="scope.row.productTradeRateD0">
                      <template slot="append" v-if="scope.row.policyType!='withdraw'">%</template>
                      <template slot="append" v-if="scope.row.policyType=='withdraw'">元/笔</template>
                    </el-input>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column label="D0商户默认费率">
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
          <el-button type="primary" size="small" @click="create" style="margin-bottom: 20px" v-if="isShow">创建产品</el-button>
          <el-button type="primary" size="small" @click="_$power(change,'boss_product_update')"  v-if="!isShow" style="margin-bottom: 20px">修改</el-button>
        </div>
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
