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

// 生成中转运输单详细信息
export function generateTransport(ids, data) {
  return request({
    url: '/transportdocuments/detail/generate/transport/' + ids,
    method: 'post',
    data: data
  })
}

// 生成运输单编号
export function generateTransportId() {
  return request({
    url: '/transportdocuments/detail/generate/transport/id',
    method: 'get'
  })
}
