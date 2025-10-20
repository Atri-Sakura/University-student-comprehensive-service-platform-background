import request from '@/utils/request'

// 查询客服工单列表
export function listWorkorder(query) {
  return request({
    url: '/system/workorder/list',
    method: 'get',
    params: query
  })
}

// 查询客服工单详细
export function getWorkorder(platformWorkorderId) {
  return request({
    url: '/system/workorder/' + platformWorkorderId,
    method: 'get'
  })
}

// 新增客服工单
export function addWorkorder(data) {
  return request({
    url: '/system/workorder',
    method: 'post',
    data: data
  })
}

// 修改客服工单
export function updateWorkorder(data) {
  return request({
    url: '/system/workorder',
    method: 'put',
    data: data
  })
}

// 删除客服工单
export function delWorkorder(platformWorkorderId) {
  return request({
    url: '/system/workorder/' + platformWorkorderId,
    method: 'delete'
  })
}
