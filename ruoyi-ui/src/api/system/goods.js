import request from '@/utils/request'

// 查询商品列表
export function listGoods(query) {
  return request({
    url: '/system/goods/list',
    method: 'get',
    params: query
  })
}

// 查询商品详细
export function getGoods(merchantGoodsId) {
  return request({
    url: '/system/goods/' + merchantGoodsId,
    method: 'get'
  })
}

// 新增商品
export function addGoods(data) {
  return request({
    url: '/system/goods',
    method: 'post',
    data: data
  })
}

// 修改商品
export function updateGoods(data) {
  return request({
    url: '/system/goods',
    method: 'put',
    data: data
  })
}

// 删除商品
export function delGoods(merchantGoodsId) {
  return request({
    url: '/system/goods/' + merchantGoodsId,
    method: 'delete'
  })
}
