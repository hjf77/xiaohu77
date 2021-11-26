import{C as e,V as t,P as s,W as a}from"./vendor.d6aee48f.js";import{A as n,n as i,i as r,U as o}from"./index.5ef69292.js";var l=Object.defineProperty,c=Object.getOwnPropertyDescriptor,d=(e,t,s,a)=>{for(var n,i=a>1?void 0:a?c(t,s):t,r=e.length-1;r>=0;r--)(n=e[r])&&(i=(a?n(t,s,i):n(i))||i);return a&&i&&l(t,s,i),i};let u=class extends t{constructor(){super(...arguments),this.levelList=[],this.langList=[]}get language(){return n.language}created(){}mounted(){document.documentElement.setAttribute("lang",this.language),this.langList=Object.keys(this.$i18n.messages)}handleSetLanguage(e){document.documentElement.setAttribute("lang",e),this.$i18n.locale=e,n.SetLanguage(e).then((t=>{this.$emit("langChanged",e)})).catch((e=>{})),this.$message({showClose:!0,message:this.$tc("switchLang"),type:"success",duration:800})}};d([s({default:"14px"})],u.prototype,"size",2),u=d([e({})],u);const g={};var p=i(u,(function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("el-dropdown",{staticClass:"international",attrs:{trigger:"click"},on:{command:e.handleSetLanguage}},[s("div",{staticClass:"el-dropdown-link"},[e._t("default",[s("i",{ref:"icon",staticClass:"el-icon-setting",style:"font-size:"+e.size})])],2),s("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},e._l(e.langList,(function(t,a){return s("el-dropdown-item",{key:a,attrs:{disabled:e.language===t,command:t}},[e._v(e._s(e.$t("lang")[t]))])})),1)],1)}),[],!1,(function(e){for(let t in g)this[t]=g[t]}),"7d27a339",null,null);p.options.__file="src/components/LangSelect/index.vue";var m=p.exports,h=Object.defineProperty,f=Object.getOwnPropertyDescriptor,w=(e,t,s,a)=>{for(var n,i=a>1?void 0:a?f(t,s):t,r=e.length-1;r>=0;r--)(n=e[r])&&(i=(a?n(t,s,i):n(i))||i);return a&&i&&h(t,s,i),i};let y=class extends t{constructor(){super(...arguments),this.validateUsername=(e,t,s)=>{r(t)?s():s(new Error("Please enter the correct user name"))},this.validatePassword=(e,t,s)=>{t.length<6?s(new Error("The password can not be less than 6 digits")):s()},this.loginForm={username:"admin",password:"root123"},this.loginRules={username:[{validator:this.validateUsername,trigger:"blur"}],password:[{validator:this.validatePassword,trigger:"blur"}]},this.passwordType="password",this.loading=!1,this.otherQuery={},this.currentLang="",this.animateClass="",this.animateClassTitle=""}onRouteChange(e){const t=e.query;t&&(this.redirect=t.redirect,this.otherQuery=this.getOtherQuery(t))}get language(){return n.language}mounted(){""===this.loginForm.username?this.$refs.username.focus():""===this.loginForm.password&&this.$refs.password.focus(),this.changeLanguage(this.language)}changeLanguage(e){this.currentLang=e.toUpperCase()}showPwd(){"password"===this.passwordType?this.passwordType="":this.passwordType="password",this.$nextTick((()=>{this.$refs.password.focus()}))}handleLogin(){this.$refs.loginForm.validate((async e=>{if(!e)return!1;this.loading=!0,this.animateClass="animate__animated animate__zoomOut",this.animateClassTitle="animate__animated animate__delay-1s animate__slideInDown";try{await o.Login(this.loginForm),this.$router.push({path:this.redirect||"/",query:this.otherQuery})}catch(t){this.animateClass="",this.animateClassTitle="",this.loading=!1,console.log(t)}}))}getOtherQuery(e){return Object.keys(e).reduce(((t,s)=>("redirect"!==s&&(t[s]=e[s]),t)),{})}};w([a("$route",{immediate:!0})],y.prototype,"onRouteChange",1),y=w([e({components:{LangSelect:m}})],y);let v={redirect:"xx"};const _={};var C=i(y,(function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"login-container"},[s("el-form",{ref:"loginForm",staticClass:"login-form",attrs:{model:e.loginForm,rules:e.loginRules,autocomplete:"on","label-position":"left"}},[s("div",{staticClass:"title-container",class:e.animateClassTitle},[s("h3",{staticClass:"title"},[e._v(" "+e._s(e.$t("login.title"))+" "),s("span",[e._v(e._s(e.$t("login.sys_name")))])])]),s("div",{staticClass:"form-layer",class:e.animateClass},[s("el-form-item",{attrs:{prop:"username"}},[s("el-input",{ref:"username",attrs:{"prefix-icon":"el-icon-user",name:"username",type:"text",autocomplete:"on",placeholder:"username"},model:{value:e.loginForm.username,callback:function(t){e.$set(e.loginForm,"username",t)},expression:"loginForm.username"}})],1),s("el-form-item",{attrs:{prop:"password"}},[s("el-input",{key:e.passwordType,ref:"password",attrs:{type:e.passwordType,placeholder:"password",name:"password","prefix-icon":"el-icon-postcard",autocomplete:"on"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleLogin(t)}},model:{value:e.loginForm.password,callback:function(t){e.$set(e.loginForm,"password",t)},expression:"loginForm.password"}},[s("i",{staticClass:"el-input__icon",class:"password"===e.passwordType?"el-icon-turn-off":"el-icon-open",attrs:{slot:"suffix"},on:{click:e.showPwd},slot:"suffix"})])],1),s("el-button",{staticStyle:{width:"100%","margin-bottom":"30px"},attrs:{loading:e.loading,type:"primary",size:"big"},nativeOn:{click:function(t){return t.preventDefault(),e.handleLogin(t)}}},[e._v(" "+e._s(e.$t("login.submitText"))+" ")]),s("div",{staticClass:"tips"},[s("span",[e._v(e._s(e.$t("login.username"))+": admin/editor ")]),s("span",[e._v(e._s(e.$t("login.password"))+": root123 ")])])],1)]),s("lang-select",{staticClass:"set-language",attrs:{size:"16px"},on:{langChanged:e.changeLanguage}},[e._v(e._s(e.currentLang))])],1)}),[],!1,(function(e){for(let t in _)this[t]=_[t]}),null,null,null);C.options.__file="src/views/login/index.vue";var x=C.exports;export default x;export{v as route};
