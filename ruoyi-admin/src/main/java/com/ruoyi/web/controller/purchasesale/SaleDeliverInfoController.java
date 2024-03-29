package com.ruoyi.web.controller.purchasesale;

import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.purchase.sale.domain.SaleDeliverInfo;
import com.ruoyi.purchase.sale.service.ISaleDeliverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 发货管理Controller
 * 
 * @author chanagong0513
 * @date 2022-11-05
 */
@RestController
@RequestMapping("/deliver/mgr")
public class SaleDeliverInfoController extends BaseController
{
    @Autowired
    private ISaleDeliverInfoService saleDeliverInfoService;

    /**
     * 查询发货管理列表
     */
    // @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:list')")
    @GetMapping("/list")
    public TableDataInfo list(SaleDeliverInfo saleDeliverInfo)
    {
        startPage();
        List<SaleDeliverInfo> list = saleDeliverInfoService.selectSaleDeliverInfoList(saleDeliverInfo);

        list.stream().forEach(element -> {
            if (element.getDeliverQuantity().compareTo(new BigDecimal(0)) > 0) {
                // 已发货
                element.setDeliverStatus("2");
            } else {
                // 待发货
                element.setDeliverStatus("1");
            }
        });

        // 查询条件包含订单状态的场合
        List<SaleDeliverInfo> filterList = null;
        if (StringUtils.isNotBlank(saleDeliverInfo.getDeliverStatus())) {
            filterList = list.stream().filter(element -> element.getDeliverStatus().equals(saleDeliverInfo.getDeliverStatus()))
                    .collect(Collectors.toList());

            if (filterList != null) {
                return getDataTable(filterList);
            }
        }

        return getDataTable(list);
    }

    /**
     * 导出发货管理列表
     */
    // @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:export')")
    @Log(title = "发货管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SaleDeliverInfo saleDeliverInfo)
    {
        List<SaleDeliverInfo> list = saleDeliverInfoService.selectSaleDeliverInfoList(saleDeliverInfo);
        ExcelUtil<SaleDeliverInfo> util = new ExcelUtil<SaleDeliverInfo>(SaleDeliverInfo.class);
        util.exportExcel(response, list, "发货管理数据");
    }

    /**
     * 获取发货管理详细信息
     */
    // @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:query')")
    @GetMapping(value = "/{deliverId}")
    public AjaxResult getInfo(@PathVariable("deliverId") String deliverId)
    {
        return AjaxResult.success(saleDeliverInfoService.selectSaleDeliverInfoByDeliverId(deliverId));
    }

    /**
     * 新增发货管理
     */
    // @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:add')")
    @Log(title = "发货管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaleDeliverInfo saleDeliverInfo) {

        SaleDeliverInfo maxSaleDeliverInfo = saleDeliverInfoService.selectMaxDeliverId();
        if (maxSaleDeliverInfo == null) {
            saleDeliverInfo.setDeliverId("ASN00000000");
        } else {
            String maxDeliverId = maxSaleDeliverInfo.getDeliverId();
            String id = maxDeliverId.substring(3, maxDeliverId.length());
            int maxId = Integer.parseInt(id) + 1;
            saleDeliverInfo.setDeliverId("ASN" + StringUtils.padl(maxId, 8));
        }

        saleDeliverInfo.setSaleOrderId(saleDeliverInfo.getSaleOrderId());
        saleDeliverInfo.setBizVersion(1L);
        saleDeliverInfo.setCreateTime(DateUtils.getNowDate());
        saleDeliverInfo.setUpdateTime(DateUtils.getNowDate());
        saleDeliverInfo.setCreateBy(SecurityUtils.getUsername());
        saleDeliverInfo.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(saleDeliverInfoService.insertSaleDeliverInfo(saleDeliverInfo));
    }

    /**
     * 修改发货管理
     */
    // @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:edit')")
    @Log(title = "发货管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaleDeliverInfo saleDeliverInfo) {
        saleDeliverInfo.setBizVersion(1L);
        saleDeliverInfo.setCreateTime(DateUtils.getNowDate());
        saleDeliverInfo.setUpdateTime(DateUtils.getNowDate());
        saleDeliverInfo.setCreateBy(SecurityUtils.getUsername());
        saleDeliverInfo.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(saleDeliverInfoService.updateSaleDeliverInfo(saleDeliverInfo));
    }

    /**
     * 删除发货管理
     */
    // @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:remove')")
    @Log(title = "发货管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deliverIds}")
    public AjaxResult remove(@PathVariable String[] deliverIds)
    {
        return toAjax(saleDeliverInfoService.deleteSaleDeliverInfoByDeliverIds(deliverIds));
    }
}
