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
  <div class="content-wrapper" style="margin-left: 130px">
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
      <el-tabs v-model="$editableTabsValue2" type="card" closable @tab-remove="removeTab">
        <el-tab-pane
          v-for="(item, index) in $editableTabs2"
          :label="item.title"
          :name="item.name"
        >
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
    data () {
      return {
        msg: '注册',
        editableTabsValue2: '2',
        editableTabs2: [{
          title: 'Tab 1',
          name: '1',
          content: 'Tab 1 content'
        }, {
          title: 'Tab 2',
          name: '2',
          content: 'Tab 2 content'
        }],
        tabIndex: 2,
        sess:[],
      }
    },
    created:function () {
      setInterval(function () {
        this.editableTabs2 = JSON.parse(localStorage.getItem('tabsData'));
//        console.log(this.sess)
      },20)

    },
    computed: {
      $editableTabs2:function () {
//        this.editableTabs2.concat(this.sess)
        return this.editableTabs2
      }
    },
    watch:{
      sess:function () {
        console.log(arguments)
      }
    },
    methods: {
      addTab(targetName) {
        let newTabName = ++this.tabIndex + '';
        this.editableTabs2.push({
          title: 'New Tab',
          name: newTabName,
          content: 'New Tab content'
        });
        this.editableTabsValue2 = newTabName;
      },
      removeTab(targetName) {
        let tabs = this.editableTabs2;
        let activeName = this.editableTabsValue2;
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (tab.name === targetName) {
              let nextTab = tabs[index + 1] || tabs[index - 1];
              if (nextTab) {
                activeName = nextTab.name;
              }
            }
          });
        }

        this.editableTabsValue2 = activeName;
        this.editableTabs2 = tabs.filter(tab => tab.name !== targetName);
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
