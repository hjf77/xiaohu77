import '@babel/polyfill'
import Vue from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'
import Cookies from 'js-cookie'
// import axios from 'axios';
import request from '@/lib/utils/request'
import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import Element from 'element-ui'
import './assets/styles/element-variables.scss'
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import { Message } from 'element-ui'

import permission from './directive/permission'

import './assets/icons' // icon
import './permission' // permission control
import {
  parseTime,
  resetForm,
  addDateRange,
  selectDictLabel,
  download,
  handleTree
} from "@/utils/ruoyi";
import Pagination from "@/components/Pagination/index.vue";
import BaseContainer from "@/components/BaseContainer"
import renderFun from "@/lib/components/render";
import eIconPicker from 'e-icon-picker';
import "e-icon-picker/lib/symbol.js"; //基本彩色图标库
import 'e-icon-picker/lib/index.css'; // 基本样式，包含基本图标
import 'font-awesome/css/font-awesome.min.css'; //font-awesome 图标库
import 'element-ui/lib/theme-chalk/icon.css'; //element-ui 图标库
import VueClipboard from 'vue-clipboard2';

//粘贴插件
Vue.use(VueClipboard);
Vue.use(eIconPicker, {FontAwesome: true, ElementUI: true, eIcon: true, eIconSymbol: true});
Vue.component(renderFun)
Vue.use(BaseContainer)
Vue.config.productionTip = false
const on = Vue.prototype.$on
Vue.prototype.$on = function (event, func) {
  let timer
  let newFunc = func
  if (event === "click") {
    newFunc = function () {
      clearTimeout(timer)
      timer = setTimeout(function () {
        func.apply(this, arguments)
      }, 300)
    }
  }
  on.call(this, event, newFunc)
}


/*import moment from 'moment'//导入文件
Vue.prototype.$moment = moment;//赋值使用*/

Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree
// Vue.prototype.$pagexRequest = axios;
Vue.prototype.$pagexRequest = request;
Vue.prototype.msgSuccess = function (msg) {
  Message({
    message: msg,
    type: 'success',
    showClose: true,
    duration: 3 * 1000
  })
}

Vue.prototype.msgError = function (msg) {
  Message({
    message: msg,
    type: 'error',
    showClose: true,
    duration: 3 * 1000
  })
}

Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}

// 全局组件挂载
Vue.component('Pagination', Pagination)

Vue.use(permission)

Vue.prototype.$EventBus = new Vue();

import xhbEventBus from './utils/eventBusPlugin'

Vue.use(xhbEventBus)
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})


import VeeElement from 'vee-element'
import VeeValidate,{ Validator } from 'vee-validate'
import zh_CN from "vee-validate/dist/locale/zh_CN";
import veeValidator from '@/utils/vee-validator'
import {range,greaterThanZero,mobile} from '@/utils/vee-customize'
const rules = {}
const options = {}
Vue.use(Element);

//vee，自定义校验规则；
Validator.extend('range', range);
Validator.extend('greaterThanZero', greaterThanZero);
//vee，自定义校验规则；
Validator.extend('mobile', mobile);


const validator = new VeeValidate.Validator(rules, options)
validator.localize("zh_CN", zh_CN);


import("@/lib/utils/element-reset.js")




//自定义vee提示信息
validator.localize(veeValidator);
Vue.use(VeeElement, validator, false)

import formCreate from '@form-create/element-ui';
Vue.use(formCreate);
new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App)
}).$mount("#app");
