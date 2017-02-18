<template lang="html">
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script lang="babel">
  export default {
    name: 'app'
  }
</script>

<style lang="less">
  #app {
    font-family: 'Microsoft YaHei' !important;
    font-size: 12px !important;
  }

  #app .el-table, #app .sidebar-menu .treeview-menu > li > a, #app .el-form-item__label, #app .el-radio__label, #app .el-form-item__content {
    font-size: 12px !important;
  }

  #app .main-sidebar, #app .main-header .logo {
    width: 150px !important;
  }

  .sidebar-collapse #app .main-sidebar, .sidebar-collapse #app .main-header .logo {
    width: 50px !important;
  }

  #app .content-wrapper, #app .main-header .navbar {
    margin-left: 150px;
  }

  .sidebar-collapse #app .content-wrapper, .sidebar-collapse #app .main-header .navbar {
    margin-left: 50px;
  }

  #app .col-xs-13 {
    width: 60%;
    position: relative;
    min-height: 1px;
    padding-right: 15px;
    padding-left: 15px;
    float: left;
  }

  #app .el-form-item {
    margin-bottom: 12px;
  }

  @media ( max-width: 767px ) {
    #app .col-xs-13 {
      width: 100%;
    }

    #app .content-wrapper, #app .main-header .navbar {
      margin-left: 0 !important;
    }

    #app .main-header .logo {
      width: 100% !important;
    }

    .sidebar-open .content-wrapper {
      -webkit-transform: translate(150px) !important;
      transform: translate(150px) !important;
    }
  }
</style>
