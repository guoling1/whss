<template lang="html">
  <div class="content-wrapper">
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
