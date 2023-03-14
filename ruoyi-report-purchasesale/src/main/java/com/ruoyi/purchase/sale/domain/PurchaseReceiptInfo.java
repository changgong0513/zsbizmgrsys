package com.ruoyi.purchase.sale.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 收货管理对象 purchase_receipt_info
 * 
 * @author changgong0513
 * @date 2022-11-05
 */
public class PurchaseReceiptInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 收货编号 */
    @Excel(name = "收货编号")
    private String receiptId;

    /** 采购订单编号 */
    @Excel(name = "采购订单编号")
    private String purchaseOrderId;

    /** 采购合同编号 */
    private String purchaseContractId;

    /** 经办人 */
    @Excel(name = "经办人")
    private String handledBy;

    /** 收货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收货日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date receiptDate;

    /** 供应商名称（编号） */
    private String supplierName;

    /** 供应商名称 */
    private String supplierRealName;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 仓库编码 */
    private String warehouseCode;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 批次号 */
    private String batchNo;

    /** 运输方式 */
    private String transportMode;

    /** 运输单号 */
    private String transportNumber;

    /** 预期收货数量 */
    private BigDecimal expectReceiptQuantity;

    /** 核算数量 */
    private BigDecimal checkQuantity;

    /** 核算单价 */
    private BigDecimal checkPrice;

    /** 核算金额 */
    private BigDecimal checkMoney;

    /** 货损数量 */
    private BigDecimal cargoDamageQuantity;

    /** 货损金额 */
    private BigDecimal cargoDamageMoney;

    /** 备注 */
    private String receiptRemark;

    /** 水分值 */
    private Double dryCalWaterValue;

    /** 烘干率 */
    private Double dryCalDryingRate;

    /** 比例范围 */
    private Double dryCalScaleRange;

    /** 结算重量 */
    private Double dryCalSettlementWeight;

    /** 计算结果 */
    private Double dryCalResult;

    /** 版本号 */
    private Long bizVersion;

    /** 车船编号 */
    private String ccbh;

    /** 合同单价 */
    private BigDecimal htdj;

    /** 收货状态 */
    private String receiptStatus;

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPurchaseContractId() {
        return purchaseContractId;
    }

    public void setPurchaseContractId(String purchaseContractId) {
        this.purchaseContractId = purchaseContractId;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getTransportNumber() {
        return transportNumber;
    }

    public void setTransportNumber(String transportNumber) {
        this.transportNumber = transportNumber;
    }

    public BigDecimal getExpectReceiptQuantity() {
        return expectReceiptQuantity;
    }

    public void setExpectReceiptQuantity(BigDecimal expectReceiptQuantity) {
        this.expectReceiptQuantity = expectReceiptQuantity;
    }

    public BigDecimal getCheckQuantity() {
        return checkQuantity;
    }

    public void setCheckQuantity(BigDecimal checkQuantity) {
        this.checkQuantity = checkQuantity;
    }

    public BigDecimal getCheckPrice() {
        return checkPrice;
    }

    public void setCheckPrice(BigDecimal checkPrice) {
        this.checkPrice = checkPrice;
    }

    public BigDecimal getCheckMoney() {
        return checkMoney;
    }

    public void setCheckMoney(BigDecimal checkMoney) {
        this.checkMoney = checkMoney;
    }

    public BigDecimal getCargoDamageQuantity() {
        return cargoDamageQuantity;
    }

    public void setCargoDamageQuantity(BigDecimal cargoDamageQuantity) {
        this.cargoDamageQuantity = cargoDamageQuantity;
    }

    public BigDecimal getCargoDamageMoney() {
        return cargoDamageMoney;
    }

    public void setCargoDamageMoney(BigDecimal cargoDamageMoney) {
        this.cargoDamageMoney = cargoDamageMoney;
    }

    public String getReceiptRemark() {
        return receiptRemark;
    }

    public void setReceiptRemark(String receiptRemark) {
        this.receiptRemark = receiptRemark;
    }

    public Double getDryCalWaterValue() {
        return dryCalWaterValue;
    }

    public void setDryCalWaterValue(Double dryCalWaterValue) {
        this.dryCalWaterValue = dryCalWaterValue;
    }

    public Double getDryCalDryingRate() {
        return dryCalDryingRate;
    }

    public void setDryCalDryingRate(Double dryCalDryingRate) {
        this.dryCalDryingRate = dryCalDryingRate;
    }

    public Double getDryCalScaleRange() {
        return dryCalScaleRange;
    }

    public void setDryCalScaleRange(Double dryCalScaleRange) {
        this.dryCalScaleRange = dryCalScaleRange;
    }

    public Double getDryCalSettlementWeight() {
        return dryCalSettlementWeight;
    }

    public void setDryCalSettlementWeight(Double dryCalSettlementWeight) {
        this.dryCalSettlementWeight = dryCalSettlementWeight;
    }

    public Double getDryCalResult() {
        return dryCalResult;
    }

    public void setDryCalResult(Double dryCalResult) {
        this.dryCalResult = dryCalResult;
    }

    public Long getBizVersion() {
        return bizVersion;
    }

    public void setBizVersion(Long bizVersion) {
        this.bizVersion = bizVersion;
    }

    public String getCcbh() {
        return ccbh;
    }

    public void setCcbh(String ccbh) {
        this.ccbh = ccbh;
    }

    public BigDecimal getHtdj() {
        return htdj;
    }

    public void setHtdj(BigDecimal htdj) {
        this.htdj = htdj;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("receiptId", getReceiptId())
            .append("purchaseOrderId", getPurchaseOrderId())
            .append("purchaseContractId", getPurchaseContractId())
            .append("handledBy", getHandledBy())
            .append("receiptDate", getReceiptDate())
            .append("supplierName", getSupplierName())
            .append("materialName", getMaterialName())
            .append("warehouseCode", getWarehouseCode())
            .append("warehouseName", getWarehouseName())
            .append("batchNo", getBatchNo())
            .append("transportMode", getTransportMode())
            .append("transportNumber", getTransportNumber())
            .append("expectReceiptQuantity", getExpectReceiptQuantity())
            .append("checkQuantity", getCheckQuantity())
            .append("checkPrice", getCheckPrice())
            .append("checkMoney", getCheckMoney())
            .append("cargoDamageQuantity", getCargoDamageQuantity())
            .append("cargoDamageMoney", getCargoDamageMoney())
            .append("receiptRemark", getReceiptRemark())
            .append("dryCalWaterValue", getDryCalWaterValue())
            .append("dryCalDryingRate", getDryCalDryingRate())
            .append("dryCalScaleRange", getDryCalScaleRange())
            .append("dryCalResult", getDryCalResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("bizVersion", getBizVersion())
            .toString();
    }
}
