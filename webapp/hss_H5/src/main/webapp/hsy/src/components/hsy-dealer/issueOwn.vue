<template lang="html">
<div id="issueOwn">
  <div class="">
    <div class="content">
      <label for="">
        分配个数
        <input type="text" name="number" placeholder="请输入分配个数" v-model="number">
      </label>
      <p>剩余可分配个数：{{this.$$data.count}}个
        <span @click="record">分配记录</span>
      </p>
    </div>
    <div class="submit" @click="submit">提交</div>
  </div>

</div>
</template>

<script>
export default {
  name:'issueOwn',
  data(){
    return {
      count:'',
      name:'',
      number:'',
      id:''
    }
  },
  created: function() {
    this.$http.post('/dealer/getCodeCount')
    .then(function(res){
      this.$data.count = res.data.count;
    },function(err) {
      console.log(err);
    })
  },
  methods:{
    record:function() {
      this.$router.push('/dealer/issueCord/recordOwn')
    },
    submit: function() {
      this.$http.post('/dealer/distributeQRCode',{
        dealerId:0,
        isSelf:1,
        count:this.$data.number
      }).then(function(res){
        this.$router.push({path:'/dealer/issueCord/success',query:{
          name: res.data.name,
          mobile: res.data.mobile,
          distributeDate: res.data.distributeDate,
          count: res.data.count,
          startCode: res.data.startCode,
          endCode: res.data.endCode
        }})
      },function(err){
        this.$store.commit('MESSAGE_ACCORD_SHOW', {
          text: err.statusMessage
        })
      })
    }
  },
  computed: {
    $$data: function() {
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
.content{
  padding: 0 15px;
  background: #fff;
  margin-bottom: 20px;
  label{
    display: inline-block;
    width: 100%;
    height: 49px;
    line-height: 49px;
    text-align: left;
    font-size: 15px;
    color: #272727;
    border-bottom: 1px solid #e8eaf2;
    input{
      margin-left: 5px;
      width: 70%;
      height: 47px;
      line-height: 47px;
    }
  }
  .list{
    position: absolute;
    right:0;
    width: 77%;
    max-height: 200px;
    overflow: auto;
    border: 1px solid #e8eaf2;
    background: #fff;
    padding-left: 5px;
    margin-right:5px;
    li{
      height: 25px;
    }
  }
  p{
    height: 32px;
    line-height: 32px;
    font-size: 12px;
    color: #272727;
    text-align: left;
    span{
      float: right;
      color: #5289fb;
    }
  }
}
.submit{
  margin: 0 15px;
  height: 50px;
  line-height: 50px;
  font-size: 16px;
  border-radius: 4px;
  color: #fff;
  background: #5289fb;
}
</style>
