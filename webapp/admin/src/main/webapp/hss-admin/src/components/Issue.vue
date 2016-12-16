<template lang="html">
  <div id="issue">
    <h1>分配二维码</h1>
    <form>
      <label for="">
        一级代理
        <input @keyup="find" type="text" name="name" placeholder="输入代理手机号或名称" v-model="name" autocomplete="off"/>
        <ul class="list" v-if="listIsShow">
          <li v-for="(findDealer,index) in this.$data.findDealers" @click='change(index)'>
            <span>{{findDealer.name}}</span>
            <span>{{findDealer.mobile}}</span>
          </li>
        </ul>
      </label>
      <label for="">
        开始码段
        <input type="text" name="number" placeholder="请输入开始码段" v-model="query.startCode"/>
      </label>
      <label for="">
        结束码段
        <input type="text" name="number" placeholder="请输入结束码段" v-model="query.endCode"/>
      </label>
      <div class="submit btn btn-primary" id="submit" @click="submit">分配</div>
    </form>
    <message></message>
  </div>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
  export default {
    name: 'issue',
    components: {
      Message
    },
    data: function () {
      return {
        name:'',
        query:{
          dealerId:'',
          startCode:'',
          endCode:''
        },
        findDealers:[],
        listIsShow: false
      }
    },
    methods: {
      find: function () {
        if(this.$data.name!=''){
          this.$http.post('/admin/dealer/find',{condition:this.$data.name})
            .then(function(res){
              this.$data.findDealers=res.data;
              if(this.$data.findDealers.length!=0){
                this.$data.listIsShow = true;
              }
            },function(err){
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        }else{
          this.$data.listIsShow = false;
        }

      },
      change: function(index){
        this.$data.name = this.$data.findDealers[index].name;
        this.$data.query.dealerId = this.$data.findDealers[index].dealerId;
        this.$data.listIsShow = false;
      },
      submit: function() {
        this.$http.post('/admin/user/distributeRangeQRCode',this.$data.query)
          .then(function(res){
            this.$router.push({path:'/admin/record/issueSuccess',query:{
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
      },
    },
    computed: {

    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  .flexBox {
    display: box;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    display: -webkit-flex;
  }
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
    background: #fff;
    position: relative;
    left: 70px;
    width: 300px;
  }

  li {
    /*display: inline-block;*/
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }
  label{
    display: block;
    height:50px;
    line-height: 50px;
    margin: 10px;
  input{
    height: 40px;
    margin-left: 10px;
    padding: 10px;
    width: 35%;
  }
  }

  .submit {
    width: 350px;
    margin-left: 20px;
    margin-top: 20px;
  }
</style>
