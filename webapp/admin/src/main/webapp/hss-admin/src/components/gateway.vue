<template>
  <div id="gateway">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">网关模板</h3>
          <a href="javascript:window.close();" class="pull-right btn btn-primary" style="margin-left: 15px">关闭</a>
          <router-link :to='"/admin/details/gatewayAdd?id="+dealerId+"&productId="+productId+"&proxyName="+name' class="btn btn-primary" style="float: right;margin-right: 15px">新增网关通道</router-link>
          <!--<a href="javascript:window.close();" class="pull-right btn btn-primary" style="margin-left: 15px">关闭</a>-->
          <!--<router-link to="/admin/details/gatewayAdd" class="pull-right btn btn-primary">新增网关通道</router-link>-->
        </div>
        <div class="box-body">
          <ul>
            <li class="same" v-if="name!=undefined&&name!=''">
              <label class="title">分公司名称:</label>
              <span>{{name}}</span>
            </li>
            <li class="same" v-if="name!=undefined&&name!=''">
              <label class="title">分公司编码:</label>
              <span>{{code}}</span>
            </li>
            <li class="same">
              <label class="title">网关模板:</label>
              <el-table max-height="637" style="font-size:12px;width:80%;display: inline-table;vertical-align: top" :data="$$records" border>
                <el-table-column type="index" width="70" label="序号"></el-table-column>
                <el-table-column prop="viewChannelName" label="展示名称"></el-table-column>
                <el-table-column prop="channelShortName" label="通道名称"></el-table-column>
                <el-table-column label="操作" min-width="100">
                  <template scope="scope">
                    <el-button type="text" @click="detail(dealerId,scope.row.productId,name,scope.$index)">修改</el-button>
                    <el-button type="text" @click="open(scope.row.id)" v-if="scope.row.recommend==0&&(name==undefined||name=='')">推荐</el-button>
                    <el-button type="text" @click="close(scope.row.id)" v-if="scope.row.recommend!=0&&(name==undefined||name=='')">取消推荐</el-button>
                    <el-button type="text" @click="delt(scope.row.id)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </li>
            <!--<li class="same">-->
              <!--<label class="title"></label>-->
              <!--<el-button type="primary" @click="submit">保存更改</el-button>-->
            <!--</li>-->
          </ul>
        </div>
        <div class="box box-info mask el-message-box" v-if="isRecommend">
          <div class="maskCon">
            <div class="head">
              <div class="title">提示</div>
              <i class="el-icon-close" @click="isRecommend=false"></i>
            </div>
            <div class="body">
              <div>确认推荐该通道吗？</div>
            </div>
            <div class="foot">
              <a href="javascript:void(0)" @click="isRecommend=false" class="el-button el-button--default">取消</a>
              <a @click="confirm('1')" class="el-button el-button-default el-button--primary">确定</a>
            </div>
          </div>
        </div>
        <div class="box box-info mask el-message-box" v-if="isNoRecommend">
          <div class="maskCon">
            <div class="head">
              <div class="title">提示</div>
              <i class="el-icon-close" @click="isNoRecommend=false"></i>
            </div>
            <div class="body">
              <div>确认取消推荐该通道吗？</div>
            </div>
            <div class="foot">
              <a href="javascript:void(0)" @click="isNoRecommend=false" class="el-button el-button--default">取消</a>
              <a @click="confirm('')" class="el-button el-button-default el-button--primary">确定</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'gateway',
    data(){
      return {
        records: [],
        isRecommend:false,
        isNoRecommend:false,
        id:'',
        dealerId:0,
        name:'',
        code:'',
        productId:0
      }
    },
    created: function () {
      if(this.$route.query.id==undefined){
        this.dealerId = 0;
        this.name = '';
        this.code = '';
        this.productId = this.$route.query.productId;
      }else {
        this.dealerId = this.$route.query.id;
        this.name = this.$route.query.proxyName;
        this.code = this.$route.query.markCode;
        this.productId = this.$route.query.productId;
      }
      this.getData();
    },
    methods: {
      delt(id) {
        this.$confirm('此网关将被删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('/admin/product/delGateway',{id:id})
            .then(()=>{
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.getData()
            })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      getData:function () {
        this.$http.post('/admin/product/listGateway',{"productType":"hss","dealerId":this.dealerId})
          .then(res => {
            this.records = res.data;
          })
          .catch(err => {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            });
          })
      },
      detail: function ( id, productId,name, index) {
        if(this.name!=''){
          this.$router.push({path:'/admin/details/gatewayAdd',query:{id:this.dealerId,productId:this.productId,proxyName:this.name,index:index}})
        }else {
          this.$router.push({path:'/admin/details/gatewayAdd',query:{productId:productId,index:index}})
        }
      }
    },
    computed:{
      $$records:function () {
        return this.records;
      }
    }
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    margin: 0 15px 15px 0;
    .title{
      width: 80px;
      margin-right: 15px;
      text-align: right;
    }
  }
  .btn{
    font-size: 12px;
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
