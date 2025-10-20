import request from '@/utils/request'

// 查询消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）列表
export function listRead(query) {
  return request({
    url: '/system/read/list',
    method: 'get',
    params: query
  })
}

// 查询消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）详细
export function getRead(readId) {
  return request({
    url: '/system/read/' + readId,
    method: 'get'
  })
}

// 新增消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
export function addRead(data) {
  return request({
    url: '/system/read',
    method: 'post',
    data: data
  })
}

// 修改消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
export function updateRead(data) {
  return request({
    url: '/system/read',
    method: 'put',
    data: data
  })
}

// 删除消息已读状态（追踪每条消息的已读情况，支撑群聊扩展）
export function delRead(readId) {
  return request({
    url: '/system/read/' + readId,
    method: 'delete'
  })
}
