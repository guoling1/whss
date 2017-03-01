<!--<template lang="html">
    <div id="passAdd">
      <div style="margin: 15px;width: inherit" class="box">
        <form class="form-horizontal">
          <div class="box-header">
            <h3 v-if="isShow" class="box-title">增加通道</h3>
            <h3 v-else="isShow" class="box-title">修改通道</h3>
          </div>
          <div class="box-body">
            <div class="form-group">
              <label for="channelName" class="col-sm-2 control-label">通道名称</label>
              <div class="col-sm-4">
                <input type="text" class="form-control" id="channelName" v-model="channelName">
              </div>
              <div class="col-sm-6 right">例如：华有支付宝</div>
            </div>
            <div class="form-group">
              <label for="thirdCompany" class="col-sm-2 control-label">收单机构</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="thirdCompany" v-model="thirdCompany">
              </div>
              <div class="col-sm-6 right">例如：支付宝、微信、京东钱包、百度钱包</div>
            </div>
            <div class="form-group">
              <label for="channelSource" class="col-sm-2 control-label">渠道来源</label>

              <div class="col-sm-4">
                <input type="text" class="form-control" id="channelSource" v-model="channelSource">
              </div>
              <div class="col-sm-6 right">例如：华有</div>
            </div>
            <div class="form-group">
              <label for="basicTradeRate" class="col-sm-2 control-label">支付费率</label>

              <div class="col-sm-4 middle">
                <input type="number" class="form-control" id="basicTradeRate" v-model="basicTradeRate">
                <i>%</i>
              </div>
              <div class="col-sm-6 right">例如：0.3%</div>
            </div>
            <div class="form-group">
              <label for="basicWithdrawFee" class="col-sm-2 control-label">提现费用</label>

              <div class="col-sm-4 middle">
                <input type="number" class="form-control" id="basicWithdrawFee" v-model="basicWithdrawFee">
                <i>元/笔</i>
              </div>
              <div class="col-sm-6 right">例如：0.5元/笔</div>
            </div>
            <div class="form-group">
              <label for="basicWithdrawFee" class="col-sm-2 control-label">结算时间</label>
              <div class="col-sm-4">
                <select class="form-control" v-model="basicBalanceType">
                  <option value="">请选择</option>
                  <option value="D0">D0</option>
                  <option value="D1">D1</option>
                  <option value="T0">T0</option>
                  <option value="T1">T1</option>
                </select>
              </div>
              <div class="col-sm-6 right"></div>
            </div>
          </div>
          <div class="box-footer">
            <div type="submit" class="btn btn-default" @click="passAdd" v-if="isShow">添 加</div>
            <div type="submit" class="btn btn-default" @click="passAdd" v-if="!isShow">修 改</div>
            <div type="submit" class="btn btn-info pull-right" v-if="!isShow" @click="goBack">返 回</div>
          </div>
        </form>
      </div>
      &lt;!&ndash;<div class="btn btn-success" @click="passAdd" v-if="isShow">添 加</div>&ndash;&gt;
      &lt;!&ndash;<div class="btn btn-danger product" @click="passAdd" v-if="!isShow">修改</div>&ndash;&gt;
      &lt;!&ndash;<div class="btn btn-success product" v-if="!isShow" @click="goBack">返回</div>&ndash;&gt;
    </div>
</template>

<script lang="babel">
    export default {
      name:"passAdd",
      data(){
        return{
          channelName:"",
          thirdCompany:"",
          channelSource:"",
          basicTradeRate:"",
          basicWithdrawFee:"",
          basicBalanceType:"",
          isShow:true,//true新增
        }
      },
      created: function () {
        if(this.$route.query.id!=undefined){
          this.$data.isShow=false
          this.$http.post('/admin/channel/list')
            .then(function (res) {
              var data= res.data[this.$route.query.id];
              this.$data.channelName=data.channelName;
              this.$data.thirdCompany=data.thirdCompany;
              this.$data.channelSource=data.channelSource;
              this.$data.basicTradeRate=data.basicTradeRate;
              this.$data.basicWithdrawFee=data.basicWithdrawFee;
              this.$data.basicBalanceType=data.basicBalanceType;
              this.$data.id=data.id;
              this.$data.status=data.status;
              this.$data.accountId=data.accountId;
            }, function (err) {
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        }
      },
      methods: {
        passAdd: function () {
          //新增
          if(this.$route.query.id==undefined){
            this.$http.post('/admin/channel/add',this.$data)
              .then(function(res){
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: "添加成功"
                })
              },function (err) {
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: "添加失败"
                })
              })
          }else {
            this.$http.post('/admin/channel/update',this.$data)
              .then(function(res){
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: "修改成功"
                })
                this.$router.push('/admin/record/passList')
              },function (err) {
                this.$store.commit('MESSAGE_ACCORD_SHOW', {
                  text: "修改失败"
                })
              })
          }
        },
        goBack:function () {
          this.$router.push('/admin/record/passList')
        }
      },
      computed: {
        $$data: function () {
          return this.$data;
        }
      }
    }
</script>
<style scoped lang="less">
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
  .passAdd{
    float: right;
    width: 80%;
  }
  .middle{
    position: relative;
  i{
    font-style: normal;
    position: absolute;
    top: 8px;
    right: 22px;
    width: 33px;
    text-align: right;
    display: inline-block;
  }
  }
  .right{
    padding-top: 7px;
    color: #b0b0b0;
  }
  /*form{
    label{
      display: block;
      input{
        padding-left: 10px;
        display: inline-block;
        height: 40px;
        margin-right: 10px;
      }

      span{
        color: #ccc;
      }
    }
  }
  .left{
    position: relative;
    left: -37px;
  }
  .btn{
    margin:40px 0 0 70px;
    width: 200px;
  }*/
</style>-->
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
            <!--<el-form :model="query" ref="query" label-width="200px" class="demo-dynamic">
              <el-form-item prop="name" label="通道名称:"
                            :rules="{required: true,  message: '请输入通道名称', trigger: 'blur' }">
                <div style="width: 400px;display: inline-block">
                  <el-input size="small" v-model="name" placeholder="请输入内容"></el-input>
                </div>
                <span class="right">例如：华有支付宝</span>
              </el-form-item>
              <el-form-item label="通道编码" prop="channelCode"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block">
                  <el-input size="small" v-model="query.channelCode" placeholder="请输入内容"></el-input>
                </div>
                <span class="right">例如：SM_Alipay</span>
              </el-form-item>
              <el-form-item label="支付方式" prop="payWay"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block">
                  <el-checkbox-group v-model="payWay">
                    <el-checkbox label="微信公众号" v-if="nameType=='wx'||nameType==''"></el-checkbox>
                    <el-checkbox label="微信公众号" v-if="nameType=='zfb'" disabled></el-checkbox>
                    <el-checkbox label="微信扫码" v-if="nameType=='wx'||nameType==''"></el-checkbox>
                    <el-checkbox label="微信扫码" v-if="nameType=='zfb'" disabled></el-checkbox>
                    <el-checkbox label="支付宝公众号" v-if="nameType=='zfb'||nameType==''"></el-checkbox>
                    <el-checkbox label="支付宝公众号" v-if="nameType=='wx'" disabled></el-checkbox>
                    <el-checkbox label="支付宝扫码" v-if="nameType=='zfb'||nameType==''"></el-checkbox>
                    <el-checkbox label="支付宝扫码" v-if="nameType=='wx'" disabled></el-checkbox>
                  </el-checkbox-group>
                </div>
                <span class="right"></span>
              </el-form-item>
              <el-form-item label="收单机构" prop="thirdCompany"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block">
                  <el-select style="width: 100%" v-model="query.thirdCompany" clearable placeholder="请选择" size="small">
                    <el-option label="微信" value="微信">微信</el-option>
                    <el-option label="支付宝" value="支付宝">支付宝</el-option>
                    <el-option label="京东钱包" value="京东钱包">京东钱包</el-option>
                    <el-option label="百度钱包" value="百度钱包">百度钱包</el-option>
                  </el-select>
                </div>
                <span class="right"></span>
              </el-form-item>
              <el-form-item label="渠道来源" prop="thirdCompany"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block">
                  <el-input size="small" v-model="query.channelSource" placeholder="数字或字母的组合，4-20位"></el-input>
                </div>
                <span class="right">例如：华有，显示给用户</span>
              </el-form-item>
              <el-form-item label="支付费率" prop="basicTradeRate"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block;position: relative">
                  <el-input size="small" v-model="query.basicTradeRate" placeholder="请输入内容"></el-input>
                  <b>%</b>
                </div>
                <span class="right">>例如：0.3%</span>
              </el-form-item>
              <el-form-item label="打款费用" prop="basicWithdrawFee"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block;position: relative">
                  <el-input size="small" v-model="query.basicWithdrawFee" placeholder="请输入内容"></el-input>
                  <b>元/笔</b>
                </div>
                <span class="right">例如：0.5元/笔</span>
              </el-form-item>
              <el-form-item label="结算时间" prop="basicBalanceType"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block">
                  <el-select style="width: 100%" v-model="query.basicBalanceType" clearable placeholder="请选择"
                             size="small">
                    <el-option label="D0" value="D0">D0</el-option>
                    <el-option label="D1" value="D1">D1</el-option>
                    <el-option label="T0" value="T0">T0</el-option>
                    <el-option label="T1" value="T1">T1</el-option>
                  </el-select>
                </div>
                <span class="right"></span>
              </el-form-item>
              <el-form-item label="结算方式" prop="basicSettleType"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block">
                  <el-select style="width: 100%" v-model="query.basicSettleType" clearable placeholder="请选择"
                             size="small">
                    <el-option label="通道自动结算" value="AUTO">通道自动结算</el-option>
                    <el-option label="自主打款结算" value="SELF">自主打款结算</el-option>
                  </el-select>
                </div>
                <span class="right"></span>
              </el-form-item>
              <el-form-item label="预估额度" prop="limitAmount"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block;position: relative;">
                  <el-input size="small" v-model="query.limitAmount" placeholder="请输入内容"></el-input>
                  <b>元/笔</b>
                </div>
                <span class="right"></span>
              </el-form-item>
              <el-form-item label="一户一报" prop="isNeed"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block;position: relative;">
                  <el-radio class="radio" v-model="query.isNeed" label="1">支持</el-radio>
                  <el-radio class="radio" v-model="query.isNeed" label="2">不支持</el-radio>
                </div>
                <span class="right"></span>
              </el-form-item>
              <el-form-item label="备注信息" prop="remarks"
                :rules="{required: true, message: '请输入通道编码', trigger: 'blur'}">
                <div style="width: 400px;display: inline-block;position: relative;">
                  <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" size="small" v-model="query.remarks"
                            placeholder="请输入内容"></el-input>
                </div>
                <span class="right"></span>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm('query')">提交</el-button>
                &lt;!&ndash;<el-button @click="addDomain">新增域名</el-button>&ndash;&gt;
                &lt;!&ndash;<el-button @click="resetForm('dynamicValidateForm')">重置</el-button>&ndash;&gt;
              </el-form-item>
            </el-form>-->
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">通道名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="name" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light right">例如：华有支付宝</div>
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
                    <el-checkbox label="微信公众号" v-if="nameType=='zfb'" disabled></el-checkbox>
                    <el-checkbox label="微信扫码" v-if="nameType=='wx'||nameType==''"></el-checkbox>
                    <el-checkbox label="微信扫码" v-if="nameType=='zfb'" disabled></el-checkbox>
                    <el-checkbox label="支付宝公众号" v-if="nameType=='zfb'||nameType==''"></el-checkbox>
                    <el-checkbox label="支付宝公众号" v-if="nameType=='wx'" disabled></el-checkbox>
                    <el-checkbox label="支付宝扫码" v-if="nameType=='zfb'||nameType==''"></el-checkbox>
                    <el-checkbox label="支付宝扫码" v-if="nameType=='wx'" disabled></el-checkbox>
                  </el-checkbox-group>
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
                <div class="alignRight">预估额度:</div>
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
                <div class="alignRight">一户一报:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-radio class="radio" v-model="query.isNeed" label="1">支持</el-radio>
                  <el-radio class="radio" v-model="query.isNeed" label="2">不支持</el-radio>
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
          isNeed: '',
          remarks: ''
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
        this.$http.get('/admin/dealer/findBydealerId/' + this.$route.query.id)
          .then(function (res) {
            this.$data.query = res.data;
            this.$data.province = res.data.belongProvinceName;
            this.$data.city = res.data.belongCityName;
          })
      }
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
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
        if(isFalse === false){
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
        }else {
          this.$message({
            showClose: true,
            message: '请补全所有信息',
            type: 'error'
          });
        }

      },
      goBack: function () {
        this.$router.push('/admin/record/passList')
      },
      //修改
      change: function () {
        this.$data.query.dealerId = this.$data.query.id;
        this.$http.post('/admin/user/updateDealer2', this.$data.query)
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '修改成功',
              type: 'success'
            });
            if (this.$route.query.level == 2) {
              this.$router.push('/admin/record/agentListSec')
            } else {
              this.$router.push('/admin/record/agentListFir')
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
    watch: {
      name: function (val) {
        if (/微信/.test(val)) {
          this.payWay = []
          this.nameType = 'wx'
        } else if (/支付宝/.test(val)) {
          this.payWay = []
          this.nameType = 'zfb'
        } else {
          this.payWay = []
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
