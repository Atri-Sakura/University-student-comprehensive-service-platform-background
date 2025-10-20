import request from '@/utils/request'

// 查询角色权限关联列表
export function listPerm(query) {
  return request({
    url: '/system/perm/list',
    method: 'get',
    params: query
  })
}

// 查询角色权限关联详细
export function getPerm(platformRolePermId) {
  return request({
    url: '/system/perm/' + platformRolePermId,
    method: 'get'
  })
}

// 新增角色权限关联
export function addPerm(data) {
  return request({
    url: '/system/perm',
    method: 'post',
    data: data
  })
}

// 修改角色权限关联
export function updatePerm(data) {
  return request({
    url: '/system/perm',
    method: 'put',
    data: data
  })
}

// 删除角色权限关联
export function delPerm(platformRolePermId) {
  return request({
    url: '/system/perm/' + platformRolePermId,
    method: 'delete'
  })
}
