import request from '@/utils/request'


// 查询服务列表
export function listTask(query) {
  return request({
    url: '/ms/myWorks/getNeedComplateTask',
    method: 'get',
    params: query
  })
}

// 获取实例详情
export function findInstanceById(taskId) {
  return request({
    url: '/ms/myWorks/findInstanceById?instanceId=' + taskId,
    method: 'get'
  })
}

// 获取人员tree
export function getUserTree() {
  return request({
    url: '/ms/sysUser/getUserTree',
    method: 'get'
  })
}
// 获取实例历史
export function getApprovalRecord(taskId) {
  return request({
    url: '/ms/flowTaskHistory/getApprovalRecord?instanceId=' + taskId,
    method: 'get'
  })
}
// 查询详细
export function getTask(taskId,nameSpace) {
  return request({
    url: '/ms/x/'+nameSpace+'/info/' + taskId,
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
    url: '/ms/x/sett_ms_menu_server/update/'+data.id,
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

// 同意ok
export function complateTask(taskId,remark) {

  return request({
    url: '/ms/myWorks/complateTask',
    method: 'post',
    data: {taskId: taskId, remark: remark},
  })
}
// 添加抄送人
export function addccToUserUserName(ccUserTos,instanceIds) {
  return request({
    url: '/ms/flow_instance/updateCCTo',
    method: 'post',
    data: {ccTo: ccUserTos, instanceId: instanceIds},
  })
}

// 委派
export function delegate(userName,userId,taskId) {
  return request({
    url: '/ms/myWorks/delegate',
    method: 'post',
    data: {userId: userId, userName: userName,taskId: taskId},
  })
}
// 駁回
export function backTask(taskId) {
  return request({
    url: '/ms/myWorks/backTask?taskId=' + taskId,
    method: 'post'
  })
}
// 駁回到指定节点
export function backTaskDesignated(taskId,activityId,remark) {
  return request({
    url: '/ms/myWorks/backTask',
    method: 'post',
    data: {taskId: taskId, activityId: activityId, isPre: true, remark: remark},
  })
}
//获取节点
export function getNode(taskId) {
  return request({
    url: '/ms/myWorks/findBackAvtivity?taskId=' + taskId,
    method: 'post'
  })
}

//
export function findBackAvtivity(taskId) {
  return request({
    url: '/ms/myWorks/findBackAvtivity?taskId=' + taskId,
    method: 'post',
    async: false
  })
}
