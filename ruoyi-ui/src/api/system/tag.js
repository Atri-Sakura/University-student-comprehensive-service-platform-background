import request from '@/utils/request'

// 查询平台标签体系（管理用户和商品标签）列表
export function listTag(query) {
  return request({
    url: '/system/tag/list',
    method: 'get',
    params: query
  })
}

// 查询平台标签体系（管理用户和商品标签）详细
export function getTag(platformTagId) {
  return request({
    url: '/system/tag/' + platformTagId,
    method: 'get'
  })
}

// 新增平台标签体系（管理用户和商品标签）
export function addTag(data) {
  return request({
    url: '/system/tag',
    method: 'post',
    data: data
  })
}

// 修改平台标签体系（管理用户和商品标签）
export function updateTag(data) {
  return request({
    url: '/system/tag',
    method: 'put',
    data: data
  })
}

// 删除平台标签体系（管理用户和商品标签）
export function delTag(platformTagId) {
  return request({
    url: '/system/tag/' + platformTagId,
    method: 'delete'
  })
}
