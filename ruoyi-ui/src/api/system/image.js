import request from '@/utils/request'

// 查询商品图片关联（支持多图展示）列表
export function listImage(query) {
  return request({
    url: '/system/image/list',
    method: 'get',
    params: query
  })
}

// 查询商品图片关联（支持多图展示）详细
export function getImage(merchantGoodsImageId) {
  return request({
    url: '/system/image/' + merchantGoodsImageId,
    method: 'get'
  })
}

// 新增商品图片关联（支持多图展示）
export function addImage(data) {
  return request({
    url: '/system/image',
    method: 'post',
    data: data
  })
}

// 修改商品图片关联（支持多图展示）
export function updateImage(data) {
  return request({
    url: '/system/image',
    method: 'put',
    data: data
  })
}

// 删除商品图片关联（支持多图展示）
export function delImage(merchantGoodsImageId) {
  return request({
    url: '/system/image/' + merchantGoodsImageId,
    method: 'delete'
  })
}
