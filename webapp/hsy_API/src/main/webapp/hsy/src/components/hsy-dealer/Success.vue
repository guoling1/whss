<template lang="html">
  <div id="success">
    <div class="success">
      <img src="../../assets/succ.png" alt="" />
      <p>分配成功</p>
    </div>
    <ul>
      <li>
        <span>代理商名称</span>
        <span class="right">{{this.$$data.name}}</span>
      </li>
      <li>
        <span>代理商手机</span>
        <span class="right">{{this.$$data.mobile}}</span>
      </li>
      <li>
        <span>分配时间</span>
        <span class="right">{{this.$$data.distributeDate}}</span>
      </li>
      <li>
        <span>分配个数</span>
        <span class="right">{{this.$$data.count}}个</span>
      </li>
      <li>
        <span>分配号段</span>
        <span class="right">{{this.$$data.startCode}}至{{this.$$data.endCode}}</span>
      </li>
    </ul>
    <div class="submit" @click="success">
      确定
    </div>
  </div>
</template>

<script>
export default {
  name:'success',
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
  methods: {
    success: function(){
      this.$router.push('/dealer/issueCord/record')
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
.success{
  width: 100%;
  height: 150px;
  background: #fff;
  img{
    display: inline-block;
    width: 64px;
    height: 64px;
    margin: 0 auto;
    margin-top: 27px;
    margin-bottom: 18px;
  }
}
ul{
  background: #fff;

  li{
    padding: 0 15px;
    height: 50px;
    line-height: 50px;
    text-align: left;
    border-bottom: 1px solid #edeef5;
    .right{
      float: right;
    }
  }
}
.submit{
  margin: 15px;
  height: 50px;
  line-height: 50px;
  text-align: center;
  font-size: 16px;
  background: #5289fb;
  border-radius: 4px;
  color: #fff;
}
</style>
