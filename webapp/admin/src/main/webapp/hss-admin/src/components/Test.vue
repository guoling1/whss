
<!--二维码分配记录-->
<!--<template>
  <div id="issueRecord">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">二维码分配记录</h3>
        </div>
        <div class="box-body">
          &lt;!&ndash;筛选&ndash;&gt;
          <ul>
            <li class="same">
              <label>代理商编号:</label>
              <el-input style="width: 120px" v-model="query.markCode" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>代理商名称:</label>
              <el-input style="width: 120px" v-model="query.merchantName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>上级代理名称:</label>
              <el-input style="width: 120px" v-model="query.proxyName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>上级代理编号:</label>
              <el-input style="width: 120px" v-model="query.proxyName1" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>分配方:</label>
              <el-select style="width: 160px" v-model="query.status" clearable placeholder="请选择" size="small">
                <el-option label="全部" value="">全部</el-option>
                <el-option label="金开门" value="0">金开门</el-option>
                <el-option label="一级代理" value="1">一级代理</el-option>
              </el-select>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          &lt;!&ndash;表格&ndash;&gt;
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border>
            <el-table-column type="index" width="70" label="序号"></el-table-column>
            <el-table-column prop="markCode" label="分配时间"></el-table-column>
            <el-table-column prop="merchantName" label="代理名称"></el-table-column>
            <el-table-column prop="proxyName" label="代理商编号"></el-table-column>
            <el-table-column prop="proxyName1" label="上级名称"></el-table-column>
            <el-table-column prop="proxyName1" label="上级编号"></el-table-column>
            <el-table-column prop="proxyName1" label="分配个数"></el-table-column>
            <el-table-column prop="proxyName1" label="起始码"></el-table-column>
            <el-table-column prop="proxyName1" label="终止码"></el-table-column>
            <el-table-column prop="proxyName1" label="操作人"></el-table-column>
          </el-table>
          &lt;!&ndash;分页&ndash;&gt;
          <div class="block" style="text-align: right">
            <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" layout="total, prev, pager, next, jumper" :total="count">
            </el-pagination>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'issueRecord',
    data(){
      return {
        query:{
          pageNo:1,
          pageSize:10,
          shortName:'',
          globalID:'',
          proxyName:'',
          proxyName1:'',
          startTime:'',
          endTime:'',
          startTime1:'',
          endTime1:'',
          startTime2:'',
          endTime2:'',
          status:''
        },
        records: [],
        count: 0,
        total: 0,
        currentPage: 1,
        loading: true,
      }
    },
    created: function () {

    },
    methods: {
      //格式化hss创建时间
      changeTime: function (row, column) {
        var val=row.createTime;
        if(val==''||val==null){
          return ''
        }else {
          val = new Date(val)
          var year=val.getFullYear();
          var month=val.getMonth()+1;
          var date=val.getDate();
          var hour=val.getHours();
          var minute=val.getMinutes();
          var second=val.getSeconds();
          function tod(a) {
            if(a<10){
              a = "0"+a
            }
            return a;
          }
          return year+"-"+tod(month)+"-"+tod(date)+" "+tod(hour)+":"+tod(minute)+":"+tod(second);
        }
      },
      search(){
        this.$data.query.pageNo = 1;
        this.$data.loading = true;
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      //当前页改变时
      handleCurrentChange(val) {
        this.$data.query.pageNo = val;
        this.$data.loading = true;
        this.$data.records = '';
        this.$http.post(this.$data.url,this.$data.query)
          .then(function (res) {
            this.$data.loading = false;
            this.$data.records   = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
          }, function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
    },
    watch:{

    },
  }
</script>
<style scoped lang="less">
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }
</style>-->

<!--hss商户详情-->
<template lang="html">
  <div id="storeAudit">
    <div class="box-header with-border" style="margin: 0 0 0 3px;">
      <h3 v-if="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户审核</h3>
      <h3 v-else="isShow" class="box-title" style="border-left: 3px solid #e4e0e0;padding-left: 10px;">商户资料</h3>
    </div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead">商户注册信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">注册手机:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.mobile" readonly></td>
              <th style="text-align: right">注册时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.createTime|changeTime" readonly></td>
              <th style="text-align: right">注册方式:</th>
              <td><input type="text" v-if="msg.source==0" style="background:#efecec;padding-left:5px;" value="扫码注册" readonly><input type="text" v-if="msg.source==1" style="background:#efecec;padding-left:5px;" value="商户推荐注册" readonly><input type="text" v-if="msg.source==2" style="background:#efecec;padding-left:5px;" value="代理商推荐注册" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">一级代理编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.markCode1==0?'':msg.markCode1" readonly></td>
              <th style="text-align: right">一级代理名称:</th>
              <td>
                <input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName" readonly>
                <!--<a href="javascript:;" @click="dealerMask=true">修改代理商归属</a>-->
              </td>
              <th></th>
              <td></td>
            </tr>
            <el-dialog title="修改商户归属信息" v-model="dealerMask">
              <el-form :label-position="right" label-width="150px">
                <el-form-item label="当前一级代理：" width="120">
                  {{msg.proxyName}}
                </el-form-item>
                <el-form-item label="当前二级代理：" width="120">
                  {{msg.proxyName1}}
                </el-form-item>
                <el-form-item label="切换类型：" width="120">
                  <el-select size="small" placeholder="请选择">
                    <el-option label="切换金开门直属" value="shanghai"></el-option>
                    <el-option label="切换为一级直属" value="beijing"></el-option>
                    <el-option label="切换到二级" value="beijing"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="切换类型：" width="120">
                  <el-input style="width: 70%" size="small" placeholder="请输入代理商编号，切换为金开门直属无需输入"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer" style="text-align: center">
                <!--<el-button @click="dealerMask = false" >取 消</el-button>-->
                <el-button type="primary" style="width: 200px;margin-top: -50px;position: relative;top: -30px;">确 定</el-button>
                <div style="text-align: center;margin-bottom: 10px">切换成功后，新产生的收款立即生效</div>
              </div>
            </el-dialog>
            <tr>
              <th style="text-align: right">二级代理编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.markCode2==0?'':msg.markCode2" readonly></td>
              <th style="text-align: right">二级代理名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyName1" readonly></td>
              <th></th>
              <td></td>
            </tr>
            <tr>
              <th style="text-align: right">推荐人编号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.recommenderCode" readonly></td>
              <th style="text-align: right">推荐人名称:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.recommenderName" readonly></td>
              <th style="text-align: right">推荐人注册手机号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.recommenderPhone" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">推荐所属一级代理名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyNameYq" readonly></td>
              <th style="text-align: right">推荐所属二级代理名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.proxyNameYq1" readonly></td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户认证信息</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">真实姓名:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.name" readonly></td>
              <th style="text-align: right">身份证号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.identity" readonly></td>
              <th style="text-align: right">商户名称:</th>
              <td>
                <input type="text" style="background:#efecec;padding-left:5px;" :value="msg.merchantName" readonly>
              </td>
            </tr>
            <tr>
              <th style="text-align: right">经营种类:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" value="—" readonly></td>
              <th style="text-align: right">商户详细地址:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.address" readonly></td>
              <th style="text-align: right">结算卡号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.bankNo" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">所属银行:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.bankName" readonly></td>
              <th style="text-align: right">开户手机号:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.reserveMobile" readonly></td>
              <th style="text-align: right">实名认证时间:</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.authenticationTime" readonly></td>
            </tr>
            <tr>
              <th style="text-align: right">认证状态</th>
              <td><input type="text" style="background:#efecec;padding-left:5px;" :value="msg.isAuthen" readonly></td>
              <th style="text-align: right">店铺上报名称:</th>
              <td>
                <input type="text" style="background:#efecec;padding-left:5px;" :value="msg.merchantChangeName" readonly>
                <el-button type="text" @click="reset" v-if="!isShow">修改上报名称</el-button>
              </td>
              <th style="text-align: right"></th>
              <td></td>
            </tr>
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">商户认证资料</p>
        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr class="row">
              <th class="col-md-3" style="text-align: center;">身份证正面:</th>
              <th class="col-md-3" style="text-align: center;">身份证反面:</th>
              <th class="col-md-3" style="text-align: center;">手持身份证:</th>
              <th class="col-md-3" style="text-align: center;">银行卡正面:</th>
              <th class="col-md-3" style="text-align: center;">手持结算卡:</th>
            </tr>
            <tr class="row">
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px" @click="changeBig()" :src="msg.identityFacePic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.identityOppositePic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.identityHandPic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.bankPic" alt=""/>
              </td>
              <td class="col-md-3" style="text-align: center;border: none;">
                <img style="width: 200px"  @click="changeBig()" :src="msg.bankHandPic" alt=""/>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="box box-primary" style="overflow: hidden">
        <p class="lead">商户结算信息(好收收)</p>
        <div style="width: 80%;margin: 0 0 15px 15px;">
          <template>
            <el-table :data="rateInfo" border style="width: 100%">
              <el-table-column prop="channelName" label="通道名称" ></el-table-column>
              <el-table-column prop="merchantRate" label="支付结算手续费"></el-table-column>
              <el-table-column prop="time" label="结算时间" ></el-table-column>
              <el-table-column prop="withdrawMoney" label="提现手续费" ></el-table-column>
              <el-table-column prop="entNet" label="商户入网状态" ></el-table-column>
              <el-table-column prop="remarks" label="商户入网备注信息" ></el-table-column>
            </el-table>
          </template>
        </div>
      </div>
      <div class="box box-primary" v-if="!isShow">
        <p class="lead">审核日志</p>
        <div class="table-responsive">
          <div class="col-sm-12">
            <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
              <thead>
              <tr role="row">
                <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">资料审核状态</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">审核时间</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">审核人</th>
                <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">批复信息</th>
              </tr>
              </thead>
              <tbody id="content">
              <tr role="row" class="odd" v-for="re in this.$data.res">
                <td class="sorting_1">{{re.status|status}}</td>
                <td>{{re.createTime|changeTime}}</td>
                <td>—</td>
                <td>{{re.descr}}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
      <div class="mask" id="mask" style="display: none" @click="isNo()">
        <p @click="isNo">×</p>
        <img src="" alt="">
      </div>
      <div class="box box-primary" v-if="isShow">
        <p class="lead">审核</p>

        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right;height: 35px;line-height: 35px;">审核意见:</th>
              <td><input type="text" name="name" placeholder="不通过必填" v-model="reason" style="height: 35px;line-height: 35px;width: 50%;"></td>
            </tr>
            <tr>
              <th style="text-align: right"><div class="btn btn-danger" @click="unAudit">不 通 过</div></th>
              <td><div class="btn btn-success" @click="audit($event)">通 过</div></td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'storeAudit',
    data () {
      return {
        dealerMask:false,
        id: '',
        msg:{
          id:'',
          merchantName:'',
          identity:'',
          address:'',
          bankNo:'',
          mobile:'',
          identityFacePic: '',
          identityOppositePic: '',
          identityHandPic: '',
          bankHandPic: '',
          proxyName1:'',
          proxyName: '',
          reserveMobile:'',
          createTime:'',
          proxyNameYQ:'',
          proxyNameYQ1:'',
        },
        reason:'',
        status:'',
        isShow:true,
        res: [],
        rateInfo:[]
      }
    },
    created: function () {
      this.$data.id = this.$route.query.id;
      if(this.$route.query.status !=2){
        this.$data.isShow = false;
      }
      this.$http.post('/admin/QueryMerchantInfoRecord/getAll',{id:this.$data.id})
        .then(function (res) {
          this.$data.msg = res.data.list[0];
          this.$data.res = res.data.res;
          this.$data.rateInfo = res.data.rateInfo;
          for(let i=0;i<this.rateInfo.length;i++){
            this.rateInfo[i].merchantRate = parseFloat(this.rateInfo[i].merchantRate*100).toFixed(2)+'%'
            this.rateInfo[i].withdrawMoney = this.rateInfo[i].withdrawMoney+'元/笔'
          }
        },function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
    },
    methods: {
      //修改名称
      reset:function() {
        this.$prompt('请输入新名称', '修改上报名称', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({ value }) => {
          this.$http.post('/admin/changeMerchantName/change',{id:this.$route.query.id,merchantChangeName:value})
            .then(function (res) {
              this.$data.msg.merchantChangeName = value;
              this.$message({
                showClose: true,
                type: 'success',
                message: '修改成功'
              });
            })
            .catch(function (err) {
              this.$message({
                showClose: true,
                message: err.statusMessage,
                type: 'error'
              })
            })

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消修改'
          });
        });
      },
      audit: function (event) {
        this.$http.post('/admin/merchantInfoCheckRecord/record', {
          merchantId: this.$data.id
        }).then(function (res) {
          this.$router.push('/admin/record/storeList')
        }, function (err) {
          this.$message({
            showClose: true,
            message: err.statusMessage,
            type: 'error'
          })
        })
      },
      unAudit: function () {
        this.$http.post('/admin/merchantInfoCheckRecord/auditFailure',{merchantId: this.$data.id,descr:this.$data.reason})
          .then(function (res) {
            this.$router.push('/admin/record/storeList')
          },function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      changeBig: function (e) {
        e = e||window.event;
        var obj = e.srcElement||e.target;
        var mask = document.getElementById('mask'),
          img = mask.getElementsByTagName('img')[0];
        img.src = obj.src;
        mask.style.display= 'block'
      },
      isNo: function () {
        document.getElementById('mask').style.display='none'
      }
    },
    filters: {
      status: function (val) {
        val = Number(val)
        if(val == 0){
          val = "已注册"
        }else if(val == 1){
          val = "已提交基本资料"
        }else if(val == 2){
          val = "待审核"
        }else if(val == 3){
          val = "审核通过"
        }else if(val == 4){
          val="审核未通过"
        }
        return val;
      },
      changeTime: function (val) {
        if(val==''||val==null){
          return ''
        }else {
          val = new Date(val)
          var year=val.getFullYear();
          var month=val.getMonth()+1;
          var date=val.getDate();
          var hour=val.getHours();
          var minute=val.getMinutes();
          var second=val.getSeconds();
          function tod(a) {
            if(a<10){
              a = "0"+a
            }
            return a;
          }
          return year+"-"+tod(month)+"-"+tod(date)+" "+tod(hour)+":"+tod(minute)+":"+tod(second);
        }
      },
      changeDeal: function (val) {
        return val=val?val:'无'
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .mask{
    background: rgba(0,0,0,0.8);
    z-index: 1100;
    width: 100%;
    height: 100%;
    position: fixed;
    top:0;
    left: 0;
  p{
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

  img{
    display: inherit;
    height: 100%;
    margin: 0 auto;
  }
  }
</style>



<!--结算记录-->
<!--<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">商户结算记录</h3>
        </div>
        <div class="box-body">
          &lt;!&ndash;筛选&ndash;&gt;
          <ul>
            <li class="same">
              <label>结算日期:</label>
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions"></el-date-picker>
            </li>
            <li class="same">
              <label>结算单号:</label>
              <el-input style="width: 130px" v-model="query.settleNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>结算状态:</label>
              <el-select clearable v-model="query.settleStatus" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待结算" value="1">待结算</el-option>
                <el-option label="部分结算" value="4">部分结算</el-option>
                <el-option label="结算成功" value="3">结算成功</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>商户名称:</label>
              <el-input style="width: 130px" v-model="query.userName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>商户编号:</label>
              <el-input style="width: 130px" v-model="query.userNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          &lt;!&ndash;表格&ndash;&gt;
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border @selection-change="handleSelectionChange">
            <el-table-column type="index" width="62" label="序号" fixed="left"></el-table-column>
            &lt;!&ndash;<el-table-column type="selection" width="55"></el-table-column>&ndash;&gt;
            <el-table-column prop="userName" label="商户名称" ></el-table-column>
            <el-table-column prop="userNo" label="商户编号" ></el-table-column>
            <el-table-column label="业务线" >
              <template scope="scope">
                <span v-if="records[scope.$index].appId=='hss'">好收收</span>
                <span v-if="records[scope.$index].appId=='hsy'">好收银</span>
              </template>
            </el-table-column>
            <el-table-column prop="settleNo" label="结算单号" ></el-table-column>
            <el-table-column prop="settleDate" label="结算日期" :formatter="changeTime"></el-table-column>
            <el-table-column prop="settleAmount" label="结算金额" :formatter="changeNum" align="right"></el-table-column>
            <el-table-column prop="settleModeValue" label="结算方式" ></el-table-column>
            <el-table-column prop="settleDestinationValue" label="结算类型" ></el-table-column>
            <el-table-column prop="settleStatusValue" label="结算状态" ></el-table-column>

            &lt;!&ndash;<el-table-column label="操作" width="70">
              <template scope="scope">
                <el-button @click.native.prevent="list(scope.$index)" type="text" size="small" v-if="records[scope.$index].settleStatusValue!='结算成功'">结算</el-button>
              </template>&ndash;&gt;
            </el-table-column>
          </el-table>
          &lt;!&ndash;分页&ndash;&gt;
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
          &lt;!&ndash;审核&ndash;&gt;
          <div v-if="isShow">
            <el-dialog title="结算确认提醒" v-model="isShow">
              <div class="maskCon">
                <span>商户名称：</span>
                <span>{{records[this.$data.index].userName}}</span>
              </div>
              <div class="maskCon">
                <span>商户编号：</span>
                <span>{{records[index].userNo}}</span>
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
                <el-button @click="settle(2,records[index].id)">结算已对账部分</el-button>
                <el-button @click="settle(3,records[index].id)">强制结算全部</el-button>
              </div>
            </el-dialog>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'tAuditStore',
    data(){
      return{
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        date:'',
        records:[],
        count:0,
        total:0,
        query:{
          pageNo:1,
          pageSize:10,
          userNo:"",//编号
          userName:"",  //名字
          settleNo:"",//结算单号
          userType:2,//(2：商户，3：代理商)
          starDate:"", // 开始
          endDate:"", //结束
          settleStatus:''
        },
        multipleSelection:[],
        currentPage4: 1,
        loading:true,
        isShow:false,
        index:'',
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      changeTime: function (row, column) {
        var val = row.settleDate;
        if (val == '' || val == null) {
          return ''
        } else {
          val = new Date(val)
          var year = val.getFullYear();
          var month = val.getMonth() + 1;
          var date = val.getDate();
          var hour = val.getHours();
          var minute = val.getMinutes();
          var second = val.getSeconds();

          function tod(a) {
            if (a < 10) {
              a = "0" + a
            }
            return a;
          }

          return year + "-" + tod(month) + "-" + tod(date);
        }
      },
      changeNum: function (row, column) {
        var val = row.settleAmount;
        return parseFloat(val).toFixed(2);
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/settlementRecord/list',this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
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
              this.$data.records[i].tradeDate = changeTime(this.$data.records[i].tradeDate)
            }
          },function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      search: function () {
        this.$data.query.pageNo = 1;
        this.getData()
      },
      list: function (val) {
        this.$data.index = val;
        this.$data.isShow = true;
      },
      //行选中
      handleSelectionChange(val) {
        console.log(val)
        this.multipleSelection = val;
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
      //结算审核
      settle(val,id) {
        this.$http.post('/admin/settle/singleSettle',{recordId:id,option:val})
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '结算成功',
              type: 'success'
            })
            this.$data.isShow = false
          })
          .catch(function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      }
    },
    watch:{
      date:function (val,oldVal) {
        if(val[0]!=null){
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
              this.$data.query.starDate = str;
            }else {
              this.$data.query.starDate = str;
            }
          }
        }else {
          this.$data.query.starDate = '';
          this.$data.query.starDate = '';
        }
      }
    }
  }
</script>
<style scoped lang="less">
  .maskCon{
    margin:0 0 15px 50px
  }
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }
</style>-->

<!--<template>
  <div>
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">代理商结算记录</h3>
        </div>
        <div class="box-body">
          &lt;!&ndash;筛选&ndash;&gt;
          <ul>
            <li class="same">
              <label>结算日期:</label>
              <el-date-picker v-model="date" size="small" type="daterange" align="right" placeholder="选择日期范围" :picker-options="pickerOptions"></el-date-picker>
            </li>
            <li class="same">
              <label>结算单号:</label>
              <el-input style="width: 130px" v-model="query.settleNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>结算状态:</label>
              <el-select clearable v-model="query.settleStatus" size="small" >
                <el-option label="全部" value="">全部</el-option>
                <el-option label="待结算" value="1">待结算</el-option>
                <el-option label="部分结算" value="4">部分结算</el-option>
                <el-option label="结算成功" value="3">结算成功</el-option>
              </el-select>
            </li>
            <li class="same">
              <label>代理商名称:</label>
              <el-input style="width: 130px" v-model="query.userName" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>代理商编号:</label>
              <el-input style="width: 130px" v-model="query.userNo" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <div class="btn btn-primary" @click="search">筛选</div>
            </li>
          </ul>
          &lt;!&ndash;表格&ndash;&gt;
          <el-table v-loading.body="loading" style="font-size: 12px;margin:15px 0" :data="records" border @selection-change="handleSelectionChange">
            <el-table-column type="index" width="62" label="序号" fixed="left"></el-table-column>
            &lt;!&ndash;<el-table-column type="selection" width="55"></el-table-column>&ndash;&gt;
            <el-table-column prop="userName" label="代理商名称" ></el-table-column>
            <el-table-column prop="userNo" label="代理商编号" ></el-table-column>
            <el-table-column label="业务线" >
              <template scope="scope">
                <span v-if="records[scope.$index].appId=='hss'">好收收</span>
                <span v-if="records[scope.$index].appId=='hsy'">好收银</span>
              </template>
            </el-table-column>
            <el-table-column prop="settleNo" label="结算单号" ></el-table-column>
            <el-table-column prop="settleDate" label="结算日期" :formatter="changeTime"></el-table-column>
            <el-table-column prop="settleAmount" label="结算金额" :formatter="changeNum" align="right"></el-table-column>
            <el-table-column prop="settleModeValue" label="结算方式" ></el-table-column>
            <el-table-column prop="settleDestinationValue" label="结算类型" ></el-table-column>
            <el-table-column prop="settleStatusValue" label="结算状态" ></el-table-column>

            &lt;!&ndash;<el-table-column label="操作" width="70">
              <template scope="scope">
                <el-button @click.native.prevent="list(scope.$index)" type="text" size="small" v-if="records[scope.$index].settleStatusValue!='结算成功'">结算</el-button>
              </template>&ndash;&gt;
            </el-table-column>
          </el-table>
          &lt;!&ndash;分页&ndash;&gt;
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
          &lt;!&ndash;审核&ndash;&gt;
          <div v-if="isShow">
            <el-dialog title="结算确认提醒" v-model="isShow">
              <div class="maskCon">
                <span>商户名称：</span>
                <span>{{records[this.$data.index].userName}}</span>
              </div>
              <div class="maskCon">
                <span>商户编号：</span>
                <span>{{records[index].userNo}}</span>
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
                <el-button @click="settle(2,records[index].id)">结算已对账部分</el-button>
                <el-button @click="settle(3,records[index].id)">强制结算全部</el-button>
              </div>
            </el-dialog>
          </div>
        </div>
      </div>
    </div>
  </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'tAuditStore',
    data(){
      return{
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        date:'',
        records:[],
        count:0,
        total:0,
        query:{
          pageNo:1,
          pageSize:10,
          userNo:"",//编号
          userName:"",  //名字
          settleNo:"",//结算单号
          userType:3,//(2：商户，3：代理商)
          starDate:"", // 开始
          endDate:"", //结束
          settleStatus:''
        },
        multipleSelection:[],
        currentPage4: 1,
        loading:true,
        isShow:false,
        index:'',
      }
    },
    created: function () {
      this.getData()
    },
    methods: {
      changeTime: function (row, column) {
        var val = row.settleDate;
        if (val == '' || val == null) {
          return ''
        } else {
          val = new Date(val)
          var year = val.getFullYear();
          var month = val.getMonth() + 1;
          var date = val.getDate();
          var hour = val.getHours();
          var minute = val.getMinutes();
          var second = val.getSeconds();

          function tod(a) {
            if (a < 10) {
              a = "0" + a
            }
            return a;
          }

          return year + "-" + tod(month) + "-" + tod(date);
        }
      },
      changeNum: function (row, column) {
        var val = row.settleAmount;
        return parseFloat(val).toFixed(2);
      },
      getData: function () {
        this.loading = true;
        this.$http.post('/admin/settlementRecord/list',this.$data.query)
          .then(function (res) {
            this.$data.records = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
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
              this.$data.records[i].tradeDate = changeTime(this.$data.records[i].tradeDate)
            }
          },function (err) {
            this.$data.loading = false;
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      },
      search: function () {
        this.$data.query.pageNo = 1;
        this.getData()
      },
      list: function (val) {
        this.$data.index = val;
        this.$data.isShow = true;
      },
      //行选中
      handleSelectionChange(val) {
        console.log(val)
        this.multipleSelection = val;
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
      //结算审核
      settle(val,id) {
        this.$http.post('/admin/settle/singleSettle',{recordId:id,option:val})
          .then(function (res) {
            this.$message({
              showClose: true,
              message: '结算成功',
              type: 'success'
            })
            this.$data.isShow = false
          })
          .catch(function (err) {
            this.$message({
              showClose: true,
              message: err.statusMessage,
              type: 'error'
            })
          })
      }
    },
    watch:{
      date:function (val,oldVal) {
        if(val[0]!=null){
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
              this.$data.query.starDate = str;
            }else {
              this.$data.query.starDate = str;
            }
          }
        }else {
          this.$data.query.starDate = '';
          this.$data.query.starDate = '';
        }
      }
    }
  }
</script>
<style scoped lang="less">
  .maskCon{
    margin:0 0 15px 50px
  }
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    display: inline-block;
    margin: 0 15px 15px 0;
  }
  .btn{
    font-size: 12px;
  }
</style>-->
