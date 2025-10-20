import request from '@/utils/request'

// 查询订单优惠券列表
export function listCoupon(query) {
  return request({
    url: '/system/coupon/list',
    method: 'get',
    params: query
  })
}

// 查询订单优惠券详细
export function getCoupon(orderCouponId) {
  return request({
    url: '/system/coupon/' + orderCouponId,
    method: 'get'
  })
}

// 新增订单优惠券
export function addCoupon(data) {
  return request({
    url: '/system/coupon',
    method: 'post',
    data: data
  })
}

// 修改订单优惠券
export function updateCoupon(data) {
  return request({
    url: '/system/coupon',
    method: 'put',
    data: data
  })
}

// 删除订单优惠券
export function delCoupon(orderCouponId) {
  return request({
    url: '/system/coupon/' + orderCouponId,
    method: 'delete'
  })
}
