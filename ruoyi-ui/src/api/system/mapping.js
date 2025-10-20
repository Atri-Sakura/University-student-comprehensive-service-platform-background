import request from '@/utils/request'

// 查询角色-账号映射（多角色登录路由核心）列表
export function listMapping(query) {
  return request({
    url: '/system/mapping/list',
    method: 'get',
    params: query
  })
}

// 查询角色-账号映射（多角色登录路由核心）详细
export function getMapping(platformRoleMappingId) {
  return request({
    url: '/system/mapping/' + platformRoleMappingId,
    method: 'get'
  })
}

// 新增角色-账号映射（多角色登录路由核心）
export function addMapping(data) {
  return request({
    url: '/system/mapping',
    method: 'post',
    data: data
  })
}

// 修改角色-账号映射（多角色登录路由核心）
export function updateMapping(data) {
  return request({
    url: '/system/mapping',
    method: 'put',
    data: data
  })
}

// 删除角色-账号映射（多角色登录路由核心）
export function delMapping(platformRoleMappingId) {
  return request({
    url: '/system/mapping/' + platformRoleMappingId,
    method: 'delete'
  })
}
