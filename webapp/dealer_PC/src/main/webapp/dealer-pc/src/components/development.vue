<template lang="html">
  <div class="content-wrapper">
    <!-- Main content -->
    <section class="content">
      <h4 class="text-center">
        您好，{{dealerInfo}}
      </h4>
      <img class="dev" src="../assets/development.jpg">
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
<style scoped lang="less">
  .dev {
    display: block;
    margin: 50px auto;
  }
</style>
