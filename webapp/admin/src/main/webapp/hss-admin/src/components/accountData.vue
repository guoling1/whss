<template>
  <div id="accountData">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">对账数据</h3>
          <a @click="goBack" class="pull-right btn btn-primary">返回</a>
          <!--<a href="javascript:window.close();" class="pull-right btn btn-primary">关闭</a>-->
        </div>
        <div class="box-body">
          <ul>
            <li>帐平笔数：{{record.balanceCount}}笔</li>
            <li>帐平金额：{{record.balanceSum|toFix}}元</li>
            <li>对方单边笔数：{{record.exCount}}笔</li>
            <li>对方单边金额：{{record.exSum|toFix}}元</li>
            <li>己方单边笔数：{{record.inCountWD}}笔</li>
            <li>己方单边金额：{{record.inSumWD|toFix}}元</li>
          </ul>
          <el-tabs class="tab" v-model="activeName" type="border-card" style="margin-bottom: 20px">
            <el-tab-pane :label='"对方单边笔数("+exCount+"笔)"' name="first">
              <el-table  style="font-size: 12px;margin:15px 0" :data="exDetailList" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    {{scope.$index+1}}
                  </template>
                </el-table-column>
                <el-table-column prop="orderSN" label="交易流水"></el-table-column>
                <el-table-column prop="tradeDate" label="交易时间"></el-table-column>
                <el-table-column prop="tradeAmount" label="交易金额" align="right" header-align="left"></el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane :label='"己方单边笔数("+inCountWD+"笔)"' name="second">
              <el-table style="font-size: 12px;margin:15px 0" :data="inDetailList" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    {{scope.$index+1}}
                  </template>
                </el-table-column>
                <el-table-column prop="orderSN" label="交易流水"></el-table-column>
                <el-table-column prop="tradeDate" label="交易时间"></el-table-column>
                <el-table-column prop="tradeAmount" label="交易金额" align="right" min-width="90" header-align="left"></el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane :label='"帐平笔数("+balanceCount+"笔)"' name="third">
              <el-table style="font-size: 12px;margin:15px 0" :data="balanceList" border :row-style="tableFoot">
                <el-table-column   width="100" label="序号">
                  <template scope="scope">
                    {{scope.$index+1}}
                  </template>
                </el-table-column>
                <el-table-column prop="orderSN" label="交易流水"></el-table-column>
                <el-table-column prop="tradeTime" label="交易时间"></el-table-column>
                <el-table-column prop="tradeAmount" label="交易金额" align="right" header-align="left"></el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
          <!--<el-button @click="goBack">取 消</el-button>-->
          <el-button type="primary" @click="submit">确认对账</el-button>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
  </div>
</template>
<script lang="babel">
  import Message from './Message.vue'
  export default{
    name: 'tAuditStore',
    data(){
      return{
        exDetailList:[],
        inDetailList:[],
        balanceList:[],
        balanceCount:'',
        exCount:'',
        inCountWD:'',
        record:"",
        //正式
//        url:"http://checking.qianbaojiajia.com/external/confirmBanlanceAccount"
        //测试
        url:"http://192.168.1.99:8080/balance/external/confirmBanlanceAccount"
      }
    },
    created: function () {
      this.exDetailList = JSON.parse(sessionStorage.getItem('data')).exDetailList;
      this.inDetailList = JSON.parse(sessionStorage.getItem('data')).inDetailList;
      this.balanceList = JSON.parse(sessionStorage.getItem('data')).balanceList;
      this.balanceCount = JSON.parse(sessionStorage.getItem('data')).balanceExternalStatistic.balanceCount;
      this.exCount = JSON.parse(sessionStorage.getItem('data')).balanceExternalStatistic.exCount;
      this.inCountWD = JSON.parse(sessionStorage.getItem('data')).balanceExternalStatistic.inCountWD;
      this.record = JSON.parse(sessionStorage.getItem('data')).balanceExternalStatistic;
    },
    methods: {
      goBack:function () {
//        this.$router.push('/admin/record/accountSystem')
        this.$router.go(-1)
      },
      submit:function () {
          this.$http.post(this.url,{batchNO:JSON.parse(sessionStorage.getItem('data')).balanceExternalStatistic.batchNO},{emulateJSON: true})
            .then(res =>{
//              this.$store.commit('MESSAGE_ACCORD_SHOW', {
//                text: '对账成功'
//              })
              this.$message({
                showClose: true,
                message: '对账成功',
                type: 'success'
              })
              this.$router.go(-1)
//              this.$router.push('/admin/record/accountSystem')
            })
            .catch(err =>{
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })
      },
      getData: function () {
        this.loading = true;
        this.$http.post(this.url,this.query,{emulateJSON: true})
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
        this.query.currentPage = 1;
        this.getData()
      },
    }
  }
</script>
<style scoped lang="less">
  ul{
    padding: 0;
    li{
      display: inline-block;
      margin-right: 10px;
    }
  }
</style>
