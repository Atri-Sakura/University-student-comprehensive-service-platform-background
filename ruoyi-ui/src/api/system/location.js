import request from '@/utils/request'

// 查询骑手位置列表
export function listLocation(query) {
  return request({
    url: '/system/location/list',
    method: 'get',
    params: query
  })
}

// 查询骑手位置详细
export function getLocation(riderLocationId) {
  return request({
    url: '/system/location/' + riderLocationId,
    method: 'get'
  })
}

// 新增骑手位置
export function addLocation(data) {
  return request({
    url: '/system/location',
    method: 'post',
    data: data
  })
}

// 修改骑手位置
export function updateLocation(data) {
  return request({
    url: '/system/location',
    method: 'put',
    data: data
  })
}

// 删除骑手位置
export function delLocation(riderLocationId) {
  return request({
    url: '/system/location/' + riderLocationId,
    method: 'delete'
  })
}
