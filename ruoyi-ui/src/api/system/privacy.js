import request from '@/utils/request'

// 查询用户隐私设置列表
export function listPrivacy(query) {
  return request({
    url: '/system/privacy/list',
    method: 'get',
    params: query
  })
}

// 查询用户隐私设置详细
export function getPrivacy(userPrivacyId) {
  return request({
    url: '/system/privacy/' + userPrivacyId,
    method: 'get'
  })
}

// 新增用户隐私设置
export function addPrivacy(data) {
  return request({
    url: '/system/privacy',
    method: 'post',
    data: data
  })
}

// 修改用户隐私设置
export function updatePrivacy(data) {
  return request({
    url: '/system/privacy',
    method: 'put',
    data: data
  })
}

// 删除用户隐私设置
export function delPrivacy(userPrivacyId) {
  return request({
    url: '/system/privacy/' + userPrivacyId,
    method: 'delete'
  })
}
