import request from '@/utils/request'

// 查询定时任务调度列表
export function listJob(query) {
  return request({
    url: 'ms/task/list',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度详细
export function getJob(jobId) {
  return request({
    url: '/monitor/job/' + jobId,
    method: 'get'
  })
}

// 新增定时任务调度
export function addJob(data) {
  return request({
    url: 'ms/task/add',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度
export function updateJob(data) {
  return request({
    url: '/monitor/job',
    method: 'put',
    data: data
  })
}

// 删除定时任务调度
export function delJob(jobName,jobGroup) {
  return request({
    url: 'ms/task/remove?jobName=' + jobName+'&jobGroup='+jobGroup,
    method: 'delete'
  })
}

// 导出定时任务调度
export function exportJob(query) {
  return request({
    url: '/monitor/job/export',
    method: 'get',
    params: query
  })
}

// 任务状态修改
export function changeJobStatus(jobId, status) {
  const data = {
    jobId,
    status
  }
  return request({
    url: '/monitor/job/changeStatus',
    method: 'put',
    data: data
  })
}


// 定时任务触发
export function runJob(jobName, jobGroup) {
  return request({
    url: 'ms/task/trigger?jobName=' + jobName+'&jobGroup='+jobGroup,
    method: 'get'
  })
}

// 定时任务暂停
export function timeOutJob(jobName, jobGroup) {
  return request({
    url: 'ms/task/pause?jobName=' + jobName + '&jobGroup=' + jobGroup,
    method: 'get'
  })
}

// 定时任务恢复
export function restoreJob(jobName, jobGroup) {
  return request({
    url: 'ms/task/resume?jobName=' + jobName + '&jobGroup=' + jobGroup,
    method: 'get'
  })
}
