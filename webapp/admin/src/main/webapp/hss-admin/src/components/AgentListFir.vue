<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">一级代理商列表</h3>
          <router-link to="/admin/record/agentAddBase" class="btn btn-primary" style="float: right;color: #fff">新增代理商</router-link>
        </div>
        <div class="box-body">
          <!--筛选-->
          <el-row :gutter="20" style="width: 910px">
            <el-col :span="4">
              <label>手机号：</label>
              <el-input v-model="query.mobile" placeholder="请输入内容" size="small"></el-input>
            </el-col>
            <el-col :span="4">
              <label>代理商名称：</label>
              <el-input v-model="query.name" placeholder="请输入内容" size="small"></el-input>
            </el-col>
            <el-col :span="4">
              <label>代理商编号：</label>
              <el-input v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </el-col>
            <el-col :span="5">
              <label>省市:</label>
              <div class="select" id="select" @click="open"><span>请选择</span>
                <i class="el-icon-caret-bottom" style="float: right;margin-top: 10px"></i>
              </div>
              <div class="isShow" v-if="isOpen"><el-tree :data="provinces" :props="defaultProps" accordion @node-click="select"></el-tree></div>
            </el-col>
            <el-col  :span="5">
              <label>代理产品:</label>
              <el-select clearable v-model="query.sysType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收银" value="hsy">好收银</el-option>
                <el-option label="好收收" value="hss">好收收</el-option>
              </el-select>
            </el-col>
            <el-col  :span="1" style="margin-top: 18px">
              <div class="btn btn-primary" @click="search">筛选</div>
            </el-col>
          </el-row>
          <!--表格-->
          <el-table v-loading.body="loading"  style="font-size: 12px;margin:15px 0" :data="records" border @cell-click="toDet">
            <el-table-column prop="proxyName" label="代理商名称" class-name="blue"></el-table-column>
            <el-table-column prop="markCode" label="代理商编号" class-name="blue"></el-table-column>
            <el-table-column prop="level" label="代理商级别" ></el-table-column>
            <el-table-column prop="belong" label="省市" ></el-table-column>
            <el-table-column prop="createTime" label="注册时间" ></el-table-column>
            <el-table-column prop="mobile" label="联系手机号" ></el-table-column>
            <el-table-column label="好收收" width="90">
              <template scope="scope">
                <router-link to="/admin/record/agentAddPro" v-if="hssProductId==0">开通产品</router-link>
                <router-link to="/admin/record/agentAddPro" v-if="hssProductId!=0">修改产品设置</router-link>
              </template>
            </el-table-column>
            <el-table-column label="好收银" >
              <template scope="scope">
                <router-link to="/admin/record/agentAddPro" v-if="hsyProductId==0">开通产品</router-link>
                <router-link to="/admin/record/agentAddPro" v-if="hsyProductId!=0">修改产品设置</router-link>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <div class="block" style="text-align: right">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage4" layout="total, sizes, prev, pager, next, jumper" :total="count">
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
                <el-button @click="isShow = false">结算已对账部分</el-button>
                <el-button @click="isShow = false">强制结算全部</el-button>
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
    name: 'agentListFir',
    data(){
      return{
        loading:true,
        defaultProps: {
          label:'aname',
          children: 'list'
        },
        isOpen:false,
        select1:'',
        select2:'',
        provinces:[],//所有省份
        province: '',
        citys:[],
        city:'',
        date:'',
        records:[],
        count:0,
        total:0,
        pageSize:'',
        query:{
          pageNo:1,
          pageSize:10,
          mobile:'',
          name:"",
          markCode:"",  //商户名字
          sysType:"",
          districtCode:""
        },
        multipleSelection:[],
        currentPage4: 1,
        isShow:false,
        index:'',
      }
    },
    created: function () {
      //搜索区省市联动
      this.$http.post('/admin/district/findAllDistrict')
        .then(function (res) {
          this.$data.provinces = res.data;
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
            this.$data.records[i].createTime = changeTime(this.$data.records[i].createTime)
          }
        })
        .catch(function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })

      this.$http.post('/admin/dealer/listFirstDealer',this.$data.query)
        .then(function (res) {
          this.$data.records = res.data.records;
          this.$data.count = res.data.count;
          this.$data.total = res.data.totalPage;
          this.$data.pageSize = res.data.pageSize;
          this.$data.loading = false;
          for(var i=0;i<this.$data.records.length;i++){
            if(this.$data.records[i].belongProvinceName!=null&&this.$data.records[i].belongCityName!=null){
              this.$data.records[i].belong = this.$data.records[i].belongProvinceName+"-"+this.$data.records[i].belongCityName;
            }
          }
        })
    },
    updated :function () {
      var aBlue = document.getElementsByClassName('blue');
      console.log(aBlue)
      for(var i=2;i<aBlue.length;i++){
        console.log(1)
        aBlue[i].style.color = "#3c8dbc";
        aBlue[i].style.cursor = "pointer";
      }
    },
    methods: {
      open:function () {
        this.$data.isOpen = !this.$data.isOpen;
        document.getElementById('select').style.borderColor = '#20a0ff'
      },
      select:function (val) {
        if(val.list!=undefined){
          this.$data.select1 = val.aname;
          this.$data.select2 = '';
        }else {
          this.$data.select2 = val.aname;
        }
        if(this.$data.select1!=''&&this.$data.select2!=''){
          var oCon = document.getElementById('select').getElementsByTagName('span')[0];
          oCon.innerHTML = this.$data.select1+'-'+this.$data.select2;
          oCon.style.color = '#1f2d3d';
          this.$data.isOpen = !this.$data.isOpen;
          document.getElementById('select').style.borderColor = '#bfcbd9'
          for(var i=0; i<this.$data.provinces.length;i++){
            for(var j=0; j<this.$data.provinces[i].list.length; j++){
                if(this.$data.provinces[i].list[j].aname==this.$data.select2){
                  this.$data.query.districtCode = this.$data.provinces[i].list[j].code;
                }
            }
          }
        }
      },
      toDet:function (row,col,cell,e) {
        if(col.label=='代理商名称'||col.label=='代理商编号'){
          this.$router.push('/admin/record/agentAddBase?id='+row.id)
        }
      },
      search: function () {
        this.$data.query.pageNo = 1;
        this.$data.records = '';
        this.$data.loading = true;
        console.log(this.$data.query);
        this.$http.post('/admin/dealer/listFirstDealer',this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            for(var i=0;i<this.$data.records.length;i++){
              if(this.$data.records[i].belongProvinceName!=null&&this.$data.records[i].belongCityName!=null){
                this.$data.records[i].belong = this.$data.records[i].belongProvinceName+"-"+this.$data.records[i].belongCityName;
              }
            }
          })
      },
      list: function (val) {
        this.$data.index = val;
        this.$data.isShow = true;
      },
      handleSelectionChange(val) {
        console.log(val)
        this.multipleSelection = val;
      },
      //每页条数改变时
      handleSizeChange(val) {
        this.$data.query.pageSize = val;
        this.$data.loading = true;
        this.$data.query.pageNo = 1;
        this.$data.records = '';
        this.$http.post('/admin/dealer/listFirstDealer',this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            for(var i=0;i<this.$data.records.length;i++){
              if(this.$data.records[i].belongProvinceName!=null&&this.$data.records[i].belongCityName!=null){
                this.$data.records[i].belong = this.$data.records[i].belongProvinceName+"-"+this.$data.records[i].belongCityName;
              }
            }
          })
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.records = '';
        this.$data.loading = true;
        this.$http.post('/admin/dealer/listFirstDealer',this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.loading = false;
            for(var i=0;i<this.$data.records.length;i++){
              if(this.$data.records[i].belongProvinceName!=null&&this.$data.records[i].belongCityName!=null){
                this.$data.records[i].belong = this.$data.records[i].belongProvinceName+"-"+this.$data.records[i].belongCityName;
              }
            }
          })
      }
    },
    watch:{
      date:function (val,oldVal) {
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
    overflow: hidden;
    position: relative;
  }
  .select:hover{
    border-color: #8391a5;
  }
  .isShow{
    position: absolute;
    width: 19%;
    margin-top: 5px;
    border-radius: 2px;
    z-index: 1000;
    max-height: 250px;
    overflow: auto;
  }
  .blue div.cell{
    color: #3c8dbc;
    cursor: pointer;
  }
  .blue div:hover{
    color: #72afd2;
  }
</style>
