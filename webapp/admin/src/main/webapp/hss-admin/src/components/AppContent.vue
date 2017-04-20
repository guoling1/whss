<template lang="html">
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="margin-left: 130px;" id="content">
    <!-- Content Header (Page header) -->
    <!-- <section class="content-header">
      <h1>
        Page Header
        <small>Optional description</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
        <li class="active">Here</li>
      </ol>
    </section> -->

    <!-- Main content -->
    <section class="content" style="overflow: hidden;padding: 0;min-height: 850px">

      <!-- Your Page Content Here -->

      <!-- 路由出口 -->
      <!-- 路由匹配到的组件将渲染在这里 -->
      <router-view></router-view>

    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
</template>

<script lang="babel">
export default {
  name: 'collection',
  data () {
    return {
      msg: '注册'
    }
  },
  attached:function () {
    document.getElementById('content').style.height = (document.documentElement.clientHeight-98)+'px';
  }
}
</script>

<style scoped lang="less">
  /*.content-wrapper{*/
     /*width: 85%;*/
     /*float: right;*/
  /*}*/
</style>
