package com.ruoyi.report.contract.mapper;

import java.util.List;
import com.ruoyi.report.contract.domain.ContractSyncLog;

/**
 * 合同同步日志Mapper接口
 * 
 * @author changgong
 * @date 2023-03-18
 */
public interface ContractSyncLogMapper 
{
    /**
     * 查询合同同步日志
     * 
     * @param syncLogId 合同同步日志主键
     * @return 合同同步日志
     */
    public ContractSyncLog selectContractSyncLogBySyncLogId(String syncLogId);

    /**
     * 查询合同同步日志列表
     * 
     * @param contractSyncLog 合同同步日志
     * @return 合同同步日志集合
     */
    public List<ContractSyncLog> selectContractSyncLogList(ContractSyncLog contractSyncLog);

    /**
     * 新增合同同步日志
     * 
     * @param contractSyncLog 合同同步日志
     * @return 结果
     */
    public int insertContractSyncLog(ContractSyncLog contractSyncLog);

    /**
     * 修改合同同步日志
     * 
     * @param contractSyncLog 合同同步日志
     * @return 结果
     */
    public int updateContractSyncLog(ContractSyncLog contractSyncLog);

    /**
     * 删除合同同步日志
     * 
     * @param syncLogId 合同同步日志主键
     * @return 结果
     */
    public int deleteContractSyncLogBySyncLogId(String syncLogId);

    /**
     * 批量删除合同同步日志
     * 
     * @param syncLogIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteContractSyncLogBySyncLogIds(String[] syncLogIds);
}
