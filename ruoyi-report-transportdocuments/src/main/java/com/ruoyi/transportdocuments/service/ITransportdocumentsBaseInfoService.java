package com.ruoyi.transportdocuments.service;

import java.util.List;
import com.ruoyi.transportdocuments.domain.TransportdocumentsBaseInfo;

/**
 * 运输单基本信息Service接口
 * 
 * @author changgong0513
 * @date 2023-07-23
 */
public interface ITransportdocumentsBaseInfoService 
{
    /**
     * 查询运输单基本信息
     * 
     * @param id 运输单基本信息主键
     * @return 运输单基本信息
     */
    public TransportdocumentsBaseInfo selectTransportdocumentsBaseInfoById(Long id);

    /**
     * 查询运输单基本信息列表
     * 
     * @param transportdocumentsBaseInfo 运输单基本信息
     * @return 运输单基本信息集合
     */
    public List<TransportdocumentsBaseInfo> selectTransportdocumentsBaseInfoList(TransportdocumentsBaseInfo transportdocumentsBaseInfo);

    /**
     * 新增运输单基本信息
     * 
     * @param transportdocumentsBaseInfo 运输单基本信息
     * @return 结果
     */
    public int insertTransportdocumentsBaseInfo(TransportdocumentsBaseInfo transportdocumentsBaseInfo);

    /**
     * 修改运输单基本信息
     * 
     * @param transportdocumentsBaseInfo 运输单基本信息
     * @return 结果
     */
    public int updateTransportdocumentsBaseInfo(TransportdocumentsBaseInfo transportdocumentsBaseInfo);

    /**
     * 批量删除运输单基本信息
     * 
     * @param ids 需要删除的运输单基本信息主键集合
     * @return 结果
     */
    public int deleteTransportdocumentsBaseInfoByIds(Long[] ids);

    /**
     * 删除运输单基本信息信息
     * 
     * @param id 运输单基本信息主键
     * @return 结果
     */
    public int deleteTransportdocumentsBaseInfoById(Long id);
}
