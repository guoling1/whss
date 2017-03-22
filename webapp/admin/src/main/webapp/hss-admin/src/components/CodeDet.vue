<template lang="html">
  <div id="cordDet">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
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
              <a :href="this.href" download="二维码" class="btn btn-primary">下载</a>
              <!--<el-button type="primary" @click="download">下 载</el-button>-->
            </el-form-item>
          </el-form>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
  </div>
</template>
<script lang="babel">
  import store from '../store'
  export default {
    data () {
      return {
        code: '',
        productName: '',
        qrUrl: '',
        href:''
      }
    },
    mounted (){
      let query = this.$route.query;
      this.$http.post('/admin/code/qrCodeDetail', {
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
      this.$http.post('/admin/code/downLoadQrCode', {
        code: query.code,
      }).then(res => {
        this.href = res.data.url;
      }, err => {
        this.$message({
          showClose: true,
          message: err.data.msg,
          type: 'error'
        });
      });
    },
    methods: {
      /*download: function () {
        this.$http.post('/admin/code/downLoadQrCode', {
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
      }*/
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
