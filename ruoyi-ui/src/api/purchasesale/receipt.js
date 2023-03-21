import request from '@/utils/request'

// 查询收货管理列表
export function listReceipt(query) {
  return request({
    url: '/receipt/mgr/list',
    method: 'get',
    params: query
  })
}

// 查询收货管理详细
export function getReceipt(receiptId) {
  return request({
    url: '/receipt/mgr/' + receiptId,
    method: 'get'
  })
}

// 新增收货管理
export function addReceipt(data) {
  return request({
    url: '/receipt/mgr',
    method: 'post',
    data: data
  })
}

// 修改收货管理
export function updateReceipt(data) {
  return request({
    url: '/receipt/mgr',
    method: 'put',
    data: data
  })
}

// 删除收货管理
export function delReceipt(receiptId) {
  return request({
    url: '/receipt/mgr/' + receiptId,
    method: 'delete'
  })
}

// 查询库存列表
export function listKc(query) {
  return request({
    url: '/receipt/mgr/kc/list',
    method: 'get',
    params: query
  })
}

