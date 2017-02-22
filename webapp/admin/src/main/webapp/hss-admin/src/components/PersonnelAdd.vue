<template lang="html">
  <div id="personnelAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border">
          <h3 class="box-title" v-if="isShow">新增员工</h3>
          <h3 class="box-title" v-if="!isShow">员工详情</h3>
        </div>
        <div class="">
          <div class="table-responsive">
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">登录名:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.username" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">登录密码:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.password" placeholder="8位以上，数字字母混合"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">所属分公司:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="query.companyId" clearable placeholder="请选择" size="small">
                    <el-option label="总部" value="hss">总部</el-option>
                  </el-select>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">所属部门:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="query.deptId" clearable placeholder="请选择" size="small">
                    <el-option label="技术部" value="hss">技术部</el-option>
                    <el-option label="市场部" value="hsy">市场部</el-option>
                    <el-option label="运营部" value="hsy">运营部</el-option>
                  </el-select>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light" style="margin: 0 15px;">
                </div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">真实姓名:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.realname" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">身份证号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.idCard" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">身份证正面照:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" id="phone">
                  <el-upload id="upload" style="position: relative" action="//jsonplaceholder.typicode.com/posts/"
                             type="drag" :thumbnail-mode="true"
                             :on-preview="handlePreview"
                             :on-remove="handleRemove"
                             :on-success="handleSuccess"
                             :on-error="handleErr"
                             :default-file-list="fileList">
                    <i class="el-icon-upload"></i>
                    <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                    <!--<div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>-->
                  </el-upload>
                  <div
                    style="position: absolute;top: 127px;margin-left:1px;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div
                    style="position: absolute;top: 3px;margin-left:1px;width: 200px;height: 30px;background: #fbfdff"></div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">身份证背面照:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light" id="phone1">
                  <el-upload id="upload" style="position: relative" action="/common/picUpload" type="drag"
                             :thumbnail-mode="true"
                             :on-progress="handleProgress" :on-preview="handlePreview" :on-remove="handleRemove"
                             :on-success="handleSuccess1" :default-file-list="fileList1">
                    <i class="el-icon-upload"></i>
                    <div class="el-dragger__text">将文件拖到此处，或<em>点击上传</em></div>
                    <!--<div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>-->
                  </el-upload>
                  <div
                    style="position: absolute;top: 127px;margin-left:1px;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div
                    style="position: absolute;top: 3px;margin-left:1px;width: 200px;height: 30px;background: #fbfdff"></div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">手机号:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.mobile" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">邮箱:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-input size="small" v-model="query.email" placeholder="请输入内容"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
            </el-row>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">角色:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="query.roleId" clearable placeholder="请选择" size="small">
                    <el-option label="技术部" value="hss">技术部</el-option>
                    <el-option label="市场部" value="hsy">市场部</el-option>
                    <el-option label="运营部" value="hsy">运营部</el-option>
                  </el-select>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light" style="margin: 0 15px;">
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
        <el-row type="flex" class="row-bg" justify="center">
          <el-col :span="4">
            <div class="alignRight"></div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple-light" style="width: 100%">
              <div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">
                返回
              </div>
              <div class="btn btn-primary" @click="create" v-if="isShow"
                   style="width: 45%;float: right;margin: 20px 0 100px;">
                创建员工
              </div>
              <div class="btn btn-primary" @click="change()" v-if="!isShow"
                   style="width: 45%;float: right;margin: 20px 0 100px;">
                修改
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light"></div>
          </el-col>
        </el-row>
        <div class="mask" id="mask" v-show="isMask" @click="isMask = false">
          <p @click="isMask = false">×</p>
          <img src="" alt="">
        </div>
      </div>

    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'personnelAdd',
    data () {
      return {
        ishas: true,
        ishas1: true,
        dialogFormVisible: false,
        password: '',
        query: {
          username: "",
          password: "",
          companyId:"",
          deptId:"",
          realname:"",
          idCard: "",
          identityFacePic: "",
          identityOppositePic: "",
          mobile: "",
          email:"",
          roleId:""
        },
        id: 0,
        isShow: true,
        isMask: false,
        productId: '',
        images: [],
        fileList: [],
        fileList1: []
      }
    },
    created: function () {
      //若为查看详情
      if (this.$route.query.id != undefined) {
        this.$data.isShow = false;
        this.$http.get('/admin/dealer/findBydealerId/' + this.$route.query.id)
          .then(function (res) {
            this.$data.query = res.data;
            this.$data.province = res.data.belongProvinceName;
            this.$data.city = res.data.belongCityName;
          })
      }
      this.$data.level = this.$route.query.level;
    },
    methods: {
      //传成功
      handleSuccess: function (response, file, fileList) {
        this.query.identityFacePic = response.result.url;
        //移除继续上传按钮
        setTimeout(function () {
          var aSpan = document.getElementById('phone').getElementsByTagName('span')[0];
          document.getElementsByClassName('el-draggeer__cover__btns')[0].removeChild(aSpan)
        }, 300)
      },
      handleSuccess1: function (response, file, fileList) {
        this.query.identityOppositePic = response.result.url
        //移除继续上传按钮
        setTimeout(function () {
          var aSpan = document.getElementById('phone1').getElementsByTagName('span')[0];
          if (document.getElementsByClassName('el-draggeer__cover__btns').length == 1) {
            document.getElementsByClassName('el-draggeer__cover__btns')[0].removeChild(aSpan)
          } else {
            document.getElementsByClassName('el-draggeer__cover__btns')[1].removeChild(aSpan)
          }
        }, 300)
      },
      handleErr:function (err) {
        console.log(err)
      },
      //查看照片
      handlePreview: function (file) {
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        img.src = file.url;
        this.isMask = true
      },
      //修改密码
      resetPw: function () {
        this.$http.post('/admin/dealer/updatePwd', {dealerId: this.$route.query.id, loginPwd: this.$data.password})
          .then(function (res) {
            this.$data.dialogFormVisible = false;
            this.$data.password = '';
            this.$message({
              showClose: true,
              type: 'success',
              message: '修改成功'
            });
          })
          .catch(function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
        /*this.$prompt('请输入新密码', '修改密码', {
         confirmButtonText: '确定',
         cancelButtonText: '取消',
         }).then(({ value }) => {
         this.$http.post('/admin/dealer/updatePwd',{dealerId:this.$route.query.id,loginPwd:value})
         .then(function (res) {
         this.$message({
         showClose: true,
         type: 'success',
         message: '修改成功'
         });
         })
         .catch(function (err) {
         this.$message({
         showClose: true,
         message: err.statusMessage,
         type: 'error'
         })
         })

         }).catch(() => {
         this.$message({
         type: 'info',
         message: '取消修改'
         });
         });*/
      },
      create: function () {
        this.$http.post('/admin/user/addUser', this.query)
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '创建成功',
              type: 'success'
            });
            this.$router.push('/admin/record/personnelList')
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      goBack: function () {
        this.$router.push('/admin/record/personnelList')
      },
      //修改
      change: function () {
        this.$data.query.dealerId = this.$data.query.id;
        this.$http.post('/admin/user/addUser', this.$data.query)
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '修改成功',
              type: 'success'
            });
            this.$router.push('/admin/record/personnelList')
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      }
    },
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less" rel="stylesheet/less">
  .alignRight {
    margin-right: 15px;
    text-align: right;
    height: 30px;
    line-height: 30px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  .mask {
    background: rgba(0, 0, 0, 0.8);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    p {
      position: absolute;
      top: 20px;
      right: 20px;
      z-index: 1200;
      width: 65px;
      height: 65px;
      line-height: 55px;
      font-size: 65px;
      color: #d2d1d1;
      text-align: center;
      border: 6px solid #adaaaa;
      border-radius: 50%;
      box-shadow: 0 0 16px #000;
      text-shadow: 0 0 16px #000;
    }

    img {
      display: inherit;
      max-height: 100%;
      max-width: 100%;
      margin: 0 auto;
    }
  }
</style>
