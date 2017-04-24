<template>
  <div id="dataHistory">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">认证资料历史</h3>
          <router-link to="/admin/record/issue" class="pull-right btn btn-primary" style="margin-left: 20px">分配二维码</router-link>
        </div>
        <div class="box-body">
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="distributeTime" label="名称"></el-table-column>
            <el-table-column prop="proxyName" label="更新时间"></el-table-column>
            <el-table-column prop="markCode" label="操作人"></el-table-column>
            <el-table-column label="照片">
              <template scope="scope">
                <span>点击查看</span>
              </template>
            </el-table-column>
          </el-table>
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
        records: [],
        loading: true,
        isMask:false
      }
    },
    created: function () {
      this.$http.post('/admin/user/distributeRecord',this.$data.query)
        .then(function (res) {
          this.loading = false;
          this.$data.records   = res.data.records;
        }, function (err) {
          this.loading = false;
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
    },
    methods: {
      changeBig: function (e) {
        e = e || window.event;
        var obj = e.srcElement || e.target;
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        img.src = obj.src;
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
