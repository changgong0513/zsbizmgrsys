package com.ruoyi.transportdocuments.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.transportdocuments.mapper.WarehouseInventoryInfoMapper;
import com.ruoyi.transportdocuments.domain.WarehouseInventoryInfo;
import com.ruoyi.transportdocuments.service.IWarehouseInventoryInfoService;

/**
 * 仓库库存管理Service业务层处理
 * 
 * @author changgong0513
 * @date 2023-08-01
 */
@Service
public class WarehouseInventoryInfoServiceImpl implements IWarehouseInventoryInfoService 
{
    @Autowired
    private WarehouseInventoryInfoMapper warehouseInventoryInfoMapper;

    /**
     * 查询仓库库存管理
     * 
     * @param id 仓库库存管理主键
     * @return 仓库库存管理
     */
    @Override
    public WarehouseInventoryInfo selectWarehouseInventoryInfoById(Long id)
    {
        return warehouseInventoryInfoMapper.selectWarehouseInventoryInfoById(id);
    }

    /**
     * 查询仓库库存管理列表
     * 
     * @param warehouseInventoryInfo 仓库库存管理
     * @return 仓库库存管理
     */
    @Override
    public List<WarehouseInventoryInfo> selectWarehouseInventoryInfoList(WarehouseInventoryInfo warehouseInventoryInfo)
    {
        return warehouseInventoryInfoMapper.selectWarehouseInventoryInfoList(warehouseInventoryInfo);
    }

    /**
     * 新增仓库库存管理
     * 
     * @param warehouseInventoryInfo 仓库库存管理
     * @return 结果
     */
    @Override
    public int insertWarehouseInventoryInfo(WarehouseInventoryInfo warehouseInventoryInfo)
    {
        warehouseInventoryInfo.setCreateTime(DateUtils.getNowDate());
        return warehouseInventoryInfoMapper.insertWarehouseInventoryInfo(warehouseInventoryInfo);
    }

    /**
     * 修改仓库库存管理
     * 
     * @param warehouseInventoryInfo 仓库库存管理
     * @return 结果
     */
    @Override
    public int updateWarehouseInventoryInfo(WarehouseInventoryInfo warehouseInventoryInfo)
    {
        warehouseInventoryInfo.setUpdateTime(DateUtils.getNowDate());
        return warehouseInventoryInfoMapper.updateWarehouseInventoryInfo(warehouseInventoryInfo);
    }

    /**
     * 批量删除仓库库存管理
     * 
     * @param ids 需要删除的仓库库存管理主键
     * @return 结果
     */
    @Override
    public int deleteWarehouseInventoryInfoByIds(Long[] ids)
    {
        return warehouseInventoryInfoMapper.deleteWarehouseInventoryInfoByIds(ids);
    }

    /**
     * 删除仓库库存管理信息
     * 
     * @param id 仓库库存管理主键
     * @return 结果
     */
    @Override
    public int deleteWarehouseInventoryInfoById(Long id)
    {
        return warehouseInventoryInfoMapper.deleteWarehouseInventoryInfoById(id);
    }
}
