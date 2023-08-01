package com.ruoyi.transportdocuments.service.impl;

import java.math.BigDecimal;
import java.util.List;

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
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.transportdocuments.domain.WarehouseInventoryInfo;
import com.ruoyi.transportdocuments.mapper.WarehouseInventoryInfoMapper;
import com.ruoyi.zjzy.domain.ZjzyFkrlInfo;
import com.ruoyi.zjzy.mapper.ZjzyFkrlInfoMapper;
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
    private MasterDataWarehouseBaseInfoMapper masterDataWarehouseBaseInfoMapper;

    @Autowired
    private MasterDataMaterialInfoMapper masterDataMaterialInfoMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ZjzyFkrlInfoMapper zjzyFkrlInfoMapper;

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
//            ZjzyFkrlInfo zjzyFkrlInfo = new ZjzyFkrlInfo();
//            zjzyFkrlInfo.setFkrlBmbh(String.valueOf(SecurityUtils.getDeptId()));
//            zjzyFkrlInfo.setFkrlPch(transportdocumentsDetailInfo.getPch());
//            if (StringUtils.isNotBlank(transportdocumentsDetailInfo.getRelatedContractId())) {
//                zjzyFkrlInfo.setFkrlHtbh(transportdocumentsDetailInfo.getRelatedContractId());
//            } else {
//                zjzyFkrlInfo.setFkrlHtbh(null);
//            }
//
//            Long carriage = transportdocumentsDetailInfo.getUnitPrice() * transportdocumentsDetailInfo.getLandedQuantity();
//
//            List<ZjzyFkrlInfo> zjzyFkrlInfoList = null;
//            ZjzyFkrlInfo param = new ZjzyFkrlInfo();
//            param.setFkrlPch(transportdocumentsDetailInfo.getPch());
//            if (StringUtils.isNotBlank(transportdocumentsDetailInfo.getRelatedContractId())) {
//                param.setFkrlHtbh(transportdocumentsDetailInfo.getRelatedContractId());
//                zjzyFkrlInfoList = zjzyFkrlInfoMapper.selectZjzyFkrlInfoList(param);
//            } else {
//                zjzyFkrlInfoList = zjzyFkrlInfoMapper.selectZjzyFkrlInfoList(param);
//            }
//
//            if (null != zjzyFkrlInfoList && 0 < zjzyFkrlInfoList.size()) {
//                BigDecimal fkrlJe = zjzyFkrlInfoList.get(0).getFkrlJe();
//                zjzyFkrlInfo.setFkrlJe(fkrlJe.add(new BigDecimal(carriage)));
//                zjzyFkrlInfo.setBizVersion("1");
//                zjzyFkrlInfo.setUpdateBy(SecurityUtils.getUsername());
//                zjzyFkrlInfo.setUpdateTime(DateUtils.getNowDate());
//                result = zjzyFkrlInfoMapper.updateZjzyFkrlInfo(zjzyFkrlInfo);
//            } else {
//                zjzyFkrlInfo.setFkrlJe(new BigDecimal(carriage));
//                zjzyFkrlInfo.setBizVersion("1");
//                zjzyFkrlInfo.setCreateBy(SecurityUtils.getUsername());
//                zjzyFkrlInfo.setCreateTime(DateUtils.getNowDate());
//                zjzyFkrlInfo.setUpdateBy(SecurityUtils.getUsername());
//                zjzyFkrlInfo.setUpdateTime(DateUtils.getNowDate());
//                result = zjzyFkrlInfoMapper.insertZjzyFkrlInfo(zjzyFkrlInfo);
//            }
            result = handleZjzy(transportdocumentsDetailInfo);

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

        List<SysDictData> dictList = null;

        for (TransportdocumentsDetailInfo data : transportdocumentsList) {

            try {
                BeanValidators.validateWithException(validator, data);

                MasterDataWarehouseBaseInfo warehouseParam = new MasterDataWarehouseBaseInfo();
                warehouseParam.setWarehouseName(data.getSourcePlaceName());
                List<MasterDataWarehouseBaseInfo> warehouseList = masterDataWarehouseBaseInfoMapper
                        .selectMasterDataWarehouseBaseInfoList(warehouseParam);
                if (warehouseList != null && warehouseList.size() > 0) {
                    data.setSourcePlaceId(warehouseList.get(0).getWarehouseCode());
                } else {
                    throw new Exception("采购运输单中发货地输入错误！");
                }

                SysUser sysUser = sysUserMapper.selectUserByUserName(data.getHandledByName());
                if (sysUser != null) {
                    data.setHandledById(sysUser.getUserId());
                } else {
                    throw new Exception("采购运输单中经办人输入错误！");
                }

                MasterDataMaterialInfo materialParam = new MasterDataMaterialInfo();
                materialParam.setMaterialName(data.getMaterialName());
                List<MasterDataMaterialInfo> materialList = masterDataMaterialInfoMapper.selectMasterDataMaterialInfoList(materialParam);
                if (materialList != null && materialList.size() > 0) {
                    data.setMaterialId(Long.valueOf(materialList.get(0).getMaterialId()));
                } else {
                    throw new Exception("采购运输单中物料输入错误！");
                }

                if (StringUtils.isBlank(data.getDocumentsType())) {
                    throw new Exception("采购运输单中单据类型输入错误！");
                }

                if (StringUtils.isBlank(data.getTransportdocumentsState())) {
                    throw new Exception("采购运输单中运输单状态输入错误！");
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
                successMsg.append("<br/>" + successNum + "、采购运输单 " + data.getTransportdocumentsId() + " 导入成功");

            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、采购运输单 " + data.getTransportdocumentsId() + " 导入失败：";
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
     * 处理资金占用
     *
     * @param transportdocumentsDetailInfo
     * @return
     */
    private int handleZjzy(TransportdocumentsDetailInfo transportdocumentsDetailInfo) {
        int effectRows = 1;
        ZjzyFkrlInfo zjzyFkrlInfo = new ZjzyFkrlInfo();
        zjzyFkrlInfo.setFkrlBmbh(String.valueOf(SecurityUtils.getDeptId()));
        zjzyFkrlInfo.setFkrlPch(transportdocumentsDetailInfo.getPch());
        if (StringUtils.isNotBlank(transportdocumentsDetailInfo.getRelatedContractId())) {
            zjzyFkrlInfo.setFkrlHtbh(transportdocumentsDetailInfo.getRelatedContractId());
        } else {
            zjzyFkrlInfo.setFkrlHtbh(null);
        }

        Long carriage = transportdocumentsDetailInfo.getUnitPrice() * transportdocumentsDetailInfo.getLandedQuantity();

        List<ZjzyFkrlInfo> zjzyFkrlInfoList = null;
        ZjzyFkrlInfo param = new ZjzyFkrlInfo();
        param.setFkrlPch(transportdocumentsDetailInfo.getPch());
        if (StringUtils.isNotBlank(transportdocumentsDetailInfo.getRelatedContractId())) {
            param.setFkrlHtbh(transportdocumentsDetailInfo.getRelatedContractId());
        }

        zjzyFkrlInfoList = zjzyFkrlInfoMapper.selectZjzyFkrlInfoList(param);
        if (null != zjzyFkrlInfoList && 0 < zjzyFkrlInfoList.size()) {
            BigDecimal fkrlJe = zjzyFkrlInfoList.get(0).getFkrlJe();
            zjzyFkrlInfo.setFkrlJe(fkrlJe.add(new BigDecimal(carriage)));
            zjzyFkrlInfo.setBizVersion(String.valueOf(Integer.parseInt(zjzyFkrlInfoList.get(0).getBizVersion()) + 1));
            zjzyFkrlInfo.setUpdateBy(SecurityUtils.getUsername());
            zjzyFkrlInfo.setUpdateTime(DateUtils.getNowDate());
            effectRows = zjzyFkrlInfoMapper.updateZjzyFkrlInfo(zjzyFkrlInfo);
        } else {
            zjzyFkrlInfo.setFkrlJe(new BigDecimal(carriage));
            zjzyFkrlInfo.setBizVersion("1");
            zjzyFkrlInfo.setCreateBy(SecurityUtils.getUsername());
            zjzyFkrlInfo.setCreateTime(DateUtils.getNowDate());
            zjzyFkrlInfo.setUpdateBy(SecurityUtils.getUsername());
            zjzyFkrlInfo.setUpdateTime(DateUtils.getNowDate());
            effectRows = zjzyFkrlInfoMapper.insertZjzyFkrlInfo(zjzyFkrlInfo);
        }

        return effectRows;
    }
}


