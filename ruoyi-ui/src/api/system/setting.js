import request from '@/utils/request'

// 查询用户个性化推荐设置列表
export function listSetting(query) {
  return request({
    url: '/system/setting/list',
    method: 'get',
    params: query
  })
}

// 查询用户个性化推荐设置详细
export function getSetting(userRecommendSettingId) {
  return request({
    url: '/system/setting/' + userRecommendSettingId,
    method: 'get'
  })
}

// 新增用户个性化推荐设置
export function addSetting(data) {
  return request({
    url: '/system/setting',
    method: 'post',
    data: data
  })
}

// 修改用户个性化推荐设置
export function updateSetting(data) {
  return request({
    url: '/system/setting',
    method: 'put',
    data: data
  })
}

// 删除用户个性化推荐设置
export function delSetting(userRecommendSettingId) {
  return request({
    url: '/system/setting/' + userRecommendSettingId,
    method: 'delete'
  })
}
