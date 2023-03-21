package com.ruoyi.purchase.sale.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 库存数据
 *
 */
public class KcckInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 仓库编码 */
    @Excel(name = "仓库编码")
    private String warehouseCode;

    /** 仓库名称 */
    @Excel(name = "仓库名称")
    private String warehouseName;

    /** 区划 */
    @Excel(name = "区划")
    private Integer warehouseRegion;

    /** 仓库地址 */
    @Excel(name = "仓库地址")
    private String warehouseAddress;

    /** 管理人员 */
    @Excel(name = "管理人员")
    private String warehouseManager;

    /** 联系方式1 */
    @Excel(name = "联系方式1")
    private String contactMobile1;

    /** 仓库类别 */
    @Excel(name = "仓库类别")
    private Integer warehouseCategory;

    /** 最大容量 */
    @Excel(name = "最大容量")
    private BigDecimal maximumCapacity;

    /** 总收货数量 */
    private BigDecimal sumReceiptQuantity;

    /** 总发货数量 */
    private BigDecimal sumSaleQuantity;

    /** 可用容量 */
    private BigDecimal availableCapacity;

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

    public Integer getWarehouseRegion() {
        return warehouseRegion;
    }

    public void setWarehouseRegion(Integer warehouseRegion) {
        this.warehouseRegion = warehouseRegion;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getWarehouseManager() {
        return warehouseManager;
    }

    public void setWarehouseManager(String warehouseManager) {
        this.warehouseManager = warehouseManager;
    }

    public String getContactMobile1() {
        return contactMobile1;
    }

    public void setContactMobile1(String contactMobile1) {
        this.contactMobile1 = contactMobile1;
    }

    public Integer getWarehouseCategory() {
        return warehouseCategory;
    }

    public void setWarehouseCategory(Integer warehouseCategory) {
        this.warehouseCategory = warehouseCategory;
    }

    public BigDecimal getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(BigDecimal maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public BigDecimal getSumReceiptQuantity() {
        return sumReceiptQuantity;
    }

    public void setSumReceiptQuantity(BigDecimal sumReceiptQuantity) {
        this.sumReceiptQuantity = sumReceiptQuantity;
    }

    public BigDecimal getSumSaleQuantity() {
        return sumSaleQuantity;
    }

    public void setSumSaleQuantity(BigDecimal sumSaleQuantity) {
        this.sumSaleQuantity = sumSaleQuantity;
    }

    public BigDecimal getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(BigDecimal availableCapacity) {
        this.availableCapacity = availableCapacity;
    }
}
