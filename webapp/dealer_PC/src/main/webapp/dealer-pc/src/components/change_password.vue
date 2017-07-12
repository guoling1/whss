<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-13">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">修改密码</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-form ref="form" :model="form" :rules="rules" label-width="120px" class="demo-ruleForm">
                <el-form-item label="原密码">
                  <el-input type="password" v-model="form.pwd" size="small"></el-input>
                </el-form-item>
                <el-form-item label="新密码">
                  <el-input type="password" v-model="form.newPwd" size="small"></el-input>
                </el-form-item>
                <el-form-item label="再次输入新密码">
                  <el-input type="password" v-model="form.repeatNewPwd" size="small"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="small" @click="onSubmit">确认修改</el-button>
                </el-form-item>
              </el-form>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
  </div>
</template>
<script lang="babel">
  export default {
    name: 'app',
    data() {
      return {
        form: {
          pwd: '',
          newPwd: '',
          repeatNewPwd: ''
        }
      }
    },
    methods: {
      onSubmit: function () {
        this.$http.post('/daili/dealer/updateNewPwd', this.form).then(res => {
          this.$message({
            showClose: true,
            message: '密码修改成功',
            type: 'success'
          });
        }, err => {
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        })
      }
    }
  }
</script>
<style scoped lang="less">
  .form-label {
    margin-bottom: 15px;
  }

  .line-center {
    text-align: center;
  }
</style>
