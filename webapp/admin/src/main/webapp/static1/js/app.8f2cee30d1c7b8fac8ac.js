webpackJsonp([3,1],{0:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}var a=n(4),i=r(a),s=n(227),o=r(s);n(240);var c=n(43),u=r(c),d=n(139),l=r(d),f=n(394),v=r(f);n(135),n(136);var m=n(137),p=r(m),_=n(138),h=r(_),g=n(306),y=r(g),C=n(307),w=r(C),b=n(304),x=r(b),A=n(305),k=r(A),S=n(302),E=r(S),$=n(303),M=r($);i.default.use(o.default),i.default.use(v.default),i.default.http.interceptors.push(function(t,e){e(function(t){var e=t.status,n=t.body;return 200==e?n.code==-100||n.code==-200||n.code==-201?(u.default.commit("LOGIN_SHOW"),l.default.push("/admin/login")):1!=n.code?(t.status=500,t.statusMessage=n.msg||"系统异常",t.statusText="Internal Server Error",t.ok=!1):t.data=n.result:t.statusMessage="系统异常",t})}),i.default.use(p.default),i.default.use(h.default),l.default.beforeEach(function(t,e,n){n()}),i.default.component("app-header",y.default),i.default.component("app-menu",w.default),i.default.component("app-content",x.default),i.default.component("app-footer",k.default),i.default.component("app-aside",E.default),i.default.component("app-aside-bg",M.default);new i.default({store:u.default,router:l.default}).$mount("#app")},6:function(t,e,n){var r,a;n(266),r=n(109);var i=n(369);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-61e7739a",t.exports=r},43:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var a=n(4),i=r(a),s=n(396),o=r(s),c=n(144),u=r(c),d=n(143),l=r(d),f=n(142),v=r(f);i.default.use(o.default),e.default=new o.default.Store({modules:{title:u.default,message:l.default,login:v.default}})},84:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var a=n(6),i=r(a);e.default={name:"main",components:{Message:i.default}}},92:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},93:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},94:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},95:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册"}}}},96:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{}},methods:{logout:function(){this.$http.post("/admin/user/logout").then(function(){this.$router.push("/admin/login")},function(t){console.log(t)})}}}},97:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"collection",data:function(){return{msg:"注册",url:""}},created:function(){this.$data.url=location.pathname},methods:{refrash:function(){location.reload()}}}},108:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var a=n(6),i=r(a);e.default={name:"login",components:{Message:i.default},data:function(){return{msg:"登录",login:!1,username:"",password:""}},created:function(){this.$store.commit("LOGIN_SHOW")},methods:{submit:function(){this.$http.post("/admin/user/login",{username:this.$data.username,password:this.$data.password}).then(function(){this.$store.commit("LOGIN_HIDE"),this.$router.push({path:"/admin/record/newDeal"})},function(t){this.$store.commit("MESSAGE_ACCORD_SHOW",{text:t.statusMessage})})}},computed:{$$login:function(){return this.$store.state.login.ctrl}}}},109:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var a=n(4);r(a);e.default={name:"message",data:function(){return{delay:!1,accord:!1}},methods:{know:function(){this.$store.commit("MESSAGE_ACCORD_HIDE")}},computed:{$$message:function(){return this.$store.state.message.message},$$delay:function(){return this.$store.state.message.delay},$$accord:function(){return this.$store.state.message.accord},$$text:function(){return this.$store.state.message.text}}}},135:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}var a=n(4),i=r(a);i.default.directive("focus",{inserted:function(t){t.focus()}})},136:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}var a=n(4),i=r(a);i.default.filter("test",function(t){return 1==t})},137:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={install:function(t){t.directive("keyboard",{inserted:function(t,e){console.log(e);var n=e.arg,r=document.createElement("input");r.type="text",r.setAttribute("readOnly","true"),n&&n.input?r.className=n.input:r.style.border="1px solid #000",r.onclick=function(){console.log("唤起键盘")};var a=function(t){if("delete"==t)r.value=r.value.substr(0,r.value.length-1);else{if(r.value.indexOf(".")!=-1){var e=r.value.indexOf("."),n=r.value.length;if(n-e>2)return!1}r.value+=t}};t.appendChild(r);var i=document.createElement("div");n&&n.keyboard?i.className=n.keyboard:(i.style.width="100%",i.style.height="280px",i.style.position="fixed",i.style.left="0",i.style.bottom="0",i.style.borderTop="1px solid #000");var s="width:33.33%;height:70px;float:left;text-align:center;line-height:70px;",o=document.createElement("div"),c=document.createElement("div");c.innerHTML="1",c.style.cssText=s,c.onclick=function(){a("1")};var u=document.createElement("div");u.innerHTML="2",u.style.cssText=s,u.onclick=function(){a("2")};var d=document.createElement("div");d.innerHTML="3",d.style.cssText=s,d.onclick=function(){a("3")};var l=document.createElement("div");l.innerHTML="4",l.style.cssText=s,l.onclick=function(){a("4")};var f=document.createElement("div");f.innerHTML="5",f.style.cssText=s,f.onclick=function(){a("5")};var v=document.createElement("div");v.innerHTML="6",v.style.cssText=s,v.onclick=function(){a("6")};var m=document.createElement("div");m.innerHTML="7",m.style.cssText=s,m.onclick=function(){a("7")};var p=document.createElement("div");p.innerHTML="8",p.style.cssText=s,p.onclick=function(){a("8")};var _=document.createElement("div");_.innerHTML="9",_.style.cssText=s,_.onclick=function(){a("9")};var h=document.createElement("div");h.innerHTML=".",h.style.cssText=s,h.onclick=function(){a(".")};var g=document.createElement("div");g.innerHTML="0",g.style.cssText=s,g.onclick=function(){a("0")};var y=document.createElement("div");y.innerHTML="x",y.style.cssText=s,y.onclick=function(){a("delete")},i.appendChild(c),i.appendChild(u),i.appendChild(d),i.appendChild(l),i.appendChild(f),i.appendChild(v),i.appendChild(m),i.appendChild(p),i.appendChild(_),i.appendChild(h),i.appendChild(g),i.appendChild(y),i.appendChild(o);var C=document.createElement("div");i.appendChild(C),t.appendChild(i)}})}}},138:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var a=n(147),i=r(a),s=n(43),o=r(s);e.default={install:function(t){var e={joint:function(t){for(var e in t)if("object"==(0,i.default)(t[e])){for(var n=0;n<t[e].length;n++)if("name"==e){if(!this[e](t[e][n].data,t[e][n].text))return!1}else if(!this[e](t[e][n]))return!1}else if(!this[e](t[e]))return!1;return!0},name:function(t,e){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,15}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:(e?e:"名称")+"长度限制1-15个字"}),!1)},address:function(t){return!!/^[\u4e00-\u9fa5_a-zA-Z0-9]{1,35}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"地址长度限制1-35个字"}),!1)},phone:function(t){return!!/^1(3|4|5|7|8)\d{9}$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的手机号码"}),!1)},code:function(t){return 6==t.length||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的验证码"}),!1)},bankNo:function(t){return!!/^(\d{16}|\d{19})$/.test(t)||(o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的银行卡号"}),!1)},idCard:function(t){var e=/^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[Xx])$)$/;if(e.test(t)){if(18==t.length){for(var n=new Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2),r=new Array(1,0,10,9,8,7,6,5,4,3,2),a=0,i=0;i<17;i++)a+=t.substring(i,i+1)*n[i];var s=a%11,c=t.substring(17);if(2==s){if("X"==c||"x"==c)return!0;o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}else{if(c==r[s])return!0;o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}}}else o.default.commit("MESSAGE_ACCORD_SHOW",{text:"请输入正确的身份证号"})}};t.__validate=e,t.prototype._$validate=e}}},139:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var a=n(4),i=r(a),s=n(395),o=r(s),c=n(141),u=r(c);i.default.use(o.default),e.default=new o.default({mode:"history",routes:u.default,scrollBehavior:function(t,e,n){}})},140:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var a=n(294),i=r(a),s=function(t){return n.e(0,function(){return t(n(311))})},o=function(t){return n.e(0,function(){return t(n(310))})},c=function(t){return n.e(0,function(){return t(n(338))})},u=function(t){return n.e(0,function(){return t(n(337))})},d=function(t){return n.e(0,function(){return t(n(336))})},l=function(t){return n.e(0,function(){return t(n(322))})},f=function(t){return n.e(0,function(){return t(n(328))})},v=function(t){return n.e(0,function(){return t(n(332))})},m=function(t){return n.e(0,function(){return t(n(329))})},p=function(t){return n.e(0,function(){return t(n(331))})},_=function(t){return n.e(0,function(){return t(n(299))})},h=function(t){return n.e(0,function(){return t(n(301))})},g=function(t){return n.e(0,function(){return t(n(300))})},y=function(t){return n.e(0,function(){return t(n(295))})},C=function(t){return n.e(0,function(){return t(n(296))})},w=function(t){return n.e(0,function(){return t(n(298))})},b=function(t){return n.e(0,function(){return t(n(297))})},x=function(t){return n.e(0,function(){return t(n(309))})},A=function(t){return n.e(0,function(){return t(n(41))})},k=function(t){return n.e(0,function(){return t(n(312))})},S=function(t){return n.e(0,function(){return t(n(41))})},E=function(t){return n.e(0,function(){return t(n(327))})},$=function(t){return n.e(0,function(){return t(n(41))})},M=function(t){return n.e(0,function(){return t(n(320))})},O=function(t){return n.e(0,function(){return t(n(321))})},L=function(t){return n.e(0,function(){return t(n(323))})},P=function(t){return n.e(0,function(){return t(n(324))})},D=function(t){return n.e(0,function(){return t(n(314))})},R=function(t){return n.e(0,function(){return t(n(315))})},T=function(t){return n.e(0,function(){return t(n(316))})},F=function(t){return n.e(0,function(){return t(n(313))})},H=function(t){return n.e(0,function(){return t(n(335))})},j=function(t){return n.e(0,function(){return t(n(308))})},W=function(t){return n.e(0,function(){return t(n(330))})},I=function(t){return n.e(0,function(){return t(n(333))})},G=function(t){return n.e(0,function(){return t(n(340))})},N=function(t){return n.e(0,function(){return t(n(339))})},Q=function(t){return n.e(0,function(){return t(n(342))})},B=function(t){return n.e(0,function(){return t(n(341))})},Y=function(t){return n.e(0,function(){return t(n(319))})},z=function(t){return n.e(0,function(){return t(n(318))})},X=function(t){return n.e(0,function(){return t(n(334))})},Z=function(t){return n.e(0,function(){return t(n(325))})},J=function(t){return n.e(0,function(){return t(n(326))})};e.default={path:"/admin/record",redirect:"/admin/record/deal",component:i.default,children:[{path:"storeAuditHSY",name:"StoreAuditHSY",component:W},{path:"t1Audit",name:"T1Audit",component:I},{path:"newDeal",name:"NewDealQuery",component:G},{path:"newDealDet",name:"NewDealDet",component:N},{path:"newWithdrawalQuery",name:"NewWithdrawalQuery",component:Q},{path:"newWithdrawalDet",name:"NewWithdrawalDet",component:B},{path:"deal",name:"DealQuery",component:s},{path:"dealDet",name:"DealDet",component:o},{path:"withdrawal",name:"WithdrawalQuery",component:c},{path:"withdrawalAudit",name:"WithdrawalAudit",component:d},{path:"withdrawalDet",name:"WithdrawalDet",component:u},{path:"payQuery",name:"PayQuery",component:l},{path:"storeAccount",name:"StoreAccount",component:f},{path:"storeList",name:"StoreList",component:v},{path:"storeAuditList",name:"StoreAuditList",component:p},{path:"storeAudit",name:"StoreAudit",component:m},{path:"agentList",name:"AgentList",component:_},{path:"agentListFir",name:"AgentListFir",component:g},{path:"agentListSec",name:"AgentListSec",component:h},{path:"agentAccount",name:"AgentAccount",component:y},{path:"agentAdd",name:"AgentAdd",component:C},{path:"agentAddPro",name:"AgentAddPro",component:w},{path:"agentAddBase",name:"AgentAddBase",component:b},{path:"companyProfit",name:"CompanyProfit",component:x},{path:"companyProfitDet",name:"CompanyProfitDet",component:A},{path:"firProfit",name:"FirProfit",component:k},{path:"firProfitDet",name:"FirProfitDet",component:S},{path:"secProfit",name:"SecProfit",component:E},{path:"secProfitDet",name:"SecProfitDet",component:$},{path:"passAdd",name:"PassAdd",component:M},{path:"passList",name:"PassList",component:O},{path:"productAdd",name:"ProductAdd",component:L},{path:"productList",name:"ProductList",component:P},{path:"issue",name:"Issue",component:D},{path:"issue1",name:"Issue1",component:R},{path:"issueSuccess",name:"IssueSuccess",component:T},{path:"invite",name:"Invite",component:F},{path:"upgrade",name:"Upgrade",component:H},{path:"codeStatus",name:"CodeStatus",component:j},{path:"orderQuery",name:"OrderQuery",component:Y},{path:"orderDetail",name:"OrderDetail",component:z},{path:"tradeQuery",name:"TradeQuery",component:X},{path:"profitAccount",name:"ProfitAccount",component:Z},{path:"profitAccountDet",name:"ProfitAccountDet",component:J}]}},141:function(t,e,n){"use strict";function r(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var a=n(140),i=r(a),s=n(317),o=r(s);e.default=[{path:"/admin",redirect:"/admin/record"},{path:"/admin/login",component:o.default},i.default]},142:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={ctrl:!0},r={LOGIN_SHOW:function(t){t.ctrl=!0},LOGIN_HIDE:function(t){t.ctrl=!1}};e.default={state:n,mutations:r}},143:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={message:!1,delay:!1,accord:!1,text:""},r={MESSAGE_DELAY_SHOW:function(t,e){t.message=!0,t.delay=!0,t.text=e.text,setTimeout(function(){t.message=!1,t.delay=!1},3e3)},MESSAGE_DELAY_HIDE:function(t){t.message=!1,t.delay=!1},MESSAGE_ACCORD_SHOW:function(t,e){t.message=!0,t.accord=!0,t.text=e.text},MESSAGE_ACCORD_HIDE:function(t){t.message=!1,t.accord=!1}};e.default={state:n,mutations:r}},144:function(t,e){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n={title:"钱包++",router:{ticketReserve:"车票预定",ticketRob:"抢票",ticketRobOrder:"确认订单",ticketRobDetail:"抢票详情",ticketPrivate:"私人定制",ticketOrder:"我的订单",ticketSubmitOrder:"提交订单",ticketSureOrder:"确认订单",ticketPayOrder:"订单详情",ticketTrain:"北京 → 深圳",ticketContacts:"常用联系人",ticketLogin:"12306登录",firstAdd:"确认订单",ticketRefundDetail:"订单详情",secondAdd:"确认订单",ticketAddChild:"常用联系人",ticketRefundSuccess:"出票成功"}},r={TITLE_CHANGE:function(t,e){"ticketTrain"==e.name&&(t.router.ticketTrain=e.formName+" → "+e.toName),t.title=t.router[e.name]}};e.default={state:n,mutations:r}},240:function(t,e){},242:function(t,e){},266:function(t,e){},271:function(t,e){},283:function(t,e){},285:function(t,e){},288:function(t,e){},294:function(t,e,n){var r,a;n(271),r=n(84);var i=n(375);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,t.exports=r},302:function(t,e,n){var r,a;r=n(92);var i=n(371);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,t.exports=r},303:function(t,e,n){var r,a;r=n(93);var i=n(389);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,t.exports=r},304:function(t,e,n){var r,a;n(283),r=n(94);var i=n(387);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-a76ae156",t.exports=r},305:function(t,e,n){var r,a;r=n(95);var i=n(364);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,t.exports=r},306:function(t,e,n){var r,a;n(288),r=n(96);var i=n(393);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-fff64b98",t.exports=r},307:function(t,e,n){var r,a;n(285),r=n(97);var i=n(390);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-c2ed6b9a",t.exports=r},317:function(t,e,n){var r,a;n(242),r=n(108);var i=n(344);a=r=r||{},"object"!=typeof r.default&&"function"!=typeof r.default||(a=r=r.default),"function"==typeof a&&(a=a.options),a.render=i.render,a.staticRenderFns=i.staticRenderFns,a._scopeId="data-v-0e0a7688",t.exports=r},344:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return t.$$login?n("div",{attrs:{id:"login"}},[n("div",{staticClass:"login-space"},[n("div",{staticClass:"login-title"},[t._v("登录")]),t._v(" "),n("div",{staticClass:"flexBox group"},[n("div",{staticClass:"text"},[t._v("用户名")]),t._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:t.username,expression:"username"}],staticClass:"ipt",attrs:{type:"text",placeholder:"请输入用户名"},domProps:{value:t._s(t.username)},on:{input:function(e){e.target.composing||(t.username=e.target.value)}}})]),t._v(" "),n("div",{staticClass:"flexBox group"},[n("div",{staticClass:"text"},[t._v("密码")]),t._v(" "),n("input",{directives:[{name:"model",rawName:"v-model",value:t.password,expression:"password"}],staticClass:"ipt",attrs:{type:"password",placeholder:"请输入密码"},domProps:{value:t._s(t.password)},on:{input:function(e){e.target.composing||(t.password=e.target.value)}}})]),t._v(" "),n("div",{staticClass:"submit",on:{click:t.submit}},[t._v("登录")])]),t._v(" "),n("message")],1):t._e()},staticRenderFns:[]}},364:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement;t._self._c||e;return t._m(0)},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("footer",{staticClass:"main-footer",staticStyle:{"margin-left":"130px"}},[n("div",{staticClass:"pull-right hidden-xs"},[t._v("\n    Anything you want\n  ")]),t._v(" "),n("strong",[t._v("Copyright © 2016 "),n("a",{attrs:{href:"#"}},[t._v("Company")]),t._v(".")]),t._v(" All rights reserved.\n")])}]}},369:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("transition",{attrs:{name:"fade"}},[t.$$message?n("div",{staticClass:"main",on:{click:t.know}},[n("div",{directives:[{name:"show",rawName:"v-show",value:t.$$accord,expression:"$$accord"}],staticClass:"col-md-3 content"},[n("div",{staticClass:"box box-danger"},[n("div",{staticClass:"box-header with-border"},[n("h3",{staticClass:"box-title"},[t._v("提示")])]),t._v(" "),n("div",{staticClass:"box-body"},[t._v("\n          "+t._s(t.$$text)+"\n        ")])])]),t._v(" "),n("div",{directives:[{name:"show",rawName:"v-show",value:t.$$delay,expression:"$$delay"}],staticClass:"col-md-3 content"},[n("div",{staticClass:"box box-danger"},[n("div",{staticClass:"box-header with-border"},[n("h3",{staticClass:"box-title"},[t._v("提示")])]),t._v(" "),n("div",{staticClass:"box-body"},[t._v("\n          "+t._s(t.$$text)+"\n        ")])])])]):t._e()])},staticRenderFns:[]}},371:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("aside",{staticClass:"control-sidebar control-sidebar-dark"},[t._m(0),t._v(" "),n("div",{staticClass:"tab-content"},[t._m(1),t._v(" "),n("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-stats-tab"}},[t._v("Stats Tab Content")]),t._v(" "),n("div",{staticClass:"tab-pane",attrs:{id:"control-sidebar-settings-tab"}},[n("form",{attrs:{method:"post"}},[n("h3",{staticClass:"control-sidebar-heading"},[t._v("General Settings")]),t._v(" "),n("div",{staticClass:"form-group"},[n("label",{staticClass:"control-sidebar-subheading"},[t._v("\n            Report panel usage\n            "),n("input",{staticClass:"pull-right",attrs:{type:"checkbox",checked:""},domProps:{checked:!0}})]),t._v(" "),n("p",[t._v("\n            Some information about this general settings option\n          ")])])])])])])},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("ul",{staticClass:"nav nav-tabs nav-justified control-sidebar-tabs"},[n("li",{staticClass:"active"},[n("a",{attrs:{href:"#control-sidebar-home-tab","data-toggle":"tab"}},[n("i",{staticClass:"fa fa-home"})])]),t._v(" "),n("li",[n("a",{attrs:{href:"#control-sidebar-settings-tab","data-toggle":"tab"}},[n("i",{staticClass:"fa fa-gears"})])])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"tab-pane active",attrs:{id:"control-sidebar-home-tab"}},[n("h3",{staticClass:"control-sidebar-heading"},[t._v("Recent Activity")]),t._v(" "),n("ul",{staticClass:"control-sidebar-menu"},[n("li",[n("a",{attrs:{href:"javascript::;"}},[n("i",{staticClass:"menu-icon fa fa-birthday-cake bg-red"}),t._v(" "),n("div",{staticClass:"menu-info"},[n("h4",{staticClass:"control-sidebar-subheading"},[t._v("Langdon's Birthday")]),t._v(" "),n("p",[t._v("Will be 23 on April 24th")])])])])]),t._v(" "),n("h3",{staticClass:"control-sidebar-heading"},[t._v("Tasks Progress")]),t._v(" "),n("ul",{staticClass:"control-sidebar-menu"},[n("li",[n("a",{attrs:{href:"javascript::;"}},[n("h4",{staticClass:"control-sidebar-subheading"},[t._v("\n              Custom Template Design\n              "),n("span",{staticClass:"pull-right-container"},[n("span",{staticClass:"label label-danger pull-right"},[t._v("70%")])])]),t._v(" "),n("div",{staticClass:"progress progress-xxs"},[n("div",{staticClass:"progress-bar progress-bar-danger",staticStyle:{width:"70%"}})])])])])])}]}},375:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"main"}},[n("router-view"),t._v(" "),n("message")],1)},staticRenderFns:[]}},387:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"content-wrapper",staticStyle:{"margin-left":"130px"}},[n("section",{staticClass:"content",staticStyle:{overflow:"hidden",padding:"0"}},[n("router-view")],1)])},staticRenderFns:[]}},389:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"control-sidebar-bg"})},staticRenderFns:[]}},390:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("aside",{staticClass:"main-sidebar",staticStyle:{width:"130px"}},[n("section",{staticClass:"sidebar"},[n("ul",{staticClass:"sidebar-menu nav nav-pills nav-stacked"},[n("li",{class:"/admin/record/newDeal"==t.url||"/admin/record/payQuery"==t.url||"/admin/record/newWithdrawalQuery"==t.url||"/admin/record/t1Audit"==t.url?"treeview active":"treeview"},[t._m(0),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/newDeal"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/newDeal"}},[n("span",[t._v("交易查询")])])],1),t._v(" "),n("li",{class:"/admin/record/payQuery"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/payQuery"}},[n("span",[t._v("支付查询")])])],1),t._v(" "),n("li",{class:"/admin/record/newWithdrawalQuery"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/newWithdrawalQuery"}},[n("span",[t._v("打款查询")])])],1),t._v(" "),n("li",{class:"/admin/record/t1Audit"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/t1Audit"}},[n("span",[t._v("T1结算审核")])])],1)])]),t._v(" "),n("li",{class:"/admin/record/storeList"==t.url||"/admin/record/storeAuditList"==t.url?"treeview active":"treeview"},[t._m(1),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/storeList"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/storeList"}},[n("span",[t._v("商户列表")])])],1),t._v(" "),n("li",{class:"/admin/record/storeAuditList"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/storeAuditList"}},[n("span",[t._v("待审核商户")])])],1)])]),t._v(" "),n("li",{class:"/admin/record/agentListFir"==t.url||"/admin/record/agentListSec"==t.url?"treeview active":"treeview"},[t._m(2),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/agentListFir"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/agentListFir"}},[n("span",[t._v("一级代理商")])])],1),t._v(" "),n("li",{class:"/admin/record/agentListSec"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/agentListSec"}},[n("span",[t._v("二级代理商")])])],1)])]),t._v(" "),n("li",{class:"/admin/record/companyProfit"==t.url||"/admin/record/firProfit"==t.url||"/admin/record/secProfit"==t.url||"/admin/record/profitAccount"==t.url?"treeview active":"treeview"},[t._m(3),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/companyProfit"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/companyProfit"}},[n("span",[t._v("公司分润")])])],1),t._v(" "),n("li",{class:"/admin/record/firProfit"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/firProfit"}},[n("span",[t._v("一级代理商分润")])])],1),t._v(" "),n("li",{class:"/admin/record/secProfit"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/secProfit"}},[n("span",[t._v("二级代理商分润")])])],1)])]),t._v(" "),n("li",{class:"/admin/record/productList"==t.url||"/admin/record/productAdd"==t.url||"/admin/record/invite"==t.url?"treeview active":"treeview"},[t._m(4),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/productList"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/productList"}},[n("span",[t._v("产品列表")])])],1),t._v(" "),n("li",{class:"/admin/record/productAdd"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/productAdd"}},[n("span",[t._v("新增产品")])])],1),t._v(" "),n("li",{class:"/admin/record/invite"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/invite"}},[n("span",[t._v("合伙人推荐")])])],1)])]),t._v(" "),n("li",{staticClass:"treeview",class:"/admin/record/passList"==t.url||"/admin/record/passAdd"==t.url?"treeview active":"treeview"},[t._m(5),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/passList"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/passList"}},[n("span",[t._v("通道列表")])])],1),t._v(" "),n("li",{class:"/admin/record/passAdd"==t.url?"treeview active":"treeview",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/passAdd"}},[n("span",[t._v("新增通道")])])],1)])]),t._v(" "),n("li",{class:"/admin/record/issue1"==t.url||"/admin/record/issue"==t.url||"/admin/record/codeStatus"==t.url?"treeview active":"treeview"},[t._m(6),t._v(" "),n("ul",{staticClass:"treeview-menu"},[n("li",{class:"/admin/record/issue1"==t.url?"active":"",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/issue1"}},[n("span",[t._v("按个数分配")])])],1),t._v(" "),n("li",{class:"/admin/record/issue"==t.url?"active":"",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/issue"}},[n("span",[t._v("按码段分配")])])],1),t._v(" "),n("li",{class:"/admin/record/codeStatus"==t.url?"active":"",on:{click:t.refrash}},[n("router-link",{attrs:{to:"/admin/record/codeStatus"}},[n("span",[t._v("二维码状态查询")])])],1)])])])])])},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("span",[t._v("交易管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("span",[t._v("商户管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("span",[t._v("代理商管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("span",[t._v("分润管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("span",[t._v("产品管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("span",[t._v("通道管理")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{attrs:{href:"#"}},[n("span",[t._v("分配二维码")])])}]}},393:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("header",{staticClass:"main-header"},[t._m(0),t._v(" "),n("nav",{staticClass:"navbar navbar-static-top",staticStyle:{"margin-left":"130px"},attrs:{role:"navigation"}},[n("div",{staticClass:"navbar-custom-menu"},[n("ul",{staticClass:"nav navbar-nav"},[n("li",{staticClass:"dropdown user user-menu"},[n("div",{staticClass:"loginInfo"},[n("span",{staticClass:"name"},[t._v("超级管理员")]),t._v(" "),n("span",{staticClass:"btn btn-danger",on:{click:t.logout}},[t._v("退出")])])])])])])])},staticRenderFns:[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("a",{staticClass:"logo",staticStyle:{width:"130px"},attrs:{href:"javascript:;"}},[n("span",{staticClass:"logo-mini",staticStyle:{"font-size":"20px"}},[t._v("金开门")]),t._v(" "),n("span",{staticClass:"logo-lg"},[t._v("金开门")])])}]}}});
//# sourceMappingURL=app.8f2cee30d1c7b8fac8ac.js.map