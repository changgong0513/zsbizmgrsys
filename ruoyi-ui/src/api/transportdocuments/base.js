import request from '@/utils/request'

// 查询运输单基本信息列表
export function listBase(query) {
  return request({
    url: '/transportdocuments/base/list',
    method: 'get',
    params: query
  })
}

// 查询运输单基本信息详细
export function getBase(id) {
  return request({
    url: '/transportdocuments/base/' + id,
    method: 'get'
  })
}

// 新增运输单基本信息
export function addBase(data) {
  return request({
    url: '/transportdocuments/base',
    method: 'post',
    data: data
  })
}

// 修改运输单基本信息
export function updateBase(data) {
  return request({
    url: '/transportdocuments/base',
    method: 'put',
    data: data
  })
}

// 删除运输单基本信息
export function delBase(id) {
  return request({
    url: '/transportdocuments/base/' + id,
    method: 'delete'
  })
}
