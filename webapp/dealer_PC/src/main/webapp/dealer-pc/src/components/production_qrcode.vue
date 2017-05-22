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
              <el-form ref="form" :model="form" label-width="80px" class="demo-ruleForm">
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
                <el-form-item label="数量">
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
        label: {
          sysType: 0
        },
        form: {
          sysType: 'hss',
          type: 1,
          count: ''
        }
      }
    },
    methods: {
      onSubmit: function () {
        this.$http.post('/daili/qrCode/productionQrCode', this.form).then(res => {
          console.log(res);
        }, err => {
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
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
