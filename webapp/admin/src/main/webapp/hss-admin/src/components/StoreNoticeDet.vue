<!--<template>
  <div id="StoreNoticeDet">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">发布消息</h3>
        </div>
        <div class="box-body">
          <textarea id="textarea1">
            <p>请输入内容...</p>
          </textarea>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name: 'deal',
    data(){
      return {
        query: {
          pageNo: 1,
          pageSize: 10,
          startTime: '',
          endTime: '',
        },
        date: '',
        records: [],
        count: 0,
        total: 0,
        loading: true,
        url: ''
      }
    },
    created: function () {
      // 获取元素
      var textarea = document.getElementById('textarea1');
      // 生成编辑器
      var editor = new wangEditor(textarea);
      editor.create();
    },
    methods: {},
  }
</script>

<style scoped lang="less" rel="stylesheet/less">


</style>-->
<template lang="html">
  <div id="StoreNoticeDet">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">发布消息</h3>
        </div>
        <div class="box-body">
          <el-row type="flex" class="row-bg" justify="left">
            <el-col :span="2">
              <div class="alignRight" style="line-height: 42px">项目类型:</div>
            </el-col>
            <el-col :span="10">
              <div class="grid-content bg-purple-light">
                <el-radio class="radio" v-model="query.productName" label="好收收">好收收</el-radio>
                <el-radio class="radio" v-model="query.productName" label="好收银">好收银</el-radio>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content bg-purple-light" style="margin: 0 15px;">
              </div>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="left" style="margin-bottom: 10px">
            <el-col :span="2">
              <div class="alignRight" style="line-height: 30px">产品名称:</div>
            </el-col>
            <el-col :span="10">
              <div class="grid-content bg-purple-light">
                <el-input size="small" v-model="query.title" placeholder="请输入内容"></el-input>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content bg-purple-light right"></div>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="left">
            <el-col :span="2">
              <div class="alignRight">添加通道:</div>
            </el-col>
            <el-col :span="18">
              <div class="grid-content bg-purple-light tableSel">
                <div class="">
                  <v-editor
                    :input-content="inputContent"
                    v-model="query.text"></v-editor>
                  <input type="button" class="btn btn-primary" name="name" value="立即发布" style="margin: 15px 0 100px" @click="submit">
                </div>
              </div>
            </el-col>
            <el-col :span="1"></el-col>
          </el-row>

        </div>
      </div>
    </div>
  </div>

</template>

<script>
  import Editor from './Editor'
  export default {
    data() {
      return {
        query:{
          productId:6,
          productName:"",
          productType:'',
          title:'',
          text:'',
          publisher:''
        },
        name:'',
        // input content to editor
        inputContent: '',
        // output content from editor
        outputContent: '',
      }
    },
    computed: {},
    ready() {},
    attached() {},
    methods: {
      submit() {
        console.log(this.inputContent)
        console.log(this.text)
      }
    },
    components: {
      'v-editor': Editor
    }
  }
</script>

<style lang="css">
  .alignRight{
    text-align: right;
    margin-right: 10px;
    font-weight: bold;
    font-size: 13px;
  }
</style>
