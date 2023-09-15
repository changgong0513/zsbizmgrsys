package com.ruoyi.web.controller.transportdocuments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.transportdocuments.domain.TraceTimeLine;
import org.apache.poi.ss.formula.functions.T;
import org.aspectj.weaver.loadtime.Aj;
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
    public TableDataInfo list(TransportdocumentsTraceInfo transportdcoumentsTraceInfo)
    {
        startPage();
//        TransportdocumentsTraceInfo param = new TransportdocumentsTraceInfo();
        List<TransportdocumentsTraceInfo> list = transportdocumentsTraceInfoService.selectTransportdocumentsTraceInfoList(transportdcoumentsTraceInfo);
//        List<String> tracePreviousList = new ArrayList<>();
//        trace(list, transportdcoumentsTraceInfo, "previous", tracePreviousList);
//
//        List<String> tracePostList = new ArrayList<>();
//        trace(list, transportdcoumentsTraceInfo, "post", tracePostList);

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

    @GetMapping("/previous/post/list")
    public AjaxResult trace(TransportdocumentsTraceInfo transportdcoumentsTraceInfo) {
        // 取得所有运输单追踪数据列表
        List<TransportdocumentsTraceInfo> allTraceDatalist = transportdocumentsTraceInfoService
                .selectTransportdocumentsTraceInfoList(new TransportdocumentsTraceInfo());

        List<TraceTimeLine> traceTimeLineList = new ArrayList<>();
        // 取得前置运输单追踪数据列表
        List<String> tracePreviousList = new ArrayList<>();
        trace(allTraceDatalist, transportdcoumentsTraceInfo, "previous", tracePreviousList);
        for (String previous : tracePreviousList) {
            TraceTimeLine previousTraceTimeLine = new TraceTimeLine();
            previousTraceTimeLine.setType("warning");
            previousTraceTimeLine.setContent(previous);
            traceTimeLineList.add(previousTraceTimeLine);
        }

        TraceTimeLine currentTraceTimeLine = new TraceTimeLine();
        currentTraceTimeLine.setContent(transportdcoumentsTraceInfo.getTransportdocumentsId());
        currentTraceTimeLine.setType("primary");
        traceTimeLineList.add(currentTraceTimeLine);

        // 取得后置运输单追踪数据列表
        List<String> tracePostList = new ArrayList<>();
        trace(allTraceDatalist, transportdcoumentsTraceInfo, "post", tracePostList);
        for (String post : tracePostList) {
            TraceTimeLine postTraceTimeLine = new TraceTimeLine();
            postTraceTimeLine.setType("success");
            postTraceTimeLine.setContent(post);
            traceTimeLineList.add(postTraceTimeLine);
        }

        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("traceTimeLineList", traceTimeLineList);

        return ajaxResult;
    }

//    private void trace(List<TransportdocumentsTraceInfo> list, TransportdocumentsTraceInfo item, List<String> traceList) {
//
//        Optional<TransportdocumentsTraceInfo> optionalCurrent = list.stream()
//                .filter(data -> data.getTransportdocumentsId().contains(item.getTransportdocumentsId()))
//                .findAny();
//        TransportdocumentsTraceInfo current = optionalCurrent.isPresent() ? optionalCurrent.get() : null;
//        if (null == current) {
//            return;
//        }
//
//        traceList.add(current.getPreTransportdocumentsId());
//
//        Optional<TransportdocumentsTraceInfo> optionalPrevious = list.stream()
//                .filter(data ->current.getPreTransportdocumentsId().contains(data.getTransportdocumentsId()))
//                .findAny();
//
//        TransportdocumentsTraceInfo previous = optionalPrevious.isPresent() ? optionalPrevious.get() : null;
//        if (null ==  previous) {
//            return;
//        }
//
//        trace(list, previous, traceList);
//    }

    private void trace(List<TransportdocumentsTraceInfo> list, TransportdocumentsTraceInfo item, String traceType, List<String> traceList) {

        Optional<TransportdocumentsTraceInfo> optionalCurrent = list.stream()
                .filter(data -> data.getTransportdocumentsId().contains(item.getTransportdocumentsId()))
                .findAny();
        TransportdocumentsTraceInfo current = optionalCurrent.isPresent() ? optionalCurrent.get() : null;
        if (null == current) {
            return;
        }



        Optional<TransportdocumentsTraceInfo> optionalFindTransport = Optional.empty();
        if (StringUtils.equals(traceType, "previous")) {
            if (StringUtils.isNotBlank(current.getPreTransportdocumentsId())) {
                traceList.add(current.getPreTransportdocumentsId());
                optionalFindTransport = list.stream()
                        .filter(data ->current.getPreTransportdocumentsId().contains(data.getTransportdocumentsId()))
                        .findAny();
            }
        } else {
            if (StringUtils.isNotBlank(current.getPostTransportdocumentsId())) {
                traceList.add(current.getPostTransportdocumentsId());
                optionalFindTransport = list.stream()
                        .filter(data ->current.getPostTransportdocumentsId().contains(data.getTransportdocumentsId()))
                        .findAny();
            }
        }

        TransportdocumentsTraceInfo findTransport = optionalFindTransport.isPresent() ? optionalFindTransport.get() : null;
        if (null ==  findTransport) {
            return;
        }

        trace(list, findTransport, traceType, traceList);
    }
}
