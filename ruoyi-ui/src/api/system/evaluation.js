import request from '@/utils/request'

// 查询商家评价列表
export function listEvaluation(query) {
  return request({
    url: '/system/evaluation/list',
    method: 'get',
    params: query
  })
}

// 查询商家评价详细
export function getEvaluation(merchantEvaluationId) {
  return request({
    url: '/system/evaluation/' + merchantEvaluationId,
    method: 'get'
  })
}

// 新增商家评价
export function addEvaluation(data) {
  return request({
    url: '/system/evaluation',
    method: 'post',
    data: data
  })
}

// 修改商家评价
export function updateEvaluation(data) {
  return request({
    url: '/system/evaluation',
    method: 'put',
    data: data
  })
}

// 删除商家评价
export function delEvaluation(merchantEvaluationId) {
  return request({
    url: '/system/evaluation/' + merchantEvaluationId,
    method: 'delete'
  })
}
