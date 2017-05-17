<template>
  <div id="filialeAccount">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden;min-height: 400px">
        <div class="box-header">
          <h3 class="box-title">分公司账户</h3>
        </div>
        <div class="box-body">
          <ul class="search">
            <li class="same">
              <label>分公司编号:</label>
              <el-input style="width: 188px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>分公司名称:</label>
              <el-input style="width: 188px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px;" :data="records" border>
            <el-table-column width="62" label="序号" type="index"></el-table-column>
            <el-table-column prop="markCode" label="分公司编号" min-width="85"></el-table-column>
            <el-table-column prop="proxyName" label="分公司名称" min-width="85"></el-table-column>
            <el-table-column prop="totalAmount" label="账户总额（元）" align="right" min-width="85"></el-table-column>
            <el-table-column prop="dueSettleAmount" label="待结算金额（元）" align="right" min-width="85"></el-table-column>
            <el-table-column prop="available" label="可用余额（元）" align="right" min-width="85"></el-table-column>
            <el-table-column label="操作" min-width="85">
              <template scope="scope">
                <!--<a @click="details">查看明细</a>-->
                <router-link target="_blank" :to="{path:'/admin/details/profitAccountDet',query:{id:scope.row.id,type:'filiale'}}" type="text" size="small" v-if="scope.row.totalAmount!=0||scope.row.dueSettleAmount!=0||scope.row.available!=0">明细
                </router-link>
              </template>
            </el-table-column>
          </el-table>
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
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default{
    name: 'filialeManage',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          markCode:"",
          proxyName:""
        },
        records: [],
        count: 0,
        loading: true,
      }
    },
    created: function () {
      this.getData();
    },
    methods: {
      reset:function () {
        this.query = {
          pageNo: 1,
          pageSize: 10,
          markCode: "",
          proxyName: ""
        }
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/branchAccount/getBranch',this.query)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.records = res.data.records;
            },1000)
          },function (err) {
            setTimeout(()=>{
              this.loading = false;
            },1000)
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      search(){
        this.query.pageNo = 1;
        this.getData();
      },
      //每页条数改变
      handleSizeChange(val) {
        this.query.pageNo = 1;
        this.query.pageSize = val;
        this.getData();
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.pageNo = val;
        this.getData()
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
    label{
      display: block;
      margin-bottom: 0;
    }
  }

  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 10px 0;
  }
  .btn{
    font-size: 12px;
  }
  .select{
    width: 100%;
    height: 30px;
    background-color: #fff;
    border-radius: 4px;
    border: 1px solid #bfcbd9;
    color: #bfcbd9;
    display: block;
    font-size: 12px;
    line-height: 30px;
    padding: 0px 10px;
    transition: border-color .2s cubic-bezier(.645,.045,.355,1);
    position: relative;
  }
  .select:hover{
    border-color: #8391a5;
  }
  .isShow{
    position: absolute;
    width: 19%;
    border-radius: 2px;
    z-index: 1000;
    max-height: 250px;
    overflow: auto;
    border: 1px solid #d1dbe5;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0,0,0,.12),0 0 6px rgba(0,0,0,.04);
    box-sizing: border-box;
    margin: 5px 0;
    padding:5px;
    li{
      list-style: none;
      height: 25px;
      padding: 0 5px;
      line-height: 25px;
      &:hover{
        background: #1c8de0;
      }
    }
  }

  .isShow1{
    border: 1px solid #d1dbe5;
    border-radius: 2px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0,0,0,.12),0 0 6px rgba(0,0,0,.04);
    box-sizing: border-box;
    margin: 5px 0;
    position: absolute;
    left: 67%;
    /*top: 30%;*/
    width: 16%;
    padding: 5px;
    z-index: 1000;
    max-height: 285px;
    overflow: auto;
    li{
      list-style: none;
      padding: 0 5px;
      line-height: 25px;
      &:hover{
        background: #1c8de0;
      }
    }
  }
</style>
