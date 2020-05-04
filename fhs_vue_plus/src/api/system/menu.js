import request from '@/utils/request'

// 查询菜单列表
export function listMenu(query) {
  return request({
    url: '/ms/sysMenu/getMenuTree',
    method: 'get',
    params: query
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: '/ms/sysMenu/info/' + menuId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/ms/sysMenu/addMenu',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/ms/sysMenu/updateMenu',
    method: 'put',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: '/ms/sysMenu/del?id=' + menuId,
    method: 'delete'
  })
}

// 所在服务 下拉数据
export function getServerOptions() {
  return request({
    url: '/ms/sett_ms_menu_server/findList',
    method: 'get'
  })
}

// 子系统 下拉数据
export function getSystemOptions() {
  return request({
    url: '/ms/sett_ms_system/findList',
    method: 'get'
  })
}

//查询 权限菜单列表
export function getPermissionList(menuId) {
  return request({
    url: '/ms/sysMenuPermission/findListData?menuId=' + menuId,
    method: 'get'
  })
}

//查询 权限菜单
export function getPermission(permissionId) {
  return request({
    url: '/ms/sysMenuPermission/info/' + permissionId,
    method: 'get'
  })
}

// 批量增加增删
export function addAllPermission(menuId) {
  return request({
    url: '/ms/sysMenuPermission/addBaseMenuBatch?menuId=' + menuId,
    method: 'post',
  })
}

// 新增权限
export function addPermission(data) {
  return request({
    url: '/ms/sysMenuPermission/add',
    method: 'post',
    data: data
  })
}

// 修改权限
export function updatePermission(data) {
  return request({
    url: '/ms/sysMenuPermission/update?permissionId=' + data.permissionId,
    method: 'put',
    data: data
  })
}

// 删除权限
export function delPermission(permissionId) {
  return request({
    url: '/ms/sysMenuPermission/del?id=' + permissionId,
    method: 'delete'
  })
}
