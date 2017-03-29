<template lang="html">
  <div class="content-wrapper">
      <!-- Main content -->
      <section class="content">
        <div class="box" style="margin-top:15px;overflow: hidden">
          <div class="box-header">
            <h3 class="box-title">新增角色</h3>
            <h3 class="box-title" v-else>角色详情</h3>
          </div>
          <div class="box-body">
            <ul>
              <li class="same">
                <label class="title">角色名称:</label>
                <el-input v-model="roleName" style="width: 220px" placeholder="请输入内容" size="small"></el-input>
              </li>
              <li class="same">
                <label class="title">权限:</label>
                <table style="width:90%;display: inline-table;vertical-align: top" border="0" cellspacing="0" cellpadding="0" class="table table-bordered">
                  <thead>
                  <tr>
                    <th width="35%">一级菜单名称</th>
                    <th width="35%">子菜单名称</th>
                    <th >操作</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="item in $tableData">
                    <td><el-checkbox style="margin: 0 5px" v-model="item.isSelected"></el-checkbox>{{item.menuName}}</td>
                    <td style="padding: 0">
                      <table style="width: 100%;height: 100%" border="0" cellspacing="0" cellpadding="0" class="table table-bordered">
                        <tbody>
                        <tr v-for="itemChild in item.children">
                          <td><el-checkbox style="margin: 0 5px" v-model="itemChild.isSelected"></el-checkbox>{{itemChild.menuName}}</td>
                        </tr>
                        </tbody>
                      </table>
                    </td>
                    <td>
                      <table style="width: 100%;height: 100%" border="0" cellspacing="0" cellpadding="0" class="table table-bordered">
                        <tbody>
                        <tr width="100%" v-for="itemChild in item.children">
                          <td>
                          <span v-for="opt in itemChild.opts" style="margin-right: 10px"><el-checkbox
                            style="margin: 0 5px" v-model="opt.isSelected"></el-checkbox>{{opt.optName}}</span>
                          </td>
                        </tr>
                        </tbody>
                      </table>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </li>
              <li class="same">
                <label class="title"></label>
                <el-button type="primary" @click="submit" v-if="isAdd">确 定</el-button>
                <el-button type="primary" @click="submit" v-else>修改</el-button>
              </li>
            </ul>
          </div>
        </div>
      </section>
    </div>
</template>
<script lang="babel">
  import store from '../store'
  export default {
    data(){
      return {
        roleName:'',
        tableData: [],
        isAdd:true
      }
    },
    created: function () {
      let id=0;
      if(this.$route.query.id!=undefined){
        id = this.$route.query.id;
        this.isAdd = false;
      }
      this.$http.post('/daili/user/getRoleDetail',{id:id})
        .then(res => {
        this.tableData = res.data.list;
      this.roleName = res.data.roleName;
      /* this.tableData = JSON.parse(JSON.stringify(res.data.list));*/
      for (var i = 0; i < this.tableData.length; i++) {
        for (var j = 0; j < this.tableData[i].children.length; j++) {
          for (var k = 0; k < this.tableData[i].children[j].opts.length; k++) {
            this.tableData[i].children[j].opts[k].isSelected = Boolean(this.tableData[i].children[j].opts[k].isSelected);
          }
          this.tableData[i].children[j].isSelected = Boolean(this.tableData[i].children[j].isSelected);
        }
        this.tableData[i].isSelected = Boolean(this.tableData[i].isSelected);
      }
    })
      .catch(err => {
        this.$message({
        showClose: true,
        message: err.statusMessage,
        type: 'error'
      });
    })
    },
    methods: {
      submit:function () {
        var list = JSON.parse(JSON.stringify(this.tableData));
        for (var i = 0; i < list.length; i++) {
          for (var j = 0; j < list[i].children.length; j++) {
            for (var k = 0; k < list[i].children[j].opts.length; k++) {
              list[i].children[j].opts[k].isSelected = Number(list[i].children[j].opts[k].isSelected);
            }
            list[i].children[j].isSelected = Number(list[i].children[j].isSelected);
          }
          list[i].isSelected = Number(list[i].isSelected);
        }
        let id=0;
        if(this.$route.query.id!=undefined){
          id = this.$route.query.id;
          this.isAdd = false;
        }
        this.$http.post('/daili/user/saveRole',{
          roleId:id,
          roleName:this.roleName,
          list:list
        }).then(res => {
          this.$message({
          showClose: true,
          message: '添加成功',
          type: 'success'
        });
        this.$router.push('/daili/record/role')
      }).catch(err =>{
          this.$message({
          showClose: true,
          message: err.statusMessage,
          type: 'error'
        });
      })
      }
    },
    computed: {
      $tableData: function () {
        let flag2 = false;
        for (let i = 0; i < this.tableData.length; i++) {
          let flag1 = false;
          for (let j = 0; j < this.tableData[i].children.length; j++) {
            let flag = false;
            for (let k = 0; k < this.tableData[i].children[j].opts.length; k++) {
              if (this.tableData[i].children[j].opts[k].isSelected) {
                flag = true;
                continue;
              }
            }
            if (flag) {
              this.tableData[i].children[j].isSelected = true
            }
            if (this.tableData[i].children[j].isSelected == true) {
              flag1 = true;
              continue;
            }
          }
          if (flag1) {
            this.tableData[i].isSelected = true;
            continue;
          }
        }
        return this.tableData;
      }
    }
  }
</script>
<style scoped lang="less" rel="stylesheet/less">
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    margin: 0 15px 15px 0;
  .title{
    width: 80px;
    margin-right: 15px;
    text-align: right;
  }
  }
  .btn{
    font-size: 12px;
  }
  table td{
    padding: 0;
    margin:0;
    height: 100%;
  }
  .table>tbody>tr>td, .table>tbody>tr>th, .table>tfoot>tr>td, .table>tfoot>tr>th, .table>thead>tr>td, .table>thead>tr>th{
    padding: 0;
    height: 35px;
    line-height: 35px;
  }
  table{
    margin:0;
    padding: 0;
  }

  table th {
    background: #eef1f6;
  }
</style>
