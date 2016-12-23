<template lang="html">
  <div id="withdrawalDet">
    <div style="padding: 8px 30px; background: rgb(243, 156, 18); z-index: 999999; font-size: 22px; font-weight: 600;margin-bottom: 15px;color: #fff;">打款详情</div>
    <div style="margin: 0 15px">
      <div class="box box-primary">
        <p class="lead">Amount Due 2/22/2014</p>

        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            </tbody></table>
        </div>
      </div>
      <div class="box box-primary">
        <p class="lead">Amount Due 2/22/2014</p>

        <div class="table-responsive">
          <table class="table">
            <tbody>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            <tr>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
              <th style="text-align: right">Subtotal:</th>
              <td>$250.30</td>
            </tr>
            </tbody></table>
        </div>
      </div>
    </div>
    <message></message>
  </div>
  </div>
</template>

<script lang="babel">
  import Message from './Message.vue'
  export default {
    name: 'withdrawalDet',
    components: {
      Message
    },
    data: function () {
      return {
        name:'',
        query:{
          dealerId:'',
          startCode:'',
          endCode:''
        },
        findDealers:[],
        listIsShow: false
      }
    },
    methods: {

    },
    computed: {

    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="less">
  .flexItem(@val) {
    box-flex: @val;
    -webkit-box-flex: @val;
    -webkit-flex: @val;
    -ms-flex: @val;
    flex: @val;
  }

  .flexBox {
    display: box;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    display: -webkit-flex;
  }
  h1, h2 {
    font-weight: normal;
    color: #337ab7;
    font-weight: bold;
    border-bottom: 2px solid #ccc;
    padding-bottom: 10px;
  }

  ul {
    list-style-type: none;
    padding: 0;
    background: #fff;
    position: relative;
    left: 70px;
    width: 300px;
  }

  li {
    /*display: inline-block;*/
    margin: 0 10px;
  }

  a {
    color: #42b983;
  }

</style>
