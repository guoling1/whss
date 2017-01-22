<template>
  <div>
    <div class="col-md-12">

      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">交易查询</h3>
        </div>
        <div class="box-body">
          <el-row :gutter="20">
            <el-col :span="6">
              <label>商户编号</label>
              <el-input placeholder="请输入内容"></el-input>
            </el-col>
            <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
            <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
            <el-col :span="6"><div class="grid-content bg-purple"></div></el-col>
          </el-row>
          <!--<div class="row">

          </div>-->
          <div id="example2_wrapper" class="dataTables_wrapper form-inline dt-bootstrap">
            <div class="row">
              <div class="col-sm-12">
                <table id="example2" class="table table-bordered table-hover dataTable" role="grid"
                       aria-describedby="example2_info">
                  <thead>
                  <tr role="row">

                  </tr>
                  </thead>
                  <tbody id="content">
                  <tr role="row" >

                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-if="isShow">
              <img src="http://img.jinkaimen.cn/admin/common/dist/img/ICBCLoading.gif" alt="">
            </div>
            <!--<div v-if="orders.length==0&&!isShow" class="row" style="text-align: center;color: red;font-size: 16px;">
              <div class="col-sm-12">无此数据</div>
            </div>-->
            <!--<div class="row">
              <div class="col-sm-5">
                <div class="dataTables_info" id="example2_info" role="status" aria-live="polite">
                </div>
              </div>
              <div class="col-sm-7">
                <div class="dataTables_paginate paging_simple_numbers" id="example2_paginate">
                  <ul class="pagination" id="page" @click="bindEvent($event)">
                  </ul>
                  <span class="count">共{{count}}条</span>
                </div>
              </div>
            </div>-->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
    export default{
      name: 't1Audit',
        data(){
            return{

            }
        },
    }
</script>
<style scoped lang="less">
  body{
    background-color:#ff0000;
  }
</style>
