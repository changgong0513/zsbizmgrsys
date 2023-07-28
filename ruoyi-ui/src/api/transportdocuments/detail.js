import request from '@/utils/request'

// 查询运输单详细信息列表
export function listDetail(query) {
  return request({
    url: '/transportdocuments/detail/list',
    method: 'get',
    params: query
  })
}

// 查询运输单详细信息详细
export function getDetail(id) {
  return request({
    url: '/transportdocuments/detail/' + id,
    method: 'get'
  })
}

// 新增运输单详细信息
export function addDetail(data) {
  return request({
    url: '/transportdocuments/detail',
    method: 'post',
    data: data
  })
}

// 修改运输单详细信息
export function updateDetail(data) {
  return request({
    url: '/transportdocuments/detail',
    method: 'put',
    data: data
  })
}

// 删除运输单详细信息
export function delDetail(id) {
  return request({
    url: '/transportdocuments/detail/' + id,
    method: 'delete'
  })
}
