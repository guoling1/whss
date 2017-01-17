<template>
  <div>
    <select name=""  v-model="province">
      <option value="">请选择</option>
      <option :value="province.name" v-for="province in $$data.provinces">{{province.name}}</option>
    </select>
    <select name="" v-model="city">
      <option value="">请选择</option>
      <option :value="city.name" v-for="city in $$data.citys">{{city.name}}</option>
    </select>
    <select name=""  v-model="district">
      <option value="">请选择</option>
      <option :value="district" v-for="district in $$data.districts">{{district}}</option>
    </select>
  </div>
</template>

<script>
  var msg = require('../../city.json')
  export default {
    name:'xx',
    data(){
      return {
        provinces:'',
        citys:'',
        districts:'',
        province:'',
        city:'',
        district:''
      }
    },
    created:function () {
      this.$data.provinces = msg;
      if(this.$data.province!=''){
        for(var i=0; i<this.$data.provinces.length; i++){
          if(this.$data.provinces[i].name==this.$data.province){
            this.$data.citys = this.$data.provinces[i].city
          }
        }
      }
    },
    watch: {
      province: function (val, oldval) {
        if(val!=oldval){
          this.$data.city = '';
          this.$data.district = '';
        }
        if(this.$data.province!=''){
          for(var i=0; i<this.$data.provinces.length; i++){
            if(this.$data.provinces[i].name==this.$data.province){
              this.$data.citys = this.$data.provinces[i].city
            }
          }
        }
      },
      city: function (val, oldval) {
        if(this.$data.city!=''){
          for(var i=0; i<this.$data.citys.length; i++){
            if(this.$data.citys[i].name==this.$data.city){
              this.$data.districts = this.$data.citys[i].area
            }
          }
        }
      }
    },
    computed:{
      $$data:function () {
        return this.$data
      }
    }
  }
</script>
