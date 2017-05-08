<template lang="html">
  <div id="invite">
    <div style="margin: 15px;">
      <div class="box ">
        <form class="form-horizontal">
          <div class="box-header">
            <h3 class="box-title">邀请规则设置</h3>
          </div>
          <div class="box-body">
            <div class="form-group">
              <div class="col-xs-12">
                产品选择：
                <el-select v-model="productId" placeholder="请选择" size="small">
                  <el-option v-for="product in productList" :key="product.productId"
                    :label="product.productName"
                    :value="product.productId">
                  </el-option>
                </el-select>
                <div @click="search" class="btn btn-primary">确定</div>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="box box-info">
        <div class="box-header with-border">
          <h3 class="box-title">商户升级规则设置</h3>
        </div>
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <div class="col-xs-10">
                <div class="box box1">
                  <div class="box-body table-responsive no-padding">
                    <table class="table table-hover">
                      <tbody>
                      <tr>
                        <th>合伙人等级<br/>参与的通道</th>
                        <th style="line-height: 250%">默认分润空间</th>
                        <th style="line-height: 250%">普通（基准费率）</th>
                        <th style="line-height: 250%">店员</th>
                        <th style="line-height: 250%">店长</th>
                        <th style="line-height: 250%">老板</th>
                      </tr>
                      <!--<tr>
                        <td>普通</td>
                        <td>{{result.upgradeRulesList[0].weixinRate}}%</td>
                        <td>{{result.upgradeRulesList[0].alipayRate}}%</td>
                        <td>{{result.upgradeRulesList[0].fastRate}}%</td>
                        <td>无</td>
                        <td>无</td>
                        <td>无</td>
                        <td>无</td>
                      </tr>-->
                      <tr v-for="(upgrade,index) in $$upgradeRulesList" v-if="index!=0">
                        <td>{{upgrade.channelShortName}}</td>
                        <td><input type="text" name="name" v-model="upgrade.defaultProfitSpace">%</td>
                        <td><input type="text" name="name" v-model="upgrade.commonRate">%</td>
                        <td><input type="text" name="name" v-model="upgrade.clerkRate">%</td>
                        <td><input type="text" name="name" v-model="upgrade.shopownerRate">元</td>
                        <td><input type="text" name="name" v-model="upgrade.bossRate">人</td>
                      </tr>
                      <tr >
                        <td><el-button type="text" size="small" @click="isAdd = true">点击添加通道</el-button></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                      </tr>
                      </tbody></table>
                  </div>
                  <el-dialog title="添加通道" v-model="isAdd">
                    <el-form :label-position="right" label-width="150px">
                      <el-form-item label="选择通道：" width="120" style="margin-bottom: 0">
                        <el-select size="small" placeholder="请选择" v-model="channel">
                          <el-option v-for="channel in channelList"
                                     :label="channel.channelShortName"
                                     :value="channel">
                          </el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item label="默认分润空间：" width="120" style="margin-bottom: 0">
                        <el-input style="width: 70%" size="small" v-model="defaultProfitSpace"></el-input>
                      </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer" style="text-align: center">
                      <el-button @click="isAdd = false" style="position: relative;top: -20px;">取 消</el-button>
                      <el-button @click="add" type="primary" style="position: relative;top: -20px;">确 定</el-button>
                    </div>
                  </el-dialog>
                  <el-dialog title="确认添加" v-model="isSub">
                    <el-form :label-position="right" label-width="150px">
                      <el-form-item label="通道名称：" width="120" style="margin-bottom: 0">
                        <span>{{channelShortName}}</span>
                      </el-form-item>
                      <el-form-item label="支付方式：" width="120" style="margin-bottom: 0">
                        <span>{{supportWay}}</span>
                      </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer" style="text-align: center">
                      <el-button @click="isSub = false" style="position: relative;top: -20px;">取 消</el-button>
                      <el-button @click="channelAdd" type="primary" style="position: relative;top: -20px;">确 定</el-button>
                    </div>
                  </el-dialog>
                </div>
              </div>
              <div class="col-xs-8">
                <div class="box box1">
                  <div class="box-body table-responsive no-padding">
                    <table class="table table-hover">
                      <tbody>
                      <tr>
                        <th>合伙人等级<br/>通道名称</th>
                        <th style="line-height: 250%">普通</th>
                        <th style="line-height: 250%">店员</th>
                        <th style="line-height: 250%">店长</th>
                        <th style="line-height: 250%">老板</th>
                      </tr>
                      <tr>
                        <td>升级费</td>
                        <td>无</td>
                        <td></td>
                        <td></td>
                        <td></td>
                      </tr>
                      <tr>
                        <td>直推升级费奖励</td>
                        <td>无</td>
                        <td></td>
                        <td></td>
                        <td></td>
                      </tr>
                      <tr>
                        <td>间推升级费奖励</td>
                        <td>无</td>
                        <td></td>
                        <td></td>
                        <td></td>
                      </tr>
                      <tr>
                        <td>邀请升级达标人数</td>
                        <td>无</td>
                        <td></td>
                        <td></td>
                        <td></td>
                      </tr>
                      <tr >
                        <td>邀请用户有效标准</td>
                        <td colspan="4">有效激活标准：收款满<input type="text" name="name" style="width: 100px;" v-model="result.standard">元
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
        </form>
      </div>
      <div class="box box-info">
        <div class="box-header with-border">
          <h3 class="box-title">升级推荐分润设置</h3>
        </div>
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <div class="col-xs-8">
                <div class="box box1">
                  <div class="box-body table-responsive no-padding">
                    <table class="table table-hover">
                      <tbody>
                      <tr>
                        <th>分润类型</th>
                        <th>金开门分润比例</th>
                      </tr>
                      <tr>
                        <td>升级费分润</td>
                        <td><input type="text" name="name" v-model="result.upgradeRate">%</td>
                      </tr>
                      <tr >
                        <td>收单分润</td>
                        <td><input type="text" name="name" v-model="result.tradeRate">%</td>
                      </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="btn btn-primary" @click="save">保 存</div>
      <span v-if="result.length!=0">(对新审核通过的有效)</span>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name:"invite",
    data(){
      return{
        productList:[],
        channelList:[],
        channel:{},//要添加的通道
        defaultProfitSpace:'',//默认分润空间
        channelShortName:"",
        supportWay:'',
        isAdd:false,
        isSub:false,
        tableData1:[{
          name:'微信',
          data1:'',
          data2:'',
          data3:'',
          data4:'',
          data5:'',
        }],
        result: '',
        lists:[], //产品列表
        productId:'',
        query:''
      }
    },
    created: function () {
      //产品列表
      this.$http.post("/admin/product/list")
        .then(function (res) {
          this.productList = res.data
        },function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        });
      //内容
      this.$http.post('/admin/upgrade/init', {productId: this.productId})
        .then(function (res) {
          this.result = res.data;

          /*this.$data.result.upgradeRulesList.sort(function (a, b) {
            return a.type-b.type
          })
          this.$data.result.upgradeRulesList[0].name = '普通'
          this.$data.result.upgradeRulesList[1].name = '店员'
          this.$data.result.upgradeRulesList[2].name = '店长'
          this.$data.result.upgradeRulesList[3].name = '老板'*/
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
    },
    methods: {
      add:function () {
        this.isAdd = false;
        this.isSub = true;
        console.log(this.channel);
        this.channel.defaultProfitSpace = this.defaultProfitSpace;
        this.channelShortName = this.channel.channelShortName;
        this.supportWay = this.channel.supportWay;
      },
      channelAdd: function () {
        this.isSub = false;
        let partnerRuleSetting = {};
        partnerRuleSetting.id = this.channel.id;
        partnerRuleSetting.channelShortName = this.channel.channelShortName;
        partnerRuleSetting.productId = this.productId;
        partnerRuleSetting.channelTypeSign = this.channel.channelTypeSign;
        partnerRuleSetting.defaultProfitSpace = this.defaultProfitSpace;
        partnerRuleSetting.commonRate = '';
        partnerRuleSetting.clerkRate = '';
        partnerRuleSetting.shopownerRate = '';
        partnerRuleSetting.bossRate = '';
        this.partnerRuleSettingList.push(this.partnerRuleSetting)
      },
      search: function () {
        this.result = '';
        //通道列表
        this.$http.post("/admin/upgrade/getProductChannelList",{productId: this.productId})
          .then(function (res) {
            this.channelList = res.data;
          },function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
        this.$http.post('/admin/upgrade/init', {productId: this.productId})
          .then(function (res) {
            this.result = res.data;
            this.partnerRuleSettingList = res.data.partnerRuleSettingList;
            /*this.$data.result.upgradeRulesList.sort(function (a, b) {
              return a.type-b.type
            })
            console.log(this.$data.result.upgradeRulesList)
            this.$data.result.upgradeRulesList[0].name = '普通'
            this.$data.result.upgradeRulesList[1].name = '店员'
            this.$data.result.upgradeRulesList[2].name = '店长'
            this.$data.result.upgradeRulesList[3].name = '老板'*/
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      save: function () {
        var upgradeRulesList= this.$data.result.upgradeRulesList.concat()
        upgradeRulesList.shift()
        var query = {
          productId:this.$data.productId,
          standard:this.$data.result.standard,
          upgradeRate:this.$data.result.upgradeRate,
          tradeRate:this.$data.result.tradeRate,
          rewardRate:this.$data.result.rewardRate,
          upgradeRulesList:upgradeRulesList
        }
        this.$http.post('/admin/upgrade/addOrUpdate',query)
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '操作成功',
              type: 'success'
            });
          },function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      }
    },
    computed:{
      $$upgradeRulesList: function () {
        return this.upgradeRulesList;
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


  input {
    width: 77%;
    border: none;
    border-bottom: 1px solid #d0d0d0;
  }
  .btn,select{
    font-size: 12px;
  }
</style>
