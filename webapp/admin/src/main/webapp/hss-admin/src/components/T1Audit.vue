<template>
  <div>
    <div class="col-md-12">

      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">T1结算审核</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <el-row :gutter="20" style="width: 910px">
            <el-col :span="4">
              <label>商户编号：</label>
              <el-input v-model="query.merchantNo" placeholder="请输入内容" size="small"></el-input>
            </el-col>
            <el-col :span="4">
              <label>商户名称：</label>
              <el-input v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </el-col>
            <el-col :span="6">
              <label style="display: block">结算日期：</label>
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions"></el-date-picker>
            </el-col>
            <el-col  :span="5">
              <label>对账结果:</label>
              <el-select clearable v-model="query.checkedStatus" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="未对账" value="1">未对账</el-option>
                <el-option label="对账完成无异常" value="2">对账完成无异常</el-option>
                <el-option label="有单边" value="3">有单边</el-option>
              </el-select>
            </el-col>
            <el-col  :span="4">
              <label>结算状态:</label>
              <el-select clearable v-model="query.settleStatus" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待结算" value="1">待结算</el-option>
                <el-option label="部分结算" value="4">部分结算</el-option>
                <el-option label="结算成功" value="3">结算成功</el-option>
              </el-select>
            </el-col>
            <el-col  :span="1" style="margin-top: 18px">
              <div class="btn btn-primary" @click="search">筛选</div>
            </el-col>
          </el-row>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="merchantNo" label="商户编号" ></el-table-column>
            <el-table-column prop="merchantName" label="商户名称" ></el-table-column>
            <el-table-column prop="dealerNo" label="上级代理编号" ></el-table-column>
            <el-table-column prop="dealerName" label="上级代理名称" ></el-table-column>
            <el-table-column prop="appName" label="结算产品" ></el-table-column>
            <el-table-column prop="tradeDate" label="交易日期
" ></el-table-column>
            <el-table-column prop="tradeNumber" label="交易笔数" align="right" width="90"></el-table-column>
            <el-table-column prop="settleAmount" label="结算金额" align="right"></el-table-column>
            <el-table-column prop="checkedStatusValue" label="对账结果" ></el-table-column>
            <el-table-column prop="settleStatusValue" label="结算状态" ></el-table-column>
            <el-table-column label="操作" width="70">
              <template scope="scope">
                <el-button @click.native.prevent="list(scope.$index)" type="text" size="small" v-if="records[scope.$index].settleStatusValue!='结算成功'">结算</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage4" :page-sizes="[10, 20, 50]" :page-size="100" layout="total, sizes, prev, pager, next, jumper" :total="count">
            </el-pagination>
          </div>
          <!--审核-->
          <div v-if="isShow">
            <el-dialog title="结算确认提醒" v-model="isShow">
              <div class="maskCon">
                <span>商户名称：</span>
                <span>{{records[this.$data.index].merchantName}}</span>
              </div>
              <div class="maskCon">
                <span>商户编号：</span>
                <span>{{records[index].merchantNo}}</span>
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
          <!--<div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid"
                       aria-describedby="example2_info">
                  <thead>
                  <tr role="row">

                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" >

                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-if="isShow">
              <img src="http://img.jinkaimen.cn/admin/common/dist/img/ICBCLoading.gif" alt="">
            </div>
            &lt;!&ndash;<div v-if="orders.length==0&&!isShow" class="row" style="text-align: center;color: red;font-size: 16px;">
              <div class="col-sm-12">无此数据</div>
            </div>&ndash;&gt;
            &lt;!&ndash;<div class="row">
              <div class="col-sm-5">
                <div class="dataTables_info" id="example2_info" role="status" aria-live="polite">
                </div>
              </div>
              <div class="col-sm-7">
                <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                  <ul class="pagination" id="page" @click="bindEvent($event)">
                  </ul>
                  <span class="count">共{{count}}条</span>
                </div>
              </div>
            </div>&ndash;&gt;
          </div>-->
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
                merchantNo:"",//商户编号
                merchantName:"",  //商户名字
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
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      },
      methods: {
        search: function () {
          this.$data.query.pageNo = 1;
          this.$data.records = '';
          this.$data.loading = true;
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
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
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
        //每页条数改变时
        handleSizeChange(val) {
          this.$data.query.pageSize = val;
          this.$data.loading = true;
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
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        },
        //当前页改变时
        handleCurrentChange(val) {
          this.$data.query.pageNo = val;
          this.$data.loading = true;
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
              this.$store.commit('MESSAGE_ACCORD_SHOW', {
                text: err.statusMessage
              })
            })
        },
        //结算审核
        settle(val,id) {
          this.$http.post('/admin/settle/singleSettle',{recordId:id,option:val})
            .then(function (res) {
              this.$message({
                showClose: true,
                message: '结算成功',
                type: 'error'
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
  body{
    background-color:#ff0000;
  }
  .maskCon{
    margin:0 0 15px 50px
  }
</style>
