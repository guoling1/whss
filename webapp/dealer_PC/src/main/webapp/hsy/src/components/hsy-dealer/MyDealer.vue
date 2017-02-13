<template lang="html">
  <div id="myDealer">
    <div class="header">
      <p class="w1">二级代理</p>

      <p class="w3">配码数</p>

      <p class="w3">已激活</p>
    </div>
    <ul>
      <li>
        <div class="" @click='show(index)' v-for='(myDealer,index) in this.$$data.myDealers'>
          <p class="w1">{{myDealer.proxyName}}</p>

          <p :class="myDealer.isBlue?'w3 blue':'w3'">{{myDealer.distributeCount}}</p>

          <p :class="myDealer.isBlue?'w3 blue':'w3'">{{myDealer.activeCount}}</p>
        </div>
      </li>
    </ul>
    <div class="mask" v-if="this.$$data.isShow">
      <div class="content">
        <h3>{{dealerDetail.proxyName}}</h3>
        <span class="X" @click='close'>×</span>

        <div class="total">
          <p>手机号:<span>{{dealerDetail.mobile}}</span></p>

          <p>总数:<span>{{myDealers[idx].distributeCount}}</span></p>

          <p>已激活:<span>{{myDealers[idx].activeCount}}</span></p>
        </div>
        <div class="detail">
          <div class="title">
            <p class="w35">配码日期</p>

            <p class="w50">码段</p>

            <p>个数</p>
          </div>
          <div class="content" v-for='codeRecord in dealerDetail.codeRecords'>
            <div class="row">
              <p class="w35">{{codeRecord.distributeDate|time}}</p>

              <p class="w50 middle">{{codeRecord.startCode}}-{{codeRecord.endCode}}</p>

              <p>{{codeRecord.count}}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'myDealer',
    data: function () {
      return {
        isShow: false,
        myDealers: [],
        dealerDetail: {},
        idx:''
      }
    },
    created: function () {
      this.$http.post('/dealer/getMyDealers')
        .then(function (res) {
          this.$data.myDealers = res.data;
          for(var i=0;i<this.$data.myDealers.length;i++){
            this.$data.myDealers[i].isBlue=false;
            if(this.$data.myDealers[i].distributeCount!=0){
              this.$data.myDealers[i].isBlue=true;
            }
          }
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      show: function (index) {
        if(this.$data.myDealers[index].isBlue==true){
          this.$data.isShow = !this.$data.isShow;
          this.$data.idx = index;
          this.$http.post('/dealer/getMyDealerDetail', {dealerId: this.$data.myDealers[index].dealerId})
            .then(function (res) {
              this.$data.dealerDetail = res.data;
            }, function (err) {
              console.log(err);
            })
        }
      },
      close: function () {
        this.$data.isShow = !this.$data.isShow;
      }
    },
    computed: {
      $$data: function () {
        return this.$data;
      }
    },
    filters: {
      time: function (val) {
        let now = new Date(val);
        var   year=now.getFullYear();
        var   month=now.getMonth()+1;
        var   date=now.getDate();
        return   year+"-"+month+"-"+date;
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
    text-align: left;
  }

  #myDealer {
    font-size: 12px;
    color: #272727;
  }

  .header {
    background: #fff;
    height: 32px;
    line-height: 32px;
    padding-left: 15px;
    p {
      display: inline-block;
      font-weight: bold;
      font-size: 12px;
    }
  }

  .w1 {
    width: 32%;
  }

  .w3 {
    width: 33%;
  }

  .blue {
    color: #5289fb;
  }

  ul {
    li {
      margin: 7px 0;
      background: #fff;
      div {
        padding-left: 15px;
        height: 36px;
        line-height: 36px;
        border-bottom: 1px solid #eceff6;
        p {
          display: inline-block;
          font-size: 12px;
        }
        .red {
          color: #fd4545;
        }
      }
    }
  }

  .mask {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, .4);
    .content {
      position: inherit;
      left: 0;
      bottom: 0;
      padding: 0 15px;
      width: 100%;
      height: 374px;
      background: #fff;
      overflow: hidden;
      color: #272727;
      h3 {
        display: inline-block;
        font-size: 16px;
        font-weight: bold;
        margin: 20px 0;
      }
      .X {
        float: right;
        font-size: 36px;
        color: #c0c0c0;
        font-weight: normal;
        height: 56px;
        line-height: 56px;
      }
      .total {
        height: 30px;
        line-height: 30px;
        width: 100%;
        font-size: 14px;
        p {
          display: inline-block;
          margin-right: 3%;;
          span {
            font-weight: bold;
          }
        }
      }
      .middle{
        line-height: 13px;
        margin-top: 7px;
      }
      .detail {
        width: 100%;
        font-size: 12px;
        .title {
          height: 30px;
          line-height: 30px;
          background: #eceff6;
          font-weight: bold;
          padding: 0 23px;
          p {
            display: inline-block;
            font-weight: bold;
          }
        }
        .content {
          padding: 0;
          .row {
            height: 43px;
            line-height: 43px;
            border: 1px solid #eceff6;
            border-top: none;
            padding: 0 23px;
            p {
              display: inline-block;
            }
          }
        }

      }
    }
    .w35 {
      width: 35.5%;
    }
    .w50 {
      width: 50%;
    }
  }
</style>
