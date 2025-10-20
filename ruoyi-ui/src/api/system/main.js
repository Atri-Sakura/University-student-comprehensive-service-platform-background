import request from '@/utils/request'

// 查询订单主（整合地址与定位信息）列表
export function listMain(query) {
  return request({
    url: '/system/main/list',
    method: 'get',
    params: query
  })
}

// 查询订单主（整合地址与定位信息）详细
export function getMain(orderMainId) {
  return request({
    url: '/system/main/' + orderMainId,
    method: 'get'
  })
}

// 新增订单主（整合地址与定位信息）
export function addMain(data) {
  return request({
    url: '/system/main',
    method: 'post',
    data: data
  })
}

// 修改订单主（整合地址与定位信息）
export function updateMain(data) {
  return request({
    url: '/system/main',
    method: 'put',
    data: data
  })
}

// 删除订单主（整合地址与定位信息）
export function delMain(orderMainId) {
  return request({
    url: '/system/main/' + orderMainId,
    method: 'delete'
  })
}
