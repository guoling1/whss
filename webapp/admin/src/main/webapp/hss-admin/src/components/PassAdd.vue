<template lang="html">
  <div id="passAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border" style="margin-bottom: 15px">
          <h3 class="box-title" v-if="isShow">新增通道</h3>
          <h3 class="box-title" v-if="!isShow">通道详情</h3>
        </div>
        <div class="">
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">通道名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.channelShortName" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">例如：华有支付宝</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">原始名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="name" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">通道编码:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.channelCode" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">例如：SM_Alipay</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">支付方式:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-checkbox-group v-model="payWay">
                    <el-checkbox label="微信公众号" v-if="nameType=='wx'||nameType==''"></el-checkbox>
                    <el-checkbox label="微信公众号" v-if="nameType=='yl'||nameType=='zfb'" disabled></el-checkbox>
                    <el-checkbox label="微信扫码" v-if="nameType=='wx'||nameType==''"></el-checkbox>
                    <el-checkbox label="微信扫码" v-if="nameType=='yl'||nameType=='zfb'" disabled></el-checkbox>
                    <el-checkbox label="支付宝公众号" v-if="nameType=='zfb'||nameType==''"></el-checkbox>
                    <el-checkbox label="支付宝公众号" v-if="nameType=='yl'||nameType=='wx'" disabled></el-checkbox>
                    <el-checkbox label="支付宝扫码" v-if="nameType=='zfb'||nameType==''"></el-checkbox>
                    <el-checkbox label="支付宝扫码" v-if="nameType=='yl'||nameType=='wx'" disabled></el-checkbox>
                    <el-checkbox label="无卡快捷" v-if="nameType=='yl'||nameType==''"></el-checkbox>
                    <el-checkbox label="无卡快捷" v-if="nameType=='wx'||nameType=='zfb'" disabled></el-checkbox>
                  </el-checkbox-group>
                </div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">鉴权要素:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio-group v-model="query.checkType">
                    <el-radio :label="4">四要素：银行卡号，姓名，身份证号，手机号</el-radio>
                    <el-radio :label="5" style="margin-left: 0">五要素：银行卡号，姓名，身份证号，手机号，有效期（信用卡）</el-radio>
                    <el-radio :label="6" style="margin-left: 0">六要素：银行卡号，姓名，身份证号，手机号，有效期（信用卡），CVV（信用卡）</el-radio>
                  </el-radio-group>
                </div>
              </el-col>
              <el-col :span="8"></el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">收单机构:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="query.thirdCompany" clearable placeholder="请选择" size="small">
                    <el-option label="微信" value="微信">微信</el-option>
                    <el-option label="支付宝" value="支付宝">支付宝</el-option>
                    <el-option label="京东钱包" value="京东钱包">京东钱包</el-option>
                    <el-option label="百度钱包" value="百度钱包">百度钱包</el-option>
                    <el-option label="银行" value="银行">银行</el-option>
                  </el-select>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">例如：支付宝、微信、京东钱包、百度钱包</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">渠道来源:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.channelSource" placeholder="数字或字母的组合，4-20位"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">例如：华有，显示给用户</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">支付费率:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" style="position: relative">
                  <el-input size="small" v-model="query.basicTradeRate" placeholder="请输入内容"></el-input>
                  <b>%</b>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">例如：0.3%</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">打款费用:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" style="position: relative">
                  <el-input size="small" v-model="query.basicWithdrawFee" placeholder="请输入内容"></el-input>
                  <b>元/笔</b>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">例如：0.5元/笔</div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">结算时间:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="query.basicBalanceType" clearable placeholder="请选择"
                             size="small">
                    <el-option label="D0" value="D0">D0</el-option>
                    <el-option label="D1" value="D1">D1</el-option>
                    <el-option label="T0" value="T0">T0</el-option>
                    <el-option label="T1" value="T1">T1</el-option>
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
                <div class="alignRight">结算方式:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="query.basicSettleType" clearable placeholder="请选择"
                             size="small">
                    <el-option label="通道自动结算" value="AUTO">通道自动结算</el-option>
                    <el-option label="自主打款结算" value="SELF">自主打款结算</el-option>
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
                <div class="alignRight">预估最低额度:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" style="position: relative">
                  <el-input size="small" v-model="query.limitMinAmount" placeholder="请输入内容"></el-input>
                  <b>元/笔</b>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">预估最大额度:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" style="position: relative">
                  <el-input size="small" v-model="query.limitAmount" placeholder="请输入内容"></el-input>
                  <b>元/笔</b>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">最低手续费:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" style="position: relative">
                  <el-input size="small" v-model="query.lowestFee" placeholder="请输入内容"></el-input>
                  <b>元/笔</b>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">通道状态:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio-group v-model="query.isUse">
                    <el-radio :label="1">可用</el-radio>
                    <el-radio :label="0">维护</el-radio>
                  </el-radio-group>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">一户一报:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio-group v-model="query.isNeed">
                    <el-radio :label="1">支持</el-radio>
                    <el-radio :label="2">不支持</el-radio>
                  </el-radio-group>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">备注信息:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" size="small" v-model="query.remarks"
                            placeholder="请输入内容"></el-input>
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
              <div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">
                返回
              </div>
              <div class="btn btn-primary" @click="create" v-if="isShow"
                   style="width: 45%;float: right;margin: 20px 0 100px;">
                添加通道
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
    name: 'passAdd',
    data () {
      return {
        query: {
          channelShortName:'',
          channelName: '',
          channelCode: '',
          supportWay: '',
          thirdCompany: '',
          channelSource: '',
          basicTradeRate: '',
          basicWithdrawFee: '',
          basicBalanceType: '',
          basicSettleType: '',
          limitAmount: '',
          limitMinAmount: '',
          lowestFee:'',
          isUse:'',
          isNeed: '',
          remarks: '',
          id: '',
          status: '',
          accountId: '',
          checkType:''
        },
        id: 0,
        isShow: true,
        name: '',
        payWay: [],
        nameType: ''
      }
    },
    created: function () {
      //若为查看详情
      if (this.$route.query.id != undefined) {
        this.$data.isShow = false;
        this.$http.post('/admin/channel/list')
          .then(function (res) {
            this.query = res.data[this.$route.query.id];
            this.name = res.data[this.$route.query.id].channelName;
            this.query.isNeed = res.data[this.$route.query.id].isNeed;
            this.query.isUse = res.data[this.$route.query.id].isUse;
            if (/微信/.test(this.name)) {
              this.nameType = 'wx';
              if (this.query.supportWay == 3) {
                this.payWay = ['微信公众号', '微信扫码']
              } else if (this.query.supportWay == 2) {
                this.payWay = ['微信公众号']
              } else if (this.query.supportWay == 1) {
                this.payWay = ['微信扫码']
              }
            } else if (/支付宝/.test(this.name)) {
              this.nameType = 'zfb';
              if (this.query.supportWay == 3) {
                this.payWay = ['支付宝公众号','支付宝扫码']
              } else if (this.query.supportWay == 2) {
                this.payWay = ['支付宝公众号']
              } else if (this.query.supportWay == 1) {
                this.payWay = ['支付宝扫码']
              }
            }else {
              this.payWay = ['无卡快捷']
            }
            this.query.id = res.data[this.$route.query.id].id;
            this.query.status = res.data[this.$route.query.id].status;
            this.query.accountId = res.data[this.$route.query.id].accountId;
            this.query.checkType = res.data[this.$route.query.id].checkType;
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
        if (this.payWay.length == 2) {
          this.query.supportWay = 3;
        } else if (this.payWay.length == 1) {
          if (/公众号/.test(this.payWay[0])) {
            this.query.supportWay = 2;
          } else if (/扫码/.test(this.payWay[0])) {
            this.query.supportWay = 1;
          }else if (/快捷/.test(this.payWay[0])){
            this.query.supportWay = 4;
          }
        }
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
    },
    watch: {
      name: function (val) {
        if(val.length!=0){
          if (/微信/.test(val)) {
            this.nameType = 'wx'
          } else if (/支付宝/.test(val)) {
            this.nameType = 'zfb'
          }else {
            this.nameType = 'yl'
          }
        } else {
          this.nameType = ''
        }
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
