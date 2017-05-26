<template>
  <div id="deal">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户公告</h3>
          <a @click="_$power(release,'boss_merchant_send_message')" to="/admin/record/storeNoticeDet" class="btn btn-primary" style="color: #fff;float: right;">发布消息</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>发布日期:</label>
              <el-date-picker
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" height="583" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column type="index" width="62" label="序号"></el-table-column>
             <!--:formatter="changeTime"-->
            <el-table-column prop="dates" label="发布时间"></el-table-column>
            <el-table-column label="标题" min-width="160">
              <template scope="scope">
                <router-link target="_blank" :to="{path:'/admin/details/StoreNoticeDet',query:{id:records[scope.$index].id}}"
                  type="text" size="small">
                  {{records[scope.$index].title}}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column prop="pushStatus" label="状态" min-width="90"></el-table-column>
            <el-table-column prop="publisher" label="发布人" min-width="90"></el-table-column>
          </el-table>
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
    name: 'deal',
    data(){
      return {
        pickerOptions: {
          onPick:function({ maxDate, minDate }){
            if(maxDate==''||maxDate==null){
              this.disabledDate=function(maxDate) {
                return minDate < maxDate.getTime() - 8.64e7*30||minDate.getTime() > maxDate;
              }
            }else{
              this.disabledDate=function(){}
            }
          }
        },
        query:{
          pageNo:1,
          pageSize:10,
          startTime:'',
          endTime: '',
        },
        date: '',
        records: [],
        count: 0,
        total: 0,
        loading: true,
        url: ''
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      release:function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/storeNoticeDet')
//        this.$router.push('/admin/record/storeNoticeDet')
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/pushNotice/noticeList',this.$data.query)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.$data.records = res.data.records;
          },1000)
            this.$data.total=res.data.totalPage;
            this.$data.url=res.data.ext;
            this.$data.count = res.data.count;
          },function (err) {
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
      //格式化hss创建时间
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
      },
    },
    watch: {
      date: function (val, oldVal) {
        if (val[0] != null) {
          for (var j = 0; j < val.length; j++) {
            var str = val[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for (var i = 0, len = ary.length; i < len; i++) {
              if (ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if (j == 0) {
              this.$data.query.startTime = str;
            } else {
              this.$data.query.endTime = str;
            }
          }
        } else {
          this.$data.query.startTime = '';
          this.$data.query.endTime = '';
        }
      }
    },
  }
</script>

<style scoped lang="less" rel="stylesheet/less">
  ul {
    padding: 0;
    margin:0;
  }
  .search{
    margin-bottom:0;
    label{
      display: block;
      margin-bottom: 0;
    }
  }
  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }

</style>
