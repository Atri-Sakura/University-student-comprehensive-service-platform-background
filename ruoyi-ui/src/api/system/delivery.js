import request from '@/utils/request'

// 查询订单配送（含实际配送定位）列表
export function listDelivery(query) {
  return request({
    url: '/system/delivery/list',
    method: 'get',
    params: query
  })
}

// 查询订单配送（含实际配送定位）详细
export function getDelivery(orderDeliveryId) {
  return request({
    url: '/system/delivery/' + orderDeliveryId,
    method: 'get'
  })
}

// 新增订单配送（含实际配送定位）
export function addDelivery(data) {
  return request({
    url: '/system/delivery',
    method: 'post',
    data: data
  })
}

// 修改订单配送（含实际配送定位）
export function updateDelivery(data) {
  return request({
    url: '/system/delivery',
    method: 'put',
    data: data
  })
}

// 删除订单配送（含实际配送定位）
export function delDelivery(orderDeliveryId) {
  return request({
    url: '/system/delivery/' + orderDeliveryId,
    method: 'delete'
  })
}
