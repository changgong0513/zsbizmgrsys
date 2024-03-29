package com.ruoyi.transportdocuments.service;

import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.purchase.sale.domain.PurchaseSaleOrderInfo;
import com.ruoyi.transportdocuments.domain.TransportdocumentsDetailInfo;
import com.ruoyi.transportdocuments.domain.TransportdocumentsTraceInfo;

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

    /**
     * 导入运输单数据
     *
     * @param transportdocumentsList 运输单数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importTransportdocumentsData(List<TransportdocumentsDetailInfo> transportdocumentsList,
                                               Boolean isUpdateSupport,
                                               String operName,
                                               String transportdocumentsType);

    /**
     * 生成中转运输单数据。
     *
     * @param ids 需要生成中转运输单详细信息主键集合
     * @param data 生成中转运输单时，选择的运输方式、运载量和计量单位
     * @return
     */
    public int generateTransport(Long[] ids, JSONObject data);
}
