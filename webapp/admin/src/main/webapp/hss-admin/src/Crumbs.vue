<template lang="html">
  <div id="app" class="wrapper" style="background: inherit">
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
