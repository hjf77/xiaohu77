import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'
import qs from 'qs';

// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 10000
})
// request拦截器
service.interceptors.request.use(
  config => {
    if (getToken()) {
      config.headers['Authorization'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    if(config.data && config.data.useJson){
      config.headers['Content-Type']='application/json;charset=utf-8'
      config.data = JSON.stringify(config.data);
    }else{
      config.headers['Content-Type']='application/x-www-form-urlencoded'
      config.data = qs.stringify(config.data)
    }
    return config
  },
  error => {
    console.log(error)
    Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(res => {
    if(res.data instanceof Array || !res.data.code ){
      return res.data
    }
    const code = res.data.code
    if (code === 403) {
      MessageBox.confirm(
        '登录状态已过期，您可以继续留在该页面，或者重新登录',
        '系统提示',
        {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        store.dispatch('LogOut').then(() => {
          location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      })
    } else if (code !== 200) {
      Notification.error({
        title: res.data.message
      })
      return Promise.reject('error')
    } else {
      return res.data
    }
  },
  error => {
    let { message } = error;
    if (message == "Network Error") {
      message = "网络错误";
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    } else if (message.includes("Request failed with status code")) {
      let code = message.substr(message.length - 3);
      if (code == 401) {
        localStorage.clear();
        sessionStorage.clear();
        window.location.reload();
        return;
      }
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
