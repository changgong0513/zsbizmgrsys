package com.ruoyi.web.controller.purchasesale;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.purchase.sale.domain.PurchaseSaleOrderInfo;
import com.ruoyi.purchase.sale.domain.UploadDataPurchaseSale;
import com.ruoyi.purchase.sale.service.IPurchaseSaleOrderInfoService;
import com.ruoyi.report.contract.domain.ContractAdditionalInfo;
import com.ruoyi.report.contract.service.IContractAdditionalInfoService;
import com.ruoyi.report.masterdata.domain.MasterDataClientInfo;
import com.ruoyi.report.masterdata.service.IMasterDataClientInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 采购收货销售发货管理Controller
 * 
 * @author changgong0513
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/purchase/mgr")
public class PurchaseSaleOrderInfoController extends BaseController {

    @Autowired
    private IPurchaseSaleOrderInfoService purchaseSaleOrderInfoService;

    @Autowired
    private IContractAdditionalInfoService contractAdditionalInfoService;

    @Autowired
    private IMasterDataClientInfoService masterDataClientInfoService;

    @Autowired
    private ServerConfig serverConfig;

    // 合同类型：采购合同
    private final static String CONST_CONTRACT_TYPE_PURCHASE = "p";

    // 合同类型：销售合同
    private final static String CONST_CONTRACT_TYPE_SALE = "s";

    /**
     * 查询采购收货销售发货管理列表
     */
    @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:list')")
    @GetMapping("/list")
    public TableDataInfo list(PurchaseSaleOrderInfo purchaseSaleOrderInfo) {

        startPage();

        purchaseSaleOrderInfo.setBelongDept(String.valueOf(this.getDeptId()));

        List<PurchaseSaleOrderInfo> list = null;
        if (StringUtils.equals(purchaseSaleOrderInfo.getContractType(), CONST_CONTRACT_TYPE_PURCHASE)) {
            // 采购订单管理-收货管理
            list = purchaseSaleOrderInfoService.selectPurchaseOrderInfoUnionList(purchaseSaleOrderInfo);
        } else {
            // 销售订单管理-发货管理
            list = purchaseSaleOrderInfoService.selectSaleOrderInfoUnionList(purchaseSaleOrderInfo);
        }

        // 设置采购合同状态和完成率
        purchaseSaleOrderInfoService.setPurchaseOrderStatusAndCompletionRate(list);

        return getDataTable(list);
    }

    /**
     * 导出采购收货销售发货管理列表
     */
    @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:export')")
    @Log(title = "采购收货销售发货管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PurchaseSaleOrderInfo purchaseSaleOrderInfo) {
        List<PurchaseSaleOrderInfo> list = null;
        if (StringUtils.equals(purchaseSaleOrderInfo.getContractType(), CONST_CONTRACT_TYPE_PURCHASE)) {
            // 采购订单管理-收货管理
            list = purchaseSaleOrderInfoService.selectPurchaseOrderInfoUnionList(purchaseSaleOrderInfo);
        } else {
            // 销售订单管理-发货管理
            list = purchaseSaleOrderInfoService.selectSaleOrderInfoUnionList(purchaseSaleOrderInfo);
        }

        ExcelUtil<PurchaseSaleOrderInfo> util = new ExcelUtil<PurchaseSaleOrderInfo>(PurchaseSaleOrderInfo.class);
        util.exportExcel(response, list, "采购收货销售发货管理数据");
    }

    /**
     * 获取采购收货销售发货管理详细信息
     */
    //@PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:query')")
    @GetMapping(value = "/{contractId}")
    public AjaxResult getInfo(@PathVariable("contractId") String contractId) {

        PurchaseSaleOrderInfo purchaseSaleOrderInfo = purchaseSaleOrderInfoService.selectPurchaseSaleOrderInfoByContractId(contractId);

        // 取得所有客户主数据
        List<MasterDataClientInfo> clientList = masterDataClientInfoService.selectMasterDataClientInfoList(new MasterDataClientInfo());
        // 客户主数据列表转成Map（key：baseId, value：companyName）
        Map<String, String> clientMap = clientList
                .stream()
                .collect(Collectors.toMap(MasterDataClientInfo::getBaseId, MasterDataClientInfo::getCompanyName));

        purchaseSaleOrderInfo.setSupplierRealName(clientMap.get(purchaseSaleOrderInfo.getSupplierName()));

        return AjaxResult.success(purchaseSaleOrderInfo);
    }

    /**
     * 取得采购合同总数
     */
    @GetMapping(value = "/purchase/counts")
    public AjaxResult getPurchaseContractCounts() {
        int purchaseCounts = purchaseSaleOrderInfoService
                .getPurchaseContractCounts(String.valueOf(getDeptId()));
        return AjaxResult.success(purchaseCounts);
    }

    /**
     * 取得销售合同总数
     */
    @GetMapping(value = "/sale/counts")
    public AjaxResult getSaleContractCounts() {
        int saleCounts = purchaseSaleOrderInfoService
                .getSaleContractCounts(String.valueOf(getDeptId()));
        return AjaxResult.success(saleCounts);
    }

    /**
     * 新增采购收货销售发货管理
     */
//    @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:add')")
    @Log(title = "采购收货销售发货管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PurchaseSaleOrderInfo purchaseSaleOrderInfo) {
        // 检查订单编号是否存在
        purchaseSaleOrderInfo.setBelongDept(String.valueOf(SecurityUtils.getDeptId()));
        int checkResult = purchaseSaleOrderInfoService.checkPurchaseOrderId(purchaseSaleOrderInfo);
        if (0 < checkResult) {
            return AjaxResult.error("该采购订单编号已存在，请重新输入新的采购订单编号！");
        }

        purchaseSaleOrderInfo.setBizVersion(1L);
        purchaseSaleOrderInfo.setCreateTime(DateUtils.getNowDate());
        purchaseSaleOrderInfo.setUpdateTime(DateUtils.getNowDate());
        purchaseSaleOrderInfo.setCreateBy(SecurityUtils.getUsername());
        purchaseSaleOrderInfo.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(purchaseSaleOrderInfoService.insertPurchaseSaleOrderInfo(purchaseSaleOrderInfo));
    }

    /**
     * 修改采购收货销售发货管理
     */
//    @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:edit')")
    @Log(title = "采购收货销售发货管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PurchaseSaleOrderInfo purchaseSaleOrderInfo) {

        String supplierRealName = purchaseSaleOrderInfo.getSupplierRealName();
        if (StringUtils.isNotBlank(supplierRealName)) {
            if (!StringUtils.startsWith(supplierRealName, "GYS") && !StringUtils.startsWith(supplierRealName, "KH")) {
                // 取得所有客户主数据
                List<MasterDataClientInfo> clientList = masterDataClientInfoService.
                        selectMasterDataClientInfoList(new MasterDataClientInfo());

                if (StringUtils.equals(purchaseSaleOrderInfo.getPurchaseType(), "P")) {
                    // 供应商列表转成Map（key：companyName, value：baseId）
                    Map<String, String> supplierNameMap = clientList
                            .stream()
                            .filter(element -> element.getBaseId().startsWith("GYS"))
                            .collect(Collectors.toMap(MasterDataClientInfo::getCompanyName, MasterDataClientInfo::getBaseId));

                    purchaseSaleOrderInfo.setSupplierName(supplierNameMap.get(supplierRealName));
                } else {
                    // 客户列表转成Map（key：companyName, value：baseId）
                    Map<String, String> clientMap = clientList
                            .stream()
                            .filter(element -> element.getBaseId().startsWith("KH"))
                            .collect(Collectors.toMap(MasterDataClientInfo::getCompanyName, MasterDataClientInfo::getBaseId));

                    purchaseSaleOrderInfo.setSupplierName(clientMap.get(supplierRealName));
                }
            } else {
                purchaseSaleOrderInfo.setSupplierName(supplierRealName);
            }
        } else {
            purchaseSaleOrderInfo.setSupplierName(supplierRealName);
        }

        purchaseSaleOrderInfo.setBizVersion(1L);
        purchaseSaleOrderInfo.setCreateTime(DateUtils.getNowDate());
        purchaseSaleOrderInfo.setUpdateTime(DateUtils.getNowDate());
        purchaseSaleOrderInfo.setCreateBy(SecurityUtils.getUsername());
        purchaseSaleOrderInfo.setUpdateBy(SecurityUtils.getUsername());

        AjaxResult ajaxResult = AjaxResult.success();
        if (purchaseSaleOrderInfoService.updatePurchaseSaleOrderInfo(purchaseSaleOrderInfo) == 100) {
            ajaxResult = AjaxResult.error("发票已经开具中，无法修改是否开具发票的选项。");
        }

        return ajaxResult;
    }

    /**
     * 删除采购收货销售发货管理
     */
//    @PreAuthorize("@ss.hasPermi('purchasesale:purchasesale:remove')")
    @Log(title = "采购收货销售发货管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable String[] orderIds)
    {
        return toAjax(purchaseSaleOrderInfoService.deletePurchaseSaleOrderInfoByOrderIds(orderIds));
    }

    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file,
                                 UploadDataPurchaseSale uploadData) throws Exception {
        try {
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);

            ContractAdditionalInfo contractAdditionalInfo = new ContractAdditionalInfo();
            contractAdditionalInfo.setContractId(uploadData.getUploadOrderId());
            contractAdditionalInfo.setBizVersion(1L);
            contractAdditionalInfo.setCreateTime(DateUtils.getNowDate());
            contractAdditionalInfo.setUpdateTime(DateUtils.getNowDate());
            contractAdditionalInfo.setCreateBy(SecurityUtils.getUsername());
            contractAdditionalInfo.setUpdateBy(SecurityUtils.getUsername());
            contractAdditionalInfo.setContractId(uploadData.getUploadOrderId());

            if (fileName.lastIndexOf(".jpeg") > 0 || fileName.lastIndexOf(".png") > 0) {
                contractAdditionalInfo.setUploadImagePath(filePath + File.separator + fileName);
            } else {
                contractAdditionalInfo.setUplloadFilePath(fileName);
            }

            contractAdditionalInfoService.insertContractAdditionalInfo(contractAdditionalInfo);

            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除上传文件
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    @PostMapping("/del/uploadfile")
    public AjaxResult delUploadFile(@RequestBody String filePath) throws Exception {
        return toAjax(contractAdditionalInfoService.deleteUploadFile(filePath));
    }
}
