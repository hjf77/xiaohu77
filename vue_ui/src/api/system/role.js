import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询角色列表
export function listRole(params) {
  return request({
    url: '/basic/ms/sysRole/findPage/',
    params,
    method: 'get',
  })
}

// 查询角色详细
export function getRole(roleId) {
  return request({
    url: '/basic/ms/sysRole/info/' + roleId,
    method: 'get'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/basic/ms/sysRole/addSysRole',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: '/basic/ms/sysRole/updateSysRole',
    method: 'put',
    data: data
  })
}

// 角色数据权限
export function dataScope() {
  return request({
    url: '/basic/ms/sysMenu/getMenuPermissionTree',
    method: 'put',
  })
}

// 删除角色
export function delRole(role) {
  return request({
    url: '/basic/ms/sysRole/delSysRole/',
    method: 'put',
    data: role
  })
}

// 根据机构id获取角色下拉框数据
export function getSelectOrganSysRoles(organizationId) {
  return request({
    url: '/basic/ms/sysRole/getSelectOrganSysRoles/'+organizationId,
    method: 'get',
  })
}

// 根据角色id获取菜单权限数据
export function getPermissionByRoleId(roleId) {
  return request({
    url: '/basic/ms/sysRole/getRolePermissionButtons?roleId='+roleId,
    method: 'POST',
  })
}
