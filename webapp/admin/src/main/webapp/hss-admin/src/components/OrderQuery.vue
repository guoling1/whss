<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">订单查询</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>业务订单号：</label>
              <el-input style="width: 120px" v-model="query.mobile" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>交易单号：</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>支付方式：</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>状态:</label>
              <el-select style="width: 120px" clearable v-model="query.sysType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收银" value="hsy">好收银</el-option>
                <el-option label="好收收" value="hss">好收收</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading"  style="font-size: 12px;margin:15px 0" :data="records" border >
            <el-table-column label="业务订单号">
              <template scope="scope">
                <router-link to="">{{records[scope.$index].proxyName}}</router-link>
              </template>
            </el-table-column>
            <el-table-column label="交易单号">
              <template scope="scope">
                <router-link to="">{{records[scope.$index].markCode}}</router-link>
              </template>
            </el-table-column>
            <el-table-column prop="level" label="支付流水号" ></el-table-column>
            <el-table-column prop="belong" label="订单金额" ></el-table-column>
            <el-table-column prop="createTime" label="订单状态" ></el-table-column>
            <el-table-column prop="mobile" label="创建时间" ></el-table-column>
            <el-table-column prop="mobile" label="商户名称" ></el-table-column>
            <el-table-column prop="mobile" label="商户编号" ></el-table-column>
            <el-table-column prop="mobile" label="店铺名称" ></el-table-column>
            <el-table-column prop="mobile" label="订单信息" ></el-table-column>
            <el-table-column label="操作" >
              <template scope="scope">
                <router-link to="/admin/record/agentAddPro" v-if="records[scope.$index].hsyProductId==0">开通产品</router-link>
                <router-link to="/admin/record/agentAddPro" v-else="records[scope.$index].hsyProductId==0">修改产品设置</router-link>
              </template>
            </el-table-column>
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
    name: 'orderQuery',
    data(){
      return{
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
