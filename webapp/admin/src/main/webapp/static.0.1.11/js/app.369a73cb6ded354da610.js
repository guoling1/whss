webpackJsonp([3,1],{0:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var s=n(10),i=a(s),r=n(31),o=a(r),c=n(73),u=a(c),l=n(205),d=a(l);n(69),n(70);var f=n(71),v=a(f),p=n(72),m=a(p),_=n(159),h=a(_),C=n(160),g=a(C),b=n(157),y=a(b),k=n(158),x=a(k),E=n(155),$=a(E),M=n(156),O=a(M);i.default.use(d.default),i.default.http.interceptors.push(function(t,e){e(function(t){var e=t.status,n=t.body;return 200==e?n.code==-100||n.code==-200||n.code==-201?(o.default.commit("LOGIN_SHOW"),u.default.push("/login")):1!=n.code?(t.status=500,t.statusMessage=n.msg||"系统异常",t.statusText="Internal Server Error",t.ok=!1):t.data=n.result:t.statusMessage="系统异常",t})}),i.default.use(v.default),i.default.use(m.default),u.default.beforeEach(function(t,e,n){n()}),i.default.component("app-header",h.default),i.default.component("app-menu",g.default),i.default.component("app-content",y.default),i.default.component("app-footer",x.default),i.default.component("app-aside",$.default),i.default.component("app-aside-bg",O.default);new i.default({store:o.default,router:u.default}).$mount("#app")},17:function(t,e,n){var a,s;n(129),a=n(59);var i=n(178);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,s._scopeId="data-v-05ec88fc",t.exports=a},31:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(10),i=a(s),r=n(207),o=a(r),c=n(78),u=a(c),l=n(77),d=a(l),f=n(76),v=a(f);i.default.use(o.default),e.default=new o.default.Store({modules:{title:u.default,message:d.default,login:v.default}})},42:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(17),i=a(s);e.default={name:"main",components:{Message:i.default}}},45:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},46:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},47:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},48:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},49:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{}},methods:{logout:function(){this.$http.post("/admin/user/logout").then(function(){this.$router.push("/admin/login")},function(t){console.log(t)})}}}},50:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},58:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(17),i=a(s);e.default={name:"login",components:{Message:i.default},data:function(){return{msg:"登录",login:!1,username:"",password:""}},created:function(){this.$store.commit("LOGIN_SHOW")},methods:{loginQ:function(){this.$http.post("/admin/user/login",{username:this.$data.username,password:this.$data.password}).then(function(){this.$store.commit("LOGIN_HIDE"),this.$router.push({path:"/admin/record/deal"})},function(t){console.log(t)})}},computed:{$$login:function(){return this.$store.state.login.ctrl}}}},59:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(10);a(s);e.default={name:"message",data:function(){return{delay:!1,accord:!1}},methods:{know:function(){this.$store.commit("MESSAGE_ACCORD_HIDE")}},computed:{$$message:function(){return this.$store.state.message.message},$$delay:function(){return this.$store.state.message.delay},$$accord:function(){return this.$store.state.message.accord},$$text:function(){return this.$store.state.message.text}}}},69:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var s=n(10),i=a(s);i.default.directive("focus",{inserted:function(t){t.focus()}})},70:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var s=n(10),i=a(s);i.default.filter("test",function(t){return 1==t})},71:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={install:function(t){t.directive("keyboard",{inserted:function(t,e){console.log(e);var n=e.arg,a=document.createElement("input");a.type="text",a.setAttribute("readOnly","true"),n&&n.input?a.className=n.input:a.style.border="1px solid #000",a.onclick=function(){console.log("唤起键盘")};var s=function(t){if("delete"==t)a.value=a.value.substr(0,a.value.length-1);else{if(a.value.indexOf(".")!=-1){var e=a.value.indexOf("."),n=a.value.length;if(n-e>2)return!1}a.value+=t}};t.appendChild(a);var i=document.createElement("div");n&&n.keyboard?i.className=n.keyboard:(i.style.width="100%",i.style.height="280px",i.style.position="fixed",i.style.left="0",i.style.bottom="0",i.style.borderTop="1px solid #000");var r="width:33.33%;height:70px;float:left;text-align:center;line-height:70px;",o=document.createElement("div"),c=document.createElement("div");c.innerHTML="1",c.style.cssText=r,c.onclick=function(){s("1")};var u=document.createElement("div");u.innerHTML="2",u.style.cssText=r,u.onclick=function(){s("2")};var l=document.createElement("div");l.innerHTML="3",l.style.cssText=r,l.onclick=function(){s("3")};var d=document.createElement("div");d.innerHTML="4",d.style.cssText=r,d.onclick=function(){s("4")};var f=document.createElement("div");f.innerHTML="5",f.style.cssText=r,f.onclick=function(){s("5")};var v=document.createElement("div");v.innerHTML="6",v.style.cssText=r,v.onclick=function(){s("6")};var p=document.createElement("div");p.innerHTML="7",p.style.cssText=r,p.onclick=function(){s("7")};var m=document.createElement("div");m.innerHTML="8",m.style.cssText=r,m.onclick=function(){s("8")};var _=document.createElement("div");_.innerHTML="9",_.style.cssText=r,_.onclick=function(){s("9")};var h=document.createElement("div");h.innerHTML=".",h.style.cssText=r,h.onclick=function(){s(".")};var C=document.createElement("div");C.innerHTML="0",C.style.cssText=r,C.onclick=function(){s("0")};var g=document.createElement("div");g.innerHTML="x",g.style.cssText=r,g.onclick=function(){s("delete")},i.appendChild(c),i.appendChild(u),i.appendChild(l),i.appendChild(d),i.appendChild(f),i.appendChild(v),i.appendChild(p),i.appendChild(m),i.appendChild(_),i.appendChild(h),i.appendChild(C),i.appendChild(g),i.appendChild(o);var b=document.createElement("div");i.appendChild(b),t.appendChild(i)}})}}},72:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(81),i=a(s),r=n(31),o=a(r);e.default={install:function(t){var e={joint:function(t){for(var e in t)if("object"==(0,i.default)(t[e])){for(var n=0;n<t[e].length;n++)if("name"==e){if(!this[e](t[e][n].data,t[e][n].text))return!1}else if(!this[e](t[e][n]))return!1}else if(!this[e](t[e]))return!1;return!0},name:function(t,e){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,15}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:(e?e:"名称")+"长度限制1-15个字"}),!1)},address:function(t){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,35}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"地址长度限制1-35个字"}),!1)},phone:function(t){return!!/^1(3|4|5|7|8)\d{9}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的手机号码"}),!1)},code:function(t){return 6==t.length||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的验证码"}),!1)},bankNo:function(t){return!!/^(\d{16}|\d{19})$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的银行卡号"}),!1)},idCard:function(t){var e=/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;if(e.test(t)){if(18==t.length){for(var n=new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2),a=new Array(1,0,10,9,8,7,6,5,4,3,2),s=0,i=0;i<17;i++)s+=t.substring(i,i+1)*n[i];var r=s%11,c=t.substring(17);if(2==r){if("X"==c||"x"==c)return!0;o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}else{if(c==a[r])return!0;o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}}}else o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}};t.__validate=e,t.prototype._$validate=e}}},73:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(10),i=a(s),r=n(206),o=a(r),c=n(75),u=a(c);i.default.use(o.default),e.default=new o.default({mode:"history",routes:u.default,scrollBehavior:function(t,e,n){}})},74:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(152),i=a(s),r=function(t){return n.e(0,function(){return t(n(163))})},o=function(t){return n.e(0,function(){return t(n(177))})},c=function(t){return n.e(0,function(){return t(n(176))})},u=function(t){return n.e(0,function(){return t(n(175))})},l=function(t){return n.e(0,function(){return t(n(154))})},d=function(t){return n.e(0,function(){return t(n(153))})},f=function(t){return n.e(0,function(){return t(n(161))})},v=function(t){return n.e(0,function(){return t(n(162))})},p=function(t){return n.e(0,function(){return t(n(164))})},m=function(t){return n.e(0,function(){return t(n(174))})},_=function(t){return n.e(0,function(){return t(n(170))})},h=function(t){return n.e(0,function(){return t(n(171))})},C=function(t){return n.e(0,function(){return t(n(172))})},g=function(t){return n.e(0,function(){return t(n(173))})},b=function(t){return n.e(0,function(){return t(n(165))})},y=function(t){return n.e(0,function(){return t(n(166))})},k=function(t){return n.e(0,function(){return t(n(167))})},x=function(t){return n.e(0,function(){return t(n(169))})};e.default={path:"/admin/record",redirect:"/admin/record/deal",component:i.default,children:[{path:"deal",name:"DealQuery",component:r},{path:"withdrawal",name:"WithdrawalQuery",component:o},{path:"storeList",name:"StoreList",component:c},{path:"storeAudit",name:"StoreAudit",component:u},{path:"agentList",name:"AgentList",component:l},{path:"agentAdd",name:"AgentAdd",component:d},{path:"companyProfit",name:"CompanyProfit",component:f},{path:"companyProfitDet",name:"CompanyProfitDet",component:v},{path:"firProfit",name:"FirProfit",component:p},{path:"secProfit",name:"SecProfit",component:m},{path:"passAdd",name:"PassAdd",component:_},{path:"passList",name:"PassList",component:h},{path:"productAdd",name:"ProductAdd",component:C},{path:"productList",name:"ProductList",component:g},{path:"issue",name:"Issue",component:b},{path:"issue1",name:"Issue1",component:y},{path:"issueSuccess",name:"IssueSuccess",component:k},{path:"MoneyList",name:"MoneyList",component:x}]}},75:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(74),i=a(s),r=n(168),o=a(r);e.default=[{path:"/admin",redirect:"/admin/record"},{path:"/admin/login",component:o.default},i.default]},76:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={ctrl:!0},a={LOGIN_SHOW:function(t){t.ctrl=!0},LOGIN_HIDE:function(t){t.ctrl=!1}};e.default={state:n,mutations:a}},77:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={message:!1,delay:!1,accord:!1,text:""},a={MESSAGE_DELAY_SHOW:function(t,e){t.message=!0,t.delay=!0,t.text=e.text,setTimeout(function(){t.message=!1,t.delay=!1},3e3)},MESSAGE_DELAY_HIDE:function(t){t.message=!1,t.delay=!1},MESSAGE_ACCORD_SHOW:function(t,e){t.message=!0,t.accord=!0,t.text=e.text},MESSAGE_ACCORD_HIDE:function(t){t.message=!1,t.accord=!1}};e.default={state:n,mutations:a}},78:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={title:"钱包++",router:{ticketReserve:"车票预定",ticketRob:"抢票",ticketRobOrder:"确认订单",ticketRobDetail:"抢票详情",ticketPrivate:"私人定制",ticketOrder:"我的订单",ticketSubmitOrder:"提交订单",ticketSureOrder:"确认订单",ticketPayOrder:"订单详情",ticketTrain:"北京 → 深圳",ticketContacts:"常用联系人",ticketLogin:"12306登录",firstAdd:"确认订单",ticketRefundDetail:"订单详情",secondAdd:"确认订单",ticketAddChild:"常用联系人",ticketRefundSuccess:"出票成功"}},a={TITLE_CHANGE:function(t,e){"ticketTrain"==e.name&&(t.router.ticketTrain=e.formName+" → "+e.toName),t.title=t.router[e.name]}};e.default={state:n,mutations:a}},129:function(t,e){},133:function(t,e){},135:function(t,e){},141:function(t,e){},143:function(t,e){},152:function(t,e,n){var a,s;n(143),a=n(42);var i=n(195);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,t.exports=a},155:function(t,e,n){var a,s;a=n(45);var i=n(196);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,t.exports=a},156:function(t,e,n){var a,s;a=n(46);var i=n(191);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,t.exports=a},157:function(t,e,n){var a,s;n(135),a=n(47);var i=n(185);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,s._scopeId="data-v-5044329a",t.exports=a},158:function(t,e,n){var a,s;a=n(48);var i=n(194);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,t.exports=a},159:function(t,e,n){var a,s;n(133),a=n(49);var i=n(182);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,s._scopeId="data-v-2409ef96",t.exports=a},160:function(t,e,n){var a,s;a=n(50);var i=n(184);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,t.exports=a},168:function(t,e,n){var a,s;n(141),a=n(58);var i=n(192);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=i.render,s.staticRenderFns=i.staticRenderFns,s._scopeId="data-v-66ac6ac4",t.exports=a},178:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("transition",{attrs:{name:"fade"}},[t.$$message?e("div",{staticClass:"main"},[e("div",{directives:[{name:"show",rawName:"v-show",value:t.$$delay,expression:"$$delay"}],staticClass:"group"},[e("div",{staticClass:"prompt"},[t._v("提示")]),t._v(" "),e("div",{staticClass:"text"},[t._v(t._s(t.$$text))])]),t._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:t.$$accord,expression:"$$accord"}],staticClass:"group accord"},[e("div",{staticClass:"prompt"},[t._v("提示")]),t._v(" "),e("div",{staticClass:"text"},[t._v(t._s(t.$$text))]),t._v(" "),e("div",{staticClass:"btn",on:{click:t.know}},[t._v("我知道了")])])]):t._e()])},staticRenderFns:[]}},182:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("header",{staticClass:"main-header"},[t._m(0),t._v(" "),e("nav",{staticClass:"navbar navbar-static-top",attrs:{role:"navigation"}},[t._m(1),t._v(" "),e("div",{staticClass:"navbar-custom-menu"},[e("ul",{staticClass:"nav navbar-nav"},[e("li",{staticClass:"dropdown user user-menu"},[e("div",{staticClass:"loginInfo"},[e("span",{staticClass:"name"},[t._v("超级管理员")]),t._v(" "),e("span",{staticClass:"btn btn-danger",on:{click:t.logout}},[t._v("退出")])])])])])])])},staticRenderFns:[function(){var t=this,e=(t.$createElement,t._c);return e("a",{staticClass:"logo",attrs:{href:"index2.html"}},[e("span",{staticClass:"logo-mini"},[t._v("金")]),t._v(" "),t._v(" "),e("span",{staticClass:"logo-lg"},[t._v("好收银")])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{staticClass:"sidebar-toggle",attrs:{href:"#","data-toggle":"offcanvas",role:"button"}},[e("span",{staticClass:"sr-only"},[t._v("Toggle navigation")])])}]}},184:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("aside",{staticClass:"main-sidebar"},[e("section",{staticClass:"sidebar"},[e("ul",{staticClass:"sidebar-menu nav nav-pills nav-stacked"},[e("li",{staticClass:"treeview active"},[t._m(0),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",{staticClass:"active"},[e("router-link",{attrs:{to:"/admin/record/deal"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("交易查询")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/withdrawal"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("提现查询")])])]),t._v(" ")])]),t._v(" "),e("li",{staticClass:"treeview"},[e("router-link",{attrs:{to:"/admin/record/storeList"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("商户管理")]),t._v(" "),e("span",{staticClass:"pull-right-container"},[e("i",{staticClass:"fa fa-angle-left pull-right"})])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(1),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/agentList"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("代理商列表")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/agentAdd"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("新增代理商")])])])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(2),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/companyProfit"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("公司分润")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/firProfit"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("一级代理商分润")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/secProfit"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("二级代理商分润")])])])])]),t._v(" "),t._v(" "),e("li",{staticClass:"treeview"},[t._m(3),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/productList"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("产品列表")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/productAdd"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("新增产品")])])])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(4),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/passList"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("通道列表")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/passAdd"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("新增通道")])])])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(5),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/issue1"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("按个数分配")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/issue"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("按码段分配")])])])])])]),t._v(" ")]),t._v(" ")])},staticRenderFns:[function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("交易管理")]),t._v(" "),e("span",{staticClass:"pull-right-container"},[e("i",{staticClass:"fa fa-angle-left pull-right"})])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("代理商管理")]),t._v(" "),e("span",{staticClass:"pull-right-container"},[e("i",{staticClass:"fa fa-angle-left pull-right"})])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("分润管理")]),t._v(" "),e("span",{staticClass:"pull-right-container"},[e("i",{staticClass:"fa fa-angle-left pull-right"})])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("产品管理")]),t._v(" "),e("span",{staticClass:"pull-right-container"},[e("i",{staticClass:"fa fa-angle-left pull-right"})])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("通道管理")]),t._v(" "),e("span",{staticClass:"pull-right-container"},[e("i",{staticClass:"fa fa-angle-left pull-right"})])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("分配二维码")]),t._v(" "),e("span",{staticClass:"pull-right-container"},[e("i",{staticClass:"fa fa-angle-left pull-right"})])])}]}},185:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("div",{staticClass:"content-wrapper"},[e("section",{staticClass:"content"},[e("router-view")]),t._v(" ")])},staticRenderFns:[]}},191:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("div",{staticClass:"control-sidebar-bg"})},staticRenderFns:[]}},192:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return t.$$login?e("div",{attrs:{id:"login"}},[e("div",{staticClass:"login-space"},[e("div",{staticClass:"login-title"},[t._v("登录")]),t._v(" "),e("div",{staticClass:"flexBox group"},[e("div",{staticClass:"text"},[t._v("用户名")]),t._v(" "),e("input",{directives:[{name:"model",rawName:"v-model",value:t.username,expression:"username"}],staticClass:"ipt",attrs:{type:"text",placeholder:"请输入用户名"},domProps:{value:t._s(t.username)},on:{input:function(e){e.target.composing||(t.username=e.target.value)}}})]),t._v(" "),e("div",{staticClass:"flexBox group"},[e("div",{staticClass:"text"},[t._v("密码")]),t._v(" "),e("input",{directives:[{name:"model",rawName:"v-model",value:t.password,expression:"password"}],staticClass:"ipt",attrs:{type:"password",placeholder:"请输入密码"},domProps:{value:t._s(t.password)},on:{input:function(e){e.target.composing||(t.password=e.target.value)}}})]),t._v(" "),e("div",{staticClass:"submit",on:{click:t.loginQ}},[t._v("登录")])]),t._v(" "),e("message")]):t._e()},staticRenderFns:[]}},194:function(t,e){t.exports={render:function(){var t=this;t.$createElement,t._c;return t._m(0)},staticRenderFns:[function(){var t=this,e=(t.$createElement,t._c);return e("footer",{staticClass:"main-footer"},[e("div",{staticClass:"pull-right hidden-xs"},[t._v("\n    Anything you want\n  ")]),t._v(" "),t._v(" "),e("strong",[t._v("Copyright © 2016 "),e("a",{attrs:{href:"#"}},[t._v("Company")]),t._v(".")]),t._v(" All rights reserved.\n")])}]}},195:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("div",{attrs:{id:"main"}},[e("router-view"),t._v(" "),e("message")])},staticRenderFns:[]}},196:function(t,e){t.exports={render:function(){var t=this;t.$createElement,t._c;return t._m(0)},staticRenderFns:[function(){var t=this,e=(t.$createElement,t._c);return e("aside",{staticClass:"control-sidebar control-sidebar-dark"},[e("ul",{staticClass:"nav nav-tabs nav-justified control-sidebar-tabs"},[e("li",{staticClass:"active"},[e("a",{attrs:{href:"#control-sidebar-home-tab","data-toggle":"tab"}},[e("i",{staticClass:"fa fa-home"})])]),t._v(" "),e("li",[e("a",{attrs:{href:"#control-sidebar-settings-tab","data-toggle":"tab"}},[e("i",{staticClass:"fa fa-gears"})])])]),t._v(" "),t._v(" "),e("div",{staticClass:"tab-content"},[e("div",{staticClass:"tab-pane active",attrs:{id:"control-sidebar-home-tab"}},[e("h3",{staticClass:"control-sidebar-heading"},[t._v("Recent Activity")]),t._v(" "),e("ul",{staticClass:"control-sidebar-menu"},[e("li",[e("a",{attrs:{href:"javascript::;"}},[e("i",{staticClass:"menu-icon fa fa-birthday-cake bg-red"}),t._v(" "),e("div",{staticClass:"menu-info"},[e("h4",{staticClass:"control-sidebar-subheading"},[t._v("Langdon's Birthday")]),t._v(" "),e("p",[t._v("Will be 23 on April 24th")])])])])]),t._v(" "),t._v(" "),e("h3",{staticClass:"control-sidebar-heading"},[t._v("Tasks Progress")]),t._v(" "),e("ul",{staticClass:"control-sidebar-menu"},[e("li",[e("a",{attrs:{href:"javascript::;"}},[e("h4",{staticClass:"control-sidebar-subheading"},[t._v("\n              Custom Template Design\n              "),e("span",{staticClass:"pull-right-container"},[e("span",{staticClass:"label label-danger pull-right"},[t._v("70%")])])]),t._v(" "),e("div",{staticClass:"progress progress-xxs"},[e("div",{staticClass:"progress-bar progress-bar-danger",staticStyle:{width:"70%"}})])])])]),t._v(" ")]),t._v(" "),t._v(" "),t._v(" "),e("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-stats-tab"}},[t._v("Stats Tab Content")]),t._v(" "),t._v(" "),t._v(" "),e("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-settings-tab"}},[e("form",{attrs:{method:"post"}},[e("h3",{staticClass:"control-sidebar-heading"},[t._v("General Settings")]),t._v(" "),e("div",{staticClass:"form-group"},[e("label",{staticClass:"control-sidebar-subheading"},[t._v("\n            Report panel usage\n            "),e("input",{staticClass:"pull-right",attrs:{type:"checkbox",checked:""}})]),t._v(" "),e("p",[t._v("\n            Some information about this general settings option\n          ")])]),t._v(" ")])]),t._v(" ")])])}]}}});
//# sourceMappingURL=app.369a73cb6ded354da610.js.map