/**
 * Created by guoling on 2017/3/30.
 */
export default{
  install(Vue){
    //查询权限
    /*
     倒数第二参：按钮的方法
     最后一个参：按钮事件的参数
     参3：查询权限的query
     */
    const tabsData = [{title:'主页',name:'主页',url:'/admin/record/home'}];
    const tabsVal = {name:'主页'};
    Vue.__tabsData = tabsData;
    Vue.__tabsVal = tabsVal;
    Vue.prototype._$tabsData = tabsData;
    Vue.prototype._$tabsVal = tabsVal;
    }


}
