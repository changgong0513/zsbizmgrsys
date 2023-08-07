package com.ruoyi.transportdocuments.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 运输单追溯信息对象 transportdocuments_trace_info
 * 
 * @author changgong0513
 * @date 2023-08-07
 */
public class TransportdocumentsTraceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 关联订单编号 */
    private String relatedOrderId;

    /** 前置运输单号 */
    private String preTransportdocumentsId;

    /** 前置运输单顺序 */
    private Long preTransportdocumentsSequence;

    /** 现在运输单号 */
    private String transportdocumentsId;

    /** 后置运输单号 */
    private String postTransportdocumentsId;

    /** 后置运输单顺序 */
    private Long postTransportdocumentsSequence;

    /** 版本号 */
    private Long bizVersion;

    /** 临时运输单编号 */
    private String tempTransportdocumentsId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRelatedOrderId(String relatedOrderId) 
    {
        this.relatedOrderId = relatedOrderId;
    }

    public String getRelatedOrderId() 
    {
        return relatedOrderId;
    }
    public void setPreTransportdocumentsId(String preTransportdocumentsId) 
    {
        this.preTransportdocumentsId = preTransportdocumentsId;
    }

    public String getPreTransportdocumentsId() 
    {
        return preTransportdocumentsId;
    }
    public void setPreTransportdocumentsSequence(Long preTransportdocumentsSequence) 
    {
        this.preTransportdocumentsSequence = preTransportdocumentsSequence;
    }

    public Long getPreTransportdocumentsSequence() 
    {
        return preTransportdocumentsSequence;
    }
    public void setTransportdocumentsId(String transportdocumentsId) 
    {
        this.transportdocumentsId = transportdocumentsId;
    }

    public String getTransportdocumentsId() 
    {
        return transportdocumentsId;
    }
    public void setPostTransportdocumentsId(String postTransportdocumentsId) 
    {
        this.postTransportdocumentsId = postTransportdocumentsId;
    }

    public String getPostTransportdocumentsId() 
    {
        return postTransportdocumentsId;
    }
    public void setPostTransportdocumentsSequence(Long postTransportdocumentsSequence) 
    {
        this.postTransportdocumentsSequence = postTransportdocumentsSequence;
    }

    public Long getPostTransportdocumentsSequence() 
    {
        return postTransportdocumentsSequence;
    }
    public void setBizVersion(Long bizVersion) 
    {
        this.bizVersion = bizVersion;
    }

    public Long getBizVersion() 
    {
        return bizVersion;
    }

    public String getTempTransportdocumentsId() {
        return tempTransportdocumentsId;
    }

    public void setTempTransportdocumentsId(String tempTransportdocumentsId) {
        this.tempTransportdocumentsId = tempTransportdocumentsId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("relatedOrderId", getRelatedOrderId())
            .append("preTransportdocumentsId", getPreTransportdocumentsId())
            .append("preTransportdocumentsSequence", getPreTransportdocumentsSequence())
            .append("transportdocumentsId", getTransportdocumentsId())
            .append("postTransportdocumentsId", getPostTransportdocumentsId())
            .append("postTransportdocumentsSequence", getPostTransportdocumentsSequence())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("bizVersion", getBizVersion())
            .toString();
    }
}
