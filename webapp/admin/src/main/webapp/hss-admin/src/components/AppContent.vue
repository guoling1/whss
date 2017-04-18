<!--<template lang="html">
  &lt;!&ndash; Content Wrapper. Contains page content &ndash;&gt;
  <div class="content-wrapper" style="margin-left: 130px">
    &lt;!&ndash; Content Header (Page header) &ndash;&gt;
    &lt;!&ndash; <section class="content-header">
      <h1>
        Page Header
        <small>Optional description</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
        <li class="active">Here</li>
      </ol>
    </section> &ndash;&gt;

    &lt;!&ndash; Main content &ndash;&gt;
    <section class="content" style="overflow: hidden;padding: 0">

      &lt;!&ndash; Your Page Content Here &ndash;&gt;

      &lt;!&ndash; 路由出口 &ndash;&gt;
      &lt;!&ndash; 路由匹配到的组件将渲染在这里 &ndash;&gt;
      <router-view></router-view>

    </section>
    &lt;!&ndash; /.content &ndash;&gt;
  </div>
  &lt;!&ndash; /.content-wrapper &ndash;&gt;
</template>

<script lang="babel">
export default {
  name: 'collection',
  data () {
    return {
      msg: '注册'
    }
  }
}
</script>

<style scoped lang="less">
  /*.content-wrapper{*/
     /*width: 85%;*/
     /*float: right;*/
  /*}*/
</style>-->
<template lang="html">
  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper" style="margin-left: 130px;" id="content">
    <!-- Content Header (Page header) -->
    <!-- <section class="content-header">
      <h1>
        Page Header
        <small>Optional description</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> Level</a></li>
        <li class="active">Here</li>
      </ol>
    </section> -->

    <!-- Main content -->
    <section class="content" style="overflow: hidden;padding: 0">

      <!-- Your Page Content Here -->

      <!-- 路由出口 -->
      <!-- 路由匹配到的组件将渲染在这里 -->
      <el-tabs v-model="editableTabsValue2" type="card" closable @tab-remove="removeTab" @tab-click="handleClick">
        <el-tab-pane
          v-for="(item, index) in editableTabs2"
          :label="item.title"
          :name="item.url"
        >
          <keep-alive>
            <router-view></router-view>
          </keep-alive>

        </el-tab-pane>
      </el-tabs>
      <!--<keep-alive>-->
        <!--<router-view></router-view>-->
      <!--</keep-alive>-->


    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
</template>

<script lang="babel">
  export default {
    name: 'collection',
    props: {
      tabs: {
        type: Array
      }
    },
    data () {
      return {
        msg: '注册',
        editableTabsValue2: '/admin/record/home',
        editableTabs2: [],
        tabIndex: 2,
        sess:[],
        a:[],
        b:''
      }
    },
    created:function () {
      this.a = this._$tabsData;
      //this.editableTabsValue2 = this.$route.path
    },
    watch:{
      a:function (val) {
        this.editableTabsValue2 = this.$route.path
        this.editableTabs2 = val
      }
    },
    methods: {
      handleClick:function (tab,event) {
        console.log(tab,event)
        this.editableTabsValue2 = tab.name
        this.$router.push(tab.name)
      },
      removeTab(targetName) {


        let tabs = this._$tabsData;
        console.log(tabs)
        let activeName = this.editableTabsValue2;
        if (activeName === targetName.name) {
          tabs.forEach((tab, index) => {
            if (tab.url === targetName.name) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              for(let i=0; i<this._$tabsData.length;i++){
                if(this._$tabsData[i].name == targetName.label){
                  this._$tabsData.splice(i,1);
                  i--;
                  break
                }
              }
              if (nextTab) {
                activeName = nextTab.url;
              }
            }
          });
        }
        console.log(tabs)
        this.editableTabsValue2 = activeName;
        this.$router.push(activeName)
//        this._$tabsData = this.editableTabs2 = tabs.filter(tab => tab.name !== targetName);
      }
    },
    attached:function () {
      document.getElementById('content').style.height = (document.documentElement.clientHeight-98)+'px';
    }
  }

</script>

<style scoped lang="less">
  /*.content-wrapper{*/
  /*width: 85%;*/
  /*float: right;*/
  /*}*/
</style>
