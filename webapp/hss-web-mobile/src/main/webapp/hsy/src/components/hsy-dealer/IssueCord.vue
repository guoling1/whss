<template lang="html">

<div id="issueCord">
  <div class="header">
    <span :class="{active:select}" @click='toDealer'>分配给二级代理</span>
    <span :class="{active:!select}" @click='toOwn'>分配给自己</span>
  </div>
  <router-view></router-view>
</div>
</template>

<script>
export default {
  name:'issueCord',
  data(){
    return {
      select:true
    }
  },
  // beforeCreate: function() {
  //   this.$http.post('/dealer/getCodeCount')
  // },
  methods: {
    toDealer: function() {
      this.$router.push('/dealer/issueCord/issue')
      this.$data.select=true;
    },
    toOwn: function() {
      this.$router.push('/dealer/issueCord/issueOwn')
      this.$data.select=false;
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
.header{
  height: 50px;
  width: 100%;
  line-height: 50px;
  font-size: 15px;
  color: #5289fb;
  margin-bottom: 7px;
  span{
    float: left;
    background: #fff;
    width: 50%;
    &.active{
      background: #f7f9fd;
    }
  }
}
</style>
