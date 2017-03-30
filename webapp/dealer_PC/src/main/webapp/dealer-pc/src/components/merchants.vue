<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">所有商户</h3>
            </div>
            <!-- tebs 切换 -->
            <div class="box-body">
              <el-tabs v-model="activeName2" type="card" @tab-click="handleClick">
                <el-tab-pane label="好收收" name="first">
                  <div class="box-body screen-top">
                    <div class="screen-item">
                      <span class="screen-title">商户编号</span>
                      <el-input v-model="markCode" placeholder="商户编号" size="small" style="width:240px"></el-input>
                    </div>
                    <div class="screen-item">
                      <span class="screen-title">商户名称</span>
                      <el-input v-model="merchantName" placeholder="商户名称" size="small" style="width:240px"></el-input>
                    </div>
                    <div class="screen-item">
                      <span class="screen-title"></span>
                      <el-button type="primary" size="small" @click="screen">筛选</el-button>
                    </div>
                  </div>
                  <div class="box-body">
                    <el-table :data="tableData" border
                              v-loading="tableLoading"
                              element-loading-text="数据加载中">
                      <el-table-column prop="markCode" label="商户编号"></el-table-column>
                      <el-table-column prop="merchantName" label="商户名称"></el-table-column>
                      <el-table-column prop="proxyName" label="所属一级"></el-table-column>
                      <el-table-column prop="proxyName1" label="所属二级"></el-table-column>
                      <el-table-column label="注册时间">
                        <template scope="scope">
                          {{ scope.row.createTime | datetime }}
                        </template>
                      </el-table-column>
                      <el-table-column prop="mobile" label="注册手机号"></el-table-column>
                      <el-table-column prop="registered" label="注册方式"></el-table-column>
                      <el-table-column label="认证时间">
                        <template scope="scope">
                          {{ scope.row.authenticationTime | datetime }}
                        </template>
                      </el-table-column>
                      <el-table-column prop="statusValue" label="商户状态"></el-table-column>
                    </el-table>
                  </div>
                  <div class="box-body">
                    <el-pagination style="float:right"
                                   @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="pageNo"
                                   :page-sizes="[20, 100, 200, 500]"
                                   :page-size="pageSize"
                                   layout="total, sizes, prev, pager, next, jumper"
                                   :total="total">
                    </el-pagination>
                  </div>
                </el-tab-pane>
                <el-tab-pane label="好收银" name="second">好收银</el-tab-pane>
              </el-tabs>
            </div>
            <!-- /.box-header -->

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
        total: 0,
        pageSize: 20,
        pageNo: 1,
        tableData: [],
        tableLoading: false,
        markCode: '',
        merchantName: '',
      }
    },
    created() {
      this.getData();
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      },
      screen: function () {
        this.getData();
      },
      getData: function () {
        this.tableLoading = true;
        this.$http.post('/daili/queryMerchant/dealerMerchantList', {
          pageSize: this.pageSize,
          pageNo: this.pageNo,
          markCode: this.markCode,
          merchantName: this.merchantName
        }).then(res => {
          this.tableLoading = false;
          this.total = res.data.count;
          this.tableData = res.data.records;
        }, err => {
          this.tableLoading = false;
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        });
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.getData();
      },
      handleCurrentChange(val) {
        this.pageNo = val;
        this.getData();
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