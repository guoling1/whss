<template lang="html">
  <div id="issueSuccess">
    <h2>分配成功</h2>
    <ul>
      <li>
        <span class="left">代理商名称</span>
        <span>{{name}}</span>
      </li>
      <li>
        <span class="left">代理商手机</span>
        <span>{{mobile}}</span>
      </li>
      <li>
        <span class="left">分配时间</span>
        <span>{{distributeDate}}</span>
      </li>
      <li>
        <span class="left">分配个数</span>
        <span>{{count}}个</span>
      </li>
      <li style="height: inherit">
        <span class="left">分配号段</span>
        <div>
          <p v-for="code in codes">{{code.startCode}} 至 {{code.endCode}}</p>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
    name:'issueSuccess',
    data:function(){
      return {
        name:'',
        mobile:'',
        distributeDate:'',
        count:'',
        startCode:'',
        endCode:''
      }
    },
    created: function(){
      this.$data.name=this.$route.query.name;
      this.$data.mobile=this.$route.query.mobile;
      this.$data.distributeDate=this.$route.query.distributeDate;
      this.$data.count=this.$route.query.count;
      this.$data.startCode=this.$route.query.startCode;
      this.$data.endCode=this.$route.query.endCode;
    },
    computed:{
      $$data:function () {
        return this.$data;
      }
    },
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
  #issueSuccess {
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }
  ul{
  li{
    list-style: none;
    padding: 0 15px;
    height: 50px;
    line-height: 50px;
    text-align: left;
  span{
    margin-right:30px;
  }
  .left{
    font-size: 16px;
  }
  div{
    display: inline-block;
    vertical-align: top;
    padding-top: 10px;
  p{
    height: 20px;
    line-height: 20px;
  }
  }
  }
  }
  }


</style>
