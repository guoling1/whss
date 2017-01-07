<template lang="html">
  <div id="storeAudit">
    <div v-if="isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">商户审核</div>
    <div v-else="isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">商户资料</div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead">商户注册信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">注册手机:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.mobile" readonly></td>
              <th style="text-align: right">注册时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.createTime|changeTime" readonly></td>
              <th style="text-align: right">注册方式:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">一级代理编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">一级代理名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName|changeDeal" readonly></td>
              <th></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">二级代理编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">二级代理名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName1|changeDeal" readonly></td>
              <th></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">推荐人编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">推荐人名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName1|changeDeal" readonly></td>
              <th style="text-align: right">推荐人注册手机号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
            </tr>
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户认证信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">真实姓名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.name" readonly></td>
              <th style="text-align: right">身份证号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.identity" readonly></td>
              <th style="text-align: right">商户名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.merchantName" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">经营种类:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">商户详细地址:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.address" readonly></td>
              <th style="text-align: right">结算卡号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.bankNo" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">所属银行:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.bankName" readonly></td>
              <th style="text-align: right">开户手机号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.reserveMobile" readonly></td>
              <th style="text-align: right">实名认证时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.authenticationTime" readonly></td>
            </tr>
            </tbody></table>
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
          <!--<table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">资料审核状态:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.status|status" readonly></td>
              <th style="text-align: right">审核时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.checkedTime|changeTime" readonly></td>
              <th style="text-align: right">审核人:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">审核批复信息:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th></th>
              <td></td>
              <th></th>
              <td></td>
            </tr>
            </tbody></table>-->
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
              <th class="col-md-3" style="text-align: center;">手持身份证:</th>
              <th class="col-md-3" style="text-align: center;">手持结算卡:</th>
            </tr>
            <tr class="row">
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="msg.identityFacePic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.identityOppositePic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.identityHandPic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.bankHandPic" alt=""/>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户结算信息(好收收)</p>
        <div class="table-responsive">
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
        </div>
      </div>
      <div class="mask" id="mask" style="display: none" @click="isNo()">
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
        },
        reason:'',
        status:'',
        isShow:true,
        res: []
      }
    },
    created: function () {
      this.$data.id = this.$route.query.id;
      this.$data.status = this.$route.query.status;
      if(this.$data.status !=2){
        this.$data.isShow = false;
      }
      this.$http.post('/admin/QueryMerchantInfoRecord/getAll',{id:this.$data.id})
        .then(function (res) {
          this.$data.msg = res.data.list[0];
          this.$data.res = res.data.res;
          console.log(this.$data.res)
        },function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })

    },
    methods: {
      audit: function (event) {
        this.$http.post('/admin/merchantInfoCheckRecord/record', {
          merchantId: this.$data.id
        }).then(function (res) {
          console.log(res)

          this.$router.push('/admin/record/storeList')
        }, function (err) {
          console.log(err);
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      },
      unAudit: function () {
        this.$http.post('/admin/merchantInfoCheckRecord/auditFailure',{merchantId: this.$data.id,descr:this.$data.reason})
          .then(function (res) {
            this.$router.push('/admin/record/storeList')
          },function (err) {
            console.log(err)
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
          return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
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

  img{
    display: inherit;
    height: 100%;
    margin: 0 auto;
  }
  }
</style>
