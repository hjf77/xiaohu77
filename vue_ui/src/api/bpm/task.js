import request from '@/utils/request'

export function getTodoTaskPage(query) {
  return request({
    url: '/basic/ms/task/todo-page',
    method: 'get',
    params: query
  })
}

export function getDoneTaskPage(query) {
  return request({
    url: '/basic/ms/task/done-page',
    method: 'get',
    params: query
  })
}

export function completeTask(data) {
  return request({
    url: '/basic/ms/task/complete',
    method: 'PUT',
    data: data
  })
}

export function approveTask(data) {
  return request({
    url: '/basic/ms/task/approve', 
    method: 'PUT',
    data: data
  })
}

export function rejectTask(data) {
  return request({
    url: '/basic/ms/task/reject',
    method: 'PUT',
    data: data
  })
}
export function backTask(data) {
  return request({
    url: '/basic/ms/task/back',
    method: 'PUT',
    data: data
  })
}

export function updateTaskAssignee(data) {
  return request({
    url: '/basic/ms/task/update-assignee',
    method: 'PUT',
    data: data
  })
}

export function getTaskListByProcessInstanceId(processInstanceId) {
  return request({
    url: '/basic/ms/task/list-by-process-instance-id?processInstanceId=' + processInstanceId,
    method: 'get',
  })
}
