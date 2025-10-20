import request from '@/utils/request'

// 查询平台管理员列表
export function listAdmin(query) {
  return request({
    url: '/system/admin/list',
    method: 'get',
    params: query
  })
}

// 查询平台管理员详细
export function getAdmin(platformAdminId) {
  return request({
    url: '/system/admin/' + platformAdminId,
    method: 'get'
  })
}

// 新增平台管理员
export function addAdmin(data) {
  return request({
    url: '/system/admin',
    method: 'post',
    data: data
  })
}

// 修改平台管理员
export function updateAdmin(data) {
  return request({
    url: '/system/admin',
    method: 'put',
    data: data
  })
}

// 删除平台管理员
export function delAdmin(platformAdminId) {
  return request({
    url: '/system/admin/' + platformAdminId,
    method: 'delete'
  })
}
