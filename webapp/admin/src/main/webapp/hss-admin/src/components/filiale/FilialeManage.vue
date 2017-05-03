<template>
  <div id="filialeManage">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden;min-height: 400px">
        <div class="box-header">
          <h3 class="box-title">分公司管理</h3>
          <el-button type="text" @click="addFiliale" class="pull-right">新增分公司</el-button>
        </div>
        <div class="box-body">
          <ul class="search">
            <li class="same">
              <label>手机号:</label>
              <el-input style="width: 188px" v-model="query.mobile" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>代理商编号:</label>
              <el-input style="width: 188px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>代理商名称:</label>
              <el-input style="width: 188px" v-model="query.name" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>省市:</label>
              <div style="width: 188px" class="select" id="select" @click="open"><span style="color: #1f2d3d">{{selectCon}}</span>
                <i class="el-icon-caret-bottom" style="float: right;margin-top: 10px"></i>
              </div>
              <ul class="isShow" v-if="isOpen">
                <li @click="selectAll()">全部</li>
                <li v-for="province in provinces" @mouseover="selectCity(province.code,province.aname)" @click="select(province.code,province.aname)">{{province.aname}}
                </li>
              </ul>
              <ul class="isShow1" v-if="isOpen1">
                <li :class="'cityLi'+$index" v-for="city in citys" @click="select(city.code,city.aname)">{{city.aname}}</li>
              </ul>
            </li>
            <li class="same">
              <label>代理产品:</label>
              <el-select style="width: 188px" clearable v-model="query.status" size="small">
                <el-option label="全部" value=""></el-option>
                <el-option label="好收收" value="hss"></el-option>
                <el-option label="好收银" value="hsy"></el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px;" :data="records" border>
            <el-table-column width="62" label="序号" type="index"></el-table-column>
            <el-table-column label="分公司简称" min-width="85">
              <template scope="scope">
                <router-link to="/"></router-link>
              </template>
            </el-table-column>
            <el-table-column label="分公司编号" min-width="85">
              <template scope="scope">
                <router-link to="/"></router-link>
              </template>
            </el-table-column>
            <el-table-column prop="appId" label="地区" min-width="85"></el-table-column>
            <el-table-column prop="appId" label="开通日期" min-width="85"></el-table-column>
            <el-table-column prop="appId" label="联系人手机号" min-width="85"></el-table-column>
            <el-table-column prop="appId" label="好收收" min-width="85">
              <template scope="scope">
                <a @click="_$power(scope.row.id,scope.row.hssProductId,'hss',openProduct,'boss_first_product_add')" v-if="records[scope.$index].hssProductId==0">开通产品</a>
                <a @click="_$power(scope.row.id,scope.row.hssProductId,'hss',auditProduct,'boss_first_product_update_btn')" v-else="records[scope.$index].hssProductId==0">修改产品设置</a>
                <a @click="_$power(scope.row.id,scope.row.hssProductId,'hss',auditProduct,'boss_first_product_update_btn')" v-else="records[scope.$index].hssProductId==0">配置网关</a>
                <a @click="_$power(scope.row.id,scope.row.hssProductId,'hss',auditProduct,'boss_first_product_update_btn')" v-else="records[scope.$index].hssProductId==0">配置O单</a>
              </template>
            </el-table-column>
            <el-table-column label="好收银" min-width="85">
              <template scope="scope">
                <a @click="_$power(scope.row.id,scope.row.hsyProductId,'hsy',openProduct,'boss_first_product_add')" v-if="records[scope.$index].hsyProductId==0">开通产品</a>
                <a @click="_$power(scope.row.id,scope.row.hsyProductId,'hsy',auditProduct,'boss_first_product_update_btn')" v-else="records[scope.$index].hsyProductId==0">修改产品设置</a>
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
          mobile:'',
          name:"",
          markCode:"",
          districtCode:"",
          status:'hss'
        },
        date: '',
        records: [],
        count: 0,
        total: '',
        loading: true,
        url: '',
        options:[],
        provinces:[],//所有省份
        province: '',
        citys:[],
        city:'',
        selectCon:'全部',
        isOpen:false,
        isOpen1:false,
      }
    },
    created: function () {
      this.$http.post('/admin/district/findAllProvinces')
        .then(res =>{
          this.provinces = res.data;
        })
        .catch(err =>{
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
      this.getData();
    },
    methods: {
      reset:function () {

      },
      addFiliale:function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/filialeAdd');
      },
      selectCity: function (valCol,val) {
        this.province = val;
        this.$http.post('/admin/district/findAllCities',{code:valCol})
          .then(function (res) {
            this.$data.citys = res.data;
            this.$data.isOpen1 = true;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      open:function () {
        console.log(this.isOpen)
        this.isOpen = !this.isOpen;
        this.isOpen1 = false;
        document.getElementById('select').style.borderColor = '#20a0ff';
      },
      select:function (valCode,val) {
        this.selectCon = val;
        this.query.districtCode = valCode;
        this.isOpen = !this.isOpen;
        this.isOpen1 = !this.isOpen1;
      },
      selectAll: function () {
        this.selectCon = val;
        this.query.districtCode = '';
        this.isOpen = !this.isOpen;
        this.isOpen1 = false;
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/queryOrder/orderList',this.query)
          .then(function (res) {
            this.loading = false;
            this.records = res.data.records;
            this.loading = false;
          },function (err) {
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })

      },
      search(){
        this.total = '';
        this.query.page = 1;
        this.getData();
      },
      //每页条数改变
      handleSizeChange(val) {
        this.query.page = 1;
        this.query.size = val;
        this.getData();
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.page = val;
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
