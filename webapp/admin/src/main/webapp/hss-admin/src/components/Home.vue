<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <h4 class="text-center top">
        您好，{{userName}}
      </h4>
      <h2 class="text-center bottom">
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
        userName: ''
      }
    },
    created:function () {
      this.userName = JSON.parse(sessionStorage.getItem('user'));
    },
    methods: {}
  }
</script>
<style scoped lang="less">
  .top {
    margin-top: 200px;
  }

  .bottom {
    margin-bottom: 200px;
  }
</style>
