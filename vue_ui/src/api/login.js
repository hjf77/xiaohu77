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
    url: '/basic/login',
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/basic/ms/getUser',
    method: 'get'
  })
}

// 退出方法
export function logout(token) {
  return request({
    url: `/basic/logout?token=${token}`,
    method: 'GET'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/basic/defaultKaptcha',
    method: 'get'
  })
}


