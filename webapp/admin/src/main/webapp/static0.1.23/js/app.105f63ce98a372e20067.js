webpackJsonp([3,1],{0:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var r=n(11),s=a(r),i=n(33),o=a(i),c=n(86),u=a(c),l=n(264),d=a(l);n(82),n(83);var f=n(84),p=a(f),v=n(85),m=a(v),_=n(199),h=a(_),g=n(200),C=a(g),y=n(197),b=a(y),x=n(198),E=a(x),k=n(195),S=a(k),$=n(196),A=a($);s.default.use(d.default),s.default.http.interceptors.push(function(t,e){e(function(t){var e=t.status,n=t.body;return 200==e?n.code==-100||n.code==-200||n.code==-201?(o.default.commit("LOGIN_SHOW"),u.default.push("/admin/login")):1!=n.code?(t.status=500,t.statusMessage=n.msg||"系统异常",t.statusText="Internal Server Error",t.ok=!1):t.data=n.result:t.statusMessage="系统异常",t})}),s.default.use(p.default),s.default.use(m.default),u.default.beforeEach(function(t,e,n){n()}),s.default.component("app-header",h.default),s.default.component("app-menu",C.default),s.default.component("app-content",b.default),s.default.component("app-footer",E.default),s.default.component("app-aside",S.default),s.default.component("app-aside-bg",A.default);new s.default({store:o.default,router:u.default}).$mount("#app")},8:function(t,e,n){var a,r;n(176),a=n(66);var s=n(246);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,r._scopeId="data-v-61e7739a",t.exports=a},33:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var r=n(11),s=a(r),i=n(266),o=a(i),c=n(91),u=a(c),l=n(90),d=a(l),f=n(89),p=a(f);s.default.use(o.default),e.default=new o.default.Store({modules:{title:u.default,message:d.default,login:p.default}})},44:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var r=n(8),s=a(r);e.default={name:"main",components:{Message:s.default}}},48:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},49:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},50:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},51:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},52:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{}},methods:{logout:function(){this.$http.post("/admin/user/logout").then(function(){this.$router.push("/login")},function(t){console.log(t)})}}}},53:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},65:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var r=n(8),s=a(r);e.default={name:"login",components:{Message:s.default},data:function(){return{msg:"登录",login:!1,username:"",password:""}},created:function(){this.$store.commit("LOGIN_SHOW")},methods:{submit:function(){this.$http.post("/admin/user/login",{username:this.$data.username,password:this.$data.password}).then(function(){this.$store.commit("LOGIN_HIDE"),this.$router.push({path:"/admin/record/deal"})},function(t){this.$store.commit("MESSAGE_ACCORD_SHOW",{text:t.statusMessage})})}},computed:{$$login:function(){return this.$store.state.login.ctrl}}}},66:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var r=n(11);a(r);e.default={name:"message",data:function(){return{delay:!1,accord:!1}},methods:{know:function(){this.$store.commit("MESSAGE_ACCORD_HIDE")}},computed:{$$message:function(){return this.$store.state.message.message},$$delay:function(){return this.$store.state.message.delay},$$accord:function(){return this.$store.state.message.accord},$$text:function(){return this.$store.state.message.text}}}},82:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var r=n(11),s=a(r);s.default.directive("focus",{inserted:function(t){t.focus()}})},83:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}var r=n(11),s=a(r);s.default.filter("test",function(t){return 1==t})},84:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={install:function(t){t.directive("keyboard",{inserted:function(t,e){console.log(e);var n=e.arg,a=document.createElement("input");a.type="text",a.setAttribute("readOnly","true"),n&&n.input?a.className=n.input:a.style.border="1px solid #000",a.onclick=function(){console.log("唤起键盘")};var r=function(t){if("delete"==t)a.value=a.value.substr(0,a.value.length-1);else{if(a.value.indexOf(".")!=-1){var e=a.value.indexOf("."),n=a.value.length;if(n-e>2)return!1}a.value+=t}};t.appendChild(a);var s=document.createElement("div");n&&n.keyboard?s.className=n.keyboard:(s.style.width="100%",s.style.height="280px",s.style.position="fixed",s.style.left="0",s.style.bottom="0",s.style.borderTop="1px solid #000");var i="width:33.33%;height:70px;float:left;text-align:center;line-height:70px;",o=document.createElement("div"),c=document.createElement("div");c.innerHTML="1",c.style.cssText=i,c.onclick=function(){r("1")};var u=document.createElement("div");u.innerHTML="2",u.style.cssText=i,u.onclick=function(){r("2")};var l=document.createElement("div");l.innerHTML="3",l.style.cssText=i,l.onclick=function(){r("3")};var d=document.createElement("div");d.innerHTML="4",d.style.cssText=i,d.onclick=function(){r("4")};var f=document.createElement("div");f.innerHTML="5",f.style.cssText=i,f.onclick=function(){r("5")};var p=document.createElement("div");p.innerHTML="6",p.style.cssText=i,p.onclick=function(){r("6")};var v=document.createElement("div");v.innerHTML="7",v.style.cssText=i,v.onclick=function(){r("7")};var m=document.createElement("div");m.innerHTML="8",m.style.cssText=i,m.onclick=function(){r("8")};var _=document.createElement("div");_.innerHTML="9",_.style.cssText=i,_.onclick=function(){r("9")};var h=document.createElement("div");h.innerHTML=".",h.style.cssText=i,h.onclick=function(){r(".")};var g=document.createElement("div");g.innerHTML="0",g.style.cssText=i,g.onclick=function(){r("0")};var C=document.createElement("div");C.innerHTML="x",C.style.cssText=i,C.onclick=function(){r("delete")},s.appendChild(c),s.appendChild(u),s.appendChild(l),s.appendChild(d),s.appendChild(f),s.appendChild(p),s.appendChild(v),s.appendChild(m),s.appendChild(_),s.appendChild(h),s.appendChild(g),s.appendChild(C),s.appendChild(o);var y=document.createElement("div");s.appendChild(y),t.appendChild(s)}})}}},85:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var r=n(96),s=a(r),i=n(33),o=a(i);e.default={install:function(t){var e={joint:function(t){for(var e in t)if("object"==(0,s.default)(t[e])){for(var n=0;n<t[e].length;n++)if("name"==e){if(!this[e](t[e][n].data,t[e][n].text))return!1}else if(!this[e](t[e][n]))return!1}else if(!this[e](t[e]))return!1;return!0},name:function(t,e){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,15}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:(e?e:"名称")+"长度限制1-15个字"}),!1)},address:function(t){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,35}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"地址长度限制1-35个字"}),!1)},phone:function(t){return!!/^1(3|4|5|7|8)\d{9}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的手机号码"}),!1)},code:function(t){return 6==t.length||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的验证码"}),!1)},bankNo:function(t){return!!/^(\d{16}|\d{19})$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的银行卡号"}),!1)},idCard:function(t){var e=/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;if(e.test(t)){if(18==t.length){for(var n=new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2),a=new Array(1,0,10,9,8,7,6,5,4,3,2),r=0,s=0;s<17;s++)r+=t.substring(s,s+1)*n[s];var i=r%11,c=t.substring(17);if(2==i){if("X"==c||"x"==c)return!0;o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}else{if(c==a[i])return!0;o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}}}else o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}};t.__validate=e,t.prototype._$validate=e}}},86:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var r=n(11),s=a(r),i=n(265),o=a(i),c=n(88),u=a(c);s.default.use(o.default),e.default=new o.default({mode:"history",routes:u.default,scrollBehavior:function(t,e,n){}})},87:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var r=n(191),s=a(r),i=function(t){return n.e(0,function(){return t(n(204))})},o=function(t){return n.e(0,function(){return t(n(203))})},c=function(t){return n.e(0,function(){return t(n(224))})},u=function(t){return n.e(0,function(){return t(n(223))})},l=function(t){return n.e(0,function(){return t(n(222))})},d=function(t){return n.e(0,function(){return t(n(213))})},f=function(t){return n.e(0,function(){return t(n(217))})},p=function(t){return n.e(0,function(){return t(n(219))})},v=function(t){return n.e(0,function(){return t(n(218))})},m=function(t){return n.e(0,function(){return t(n(194))})},_=function(t){return n.e(0,function(){return t(n(192))})},h=function(t){return n.e(0,function(){return t(n(193))})},g=function(t){return n.e(0,function(){return t(n(202))})},C=function(t){return n.e(0,function(){return t(n(32))})},y=function(t){return n.e(0,function(){return t(n(205))})},b=function(t){return n.e(0,function(){return t(n(32))})},x=function(t){return n.e(0,function(){return t(n(216))})},E=function(t){return n.e(0,function(){return t(n(32))})},k=function(t){return n.e(0,function(){return t(n(211))})},S=function(t){return n.e(0,function(){return t(n(212))})},$=function(t){return n.e(0,function(){return t(n(214))})},A=function(t){return n.e(0,function(){return t(n(215))})},w=function(t){return n.e(0,function(){return t(n(207))})},M=function(t){return n.e(0,function(){return t(n(208))})},O=function(t){return n.e(0,function(){return t(n(209))})},R=function(t){return n.e(0,function(){return t(n(206))})},P=function(t){return n.e(0,function(){return t(n(221))})},T=function(t){return n.e(0,function(){return t(n(201))})},D=function(t){return n.e(0,function(){return t(n(220))})},L=function(t){return n.e(0,function(){return t(n(43))})},j=function(t){return n.e(0,function(){return t(n(225))})};e.default={path:"/admin/record",redirect:"/admin/record/deal",component:s.default,children:[{path:"newDeal",name:"NewDealQuery",component:j},{path:"test",name:"Test",component:D},{path:"bootPage",name:"BootPage",component:L},{path:"deal",name:"DealQuery",component:i},{path:"dealDet",name:"DealDet",component:o},{path:"withdrawal",name:"WithdrawalQuery",component:c},{path:"withdrawalAudit",name:"WithdrawalAudit",component:l},{path:"withdrawalDet",name:"WithdrawalDet",component:u},{path:"payQuery",name:"PayQuery",component:d},{path:"storeAccount",name:"StoreAccount",component:f},{path:"storeList",name:"StoreList",component:p},{path:"storeAudit",name:"StoreAudit",component:v},{path:"agentList",name:"AgentList",component:m},{path:"agentAccount",name:"AgentAccount",component:_},{path:"agentAdd",name:"AgentAdd",component:h},{path:"companyProfit",name:"CompanyProfit",component:g},{path:"companyProfitDet",name:"CompanyProfitDet",component:C},{path:"firProfit",name:"FirProfit",component:y},{path:"firProfitDet",name:"FirProfitDet",component:b},{path:"secProfit",name:"SecProfit",component:x},{path:"secProfitDet",name:"SecProfitDet",component:E},{path:"passAdd",name:"PassAdd",component:k},{path:"passList",name:"PassList",component:S},{path:"productAdd",name:"ProductAdd",component:$},{path:"productList",name:"ProductList",component:A},{path:"issue",name:"Issue",component:w},{path:"issue1",name:"Issue1",component:M},{path:"issueSuccess",name:"IssueSuccess",component:O},{path:"invite",name:"Invite",component:R},{path:"upgrade",name:"Upgrade",component:P},{path:"codeStatus",name:"CodeStatus",component:T}]}},88:function(t,e,n){"use strict";function a(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var r=n(87),s=a(r),i=n(210),o=a(i);e.default=[{path:"/admin",redirect:"/admin/record"},{path:"/admin/login",component:o.default},s.default]},89:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={ctrl:!0},a={LOGIN_SHOW:function(t){t.ctrl=!0},LOGIN_HIDE:function(t){t.ctrl=!1}};e.default={state:n,mutations:a}},90:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={message:!1,delay:!1,accord:!1,text:""},a={MESSAGE_DELAY_SHOW:function(t,e){t.message=!0,t.delay=!0,t.text=e.text,setTimeout(function(){t.message=!1,t.delay=!1},3e3)},MESSAGE_DELAY_HIDE:function(t){t.message=!1,t.delay=!1},MESSAGE_ACCORD_SHOW:function(t,e){t.message=!0,t.accord=!0,t.text=e.text},MESSAGE_ACCORD_HIDE:function(t){t.message=!1,t.accord=!1}};e.default={state:n,mutations:a}},91:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={title:"钱包++",router:{ticketReserve:"车票预定",ticketRob:"抢票",ticketRobOrder:"确认订单",ticketRobDetail:"抢票详情",ticketPrivate:"私人定制",ticketOrder:"我的订单",ticketSubmitOrder:"提交订单",ticketSureOrder:"确认订单",ticketPayOrder:"订单详情",ticketTrain:"北京 → 深圳",ticketContacts:"常用联系人",ticketLogin:"12306登录",firstAdd:"确认订单",ticketRefundDetail:"订单详情",secondAdd:"确认订单",ticketAddChild:"常用联系人",ticketRefundSuccess:"出票成功"}},a={TITLE_CHANGE:function(t,e){"ticketTrain"==e.name&&(t.router.ticketTrain=e.formName+" → "+e.toName),t.title=t.router[e.name]}};e.default={state:n,mutations:a}},158:function(t,e){},176:function(t,e){},179:function(t,e){},186:function(t,e){},190:function(t,e){},191:function(t,e,n){var a,r;n(179),a=n(44);var s=n(250);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,t.exports=a},195:function(t,e,n){var a,r;a=n(48);var s=n(247);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,t.exports=a},196:function(t,e,n){var a,r;a=n(49);var s=n(259);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,t.exports=a},197:function(t,e,n){var a,r;n(186),a=n(50);var s=n(257);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,r._scopeId="data-v-a76ae156",t.exports=a},198:function(t,e,n){var a,r;a=n(51);var s=n(242);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,t.exports=a},199:function(t,e,n){var a,r;n(190),a=n(52);var s=n(263);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,r._scopeId="data-v-fff64b98",t.exports=a},200:function(t,e,n){var a,r;a=n(53);var s=n(260);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,t.exports=a},210:function(t,e,n){var a,r;n(158),a=n(65);var s=n(227);r=a=a||{},"object"!=typeof a.default&&"function"!=typeof a.default||(r=a=a.default),"function"==typeof r&&(r=r.options),r.render=s.render,r.staticRenderFns=s.staticRenderFns,r._scopeId="data-v-0e0a7688",t.exports=a},227:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return t.$$login?n("div",{attrs:{id:"login"}},[n("div",{staticClass:"login-space"},[n("div",{staticClass:"login-title"},[t._v("登录")]),t._v(" "),n("div",{staticClass:"flexBox group"},[n("div",{staticClass:"text"},[t._v("用户名")]),t._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:t.username,expression:"username"}],staticClass:"ipt",attrs:{type:"text",placeholder:"请输入用户名"},domProps:{value:t._s(t.username)},on:{input:function(e){e.target.composing||(t.username=e.target.value)}}})]),t._v(" "),n("div",{staticClass:"flexBox group"},[n("div",{staticClass:"text"},[t._v("密码")]),t._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:t.password,expression:"password"}],staticClass:"ipt",attrs:{type:"password",placeholder:"请输入密码"},domProps:{value:t._s(t.password)},on:{input:function(e){e.target.composing||(t.password=e.target.value)}}})]),t._v(" "),n("div",{staticClass:"submit",on:{click:t.submit}},[t._v("登录")])]),t._v(" "),n("message")],1):t._e()},staticRenderFns:[]}},242:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement;t._self._c||e;return t._m(0)},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("footer",{staticClass:"main-footer",staticStyle:{"margin-left":"130px"}},[n("div",{staticClass:"pull-right hidden-xs"},[t._v("\n    Anything you want\n  ")]),t._v(" "),n("strong",[t._v("Copyright © 2016 "),n("a",{attrs:{href:"#"}},[t._v("Company")]),t._v(".")]),t._v(" All rights reserved.\n")])}]}},246:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("transition",{attrs:{name:"fade"}},[t.$$message?n("div",{staticClass:"main",on:{click:t.know}},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.$$accord,expression:"$$accord"}],staticClass:"col-md-3 content"},[n("div",{staticClass:"box box-danger"},[n("div",{staticClass:"box-header with-border"},[n("h3",{staticClass:"box-title"},[t._v("提示")])]),t._v(" "),n("div",{staticClass:"box-body"},[t._v("\n          "+t._s(t.$$text)+"\n        ")])])])]):t._e()])},staticRenderFns:[]}},247:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("aside",{staticClass:"control-sidebar control-sidebar-dark"},[t._m(0),t._v(" "),n("div",{staticClass:"tab-content"},[t._m(1),t._v(" "),n("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-stats-tab"}},[t._v("Stats Tab Content")]),t._v(" "),n("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-settings-tab"}},[n("form",{attrs:{method:"post"}},[n("h3",{staticClass:"control-sidebar-heading"},[t._v("General Settings")]),t._v(" "),n("div",{staticClass:"form-group"},[n("label",{staticClass:"control-sidebar-subheading"},[t._v("\n            Report panel usage\n            "),n("input",{staticClass:"pull-right",attrs:{type:"checkbox",checked:""},domProps:{checked:!0}})]),t._v(" "),n("p",[t._v("\n            Some information about this general settings option\n          ")])])])])])])},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("ul",{staticClass:"nav nav-tabs nav-justified control-sidebar-tabs"},[n("li",{staticClass:"active"},[n("a",{attrs:{href:"#control-sidebar-home-tab","data-toggle":"tab"}},[n("i",{staticClass:"fa fa-home"})])]),t._v(" "),n("li",[n("a",{attrs:{href:"#control-sidebar-settings-tab","data-toggle":"tab"}},[n("i",{staticClass:"fa fa-gears"})])])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"tab-pane active",attrs:{id:"control-sidebar-home-tab"}},[n("h3",{staticClass:"control-sidebar-heading"},[t._v("Recent Activity")]),t._v(" "),n("ul",{staticClass:"control-sidebar-menu"},[n("li",[n("a",{attrs:{href:"javascript::;"}},[n("i",{staticClass:"menu-icon fa fa-birthday-cake bg-red"}),t._v(" "),n("div",{staticClass:"menu-info"},[n("h4",{staticClass:"control-sidebar-subheading"},[t._v("Langdon's Birthday")]),t._v(" "),n("p",[t._v("Will be 23 on April 24th")])])])])]),t._v(" "),n("h3",{staticClass:"control-sidebar-heading"},[t._v("Tasks Progress")]),t._v(" "),n("ul",{staticClass:"control-sidebar-menu"},[n("li",[n("a",{attrs:{href:"javascript::;"}},[n("h4",{staticClass:"control-sidebar-subheading"},[t._v("\n              Custom Template Design\n              "),n("span",{staticClass:"pull-right-container"},[n("span",{staticClass:"label label-danger pull-right"},[t._v("70%")])])]),t._v(" "),n("div",{staticClass:"progress progress-xxs"},[n("div",{staticClass:"progress-bar progress-bar-danger",staticStyle:{width:"70%"}})])])])])])}]}},250:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"main"}},[n("router-view"),t._v(" "),n("message")],1)},staticRenderFns:[]}},257:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"content-wrapper",staticStyle:{"margin-left":"130px"}},[n("section",{staticClass:"content",staticStyle:{overflow:"hidden",padding:"0"}},[n("router-view")],1)])},staticRenderFns:[]}},259:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"control-sidebar-bg"})},staticRenderFns:[]}},260:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("aside",{staticClass:"main-sidebar",staticStyle:{width:"130px"}},[n("section",{staticClass:"sidebar"},[n("ul",{staticClass:"sidebar-menu nav nav-pills nav-stacked"},[n("li",{staticClass:"treeview active"},[t._m(0),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{staticClass:"active"},[n("router-link",{attrs:{to:"/admin/record/deal"}},[n("span",[t._v("交易查询")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/payQuery"}},[n("span",[t._v("支付查询")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/withdrawal"}},[n("span",[t._v("打款查询")])])],1)])]),t._v(" "),n("li",{staticClass:"treeview"},[t._m(1),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{staticClass:"active"},[n("router-link",{attrs:{to:"/admin/record/storeList"}},[n("span",[t._v("商户列表")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/storeAccount"}},[n("span",[t._v("商户账户")])])],1)])]),t._v(" "),n("li",{staticClass:"treeview"},[t._m(2),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",[n("router-link",{attrs:{to:"/admin/record/agentList"}},[n("span",[t._v("代理商列表")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/agentAccount"}},[n("span",[t._v("代理商账户")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/agentAdd"}},[n("span",[t._v("新增代理商")])])],1)])]),t._v(" "),n("li",{staticClass:"treeview"},[t._m(3),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",[n("router-link",{attrs:{to:"/admin/record/companyProfit"}},[n("span",[t._v("公司分润")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/firProfit"}},[n("span",[t._v("一级代理商分润")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/secProfit"}},[n("span",[t._v("二级代理商分润")])])],1)])]),t._v(" "),n("li",{staticClass:"treeview"},[t._m(4),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",[n("router-link",{attrs:{to:"/admin/record/productList"}},[n("span",[t._v("产品列表")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/productAdd"}},[n("span",[t._v("新增产品")])])],1)])]),t._v(" "),n("li",{staticClass:"treeview"},[t._m(5),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",[n("router-link",{attrs:{to:"/admin/record/passList"}},[n("span",[t._v("通道列表")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/passAdd"}},[n("span",[t._v("新增通道")])])],1)])]),t._v(" "),n("li",{staticClass:"treeview"},[t._m(6),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",[n("router-link",{attrs:{to:"/admin/record/issue1"}},[n("span",[t._v("按个数分配")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/issue"}},[n("span",[t._v("按码段分配")])])],1),t._v(" "),n("li",[n("router-link",{attrs:{to:"/admin/record/codeStatus"}},[n("span",[t._v("二维码状态查询")])])],1)])])])])])},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("i",{staticClass:"fa fa-link"}),t._v(" "),n("span",[t._v("交易管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("i",{staticClass:"fa fa-link"}),t._v(" "),n("span",[t._v("商户管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("i",{staticClass:"fa fa-link"}),n("span",[t._v("代理商管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("i",{staticClass:"fa fa-link"}),n("span",[t._v("分润管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("i",{staticClass:"fa fa-link"}),n("span",[t._v("产品管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("i",{staticClass:"fa fa-link"}),n("span",[t._v("通道管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("i",{staticClass:"fa fa-link"}),n("span",[t._v("分配二维码")])])}]}},263:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("header",{staticClass:"main-header"},[t._m(0),t._v(" "),n("nav",{staticClass:"navbar navbar-static-top",staticStyle:{"margin-left":"130px"},attrs:{role:"navigation"}},[t._m(1),t._v(" "),n("div",{staticClass:"navbar-custom-menu"},[n("ul",{staticClass:"nav navbar-nav"},[n("li",{staticClass:"dropdown user user-menu"},[n("div",{staticClass:"loginInfo"},[n("span",{staticClass:"name"},[t._v("超级管理员")]),t._v(" "),n("span",{staticClass:"btn btn-danger",on:{click:t.logout}},[t._v("退出")])])])])])])])},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{staticClass:"logo",staticStyle:{width:"130px"},attrs:{href:"javascript:;"}},[n("span",{staticClass:"logo-mini",staticStyle:{"font-size":"20px"}},[t._v("好收收")]),t._v(" "),n("span",{staticClass:"logo-lg"},[t._v("好收收")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{staticClass:"sidebar-toggle",attrs:{href:"#","data-toggle":"offcanvas",role:"button"}},[n("span",{staticClass:"sr-only"},[t._v("Toggle navigation")])])}]}}});
//# sourceMappingURL=app.105f63ce98a372e20067.js.map