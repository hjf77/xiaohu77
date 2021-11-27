import request from '@/utils/request'

// 查询部门列表
export function listDept(data) {
  return request({
    url: '/ms/sysOrganization/tree',
    method: 'POST',
    data
  })
}

// 查询部门详细
export function getDept(deptId) {
  return request({
    url: '/ms/sysOrganization/' + deptId,
    method: 'get'
  })
}

// 查询部门下拉树结构
export function treeselect() {
  return request({
    url: '/ms/sysOrganization/getOrgIdComBoxData',
    method: 'post'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/ms/sysOrganization',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/ms/sysOrganization',
    method: 'PUT',
    data: data
  })
}

// 删除部门
export function delDept(deptId) {
  return request({
    url: '/ms/sysOrganization/' + deptId,
    method: 'DELETE'
  })
}

