import request from '@/utils/request'

// 查询消息附件（存储图片/语音等附件的元信息）列表
export function listAttachment(query) {
  return request({
    url: '/system/attachment/list',
    method: 'get',
    params: query
  })
}

// 查询消息附件（存储图片/语音等附件的元信息）详细
export function getAttachment(attachmentId) {
  return request({
    url: '/system/attachment/' + attachmentId,
    method: 'get'
  })
}

// 新增消息附件（存储图片/语音等附件的元信息）
export function addAttachment(data) {
  return request({
    url: '/system/attachment',
    method: 'post',
    data: data
  })
}

// 修改消息附件（存储图片/语音等附件的元信息）
export function updateAttachment(data) {
  return request({
    url: '/system/attachment',
    method: 'put',
    data: data
  })
}

// 删除消息附件（存储图片/语音等附件的元信息）
export function delAttachment(attachmentId) {
  return request({
    url: '/system/attachment/' + attachmentId,
    method: 'delete'
  })
}
