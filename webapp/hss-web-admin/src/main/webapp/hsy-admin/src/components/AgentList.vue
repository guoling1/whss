<template lang="html">
  <div id="agentList">
    <h1>代理商列表</h1>

    <div class="col-md-12">
      <div class="box box-success box-solid">
        <div class="box-header with-border">
          <h3 class="box-title">筛选条件</h3>

          <div class="box-tools pull-right">
            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
            </button>
          </div>
          <!-- /.box-tools -->
        </div>
        <!-- /.box-header -->
        <div class="box-body">
          <label for="">
            手机号：
            <input type="tel" v-model="$$data.mobile">
          </label>
          <label for="">
            名称：
            <input type="text" v-model="$$data.name">
          </label>
          <label for="">
            级别：
            <select class="fun" name="" v-model="$$data.level">
              <option value="1">一级代理商</option>
              <option value="2">二级代理商</option>
            </select>
          </label>
          <div class="btn btn-primary" @click="lookup">
            筛选
          </div>
        </div>
        <!-- /.box-body -->
      </div>
      <!-- /.box -->
    </div>
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
                      代理名称
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Browser: activate to sort column ascending">手机号
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">所属区域
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">级别
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">银行账户名称
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">银行卡号
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">银行预留手机号
                    </th>
                    <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作
                    </th>
                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" class="odd" v-for="(dealer,index) in dealers">
                    <td class="sorting_1">{{dealer.proxyName}}</td>
                    <td>{{dealer.mobile}}</td>
                    <td>{{dealer.belongArea}}</td>
                    <td>{{dealer.level}}</td>
                    <td>{{dealer.bankAccountName}}</td>
                    <td>{{dealer.settleBankCard}}</td>
                    <td>{{dealer.bankReserveMobile}}</td>
                    <td><div class="btn btn-primary" @click="detail(index,dealer.id)">修改</div></td>
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

    <div class="btn btn-primary" @click="create">
      新增代理商
    </div>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'agentList',
    data () {
      return {
        dealers: [],
        pageSize:10,
        pageNO:1,
        mobile:'',
        belongArea:'',
        id:'',
        level:1,
        name:'',
        total:0
      }
    },
    created: function () {
      this.$http.post('/admin/dealer/listDealer',{
        pageNo:1,
        pageSize:10,
        mobile:this.$data.mobile,
        belongArea:this.$data.belongArea,
        id:this.$data.id,
        name:this.$data.name,
        level:this.$data.level
      })
        .then(function (res) {
          this.$data.dealers = res.data.records;
          this.$data.total = res.data.totalPage;
          this.$data.pageNO = res.data.pageNO;
          console.log(this.$data)
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a></li>'
          for (var i=1; i<=this.$data.total;i++){
            if(i==res.data.pageNO){
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
        this.$http.post('/admin/dealer/listDealer',{
          pageNo:this.$data.pageNo,
          pageSize:10
        })
          .then(function (res) {
            this.$data.dealers = res.data.records;
            this.$data.total = res.data.totalPage;
            this.$data.pageNO = res.data.pageNO;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==res.data.pageNO){
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
      create: function () {
        this.$router.push('/admin/record/agentAdd')
      },
      detail: function (val,id) {
        this.$router.push({path:'/admin/record/agentAdd',query:{id:id}})
      },
      lookup: function () {
        this.$http.post('/admin/dealer/listDealer',{
          pageNo:1,
          pageSize:10,
          mobile:this.$data.mobile,
          belongArea:this.$data.belongArea,
          id:this.$data.id,
          name:this.$data.name,
          level:this.$data.level
        })
          .then(function (res) {
            this.$data.dealers = res.data.records;
            this.$data.pageSize = res.data.totalPage;
            this.$data.pageNO = res.data.pageNO;
            console.log(this.$data)
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">Previous</a></li>'
            for (var i=1; i<=this.$data.pageSize;i++){
              if(i==res.data.pageNO){
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

  a {
    color: #42b983;
  }
  input{
    margin-right: 30px;
  }

</style>
