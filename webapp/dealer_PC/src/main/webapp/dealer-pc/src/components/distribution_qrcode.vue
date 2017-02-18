<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-13">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title">分配二维码</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-form ref="form" :model="form" :rules="rules" label-width="80px" class="demo-ruleForm">
                <el-form-item label="产品">
                  <el-radio-group v-model="label.sysType" @change="sysTypeChange">
                    <el-popover placement="top" title="提示" width="200" trigger="hover" :disabled="product.proxyHss!=0"
                                style="margin-right:15px">
                      <span>一级代理商未开通该产品</span>
                      <el-radio slot="reference" :label="0" :disabled="product.proxyHss==0">好收收</el-radio>
                    </el-popover>
                    <el-popover placement="top" title="提示" width="200" trigger="hover" :disabled="product.proxyHsy!=0">
                      <span>一级代理商未开通该产品</span>
                      <el-radio slot="reference" :label="1" :disabled="product.proxyHsy==0">好收银</el-radio>
                    </el-popover>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="分配对象">
                  <el-radio-group v-model="form.isSelf" @change="isSelfChange">
                    <el-radio :label="0">下级代理</el-radio>
                    <el-radio :label="1">分配给自己</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="代理名称" v-show="label.isSelf">
                  <el-popover placement="top" title="提示" width="200" trigger="hover">
                    <span>匹配结果中不可选择的代理商表示未开通该产品</span>
                    <el-select slot="reference" v-model="form.dealerId" size="small" filterable remote style="width:100%"
                               @change="selectDealer"
                               placeholder="请输入代理商名称或手机号"
                               :remote-method="remoteMethod"
                               :loading="loading">
                      <el-option v-for="item in label.dealerData"
                                 :disabled="item.isDistribute==0"
                                 :label="item.proxyName + '(' + item.mobile + ')'"
                                 :value="item.id">
                      </el-option>
                    </el-select>
                  </el-popover>
                </el-form-item>
                <el-form-item label="代理商信息" v-show="label.isSelf">
                  <el-table :data="label.selectDealerData" style="width: 100%">
                    <el-table-column prop="proxyName" label="姓名"></el-table-column>
                    <el-table-column prop="mobile" label="手机号"></el-table-column>
                  </el-table>
                </el-form-item>
                <el-form-item label="类型">
                  <el-radio-group v-model="form.type">
                    <el-radio :label="1">实体码</el-radio>
                    <el-radio :label="2" :disabled="true">电子码</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="分配方式">
                  <el-radio-group v-model="form.distributeType">
                    <el-radio :label="1">按码段</el-radio>
                    <el-radio :label="2">按个数</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="起始码" v-show="form.distributeType==1">
                  <el-input v-model="form.startCode" size="small"></el-input>
                </el-form-item>
                <el-form-item label="终止码" v-show="form.distributeType==1">
                  <el-input v-model="form.endCode" size="small"></el-input>
                </el-form-item>
                <el-form-item label="分配数量" v-show="form.distributeType==2">
                  <el-input v-model="form.count" size="small"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" size="small" @click="onSubmit">分配二维码</el-button>
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
      this.$http.post('/daili/qrCode/proxyProduct').then(res => {
        this.product.proxyHss = res.data.proxyHss;
        this.product.proxyHsy = res.data.proxyHsy;
      }, err => {
        this.$message({
          showClose: true,
          message: err.data.msg,
          type: 'error'
        });
      })
    },
    data() {
      return {
        product: {
          proxyHss: 0,
          proxyHsy: 0
        },
        label: {
          sysType: 0,
          isSelf: true,
          dealer: '',
          dealerData: [],
          selectDealerData: [],
        },
        form: {
          sysType: 'hss',
          isSelf: 0,
          dealerId: '',
          type: 1,
          distributeType: 1,
          startCode: '',
          endCode: '',
          count: ''
        },
        rules: {
          mobile: [
            {required: true, message: '请输入手机号', trigger: 'blur'},
            {pattern: /^1(3|4|5|7|8)\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      sysTypeChange: function (v) {
        if (v == 0) {
          this.form.sysType = 'hss';
        } else if (v == 1) {
          this.form.sysType = 'hsy';
        }
      },
      isSelfChange: function (v) {
        this.label.isSelf = (v == 0);
      },
      remoteMethod(query) {
        if (query !== '') {
          this.loading = true;
          this.$http.post('/daili/qrCode/listSecondDealer', {
            condition: query,
            sysType: this.form.sysType
          }).then(res => {
            this.loading = false;
            this.label.dealerData = res.data;
          }, err => {
            this.$message({
              showClose: true,
              message: err.data.msg,
              type: 'error'
            });
          });
        } else {
          this.label.dealerDate = [];
        }
      },
      selectDealer: function (v) {
        this.label.selectDealerData = [];
        for (let i = 0; i < this.label.dealerData.length; i++) {
          if (this.label.dealerData[i].id == v) {
            this.label.selectDealerData[0] = this.label.dealerData[i];
          }
        }
      },
      onSubmit: function () {
        this.$http.post('/daili/qrCode/distributeQrCodeToDealer', this.form).then(res => {
          this.$message({
          showClose: true,
          message: '二维码分配成功成功',
          type: 'success'
        });
        this.$router.push('/daili/app/qrcode_distribution');
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
