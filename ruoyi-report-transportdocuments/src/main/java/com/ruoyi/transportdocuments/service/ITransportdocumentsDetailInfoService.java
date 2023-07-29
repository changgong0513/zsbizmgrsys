package com.ruoyi.transportdocuments.service;

import java.util.List;
import com.ruoyi.transportdocuments.domain.TransportdocumentsDetailInfo;

/**
 * 运输单详细信息Service接口
 * 
 * @author changgong0513
 * @date 2023-07-28
 */
public interface ITransportdocumentsDetailInfoService 
{
    /**
     * 查询运输单详细信息
     * 
     * @param id 运输单详细信息主键
     * @return 运输单详细信息
     */
    public TransportdocumentsDetailInfo selectTransportdocumentsDetailInfoById(Long id);

    /**
     * 查询运输单详细信息列表
     * 
     * @param transportdocumentsDetailInfo 运输单详细信息
     * @return 运输单详细信息集合
     */
    public List<TransportdocumentsDetailInfo> selectTransportdocumentsDetailInfoList(TransportdocumentsDetailInfo transportdocumentsDetailInfo);

    /**
     * 新增运输单详细信息
     * 
     * @param transportdocumentsDetailInfo 运输单详细信息
     * @return 结果
     */
    public int insertTransportdocumentsDetailInfo(TransportdocumentsDetailInfo transportdocumentsDetailInfo);

    /**
     * 修改运输单详细信息
     * 
     * @param transportdocumentsDetailInfo 运输单详细信息
     * @return 结果
     */
    public int updateTransportdocumentsDetailInfo(TransportdocumentsDetailInfo transportdocumentsDetailInfo);

    /**
     * 批量删除运输单详细信息
     * 
     * @param ids 需要删除的运输单详细信息主键集合
     * @return 结果
     */
    public int deleteTransportdocumentsDetailInfoByIds(Long[] ids);

    /**
     * 删除运输单详细信息信息
     * 
     * @param id 运输单详细信息主键
     * @return 结果
     */
    public int deleteTransportdocumentsDetailInfoById(Long id);
}