<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">产码记录</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">产品类型</span>
                <el-select v-model="sysType" size="small" clearable placeholder="请选择">
                  <el-option v-for="item in sysTypes"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">二维码类型</span>
                <el-select v-model="qrcodeType" size="small" clearable placeholder="请选择">
                  <el-option v-for="item in qrcodeTypes"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
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
                <el-table-column prop="firstProxyName" label="上级代理"></el-table-column>
                <el-table-column label="省市">
                  <template scope="scope">
                    {{scope.row.belongProvinceName}}{{scope.row.belongCityName}}
                  </template>
                </el-table-column>
                <el-table-column label="注册时间" width="180">
                  <template scope="scope">
                    {{ scope.row.createTime | datetime }}
                  </template>
                </el-table-column>
                <el-table-column prop="mobile" label="联系手机号"></el-table-column>
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
    name: 'app',
    data() {
      return {
        sysType: '',
        sysTypes: [
          {
            value: 'hss',
            label: '好收收'
          },
          {
            value: 'hsy',
            label: '好收银'
          }
        ],
        qrcodeType: '',
        qrcodeTypes: [
          {
            value: '1',
            label: '实体码'
          },
          {
            value: '2',
            label: '电子嘛'
          }
        ],
        total: 0,
        pageSize: 20,
        pageNo: 1,
        tableData: [],
        tableLoading: false,
        districtCode: ''
      }
    },
    beforeRouteEnter (to, from, next){
      store.dispatch('actions_users_getInfo').then(function (data) {
        next((vm) => {
          if (data.status === 1) {
            vm.canAdd = (data.dealerLeavel == 1);
          }
        });
      });
    },
    created() {
      this.getData();
    },
    methods: {
      getData: function () {
        this.tableLoading = true;
        this.$http.post('/daili/qrCode/productionList', {
          pageSize: this.pageSize,
          pageNo: this.pageNo,
          sysType: this.sysType,
          qrType: this.qrcodeType
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
      screen: function () {
        this.getData();
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
