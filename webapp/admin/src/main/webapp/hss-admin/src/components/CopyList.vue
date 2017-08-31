<template>
  <div id="limitList">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">中央文案库</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>发布日期:</label>
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围"
                              style="width: 193px" :clearable="false" @change="datetimeSelect"
                              :editable="false"></el-date-picker>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
              <div class="btn btn-primary" @click="release">发布</div>
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
            <el-table-column label="标题">
              <template scope="scope">
                <el-button type="text" @click="modification(scope.row)">{{scope.row.title}}</el-button>
              </template>
            </el-table-column>
            <el-table-column label="发布时间">
              <template scope="scope">
                {{scope.row.createTime | changeTime}}
              </template>
            </el-table-column>
            <el-table-column label="状态">
              <template scope="scope">
                {{scope.row.status | ss}}
              </template>
            </el-table-column>
            <el-table-column prop="adminName" label="发布人"></el-table-column>
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
        date: '',
        query: {
          pageNo: 1,
          pageSize: 10,
          startTime: '',
          endTime: ''
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
      modification: function (item) {
        window.open('http://admin.qianbaojiajia.com/admin/details/copyListDetail?type=2&id=' + item.id);
      },
      datetimeSelect: function (val) {
        console.log(val);
        if (val == undefined) {
          this.query.startTime = '';
          this.query.endTime = '';
        } else {
          let format = val.split(' - ');
          this.query.startTime = format[0];
          this.query.endTime = format[1];
        }
      },
      release: function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/copyListDetail?type=1');
      },
      reset: function () {
        this.date = '';
        this.query = {
          pageNo: 1,
          pageSize: 10,
          startTime: '',
          endTime: ''
        };
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/center/getList', this.query)
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
    },
    filters: {
      ss: function (v) {
        if (v == 0) {
          return '已发布'
        } else {
          return '已删除'
        }
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
