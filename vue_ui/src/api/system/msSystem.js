import request from '@/utils/request'

// 子系统列表
export function listMsSystem(query) {
  return request({
    url: '/ms/x/sett_ms_system/findPager',
    method: 'get',
    params: query
  })
}

// 子系统详细
export function getMsSystem(id) {
  return request({
    url: '/ms/x/sett_ms_system/info/' + id,
    method: 'get'
  })
}

// 新增岗位
export function addMsSystem(data) {
  return request({
    url: '/ms/x/sett_ms_system/add',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updateMsSystem(data) {
  return request({
    url: '/ms/x/sett_ms_system/update/'+ data.id,
    method: 'put',
    data: data
  })
}

// 删除岗位
export function delMsSystem(id) {
  return request({
    url: '/ms/x/sett_ms_system/del/' + id,
    method: 'delete'
  })
}

// 导出岗位
export function exportMsSystem(query) {
  return request({
    url: '/ms/x/sett_ms_system/setExportField',
    method: 'get',
    params: query
  })
}
