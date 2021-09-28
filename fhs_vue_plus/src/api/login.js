import request from '@/utils/request'

// 登录方法
export function login(userLoginName, password, identifyCode, uuid) {
  const data = {
    userLoginName,
    password,
    identifyCode,
    uuid
  }
  return request({
    url: '/vueLogin',
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/ms/getUserForVue',
    method: 'get'
  })
}

// 退出方法
export function logout(token) {
  return request({
    url: `/logoutForVue?token=${token}`,
    method: 'GET'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/defaultKaptchaForVue',
    method: 'get'
  })
}

// 外协人员登录
export function stuLogin(userLoginName, password, identifyCode, uuid) {
  const data = {
    userLoginName,
    password,
    identifyCode,
    uuid
  }
  return request({
    url: '/stu/vueLogin',
    method: 'post',
    data: data
  })
}

