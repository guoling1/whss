<template lang="html">
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        钱包++代理商系统
        <small>Version 1.0</small>
      </h1>
      <ol class="breadcrumb">
        <li>
          <router-link to="/app/home"><i class="glyphicon glyphicon-home"></i> 主页</router-link>
        </li>
        <!--<li class="active">Dashboard</li>-->
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-header">
              <h3 class="box-title">代理商列表</h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body screen-top">
              <el-button type="primary" icon="plus" @click="addDealer">新增代理商</el-button>
            </div>
            <div class="box-body screen-top">
              <div class="screen-item">
                <span class="screen-title">代理商名称</span>
                <el-input v-model="name" placeholder="代理商名称" size="small" style="width:180px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">代理商编号</span>
                <el-input v-model="markCode" placeholder="代理商编号" size="small" style="width:180px"></el-input>
              </div>
              <div class="screen-item">
                <span class="screen-title">代理商类型</span>
                <el-select v-model="sysType" size="small" clearable placeholder="请选择">
                  <el-option v-for="item in sysTypes"
                             :label="item.label"
                             :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">代理商所在省份</span>
                <el-select v-model="provinceCode" size="small" clearable placeholder="请选择" @change="province_select">
                  <el-option v-for="item in item_province"
                             :label="item.aname"
                             :value="item.code">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title">代理商所在市区</span>
                <el-select v-model="cityCode" size="small" clearable placeholder="请选择" @change="city_select">
                  <el-option v-for="item in item_city"
                             :label="item.aname"
                             :value="item.code">
                  </el-option>
                </el-select>
              </div>
              <div class="screen-item">
                <span class="screen-title"></span>
                <el-button type="primary" size="small" @click="screen">筛选</el-button>
              </div>
            </div>
            <div class="box-body">
              <el-table :data="tableData" border>
                <el-table-column prop="proxyName" label="代理商名称"></el-table-column>
                <el-table-column prop="markCode" label="代理商编号" sortable="custom"></el-table-column>
                <el-table-column label="省市">
                  <template scope="scope">
                    {{scope.row.belongProvinceName}}{{scope.row.belongCityName}}
                  </template>
                </el-table-column>
                <el-table-column label="注册时间" width="180">
                  <template scope="scope">
                    {{ scope.row.createTime | datetime }}
                  </template>
                </el-table-column>
                <el-table-column prop="mobile" label="联系手机号"></el-table-column>
                <el-table-column label="产品">
                  <el-table-column label="好收收" width="120">
                    <template scope="scope">
                      <span v-if="ext[0]!=0">
                        <span v-if="scope.row.hssProductId==0">
                          <el-button size="small" type="primary"
                                     @click="openHss($event,scope.row.id,scope.row.hssProductId)">开通</el-button>
                        </span>
                        <span v-else>
                          <el-button type="text"
                                     @click="checkHsy($event,scope.row.id,scope.row.hssProductId)">查看产品详情</el-button>
                        </span>
                      </span>
                      <span v-else>未开通</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="好收银" width="120">
                    <template scope="scope">
                      <span v-if="ext[0]!=0">
                        <span v-if="scope.row.hsyProductId==0">
                          <el-button size="small" type="primary"
                                     @click="openHsy($event,scope.row.id,scope.row.hsyProductId)">开通</el-button>
                        </span>
                        <span v-else>
                          <el-button type="text"
                                     @click="checkHsy($event,scope.row.id,scope.row.hsyProductId)">查看产品详情</el-button>
                        </span>
                      </span>
                      <span v-else>未开通</span>
                    </template>
                  </el-table-column>
                </el-table-column>
              </el-table>
            </div>
            <div class="box-body">
              <el-pagination style="float:right"
                             @size-change="handleSizeChange"
                             @current-change="handleCurrentChange"
                             :current-page="pageNo"
                             :page-sizes="[20, 100, 200, 500]"
                             :page-size="pageSize"
                             layout="total, sizes, prev, pager, next, jumper"
                             :total="total">
              </el-pagination>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
  </div>
</template>
<script lang="babel">
  export default {
    name: 'app',
    data() {
      return {
        name: '',
        markCode: '',
        sysType: '',
        sysTypes: [
          {
            value: 'hss',
            label: '好收收'
          },
          {
            value: 'hsy',
            label: '好收银'
          }
        ],
        item_province: [],
        provinceCode: '',
        provinceName: '',
        item_city: [],
        cityCode: '',
        cityName: '',
        total: 0,
        pageSize: 20,
        pageNo: 1,
        tableData: [],
        districtCode: '',
        ext: []
      }
    },
    created() {
      this.getData();
      this.$http.post('/api/daili/district/findAllProvinces').then(res => {
        this.item_province = res.data;
      }, err => {
        this.$message({
          showClose: true,
          message: err.data.msg,
          type: 'error'
        });
      });
    },
    methods: {
      addDealer: function () {
        this.$router.push('/app/dealer_add');
      },
      getData: function () {
        this.$http.post('/api/daili/dealer/listSecondDealer', {
          pageSize: this.pageSize,
          pageNo: this.pageNo,
          name: this.name,
          markCode: this.markCode,
          sysType: this.sysType,
          districtCode: this.districtCode
        }).then(res => {
          this.total = res.data.count;
          this.tableData = res.data.records;
          this.ext = res.data.ext.split('|');
        }, err => {
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        });
      },
      province_select: function (provinceCode) {
        this.districtCode = provinceCode;
        this.$http.post('/api/daili/district/findAllCities', {
          code: provinceCode
        }).then(res => {
          this.item_city = res.data;
          this.cityCode = '';
          this.cityName = '';
        }, err => {
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        })
      },
      city_select: function (cityCode) {
        this.districtCode = cityCode;
      },
      screen: function () {
        this.getData();
      },
      openHss: function (event, id, productId) {
        this.$router.push({path: '/app/product_add', query: {product: 'hss', dealerId: id, productId: productId}});
      },
      openHsy: function (event, id, productId) {
        this.$router.push({path: '/app/product_add', query: {product: 'hsy', dealerId: id, productId: productId}});
      },
      checkHss: function (event, id, productId) {
        this.$router.push({path: '/app/product_add', query: {product: 'hss', dealerId: id, productId: productId}});
      },
      checkHsy: function (event, id, productId) {
        this.$router.push({path: '/app/product_add', query: {product: 'hsy', dealerId: id, productId: productId}});
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.getData();
      },
      handleCurrentChange(val) {
        this.pageNo = val;
        this.getData();
      }
    }
  }
</script>
<style scoped lang="less">
  .screen-top {
    padding-top: 0 !important;
  }

  .screen-item {
    float: left;
    margin-right: 10px;
  }

  .screen-title {
    display: block;
    height: 24px;
    line-height: 24px;
  }
</style>
