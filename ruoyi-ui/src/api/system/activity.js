import request from '@/utils/request'

// 查询商家活动列表
export function listActivity(query) {
  return request({
    url: '/system/activity/list',
    method: 'get',
    params: query
  })
}

// 查询商家活动详细
export function getActivity(merchantActivityId) {
  return request({
    url: '/system/activity/' + merchantActivityId,
    method: 'get'
  })
}

// 新增商家活动
export function addActivity(data) {
  return request({
    url: '/system/activity',
    method: 'post',
    data: data
  })
}

// 修改商家活动
export function updateActivity(data) {
  return request({
    url: '/system/activity',
    method: 'put',
    data: data
  })
}

// 删除商家活动
export function delActivity(merchantActivityId) {
  return request({
    url: '/system/activity/' + merchantActivityId,
    method: 'delete'
  })
}
