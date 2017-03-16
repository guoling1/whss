<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">所有二维码</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <span style="font-size:14px">
                未分配个数:{{codeData.unDistributeCount}}个&nbsp;&nbsp;
                已分配个数:{{codeData.distributeCount}}个&nbsp;&nbsp;
                未激活个数:{{codeData.unActivateCount}}个&nbsp;&nbsp;
                已激活个数:{{codeData.activateCount}}个&nbsp;&nbsp;
              </span>
              <el-button type="primary" size="small" @click="withdrawal($event)">提现</el-button>
            </div>
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">二维码编号</span>
                <el-input v-model="code" placeholder="二维码编号" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">商户名称</span>
                <el-input v-model="merchantName" placeholder="商户名称" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">二级代理名称</span>
                <el-input v-model="secondDealerName" placeholder="二级代理名称" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">产品类型</span>
                <el-select v-model="businessType" size="small" placeholder="请选择">
                  <el-option v-for="item in item_businessType"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">二维码激活状态</span>
                <el-select v-model="activeType" size="small" placeholder="请选择">
                  <el-option v-for="item in item_activeType"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">商户状态</span>
                <el-select v-model="statusType" size="small" placeholder="请选择">
                  <el-option v-for="item in item_statusType"
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
                <el-table-column type="index" label="序号"></el-table-column>
                <el-table-column prop="sysTypeName" label="产品类型"></el-table-column>
                <el-table-column label="二维码编号">
                  <template scope="scope">
                    <el-button type="text" @click="checkDetail($event,scope.row.code)">{{scope.row.code}}</el-button>
                  </template>
                </el-table-column>
                <el-table-column label="分配时间" width="180">
                  <template scope="scope">
                    {{ scope.row.distributeTime | datetime }}
                  </template>
                </el-table-column>
                <el-table-column label="激活时间" width="180">
                  <template scope="scope">
                    {{ scope.row.activateTime | datetime }}
                  </template>
                </el-table-column>
                <el-table-column prop="secondDealerName" label="代理名称"></el-table-column>
                <el-table-column prop="merchantName" label="商户名称"></el-table-column>
                <el-table-column prop="merchantMarkCode" label="商户编码"></el-table-column>
                <el-table-column prop="merchantStatus" label="商户状态"></el-table-column>
                <el-table-column prop="shopName" label="店铺名称"></el-table-column>
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
  export default {
    name: 'app',
    data() {
      return {
        total: 0,
        pageSize: 20,
        pageNo: 1,
        codeData: {
          unDistributeCount: 0,
          distributeCount: 0,
          unActivateCount: 0,
          activateCount: 0
        },
        tableData: [],
        tableLoading: false,
        code: '',
        merchantName: '',
        secondDealerName: '',
        businessType: 'hss',
        item_businessType: [
          {
            value: 'hss',
            label: '好收收'
          },
          {
            value: 'hsy',
            label: '好收银'
          }
        ],
        activeType: '0',
        item_activeType: [
          {
            value: '0',
            label: '全部'
          },
          {
            value: '1',
            label: '已激活'
          },
          {
            value: '2',
            label: '未激活'
          }
        ],
        statusType: '0',
        item_statusType: [
          {
            value: '0',
            label: '全部'
          },
          {
            value: '1',
            label: '未提交资料'
          },
          {
            value: '2',
            label: '待审核'
          },
          {
            value: '4',
            label: '审核通过'
          },
          {
            value: '3',
            label: '审核不通过'
          }
        ]
      }
    },
    created() {
      this.getData();
    },
    methods: {
      checkDetail: function (event, code) {
        this.$router.push({path: '/daili/app/qrcode_detail', query: {code: code}});
      },
      screen: function () {
        this.getData();
      },
      getData: function () {
        this.tableLoading = true;
        this.$http.post('/daili/qrCode/myQrCodeList', {
          pageSize: this.pageSize,
          pageNo: this.pageNo,
          code: this.code,
          merchantName: this.merchantName,
          secondDealerName: this.secondDealerName,
          sysType: this.businessType,
          activateStatus: this.activeType,
          merchantStatus: this.statusType
        }).then(res => {
          this.tableLoading = false;
          this.codeData.unDistributeCount = res.data.unDistributeCount;
          this.codeData.distributeCount = res.data.distributeCount;
          this.codeData.unActivateCount = res.data.unActivateCount;
          this.codeData.activateCount = res.data.activateCount;
          this.total = res.data.pageModel.count;
          this.tableData = res.data.pageModel.records;
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
