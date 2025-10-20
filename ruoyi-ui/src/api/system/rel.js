import request from '@/utils/request'

// 查询骑手接单关联列表
export function listRel(query) {
  return request({
    url: '/system/rel/list',
    method: 'get',
    params: query
  })
}

// 查询骑手接单关联详细
export function getRel(riderOrderRelId) {
  return request({
    url: '/system/rel/' + riderOrderRelId,
    method: 'get'
  })
}

// 新增骑手接单关联
export function addRel(data) {
  return request({
    url: '/system/rel',
    method: 'post',
    data: data
  })
}

// 修改骑手接单关联
export function updateRel(data) {
  return request({
    url: '/system/rel',
    method: 'put',
    data: data
  })
}

// 删除骑手接单关联
export function delRel(riderOrderRelId) {
  return request({
    url: '/system/rel/' + riderOrderRelId,
    method: 'delete'
  })
}
