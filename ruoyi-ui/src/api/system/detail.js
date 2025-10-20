import request from '@/utils/request'

// 查询外卖订单明细（不含地址信息）列表
export function listDetail(query) {
  return request({
    url: '/system/detail/list',
    method: 'get',
    params: query
  })
}

// 查询外卖订单明细（不含地址信息）详细
export function getDetail(orderTakeoutDetailId) {
  return request({
    url: '/system/detail/' + orderTakeoutDetailId,
    method: 'get'
  })
}

// 新增外卖订单明细（不含地址信息）
export function addDetail(data) {
  return request({
    url: '/system/detail',
    method: 'post',
    data: data
  })
}

// 修改外卖订单明细（不含地址信息）
export function updateDetail(data) {
  return request({
    url: '/system/detail',
    method: 'put',
    data: data
  })
}

// 删除外卖订单明细（不含地址信息）
export function delDetail(orderTakeoutDetailId) {
  return request({
    url: '/system/detail/' + orderTakeoutDetailId,
    method: 'delete'
  })
}
