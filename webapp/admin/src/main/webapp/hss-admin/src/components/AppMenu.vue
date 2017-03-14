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
        <!-- <li class="header">HEADER</li>
         Optionally, you can add icons to the links
        <li><a href="#"><i class="fa fa-link"></i> <span>Link</span></a></li>-->
        <li :class="(url=='/admin/record/newDeal')?'treeview active':'treeview'">
          <a href="#"><span>交易查询</span></a>
          <ul class="treeview-menu">
            <li :class="(url=='/admin/record/newDeal')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/newDeal"><span>交易查询</span></router-link>
            </li>
          </ul>
        </li>
        <li :class="(url=='/admin/record/profitCom'||url=='/admin/record/profitFir'||url=='/admin/record/profitSec'||url=='/admin/record/profitAccount'||url=='/admin/record/profitDet')?'treeview active':'treeview'">
          <a href="#"><span>分润管理</span>
          </a>
          <ul class="treeview-menu">
            <li @click="refrash" :class="(url=='/admin/record/profitDet')?'treeview active':'treeview'"><router-link to="/admin/record/profitDet"><span>分润明细</span> </router-link></li>
            <li :class="(url=='/admin/record/profitCom'||url=='/admin/record/profitFir'||url=='/admin/record/profitSec')?'treeview active':'treeview'">
              <a href="#" @click="open"><span>分润统计</span>
                <span class="pull-right-container">
                  <i id="right" class="el-icon-arrow-left pull-right"></i>
                </span>
                <!--<i class="el-icon-arrow-left"></i>-->
              </a>
              <ul class="treeview-menu" style="margin-left: -8px">
                <li @click="refrash" :class="(url=='/admin/record/profitCom')?'treeview active':'treeview'"><router-link to="/admin/record/profitCom"><span>公司分润</span> </router-link></li>
                <li @click="refrash" :class="(url=='/admin/record/profitFir')?'treeview active':'treeview'"><router-link to="/admin/record/profitFir"><span>一级代理商分润</span> </router-link></li>
                <li @click="refrash" :class="(url=='/admin/record/profitSec')?'treeview active':'treeview'"><router-link to="/admin/record/profitSec"><span>二级代理商分润</span> </router-link></li>
              </ul>
            </li>
            <li @click="refrash" :class="(url=='/admin/record/profitAccount')?'treeview active':'treeview'"><router-link to="/admin/record/profitAccount"><span>公司分润账户</span> </router-link></li>
          </ul>
        </li>
        <li :class="(url=='/admin/record/t1Audit'||url=='/admin/record/tAuditStore'||url=='/admin/record/tAuditDealer')?'treeview active':'treeview'">
          <a href="#"><span>结算管理</span></a>
          <ul class="treeview-menu">
            <li :class="(url=='/admin/record/t1Audit')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/t1Audit"><span>T1结算审核</span></router-link>
            </li>
            <li :class="(url=='/admin/record/tAuditStore')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/tAuditStore"><span>商户结算记录</span></router-link>
            </li>
            <li :class="(url=='/admin/record/tAuditDealer')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/tAuditDealer"><span>代理商结算记录</span></router-link>
            </li>
          </ul>
        </li>
        <li :class="(url=='/admin/record/payQuery'||url=='/admin/record/newWithdrawalQuery')?'treeview active':'treeview'">
          <a href="#"><span>渠道交易查询</span></a>
          <ul class="treeview-menu">
            <li :class="(url=='/admin/record/payQuery')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/payQuery"><span>支付查询</span></router-link>
            </li>
            <li :class="(url=='/admin/record/newWithdrawalQuery')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/newWithdrawalQuery"><span>打款查询</span></router-link>
            </li>
          </ul>
        </li>
        <li :class="(url=='/admin/record/storeList'||url=='/admin/record/storeAuditList'||url=='/admin/record/storeNotice')?'treeview active':'treeview'">
          <a href="#"><span>商户管理</span>
          </a>
          <ul class="treeview-menu">
            <li :class="(url=='/admin/record/storeList')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/storeList"><span>商户列表</span></router-link>
            </li>
            <!--<li @click="refrash"><router-link to="/admin/record/storeAccount"><span>商户账户</span> </router-link></li>-->
            <li :class="(url=='/admin/record/storeAuditList')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/storeAuditList"><span>待审核商户</span></router-link>
            </li>
            <li :class="(url=='/admin/record/storeNotice')?'treeview active':'treeview'" @click="refrash">
              <router-link to="/admin/record/storeNotice"><span>商户公告</span></router-link>
            </li>
          </ul>
          </router-link>
        </li>
        <li
          :class="(url=='/admin/record/agentListFir'||url=='/admin/record/agentListSec')?'treeview active':'treeview'">
          <a href="#"><span>代理商管理</span>
          </a>
          <ul class="treeview-menu">
            <li @click="refrash" :class="(url=='/admin/record/agentListFir')?'treeview active':'treeview'">
              <router-link to="/admin/record/agentListFir"><span>一级代理商</span></router-link>
            </li>
            <li @click="refrash" :class="(url=='/admin/record/agentListSec')?'treeview active':'treeview'">
              <router-link to="/admin/record/agentListSec"><span>二级代理商</span></router-link>
            </li>
          </ul>
        </li>
        <li
          :class="(url=='/admin/record/issueRecord'||url=='/admin/record/issue'||url=='/admin/record/codeStatus'||url=='/admin/record/codeProRecord')?'treeview active':'treeview'">
          <a href="#"><span>设备管理</span></a>
          <ul class="treeview-menu">
            <!--<li :class="(url=='/admin/record/issue1')?'active':''" @click="refrash"><router-link to="/admin/record/issue1"><span>按个数分配</span> </router-link></li>-->
            <li :class="(url=='/admin/record/issue')?'active':''" @click="refrash">
              <router-link to="/admin/record/issue"><span>分配二维码</span></router-link>
            </li>
            <li :class="(url=='/admin/record/issueRecord')?'active':''" @click="refrash">
              <router-link to="/admin/record/issueRecord"><span>二维码分配记录</span></router-link>
            </li>
            <li :class="(url=='/admin/record/codeStatus')?'active':''" @click="refrash">
              <router-link to="/admin/record/codeStatus"><span>二维码状态查询</span></router-link>
            </li>
            <li :class="(url=='/admin/record/codeProRecord')?'active':''" @click="refrash">
              <router-link to="/admin/record/codeProRecord"><span>产码记录</span></router-link>
            </li>
          </ul>
        </li>
        <li
          :class="(url=='/admin/record/productList'||url=='/admin/record/productAdd'||url=='/admin/record/invite')?'treeview active':'treeview'">
          <a href="#"><span>产品管理</span>
          </a>
          <ul class="treeview-menu">
            <li @click="refrash" :class="(url=='/admin/record/productList')?'treeview active':'treeview'">
              <router-link to="/admin/record/productList"><span>产品列表</span></router-link>
            </li>
            <li @click="refrash" :class="(url=='/admin/record/productAdd')?'treeview active':'treeview'">
              <router-link to="/admin/record/productAdd"><span>新增产品</span></router-link>
            </li>
            <li @click="refrash" :class="(url=='/admin/record/invite')?'treeview active':'treeview'">
              <router-link to="/admin/record/invite"><span>合伙人推荐</span></router-link>
            </li>
          </ul>
        </li>
        <li class="treeview"
            :class="(url=='/admin/record/passList'||url=='/admin/record/passAdd')?'treeview active':'treeview'">
          <a href="#"><span>通道管理</span>
          </a>
          <ul class="treeview-menu">
            <li @click="refrash" :class="(url=='/admin/record/passList')?'treeview active':'treeview'">
              <router-link to="/admin/record/passList"><span>通道列表</span></router-link>
            </li>
            <li @click="refrash" :class="(url=='/admin/record/passAdd')?'treeview active':'treeview'">
              <router-link to="/admin/record/passAdd"><span>新增通道</span></router-link>
            </li>
          </ul>
        </li>

        <li
          :class="(url=='/admin/record/personnelList')?'treeview active':'treeview'">
          <a href="#"><span>员工权限管理</span></a>
          <ul class="treeview-menu">
            <li :class="(url=='/admin/record/personnelList')?'active':''" @click="refrash">
              <router-link to="/admin/record/personnelList"><span>员工管理</span></router-link>
            </li>
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
        msg: '注册',
        url: ''
      }
    },
    created: function () {
      this.$data.url = location.pathname;
      /*if(document.getElementById('open').className=='treeview active'){
       document.getElementById('right').className=''
       }*/
    },
    methods: {
      refrash: function () {
        location.reload()
      },
      open: function () {
        var obj = document.getElementById('right');
        if (obj.className == 'el-icon-arrow-down  pull-right') {
          obj.className = 'el-icon-arrow-left  pull-right'
        } else {
          obj.className = 'el-icon-arrow-down  pull-right'
        }
      }
    }
  }
</script>
<style scoped lang="less">
  /*.content-wrapper{*/
  /*width: 85%;*/
  /*float: right;*/
  /*}*/
  .sidebar-menu .treeview-menu > li > a {
    font-size: 12px;
  }

  #right {
    width: 5px;
    height: 5px;
  }
</style>
