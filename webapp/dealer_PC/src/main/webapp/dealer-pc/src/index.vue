<!-- index.vue -->
<template lang="html">
  <div id="app" class="wrapper">
    <header class="main-header">

      <!-- Logo -->
      <div class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini">钱</span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg">钱包++</span>
      </div>

      <!-- Header Navbar: style can be found in header.less -->
      <nav class="navbar navbar-static-top">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
          <span class="sr-only">Toggle navigation</span>
        </a>
        <!-- Navbar Right Menu -->
        <div class="navbar-custom-menu">
          <ul class="nav navbar-nav">
            <!--<li>-->
            <!--<a href="#">-->
            <!--<span class="hidden-xs">您当前拥有0积分</span>-->
            <!--</a>-->
            <!--</li>-->
            <!-- Messages: style can be found in dropdown.less-->
            <!-- Notifications: style can be found in dropdown.less -->
            <!--<li class="dropdown notifications-menu">-->
            <!--<a href="#" class="dropdown-toggle" data-toggle="dropdown">-->
            <!--<i class="fa fa-bell-o"></i>-->
            <!--<span class="label label-warning">10</span>-->
            <!--</a>-->
            <!--<ul class="dropdown-menu">-->
            <!--<li class="header text-center">您有10条待办事项需要处理</li>-->
            <!--<li>-->
            <!--&lt;!&ndash; inner menu: contains the actual data &ndash;&gt;-->
            <!--<ul class="menu">-->
            <!--<li>-->
            <!--<a href="#">-->
            <!--<i class="fa fa-users text-aqua"></i> 5 new members joined today-->
            <!--</a>-->
            <!--</li>-->
            <!--<li>-->
            <!--<a href="#">-->
            <!--<i class="fa fa-warning text-yellow"></i> Very long description here-->
            <!--that may not fit into the-->
            <!--page and may cause design problems-->
            <!--</a>-->
            <!--</li>-->
            <!--<li>-->
            <!--<a href="#">-->
            <!--<i class="fa fa-users text-red"></i> 5 new members joined-->
            <!--</a>-->
            <!--</li>-->
            <!--<li>-->
            <!--<a href="#">-->
            <!--<i class="fa fa-shopping-cart text-green"></i> 25 sales made-->
            <!--</a>-->
            <!--</li>-->
            <!--<li>-->
            <!--<a href="#">-->
            <!--<i class="fa fa-user text-red"></i> You changed your username-->
            <!--</a>-->
            <!--</li>-->
            <!--</ul>-->
            <!--</li>-->
            <!--<li class="footer"><a href="#">查看全部</a></li>-->
            <!--</ul>-->
            <!--</li>-->
            <!-- Tasks: style can be found in dropdown.less -->
            <!-- User Account: style can be found in dropdown.less -->
            <li class="dropdown user user-menu">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                <img src="./dist/img/user2-160x160.jpg" class="user-image" alt="User Image">
                <span class="hidden-xs">{{dealerInfo}}</span>
              </a>
              <ul class="dropdown-menu">
                <!-- User image -->
                <li class="user-header">
                  <img src="./dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                  <p>
                    {{dealerInfo}}
                    <small>{{position}}</small>
                  </p>
                </li>
                <!-- Menu Body -->
                <!-- <li class="user-body"> -->
                <!-- <div class="row"> -->
                <!-- <div class="col-xs-4 text-center">
                  <a href="#">Followers</a>
                </div>
                <div class="col-xs-4 text-center">
                  <a href="#">Sales</a>
                </div>
                <div class="col-xs-4 text-center">
                  <a href="#">Friends</a>
                </div> -->
                <!-- <div class="col-xs-12 text-center">
                  可用调休时长：10小时
                </div> -->
                <!-- </div> -->
                <!-- &lt;!&ndash; /.row &ndash;&gt; -->
                <!-- </li> -->
                <!-- Menu Footer-->
                <li class="user-footer">
                  <!--<div class="pull-left">-->
                  <!--<a href="#" class="btn btn-default btn-flat">个人资料</a>-->
                  <!--</div>-->
                  <div class="pull-right">
                    <div class="btn btn-default btn-flat" @click="logout">退出登录</div>
                  </div>
                </li>
              </ul>
            </li>
            <!-- Control Sidebar Toggle Button -->
            <!--<li>-->
            <!--<a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>-->
            <!--</li>-->
          </ul>
        </div>

      </nav>
    </header>
    <!-- Left side column. contains the logo and sidebar -->
    <aside class="main-sidebar">
      <!-- sidebar: style can be found in sidebar.less -->
      <section class="sidebar">
        <!-- search form -->
        <!--<form action="#" method="get" class="sidebar-form">-->
        <!--<div class="input-group">-->
        <!--<input type="text" name="q" class="form-control" placeholder="Search...">-->
        <!--<span class="input-group-btn">-->
        <!--<button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>-->
        <!--</button>-->
        <!--</span>-->
        <!--</div>-->
        <!--</form>-->
        <!-- /.search form -->
        <!-- sidebar menu: : style can be found in sidebar.less -->
        <ul class="sidebar-menu">
          <li class="header">钱包++ Version 1.0</li>
          <li class="active">
            <router-link :to="{path:'/daili/app/home'}">
              <i class="glyphicon glyphicon-home"></i> <span>主页</span>
              <!--<span class="pull-right-container">-->
              <!--<i class="fa fa-angle-left pull-right"></i>-->
              <!--</span>-->
            </router-link>
            <!--<ul class="treeview-menu">-->
            <!--<li><a href="index.html"><i class="fa fa-circle-o"></i> Dashboard v1</a></li>-->
            <!--<li class="active"><a href="index2.html"><i class="fa fa-circle-o"></i> Dashboard v2</a></li>-->
            <!--</ul>-->
          </li>
          <li class="treeview" v-for="(menu,index) in menus">
            <a href="#">
              <i :class="classList[index]"></i>
              <span>{{menu.menuName}}</span>
              <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
                <!-- <small class="label pull-right bg-green">new</small> -->
              </span>
            </a>
            <ul class="treeview-menu">
              <li v-for="item in menu.children">
                <router-link :to="item.menuUrl"><i class="fa fa-circle-o"></i>{{item.menuName}}</router-link>
              </li>
              <!--<li>-->
              <!--<router-link to="settlement"><i class="fa fa-circle-o"></i>结算记录</router-link>-->
              <!--</li>-->
              <!--<li>-->
              <!--<router-link to="balance_withdrawal"><i class="fa fa-circle-o"></i>余额提现</router-link>-->
              <!--</li>-->
            </ul>
          </li>
          <!--<li class="treeview">
            <a href="#">
              <i class="fa fa-money"></i>
              <span>我的分润</span>
              <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
                &lt;!&ndash; <small class="label pull-right bg-green">new</small> &ndash;&gt;
              </span>
            </a>
            <ul class="treeview-menu">
              <li>
                <router-link to="profits_detail"><i class="fa fa-circle-o"></i>分润明细</router-link>
              </li>
              &lt;!&ndash;<li>&ndash;&gt;
                &lt;!&ndash;<router-link to="settlement"><i class="fa fa-circle-o"></i>结算记录</router-link>&ndash;&gt;
              &lt;!&ndash;</li>&ndash;&gt;
              <li>
                <router-link to="balance_withdrawal"><i class="fa fa-circle-o"></i>余额提现</router-link>
              </li>
            </ul>
          </li>
          <li class="treeview" v-show="dealerLeavel!=2">
            <a href="#">
              <i class="fa fa-users"></i> <span>下级代理</span>
              <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
                &lt;!&ndash; <small class="label pull-right bg-green">new</small> &ndash;&gt;
              </span>
            </a>
            <ul class="treeview-menu">
              <li>
                <router-link to="dealer_list"><i class="fa fa-circle-o"></i> 所有代理商</router-link>
              </li>
              &lt;!&ndash;<li>&ndash;&gt;
                &lt;!&ndash;<router-link to="dealer_settlement"><i class="fa fa-circle-o"></i> 代理结算记录</router-link>&ndash;&gt;
              &lt;!&ndash;</li>&ndash;&gt;
              &lt;!&ndash;<li>&ndash;&gt;
                &lt;!&ndash;<router-link to="dealer_account"><i class="fa fa-circle-o"></i> 代理商账户</router-link>&ndash;&gt;
              &lt;!&ndash;</li>&ndash;&gt;
            </ul>
          </li>
          <li class="treeview">
            <a href="#">
              <i class="fa fa-bank"></i> <span>商户管理</span>
              <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
                &lt;!&ndash; <small class="label pull-right bg-green">new</small> &ndash;&gt;
              </span>
            </a>
            <ul class="treeview-menu">
              <li>
                <router-link to="merchants"><i class="fa fa-circle-o"></i> 所有商户</router-link>
              </li>
              <li>
                <router-link to="transaction"><i class="fa fa-circle-o"></i> 交易管理</router-link>
              </li>
            </ul>
          </li>
          <li class="treeview">
            <a href="#">
              <i class="fa fa-calculator"></i> <span>设备管理</span>
              <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
                &lt;!&ndash; <small class="label pull-right bg-green">new</small> &ndash;&gt;
              </span>
            </a>
            <ul class="treeview-menu">
              <li v-show="dealerLeavel!=2">
                <router-link to="distribution_qrcode"><i class="fa fa-circle-o"></i> 分配二维码</router-link>
              </li>
              &lt;!&ndash;<li>&ndash;&gt;
                &lt;!&ndash;<router-link to="qrcode_distribution"><i class="fa fa-circle-o"></i> 二维码分配记录</router-link>&ndash;&gt;
              &lt;!&ndash;</li>&ndash;&gt;
              <li>
                <router-link to="qrcode"><i class="fa fa-circle-o"></i> 所有二维码</router-link>
              </li>
            </ul>
          </li>
          <li class="treeview">
            <a href="#">
              <i class="fa fa-user-plus"></i> <span>员工权限管理</span>
              <span class="pull-right-container">
                <i class="fa fa-angle-left pull-right"></i>
                &lt;!&ndash; <small class="label pull-right bg-green">new</small> &ndash;&gt;
              </span>
            </a>
            <ul class="treeview-menu">
              <li>
                <router-link to="employees"><i class="fa fa-circle-o"></i> 员工管理</router-link>
              </li>
              &lt;!&ndash;<li>&ndash;&gt;
                &lt;!&ndash;<router-link to="roles"><i class="fa fa-circle-o"></i> 角色管理</router-link>&ndash;&gt;
              &lt;!&ndash;</li>&ndash;&gt;
            </ul>
          </li>-->
          <!--<li class="treeview">-->
          <!--<a href="#">-->
          <!--<i class="fa fa-user-secret"></i> <span>我的信息</span>-->
          <!--<span class="pull-right-container">-->
          <!--<i class="fa fa-angle-left pull-right"></i>-->
          <!--&lt;!&ndash; <small class="label pull-right bg-green">new</small> &ndash;&gt;-->
          <!--</span>-->
          <!--</a>-->
          <!--<ul class="treeview-menu">-->
          <!--<li>-->
          <!--<router-link to="information"><i class="fa fa-circle-o"></i> 注册信息</router-link>-->
          <!--</li>-->
          <!--<li>-->
          <!--<router-link to="policy"><i class="fa fa-circle-o"></i> 代理政策</router-link>-->
          <!--</li>-->
          <!--</ul>-->
          <!--</li>-->

          <!-- <li class="header">LABELS</li>
          <li><a href="#"><i class="fa fa-circle-o text-red"></i> <span>Important</span></a></li>
          <li><a href="#"><i class="fa fa-circle-o text-yellow"></i> <span>Warning</span></a></li>
          <li><a href="#"><i class="fa fa-circle-o text-aqua"></i> <span>Information</span></a></li> -->
        </ul>
      </section>
      <!-- /.sidebar -->
    </aside>

    <!-- Content Wrapper. Contains page content -->
    <router-view></router-view>
    <!-- /.content-wrapper -->

    <!-- /.content-wrapper -->
    <!-- global message -->
    <div id="message">
      <!-- 内容将动态填充 -->
    </div>

  </div>
</template>
<script lang="babel">
  import store from './store'
  export default {
    data () {
      return {
        dealerInfo: '',
        position: '',
        dealerLeavel: '',
        list: {},
        classList: ['fa fa-money', 'fa fa-users', 'fa fa-bank', 'fa fa-calculator', 'fa fa-user-plus']
      }
    },
    beforeRouteEnter (to, from, next){
      store.dispatch('actions_users_getInfo').then(function (data) {
        next((vm) => {
          if (data.status === 1) {
            vm.dealerInfo = data.dealerInfo;
            if (data.dealerLeavel == 1) {
              vm.position = '一级代理商'
            } else if (data.dealerLeavel == 2) {
              vm.position = '二级代理商'
            } else {
              vm.position = '分公司'
            }
            vm.dealerLeavel = data.dealerLeavel;
          }
        });
      });
    },
    // 首次创建会判断是否在移动端访问
    created () {
      const sUserAgent = navigator.userAgent.toLowerCase();
      const bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
      const bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
      const bIsMidp = sUserAgent.match(/midp/i) == "midp";
      const bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
      const bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
      const bIsAndroid = sUserAgent.match(/android/i) == "android";
      const bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
      const bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
      if (bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) {
        $("a[href!='#']").attr("data-toggle", "offcanvas");
      }
      this.menus = JSON.parse(sessionStorage.getItem('login'));
    },
    methods: {
      logout: function () {
        this.$http.post('/daili/login/logout').then(() => {
          this.$store.dispatch('actions_users_clearInfo');
          this.$router.push('/daili/login');
        }, (err) => {
          console.log(err);
        })
      }
    }
  }
</script>
