<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-13">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">二维码详情</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-form ref="form" :model="form" label-width="100px" class="demo-ruleForm">
                <el-form-item label="产品">
                  {{productName}}
                </el-form-item>
                <el-form-item label="二维码号">
                  {{code}}
                </el-form-item>
                <el-form-item label="二维码">
                  <div id="qrCode"></div>
                  <div>点击按钮或右键另存二维码保存</div>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="small" @click="download">下载</el-button>
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
  import store from '../store'
  export default {
    data () {
      return {
        code: '',
        productName: '',
        qrUrl: ''
      }
    },
    mounted (){
      let query = this.$route.query;
      this.$http.post('/daili/qrCode/qrCodeDetail', {
        code: query.code,
      }).then(res => {
        this.code = res.data.code;
        this.productName = res.data.productName;
        this.qrUrl = res.data.qrUrl;
        new QRCode(document.getElementById('qrCode'), {
          text: res.data.qrUrl,
          width: 180,
          height: 180,
          colorDark: "#000000",
          colorLight: "#ffffff",
          correctLevel: QRCode.CorrectLevel.L
        });
      }, err => {
        this.$message({
          showClose: true,
          message: err.data.msg,
          type: 'error'
        });
      });
    },
    methods: {
      download: function () {
        this.$http.post('/daili/qrCode/downLoadQrCode', {
          code: this.code,
        }).then(res => {
          let links = document.createElement("a");
          document.body.appendChild(links);
          links.style = "display: none";
          links.href = res.data.url;
          links.download = '123.jpg';
          links.click();
          document.body.removeChild(links);
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
  .top {
    margin-top: 200px;
  }

  .bottom {
    margin-bottom: 200px;
  }
</style>
