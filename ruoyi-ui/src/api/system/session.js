import request from '@/utils/request'

// 查询聊天会话（管理双方的聊天窗口关系）列表
export function listSession(query) {
  return request({
    url: '/system/session/list',
    method: 'get',
    params: query
  })
}

// 查询聊天会话（管理双方的聊天窗口关系）详细
export function getSession(sessionId) {
  return request({
    url: '/system/session/' + sessionId,
    method: 'get'
  })
}

// 新增聊天会话（管理双方的聊天窗口关系）
export function addSession(data) {
  return request({
    url: '/system/session',
    method: 'post',
    data: data
  })
}

// 修改聊天会话（管理双方的聊天窗口关系）
export function updateSession(data) {
  return request({
    url: '/system/session',
    method: 'put',
    data: data
  })
}

// 删除聊天会话（管理双方的聊天窗口关系）
export function delSession(sessionId) {
  return request({
    url: '/system/session/' + sessionId,
    method: 'delete'
  })
}
