package com.ruoyi.report.contract.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 合同同步日志对象 contract_sync_log
 * 
 * @author changgong
 * @date 2023-03-18
 */
public class ContractSyncLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 同步日志编号 */
    private String syncLogId;

    /** 合同编号 */
    @Excel(name = "合同编号")
    private String contractId;

    /** 合同类型 */
    @Excel(name = "合同类型")
    private String contractType;

    /** 同步时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "同步时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date syncTime;

    /** 部门编号 */
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 同步状态 */
    @Excel(name = "同步状态")
    private String syncStatus;

    /** 状态描述
 */
    @Excel(name = "状态描述 ")
    private String statusDescription;

    public void setSyncLogId(String syncLogId) 
    {
        this.syncLogId = syncLogId;
    }

    public String getSyncLogId() 
    {
        return syncLogId;
    }
    public void setContractId(String contractId) 
    {
        this.contractId = contractId;
    }

    public String getContractId() 
    {
        return contractId;
    }
    public void setContractType(String contractType) 
    {
        this.contractType = contractType;
    }

    public String getContractType() 
    {
        return contractType;
    }
    public void setSyncTime(Date syncTime) 
    {
        this.syncTime = syncTime;
    }

    public Date getSyncTime() 
    {
        return syncTime;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }
    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }
    public void setSyncStatus(String syncStatus) 
    {
        this.syncStatus = syncStatus;
    }

    public String getSyncStatus() 
    {
        return syncStatus;
    }
    public void setStatusDescription(String statusDescription) 
    {
        this.statusDescription = statusDescription;
    }

    public String getStatusDescription() 
    {
        return statusDescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("syncLogId", getSyncLogId())
            .append("contractId", getContractId())
            .append("contractType", getContractType())
            .append("syncTime", getSyncTime())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("syncStatus", getSyncStatus())
            .append("statusDescription", getStatusDescription())
            .toString();
    }
}
