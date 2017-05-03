<template lang="html">
  <div id="agentAddBase">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title" v-if="isShow">开通分公司</h3>
          <h3 class="box-title" v-if="!isShow">代理商详情</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>
        </div>
        <div class="">
          <div class="box-header">
            <h3 class="box-title title2">基本信息：</h3>
          </div>
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">联系人手机号:</div>
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
                <div class="alignRight">分公司名称:</div>
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
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">登录名:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.loginName" placeholder="数字或字母的组合，4-20位"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">登录密码:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input type="password" size="small" v-model="query.loginPwd" placeholder="最低6位"  v-if="isShow"></el-input>
                  <el-input type="password" size="small" value="******" placeholder="最低6位"  v-if="!isShow" :disabled="true"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light" style="margin: 0 15px;">
                  <el-button type="text" @click="_$power(function(){dialogFormVisible = true},'boss_first_update_pwd')" v-if="!isShow&&level==1">修改密码</el-button>
                  <el-button type="text" @click="_$power(function(){dialogFormVisible = true},'boss_second_update_pwd')" v-if="!isShow&&level==2">修改密码</el-button>
                </div>
              </el-col>
            </el-row>
            <!--修改密码-->
            <el-dialog title="修改密码" v-model="dialogFormVisible">
              <el-form>
                <el-form-item label="新密码" width="120">
                  <el-input type="password" v-model="password" auto-complete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="resetPw">确 定</el-button>
              </div>
            </el-dialog>

            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">联系邮箱:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.email" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">省市区域:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <div style="width: 39%;display: inline-block;margin-right: 5px">
                    <el-select v-model="province" placeholder="请选择" size="small">
                      <el-option
                        v-for="province in provinces"
                        :label="province.name"
                        :value="province.name">
                      </el-option>
                    </el-select>
                  </div>
                  省
                  <div style="width: 39%;display: inline-block;margin-left: 10px;margin-right: 5px;">
                    <el-select v-model="city" placeholder="请选择" size="small">
                      <el-option
                        v-for="city in citys"
                        :label="city.name"
                        :value="city.name">
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
                <div class="alignRight">持卡人姓名:</div>
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
              <!--<div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">-->
                <!--返回-->
              <!--</div>-->
              <div class="btn btn-primary" @click="create" v-if="isShow" style="width: 45%;margin: 20px 0 100px;">
                创建分公司
              </div>
              <div class="btn btn-primary" @click="_$power(change,'boss_first_update')" v-if="!isShow&&level==1" style="width: 45%;margin: 20px 0 100px;">修改</div>
              <div class="btn btn-primary" @click="_$power(change,'boss_second_update')" v-if="!isShow&&level==2" style="width: 45%;margin: 20px 0 100px;">修改</div>
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
  import Message from '../Message.vue'
  export default {
    name: 'agentAddBase',
    data () {
      return {
        dialogFormVisible: false,
        password:'',
        provinces: '',
        citys: '',
        province: '',
        city: '',
        level: '',
        query: {
          mobile: '',
          name: '',
          loginName:'',
          loginPwd:'',
          email:'',
          belongProvinceCode:'',
          belongProvinceName:'',
          belongCityCode: '',
          belongCityName: '',
          belongArea: '',
          bankCard: '',
          bankAccountName: '',
          bankReserveMobile: '',
          idCard: '',
          oemType:1
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
            this.$data.query = res.data;
            this.$data.province = res.data.belongProvinceName;
            this.$data.city = res.data.belongCityName;
          })
      }
      this.$data.level = this.$route.query.level;
    },
    watch: {
      province: function (val, oldval) {
        if (val != oldval&&oldval!="") {
          this.$data.city = '';
          this.$data.query.province = this.$data.province;
        }
        if (this.$data.province != '') {
          for (var i = 0; i < this.$data.provinces.length; i++) {
            if (this.$data.provinces[i].name == this.$data.province) {
              this.$data.query.belongProvinceCode= this.$data.provinces[i].code
              this.$data.query.belongProvinceName= this.$data.province
              this.$data.citys = this.$data.provinces[i].cityList
            }
          }
        }
      },
      city: function (val, oldval) {
        if (val != oldval) {
          this.$data.query.belongCityName = this.$data.city;
          for(var i=0;i<this.$data.citys.length;i++){
            if(this.$data.citys[i].name == this.$data.city){
              this.$data.query.belongCityCode = this.$data.citys[i].code;
            }
          }
        }
      }
    },
    methods: {
      //修改密码
      resetPw:function() {
        this.$http.post('/admin/dealer/updatePwd',{dealerId:this.$route.query.id,loginPwd:this.$data.password})
          .then(function (res) {
            this.$data.dialogFormVisible = false;
            this.$data.password = '';
            this.$message({
              showClose: true,
              type: 'success',
              message: '修改成功'
            });
          })
          .catch(function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      //创建一级代理
      create: function () {
        this.$http.post('/admin/user/addFirstDealer2', this.query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '创建成功'
            })
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      //修改
      change: function () {
        this.$data.query.dealerId = this.$data.query.id;
        this.$http.post('/admin/user/updateDealer2', this.query)
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: '修改成功'
            })
//            }
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

  .title2 {
    margin-left: 10%;
  }
</style>
