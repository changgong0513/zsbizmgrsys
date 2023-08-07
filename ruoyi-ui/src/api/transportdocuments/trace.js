import request from '@/utils/request'

// 查询运输单追溯信息列表
export function listTrace(query) {
  return request({
    url: '/transportdocuments/trace/list',
    method: 'get',
    params: query
  })
}

// 查询运输单追溯信息详细
export function getTrace(id) {
  return request({
    url: '/transportdocuments/trace/' + id,
    method: 'get'
  })
}

// 新增运输单追溯信息
export function addTrace(data) {
  return request({
    url: '/transportdocuments/trace',
    method: 'post',
    data: data
  })
}

// 修改运输单追溯信息
export function updateTrace(data) {
  return request({
    url: '/transportdocuments/trace',
    method: 'put',
    data: data
  })
}

// 删除运输单追溯信息
export function delTrace(id) {
  return request({
    url: '/transportdocuments/trace/' + id,
    method: 'delete'
  })
}
