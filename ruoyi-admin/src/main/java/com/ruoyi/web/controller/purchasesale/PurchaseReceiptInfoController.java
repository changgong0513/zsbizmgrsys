package com.ruoyi.web.controller.purchasesale;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.purchase.sale.domain.KcckInfo;
import com.ruoyi.purchase.sale.domain.PurchaseReceiptInfo;
import com.ruoyi.purchase.sale.service.IPurchaseReceiptInfoService;
import com.ruoyi.report.masterdata.domain.MasterDataClientInfo;
import com.ruoyi.report.masterdata.service.IMasterDataClientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 收货管理Controller
 * 
 * @author changgong0513
 * @date 2022-11-05
 */
@RestController
@RequestMapping("/receipt/mgr")
public class PurchaseReceiptInfoController extends BaseController
{
    @Autowired
    private IPurchaseReceiptInfoService purchaseReceiptInfoService;

    @Autowired
    private IMasterDataClientInfoService masterDataClientInfoService;

    /**
     * 查询收货管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PurchaseReceiptInfo purchaseReceiptInfo) {

        startPage();
        List<PurchaseReceiptInfo> list = purchaseReceiptInfoService.selectPurchaseReceiptInfoList(purchaseReceiptInfo);

        // 取得所有客户主数据
        List<MasterDataClientInfo> clientList = masterDataClientInfoService.selectMasterDataClientInfoList(new MasterDataClientInfo());
        // 客户主数据列表转成Map（key：baseId, value：companyName）
        Map<String, String> clientMap = clientList
                .stream()
                .collect(Collectors.toMap(MasterDataClientInfo::getBaseId, MasterDataClientInfo::getCompanyName));

        list.stream().forEach(element -> {
            element.setSupplierRealName(clientMap.get(element.getSupplierName()));
        });

        list.stream().forEach(element -> {
            if (element.getCheckQuantity().compareTo(new BigDecimal(0)) > 0) {
                // 已收货
                element.setReceiptStatus("2");
            } else {
                // 待收货
                element.setReceiptStatus("1");
            }
        });

        return getDataTable(list);
    }

    /**
     * 导出收货管理列表
     */
    @Log(title = "收货管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PurchaseReceiptInfo purchaseReceiptInfo)
    {
        List<PurchaseReceiptInfo> list = purchaseReceiptInfoService.selectPurchaseReceiptInfoList(purchaseReceiptInfo);
        ExcelUtil<PurchaseReceiptInfo> util = new ExcelUtil<PurchaseReceiptInfo>(PurchaseReceiptInfo.class);
        util.exportExcel(response, list, "收货管理数据");
    }

    /**
     * 获取收货管理详细信息
     */
    @GetMapping(value = "/{receiptId}")
    public AjaxResult getInfo(@PathVariable("receiptId") String receiptId) {
        return AjaxResult.success(purchaseReceiptInfoService.selectPurchaseReceiptInfoByReceiptId(receiptId));
    }

    /**
     * 新增收货管理
     */
    @Log(title = "收货管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PurchaseReceiptInfo purchaseReceiptInfo) {

        PurchaseReceiptInfo maxReceiptInfo = purchaseReceiptInfoService.selectMaxReceiptId();
        if (maxReceiptInfo == null) {
            purchaseReceiptInfo.setReceiptId("SH00000000");
        } else {
            String maxReceiptId = maxReceiptInfo.getReceiptId();
            String id = maxReceiptId.substring(2, maxReceiptId.length());
            int maxId = Integer.parseInt(id) + 1;
            purchaseReceiptInfo.setReceiptId("SH" + StringUtils.padl(maxId, 8));
        }

        purchaseReceiptInfo.setBizVersion(1L);
        purchaseReceiptInfo.setCreateTime(DateUtils.getNowDate());
        purchaseReceiptInfo.setUpdateTime(DateUtils.getNowDate());
        purchaseReceiptInfo.setCreateBy(SecurityUtils.getUsername());
        purchaseReceiptInfo.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(purchaseReceiptInfoService.insertPurchaseReceiptInfo(purchaseReceiptInfo));
    }

    /**
     * 修改收货管理
     */
    @Log(title = "收货管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PurchaseReceiptInfo purchaseReceiptInfo) {

        purchaseReceiptInfo.setBizVersion(1L);
        purchaseReceiptInfo.setCreateTime(DateUtils.getNowDate());
        purchaseReceiptInfo.setUpdateTime(DateUtils.getNowDate());
        purchaseReceiptInfo.setCreateBy(SecurityUtils.getUsername());
        purchaseReceiptInfo.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(purchaseReceiptInfoService.updatePurchaseReceiptInfo(purchaseReceiptInfo));
    }

    /**
     * 删除收货管理
     */
    @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:remove')")
    @Log(title = "收货管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{receiptIds}")
    public AjaxResult remove(@PathVariable String[] receiptIds)
    {
        return toAjax(purchaseReceiptInfoService.deletePurchaseReceiptInfoByReceiptIds(receiptIds));
    }

    /**
     * 查询库存列表
     *
     * @param kcckInfo
     * @return
     */
    @GetMapping("/kc/list")
    public TableDataInfo kc(KcckInfo kcckInfo) {

        startPage();

        List<KcckInfo> list = purchaseReceiptInfoService.selectKcckInfoList(kcckInfo);

        list.stream().forEach(element -> {
            if (element.getSumReceiptQuantity() != null && element.getMaximumCapacity() != null) {
                element.setAvailableCapacity(element.getMaximumCapacity().subtract(element.getSumReceiptQuantity()));
            }
        });

        return getDataTable(list);
    }
}
