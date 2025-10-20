import request from '@/utils/request'

// 查询商家钱包列表
export function listWallet(query) {
  return request({
    url: '/system/wallet/list',
    method: 'get',
    params: query
  })
}

// 查询商家钱包详细
export function getWallet(merchantWalletId) {
  return request({
    url: '/system/wallet/' + merchantWalletId,
    method: 'get'
  })
}

// 新增商家钱包
export function addWallet(data) {
  return request({
    url: '/system/wallet',
    method: 'post',
    data: data
  })
}

// 修改商家钱包
export function updateWallet(data) {
  return request({
    url: '/system/wallet',
    method: 'put',
    data: data
  })
}

// 删除商家钱包
export function delWallet(merchantWalletId) {
  return request({
    url: '/system/wallet/' + merchantWalletId,
    method: 'delete'
  })
}
