webpackJsonp([3,1],{0:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}var a=n(4),i=r(a),o=n(238),s=r(o);n(251);var c=n(25),u=r(c),d=n(144),l=r(d),f=n(426),v=r(f);n(140),n(141);var m=n(142),p=r(m),_=n(143),h=r(_),g=n(326),w=r(g),C=n(327),y=r(C),b=n(324),k=r(b),A=n(325),x=r(A),S=n(322),E=r(S),$=n(323),D=r($);i.default.use(s.default),i.default.use(v.default),i.default.http.interceptors.push(function(e,t){t(function(e){var t=e.status,n=e.body;return 200==t?n.code==-100||n.code==-200||n.code==-201?(u.default.commit("LOGIN_SHOW"),l.default.push("/admin/login")):1!=n.code?(e.status=500,e.statusMessage=n.msg||"系统异常",e.statusText="Internal Server Error",e.ok=!1):e.data=n.result:e.statusMessage="系统异常",e})}),i.default.use(p.default),i.default.use(h.default),l.default.beforeEach(function(e,t,n){n()}),i.default.component("app-header",w.default),i.default.component("app-menu",y.default),i.default.component("app-content",k.default),i.default.component("app-footer",x.default),i.default.component("app-aside",E.default),i.default.component("app-aside-bg",D.default);new i.default({store:u.default,router:l.default}).$mount("#app")},15:function(e,t,n){var r,a;n(281),r=n(108);var i=n(400);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-61e7739a",e.exports=r},25:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var a=n(4),i=r(a),o=n(428),s=r(o),c=n(149),u=r(c),d=n(148),l=r(d),f=n(147),v=r(f);i.default.use(s.default),t.default=new s.default.Store({modules:{title:u.default,message:l.default,login:v.default}})},84:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var a=n(15),i=r(a);t.default={name:"main",components:{Message:i.default}}},90:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"collection",data:function(){return{msg:"注册"}}}},91:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"collection",data:function(){return{msg:"注册"}}}},92:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"collection",data:function(){return{msg:"注册"}}}},93:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"collection",data:function(){return{msg:"注册"}}}},94:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"collection",data:function(){return{}},methods:{logout:function(){this.$http.post("/admin/user/logout").then(function(){this.$router.push("/admin/login")},function(e){console.log(e)})}}}},95:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={name:"collection",data:function(){return{msg:"注册",url:""}},created:function(){this.$data.url=location.pathname},methods:{refrash:function(){location.reload()},open:function(){var e=document.getElementById("right");"el-icon-arrow-down  pull-right"==e.className?e.className="el-icon-arrow-left  pull-right":e.className="el-icon-arrow-down  pull-right"}}}},107:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var a=n(15),i=r(a);t.default={name:"login",components:{Message:i.default},data:function(){return{msg:"登录",login:!1,username:"",password:""}},created:function(){this.$store.commit("LOGIN_SHOW")},methods:{submit:function(){this.$http.post("/admin/user/login",{username:this.$data.username,password:this.$data.password}).then(function(){this.$store.commit("LOGIN_HIDE"),this.$router.push({path:"/admin/record/newDeal"})},function(e){this.$store.commit("MESSAGE_ACCORD_SHOW",{text:e.statusMessage})})}},computed:{$$login:function(){return this.$store.state.login.ctrl}}}},108:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var a=n(4);r(a);t.default={name:"message",data:function(){return{delay:!1,accord:!1}},methods:{know:function(){this.$store.commit("MESSAGE_ACCORD_HIDE")}},computed:{$$message:function(){return this.$store.state.message.message},$$delay:function(){return this.$store.state.message.delay},$$accord:function(){return this.$store.state.message.accord},$$text:function(){return this.$store.state.message.text}}}},140:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}var a=n(4),i=r(a);i.default.directive("focus",{inserted:function(e){e.focus()}})},141:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}var a=n(4),i=r(a);i.default.filter("changeTime",function(e){if(""==e||null==e)return"";var t=function(e){return e<10&&(e="0"+e),e};e=new Date(e);var n=e.getFullYear(),r=e.getMonth()+1,a=e.getDate(),i=e.getHours(),o=e.getMinutes(),s=e.getSeconds();return n+"-"+t(r)+"-"+t(a)+" "+t(i)+":"+t(o)+":"+t(s)})},142:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default={install:function(e){e.directive("keyboard",{inserted:function(e,t){console.log(t);var n=t.arg,r=document.createElement("input");r.type="text",r.setAttribute("readOnly","true"),n&&n.input?r.className=n.input:r.style.border="1px solid #000",r.onclick=function(){console.log("唤起键盘")};var a=function(e){if("delete"==e)r.value=r.value.substr(0,r.value.length-1);else{if(r.value.indexOf(".")!=-1){var t=r.value.indexOf("."),n=r.value.length;if(n-t>2)return!1}r.value+=e}};e.appendChild(r);var i=document.createElement("div");n&&n.keyboard?i.className=n.keyboard:(i.style.width="100%",i.style.height="280px",i.style.position="fixed",i.style.left="0",i.style.bottom="0",i.style.borderTop="1px solid #000");var o="width:33.33%;height:70px;float:left;text-align:center;line-height:70px;",s=document.createElement("div"),c=document.createElement("div");c.innerHTML="1",c.style.cssText=o,c.onclick=function(){a("1")};var u=document.createElement("div");u.innerHTML="2",u.style.cssText=o,u.onclick=function(){a("2")};var d=document.createElement("div");d.innerHTML="3",d.style.cssText=o,d.onclick=function(){a("3")};var l=document.createElement("div");l.innerHTML="4",l.style.cssText=o,l.onclick=function(){a("4")};var f=document.createElement("div");f.innerHTML="5",f.style.cssText=o,f.onclick=function(){a("5")};var v=document.createElement("div");v.innerHTML="6",v.style.cssText=o,v.onclick=function(){a("6")};var m=document.createElement("div");m.innerHTML="7",m.style.cssText=o,m.onclick=function(){a("7")};var p=document.createElement("div");p.innerHTML="8",p.style.cssText=o,p.onclick=function(){a("8")};var _=document.createElement("div");_.innerHTML="9",_.style.cssText=o,_.onclick=function(){a("9")};var h=document.createElement("div");h.innerHTML=".",h.style.cssText=o,h.onclick=function(){a(".")};var g=document.createElement("div");g.innerHTML="0",g.style.cssText=o,g.onclick=function(){a("0")};var w=document.createElement("div");w.innerHTML="x",w.style.cssText=o,w.onclick=function(){a("delete")},i.appendChild(c),i.appendChild(u),i.appendChild(d),i.appendChild(l),i.appendChild(f),i.appendChild(v),i.appendChild(m),i.appendChild(p),i.appendChild(_),i.appendChild(h),i.appendChild(g),i.appendChild(w),i.appendChild(s);var C=document.createElement("div");i.appendChild(C),e.appendChild(i)}})}}},143:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var a=n(152),i=r(a),o=n(25),s=r(o);t.default={install:function(e){var t={joint:function(e){for(var t in e)if("object"==(0,i.default)(e[t])){for(var n=0;n<e[t].length;n++)if("name"==t){if(!this[t](e[t][n].data,e[t][n].text))return!1}else if(!this[t](e[t][n]))return!1}else if(!this[t](e[t]))return!1;return!0},name:function(e,t){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,15}$/.test(e)||(s.default.commit("MESSAGE_ACCORD_SHOW",{text:(t?t:"名称")+"长度限制1-15个字"}),!1)},address:function(e){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,35}$/.test(e)||(s.default.commit("MESSAGE_ACCORD_SHOW",{text:"地址长度限制1-35个字"}),!1)},phone:function(e){return!!/^1(3|4|5|7|8)\d{9}$/.test(e)||(s.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的手机号码"}),!1)},code:function(e){return 6==e.length||(s.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的验证码"}),!1)},bankNo:function(e){return!!/^(\d{16}|\d{19})$/.test(e)||(s.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的银行卡号"}),!1)},idCard:function(e){var t=/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;if(t.test(e)){if(18==e.length){for(var n=new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2),r=new Array(1,0,10,9,8,7,6,5,4,3,2),a=0,i=0;i<17;i++)a+=e.substring(i,i+1)*n[i];var o=a%11,c=e.substring(17);if(2==o){if("X"==c||"x"==c)return!0;s.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}else{if(c==r[o])return!0;s.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}}}else s.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}};e.__validate=t,e.prototype._$validate=t}}},144:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var a=n(4),i=r(a),o=n(427),s=r(o),c=n(146),u=r(c);i.default.use(s.default),t.default=new s.default({mode:"history",routes:u.default,scrollBehavior:function(e,t,n){}})},145:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var a=n(316),i=r(a),o=function(e){return n.e(0,function(){return e(n(341))})},s=function(e){return n.e(0,function(){return e(n(340))})},c=function(e){return n.e(0,function(){return e(n(334))})},u=function(e){return n.e(0,function(){return e(n(333))})},d=function(e){return n.e(0,function(){return e(n(368))})},l=function(e){return n.e(0,function(){return e(n(367))})},f=function(e){return n.e(0,function(){return e(n(351))})},v=function(e){return n.e(0,function(){return e(n(352))})},m=function(e){return n.e(0,function(){return e(n(353))})},p=function(e){return n.e(0,function(){return e(n(23))})},_=function(e){return n.e(0,function(){return e(n(23))})},h=function(e){return n.e(0,function(){return e(n(23))})},g=function(e){return n.e(0,function(){return e(n(23))})},w=function(e){return n.e(0,function(){return e(n(349))})},C=function(e){return n.e(0,function(){return e(n(350))})},y=function(e){return n.e(0,function(){return e(n(360))})},b=function(e){return n.e(0,function(){return e(n(361))})},k=function(e){return n.e(0,function(){return e(n(362))})},A=function(e){return n.e(0,function(){return e(n(344))})},x=function(e){return n.e(0,function(){return e(n(369))})},S=function(e){return n.e(0,function(){return e(n(366))})},E=function(e){return n.e(0,function(){return e(n(365))})},$=function(e){return n.e(0,function(){return e(n(364))})},D=function(e){return n.e(0,function(){return e(n(357))})},M=function(e){return n.e(0,function(){return e(n(356))})},O=function(e){return n.e(0,function(){return e(n(354))})},L=function(e){return n.e(0,function(){return e(n(358))})},R=function(e){return n.e(0,function(){return e(n(359))})},P=function(e){return n.e(0,function(){return e(n(355))})},F=function(e){return n.e(0,function(){return e(n(321))})},T=function(e){return n.e(0,function(){return e(n(320))})},H=function(e){return n.e(0,function(){return e(n(317))})},j=function(e){return n.e(0,function(){return e(n(319))})},N=function(e){return n.e(0,function(){return e(n(318))})},W=function(e){return n.e(0,function(){return e(n(337))})},I=function(e){return n.e(0,function(){return e(n(338))})},G=function(e){return n.e(0,function(){return e(n(332))})},Q=function(e){return n.e(0,function(){return e(n(330))})},B=function(e){return n.e(0,function(){return e(n(331))})},Y=function(e){return n.e(0,function(){return e(n(328))})},z=function(e){return n.e(0,function(){return e(n(329))})},X=function(e){return n.e(0,function(){return e(n(347))})},Z=function(e){return n.e(0,function(){return e(n(348))})},J=function(e){return n.e(0,function(){return e(n(336))})},q=function(e){return n.e(0,function(){return e(n(342))})},K=function(e){return n.e(0,function(){return e(n(343))})},U=function(e){return n.e(0,function(){return e(n(346))})},V=function(e){return n.e(0,function(){return e(n(345))})},ee=function(e){return n.e(0,function(){return e(n(363))})};t.default={path:"/admin/record",redirect:"/admin/record/newDeal",component:i.default,children:[{path:"storeAuditHSY",name:"StoreAuditHSY",component:P},{path:"t1Audit",name:"T1Audit",component:y},{path:"tAuditDealer",name:"TAuditDealer",component:b},{path:"tAuditStore",name:"TAuditStore",component:k},{path:"newDeal",name:"NewDealQuery",component:d},{path:"newDealDet",name:"NewDealDet",component:l},{path:"newWithdrawalQuery",name:"NewWithdrawalQuery",component:x},{path:"deal",name:"DealQuery",component:c},{path:"dealDet",name:"DealDet",component:u},{path:"withdrawal",name:"WithdrawalQuery",component:S},{path:"withdrawalAudit",name:"WithdrawalAudit",component:$},{path:"withdrawalDet",name:"WithdrawalDet",component:E},{path:"payQuery",name:"PayQuery",component:A},{path:"storeList",name:"StoreList",component:D},{path:"storeNotice",name:"StoreNotice",component:L},{path:"storeNoticeDet",name:"StoreNoticeDet",component:R},{path:"storeAuditList",name:"StoreAuditList",component:M},{path:"storeAudit",name:"StoreAudit",component:O},{path:"agentListFir",name:"AgentListFir",component:T},{path:"agentListSec",name:"AgentListSec",component:F},{path:"agentAdd",name:"AgentAdd",component:H},{path:"agentAddPro",name:"AgentAddPro",component:j},{path:"agentAddBase",name:"AgentAddBase",component:N},{path:"passAdd",name:"PassAdd",component:q},{path:"passList",name:"PassList",component:K},{path:"productAdd",name:"ProductAdd",component:X},{path:"productList",name:"ProductList",component:Z},{path:"issue",name:"Issue",component:W},{path:"issueRecord",name:"IssueRecord",component:I},{path:"codeProRecord",name:"CodeProRecord",component:Q},{path:"invite",name:"Invite",component:J},{path:"codeStatus",name:"CodeStatus",component:G},{path:"codeAll",name:"CodeAll",component:Y},{path:"codeDet",name:"CodeDet",component:z},{path:"codeProduct",name:"CodeProduct",component:B},{path:"orderQuery",name:"OrderQuery",component:o},{path:"orderDetail",name:"OrderDetail",component:s},{path:"profitAccount",name:"ProfitAccount",component:w},{path:"profitAccountDet",name:"ProfitAccountDet",component:C},{path:"profitCom",name:"ProfitCom",component:f},{path:"profitFir",name:"ProfitFir",component:v},{path:"profitSec",name:"ProfitSec",component:m},{path:"profitDet",name:"ProfitDet",component:_},{path:"profitComDet",name:"ProfitComDet",component:p},{path:"profitFirDet",name:"ProfitFirDet",component:h},{path:"profitSecDet",name:"ProfitSecDet",component:g},{path:"personnelList",name:"PersonnelList",component:U},{path:"personnelAdd",name:"PersonnelAdd",component:V},{path:"test",name:"Test",component:ee}]}},146:function(e,t,n){"use strict";function r(e){return e&&e.__esModule?e:{default:e}}Object.defineProperty(t,"__esModule",{value:!0});var a=n(145),i=r(a),o=n(339),s=r(o);t.default=[{path:"/admin",redirect:"/admin/record"},{path:"/admin/login",component:s.default},i.default]},147:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={ctrl:!0},r={LOGIN_SHOW:function(e){e.ctrl=!0},LOGIN_HIDE:function(e){e.ctrl=!1}};t.default={state:n,mutations:r}},148:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={message:!1,delay:!1,accord:!1,text:""},r={MESSAGE_DELAY_SHOW:function(e,t){e.message=!0,e.delay=!0,e.text=t.text,setTimeout(function(){e.message=!1,e.delay=!1},3e3)},MESSAGE_DELAY_HIDE:function(e){e.message=!1,e.delay=!1},MESSAGE_ACCORD_SHOW:function(e,t){e.message=!0,e.accord=!0,e.text=t.text},MESSAGE_ACCORD_HIDE:function(e){e.message=!1,e.accord=!1}};t.default={state:n,mutations:r}},149:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={title:"钱包++",router:{ticketReserve:"车票预定",ticketRob:"抢票",ticketRobOrder:"确认订单",ticketRobDetail:"抢票详情",ticketPrivate:"私人定制",ticketOrder:"我的订单",ticketSubmitOrder:"提交订单",ticketSureOrder:"确认订单",ticketPayOrder:"订单详情",ticketTrain:"北京 → 深圳",ticketContacts:"常用联系人",ticketLogin:"12306登录",firstAdd:"确认订单",ticketRefundDetail:"订单详情",secondAdd:"确认订单",ticketAddChild:"常用联系人",ticketRefundSuccess:"出票成功"}},r={TITLE_CHANGE:function(e,t){"ticketTrain"==t.name&&(e.router.ticketTrain=t.formName+" → "+t.toName),e.title=e.router[t.name]}};t.default={state:n,mutations:r}},251:function(e,t){},253:function(e,t){},281:function(e,t){},287:function(e,t){},299:function(e,t){},301:function(e,t){},304:function(e,t){},316:function(e,t,n){var r,a;n(287),r=n(84);var i=n(407);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,e.exports=r},322:function(e,t,n){var r,a;r=n(90);var i=n(402);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,e.exports=r},323:function(e,t,n){var r,a;r=n(91);var i=n(421);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,e.exports=r},324:function(e,t,n){var r,a;n(299),r=n(92);var i=n(419);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-a76ae156",e.exports=r},325:function(e,t,n){var r,a;r=n(93);var i=n(396);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,e.exports=r},326:function(e,t,n){var r,a;n(304),r=n(94);var i=n(425);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-fff64b98",e.exports=r},327:function(e,t,n){var r,a;n(301),r=n(95);var i=n(422);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-c2ed6b9a",e.exports=r},339:function(e,t,n){var r,a;n(253),r=n(107);var i=n(371);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-0e0a7688",e.exports=r},371:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return e.$$login?n("div",{attrs:{id:"login"}},[n("div",{staticClass:"login-space"},[n("div",{staticClass:"login-title"},[e._v("登录")]),e._v(" "),n("div",{staticClass:"flexBox group"},[n("div",{staticClass:"text"},[e._v("用户名")]),e._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:e.username,expression:"username"}],staticClass:"ipt",attrs:{type:"text",placeholder:"请输入用户名"},domProps:{value:e._s(e.username)},on:{input:function(t){t.target.composing||(e.username=t.target.value)}}})]),e._v(" "),n("div",{staticClass:"flexBox group"},[n("div",{staticClass:"text"},[e._v("密码")]),e._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:e.password,expression:"password"}],staticClass:"ipt",attrs:{type:"password",placeholder:"请输入密码"},domProps:{value:e._s(e.password)},on:{input:function(t){t.target.composing||(e.password=t.target.value)}}})]),e._v(" "),n("div",{staticClass:"submit",on:{click:e.submit}},[e._v("登录")])]),e._v(" "),n("message")],1):e._e()},staticRenderFns:[]}},396:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement;e._self._c||t;return e._m(0)},staticRenderFns:[function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("footer",{staticClass:"main-footer",staticStyle:{"margin-left":"130px"}},[n("div",{staticClass:"pull-right hidden-xs"},[e._v("\n    Anything you want\n  ")]),e._v(" "),n("strong",[e._v("Copyright © 2016 "),n("a",{attrs:{href:"#"}},[e._v("Company")]),e._v(".")]),e._v(" All rights reserved.\n")])}]}},400:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("transition",{attrs:{name:"fade"}},[e.$$message?n("div",{staticClass:"main",on:{click:e.know}},[n("div",{directives:[{name:"show",rawName:"v-show",value:e.$$accord,expression:"$$accord"}],staticClass:"col-md-3 content"},[n("div",{staticClass:"box box-danger"},[n("div",{staticClass:"box-header with-border"},[n("h3",{staticClass:"box-title"},[e._v("提示")])]),e._v(" "),n("div",{staticClass:"box-body"},[e._v("\n          "+e._s(e.$$text)+"\n        ")])])]),e._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:e.$$delay,expression:"$$delay"}],staticClass:"col-md-3 content"},[n("div",{staticClass:"box box-danger"},[n("div",{staticClass:"box-header with-border"},[n("h3",{staticClass:"box-title"},[e._v("提示")])]),e._v(" "),n("div",{staticClass:"box-body"},[e._v("\n          "+e._s(e.$$text)+"\n        ")])])])]):e._e()])},staticRenderFns:[]}},402:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("aside",{staticClass:"control-sidebar control-sidebar-dark"},[e._m(0),e._v(" "),n("div",{staticClass:"tab-content"},[e._m(1),e._v(" "),n("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-stats-tab"}},[e._v("Stats Tab Content")]),e._v(" "),n("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-settings-tab"}},[n("form",{attrs:{method:"post"}},[n("h3",{staticClass:"control-sidebar-heading"},[e._v("General Settings")]),e._v(" "),n("div",{staticClass:"form-group"},[n("label",{staticClass:"control-sidebar-subheading"},[e._v("\n            Report panel usage\n            "),n("input",{staticClass:"pull-right",attrs:{type:"checkbox",checked:""},domProps:{checked:!0}})]),e._v(" "),n("p",[e._v("\n            Some information about this general settings option\n          ")])])])])])])},staticRenderFns:[function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("ul",{staticClass:"nav nav-tabs nav-justified control-sidebar-tabs"},[n("li",{staticClass:"active"},[n("a",{attrs:{href:"#control-sidebar-home-tab","data-toggle":"tab"}},[n("i",{staticClass:"fa fa-home"})])]),e._v(" "),n("li",[n("a",{attrs:{href:"#control-sidebar-settings-tab","data-toggle":"tab"}},[n("i",{staticClass:"fa fa-gears"})])])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"tab-pane active",attrs:{id:"control-sidebar-home-tab"}},[n("h3",{staticClass:"control-sidebar-heading"},[e._v("Recent Activity")]),e._v(" "),n("ul",{staticClass:"control-sidebar-menu"},[n("li",[n("a",{attrs:{href:"javascript::;"}},[n("i",{staticClass:"menu-icon fa fa-birthday-cake bg-red"}),e._v(" "),n("div",{staticClass:"menu-info"},[n("h4",{staticClass:"control-sidebar-subheading"},[e._v("Langdon's Birthday")]),e._v(" "),n("p",[e._v("Will be 23 on April 24th")])])])])]),e._v(" "),n("h3",{staticClass:"control-sidebar-heading"},[e._v("Tasks Progress")]),e._v(" "),n("ul",{staticClass:"control-sidebar-menu"},[n("li",[n("a",{attrs:{href:"javascript::;"}},[n("h4",{staticClass:"control-sidebar-subheading"},[e._v("\n              Custom Template Design\n              "),n("span",{staticClass:"pull-right-container"},[n("span",{staticClass:"label label-danger pull-right"},[e._v("70%")])])]),e._v(" "),n("div",{staticClass:"progress progress-xxs"},[n("div",{staticClass:"progress-bar progress-bar-danger",staticStyle:{width:"70%"}})])])])])])}]}},407:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{attrs:{id:"main"}},[n("router-view"),e._v(" "),n("message")],1)},staticRenderFns:[]}},419:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"content-wrapper",staticStyle:{"margin-left":"130px"}},[n("section",{staticClass:"content",staticStyle:{overflow:"hidden",padding:"0"}},[n("router-view")],1)])},staticRenderFns:[]}},421:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"control-sidebar-bg"})},staticRenderFns:[]}},422:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("aside",{staticClass:"main-sidebar",staticStyle:{width:"130px"}},[n("section",{staticClass:"sidebar"},[n("ul",{staticClass:"sidebar-menu nav nav-pills nav-stacked"},[n("li",{class:"/admin/record/newDeal"==e.url?"treeview active":"treeview"},[e._m(0),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/newDeal"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/newDeal"}},[n("span",[e._v("交易查询")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/profitCom"==e.url||"/admin/record/profitFir"==e.url||"/admin/record/profitSec"==e.url||"/admin/record/profitAccount"==e.url||"/admin/record/profitDet"==e.url?"treeview active":"treeview"},[e._m(1),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/profitDet"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/profitDet"}},[n("span",[e._v("分润明细")])])],1),e._v(" "),n("li",{class:"/admin/record/profitCom"==e.url||"/admin/record/profitFir"==e.url||"/admin/record/profitSec"==e.url?"treeview active":"treeview"},[n("a",{attrs:{href:"#"},on:{click:e.open}},[n("span",[e._v("分润统计")]),e._v(" "),e._m(2)]),e._v(" "),n("ul",{staticClass:"treeview-menu",staticStyle:{"margin-left":"-8px"}},[n("li",{class:"/admin/record/profitCom"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/profitCom"}},[n("span",[e._v("公司分润")])])],1),e._v(" "),n("li",{class:"/admin/record/profitFir"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/profitFir"}},[n("span",[e._v("一级代理商分润")])])],1),e._v(" "),n("li",{class:"/admin/record/profitSec"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/profitSec"}},[n("span",[e._v("二级代理商分润")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/profitAccount"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/profitAccount"}},[n("span",[e._v("公司分润账户")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/t1Audit"==e.url||"/admin/record/tAuditStore"==e.url||"/admin/record/tAuditDealer"==e.url?"treeview active":"treeview"},[e._m(3),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/t1Audit"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/t1Audit"}},[n("span",[e._v("T1结算审核")])])],1),e._v(" "),n("li",{class:"/admin/record/tAuditStore"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/tAuditStore"}},[n("span",[e._v("商户结算记录")])])],1),e._v(" "),n("li",{class:"/admin/record/tAuditDealer"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/tAuditDealer"}},[n("span",[e._v("代理商结算记录")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/payQuery"==e.url||"/admin/record/newWithdrawalQuery"==e.url?"treeview active":"treeview"},[e._m(4),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/payQuery"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/payQuery"}},[n("span",[e._v("支付查询")])])],1),e._v(" "),n("li",{class:"/admin/record/newWithdrawalQuery"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/newWithdrawalQuery"}},[n("span",[e._v("打款查询")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/storeList"==e.url||"/admin/record/storeAuditList"==e.url||"/admin/record/storeNotice"==e.url?"treeview active":"treeview"},[e._m(5),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/storeList"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/storeList"}},[n("span",[e._v("商户列表")])])],1),e._v(" "),n("li",{class:"/admin/record/storeAuditList"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/storeAuditList"}},[n("span",[e._v("待审核商户")])])],1),e._v(" "),n("li",{class:"/admin/record/storeNotice"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/storeNotice"}},[n("span",[e._v("商户公告")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/agentListFir"==e.url||"/admin/record/agentListSec"==e.url?"treeview active":"treeview"},[e._m(6),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/agentListFir"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/agentListFir"}},[n("span",[e._v("一级代理商")])])],1),e._v(" "),n("li",{class:"/admin/record/agentListSec"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/agentListSec"}},[n("span",[e._v("二级代理商")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/issueRecord"==e.url||"/admin/record/issue"==e.url||"/admin/record/codeStatus"==e.url||"/admin/record/codeProRecord"==e.url||"/admin/record/codeAll"==e.url?"treeview active":"treeview"},[e._m(7),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/issue"==e.url?"active":"",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/issue"}},[n("span",[e._v("分配二维码")])])],1),e._v(" "),n("li",{class:"/admin/record/issueRecord"==e.url?"active":"",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/issueRecord"}},[n("span",[e._v("二维码分配记录")])])],1),e._v(" "),n("li",{class:"/admin/record/codeStatus"==e.url?"active":"",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/codeStatus"}},[n("span",[e._v("二维码状态查询")])])],1),e._v(" "),n("li",{class:"/admin/record/codeProRecord"==e.url?"active":"",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/codeProRecord"}},[n("span",[e._v("产码记录")])])],1),e._v(" "),n("li",{class:"/admin/record/codeAll"==e.url?"active":"",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/codeAll"}},[n("span",[e._v("所有二维码")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/productList"==e.url||"/admin/record/productAdd"==e.url||"/admin/record/invite"==e.url?"treeview active":"treeview"},[e._m(8),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/productList"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/productList"}},[n("span",[e._v("产品列表")])])],1),e._v(" "),n("li",{class:"/admin/record/productAdd"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/productAdd"}},[n("span",[e._v("新增产品")])])],1),e._v(" "),n("li",{class:"/admin/record/invite"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/invite"}},[n("span",[e._v("合伙人推荐")])])],1)])]),e._v(" "),n("li",{staticClass:"treeview",class:"/admin/record/passList"==e.url||"/admin/record/passAdd"==e.url?"treeview active":"treeview"},[e._m(9),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/passList"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/passList"}},[n("span",[e._v("通道列表")])])],1),e._v(" "),n("li",{class:"/admin/record/passAdd"==e.url?"treeview active":"treeview",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/passAdd"
}},[n("span",[e._v("新增通道")])])],1)])]),e._v(" "),n("li",{class:"/admin/record/personnelList"==e.url?"treeview active":"treeview"},[e._m(10),e._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/personnelList"==e.url?"active":"",on:{click:e.refrash}},[n("router-link",{attrs:{to:"/admin/record/personnelList"}},[n("span",[e._v("员工管理")])])],1)])])])])])},staticRenderFns:[function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("交易查询")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("分润管理")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("span",{staticClass:"pull-right-container"},[n("i",{staticClass:"el-icon-arrow-left pull-right",attrs:{id:"right"}})])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("结算管理")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("渠道交易查询")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("商户管理")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("代理商管理")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("设备管理")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("产品管理")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("通道管理")])])},function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{attrs:{href:"#"}},[n("span",[e._v("员工权限管理")])])}]}},425:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("header",{staticClass:"main-header"},[e._m(0),e._v(" "),n("nav",{staticClass:"navbar navbar-static-top",staticStyle:{"margin-left":"130px"},attrs:{role:"navigation"}},[n("div",{staticClass:"navbar-custom-menu"},[n("ul",{staticClass:"nav navbar-nav"},[n("li",{staticClass:"dropdown user user-menu"},[n("div",{staticClass:"loginInfo"},[n("span",{staticClass:"name"},[e._v("超级管理员")]),e._v(" "),n("span",{staticClass:"btn btn-danger",on:{click:e.logout}},[e._v("退出")])])])])])])])},staticRenderFns:[function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a",{staticClass:"logo",staticStyle:{width:"130px"},attrs:{href:"javascript:;"}},[n("span",{staticClass:"logo-mini",staticStyle:{"font-size":"20px"}},[e._v("金开门")]),e._v(" "),n("span",{staticClass:"logo-lg"},[e._v("金开门")])])}]}}});
//# sourceMappingURL=app.ff03f35df57ec70bf48b.js.map