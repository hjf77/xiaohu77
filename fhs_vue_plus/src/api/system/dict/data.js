import request from '@/utils/request'

// 查询字典数据列表
export function listData(query) {
  return request({
    url: 'ms/wordbook/findWordbookForPage',
    method: 'get',
    params: query
  })
}

// 查询字典数据详细
export function getData(wordbookId) {
  return request({
    url: '/ms/wordbook/getWordbookBean?wordbookId=' + wordbookId,
    method: 'get'
  })
}

// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
  return request({
    url: '/webApi/wordbook/getData?wordbookGroupCode=' + dictType,
    method: 'get'
  })
}

// 新增字典数据
export function addData(data) {
  return request({
    url: 'ms/wordbook/addWordbook',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateData(data) {
  return request({
    url: '/ms/wordbook/updateWordbook',
    method: 'put',
    data: data
  })
}

// 删除字典数据
export function delData(wordbookId) {
  return request({
    url: '/ms/wordbook/delWordbook?wordbookId=' + wordbookId,
    method: 'delete'
  })
}

// 导出字典数据
export function exportData(query) {
  return request({
    url: '/system/dict/data/export',
    method: 'get',
    params: query
  })
}
