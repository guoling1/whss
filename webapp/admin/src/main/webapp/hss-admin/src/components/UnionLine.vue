<template>
  <div id="unionLine">
    <div class="col-md-12">
      <div class="box" style="overflow: hidden" v-if="!isAdd">
        <div class="box-header">
          <h3 class="box-title">联行号管理</h3>
          <el-button type="text" class="pull-right" @click="isAdd=true">新增联行号</el-button>
        </div>
        <div class="box-body">
          <ul class="search">
            <li class="same">
              <label>省:</label>
              <el-select v-model="query.province" size="small" style="width:100%" placeholder="请选择" clearable
                         @change="province_select1">
                <el-option v-for="item in list_province"
                           :label="item.aname"
                           :value="item.code">
                </el-option>
              </el-select>
              <!--<el-select style="width: 193px" clearable v-model="query.province" size="small">-->
                <!--<el-option label="全部" value="">全部</el-option>-->
              <!--</el-select>-->
            </li>
            <li class="same">
              <label>市:</label>
              <el-select v-model="query.city" size="small" style="width:100%" placeholder="请选择" clearable :disabled="isSelect"
                         @change="city_select1">
                <el-option v-for="item in list_city"
                           :label="item.aname"
                           :value="item.code">
                </el-option>
              </el-select>
              <!--<el-select style="width: 193px" clearable v-model="query.city" size="small">
                <el-option label="全部" value="">全部</el-option>
              </el-select>-->
            </li>
            <li class="same">
              <label>支行:</label>
              <el-input style="width: 193px" v-model="query.branchName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>联行号:</label>
              <el-input style="width: 193px" v-model="query.branchCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
              <div class="btn btn-primary" @click="reset">重置</div>
            </li>
          </ul>
          <!--表格-->
          <el-table v-loading.body="loading" style="font-size: 12px;margin-bottom: 15px;" :data="records" border>
            <el-table-column width="62" label="序号" type="index"></el-table-column>
            <el-table-column prop="branchCode" label="联行号"></el-table-column>
            <el-table-column prop="province" label="省"></el-table-column>
            <el-table-column prop="city" label="市"></el-table-column>
            <!--<el-table-column prop="userType" label="区/县"></el-table-column>-->
            <el-table-column prop="bankName" label="银行名称"></el-table-column>
            <el-table-column prop="branchName" label="支行名称"></el-table-column>
            <!--<el-table-column prop="userType" label="银行简称"></el-table-column>-->
            <!--<el-table-column prop="userType" label="银行编码"></el-table-column>-->
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
      <div class="box" style="padding: 15px 15px 20px 10%" v-if="isAdd">
        <i class="el-icon-circle-cross pull-right" style="color: #8f9092;font-size: 18px;cursor: pointer" @click="closeAdd"></i>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" class="demo-ruleForm" style="margin-right: 30%">
          <el-form-item label="银行名称:" prop="bankName">
            <el-autocomplete v-model="form.bankName" :fetch-suggestions="querySearchAsync" size="small" placeholder="输入匹配"></el-autocomplete>
          </el-form-item>
          <el-form-item label="所在省市" prop="city">
            <el-col :span="10">
              <el-select v-model="form.province" size="small" style="width:100%" placeholder="请选择"
                         @change="province_select">
                <el-option v-for="item in item_province"
                           :label="item.aname"
                           :value="item.code">
                </el-option>
              </el-select>
            </el-col>
            <el-col class="line-center" :span="2">省</el-col>
            <el-col :span="10">
              <el-select v-model="form.city" size="small" style="width:100%" placeholder="请选择"
                         @change="city_select">
                <el-option v-for="item in item_city"
                           :label="item.aname"
                           :value="item.code">
                </el-option>
              </el-select>
            </el-col>
            <el-col class="line-center" :span="2">市</el-col>
          </el-form-item>
          <el-form-item label="支行名称" prop="branchName">
            <el-input v-model="form.branchName" size="small"></el-input>
          </el-form-item>
          <el-form-item label="联行号" prop="branchCode">
            <el-input v-model="form.branchCode" size="small"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="onSubmit">新增</el-button>
          </el-form-item>
        </el-form>
        <!--确认新增页面-->
        <el-dialog title="新增联行号" :visible.sync="dialogFormVisible">
          <el-form :model="form" style="margin-left: 10%">
            <el-form-item label="银行名称：" :label-width="formLabelWidth" style="margin-bottom: 0">
              中国农业银行
            </el-form-item>
            <el-form-item label="所在省：" :label-width="formLabelWidth" style="margin-bottom: 0">
              北京
            </el-form-item>
            <el-form-item label="所在市：" :label-width="formLabelWidth" style="margin-bottom: 0">
              北京
            </el-form-item>
            <el-form-item label="支行名称：" :label-width="formLabelWidth" style="margin-bottom: 0">
              中国农业银行东城区支行
            </el-form-item>
            <el-form-item label="联行号：" :label-width="formLabelWidth" style="margin-bottom: 0">
              001200889814
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible = false">返回修改</el-button>
            <el-button type="primary" @click="onSubmit">提交保存</el-button>
          </div>
        </el-dialog>
      </div>
    </div>

  </div>
</template>

<script lang="babel">
  export default{
    name: 'unionLine',
    data(){
      return {
        isSelect:false,
        query:{
          pageNo:1,
          pageSize:10,
          province:'',
          provinceName:'',
          city:'',
          branchName:'',
          branchCode: '',
          cityName:''
        },
        records: [],
        count: 0,
        total: '',
        loading: true,
        isAdd:false,
        dialogFormVisible:false,
        bankName:"",
        form: {
          bankName: '',
          province:'',
          city:'',
          branchName:'',
          branchCode:'',
          belongProvinceName: '',
          belongCityName: '',
        },
        rules: {
          bankName: [
            {required: true, message: '请输入银行名称', trigger: 'blur'}
          ],
          branchName: [
            {required: true, message: '请输入支行名称', trigger: 'blur'}
          ],
          branchCode: [
            {required: true, message: '请输入联行号', trigger: 'blur'}
          ],
          city: [
            {required: true, message: '请选择代理商所在省市', trigger: 'blur'}
          ]
        },
        list_province:[],
        list_city:[],
        item_province: [],
        item_city: [],
        restaurants: [],
      }
    },
    created: function () {
//      获取省份
      this.$http.post('/admin/unionNumber/findAllProvinces').then(res => {
        this.list_province = res.data;
        this.item_province = res.data;
//        this.form.province = res.data[0].code;
//        this.form.belongProvinceName = res.data[0].aname;
      }, err => {
        this.$message({
          showClose: true,
          message: err.data.msg,
          type: 'error'
        });
      });
      this.getData();
    },
    methods: {
      reset: function () {
        this.query = {
          pageNo:1,
          pageSize:10,
          province:'',
          provinceName:'',
          city:'',
          branchName:'',
          branchCode: '',
          cityName:''
        };
      },
      closeAdd: function () {
        this.isAdd = false
        this.form={
          bankName: '',
          province:'',
          city:'',
          branchName:'',
          branchCode:'',
          belongProvinceName: '',
          belongCityName: '',
        }
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/unionNumber/unionInfo',this.query)
          .then(function (res) {
            setTimeout(()=>{
              this.loading = false;
              this.records = res.data.records;
            },1000)
            this.count = res.data.count;
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
      province_select: function (provinceCode) {
        for (let m = 0; m < this.item_province.length; m++) {
          if (this.item_province[m].code == provinceCode) {
            this.form.belongProvinceName = this.item_province[m].aname;
          }
        }
        if(this.form.belongProvinceName=="北京市"||this.form.belongProvinceName=="天津市"||this.form.belongProvinceName=="上海市"||this.form.belongProvinceName=="重庆市"){
          this.item_city = [{
            code:this.form.province,
            aname:this.form.belongProvinceName
          }]
          this.form.city = this.item_city[0].code;
          this.form.belongCityName = this.item_city[0].aname;
        }else{
          this.$http.post('/admin/unionNumber/findAllCities', {
            code: provinceCode
          }).then(res => {
            this.item_city = res.data;
            this.form.city = res.data[0].code;
            this.form.belongCityName = res.data[0].aname;
          }, err => {
            this.$message({
              showClose: true,
              message: err.data.msg,
              type: 'error'
            });
          })
        }
      },
      province_select1: function (provinceCode) {
        for (let m = 0; m < this.list_province.length; m++) {
          if (this.list_province[m].code == provinceCode) {
            this.query.provinceName = this.list_province[m].aname;
          }
        }
        if(this.query.provinceName=="北京市"||this.query.provinceName=="天津市"||this.query.provinceName=="上海市"||this.query.provinceName=="重庆市"){
          this.isSelect = true;
          this.query.city = '';
          this.query.cityName = '';
        }else {
          this.isSelect = false;
          this.$http.post('/admin/unionNumber/findAllCities', {
            code: provinceCode
          }).then(res => {
            this.list_city = res.data;
            this.query.city = '';
          }, err => {
            this.$message({
              showClose: true,
              message: err.data.msg,
              type: 'error'
            });
          })
        }
      },
      city_select: function (cityCode) {
        for (let n = 0; n < this.item_city.length; n++) {
          if (this.item_city[n].code == cityCode) {
            this.form.belongCityName = this.item_city[n].aname;
          }
        }
      },
      city_select1: function (cityCode) {
        for (let n = 0; n < this.list_city.length; n++) {
          if (this.list_city[n].code == cityCode) {
            this.query.cityName = this.list_city[n].aname;
          }
        }
      },
      onSubmit: function () {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.$http.post('/admin/unionNumber/addBankCode', this.form).then(res => {
              this.$message({
                showClose: true,
                message: '创建代理商成功',
                type: 'success'
              });
              this.form={
                bankName: '',
                province:'',
                city:'',
                branchName:'',
                branchCode:'',
                belongProvinceName: '',
                belongCityName: '',
              }
              this.dialogFormVisible = true;
            }, err => {
              this.$message({
                showClose: true,
                message: err.data.msg,
                type: 'error'
              });
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      querySearchAsync(queryString, cb) {
        var restaurants = this.restaurants;
        var results=[];
        this.$http.post('/admin/unionNumber/bankName',{bankName:queryString})
          .then(res=>{
            for(let i=0; i<res.data.length; i++){
              res.data[i].value = res.data[i].bankName;
            }
            results = res.data;
          })
          .catch(err=>{

          });
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          cb(results);
        }, 1000 * Math.random());
      },
      createStateFilter(queryString) {
        return (state) => {
          return (state.value.indexOf(queryString.toLowerCase()) === 0);
        };
      },
      search(){
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
      }
    }
  }
</script>

<style scoped lang="less" rel="stylesheet/less">
  .search {
    label{
      display: block;
      margin-bottom: 0;
    }
  }
  ul{
    padding: 0;
    margin: 0;
  }
  .same {
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }

  .price {
    display: inline-block;
    width: 210px;
    height: 30px;
    border-radius: 4px;
    border-color: #bfcbd9;
    position: relative;
    top: 6px;
    input {
      border: none;
      display: inline-block;
      width: 45%;
      height: 25px;
      position: relative;
      top: -3px;
    }
  }

  .price:hover {
    border-color: #20a0ff;
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
