webpackJsonp([3,1],{0:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var s=n(6),r=a(s),i=n(41),o=a(i),c=n(149),u=a(c),l=n(85),d=a(l);n(145),n(146);var f=n(147),v=a(f),p=n(148),m=a(p),_=n(165),h=a(_),g=n(166),C=a(g),y=n(163),b=a(y),x=n(164),E=a(x),k=n(161),$=a(k),M=n(162),S=a(M);r.default.use(d.default),r.default.http.interceptors.push(function(t,e){e(function(t){var e=t.status,n=t.body;return 200==e?n.code==-100||n.code==-200||n.code==-201?(o.default.commit("LOGIN_SHOW"),u.default.push("/admin/login")):1!=n.code?(t.status=500,t.statusMessage=n.msg||"系统异常",t.statusText="Internal Server Error",t.ok=!1):t.data=n.result:t.statusMessage="系统异常",t})}),r.default.use(v.default),r.default.use(m.default),u.default.beforeEach(function(t,e,n){n()}),r.default.component("app-header",h.default),r.default.component("app-menu",C.default),r.default.component("app-content",b.default),r.default.component("app-footer",E.default),r.default.component("app-aside",$.default),r.default.component("app-aside-bg",S.default);new r.default({store:o.default,router:u.default}).$mount("#app")},11:function(t,e,n){var a,s;n(73),a=n(135);var r=n(198);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,s._scopeId="data-v-61e7739a",t.exports=a},41:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(6),r=a(s),i=n(87),o=a(i),c=n(154),u=a(c),l=n(153),d=a(l),f=n(152),v=a(f);r.default.use(o.default),e.default=new o.default.Store({modules:{title:u.default,message:d.default,login:v.default}})},61:function(t,e){},73:function(t,e){},77:function(t,e){},82:function(t,e){},84:function(t,e){},117:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(11),r=a(s);e.default={name:"main",components:{Message:r.default}}},120:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},121:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},122:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},123:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},124:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{}},methods:{logout:function(){this.$http.post("/admin/user/logout").then(function(){this.$router.push("/login")},function(t){console.log(t)})}}}},125:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},134:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(11),r=a(s);e.default={name:"login",components:{Message:r.default},data:function(){return{msg:"登录",login:!1,username:"",password:""}},created:function(){this.$store.commit("LOGIN_SHOW")},methods:{submit:function(){this.$http.post("/admin/user/login",{username:this.$data.username,password:this.$data.password}).then(function(){this.$store.commit("LOGIN_HIDE"),this.$router.push({path:"/admin/record/deal"})},function(t){console.log(t)})}},computed:{$$login:function(){return this.$store.state.login.ctrl}}}},135:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(6);a(s);e.default={name:"message",data:function(){return{delay:!1,accord:!1}},methods:{know:function(){this.$store.commit("MESSAGE_ACCORD_HIDE")}},computed:{$$message:function(){return this.$store.state.message.message},$$delay:function(){return this.$store.state.message.delay},$$accord:function(){return this.$store.state.message.accord},$$text:function(){return this.$store.state.message.text}}}},145:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var s=n(6),r=a(s);r.default.directive("focus",{inserted:function(t){t.focus()}})},146:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var s=n(6),r=a(s);r.default.filter("test",function(t){return 1==t})},147:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={install:function(t){t.directive("keyboard",{inserted:function(t,e){console.log(e);var n=e.arg,a=document.createElement("input");a.type="text",a.setAttribute("readOnly","true"),n&&n.input?a.className=n.input:a.style.border="1px solid #000",a.onclick=function(){console.log("唤起键盘")};var s=function(t){if("delete"==t)a.value=a.value.substr(0,a.value.length-1);else{if(a.value.indexOf(".")!=-1){var e=a.value.indexOf("."),n=a.value.length;if(n-e>2)return!1}a.value+=t}};t.appendChild(a);var r=document.createElement("div");n&&n.keyboard?r.className=n.keyboard:(r.style.width="100%",r.style.height="280px",r.style.position="fixed",r.style.left="0",r.style.bottom="0",r.style.borderTop="1px solid #000");var i="width:33.33%;height:70px;float:left;text-align:center;line-height:70px;",o=document.createElement("div"),c=document.createElement("div");c.innerHTML="1",c.style.cssText=i,c.onclick=function(){s("1")};var u=document.createElement("div");u.innerHTML="2",u.style.cssText=i,u.onclick=function(){s("2")};var l=document.createElement("div");l.innerHTML="3",l.style.cssText=i,l.onclick=function(){s("3")};var d=document.createElement("div");d.innerHTML="4",d.style.cssText=i,d.onclick=function(){s("4")};var f=document.createElement("div");f.innerHTML="5",f.style.cssText=i,f.onclick=function(){s("5")};var v=document.createElement("div");v.innerHTML="6",v.style.cssText=i,v.onclick=function(){s("6")};var p=document.createElement("div");p.innerHTML="7",p.style.cssText=i,p.onclick=function(){s("7")};var m=document.createElement("div");m.innerHTML="8",m.style.cssText=i,m.onclick=function(){s("8")};var _=document.createElement("div");_.innerHTML="9",_.style.cssText=i,_.onclick=function(){s("9")};var h=document.createElement("div");h.innerHTML=".",h.style.cssText=i,h.onclick=function(){s(".")};var g=document.createElement("div");g.innerHTML="0",g.style.cssText=i,g.onclick=function(){s("0")};var C=document.createElement("div");C.innerHTML="x",C.style.cssText=i,C.onclick=function(){s("delete")},r.appendChild(c),r.appendChild(u),r.appendChild(l),r.appendChild(d),r.appendChild(f),r.appendChild(v),r.appendChild(p),r.appendChild(m),r.appendChild(_),r.appendChild(h),r.appendChild(g),r.appendChild(C),r.appendChild(o);var y=document.createElement("div");r.appendChild(y),t.appendChild(r)}})}}},148:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(157),r=a(s),i=n(41),o=a(i);e.default={install:function(t){var e={joint:function(t){for(var e in t)if("object"==(0,r.default)(t[e])){for(var n=0;n<t[e].length;n++)if("name"==e){if(!this[e](t[e][n].data,t[e][n].text))return!1}else if(!this[e](t[e][n]))return!1}else if(!this[e](t[e]))return!1;return!0},name:function(t,e){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,15}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:(e?e:"名称")+"长度限制1-15个字"}),!1)},address:function(t){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,35}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"地址长度限制1-35个字"}),!1)},phone:function(t){return!!/^1(3|4|5|7|8)\d{9}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的手机号码"}),!1)},code:function(t){return 6==t.length||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的验证码"}),!1)},bankNo:function(t){return!!/^(\d{16}|\d{19})$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的银行卡号"}),!1)},idCard:function(t){var e=/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;if(e.test(t)){if(18==t.length){for(var n=new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2),a=new Array(1,0,10,9,8,7,6,5,4,3,2),s=0,r=0;r<17;r++)s+=t.substring(r,r+1)*n[r];var i=s%11,c=t.substring(17);if(2==i){if("X"==c||"x"==c)return!0;o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}else{if(c==a[i])return!0;o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}}}else o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}};t.__validate=e,t.prototype._$validate=e}}},149:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(6),r=a(s),i=n(86),o=a(i),c=n(151),u=a(c);r.default.use(o.default),e.default=new o.default({mode:"history",routes:u.default,scrollBehavior:function(t,e,n){}})},150:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(158),r=a(s),i=function(t){return n.e(0,function(){return t(n(170))})},o=function(t){return n.e(0,function(){return t(n(169))})},c=function(t){return n.e(0,function(){return t(n(184))})},u=function(t){return n.e(0,function(){return t(n(183))})},l=function(t){return n.e(0,function(){return t(n(182))})},d=function(t){return n.e(0,function(){return t(n(160))})},f=function(t){return n.e(0,function(){return t(n(159))})},v=function(t){return n.e(0,function(){return t(n(167))})},p=function(t){return n.e(0,function(){return t(n(168))})},m=function(t){return n.e(0,function(){return t(n(171))})},_=function(t){return n.e(0,function(){return t(n(181))})},h=function(t){return n.e(0,function(){return t(n(177))})},g=function(t){return n.e(0,function(){return t(n(178))})},C=function(t){return n.e(0,function(){return t(n(179))})},y=function(t){return n.e(0,function(){return t(n(180))})},b=function(t){return n.e(0,function(){return t(n(172))})},x=function(t){return n.e(0,function(){return t(n(173))})},E=function(t){return n.e(0,function(){return t(n(174))})},k=function(t){return n.e(0,function(){return t(n(176))})};e.default={path:"/admin/record",redirect:"/admin/record/deal",component:r.default,children:[{path:"deal",name:"DealQuery",component:i},{path:"dealList",name:"DealList",component:o},{path:"withdrawal",name:"WithdrawalQuery",component:c},{path:"storeList",name:"StoreList",component:u},{path:"storeAudit",name:"StoreAudit",component:l},{path:"agentList",name:"AgentList",component:d},{path:"agentAdd",name:"AgentAdd",component:f},{path:"companyProfit",name:"CompanyProfit",component:v},{path:"companyProfitDet",name:"CompanyProfitDet",component:p},{path:"firProfit",name:"FirProfit",component:m},{path:"secProfit",name:"SecProfit",component:_},{path:"passAdd",name:"PassAdd",component:h},{path:"passList",name:"PassList",component:g},{path:"productAdd",name:"ProductAdd",component:C},{path:"productList",name:"ProductList",component:y},{path:"issue",name:"Issue",component:b},{path:"issue1",name:"Issue1",component:x},{path:"issueSuccess",name:"IssueSuccess",component:E},{path:"MoneyList",name:"MoneyList",component:k}]}},151:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=n(150),r=a(s),i=n(175),o=a(i);e.default=[{path:"/admin",redirect:"/admin/record"},{path:"/admin/login",component:o.default},r.default]},152:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={ctrl:!0},a={LOGIN_SHOW:function(t){t.ctrl=!0},LOGIN_HIDE:function(t){t.ctrl=!1}};e.default={state:n,mutations:a}},153:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={message:!1,delay:!1,accord:!1,text:""},a={MESSAGE_DELAY_SHOW:function(t,e){t.message=!0,t.delay=!0,t.text=e.text,setTimeout(function(){t.message=!1,t.delay=!1},3e3)},MESSAGE_DELAY_HIDE:function(t){t.message=!1,t.delay=!1},MESSAGE_ACCORD_SHOW:function(t,e){t.message=!0,t.accord=!0,t.text=e.text},MESSAGE_ACCORD_HIDE:function(t){t.message=!1,t.accord=!1}};e.default={state:n,mutations:a}},154:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={title:"钱包++",router:{ticketReserve:"车票预定",ticketRob:"抢票",ticketRobOrder:"确认订单",ticketRobDetail:"抢票详情",ticketPrivate:"私人定制",ticketOrder:"我的订单",ticketSubmitOrder:"提交订单",ticketSureOrder:"确认订单",ticketPayOrder:"订单详情",ticketTrain:"北京 → 深圳",ticketContacts:"常用联系人",ticketLogin:"12306登录",firstAdd:"确认订单",ticketRefundDetail:"订单详情",secondAdd:"确认订单",ticketAddChild:"常用联系人",ticketRefundSuccess:"出票成功"}},a={TITLE_CHANGE:function(t,e){"ticketTrain"==e.name&&(t.router.ticketTrain=e.formName+" → "+e.toName),t.title=t.router[e.name]}};e.default={state:n,mutations:a}},158:function(t,e,n){var a,s;n(77),a=n(117);var r=n(203);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,t.exports=a},161:function(t,e,n){var a,s;a=n(120);var r=n(199);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,t.exports=a},162:function(t,e,n){var a,s;a=n(121);var r=n(209);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,t.exports=a},163:function(t,e,n){var a,s;n(82),a=n(122);var r=n(208);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,s._scopeId="data-v-a76ae156",t.exports=a},164:function(t,e,n){var a,s;a=n(123);var r=n(194);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,t.exports=a},165:function(t,e,n){var a,s;n(84),a=n(124);var r=n(212);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,s._scopeId="data-v-fff64b98",t.exports=a},166:function(t,e,n){var a,s;a=n(125);var r=n(210);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,t.exports=a},175:function(t,e,n){var a,s;n(61),a=n(134);var r=n(185);s=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(s=a=a.default),"function"==typeof s&&(s=s.options),s.render=r.render,s.staticRenderFns=r.staticRenderFns,s._scopeId="data-v-0e0a7688",t.exports=a},185:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return t.$$login?e("div",{attrs:{id:"login"}},[e("div",{staticClass:"login-space"},[e("div",{staticClass:"login-title"},[t._v("登录")]),t._v(" "),e("div",{staticClass:"flexBox group"},[e("div",{staticClass:"text"},[t._v("用户名")]),t._v(" "),e("input",{directives:[{name:"model",rawName:"v-model",value:t.username,expression:"username"}],staticClass:"ipt",attrs:{type:"text",placeholder:"请输入用户名"},domProps:{value:t._s(t.username)},on:{input:function(e){e.target.composing||(t.username=e.target.value)}}})]),t._v(" "),e("div",{staticClass:"flexBox group"},[e("div",{staticClass:"text"},[t._v("密码")]),t._v(" "),e("input",{directives:[{name:"model",rawName:"v-model",value:t.password,expression:"password"}],staticClass:"ipt",attrs:{type:"password",placeholder:"请输入密码"},domProps:{value:t._s(t.password)},on:{input:function(e){e.target.composing||(t.password=e.target.value)}}})]),t._v(" "),e("div",{staticClass:"submit",on:{click:t.submit}},[t._v("登录")])]),t._v(" "),e("message")]):t._e()},staticRenderFns:[]}},194:function(t,e){t.exports={render:function(){var t=this;t.$createElement,t._c;return t._m(0)},staticRenderFns:[function(){var t=this,e=(t.$createElement,t._c);return e("footer",{staticClass:"main-footer",staticStyle:{"margin-left":"130px"}},[e("div",{staticClass:"pull-right hidden-xs"},[t._v("\n    Anything you want\n  ")]),t._v(" "),t._v(" "),e("strong",[t._v("Copyright © 2016 "),e("a",{attrs:{href:"#"}},[t._v("Company")]),t._v(".")]),t._v(" All rights reserved.\n")])}]}},198:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("transition",{attrs:{name:"fade"}},[t.$$message?e("div",{staticClass:"main"},[e("div",{directives:[{name:"show",rawName:"v-show",value:t.$$delay,expression:"$$delay"}],staticClass:"group"},[e("div",{staticClass:"prompt"},[t._v("提示")]),t._v(" "),e("div",{staticClass:"text"},[t._v(t._s(t.$$text))])]),t._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:t.$$accord,expression:"$$accord"}],staticClass:"group accord"},[e("div",{staticClass:"prompt"},[t._v("提示")]),t._v(" "),e("div",{staticClass:"text"},[t._v(t._s(t.$$text))]),t._v(" "),e("div",{staticClass:"btn",on:{click:t.know}},[t._v("我知道了")])])]):t._e()])},staticRenderFns:[]}},199:function(t,e){t.exports={render:function(){var t=this;t.$createElement,t._c;return t._m(0)},staticRenderFns:[function(){var t=this,e=(t.$createElement,t._c);return e("aside",{staticClass:"control-sidebar control-sidebar-dark"},[e("ul",{staticClass:"nav nav-tabs nav-justified control-sidebar-tabs"},[e("li",{staticClass:"active"},[e("a",{attrs:{href:"#control-sidebar-home-tab","data-toggle":"tab"}},[e("i",{staticClass:"fa fa-home"})])]),t._v(" "),e("li",[e("a",{attrs:{href:"#control-sidebar-settings-tab","data-toggle":"tab"}},[e("i",{staticClass:"fa fa-gears"})])])]),t._v(" "),t._v(" "),e("div",{staticClass:"tab-content"},[e("div",{staticClass:"tab-pane active",attrs:{id:"control-sidebar-home-tab"}},[e("h3",{staticClass:"control-sidebar-heading"},[t._v("Recent Activity")]),t._v(" "),e("ul",{staticClass:"control-sidebar-menu"},[e("li",[e("a",{attrs:{href:"javascript::;"}},[e("i",{staticClass:"menu-icon fa fa-birthday-cake bg-red"}),t._v(" "),e("div",{staticClass:"menu-info"},[e("h4",{staticClass:"control-sidebar-subheading"},[t._v("Langdon's Birthday")]),t._v(" "),e("p",[t._v("Will be 23 on April 24th")])])])])]),t._v(" "),t._v(" "),e("h3",{staticClass:"control-sidebar-heading"},[t._v("Tasks Progress")]),t._v(" "),e("ul",{staticClass:"control-sidebar-menu"},[e("li",[e("a",{attrs:{href:"javascript::;"}},[e("h4",{staticClass:"control-sidebar-subheading"},[t._v("\n              Custom Template Design\n              "),e("span",{staticClass:"pull-right-container"},[e("span",{staticClass:"label label-danger pull-right"},[t._v("70%")])])]),t._v(" "),e("div",{staticClass:"progress progress-xxs"},[e("div",{staticClass:"progress-bar progress-bar-danger",staticStyle:{width:"70%"}})])])])]),t._v(" ")]),t._v(" "),t._v(" "),t._v(" "),e("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-stats-tab"}},[t._v("Stats Tab Content")]),t._v(" "),t._v(" "),t._v(" "),e("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-settings-tab"}},[e("form",{attrs:{method:"post"}},[e("h3",{staticClass:"control-sidebar-heading"},[t._v("General Settings")]),t._v(" "),e("div",{staticClass:"form-group"},[e("label",{staticClass:"control-sidebar-subheading"},[t._v("\n            Report panel usage\n            "),e("input",{staticClass:"pull-right",attrs:{type:"checkbox",checked:""}})]),t._v(" "),e("p",[t._v("\n            Some information about this general settings option\n          ")])]),t._v(" ")])]),t._v(" ")])])}]}},203:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("div",{attrs:{id:"main"}},[e("router-view"),t._v(" "),e("message")])},staticRenderFns:[]}},208:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("div",{staticClass:"content-wrapper",staticStyle:{"margin-left":"130px"}},[e("section",{staticClass:"content",staticStyle:{overflow:"hidden",padding:"0"}},[e("router-view")]),t._v(" ")])},staticRenderFns:[]}},209:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("div",{staticClass:"control-sidebar-bg"})},staticRenderFns:[]}},210:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("aside",{staticClass:"main-sidebar",staticStyle:{width:"130px"}},[e("section",{staticClass:"sidebar"},[e("ul",{staticClass:"sidebar-menu nav nav-pills nav-stacked"},[e("li",{staticClass:"treeview active"},[t._m(0),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",{staticClass:"active"},[e("router-link",{attrs:{to:"/admin/record/deal"}},[e("span",[t._v("交易查询")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/withdrawal"}},[e("span",[t._v("提现查询")])])]),t._v(" ")])]),t._v(" "),e("li",{staticClass:"treeview"},[e("router-link",{attrs:{to:"/admin/record/storeList"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("商户管理")])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(1),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/agentList"}},[e("span",[t._v("代理商列表")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/agentAdd"}},[e("span",[t._v("新增代理商")])])])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(2),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/companyProfit"}},[e("span",[t._v("公司分润")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/firProfit"}},[e("span",[t._v("一级代理商分润")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/secProfit"}},[e("span",[t._v("二级代理商分润")])])])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(3),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/productList"}},[e("span",[t._v("产品列表")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/productAdd"}},[e("span",[t._v("新增产品")])])])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(4),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/passList"}},[e("span",[t._v("通道列表")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/passAdd"}},[e("span",[t._v("新增通道")])])])])]),t._v(" "),e("li",{staticClass:"treeview"},[t._m(5),t._v(" "),e("ul",{staticClass:"treeview-menu"},[e("li",[e("router-link",{attrs:{to:"/admin/record/issue1"}},[e("span",[t._v("按个数分配")])])]),t._v(" "),e("li",[e("router-link",{attrs:{to:"/admin/record/issue"}},[e("span",[t._v("按码段分配")])])])])])]),t._v(" ")]),t._v(" ")])},staticRenderFns:[function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),t._v(" "),e("span",[t._v("交易管理")])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),e("span",[t._v("代理商管理")])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),e("span",[t._v("分润管理")])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),e("span",[t._v("产品管理")])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),e("span",[t._v("通道管理")])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{attrs:{href:"#"}},[e("i",{staticClass:"fa fa-link"}),e("span",[t._v("分配二维码")])])}]}},212:function(t,e){t.exports={render:function(){var t=this,e=(t.$createElement,t._c);return e("header",{staticClass:"main-header"},[t._m(0),t._v(" "),e("nav",{staticClass:"navbar navbar-static-top",staticStyle:{"margin-left":"130px"},attrs:{role:"navigation"}},[t._m(1),t._v(" "),e("div",{staticClass:"navbar-custom-menu"},[e("ul",{staticClass:"nav navbar-nav"},[e("li",{staticClass:"dropdown user user-menu"},[e("div",{staticClass:"loginInfo"},[e("span",{staticClass:"name"},[t._v("超级管理员")]),t._v(" "),e("span",{staticClass:"btn btn-danger",on:{click:t.logout}},[t._v("退出")])])])])])])])},staticRenderFns:[function(){var t=this,e=(t.$createElement,t._c);return e("a",{staticClass:"logo",staticStyle:{width:"130px"},attrs:{href:"javascript:;"}},[e("span",{staticClass:"logo-mini",staticStyle:{"font-size":"20px"}},[t._v("好收银")]),t._v(" "),t._v(" "),e("span",{staticClass:"logo-lg"},[t._v("好收银")])])},function(){var t=this,e=(t.$createElement,t._c);return e("a",{staticClass:"sidebar-toggle",attrs:{href:"#","data-toggle":"offcanvas",role:"button"}},[e("span",{staticClass:"sr-only"},[t._v("Toggle navigation")])])}]}}});
//# sourceMappingURL=app.977bff06071582a82854.js.map