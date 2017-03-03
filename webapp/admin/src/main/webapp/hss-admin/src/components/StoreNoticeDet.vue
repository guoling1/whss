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
              <div class="alignRight" style="line-height: 42px">产品:</div>
            </el-col>
            <el-col :span="10">
              <div class="grid-content bg-purple-light">
                <el-radio class="radio" v-model="query.productName" label="好收收">好收收</el-radio>
                <el-radio class="radio" v-model="query.productName" label="好收银" disabled>好收银</el-radio>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content bg-purple-light" style="margin: 0 15px;">
              </div>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="left" style="margin-bottom: 10px">
            <el-col :span="2">
              <div class="alignRight" style="line-height: 30px">标题:</div>
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
              <div class="alignRight">正文:</div>
            </el-col>
            <el-col :span="18">
              <div class="grid-content bg-purple-light tableSel">
                <div class="">
                  <v-editor
                    :input-content="inputContent"
                    v-model="query.text"></v-editor>
                  <input type="button" class="btn btn-primary" value="立即发布" v-if="!isShow" style="margin: 15px 0 100px"
                         @click="open">
                  <input type="button" class="btn btn-primary" value="修 改" v-if="isShow"
                         style="margin: 15px 30px 100px 0" @click="change">
                  <input type="button" class="btn btn-primary" value="删 除" v-if="isShow" style="margin: 15px 0 100px"
                         @click="del">
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
        isShow: false,
        query:{
          productId:6,
          productName:"好收收",
          productType:'',
          title:'',
          text:'',
        },
        name:'',
        // input content to editor
        inputContent: '',
        // output content from editor
        outputContent: '',
      }
    },
    created: function () {
      if(this.query.productName == '好收收'){
        this.query.productType = 'hss'
      }else if(this.query.productName == '好收银'){
        this.query.productType = 'hsy'
      }
      if(this.$route.query.id != undefined){
        this.isShow = true;
        this.$http.post('/admin/pushNotice/noticeDetails',{id:this.$route.query.id})
          .then(function (res) {
            this.query = res.data;
            this.inputContent = res.data.text;
          })
          .catch(function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      }
    },
    attached() {},
    methods: {
      open() {
        if(this.query.title==''||this.query.text==''){
          this.$message({
            type: 'warning',
            message: '请输入内容!'
          });
        }else {
          this.$confirm('确认发布这条消息吗?', '提醒', {
            confirmButtonText: '确认发布',
            cancelButtonText: '取消',
            type: 'info'
          }).then(() => {
            this.$http.post('/admin/pushNotice/notice',this.query)
              .then(res=>{
                this.$message({
                  type: 'success',
                  message: '发布成功!'
                });
              })
              .catch(err=>{
                this.$message({
                  showClose: true,
                  message: err.statusMessage,
                  type: 'error'
                })
              })
          })
            .catch(() => {
              this.$message({
                type: 'info',
                message: '已取消发布'
              });
            });
        }

      },
      del(){
        this.$confirm('确认删除这条消息吗?', '提醒', {
          confirmButtonText: '确认删除',
          cancelButtonText: '取消',
          type: 'info'
        }).then(() => {
          this.$http.post('/admin/pushNotice/updateNotice', {id: this.$route.id})
            .then(function (res) {
              this.$message({
                showClose: true,
                message: '删除成功',
                type: 'error'
              })
            }, function (err) {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
        })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
      },
      change(){
        this.query.id = this.$route.query.id;
        if (this.query.title == '' || this.query.text == '') {
          this.$message({
            type: 'warning',
            message: '请输入内容!'
          });
        } else {
          this.$confirm('确认修改这条消息吗?', '提醒', {
            confirmButtonText: '确认修改',
            cancelButtonText: '取消',
            type: 'info'
          }).then(() => {
            this.$http.post('/admin/pushNotice/updateNotice', this.query)
              .then(res => {
                this.$message({
                  type: 'success',
                  message: '修改成功!'
                });
              })
              .catch(err => {
                this.$message({
                  showClose: true,
                  message: err.statusMessage,
                  type: 'error'
                })
              })
          })
            .catch(() => {
              this.$message({
                type: 'info',
                message: '已取消修改'
              });
            });
        }
      },
      submit() {
        console.log(this.inputContent)
        console.log(this.query)
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
