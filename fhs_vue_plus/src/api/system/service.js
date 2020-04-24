import request from '@/utils/request'


// 查询服务列表
export function listService(query) {
  return request({
    url: '/ms/x/sett_ms_menu_server/findPager',
    method: 'get',
    params: query
  })
}

// 查询服务详细
export function getService(roleId) {
  return request({
    url: '/ms/x/sett_ms_menu_server/info/' + roleId,
    method: 'get'
  })
}

// 新增服务
export function addService(data) {
  return request({
    url: '/ms/x/sett_ms_menu_server/add',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateService(data) {
  return request({
    url: '/ms/x/sett_ms_menu_server/update',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delService(roleId) {
  return request({
    url: '/ms/x/sett_ms_menu_server/del/' + roleId.id,
    method: 'delete'
  })
}

// 导出角色
export function exportService(query) {
  return request({
    url: '/ms/x/sett_ms_menu_server/pubExportExcel',
    method: 'get',
    params: query
  })
}
