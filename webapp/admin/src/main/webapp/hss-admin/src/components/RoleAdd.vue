<template>
  <div id="roleAdd">
    <div class="col-md-12">
      <div class="box" style="margin-top:15px;overflow: hidden">
        <div class="box-header">
          <h3 class="box-title">角色管理</h3>
        </div>
        <div class="box-body">
          <!--筛选-->
          <ul>
            <li class="same">
              <label>角色名称:</label>
              <el-input style="width: 220px" placeholder="请输入内容" size="small"></el-input>
            </li>
            <li class="same">
              <label>角色名称:</label>
              <table style="width:90%;display: inline-table;vertical-align: top" border="0" cellspacing="0" cellpadding="0" class="table table-bordered">
                <thead>
                <tr>
                  <th width="35%">一级菜单名称</th>
                  <th width="35%">子菜单名称</th>
                  <th >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="item in tableData">
                  <td><el-checkbox style="margin: 0 5px" v-model="item.isTrue"></el-checkbox>{{item.name}}</td>
                  <td style="padding: 0">
                    <table style="width: 100%;height: 100%" border="0" cellspacing="0" cellpadding="0" class="table table-bordered">
                      <tbody>
                      <tr v-for="itemSon in item.son">
                        <td>{{itemSon.name}}</td>
                      </tr>
                      </tbody>
                    </table>
                  </td>
                  <td>
                    <table style="width: 100%;height: 100%" border="0" cellspacing="0" cellpadding="0" class="table table-bordered">
                      <tbody>
                      <tr width="100%" v-for="itemSon in item.son">
                        <td>
                          <span v-for="itemChild in itemSon.children">{{itemChild.name}}</span>
                        </td>
                      </tr>
                      </tbody>

                    </table>
                  </td>
                </tr>
                </tbody>

              </table>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="babel">
  export default{
    name: 'roleAdd',
    data(){
      return {
        tableData: [
          {

            name: '员工权限管理',
            isTrue: true,
            son: [{
              name: '员工管理',
              isTrue: false,
              children: [{
                name: '禁用',
                isTrue: false,
              }, {
                name: '编辑',
                isTrue: false,
              }]
            }, {
              name: '新增员工',
              isTrue: false,
              children: [{
                name: '禁用',
                isTrue: false,
              }, {
                name: '编辑',
                isTrue: false,
              }]
            }]

          },
          {
            name: '交易查询',
            isTrue: false,
            son: [{
              name: '员工管理',
              isTrue: false,
              children: [{
                name: '禁用',
                isTrue: false,
              }, {
                name: '编辑',
                isTrue: false,
              }]
            }, {
              name: '新增员工',
              isTrue: false,
              children: [{
                name: '禁用',
                isTrue: false,
              }, {
                name: '编辑',
                isTrue: false,
              }]
            }]
          }
        ]
      }
    },
    created: function () {

    },
    methods: {},
    watch: {},
  }
</script>
<style scoped lang="less">
  ul{
    padding: 0;
  }
  .same{
    list-style: none;
    margin: 0 15px 15px 0;
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

  /*table {
    display: inline-block;
    vertical-align: top;
    border-right: 1px solid #dfe6ec;
    border-bottom: 1px solid #dfe6ec
  }

  table th, table td {
    border-left: 1px solid #dfe6ec;
    border-top: 1px solid #dfe6ec
  }*/
</style>
