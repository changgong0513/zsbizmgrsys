package com.ruoyi.zjzy.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.zjzy.mapper.ZjzyFkrlInfoMapper;
import com.ruoyi.zjzy.domain.ZjzyFkrlInfo;
import com.ruoyi.zjzy.service.IZjzyFkrlInfoService;

/**
 * 付款认领Service业务层处理
 * 
 * @author changgong0513
 * @date 2022-12-07
 */
@Service
public class ZjzyFkrlInfoServiceImpl implements IZjzyFkrlInfoService 
{
    @Autowired
    private ZjzyFkrlInfoMapper zjzyFkrlInfoMapper;

    /**
     * 查询付款认领
     * 
     * @param fkrlId 付款认领主键
     * @return 付款认领
     */
    @Override
    public ZjzyFkrlInfo selectZjzyFkrlInfoByFkrlId(String fkrlId)
    {
        return zjzyFkrlInfoMapper.selectZjzyFkrlInfoByFkrlId(fkrlId);
    }

    /**
     * 查询付款认领列表
     * 
     * @param zjzyFkrlInfo 付款认领
     * @return 付款认领
     */
    @Override
    public List<ZjzyFkrlInfo> selectZjzyFkrlInfoList(ZjzyFkrlInfo zjzyFkrlInfo)
    {
        return zjzyFkrlInfoMapper.selectZjzyFkrlInfoList(zjzyFkrlInfo);
    }

    /**
     * 新增付款认领
     * 
     * @param zjzyFkrlInfo 付款认领
     * @return 结果
     */
    @Override
    public int insertZjzyFkrlInfo(ZjzyFkrlInfo zjzyFkrlInfo)
    {
        zjzyFkrlInfo.setCreateTime(DateUtils.getNowDate());
        return zjzyFkrlInfoMapper.insertZjzyFkrlInfo(zjzyFkrlInfo);
    }

    /**
     * 修改付款认领
     * 
     * @param zjzyFkrlInfo 付款认领
     * @return 结果
     */
    @Override
    public int updateZjzyFkrlInfo(ZjzyFkrlInfo zjzyFkrlInfo)
    {
        zjzyFkrlInfo.setUpdateTime(DateUtils.getNowDate());
        return zjzyFkrlInfoMapper.updateZjzyFkrlInfo(zjzyFkrlInfo);
    }

    /**
     * 批量删除付款认领
     * 
     * @param fkrlIds 需要删除的付款认领主键
     * @return 结果
     */
    @Override
    public int deleteZjzyFkrlInfoByFkrlIds(String[] fkrlIds)
    {
        return zjzyFkrlInfoMapper.deleteZjzyFkrlInfoByFkrlIds(fkrlIds);
    }

    /**
     * 删除付款认领信息
     * 
     * @param fkrlId 付款认领主键
     * @return 结果
     */
    @Override
    public int deleteZjzyFkrlInfoByFkrlId(String fkrlId)
    {
        return zjzyFkrlInfoMapper.deleteZjzyFkrlInfoByFkrlId(fkrlId);
    }

    /**
     * 根据部门编号，取得该部门的付款总额
     *
     * @param bmbh 部门编号
     * @return
     */
    public double getFkrlTotalByBmbh(final double bmbh) {
        return zjzyFkrlInfoMapper.getFkrlTotalByBmbh(bmbh);
    }

    /**
     * 处理资金占用
     *
     * @param data
     * @return
     */
    @Override
    public int handleZjzy(JSONObject data) {
        if (data.getLong("unitPrice") == null || data.getLong("landedQuantity") == null) {
            return 0;
        }

        int effectRows = 1;
        ZjzyFkrlInfo zjzyFkrlInfo = new ZjzyFkrlInfo();
        zjzyFkrlInfo.setFkrlBmbh(String.valueOf(SecurityUtils.getDeptId()));
        zjzyFkrlInfo.setFkrlPch(data.getString("pch"));
        if (StringUtils.isNotBlank(data.getString("relatedContractId"))) {
            zjzyFkrlInfo.setFkrlHtbh(data.getString("relatedContractId"));
        } else {
            zjzyFkrlInfo.setFkrlHtbh(null);
        }

        Long carriage = data.getLong("unitPrice") * data.getLong("landedQuantity");

        List<ZjzyFkrlInfo> zjzyFkrlInfoList = null;
        ZjzyFkrlInfo param = new ZjzyFkrlInfo();
        param.setFkrlPch(data.getString("pch"));
        if (StringUtils.isNotBlank(data.getString("relatedContractId"))) {
            param.setFkrlHtbh(data.getString("relatedContractId"));
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
