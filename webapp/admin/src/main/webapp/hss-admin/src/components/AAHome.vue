<template>
  <div class="wrapper">
    <app-header></app-header>

    <app-menu></app-menu>

    <app-content></app-content>

    <!--<app-footer></app-footer>-->
  </div>
</template>

<script lang="babel">
  export default{
    data(){
      return {
        msg: 'hello vue',
        tabs: ['123']
      }
    },
    created(){
//      console.log(this.$refs.child)
    },
    methods: {},
    watch:{
        tabs:function () {
console.log(arguments)
        }
    }
  }
</script>

<style scoped lang="less" rel="stylesheet/less">

</style>
