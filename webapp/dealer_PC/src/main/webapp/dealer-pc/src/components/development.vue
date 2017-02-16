<template lang="html">
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>
        钱包++代理商系统
        <small>Version 1.0</small>
      </h1>
      <ol class="breadcrumb">
        <li>
          <router-link to="/app/home"><i class="glyphicon glyphicon-home"></i> 主页</router-link>
        </li>
        <!--<li class="active">Dashboard</li>-->
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <h4 class="text-center">
        您好，{{dealerInfo}}
      </h4>
      <h2 class="text-center">
        welcome
      </h2>
    </section>
  </div>
</template>
<script lang="babel">
  import store from '../store'
  export default {
    data () {
      return {
        dealerInfo: ''
      }
    },
    mounted (){
      this.$store.dispatch('actions_users_getInfo').then(data => {
        if (data.status === 1) {
          this.dealerInfo = data.dealerInfo;
        }
      });
    },
    methods: {}
  }
</script>
