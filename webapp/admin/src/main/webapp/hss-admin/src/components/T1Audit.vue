<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">T1结算审核</h3>
          <a @click="isgenerateRecord = true" class="pull-right btn btn-primary" style="margin-left: 20px">生成结算单
          </a>
          <a @click="ismarkSettled = true" class="btn btn-primary" style="float: right;">更新结算状态</a>
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
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" style="width: 188px" :clearable="false" :editable="false"></el-date-picker>
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
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column prop="userNo" label="结算对象编号" ></el-table-column>
            <el-table-column prop="userName" label="结算对象名称" ></el-table-column>
            <el-table-column label="结算对象类型" >
              <template scope="scope">
                <span v-if="scope.row.userType==2">商户</span>
                <span v-if="scope.row.userType==1">金开门</span>
                <span v-if="scope.row.userType==3">代理商</span>
              </template>
            </el-table-column>
            <el-table-column label="交易日期">
              <template scope="scope">
                {{scope.row.tradeDate|changeDate}}
              </template>
            </el-table-column>
            <el-table-column prop="tradeNumber" label="交易笔数" align="right" width="90"></el-table-column>
            <el-table-column prop="settleAmount" label="结算金额" align="right">
              <template scope="scope">
                {{scope.row.settleAmount|toFix}}
              </template>
            </el-table-column>
            <el-table-column prop="settleStatusValue" label="结算状态" ></el-table-column>
            <!--<el-table-column label="操作" width="70">-->
              <!--<template scope="scope">-->
                <!--<el-button @click.native.prevent="list(scope.$index)" type="text" size="small" v-if="records[scope.$index].settleStatusValue!='结算成功'">结算</el-button>-->
              <!--</template>-->
            <!--</el-table-column>-->
            <!--<el-table-column label="操作" width="70">
              <template scope="scope">
                <el-button @click.native.prevent="_$power(scope.$index,list,'boss_trade_export')" type="text" size="small" v-if="records[scope.$index].settleStatusValue!='结算成功'">结算</el-button>
              </template>
            </el-table-column>-->
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
          <!--<div v-if="isShow">
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
          </div>-->
          <div v-if="isgenerateRecord">
            <el-dialog title="生成结算单" v-model="isgenerateRecord">
              <div class="maskCon">
                <span>生成结算单后，将会展示给商户，请务必在对账完成后生成结算单</span>
              </div>
              <div slot="footer" class="dialog-footer" style="text-align: center;">
                <el-button @click="isgenerateRecord = false">取 消</el-button>
                <el-button @click="generate">立即生成</el-button>
              </div>
            </el-dialog>
          </div>
          <div v-if="ismarkSettled">
            <el-dialog title="更新结算单" v-model="ismarkSettled">
              <div class="maskCon">
                <span>更新结算单为成功状态</span>
              </div>
              <div slot="footer" class="dialog-footer" style="text-align: center;">
                <el-button @click="ismarkSettled = false">取 消</el-button>
                <el-button @click="markSettled">立即更新</el-button>
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
                disabledDate(time) {
                  return time.getTime() < Date.now() - 8.64e7*30||time.getTime() > Date.now();
                }
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
              loading:true,
              isShow:false,
              isgenerateRecord:false,
              ismarkSettled:false,
              index:'',
            }
        },
      created: function () {
        this.currentDate();
        this.getData()
      },
      methods: {
        currentDate: function () {
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
        },
        reset: function () {
          this.query = {
            pageNo:1,
            pageSize:10,
            userNo:"",//商户编号
            userName:"",  //商户名字
            userType:'',
            startSettleDate:"",
            endSettleDate:"",
            checkedStatus:'',
            settleStatus:''
          };
          this.currentDate()
        },
        generate:function () {
          this.$http.post('/admin/settle/generateRecord')
            .then(function (res) {
              this.$message({
                showClose: true,
                message: '生成成功',
                type: 'success'
              });
              this.isgenerateRecord = false
            })
            .catch(function (err) {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
        },
        markSettled:function () {
          this.$http.post('/admin/settle/markSettled')
            .then(function (res) {
              this.$message({
                showClose: true,
                message: '更新成功',
                type: 'success'
              });
              this.ismarkSettled = false
            })
            .catch(function (err) {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
        },
        getData: function () {
          this.loading = true;
          this.$http.post('/admin/settle/list',this.$data.query)
            .then(function (res) {
              setTimeout(()=>{
                this.loading = false;
                this.$data.records = res.data.records;
              },1000)
              this.count = res.data.count;
              this.total = res.data.totalPage;
            },function (err) {
              setTimeout(()=>{
                this.loading = false;
              },1000)
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
        },
        search: function () {
          this.query.pageNo = 1;
          this.getData()
        },
        /*list: function (val) {
          this.index = val;
          this.isShow = true;
        },*/
        //每页条数改变
        handleSizeChange(val) {
          this.query.pageNo = 1;
          this.query.pageSize = val;
          this.getData()
        },
        //当前页改变时
        handleCurrentChange(val) {
          this.query.pageNo = val;
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
              this.isShow = false
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
                this.query.startSettleDate = str;
              }else {
                this.query.endSettleDate = str;
              }
            }
          }else {
            this.query.startSettleDate = '';
            this.query.endSettleDate = '';
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
