<template>
  <div id="withDrawal">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">打款查询</h3>
          <router-link to="/admin/record/withdrawal" class="pull-right" style="margin-left: 20px">切换旧版</router-link>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>打款流水号:</label>
              <el-input style="width: 130px" v-model="query.sn" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>交易单号:</label>
              <el-input style="width: 130px" v-model="query.orderNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>备注:</label>
              <el-input style="width: 130px" v-model="query.remark" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>收款账户名:</label>
              <el-input style="width: 130px" v-model="query.userName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>打款状态:</label>
              <el-select style="width: 120px" clearable v-model="query.status" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待提现" value="1">待提现</el-option>
                <el-option label="请求成功" value="3">请求成功</el-option>
                <el-option label="打款成功" value="4">打款成功</el-option>
                <el-option label="打款失败" value="5">打款失败</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              </li>
            <li class="same" style="float: right">
              <span @click="onload()" download="打款记录" class="btn btn-primary" style="color: #fff">导出</span>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" max-height="637" style="font-size: 12px;margin-bottom: 15px" :data="records" border :row-style="tableFoot">
            <el-table-column width="62" label="序号" fixed="left">
              <template scope="scope">
                <div v-if="records[scope.$index].orderNo!='当页总额'&&records[scope.$index].orderNo!='筛选条件统计'">{{scope.$index+1}}</div>
              </template>
            </el-table-column>
            <el-table-column label="打款流水号" min-width="100">
              <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].sn" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{records[scope.$index].sn|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column label="交易单号" min-width="100">
              <template scope="scope">
                <span v-if="records[scope.$index].orderNo!='当页总额'&&records[scope.$index].orderNo!='筛选条件统计'" class="td" :data-clipboard-text="records[scope.$index].orderNo" type="text" size="small" style="cursor: pointer" title="点击复制">{{records[scope.$index].orderNo|changeHide}}</span>
                <span v-if="records[scope.$index].orderNo=='当页总额'">当页总额</span>
                <span v-if="records[scope.$index].orderNo=='筛选条件统计'">筛选条件统计</span>
              </template>
            </el-table-column>
            <el-table-column prop="amount" align="right" label="打款金额" min-width="90"></el-table-column>
            <el-table-column label="收款账户名" min-width="100">
              <template scope="scope">
                <div v-if="records[scope.$index].orderNo!='当页总额'&&records[scope.$index].orderNo!='筛选条件统计'">{{records[scope.$index].receiptUserName}}</div>
                <a v-if="records[scope.$index].orderNo=='筛选条件统计'" @click="add">点击统计</a>
              </template>
            </el-table-column>
            <el-table-column label="收款银行账号" min-width="100">
              <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].bankCard" type="text" size="small"
                      style="cursor: pointer" title="点击复制">{{records[scope.$index].bankCard|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column prop="requestTime" :formatter="changeTime" label="打款时间" width="160"></el-table-column>
            <el-table-column prop="statusValue" label="打款状态" min-width="90"></el-table-column>
            <el-table-column prop="playMoneyChannel" label="打款通道" min-width="120"></el-table-column>
            <el-table-column prop="message" label="渠道信息" min-width="85"></el-table-column>
            <el-table-column label="备注信息" min-width="90">
              <template scope="scope">
                <span class="td" :data-clipboard-text="records[scope.$index].remark" type="text" size="small" style="cursor: pointer" title="点击复制">{{records[scope.$index].remark|changeHide}}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="80" fixed="right">
              <template scope="scope">
                <router-link :to="{path:'/admin/record/withdrawalAudit',query:{orderNo:records[scope.$index].orderNo,sn:records[scope.$index].sn,requestTime:records[scope.$index].requestTime,amount:records[scope.$index].amount,receiptUserName:records[scope.$index].receiptUserName,playMoneyChannel:records[scope.$index].playMoneyChannel,status:records[scope.$index].status,bankCard:records[scope.$index].bankCard,message:records[scope.$index].message}}" id="audit" v-if="(records[scope.$index].status=='2'||records[scope.$index].status=='3'||records[scope.$index].status=='5')&&records[scope.$index].auditStatus==0&&records[scope.$index].orderNo!='当页总额'&&records[scope.$index].orderNo!='筛选条件统计'" >审核</router-link>
                <span v-if="records[scope.$index].status=='5'&&records[scope.$index].auditStatus!=0&&records[scope.$index].orderNo!='当页总额'&&records[scope.$index].orderNo!='筛选条件统计'">{{records[scope.$index].auditStatusValue}}</span>
                <a href="javascript:;" @click="updata(records[scope.$index].sn)" v-if="records[scope.$index].orderNo!='当页总额'&&records[scope.$index].orderNo!='筛选条件统计'">同步</a>
              </template>
            </el-table-column>
          </el-table>
          </el-table>
          <ul style="float: left;margin-top: 5px">
            <li>
              <label style="margin-right: 10px;">打款金额</label>
              <span>当页总额：{{pageTotal}}&nbsp;元</span>&nbsp;&nbsp;&nbsp;&nbsp;
              <span>统计总额：{{addTotal}}&nbsp;元</span>
            </li>
          </ul>
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
        </div>
      </div>
    </div>
    <!--下载-->
    <div class="box box-info mask el-message-box" v-if="isMask">
      <div class="maskCon">
        <div class="head">
          <div class="title">消息</div>
          <i class="el-icon-close" @click="isMask=false"></i>
        </div>
        <div class="body">
          <div>确定导出列表吗？</div>
        </div>
        <div class="foot">
          <a href="javascript:void(0)" @click="isMask=false" class="el-button el-button--default">取消</a>
          <a :href="'http://'+url" @click="isMask=false" class="el-button el-button-default el-button--primary ">下载</a>
        </div>
      </div>

    </div>
  </div>
</template>

<script lang="babel">
  import Clipboard from "clipboard"
  export default{
    name: 'withDrawal',
    data(){
      return {
        isMask:false,
        query:{
          pageNo:1,
          pageSize:10,
          orderNo:'',
          remark:'',
          sn:'',
          userName:'',
          status:''
        },
        records: [],
        count: 0,
        total: '',
        loading: true,
        url:'',
        pageTotal: 0,
        addTotal: 0,
        //正式
        queryUrl:'http://pay.qianbaojiajia.com/order/withdraw/listOrder',
         excelUrl:'http://pay.qianbaojiajia.com/order/withdraw/exportExcel',
         syncUrl:'http://pay.qianbaojiajia.com/order/syncWithdrawOrder',
         addUrl:'http://pay.qianbaojiajia.com/order/withdraw/countAmount',
        //测试
        /*queryUrl:'http://192.168.1.20:8076/order/withdraw/listOrder',
        excelUrl:'http://192.168.1.20:8076/order/withdraw/exportExcel',
        syncUrl:'http://192.168.1.20:8076/order/syncWithdrawOrder',
        addUrl:'http://192.168.1.25:8240/order/withdraw/countAmount',*/
      }
    },
    created: function () {
      var clipboard = new Clipboard('.td');
      //复制成功执行的回调，可选
      clipboard.on('success', (e) => {
        this.$message({
          showClose: true,
          message: "复制成功  内容为：" + e.text,
          type: 'success'
        });
      });
      this.getData();
      this.getAddTotal()
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post(this.queryUrl,this.$data.query)
          .then(function (res) {
            this.loading = false;
            this.$data.records = res.data.records;
            this.$data.url=res.data.ext;
            this.$data.count = res.data.count;
            var price=0;
            var toFix = function (val) {
              return parseFloat(val).toFixed(2)
            };
            for (var i = 0; i < this.records.length; i++) {
              price = toFix(parseFloat(price)+parseFloat(this.records[i].amount));
            }
            this.pageTotal = price;
            /*if(this.records.length!=0){
              this.records.push({
                orderNo:"当页总额",
                amount:price
              },{
                orderNo:"筛选条件统计",
                amount:''
              });
              this.records[this.records.length-1].amount = this.total;
            }*/
          },function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      getAddTotal(){
        this.$http.post(this.addUrl,this.query)
          .then(res=>{
            this.addTotal = res.data;
          })
          .catch(err=>{
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      tableFoot(row, index) {
        if (row.orderNo === '当页总额'||row.orderNo === '筛选条件统计') {
          return {background:'#eef1f6'}
        }
        return '';
      },
      //格式化时间
      changeTime: function (row, column) {
        var val = row.requestTime;
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
      onload:function () {
        this.$data.isMask = true;
        this.$http.post(this.$data.excelUrl,this.$data.query)
          .then(function (res) {
            this.$data.url = res.data.url;
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
            this.$data.isMask = false;
          })
      },
      updata: function (val) {
        this.$data.loading = true;
        this.$http.post(this.$data.syncUrl,{sn:val})
          .then(function (res) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: '同步成功',
              type: 'success'
            });
          },function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      search(){
        this.total = '';
        this.$data.query.pageNo = 1;
        this.getData();
        this.getAddTotal()
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
              this.$data.query.startCreateTime = str;
            } else {
              this.$data.query.endCreateTime = str;
            }
          }
        } else {
          this.$data.query.startCreateTime = '';
          this.$data.query.endCreateTime = '';
        }
      },
      date1: function (val, oldVal) {
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
              this.$data.query.startFinishTime = str;
            } else {
              this.$data.query.endFinishTime = str;
            }
          }
        } else {
          this.$data.query.startFinishTime = '';
          this.$data.query.endFinishTime = '';
        }
      },
    },
    filters: {
      changeHide: function (val) {
        if(val!=""&&val!=null){
          val = val.replace(val.substring(3,val.length-6),"…");
        }
        return val
      },
    }
  }
</script>

<style scoped lang="less" rel="stylesheet/less">
  ul {
    padding: 0;
  }

  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }
  .mask{
    z-index: 2020;
    position: fixed;
    top:0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.45);
    .maskCon{
      margin: 250px auto;
      text-align: left;
      vertical-align: middle;
      background-color: #fff;
      width: 420px;
      border-radius: 3px;
      font-size: 16px;
      overflow: hidden;
      -webkit-backface-visibility: hidden;
      backface-visibility: hidden;
      .head{
        position: relative;
        padding: 20px 20px 0;
        .title{
          padding-left: 0;
          margin-bottom: 0;
          font-size: 16px;
          font-weight: 700;
          height: 18px;
          color: #333;
        }
        i{
          font-family: element-icons!important;
          speak: none;
          font-style: normal;
          font-weight: 400;
          font-variant: normal;
          text-transform: none;
          vertical-align: baseline;
          display: inline-block;
          -webkit-font-smoothing: antialiased;
          position: absolute;
          top: 19px;
          right: 20px;
          color: #999;
          cursor: pointer;
          line-height: 20px;
          text-align: center;
        }
      }
      .body{
        padding: 30px 20px;
        color: #48576a;
        font-size: 14px;
        position: relative;
        div{
          margin: 0;
          line-height: 1.4;
          font-size: 14px;
          color: #48576a;
          font-weight: 400;
        }
      }
      .foot{
        padding: 10px 20px 15px;
        text-align: right;
      }
    }

  }

</style>
