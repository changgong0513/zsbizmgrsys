package com.ruoyi.transportdocuments.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.transportdocuments.mapper.TransportdocumentsDetailInfoMapper;
import com.ruoyi.transportdocuments.domain.TransportdocumentsDetailInfo;
import com.ruoyi.transportdocuments.service.ITransportdocumentsDetailInfoService;

import javax.validation.Validator;

/**
 * 运输单详细信息Service业务层处理
 * 
 * @author changgong0513
 * @date 2023-07-28
 */
@Service
public class TransportdocumentsDetailInfoServiceImpl implements ITransportdocumentsDetailInfoService {

    private static final Logger log = LoggerFactory.getLogger(TransportdocumentsDetailInfoServiceImpl.class);

    @Autowired
    private TransportdocumentsDetailInfoMapper transportdocumentsDetailInfoMapper;

    @Autowired
    protected Validator validator;

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

    /**
     * 导入运输单数据
     *
     * @param transportdocumentsList 运输单数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importTransportdocumentsData(List<TransportdocumentsDetailInfo> transportdocumentsList,
                                               Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(transportdocumentsList) || transportdocumentsList.size() == 0) {
            throw new ServiceException("导入运输单数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

//        MasterDataClientInfo masterDataClientInfo = new MasterDataClientInfo();

        for (TransportdocumentsDetailInfo transportdocumentsDetailInfo : transportdocumentsList) {

            try {
                BeanValidators.validateWithException(validator, transportdocumentsDetailInfo);
//                masterDataClientInfo.setCompanyName(hkData.getHkKhmc());
//                List<MasterDataClientInfo> khList = masterDataClientInfoService.selectMasterDataClientInfoList(masterDataClientInfo);
//                hkData.setHkKhbh(khList.get(0).getBaseId());
//                hkData.setBizVersion(1L);
//                hkData.setCreateTime(DateUtils.getNowDate());
//                hkData.setUpdateTime(DateUtils.getNowDate());
//                hkData.setCreateBy(operName);
//                hkData.setUpdateBy(operName);
//                insertZjzyHkInfo(hkData);
                successNum++;
                successMsg.append("<br/>" + successNum + "、账号 " + transportdocumentsDetailInfo.getTransportdocumentsId() + " 导入成功");

            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、合同 " + transportdocumentsDetailInfo.getTransportdocumentsId() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }

        return successMsg.toString();
    }
}


