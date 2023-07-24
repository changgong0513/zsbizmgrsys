package com.ruoyi.transportdocuments.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.transportdocuments.mapper.TransportdocumentsBaseInfoMapper;
import com.ruoyi.transportdocuments.domain.TransportdocumentsBaseInfo;
import com.ruoyi.transportdocuments.service.ITransportdocumentsBaseInfoService;

/**
 * 运输单基本信息Service业务层处理
 * 
 * @author changgong0513
 * @date 2023-07-23
 */
@Service
public class TransportdocumentsBaseInfoServiceImpl implements ITransportdocumentsBaseInfoService 
{
    @Autowired
    private TransportdocumentsBaseInfoMapper transportdocumentsBaseInfoMapper;

    /**
     * 查询运输单基本信息
     * 
     * @param id 运输单基本信息主键
     * @return 运输单基本信息
     */
    @Override
    public TransportdocumentsBaseInfo selectTransportdocumentsBaseInfoById(Long id)
    {
        return transportdocumentsBaseInfoMapper.selectTransportdocumentsBaseInfoById(id);
    }

    /**
     * 查询运输单基本信息列表
     * 
     * @param transportdocumentsBaseInfo 运输单基本信息
     * @return 运输单基本信息
     */
    @Override
    public List<TransportdocumentsBaseInfo> selectTransportdocumentsBaseInfoList(TransportdocumentsBaseInfo transportdocumentsBaseInfo)
    {
        return transportdocumentsBaseInfoMapper.selectTransportdocumentsBaseInfoList(transportdocumentsBaseInfo);
    }

    /**
     * 新增运输单基本信息
     * 
     * @param transportdocumentsBaseInfo 运输单基本信息
     * @return 结果
     */
    @Override
    public int insertTransportdocumentsBaseInfo(TransportdocumentsBaseInfo transportdocumentsBaseInfo) {
        transportdocumentsBaseInfo.setCreateBy(SecurityUtils.getUsername());
        transportdocumentsBaseInfo.setCreateTime(DateUtils.getNowDate());
        transportdocumentsBaseInfo.setUpdateBy(SecurityUtils.getUsername());
        transportdocumentsBaseInfo.setUpdateTime(DateUtils.getNowDate());
        transportdocumentsBaseInfo.setBizVersion(0L);
        return transportdocumentsBaseInfoMapper.insertTransportdocumentsBaseInfo(transportdocumentsBaseInfo);
    }

    /**
     * 修改运输单基本信息
     * 
     * @param transportdocumentsBaseInfo 运输单基本信息
     * @return 结果
     */
    @Override
    public int updateTransportdocumentsBaseInfo(TransportdocumentsBaseInfo transportdocumentsBaseInfo)
    {
        transportdocumentsBaseInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsBaseInfoMapper.updateTransportdocumentsBaseInfo(transportdocumentsBaseInfo);
    }

    /**
     * 批量删除运输单基本信息
     * 
     * @param ids 需要删除的运输单基本信息主键
     * @return 结果
     */
    @Override
    public int deleteTransportdocumentsBaseInfoByIds(Long[] ids)
    {
        return transportdocumentsBaseInfoMapper.deleteTransportdocumentsBaseInfoByIds(ids);
    }

    /**
     * 删除运输单基本信息信息
     * 
     * @param id 运输单基本信息主键
     * @return 结果
     */
    @Override
    public int deleteTransportdocumentsBaseInfoById(Long id)
    {
        return transportdocumentsBaseInfoMapper.deleteTransportdocumentsBaseInfoById(id);
    }
}
