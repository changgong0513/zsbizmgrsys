package com.ruoyi.report.contract.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.fpgl.domain.FpglMainInfo;
import com.ruoyi.fpgl.mapper.FpglMainInfoMapper;
import com.ruoyi.purchase.sale.domain.PurchaseSaleOrderInfo;
import com.ruoyi.purchase.sale.mapper.PurchaseSaleOrderInfoMapper;
import com.ruoyi.report.contract.domain.ContractApprovalInfo;
import com.ruoyi.report.contract.domain.ContractApprovalRecordsInfo;
import com.ruoyi.report.contract.domain.ContractSyncLog;
import com.ruoyi.report.contract.mapper.ContractApprovalInfoMapper;
import com.ruoyi.report.contract.mapper.ContractApprovalRecordsInfoMapper;
import com.ruoyi.report.contract.mapper.ContractSyncLogMapper;
import com.ruoyi.report.masterdata.domain.MasterDataClientInfo;
import com.ruoyi.report.masterdata.domain.MasterDataMaterialInfo;
import com.ruoyi.report.masterdata.mapper.MasterDataClientInfoMapper;
import com.ruoyi.report.masterdata.mapper.MasterDataMaterialInfoMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.zjzy.domain.ZjzyFkInfo;
import com.ruoyi.zjzy.mapper.ZjzyFkInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.report.contract.mapper.ContractContentInfoMapper;
import com.ruoyi.report.contract.domain.ContractContentInfo;
import com.ruoyi.report.contract.service.IContractContentInfoService;

import javax.annotation.Resource;
import javax.validation.Validator;

/**
 * 合同管理Service业务层处理
 * 
 * @author changgong
 * @date 2022-10-30
 */
@Service
public class ContractContentInfoServiceImpl implements IContractContentInfoService {

    private static final Logger log = LoggerFactory.getLogger(ContractContentInfoServiceImpl.class);

    @Autowired
    private ContractContentInfoMapper contractContentInfoMapper;

    @Autowired
    private PurchaseSaleOrderInfoMapper purchaseSaleOrderInfoMapper;

    @Autowired
    private ContractApprovalInfoMapper contractApprovalInfoMapper;

    @Autowired
    private ContractApprovalRecordsInfoMapper contractApprovalRecordsInfoMapper;

    @Autowired
    private ZjzyFkInfoMapper zjzyFkInfoMapper;

    @Autowired
    private MasterDataClientInfoMapper masterDataClientInfoMapper;

    @Autowired
    private MasterDataMaterialInfoMapper masterDataMaterialInfoMapper;

    @Autowired
    private FpglMainInfoMapper fpglMainInfoMapper;

    @Resource
    private ContractSyncLogMapper contractSyncLogMapper;

    @Resource
    private ISysDeptService sysDeptService;

    @Autowired
    protected Validator validator;

    private static String companyBaseId = "";

    private static com.aliyun.dingtalkworkflow_1_0.Client client = null;

    private Map<String, Integer> materialMap = new HashMap<>();

    static {
        try {
            client = createClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询合同管理
     *
     * @param contactId 合同管理主键
     * @return 合同管理
     */
    @Override
    public ContractContentInfo selectContractContentInfoByContractId(String contactId) {

        // 取得所有采购销售订单
        PurchaseSaleOrderInfo purchaseSaleOrderInfo = new PurchaseSaleOrderInfo();
        List<PurchaseSaleOrderInfo> purchaseSaleOrderInfoList = purchaseSaleOrderInfoMapper
                .selectPurchaseSaleOrderInfoList(purchaseSaleOrderInfo);

        List<String> contractIdList = purchaseSaleOrderInfoList
                .stream()
                .map(PurchaseSaleOrderInfo::getContractId)
                .collect(Collectors.toList());

        ContractContentInfo contractContentInfo = contractContentInfoMapper
                .selectContractContentInfoByContractId(contactId);
        if (contractIdList.contains(contractContentInfo.getContractId())) {
            contractContentInfo.setConstractIsExist(1);
        } else {
            contractContentInfo.setConstractIsExist(0);
        }

        // 取得所有客户主数据
        List<MasterDataClientInfo> clientList =  masterDataClientInfoMapper
                .selectMasterDataClientInfoList(new MasterDataClientInfo());
        Map<String, String> clientMap = clientList
                .stream()
                .collect(Collectors.toMap(MasterDataClientInfo::getBaseId, MasterDataClientInfo::getCompanyName));

        // 设置显示的公司名称
        if (clientMap.containsKey(contractContentInfo.getOppositeCompanyName())) {
            contractContentInfo.setBaseId(contractContentInfo.getOppositeCompanyName());
            contractContentInfo.setCompanyName(clientMap.get(contractContentInfo.getOppositeCompanyName()));
        }

        return contractContentInfo;
    }

    /**
     * 查询合同管理列表
     *
     * @param contractContentInfo 合同管理
     * @return 合同管理
     */
    @Override
    public List<ContractContentInfo> selectContractContentInfoList(ContractContentInfo contractContentInfo) {
        return contractContentInfoMapper.selectContractContentInfoList(contractContentInfo);
    }

    /**
     * 新增合同管理
     *
     * @param contractContentInfo 合同管理
     * @return 结果
     */
    @Override
    public int insertContractContentInfo(ContractContentInfo contractContentInfo)
    {
        contractContentInfo.setCreateTime(DateUtils.getNowDate());
        return contractContentInfoMapper.insertContractContentInfo(contractContentInfo);
    }

    /**
     * 修改合同管理
     *
     * @param contractContentInfo 合同管理
     * @return 结果
     */
    @Override
    public int updateContractContentInfo(ContractContentInfo contractContentInfo)
    {
        contractContentInfo.setUpdateTime(DateUtils.getNowDate());
        return contractContentInfoMapper.updateContractContentInfo(contractContentInfo);
    }

    /**
     * 批量删除合同管理
     *
     * @param contractId 需要删除的合同管理主键
     * @return 结果
     */
    @Override
    public int deleteContractContentInfoByContractIds(String[] contractId)
    {
        return contractContentInfoMapper.deleteContractContentInfoByContractIds(contractId);
    }

    /**
     * 删除合同管理信息
     *
     * @param goodsId 合同管理主键
     * @return 结果
     */
    @Override
    public int deleteContractContentInfoByContractId(String goodsId)
    {
        return contractContentInfoMapper.deleteContractContentInfoByContractId(goodsId);
    }

    /**
     * 从钉钉同步合同数据
     *
     * @return 结果
     */
    @Override
    public int syncContractContentInfo() throws Exception {

        // 取得主数据管理中所有物料列表
        List<MasterDataMaterialInfo> masterDataMaterialInfoList =  masterDataMaterialInfoMapper
                .selectMasterDataMaterialInfoList(new MasterDataMaterialInfo());
        // 取得物料名称 -> 物料编号的Map
        materialMap = masterDataMaterialInfoList.stream().collect(Collectors
                .toMap(MasterDataMaterialInfo::getMaterialName, MasterDataMaterialInfo::getMaterialId));

        // 获取当前企业钉钉访问令牌
        String accessToken = getDingTalkAccessToken();

        // 获取审批实例ID列表(测试数据用)
        List <String> processCodeList = setSyncProcessCodeList();
        for (String processCode : processCodeList) {

            List<String> contractIdIsEmptyList = new ArrayList<>();

            String[] processCodeAndDeptId = processCode.split("@");

            List<String> ids = getContract(accessToken, processCodeAndDeptId[0]);
            int size = ids.size();
            for (int i = 0; i < size; i++) {
                // 合同同步日志
                ContractSyncLog contractSyncLog = new ContractSyncLog();
                contractSyncLog.setSyncTime(DateUtils.getNowDate()); // 合同同步时间
                contractSyncLog.setSyncStatus("1"); // 合同同步状态

                String id = ids.get(i);
                ContractContentInfo contract = getContractData(accessToken, id, contractSyncLog);
                if (contract != null && StringUtils.isNotBlank(contract.getContractId())) {
                    if (StringUtils.equals(contractSyncLog.getSyncStatus(), "1")) {
                        // 设置合同所属部门编号
                        contract.setBelongDeptId(Long.parseLong(processCodeAndDeptId[1]));

                        contractSyncLog.setSyncLogId(id); // 合同同步编号
                        contractSyncLog.setDeptId(contract.getBelongDeptId());
                        SysDept sysDept = sysDeptService.selectDeptById(contract.getBelongDeptId());
                        if (sysDept != null) {
                            contractSyncLog.setDeptName(sysDept.getDeptName());
                        }

                        // 检查合同是否已经导入合同表
                        ContractContentInfo selectContract = null;
                        selectContract = contractContentInfoMapper
                                .selectContractContentInfoByContractId(contract.getContractId());
                        if (selectContract == null) {
                            contract.setBizVersion(1L);
                            contract.setCreateTime(DateUtils.getNowDate());
                            contract.setUpdateTime(DateUtils.getNowDate());
                            contract.setCreateBy(SecurityUtils.getUsername());
                            contract.setUpdateBy(SecurityUtils.getUsername());
                            contractContentInfoMapper.insertContractContentInfo(contract);
                        } else {
                            contractSyncLog.setSyncStatus("2");
                            contract.setUpdateTime(DateUtils.getNowDate());
                            contract.setUpdateBy(SecurityUtils.getUsername());
                            contractContentInfoMapper.updateContractContentInfo(contract);
                        }

                        // 取得审批数据，添加到审批表和审批记录表
                        ContractApprovalInfo ccontractApprovalInfo = getContractApprovaData(accessToken, id);

                        // 检查审批数据是否已经导入合同审批信息表
                        ContractApprovalInfo selContractApprovalInfo = null;
                        selContractApprovalInfo = contractApprovalInfoMapper.selectContractApprovalInfoByApprovalId(ccontractApprovalInfo.getApprovalId());
                        if (selContractApprovalInfo == null) {
                            ccontractApprovalInfo.setBizVersion(1L);
                            ccontractApprovalInfo.setCreateTime(DateUtils.getNowDate());
                            ccontractApprovalInfo.setUpdateTime(DateUtils.getNowDate());
                            ccontractApprovalInfo.setCreateBy(SecurityUtils.getUsername());
                            ccontractApprovalInfo.setUpdateBy(SecurityUtils.getUsername());
                            contractApprovalInfoMapper.insertContractApprovalInfo(ccontractApprovalInfo);
                        } else {
                            ccontractApprovalInfo.setBizVersion(1L);
                            ccontractApprovalInfo.setUpdateTime(DateUtils.getNowDate());
                            ccontractApprovalInfo.setUpdateBy(SecurityUtils.getUsername());
                            contractApprovalInfoMapper.updateContractApprovalInfo(ccontractApprovalInfo);
                        }
                    }
                } else {
                    // 合同编号为空的场合
                    contractIdIsEmptyList.add(id);
                    contractSyncLog.setSyncLogId(id); // 同步日志编号
                    contractSyncLog.setContractId("empty"); // 合同编号
                    contractSyncLog.setStatusDescription("同步合同编号为空，请确认。"); // 同步状态描述
                    contractSyncLog.setSyncStatus("0"); // 合同同步状态
                }

                if (StringUtils.equals(contractSyncLog.getSyncStatus(), "0")) {
                    // 合同同步状态为0的场合，准备写入合同同步日志表
                    String searchValue = contractSyncLog.getSyncLogId();
                    if (StringUtils.isNotBlank(searchValue)) {
                        ContractSyncLog selectContractSyncLog = contractSyncLogMapper.selectContractSyncLogBySyncLogId(searchValue);
                        if (selectContractSyncLog == null) {
                            // 写入合同同步日志表
                            contractSyncLogMapper.insertContractSyncLog(contractSyncLog);
                        }
                    }
                }
            }
        }

        return 1; // 成功
    }

    /**
     * 从钉钉同步付款数据
     *
     * @return 结果
     */
    @Override
    public int syncFkContractInfo() throws Exception {

        // 获取当前企业钉钉访问令牌
        String accessToken = getDingTalkAccessToken();

        // 获取审批实例ID列表(测试数据用)
        List <String> fkProcessCodeList = setSyncFkProcessCodeList();
        for (String fkProcessCode : fkProcessCodeList) {
            String[] fkProcessCodeAndDeptId = fkProcessCode.split("@");
            List<String> ids = getFkContract(accessToken, fkProcessCodeAndDeptId[0]);
            int size = ids.size();
            for (int i = 0; i < size; i++) {
                String id = ids.get(i);
                ZjzyFkInfo zjzyFkInfo = getFkContractData(accessToken, id);
                if (zjzyFkInfo != null) {
                    zjzyFkInfo.setFkBm(Long.parseLong(fkProcessCodeAndDeptId[1]));

                    // 检查合同是否已经导入合同表
                    ZjzyFkInfo selectZjzyFkInfo = zjzyFkInfoMapper
                            .selectZjzyFkInfoByFkBusinessId(zjzyFkInfo.getFkBusinessId());
                    if (selectZjzyFkInfo == null) {
                        zjzyFkInfo.setBizVersion(1L);
                        zjzyFkInfo.setCreateTime(DateUtils.getNowDate());
                        zjzyFkInfo.setUpdateTime(DateUtils.getNowDate());
                        zjzyFkInfo.setCreateBy(SecurityUtils.getUsername());
                        zjzyFkInfo.setUpdateBy(SecurityUtils.getUsername());
                        zjzyFkInfoMapper.insertZjzyFkInfo(zjzyFkInfo);
                    } else {
                        zjzyFkInfo.setBizVersion(1L);
                        zjzyFkInfo.setUpdateTime(DateUtils.getNowDate());
                        zjzyFkInfo.setUpdateBy(SecurityUtils.getUsername());
                        zjzyFkInfoMapper.updateZjzyFkInfo(zjzyFkInfo);
                    }
                } else {
                    System.out.println("------zjzyFkInfo is null!");
                }
            }
        }

        return 1;
    }

    @Override
    public String importContract(List<ContractContentInfo> contractList, Boolean isUpdateSupport,
                                 String operName) {

        if (StringUtils.isNull(contractList) || contractList.size() == 0) {
            throw new ServiceException("导入合同数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (ContractContentInfo contract : contractList) {
            try {
                ContractContentInfo selContract = contractContentInfoMapper
                        .selectContractContentInfoByContractId(contract.getContractId());
                if (StringUtils.isNull(selContract)) {
                    BeanValidators.validateWithException(validator, contract);
                    contract.setBizVersion(1L);
                    contract.setCreateBy(operName);
                    contract.setCreateTime(DateUtils.parseDate(DateUtils.getTime()));
                    contract.setUpdateBy(operName);
                    contract.setUpdateTime(DateUtils.parseDate(DateUtils.getTime()));
                    insertContractContentInfo(contract);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + contract.getContractName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, contract);
                    contract.setBizVersion(1L);
                    contract.setUpdateBy(operName);
                    contract.setUpdateTime(DateUtils.parseDate(DateUtils.getNowDate()));
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、合同 " + contract.getContractName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、合同 " + contract.getContractName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、合同 " + contract.getContractName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    private static com.aliyun.dingtalkworkflow_1_0.Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkworkflow_1_0.Client(config);
    }

    /**
     * 获取当前企业钉钉访问令牌
     *
     * @return 结果
     */
    private String getDingTalkAccessToken() throws Exception {
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey("dinghdigcsx1pnri4ar2");
        request.setAppsecret("oXvRZJ_6SrD8TfVHM_wOBByF4_Dv7njrsIuR-mNMrq3ExoVnK_CB-pUSATEo1pk5");
        request.setHttpMethod("GET");

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenResponse response = client.execute(request);
        JSONObject respJSONObject = JSONObject.parseObject(response.getBody());
        String accessToken = respJSONObject.getString("access_token");

        return accessToken;
    }

    /**
     * 获取审批实例ID列表
     *
     * @param accessToken
     * @throws Exception
     */
    private List<String> getContract(final String accessToken, final String processCode) throws Exception {

        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders listProcessInstanceIdsHeaders =
                new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders();
        listProcessInstanceIdsHeaders.xAcsDingtalkAccessToken = accessToken;

        // https://open.dingtalk.com/document/orgapp/obtain-an-approval-list-of-instance-ids
        List<String> instanceList = new ArrayList<>();
        Long nextToken = 0L;
        while (nextToken >= 0L) {
            com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest listProcessInstanceIdsRequest =
                    new com.aliyun.dingtalkworkflow_1_0.models
                            .ListProcessInstanceIdsRequest()
                            .setProcessCode(processCode)
                            .setStartTime(1669824000000L) // 2022-12-01 00:00:00------至今
                            .setNextToken(nextToken)
                            .setMaxResults(20L);
            try {
                ListProcessInstanceIdsResponse resp = client.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest,
                        listProcessInstanceIdsHeaders, new com.aliyun.teautil.models.RuntimeOptions());
                String tempNextToken = resp.getBody().getResult().getNextToken();
                if (StringUtils.isNotBlank(tempNextToken)) {
                    nextToken = Long.parseLong(tempNextToken);
                } else {
                    break;
                }
                instanceList.addAll(resp.getBody().getResult().getList());
            } catch (TeaException err) {
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                    System.out.println("TeaException");
                    System.out.println(err.code);
                    System.out.println(err.message);
                    break;
                }
            } catch (Exception _err) {
                TeaException err = new TeaException(_err.getMessage(), _err);
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                    System.out.println("Exception");
                    System.out.println(err.code);
                    System.out.println(err.message);
                    break;
                }
            }
        }

        return instanceList;
    }

    /**
     * 获取付款合同审批实例ID列表
     *
     * @param accessToken
     * @param fkProcessCode
     * @return
     * @throws Exception
     */
    private List<String> getFkContract(final String accessToken, final String fkProcessCode) throws Exception {

        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders listProcessInstanceIdsHeaders = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders();
        listProcessInstanceIdsHeaders.xAcsDingtalkAccessToken = accessToken;

//        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest listProcessInstanceIdsRequest = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest()
//                .setProcessCode("PROC-14DEE72E-B6C8-4D18-B684-06A88E4CC714") // 测试API
//                .setStartTime(1667232000000L)
//                .setNextToken(0L)
//                .setMaxResults(10L);
//        try {
//            ListProcessInstanceIdsResponse resp = client.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest, listProcessInstanceIdsHeaders, new com.aliyun.teautil.models.RuntimeOptions());
//            System.out.println("审批实例ID列表：" + resp.getBody().getResult().getList());
//            return resp.getBody().getResult().getList();
//        } catch (TeaException err) {
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//                System.out.println("TeaException");
//                System.out.println(err.code);
//                System.out.println(err.message);
//            }
//        } catch (Exception _err) {
//            TeaException err = new TeaException(_err.getMessage(), _err);
//            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
//                // err 中含有 code 和 message 属性，可帮助开发定位问题
//                System.out.println("Exception");
//                System.out.println(err.code);
//                System.out.println(err.message);
//            }
//        }
//
//        return null;

        // https://open.dingtalk.com/document/orgapp/obtain-an-approval-list-of-instance-ids
        List<String> instanceList = new ArrayList<>();
        Long nextToken = 0L;
        while (nextToken >= 0L) {
            com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest listProcessInstanceIdsRequest =
                    new com.aliyun.dingtalkworkflow_1_0.models
                            .ListProcessInstanceIdsRequest()
                            .setProcessCode(fkProcessCode)
                            .setStartTime(1669824000000L) // 2022-12-01 00:00:00------至今
                            .setNextToken(nextToken)
                            .setMaxResults(20L);
            try {
                ListProcessInstanceIdsResponse resp = client.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest,
                        listProcessInstanceIdsHeaders, new com.aliyun.teautil.models.RuntimeOptions());
                String tempNextToken = resp.getBody().getResult().getNextToken();
                if (StringUtils.isNotBlank(tempNextToken)) {
                    nextToken = Long.parseLong(tempNextToken);
                } else {
                    break;
                }
                instanceList.addAll(resp.getBody().getResult().getList());
            } catch (TeaException err) {
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                    System.out.println("TeaException");
                    System.out.println(err.code);
                    System.out.println(err.message);
                    break;
                }
            } catch (Exception _err) {
                TeaException err = new TeaException(_err.getMessage(), _err);
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                    System.out.println("Exception");
                    System.out.println(err.code);
                    System.out.println(err.message);
                    break;
                }
            }
        }

        return instanceList;
    }

    /**
     * 根据实例ID，获取合同数据(测试用)
     *
     * @param accessToken
     * @param id
     */
    private ContractContentInfo getContractData(final String accessToken, final String id, final ContractSyncLog contractSyncLog) {
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
        getProcessInstanceHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
                .setProcessInstanceId(id);

        ContractContentInfo contract = new ContractContentInfo();

        try {
            GetProcessInstanceResponse resp = client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            contract.setContractStatus(resp.getBody().getResult().status);
            contract.setLocalContractStatus("1"); // 本地合同状态：已同步

            List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> list = resp.getBody().getResult().formComponentValues;
            for (int i = 0; i < list.size(); i++) {
                GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues item = list.get(i);
                // 货物名称
                if (StringUtils.equals(item.getName(), "货物名称")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        contract.setGoodsId(String.valueOf(materialMap.get(item.getValue())));
                        contract.setGoodsName(item.getValue());
                    } else {
                        contractSyncLog.setSyncStatus("0");
                        contractSyncLog.setStatusDescription("同步的货物名称内容不正确，请确认。");
                        throw  new Exception("同步的货物名称内容不正确，请确认。");
                    }

                }
                // 合同类型
                if (StringUtils.equals(item.getName(), "合同类型")) {
                    if (StringUtils.contains(item.getValue(), "收购合同") ||
                            StringUtils.contains(item.getValue(), "采购合同")) {
                        contract.setContractType("P");
                    } else if (StringUtils.contains(item.getValue(), "物流合同") ||
                            StringUtils.contains(item.getValue(), "销售合同")) {
                        contract.setContractType("S");
                    } else {
                        // 其他合同类型
                        //contract.setContractType("Q");
                        contractSyncLog.setContractType(item.getValue());
                        contractSyncLog.setSyncStatus("0");
                        contractSyncLog.setStatusDescription("同步的合同类型不是收购合同或者销售合同，请确认。");
                        throw  new Exception("同步的合同类型不是收购合同或者销售合同，请确认。");
                    }
                }
                // 合同名称
                if (StringUtils.equals(item.getName(), "合同名称")) {
                    contract.setContractName(item.getValue());
                }
                // 合同编号
                if (StringUtils.equals(item.getName(), "合同编号")) {
                    if (!StringUtils.contains(item.getValue(), "/")) {
                        contract.setContractId(item.getValue());
                    } else {
                        contractSyncLog.setSyncStatus("0");
                        contractSyncLog.setStatusDescription("同步的合同编号包含非法字符[/]，请确认。");
                        throw new Exception("同步的合同编号包含非法字符[/]");
                    }
                }
                // 签约日期
                if (StringUtils.equals(item.getName(), "签约日期")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        contract.setSignDate(DateUtils.dateTime(DateUtils.YYYY_MM_DD, item.getValue()));
                    }
                }
                // 交货日期
                if (StringUtils.equals(item.getName(), "交货日期")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        contract.setDeliveryDate(DateUtils.dateTime(DateUtils.YYYY_MM_DD, item.getValue()));
                    }
                }
                // 我方单位名称
                if (StringUtils.equals(item.getName(), "我方单位名称")) {
                    contract.setOurCompanyName(item.getValue());
                }
                // 我方负责人
                if (StringUtils.equals(item.getName(), "我方负责人")) {
                    contract.setOurPrincipal(item.getValue());
                }
                // 对方单位名称
                if (StringUtils.equals(item.getName(), "对方单位名称")) {
                    contract.setOppositeCompanyName(item.getValue());
                }
                // 对方负责人
                if (StringUtils.equals(item.getName(), "对方负责人")) {
                    contract.setOppositePrincipal(item.getValue());
                }
                // 合同数量
                if (StringUtils.equals(item.getName(), "合同数量")) {
                    contract.setContractQuantity(item.getValue());
                }
                // 合同单价
                if (StringUtils.equals(item.getName(), "合同单价")) {
                    if (StringUtils.isNotBlank(item.getValue()) && isDigit(item.getValue())) {
                        contract.setContractPrice(new BigDecimal(getDigit(item.getValue())));
                    }
                }
                // 合同总价
                if (StringUtils.equals(item.getName(), "合同总价")) {
                    if (StringUtils.isNotBlank(item.getValue()) && isDigit(item.getValue())) {
                        contract.setContractTotal(new BigDecimal(getDigit(item.getValue())));
                    }
                }
                // 账期方式
                if (StringUtils.equals(item.getName(), "账期方式")) {
                    contract.setAccountingPeriod(item.getValue());
                }
                // 账期期限
                if (StringUtils.equals(item.getName(), "账期期限")) {
                    contract.setAccountingPeriod(contract.getAccountingPeriod() + item.getValue());
                }
                // 交货方式
                if (StringUtils.equals(item.getName(), "交货方式")) {
                    contract.setDeliveryMethod(item.getValue());
                }
                // 港口到厂运费
                if (StringUtils.equals(item.getName(), "港口到厂运费")) {
                    if (StringUtils.isNotBlank(item.getValue()) && isDigit(item.getValue())) {
                        contract.setPortToFactoryFare(new BigDecimal(getDigit(item.getValue())));
                    }
                }
                // 港口到港口运费
                if (StringUtils.equals(item.getName(), "港口到港口运费")) {
                    if (StringUtils.isNotBlank(item.getValue()) && isDigit(item.getValue())) {
                        contract.setPortToPortFare(new BigDecimal(getDigit(item.getValue())));
                    }
                }
                // 其他
                if (StringUtils.equals(item.getName(), "其他")) {
                    contract.setContractOther(item.getValue());
                }
                // 代理或合作方
                if (StringUtils.equals(item.getName(), "代理或合作方")) {
                    contract.setContractAgent(item.getValue());
                }
                // 备注
                if (StringUtils.equals(item.getName(), "备注")) {
                    contract.setContractRemark(item.getValue());
                }
            }

            return contract;
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                System.out.println("TeaException");
                System.out.println(err.code);
                System.out.println(err.message);
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
                System.out.println("Exception");
                System.out.println(err.code);
                System.out.println(err.message);
            }
        }

        return null;
    }

    private void fillPurchaseInfoFromContract(ContractContentInfo contractInfo,
                                              PurchaseSaleOrderInfo purchaseInfo) {
        // 采购类型 -> 收购合同对应企业采购
        purchaseInfo.setPurchaseType(contractInfo.getContractType());
        // 合同编号 -> 合同编号
        purchaseInfo.setContractId(contractInfo.getContractId());
        // 经办人 -> 当前登录用户名称
        purchaseInfo.setHandledBy(SecurityUtils.getUsername());
        // 所属部门 -> 当前登录用户所属部门
        purchaseInfo.setBelongDept(String.valueOf(SecurityUtils.getDeptId()));
        // 业务日期 -> 签约日期
        purchaseInfo.setBusinessDate(contractInfo.getSignDate());
        // 物料名称 -> 货物名称
        purchaseInfo.setMaterialName(contractInfo.getGoodsName());
        // 采购数量 -> 合同数量
        if (StringUtils.isNotBlank(contractInfo.getContractQuantity())) {
            purchaseInfo.setPurchaseQuantity(Long.parseLong(contractInfo.getContractQuantity()));
        } else {
            purchaseInfo.setPurchaseQuantity(0L);
        }
        // 供应商名称 -> 对方单位名称
        if (StringUtils.contains(contractInfo.getOppositeCompanyName(), "KH")) {
            // 供应商名称为供应商编码（KHXXXXXX）的场合
            purchaseInfo.setSupplierName(contractInfo.getOppositeCompanyName());
        } else {
            // 供应商名称为非编号的场合
            String oppositeCompanyName = contractInfo.getOppositeCompanyName();
            if (StringUtils.isNotBlank(oppositeCompanyName)) {
                MasterDataClientInfo param = new MasterDataClientInfo();
                param.setCompanyName(contractInfo.getOppositeCompanyName());
                List<MasterDataClientInfo> masterDataClientInfoList = masterDataClientInfoMapper
                        .selectMasterDataClientInfoList(param);
                purchaseInfo.setSupplierName(masterDataClientInfoList.get(0).getBaseId());
            } else {
                purchaseInfo.setSupplierName(StringUtils.EMPTY);
            }
        }
        // 单价 -> 合同单价
        purchaseInfo.setUnitPrice(contractInfo.getContractPrice());
        // 计量单位 ->  默认值为吨
        purchaseInfo.setMeteringUnit("1");
        // 预期到货日期 -> 交货日期
        purchaseInfo.setArrivalDate(contractInfo.getDeliveryDate());
        // 要求交货期 —> 交货日期
        purchaseInfo.setRequiredDeliveryDate(contractInfo.getDeliveryDate());
        // 账期 —> 账期期限中的数字部分
        if (StringUtils.isNotBlank(contractInfo.getAccountingPeriod())) {
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(contractInfo.getAccountingPeriod());
            String result = m.replaceAll("").trim();
            if (StringUtils.isNotBlank(result)) {
                purchaseInfo.setPurchaseQuantity(Long.parseLong(result));
            } else {
                purchaseInfo.setAccountPeriod(0L);
            }
        }
        // 到账条件 —>  账期期限
        purchaseInfo.setArrivalTerms(contractInfo.getAccountingPeriod());
        // 结算方式 -> 发货 = 发货数量结算
        if (StringUtils.contains(contractInfo.getAccountingPeriod(), "卸货")) {
            purchaseInfo.setSettlementMethod("1");
        } else if (StringUtils.contains(contractInfo.getAccountingPeriod(), "发货")) {
            purchaseInfo.setSettlementMethod("2");
        }

        purchaseInfo.setOrderStatus(contractInfo.getContractStatus());
    }

    /**
     * 根据实例ID，获取付款合同数据(测试用)
     *
     * @param accessToken
     * @param id
     */
    private ZjzyFkInfo getFkContractData(final String accessToken, final String id) {
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
        getProcessInstanceHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
                .setProcessInstanceId(id);

        ZjzyFkInfo fk = new ZjzyFkInfo();

        try {
            GetProcessInstanceResponse resp = client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            fk.setFkspzt(resp.getBody().getResult().status);
            fk.setFkBusinessId(resp.getBody().getResult().businessId);

            List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> list = resp.getBody().getResult().formComponentValues;
            for (int i = 0; i < list.size(); i++) {
                GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues item = list.get(i);
                // 付款事由
                if (StringUtils.equals(item.getName(), "付款事由")) {
                    fk.setFkSy(item.getValue());
                }
                // 贸易品种
                if (StringUtils.equals(item.getName(), "贸易品种")) {
                    //TODO 需要补充
                    fk.setFkWlbh("");
                    fk.setFkWlmc(item.getValue());
                }
                // 其他品种名称
                if (StringUtils.equals(item.getName(), "其他品种名称")) {
                    fk.setFkQtpzmc(item.getValue());
                }
                // 资金用途
                if (StringUtils.equals(item.getName(), "资金用途")) {
                    if (StringUtils.equals(item.getValue(), "运费")) {
                        fk.setFkZjyt("1");
                    } else {
                        //TODO 需要补充
                        fk.setFkZjyt("0");
                    }
                }
                // 单价
                if (StringUtils.equals(item.getName(), "单价")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        fk.setFkDj(new BigDecimal(item.getValue()));
                    } else {
                        fk.setFkDj(new BigDecimal(0));
                    }
                }
                // 数量
                if (StringUtils.equals(item.getName(), "数量")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        fk.setFkSl(new BigDecimal(item.getValue()));
                    } else {
                        fk.setFkSl(new BigDecimal(0));
                    }
                }
                // 付款金额（元）
                if (StringUtils.equals(item.getName(), "付款金额（元）")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        fk.setFkJe(new BigDecimal(item.getValue()));
                    } else {
                        fk.setFkJe(new BigDecimal(0L));
                    }
                }
                // 付款账户
                if (StringUtils.equals(item.getName(), "付款账户")) {
                    //TODO 需要补充
                    fk.setFkKhbh("");
                    fk.setFkKhmc(item.getValue());
                }
                // 收款账号
                if (StringUtils.equals(item.getName(), "收款账号")) {
                    fk.setFkZh(item.getValue());
                }
                // 开户银行
                if (StringUtils.equals(item.getName(), "开户银行")) {
                    fk.setFkKhyh(item.getValue());
                }
                // 银行行号
                if (StringUtils.equals(item.getName(), "银行行号")) {
                    fk.setFkYhhh(item.getValue());
                }
                // 运输方式
                if (StringUtils.equals(item.getName(), "运输方式")) {
                    if (StringUtils.equals(item.getValue(), "铁运")) {
                        fk.setFkYsfs("1");
                    } else {
                        //TODO 需要补充
                        fk.setFkYsfs("0");
                    }
                }
                // 装车、到货
                if (StringUtils.equals(item.getName(), "装车、到货")) {
                    fk.setFkZcdh(item.getValue());
                }
                // 备注
                if (StringUtils.equals(item.getName(), "账期期限")) {
                    fk.setFkBz(item.getValue());
                }
            }

            return fk;
        } catch (TeaException err) {
            // err 中含有 code 和 message 属性，可帮助开发定位问题
            System.out.println("TeaException");
            System.out.println(err.code);
            System.out.println(err.message);
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            // err 中含有 code 和 message 属性，可帮助开发定位问题
            System.out.println("Exception");
            System.out.println(err.code);
            System.out.println(err.message);
        }

        return null;
    }

    /**
     * 根据实例ID，获取合同审批数据(测试用)
     *
     * @param accessToken
     * @param id
     * @return
     */
    private ContractApprovalInfo getContractApprovaData(final String accessToken, final String id) {

        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders =
                new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
        getProcessInstanceHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest =
                new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
                .setProcessInstanceId(id);

        ContractApprovalInfo contractApprovalInfo = new ContractApprovalInfo();

        try {
            com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponse rsp = null;
            rsp = client.getProcessInstanceWithOptions(getProcessInstanceRequest,
                    getProcessInstanceHeaders, new com.aliyun.teautil.models.RuntimeOptions());

            List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> valuesList = null;

            // 合同编号
            valuesList = rsp.getBody().getResult().formComponentValues;
            String contractId = valuesList.get(3).value;
            contractApprovalInfo.setContractId(contractId);

            // 审批编号
            String businessId = rsp.getBody().getResult().businessId;
            contractApprovalInfo.setApprovalId(businessId);

            // 审批合同标题
            String title = rsp.getBody().getResult().title;
            contractApprovalInfo.setApprovalTitle(title);

            // 审批状态
            String status = rsp.getBody().getResult().status;
            if (StringUtils.isBlank(status)) {
                contractApprovalInfo.setApprovalStatus(StringUtils.EMPTY);
            } else {
                contractApprovalInfo.setApprovalStatus(status);
            }

            // 审批结果
            String result = rsp.getBody().getResult().result;
            contractApprovalInfo.setApprovalResult(result);

            // 审批发起时间
            String fqsj = rsp.getBody().getResult().createTime;
            contractApprovalInfo.setLaunchTime(converTZDate2UTCDate(fqsj));

            // 审批完成时间
            String wcsj = rsp.getBody().getResult().finishTime;
            if (StringUtils.isBlank(wcsj)) {
                contractApprovalInfo.setCompleteTime(DateUtils.parseDate(DateUtils.getTime()));
                wcsj = DateUtils.getTime();
            } else {
                contractApprovalInfo.setCompleteTime(converTZDate2UTCDate(wcsj));
            }

            // 耗时
            String takeupTime = StringUtils.EMPTY;
            if (StringUtils.indexOf(wcsj, "T") == -1
                    && StringUtils.indexOf(wcsj, "Z") == -1) {
                takeupTime = DateUtils.timeDistance(DateUtils.parseDate(wcsj), converTZDate2UTCDate(fqsj));
            } else {
                takeupTime = DateUtils.timeDistance(converTZDate2UTCDate(wcsj), converTZDate2UTCDate(fqsj));
            }
            contractApprovalInfo.setTakeupTime(takeupTime);

            // 发起人工号
            String fqrgh = "";
            contractApprovalInfo.setLaunchJobId(fqrgh);

            // 发起人ID
            String fqrid = rsp.getBody().getResult().originatorUserId;
            contractApprovalInfo.setLaunchId(fqrid);

            // 发起人姓名
            String fqrxm = rsp.getBody().getResult().originatorUserId;
            contractApprovalInfo.setLaunchName(fqrxm);

            // 发起人部门
            String fqrbmId = rsp.getBody().getResult().originatorDeptId;
            String fqrbm = rsp.getBody().getResult().originatorDeptName;
            contractApprovalInfo.setLaunchDepartment(fqrbmId);

            // 审批人姓名
            List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultOperationRecords> list = null;
            list = rsp.getBody().getResult().operationRecords;
            List<String> sprxmList = new ArrayList<>();
            List<ContractApprovalRecordsInfo> listApprovalRecords = new ArrayList<>();
            for(GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultOperationRecords record: list) {

                sprxmList.add(record.getUserId());

                ContractApprovalRecordsInfo approvalRecords  = new ContractApprovalRecordsInfo();
                approvalRecords.setApprovalName(record.getUserId());
                approvalRecords.setCompleteTime(converTZDate2UTCDate(record.getDate()));
                if (StringUtils.endsWithIgnoreCase(record.getType(), "START_PROCESS_INSTANCE")) {
                    approvalRecords.setApprovalRecord("提交申请");
                    approvalRecords.setApprovalResult("提交申请");
                } else  {
                    approvalRecords.setApprovalRecord("审批");
                    approvalRecords.setApprovalResult(record.getResult());
                }

                approvalRecords.setApprovalId(contractApprovalInfo.getApprovalId());
                approvalRecords.setBizVersion(1L);
                approvalRecords.setCreateTime(DateUtils.getNowDate());
                approvalRecords.setUpdateTime(DateUtils.getNowDate());
                approvalRecords.setCreateBy(SecurityUtils.getUsername());
                approvalRecords.setUpdateBy(SecurityUtils.getUsername());

                listApprovalRecords.add(approvalRecords);
            }

            String sprxm = StringUtils.join(sprxmList, ",");
            contractApprovalInfo.setApprovalName(sprxm);

            // 当前处理人
            String dqclr = sprxmList.get(sprxmList.size() - 1);
            contractApprovalInfo.setProcessorName(dqclr);

            for (ContractApprovalRecordsInfo approvalRecord: listApprovalRecords) {

                ContractApprovalRecordsInfo selRecord = contractApprovalRecordsInfoMapper
                        .selectContractApprovalRecordsInfoByApprovalIdAndUserId(approvalRecord);

                if (selRecord == null) {
                    approvalRecord.setApprovalRecordId(UUID.randomUUID().toString().replace("-", ""));
                    approvalRecord.setBizVersion(1L);
                    approvalRecord.setCreateTime(DateUtils.getNowDate());
                    approvalRecord.setUpdateTime(DateUtils.getNowDate());
                    approvalRecord.setCreateBy(SecurityUtils.getUsername());
                    approvalRecord.setUpdateBy(SecurityUtils.getUsername());
                    contractApprovalRecordsInfoMapper.insertContractApprovalRecordsInfo(approvalRecord);
                } else {
                    approvalRecord.setUpdateTime(DateUtils.getNowDate());
                    approvalRecord.setUpdateBy(SecurityUtils.getUsername());
                    contractApprovalRecordsInfoMapper.updateContractApprovalRecordsInfo(approvalRecord);
                }
            }
        } catch (TeaException err) {
            System.out.println("TeaException");
            System.out.println(err.code);
            System.out.println(err.message);
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            // err 中含有 code 和 message 属性，可帮助开发定位问题
            System.out.println("Exception");
            System.out.println(err.code);
            System.out.println(err.message);
        }

        return contractApprovalInfo;
    }

    /**
     * 导入合同数据到采购表或者销售表
     *
     * @return 结果
     */
    public int importContractDataIntoPurchaseSaleTable(ContractContentInfo contract) {

        if (contract == null) {
            return 0;
        }

        contract.setBizVersion(1L);
        contract.setCreateTime(DateUtils.getNowDate());
        contract.setUpdateTime(DateUtils.getNowDate());
        contract.setCreateBy(SecurityUtils.getUsername());
        contract.setUpdateBy(SecurityUtils.getUsername());

        // 检查是否已经导入采购表和销售表
        PurchaseSaleOrderInfo purchaseInfo = purchaseSaleOrderInfoMapper
                .selectPurchaseSaleOrderInfoByContractId(contract.getContractId());
        if (purchaseInfo == null) {
            purchaseInfo = new PurchaseSaleOrderInfo();
            purchaseInfo.setOrderId(contract.getContractId());
            purchaseInfo.setBizVersion(1L);
            purchaseInfo.setCreateTime(DateUtils.getNowDate());
            purchaseInfo.setUpdateTime(DateUtils.getNowDate());
            purchaseInfo.setCreateBy(SecurityUtils.getUsername());
            purchaseInfo.setUpdateBy(SecurityUtils.getUsername());

            fillPurchaseInfoFromContract(contract, purchaseInfo);

            purchaseSaleOrderInfoMapper.insertPurchaseSaleOrderInfo(purchaseInfo);
        } else {
            fillPurchaseInfoFromContract(contract, purchaseInfo);

            purchaseSaleOrderInfoMapper.updatePurchaseSaleOrderInfo(purchaseInfo);
        }

        // 采购订单或者销售订单，默认都需要开发票
        FpglMainInfo fpglMainInfo = new FpglMainInfo();

        // 取得主数据管理中所有物料列表
        List<MasterDataMaterialInfo> masterDataMaterialInfoList =  masterDataMaterialInfoMapper
                .selectMasterDataMaterialInfoList(new MasterDataMaterialInfo());
        // 取得物料名称 -> 物料编号的Map
        Map<String, Integer> maps = masterDataMaterialInfoList.stream()
                .collect(Collectors.toMap(MasterDataMaterialInfo::getMaterialName, MasterDataMaterialInfo::getMaterialId));
        fpglMainInfo.setFpglKpmx(String.valueOf(maps.get(purchaseInfo.getMaterialName())));
        fpglMainInfo.setFpglKpsl(0L);
        fpglMainInfo.setFpglKpdj(BigDecimal.ZERO);
        fpglMainInfo.setFpglKpje(BigDecimal.ZERO);
        fpglMainInfo.setFpglFpzt("3");
        fpglMainInfo.setFpglDdbh(purchaseInfo.getOrderId());
        fpglMainInfo.setFpglSqr(SecurityUtils.getUsername());

        FpglMainInfo selectFpglMainInfo = fpglMainInfoMapper
                .selectFpglMainInfoByFpglDdbh(purchaseInfo.getOrderId());
        if (selectFpglMainInfo == null) {
            fpglMainInfo.setBizVersion(1L);
            fpglMainInfo.setCreateTime(DateUtils.getNowDate());
            fpglMainInfo.setUpdateTime(DateUtils.getNowDate());
            fpglMainInfo.setCreateBy(SecurityUtils.getUsername());
            fpglMainInfo.setUpdateBy(SecurityUtils.getUsername());
            fpglMainInfoMapper.insertFpglMainInfo(fpglMainInfo);
        } else {
            fpglMainInfo.setUpdateTime(DateUtils.getNowDate());
            fpglMainInfo.setUpdateBy(SecurityUtils.getUsername());
            fpglMainInfoMapper.updateFpglMainInfo(fpglMainInfo);
        }

        return 1;
    }

    /**
     * 生成前端显示合同数据列表
     *
     * @param contractContentInfoList
     * @return
     */
    public void makeModelViewData(List<ContractContentInfo> contractContentInfoList) {

        // 取得所有采购销售订单
        List<PurchaseSaleOrderInfo> purchaseSaleOrderInfoList = purchaseSaleOrderInfoMapper
                .selectPurchaseSaleOrderInfoList(new PurchaseSaleOrderInfo());

        // 取得所有采购销售订单对应的合同列表
        List<String> contractIdList = purchaseSaleOrderInfoList
                .stream()
                .map(PurchaseSaleOrderInfo::getContractId)
                .collect(Collectors.toList());

        // 取得所有客户主数据
        List<MasterDataClientInfo> clientList =  masterDataClientInfoMapper
                .selectMasterDataClientInfoList(new MasterDataClientInfo());

        // 客户主数据列表转成Map（key：baseId, value：companyName）
        Map<String, String> clientMap = clientList
                .stream()
                .collect(Collectors.toMap(MasterDataClientInfo::getBaseId, MasterDataClientInfo::getCompanyName));

        // 钉钉同步过来的合同， 在采购或销售表是否存在
        contractContentInfoList.stream().forEach(element -> {
            String contractId = element.getContractId();
            if (contractIdList.contains(contractId)) {
                element.setConstractIsExist(1);
            } else {
                element.setConstractIsExist(0);
            }
        });

        // 钉钉同步过来的合同， 设置显示的公司名称
        contractContentInfoList.stream().forEach(element -> {
            // 设置显示的公司名称
            if (clientMap.containsKey(element.getOppositeCompanyName()) && element.getOppositeCompanyName().contains("KH")) {
                element.setBaseId(element.getOppositeCompanyName());
                element.setCompanyName(clientMap.get(element.getOppositeCompanyName()));
            } else {
                clientMap.forEach((k, v) -> {
                    if(v.indexOf(element.getOppositeCompanyName()) >= 0){
                        companyBaseId = k;
                        return;
                    }
                });
                element.setBaseId(companyBaseId);
                element.setCompanyName(element.getOppositeCompanyName());
            }
        });
    }

    /**
     * 根据合同编号，判断该编号合同是否存在
     *
     * @param contractId 合同管理主键
     * @return 合同管理
     */
    public boolean isExistContractByContractId(final String contractId) {
        ContractContentInfo contractContentInfo = contractContentInfoMapper.selectContractContentInfoByContractId(contractId);
        if (contractContentInfo == null) {
            return false;
        }

        return true;
    }

    /**
     * // 将日期字符串（包含T,Z）转化为Date类型.
     *
     * @param utDate
     * @return
     */
    private Date converTZDate2UTCDate(final String utDate) {

        SimpleDateFormat sdfTZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        SimpleDateFormat sdfUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date tempDate = null;
        Date targetDate = null;
        try {
            // 解析到Date对象
            tempDate = sdfTZ.parse(utDate);

            // 输出UTC格式：2017-01-22 09:28:33字符串
            String utcDate  = sdfUTC.format(tempDate);
            targetDate = DateUtils.parseDate(utcDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return targetDate;
    }

    /**
     *
     *
     * @param value
     * @return
     */
    private boolean isDigit(final String value) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(value);
        String result = m.replaceAll("").trim();
        if (StringUtils.isNotBlank(result)) {
            return true;
        }

        return false;
    }

    /**
     *
     *
     * @param value
     * @return
     */
    private String getDigit(final String value) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(value);
        String result = m.replaceAll("").trim();
        if (StringUtils.isNotBlank(result)) {
            return result;
        }

        return "0";
    }

    /**
     * 设置同步合同审批ProcessCode列表
     *
     * @return
     */
    private List<String> setSyncProcessCodeList() {

        List<String> processCodeList = new ArrayList<>();
        processCodeList.add("PROC-14C71A8A-12BA-4CD2-99C7-2B8E9F9DB32B@209"); // 经营一部-合同审批
        processCodeList.add("PROC-7B80921A-0746-4574-8C93-8C47DCC0B2CC@214"); // 经营二部-合同审批
        processCodeList.add("PROC-348E896C-3F8A-4E04-A831-E7CA5D301828@215"); // 291农场
        processCodeList.add("PROC-FB5C0F09-EE6D-4C38-9DD7-6081D8D21472@216"); // 597农场
        processCodeList.add("PROC-EAE49B11-94A0-431A-8E52-5D8BCB5255CC@217"); // 852农场
        processCodeList.add("PROC-9D5B3FE0-49C8-4B06-9D4F-4ECB1FDF59EC@218"); // 853农场
        processCodeList.add("PROC-D699DB1F-0906-4CFE-989E-095B8B12DF63@219"); // 巴彦库
        processCodeList.add("PROC-571AA361-2B20-43B1-BFEF-F3E7048985CA@220"); // 摆渡版块
        processCodeList.add("PROC-2E9937D5-5A8C-470D-ABCA-7818184BBBF3@221"); // 赤峰版块
        processCodeList.add("PROC-307F4B44-9C76-497F-9B3B-301DBDD02D7E@222"); // 创业十一部
        processCodeList.add("PROC-CA22F065-4D18-4203-B982-A0B8F9344D28@223"); // 大连版块
        // processCodeList.add("@224"); // 董氏合作，无合同审批ProcessCode
        processCodeList.add("PROC-F54B67DB-4514-44C1-B9B6-AD6937CF7E10@225"); // 甘旗卡烘干塔
        processCodeList.add("PROC-BC43CB75-D348-4AFD-BF58-17FF5B8BC077@226"); // 广西版块
        // processCodeList.add("@227"); // 河西库，无合同审批ProcessCode
        processCodeList.add("PROC-B29300AA-6B2E-4705-A270-E4B830942942@228"); // 菏泽版块
        processCodeList.add("PROC-A381599C-57C7-4DC3-BE35-07C574839CA8@229"); // 红旗岭农场
        processCodeList.add("PROC-E8E3AE6B-DE49-40A1-BEBF-E68C5C3A76EC@230"); // 湖南版块
        processCodeList.add("PROC-1EE93F9D-5124-44D7-A8F9-66584752853B@231"); // 嘉荫农场
        processCodeList.add("PROC-905BAAD6-4D93-4637-8074-95D553014523@232"); // 江西版块
        processCodeList.add("PROC-15E80363-4C7F-4A62-A67F-D8CA8281F7C2@233"); // 金融事业部
        processCodeList.add("PROC-23D042F0-8465-4C80-9792-9AF95AC539EF@234"); // 进出口事业部
        processCodeList.add("PROC-F61E556B-09C0-4E1A-BA22-AA87F869EC69@235"); // 经营三部
        processCodeList.add("PROC-EF776D1B-0355-4753-A579-4E8D359CCB86@236"); // 经营四部
        processCodeList.add("PROC-08557BF8-6872-4B4F-B029-E2AA980E941C@237"); // 开原版块
        // processCodeList.add("@238"); // 骊骅事业部，无合同审批ProcessCode
        processCodeList.add("PROC-ED8C0FDE-DA30-46FA-82E7-E93249AF5FBB@239"); // 辽中版块
        processCodeList.add("PROC-238EECD7-3DD5-4B7F-B87C-E200CA83BEF7@240"); // 临时合作版块
        processCodeList.add("PROC-2A086A24-3FBB-47B4-B76F-F7F116497980@241"); // 凌源版块
        processCodeList.add("PROC-8D88A6B0-241C-40E2-8182-C9E86741DCFC@242"); // 龙江版块
        processCodeList.add("PROC-1546CFB7-056C-435A-A338-A14C6CE28255@243"); // 农安版块
        // processCodeList.add("@244"); // 拍卖粮，无合同审批ProcessCode
        processCodeList.add("PROC-67A68F1B-17C1-43D9-A060-492990F1DE07@245"); // 平台事业部
        // processCodeList.add("@246"); // 庆阳农场，无合同审批ProcessCode
        processCodeList.add("PROC-0DB84823-B6AC-4F2A-9362-AC6B5C0175C4@247"); // 饶河版块
        processCodeList.add("PROC-2D540863-74DC-434D-8570-6F0BA4C64779@248"); // 饶河农场
        processCodeList.add("PROC-5E8A0797-87CC-469E-AD46-91FB89004A97@249"); // 三宝版块
        processCodeList.add("PROC-2CAD27CC-CE0C-4B30-91BF-8D5097935C08@250"); // 三道岗版块
        // processCodeList.add("@251"); // 赎货业务，无合同审批ProcessCode
        processCodeList.add("PROC-206A68CE-E994-47B3-AC97-DC4E056BFD8E@252"); // 曙光农场
        // processCodeList.add("@253"); // 双鸭山农场，无合同审批ProcessCode
        processCodeList.add("PROC-6D395303-1F0D-4056-9C10-0E78B51F529F@254"); // 四方山农场
        // processCodeList.add("@255"); // 锁利业务，无合同审批ProcessCode
        processCodeList.add("PROC-C49883C0-86BB-4E9C-A027-11405C5C3FCF@256"); // 泰来版块
        // processCodeList.add("@257"); // 唐山中粟农牧，无合同审批ProcessCode
        processCodeList.add("PROC-EAD2CA68-C591-4F7E-9D88-92DC2DD610DA@258"); // 通河版块
        processCodeList.add("PROC-68334B5B-FE9E-4DFA-96CC-7A84BEE34205@259"); // 通辽版块
        processCodeList.add("PROC-027C866D-D571-4A9A-AB02-85C343F00C4B@260"); // 万里服务部
        processCodeList.add("PROC-06DF623F-A483-405F-A44E-49C25765C673@261"); // 翁牛特旗版块
        // processCodeList.add("@262"); // 香坊农场，无合同审批ProcessCode
        processCodeList.add("PROC-7F659C70-380F-47D6-81B0-DE6A502F1360@263"); // 小麦事业部
        processCodeList.add("PROC-139ADD8C-9979-4DAA-9756-31A8651A9BB0@264"); // 新肇版块
        processCodeList.add("PROC-387C024E-A205-4BD1-87D5-F855F7C4925C@265"); // 业务部
        processCodeList.add("PROC-C8F7C8C4-29C9-4CC5-B2E3-1DAE208D4201@266"); // 依兰版块
        processCodeList.add("PROC-38D685F0-C5FA-4D5F-894F-67E641557D5E@267"); // 依兰农场
        processCodeList.add("PROC-373F06FE-F26B-4968-A4A0-91AB48D1D168@268"); // 友谊农场
        processCodeList.add("PROC-02CB03AD-D830-4175-B251-D15BCB056B2E@269"); // 云南版块
        processCodeList.add("PROC-ACFC4E4C-9B03-4E2D-B9BE-07AF9B461EAB@270"); // 运营部
        processCodeList.add("PROC-2B9E356A-F6C8-4AE4-AFC5-11D6D2483067@271"); // 扎鲁特旗版块
        // processCodeList.add("@272"); // 中粟（服务部），无合同审批ProcessCode

        return processCodeList;
    }

    /**
     * 设置同步合同审批ProcessCode列表
     *
     * @return
     */
    private List<String> setSyncFkProcessCodeList() {

        List<String> fkProcessCodeList = new ArrayList<>();
        fkProcessCodeList.add("PROC-A5055538-5A84-46CC-A1D1-7AEC15902D22@209"); // 经营一部-经营付款审批
        fkProcessCodeList.add("PROC-543BC84D-E297-4700-9C93-D41CCC5D0D01@214"); // 经营二部-经营付款审批
        fkProcessCodeList.add("PROC-F7B289AB-D53A-45E5-979A-45AFE28DCA31@215"); // 291农场
        fkProcessCodeList.add("PROC-8340317B-C9D8-463E-B45C-5530EDD36778@216"); // 597农场
        fkProcessCodeList.add("PROC-DA619DB0-BDD0-4FC0-A3EA-23D0B16CB53C@217"); // 852农场
        fkProcessCodeList.add("PROC-D7D260E0-003D-4D00-85DB-91FFAE9C84F9@218"); // 853农场
        fkProcessCodeList.add("PROC-2DE10510-C953-4A45-B25F-E96BBB3CBA30@219"); // 巴彦库
        fkProcessCodeList.add("PROC-9CFF8B74-2CF5-4D0F-BE74-A03B3B5A583D@220"); // 摆渡版块
        fkProcessCodeList.add("PROC-CEBCB600-8718-46D4-A316-E74A982192D8@221"); // 赤峰版块
        fkProcessCodeList.add("PROC-7E7031E4-368A-4F8F-B4AF-99943ACD1EB3@222"); // 创业十一部
        fkProcessCodeList.add("PROC-C9B9011B-881F-4E4A-985E-F0E8EC54F60F@223"); // 大连版块
        fkProcessCodeList.add("PROC-B333A507-00E1-4DC4-AB3A-96517C514E1E@224"); // 董氏合作
        // fkProcessCodeList.add("@225"); // 甘旗卡烘干塔，无经营付款审批ProcessCode
        fkProcessCodeList.add("PROC-E420D9B9-B8B3-405A-BBF6-557497D6273A@226"); // 广西版块
        fkProcessCodeList.add("PROC-BB06C181-C128-4990-9D51-95C63E5ACD43@227"); // 河西库
        fkProcessCodeList.add("PROC-6F2932FF-7F02-4BDC-BF05-2EF221CB2594@228"); // 菏泽版块
        fkProcessCodeList.add("PROC-51FC5CF6-D49F-4977-996B-B5AD5EDB8EE3@229"); // 红旗岭农场
        fkProcessCodeList.add("PROC-661C1FD8-A120-4100-A09C-6C4957724F49@230"); // 湖南版块
        fkProcessCodeList.add("PROC-6CB96281-0823-4015-9630-4E534947F4A2@231"); // 嘉荫农场
        fkProcessCodeList.add("PROC-3657BD04-C09F-41B4-87FD-78B9BDB015B1@232"); // 江西版块
        // fkProcessCodeList.add("@233"); // 金融事业部，无经营付款审批ProcessCode
        fkProcessCodeList.add("PROC-C81D090C-7100-49C9-8B19-3C7F4609B7BA@234"); // 进出口事业部
        fkProcessCodeList.add("PROC-0KYJJ80V-U773EQOPZJBVKIVFNV9S1-TF440KSJ-Y1@235"); // 经营三部
        fkProcessCodeList.add("PROC-CB85A1E0-9CF9-43DA-8422-026EA5BF7263@236"); // 经营四部
        fkProcessCodeList.add("PROC-DD9A36CB-7ECF-4C5D-8C93-39E73AE287B5@237"); // 开原版块
        fkProcessCodeList.add("PROC-A68AAAC6-51AC-4645-8EB1-2685E28CEBB0@238"); // 骊骅事业部
        fkProcessCodeList.add("PROC-9D3578CA-C4F0-48A6-9719-2195E6DCBF91@239"); // 辽中版块
        // fkProcessCodeList.add("@240"); // 临时合作版块，无经营付款审批ProcessCode
        fkProcessCodeList.add("PROC-D97C59D4-7959-416D-8B9F-28FBAC21EEB1@241"); // 凌源版块
        fkProcessCodeList.add("PROC-CF21E5D1-35D2-4212-A896-CA3FCAFCD073@242"); // 龙江版块
        fkProcessCodeList.add("PROC-0C654E2C-71DF-4C6A-8AE6-A48F1BE8AD99@243"); // 农安版块
        fkProcessCodeList.add("PROC-A3237688-9C85-4707-9C31-6015BFEEBF6E@244"); // 拍卖粮
        fkProcessCodeList.add("PROC-51C2C617-07FA-454F-971A-84F0E173984B@245"); // 平台事业部
        fkProcessCodeList.add("PROC-F32DF427-FAFF-4435-B9FA-522EF008B808@246"); // 庆阳农场
        fkProcessCodeList.add("PROC-BD9E9133-0A2B-48EC-A248-4024F0C10295@247"); // 饶河版块
        fkProcessCodeList.add("PROC-487F700B-2FD6-420F-8A6B-B123F4840827@248"); // 饶河农场
        fkProcessCodeList.add("PROC-45B9B0CD-3C7C-4FC1-B1C8-B277152D23C7@249"); // 三宝版块
        fkProcessCodeList.add("PROC-2060839F-1FEA-4852-9DFA-D1E2B9306434@250"); // 三道岗版块
        fkProcessCodeList.add("PROC-DD7858E6-34E0-4F8C-A836-C4DF733FC6BF@251"); // 赎货业务
        fkProcessCodeList.add("PROC-6580690A-F2C8-4578-86E4-9964C6FF604D@252"); // 曙光农场
        fkProcessCodeList.add("PROC-955CF09F-ED9D-4C53-B6F2-F65A07A1C31D@253"); // 双鸭山农场
        fkProcessCodeList.add("PROC-AECAEAFA-3D6C-4DEC-AAA5-95401C09FEC0@254"); // 四方山农场
        fkProcessCodeList.add("PROC-ABD127FF-3BA4-4C1C-94E5-5639010DB6EC@255"); // 锁利业务
        fkProcessCodeList.add("PROC-AAFFFC3C-29DE-4FA9-B451-AC13FAB8EA56@256"); // 泰来版块
        fkProcessCodeList.add("PROC-1BF5D12E-3C7E-4FDF-924A-75DD317BEE6B@257"); // 唐山中粟农牧
        fkProcessCodeList.add("PROC-51E43FD0-733E-4388-BCAF-2C265389F9A4@258"); // 通河版块
        fkProcessCodeList.add("PROC-C68FC176-F1E5-483E-B827-C9E09667FB04@259"); // 通辽版块
        fkProcessCodeList.add("PROC-CCC8610A-EEEA-4353-ACD0-304DAC4C5F9D@260"); // 万里服务部
        fkProcessCodeList.add("PROC-7BB2ABD9-F105-4C07-A445-E5015EA82DF4@261"); // 翁牛特旗版块
        fkProcessCodeList.add("PROC-82522CBD-ABE9-4A03-AC18-5E0D44C8487D@262"); // 香坊农场
        fkProcessCodeList.add("PROC-88B1514E-5AAD-4011-A58F-62B500412EC5@263"); // 小麦事业部
        fkProcessCodeList.add("PROC-B6237FDE-8B23-4D8B-84EE-72A2B9C201DA@264"); // 新肇版块
        fkProcessCodeList.add("PROC-EB629918-FACB-4560-AA56-70A37CCD0FF1@265"); // 业务部
        fkProcessCodeList.add("PROC-A819BABB-D355-4085-93E8-55EB21E147CE@266"); // 依兰版块
        fkProcessCodeList.add("PROC-7EB8577E-D5F3-4C73-8FD6-E4AF82D93DB5@267"); // 依兰农场
        fkProcessCodeList.add("PROC-74E7D56C-95B7-43C3-A093-42FF5A6EAC2B@268"); // 友谊农场
        fkProcessCodeList.add("PROC-46F5E8AC-BA5C-4D2B-B69F-50E59BB851BC@269"); // 云南版块
        fkProcessCodeList.add("PROC-99775D91-20CA-4CB8-82D2-5170424DE4AF@270"); // 运营部
        fkProcessCodeList.add("PROC-E053F5D5-D510-4D8C-B63B-DDF54749564A@271"); // 扎鲁特旗版块(良丰)
        fkProcessCodeList.add("PROC-82057431-E7C4-445E-9F33-1FA3046D4C1B@271"); // 扎鲁特旗版块(梅花)
        fkProcessCodeList.add("PROC-79234A80-B2A2-47CE-8C88-652881F61434@271"); // 扎鲁特旗版块(庆丰)
        // processCodeList.add("@272"); // 中粟（服务部），无经营付款审批ProcessCode

        return fkProcessCodeList;
    }
}
