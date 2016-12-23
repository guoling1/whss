<template lang="html">
  <div id="storeAudit">
    <div v-if="isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">商户审核</div>
    <div v-else="isShow" style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">商户资料</div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead">基本信息</p>

        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">商户名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.merchantName"></td>
              <th style="text-align: right">商户地址:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.address"></td>
              <th style="text-align: right">所属二代:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName1|changeDeal"></td>
            </tr>
            <tr>
              <th style="text-align: right">所属一代:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName|changeDeal"></td>
              <th style="text-align: right">注册手机:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.mobile"></td>
              <th style="text-align: right">注册时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.createTime|changeTime"></td>
            </tr>
            <tr>
              <th style="text-align: right">商户编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.id"></td>
              <th style="text-align: right">真实姓名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.name"></td>
              <th style="text-align: right">身份证号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.identity"></td>
            </tr>
            <tr>
              <th style="text-align: right">结算卡号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.bankNo"></td>
              <th style="text-align: right">开户手机号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.reserveMobile"></td>

            </tr>
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">照片</p>

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
              <td class="col-md-3" style="text-align: center;border: none;"><img style="width: 200px" @click="changeBig()" :src="msg.identityFacePic" alt=""/></td>
              <td class="col-md-3" style="text-align: center;border: none;"><img style="width: 200px"  @click="changeBig()" :src="msg.identityOppositePic" alt=""/></td>

              <td class="col-md-3" style="text-align: center;border: none;"><img style="width: 200px"  @click="changeBig()" :src="msg.identityHandPic" alt=""/></td>

              <td class="col-md-3" style="text-align: center;border: none;"><img style="width: 200px"  @click="changeBig()" :src="msg.bankHandPic" alt=""/></td>
            </tr>
            </tbody>
          </table>
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
          createTime:''
        },
        reason:'',
        status:'',
        isShow:true
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
          this.$data.msg = res.data[0];
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
