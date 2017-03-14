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
              <div class="grid-content bg-purple-light" id="inp">
                <el-input size="small" v-model="query.title" placeholder="请输入内容"></el-input>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content bg-purple-light right"></div>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="left">
            <el-col :span="2">
              <div class="alignRight" style="line-height: 42px">类型:</div>
            </el-col>
            <el-col :span="10">
              <div class="grid-content bg-purple-light">
                <el-select v-model="query.type" placeholder="请选择">
                  <el-option label="维护" value="1"></el-option>
                  <el-option label="通知" value="2"></el-option>
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="grid-content bg-purple-light" style="margin: 0 15px;">
              </div>
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
                    v-model="outputContent"></v-editor>
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
          type:'',
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
            this.query.text = this.inputContent = res.data.text;
            console.log(this.inputContent)
            console.log(this.outputContent)
            console.log(this.query.text)
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
    compiled(){

      document.getElementById('inp').getElementsByTagName('input')[0].focus()
    },
    attached() {},
    methods: {
      open() {
        console.log(this.inputContent);
        console.log(this.outputContent);
        console.log(this.query.text);
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
            this.query.text = this.outputContent;
            this.$http.post('/admin/pushNotice/notice',this.query)
              .then(res=>{
                this.$message({
                  type: 'success',
                  message: '发布成功!'
                });
                this.$router.push('/admin/record/storeNotice')
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
          this.$http.post('/admin/pushNotice/deleteNotice', {id: this.$route.query.id})
            .then(function (res) {
              this.$message({
                showClose: true,
                message: '删除成功',
                type: 'error'
              })
              this.$router.push('/admin/record/storeNotice')
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
        console.log(this.inputContent);
        console.log(this.outputContent);

        this.query.text = this.outputContent;
        console.log(this.query.text);
        if(this.inputContent!=''&&this.outputContent==''){
          this.$message({
            type: 'warning',
            message: '内容无修改!'
          });
        }else if (this.query.title == '' || this.query.text == '') {
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
                  this.$router.push('/admin/record/storeNotice')
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
