package com.ruoyi.purchase.sale.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 采购收货销售发货管理对象 purchase_sale_order_info
 * 
 * @author changgong0513
 * @date 2022-11-03
 */
public class PurchaseSaleOrderInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderId;

    /** 订单状态 */
    @Excel(name = "订单状态")
    private String orderStatus;

    /** 采购类型 */
    private String purchaseType;

    /** 合同编号 */
    private String contractId;

    /** 合同类型 */
    private String contractType;

    /** 经办人 */
    @Excel(name = "经办人")
    private String handledBy;

    /** 所属部门编号 */
    private String belongDept;

    /** 业务日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date businessDate;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 采购数量 */
    private Long purchaseQuantity;

    /** 供应商名称编号 */
    private String supplierName;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierRealName;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 计量单位 */
    private String meteringUnit;

    /** 预计到货期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date arrivalDate;

    /** 要求交货期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date requiredDeliveryDate;

    /** 账期 */
    private Long accountPeriod;

    /** 到账条件 */
    private String arrivalTerms;

    /** 到账条件值 */
    private Long arrivalTermsValue;

    /** 结算方式 */
    private String settlementMethod;

    /** 是否开票 */
    private Long isInvoicing;

    /** 备注 */
    private String orderRemark;

    /** 版本号 */
    private Long bizVersion;

    /** 核算数量（来自于收货管理） */
    private Long checkQuantity;

    /** 核算金额（来自于发货管理） */
    private Long checkMoney;

    /** 核算金额Min（查询用） */
    private Long checkMoneyMin;

    /** 核算金额max（查询用） */
    private Long checkMoneyMax;

    /** 完成率（计算属性） */
    private String completionRate;

    /** 合同数量 */
    private int htsl;

    /** 所属部门名称 */
    @Excel(name = "所属部门")
    private String deptName;

    private String orderType;

    private String batchNumber;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public String getBelongDept() {
        return belongDept;
    }

    public void setBelongDept(String belongDept) {
        this.belongDept = belongDept;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Long purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierRealName() {
        return supplierRealName;
    }

    public void setSupplierRealName(String supplierRealName) {
        this.supplierRealName = supplierRealName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMeteringUnit() {
        return meteringUnit;
    }

    public void setMeteringUnit(String meteringUnit) {
        this.meteringUnit = meteringUnit;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Date getRequiredDeliveryDate() {
        return requiredDeliveryDate;
    }

    public void setRequiredDeliveryDate(Date requiredDeliveryDate) {
        this.requiredDeliveryDate = requiredDeliveryDate;
    }

    public Long getAccountPeriod() {
        return accountPeriod;
    }

    public void setAccountPeriod(Long accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public String getArrivalTerms() {
        return arrivalTerms;
    }

    public void setArrivalTerms(String arrivalTerms) {
        this.arrivalTerms = arrivalTerms;
    }

    public Long getArrivalTermsValue() {
        return arrivalTermsValue;
    }

    public void setArrivalTermsValue(Long arrivalTermsValue) {
        this.arrivalTermsValue = arrivalTermsValue;
    }

    public String getSettlementMethod() {
        return settlementMethod;
    }

    public void setSettlementMethod(String settlementMethod) {
        this.settlementMethod = settlementMethod;
    }

    public Long getIsInvoicing() {
        return isInvoicing;
    }

    public void setIsInvoicing(Long isInvoicing) {
        this.isInvoicing = isInvoicing;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public Long getBizVersion() {
        return bizVersion;
    }

    public void setBizVersion(Long bizVersion) {
        this.bizVersion = bizVersion;
    }

    public Long getCheckQuantity() {
        return checkQuantity;
    }

    public void setCheckQuantity(Long checkQuantity) {
        this.checkQuantity = checkQuantity;
    }

    public Long getCheckMoney() {
        return checkMoney;
    }

    public void setCheckMoney(Long checkMoney) {
        this.checkMoney = checkMoney;
    }

    public Long getCheckMoneyMin() {
        return checkMoneyMin;
    }

    public void setCheckMoneyMin(Long checkMoneyMin) {
        this.checkMoneyMin = checkMoneyMin;
    }

    public Long getCheckMoneyMax() {
        return checkMoneyMax;
    }

    public void setCheckMoneyMax(Long checkMoneyMax) {
        this.checkMoneyMax = checkMoneyMax;
    }

    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }

    public int getHtsl() {
        return htsl;
    }

    public void setHtsl(int htsl) {
        this.htsl = htsl;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderStatus", getOrderStatus())
            .append("purchaseType", getPurchaseType())
            .append("contractId", getContractId())
            .append("contractType", getContractType())
            .append("handledBy", getHandledBy())
            .append("belongDept", getBelongDept())
            .append("businessDate", getBusinessDate())
            .append("materialName", getMaterialName())
            .append("purchaseQuantity", getPurchaseQuantity())
            .append("supplierName", getSupplierName())
            .append("unitPrice", getUnitPrice())
            .append("meteringUnit", getMeteringUnit())
            .append("arrivalDate", getArrivalDate())
            .append("requiredDeliveryDate", getRequiredDeliveryDate())
            .append("accountPeriod", getAccountPeriod())
            .append("arrivalTerms", getArrivalTerms())
            .append("arrivalTermsValue", getArrivalTermsValue())
            .append("settlementMethod", getSettlementMethod())
            .append("isInvoicing", getIsInvoicing())
            .append("orderRemark", getOrderRemark())
            .append("checkQuantity", getCheckQuantity())
            .append("checkMoney", getCheckMoney())
            .append("bizVersion", getBizVersion())
            .toString();
    }
}
