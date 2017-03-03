<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户结算记录</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>结算日期:</label>
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions"></el-date-picker>
            </li>
            <li class="same">
              <label>结算单号:</label>
              <el-input style="width: 130px" v-model="query.settleNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>结算状态:</label>
              <el-select clearable v-model="query.settleStatus" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待结算" value="1">待结算</el-option>
                <el-option label="部分结算" value="4">部分结算</el-option>
                <el-option label="结算成功" value="3">结算成功</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>商户名称:</label>
              <el-input style="width: 130px" v-model="query.userName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>商户编号:</label>
              <el-input style="width: 130px" v-model="query.userNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border @selection-change="handleSelectionChange">
            <el-table-column type="index" width="62" label="序号" fixed="left"></el-table-column>
            <!--<el-table-column type="selection" width="55"></el-table-column>-->
            <el-table-column prop="userName" label="商户名称" ></el-table-column>
            <el-table-column prop="userNo" label="商户编号" ></el-table-column>
            <el-table-column label="业务线" >
              <template scope="scope">
                <span v-if="records[scope.$index].appId=='hss'">好收收</span>
                <span v-if="records[scope.$index].appId=='hsy'">好收银</span>
              </template>
            </el-table-column>
            <el-table-column prop="settleNo" label="结算单号" ></el-table-column>
            <el-table-column prop="settleDate" label="结算日期" :formatter="changeTime"></el-table-column>
            <el-table-column prop="settleAmount" label="结算金额" :formatter="changeNum" align="right"></el-table-column>
            <el-table-column prop="settleModeValue" label="结算方式" ></el-table-column>
            <el-table-column prop="settleDestinationValue" label="结算类型" ></el-table-column>
            <el-table-column prop="settleStatusValue" label="结算状态" ></el-table-column>

            <!--<el-table-column label="操作" width="70">
              <template scope="scope">
                <el-button @click.native.prevent="list(scope.$index)" type="text" size="small" v-if="records[scope.$index].settleStatusValue!='结算成功'">结算</el-button>
              </template>-->
            </el-table-column>
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
          <!--审核-->
          <div v-if="isShow">
            <el-dialog title="结算确认提醒" v-model="isShow">
              <div class="maskCon">
                <span>商户名称：</span>
                <span>{{records[this.$data.index].userName}}</span>
              </div>
              <div class="maskCon">
                <span>商户编号：</span>
                <span>{{records[index].userNo}}</span>
              </div>
              <div class="maskCon">
                <span>结算金额：</span>
                <span>{{records[index].settleAmount}}</span>
              </div>
              <div class="maskCon">
                <span>结算交易笔数：</span>
                <span>{{records[index].tradeNumber}}笔</span>
              </div>
              <div class="maskCon">
                <span>交易日期：</span>
                <span>{{records[index].tradeDate}}</span>
              </div>
              <div slot="footer" class="dialog-footer" style="text-align: center;">
                <el-button @click="isShow = false">取 消</el-button>
                <el-button @click="settle(2,records[index].id)">结算已对账部分</el-button>
                <el-button @click="settle(3,records[index].id)">强制结算全部</el-button>
              </div>
            </el-dialog>
          </div>
        </div>
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
        pickerOptions: {
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
        },
        date:'',
        records:[],
        count:0,
        total:0,
        query:{
          pageNo:1,
          pageSize:10,
          userNo:"",//编号
          userName:"",  //名字
          settleNo:"",//结算单号
          userType:2,//(2：商户，3：代理商)
          starDate:"", // 开始
          endDate:"", //结束
          settleStatus:''
        },
        multipleSelection:[],
        currentPage4: 1,
        loading:true,
        isShow:false,
        index:'',
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      changeTime: function (row, column) {
        var val = row.settleDate;
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

          return year + "-" + tod(month) + "-" + tod(date);
        }
      },
      changeNum: function (row, column) {
        var val = row.settleAmount;
        return parseFloat(val).toFixed(2);
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/settlementRecord/list',this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            var changeTime=function (val) {
              if(val==''||val==null){
                return ''
              }else {
                val = new Date(val)
                var year=val.getFullYear();
                var month=val.getMonth()+1;
                var date=val.getDate();
                function tod(a) {
                  if(a<10){
                    a = "0"+a
                  }
                  return a;
                }
                return year+"-"+tod(month)+"-"+tod(date);
              }
            }
            for(let i = 0; i < this.$data.records.length; i++){
              this.$data.records[i].tradeDate = changeTime(this.$data.records[i].tradeDate)
            }
          },function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      search: function () {
        this.$data.query.pageNo = 1;
        this.getData()
      },
      list: function (val) {
        this.$data.index = val;
        this.$data.isShow = true;
      },
      //行选中
      handleSelectionChange(val) {
        console.log(val)
        this.multipleSelection = val;
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
      //结算审核
      settle(val,id) {
        this.$http.post('/admin/settle/singleSettle',{recordId:id,option:val})
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '结算成功',
              type: 'success'
            })
            this.$data.isShow = false
          })
          .catch(function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      }
    },
    watch:{
      date:function (val,oldVal) {
        if(val[0]!=null){
          for(var j=0;j<val.length;j++){
            var str = val[j];
            var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
            for(var i = 0, len = ary.length; i < len; i ++) {
              if(ary[i] < 10) {
                ary[i] = '0' + ary[i];
              }
            }
            str = ary[0] + '-' + ary[1] + '-' + ary[2];
            if(j==0){
              this.$data.query.starDate = str;
            }else {
              this.$data.query.starDate = str;
            }
          }
        }else {
          this.$data.query.starDate = '';
          this.$data.query.starDate = '';
        }
      }
    }
  }
</script>
<style scoped lang="less">
  .maskCon{
    margin:0 0 15px 50px
  }
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
