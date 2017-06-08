<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">一级代理商列表</h3>
          <!--<router-link to="/admin/record/agentAddBase" class="btn btn-primary" style="float: right;color: #fff">新增代理商</router-link>-->
          <a @click="_$power(agentAdd,'boss_first_add')" class="btn btn-primary" style="float: right;color: #fff">新增代理商</a>
        </div>
        <div class="box-body">
          <!--筛选-->
          <el-row :gutter="21" style="width: 950px">
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
            <el-col :span="4">
              <label>省市:</label>
              <div class="select" id="select" @click="open"><span style="color: #1f2d3d">{{selectCon}}</span>
                <i class="el-icon-caret-bottom" style="float: right;margin-top: 10px"></i>
              </div>
              <ul class="isShow" v-if="isOpen">
                <li @click="selectAll()">全部</li>
                <li v-for="province in provinces" @mouseover="selectCity(province.code,province.name)" @click="select(province.code,province.name)">{{province.name}}
                </li>
              </ul>
              <ul class="isShow1" v-if="isOpen1">
                <li :class="'cityLi'+$index" v-for="city in citys" @click="select(city.code,city.aname)">{{city.aname}}</li>
              </ul>
            </el-col>
            <el-col  :span="4">
              <label>代理产品:</label>
              <el-select clearable v-model="query.sysType" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="好收银" value="hsy">好收银</el-option>
                <el-option label="好收收" value="hss">好收收</el-option>
              </el-select>
            </el-col>
            <el-col  :span="4" style="margin-top: 18px">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </el-col>
          </el-row>
          <!--表格-->
          <el-table v-loading.body="loading"  style="font-size: 12px;margin:15px 0" :data="records" border >
            <el-table-column label="代理商名称">
              <template scope="scope">
                <router-link target="_blank" :to="'/admin/details/agentAddBase?level=1&id='+records[scope.$index].id">{{records[scope.$index].proxyName}}</router-link>
              </template>
            </el-table-column>
            <el-table-column label="代理商编号">
              <template scope="scope">
                <router-link target="_blank" :to="'/admin/details/agentAddBase?level=1&id='+records[scope.$index].id">{{records[scope.$index].markCode}}</router-link>
              </template>
            </el-table-column>
            <el-table-column prop="level" label="代理商级别" ></el-table-column>
            <el-table-column prop="belong" label="省市" ></el-table-column>
            <el-table-column prop="createTime" label="注册时间" ></el-table-column>
            <el-table-column prop="mobile" label="联系手机号" ></el-table-column>
            <el-table-column label="好收收">
              <template scope="scope">
                <a @click="_$power(scope.row.id,scope.row.hssProductId,'hss',openProduct,'boss_first_product_add')" v-if="records[scope.$index].hssProductId==0">开通产品</a>
                <a @click="_$power(scope.row.id,scope.row.hssProductId,'hss',auditProduct,'boss_first_product_update_btn')" v-else="records[scope.$index].hssProductId==0">修改产品设置</a>
              </template>
            </el-table-column>
            <el-table-column label="好收银" >
              <template scope="scope">
                <a @click="_$power(scope.row.id,scope.row.hsyProductId,'hsy',openProductHsy,'boss_first_product_add')" v-if="records[scope.$index].hsyProductId==0">开通产品</a>
                <a @click="_$power(scope.row.id,scope.row.hsyProductId,'hsy',auditProductHsy,'boss_first_product_update_btn')" v-else="records[scope.$index].hsyProductId==0">修改产品设置</a>
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
          label:'name',
          children: 'list'
        },
        isOpen:false,
        isOpen1:false,
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
        query:{
          pageNo:1,
          pageSize:10,
          mobile:'',
          name:"",
          markCode:"",  //商户名字
          sysType:"",
          districtCode:""
        },
        isShow:false,
        index:'',
        selectCon:'全部'
      }
    },
    created: function () {
      //搜索区省市联动
      this.$http.post('/admin/district/findAllDistrict')
        .then(function (res) {
          this.$data.provinces = res.data;
        })
        .catch(function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        });
      this.getData()
    },
    methods: {
      reset: function () {
        this.selectCon = '全部';
        this.query = {
          pageNo:1,
          pageSize:10,
          mobile:'',
          name:"",
          markCode:"",  //商户名字
          sysType:"",
          districtCode:""
        };
      },
      openProduct:function (id,productId,val) {
        window.open('http://admin.qianbaojiajia.com/admin/details/agentAddPro?dealerId='+id+'&productId='+productId+'&product='+val);
//        this.$router.push({path:'/admin/record/agentAddPro',query:{dealerId:idproductId:productId,product:val}});
      },
      openProductHsy:function (id,productId,val) {
        window.open('http://admin.qianbaojiajia.com/admin/details/agentAddProHsy?dealerId='+id+'&productId='+productId+'&product='+val);
      },
      auditProduct:function (id,productId,val) {
        window.open('http://admin.qianbaojiajia.com/admin/details/agentAddPro?dealerId='+id+'&productId='+productId+'&product='+val);
//        this.$router.push({path:'/admin/record/agentAddPro',query:{dealerId:id,productId:productId,product:val}});
      },
      auditProductHsy:function (id,productId,val) {
        window.open('http://admin.qianbaojiajia.com/admin/details/agentAddProHsy?dealerId='+id+'&productId='+productId+'&product='+val);
      },
      agentAdd:function () {
        window.open('http://admin.qianbaojiajia.com/admin/details/agentAddBase');
//        this.$router.push("/admin/record/agentAddBase")
      },
      getData: function () {
        this.$data.loading = true;
        this.$http.post('/admin/dealer/listFirstDealer', this.$data.query)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.$data.records = res.data.records;
          },1000)
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            this.$data.pageSize = res.data.pageSize;
            var changeTime = function (val) {
              if (val == '' || val == null) {
                return ''
              } else {
                val = new Date(val)
                var year = val.getFullYear();
                var month = val.getMonth() + 1;
                var date = val.getDate();

                function tod(a) {
                  if (a < 10) {
                    a = "0" + a
                  }
                  return a;
                }

                return year + "-" + tod(month) + "-" + tod(date);
              }
            }
            for (var i = 0; i < res.data.records.length; i++) {
              res.data.records[i].createTime = changeTime(res.data.records[i].createTime)
              if (res.data.records[i].belongProvinceName != null && res.data.records[i].belongCityName != null) {
                res.data.records[i].belong = res.data.records[i].belongProvinceName + "-" + res.data.records[i].belongCityName;
              }
            }
          }, function (err) {
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
      selectCity: function (valCol,val) {
        this.$data.province = val;
        this.$http.post('/admin/district/findAllCities',{code:valCol})
          .then(function (res) {
            this.$data.citys = res.data;
            this.$data.isOpen1 = true;
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      open:function () {
        this.$data.isOpen = !this.$data.isOpen;
        this.$data.isOpen1 = false;
        document.getElementById('select').style.borderColor = '#20a0ff';
      },
      select:function (valCode,val) {
//        var oCon = document.getElementById('select').getElementsByTagName('span')[0];
//        var oCon = document.getElementById('selectCol');
        this.selectCon = val;
//        oCon.innerHTML = val;
//        oCon.style.color = '#1f2d3d';
        this.$data.query.districtCode = valCode;
        this.$data.isOpen = !this.$data.isOpen;
        this.$data.isOpen1 = !this.$data.isOpen1;
      },
      selectAll: function () {
//        var oCon = document.getElementById('select').getElementsByTagName('span')[0];
//        var oCon = document.getElementById('selectCol');
//        oCon.innerHTML = '全部';
        this.selectCon = '全部';
//        oCon.style.color = '#1f2d3d';
        this.$data.query.districtCode = '';
        this.$data.isOpen = !this.$data.isOpen;
        this.$data.isOpen1 = false;
      },
      search: function () {
        this.$data.query.pageNo = 1;
        this.getData()
      },
      list: function (val) {
        this.$data.index = val;
        this.$data.isShow = true;
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
    watch:{
      date:function (val,oldVal) {
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
              this.$data.query.startSettleDate = str;
            } else {
              this.$data.query.endSettleDate = str;
            }
          }
        } else {
          this.$data.query.startSettleDate = '';
          this.$data.query.endSettleDate = '';
        }
      }
    }
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
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
    top: 0100%;
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
