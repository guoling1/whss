<template>
  <div>
    <div class="col-md-12" style="height: 880px">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">T1结算审核</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul class="search">
            <li class="same">
              <label>结算对象编号:</label>
              <el-input style="width: 188px" v-model="query.userNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>名称:</label>
              <el-input style="width: 188px" v-model="query.userName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>类型:</label>
              <el-select clearable v-model="query.userType" size="small" style="width: 188px">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="商户" value="2">商户</el-option>
                <el-option label="代理商" value="3">代理商</el-option>
                <el-option label="金开门" value="1">金开门</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>结算日期:</label>
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" style="width: 188px"></el-date-picker>
            </li>
            <li class="same">
              <label>对账结果:</label>
              <el-select clearable v-model="query.checkedStatus" size="small" style="width: 188px">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="未对账" value="1">未对账</el-option>
                <el-option label="对账完成无异常" value="2">对账完成无异常</el-option>
                <el-option label="有单边" value="3">有单边</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>结算状态:</label>
              <el-select clearable v-model="query.settleStatus" size="small" style="width: 188px">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待结算" value="1">待结算</el-option>
                <el-option label="部分结算" value="4">部分结算</el-option>
                <el-option label="结算成功" value="3">结算成功</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border @selection-change="handleSelectionChange">
            <!--<el-table-column type="selection" width="55"></el-table-column>-->
            <el-table-column prop="userNo" label="结算对象编号" ></el-table-column>
            <el-table-column prop="userName" label="结算对象名称" ></el-table-column>
            <el-table-column label="结算对象类型" >
              <template scope="scope">
                <span v-if="records[scope.$index].userType==2">商户</span>
                <span v-if="records[scope.$index].userType==1">金开门</span>
                <span v-if="records[scope.$index].userType==3">代理商</span>
              </template>
            </el-table-column>
            <el-table-column prop="tradeDate" :formatter="changeTime" label="交易日期"></el-table-column>
            <el-table-column prop="tradeNumber" label="交易笔数" align="right" width="90"></el-table-column>
            <el-table-column prop="settleAmount" label="结算金额" align="right" :formatter="changeNum"></el-table-column>
            <el-table-column prop="checkedStatusValue" label="对账结果" ></el-table-column>
            <el-table-column prop="settleStatusValue" label="结算状态" ></el-table-column>
            <el-table-column label="操作" width="70">
              <template scope="scope">
                <el-button @click.native.prevent="_$power(scope.$index,list,'boss_trade_export')" type="text" size="small" v-if="records[scope.$index].settleStatusValue!='结算成功'">结算</el-button>
              </template>
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
      name: 't1Audit',
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
                userNo:"",//商户编号
                userName:"",  //商户名字
                userType:'',
                startSettleDate:"",
                endSettleDate:"",
                checkedStatus:'',
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
        let time = new Date();
        this.date = [time,time];
        for (var j = 0; j < this.date.length; j++) {
          var str = this.date[j];
          var ary = [str.getFullYear(), str.getMonth() + 1, str.getDate()];
          for (var i = 0, len = ary.length; i < len; i++) {
            if (ary[i] < 10) {
              ary[i] = '0' + ary[i];
            }
          }
          str = ary[0] + '-' + ary[1] + '-' + ary[2];
          if (j == 0) {
            this.$data.query.startSettleDate = str;
          } else {
            this.$data.query.endSettleDate = str;
          }
        }
        this.getData()
      },
      methods: {
        changeTime: function (row, column) {
          var val = row.tradeDate;
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
        changeNum: function (row, column) {
          var val = row.settleAmount;
          return parseFloat(val).toFixed(2);
        },
        getData: function () {
          this.loading = true;
          this.$http.post('/admin/settle/list',this.$data.query)
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
          if(val!=undefined&&val[0]!=null){
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
                this.$data.query.startSettleDate = str;
              }else {
                this.$data.query.endSettleDate = str;
              }
            }
          }else {
            this.$data.query.startSettleDate = '';
            this.$data.query.endSettleDate = '';
          }
        }
      }
    }
</script>
<style scoped lang="less">
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

  .maskCon{
    margin:0 0 15px 50px
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
