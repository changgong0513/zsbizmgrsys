package com.ruoyi.web.controller.transportdocuments;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.transportdocuments.domain.TransportdocumentsTraceInfo;
import com.ruoyi.transportdocuments.service.ITransportdocumentsTraceInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 运输单追溯信息Controller
 * 
 * @author changgong0513
 * @date 2023-08-07
 */
@RestController
@RequestMapping("/transportdocuments/trace")
public class TransportdocumentsTraceInfoController extends BaseController
{
    @Autowired
    private ITransportdocumentsTraceInfoService transportdocumentsTraceInfoService;

    /**
     * 查询运输单追溯信息列表
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:trace:list')")
    @GetMapping("/list")
    public TableDataInfo list(TransportdocumentsTraceInfo transportdocumentsTraceInfo)
    {
        startPage();
        List<TransportdocumentsTraceInfo> list = transportdocumentsTraceInfoService.selectTransportdocumentsTraceInfoList(transportdocumentsTraceInfo);
        return getDataTable(list);
    }

    /**
     * 导出运输单追溯信息列表
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:trace:export')")
    @Log(title = "运输单追溯信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TransportdocumentsTraceInfo transportdocumentsTraceInfo)
    {
        List<TransportdocumentsTraceInfo> list = transportdocumentsTraceInfoService.selectTransportdocumentsTraceInfoList(transportdocumentsTraceInfo);
        ExcelUtil<TransportdocumentsTraceInfo> util = new ExcelUtil<TransportdocumentsTraceInfo>(TransportdocumentsTraceInfo.class);
        util.exportExcel(response, list, "运输单追溯信息数据");
    }

    /**
     * 获取运输单追溯信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:trace:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(transportdocumentsTraceInfoService.selectTransportdocumentsTraceInfoById(id));
    }

    /**
     * 新增运输单追溯信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:trace:add')")
    @Log(title = "运输单追溯信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TransportdocumentsTraceInfo transportdocumentsTraceInfo)
    {
        return toAjax(transportdocumentsTraceInfoService.insertTransportdocumentsTraceInfo(transportdocumentsTraceInfo));
    }

    /**
     * 修改运输单追溯信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:trace:edit')")
    @Log(title = "运输单追溯信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TransportdocumentsTraceInfo transportdocumentsTraceInfo)
    {
        return toAjax(transportdocumentsTraceInfoService.updateTransportdocumentsTraceInfo(transportdocumentsTraceInfo));
    }

    /**
     * 删除运输单追溯信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:trace:remove')")
    @Log(title = "运输单追溯信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(transportdocumentsTraceInfoService.deleteTransportdocumentsTraceInfoByIds(ids));
    }
}
