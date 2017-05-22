<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-13">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">详细信息</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-form ref="form" :model="form" label-width="80px" class="demo-ruleForm">
                <el-form-item label="产品名称">
                  {{form.productName}}
                </el-form-item>
                <el-form-item label="类型">
                  {{form.qrType}}
                </el-form-item>
                <el-form-item label="生产数量">
                  {{form.count}}
                </el-form-item>
                <el-form-item label="生产时间">
                  {{form.productionTime | datetime}}
                </el-form-item>
                <el-form-item label="生产号段">
                  {{form.startCode}} 至 {{form.endCode}}
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="small" @click="download">下载二维码</el-button>
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
    created(){
      let query = this.$route.query;
      this.form.productName = query.productName;
      this.form.qrType = query.qrType;
      this.form.count = query.count;
      this.form.productionTime = query.productionTime;
      this.form.startCode = query.startCode;
      this.form.endCode = query.endCode;
      this.form.downloadUrl = query.downloadUrl;
    },
    data() {
      return {
        form: {
          productName: '',
          qrType: '',
          count: 0,
          productionTime: '',
          startCode: '',
          endCode: '',
          downloadUrl: ''
        }
      }
    },
    methods: {
      download: function () {
        window.location.href = 'http://' + this.form.downloadUrl;
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
