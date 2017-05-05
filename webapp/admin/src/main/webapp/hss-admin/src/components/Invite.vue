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
                  <el-option v-for="list in lists" :key="list.productId"
                    :label="list.productName"
                    :value="list.productId">
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
                      </tr>
                      <tr v-for="(upgrade,index) in result.upgradeRulesList" v-if="index!=0">
                        <td>{{upgrade.name}}</td>
                        <td><input type="text" name="name" v-model="upgrade.weixinRate">%</td>
                        <td><input type="text" name="name" v-model="upgrade.alipayRate">%</td>
                        <td><input type="text" name="name" v-model="upgrade.fastRate">%</td>
                        <td><input type="text" name="name" v-model="upgrade.upgradeCost">元</td>
                        <td><input type="text" name="name" v-model="upgrade.promotionNum">人</td>
                        <td><input type="text" name="name" v-model="upgrade.directPromoteShall">元</td>
                        <td><input type="text" name="name" v-model="upgrade.inDirectPromoteShall">元</td>
                      </tr>-->
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
                        <el-select size="small" placeholder="请选择" v-model="addQuery.a">
                          <el-option label="切换金开门直属" value="1"></el-option>
                          <el-option label="切换为一级直属" value="2"></el-option>
                          <el-option label="切换到二级" value="3"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item label="默认分润空间：" width="120" style="margin-bottom: 0">
                        <el-input style="width: 70%" size="small" v-model="addQuery.b"></el-input>
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
                        <span>xx</span>
                      </el-form-item>
                      <el-form-item label="支付方式：" width="120" style="margin-bottom: 0">
                        <span>xx</span>
                      </el-form-item>
                    </el-form>
                    <div slot="footer" class="dialog-footer" style="text-align: center">
                      <el-button @click="isSub = false" style="position: relative;top: -20px;">取 消</el-button>
                      <el-button @click="isSub = false" type="primary" style="position: relative;top: -20px;">确 定</el-button>
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
        isAdd:false,
        isSub:false,
        addQuery:{
           a:'',
          b:''
        },
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
          this.$data.lists = res.data
        },function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
      //内容
      /*this.$http.post('/admin/upgrade/init', {productId: this.$data.productId})
        .then(function (res) {
          console.log(res)
          this.$data.result = res.data;

          this.$data.result.upgradeRulesList.sort(function (a, b) {
            return a.type-b.type
          })
          console.log(this.$data.result.upgradeRulesList)
          this.$data.result.upgradeRulesList[0].name = '普通'
          this.$data.result.upgradeRulesList[1].name = '店员'
          this.$data.result.upgradeRulesList[2].name = '店长'
          this.$data.result.upgradeRulesList[3].name = '老板'
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })*/
    },
    methods: {
      add:function () {
        this.isAdd = false;
        this.isSub = true;
      },
      search: function () {
        this.$data.result = ''
        this.$http.post('/admin/upgrade/init', {productId: this.$data.productId})
          .then(function (res) {
            this.$data.result = res.data;
            this.$data.result.upgradeRulesList.sort(function (a, b) {
              return a.type-b.type
            })
            console.log(this.$data.result.upgradeRulesList)
            this.$data.result.upgradeRulesList[0].name = '普通'
            this.$data.result.upgradeRulesList[1].name = '店员'
            this.$data.result.upgradeRulesList[2].name = '店长'
            this.$data.result.upgradeRulesList[3].name = '老板'
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
