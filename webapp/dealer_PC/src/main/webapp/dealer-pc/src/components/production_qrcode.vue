<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-13">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">生产二维码</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-form ref="form" :model="form" :rules="rules" label-width="80px" class="demo-ruleForm">
                <el-form-item label="产品">
                  <el-radio-group v-model="label.sysType">
                    <el-radio :label="0">好收收</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="类型">
                  <el-radio-group v-model="form.type">
                    <el-radio :label="1">实体码</el-radio>
                    <el-radio :label="2">电子码</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="数量" prop="count">
                  <el-input v-model="form.count" size="small"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="small" @click="onSubmit">立即生产</el-button>
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
        dialogInfo: {},
        label: {
          sysType: 0
        },
        form: {
          sysType: 'hss',
          type: 1,
          count: ''
        },
        rules: {
          count: [
            {required: true, message: '请输入生产数量', trigger: 'blur'},
            {pattern: /^[0-9]*[1-9][0-9]*$/, message: '必须输入正整数', trigger: 'blur'}
          ],
        }
      }
    },
    methods: {
      onSubmit: function () {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.$http.post('/daili/qrCode/productionQrCode', this.form).then(res => {
              this.$message({
                showClose: true,
                message: '二维码生成成功',
                type: 'success'
              });
              this.$router.push({path: '/daili/app/distribution_qrcode_success', query: res.data.productionQrCodeRecord})
            }, err => {
              this.$message({
                showClose: true,
                message: err.data.msg,
                type: 'error'
              });
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
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
