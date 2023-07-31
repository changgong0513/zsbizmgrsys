package com.ruoyi.transportdocuments.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.transportdocuments.mapper.TransportdocumentsDetailInfoMapper;
import com.ruoyi.transportdocuments.domain.TransportdocumentsDetailInfo;
import com.ruoyi.transportdocuments.service.ITransportdocumentsDetailInfoService;

/**
 * 运输单详细信息Service业务层处理
 * 
 * @author changgong0513
 * @date 2023-07-28
 */
@Service
public class TransportdocumentsDetailInfoServiceImpl implements ITransportdocumentsDetailInfoService 
{
    @Autowired
    private TransportdocumentsDetailInfoMapper transportdocumentsDetailInfoMapper;

    /**
     * 查询运输单详细信息
     * 
     * @param id 运输单详细信息主键
     * @return 运输单详细信息
     */
    @Override
    public TransportdocumentsDetailInfo selectTransportdocumentsDetailInfoById(Long id)
    {
        return transportdocumentsDetailInfoMapper.selectTransportdocumentsDetailInfoById(id);
    }

    /**
     * 查询运输单详细信息列表
     * 
     * @param transportdocumentsDetailInfo 运输单详细信息
     * @return 运输单详细信息
     */
    @Override
    public List<TransportdocumentsDetailInfo> selectTransportdocumentsDetailInfoList(TransportdocumentsDetailInfo transportdocumentsDetailInfo)
    {
        return transportdocumentsDetailInfoMapper.selectTransportdocumentsDetailInfoList(transportdocumentsDetailInfo);
    }

    /**
     * 新增运输单详细信息
     * 
     * @param transportdocumentsDetailInfo 运输单详细信息
     * @return 结果
     */
    @Override
    public int insertTransportdocumentsDetailInfo(TransportdocumentsDetailInfo transportdocumentsDetailInfo) {
        transportdocumentsDetailInfo.setCreateBy(SecurityUtils.getUsername());
        transportdocumentsDetailInfo.setCreateTime(DateUtils.getNowDate());
        transportdocumentsDetailInfo.setUpdateBy(SecurityUtils.getUsername());
        transportdocumentsDetailInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsDetailInfoMapper.insertTransportdocumentsDetailInfo(transportdocumentsDetailInfo);
    }

    /**
     * 修改运输单详细信息
     * 
     * @param transportdocumentsDetailInfo 运输单详细信息
     * @return 结果
     */
    @Override
    public int updateTransportdocumentsDetailInfo(TransportdocumentsDetailInfo transportdocumentsDetailInfo)
    {
        transportdocumentsDetailInfo.setUpdateTime(DateUtils.getNowDate());
        return transportdocumentsDetailInfoMapper.updateTransportdocumentsDetailInfo(transportdocumentsDetailInfo);
    }

    /**
     * 批量删除运输单详细信息
     * 
     * @param ids 需要删除的运输单详细信息主键
     * @return 结果
     */
    @Override
    public int deleteTransportdocumentsDetailInfoByIds(Long[] ids)
    {
        return transportdocumentsDetailInfoMapper.deleteTransportdocumentsDetailInfoByIds(ids);
    }

    /**
     * 删除运输单详细信息信息
     * 
     * @param id 运输单详细信息主键
     * @return 结果
     */
    @Override
    public int deleteTransportdocumentsDetailInfoById(Long id)
    {
        return transportdocumentsDetailInfoMapper.deleteTransportdocumentsDetailInfoById(id);
    }
}
