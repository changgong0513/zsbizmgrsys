package com.ruoyi.transportdocuments.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
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
     * 查询运输单追溯信息
     *
     * @param param 运输单追溯信息
     * @return 运输单追溯信息
     */
    public TransportdocumentsTraceInfo selectTransportdocumentsTraceInfo(TransportdocumentsTraceInfo param) {
        return transportdocumentsTraceInfoMapper.selectTransportdocumentsTraceInfo(param);
    }

    public TransportdocumentsTraceInfo selectTransportdocumentsTraceInfoByPost(final String transportdocumentsIds) {
        return transportdocumentsTraceInfoMapper.selectTransportdocumentsTraceInfoByPost(transportdocumentsIds);
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
     * 修改运输单追溯信息
     *
     * @param transportdocumentsTraceInfo 运输单追溯信息
     * @return 结果
     */
    public int updateByTempTransportdocumentsId(TransportdocumentsTraceInfo transportdocumentsTraceInfo) {
        transportdocumentsTraceInfo.setUpdateBy(SecurityUtils.getUsername());
        transportdocumentsTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsTraceInfoMapper.updateByTempTransportdocumentsId(transportdocumentsTraceInfo);
    }

    /**
     * 修改后置运输单追溯信息
     *
     * @param transportdocumentsTraceInfo 运输单追溯信息
     * @return 结果
     */
    public int updatePostTransportdocumentsId(TransportdocumentsTraceInfo transportdocumentsTraceInfo) {
        transportdocumentsTraceInfo.setUpdateBy(SecurityUtils.getUsername());
        transportdocumentsTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsTraceInfoMapper.updatePostTransportdocumentsId(transportdocumentsTraceInfo);
    }

    public int updatePreTransportdocumentsIdByTemp(TransportdocumentsTraceInfo transportdocumentsTraceInfo) {
        transportdocumentsTraceInfo.setUpdateBy(SecurityUtils.getUsername());
        transportdocumentsTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsTraceInfoMapper.updatePreTransportdocumentsIdByTemp(transportdocumentsTraceInfo);
    }

    public int updateTransportdocumentsIdByTemp(TransportdocumentsTraceInfo transportdocumentsTraceInfo) {
        transportdocumentsTraceInfo.setUpdateBy(SecurityUtils.getUsername());
        transportdocumentsTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsTraceInfoMapper.updateTransportdocumentsIdByTemp(transportdocumentsTraceInfo);
    }

    public int updatePostTransportdocumentsIdByTemp(TransportdocumentsTraceInfo transportdocumentsTraceInfo) {
        transportdocumentsTraceInfo.setUpdateBy(SecurityUtils.getUsername());
        transportdocumentsTraceInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsTraceInfoMapper.updatePostTransportdocumentsIdByTemp(transportdocumentsTraceInfo);
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

    public int deleteTransportdocumentsTraceInfoByPre(String[] preTransportdocumentsIds) {
        return transportdocumentsTraceInfoMapper.deleteTransportdocumentsTraceInfoByPre(preTransportdocumentsIds);
    }

    public int deleteTransportdocumentsTraceInfoByCurrent(String[] transportdocumentsIds) {
        return transportdocumentsTraceInfoMapper.deleteTransportdocumentsTraceInfoByCurrent(transportdocumentsIds);
    }

    public int deleteTransportdocumentsTraceInfoByPost(String[] postTransportdocumentsIds) {
        return transportdocumentsTraceInfoMapper.deleteTransportdocumentsTraceInfoByPost(postTransportdocumentsIds);
    }
}
