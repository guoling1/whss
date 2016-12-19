<template lang="html">
  <div id="storeAudit">
    <h1 v-if="isShow">商户审核</h1>
    <h1 v-else="isShow">商户资料</h1>

    <div class="left">
      <div class="storeName">
        商户名称：{{msg.merchantName}}
      </div>
      <div class="dress">
        商户地址：{{msg.address}}
      </div>
      <div class="sec">
        所属二代：{{msg.proxyName1|changeDeal}}
      </div>
      <div class="fir">
        所属一代：{{msg.proxyName|changeDeal}}
      </div>
      <div class="phone">
        注册手机：{{msg.mobile}}
      </div>
      <div class="date">
        注册时间：{{msg.createTime|changeTime}}
      </div>
      <div class="num">
        商户编号：{{msg.id}}
      </div>
      <div class="name">
        真实姓名：{{msg.name}}
      </div>
      <div class="id">
        身份证号：{{msg.identity}}
      </div>
      <div class="cardNum">
        结算卡号：{{msg.bankNo}}
      </div>
      <div class="cardNum">
        开户手机号：{{msg.reserveMobile}}
      </div>
      <!--<div v-if="status==4">
        未通过原因：不合格
      </div>-->
    </div>
    <div class="right">
      <div class="firID">
        身份证正面:
        <div class="img">
          <!--<div @click="detail()">点击查看</div>-->
          <img @click="changeBig()" :src="msg.identityFacePic" alt=""/>
        </div>
      </div>
      <div class="lastID">
        身份证反面:
        <div class="img">
          <!--<div @click="detail()">点击查看</div>-->
          <img @click="changeBig()" :src="msg.identityOppositePic" alt=""/>
        </div>
      </div>
      <div class="handID">
        手持身份证:
        <div class="img">
          <!--<div @click="detail()">点击查看</div>-->
          <img @click="changeBig()" :src="msg.identityHandPic" alt=""/>
        </div>
      </div>
      <div class="handCard">
        手持结算卡:
        <div class="img">
          <!--<div @click="detail()">点击查看</div>-->
          <img @click="changeBig()" :src="msg.bankHandPic" alt=""/>
        </div>
      </div>
      <div class="mask" id="mask" style="display: none" @click="isNo()">
        <img src="" alt="">
      </div>
    </div>
    <div class="view" v-if="isShow">
      审核意见：
      <input type="text" name="name" placeholder="不通过必填" v-model="reason">

      <div class="btn btn-danger" @click="unAudit">不 通 过</div>
      <div class="btn btn-success" @click="audit($event)">通 过</div>
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
        val = new Date(val)
        var year=val.getFullYear();
        var month=val.getMonth()+1;
        var date=val.getDate();
        var hour=val.getHours();
        var minute=val.getMinutes();
        var second=val.getSeconds();
        return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
      },
      changeDeal: function (val) {
        return val=val?val:'无'
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
    margin-bottom: 20px;
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
  .storeAudit {
    float: right;
    width: 80%;
  }

  .left {
    float: left;
    width: 35%;
    font-size: 14px;
    line-height: 40px;
  }

  .right {
    float: left;
    width: 50%;
    & > div {
      display: inline-block;
      padding-left: 40px;
      padding-bottom: 50px;

      .img {
        display: inline-block;
        vertical-align: middle;
        width: 250px;
        height: 150px;
        position: relative;
        background: #dcdada;
        img{
          position: absolute;
          top:0;
          left: 0;
          width: 100%;
          height: 100%;
        }
  .big{
    position: fixed;
    top:10%;
    left:10%;
    z-index: 1100;
  }
        div{
          line-height: 150px;
          text-align: center;
          font-size: 22px;
          text-shadow: 4px 2px 5px #000;
        }
      }
    }
  }

  .view {
    overflow: hidden;
    margin-left: 10%;
    width: 500px;
    input {
      height: 30px;
      width: 400px;
    }
    .btn {
      font-size: 18px;
      width: 150px;
      //height: 30px;
      //padding: 0 10px;
      margin: 20px 0 0 80px;
    }
  }
</style>
