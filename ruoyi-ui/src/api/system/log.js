import request from '@/utils/request'

// 查询系统操作日志列表
export function listLog(query) {
  return request({
    url: '/system/log/list',
    method: 'get',
    params: query
  })
}

// 查询系统操作日志详细
export function getLog(platformOperateLogId) {
  return request({
    url: '/system/log/' + platformOperateLogId,
    method: 'get'
  })
}

// 新增系统操作日志
export function addLog(data) {
  return request({
    url: '/system/log',
    method: 'post',
    data: data
  })
}

// 修改系统操作日志
export function updateLog(data) {
  return request({
    url: '/system/log',
    method: 'put',
    data: data
  })
}

// 删除系统操作日志
export function delLog(platformOperateLogId) {
  return request({
    url: '/system/log/' + platformOperateLogId,
    method: 'delete'
  })
}
