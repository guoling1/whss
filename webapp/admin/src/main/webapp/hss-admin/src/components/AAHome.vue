<template>
  <div class="wrapper">
    <app-header></app-header>

    <app-menu></app-menu>

    <app-content ></app-content>

    <!--<app-footer></app-footer>-->
  </div>
</template>

<script lang="babel">
  export default{
    data(){
      return {
        msg: 'hello vue'
      }
    },
    created(){

    },
    methods: {}
  }
</script>

<style scoped lang="less" rel="stylesheet/less">

</style>
