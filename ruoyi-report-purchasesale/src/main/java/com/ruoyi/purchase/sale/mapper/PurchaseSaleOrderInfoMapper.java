package com.ruoyi.purchase.sale.mapper;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.purchase.sale.domain.PurchaseSaleOrderInfo;

/**
 * 采购收货销售发货管理Mapper接口
 * 
 * @author changgong0513
 * @date 2022-11-03
 */
public interface PurchaseSaleOrderInfoMapper 
{
    /**
     * 查询采购收货销售发货管理
     *
     * @param ContractId 合同编号
     * @return 采购收货销售发货管理
     */
    public PurchaseSaleOrderInfo selectPurchaseSaleOrderInfoByContractId(String ContractId);

    /**
     * 查询采购收货销售发货管理
     * 
     * @param orderId 订单编号
     * @return 采购收货销售发货管理
     */
    public PurchaseSaleOrderInfo selectPurchaseSaleOrderInfoByOrderId(String orderId);

    /**
     * 查询采购收货销售发货管理列表
     * 
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 采购收货销售发货管理集合
     */
    public List<PurchaseSaleOrderInfo> selectPurchaseSaleOrderInfoList(PurchaseSaleOrderInfo purchaseSaleOrderInfo);

    /**
     * 查询采购收货管理列表
     *
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 采购收货集合
     */
    public List<PurchaseSaleOrderInfo> selectPurchaseOrderInfoUnionList(PurchaseSaleOrderInfo purchaseSaleOrderInfo);

    /**
     * 查询销售发货管理列表
     *
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 销售发货集合
     */
    public List<PurchaseSaleOrderInfo> selectSaleOrderInfoUnionList(PurchaseSaleOrderInfo purchaseSaleOrderInfo);

    /**
     * 取得采购合同总数
     *
     * @return 结果
     */
    public int getPurchaseContractCounts(String belongDept);

    /**
     * 取得销售合同总数
     *
     * @return 结果
     */
    public int getSaleContractCounts(String belongDept);

    /**
     * 新增采购收货销售发货管理
     * 
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 结果
     */
    public int insertPurchaseSaleOrderInfo(PurchaseSaleOrderInfo purchaseSaleOrderInfo);

    /**
     * 修改采购收货销售发货管理
     * 
     * @param purchaseSaleOrderInfo 采购收货销售发货管理
     * @return 结果
     */
    public int updatePurchaseSaleOrderInfo(PurchaseSaleOrderInfo purchaseSaleOrderInfo);

    /**
     * 删除采购收货销售发货管理
     * 
     * @param orderId 采购收货销售发货管理主键
     * @return 结果
     */
    public int deletePurchaseSaleOrderInfoByOrderId(String orderId);

    /**
     * 批量删除采购收货销售发货管理
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePurchaseSaleOrderInfoByOrderIds(String[] orderIds);

    /**
     * 检查订单编号是否存在
     *
     * @param param 采购收货销售发货管理
     * @return
     */
    public int checkPurchaseOrder(PurchaseSaleOrderInfo param);

    public List<JSONObject> selectTransportdocumentsDetailInfoByRelatedOrderIds(String[] relatedOrderIds);
}
