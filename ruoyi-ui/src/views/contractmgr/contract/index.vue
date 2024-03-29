<!-- 合同管理 -->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- 合同编号 -->
      <el-form-item label="合同编号" prop="contractId">
        <el-input
        v-model="queryParams.contractId"
        placeholder="请输入合同编号"
        clearable
        @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同名称" prop="contractName">
        <el-input
          v-model="queryParams.contractName"
          placeholder="请输入合同名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户名称" prop="oppositeCompanyName">
        <el-input
          v-model="queryParams.oppositeCompanyName"
          placeholder="请输入客户名称"
          clearable
          @keyup.enter.native="handleQuery" 
          style="width: 240px"
        />
      </el-form-item>
      <!-- 合同总价min -->
      <el-form-item label="合同总价" prop="leftContractTotal">
        <el-input
          v-model="queryParams.leftContractTotal"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- 合同总价max -->
      <el-form-item label="~" prop="rightContractTotal" label-width="15px">
        <el-input
          v-model="queryParams.rightContractTotal"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- 签约日期 -->
      <el-form-item label="签约日期" prop="signDate">
        <el-date-picker 
          clearable
          v-model="queryParams.signDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择签约日期"
          style="width: 240px" >
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          
          icon="el-icon-check"
          size="mini"
          @click="handleContractSync"
        >合同同步</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          
          icon="el-icon-star-off"
          size="mini"
          @click="handleContractSyncResult"
        >同步结果</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          v-show="!isAdmin"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          v-show="!isAdmin"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          v-show="!isAdmin"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="contractList" @selection-change="handleSelectionChange"
      @row-dblclick="handleUpdate">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同编号" align="center" prop="contractId" width="300" />
      <el-table-column label="签约日期" align="center" prop="signDate" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同名称" align="center" prop="contractName" width="300" :show-overflow-tooltip="true" />
      <el-table-column label="合同类型" align="center" prop="contractType" width="150">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.contractmgr_contract_type" :value="scope.row.contractType"/>
        </template>
      </el-table-column>
      <el-table-column label="客户名称" align="center" prop="companyName" width="350" :show-overflow-tooltip="true" />
      <el-table-column label="合同金额" align="center" prop="contractTotal" width="100" />
      <el-table-column label="合同状态" align="center" prop="localContractStatus" width="150">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.local_contract_status" :value="scope.row.localContractStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="上传合同" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-upload
            ref="uploadRef"
            action=""
            accept=".xlsx, .xls, .docx, .doc, .pdf"
            :auto-upload="false"
            :on-change="(file, fileList) => { handleChange(file, fileList, scope.row.contractId) }"
            :show-file-list="false"
            :before-upload="beforeUpload"
          >
            <el-button type="text" size="mini">上传</el-button>
          </el-upload>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改合同内容对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1150px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="货物名称" prop="goodsName">
              <el-select
                v-model="form.goodsName"
                filterable
                remote
                clearable
                reserve-keyword
                style="width: 200px"
                placeholder="请输入货物名称关键字"
                :remote-method="remoteMethodGoodsName"
                :loading="remoteLoadingGoodsName">
                <el-option
                  v-for="item in contractOptionsGoodsName"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同类型" prop="contractType">
              <el-select
                v-model="form.contractType"
                placeholder="合同类型"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="dict in dict.type.contractmgr_contract_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" placeholder="请输入合同名称"  style="width: 200px"
                maxlength="64" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="合同编号" prop="contractId">
              <el-input v-model="form.contractId" placeholder="请输入合同编号" 
              :disabled="isUpdate" style="width: 200px"
              maxlength="64" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="签约日期" prop="signDate">
              <el-date-picker clearable
                v-model="form.signDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择签约日期"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交货日期" prop="deliveryDate">
              <el-date-picker clearable
                v-model="form.deliveryDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择交货日期"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="我方单位名称" prop="ourCompanyName">
              <el-select
                v-model="form.ourCompanyName"
                placeholder="我方单位名称"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="dict in dict.type.contract_our_company_name"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="我方负责人" prop="ourPrincipal">
              <el-input v-model="form.ourPrincipal" placeholder="请输入我方负责人" style="width: 200px"
                maxlength="32" show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="对方单位名称" prop="companyName">
              <el-select
                v-model="form.companyName"
                filterable
                remote
                clearable
                reserve-keyword
                placeholder="请输入对方单位名称关键字"
                style="width: 200px"
                :remote-method="remoteMethodClientName"
                :loading="remoteLoadingSClientName"
                @change="selChangeClientName">
                <el-option
                  v-for="item in optionsClientName"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="对方负责人" prop="oppositePrincipal">
              <el-input v-model="form.oppositePrincipal" placeholder="请输入对方负责人" style="width: 200px"
                maxlength="32" show-word-limit />
            </el-form-item>
        </el-col>
          <el-col :span="8">
            <el-form-item label="合同数量" prop="contractQuantity">
              <el-input v-model="form.contractQuantity" placeholder="请输入合同数量" style="width: 200px" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同单价" prop="contractPrice">
              <el-input v-model="form.contractPrice" placeholder="请输入合同单价" style="width: 200px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- <el-col :span="8" v-show="displayContractTotal">
            <el-form-item label="合同总价" prop="contractTotal">{{calContractTotal}}</el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="合同金额" prop="contractTotal">{{calContractTotal}}</el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交货方式" prop="deliveryMethod">
              <el-input v-model="form.deliveryMethod" placeholder="请输入交货方式" style="width: 200px" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="港口到厂运费" prop="portToFactoryFare">
              <el-input v-model="form.portToFactoryFare" placeholder="请输入港口到厂运费" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="8" v-show="!displayContractTotal">
            <el-form-item label="港口到港口运费" prop="portToPortFare">
              <el-input v-model="form.portToPortFare" placeholder="请输入港口到港口运费" style="width: 200px" />
            </el-form-item>
          </el-col> -->
        </el-row>
        <el-row>
          <!-- <el-col :span="8" v-show="displayContractTotal">
            <el-form-item label="港口到港口运费" prop="portToPortFare">
              <el-input v-model="form.portToPortFare" placeholder="请输入港口到港口运费" style="width: 200px" />
            </el-form-item>
          </el-col> -->
          <el-col :span="8">
            <el-form-item label="港口到港口运费" prop="portToPortFare">
              <el-input v-model="form.portToPortFare" placeholder="请输入港口到港口运费" style="width: 200px" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="其他" prop="contractOther">
              <el-input v-model="form.contractOther" placeholder="请输入其他" style="width: 200px" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="代理或合作方" prop="contractAgent">
              <el-input v-model="form.contractAgent" placeholder="请输入代理或合作方" style="width: 200px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="合同备注" prop="contractRemark">
              <el-input v-model="form.contractRemark" placeholder="请输入合同备注" style="width: 940px"
                maxlength="256" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <el-row :gutter="10" class="mb8" v-show="showApproval">
        <el-divider />
        <el-col :span="12">
          <h3 style="display:inline; margin-right: 15px">合同审批信息</h3>
          <el-switch
            v-model="showApprovalSwitch"
            active-color="#13ce66">
          </el-switch>
        </el-col>
      </el-row>
      <el-form ref="formApproval" :model="formApproval" label-width="110px" v-show="showApproval && showApprovalSwitch">
        <el-row>
          <el-col :span="8"><el-form-item label="合同编号">{{formApproval.contractId}}</el-form-item></el-col>
          <el-col :span="8"><el-form-item label="审批编号">{{formApproval.approvalId}}</el-form-item></el-col>
          <el-col :span="8"><el-form-item label="标题">{{formApproval.approvalTitle}}</el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><el-form-item label="审批状态">{{formApproval.approvalStatus}}</el-form-item></el-col>
          <el-col :span="8"><el-form-item label="审批结果">{{formApproval.approvalResult}}</el-form-item></el-col>
          <el-col :span="8"><el-form-item label="发起时间">{{formApproval.launchTime}}</el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><el-form-item label="完成时间">{{formApproval.completeTime}}</el-form-item></el-col>
          <el-col :span="8"><el-form-item label="耗时">{{formApproval.takeupTime}}</el-form-item></el-col>
          <el-col :span="8"><el-form-item label="发起人工号">{{formApproval.launchJobId}}</el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><el-form-item label="发起人ID">{{formApproval.launchId}}</el-form-item></el-col>
          <el-col :span="8"><el-form-item label="发起人姓名">{{formApproval.launchName}}</el-form-item></el-col>
          <el-col :span="8"><el-form-item label="发起人部门">{{formApproval.launchDepartment}}</el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="8"><el-form-item label="审批人姓名">{{formApproval.approvalName}}</el-form-item></el-col>
          <el-col :span="16"><el-form-item label="当前处理人姓名">{{formApproval.processorName}}</el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="审批记录">
              <el-input
                type="textarea"
                :rows="6"
                v-model="formApproval.approvalRecords">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm('1')" :disabled="form.constractIsExist == 1">保 存</el-button>
        <el-button type="warning" @click="submitForm('2')" :disabled="form.localContractStatus == 2 ? false : true">生 成</el-button>
        <el-button @click="cancel(1)">取 消</el-button>
      </div>
    </el-dialog> 

    <!-- 合同导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 合同同步结果列表 start -->
    <el-dialog :title="titleContractSyncResult" :visible.sync="openContractSyncResult" 
      width="80%" append-to-body :close-on-click-modal="false">
      <el-table v-loading="loadingContractSyncResult" :data="listContractSyncResult">
      <el-table-column label="合同编号" align="center" prop="contractId" width="200" />
      <el-table-column label="合同名称" align="center" prop="contractName" width="300" />
      <el-table-column label="合同类型" align="center" prop="contractType" width="150" />
      <el-table-column label="同步时间" align="center" prop="syncTime" width="150" />
      <el-table-column label="部门名称" align="center" prop="deptName" width="200" />
      <el-table-column label="状态描述" align="center" prop="statusDescription" class-name="small-padding fixed-width" :show-overflow-tooltip="true" />
    </el-table>
    <pagination
      v-show="totalContractSyncResult > 0"
      :total="totalContractSyncResult"
      :page.sync="queryParamsContractSyncResult.pageNum"
      :limit.sync="queryParamsContractSyncResult.pageSize"
      @pagination="getListContractSyncResult"
    />
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeContractSyncResult">关 闭</el-button>
    </div>
    </el-dialog>
    <!-- 合同同步结果列表 end -->
  </div>
</template>

<script>
import { listContract, getContract, addContract, delContract, updateContract, 
  syncContract, uploadFile, getContractAdditional, delteFile, getContractApprovalInfoByContractId, 
  listContractSyncResultData } from "@/api/contract/contract";
import { listMaterialData } from "@/api/masterdata/material";
import { listClient } from "@/api/masterdata/client";
import { download } from "@/utils/request";
import { getToken } from "@/utils/auth";
import { converTDateToDate } from "@/utils/xmy";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Contract",
  dicts: ['contractmgr_contract_approval_status', 'contractmgr_contract_type', 'contract_our_company_name', 'local_contract_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 合同同步结果遮罩层
      loadingContractSyncResult: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedContractAdditionalInfo: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 显示审批详细
      showApproval: true,
      showApprovalSwitch: true,
      // 总条数
      total: 1,
      // 合同同步结果总条数
      totalContractSyncResult: 1,
      // 合同管理表格数据
      contractList: [],
      // 合同同步结果表格数据
      listContractSyncResult: [],
      // 弹出层标题
      title: "",
      // 合同同步结果弹出层标题
      titleContractSyncResult: "",
      // 是否显示弹出层
      open: false,
      // 是否显示合同详细弹出层
      openDetail: false,
      // 是否显示合同同步结果列表弹出层
      openContractSyncResult: false,
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/contract/mgr/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractName: null,
        contractId: null,
        signDate: null,
        oppositeCompanyName: null,
        leftContractTotal: null,
        rightContractTotal: null,
        materialName: null
      },
      // 合同同步结果查询参数
      queryParamsContractSyncResult: {
        pageNum: 1,
        pageSize: 10,
        contractId: null
      },
      // 表单参数
      form: {},
      // 合同详细表单
      formDetail: {},
      formApproval: {},
      // 表单校验
      rules: {
        goodsId: [
          { required: true, message: "货物编号不能为空", trigger: "blur" }
        ],
        goodsName: [
          { required: true, message: "货物名称不能为空", trigger: "blur" }
        ],
        contractType: [
          { required: true, message: "合同类型不能为空", trigger: "change" }
        ],
        contractName: [
          { required: true, message: "合同名称不能为空", trigger: "blur" }
        ],
        contractId: [
          { required: true, message: "合同编号不能为空", trigger: "blur" }
        ],
        contractQuantity: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的合同数量", trigger: "blur" }
        ],
        contractPrice: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的合同单价", trigger: "blur" }
        ],
        companyName: [
          { required: true, message: "对方单位名称不能为空", trigger: "change" }
        ]
      },
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      srcList: [
        'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
        'https://fuss10.elemecdn.com/1/8e/aeffeb4de74e2fde4bd74fc7b4486jpeg.jpeg'
      ],
      isUpdate: false,
      isSave: false, // 是否单选保存按钮
      // 合同附件列表
      contractAdditionalList: [],
      // 合同审批记录
      contractApprovalRecordList: [],
      remoteLoadingGoodsName: false,
      contractOptionsGoodsName: [],
      contractListGoodsName: [],
      // 客户姓名选择用
      optionsClientName: [],
      listClientName: [],
      remoteLoadingSClientName: false,
      isAdmin: false,
      user: {},
      // displayContractTotal: true
    };
  },
  filters:{
    getDownloadFileName(value) {
      // let index = value.lastIndexOf("\\"); // 提交阿里云切换为Linux的路径分隔符/
      let index = value.lastIndexOf("/"); // 提交阿里云切换为Linux的路径分隔符/
      return value.substring(index + 1, index.length);
    }
  },
  created() {
    this.getList();
    this.getUser();
  },
  computed: {
    calContractTotal: function () {
      if (this.form.contractQuantity && this.form.contractPrice) {
        return Number(this.form.contractQuantity) * Number(this.form.contractPrice)
      }
      
      return 0;
    }
  },
  methods: {
    /** 根据输入货物名称关键字，取得货物名称列表 */
    remoteMethodGoodsName(query) {
      if (query !== '') {
        this.remoteLoadingGoodsName = true;
        this.queryParams.materialName = query;
        // console.log("取得货物名称远程方法调用查询参数：" + JSON.stringify(this.queryParams));
        listMaterialData(this.queryParams).then(response => {
          this.remoteLoadingGoodsName = false;
          this.contractListGoodsName = response.rows;
          this.contractOptionsGoodsName = response.rows.map(item => {
            return { value: `${item.materialId}`, label: `${item.materialName}` };
          }).filter(item => {
            return item.label.toLowerCase()
              .indexOf(query.toLowerCase()) > -1;
          });
        });
      } else {
        this.contractOptionsGoodsName = [];
      }
    },
    /** 根据输入客户姓名关键字，取得客户姓名列表 */
    remoteMethodClientName(query) {
      if (query !== '') {
        this.remoteLoadingSClientName = true;
        this.queryParams.companyName = query;
        this.queryParams.recordFlag = 2;
        listClient(this.queryParams).then(response => {
          this.remoteLoadingSClientName = false;
          this.listClientName = response.rows;
          this.optionsClientName = response.rows.map(item => {
            return { value: `${item.baseId}`, label: `${item.companyName}` };
          }).filter(item => {
            return item.label.toLowerCase()
              .indexOf(query.toLowerCase()) > -1;
          });
        });
      } else {
        this.optionsClientName = [];
      }
    },
    /** 客户姓名下拉列表框，选择值改变后回调方法 */
    selChangeClientName(selValue) {
      console.log("选择的客户姓名编号是：" + selValue);
      this.form.oppositeCompanyName = selValue; // 客户姓名编号
    },
    /** 查询合同管理列表 */
    getList() {
      this.loading = true;
      listContract(this.queryParams).then(response => {
        this.contractList = response.rows;
        // console.log(response.rows);
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel(dialogType) {
      if (dialogType === 1) {
        this.open = false;
      } else {
        this.openDetail = false;
      }
      
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        goodsId: null,
        goodsName: null,
        contractType: null,
        contractName: null,
        contractId: null,
        signDate: null,
        deliveryDate: null,
        ourCompanyName: null,
        ourPrincipal: null,
        oppositeCompanyName: null,
        oppositePrincipal: null,
        contractQuantity: null,
        contractPrice: null,
        contractTotal: null,
        accountingPeriod: null,
        deliveryMethod: null,
        portToFactoryFare: null,
        portToPortFare: null,
        contractOther: null,
        contractAgent: null,
        contractRemark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.contractId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加合同数据";
      this.isUpdate = false;
      this.showApproval = false;
      this.form.contractActionType = '0';
      this.form.ourCompanyName = '1'
      // this.displayContractTotal = false;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const contractId = row.contractId || this.ids;
      getContract(contractId).then(response => {
        this.form = response.data;
        console.log("@@@@@@" + response.data.constractIsExist);
        this.form.constractIsExist = response.data.constractIsExist;
        this.title = "修改合同数据";
        this.isUpdate = true;
        // if (this.form.contractStatus == 'MANUALADD' || this.form.contractStatus == '7IMPORT') {
        //   this.displayContractTotal = false;
        // } else {
        //   this.displayContractTotal = true;
        // }
      });

      getContractAdditional(contractId).then(response => {
        // console.log(JSON.stringify(response.rows));
        this.contractAdditionalList = response.rows;
      });
    
      // 根据合同编号，取得合同审批数据
      getContractApprovalInfoByContractId(contractId).then(response => {
          // console.log("根据合同编号，取得合同审批数据" + JSON.stringify(response.data));
          if (response.data != undefined) {
            this.formApproval = response.data;
            // console.log("取得审批编号对应的审批记录数据" + JSON.stringify( response.data.approvalRecordList));
            response.data.approvalRecordList.forEach((element, index) => {
              // 审批人姓名 | 审批记录 | 完成时间 | 审批结果
              if (index == 0) {
                this.formApproval.approvalRecords = 
                element.approvalName + " | " + 
                element.approvalRecord + " | " + 
                converTDateToDate(element.completeTime) + " | " + 
                element.approvalResult + "\n";
              } else {
                this.formApproval.approvalRecords += 
                element.approvalName + " | " + 
                element.approvalRecord + " | " + 
                converTDateToDate(element.completeTime) + " | " + 
                element.approvalResult + "\n";
              }
            });
            this.showApproval = true;
          } else {
            this.showApproval = false;
          }
          

        this.open = true;
      });
    },
    /** 提交按钮 */
    submitForm(actionType) {
      if (this.form.contractId.includes("/")) {
        this.$modal.msgError(`合同编号包含非法字符[/]!`);
        return;
      }

      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.contractTotal = this.calContractTotal; // 合同金额赋值
          if (this.isUpdate) {
            this.form.contractActionType = actionType;
            updateContract(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.contractActionType = actionType;
            addContract(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const contractId = row.contractId || this.ids;
      this.$modal.confirm('是否确认删除合同编号为"' + contractId + '"的数据项？').then(function() {
        return delContract(contractId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('contract/mgr/export', {
        ...this.queryParams
      }, `合同管理_合同列表_${new Date().getFullYear()}年${new Date().getMonth()+1}月${new Date().getDate()}日 ${new Date().getHours()}:${new Date().getMinutes()}:${new Date().getSeconds()}.xlsx`)
    },
    /** 同步合同 */
    handleContractSync() {
      this.loading = true;
      syncContract(this.queryParams).then(response => {
        this.$modal.msgSuccess("同步合同成功");
        this.loading = false;
        this.getList();
      }).catch(() => {
        this.loading = false;
        this.getList();
      });
    },
    /** 查看合同数据 */ 
    // handleView(row) {
    //   getContractAdditional(row.contractId).then(response => {
    //       console.log(JSON.stringify(response.rows));
    //       this.contractAdditionalList = response.rows;
    //       this.formDetail = row;
    //       this.openDetail = true;
    //   });

    // },
    /** 文件上传 */
    handleChange(file, fileList, uploadContractId) {
      console.log("文件上传" + uploadContractId);
      if(file != null){
        let formData = new FormData();
        formData.append('file', file.raw)
        formData.append('uploadContractId', uploadContractId)
        uploadFile(formData).then(response => {
          console.log(response)
          this.$modal.msgSuccess("上传成功！");
          getContractAdditional(uploadContractId).then(response => {
              console.log(JSON.stringify(response.rows));
              this.contractAdditionalList = response.rows;
          });
        });
      }
    },
    // 上传预处理
    beforeUpload(file) {
      console.log("上传预处理file：" + file);
      // if (file.type.indexOf("image/") == -1) {
      //   this.$modal.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      // } else if (file.type.indexOf(".xlsx") == -1 || file.type.indexOf(".xls") == -1) {
      //   this.$modal.msgError("文件格式错误，请上传图片类型,如：.xlsx，.xls后缀的文件。");
      // } else if (file.type.indexOf(".docx") == -1 || file.type.indexOf(".doc") == -1) {
      //   this.$modal.msgError("文件格式错误，请上传图片类型,如：.docx，.doc后缀的文件。");
      // }
      if (file.type.indexOf(".xlsx") == -1 || file.type.indexOf(".xls") == -1) {
        this.$modal.msgError("文件格式错误，请上传正确的类型,如：.xlsx，.xls, .docx，.doc, .pdf后缀的文件。");
      } else if (file.type.indexOf(".docx") == -1 || file.type.indexOf(".doc") == -1) {
        this.$modal.msgError("文件格式错误，请上传正确的类型,如：.xlsx，.xls, .docx，.doc, .pdf后缀的文件。");
      } else if (file.type.indexOf(".pdf") == -1 || file.type.indexOf(".PDF") == -1) {
        this.$modal.msgError("文件格式错误，请上传正确的类型,如：.xlsx，.xls, .docx，.doc, .pdf后缀的文件。");
      }
    },
    downLoadFile(filePath) {
      // let index = filePath.lastIndexOf("\\"); // 提交阿里云切换为Linux的路径分隔符/
      let index = filePath.lastIndexOf("/"); // 提交阿里云切换为Linux的路径分隔符/
      let fileName = filePath.substring(index + 1, index.length);

      let params = {
        resource:filePath
      }

      download("/common/download/resource", params, fileName);
    },
    delteFileByAdditionalId(contractId, additionalId) {
        delteFile(contractId, additionalId).then(response => {
          console.log(response)
          this.$modal.msgSuccess("删除附件成功！");
          console.log(JSON.stringify(response.rows));
          this.contractAdditionalList = response.rows;
      });
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "合同导入";
      this.upload.open = true;
    },
       /** 下载模板操作 */
    importTemplate() {
      this.download('/contract/mgr/importTemplate', {
      }, `合同模板_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 取得用户信息 */
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
        this.judgeIsAdmin();
      });
    },
    /** 判断登录使用是否是管理员 */
    judgeIsAdmin() {
      let userName = this.user.userName;
      if (userName == 'admin' || userName == 'zjltest') {
        this.isAdmin = true;
      } else {
        this.isAdmin = false;
      }
    },
    /** 同步结果按钮操作 */
    handleContractSyncResult() {
      this.openContractSyncResult = true;
      this.titleContractSyncResult = "查看合同同步结果";
      this.getListContractSyncResult();
    },
    /** 查询合同同步结果列表 */
    getListContractSyncResult() {
      this.loadingContractSyncResult = true;
      listContractSyncResultData(this.queryParamsContractSyncResult).then(response => {
        this.listContractSyncResult = response.rows;
        this.totalContractSyncResult = response.total;
        this.loadingContractSyncResult = false;
      });


    },
    /** 关闭查看合同同步结果列表按钮 */
    closeContractSyncResult() {
     this.openContractSyncResult = false;
    }
  }
};
</script>

<style scoped>
  i:hover{
    cursor:pointer
  }
</style>
