import Vue from "vue";
import axios from "axios";
import { getToken } from '@/utils/auth'
import { Message } from 'element-ui'
import qs from "qs";
// Admin-Token


let errorCode  = {
    401: '认证失败，无法访问系统资源',
    403: '当前操作没有权限',
    404: '访问资源不存在',
    default: '系统未知错误,请反馈给管理员'
}

let setting={
    baseURL : process.env.VUE_APP_BASE_API, //基础url
    tokenField:'Authorization',//header中的token字段
    token:function(){
        return getToken();//token值
    },
    errorMsgAlert:function(_msg){
      Message({
        message: _msg,
        type: 'error',
        showClose: true,
        duration: 3 * 1000
      })
      console.log(_msg);
    },
    loginRouter:'/login',//登录跳转
    successCode:200,//成功的code
    authErrorCode:403,//没有权限的code
    tokenTimeoutCode:401,//token过期code
    paramErrorCode:400,
    systemErrorCode:500,
    dataField:'data',//数据的字段
    msgField:'message',//后台给前端传的消息字段
    codeField:'code',//code字段
    timeout:30000,//过期时间
}

// axios.defaults.headers["Content-Type"] = "application/json;charset=utf-8";
// 创建axios实例
const request = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: setting.baseURL,
    // 超时
    timeout: setting.timeout
});

// request拦截器
request.interceptors.request.use(config => {
    config.headers[setting.tokenField] = setting.token() // 让每个请求携带自定义token 请根据实际情况自行修改
    /*
        todo 判断请求url 如果是 get开头则设置为get post put delete 则设置为 这些 如果没指定则为get
     */
  config.headers['Content-Type']='application/json;charset=utf-8'
    /*if(config.data && config.data.useJson){
      config.headers['Content-Type']='application/json;charset=utf-8'
      delete config.data.useJson;
      config.data = JSON.stringify(config.data);
    }else{
      config.headers['Content-Type']='application/x-www-form-urlencoded'
      config.data = qs.stringify(config.data)
    }*/
    return config
}, error => {
    Promise.reject(error)
});


// 响应拦截器
request.interceptors.response.use(
    res => {
        const code = res.data[setting.codeField];
        //也没有message也没有code字段代表直接返回数据的老接口
        if(!res.data[setting.codeField] && !res.data[setting.msgField]){
          return res.data;
        }
        // 获取错误信息
        const msg = errorCode[code] || res.data[setting.msgField] || errorCode["default"];
        //清除缓存跳转到登录页
        if (code === setting.tokenTimeoutCode) {
            localStorage.clear();
            sessionStorage.clear();
            window.location.reload();
            return;
        }
        else if (code === setting.paramErrorCode || code===setting.systemErrorCode || code !== setting.successCode) {
            setting.errorMsgAlert(msg);
            return Promise.reject(new Error(msg));
        } else {
            return res.data;
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
            if (code == setting.tokenTimeoutCode) {
              localStorage.clear();
              sessionStorage.clear();
              window.location.reload();
              return;
            }
            message = "系统接口" + message.substr(message.length - 3) + "异常";
        }
        setting.errorMsgAlert(message);
        return Promise.reject(error);
    }
);
export default request;
