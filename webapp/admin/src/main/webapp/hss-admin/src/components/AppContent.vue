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
    <section class="content" style="overflow: hidden;padding: 0;min-height: 850px">

      <!-- Your Page Content Here -->

      <!-- 路由出口 -->
      <!-- 路由匹配到的组件将渲染在这里 -->
      <el-tabs v-model="editableTabsValue2" type="card" @tab-click="handleClick">
        <el-tab-pane
          v-for="(item, index) in $editableTabs2"
          :name="item.url"
        >
          <span slot="label">{{item.title}}<i class="el-icon-circle-cross" @click.stop="removeTab(item.url)" style="margin-left: 8px;color: #a09e9e"></i></span>
          <keep-alive>
            <router-view></router-view>
          </keep-alive>

        </el-tab-pane>
      </el-tabs>
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
        tabsData:[],
        b:''
      }
    },
    created:function () {
      this.tabsData = this._$tabsData;
      this.editableTabsValue2 = this.$route.path
    },
    watch:{
      tabsData:function (val) {
        this.tabsData = val;
      },
      $route:function (val) {
        this.editableTabsValue2 = this.$route.path;
      }
    },
    methods: {
      handleClick:function (tab,event) {
        this.editableTabsValue2 = tab.name
        this.$router.push(tab.name)
      },
      removeTab(targetName) {
        let tabs = this.tabsData;
        let activeName = this.editableTabsValue2;
        if (activeName == targetName) {
          tabs.forEach((tab, index) => {
            if (tab.url == targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.url;
              }
            }
          });
        }
        for(var i=0;i<this._$tabsData.length;i++){
            if(this._$tabsData[i].url==targetName){
              this._$tabsData.splice(i,1);
              i--;
              break;
            }
        }
        this.$router.push(activeName);
      }
    },
    attached:function () {
      document.getElementById('content').style.height = (document.documentElement.clientHeight-98)+'px';
    },
    computed: {
        $editableTabs2:function () {
          return this.tabsData
        }
    }
  }

</script>

<style scoped lang="less">
  /*.content-wrapper{*/
  /*width: 85%;*/
  /*float: right;*/
  /*}*/
</style>
