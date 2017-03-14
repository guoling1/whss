<template lang="html">
  <div id="storeAudit">
    <div class="box-header with-border" style="margin: 0 0 0 3px;">
      <h3 v-if="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户审核</h3>
      <h3 v-else="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户资料</h3>
    </div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead">注册信息</p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">注册手机：<span>{{msg.mobile}}</span>
              <!--<a>修改登录手机号</a>-->
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">注册时间：<span>{{msg.createTime|changeTime}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">注册方式：
              <span v-if="msg.source==0">扫码注册</span>
              <span v-if="msg.source==1">商户推荐注册</span>
              <span v-if="msg.source==2">代理商推荐注册</span>
            </div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">一级代理编号：<span>{{msg.markCode1}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">一级代理名称：<span>{{msg.proxyName}}</span>
              <!--<a>修改代理商归属</a>-->
            </div>
          </el-col>
          <el-col :span="5">
            <div class="label">推荐商户注册手机号：<span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">二级代理编号：<span>{{msg.markCode2}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">二级代理名称：<span>{{msg.proxyName1}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">邀请层级：<span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">推荐商户编号：<span>{{msg.recommenderCode}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">推荐商户名称：<span>{{msg.recommenderName}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">推荐人注册手机号<span>{{msg.recommenderPhone}}</span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">推荐所属一级代理名：<span>{{msg.proxyNameYq}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">推荐所属二级代理名：<span>{{msg.proxyNameYq1}}</span></div>
          </el-col>
          <el-col :span="5"></el-col>
        </el-row>
      </div>
      <div class="box box-primary">
        <p class="lead">认证信息</p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商铺名称（全称）：<span>{{msg.merchantName}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商铺简称：<span>——</span></div>
          </el-col>
          <el-col :span="5">
            <!--<div class="label">商户营业执照号：<span>——</span></div>-->
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">店主（法人）实名：<span>{{msg.name}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">店主身份证号：<span>{{msg.identity}}</span></div>
          </el-col>
          <el-col :span="5"></el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商铺上报名称：<span>{{msg.merchantChangeName}}</span>
              <a @click="reset" v-if="!isShow">修改上报名称</a>
            </div>
          </el-col>
          <el-col :span="5"></el-col>
          <el-col :span="5"></el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">经营种类：<span>——</span></div>
          </el-col>
          <el-col :span="5"></el-col>
          <el-col :span="5"></el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">省市区：<span>--</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">商铺详细地址：<span>{{msg.address}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">资料提交时间：<span>——</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label">资料审核状态：<span>{{msg.stat}}</span></div>
          </el-col>
          <el-col :span="5">
            <div class="label"><span></span></div>
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary">
        <p class="lead">认证资料</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr class="row">
              <th class="col-md-3" style="text-align: center;">身份证正面:</th>
              <th class="col-md-3" style="text-align: center;">身份证反面:</th>
              <th class="col-md-3" style="text-align: center;">手持身份证:</th>
              <th class="col-md-3" style="text-align: center;">银行卡正面:</th>
              <th class="col-md-3" style="text-align: center;">手持结算卡:</th>
            </tr>
            <tr class="row">
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="msg.identityFacePic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="msg.identityOppositePic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="msg.identityHandPic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="msg.bankPic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="msg.bankHandPic" alt=""/>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">默认结算卡</p>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">结算卡开户名：<span>{{msg.name}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">身份证号：<span>{{msg.identity}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">商户结算卡号：<span>{{msg.bankNo}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">结算卡所属银行：<span>{{msg.bankName}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">支行信息：<span>{{msg.branchName}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg" justify="space-around" style="margin-bottom: 15px">
          <el-col :span="5">
            <div class="label">认证状态：<span>{{msg.isAuthen}}</span></div>
          </el-col>
          <el-col :span="5">
          </el-col>
          <el-col :span="5">
          </el-col>
        </el-row>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <p class="lead">费率信息</p>
        <div style="width: 80%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="rateInfo" border style="width: 100%">
              <el-table-column prop="channelName" label="通道名称"></el-table-column>
              <el-table-column prop="merchantRate" label="支付结算手续费"></el-table-column>
              <el-table-column prop="time" label="结算时间"></el-table-column>
              <el-table-column prop="withdrawMoney" label="提现手续费"></el-table-column>
              <el-table-column prop="entNet" label="商户入网状态"></el-table-column>
              <el-table-column prop="remarks" label="商户入网备注信息"></el-table-column>
            </el-table>
          </template>
        </div>
      </div>
      <div class="box box-primary" v-if="!isShow">
        <p class="lead">审核日志</p>
        <div class="table-responsive">
          <div class="col-sm-12">
            <table id="example2" class="table table-bordered table-hover dataTable" role="grid"
                   aria-describedby="example2_info">
              <thead>
              <tr role="row">
                <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                    aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">资料审核状态
                </th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                    aria-label="Browser: activate to sort column ascending">审核时间
                </th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                    aria-label="Platform(s): activate to sort column ascending">审核人
                </th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                    aria-label="Engine version: activate to sort column ascending">批复信息
                </th>
              </tr>
              </thead>
              <tbody id="content">
              <tr role="row" class="odd" v-for="re in this.$data.res">
                <td class="sorting_1">{{re.status|status}}</td>
                <td>{{re.createTime|changeTime}}</td>
                <td>—</td>
                <td>{{re.descr}}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="mask" id="mask" style="display: none" @click="isNo()">
        <p @click="isNo">×</p>
        <img src="" alt="">
      </div>
      <div class="box box-primary" v-if="isShow">
        <p class="lead">审核</p>

        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right;height: 35px;line-height: 35px;">审核意见:</th>
              <td><input type="text" name="name" placeholder="不通过必填" v-model="reason"
                         style="height: 35px;line-height: 35px;width: 50%;"></td>
            </tr>
            <tr>
              <th style="text-align: right">
                <div class="btn btn-danger" @click="unAudit">不 通 过</div>
              </th>
              <td>
                <div class="btn btn-success" @click="audit($event)">通 过</div>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'storeAudit',
    data () {
      return {
        dealerMask: false,
        id: '',
        msg: {
          id: '',
          merchantName: '',
          identity: '',
          address: '',
          bankNo: '',
          mobile: '',
          identityFacePic: '',
          identityOppositePic: '',
          identityHandPic: '',
          bankHandPic: '',
          proxyName1: '',
          proxyName: '',
          reserveMobile: '',
          createTime: '',
          proxyNameYQ: '',
          proxyNameYQ1: '',
        },
        reason: '',
        status: '',
        isShow: true,
        res: [],
        rateInfo: []
      }
    },
    created: function () {
      this.$data.id = this.$route.query.id;
      if (this.$route.query.status != 2) {
        this.$data.isShow = false;
      }
      this.$http.post('/admin/QueryMerchantInfoRecord/getAll', {id: this.$data.id})
        .then(function (res) {
          this.$data.msg = res.data.list[0];
          this.$data.res = res.data.res;
          this.$data.rateInfo = res.data.rateInfo;
          for (let i = 0; i < this.rateInfo.length; i++) {
            this.rateInfo[i].merchantRate = parseFloat(this.rateInfo[i].merchantRate * 100).toFixed(2) + '%'
            this.rateInfo[i].withdrawMoney = this.rateInfo[i].withdrawMoney + '元/笔'
          }
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
    },
    methods: {
      //修改名称
      reset: function () {
        this.$prompt('请输入新名称', '修改上报名称', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => {
          this.$http.post('/admin/changeMerchantName/change', {id: this.$route.query.id, merchantChangeName: value})
            .then(function (res) {
              this.$data.msg.merchantChangeName = value;
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

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消修改'
          });
        });
      },
      audit: function (event) {
        this.$http.post('/admin/merchantInfoCheckRecord/record', {
          merchantId: this.$data.id
        }).then(function (res) {
          this.$router.push('/admin/record/storeList')
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
      },
      unAudit: function () {
        this.$http.post('/admin/merchantInfoCheckRecord/auditFailure', {
          merchantId: this.$data.id,
          descr: this.$data.reason
        })
          .then(function (res) {
            this.$router.push('/admin/record/storeList')
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      changeBig: function (e) {
        e = e || window.event;
        var obj = e.srcElement || e.target;
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        img.src = obj.src;
        mask.style.display = 'block'
      },
      isNo: function () {
        document.getElementById('mask').style.display = 'none'
      }
    },
    filters: {
      status: function (val) {
        val = Number(val)
        if (val == 0) {
          val = "已注册"
        } else if (val == 1) {
          val = "已提交基本资料"
        } else if (val == 2) {
          val = "待审核"
        } else if (val == 3) {
          val = "审核通过"
        } else if (val == 4) {
          val = "审核未通过"
        }
        return val;
      },
      changeTime: function (val) {
        if (val == '' || val == null) {
          return ''
        } else {
          val = new Date(val)
          var year = val.getFullYear();
          var month = val.getMonth() + 1;
          var date = val.getDate();
          var hour = val.getHours();
          var minute = val.getMinutes();
          var second = val.getSeconds();

          function tod(a) {
            if (a < 10) {
              a = "0" + a
            }
            return a;
          }

          return year + "-" + tod(month) + "-" + tod(date) + " " + tod(hour) + ":" + tod(minute) + ":" + tod(second);
        }
      },
      changeDeal: function (val) {
        return val = val ? val : '无'
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less" rel="stylesheet/less">
  .box-primary {
    padding-bottom: 15px;
  }

  .label {
    color: #333;
    span, a {
      font-weight: normal;
    }
    a {
      color: #20a0ff;
    }
  }

  .mask {
    background: rgba(0, 0, 0, 0.8);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    p {
      position: absolute;
      top: 20px;
      right: 20px;
      z-index: 1200;
      width: 65px;
      height: 65px;
      line-height: 55px;
      font-size: 65px;
      color: #d2d1d1;
      text-align: center;
      border: 6px solid #adaaaa;
      border-radius: 50%;
      box-shadow: 0 0 16px #000;
      text-shadow: 0 0 16px #000;
    }

    img {
      display: inherit;
      height: 100%;
      margin: 0 auto;
    }
  }
</style>
