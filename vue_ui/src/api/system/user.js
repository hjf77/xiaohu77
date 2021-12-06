import request from '@/utils/request'


// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/basic/ms/sysUser/updatePass',
    method: 'put',
    data
  })
}


