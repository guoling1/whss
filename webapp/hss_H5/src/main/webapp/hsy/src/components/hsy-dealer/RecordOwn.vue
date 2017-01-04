<template lang="html">
  <div id="record">
    <div class="head">
      <span class="w30">起始号码</span>
      <span class="w30">终止号码</span>
      <span class="w12">个数</span>
      <span>日期</span>
    </div>
    <ul>
      <li v-for="record in this.$$data.records">
        <p>{{record.secondLevelDealerName}}</p>
        <span class="w30">{{record.startCode}}</span>
        <span class="w30">{{record.endCode}}</span>
        <span class="w12">{{record.count}}</span>
        <span>{{record.distributeDate|time}}</span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name:'record',
  data(){
    return{
      records:[]
    }
  },
  created:function() {
    this.$http.post('/dealer/toSelfQRCode')
    .then(function(res){
      this.$data.records = res.data;
    },function(err){
      this.$store.commit('MESSAGE_ACCORD_SHOW', {
        text: err.statusMessage
      })
    })
  },
  methods: {

  },
  computed: {
    $$data:function () {
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
}
#record{
  background: #fff;
  width: 100%;
  color: #272727;
  font-size: 12px;
  text-align: left;
  .head{
    height: 32px;
    line-height: 32px;
    padding: 0 15px;
  }
  li{
    border-top: 1px solid #e8eaf2;
    padding: 0 15px 17px 15px;
    p{
      font-size: 14px;
      font-weight: bold;
      margin: 16px 0 12px 0;
    }
  }
  span.w30{
    display: inline-block;
    width: 30%;
  }
  span.w12{
    display: inline-block;
    width: 12%;
  }
}
</style>
