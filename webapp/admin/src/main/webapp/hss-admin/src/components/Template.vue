<template lang="html">
  <div id="template">
    <div style="margin-top: 15px" class="col-xs-12">
      <div class="box">
        <div class="box-header">
          <h3 class="box-title">代理通道模板</h3>
          <router-link class="btn btn-primary pull-right" to="/admin/record/templateAdd" style="margin: 0 15px">
            新增代理政策
          </router-link>
        </div>
        <div class="box-body">
          <el-table max-height="637" style="font-size: 12px;margin-bottom: 15px;width: 70%" :data="products" border>
            <el-table-column prop="productName" label="产品名称"></el-table-column>
            <el-table-column prop="productName" label="模板名称"></el-table-column>
            <el-table-column label="操作" min-width="100">
              <template scope="scope">
                <el-button type="text" @click="detail(scope.$index)">查看详情</el-button>
                <el-button type="text" @click="setup(scope.$index)">配置</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'template',
    data () {
      return {
        mobile: '',
        name: '',
        belongArea: '',
        bankCard: '',
        bankAccountName: '',
        bankReserveMobile: '',
        product: {
          channels: []
        },
        products: [],//所有产品
        id: 0
      }
    },
    created: function () {
      this.$http.post('/admin/product/list')
        .then(function (res) {
          this.$data.products = res.data;
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          });
        })
    },
    methods: {
      setup:function () {
        this.$router.push('/admin/record/templateAdd')
      },
      create: function () {
        this.$http.post('/admin/user/addFirstDealer',{})
          .then(function (res) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: "添加成功"
            })
          }, function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      detail: function (val) {
        this.$router.push({path:'/admin/record/templateAdd',query:{id:val}})
      }
    },
    computed: {
      $$products: function () {
        return this.$data.products
      },
      $$data: function () {
        return this.$data;
      }
    },
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
    margin: 0 10px;
  }

  .btn{
    font-size: 12px;
  }

  .product .table {
    display: inline-block;
    font-size: 12px;
    width: 90%;

  thead tr, caption {
    font-size: 8px;
    color: #777;
    background: #ccc
  }

  td {
    width: 16.5%;

  input {
    width: 77%;
    border: none;
  }

  }
  }
</style>
