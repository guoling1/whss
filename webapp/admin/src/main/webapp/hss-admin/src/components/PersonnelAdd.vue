<template lang="html">
  <div id="personnelAdd">
    <div style="margin: 15px 15px 150px;">
      <div class="box tableTop">
        <div class="box-header with-border" style="margin-bottom: 20px">
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
                  <el-input type="password" size="small" v-model="query.password" v-if="isShow"
                            placeholder="6-32位以上，数字字母混合"></el-input>
                  <el-input type="password" size="small" value="******" v-if="!isShow" :disabled="true"></el-input>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="grid-content bg-purple-light">
                  <el-button v-if="!isShow" type="text" @click="_$power(function(){dialogFormVisible = true},'boss_staff_updatepwd')" style="margin-left: 15px">修改密码</el-button>
                </div>
              </el-col>
            </el-row>
            <!--修改密码-->
            <el-dialog title="修改密码" v-model="dialogFormVisible">
              <el-form>
                <el-form-item label="新密码" width="120">
                  <el-input type="password" placeholder="6-32位以上，数字字母混合" v-model="password"
                            auto-complete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="resetPw">确 定</el-button>
              </div>
            </el-dialog>

            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="4">
                <div class="alignRight">所属分公司:</div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light">
                  <el-select style="width: 100%" v-model="query.companyId" clearable placeholder="请选择" size="small">
                    <el-option :label="com.dictName" :value="com.dictValue" v-for="com in company">{{com.dictName}}</el-option>
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
                    <el-option :label="com.dictName" :value="com.dictValue" v-for="com in dept">{{com.dictName}}</el-option>
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
              <el-col :span="6" >
                <div class="grid-content bg-purple-light" >
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
                  <el-upload id="upload" style="position: relative" action="/upload/picUpload"
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
                  <el-upload id="upload" style="position: relative" action="/upload/picUpload" type="drag"
                             :thumbnail-mode="true"
                             :on-progress="handleProgress" :on-preview="handlePreview" :on-remove="handleRemove1"
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
                    <el-option :label="item.roleName" :value="item.id" v-for="item in role">{{item.roleName}}</el-option>
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
              <!--<div class="btn btn-primary" @click="goBack" style="width: 45%;margin: 20px 0 100px;">-->
                <!--返回-->
              <!--</div>-->
              <div class="btn btn-primary" @click="create" v-if="isShow"
                   style="display: inherit;margin: 15px 0">
                创  建  员  工
              </div>
              <div class="btn btn-primary" @click="upDate" v-if="!isShow"
                   style="display: inherit;margin: 15px 0">
                修 改
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
  import Message from './Message.vue'
  export default {
    name: 'personnelAdd',
    data () {
      return {
        company:[],
        dept:[],
        role:[],
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
          roleId: ""
        },
        id: 0,
        isShow: true,
        isMask: false,
        productId: '',
        fileList: [],
        fileList1: []
      }
    },
    created: function () {
      this.$http.post('/admin/dict/selectAllByType',{dictType:"user_company"}).then((res)=>{
        this.company = res.data;
      });
      this.$http.post('/admin/dict/selectAllByType',{dictType:"user_dept"}).then((res)=>{
        this.dept = res.data;
      });
      this.$http.post('/admin/user/userRoleList').then((res)=>{
        this.role = res.data;
    });
      //若为查看详情
      if (this.$route.query.id != undefined) {
        this.$data.isShow = false;
        this.$http.get('/admin/user/userDetail/' + this.$route.query.id)
          .then(function (res) {
            this.$data.query = res.data;
            if (res.data.realIdentityFacePic != null) {
              this.fileList.push({
                url: res.data.realIdentityFacePic
              });
              setTimeout(function () {
                var aSpan = document.getElementById('phone').getElementsByTagName('span')[0];
                document.getElementsByClassName('el-draggeer__cover__btns')[0].removeChild(aSpan)
              }, 300)
            }
            if (res.data.realIdentityOppositePic != null) {
              this.fileList1.push({
                url: res.data.realIdentityOppositePic
              })
              setTimeout(function () {
                var aSpan = document.getElementById('phone1').getElementsByTagName('span')[0];
                if (document.getElementsByClassName('el-draggeer__cover__btns').length == 1) {
                  document.getElementsByClassName('el-draggeer__cover__btns')[0].removeChild(aSpan)
                } else {
                  document.getElementsByClassName('el-draggeer__cover__btns')[1].removeChild(aSpan)
                }
              }, 300)
            }
          })
      }
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
      handleRemove: function () {
        this.query.identityFacePic = ''
      },
      handleRemove1: function () {
        this.query.identityOppositePic = ''
      },
      handleErr:function (err) {
        this.$message({
          showClose: true,
          message: err.statusMessage,
          type: 'error'
        });
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
        var reg = /(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}/;
        if (!reg.test(this.password)) {
          this.$message({
            showClose: true,
            message: '请设置6-32位，数字字母混合密码',
            type: 'error'
          });
        } else {
          this.$http.post('/admin/user/updatePwd', {id: this.$route.query.id, password: this.$data.password})
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
        }
      },
      create: function () {
        var reg = /(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}/;
        if (this.query.username.length > 16 || this.query.username.length < 4) {
          this.$message({
            showClose: true,
            message: '请设置4-16位登录名',
            type: 'error'
          });
        } else if (!reg.test(this.query.password)) {
          this.$message({
            showClose: true,
            message: '请设置6-32位以上，数字字母混合密码',
            type: 'error'
          });
        } else {
          this.$http.post('/admin/user/addUser', this.query)
            .then(function (res) {
//              this.$message({
//                showClose: true,
//                message: '创建成功',
//                type: 'success'
//              });
//              this.$router.push('/admin/record/personnelList')
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: '创建成功'
              })
            }, function (err) {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              });
            })
        }

      },
      goBack: function () {
        this.$router.push('/admin/record/personnelList')
      },
      //修改
      upDate: function () {
        if (this.query.username.length > 16 || this.query.username.length < 4) {
          this.$message({
            showClose: true,
            message: '请设置4-16位登录名',
            type: 'error'
          });
        } else {
          this.$http.post('/admin/user/updateUser', this.$data.query)
            .then(function (res) {
//              this.$message({
//                showClose: true,
//                message: '修改成功',
//                type: 'success'
//              });
//              this.$router.push('/admin/record/personnelList')
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: '修改成功'
              })
            }, function (err) {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              });
            })
        }
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
