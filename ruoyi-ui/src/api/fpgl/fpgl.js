import request from '@/utils/request'

// 查询发票管理列表
export function listMain(query) {
  return request({
    url: '/fpgl/mgr/list',
    method: 'get',
    params: query
  })
}

// 查询开票明细列表
export function listFpmx(orderId) {
  console.log("查询开票明细列表");
  return request({
    url: '/fpgl/mgr/fpmxlist/' + orderId,
    method: 'get'
  })
}

// 查询发票管理详细
export function getMain(fpglId) {
  return request({
    url: '/fpgl/mgr/' + fpglId,
    method: 'get'
  })
}

// 新增发票管理
export function addMain(data) {
  return request({
    url: '/fpgl/mgr',
    method: 'post',
    data: data
  })
}

// 修改发票管理
export function updateMain(data) {
  return request({
    url: '/fpgl/mgr',
    method: 'put',
    data: data
  })
}

// 删除发票管理
export function delMain(fpglId) {
  return request({
    url: '/fpgl/mgr/' + fpglId,
    method: 'delete'
  })
}

// 查询申请开票列表
export function listSqkp(query) {
  return request({
    url: '/fpgl/mgr/sqkp/list',
    method: 'get',
    params: query
  })
}

// 申请开票上传附件
export function uploadFile(data) {
  return request({
    url: '/fpgl/mgr/sqkp/upload',
    method: 'post',
    data: data
  })
}

// 查询合同附件
export function getContractAdditional(contractId) {
  // console.log("查询合同附件");
  return request({
    url: '/fpgl/mgr/sqkp/additional/' + contractId,
    method: 'get'
  })
}
