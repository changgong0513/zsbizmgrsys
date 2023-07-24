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
import com.ruoyi.transportdocuments.domain.TransportdocumentsBaseInfo;
import com.ruoyi.transportdocuments.service.ITransportdocumentsBaseInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 运输单基本信息Controller
 * 
 * @author changgong0513
 * @date 2023-07-23
 */
@RestController
@RequestMapping("/transportdocuments/base")
public class TransportdocumentsBaseInfoController extends BaseController
{
    @Autowired
    private ITransportdocumentsBaseInfoService transportdocumentsBaseInfoService;

    /**
     * 查询运输单基本信息列表
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:base:list')")
    @GetMapping("/list")
    public TableDataInfo list(TransportdocumentsBaseInfo transportdocumentsBaseInfo)
    {
        startPage();
        List<TransportdocumentsBaseInfo> list = transportdocumentsBaseInfoService.selectTransportdocumentsBaseInfoList(transportdocumentsBaseInfo);
        return getDataTable(list);
    }

    /**
     * 导出运输单基本信息列表
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:base:export')")
    @Log(title = "运输单基本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TransportdocumentsBaseInfo transportdocumentsBaseInfo)
    {
        List<TransportdocumentsBaseInfo> list = transportdocumentsBaseInfoService.selectTransportdocumentsBaseInfoList(transportdocumentsBaseInfo);
        ExcelUtil<TransportdocumentsBaseInfo> util = new ExcelUtil<TransportdocumentsBaseInfo>(TransportdocumentsBaseInfo.class);
        util.exportExcel(response, list, "运输单基本信息数据");
    }

    /**
     * 获取运输单基本信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:base:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(transportdocumentsBaseInfoService.selectTransportdocumentsBaseInfoById(id));
    }

    /**
     * 新增运输单基本信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:base:add')")
    @Log(title = "运输单基本信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TransportdocumentsBaseInfo transportdocumentsBaseInfo)
    {
        return toAjax(transportdocumentsBaseInfoService.insertTransportdocumentsBaseInfo(transportdocumentsBaseInfo));
    }

    /**
     * 修改运输单基本信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:base:edit')")
    @Log(title = "运输单基本信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TransportdocumentsBaseInfo transportdocumentsBaseInfo)
    {
        return toAjax(transportdocumentsBaseInfoService.updateTransportdocumentsBaseInfo(transportdocumentsBaseInfo));
    }

    /**
     * 删除运输单基本信息
     */
    @PreAuthorize("@ss.hasPermi('transportdocuments:base:remove')")
    @Log(title = "运输单基本信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(transportdocumentsBaseInfoService.deleteTransportdocumentsBaseInfoByIds(ids));
    }
}
