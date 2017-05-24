<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">代理政策</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">
                <el-tab-pane label="好收收" name="first">
                  <div class="box-header with-border">
                    <h3 class="box-title">收款及提现结算价</h3>
                  </div>
                  <div class="box-body">
                    <el-table :data="tableData" border
                              v-loading="tableLoading"
                              element-loading-text="数据加载中">
                      <el-table-column label="注册时间" width="180">
                        <template scope="scope">
                          {{ scope.row.createTime | datetime }}
                        </template>
                      </el-table-column>
                      <el-table-column prop="mobile" label="联系手机号"></el-table-column>
                    </el-table>
                  </div>
                  <div class="box-header with-border">
                    <h3 class="box-title">合伙人推荐分润设置</h3>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="好收银" name="second">好收银</el-tab-pane>
              </el-tabs>
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
      return {}
    },
    created() {
      this.getData();
    },
    methods: {
      getData: function () {
        this.tableLoading = true;
        this.$http.post('/daili/dealer/dealerPolicy', {
          sysType: 'hss'
        }).then(res => {
          console.log(res);
        }, err => {
          this.tableLoading = false;
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
  .screen-top {
    padding-top: 0 !important;
  }

  .screen-item {
    float: left;
    margin-right: 10px;
  }

  .screen-title {
    display: block;
    height: 24px;
    line-height: 24px;
  }
</style>
