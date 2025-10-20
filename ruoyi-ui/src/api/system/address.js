import request from '@/utils/request'

// 查询商家地址列表
export function listAddress(query) {
  return request({
    url: '/system/address/list',
    method: 'get',
    params: query
  })
}

// 查询商家地址详细
export function getAddress(merchantAddressId) {
  return request({
    url: '/system/address/' + merchantAddressId,
    method: 'get'
  })
}

// 新增商家地址
export function addAddress(data) {
  return request({
    url: '/system/address',
    method: 'post',
    data: data
  })
}

// 修改商家地址
export function updateAddress(data) {
  return request({
    url: '/system/address',
    method: 'put',
    data: data
  })
}

// 删除商家地址
export function delAddress(merchantAddressId) {
  return request({
    url: '/system/address/' + merchantAddressId,
    method: 'delete'
  })
}
