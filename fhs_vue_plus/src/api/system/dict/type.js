import request from '@/utils/request'

// 查询字典类型列表
export function listType(query) {
  return request({
    url: '/ms/wordbook/findWordbookGroupForPage',
    method: 'get',
    params: query
  })
}

// 查询字典类型详细
export function getType(groupId) {
  return request({
    url: '/ms/wordbook/getWordbookGroupBean?groupId=' + groupId,
    method: 'get'
  })
}

// 新增字典类型
export function addType(data) {
  return request({
    url: '/ms/wordbook/addWordbookGroup',
    method: 'post',
    data: data
  })
}

// 修改字典类型
export function updateType(data) {
  return request({
    url: '/ms/wordbook/updateWordbookGroup',
    method: 'put',
    data: data
  })
}

// 删除字典类型
export function delType(wordbookGroupCode,groupId) {
  return request({
    url: '/ms/wordbook/delWordbookGroup?wordbookGroupCode=' + wordbookGroupCode+'&groupId='+groupId,
    method: 'delete'
  })
}

// 导出字典类型
export function exportType(query) {
  return request({
    url: '/system/dict/type/export',
    method: 'get',
    params: query
  })
}

// 获取字典选择框列表
export function optionselect() {
  return request({
    url: '/system/dict/type/optionselect',
    method: 'get'
  })
}

// 刷新字典缓存
export function refresh(wordbookGroupCode) {
  return request({
    url: '/ms/wordbook/refreshRedisCache?wordbookGroupCode='+wordbookGroupCode,
    method: 'get'
  })
}
