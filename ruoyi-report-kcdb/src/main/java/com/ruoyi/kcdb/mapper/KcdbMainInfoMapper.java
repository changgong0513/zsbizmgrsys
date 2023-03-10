package com.ruoyi.kcdb.mapper;

import java.util.List;

import com.ruoyi.kcdb.domain.KcdbMainInfo;

/**
 * 存库调拨Mapper接口
 * 
 * @author changgong0513
 * @date 2022-11-05
 */
public interface KcdbMainInfoMapper 
{
    /**
     * 查询存库调拨
     * 
     * @param dh 存库调拨主键
     * @return 存库调拨
     */
    public KcdbMainInfo selectKcdbMainInfoByDh(String dh);

    /**
     * 查询存库调出列表
     * 
     * @param kcdbMainInfo 存库调拨
     * @return 存库调拨集合
     */
    public List<KcdbMainInfo> selectKcdbMainInfoListDc(KcdbMainInfo kcdbMainInfo);

    /**
     * 查询存库调入列表
     *
     * @param kcdbMainInfo 存库调拨
     * @return 存库调拨集合
     */
    public List<KcdbMainInfo> selectKcdbMainInfoListDr(KcdbMainInfo kcdbMainInfo);

    /**
     * 取得库存调出最大的调拨单号
     *
     * @return
     */
    public KcdbMainInfo selectMaxDhForDc();

    /**
     * 新增存库调拨
     * 
     * @param kcdbMainInfo 存库调拨
     * @return 结果
     */
    public int insertKcdbMainInfo(KcdbMainInfo kcdbMainInfo);

    /**
     * 修改存库调拨
     * 
     * @param kcdbMainInfo 存库调拨
     * @return 结果
     */
    public int updateKcdbMainInfo(KcdbMainInfo kcdbMainInfo);

    /**
     * 删除存库调拨
     * 
     * @param dh 存库调拨主键
     * @return 结果
     */
    public int deleteKcdbMainInfoByDh(String dh);

    /**
     * 批量删除存库调拨
     * 
     * @param dhs 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKcdbMainInfoByDhs(String[] dhs);
}
