<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="运输单号" prop="transportdocumentsId">
        <el-input
          v-model="queryParams.transportdocumentsId"
          placeholder="请输入运输单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业务日期" prop="businessDate">
        <el-date-picker clearable
          v-model="queryParams.businessDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择业务日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="单据类型" prop="documentsType">
        <el-select v-model="queryParams.documentsType" placeholder="请选择单据类型" clearable>
          <el-option
            v-for="dict in dict.type.transportdocuments_documents_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="运输单状态" prop="transportdocumentsState">
        <el-select v-model="queryParams.transportdocumentsState" placeholder="请选择运输单状态" clearable>
          <el-option
            v-for="dict in dict.type.transportdocuments_state"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['transportdocuments:detail:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['transportdocuments:detail:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['transportdocuments:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['transportdocuments:detail:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleImport"
          v-hasPermi="['transportdocuments:detail:export']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-connection"
          size="mini"
          :disabled="multiple"
          @click="handleTransfer"
        >生成中转运单</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detailList" @selection-change="handleSelectionChange" :key="key" @cell-dblclick="doubleClick">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="运输单号" align="center" prop="transportdocumentsId">
        <template slot-scope="scope">
          <el-input 
            v-focus v-if="scope.row[scope.column.property + 'Show']" 
            clearable 
            v-model="scope.row.transportdocumentsId"
            @keyup.enter.native="$event.target.blur"
            @blur="onBlur(scope.row, scope.column)" />
          <span v-else>{{ scope.row.transportdocumentsId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="经办人姓名" align="center" prop="handledByName" />
      <el-table-column label="物料名称" align="center" prop="materialName" />
      <el-table-column label="业务日期" align="center" prop="businessDate" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.businessDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单据类型" align="center" prop="documentsType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.transportdocuments_documents_type" :value="scope.row.documentsType"/>
        </template>
      </el-table-column>
      <el-table-column label="运输单状态" align="center" prop="transportdocumentsState" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.transportdocuments_state" :value="scope.row.transportdocumentsState"/>
        </template>
      </el-table-column>
      <el-table-column label="关联订单" align="center" prop="relatedOrderId">
        <template slot-scope="scope">
          <el-input 
            v-focus v-if="scope.row[scope.column.property + 'Show']" 
            clearable 
            v-model="scope.row.relatedOrderId"
            @keyup.enter.native="onBlur(scope.row, scope.column)" 
            @blur="onBlur(scope.row, scope.column)" />
          <span v-else>{{ scope.row.relatedOrderId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['transportdocuments:detail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['transportdocuments:detail:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-location-information"
            @click="handleTrace(scope.row)"
            v-hasPermi="['transportdocuments:detail:trace']"
          >追踪</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改运输单详细信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1100px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="运输单号" prop="transportdocumentsId">
              <el-input v-model="form.transportdocumentsId" placeholder="请输入运输单号" :disabled="true" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="批次号" prop="pch">
              <el-input v-model="form.pch" placeholder="请输入批次号" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="车号" prop="wagonNumber">
              <el-input v-model="form.wagonNumber" placeholder="请输入车号" style="width: 200px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="发货地名称" prop="sourcePlaceName">
              <!-- 运输单发货地址是仓库，就是销售单，创建运输单时需要仓库减去对应数量 -->
              <el-input v-model="form.sourcePlaceName" placeholder="请输入发货地名称" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="卸货地名称" prop="targetPlaceName">
              <!-- 运输单最终目的地是仓库，就是采购单，当运输单完成时，需要仓库加上对应数量 -->
              <el-input v-model="form.targetPlaceName" placeholder="请输入卸货地名称" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="装车数量" prop="loadingQuantity">
              <el-input v-model="form.loadingQuantity" placeholder="请输入装车数量" style="width: 200px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="经办人姓名" prop="handledByName">
              <el-input v-model="form.handledByName" placeholder="请输入经办人姓名" style="width: 200px;" />
            </el-form-item>
            <el-input v-model="form.handledById" v-show="false" />
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话" prop="telephone">
              <el-input v-model="form.telephone" placeholder="请输入联系电话" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="物料名称" prop="materialId">
              <el-select
                v-model="form.materialId"
                filterable
                remote
                clearable
                reserve-keyword
                placeholder="请输入物料名称关键字"
                :remote-method="remoteMethodMaterialName"
                :loading="remoteLoadingMaterialName"
                @change="selChangeMaterial"
                style="width: 200px">
                <el-option
                  v-for="item in optionsMaterialName"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-input v-model="form.materialId"  v-show="false" />
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="业务日期" prop="businessDate">
              <el-date-picker clearable
                v-model="form.businessDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择业务日期"
                style="width: 200px;">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单据类型" prop="documentsType">
              <el-select v-model="form.documentsType" placeholder="请选择单据类型" style="width: 200px;" >
                <el-option
                  v-for="dict in dict.type.transportdocuments_documents_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单价" prop="unitPrice">
              <el-input 
                v-model="form.unitPrice" 
                placeholder="请输入单价" 
                @input="inputUnitPrice"
                style="width: 200px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="运输单状态" prop="transportdocumentsState">
              <el-select 
                v-model="form.transportdocumentsState" 
                placeholder="请选择运输单状态"
                @change="selChangeTransportdocumentsState"
                style="width: 200px;"
                >
                <el-option
                  v-for="dict in dict.type.transportdocuments_state"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="关联订单" prop="relatedOrderId">
              <el-input v-model="form.relatedOrderId" placeholder="请输入关联订单" style="width: 200px;" />
            </el-form-item>
            <el-input v-model="form.handledById" v-show="false" />
          </el-col>
          <el-col :span="8">
            <el-form-item label="关联合同" prop="relatedContractId">
              <el-select
                v-model="form.relatedContractId"
                filterable
                remote
                clearable
                reserve-keyword
                multiple
                placeholder="请输入合同编号关键字"
                style="width: 200px"
                :remote-method="remoteMethodByTransport"
                :loading="remoteLoadingByTransport">
                <el-option
                  v-for="item in optionsByTransport"
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
            <el-form-item label="卸货数量" prop="landedQuantity">
              <el-input v-model="form.landedQuantity" placeholder="请输入卸货数量" :disabled="isEditByTransportState" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="核算数量" prop="accountingQuantity">
              <el-input v-model="form.accountingQuantity" placeholder="请输入核算数量" :disabled="isEditByTransportState" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结算单价" prop="settlementUnitPrice">
              <el-input v-model="form.settlementUnitPrice" placeholder="请输入结算单价" :disabled="isEditByTransportState" style="width: 200px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="运费单价" prop="freightUnitPrice">
              <el-input v-model="form.freightUnitPrice" placeholder="请输入运费单价" :disabled="isEditByTransportState" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="扣款金额" prop="deductionAmount">
              <el-input v-model="form.deductionAmount" placeholder="请输入扣款金额" :disabled="isEditByTransportState" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="压车费" prop="followUpFare">
              <el-input v-model="form.followUpFare" placeholder="请输入压车费" :disabled="isEditByTransportState" style="width: 200px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="卸货日期" prop="landedDate">
              <el-date-picker clearable
                v-model="form.landedDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择卸货日期"
                :disabled="isEditByTransportState"
                style="width: 200px;">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="涨量">
              <el-input :disabled="true" style="width: 200px;" :value="calInflation" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="运费">
              <el-input :disabled="true" style="width: 200px;" :value="calCarriage" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="实付运费金额">
              <el-input :disabled="true" style="width: 200px;" :value="calRealCarriageAmount" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="货损金额">
              <el-input :disabled="true" style="width: 200px;" :value="calCargoDamageAmount" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 运输单导入对话框 -->
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
          <!-- <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的用户数据
          </div> -->
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 生成中转运单对话框 -->
    <el-dialog :title="titleTransfer" :visible.sync="openTransfer" width="400px" append-to-body>
      <el-form ref="formTransfer" :model="formTransfer" :rules="rulesTransfer" label-width="130px">
        <el-form-item label="运输方式" prop="transportMode">
          <el-select
            v-model="formTransfer.transportMode"
            placeholder="请选择运输方式"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="dict in dict.type.purchasesale_transport_mode"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="装载量" prop="transportLoadingCapacity">
          <el-input v-model="formTransfer.transportLoadingCapacity" placeholder="请输入装载量" style="width: 200px;" />
        </el-form-item>
        <el-form-item label="计量单位" prop="transportUnitOfMeasurement">
          <el-radio-group v-model="formTransfer.transportUnitOfMeasurement">
            <el-radio-button :label="1">吨</el-radio-button>
            <el-radio-button :label="2">斤</el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormTransfer">确 定</el-button>
        <el-button @click="cancelTransfer">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看订单追踪对话框 -->
    <el-dialog :title="titleTrace" :visible.sync="openTrace" width="650px" append-to-body>
      <el-table v-loading="loadingTrace" :data="traceList">
        <el-table-column label="前置运单编号" align="center" prop="preTransportdocumentsId" />
        <el-table-column label="当前运单编号" align="center" prop="transportdocumentsId" /> 
        <el-table-column label="后置运单编号" align="center" prop="postTransportdocumentsId" class-name="small-padding fixed-width" /> 
      </el-table>
    
      <pagination
        v-show="totalTrace > 0"
        :total="totalTrace"
        :page.sync="queryTraceParams.pageNum"
        :limit.sync="queryTraceParams.pageSize"
        @pagination="getTraceList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeTrace">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDetail, getDetail, delDetail, addDetail, updateDetail, generateTransport, generateTransportId } from "@/api/transportdocuments/detail";
import { listTrace } from "@/api/transportdocuments/trace";
import { listContract } from "@/api/contract/contract";
import { listWarehouse } from "@/api/masterdata/warehouse";
import { regionData, CodeToText, TextToCode } from "element-china-area-data"
import { listMaterialData } from "@/api/masterdata/material";
import { getToken } from "@/utils/auth";

export default {
  name: "Detail",
  dicts: ['transportdocuments_state', 'transportdocuments_documents_type', 'purchasesale_transport_mode'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 运单追踪遮罩层
      loadingTrace: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 运单追踪总条数
      totalTrace: 0,
      // 运输单详细信息表格数据
      detailList: [],
      // 弹出层标题
      title: "",
      // 中转运单弹出层标题
      titleTransfer: "",
      // 查看运单追踪弹出层标题
      titleTrace: "",
      // 是否显示弹出层
      open: false,
      // 是否显示中转运单弹出层
      openTransfer: false,
      // 是否显示运单追踪弹出层
      openTrace: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        transportdocumentsId: null,
        businessDate: null,
        documentsType: null,
        transportdocumentsState: null,
      },
      // 查询运单追踪参数
      queryTraceParams: {
        pageNum: 1,
        pageSize: 10,
        transportdocumentsId: null,
      },
      // 表单参数
      form: {},
      // 中转运单表单参数
      formTransfer: {},
      // 表单校验
      rules: {
        transportdocumentsId: [
          { required: true, message: "运输单号不能为空", trigger: "blur" }
        ],
        pch: [
          { required: true, message: "批次号不能为空", trigger: "blur" }
        ],
        wagonNumber: [
          { required: true, message: "车号不能为空", trigger: "blur" }
        ],
        sourcePlaceId: [
          { required: true, message: "发货地名称不能为空", trigger: "blur" }
        ],
        loadingQuantity: [
          { required: true, message: "装车数量不能为空", trigger: "blur" }
        ],
        handledByName: [
          { required: true, message: "经办人姓名不能为空", trigger: "blur" }
        ],
        telephone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        materialId: [
          { required: true, message: "物料编号不能为空", trigger: "blur" }
        ],
        materialName: [
          { required: true, message: "物料名称不能为空", trigger: "blur" }
        ],
        businessDate: [
          { required: true, message: "业务日期不能为空", trigger: "blur" }
        ],
        documentsType: [
          { required: true, message: "单据类型不能为空", trigger: "change" }
        ],
        unitPrice: [
          { required: true, message: "单价不能为空", trigger: "blur" }
        ],
        transportdocumentsState: [
          { required: true, message: "运输单状态不能为空", trigger: "change" }
        ],
        relatedOrderId: [
          { required: true, message: "关联订单不能为空", trigger: "blur" }
        ],
        landedQuantity: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的卸货数量", trigger: "blur" }
        ],
        accountingQuantity: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的核算数量", trigger: "blur" }
        ],
        settlementUnitPrice: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的结算单价", trigger: "blur" }
        ],
        freightUnitPrice: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的运费单价", trigger: "blur" }
        ],
        deductionAmount: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的扣款金额", trigger: "blur" }
        ],
        followUpFare: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的压车费", trigger: "blur" }
        ],
      },
      // 中转运单表单校验
      rulesTransfer: {
        transportMode: [
          { required: true, message: "请选择运输方式", trigger: "change" }
        ],
        transportLoadingCapacity: [
          { required: true, message: "请输入装载量", trigger: "blur" },
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的装载量", trigger: "blur" }
        ],
        transportUnitOfMeasurement: [
          { required: true, message: "请选择计量单位", trigger: "change" }
        ],
      },
      // 省市区级联数据
      regionOptions: regionData,
      // 物料名称选择用
      optionsMaterialName: [],
      listMaterialName: [],
      remoteLoadingMaterialName: false,
      upload: {
        // 是否显示弹出层（回款导入）
        open: false,
        // 弹出层标题（回款导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的回款数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/transportdocuments/detail/importData/p"
      },
      isEditByTransportState: true,
      // 合同编号选择用
      optionsByTransport: [],
      listByTransport: [],
      remoteLoadingByTransport: false,
      // 仓库名称选择用
      optionsWarehouseName: [],
      ListWarehouseName: [],
      remoteLoadingWarehouseName: false,
      key: Math.random(),
      hidTempTransportdocumentsId: null,
      // 运单追踪表格数据
      traceList: [],
    };
  },
  created() {
    this.getList();
  },
  computed: {
    /** 涨量 */
    calInflation: function () {
      if (this.form.landedQuantity && this.form.loadingQuantity) {
        return Number(this.form.landedQuantity) - Number(this.form.loadingQuantity)
      }
      return '';
    },
    /** 运费 */
    calCarriage: function () {
      if (this.form.unitPrice && this.form.landedQuantity) {
        return Number(this.form.unitPrice) * Number(this.form.landedQuantity)
      }
      return '';
    },
    /** 实付运费金额 */
    calRealCarriageAmount: function () {
      if (this.calCarriage && this.form.deductionAmount && this.form.followUpFare) {
        return this.calCarriage - Number(this.form.deductionAmount) + Number(this.form.followUpFare)
      }
      return '';
    },
    /** 货损金额 */
    calCargoDamageAmount: function () {
      if (this.form.loadingQuantity && this.form.landedQuantity && this.form.accountingQuantity && this.form.settlementUnitPrice) {
        return (Number(this.form.loadingQuantity) / Number(this.form.landedQuantity) - Number(this.form.accountingQuantity)) * this.form.settlementUnitPrice;
      }
      return '';
    },
  },
  methods: {
    /** 查询运输单详细信息列表 */
    getList() {
      this.loading = true;
      listDetail(this.queryParams).then(response => {
        this.detailList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询追踪运单数据列表 */
    getTraceList() {
      this.loadingTrace = true;
      listDetail(this.queryTraceParams).then(response => {
        this.traceList = response.rows;
        this.totalTrace = response.total;
        this.loadingTrace = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        transportdocumentsId: null,
        transportdocumentsType: null,
        pch: null,
        wagonNumber: null,
        sourcePlaceId: null,
        sourcePlaceName: null,
        targetPlaceId: null,
        targetPlaceName: null,
        loadingQuantity: null,
        handledById: null,
        handledByName: null,
        telephone: null,
        materialId: null,
        materialName: null,
        businessDate: null,
        documentsType: null,
        unitPrice: null,
        relatedOrderId: null,
        relatedContractId: null,
        relatedContractName: null,
        landedQuantity: null,
        accountingQuantity: null,
        settlementUnitPrice: null,
        freightUnitPrice: null,
        deductionAmount: null,
        followUpFare: null,
        landedDate: null,
        transportdocumentsState: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        bizVersion: null
      };

      this.formTransfer = {
        transportMode: null,
        transportLoadingCapacity: null,
        transportUnitOfMeasurement: null,
      }

      this.traceList = [];

      this.resetForm("form");
      this.resetForm("formTransfer");
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      generateTransportId().then(response => {
        this.reset();
        this.open = true;
        this.form.transportdocumentsState = '1';
        this.form.transportdocumentsId = response.transportdocumentsId;
        this.title = "添加采购运输单详细信息";
      });    
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDetail(id).then(response => {
        this.form = response.data;
        this.form.settlementUnitPrice = this.form.unitPrice;
        if (this.form.sourcePlaceId) {
          if (this.form.sourcePlaceId.indexOf('-') != -1) {
            this.form.sourcePlaceId = this.form.sourcePlaceId.split('-');
          }
        }
        
        if (this.form.sourcePlaceName) {
          let sourcePlaceArray = this.form.sourcePlaceName.split('/');
          this.form.sourcePlaceId = TextToCode[sourcePlaceArray[0]][sourcePlaceArray[1]][sourcePlaceArray[2]].code 
        }

        if (this.form.relatedContractId) {
          this.form.relatedContractId = this.form.relatedContractId.split('-');
        }

        this.optionsMaterialName = [];
        this.optionsMaterialName.push({
          label: this.form.materialName,
          value: this.form.materialId
        });

        this.optionsWarehouseName = [];
        this.optionsWarehouseName.push({
          label: this.form.targetPlaceName,
          value: this.form.targetPlaceId
        });

        this.open = true;
        this.title = "修改运输单详细信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      // 发货地省市区级联选择器数组转字符串
      // console.log("@@@@@@" + JSON.stringify(this.form.relatedContractId));
      let changgedSourcePlaceId = this.form.sourcePlaceId;
      if (changgedSourcePlaceId) {
        if (changgedSourcePlaceId instanceof Array) {
          this.form.sourcePlaceId = changgedSourcePlaceId.join('-');
        }
      }

      // 关联合同选择器数组转字符串
      let relatedContractIdArray = this.form.relatedContractId;
      if (relatedContractIdArray) {
        this.form.relatedContractId = relatedContractIdArray.join('-');
      }

      this.form.transportdocumentsType = 'P'; // 采购运单

      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDetail(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除运输单详细信息编号为"' + ids + '"的数据项？').then(function() {
        return delDetail(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('transportdocuments/detail/export', {
        ...this.queryParams
      }, `detail_${new Date().getTime()}.xlsx`)
    },
    /** 发货地省市区级联选择器变更后 */
    handleSourcePlaceRegionChange (value) {
      this.form.sourcePlaceName = this.getCodeToText(null, value)
    },
    /** 将城市代码转为文字 */
    getCodeToText (codeStr, codeArray) {
      if (null === codeStr && null === codeArray) {
          return null;
      } 
      else if (null === codeArray) {
          codeArray = codeStr.split(",");
      }
      let area = "";
      switch (codeArray.length) {
        case 1:
          area += CodeToText[codeArray[0]];
          break;
        case 2:
          area += CodeToText[codeArray[0]] + "/" + CodeToText[codeArray[1]];
          break;
        case 3:
          area +=
              CodeToText[codeArray[0]] +
              "/" +
              CodeToText[codeArray[1]] +
              "/" +
              CodeToText[codeArray[2]];
          break;
        default:
          break;
      } 
      return area;
    },
    /** 根据输入物料名称关键字，取得物料名称列表 */
    remoteMethodMaterialName(query) {
      if (query !== '') {
        this.remoteLoadingMaterialName = true;
        this.queryParams.materialName = query;
        listMaterialData(this.queryParams).then(response => {
          this.remoteLoadingMaterialName = false;
          this.listMaterialName = response.rows;
          this.optionsMaterialName = response.rows.map(item => {
            return { value: `${item.materialId}`, label: `${item.materialName}` };
          }).filter(item => {
            return item.label.toLowerCase()
              .indexOf(query.toLowerCase()) > -1;
          });
        });
      } else {
        this.optionsClientName = [];
      }
    },
    /** 物料名称下拉列表框，选择值改变后回调方法 */
    selChangeMaterial(selValue) {
      let selMaterial = this.listMaterialName.find(item => {
        return item.materialId == selValue;
      });
      this.form.materialName = selMaterial.materialName; // 物料名称
    },
    /** 根据输入合同编号关键字，取得合同编号列表 */
    remoteMethodByTransport(query) {
      if (query !== '') {
        this.remoteLoadingByTransport = true;
        listContract(this.queryParams).then(response => {
          this.remoteLoadingByTransport = false;
          this.listByTransport = response.rows;
          this.optionsByTransport = response.rows.map(item => {
            return { value: `${item.contractId}`, label: `${item.contractId}` };
          }).filter(item => {
            return item.label.toLowerCase()
              .indexOf(query.toLowerCase()) > -1;
          });
        });
      } else {
        this.optionsByTransport = [];
      }
    },
    /** 根据输入仓库名称关键字，取得仓库名称列表 */
    remoteWarehouseName(query) {
      if (query !== '') {
        this.remoteLoadingWarehouseName = true;
        this.queryParams.warehouseName = query;
        listWarehouse(this.queryParams).then(response => {
          this.remoteLoadingWarehouseName = false;
          this.ListWarehouseName = response.rows;
          this.optionsWarehouseName = response.rows.map(item => {
            return { value: `${item.warehouseCode}`, label: `${item.warehouseName}` };
          }).filter(item => {
            return item.label.toLowerCase()
              .indexOf(query.toLowerCase()) > -1;
          });
        });
      } else {
        this.optionsWarehouseName = [];
      }
    },
    /** 仓库名称下拉列表框，选择值改变后回调方法 */
    selChangeWarehouse(selValue) {
      let selWarehouse = this.ListWarehouseName.find(item => {
        return item.warehouseCode == selValue;
      });
      this.form.targetPlaceName = selWarehouse.warehouseName; // 仓库名称
    },
    /** 运输单状态下拉列表框，选择值改变后回调方法 */
    selChangeTransportdocumentsState(selValue) {
      if (selValue != '1') {
        this.isEditByTransportState = false;
      } else {
        this.isEditByTransportState = true;
      }
    },
    /** 单价输入框，在Input值改变时触发回调方法 */
    inputUnitPrice(value) {
      this.form.settlementUnitPrice = value;
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "采购运输单导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('/transportdocuments/detail/importTemplate', {}, `采购运输单导入模板_${new Date().getTime()}.xlsx`)
    },
    /** 文件上传中处理 */
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 
    /** 提交上传文件 */
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 处理中转运单 */
    handleTransfer() {
      for (let i = 0; i < this.ids.length; i++) {
        let isFind = false;
        for (let j = 0; j < this.detailList.length; j++) {
          if (this.ids[i] === this.detailList[j].id && this.detailList[j].transportdocumentsState != '2') {
            isFind = true;
            break;
          }
        }
        if (isFind) {
          this.$modal.msgError('要合并的有不包含中转状态运输单，请确认！');
          return false;
        }
      }

      // this.$modal.confirm('是否确认生成中转运输单？').then(() => {
      //   // 如果需要在then中使用this引用变量，必须使用箭头函数。
      //   // this.$modal.confirm('是否确认删除运输单详细信息编号为"' + ids + '"的数据项？').then(function(this.变量) {})，这种写法无法使用使用this引用变量
      //   return mergeDetail(this.ids);
      // }).then(() => {
      //   this.getList();
      //   this.$modal.msgSuccess("生成中转运输单成功");
      // }).catch(() => {});
      this.openTransfer = true;
      this.titleTransfer = "生成中转运单";
      this.formTransfer.transportUnitOfMeasurement = '1';
    },
    // 双击单元格触发事件
    doubleClick(row, column) {
      // 避免点击过快导致多个输入框处于焦点状态
      row[column.property + 'Show'] = false;
      // 避免点击其他单元格导致表格刷新 
      if (!['relatedOrderId'].includes(column.property) && !['transportdocumentsId'].includes(column.property)) return;
      row[column.property + 'Show'] = true;
      this.hidTempTransportdocumentsId = row.transportdocumentsId;
      // 刷新表格，显示input输入框
      this.key = Math.random(); 
    },
    //输入框失焦事件
    onBlur(row, column) {
      row[column.property + 'Show'] = false;
      // 请求后台更改数据
      this.updateTable(row);
    },
    //更新表格
    updateTable(row) {
      // 刷新表格，隐藏input输入框
      this.key = Math.random();
      row.tempTransportdocumentsId = this.hidTempTransportdocumentsId;
      updateDetail(row);
    },
    /** 生成中转运单提交按钮 */
    submitFormTransfer() {
      this.$refs["formTransfer"].validate(valid => {
        if (valid) {
          generateTransport(this.ids, this.formTransfer).then(response => {
            this.$modal.msgSuccess("生成中转运单成功");
            this.openTransfer = false;
            this.getList();
          });
        }
      });
      this.reset();
    },
    /** 生成中转运单取消按钮 */
    cancelTransfer() {
      this.openTransfer = false;
      this.reset();
    },
    /** 追踪按钮操作 */
    handleTrace(row) {
      this.reset();
      this.queryTraceParams.transportdocumentsId = row.transportdocumentsId;
      this.loadingTrace = true;
      listTrace(this.queryTraceParams).then(response => {
        this.traceList = response.rows;
        this.totalTrace = response.total;
        this.loadingTrace = false;

        this.openTrace = true;
        this.titleTrace = "查看追踪运单数据列表";
      });
    },
    /** 追踪运单关闭按钮 */
    closeTrace() {
      this.openTrace = false;
      this.reset();
    },
  }
};
</script>
