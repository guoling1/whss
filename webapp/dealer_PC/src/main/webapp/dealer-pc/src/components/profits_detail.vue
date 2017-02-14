<template lang="html">
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        钱包++代理商系统
        <small>Version 1.0</small>
      </h1>
      <ol class="breadcrumb">
        <li>
          <router-link to="/app/home"><i class="glyphicon glyphicon-home"></i> 主页</router-link>
        </li>
        <!--<li class="active">Dashboard</li>-->
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">分润详情</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">交易订单号</span>
                <el-input v-model="input3" placeholder="交易订单号" size="small" style="width:240px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">分润方名称</span>
                <el-input v-model="input4" placeholder="分润方名称" size="small" style="width:180px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">分润日期</span>
                <el-date-picker
                        size="small"
                        v-model="value7"
                        type="daterange"
                        align="right"
                        placeholder="选择日期范围"
                        :picker-options="pickerOptions2">
                </el-date-picker>
              </div>
              <div class="screen-item">
                <span class="screen-title"></span>
                <el-button type="primary" size="small">筛选</el-button>
              </div>
            </div>
            <div class="box-body">
              <el-table :data="tableData" border>
                <el-table-column prop="id" label="代理商名称"></el-table-column>
                <el-table-column prop="author" label="代理商编号" sortable="custom"></el-table-column>
                <el-table-column prop="type" label="省市"></el-table-column>
                <el-table-column label="注册时间" width="180">
                  <template scope="scope">
                    {{ scope.row.create_time | datetime }}
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="联系手机号"></el-table-column>
                <el-table-column label="产品">
                  <el-table-column prop="name" label="好收收" width="120"></el-table-column>
                  <el-table-column prop="name" label="好收银" width="120"></el-table-column>
                </el-table-column>
              </el-table>
            </div>
            <div class="box-body">
              <el-pagination style="float:right"
                             @size-change="handleSizeChange"
                             @current-change="handleCurrentChange"
                             :current-page="currentPage4"
                             :page-sizes="[20, 100, 200, 500]"
                             :page-size="20"
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
        total: 420,
        tableData: [],
        input3: '',
        input4: '',
        value7: '',
        pickerOptions2: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        }
      }
    },
    created() {
//      this.$http.post('/api/getFileList', {}).then(res => {
//        if (res.data.status == 0) {
//          this.tableData = res.data.result;
//        }
//      }, err => {
//        console.log(err);
//      });
    },
    methods: {
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
      },
      handleCurrentChange(val) {
        this.currentPage = val;
        console.log(`当前页: ${val}`);
      }
    }
  }
</script>
<style lang="less">
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
