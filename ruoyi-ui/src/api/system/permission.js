import request from '@/utils/request'

// 查询权限列表
export function listPermission(query) {
  return request({
    url: '/system/permission/list',
    method: 'get',
    params: query
  })
}

// 查询权限详细
export function getPermission(platformPermissionId) {
  return request({
    url: '/system/permission/' + platformPermissionId,
    method: 'get'
  })
}

// 新增权限
export function addPermission(data) {
  return request({
    url: '/system/permission',
    method: 'post',
    data: data
  })
}

// 修改权限
export function updatePermission(data) {
  return request({
    url: '/system/permission',
    method: 'put',
    data: data
  })
}

// 删除权限
export function delPermission(platformPermissionId) {
  return request({
    url: '/system/permission/' + platformPermissionId,
    method: 'delete'
  })
}
