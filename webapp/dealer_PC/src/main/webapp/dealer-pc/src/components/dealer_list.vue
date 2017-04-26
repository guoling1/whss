<template lang="html">
  <div class="content-wrapper">
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
              <el-button type="primary" icon="plus" size="small" @click="_$power(addDealer,'dealer_add')">新增代理商</el-button>
              <!--<el-button type="primary" icon="plus" size="small" @click="addDealer">新增代理商</el-button>-->
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
              <el-table :data="tableData" border
                        v-loading="tableLoading"
                        element-loading-text="数据加载中">
                <el-table-column label="代理商名称">
                  <template scope="scope">
                    <el-button type="text" @click="checkDealer($event,scope.row.id)">{{scope.row.proxyName}}</el-button>
                  </template>
                </el-table-column>
                <el-table-column label="代理商编号">
                  <template scope="scope">
                    <el-button type="text" @click="checkDealer($event,scope.row.id)">{{scope.row.markCode}}</el-button>
                  </template>
                </el-table-column>
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
                                     @click="_$power(scope.row.id,scope.row.hssProductId,openHss,'dealer_product_add')">开通</el-button>
                        </span>
                        <span v-else>
                          <el-button type="text"
                                     @click="_$power(scope.row.id,scope.row.hssProductId,checkHss,'dealer_detail')">查看产品详情</el-button>
                        </span>
                      </span>
                      <span v-else>未开通</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="好收银" width="120">
                    <template scope="scope">
                      <span v-if="ext[1]!=0">
                        <span v-if="scope.row.hsyProductId==0">
                          <el-button size="small" type="primary"
                                     @click="_$power(scope.row.id,scope.row.hsyProductId,openHsy,'dealer_product_add')">开通</el-button>
                        </span>
                        <span v-else>
                          <el-button type="text" @click="_$power(scope.row.id,scope.row.hsyProductId,checkHsy,'dealer_detail')">查看产品详情</el-button>
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
        tableLoading: false,
        districtCode: '',
        ext: []
      }
    },
    created() {
      this.getData();
      this.$http.post('/daili/district/findAllProvinces').then(res => {
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
        this.$router.push('/daili/app/dealer_add');
      },
      getData: function () {
        this.tableLoading = true;
        this.$http.post('/daili/dealer/listSecondDealer', {
          pageSize: this.pageSize,
          pageNo: this.pageNo,
          name: this.name,
          markCode: this.markCode,
          sysType: this.sysType,
          districtCode: this.districtCode
        }).then(res => {
          this.tableLoading = false;
          this.total = res.data.count;
          this.tableData = res.data.records;
          this.ext = res.data.ext.split('|');
        }, err => {
          this.tableLoading = false;
          this.$message({
            showClose: true,
            message: err.data.msg,
            type: 'error'
          });
        });
      },
      province_select: function (provinceCode) {
        this.districtCode = provinceCode;
        this.$http.post('/daili/district/findAllCities', {
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
      checkDealer: function (event, id) {
        this.$router.push({path: '/daili/app/dealer_modify', query: {dealerId: id}});
      },
      openHss: function (id, productId) {
        this.$router.push({path: '/daili/app/product_add', query: {product: 'hss', dealerId: id, productId: productId}});
      },
      openHsy: function (id, productId) {
        this.$router.push({path: '/daili/app/product_add', query: {product: 'hsy', dealerId: id, productId: productId}});
      },
      checkHss: function (id, productId) {
        this.$router.push({path: '/daili/app/product_add', query: {product: 'hss', dealerId: id, productId: productId}});
      },
      checkHsy: function (id, productId) {
        this.$router.push({path: '/daili/app/product_add', query: {product: 'hsy', dealerId: id, productId: productId}});
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
