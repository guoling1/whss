<template lang="html">
  <div id="storeList">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;    color: #fff;">商户列表</div>
    <div class="col-xs-12">
      <div class="box">
        <div class="box-body">
          <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div class="search" id="search">
              <label for="name">商户名称：</label>
              <input class="form-control" type="text" name="name" value="" v-model="merchantName">
              <label>订单状态：</label>
              <select class="form-control select2 select2-hidden-accessible" tabindex="-1" aria-hidden="true" v-model="status">
                <option value="">全部</option>
                <option value="0">已注册</option>
                <option value="1">已提交基本资料</option>
                <option value="2">待审核</option>
                <option value="3">审核通过</option>
                <option value="4">审核未通过</option>
              </select>
              <div class="btn btn-primary" @click="search">筛选</div>
              <span @click="onload()" download="交易记录" class="btn btn-primary pull-right" style="float: right;color: #fff">导出</span>
            </div>
            <div class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                  <thead>
                  <tr role="row">
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">商户编号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">商户名称</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">所属一级代理商</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">所属二级代理</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">注册时间</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">认证时间</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">审核时间</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">可用状态</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作</th>
                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" class="odd" v-for="store in $$data.stores">
                    <td class="sorting_1">{{store.id}}</td>
                    <td>{{store.merchantName}}</td>
                    <td>{{store.proxyName|changeName}}</td>
                    <td>{{store.proxyName1|changeName}}</td>
                    <td>{{store.createTime|changeTime}}</td>
                    <td>{{store.authenticationTime|changeTime}}</td>
                    <td>{{store.checkedTime|changeTime}}</td>
                    <td>{{store.status|status}}</td>
                    <td>
                      <div class="btn btn-primary" @click="audit($event,store.id,store.status)">{{store.status|operate}}</div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="row">
              <div class="col-sm-5">
              </div>
              <div class="col-sm-7">
                <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                  <ul class="pagination" id="page" @click="bindEvent($event)">

                  </ul>
                  <span class="count">共{{count}}条</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
    <!--下载-->
    <div class="box box-info mask" v-if="isMask">
      <div class="box-body" style="text-align: center;font-size: 20px;">
        确认下载吗？
      </div>
      <div class="box-footer clearfix" style="border-top: none">
        <a :href="'http://'+$$url" @click="close()" class="btn btn-sm btn-info btn-flat pull-left">下载</a>
        <a href="javascript:void(0)" @click="close()" class="btn btn-sm btn-default btn-flat pull-right">取消</a>
      </div>
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'storeList',
    data () {
      return {
        merchantName:'',
        stores: [],
        pageNo:1,
        pageSize:10,
        total:0,
        status:'',
        isMask: false,
        url: '',
        count:''
      }
    },
    created: function () {
      var content = document.getElementById('content'),
        page = document.getElementById('page');
      this.$http.post('/admin/query/getAll',{
        pageNo:this.$data.pageNo,
        pageSize:this.$data.pageSize,
        merchantName:this.$data.merchantName,
        status: this.$data.status
      }).then(function (res) {
        this.$data.stores   = res.data.records;
        this.$data.total = res.data.totalPage;
        this.$data.count = res.data.count;
        var str='',
          page=document.getElementById('page');
        str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
        for (var i=1; i<=this.$data.total;i++){
          if(i==this.$data.pageNo){
            str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
            continue;
          }
          str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
        }
        str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
        page.innerHTML=str;
      }, function (err) {
        this.$store.commit('MESSAGE_ACCORD_SHOW', {
          text: err.statusMessage
        })
      })
    },
    methods: {
      onload:function () {
        this.$data.isMask = true;
        this.$http.post('/admin/query/downLoad',{
          /*pageNo:this.$data.pageNo,
          pageSize:this.$data.pageSize,
          merchantName:this.$data.merchantName,
          status: this.$data.status*/
        })
          .then(function (res) {
            console.log(res)
            this.$data.url = res.data.url;
          },function (err) {
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
            this.$data.isMask = false;
          })
      },
      close: function () {
        this.$data.isMask = false;
      },
      audit: function (event, id, status) {
        this.$router.push({
          path: '/admin/record/StoreAudit', query: {
            id: id,
            status: status
          }
        })
      },
      bindEvent: function (e) {
        e = e||window.event;
        var tar = e.target||e.srcElement,
          tarInn = tar.innerHTML,
          n = this.$data.pageNo;
        if(tarInn == '上一页'){
          if(n == 1){
            tar.parentNode.className+=' disabled'
            return;
          }
          n--;
        }
        if(tarInn == '下一页'){
          if(n == this.$data.total){
            tar.parentNode.className+=' disabled'
            return;
          }
          n++;
        }
        if(Number(tarInn)){
          if(n == Number(tarInn)){
            return;
          }
          tar.parentNode.className+=' active'
          n = Number(tarInn);
        }
        this.$data.pageNo = n;
        this.$http.post('/admin/query/getAll',{
          pageNo:this.$data.pageNo,
          pageSize:this.$data.pageSize,
          merchantName:this.$data.merchantName,
          status: this.$data.status
        }).then(function (res) {
          this.$data.stores   = res.data.records;
          this.$data.total = res.data.totalPage;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
          for (var i=1; i<=this.$data.total;i++){
            if(i==this.$data.pageNo){
              str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
              continue;
            }
            str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
          }
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      },
      search: function () {
        var content = document.getElementById('content'),
          page = document.getElementById('page');
        this.$http.post('/admin/query/getAll',{
          pageNo:this.$data.pageNo,
          pageSize:this.$data.pageSize,
          merchantName:this.$data.merchantName,
          status: this.$data.status
        }).then(function (res) {
          this.$data.stores  = res.data.records;
          this.$data.total = res.data.totalPage;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
          for (var i=1; i<=this.$data.total;i++){
            if(i==this.$data.pageNo){
              str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
              continue;
            }
            str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
          }
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      }
    },
    computed:{
      $$data:function () {
        return this.$data
      },
      $$url: function () {
        return this.$data.url
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
      operate: function (val) {
        val = Number(val)
        if(val == 0||val == 1||val == 3||val == 4){
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
          return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
        }
      },
      changeName: function (val) {
        if(val==null){
          return "无"
        }else {
          return val
        }
      }
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


  .search{
    margin-bottom: 15px;
    input{
      margin-right: 20px;
    }
    select{
      margin-right: 20px;
    }
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
</style>
