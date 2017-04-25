<template>
  <div id="dataHistory">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">认证资料历史</h3>
        </div>
        <div class="box-body">
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="cardName" label="名称"></el-table-column>
            <el-table-column prop="createTimes" label="更新时间"></el-table-column>
            <el-table-column prop="operator" label="操作人"></el-table-column>
            <el-table-column label="照片">
              <template scope="scope">
                <el-button type="text" @click="changeBig(scope.row.pohto)">点击查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
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
        <div class="mask" id="mask" style="display: none" @click="isNo()">
          <p @click="isNo">×</p>
          <img src="" alt="">
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'dataHistory',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          merchantId:""
        },
        records: [],
        count: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {
      this.query.merchantId = this.$route.query.merchantId;
      this.getData();
    },
    methods: {
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/photoChange/selectHistory',this.query)
          .then(function (res) {
            this.loading = false;
            this.count = res.data.count;
            this.records = res.data.records;
          }, function (err) {
            this.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      handleSizeChange(val) {
        this.query.page = 1;
        this.query.size = val;
        this.getData();
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.query.page = val;
        this.getData()
      },
      changeBig: function (val) {
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        img.src = val;
        mask.style.display = 'block'
      },
      isNo: function () {
        document.getElementById('mask').style.display = 'none'
      }
    }
  }
</script>
<style scoped lang="less">
  .btn{
    font-size: 12px;
  }
  .mask {
    background: rgba(0, 0, 0, 0.8);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
  p {
    position: absolute;
    top: 20px;
    right: 20px;
    z-index: 1200;
    width: 65px;
    height: 65px;
    line-height: 55px;
    font-size: 65px;
    color: #d2d1d1;
    text-align: center;
    border: 6px solid #adaaaa;
    border-radius: 50%;
    box-shadow: 0 0 16px #000;
    text-shadow: 0 0 16px #000;
  }

  img {
    display: inherit;
    height: 100%;
    margin: 0 auto;
  }
  }
</style>
