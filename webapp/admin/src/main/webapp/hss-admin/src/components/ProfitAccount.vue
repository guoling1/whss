<template>
  <div id="profitAccount">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">公司账户</h3>
        </div>
        <div class="box-body">
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="100" label="序号"></el-table-column>
            <el-table-column prop="merchantNo" label="账户名称"></el-table-column>
            <el-table-column prop="merchantName" label="账户总额（元）"></el-table-column>
            <el-table-column prop="dealerNo" label="待结算金额（元）"></el-table-column>
            <el-table-column prop="dealerName" label="可用余额（元）"></el-table-column>
            <el-table-column label="操作" width="70">
              <template scope="scope">
                <router-link to="/admin/record/profitAccountDet" type="text" size="small">明细
                </router-link>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="total">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'profitAccount',
    data(){
      return {
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
        index: '',
      }
    },
    created: function () {
      /*this.$http.post('/admin/settle/list', this.$data.query)
        .then(function (res) {
          this.$data.records = res.data.records;
          this.$data.count = res.data.count;
          this.$data.total = res.data.totalPage;
          this.$data.loading = false;
          var changeTime = function (val) {
            if (val == '' || val == null) {
              return ''
            } else {
              val = new Date(val)
              var year = val.getFullYear();
              var month = val.getMonth() + 1;
              var date = val.getDate();

              function tod(a) {
                if (a < 10) {
                  a = "0" + a
                }
                return a;
              }

              return year + "-" + tod(month) + "-" + tod(date);
            }
          }
          for (let i = 0; i < this.$data.records.length; i++) {
            this.$data.records[i].tradeDate = changeTime(this.$data.records[i].tradeDate)
          }
        }, function (err) {
          this.$data.loading = false;
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })*/
    },
    methods: {
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.loading = true;
        this.$http.post('/admin/settle/list', this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            var changeTime = function (val) {
              if (val == '' || val == null) {
                return ''
              } else {
                val = new Date(val)
                var year = val.getFullYear();
                var month = val.getMonth() + 1;
                var date = val.getDate();

                function tod(a) {
                  if (a < 10) {
                    a = "0" + a
                  }
                  return a;
                }

                return year + "-" + tod(month) + "-" + tod(date);
              }
            }
            for (let i = 0; i < this.$data.records.length; i++) {
              this.$data.records[i].tradeDate = changeTime(this.$data.records[i].tradeDate)
            }
          }, function (err) {
            this.$data.loading = false;
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      },
    },
  }
</script>
<style scoped lang="less">
  body {
    background-color: #ff0000;
  }
</style>
