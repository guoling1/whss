<template lang="html">
  <div id="agentList">
    <div class="col-xs-12" style="margin-top: 15px;">
      <div class="box">
        <div class="box-header">
          <h3 class="box-title">代理商列表</h3>
        </div>
        <div class="box-body">
          <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div>
              <div class="form-group" style="margin-bottom: 15px;">
                <label>手机号：</label>
                <input class="form-control" type="tel" v-model="query.mobile">
              </div>
              <div class="form-group" style="margin-bottom: 15px;">
                <label>名称：</label>
                <input class="form-control" type="text" v-model="query.name">
              </div>
              <div class="form-group" style="margin-bottom: 15px;">
                <label>所属区域：</label>
                <input class="form-control" type="text" v-model="query.belongArea">
              </div>
              <div class="form-group" style="margin-bottom: 15px;">
                <label>银行卡号：</label>
                <input class="form-control" type="text" v-model="query.settleBankCard">
              </div>
              <div class="form-group" style="margin-bottom: 15px;">
                <label>银行预留手机号：</label>
                <input class="form-control" type="text" v-model="query.bankReserveMobile">
              </div>
              <div class="form-group" style="margin-bottom: 15px;">
                <label>级别：</label>
                <select class="form-control fun"  name="" v-model="query.level">
                  <option value="">全部</option>
                  <option value="1">一级代理商</option>
                  <option value="2">二级代理商</option>
                </select>
              </div>
              <div class="btn btn-primary" @click="lookup" style="margin-top: -15px">
                筛选
              </div>
            </div>
            <div style="margin-top: 15px" class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid" aria-describedby="example2_info">
                  <thead>
                  <tr role="row">
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">序号</th>
                    <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">代理名称</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">手机号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">所属区域</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">级别</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">银行账户名称</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">结算卡号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">银行预留手机号</th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作</th>
                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" class="odd" v-for="(dealer,index) in dealers">
                    <td>{{(pageNo-1)*10+(index+1)}}</td>
                    <td class="sorting_1">{{dealer.proxyName}}</td>
                    <td>{{dealer.mobile}}</td>
                    <td>{{dealer.belongArea}}</td>
                    <td>{{dealer.level}}</td>
                    <td>{{dealer.bankAccountName}}</td>
                    <td>{{dealer.settleBankCard}}</td>
                    <td>{{dealer.bankReserveMobile}}</td>
                    <td>
                      <div class="btn btn-primary" v-if="dealer.level==1" @click="detail(index,dealer.id,dealer.level)">修改</div>
                      <div class="btn btn-primary" v-if="dealer.level==2" @click="detail(index,dealer.id,dealer.level)">查看详情</div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-if="isShow">
              <img src="http://img.jinkaimen.cn/admin/common/dist/img/ICBCLoading.gif" alt="">
            </div>
            <div v-if="dealers.length==0&&!isShow" class="row" style="text-align: center;color: red;font-size: 16px;">
              <div class="col-sm-12">无此数据</div>
            </div>
            <div class="row">
              <div class="col-sm-5">
              </div>
              <div class="col-sm-7">
                <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                  <ul class="pagination" id="page" @click="bindEvent($event)">
                  </ul>
                  <span class="count">共{{total}}页 {{count}}条</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
    <div class="btn btn-primary" @click="create" style="margin-left: 15px">
      新增代理商
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'agentList',
    data () {
      return {
        query:{
          pageNo:1,
          pageSize:10,
          mobile:'',
          name:'',
          belongArea:'',
          settleBankCard:'',
          bankReserveMobile:'',
          level:''
        },
        dealers: [],
        pageSize:10,
        pageNo:1,
        mobile:'',
        belongArea:'',
        id:'',
        level:'',
        name:'',
        total:0,
        count:0,
        isShow:false
      }
    },
    created: function () {
      this.$data.isShow = true;
      this.$data.dealers = '';
      this.$data.total = 0;
      this.$data.pageNo = 1;
      this.$data.count = 0;
      this.$http.post('/admin/dealer/listDealer',this.$data.query)
        .then(function (res) {
          this.$data.isShow = false;
          this.$data.dealers = res.data.records;
          this.$data.total = res.data.totalPage;
          this.$data.count = res.data.count;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
          for (var i=1; i<=this.$data.total;i++){
            if(i==res.data.pageNO){
              str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
              continue;
            }
            str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
          }
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
        }, function (err) {
          this.$data.isShow = false;
          this.$store.commit('MESSAGE_ACCORD_SHOW', {
            text: err.statusMessage
          })
        })
    },
    methods: {
      bindEvent: function (e) {
        e = e||window.event;
        var tar = e.target||e.srcElement,
          tarInn = tar.innerHTML,
          n = this.$data.query.pageNo;
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
        this.$data.query.pageNo = n;
        this.$http.post('/admin/dealer/listDealer',this.$data.query)
          .then(function (res) {
            this.$data.dealers = res.data.records;
            this.$data.total = res.data.totalPage;
            this.$data.count = res.data.count;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==res.data.pageNO){
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
      create: function () {
        this.$router.push('/admin/record/agentAdd')
      },
      detail: function (val,id,level) {
        this.$router.push({path:'/admin/record/agentAdd',query:{id:id,level:level}})
      },
      lookup: function () {
        this.$data.isShow = true;
        this.$data.dealers = '';
        this.$data.total = 0;
        this.$data.query.pageNo = 1;
        this.$data.count = 0;
        this.$http.post('/admin/dealer/listDealer',this.$data.query)
          .then(function (res) {
            this.$data.isShow = false;
            this.$data.dealers = res.data.records;
            this.$data.count = res.data.count;
            this.$data.total = res.data.totalPage;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==res.data.pageNO){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
            page.innerHTML=str;
          }, function (err) {
            this.$data.isShow = false
            this.$store.commit('MESSAGE_ACCORD_SHOW', {
              text: err.statusMessage
            })
          })
      }
    },
    computed: {
      $$data:function () {
        return this.$data
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
  input,select{
    margin-right: 30px;
    height: 27px;
    font-size: 12px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .btn{
    font-size: 12px;
  }
  img{
    width: 8%;
    margin: 0 auto;
    display: inherit;
  }
  .count{
    display: inline-block;
    vertical-align: top;
    margin: 28px 10px;
  }
</style>
