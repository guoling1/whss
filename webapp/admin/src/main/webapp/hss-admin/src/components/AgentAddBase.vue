<template lang="html">
  <div id="agentAddBase">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title" v-if="isShow">新增代理商：基础注册信息</h3>
          <h3 class="box-title" v-if="!isShow">代理商详情</h3>
        </div>
        <div class="">
          <div class="box-header">
            <h3 class="box-title title2">基本信息：</h3>
          </div>
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">代理商手机号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.mobile" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">代理名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.name" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center" v-if="!isShow">
              <el-col :span="4">
                <div class="alignRight">代理编号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.markCode" placeholder="请输入内容" :disabled="true"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">所在地:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <div style="width: 39%;display: inline-block;margin-right: 5px">
                    <el-select v-model="province" placeholder="请选择" size="small">
                      <el-option
                        v-for="province in provinces"
                        :label="province.aname"
                        :value="province.aname">
                      </el-option>
                    </el-select>
                  </div>
                  省
                  <div style="width: 39%;display: inline-block;margin-left: 10px;margin-right: 5px;">
                    <el-select v-model="city" placeholder="请选择" size="small">
                      <el-option
                        v-for="city in citys"
                        :label="city.aname"
                        :value="city.aname">
                      </el-option>
                    </el-select>
                  </div>
                  市
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">详细地址:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.belongArea" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center" v-if="!isShow">
              <el-col :span="4">
                <div class="alignRight">上级代理名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.firstDealerName" placeholder="请输入内容" :disabled="true"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center" v-if="!isShow">
              <el-col :span="4">
                <div class="alignRight">上级代理编号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.firstMarkCode" placeholder="请输入内容" :disabled="true"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
          </div>
        </div>
        <div>
          <div class="box-header">
            <h3 class="box-title title2">结算信息设置：</h3>
          </div>
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">结算卡:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.bankCard" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">开户名称:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.bankAccountName" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">身份证号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.idCard" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">开户手机号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.bankReserveMobile" placeholder="请输入内容"></el-input>
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
              <div class="btn btn-default" @click="create" v-if="isShow" style="margin: 20px 0 100px;width: 100%">
                创建代理商
              </div>
              <div class="btn btn-default" @click="goBack" v-if="!isShow" style="width: 45%;margin: 20px 0 100px;">
                返回
              </div>
              <div class="btn btn-default" @click="change(query.id)" v-if="!isShow&&level==1"
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
  var msg = require('../../city.json')
  export default {
    name: 'agentAddBase',
    data () {
      return {
        provinces: '',
        citys: '',
        province: '',
        city: '',
        level: '',
        query: {
          mobile: '',
          name: '',
          belongProvinceCode:'',
          belongProvinceName:'',
          belongCityCode: '',
          belongCityName: '',
          belongArea: '',
          bankCard: '',
          bankAccountName: '',
          bankReserveMobile: '',
          idCard: '',
        },
        id: 0,
        isShow: true,
        productId: ''
      }
    },
    created: function () {
      //获取所有省
      this.$http.post('/admin/district/findAllDistrict')
        .then(function (res) {
          this.$data.provinces = res.data;
        })
        .catch(function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
      //若为查看详情
      if (this.$route.query.id != undefined) {
        this.$data.isShow = false;
        this.$http.get('/admin/dealer/findBydealerId/' + this.$route.query.id)
          .then(function (res) {
            this.$data.query = res.data
          })
      }
      this.$data.level = this.$route.query.level;
    },
    watch: {
      province: function (val, oldval) {
        if (val != oldval) {
          this.$data.city = '';
          this.$data.query.province = this.$data.province;
        }
        if (this.$data.province != '') {
          for (var i = 0; i < this.$data.provinces.length; i++) {
            if (this.$data.provinces[i].aname == this.$data.province) {
              this.$data.query.belongProvinceCode= this.$data.provinces[i].code
              this.$data.query.belongProvinceName= this.$data.province
              this.$data.citys = this.$data.provinces[i].list
            }
          }
        }
      },
      city: function (val, oldval) {
        if (val != oldval) {
          this.$data.query.belongCityName = this.$data.city;
          for(var i=0;i<this.$data.citys.length;i++){
            if(this.$data.citys[i].aname == this.$data.city){
              this.$data.query.belongCityCode = this.$data.citys[i].code;
            }
          }
        }
      }
    },
    methods: {
      //创建一级代理
      create: function () {
        this.$http.post('/admin/user/addFirstDealer2', this.$data.query)
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '创建成功',
              type: 'success'
            });
            this.$router.push('/admin/record/agentListFir')
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      goBack: function () {
        this.$router.push('/admin/record/agentList')
      },
      //修改
      change: function () {
        this.$http.post('/admin/user/updateDealer', this.$data.query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: "修改成功"
            })
            this.$router.push('/admin/record/agentList')
          }, function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      }
    },
    filters: {
      changeName: function (val) {
        if (val == 101) {
          val = '阳光微信扫码'
        } else if (val == 102) {
          val = '阳光支付宝扫码'
        } else if (val == 103) {
          val = '阳光银联支付'
        }
        return val;
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

  .title2 {
    margin-left: 10%;
  }
</style>
