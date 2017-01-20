<template lang="html">
  <div id="storeList" style="margin-top: 15px">
    <div class="col-xs-12">
      <div class="box">
        <div class="box-header">
          <h3 class="box-title">商户列表</h3>
        </div>
        <div class="box-body">
          <el-tabs class="tab" v-model="activeName2" type="card" @tab-click="handleClick">
            <el-tab-pane label="好收收" name="first">
              <div class="row">
                <div class="col-md-2">
                  <div class="form-group">
                    <label>商户编号：</label>
                    <input type="text" class="form-control" v-model="query.markCode">
                  </div>
                  <div class="form-group">
                    <label>商户名称</label>
                    <input type="text" class="form-control" v-model="query.merchantName">
                  </div>
                </div>
                <div class="col-md-2">
                  <div class="form-group">
                    <label>所属一级代理：</label>
                    <input type="text" class="form-control" v-model="query.proxyName">
                  </div>
                  <div class="form-group">
                    <label>所属二级代理</label>
                    <input type="text" class="form-control" v-model="query.proxyName1">
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label>注册时间：</label>
                    <div class="form-control">
                      <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="query.startTime">至
                      <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="query.endTime">
                    </div>
                  </div>
                  <div class="form-group">
                    <label>认证时间：</label>
                    <div class="form-control">
                      <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="query.startTime1">至
                      <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="query.endTime1">
                    </div>
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label>审核时间：</label>
                    <div class="form-control">
                      <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="query.startTime2">至
                      <input type="date" style="border: none;display:inline-block;width: 45%" name="date" value="" v-model="query.endTime2">
                    </div>
                  </div>
                  <div class="form-group">
                    <label>审核状态：</label>
                    <select class="form-control select2 select2-hidden-accessible" tabindex="-1" aria-hidden="true" v-model="query.status">
                      <option value="">全部</option>
                      <option value="0">已注册</option>
                      <option value="1">已提交基本资料</option>
                      <option value="2">待审核</option>
                      <option value="3">审核通过</option>
                      <option value="4">审核未通过</option>
                    </select>
                  </div>
                </div>
                <div class="col-md-1">
                  <div class="btn btn-primary" @click="search" style="margin-top: 85px">筛选</div>
                  <!--<span @click="onload()" download="交易记录" class="btn btn-primary pull-right" style="float: right;color: #fff">导出</span>-->
                </div>
              </div>
              <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
                <div class="row">
                  <div class="col-sm-12">
                    <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                      <thead>
                      <tr role="row">
                        <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">序号</th>
                        <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">商户编号</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">商户名称</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">所属一级代理商</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">所属二级代理</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">注册时间</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">注册方式</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">认证时间</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">审核时间</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">状态</th>
                        <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作</th>
                      </tr>
                      </thead>
                      <tbody id="content">
                      <tr role="row" class="odd" v-for="(store,index) in $$data.stores">
                        <td>{{(query.pageNo-1)*10+(index+1)}}</td>
                        <td class="sorting_1">{{store.markCode}}</td>
                        <td>{{store.merchantName}}</td>
                        <td>{{store.proxyName}}</td>
                        <td>{{store.proxyName1}}</td>
                        <td>{{store.createTime|changeTime}}</td>
                        <td>{{store.source|changeSource}}</td>
                        <td>{{store.authenticationTime|changeTime}}</td>
                        <td>{{store.checkedTime|changeTime}}</td>
                        <td>{{store.status|status}}</td>
                        <td>
                          <a @click="audit($event,store.id,store.status)">{{store.status|operate}}</a>
                        </td>
                      </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
                <div v-if="isShow">
                  <img src="http://img.jinkaimen.cn/admin/common/dist/img/ICBCLoading.gif" alt="">
                </div>
                <div v-if="$$data.stores.length==0&&!isShow" class="row" style="text-align: center;color: red;font-size: 16px;">
                  <div class="col-sm-12">无此数据</div>
                </div>
                <div class="row">
                  <div class="col-sm-5">
                  </div>
                  <div class="col-sm-7">
                    <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                      <ul class="pagination" id="page" @click="bindEvent($event)"></ul>
                      <span class="count">共{{total}}页 {{count}}条</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="好收银" name="second">

            </el-tab-pane>
          </el-tabs>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'storeList',
    data () {
      return {
        query:{
          pageNo:1,
          pageSize:10,
          markCode:'',
          merchantName:'',
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
        stores: [],
        total:0,
        count:0,
        isShow: false
      }
    },
    created: function () {
      this.$data.isShow = true;
      this.$data.stores = '';
      this.$data.total = 0;
      this.$data.count = 0;
      var content = document.getElementById('content'),
        page = document.getElementById('page');
//      好收收
      this.$http.post('/admin/hsyMerchantList/getMerchantList',this.$data.query)
        .then(function (res) {
        this.$data.isShow = false;
        this.$data.stores = res.data.records;
        this.$data.total = res.data.totalPage;
        this.$data.count = res.data.count;
        var str='',
          page=document.getElementById('page');
        str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
        if(this.$data.total<=10){
          for (var i = 1; i <= this.$data.total; i++){
            if(i == this.$data.query.pageNo){
              str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
              continue;
            }
            str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
          }
        }else {
          if(this.$data.query.pageNo<6){
            for (var i = 1; i <= 10; i++){
              if(i == this.$data.query.pageNo){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
          }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo<=(this.$data.total-5)){
            for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
              if(i == this.$data.query.pageNo){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
          }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
            for (var i = this.$data.total-9; i <= this.$data.total; i++){
              if(i == this.$data.query.pageNo){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
          }}
        str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
        page.innerHTML=str;
      }, function (err) {
        this.$data.isShow = false;
        this.$store.commit('MESSAGE_ACCORD_SHOW', {
          text: err.statusMessage
        })
      })
//      好收银
      /*this.$http.post('/admin/hsyMerchantList/getMerchantList',this.$data.query)
        .then(function (res) {

        })*/
    },
    methods: {
      //审核
      audit: function (event, id, status) {
        this.$router.push({
          path: '/admin/record/StoreAudit', query: {
            id: id,
            status: status
          }
        })
      },
      //分页
      bindEvent: function (e) {
        e = e||window.event;
        var tar = e.target||e.srcElement,
          tarInn = tar.innerHTML,
          n = this.$data.query.pageNo;
        if(tarInn == '上一页'){
          if(n == 1){
            tar.parentNode.className+=' disabled';
            return;
          }
          n--;
        }
        if(tarInn == '下一页'){
          if(n == this.$data.total){
            tar.parentNode.className+=' disabled';
            return;
          }
          n++;
        }
        if(Number(tarInn)){
          if(n == Number(tarInn)){
            return;
          }
          tar.parentNode.className+=' active';
          n = Number(tarInn);
        }
        this.$data.query.pageNo = n;
        this.$http.post('/admin/query/getAll',this.$data.query)
          .then(function (res) {
          this.$data.stores   = res.data.records;
          this.$data.total = res.data.totalPage;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            if(this.$data.total<=10){
              for (var i = 1; i <= this.$data.total; i++){
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else {
              if(this.$data.query.pageNo<6){
                for (var i = 1; i <= 10; i++){
                  if(i == this.$data.query.pageNo){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo<=(this.$data.total-5)){
                for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
                  if(i == this.$data.query.pageNo){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
                for (var i = this.$data.total-9; i <= this.$data.total; i++){
                  if(i == this.$data.query.pageNo){
                    str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                    continue;
                  }
                  str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
                }
              }}
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      },
      //搜索
      search: function () {
        var content = document.getElementById('content'),
          page = document.getElementById('page');
        this.$data.pageNo=1;
        this.$data.isShow  = true;
        this.$data.stores  = '';
        this.$data.total = 0;
        this.$data.count = 0;
        this.$http.post('/admin/query/getAll',this.$data.query)
          .then(function (res) {
          this.$data.isShow  = false;
          this.$data.stores  = res.data.records;
          this.$data.total = res.data.totalPage;
          this.$data.count = res.data.count;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
          if(this.$data.total<=10){
            for (var i = 1; i <= this.$data.total; i++){
              if(i == this.$data.query.pageNo){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
          }else {
            if(this.$data.query.pageNo<6){
              for (var i = 1; i <= 10; i++){
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo<=(this.$data.total-5)){
              for (var i = this.$data.query.pageNo-4; i <= this.$data.query.pageNo+5; i++){
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }else if(this.$data.query.pageNo>=6&&this.$data.query.pageNo>this.$data.total-5){
              for (var i = this.$data.total-9; i <= this.$data.total; i++){
                if(i == this.$data.query.pageNo){
                  str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                  continue;
                }
                str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
              }
            }}
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
        }, function (err) {
          this.$data.isShow  = false;
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      },
      handleClick: function () {

      }
    },
    computed:{
      $$data:function () {
        return this.$data
      }
    },
    filters: {
      changeSource: function (val) {
        if(val== 0){
          val = "扫码注册"
        }else if(val == 1){
          val = "推荐注册"
        }
        return val
      },
      status: function (val) {
        val = Number(val)
        if(val == 0){
          val = "已注册"
        }else if(val == 1){
          val = "已提交基本资料"
        }else if(val == 2){
          val = "待审核"
        }else if(val == 3||val==6){
          val = "审核通过"
        }else if(val == 4){
          val="审核未通过"
        }
        return val;
      },
      operate: function (val) {
        val = Number(val)
        if(val == 0||val == 1||val == 3||val == 4||val==6){
          val = "查看详情"
        }else if(val == 2){
          val = "审核"
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
    }
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

  .form-control{
    font-size: 12px;
    height: 28px;
    padding-top: 0;
    padding-bottom: 0;
    input{
      height: 25px;
    }
  }
  input,.form-control,.btn{
    font-size: 12px;
  }
  .mask{
    width: 30%;
    position: fixed;
    top: 30%;
    left: 46%;
    box-shadow: 0 0 15px #000;
  }
  .count{
    display: inline-block;
    vertical-align: top;
    margin: 28px 10px;
  }
  img{
    width: 8%;
    margin: 0 auto;
    display: inherit;
  }
</style>
