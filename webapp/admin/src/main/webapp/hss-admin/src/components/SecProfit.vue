<template lang="html">
  <div id="secProfit">
    <h1>二级代理分润</h1>
    <div class="search">
      <span>日期：</span>
      <input type="date" v-model="begin">
      <span>至</span>
      <input type="date" v-model="end">
      <div class="btn btn-primary" @click="lookup()">筛选</div>
    </div>
    <div class="box">
      <div class="box-header">
        <h3 class="box-title">分润记录</h3>
      </div>
      <!-- /.box-header -->
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
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">序号
                  </th>
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">二级代理商名称
                  </th>
                  <th class="sorting_asc" tabindex="0" aria-controls="example2" rowspan="1" colspan="1"
                      aria-sort="ascending" aria-label="Rendering engine: activate to sort column descending">收益日期
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Platform(s): activate to sort column ascending">收单收益
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="Engine version: activate to sort column ascending">结算收益
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">收益总额
                  </th>
                  <th class="sorting" tabindex="0" aria-controls="example2" rowspan="1" colspan="1" aria-label="CSS grade: activate to sort column ascending">操作
                  </th>
                </tr>
                </thead>
                <tbody id="content">
                <tr role="row" v-for="(record,index) in $$records">
                  <td>{{index+1}}</td>
                  <td>{{record.dealerName}}</td>
                  <td>{{record.statisticsDate}}</td>
                  <td style="text-align: right">{{record.collectMoney|toFix}}</td>
                  <td style="text-align: right">{{record.withdrawMoney|toFix}}</td>
                  <td style="text-align: right">{{record.totalMoney|toFix}}</td>
                  <td><route-link to="/admin/record/companyProfitDet" class="btn btn-success">查看明细</route-link></td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-5">
              <div class="dataTables_info" id="example2_info" role="status" aria-live="polite">
              </div>
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
  </div>
</template>

<script lang="babel">
  export default {
    name: 'secProfit',
    data () {
      return {
        msg:{
          pageNo:1,
          pageSize:10,
          beginProfitDate:'',
          endProfitDate:''
        },
        begin:'',
        end:'',
        records:[],
        total:1
      }
    },
    created: function () {
      if(this.$route.path=='/admin/record/firProfit'){
        this.$data.path = '/admin/profit/firstProfit'
      }else if(this.$route.path=='/admin/record/secProfit'){
        this.$data.path = '/admin/profit/secondProfit'
      }
      this.$http.post(this.$data.path,this.$data.msg)
        .then(function (res) {
          this.$data.records = res.data.records;
          this.$data.total=res.data.totalPage;
          var str='',
            page=document.getElementById('page');
          str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
          for (var i=1; i<=this.$data.total;i++){
            if(i==this.$data.msg.pageNo){
              str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
              continue;
            }
            str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
          }
          str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
          page.innerHTML=str;
          var aLi = page.getElementsByTagName('li');
          if(this.$data.msg.pageNo<6){
            for(var i=0;i<aLi.length;i++){
              if(i>11){
                aLi[i].style.display='none'
              }
            }
          }else if(this.$data.msg.pageNo>(this.$data.total-5)){
            for(var i=0;i<aLi.length;i++){
              if(i<(this.$data.total-12)){
                aLi[i].style.display='none'
              }
            }
          }else{
            for(var i=0;i<aLi.length;i++){
              if((i!=0&&i<this.$data.msg.pageNo-5)||(i!=this.$data.total+1&&i>this.$data.msg.pageNo+4)){
                aLi[i].style.display='none'
              }
            }
          }
        },function (err) {
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
          n = this.$data.msg.pageNo;
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
        this.$data.msg.pageNo = n;
        this.$http.post(this.$data.path,this.$data.msg)
          .then(function (res) {
            this.$data.records=res.data.records;
            this.$data.total=res.data.totalPage;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==this.$data.msg.pageNo){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
            page.innerHTML=str;
            var aLi = page.getElementsByTagName('li');
            if(this.$data.msg.pageNo<6){
              for(var i=0;i<aLi.length;i++){
                if(i>11){
                  aLi[i].style.display='none'
                }
              }
            }else if(this.$data.msg.pageNo>(this.$data.total-5)){
              for(var i=0;i<aLi.length;i++){
                if(i<(this.$data.total-12)){
                  aLi[i].style.display='none'
                }
              }
            }else{
              for(var i=0;i<aLi.length;i++){
                if((i!=0&&i<this.$data.msg.pageNo-5)||(i!=this.$data.total+1&&i>this.$data.msg.pageNo+4)){
                  aLi[i].style.display='none'
                }
              }
            }
          },function (err) {
            console.log(err)
          })
      },
      lookup: function () {
        if(this.$data.begin!=''){
          this.$data.msg.beginProfitDate=this.$data.begin.replace(/\//g,'-')
        }
        if(this.$data.end!=''){
          this.$data.msg.endProfitDate=this.$data.end.replace(/\//g,'-')
        }
        this.$http.post(this.$data.path,this.$data.msg)
          .then(function (res) {
            this.$data.records=res.data.records;
            this.$data.total=res.data.totalPage;
            var str='',
              page=document.getElementById('page');
            str+='<li class="paginate_button previous disabled" id="example2_previous"><a href="#" aria-controls="example2" data-dt-idx="0" tabindex="0">上一页</a></li>'
            for (var i=1; i<=this.$data.total;i++){
              if(i==this.$data.msg.pageNo){
                str+='<li class="paginate_button active"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>';
                continue;
              }
              str+='<li class="paginate_button"><a href="#" aria-controls="example2" data-dt-idx="1" tabindex="0">'+i+'</a></li></li>'
            }
            str+='<li class="paginate_button next" id="example2_next"><a href="#" aria-controls="example2" data-dt-idx="7" tabindex="0">下一页</a></li>'
            page.innerHTML=str;
            var aLi = page.getElementsByTagName('li');
            if(this.$data.msg.pageNo<6){
              for(var i=0;i<aLi.length;i++){
                if(i>11){
                  aLi[i].style.display='none'
                }
              }
            }else if(this.$data.msg.pageNo>(this.$data.total-5)){
              for(var i=0;i<aLi.length;i++){
                if(i<(this.$data.total-12)){
                  aLi[i].style.display='none'
                }
              }
            }else{
              for(var i=0;i<aLi.length;i++){
                if((i!=0&&i<this.$data.msg.pageNo-5)||(i!=this.$data.total+1&&i>this.$data.msg.pageNo+4)){
                  aLi[i].style.display='none'
                }
              }
            }
          },function (err) {
            console.log(err)
          })
      },
    },
    computed: {
      $$records: function () {
        return this.$data.records;
      }
    },
    filters: {
      toFix: function (val) {
        return parseFloat(val).toFixed(2)
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
    margin:20px 0;
  span{
    font-size: 18px;

  }
  span:nth-child(2){
    margin:0 10px;
  }
  }
  .box-title {
    display: inline-block;
    font-size: 20px;
    line-height: 34px;
    height: 34px;
  }
</style>
