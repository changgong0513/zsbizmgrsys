package com.ruoyi.purchase.sale.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.fpgl.domain.FpglMainInfo;
import com.ruoyi.fpgl.mapper.FpglMainInfoMapper;
import com.ruoyi.report.masterdata.domain.MasterDataClientInfo;
import com.ruoyi.report.masterdata.service.IMasterDataClientInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.purchase.sale.mapper.PurchaseSaleOrderInfoMapper;
import com.ruoyi.purchase.sale.domain.PurchaseSaleOrderInfo;
import com.ruoyi.purchase.sale.service.IPurchaseSaleOrderInfoService;

/**
 * 采购收货销售发货管理Service业务层处理
 * 
 * @author changgong0513
 * @date 2022-11-03
 */
@Service
public class PurchaseSaleOrderInfoServiceImpl implements IPurchaseSaleOrderInfoService 
{
    @Autowired
    private PurchaseSaleOrderInfoMapper purchaseSaleOrderInfoMapper;

    @Autowired
    private FpglMainInfoMapper fpglMainInfoMapper;

    @Autowired
    private IMasterDataClientInfoService masterDataClientInfoService;

    @Override
    public PurchaseSaleOrderInfo selectPurchaseSaleOrderInfoByContractId(String ContractId) {
        PurchaseSaleOrderInfo purchaseSaleOrderInfo = purchaseSaleOrderInfoMapper
                .selectPurchaseSaleOrderInfoByOrderId(ContractId);

        if (StringUtils.equals(purchaseSaleOrderInfo.getPurchaseType(), "1")) {
            // 企业采购，其他采购类型供应商为空
            // 取得所有客户主数据
            List<MasterDataClientInfo> clientList = masterDataClientInfoService
                    .selectMasterDataClientInfoList(new MasterDataClientInfo());
            // 客户主数据列表转成Map（key：baseId, value：companyName）
            Map<String, String> clientMap = clientList
                    .stream()
                    .collect(Collectors.toMap(MasterDataClientInfo::getBaseId, MasterDataClientInfo::getCompanyName));
            purchaseSaleOrderInfo.setSupplierRealName(clientMap.get(purchaseSaleOrderInfo.getSupplierName()));
        }

        return purchaseSaleOrderInfo;
    }

    /**
     * 查询采购收货销售发货管理
     *
     * @param orderId 订单编号
     * @return 采购收货销售发货管理
     */
    @Override
    public PurchaseSaleOrderInfo selectPurchaseSaleOrderInfoByOrderId(String orderId) {
        PurchaseSaleOrderInfo purchaseSaleOrderInfo = purchaseSaleOrderInfoMapper
                .selectPurchaseSaleOrderInfoByOrderId(orderId);

        if (StringUtils.equals(purchaseSaleOrderInfo.getPurchaseType(), "1")) {
            // 企业采购，其他采购类型供应商为空
            // 取得所有客户主数据
            List<MasterDataClientInfo> clientList = masterDataClientInfoService
                    .selectMasterDataClientInfoList(new MasterDataClientInfo());
            // 客户主数据列表转成Map（key：baseId, value：companyName）
            Map<String, String> clientMap = clientList
                    .stream()
                    .collect(Collectors.toMap(MasterDataClientInfo::getBaseId, MasterDataClientInfo::getCompanyName));
            purchaseSaleOrderInfo.setSupplierRealName(clientMap.get(purchaseSaleOrderInfo.getSupplierName()));
        }

        return purchaseSaleOrderInfo;
    }

    /**
     * 查询采购收货销售发货管理列表
     * 
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 采购收货销售发货管理
     */
    @Override
    public List<PurchaseSaleOrderInfo> selectPurchaseSaleOrderInfoList(PurchaseSaleOrderInfo purchaseSaleOrderInfo) {
        return purchaseSaleOrderInfoMapper.selectPurchaseSaleOrderInfoList(purchaseSaleOrderInfo);
    }

    /**
     * 查询采购收货管理列表
     *
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 采购收货集合
     */
    @DataScope(deptAlias = "tt1")
    @Override
    public List<PurchaseSaleOrderInfo> selectPurchaseOrderInfoUnionList(PurchaseSaleOrderInfo purchaseSaleOrderInfo) {
        List<PurchaseSaleOrderInfo> list = purchaseSaleOrderInfoMapper.selectPurchaseOrderInfoUnionList(purchaseSaleOrderInfo);
        Map<String, List<PurchaseSaleOrderInfo>> map = list.stream()
                .collect(Collectors.groupingBy(element -> element.getSupplierName()));

        // 根据客户编号，取得客户名称
        list.stream().forEach(elment -> {
            String supplierName = elment.getSupplierName();
            MasterDataClientInfo supplierData = masterDataClientInfoService.selectMasterDataClientInfoByBaseId(supplierName);
            if (supplierData != null) {
                String supplierRealName = supplierData.getCompanyName();
                elment.setSupplierRealName(supplierRealName);
            }
        });

        // 设置采购合同状态和完成率
//        transportdocumentsDetailInfoService.setPurchaseOrderStatusAndCompletionRate(list);

        return list;
    }

    /**
     * 查询销售发货管理列表
     *
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 销售发货集合
     */
    @Override
    public List<PurchaseSaleOrderInfo> selectSaleOrderInfoUnionList(PurchaseSaleOrderInfo purchaseSaleOrderInfo) {

        List<PurchaseSaleOrderInfo> list = purchaseSaleOrderInfoMapper.selectSaleOrderInfoUnionList(purchaseSaleOrderInfo);

        Map<String, List<PurchaseSaleOrderInfo>> map = list.stream()
                .collect(Collectors.groupingBy(element -> element.getSupplierName()));

        // 根据客户编号，取得客户名称
        list.stream().forEach(elment -> {
            String supplierName = elment.getSupplierName();
            if (StringUtils.isNotBlank(supplierName)) {
                MasterDataClientInfo supplierData = masterDataClientInfoService
                        .selectMasterDataClientInfoByBaseId(supplierName);
                String supplierRealName = supplierData.getCompanyName();
                elment.setSupplierRealName(supplierRealName);
            }
        });

        return list;
    }

    /**
     * 取得采购合同总数
     *
     * @return 结果
     */
    public int getPurchaseContractCounts(String belongDept) {
        return purchaseSaleOrderInfoMapper.getPurchaseContractCounts(belongDept);
    }

    /**
     * 取得销售合同总数
     *
     * @return 结果
     */
    public int getSaleContractCounts(String belongDept) {
        return purchaseSaleOrderInfoMapper.getSaleContractCounts(belongDept);
    }

    /**
     * 新增采购收货销售发货管理
     * 
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 结果
     */
    @Override
    public int insertPurchaseSaleOrderInfo(PurchaseSaleOrderInfo purchaseSaleOrderInfo) {
        purchaseSaleOrderInfo.setCreateTime(DateUtils.getNowDate());
        return purchaseSaleOrderInfoMapper.insertPurchaseSaleOrderInfo(purchaseSaleOrderInfo);
    }

    /**
     * 修改采购收货销售发货管理
     * 
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 结果
     */
    @Override
    public int updatePurchaseSaleOrderInfo(PurchaseSaleOrderInfo purchaseSaleOrderInfo) {

        purchaseSaleOrderInfo.setUpdateTime(DateUtils.getNowDate());
        int result = purchaseSaleOrderInfoMapper.updatePurchaseSaleOrderInfo(purchaseSaleOrderInfo);
        if (result > 0) {
            FpglMainInfo data = fpglMainInfoMapper.selectFpglMainInfoByFpglDdbh(purchaseSaleOrderInfo.getOrderId());
            if (purchaseSaleOrderInfo.getIsInvoicing() == 1) {
                // 开票
                if (data == null) {
                    // 未查询到该订单编号的发票数据
                    FpglMainInfo fpglMainInfo = new FpglMainInfo();
                    fpglMainInfo.setFpglKpmx(purchaseSaleOrderInfo.getMaterialName());
                    fpglMainInfo.setFpglKpsl(0L);
                    fpglMainInfo.setFpglKpdj(BigDecimal.ZERO);
                    fpglMainInfo.setFpglKpje(BigDecimal.ZERO);
                    fpglMainInfo.setFpglFpzt("3");
                    fpglMainInfo.setFpglDdbh(purchaseSaleOrderInfo.getOrderId());
                    fpglMainInfo.setFpglSqr(SecurityUtils.getUsername());
                    fpglMainInfo.setCreateTime(DateUtils.getNowDate());
                    fpglMainInfo.setUpdateTime(DateUtils.getNowDate());
                    fpglMainInfo.setCreateBy(SecurityUtils.getUsername());
                    fpglMainInfo.setUpdateBy(SecurityUtils.getUsername());
                    result = fpglMainInfoMapper.insertFpglMainInfo(fpglMainInfo);
                }
            } else {
                // 不开票
                if (data != null) {
                    if (!StringUtils.equals(data.getFpglFpzt(), "3")) {
                        result = 100;
                    } else {
                        result = fpglMainInfoMapper.deleteFpglMainInfoByFpglId(data.getFpglId());
                    }
                }
            }
        }

        return result;
    }

    /**
     * 批量删除采购收货销售发货管理
     * 
     * @param orderIds 需要删除的采购收货销售发货管理主键
     * @return 结果
     */
    @Override
    public int deletePurchaseSaleOrderInfoByOrderIds(String[] orderIds)
    {
        return purchaseSaleOrderInfoMapper.deletePurchaseSaleOrderInfoByOrderIds(orderIds);
    }

    /**
     * 删除采购收货销售发货管理信息
     * 
     * @param orderId 采购收货销售发货管理主键
     * @return 结果
     */
    @Override
    public int deletePurchaseSaleOrderInfoByOrderId(String orderId)
    {
        return purchaseSaleOrderInfoMapper.deletePurchaseSaleOrderInfoByOrderId(orderId);
    }

    /**
     * 设置采购订单状态和完成率
     *
     * @param list
     */
    @Override
    public void setPurchaseOrderStatusAndCompletionRate(List<PurchaseSaleOrderInfo> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        // 取得所有采购运输单状态为完成的数据
        List<String> orderIdList = list.stream().map(PurchaseSaleOrderInfo::getOrderId).collect(Collectors.toList());
        String[] orderIdArray = orderIdList.toArray(new String[list.size()]);
        List<JSONObject> transportdocumentsDetailInfoList = selectTransportdocumentsDetailInfoByRelatedOrderIds(orderIdArray);
        if (null == transportdocumentsDetailInfoList && 0 == transportdocumentsDetailInfoList.size()) return;
        List<JSONObject> completeList = transportdocumentsDetailInfoList.stream()
                .filter(d -> org.apache.commons.lang3.StringUtils.equals(d.getString("transportdocuments_state"), "3")).collect(Collectors.toList());

        // 设置合同状态和完成率
        list.stream().forEach(element -> {
            // 采购数量（来自于采购管理）
            long sumLandedQuantity = 0L;
            if (null == completeList && 0 == completeList.size()) {
                element.setCompletionRate(0 + "%");
            } else {
                // 计算关联订单的卸货数量总和
                String orderId = element.getOrderId();
                sumLandedQuantity = completeList
                        .stream()
                        .filter(d -> orderId.equals(d.getString("related_order_id")) && null != d.getLong("landed_quantity"))
                        .map(d -> d.getLong("landed_quantity")).mapToLong(Long::longValue).sum();
            }

            // 设置订单完成率为100%范围
            long purchaseQuantity = element.getPurchaseQuantity();
            double minPurchaseQuantity = purchaseQuantity * 0.9;
            double maxPurchaseQuantity = purchaseQuantity + purchaseQuantity * 0.1;

            // 设置订单完成率
            if (1 == Double.compare(sumLandedQuantity, minPurchaseQuantity) &&
                    1 == Double.compare(maxPurchaseQuantity, sumLandedQuantity)) {
                // 采购数量（来自于采购管理） == 卸货数量（来自于运单管理）
                // 订单状态：已完成
                element.setOrderStatus("1");
                // 完成率：100%
                element.setCompletionRate(100 + "%");
            } else {
                // 采购数量（来自于采购管理） == 卸货数量（来自于运单管理）
                if (0 != Long.compare(purchaseQuantity, 0)) {
                    // 订单状态：未完成
                    element.setOrderStatus("2");
                    // 完成率：核算数量（来自于收货管理）/ 采购数量（来自于采购管理）* 100
                    double completionRate = division(sumLandedQuantity, purchaseQuantity, 2) * 100;
                    element.setCompletionRate(completionRate + "%");
                }
            }

            // 根据客户编号，取得客户名称
            String supplierName = element.getSupplierName();
            MasterDataClientInfo supplierData = masterDataClientInfoService
                    .selectMasterDataClientInfoByBaseId(supplierName);
            if (supplierData != null) {
                String supplierRealName = supplierData.getCompanyName();
                element.setSupplierRealName(supplierRealName);
            }
        });
    }

    /**
     * 检查订单编号是否存在
     *
     * @param param 采购收货销售发货管理
     * @return
     */
    public int checkPurchaseOrderId(PurchaseSaleOrderInfo param){
        return purchaseSaleOrderInfoMapper.checkPurchaseOrder(param);
    }

    @Override
    public List<JSONObject> selectTransportdocumentsDetailInfoByRelatedOrderIds(String[] relatedOrderIds) {
        return purchaseSaleOrderInfoMapper.selectTransportdocumentsDetailInfoByRelatedOrderIds(relatedOrderIds);
    }

    /**
     * double除法
     *
     * @param a
     * @param b
     * @param accurate 结果保留位数
     * @return
     */
    private double division(double a, double b, int accurate) {
        if (accurate < 0) {
            throw new RuntimeException("精确度必须是正整数或零");
        }
        BigDecimal b1 = new BigDecimal(a);
        BigDecimal b2 = new BigDecimal(b);
        return b1.divide(b2, accurate, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
