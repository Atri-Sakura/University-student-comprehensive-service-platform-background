import request from '@/utils/request'

// 查询个人课列表
export function listTimetable(query) {
  return request({
    url: '/system/timetable/list',
    method: 'get',
    params: query
  })
}

// 查询个人课详细
export function getTimetable(userTimetableId) {
  return request({
    url: '/system/timetable/' + userTimetableId,
    method: 'get'
  })
}

// 新增个人课
export function addTimetable(data) {
  return request({
    url: '/system/timetable',
    method: 'post',
    data: data
  })
}

// 修改个人课
export function updateTimetable(data) {
  return request({
    url: '/system/timetable',
    method: 'put',
    data: data
  })
}

// 删除个人课
export function delTimetable(userTimetableId) {
  return request({
    url: '/system/timetable/' + userTimetableId,
    method: 'delete'
  })
}
