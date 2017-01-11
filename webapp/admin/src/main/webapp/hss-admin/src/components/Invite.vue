<template lang="html">
  <div id="invite">
    <div style="margin: 15px">
      <div class="box ">
        <form class="form-horizontal">
          <div class="box-header">
            <h3 class="box-title">邀请规则设置</h3>
          </div>
          <div class="box-body">
            <div class="form-group">
              <div class="col-xs-12">
                产品选择：
                <select class="form-control select2 select2-hidden-accessible" style="width: 25%;display: inline-block" tabindex="-1" aria-hidden="true" v-model="productId">
                  <option value="">请选择产品</option>
                  <option :value="list.productId" v-for="list in lists">{{list.productName}}</option>
                </select>
                <div @click="search" class="btn btn-primary">确定</div>
              </div>
            </div>
          </div>
        </form>
      </div>
      <div class="box box-info" v-if="result.length!=0">
        <div class="box-header with-border">
          <h3 class="box-title">商户升级规则设置</h3>
        </div>
        <form class="form-horizontal">
          <div class="box-body">
            <div class="form-group">
              <div class="col-xs-12">
                <div class="box box1">
                  <div class="box-body table-responsive no-padding">
                    <table class="table table-hover">
                      <tbody>
                      <tr>
                        <th>合伙人等级</th>
                        <th>微信费率</th>
                        <th>支付宝费率</th>
                        <th>无卡费率</th>
                        <th>升级费</th>
                        <th>邀请人数</th>
                        <th>直接升级费奖励</th>
                        <th>间推升级费奖励</th>
                      </tr>
                      <tr>
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
                      </tr>
                      <tr >
                        <td colspan="6">有效激活标准：收款满<input type="text" name="name" style="width: 100px;" v-model="result.standard">元
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
      <div class="box box-info" v-if="result.length!=0">
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
                      <tr >
                        <td colspan="2">收单奖励分润池：<input type="text" name="name" style="width: 100px;" v-model="result.rewardRate">%
                        </td>
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
      <div class="btn btn-primary" @click="save" v-if="result.length!=0">保 存</div>
      <span v-if="result.length!=0">(对新审核通过的有效)</span>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name:"invite",
    data(){
      return{
        result: '',
        lists:[], //产品列表
        productId:3,
        query:''
      }
    },
    created: function () {
      //产品列表
      this.$http.post("/admin/product/list")
        .then(function (res) {
          this.$data.lists = res.data
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      //内容
      this.$http.post('/admin/upgrade/init', {productId: this.$data.productId})
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
        })
    },
    methods: {
      search: function () {
        this.$http.post('/admin/upgrade/init', {productId: this.$data.productId})
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
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: "操作成功"
            })
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
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

  li {
    display: inline-block;
    margin: 0 10px;
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
