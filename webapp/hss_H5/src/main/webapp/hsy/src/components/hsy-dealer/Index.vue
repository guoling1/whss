<template lang="html">
  <div id="index">
    <div class="header">
      <div class="top">
        <!--<div class="photo">-->
          <!--&lt;!&ndash; <img src="" alt="" /> &ndash;&gt;-->
        <!--</div>-->
        <span>您好，{{$$data.pageInfo.mobile}}</span>

        <div class="right">
          <router-link to="/dealer/myMsg"><img src="../../assets/tx.png" alt=""/>
            我的信息</router-link>
          <router-link to="/dealer/myCard"><img src="../../assets/yhcard.png" alt=""/>
            我的结算卡</router-link>

        </div>
      </div>
      <div class="bottom">
        <div class="left">
          <p><span>{{$$data.pageInfo.yesterdayProfitMoney}}</span>元</p>

          <p>昨日分润总额</p>
        </div>
        <div class="middle">
          <p><span>{{$$data.pageInfo.yesterdayDealMoney}}</span>元</p>

          <p>昨日门店交易总额</p>
        </div>
        <div class="right">
          <p v-if="pageInfo.level==1"><span>{{$$data.pageInfo.firstLevelDealerCodeInfos.lastDayActivateCount}}</span>家</p>
          <p v-if="pageInfo.level==2"><span>{{$$data.pageInfo.secondLevelDealerCodeInfo.lastDayActivateCount}}</span>家</p>

          <p>昨日激活二维码</p>
        </div>
      </div>
    </div>
    <!-- 一级代理商要展示的信息 -->
    <ul class="list" v-if="pageInfo.level==1">
      <li>
        <img src="../../assets/mar.png" alt=""/>

        <div class="">
          <p>{{$$data.pageInfo.firstLevelDealerCodeInfos.residueCount}}个</p>

          <p>剩余二维码</p>
        </div>
      </li>
      <li>
        <img src="../../assets/jt.png" alt=""/>

        <div class="">
          <p>{{$$data.pageInfo.firstLevelDealerCodeInfos.distributeCount}}个</p>

          <p>已配二维码</p>
        </div>
      </li>
      <li>
        <img src="../../assets/time.png" alt=""/>

        <div class="">
          <p>{{$$data.pageInfo.firstLevelDealerCodeInfos.unActivateCount}}个</p>

          <p>未激活二维码</p>
        </div>
      </li>
      <li>
        <img src="../../assets/lamp.png" alt=""/>

        <div class="">
          <p>{{$$data.pageInfo.firstLevelDealerCodeInfos.activateCount}}个</p>

          <p>已激活二维码</p>
        </div>
      </li>
    </ul>
    <div class="btn" v-if="pageInfo.level==1">
      <span @click="issueCord">分配二维码</span>
      <span @click="addDealer">新增二级代理</span>
    </div>
    <div class="secGain same" v-if="pageInfo.level==1">
      <div class="top">
        <i></i>
        <span class="title">我的分润</span>
        <span class="level">二级代理</span>
        <span class="details" @click="aginDetail">详情</span>
      </div>
      <div class="bottom">
        <div class="left">
          <p class="price">{{$$data.pageInfo.secondYesterdayProfitMoney}}</p>

          <p>昨日分润金额(元)</p>
        </div>
        <div class="right">
          <p class="price">{{$$data.pageInfo.secondHistoryProfitMoney}}</p>

          <p>历史分润总额(元)</p>
        </div>
      </div>
    </div>
    <div class="dealer same" v-if="pageInfo.level==1">
      <div class="top">
        <i></i>
        <span class="title">我发展的二级代理</span>
        <span class="details" @click="secDetail">详情</span>
      </div>
      <div class="bottom">
        <div class="left">
          <p class="price">{{$$data.pageInfo.mySecondDealerCount}}</p>

          <p>二级代理个数</p>
        </div>
        <div class="right">
          <p class="price">{{$$data.pageInfo.distributeToSecondDealerCodeCount}}</p>

          <p>已分配二维码(个)</p>
        </div>
      </div>
    </div>
    <!-- 二级代理商要展示的信息 -->
    <ul v-if="pageInfo.level==2" class="secDiff">
      <li>
        <p>待结算分润:
        <span>{{$$data.pageInfo.waitBalanceMoney}}</span>元 月底结算</p>
      </li>
      <li>
        <p>已结分润:
          <span>{{$$data.pageInfo.alreadyBalanceMoney}}</span>元</p>
      </li>
      <li>
        <p>我的二维码:
          <span>{{$$data.pageInfo.secondLevelDealerCodeInfo.codeCount}}</span>个</p>
      </li>
      <li>
        <p>未激活二维码:
          <span>{{$$data.pageInfo.secondLevelDealerCodeInfo.unActivateCount}}</span>个</p>
      </li>
    </ul>
    <!-- 都需要展示的信息代理商要展示的信息 -->
    <div class="myGain same">
      <div class="top">
        <i></i>
        <span class="title">我的分润</span>
        <span class="level">直接店铺</span>
        <span class="details" @click="aginDetail($event,'merchant')">详情</span>
      </div>
      <div class="bottom">
        <div class="left">
          <p class="price">{{$$data.pageInfo.merchantYesterdayProfitMoney}}</p>

          <p>昨日分润金额(元)</p>
        </div>
        <div class="right">
          <p class="price">{{$$data.pageInfo.merchantHistoryProfitMoney}}</p>

          <p>历史分润总额(元)</p>
        </div>
      </div>
    </div>
    <div class="myStore same">
      <div class="top">
        <i></i>
        <span class="title">我发展的店铺</span>
        <span class="details" @click="myStore">详情</span>
      </div>
      <div class="bottom">
        <div class="left">
          <p class="price">{{$$data.pageInfo.myMerchantCount.currentWeekActivateMerchantCount}}</p>

          <p>本周激活店铺</p>
        </div>
        <div class="right">
          <p class="price">{{$$data.pageInfo.myMerchantCount.activateMerchantCount}}</p>

          <p>已激活店铺总数(家)</p>
        </div>
      </div>
    </div>
    <div class="code" v-if="pageInfo.level==1">
      直客二维码：{{$$data.pageInfo.firstLevelDealerCodeInfos.distributeToSelfCount}}个
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'index',
    data: function () {
      return {
        pageInfo: {
          mobile: '',
          level: null,
          yesterdayProfitMoney: 0,
          yesterdayDealMoney: 0,
          waitBalanceMoney: null,
          alreadyBalanceMoney: null,
          historyProfitMoney: null,
          secondLevelDealerCodeInfo: {
            codeCount: 0,
            unActivateCount: 0,
            lastDayActivateCount: 0
          },
          myMerchantCount: {
            currentWeekActivateMerchantCount: 0,
            activateMerchantCount: 0
          },
          secondYesterdayProfitMoney: 0,
          secondHistoryProfitMoney: 0,
          merchantYesterdayProfitMoney: 0,
          merchantHistoryProfitMoney: 0,
          firstLevelDealerCodeInfos: {
            distributeToSelfCount: 0,
            lastDayActivateCount: 0,
            residueCount: 0,
            distributeCount: 0,
            unActivateCount: 0,
            activateCount: 0
          },
          mySecondDealerCount: 1,
          distributeToSecondDealerCodeCount: 0
        }
      }
    },
    created: function () {
      this.$http.post('/dealer/indexInfo').then(function (res) {
        this.$data.pageInfo = res.data;
      }, function (err) {
        this.$store.commit('MESSAGE_ACCORD_SHOW', {
          text: err.statusMessage
        })
      })
    },
    methods: {
      //分配二维码
      issueCord: function () {
        this.$router.push('/dealer/issueCord')
      },
      //一级发展的二级详情
      secDetail: function () {
        this.$router.push('/dealer/myDealer')
      },
      addDealer: function () {
        this.$router.push('/dealer/addDealer')
      },
      aginDetail: function ($event,val) {
        console.log(val)
        this.$router.push({path:'/dealer/aginDetail',query:{source:val}})
      },
      myStore: function () {
        this.$router.push('/dealer/myStore')
      }
    },
    computed: {
      $$data: function () {
        return this.$data;
      }
    }
  }
</script>

<style lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  #main {
    overflow: auto;
    background: #eceff6;
  }

  .header {
    width: 100%;
    height: 173px;
    background: #5289fb;
    padding: 0 10px 0 15px;
    margin-bottom: 6px;
    color: #fff;
    .top {
      height: 57px;
      line-height: 57px;
      border-bottom: 1px solid #6a9bff;
      font-size: 12px;
      text-align: left;
      .photo {
        display: inline-block;
        vertical-align: middle;
        height: 24px;
        width: 24px;
        border: 2px solid #fff;
        border-radius: 50%;
        background: #ccc;
        margin-right: 1%;
      }
      .right {
        float: right;
        color: #a9d1ff;
        width: 50%;
        text-align: right;
        img:nth-child(1) {
          width: 10px;
          height: 10px;
          margin-right: 1%;
        }
        img:nth-child(2) {
          width: 11px;
          height: 9px;
          margin-right: 1%;
          margin-left: 4%;
        }
      }
    }
    .bottom {
      div {
        display: inline-block;
        width: 30%;
        font-size: 12px;
        p:nth-child(1) {
          margin: 38px 0 8px 0;
          font-weight: bold;
          span {
            font-size: 19px;
          }
        }
      }
    }
  }

  .list {
    background: #fff;
    width: 100%;
    li {
      display: inline-block;
      width: 49%;
      height: 75px;
      border-bottom: 1px solid #eceff6;
      border-right: 1px solid #eceff6;
      &:nth-child(2), &:nth-child(4) {
        //width: 50%;
        border-right: none;
      }
      img {
        display: inline-block;
        vertical-align: super;
        width: 21px;
        height: 21px;
      }
      div {
        display: inline-block;
        margin: 23px 0 20px 23px;
        color: #272727;
        font-size: 12px;
        p:nth-child(1) {
          text-align: left;
          font-size: 15px;
          font-weight: bold;
          margin-bottom: 9px;
        }
      }
    }
  }

  .btn {
    height: 49px;
    line-height: 49px;
    width: 100%;
    background: #fff;
    font-size: 16px;
    color: #2c66de;
    margin-bottom: 6px;
    span {
      float: left;
      width: 50%;
    }
  }

  .same {
    margin-bottom: 6px;
    width: 100%;
    height: 155px;
    background: #fff;
    padding: 0 15px;
    .top {
      height: 49px;
      line-height: 49px;
      border-bottom: 1px solid #eceff6;
      text-align: left;
      i {
        display: inline-block;
        width: 3px;
        height: 11px;
        background: #5289fb;
      }
      .title {
        font-size: 15px;
        font-weight: bold;
        color: #272727;
        margin-right: 10px;
      }
      .level {
        padding-left: 9px;
        font-size: 12px;
        color: #272727;
        border-left: 1px solid #d9d9d9;
      }
      .details {
        float: right;
        font-size: 12px;
        color: #999;
      }
    }
    .bottom {
      font-size: 12px;
      color: #999;
      div {
        float: left;
        width: 43%;
        text-align: left;
        margin: 31px 0 0 15px;
        .price {
          font-size: 24px;
          color: #2f2f2f;
          margin-bottom: 15px;
        }
      }
    }
  }

  .myStore {
    margin-bottom: 0;
  }

  .code {
    padding-left: 15px;
    width: 100%;
    height: 47px;
    line-height: 47px;
    background: #fff;
    text-align: left;
    font-size: 15px;
    color: #2f2f2f;
  }
  .secDiff{
    margin:7px 0;
    background: #fff;
    overflow:hidden;
    padding-left:15px;
    li{
      float: left;
      width: 60%;
      text-align: left;

      p{
        height: 30px;
        line-height: 30px;
        color: #999;
        font-size:14px;
        span{
          color: #272727;
        }
      }
    }
    li:nth-child(even){
      width: 40%;
    }
  }
  a,a:hover,a:link,a:active{ text-decoration:none; color:#a9d1ff}
</style>
