package com.ruoyi.web.controller.transportdocuments;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.zjzy.domain.ZjzyHkInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.transportdocuments.domain.TransportdocumentsDetailInfo;
import com.ruoyi.transportdocuments.service.ITransportdocumentsDetailInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 运输单详细信息Controller
 * 
 * @author changgong0513
 * @date 2023-07-28
 */
@RestController
@RequestMapping("/transportdocuments/detail")
public class TransportdocumentsDetailInfoController extends BaseController
{
    @Autowired
    private ITransportdocumentsDetailInfoService transportdocumentsDetailInfoService;

    /**
     * 查询运输单详细信息列表
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(TransportdocumentsDetailInfo transportdocumentsDetailInfo)
    {
        startPage();
        List<TransportdocumentsDetailInfo> list = transportdocumentsDetailInfoService.selectTransportdocumentsDetailInfoList(transportdocumentsDetailInfo);
        return getDataTable(list);
    }

    /**
     * 导出运输单详细信息列表
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:detail:export')")
    @Log(title = "运输单详细信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TransportdocumentsDetailInfo transportdocumentsDetailInfo)
    {
        List<TransportdocumentsDetailInfo> list = transportdocumentsDetailInfoService.selectTransportdocumentsDetailInfoList(transportdocumentsDetailInfo);
        ExcelUtil<TransportdocumentsDetailInfo> util = new ExcelUtil<TransportdocumentsDetailInfo>(TransportdocumentsDetailInfo.class);
        util.exportExcel(response, list, "运输单详细信息数据");
    }

    /**
     * 获取运输单详细信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:detail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(transportdocumentsDetailInfoService.selectTransportdocumentsDetailInfoById(id));
    }

    /**
     * 新增运输单详细信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:detail:add')")
    @Log(title = "运输单详细信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TransportdocumentsDetailInfo transportdocumentsDetailInfo)
    {
        return toAjax(transportdocumentsDetailInfoService.insertTransportdocumentsDetailInfo(transportdocumentsDetailInfo));
    }

    /**
     * 修改运输单详细信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:detail:edit')")
    @Log(title = "运输单详细信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TransportdocumentsDetailInfo transportdocumentsDetailInfo)
    {
        return toAjax(transportdocumentsDetailInfoService.updateTransportdocumentsDetailInfo(transportdocumentsDetailInfo));
    }

    /**
     * 删除运输单详细信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:detail:remove')")
    @Log(title = "运输单详细信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(transportdocumentsDetailInfoService.deleteTransportdocumentsDetailInfoByIds(ids));
    }

    /**
     * 导入运输单模板下载。
     *
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<TransportdocumentsDetailInfo> util = new ExcelUtil<TransportdocumentsDetailInfo>(TransportdocumentsDetailInfo.class);
        util.importTemplateExcel(response, "运输单数据");
    }

    /**
     * 导入运输单数据.
     *
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @Log(title = "导入运输单数据", businessType = BusinessType.IMPORT)
    @PostMapping("/importData/{transportdocumentsType}")
    public AjaxResult importData(MultipartFile file, boolean updateSupport, @PathVariable String transportdocumentsType) throws Exception {
        ExcelUtil<TransportdocumentsDetailInfo> util = new ExcelUtil<TransportdocumentsDetailInfo>(TransportdocumentsDetailInfo.class);
        List<TransportdocumentsDetailInfo> transportdocumentsList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = transportdocumentsDetailInfoService.importTransportdocumentsData(transportdocumentsList, updateSupport,
                operName, transportdocumentsType);
        return AjaxResult.success(message);
    }

    /**
     * 生成中转运输单数据。
     *
     * @param ids 需要生成中转运输单详细信息主键集合
     * @return
     */
    @Log(title = "生成中转运输单数据", businessType = BusinessType.INSERT)
    @PostMapping("/merge/detail/{ids}")
    public AjaxResult mergeDetail(@PathVariable Long[] ids) {
        return toAjax(transportdocumentsDetailInfoService.mergeTransportdocumentsDetailInfo(ids));
    }
}
