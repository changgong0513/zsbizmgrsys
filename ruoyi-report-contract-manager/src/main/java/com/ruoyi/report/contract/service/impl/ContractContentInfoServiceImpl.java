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
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.fpgl.domain.FpglMainInfo;
import com.ruoyi.fpgl.mapper.FpglMainInfoMapper;
import com.ruoyi.purchase.sale.domain.PurchaseSaleOrderInfo;
import com.ruoyi.purchase.sale.mapper.PurchaseSaleOrderInfoMapper;
import com.ruoyi.report.contract.domain.ContractApprovalInfo;
import com.ruoyi.report.contract.domain.ContractApprovalRecordsInfo;
import com.ruoyi.report.contract.mapper.ContractApprovalInfoMapper;
import com.ruoyi.report.contract.mapper.ContractApprovalRecordsInfoMapper;
import com.ruoyi.report.masterdata.domain.MasterDataClientInfo;
import com.ruoyi.report.masterdata.domain.MasterDataMaterialInfo;
import com.ruoyi.report.masterdata.mapper.MasterDataClientInfoMapper;
import com.ruoyi.report.masterdata.mapper.MasterDataMaterialInfoMapper;
import com.ruoyi.zjzy.domain.ZjzyFkInfo;
import com.ruoyi.zjzy.mapper.ZjzyFkInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.report.contract.mapper.ContractContentInfoMapper;
import com.ruoyi.report.contract.domain.ContractContentInfo;
import com.ruoyi.report.contract.service.IContractContentInfoService;

import javax.validation.Validator;

/**
 * ????????????Service???????????????
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
     * ??????????????????
     *
     * @param contactId ??????????????????
     * @return ????????????
     */
    @Override
    public ContractContentInfo selectContractContentInfoByContractId(String contactId) {

        // ??????????????????????????????
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

        // ???????????????????????????
        List<MasterDataClientInfo> clientList =  masterDataClientInfoMapper
                .selectMasterDataClientInfoList(new MasterDataClientInfo());
        Map<String, String> clientMap = clientList
                .stream()
                .collect(Collectors.toMap(MasterDataClientInfo::getBaseId, MasterDataClientInfo::getCompanyName));

        // ???????????????????????????
        if (clientMap.containsKey(contractContentInfo.getOppositeCompanyName())) {
            contractContentInfo.setBaseId(contractContentInfo.getOppositeCompanyName());
            contractContentInfo.setCompanyName(clientMap.get(contractContentInfo.getOppositeCompanyName()));
        }

        return contractContentInfo;
    }

    /**
     * ????????????????????????
     *
     * @param contractContentInfo ????????????
     * @return ????????????
     */
    @Override
    public List<ContractContentInfo> selectContractContentInfoList(ContractContentInfo contractContentInfo) {
        return contractContentInfoMapper.selectContractContentInfoList(contractContentInfo);
    }

    /**
     * ??????????????????
     *
     * @param contractContentInfo ????????????
     * @return ??????
     */
    @Override
    public int insertContractContentInfo(ContractContentInfo contractContentInfo)
    {
        contractContentInfo.setCreateTime(DateUtils.getNowDate());
        return contractContentInfoMapper.insertContractContentInfo(contractContentInfo);
    }

    /**
     * ??????????????????
     *
     * @param contractContentInfo ????????????
     * @return ??????
     */
    @Override
    public int updateContractContentInfo(ContractContentInfo contractContentInfo)
    {
        contractContentInfo.setUpdateTime(DateUtils.getNowDate());
        return contractContentInfoMapper.updateContractContentInfo(contractContentInfo);
    }

    /**
     * ????????????????????????
     *
     * @param contractId ?????????????????????????????????
     * @return ??????
     */
    @Override
    public int deleteContractContentInfoByContractIds(String[] contractId)
    {
        return contractContentInfoMapper.deleteContractContentInfoByContractIds(contractId);
    }

    /**
     * ????????????????????????
     *
     * @param goodsId ??????????????????
     * @return ??????
     */
    @Override
    public int deleteContractContentInfoByContractId(String goodsId)
    {
        return contractContentInfoMapper.deleteContractContentInfoByContractId(goodsId);
    }

    /**
     * ???????????????????????????
     *
     * @return ??????
     */
    @Override
    public int syncContractContentInfo() throws Exception {

        // ??????????????????????????????????????????
        List<MasterDataMaterialInfo> masterDataMaterialInfoList =  masterDataMaterialInfoMapper
                .selectMasterDataMaterialInfoList(new MasterDataMaterialInfo());
        // ?????????????????? -> ???????????????Map
        materialMap = masterDataMaterialInfoList.stream().collect(Collectors
                .toMap(MasterDataMaterialInfo::getMaterialName, MasterDataMaterialInfo::getMaterialId));

        // ????????????????????????????????????
        String accessToken = getDingTalkAccessToken();

        // ??????????????????????????????????????????
//        List <String> processCodeList = getCurrentEnterpriseAllMgrForm(accessToken);
//        if (processCodeList != null && processCodeList.isEmpty()) {
//            return 10001; //  ???????????????????????????????????????
//        }

        // ??????????????????ID??????(???????????????)
        List <String> processCodeList = new ArrayList<>();
        processCodeList.add("PROC-14C71A8A-12BA-4CD2-99C7-2B8E9F9DB32B"); // ????????????-????????????
        processCodeList.add("PROC-7B80921A-0746-4574-8C93-8C47DCC0B2CC"); // ????????????-????????????
        for (String processCode : processCodeList) {

            List<String> contractIdIsEmptyList = new ArrayList<>();

            List<String> ids = getContract(accessToken, processCode);
            int size = ids.size();
            for (int i = 0; i < size; i++) {
                String id = ids.get(i);
                System.out.println("????????????ID???" + id);
                ContractContentInfo contract = getContractData(accessToken, id);
                if (StringUtils.isNotBlank(contract.getContractId())) {
                    // ??????????????????????????????
                    if (StringUtils.equals(processCode, "PROC-14C71A8A-12BA-4CD2-99C7-2B8E9F9DB32B")) {
                        contract.setBelongDeptId(209L);
                    } else if (StringUtils.equals(processCode, "PROC-7B80921A-0746-4574-8C93-8C47DCC0B2CC")) {
                        contract.setBelongDeptId(214L);
                    } else {
                        contract.setBelongDeptId(0L);
                    }

                    // ???????????????????????????????????????
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
                        contract.setUpdateTime(DateUtils.getNowDate());
                        contract.setUpdateBy(SecurityUtils.getUsername());
                        contractContentInfoMapper.updateContractContentInfo(contract);
                    }

                    // ?????????????????????????????????????????????????????????
                    ContractApprovalInfo ccontractApprovalInfo = getContractApprovaData(accessToken, id);

                    // ?????????????????????????????????????????????????????????
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
                } else {
                    // ???????????????????????????
                    contractIdIsEmptyList.add(id);
                }
            }

            // ????????????????????????
            System.out.println("Process COde???" + processCode);
            System.out.println("??????????????????????????????????????????" + contractIdIsEmptyList.size());
            for (String instanceId : contractIdIsEmptyList) {
                System.out.println("?????????????????????????????????ID???" + instanceId);
            }
        }

        return 1; // ??????
    }

    /**
     * ???????????????????????????
     *
     * @return ??????
     */
    @Override
    public int syncFkContractInfo() throws Exception {
        // ????????????????????????????????????
        String accessToken = getDingTalkAccessToken();

        // ??????????????????????????????????????????
        // getCurrentEnterpriseAllMgrForm(accessToken);

        // ??????????????????????????????ID??????(???????????????)
        List<String> idsFk = getFkContractForDemo(accessToken);
        for (String idFk : idsFk) {
            System.out.println("????????????ID???" + idFk);
            ZjzyFkInfo zjzyFkInfo = getFkContractData(accessToken, idFk);

            // ???????????????????????????????????????
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
        }

        return 1;
    }

    @Override
    public String importContract(List<ContractContentInfo> contractList, Boolean isUpdateSupport,
                                 String operName) {

        if (StringUtils.isNull(contractList) || contractList.size() == 0) {
            throw new ServiceException("?????????????????????????????????");
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
                    successMsg.append("<br/>" + successNum + "????????? " + contract.getContractName() + " ????????????");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, contract);
                    contract.setBizVersion(1L);
                    contract.setUpdateBy(operName);
                    contract.setUpdateTime(DateUtils.parseDate(DateUtils.getNowDate()));
                    successNum++;
                    successMsg.append("<br/>" + successNum + "????????? " + contract.getContractName() + " ????????????");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "????????? " + contract.getContractName() + " ?????????");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "????????? " + contract.getContractName() + " ???????????????";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0)
        {
            failureMsg.insert(0, "?????????????????????????????? " + failureNum + " ??????????????????????????????????????????");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "????????????????????????????????????????????? " + successNum + " ?????????????????????");
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
     * ????????????????????????????????????
     *
     * @return ??????
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
     * ??????????????????????????????????????????
     *
     * @param accessToken ??????????????????
     */
    private List<String> getCurrentEnterpriseAllMgrForm(final String accessToken) throws Exception {

        com.aliyun.dingtalkworkflow_1_0.Client client = createClient();
        GetManageProcessByStaffIdHeaders getManageProcessByStaffIdHeaders = new GetManageProcessByStaffIdHeaders();
        getManageProcessByStaffIdHeaders.xAcsDingtalkAccessToken = accessToken;

//        GetManageProcessByStaffIdRequest getManageProcessByStaffIdRequest = new GetManageProcessByStaffIdRequest()
//                .setUserId("282350193529366375");

        String[] syncUserIds = new String[] {
//                "1054566453903766",
//                "1054582138808487376",
//                "1023291525-1101082476",
//                "173703126738684904",
//                "013123634833456429",
//                "621832300920030353",
//                "1054575040791042",
//                "1002693800788633",
//                "214729110429276521",
//                "1729613335655268",
//                "310864301924151987",
                "282350193529366375"
        };

        List<String> processCodeList = new ArrayList<>();

        for (int index = 0; index < syncUserIds.length; index++) {

            GetManageProcessByStaffIdRequest getManageProcessByStaffIdRequest = new GetManageProcessByStaffIdRequest()
                .setUserId(syncUserIds[index]);

            try {
                GetManageProcessByStaffIdResponse resp = client
                        .getManageProcessByStaffIdWithOptions(getManageProcessByStaffIdRequest,
                                getManageProcessByStaffIdHeaders, new RuntimeOptions());

                int size = resp.getBody().getResult().size();
                for (int i = 0; i < size; i++) {
                    GetManageProcessByStaffIdResponseBody.GetManageProcessByStaffIdResponseBodyResult responseBodyResult = null;
                    responseBodyResult = resp.getBody().getResult().get(i);
                    System.out.println(responseBodyResult.getFlowTitle() + "------" + responseBodyResult.getProcessCode());
                    if (StringUtils.contains(responseBodyResult.getFlowTitle(), "??????")) {
                        // ??????????????????
                        processCodeList.add(responseBodyResult.getProcessCode());
                    }
                }
            } catch (TeaException err) {
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err ????????? code ??? message ????????????????????????????????????
                    System.out.println("TeaException-[index: " + index + "]-[userId: " + syncUserIds[index] + "]-[code: " + err.code + "]-[message: " + err.message + "]");
                }
            } catch (Exception _err) {
                TeaException err = new TeaException(_err.getMessage(), _err);
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err ????????? code ??? message ????????????????????????????????????
                    System.out.println("Exception-[index: " + index + "]-[userId: " + syncUserIds[index] + "]-[code: " + err.code + "]-[message: " + err.message + "]");
                }
            }
        }

        System.out.println("-------------------------????????????-------------------------");
        for (String processCode : processCodeList) {
            System.out.println(processCode);
        }
        System.out.println("-------------------------????????????-------------------------");

        return processCodeList;
    }

    /**
     * ??????????????????ID??????(?????????)
     *
     * @param accessToken
     * @throws Exception
     */
    private List<String> getContractForDemo(final String accessToken, List <String> processCodeList) throws Exception {

        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders listProcessInstanceIdsHeaders =
                new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders();
        listProcessInstanceIdsHeaders.xAcsDingtalkAccessToken = accessToken;

        List<String> instanceList = new ArrayList<>();
        for (int i = 0; i < processCodeList.size(); i++) {
            String processCode = processCodeList.get(i);
            long curTime = System.currentTimeMillis();
            com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest listProcessInstanceIdsRequest =
                    new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest()
//                    .setProcessCode("PROC-53AEE967-1CA5-43CF-9489-CAF178BC1E46") // ??????API
                    .setProcessCode(processCode)
//                    .setStartTime(1667232000000L)
                    .setStartTime(1667232000000L)
//                    .setEndTime(1675958400000L)
                    .setNextToken(0L).setMaxResults(10L);

            try {
                ListProcessInstanceIdsResponse resp = client.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest,
                        listProcessInstanceIdsHeaders, new com.aliyun.teautil.models.RuntimeOptions());
//                System.out.println("????????????ID?????????" + resp.getBody().getResult().getList());
                instanceList.addAll(resp.getBody().getResult().getList());
//                return resp.getBody().getResult().getList();
            } catch (TeaException err) {
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err ????????? code ??? message ????????????????????????????????????
                    System.out.println("TeaException");
                    System.out.println(err.code);
                    System.out.println(err.message);
                }
            } catch (Exception _err) {
                TeaException err = new TeaException(_err.getMessage(), _err);
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err ????????? code ??? message ????????????????????????????????????
                    System.out.println("Exception");
                    System.out.println(err.code);
                    System.out.println(err.message);
                }
            }
        }

        System.out.println("-------------------------??????????????????-------------------------");
        System.out.println("-------------------------??????????????????ID???????????????"+ instanceList.size() + "-------------------------");
        for (String isntance : instanceList) {
            System.out.println(isntance);
        }
        System.out.println("-------------------------??????????????????-------------------------");

        return instanceList;
    }

    /**
     * ??????????????????ID??????
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
                            .setStartTime(1667232000000L) // 2022-11-01 00:00:00------??????
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
                    // err ????????? code ??? message ????????????????????????????????????
                    System.out.println("TeaException");
                    System.out.println(err.code);
                    System.out.println(err.message);
                    break;
                }
            } catch (Exception _err) {
                TeaException err = new TeaException(_err.getMessage(), _err);
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err ????????? code ??? message ????????????????????????????????????
                    System.out.println("Exception");
                    System.out.println(err.code);
                    System.out.println(err.message);
                    break;
                }
            }
        }

        System.out.println("-------------------------??????????????????-------------------------");
        System.out.println("-------------------------??????????????????ID???????????????"+ instanceList.size() + "-------------------------");
        for (String instance : instanceList) {
            System.out.println(instance);
        }
        System.out.println("-------------------------??????????????????-------------------------");

        return instanceList;
    }

    /**
     * ??????????????????????????????ID??????(?????????)
     *
     * @param accessToken
     * @throws Exception
     */
    private List<String> getFkContractForDemo(final String accessToken) throws Exception {
        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders listProcessInstanceIdsHeaders = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsHeaders();
        listProcessInstanceIdsHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest listProcessInstanceIdsRequest = new com.aliyun.dingtalkworkflow_1_0.models.ListProcessInstanceIdsRequest()
                .setProcessCode("PROC-14DEE72E-B6C8-4D18-B684-06A88E4CC714") // ??????API
                .setStartTime(1667232000000L)
                .setNextToken(0L)
                .setMaxResults(10L);
        try {
            ListProcessInstanceIdsResponse resp = client.listProcessInstanceIdsWithOptions(listProcessInstanceIdsRequest, listProcessInstanceIdsHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            System.out.println("????????????ID?????????" + resp.getBody().getResult().getList());
            return resp.getBody().getResult().getList();
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err ????????? code ??? message ????????????????????????????????????
                System.out.println("TeaException");
                System.out.println(err.code);
                System.out.println(err.message);
            }
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err ????????? code ??? message ????????????????????????????????????
                System.out.println("Exception");
                System.out.println(err.code);
                System.out.println(err.message);
            }
        }

        return null;
    }

    /**
     * ????????????ID?????????????????????(?????????)
     *
     * @param accessToken
     * @param id
     */
    private ContractContentInfo getContractData(final String accessToken, final String id) {
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders getProcessInstanceHeaders = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceHeaders();
        getProcessInstanceHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest getProcessInstanceRequest = new com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceRequest()
                .setProcessInstanceId(id);

        ContractContentInfo contract = new ContractContentInfo();

        try {
            GetProcessInstanceResponse resp = client.getProcessInstanceWithOptions(getProcessInstanceRequest, getProcessInstanceHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            System.out.println("????????????------" + resp.getBody().getResult().status);
            contract.setContractStatus(resp.getBody().getResult().status);

            List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> list = resp.getBody().getResult().formComponentValues;
            System.out.println("------???????????????------" + list.size());
            System.out.println("------????????????????????????------");
            for (int i = 0; i < list.size(); i++) {
                GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues item = list.get(i);
                System.out.println(item.getName() + "------" + item.getValue());
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    contract.setGoodsId(String.valueOf(materialMap.get(item.getValue())));
                    contract.setGoodsName(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    if (StringUtils.contains(item.getValue(), "????????????") ||
                            StringUtils.contains(item.getValue(), "????????????")) {
                        contract.setContractType("P");
                    } else if (StringUtils.contains(item.getValue(), "????????????") ||
                            StringUtils.contains(item.getValue(), "????????????")) {
                        contract.setContractType("S");
                    } else {
                        // ??????????????????
                        contract.setContractType("Q");
                    }
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    contract.setContractName(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    if (!StringUtils.contains(item.getValue(), "/")) {
                        contract.setContractId(item.getValue());
                    } else {
                        throw new Exception("???????????????????????????????????????[/]");
                    }
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        contract.setSignDate(DateUtils.dateTime(DateUtils.YYYY_MM_DD, item.getValue()));
                    }
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        contract.setDeliveryDate(DateUtils.dateTime(DateUtils.YYYY_MM_DD, item.getValue()));
                    }
                }
                // ??????????????????
                if (StringUtils.equals(item.getName(), "??????????????????")) {
                    contract.setOurCompanyName(item.getValue());
                }
                // ???????????????
                if (StringUtils.equals(item.getName(), "???????????????")) {
                    contract.setOurPrincipal(item.getValue());
                }
                // ??????????????????
                if (StringUtils.equals(item.getName(), "??????????????????")) {
                    contract.setOppositeCompanyName(item.getValue());
                }
                // ???????????????
                if (StringUtils.equals(item.getName(), "???????????????")) {
                    contract.setOppositePrincipal(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    contract.setContractQuantity(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    if (StringUtils.isNotBlank(item.getValue()) && isDigit(item.getValue())) {
                        contract.setContractPrice(new BigDecimal(getDigit(item.getValue())));
                    }
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    if (StringUtils.isNotBlank(item.getValue()) && isDigit(item.getValue())) {
                        contract.setContractTotal(new BigDecimal(getDigit(item.getValue())));
                    }
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    contract.setAccountingPeriod(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    contract.setAccountingPeriod(contract.getAccountingPeriod() + item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    contract.setDeliveryMethod(item.getValue());
                }
                // ??????????????????
                if (StringUtils.equals(item.getName(), "??????????????????")) {
                    if (StringUtils.isNotBlank(item.getValue()) && isDigit(item.getValue())) {
                        contract.setPortToFactoryFare(new BigDecimal(getDigit(item.getValue())));
                    }
                }
                // ?????????????????????
                if (StringUtils.equals(item.getName(), "?????????????????????")) {
                    if (StringUtils.isNotBlank(item.getValue()) && isDigit(item.getValue())) {
                        contract.setPortToPortFare(new BigDecimal(getDigit(item.getValue())));
                    }
                }
                // ??????
                if (StringUtils.equals(item.getName(), "??????")) {
                    contract.setContractOther(item.getValue());
                }
                // ??????????????????
                if (StringUtils.equals(item.getName(), "??????????????????")) {
                    contract.setContractAgent(item.getValue());
                }
                // ??????
                if (StringUtils.equals(item.getName(), "??????")) {
                    contract.setContractRemark(item.getValue());
                }
            }

            return contract;
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err ????????? code ??? message ????????????????????????????????????
                System.out.println("TeaException");
                System.out.println(err.code);
                System.out.println(err.message);
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err ????????? code ??? message ????????????????????????????????????
                System.out.println("Exception");
                System.out.println(err.code);
                System.out.println(err.message);
            }
        }

        return null;
    }

    private void fillPurchaseInfoFromContract(ContractContentInfo contractInfo,
                                              PurchaseSaleOrderInfo purchaseInfo) {
        // ???????????? -> ??????????????????????????????
        purchaseInfo.setPurchaseType(contractInfo.getContractType());
        // ???????????? -> ????????????
        purchaseInfo.setContractId(contractInfo.getContractId());
        // ????????? -> ????????????????????????
        purchaseInfo.setHandledBy(SecurityUtils.getUsername());
        // ???????????? -> ??????????????????????????????
        purchaseInfo.setBelongDept(String.valueOf(SecurityUtils.getDeptId()));
        // ???????????? -> ????????????
        purchaseInfo.setBusinessDate(contractInfo.getSignDate());
        // ???????????? -> ????????????
        purchaseInfo.setMaterialName(contractInfo.getGoodsName());
        // ???????????? -> ????????????
        if (StringUtils.isNotBlank(contractInfo.getContractQuantity())) {
            purchaseInfo.setPurchaseQuantity(Long.parseLong(contractInfo.getContractQuantity()));
        } else {
            purchaseInfo.setPurchaseQuantity(0L);
        }
        // ??????????????? -> ??????????????????
        if (StringUtils.contains(contractInfo.getOppositeCompanyName(), "KH")) {
            // ????????????????????????????????????KHXXXXXX????????????
            purchaseInfo.setSupplierName(contractInfo.getOppositeCompanyName());
        } else {
            // ???????????????????????????????????????
            MasterDataClientInfo param = new MasterDataClientInfo();
            param.setCompanyName(contractInfo.getOppositeCompanyName());
            List<MasterDataClientInfo> masterDataClientInfoList = masterDataClientInfoMapper
                    .selectMasterDataClientInfoList(param);
            purchaseInfo.setSupplierName(masterDataClientInfoList.get(0).getBaseId());
        }
        // ?????? -> ????????????
        purchaseInfo.setUnitPrice(contractInfo.getContractPrice());
        // ???????????? -> ?????????
        purchaseInfo.setMeteringUnit("3");
        // ?????????????????? -> ????????????
        purchaseInfo.setArrivalDate(contractInfo.getDeliveryDate());
        // ??????????????? ???> ????????????
        purchaseInfo.setRequiredDeliveryDate(contractInfo.getDeliveryDate());
        // ?????? ???> ??????????????????????????????
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
        // ???????????? ???>  ????????????
        purchaseInfo.setArrivalTerms(contractInfo.getAccountingPeriod());
        // ???????????? -> ?????? = ??????????????????
        if (StringUtils.contains(contractInfo.getAccountingPeriod(), "??????")) {
            purchaseInfo.setSettlementMethod("1");
        } else if (StringUtils.contains(contractInfo.getAccountingPeriod(), "??????")) {
            purchaseInfo.setSettlementMethod("2");
        }

        purchaseInfo.setOrderStatus(contractInfo.getContractStatus());
    }

    /**
     * ????????????ID???????????????????????????(?????????)
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
            System.out.println("??????????????????------" + resp.getBody().getResult().status);
            fk.setFkspzt(resp.getBody().getResult().status);

            System.out.println("????????????------" + resp.getBody().getResult().businessId);
            fk.setFkBusinessId(resp.getBody().getResult().businessId);

            List<GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues> list = resp.getBody().getResult().formComponentValues;
            System.out.println("------?????????????????????------" + list.size());
            System.out.println("------??????????????????????????????------");
            for (int i = 0; i < list.size(); i++) {
                GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues item = list.get(i);
                System.out.println(item.getName() + "------" + item.getValue());
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    fk.setFkSy(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    //TODO ????????????
                    fk.setFkWlbh("");
                    fk.setFkWlmc(item.getValue());
                }
                // ??????????????????
                if (StringUtils.equals(item.getName(), "??????????????????")) {
                    fk.setFkQtpzmc(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    if (StringUtils.equals(item.getValue(), "??????")) {
                        fk.setFkZjyt("1");
                    } else {
                        //TODO ????????????
                        fk.setFkZjyt("0");
                    }
                }
                // ??????
                if (StringUtils.equals(item.getName(), "??????")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        fk.setFkDj(new BigDecimal(item.getValue()));
                    } else {
                        fk.setFkDj(new BigDecimal(0));
                    }
                }
                // ??????
                if (StringUtils.equals(item.getName(), "??????")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        fk.setFkSl(Long.parseLong(item.getValue()));
                    } else {
                        fk.setFkSl(0L);
                    }
                }
                // ?????????????????????
                if (StringUtils.equals(item.getName(), "?????????????????????")) {
                    if (StringUtils.isNotBlank(item.getValue())) {
                        fk.setFkJe(new BigDecimal(item.getValue()));
                    } else {
                        fk.setFkJe(new BigDecimal(0L));
                    }
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    //TODO ????????????
                    fk.setFkKhbh("");
                    fk.setFkKhmc(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    fk.setFkZh(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    fk.setFkKhyh(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    fk.setFkYhhh(item.getValue());
                }
                // ????????????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    if (StringUtils.equals(item.getValue(), "??????")) {
                        fk.setFkYsfs("1");
                    } else {
                        //TODO ????????????
                        fk.setFkYsfs("0");
                    }
                }
                // ???????????????
                if (StringUtils.equals(item.getName(), "???????????????")) {
                    fk.setFkZcdh(item.getValue());
                }
                // ??????
                if (StringUtils.equals(item.getName(), "????????????")) {
                    fk.setFkBz(item.getValue());
                }
            }

            return fk;
        } catch (TeaException err) {
            // err ????????? code ??? message ????????????????????????????????????
            System.out.println("TeaException");
            System.out.println(err.code);
            System.out.println(err.message);
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            // err ????????? code ??? message ????????????????????????????????????
            System.out.println("Exception");
            System.out.println(err.code);
            System.out.println(err.message);
        }

        return null;
    }

    /**
     * ????????????ID???????????????????????????(?????????)
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

            // ????????????
            valuesList = rsp.getBody().getResult().formComponentValues;
            String contractId = valuesList.get(3).value;
            contractApprovalInfo.setContractId(contractId);

            // ????????????
            String businessId = rsp.getBody().getResult().businessId;
            contractApprovalInfo.setApprovalId(businessId);

            // ??????????????????
            String title = rsp.getBody().getResult().title;
            contractApprovalInfo.setApprovalTitle(title);

            // ????????????
            String status = rsp.getBody().getResult().status;
            if (StringUtils.isBlank(status)) {
                contractApprovalInfo.setApprovalStatus(StringUtils.EMPTY);
            } else {
                contractApprovalInfo.setApprovalStatus(status);
            }

            // ????????????
            String result = rsp.getBody().getResult().result;
            contractApprovalInfo.setApprovalResult(result);

            // ??????????????????
            String fqsj = rsp.getBody().getResult().createTime;
            contractApprovalInfo.setLaunchTime(converTZDate2UTCDate(fqsj));

            // ??????????????????
            String wcsj = rsp.getBody().getResult().finishTime;
            if (StringUtils.isBlank(wcsj)) {
                contractApprovalInfo.setCompleteTime(DateUtils.parseDate(DateUtils.getTime()));
                wcsj = DateUtils.getTime();
            } else {
                contractApprovalInfo.setCompleteTime(converTZDate2UTCDate(wcsj));
            }

            // ??????
            String takeupTime = StringUtils.EMPTY;
            if (StringUtils.indexOf(wcsj, "T") == -1
                    && StringUtils.indexOf(wcsj, "Z") == -1) {
                takeupTime = DateUtils.timeDistance(DateUtils.parseDate(wcsj), converTZDate2UTCDate(fqsj));
            } else {
                takeupTime = DateUtils.timeDistance(converTZDate2UTCDate(wcsj), converTZDate2UTCDate(fqsj));
            }
            contractApprovalInfo.setTakeupTime(takeupTime);

            // ???????????????
            String fqrgh = "";
            contractApprovalInfo.setLaunchJobId(fqrgh);

            // ?????????ID
            String fqrid = rsp.getBody().getResult().originatorUserId;
            contractApprovalInfo.setLaunchId(fqrid);

            // ???????????????
            String fqrxm = rsp.getBody().getResult().originatorUserId;
            contractApprovalInfo.setLaunchName(fqrxm);

            // ???????????????
            String fqrbmId = rsp.getBody().getResult().originatorDeptId;
            String fqrbm = rsp.getBody().getResult().originatorDeptName;
            contractApprovalInfo.setLaunchDepartment(fqrbmId);

            // ???????????????
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
                    approvalRecords.setApprovalRecord("????????????");
                    approvalRecords.setApprovalResult("????????????");
                } else  {
                    approvalRecords.setApprovalRecord("??????");
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

            // ???????????????
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
            // err ????????? code ??? message ????????????????????????????????????
            System.out.println("Exception");
            System.out.println(err.code);
            System.out.println(err.message);
        }

        return contractApprovalInfo;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @return ??????
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

        // ?????????????????????????????????????????????
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

        // ?????????????????????????????????????????????????????????
        FpglMainInfo fpglMainInfo = new FpglMainInfo();

        // ??????????????????????????????????????????
        List<MasterDataMaterialInfo> masterDataMaterialInfoList =  masterDataMaterialInfoMapper
                .selectMasterDataMaterialInfoList(new MasterDataMaterialInfo());
        // ?????????????????? -> ???????????????Map
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
     * ????????????????????????????????????
     *
     * @param contractContentInfoList
     * @return
     */
    public void makeModelViewData(List<ContractContentInfo> contractContentInfoList) {

        // ??????????????????????????????
        List<PurchaseSaleOrderInfo> purchaseSaleOrderInfoList = purchaseSaleOrderInfoMapper
                .selectPurchaseSaleOrderInfoList(new PurchaseSaleOrderInfo());

        // ???????????????????????????????????????????????????
        List<String> contractIdList = purchaseSaleOrderInfoList
                .stream()
                .map(PurchaseSaleOrderInfo::getContractId)
                .collect(Collectors.toList());

        // ???????????????????????????
        List<MasterDataClientInfo> clientList =  masterDataClientInfoMapper
                .selectMasterDataClientInfoList(new MasterDataClientInfo());

        // ???????????????????????????Map???key???baseId, value???companyName???
        Map<String, String> clientMap = clientList
                .stream()
                .collect(Collectors.toMap(MasterDataClientInfo::getBaseId, MasterDataClientInfo::getCompanyName));

        // ?????????????????????????????? ?????????????????????????????????
        contractContentInfoList.stream().forEach(element -> {
            String contractId = element.getContractId();
            if (contractIdList.contains(contractId)) {
                element.setConstractIsExist(1);
            } else {
                element.setConstractIsExist(0);
            }
        });

        // ?????????????????????????????? ???????????????????????????
        contractContentInfoList.stream().forEach(element -> {
            // ???????????????????????????
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
     * ??????????????????????????????????????????????????????
     *
     * @param contractId ??????????????????
     * @return ????????????
     */
    public boolean isExistContractByContractId(final String contractId) {
        ContractContentInfo contractContentInfo = contractContentInfoMapper.selectContractContentInfoByContractId(contractId);
        if (contractContentInfo == null) {
            return false;
        }

        return true;
    }

    /**
     * // ???????????????????????????T,Z????????????Date??????.
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
            // ?????????Date??????
            tempDate = sdfTZ.parse(utDate);

            // ??????UTC?????????2017-01-22 09:28:33?????????
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
}
