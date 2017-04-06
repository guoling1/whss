<template lang="html">
  <div id="main" class="wrapper">
    <router-view></router-view>
    <message></message>
  </div>
</template>

<script lang="babel">
  import Message from './components/Message.vue'
  export default {
    name: 'main',
    components: {
      Message
    }
  }
</script>

<style lang="less">

</style>
