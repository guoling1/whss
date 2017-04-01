<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">对账异常记录</h3>
        </div>
        <div class="box-body">
          <ul>
            <li class="same">
              <label>交易流水:</label>
              <el-input v-model="query.orderSN" placeholder="请输入内容" size="small" style="width: 220px"></el-input>
            </li>
            <li class="same">
              <label>对账渠道:</label>
              <el-input v-model="query.channelName" placeholder="请输入内容" size="small" style="width: 220px"></el-input>
            </li>
            <li class="same">
              <label>单边方向:</label>
              <el-select clearable v-model="query.side" size="small" style="width: 220px">
                <el-option v-for="item in item_side" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </li>
            <li class="same">
              <label>对账日期:</label>
              <el-date-picker
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions2" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <label>交易类型:</label>
              <el-select clearable v-model="query.tradeType" size="small" style="width: 220px">
                <el-option label="全部" value=""></el-option>
                <el-option label="交易" value="1"></el-option>
                <el-option label="提现" value="3"></el-option>
              </el-select>
            </li>
            <li class="same">
              <label>处理结果:</label>
              <el-select clearable v-model="query.status" size="small" style="width: 220px">
                <el-option v-for="item in item_status" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </li>
            <li class="same">
              <label>对账单号:</label>
              <el-input v-model="query.no" placeholder="请输入内容" size="small" style="width: 220px"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="$records" border>
            <el-table-column type="index" width="62" label="序号" fixed="left"></el-table-column>
            <el-table-column prop="channelName" label="对账渠道" ></el-table-column>
            <el-table-column label="交易类型" >
              <template scope="scope">
                <span v-if="scope.row.tradeType==1">交易</span>
                <span v-if="scope.row.tradeType==3">提现</span>
              </template>
            </el-table-column>
            <el-table-column prop="tradeDate" label="交易时间" ></el-table-column>
            <el-table-column label="单边方向">
              <template scope="scope">
                <span v-if="scope.row.side=='self'">我方单边</span>
                <span v-if="scope.row.side=='other'">对方单边</span>
              </template>
            </el-table-column>
            <el-table-column prop="orderSN" label="交易流水" ></el-table-column>
            <el-table-column label="交易金额" align="right">
              <template scope="scope">
                {{scope.row.tradeAmount|toFix}}
              </template>
            </el-table-column>
            <el-table-column label="处理结果" >
              <template scope="scope">
                <span v-if="scope.row.status==1">未处理</span>
                <span v-if="scope.row.status==2">系统已处理</span>
                <span v-if="scope.row.status==3">无需处理</span>
                <span v-if="scope.row.status==4">已线下处理</span>
              </template>
            </el-table-column>
            <el-table-column prop="handleReason" label="处理原因" ></el-table-column>
            <el-table-column label="操作" width="70">
              <template scope="scope">
                <el-button @click="change(scope.row.id)" type="text" size="small" v-if="scope.row.status==1">处理</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.currentPage"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
        </div>
        <el-dialog title="修改商户归属信息" v-model="isShow">
          <el-form :label-position="right" label-width="150px">
            <el-form-item label="备注信息：" width="120" style="margin-bottom: 0">
              <el-input style="width: 70%"
                type="textarea"
                :rows="2"
                placeholder="非必填"
                v-model="handleQuery.handleReason">
              </el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer" style="text-align: center">
            <el-button type="primary" @click="handle" style="position: relative;top: -20px;">处 理</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'tAuditStore',
    data(){
      return{
        pickerOptions: {},
        records:[],
        count:0,
        date:'',
        query:{
          currentPage:1,
          pageSize:10,
          orderSN:'',
          side:'',
          status:'',
          channelName:'',
          tradeType:'',
          startDateStr:'',
          endDateStr:''
        },
        handleQuery:{
          handleReason:'',
          id:""
        },
        item_side:[
          {label:'全部', value:''},
          {label:'我方单边', value:'self'},
          {label:'对方单边', value:'other'}
        ],
        item_status:[
          {label:'全部', value:''},
          {label:'未处理', value:'1'},
          {label:'系统已处理', value:'2'},
          {label:'无需处理', value:'3'},
          {label:'已线下处理', value:'4'}
        ],
        loading:true,
        isShow:false,
        url:'http://192.168.1.99:8080/balance/external/detailList',
        handleUrl:'http://192.168.1.99:8080/balance/external/updateDetailReason'
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post(this.url,this.query)
          .then(function (res) {
            this.records = res.data.list;
            this.count = res.data.page.totalRecord;
            this.loading = false;
          },function (err) {
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      search: function () {
        this.$data.query.currentPage = 1;
        this.getData()
      },
      change:function(id){
        this.isShow = true;
        this.handleQuery.id=id;
      },
      handle: function () {
         this.$http.post(this.handleUrl,this.handleQuery)
           .then(res => {
             this.isShow = false;
             this.getData()
           })
           .catch(err =>{
             this.$message({
               showClose: true,
               message: err.statusMessage,
               type: 'error'
             })
           })
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.currentPage = 1;
        this.$data.query.pageSize = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.currentPage = val;
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
              this.$data.query.startDateStr = str;
            } else {
              this.$data.query.endDateStr = str;
            }
          }
        } else {
          this.$data.query.startDateStr = '';
          this.$data.query.endDateStr = '';
        }
      }
    },
    computed:{
      $records:function () {
        return this.records;
      }
    }
  }
</script>
<style scoped lang="less">
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }
</style>
