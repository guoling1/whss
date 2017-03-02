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
          <div class="">
            <v-editor
              :input-content="inputContent"
              v-model="outputContent"></v-editor>
            <input type="button" name="name" value="submit" @click="submit">
          </div>
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
        console.log(this.outputContent)
      }
    },
    components: {
      'v-editor': Editor
    }
  }
</script>

<style lang="css">
</style>
