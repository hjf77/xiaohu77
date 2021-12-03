import request from '@/utils/request'

// 查询菜单列表
export function listMenu(data) {
  return request({
    url: '/basic/ms/sysMenu/tree',
    method: 'post',
    data
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: '/basic/ms/sysMenu/' + menuId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/basic/ms/sysMenu',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/basic/ms/sysMenu',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: '/basic/ms/sysMenu/' + menuId,
    method: 'delete'
  })
}

// 子系统 下拉数据
export function getSystemOptions() {
  return request({
    url: '/basic/ms/settMsSystem/findList',
    method: 'get'
  })
}

//查询 权限菜单列表
export function getPermissionList(menuId) {
  return request({
    url: '/basic/ms/sysMenuPermission/findList?menuId=' + menuId,
    method: 'get'
  })
}

//查询 权限菜单
export function getPermission(permissionId) {
  return request({
    url: '/basic/ms/sysMenuPermission/' + permissionId,
    method: 'get'
  })
}

// 批量增加增删
export function addAllPermission(menuId) {
  return request({
    url: '/basic/ms/sysMenuPermission/addBaseMenuBatch?menuId=' + menuId,
    method: 'post',
  })
}

// 新增权限
export function addPermission(data) {
  return request({
    url: '/basic/ms/sysMenuPermission',
    method: 'POST',
    data: data
  })
}

// 修改权限
export function updatePermission(data) {
  return request({
    url: '/basic/ms/sysMenuPermission',
    method: 'PUT',
    data: data
  })
}

// 删除权限
export function delPermission(permissionId) {
  return request({
    url: '/basic/ms/sysMenuPermission/' + permissionId,
    method: 'delete'
  })
}
