<template lang="html">
  <div id="storeAudit">
    <div class="box-header with-border" style="margin: 0 0 0 3px;">
      <h3 v-if="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户审核</h3>
      <h3 v-else="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户资料</h3>
    </div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead">商户注册信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">注册手机:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.contactCellphone" readonly></td>
              <th style="text-align: right">注册时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.createTime|changeTime" readonly></td>
              <th style="text-align: right">注册方式:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="--" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">一级代理编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.firstDealerId==0?'':msg.firstDealerId" readonly></td>
              <th style="text-align: right">一级代理名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName" readonly></td>
              <th></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">二级代理编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.secondDealerId==0?'':msg.secondDealerId" readonly></td>
              <th style="text-align: right">二级代理名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName1" readonly></td>
              <th></th>
              <td></td>
            </tr>
            <!--<tr>
              <th style="text-align: right">推荐人编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">推荐人名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">推荐人注册手机号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">推荐所属一级代理名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyNameYq" readonly></td>
              <th style="text-align: right">推荐所属二级代理名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyNameYq1" readonly></td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>-->
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户认证信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">商铺名称（全称）:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.name" readonly></td>
              <th style="text-align: right">商铺简称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.shortName" readonly></td>
              <!--<th style="text-align: right">商户营业执照号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.licenceID" readonly></td>-->
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">店主（法人）实名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.contactName"  readonly></td>
              <th style="text-align: right">店主身份证号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.idcard" readonly></td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">商铺上报名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.name" readonly></td>
              <th style="text-align: right">经营种类:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.industryCode" readonly></td>
              <th style="text-align: right">省市区:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.districtCode" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">商铺详细地址:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.address" readonly></td>
              <th style="text-align: right">资料提交时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.updateTime|changeTime" readonly></td>
              <th style="text-align: right">资料审核状态:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.status" readonly></td>
            </tr>
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户认证资料</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr class="row">
              <th class="col-md-3" style="text-align: center;">身份证正面:</th>
              <th class="col-md-3" style="text-align: center;">身份证反面:</th>
              <th class="col-md-3" style="text-align: center;">营业执照:</th>
              <th class="col-md-3" style="text-align: center;">店面照片:</th>
              <th class="col-md-3" style="text-align: center;">收银台:</th>
              <th class="col-md-3" style="text-align: center;">室内照片:</th>
            </tr>
            <tr class="row">
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="msg.idcardf" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.idcardb" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.licenceID" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.storefrontID" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.counterID" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.indoorID" alt=""/>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户结算信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">结算卡类型:</th>
              <td>
                <el-radio-group v-model="msg.isPublic">
                  <el-radio :label="1">对公</el-radio>
                  <el-radio :label="2">对私</el-radio>
                </el-radio-group>
              </td>
              <th style="text-align: right">结算卡开户名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.cardAccountName" readonly></td>
              <th style="text-align: right" v-show="msg.isPublic==2">身份证号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.idcard" readonly v-show="msg.isPublic==2"></td>
            </tr>
            <tr>
              <th style="text-align: right">商户结算卡号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.cardNO" readonly></td>
              <th style="text-align: right">结算卡所属银行:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.cardBank" readonly></td>
              <th style="text-align: right">支行信息:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.bankAddress" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">结算方式:</th>
              <td>
                <el-radio-group>
                  <el-radio :label="3">T1结算到卡</el-radio>
                  <el-radio :label="4">T1结算到账户余额</el-radio>
                </el-radio-group>
              </td>
              <th style="text-align: right"></th>
              <td></td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <p class="lead">商户费率信息</p>
        <!--<div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">阳光万维微信:</th>
              <td></td>
              <th style="text-align: right">收款结算方式:</th>
              <td></td>
              <th style="text-align: right">提现费:</th>
              <td>元/笔</td>
            </tr>
            <tr>
              <th style="text-align: right">阳光万维支付宝:</th>
              <td></td>
              <th style="text-align: right">收款结算方式:</th>
              <td></td>
              <th style="text-align: right">提现费:</th>
              <td>元/笔</td>
            </tr>
            <tr>
              <th style="text-align: right">阳光万维无卡快捷:</th>
              <td></td>
              <th style="text-align: right">收款结算方式:</th>
              <td></td>
              <th style="text-align: right">提现费:</th>
              <td>元/笔</td>
            </tr>
            </tbody></table>
        </div>-->
        <div style="width: 70%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="tableData" border style="width: 100%">
              <el-table-column prop="name" label="通道名称" ></el-table-column>
              <el-table-column prop="rate" label="支付结算手续费"></el-table-column>
              <el-table-column prop="time" label="结算时间" ></el-table-column>
              <el-table-column prop="money" label="提现手续费" ></el-table-column>
            </el-table>
          </template>
        </div>

      </div>
      <div class="box box-primary" v-if="!isShow">
        <p class="lead">审核日志</p>
        <div class="table-responsive">
          <div class="col-sm-12">
            <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
              <thead>
              <tr role="row">
                <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">资料审核状态</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">审核时间</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">审核人</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">批复信息</th>
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
              <td><input type="text" name="name" placeholder="不通过必填" v-model="reason" style="height: 35px;line-height: 35px;width: 50%;"></td>
            </tr>
            <tr>
              <th style="text-align: right"><div class="btn btn-danger" @click="unAudit">不 通 过</div></th>
              <td><div class="btn btn-success" @click="audit($event)">通 过</div></td>
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
    name: 'dale',
    data () {
      return {
        id: '',
        msg:{
          id:'',
          merchantName:'',
          identity:'',
          address:'',
          bankNo:'',
          mobile:'',
          identityFacePic: '',
          identityOppositePic: '',
          identityHandPic: '',
          bankHandPic: '',
          proxyName1:'',
          proxyName: '',
          reserveMobile:'',
          createTime:'',
          proxyNameYQ:'',
          proxyNameYQ1:'',
        },
        reason:'',
        status:'',
        isShow:true,
        res: [],
        tableData:[{
          name:'魔宝支付宝',
          rate:'',
          time:'',
          money:''
        },{
          name:'魔宝微信',
          rate:'',
          time:'',
          money:''
        }]
      }
    },
    created: function () {
      this.$data.id = this.$route.query.id;
      this.$data.status = this.$route.query.status;
      if(this.$data.status !=2){
        this.$data.isShow = false;
      }
      this.$http.post('/admin/hsyMerchantList/getDetails',{id:this.$data.id})
        .then(function (res) {
          this.$data.msg = res.data;
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })

    },
    methods: {
      audit: function (event) {
        this.$http.post('/admin/hsyMerchantAudit/throughAudit', {
          id: this.$data.id,
          uid: this.$data.msg.uid,
        }).then(function (res) {
          /*this.$router.push('/admin/record/storeList')*/
          this.$router.go(-1)
        }, function (err) {
          console.log(err);
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      },
      unAudit: function () {
        this.$http.post('/admin/hsyMerchantAudit/rejectToExamine',{id: this.$data.id, uid: this.$data.msg.uid,checkErrorInfo:this.$data.reason})
          .then(function (res) {
            this.$router.push('/admin/record/storeList')
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      },
      changeBig: function (e) {
        e = e||window.event;
        var obj = e.srcElement||e.target;
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        img.src = obj.src;
        mask.style.display= 'block'
      },
      isNo: function () {
        document.getElementById('mask').style.display='none'
      }
    },
    filters: {
      status: function (val) {
        val = Number(val)
        if(val == 0){
          val = "已注册"
        }else if(val == 1){
          val = "已提交基本资料"
        }else if(val == 2){
          val = "待审核"
        }else if(val == 3){
          val = "审核通过"
        }else if(val == 4){
          val="审核未通过"
        }
        return val;
      },
      changeTime: function (val) {
        if(val==''||val==null){
          return ''
        }else {
          val = new Date(val)
          var year=val.getFullYear();
          var month=val.getMonth()+1;
          var date=val.getDate();
          var hour=val.getHours();
          var minute=val.getMinutes();
          var second=val.getSeconds();
          function tod(a) {
            if(a<10){
              a = "0"+a
            }
            return a;
          }
          return year+"-"+tod(month)+"-"+tod(date)+" "+tod(hour)+":"+tod(minute)+":"+tod(second);
        }
      },
      changeDeal: function (val) {
        return val=val?val:'无'
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .mask{
    background: rgba(0,0,0,0.8);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top:0;
    left: 0;
  p{
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

  img{
    display: inherit;
    height: 100%;
    margin: 0 auto;
  }
  }
</style>
