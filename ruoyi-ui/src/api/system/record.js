import request from '@/utils/request'

// 查询订单支付记录列表
export function listRecord(query) {
  return request({
    url: '/system/record/list',
    method: 'get',
    params: query
  })
}

// 查询订单支付记录详细
export function getRecord(orderPayRecordId) {
  return request({
    url: '/system/record/' + orderPayRecordId,
    method: 'get'
  })
}

// 新增订单支付记录
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改订单支付记录
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除订单支付记录
export function delRecord(orderPayRecordId) {
  return request({
    url: '/system/record/' + orderPayRecordId,
    method: 'delete'
  })
}
