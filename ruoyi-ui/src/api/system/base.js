import request from '@/utils/request'

// 查询商家基础信息列表
export function listBase(query) {
  return request({
    url: '/system/base/list',
    method: 'get',
    params: query
  })
}

// 查询商家基础信息详细
export function getBase(merchantBaseId) {
  return request({
    url: '/system/base/' + merchantBaseId,
    method: 'get'
  })
}

// 新增商家基础信息
export function addBase(data) {
  return request({
    url: '/system/base',
    method: 'post',
    data: data
  })
}

// 修改商家基础信息
export function updateBase(data) {
  return request({
    url: '/system/base',
    method: 'put',
    data: data
  })
}

// 删除商家基础信息
export function delBase(merchantBaseId) {
  return request({
    url: '/system/base/' + merchantBaseId,
    method: 'delete'
  })
}
