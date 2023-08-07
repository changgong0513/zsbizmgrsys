package com.ruoyi.transportdocuments.mapper;

import java.util.List;
import com.ruoyi.transportdocuments.domain.TransportdocumentsTraceInfo;

/**
 * 运输单追溯信息Mapper接口
 * 
 * @author changgong0513
 * @date 2023-08-07
 */
public interface TransportdocumentsTraceInfoMapper 
{
    /**
     * 查询运输单追溯信息
     * 
     * @param id 运输单追溯信息主键
     * @return 运输单追溯信息
     */
    public TransportdocumentsTraceInfo selectTransportdocumentsTraceInfoById(Long id);

    /**
     * 查询运输单追溯信息列表
     * 
     * @param transportdocumentsTraceInfo 运输单追溯信息
     * @return 运输单追溯信息集合
     */
    public List<TransportdocumentsTraceInfo> selectTransportdocumentsTraceInfoList(TransportdocumentsTraceInfo transportdocumentsTraceInfo);

    /**
     * 新增运输单追溯信息
     * 
     * @param transportdocumentsTraceInfo 运输单追溯信息
     * @return 结果
     */
    public int insertTransportdocumentsTraceInfo(TransportdocumentsTraceInfo transportdocumentsTraceInfo);

    /**
     * 修改运输单追溯信息
     * 
     * @param transportdocumentsTraceInfo 运输单追溯信息
     * @return 结果
     */
    public int updateTransportdocumentsTraceInfo(TransportdocumentsTraceInfo transportdocumentsTraceInfo);

    /**
     * 删除运输单追溯信息
     * 
     * @param id 运输单追溯信息主键
     * @return 结果
     */
    public int deleteTransportdocumentsTraceInfoById(Long id);

    /**
     * 批量删除运输单追溯信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTransportdocumentsTraceInfoByIds(Long[] ids);
}
