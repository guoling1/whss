<template lang="html">
  <!-- Main Header -->
  <header class="main-header">
    <!-- Logo -->
    <a href="index2.html" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini">金</span>
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg">好收银</span>
    </a>
    <nav class="navbar navbar-static-top" role="navigation">
      <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>

      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li class="dropdown user user-menu">
            <div class="loginInfo">
              <span class="name">超级管理员</span>
              <span class="btn btn-danger" @click="logout">退出</span>
            </div>
          </li>
        </ul>
      </div>
    </nav>

  </header>
</template>

<script lang="babel">
  export default {
    name: 'collection',
    data: function () {
      return {}
    },
    methods: {
      logout: function () {
        this.$http.post('/admin/user/logout').then(function () {
          this.$router.push('/login')
        }, function (err) {
          console.log(err);
        })
      }
    }
  }
</script>

<style scoped lang="less">
  .loginInfo {
    line-height: 50px;
    margin-right: 15px;
    .name {
      color: #FFF;
      margin-right: 15px;
    }
  }
</style>
