package com.ruoyi.transportdocuments.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 运输单详细信息对象 transportdocuments_detail_info
 * 
 * @author changgong0513
 * @date 2023-07-28
 */
public class TransportdocumentsDetailInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 运输单号 */
    @Excel(name = "运输单号")
    private String transportdocumentsId;

    /** 关联订单编号 */
    @Excel(name = "关联订单编号")
    private String relatedOrderId;

    /** 运输单类型：采购运单（p）或者销售运单（s） */
    private String transportdocumentsType;

    /** 批次号 */
    @Excel(name = "批次号")
    private String pch;

    /** 车号 */
    @Excel(name = "车号")
    private String wagonNumber;

    /** 发货地编号 */
    private String sourcePlaceId;

    /** 发货地名称 */
    @Excel(name = "发货地")
    private String sourcePlaceName;

    /** 卸货地编号 */
    private String targetPlaceId;

    /** 卸货地名称 */
    @Excel(name = "卸货地")
    private String targetPlaceName;

    /** 装车数量 */
    @Excel(name = "装车数量")
    private Long loadingQuantity;

    /** 经办人编号 */
    private Long handledById;

    /** 经办人姓名 */
    @Excel(name = "经办人")
    private String handledByName;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String telephone;

    /** 物料编号 */
    private Long materialId;

    /** 物料名称 */
    @Excel(name = "物料")
    private String materialName;

    /** 业务日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date businessDate;

    /** 单据类型：转运和直销 */
    @Excel(name = "单据类型", dictType = "transportdocuments_documents_type")
    private String documentsType;

    /** 单价 */
    @Excel(name = "单价")
    private Long unitPrice;

    /** 关联合同编号 */
    @Excel(name = "关联合同编号")
    private String relatedContractId;

    /** 关联合同名称 */
    private String relatedContractName;

    /** 卸货数量 */
    private Long landedQuantity;

    /** 核算数量 */
    private Long accountingQuantity;

    /** 结算单价 */
    private Long settlementUnitPrice;

    /** 运费单价 */
    private Long freightUnitPrice;

    /** 扣款金额 */
    private Long deductionAmount;

    /** 压车费 */
    private Long followUpFare;

    /** 卸货日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date landedDate;

    /** 运输单状态：在途、中转和完成 */
    @Excel(name = "运输单状态", dictType = "transportdocuments_state")
    private String transportdocumentsState;

    /** 版本号 */
    private Long bizVersion;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTransportdocumentsId(String transportdocumentsId) 
    {
        this.transportdocumentsId = transportdocumentsId;
    }

    public String getTransportdocumentsId() 
    {
        return transportdocumentsId;
    }
    public void setTransportdocumentsType(String transportdocumentsType) 
    {
        this.transportdocumentsType = transportdocumentsType;
    }

    public String getTransportdocumentsType() 
    {
        return transportdocumentsType;
    }
    public void setPch(String pch) 
    {
        this.pch = pch;
    }

    public String getPch() 
    {
        return pch;
    }
    public void setWagonNumber(String wagonNumber) 
    {
        this.wagonNumber = wagonNumber;
    }

    public String getWagonNumber() 
    {
        return wagonNumber;
    }
    public void setSourcePlaceId(String sourcePlaceId) 
    {
        this.sourcePlaceId = sourcePlaceId;
    }

    public String getSourcePlaceId() 
    {
        return sourcePlaceId;
    }
    public void setSourcePlaceName(String sourcePlaceName) 
    {
        this.sourcePlaceName = sourcePlaceName;
    }

    public String getSourcePlaceName() 
    {
        return sourcePlaceName;
    }
    public void setTargetPlaceId(String targetPlaceId) 
    {
        this.targetPlaceId = targetPlaceId;
    }

    public String getTargetPlaceId() 
    {
        return targetPlaceId;
    }
    public void setTargetPlaceName(String targetPlaceName) 
    {
        this.targetPlaceName = targetPlaceName;
    }

    public String getTargetPlaceName() 
    {
        return targetPlaceName;
    }
    public void setLoadingQuantity(Long loadingQuantity) 
    {
        this.loadingQuantity = loadingQuantity;
    }

    public Long getLoadingQuantity() 
    {
        return loadingQuantity;
    }
    public void setHandledById(Long handledById) 
    {
        this.handledById = handledById;
    }

    public Long getHandledById() 
    {
        return handledById;
    }
    public void setHandledByName(String handledByName) 
    {
        this.handledByName = handledByName;
    }

    public String getHandledByName() 
    {
        return handledByName;
    }
    public void setTelephone(String telephone) 
    {
        this.telephone = telephone;
    }

    public String getTelephone() 
    {
        return telephone;
    }
    public void setMaterialId(Long materialId) 
    {
        this.materialId = materialId;
    }

    public Long getMaterialId() 
    {
        return materialId;
    }
    public void setMaterialName(String materialName) 
    {
        this.materialName = materialName;
    }

    public String getMaterialName() 
    {
        return materialName;
    }
    public void setBusinessDate(Date businessDate) 
    {
        this.businessDate = businessDate;
    }

    public Date getBusinessDate() 
    {
        return businessDate;
    }
    public void setDocumentsType(String documentsType) 
    {
        this.documentsType = documentsType;
    }

    public String getDocumentsType() 
    {
        return documentsType;
    }
    public void setUnitPrice(Long unitPrice) 
    {
        this.unitPrice = unitPrice;
    }

    public Long getUnitPrice() 
    {
        return unitPrice;
    }
    public void setRelatedOrderId(String relatedOrderId) 
    {
        this.relatedOrderId = relatedOrderId;
    }

    public String getRelatedOrderId() 
    {
        return relatedOrderId;
    }
    public void setRelatedContractId(String relatedContractId) 
    {
        this.relatedContractId = relatedContractId;
    }

    public String getRelatedContractId() 
    {
        return relatedContractId;
    }
    public void setRelatedContractName(String relatedContractName) 
    {
        this.relatedContractName = relatedContractName;
    }

    public String getRelatedContractName() 
    {
        return relatedContractName;
    }
    public void setLandedQuantity(Long landedQuantity) 
    {
        this.landedQuantity = landedQuantity;
    }

    public Long getLandedQuantity() 
    {
        return landedQuantity;
    }
    public void setAccountingQuantity(Long accountingQuantity) 
    {
        this.accountingQuantity = accountingQuantity;
    }

    public Long getAccountingQuantity() 
    {
        return accountingQuantity;
    }
    public void setSettlementUnitPrice(Long settlementUnitPrice) 
    {
        this.settlementUnitPrice = settlementUnitPrice;
    }

    public Long getSettlementUnitPrice() 
    {
        return settlementUnitPrice;
    }
    public void setFreightUnitPrice(Long freightUnitPrice) 
    {
        this.freightUnitPrice = freightUnitPrice;
    }

    public Long getFreightUnitPrice() 
    {
        return freightUnitPrice;
    }
    public void setDeductionAmount(Long deductionAmount) 
    {
        this.deductionAmount = deductionAmount;
    }

    public Long getDeductionAmount() 
    {
        return deductionAmount;
    }
    public void setFollowUpFare(Long followUpFare) 
    {
        this.followUpFare = followUpFare;
    }

    public Long getFollowUpFare() 
    {
        return followUpFare;
    }
    public void setLandedDate(Date landedDate) 
    {
        this.landedDate = landedDate;
    }

    public Date getLandedDate() 
    {
        return landedDate;
    }
    public void setTransportdocumentsState(String transportdocumentsState) 
    {
        this.transportdocumentsState = transportdocumentsState;
    }

    public String getTransportdocumentsState() 
    {
        return transportdocumentsState;
    }
    public void setBizVersion(Long bizVersion) 
    {
        this.bizVersion = bizVersion;
    }

    public Long getBizVersion() 
    {
        return bizVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("transportdocumentsId", getTransportdocumentsId())
            .append("transportdocumentsType", getTransportdocumentsType())
            .append("pch", getPch())
            .append("wagonNumber", getWagonNumber())
            .append("sourcePlaceId", getSourcePlaceId())
            .append("sourcePlaceName", getSourcePlaceName())
            .append("targetPlaceId", getTargetPlaceId())
            .append("targetPlaceName", getTargetPlaceName())
            .append("loadingQuantity", getLoadingQuantity())
            .append("handledById", getHandledById())
            .append("handledByName", getHandledByName())
            .append("telephone", getTelephone())
            .append("materialId", getMaterialId())
            .append("materialName", getMaterialName())
            .append("businessDate", getBusinessDate())
            .append("documentsType", getDocumentsType())
            .append("unitPrice", getUnitPrice())
            .append("relatedOrderId", getRelatedOrderId())
            .append("relatedContractId", getRelatedContractId())
            .append("relatedContractName", getRelatedContractName())
            .append("landedQuantity", getLandedQuantity())
            .append("accountingQuantity", getAccountingQuantity())
            .append("settlementUnitPrice", getSettlementUnitPrice())
            .append("freightUnitPrice", getFreightUnitPrice())
            .append("deductionAmount", getDeductionAmount())
            .append("followUpFare", getFollowUpFare())
            .append("landedDate", getLandedDate())
            .append("transportdocumentsState", getTransportdocumentsState())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("bizVersion", getBizVersion())
            .toString();
    }
}
