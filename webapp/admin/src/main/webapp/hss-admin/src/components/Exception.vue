<template>
  <div id="exception">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">结算挂起的商户</h3>
        </div>
        <div class="box-body">
          <ul class="search">
            <li class="same">
              <label>结算对象编号:</label>
              <el-input style="width: 193px" v-model="query.userNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>结算对象名称:</label>
              <el-input style="width: 193px" v-model="query.userName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>结算对象类型:</label>
              <el-select clearable v-model="query.userType" size="small" style="width: 193px">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="商户" value="2">商户</el-option>
                <el-option label="代理商" value="3">代理商</el-option>
                <el-option label="金开门" value="1">金开门</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>挂起日期:</label>
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions" style="width: 193px" :clearable="false" :editable="false"></el-date-picker>
            </li>
            <li class="same">
              <label>状态:</label>
              <el-select clearable v-model="query.settleExceptionStatus" size="small" style="width: 193px">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="已处理" value="2"></el-option>
                <el-option label="未处理" value="0"></el-option>
              </el-select>
            </li>
            <li class="same">
              <el-button type="primary" size="small" @click="search">筛选</el-button>
              <el-button type="primary" size="small" @click="reset">重置</el-button>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px" :data="records" border>
            <el-table-column prop="settleTargetNo" label="结算对象编号" min-width="110"></el-table-column>
            <el-table-column prop="settleTargetName" label="结算对象名称" min-width="110"></el-table-column>
            <el-table-column label="结算对象类型" min-width="110">
              <template scope="scope">
                <span v-if="scope.row.settleTargetType==2">商户</span>
                <span v-if="scope.row.settleTargetType==1">金开门</span>
                <span v-if="scope.row.settleTargetType==3">代理商</span>
              </template>
            </el-table-column>
            <el-table-column label="挂起的结算周期" min-width="280">
              <template scope="scope">
                {{scope.row.beginTime|changeTime}}至{{scope.row.endTime|changeTime}}
              </template>
            </el-table-column>
            <el-table-column label="挂起时间" min-width="155">
              <template scope="scope">
                {{scope.row.createTime|changeTime}}
              </template>
            </el-table-column>
            <el-table-column prop="settleStatusValue" label="状态">
              <template scope="scope">
                <span v-if="scope.row.status==0">未处理</span>
                <span v-if="scope.row.status==2">已处理</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template scope="scope">
                <el-button @click.native.prevent="changeSettle(scope.row.id)" type="text" size="small" v-if="scope.row.status==0">结算</el-button>
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
          <div v-if="isSettle">
            <el-dialog title="结算" v-model="isSettle">
              <div class="maskCon">
                <span>确认结算吗？</span>
              </div>
              <div slot="footer" class="dialog-footer" style="text-align: center;">
                <el-button @click="isSettle = false">取 消</el-button>
                <el-button type="primary" @click="settle" :loading="settleClick">结算</el-button>
              </div>
            </el-dialog>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'exception',
    data(){
      return{
        id:'',
        settleClick:false,
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
          startDate:"",
          endDate:"",
          settleExceptionStatus:''
        },
        loading:true,
        isShow:false,
        isSettle:false,
        index:'',
      }
    },
    created: function () {
      this.currentDate();
      this.getData()
    },
    methods: {
      changeSettle: function (id) {
        this.isSettle = true;
        this.id = id;
      },
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
            this.query.startDate = str;
          } else {
            this.query.endDate = str;
          }
        }
      },
      reset: function () {
        this.query = {
          pageNo:1,
          pageSize:10,
          userNo:"",
          userName:"",
          userType:'',
          startDate:"",
          endDate:"",
          settleExceptionStatus:''
        };
        this.currentDate()
      },
      settle:function () {
        this.settleClick = true;
        this.$http.post('/admin/settlementRecord/handle',{settleExceptionId:this.id})
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '结算成功',
              type: 'success'
            });
            this.isSettle = false;
            this.settleClick = false;
            this.getData();
          })
          .catch(function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
            this.settleClick = false;
          })
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/settlementRecord/exceptionList',this.query)
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
              this.query.startDate = str;
            }else {
              this.query.endDate = str;
            }
          }
        }else {
          this.query.startDate = '';
          this.query.endDate = '';
        }
      }
    }
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
