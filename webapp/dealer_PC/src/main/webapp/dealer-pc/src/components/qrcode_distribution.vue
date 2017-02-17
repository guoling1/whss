<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">二维码分配记录</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">代理商编号</span>
                <el-input v-model="markCode" placeholder="代理商编号" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">代理商名称</span>
                <el-input v-model="name" placeholder="代理商名称" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title"></span>
                <el-button type="primary" size="small" @click="screen">筛选</el-button>
              </div>
            </div>
            <div class="box-body">
              <el-table :data="tableData" border>
                <el-table-column type="index" label="序号"></el-table-column>
                <el-table-column label="分配时间" sortable>
                  <template scope="scope">
                    {{ scope.row.distributeTime | datetime }}
                  </template>
                </el-table-column>
                <el-table-column prop="proxyName" label="代理商名称"></el-table-column>
                <el-table-column prop="markCode" label="代理商编号"></el-table-column>
                <el-table-column prop="count" label="分配个数"></el-table-column>
                <el-table-column prop="startCode" label="起始码"></el-table-column>
                <el-table-column prop="endCode" label="终止码"></el-table-column>
                <el-table-column prop="type" label="类型">
                  <template scope="scope">
                    {{ scope.row.type | filter_qrcodeType }}
                  </template>
                </el-table-column>
                <el-table-column prop="operateUser" label="操作人"></el-table-column>
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
        tableData: [],
        markCode: '',
        name: ''
      }
    },
    created() {
      this.getData();
    },
    methods: {
      screen: function () {
        this.getData();
      },
      getData: function () {
        this.$http.post('/daili/qrCode/distributeRecord', {
          pageSize: this.pageSize,
          pageNo: this.pageNo,
          markCode: this.markCode,
          name: this.name
        }).then(res => {
          this.total = res.data.count;
          this.tableData = res.data.records;
        }, err => {
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
