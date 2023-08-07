package com.ruoyi.transportdocuments.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.transportdocuments.mapper.TransportdocumentsTraceInfoMapper;
import com.ruoyi.transportdocuments.domain.TransportdocumentsTraceInfo;
import com.ruoyi.transportdocuments.service.ITransportdocumentsTraceInfoService;

/**
 * 运输单追溯信息Service业务层处理
 * 
 * @author changgong0513
 * @date 2023-08-07
 */
@Service
public class TransportdocumentsTraceInfoServiceImpl implements ITransportdocumentsTraceInfoService 
{
    @Autowired
    private TransportdocumentsTraceInfoMapper transportdocumentsTraceInfoMapper;

    /**
     * 查询运输单追溯信息
     * 
     * @param id 运输单追溯信息主键
     * @return 运输单追溯信息
     */
    @Override
    public TransportdocumentsTraceInfo selectTransportdocumentsTraceInfoById(Long id)
    {
        return transportdocumentsTraceInfoMapper.selectTransportdocumentsTraceInfoById(id);
    }

    /**
     * 查询运输单追溯信息列表
     * 
     * @param transportdocumentsTraceInfo 运输单追溯信息
     * @return 运输单追溯信息
     */
    @Override
    public List<TransportdocumentsTraceInfo> selectTransportdocumentsTraceInfoList(TransportdocumentsTraceInfo transportdocumentsTraceInfo)
    {
        return transportdocumentsTraceInfoMapper.selectTransportdocumentsTraceInfoList(transportdocumentsTraceInfo);
    }

    /**
     * 新增运输单追溯信息
     * 
     * @param transportdocumentsTraceInfo 运输单追溯信息
     * @return 结果
     */
    @Override
    public int insertTransportdocumentsTraceInfo(TransportdocumentsTraceInfo transportdocumentsTraceInfo)
    {
        transportdocumentsTraceInfo.setCreateTime(DateUtils.getNowDate());
        return transportdocumentsTraceInfoMapper.insertTransportdocumentsTraceInfo(transportdocumentsTraceInfo);
    }

    /**
     * 修改运输单追溯信息
     * 
     * @param transportdocumentsTraceInfo 运输单追溯信息
     * @return 结果
     */
    @Override
    public int updateTransportdocumentsTraceInfo(TransportdocumentsTraceInfo transportdocumentsTraceInfo)
    {
        transportdocumentsTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsTraceInfoMapper.updateTransportdocumentsTraceInfo(transportdocumentsTraceInfo);
    }

    /**
     * 批量删除运输单追溯信息
     * 
     * @param ids 需要删除的运输单追溯信息主键
     * @return 结果
     */
    @Override
    public int deleteTransportdocumentsTraceInfoByIds(Long[] ids)
    {
        return transportdocumentsTraceInfoMapper.deleteTransportdocumentsTraceInfoByIds(ids);
    }

    /**
     * 删除运输单追溯信息信息
     * 
     * @param id 运输单追溯信息主键
     * @return 结果
     */
    @Override
    public int deleteTransportdocumentsTraceInfoById(Long id)
    {
        return transportdocumentsTraceInfoMapper.deleteTransportdocumentsTraceInfoById(id);
    }
}
