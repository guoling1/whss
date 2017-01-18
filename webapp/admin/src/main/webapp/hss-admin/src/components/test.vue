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

    <input id="indate4" type="text" placeholder="请选择">
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
      /*jeDate({
        dateCell: '#indate4',
        format: "YYYY-MM-DD",
        minDate: jeDate.now(-1), //0代表今天，-1代表昨天，-2代表前天，以此类推
        maxDate: jeDate.now(5) //1代表明天，2代表后天，以此类推
      })*/
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
<style>
  /*input {
    display: block;
    margin: 20px;
    width: 240px;
    height: 30px;
    border:1px solid #0E90D2 ;
    text-indent: 5px;
    !*background: url(icon.png) no-repeat 210px;*!
  }
  pre{
    margin-left: 30px;
    color: red;
  }*/
</style>
