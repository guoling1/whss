<template>
  <div id="limitList">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">银行卡限额</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>通道名称:</label>
              <el-input style="width: 130px" v-model="query.channelName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>通道编码:</label>
              <el-input style="width: 130px" v-model="query.channelCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>银行编码:</label>
              <el-input style="width: 130px" v-model="query.bankCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" height="583" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column width="62" label="序号">
              <template scope="scope">
                <div>{{scope.$index+1}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="channelName" label="通道名称"></el-table-column>
            <el-table-column prop="channelCode" label="通道编码"></el-table-column>
            <el-table-column prop="bankCode" label="银行编码"></el-table-column>
            <el-table-column prop="bankName" label="银行名称"></el-table-column>
            <el-table-column prop="cardType" label="卡类型"></el-table-column>
            <el-table-column prop="singleLimitAmount" label="单笔限额"></el-table-column>
            <el-table-column prop="dayLimitAmount" label="日累计限额"></el-table-column>
            <el-table-column prop="--" label="月累计限额"></el-table-column>
            <el-table-column label="操作" width="90">
              <template scope="scope">
                <a href="#" @click="onOff(1,scope.row.id)" v-if="scope.row.status==0">启用</a>
                <a href="#" @click="onOff(2,scope.row.id)" v-if="scope.row.status==1">禁用</a>
              </template>
            </el-table-column>
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
    name: 'limitList',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          channelName:'',
          channelCode:'',
          bankCode:'',
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
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/channel/querySupportBank',this.$data.query)
          .then(function (res) {
            this.loading = false;
            this.records = res.data.records;
            this.count = res.data.count;
          },function (err) {
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      onOff(val,id){
        this.loading = true;
        this.$http.post('/admin/channel/auditSupportBank',{id:id,operation:val})
          .then(function (res) {
            for(var i=0;i<this.records.length;i++){
              if(this.records[i].id == id){
                if(val==1){
                  this.records[i].status = '1'
                }else if(val==2){
                  this.records[i].status = '0'
                }

              }
            }
            this.loading = false;
            this.$message({
              showClose: true,
              message: '操作成功',
              type: 'success'
            });
          },function (err) {
            this.loading = false;
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
