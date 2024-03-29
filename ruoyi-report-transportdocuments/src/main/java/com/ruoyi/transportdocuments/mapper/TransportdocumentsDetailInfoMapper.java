package com.ruoyi.transportdocuments.mapper;

import java.util.List;
import com.ruoyi.transportdocuments.domain.TransportdocumentsDetailInfo;

/**
 * 运输单详细信息Mapper接口
 * 
 * @author changgong0513
 * @date 2023-07-28
 */
public interface TransportdocumentsDetailInfoMapper 
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
     * @param ids 运输单详细信息主键数组
     * @return 运输单详细信息集合
     */
    public List<TransportdocumentsDetailInfo> selectTransportdocumentsDetailInfoByIds(Long[] ids);

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
     * 删除运输单详细信息
     * 
     * @param id 运输单详细信息主键
     * @return 结果
     */
    public int deleteTransportdocumentsDetailInfoById(Long id);

    /**
     * 批量删除运输单详细信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTransportdocumentsDetailInfoByIds(Long[] ids);
}
