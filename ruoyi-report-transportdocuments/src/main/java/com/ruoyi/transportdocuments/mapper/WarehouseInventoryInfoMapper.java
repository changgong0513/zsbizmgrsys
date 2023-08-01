package com.ruoyi.transportdocuments.mapper;

import java.util.List;
import com.ruoyi.transportdocuments.domain.WarehouseInventoryInfo;

/**
 * 仓库库存管理Mapper接口
 * 
 * @author changgong0513
 * @date 2023-08-01
 */
public interface WarehouseInventoryInfoMapper 
{
    /**
     * 查询仓库库存管理
     * 
     * @param id 仓库库存管理主键
     * @return 仓库库存管理
     */
    public WarehouseInventoryInfo selectWarehouseInventoryInfoById(Long id);

    /**
     * 查询仓库库存管理列表
     * 
     * @param warehouseInventoryInfo 仓库库存管理
     * @return 仓库库存管理集合
     */
    public List<WarehouseInventoryInfo> selectWarehouseInventoryInfoList(WarehouseInventoryInfo warehouseInventoryInfo);

    /**
     * 新增仓库库存管理
     * 
     * @param warehouseInventoryInfo 仓库库存管理
     * @return 结果
     */
    public int insertWarehouseInventoryInfo(WarehouseInventoryInfo warehouseInventoryInfo);

    /**
     * 修改仓库库存管理
     * 
     * @param warehouseInventoryInfo 仓库库存管理
     * @return 结果
     */
    public int updateWarehouseInventoryInfo(WarehouseInventoryInfo warehouseInventoryInfo);

    /**
     * 删除仓库库存管理
     * 
     * @param id 仓库库存管理主键
     * @return 结果
     */
    public int deleteWarehouseInventoryInfoById(Long id);

    /**
     * 批量删除仓库库存管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarehouseInventoryInfoByIds(Long[] ids);
}
