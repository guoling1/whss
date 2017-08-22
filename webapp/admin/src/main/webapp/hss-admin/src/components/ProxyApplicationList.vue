<template>
  <div id="limitList">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">代理商申请列表</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>注册手机号:</label>
              <el-input style="width: 188px" v-model="query.mobile" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>商户编号:</label>
              <el-input style="width: 188px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" height="583" style="font-size: 12px;margin-bottom: 15px" :data="records"
                    border>
            <el-table-column width="62" label="序号">
              <template scope="scope">
                <div>{{scope.$index+1}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="mobile" label="注册手机号"></el-table-column>
            <el-table-column prop="merchantName" label="商户名称"></el-table-column>
            <el-table-column prop="markCode" label="商户编号"></el-table-column>
            <el-table-column prop="oemName" label="所属分公司"></el-table-column>
            <el-table-column prop="firstDealerName" label="一级代理"></el-table-column>
            <el-table-column prop="secondDealerName" label="二级代理"></el-table-column>
            <el-table-column prop="payTime" label="支付时间"></el-table-column>
            <el-table-column prop="payAmount" label="支付金额"></el-table-column>
            <el-table-column prop="level" label="升级等级"></el-table-column>
            <el-table-column label="操作" width="90">
              <template scope="scope">
                <el-button type="text" @click="correlate(scope.row)">关联</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.page"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.size"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name: 'limitList',
    data(){
      return {
        dialogFormVisible: false,
        form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        },
        formLabelWidth: '120px',
        query: {
          pageNo: 1,
          pageSize: 10,
          mobile: '',
          markCode: ''
        },
        records: [],
        count: 0,
        total: '',
        loading: true,
        url: ''
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      correlate: function (item) {
        window.open('http://admin.qianbaojiajia.com/admin/details/proxyApplicationCorrelate?id=' + item.id + '&level=' + item.level)
      },
      reset: function () {
        this.query = {
          pageNo: 1,
          pageSize: 10,
          mobile: '',
          markCode: ''
        };
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/dealer/appliationList', this.query)
          .then(function (res) {
            console.log(res);
            setTimeout(() => {
              this.loading = false;
              this.records = res.data.records;
            }, 1000)
            this.count = res.data.count;
          }, function (err) {
            setTimeout(() => {
              this.loading = false;
            }, 1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      search(){
        this.$data.query.pageNo = 1;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.pageNo = 1;
        this.$data.query.pageSize = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.getData()
      }
    }
  }
</script>

<style scoped lang="less" rel="stylesheet/less">
  ul {
    padding: 0;
    margin: 0;
  }

  .search {
    margin-bottom: 0;
    label {
      display: block;
      margin-bottom: 0;
    }
  }

  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }

  .btn {
    font-size: 12px;
  }
</style>
