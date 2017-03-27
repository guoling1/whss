<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box tableTop">
            <div class="box-header with-border" style="margin-bottom: 20px">
              <h3 class="box-title" v-if="isShow">新增员工</h3>
              <h3 class="box-title" v-if="!isShow">员工详情</h3>
            </div>
            <!--<div class="">
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
                      <el-button v-if="!isShow" type="text" @click="dialogFormVisible = true" style="margin-left: 15px">修改密码</el-button>
                    </div>
                  </el-col>
                </el-row>
                &lt;!&ndash;修改密码&ndash;&gt;
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
                      <el-upload
                        class="upload-demo"
                        action="/upload/picUpload"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :on-success="handleSuccess"
                        :file-list="fileList"
                        list-type="picture" style="margin-bottom: 10px">
                        <el-button v-if="hasButton" style="float: left" size="small" type="primary">点击上传</el-button>
                      </el-upload>
                      <div v-if="hasButton" style="position: absolute;top: 28px;margin-left:0;width: 200px;height: 30px;background: #fbfdff;"></div>
                      <div  id="btn" style="position: absolute;top: 0;margin-left:68px;width: 200px;height: 30px;background: #fbfdff"></div>
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
                      <el-upload
                        class="upload-demo"
                        action="/upload/picUpload"
                        :on-preview="handlePreview"
                        :on-success="handleSuccess1"
                        :on-remove="handleRemove1"
                        :file-list="fileList1"
                        list-type="picture" style="margin-bottom: 10px">
                        <el-button v-if="hasButton1" style="float: left" size="small" type="primary">点击上传</el-button>
                      </el-upload>
                      <div v-if="hasButton1" style="position: absolute;top: 28px;margin-left:0;width: 200px;height: 30px;background: #fbfdff"></div>
                      <div id="btn1" style="position: absolute;top: 0;margin-left:68px;width: 200px;height: 30px;background: #fbfdff"></div>
                      &lt;!&ndash;<div
                        style="position: absolute;top: 127px;margin-left:1px;width: 200px;height: 30px;background: #fbfdff"></div>
                      <div
                        style="position: absolute;top: 3px;margin-left:1px;width: 200px;height: 30px;background: #fbfdff"></div>&ndash;&gt;
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
                        <el-option v-for="item in role" :label="item.roleName" :value="item.id">{{item.roleName}}</el-option>
                      </el-select>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="grid-content bg-purple-light" style="margin: 0 15px;">
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>-->
            <div style="width: 50%">
              <el-form ref="form" :model="query" :rules="rules" width="30%" label-width="150px" class="demo-ruleForm">
              <el-form-item label="登录名" prop="username">
                <el-input v-model="query.username" size="small"></el-input>
              </el-form-item>
              <el-form-item label="登录密码" prop="password">
                <el-input type="password" size="small" v-model="query.password" v-if="isShow"></el-input>
                <el-input type="password" size="small" value="******" v-if="!isShow" :disabled="true"></el-input>
                <el-button v-if="!isShow" type="text" @click="dialogFormVisible = true" style="margin-left: 15px;position: absolute;top: 0;left: 100%;">修改密码</el-button>
              </el-form-item>
              <el-form-item label="真实姓名" prop="realname">
                <el-input v-model="query.realname" size="small"></el-input>
              </el-form-item>
              <el-form-item label="身份证号" prop="idCard">
                <el-input v-model="query.idCard" size="small"></el-input>
              </el-form-item>
              <el-form-item label="身份证正面照" prop="identityFacePic">
                <div class="grid-content bg-purple-light">
                  <el-upload
                    class="upload-demo"
                    action="/upload/picUpload"
                    :on-preview="handlePreview"
                    :on-remove="handleRemove"
                    :on-success="handleSuccess"
                    :file-list="fileList"
                    list-type="picture" style="margin-bottom: 10px">
                    <el-button v-if="hasButton" style="float: left" size="small" type="primary">点击上传</el-button>
                  </el-upload>
                  <div v-if="hasButton" style="position: absolute;top: 28px;margin-left:0;width: 200px;height: 30px;background: #fbfdff;"></div>
                  <div  id="btn" style="position: absolute;top: 0;margin-left:68px;width: 200px;height: 30px;background: #fbfdff"></div>
                </div>
              </el-form-item>
              <el-form-item label="身份证背面照" prop="identityOppositePic">
                <div class="grid-content bg-purple-light" id="phone1">
                  <el-upload
                    class="upload-demo"
                    action="/upload/picUpload"
                    :on-preview="handlePreview"
                    :on-success="handleSuccess1"
                    :on-remove="handleRemove1"
                    :file-list="fileList1"
                    list-type="picture" style="margin-bottom: 10px">
                    <el-button v-if="hasButton1" style="float: left" size="small" type="primary">点击上传</el-button>
                  </el-upload>
                  <div v-if="hasButton1" style="position: absolute;top: 28px;margin-left:0;width: 200px;height: 30px;background: #fbfdff"></div>
                  <div id="btn1" style="position: absolute;top: 0;margin-left:68px;width: 200px;height: 30px;background: #fbfdff"></div>
                </div>
              </el-form-item>
              <el-form-item label="手机号" prop="mobile">
                <el-input v-model="query.mobile" size="small"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="query.email" size="small"></el-input>
              </el-form-item>
              <el-form-item label="角色" prop="roleId">
                <el-select style="width: 100%" v-model="query.roleId" clearable placeholder="请选择" size="small">
                  <el-option v-for="item in role" :label="item.roleName" :value="item.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button @click="goBack" style="margin-bottom: 25px;">返回</el-button>
                <el-button type="primary" @click="create" v-if="isShow">创建员工</el-button>
                <el-button type="primary" @click="upDate" v-if="!isShow">修改</el-button>
              </el-form-item>
            </el-form>
            </div>
            <el-dialog title="修改密码" v-model="dialogFormVisible">
              <el-form>
                <el-form-item label="新密码" width="120">
                  <el-input type="password" v-model="password" auto-complete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="resetPw">确 定</el-button>
              </div>
            </el-dialog>
            <div class="mask" id="mask" v-show="isMask" @click="isMask = false">
              <p @click="isMask = false">×</p>
              <img src="" alt="">
            </div>
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
    </section>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'personnelAdd',
    data () {
      return {
        rules: {
          username: [
            {required: true, message: '请输入员工登录名', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入员工登录密码', trigger: 'blur'}
          ],
          realname: [
            {required: true, message: '请输入员工真实姓名', trigger: 'blur'}
          ],
          idCard: [
            {required: true, message: '请输入身份证号', trigger: 'blur'}
          ],
          identityFacePic: [
            {required: true, message: '请上传身份证正面照', trigger: 'blur'}
          ],
          identityOppositePic: [
            {required: true, message: '请上传身份证背面照', trigger: 'blur'}
          ],
          mobile: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {pattern: /^1(3|4|5|7|8)\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'}
          ],
          email: [
            {required: true, message: '请输入邮箱', trigger: 'blur'},
            {type: 'email', message: '请输入正确的邮箱', trigger: 'blur'}
          ],
          roleId: [
            {required: true, message: '请选择角色', trigger: 'blur'}
          ],
        },
        role:[],
        dept:[],
        dialogFormVisible: false,
        password: '',
        query: {
          username: "",
          password: "",
          deptId:"",
          companyId:"",
          realname:"",
          idCard: "",
          identityFacePic: "",
          identityOppositePic: "",
          mobile: "",
          email:"",
          roleId: ""
        },
        roleName:'',
        id: 0,
        isShow: true,
        isMask: false,
        productId: '',
        fileList: [],
        fileList1: [],
        hasButton:true,
        hasButton1:true,
      }
    },
    created: function () {
      this.$http.post('/daili/privilege/userRoleList').then((res)=>{
        this.role = res.data;
        for(var i=0;i<this.role.length;i++){
          this.role[i].id = String(this.role[i].id)
        }
      });
      //若为查看详情
      if (this.$route.query.id != undefined) {
        this.$data.isShow = false;
        this.query.password = "******";
        this.$http.get('/daili/privilege/userDetail/' + this.$route.query.id)
          .then(function (res) {
            this.query = res.data;
            this.query.roleId = String(this.query.roleId)
            for(var i=0;i<this.role.length;i++){
                if(this.query.roleId==this.role[i].id){
                    this.roleName = this.role[i].roleName;
                }
            }
            if (res.data.realIdentityFacePic != null) {
              this.hasButton = false;
              document.getElementById('btn').style.marginLeft=0;
              this.fileList.push({
                url: res.data.realIdentityFacePic
              });
            }
            if (res.data.realIdentityOppositePic != null) {
              this.hasButton1 = false;
              document.getElementById('btn1').style.marginLeft=0;
              this.fileList1.push({
                url: res.data.realIdentityOppositePic
              })
            }
          })
      }
    },
    methods: {
      //传成功
      handleSuccess: function (response, file, fileList) {
        this.query.identityFacePic = response.result.url;
        this.hasButton = false;
        document.getElementById('btn').style.marginLeft=0;
      },
      handleSuccess1: function (response, file, fileList) {
        this.query.identityOppositePic = response.result.url;
        this.hasButton1 = false;
        document.getElementById('btn1').style.marginLeft=0;
      },
      handleRemove: function () {
        this.query.identityFacePic = '';
        this.hasButton = true;
        document.getElementById('btn').style.marginLeft='68px';
      },
      handleRemove1: function () {
        this.query.identityOppositePic = '';
        this.hasButton1 = true;
        document.getElementById('btn1').style.marginLeft='68px';
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
          this.$http.post('/daili/privilege/updatePwd', {id: this.$route.query.id, password: this.password})
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
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.$http.post('/daili/privilege/addUser', this.query).then(res => {
              this.$message({
                showClose: true,
                message: '创建成功',
                type: 'success'
              });
              this.$router.push('/daili/app/employees')
            }, err => {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              });
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });

      },
      goBack: function () {
        this.$router.push('/daili/app/employees')
      },
      //修改
      upDate: function () {
        this.query.password = "******";
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.$http.post('/daili/privilege/updateUser', this.query).then(res => {
              this.$message({
                showClose: true,
                message: '创建成功',
                type: 'success'
              });
              this.$router.push('/daili/app/employees')
            }, err => {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              });
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    },
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less" rel="stylesheet/less">
  input[type=file] {
    display: none !important;
    opacity: 0 !important;
  }
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
