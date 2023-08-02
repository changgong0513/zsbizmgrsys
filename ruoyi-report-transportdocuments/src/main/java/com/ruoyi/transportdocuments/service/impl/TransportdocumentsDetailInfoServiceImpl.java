package com.ruoyi.transportdocuments.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.report.masterdata.domain.MasterDataMaterialInfo;
import com.ruoyi.report.masterdata.domain.MasterDataWarehouseBaseInfo;
import com.ruoyi.report.masterdata.mapper.MasterDataMaterialInfoMapper;
import com.ruoyi.report.masterdata.mapper.MasterDataWarehouseBaseInfoMapper;
import com.ruoyi.report.masterdata.service.IMasterDataWarehouseBaseInfoService;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.transportdocuments.domain.WarehouseInventoryInfo;
import com.ruoyi.transportdocuments.mapper.WarehouseInventoryInfoMapper;
import com.ruoyi.zjzy.mapper.ZjzyFkrlInfoMapper;
import com.ruoyi.zjzy.service.IZjzyFkrlInfoService;
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
    private IMasterDataWarehouseBaseInfoService masterDataWarehouseBaseInfoService;

    @Autowired
    private MasterDataMaterialInfoMapper masterDataMaterialInfoMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private IZjzyFkrlInfoService zjzyFkrlInfoService;

    @Autowired
    private WarehouseInventoryInfoMapper warehouseInventoryInfoMapper;

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
    public int updateTransportdocumentsDetailInfo(TransportdocumentsDetailInfo transportdocumentsDetailInfo) {
        transportdocumentsDetailInfo.setUpdateBy(SecurityUtils.getUsername());
        transportdocumentsDetailInfo.setUpdateTime(DateUtils.getNowDate());
        int result = transportdocumentsDetailInfoMapper.updateTransportdocumentsDetailInfo(transportdocumentsDetailInfo);
        if (0 < result) {
            // 处理资金占用
            JSONObject data =new JSONObject();
            data.put("pch", transportdocumentsDetailInfo.getPch());
            data.put("relatedContractId", transportdocumentsDetailInfo.getRelatedContractId());
            data.put("unitPrice", transportdocumentsDetailInfo.getUnitPrice());
            data.put("landedQuantity", transportdocumentsDetailInfo.getLandedQuantity());
            result = zjzyFkrlInfoService.handleZjzy(data);

            if (StringUtils.equals(transportdocumentsDetailInfo.getTransportdocumentsState(), "3")) {
                WarehouseInventoryInfo warehouseInventoryInfo = new WarehouseInventoryInfo();
                String warehouseCode = null;
                WarehouseInventoryInfo warehouseInventoryparam = new WarehouseInventoryInfo();
                if (StringUtils.equals(transportdocumentsDetailInfo.getTransportdocumentsType(), "p")) {
                    // 采购运输单
                    warehouseCode = transportdocumentsDetailInfo.getTargetPlaceId();
                    warehouseInventoryparam.setWarehouseCode(warehouseCode);
                    List<WarehouseInventoryInfo> list = warehouseInventoryInfoMapper.selectWarehouseInventoryInfoList(warehouseInventoryparam);
                    if (null !=null && 0 < list.size()) {
                        warehouseInventoryInfo = list.get(0);
                        warehouseInventoryInfo.setInventorySum(warehouseInventoryInfo.getInventorySum() + transportdocumentsDetailInfo.getLandedQuantity());
                        warehouseInventoryInfo.setUpdateBy(SecurityUtils.getUsername());
                        warehouseInventoryInfo.setUpdateTime(DateUtils.getNowDate());
                        warehouseInventoryInfo.setBizVersion(warehouseInventoryInfo.getBizVersion() + 1L);
                        warehouseInventoryInfoMapper.updateWarehouseInventoryInfo(warehouseInventoryInfo);
                    } else {
                        warehouseInventoryInfo.setWarehouseCode(transportdocumentsDetailInfo.getTargetPlaceId());
                        warehouseInventoryInfo.setWarehouseName(transportdocumentsDetailInfo.getTargetPlaceName());
                        warehouseInventoryInfo.setInventorySum(transportdocumentsDetailInfo.getLandedQuantity());
                        warehouseInventoryInfo.setCreateBy(SecurityUtils.getUsername());
                        warehouseInventoryInfo.setCreateTime(DateUtils.getNowDate());
                        warehouseInventoryInfo.setUpdateBy(SecurityUtils.getUsername());
                        warehouseInventoryInfo.setUpdateTime(DateUtils.getNowDate());
                        warehouseInventoryInfo.setBizVersion(1L);
                        warehouseInventoryInfoMapper.insertWarehouseInventoryInfo(warehouseInventoryInfo);
                    }
                } else {
                    warehouseCode = transportdocumentsDetailInfo.getSourcePlaceId();
                    warehouseInventoryparam.setWarehouseCode(warehouseCode);
                    List<WarehouseInventoryInfo> list = warehouseInventoryInfoMapper.selectWarehouseInventoryInfoList(warehouseInventoryparam);
                    if (null !=null && 0 < list.size()) {
                        warehouseInventoryInfo = list.get(0);
                        warehouseInventoryInfo.setInventorySum(warehouseInventoryInfo.getInventorySum() - transportdocumentsDetailInfo.getLandedQuantity());
                        warehouseInventoryInfo.setUpdateBy(SecurityUtils.getUsername());
                        warehouseInventoryInfo.setUpdateTime(DateUtils.getNowDate());
                        warehouseInventoryInfo.setBizVersion(warehouseInventoryInfo.getBizVersion() + 1L);
                        warehouseInventoryInfoMapper.updateWarehouseInventoryInfo(warehouseInventoryInfo);
                    }
                }
            }
        }

        return result;
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
     * @param transportdocumentsType 运输单类型
     * @return 结果
     */
    public String importTransportdocumentsData(List<TransportdocumentsDetailInfo> transportdocumentsList,
                                               Boolean isUpdateSupport, String operName, String transportdocumentsType) {
        if (StringUtils.isNull(transportdocumentsList) || transportdocumentsList.size() == 0) {
            throw new ServiceException("导入运输单数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        String transportdocumentsName = "采购运输单";
        if(StringUtils.equals(transportdocumentsType, "s")) {
            transportdocumentsName = "销售运输单";
        }

        for (TransportdocumentsDetailInfo data : transportdocumentsList) {

            try {
                BeanValidators.validateWithException(validator, data);

                if (StringUtils.equals(transportdocumentsType, "p") && StringUtils.isNotBlank(data.getTargetPlaceName())) {
                    String warehouseCode = findWarehouseCode(data.getTargetPlaceName());
                    if (StringUtils.isNotBlank(warehouseCode)) {
                        data.setSourcePlaceId(warehouseCode);
                    } else {
                        throw new Exception(transportdocumentsName + "中发货地输入错误！");
                    }
                }

                if (StringUtils.equals(transportdocumentsType, "s") && StringUtils.isNotBlank(data.getSourcePlaceName())) {
                    String warehouseCode = findWarehouseCode(data.getTargetPlaceName());
                    if (StringUtils.isNotBlank(warehouseCode)) {
                        data.setSourcePlaceId(warehouseCode);
                    } else {
                        throw new Exception(transportdocumentsName + "中发货地输入错误！");
                    }
                }

                SysUser sysUser = sysUserMapper.selectUserByUserName(data.getHandledByName());
                if (sysUser != null) {
                    data.setHandledById(sysUser.getUserId());
                } else {
                    throw new Exception(transportdocumentsName + "中经办人输入错误！");
                }

                MasterDataMaterialInfo materialParam = new MasterDataMaterialInfo();
                materialParam.setMaterialName(data.getMaterialName());
                List<MasterDataMaterialInfo> materialList = masterDataMaterialInfoMapper.selectMasterDataMaterialInfoList(materialParam);
                if (materialList != null && materialList.size() > 0) {
                    data.setMaterialId(Long.valueOf(materialList.get(0).getMaterialId()));
                } else {
                    throw new Exception(transportdocumentsName + "中物料输入错误！");
                }

                if (StringUtils.isBlank(data.getDocumentsType())) {
                    throw new Exception(transportdocumentsName + "中单据类型输入错误！");
                }

                if (StringUtils.isBlank(data.getTransportdocumentsState())) {
                    throw new Exception(transportdocumentsName + "中运输单状态输入错误！");
                }

                if (StringUtils.isNotBlank(data.getRelatedContractId())) {
                    data.setTransportdocumentsState("3");
                }

                data.setTransportdocumentsType(transportdocumentsType);
                data.setCreateBy(operName);
                data.setCreateTime(DateUtils.getNowDate());
                data.setUpdateBy(operName);
                data.setUpdateTime(DateUtils.getNowDate());
                data.setBizVersion(1L);
                insertTransportdocumentsDetailInfo(data);
                successNum++;
                successMsg.append("<br/>" + successNum + "、"+ transportdocumentsName + " " + data.getTransportdocumentsId() + " 导入成功");

            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、"+ transportdocumentsName +" " + data.getTransportdocumentsId() + " 导入失败：";
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

    /**
     * 查找仓库编码
     *
     * @param warehouseName
     * @return
     */
    private String findWarehouseCode(final String warehouseName) {
        MasterDataWarehouseBaseInfo warehouseParam = new MasterDataWarehouseBaseInfo();
        warehouseParam.setWarehouseName(warehouseName);
        List<MasterDataWarehouseBaseInfo> warehouseList = masterDataWarehouseBaseInfoService
                .selectMasterDataWarehouseBaseInfoList(warehouseParam);
        if (warehouseList != null && warehouseList.size() > 0) {
            return warehouseList.get(0).getWarehouseCode();
        }

        return null;
    }
}


