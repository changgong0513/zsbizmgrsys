package com.ruoyi.web.controller.zjzy;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.zjzy.domain.ZjzyFkInfo;
import com.ruoyi.zjzy.domain.ZjzyStatisticsInfo;
import com.ruoyi.zjzy.domain.ZytjHistoryInfo;
import com.ruoyi.zjzy.service.IZjzyFkInfoService;
import com.ruoyi.zjzy.service.IZjzyFkrlInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 付款Controller
 * 
 * @author changgong0513
 * @date 2022-12-06
 */
@RestController
@RequestMapping("/zjzy/fk")
public class ZjzyFkInfoController extends BaseController
{
    @Autowired
    private IZjzyFkInfoService zjzyFkInfoService;

    @Autowired
    private IZjzyFkrlInfoService zjzyFkrlInfoService;

    /**
     * 查询付款列表
     */
    //@PreAuthorize("@ss.hasPermi('zjzy:fk:list')")
    @GetMapping("/list")
    public TableDataInfo list(ZjzyFkInfo zjzyFkInfo)
    {
        startPage();
        List<ZjzyFkInfo> list = zjzyFkInfoService.selectZjzyFkInfoList(zjzyFkInfo);
        list.stream().forEach(fkData -> {
            if (fkData.getFkJe() != null && fkData.getFkrlJe() != null) {
                BigDecimal fkje = fkData.getFkJe();
                BigDecimal fkrlje = fkData.getFkrlJe();
                if (fkje.compareTo(fkrlje) == 0) {
                    fkData.setFkZt("1");
                } else if (fkje.compareTo(fkrlje) == 1) {
                    fkData.setFkZt("2");
                } else {
                    fkData.setFkZt("3");
                }
            } else {
                fkData.setFkZt("3");
            }
        });
        return getDataTable(list);
    }

    /**
     * 导出付款列表
     */
    // @PreAuthorize("@ss.hasPermi('zjzy:fk:export')")
    @Log(title = "付款", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ZjzyFkInfo zjzyFkInfo)
    {
        List<ZjzyFkInfo> list = zjzyFkInfoService.selectZjzyFkInfoList(zjzyFkInfo);
        ExcelUtil<ZjzyFkInfo> util = new ExcelUtil<ZjzyFkInfo>(ZjzyFkInfo.class);
        util.exportExcel(response, list, "付款数据");
    }

    @GetMapping(value = "/total")
    public AjaxResult getFkrlTotal() {

        Long deptId = this.getDeptId();
        if (deptId == 100 || deptId == 103 || deptId == 201 || deptId == 203) {
            return AjaxResult.success(zjzyFkInfoService.getFkrlTotal());
        }

        return AjaxResult.success(zjzyFkrlInfoService.getFkrlTotalByBmbh(deptId));
    }

    /**
     * 查询占用统计列表
     *
     * @return
     */
    @GetMapping("/zytj/list")
    public TableDataInfo zjzyStatisticslist(ZjzyStatisticsInfo zjzyStatisticsInfo) {

        startPage();

        if (getDeptId() == 100 || getDeptId() == 103) {
            zjzyStatisticsInfo.setTjBmbh(null);
        } else {
            zjzyStatisticsInfo.setTjBmbh(String.valueOf(getDeptId()));
        }

        List<ZjzyStatisticsInfo> list = zjzyFkInfoService.selectZjzyStatisticsList(zjzyStatisticsInfo);

        zjzyStatisticsInfo.setTjJssj(DateUtils.dateTimeNow());
        zjzyStatisticsInfo.setCreateTime(DateUtils.getNowDate());
        zjzyStatisticsInfo.setUpdateTime(DateUtils.getNowDate());
        zjzyStatisticsInfo.setCreateBy(SecurityUtils.getUsername());
        zjzyStatisticsInfo.setUpdateBy(SecurityUtils.getUsername());
        for (ZjzyStatisticsInfo record : list) {
            record.setTjJssj(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, DateUtils.getNowDate()));
            record.setCreateTime(DateUtils.getNowDate());
            record.setUpdateTime(DateUtils.getNowDate());
            record.setCreateBy(SecurityUtils.getUsername());
            record.setUpdateBy(SecurityUtils.getUsername());
            List<ZytjHistoryInfo> existZytjRecrods = zjzyFkInfoService.selectZytjRecords(record);
            if (existZytjRecrods.size() == 0) {
                zjzyFkInfoService.insertZjzyStatisticsInfo(record);
            }
        }

        return getDataTable(list);
    }

    /**
     * 占用统计历史数据列表
     *
     * @param zjzyStatisticsInfo
     * @return
     */
    @GetMapping("/zytj/history/list")
    public TableDataInfo listZytjHistoryData(ZjzyStatisticsInfo zjzyStatisticsInfo) {
        startPage();
        List<ZytjHistoryInfo> list = zjzyFkInfoService.selectZytjRecords(zjzyStatisticsInfo);

        return getDataTable(list);
    }

    /**
     * 占用统计利息总额
     *
     * @return
     */
    @GetMapping(value = "/zytj/lx/total")
    public AjaxResult getZytjLxTotal() {

        Long deptId = this.getDeptId();
        if (deptId == 100 || deptId == 103 || deptId == 201 || deptId == 203) {
            return AjaxResult.success(zjzyFkInfoService.getZytjLxTotal());
        }

        return AjaxResult.success(zjzyFkInfoService.getZytjLxTotalByBmbh(deptId));
    }

    /**
     * 根据部门编号，占用统计利息总额
     *
     * @return
     */
    @GetMapping(value = "/zytj/bmbh/lx/total")
    public AjaxResult getZytjLxTotalByBmbh() {
        return AjaxResult.success(zjzyFkInfoService.getZytjLxTotalByBmbh(this.getDeptId()));
    }
}
