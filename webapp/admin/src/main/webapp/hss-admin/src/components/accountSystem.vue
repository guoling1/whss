<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">系统对账</h3>
        </div>
        <div class="box-body">
          <ul>
            <li class="same">
              <label>对账渠道:</label>
              <el-input v-model="query.channelName" placeholder="请输入内容" size="small" style="width: 193px"></el-input>
            </li>
            <li class="same">
              <label>交易时间:</label>
              <el-date-picker
                v-model="date"
                type="daterange"
                align="right"
                placeholder="选择日期范围"
                :picker-options="pickerOptions2" size="small">
              </el-date-picker>
            </li>
            <li class="same">
              <label>交易类型:</label>
              <el-select clearable v-model="query.tradeType" size="small" style="width: 193px">
                <el-option label="交易" value="1"></el-option>
                <el-option label="提现" value="3"></el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="62" label="序号" fixed="left"></el-table-column>
            <el-table-column prop="userName" label="通道名称" ></el-table-column>
            <el-table-column prop="userNo" label="单边方向" ></el-table-column>
            <el-table-column prop="userNo" label="类型" ></el-table-column>
            <el-table-column prop="userNo" label="日期" ></el-table-column>
            <el-table-column prop="userNo" label="对账单号" ></el-table-column>
            <el-table-column prop="userNo" label="对账金额（元）" ></el-table-column>
            <el-table-column prop="userNo" label="处理时间" ></el-table-column>
            <el-table-column prop="userNo" label="处理结果" ></el-table-column>
            <el-table-column prop="userNo" label="操作人" ></el-table-column>
            <el-table-column prop="userNo" label="备注信息" ></el-table-column>
            <el-table-column label="操作" width="70">
              <template scope="scope">
                <!--<el-button @click.native.prevent="list(scope.$index)" type="text" size="small" v-if="records[scope.$index].settleStatusValue!='结算成功'">结算</el-button>-->
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="query.currentPage"
                           :page-sizes="[10, 20, 50]"
                           :page-size="query.pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="count">
            </el-pagination>
          </div>
          <!--审核-->
          <!--<div v-if="isShow">-->

          <!--</div>-->
        </div>
        <el-dialog title="修改商户归属信息" v-model="isShow">
          <el-form :label-position="right" label-width="150px">
            <el-form-item label="处理方式：" width="80" style="margin-bottom: 0">
              <el-select size="small" placeholder="请选择" v-model="handleQuery.changeType">
                <el-option label="系统已处理" value="1"></el-option>
                <el-option label="无需处理" value="2"></el-option>
                <el-option label="已线下处理" value="3"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="备注信息：" width="120" style="margin-bottom: 0">
              <el-input style="width: 70%" type="textarea" :rows="2" placeholder="非必填" v-model="textarea"></el-input>
            </el-form-item>
            <el-form-item label="备注信息：" width="120" style="margin-bottom: 0">
              <el-upload
                class="upload-demo"
                action="//jsonplaceholder.typicode.com/posts/"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :file-list="fileList">
                <el-button id="btn" size="small" type="primary">点击上传</el-button>
                <div style="position: absolute;top: 36px;left:-1px;width: 220px;height: 30px;background: #fff"></div>
              </el-upload>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer" style="text-align: center">
            <el-button type="primary" style="position: relative;top: -20px;">处 理</el-button>
          </div>
        </el-dialog>
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
        pickerOptions: {},
        fileList: [],
        records:[],
        count:0,
        query:{
          pageSize:"",
          currentPage:"",
          channelName:"",
          tradeType:"",
          startDateStr:"",
          endDateStr:""
        },
        handleQuery:{
          changeType:'',
          res:""
        },
        item_db:[
          {label:'', value:'全部'},
          {label:'', value:'我方单边'},
          {label:'', value:'对方单边'}
        ],
        item_cl:[
          {label:'', value:'全部'},
          {label:'', value:'未处理'},
          {label:'', value:'系统已处理'},
          {label:'', value:'无需处理'},
          {label:'', value:'已线下处理'}
        ],
        loading:true,
        isShow:true,
        index:'',
        url:'http://192.168.1.99:8080/balance/external/statisticList'
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      handlePreview(file) {
        console.log(file);
      },
      getData: function () {
        this.loading = true;
        this.$http.post(this.url,this.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.loading = false;
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
        this.$data.query.currentPage = 1;
        this.getData()
      },
      //每页条数改变
      handleSizeChange(val) {
        this.$data.query.currentPage = 1;
        this.$data.query.pageSize = val;
        this.getData()
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.currentPage = val;
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
              this.$data.query.startDateStr = str;
            } else {
              this.$data.query.endDateStr = str;
            }
          }
        } else {
          this.$data.query.startDateStr = '';
          this.$data.query.endDateStr = '';
        }
      }
    }
  }
</script>
<style scoped lang="less">
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
