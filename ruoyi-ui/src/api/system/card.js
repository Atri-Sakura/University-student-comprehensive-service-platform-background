import request from '@/utils/request'

// 查询用户银行卡绑定列表
export function listCard(query) {
  return request({
    url: '/system/card/list',
    method: 'get',
    params: query
  })
}

// 查询用户银行卡绑定详细
export function getCard(id) {
  return request({
    url: '/system/card/' + id,
    method: 'get'
  })
}

// 新增用户银行卡绑定
export function addCard(data) {
  return request({
    url: '/system/card',
    method: 'post',
    data: data
  })
}

// 修改用户银行卡绑定
export function updateCard(data) {
  return request({
    url: '/system/card',
    method: 'put',
    data: data
  })
}

// 删除用户银行卡绑定
export function delCard(id) {
  return request({
    url: '/system/card/' + id,
    method: 'delete'
  })
}
