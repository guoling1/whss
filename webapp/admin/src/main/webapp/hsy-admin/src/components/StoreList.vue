<template lang="html">
  <div id="storeList">
    <h1>商户列表</h1>

    <!--<div class="search" id="search">
      <label for="name">商户编号：</label>
      <input type="text" name="name" value="">
      <label for="name">商户名称：</label>
      <input type="text" name="name" value="">
      <div class="btn btn-primary" @click="search">筛选</div>
    </div>-->
    <div class="col-xs-12">
      <div class="box">
        <div class="box-body">
          <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div class="row">
              <div class="col-sm-6"></div>
              <div class="col-sm-6"></div>
            </div>
            <div class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                  <thead>
                  <tr role="row">
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">
                      商户编号
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">商户名称
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">所属代理商
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">所属代理商编号
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">注册时间
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">可用状态
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作
                    </th>
                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" class="odd" v-for="store in $$data.stores">
                    <td class="sorting_1">{{store.id}}</td>
                    <td>{{store.merchantName}}</td>
                    <td>{{store.proxyName}}</td>
                    <td>{{store.dealerId}}</td>
                    <td>{{store.createTime|changeTime}}</td>
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
                </div>
              </div>
            </div>
          </div>
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
        stores: [],
        msg:[],
        pageNo:1,
        pageSize:10,
        total:0
      }
    },
    created: function () {
      var content = document.getElementById('content'),
        page = document.getElementById('page');

      this.$http.post('/admin/query/getAll',{
        pageNo:this.$data.pageNo,
        pageSize:this.$data.pageSize
      }).then(function (res) {
        this.$data.stores = this.$data.msg  = res.data.records;
        this.$data.total = res.data.totalPage;
        var str='',
          page=document.getElementById('page');
        str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a></li>'
        for (var i=1; i<=this.$data.total;i++){
          if(i==this.$data.pageNo){
            str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
            continue;
          }
          str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
        }
        str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a></li>'
        page.innerHTML=str;
      }, function (err) {
        this.$store.commit('MESSAGE_ACCORD_SHOW', {
          text: err.statusMessage
        })
      })
    },
    methods: {
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
        if(tarInn == 'Previous'){
          if(n == 1){
            tar.parentNode.className+=' disabled'
            return;
          }
          n--;
        }
        if(tarInn == 'Next'){
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
          pageSize:this.$data.pageSize
        }).then(function (res) {
          this.$data.stores = this.$data.msg  = res.data.records;
          this.$data.total = res.data.totalPage;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a></li>'
          for (var i=1; i<=this.$data.total;i++){
            if(i==this.$data.pageNo){
              str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
              continue;
            }
            str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
          }
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">Next</a></li>'
          page.innerHTML=str;
        }, function (err) {
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
      },
      search: function () {
        this.$data.stores = this.$data.msg;
        console.log(1)
        var search = document.getElementById('search'),
          condition = search.getElementsByTagName('input')[0],
          result = [];
        var reg = new RegExp(condition.value);
        console.log(reg)
        console.log(condition.value)
        for(var i = 0; i < this.$data.stores.length; i++){
          if(reg.test(this.$data.stores[i].merchantName)){
            result.push(this.$data.stores[i])
          }
        }
        this.$data.stores = result;
      }
    },
    computed:{
      $$data:function () {
        return this.$data
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
        if(val == 0){
          val = "查看详情"
        }else if(val == 1){
          val = "查看详情"
        }else if(val == 2){
          val = "审核"
        }else if(val == 3||val == 4){
          val = "查看详情"
        }
        return val;
      },
      changeTime: function (val) {
        val = new Date(val)
        var year=val.getFullYear();
        var month=val.getMonth()+1;
        var date=val.getDate();
        var hour=val.getHours();
        var minute=val.getMinutes();
        var second=val.getSeconds();
        return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
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

  a {
    color: #42b983;
  }

  .storeList {
    float: right;
    width: 80%;
  }

  .search {
    margin-bottom: 10px;
    input {
      margin-right: 20px;
    }
  }

  /*.table {
    overflow: hidden;
    width: 1000px;
    td, th {
      height: 36px;
      line-height: 36px;
      text-align: center;
      width: 120px;
    }
    th {
      height: 28px;
      line-height: 28px;
    }
    td:nth-child(6), th:nth-child(6), td:nth-child(7), th:nth-child(7) {
      width: 110px;
    }
    td:nth-child(5), th:nth-child(5), td:nth-child(2), th:nth-child(2), td:nth-child(3), th:nth-child(3), {
      width: 180px;
    }
    tr:nth-child(2) {
      background: #ccc;
    }
  }*/
</style>
