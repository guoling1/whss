<template>
  <div id="personnelList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">合作申请</h3>
          <a @click="refresh" class="pull-right btn btn-primary" style="margin-left: 15px;">刷新</a>
        </div>
        <div class="box-body">
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="userName" label="用户姓名"></el-table-column>
            <el-table-column prop="mobile" label="手机号"></el-table-column>
            <el-table-column prop="type" label="类型"></el-table-column>
            <el-table-column prop="ip" label="ip地址"></el-table-column>
            <el-table-column prop="createTimes" label="申请时间" :formatter="changeTime"></el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.pageNo"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.pageSize"
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
    name: 'personnelList',
    data(){
      return {
        query: {
          pageNo: 1,
          pageSize: 10,
          startTime: "",
          endTime: ""
        },
        records: [],
        count: 0,
        total: 0,
        loading: true,
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      refresh: function () {
        this.getData()
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/cooperationQuery/selectCooperation', this.$data.query)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.records = res.data.records;
              this.total = res.data.totalPage;
              this.count = res.data.count;
            },1000)
          }, function (err) {
            setTimeout(()=>{
              this.loading = false;
            },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      changeTime: function (row, column) {
        var val = row.createTime;
        if (val == '' || val == null) {
          return ''
        } else {
          val = new Date(val)
          var year = val.getFullYear();
          var month = val.getMonth() + 1;
          var date = val.getDate();
          var hour = val.getHours();
          var minute = val.getMinutes();
          var second = val.getSeconds();

          function tod(a) {
            if (a < 10) {
              a = "0" + a
            }
            return a;
          }

          return year + "-" + tod(month) + "-" + tod(date) + " " + tod(hour) + ":" + tod(minute) + ":" + tod(second);
        }
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
      },
    },
    watch: {},
  }
</script>
<style scoped lang="less">
  ul {
    padding: 0;
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
