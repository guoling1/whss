<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">交易查询</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>业务订单号：</label>
              <el-input style="width: 120px" v-model="query.mobile" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>交易订单号：</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>交易流水号：</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>收款商户名称：</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>收款商户编号：</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>交易状态:</label>
              <el-select style="width: 120px" clearable v-model="query.sysType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收银" value="hsy">待支付</el-option>
                <el-option label="好收收" value="hss">支付中</el-option>
                <el-option label="好收收" value="hss">支付中</el-option>
                <el-option label="好收收" value="hss">支付中</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>支付方式：</label>
              <el-select style="width: 120px" clearable v-model="query.sysType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收银" value="hsy">待支付</el-option>
                <el-option label="好收收" value="hss">支付中</el-option>
                <el-option label="好收收" value="hss">支付中</el-option>
                <el-option label="好收收" value="hss">支付中</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>结算状态:</label>
              <el-select style="width: 120px" clearable v-model="query.sysType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收银" value="hsy">待结算</el-option>
                <el-option label="好收收" value="hss">结算中</el-option>
                <el-option label="好收收" value="hss">已结算</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>交易日期:</label>
              <el-date-picker
                v-model="value7"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions2" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <label>上级代理名称：</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading"  style="font-size: 12px;margin:15px 0" :data="records" border >
            <el-table-column label="序号">
              <template scope="scope">
                <span to="">{{(query.page-1)*10+(index+1)}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="level" label="业务方" ></el-table-column>
            <el-table-column prop="level" label="业务订单号" ></el-table-column>
            <el-table-column prop="level" label="付款人" ></el-table-column>
            <el-table-column prop="level" label="收款商户名称" ></el-table-column>
            <el-table-column prop="level" label="交易订单号" ></el-table-column>
            <el-table-column prop="level" label="交易金额" ></el-table-column>
            <el-table-column prop="level" label="手续费" ></el-table-column>
            <el-table-column prop="level" label="交易类型" ></el-table-column>
            <el-table-column prop="level" label="交易状态" ></el-table-column>
            <el-table-column prop="level" label="结算状态" ></el-table-column>
            <el-table-column prop="level" label="交易时间" ></el-table-column>
            <el-table-column prop="level" label="交易成功时间" ></el-table-column>
            <el-table-column prop="level" label="结算周期" ></el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'tradeQuery',
    data(){
      return{
        pickerOptions2: {
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
        value7:'',
        loading:true,
        records:[],
        count:0,
        total:0,
        query:{
          pageNo:1,
          pageSize:10,
          mobile:'',
          name:"",
          markCode:"",  //商户名字
          sysType:"",
          districtCode:""
        },
        currentPage: 1,
      }
    },
    created: function () {

    },
    methods: {
      search: function () {
        this.$data.query.pageNo = 1;
        this.$data.records = '';
        this.$data.loading = true;

      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.records = '';
        this.$data.loading = true;

      }
    },

  }
</script>
<style scoped lang="less">
  .btn{
    font-size: 12px;
  }
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
</style>
