<template lang="html">
  <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar" style="width: 130px">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">

      <!-- Sidebar user panel (optional) -->
      <!-- <div class="user-panel">
        <div class="pull-left image">
          <img src="/static/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p>Alexander Pierce</p>

          <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
        </div>
      </div> -->
      <!-- search form (Optional) -->
      <!-- <form action="#" method="get" class="sidebar-form">
        <div class="input-group">
          <input type="text" name="q" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
        </div>
      </form> -->
      <!-- /.search form -->
      <!-- Sidebar Menu -->
      <ul class="sidebar-menu nav nav-pills nav-stacked">
        <!-- <li class="header">HEADER</li> -->
        <!-- Optionally, you can add icons to the links -->
        <!--<li><a href="#"><i class="fa fa-link"></i> <span>Link</span></a></li>-->
        <li class="treeview active">
          <a href="#"><i class="fa fa-link"></i> <span>交易管理</span>
          </a>
          <ul class="treeview-menu">
            <li class="active" @click="refrash"><router-link to="/admin/record/newDeal"><span>交易查询</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/payQuery"><span>支付查询</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/newWithdrawalQuery"><span>打款查询</span> </router-link></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#"><i class="fa fa-link"></i> <span>商户管理</span>
          </a>
          <ul class="treeview-menu">
            <li class="active" @click="refrash"><router-link to="/admin/record/storeList"><span>商户列表</span> </router-link></li>
            <!--<li @click="refrash"><router-link to="/admin/record/storeAccount"><span>商户账户</span> </router-link></li>-->
            <li class="active" @click="refrash"><router-link to="/admin/record/storeAuditList"><span>待审核商户</span> </router-link></li>
          </ul>
          </router-link>
        </li>
        <li class="treeview">
          <a href="#"><i class="fa fa-link"></i><span>代理商管理</span>
          </a>
          <ul class="treeview-menu">
            <li @click="refrash"><router-link to="/admin/record/agentList"><span>代理商列表</span> </router-link></li>
            <!--<li @click="refrash"><router-link to="/admin/record/agentAccount"><span>代理商账户</span> </router-link></li>-->
            <li @click="refrash"><router-link to="/admin/record/agentAdd"><span>新增代理商</span> </router-link></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#"><i class="fa fa-link"></i><span>分润管理</span>
          </a>
          <ul class="treeview-menu">
            <li @click="refrash"><router-link to="/admin/record/companyProfit"><span>公司分润</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/firProfit"><span>一级代理商分润</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/secProfit"><span>二级代理商分润</span> </router-link></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#"><i class="fa fa-link"></i><span>产品管理</span>

          </a>
          <ul class="treeview-menu">
            <li @click="refrash"><router-link to="/admin/record/productList"><span>产品列表</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/productAdd"><span>新增产品</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/invite"><span>合伙人推荐</span> </router-link></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#"><i class="fa fa-link"></i><span>通道管理</span>
          </a>
          <ul class="treeview-menu">
            <li @click="refrash"><router-link to="/admin/record/passList"><span>通道列表</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/passAdd"> <span>新增通道</span> </router-link></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#"><i class="fa fa-link"></i><span>分配二维码</span>

          </a>
          <ul class="treeview-menu">
            <li @click="refrash"><router-link to="/admin/record/issue1"><span>按个数分配</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/issue"><span>按码段分配</span> </router-link></li>
            <li @click="refrash"><router-link to="/admin/record/codeStatus"><span>二维码状态查询</span> </router-link></li>
          </ul>
        </li>
      </ul>
      <!-- /.sidebar-menu -->
    </section>
    <!-- /.sidebar -->
  </aside>
</template>

<script lang="babel">
export default {
  name: 'collection',
  data () {
    return {
      msg: '注册'
    }
  },
  methods:{
    refrash: function () {
      window.location.reload()
    }
  }
}
</script>
