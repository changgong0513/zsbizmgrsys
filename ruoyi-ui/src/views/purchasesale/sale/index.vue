<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- 订单编号 -->
      <el-form-item label="订单编号" prop="contractId">
        <el-input
          v-model="queryParams.contractId"
          placeholder="请输入订单编号"
          clearable
        />
      </el-form-item>
      <!-- 所属部门 -->
      <el-form-item label="所属部门" prop="belongDept">
        <treeselect v-model="queryParams.belongDept" 
          :options="deptOptions" :show-count="true" 
          placeholder="请选择所属部门" style="width: 240px;" />
        </el-form-item>
      <!-- 经办人 -->
      <el-form-item label="经办人" prop="handledBy">
        <el-input
          v-model="queryParams.handledBy"
          placeholder="请输入经办人"
          clearable
        />
      </el-form-item>
      <!-- 物料名称 -->
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
          clearable
        />
      </el-form-item>
      <!-- 核算金额min -->
      <el-form-item label="核算金额" prop="checkMoneyMin">
        <el-input
          v-model="queryParams.checkMoneyMin"
          placeholder="请输入核算金额"
          clearable
        />
      </el-form-item>
      <!-- 核算金额max -->
      <el-form-item label="~" prop="checkMoneyMax" label-width="15px">
        <el-input
          v-model="queryParams.checkMoneyMax"
          placeholder="请输入核算金额"
          clearable
        />
      </el-form-item>
      <!-- 业务日期 -->
      <el-form-item label="业务日期" prop="businessDate">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['purchasesale:purchasesale:add']"
        >新增</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['purchasesale:purchasesale:edit']"
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
          v-hasPermi="['purchasesale:purchasesale:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['purchasesale:purchasesale:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 列表 -->
    <el-table v-loading="loading" :data="purchaseList" @selection-change="handleSelectionChange"
    @row-dblclick="handleView">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单编号" align="center" prop="orderId" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="业务日期" align="center" prop="businessDate" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.businessDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属部门" align="center" prop="deptName" width="100" />
      <el-table-column label="经办人" align="center" prop="handledBy" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="客户名称" align="center" prop="supplierRealName" width="240" :show-overflow-tooltip="true" />
      <el-table-column label="合同状态" align="center" prop="orderStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.purchase_mgr_order_status" :value="scope.row.orderStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="物料名称" align="center" prop="materialName" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="核算金额" align="center" prop="checkMoney" width="80" :show-overflow-tooltip="true" />
      <el-table-column label="完成率" align="center" prop="completionRate" width="100" />
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改销售收货销售发货管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <!-- 订单编号 -->
          <el-col :span="8">
            <el-form-item label="订单编号">
              <el-input v-model="form.orderId" placeholder="请输入订单编号" :disabled="this.isUpdate" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- 合同编号 -->
          <el-col :span="8">
            <el-form-item label="合同编号" prop="contractId">
              <el-input v-model="form.contractId" placeholder="请输入合同编号" :disabled="this.isUpdate" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- 经办人 -->
          <el-col :span="8">
            <el-form-item label="经办人" prop="handledBy">
              <el-input v-model="form.handledBy" placeholder="请输入经办人" style="width: 200px" maxlength="16"
                show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 所属部门 -->
          <el-col :span="8">
            <el-form-item label="所属部门" prop="belongDept">
              <treeselect v-model="form.belongDept" 
                :options="deptOptions" 
                :show-count="true" 
                placeholder="请选择所属部门" 
                style="width: 200px;" />
            </el-form-item>
          </el-col>
          <!-- 业务日期 -->
          <el-col :span="8">
            <el-form-item label="业务日期" prop="businessDate">
              <el-date-picker clearable
                v-model="form.businessDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择业务日期"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <!-- 客户名称 -->
          <el-col :span="8">
            <el-form-item label="客户名称" prop="supplierRealName">
                <el-select
                v-model="form.supplierRealName"
                filterable
                remote
                clearable
                reserve-keyword
                placeholder="请输入客户名称关键字"
                style="width: 200px"
                :remote-method="remoteMethodClientName"
                :loading="remoteLoadingSClientName">
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
          <!-- 物料名称 -->
          <el-col :span="8">
            <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="form.materialName" placeholder="请输入物料名称" style="width: 200px" maxlength="64"
                show-word-limit />
            </el-form-item>
          </el-col>
          <!-- 销售数量 -->
          <el-col :span="8">
            <el-form-item label="销售数量" prop="purchaseQuantity">
              <el-input v-model="form.purchaseQuantity" placeholder="请输入销售数量" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- 单价 -->
          <el-col :span="8">
            <el-form-item label="单价" prop="unitPrice">
              <el-input v-model="form.unitPrice" placeholder="请输入单价" style="width: 200px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 计量单位 -->
          <el-col :span="8">
            <el-form-item label="计量单位" prop="meteringUnit">
              <el-select
                v-model="form.meteringUnit"
                placeholder="计量单位"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="dict in dict.type.masterdata_warehouse_measurement_unit"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- 预计到货期 -->
          <el-col :span="8">
            <el-form-item label="预计到货期" prop="arrivalDate">
              <el-date-picker clearable
                v-model="form.arrivalDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择预计到货期"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <!-- 要求到货期 -->
          <el-col :span="8">
            <el-form-item label="要求到货期" prop="requiredDeliveryDate">
              <el-date-picker clearable
                v-model="form.requiredDeliveryDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择要求到货期"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 账期 -->
          <el-col :span="8">
            <el-form-item label="账期" prop="accountPeriod">
              <el-input v-model="form.accountPeriod" placeholder="请输入账期" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- 到账条件 -->
          <el-col :span="3">
            <el-form-item label="到账条件" prop="arrivalTerms">
              <el-select
                v-model="form.arrivalTerms"
                placeholder="到账条件"
                clearable
                style="width: 130px"
              >
                <el-option
                  v-for="dict in dict.type.purchasesale_arrival_terms"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- 到账条件值 -->
          <el-col :span="5">
            <el-form-item prop="arrivalTermsValue">
              <el-input v-model="form.arrivalTermsValue" placeholder="天数" style="width: 60px" />（天）
            </el-form-item>
          </el-col>
          <!-- 结算方式 -->
          <el-col :span="8">
            <el-form-item label="结算方式" prop="settlementMethod">
              <el-select
                v-model="form.settlementMethod"
                placeholder="结算方式"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="dict in dict.type.purchasesale_settlement_method"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 是否开票 -->
          <el-col :span="24">
            <el-form-item label="是否开票" prop="isInvoicing">
              <el-switch
                :active-value="1"
                :inactive-value="0"
                v-model="form.isInvoicing"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 备注 -->
          <el-col :span="24">
            <el-form-item label="备注" prop="orderRemark">
              <el-input v-model="form.orderRemark" type="textarea" style="width: 90%" maxlength="128"
                show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 销售管理数据详细 -->
    <el-dialog title="销售管理数据详细" :visible.sync="openDetail" width="60%" append-to-body :close-on-click-modal="false">
      <el-form ref="formDetail" :model="formDetail" :rules="rules" label-width="100px">
        <el-row>
          <!-- 订单编号 -->
          <el-col :span="8">
            <el-form-item label="订单编号">
              <el-input v-model="formDetail.orderId" placeholder="请输入订单编号" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- 合同编号 -->
          <el-col :span="8">
            <el-form-item label="合同编号" prop="contractId">
              <el-input v-model="formDetail.contractId" placeholder="请输入合同编号" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- 经办人 -->
          <el-col :span="8">
            <el-form-item label="经办人" prop="handledBy">
              <el-input v-model="formDetail.handledBy" placeholder="请输入经办人" :disabled="true" style="width: 200px" maxlength="16"
                show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 所属部门 -->
          <el-col :span="8">
            <el-form-item label="所属部门" prop="belongDept">
              <treeselect v-model="formDetail.belongDept" 
                :options="deptOptions" 
                :show-count="true" 
                placeholder="请选择所属部门"
                :disabled="true"
                style="width: 200px;" />
            </el-form-item>
          </el-col>
          <!-- 业务日期 -->
          <el-col :span="8">
            <el-form-item label="业务日期" prop="businessDate">
              <el-date-picker clearable
                v-model="formDetail.businessDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择业务日期"
                :disabled="true"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <!-- 客户名称 -->
          <el-col :span="8">
            <el-form-item label="客户名称" prop="supplierRealName">
              <el-input v-model="formDetail.supplierRealName" placeholder="请输入供应商名称" :disabled="true" style="width: 200px" maxlength="128"
                show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 物料名称 -->
          <el-col :span="8">
            <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="formDetail.materialName" placeholder="请输入物料名称" :disabled="true" style="width: 200px" maxlength="64"
                show-word-limit />
            </el-form-item>
          </el-col>
          <!-- 销售数量 -->
          <el-col :span="8">
            <el-form-item label="销售数量" prop="purchaseQuantity">
              <el-input v-model="formDetail.purchaseQuantity" placeholder="请输入销售数量" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- 单价 -->
          <el-col :span="8">
            <el-form-item label="单价" prop="unitPrice">
              <el-input v-model="formDetail.unitPrice" placeholder="请输入单价" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 计量单位 -->
          <el-col :span="8">
            <el-form-item label="计量单位" prop="meteringUnit">
              <el-select
                v-model="formDetail.meteringUnit"
                placeholder="计量单位"
                clearable
                :disabled="true"
                style="width: 200px"
              >
                <el-option
                  v-for="dict in dict.type.masterdata_warehouse_measurement_unit"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- 预计到货期 -->
          <el-col :span="8">
            <el-form-item label="预计到货期" prop="arrivalDate">
              <el-date-picker clearable
                v-model="formDetail.arrivalDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择预计到货期"
                :disabled="true"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <!-- 要求到货期 -->
          <el-col :span="8">
            <el-form-item label="要求到货期" prop="requiredDeliveryDate">
              <el-date-picker clearable
                v-model="formDetail.requiredDeliveryDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择要求到货期"
                :disabled="true"
                style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 账期 -->
          <el-col :span="8">
            <el-form-item label="账期" prop="accountPeriod">
              <el-input v-model="formDetail.accountPeriod" placeholder="请输入账期" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
          <!-- 到账条件 -->
          <el-col :span="3">
            <el-form-item label="到账条件" prop="arrivalTerms">
              <el-select
                v-model="formDetail.arrivalTerms"
                placeholder="到账条件"
                clearable
                :disabled="true"
                style="width: 100px"
              >
                <el-option
                  v-for="dict in dict.type.purchasesale_arrival_terms"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- 到账条件值 -->
          <el-col :span="5">
            <el-form-item prop="arrivalTerms">
              <el-input v-model="formDetail.arrivalTermsValue" placeholder="天数" :disabled="true" style="width: 60px" />（天）
            </el-form-item>
          </el-col>
          <!-- 结算方式 -->
          <el-col :span="8">
            <el-form-item label="结算方式" prop="settlementMethod">
              <el-select
                v-model="formDetail.settlementMethod"
                placeholder="结算方式"
                clearable
                :disabled="true"
                style="width: 200px"
              >
                <el-option
                  v-for="dict in dict.type.purchasesale_settlement_method"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 是否开票 -->
          <el-col :span="24">
            <el-form-item label="是否开票" prop="isInvoicing">
              <el-switch
                :active-value="1"
                :inactive-value="0"
                v-model="formDetail.isInvoicing"
                :disabled="true"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 备注 -->
          <el-col :span="24">
            <el-form-item label="备注" prop="orderRemark">
              <el-input v-model="formDetail.orderRemark" type="textarea" :disabled="true" style="width: 90%" maxlength="128"
                show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelDetail">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { listPurchase, getPurchase, delPurchase, addPurchase, updatePurchase } from "@/api/purchasesale/purchasesale";
import { listClient } from "@/api/masterdata/client";
import { deptTreeSelect } from "@/api/system/user";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Purchase",
  dicts: ['purchasesale_purchase_type', 'purchasesale_belong_dept', 'masterdata_warehouse_measurement_unit', 
          'purchasesale_arrival_terms', 'purchasesale_settlement_method', 'purchase_mgr_order_status'],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      // loading: true,
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 1,
      // 销售收货销售发货管理表格数据
      purchaseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        belongDept: null,
        handledBy: null,
        materialName: null,
        startBusinessDate: null,
        endbusinessDate: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        handledBy: [
          { required: true, message: "经办人不能为空", trigger: "blur" }
        ],
        belongDept: [
          { required: true, message: "所属部门不能为空", trigger: "blur" }
        ],
        businessDate: [
          { required: true, message: "业务日期不能为空", trigger: "blur" }
        ],
        supplierName: [
          { required: true, message: "客户名称不能为空", trigger: "blur" }
        ],
        materialName: [
          { required: true, message: "物料名称不能为空", trigger: "blur" }
        ],
        purchaseQuantity: [
          { required: true, message: "销售数量不能为空", trigger: "blur" },
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的销售数量", trigger: "blur" }
        ],
        unitPrice: [
          { required: true, message: "单价不能为空", trigger: "blur" },
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的单价", trigger: "blur" }

        ],
        meteringUnit: [
          { required: true, message: "计量单位不能为空", trigger: "blur" }
        ],
        arrivalDate: [
          { required: true, message: "预计到货期不能为空", trigger: "blur" }
        ],
        requiredDeliveryDate: [
          { required: true, message: "要求交货期不能为空", trigger: "blur" }
        ],
        accountPeriod: [
          { required: true, message: "账期不能为空", trigger: "blur" },
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的账期", trigger: "blur" }
        ],
        arrivalTerms: [
          { required: true, message: "到账条件不能为空", trigger: "blur" }
        ],
        arrivalTermsValue: [
          { pattern: /^[0-9,.]*$/, message: "包括非数字，请输入正确的到账条件值", trigger: "blur" }
        ],
        settlementMethod: [
          { required: true, message: "结算方式不能为空", trigger: "blur" }
        ]
      },
      isUpdate: false,
      formDetail: {},
      openDetail: false,
      // 客户名称选择用
      optionsClientName: [],
      listClientName: [],
      remoteLoadingSClientName: false,
      // 部门树选项
      deptOptions: [],
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  created() {
    this.getList();
    this.getDeptTree();
  },
  methods: {
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
    /** 查询销售收货销售发货管理列表 */
    getList() {
      this.loading = true;
      this.queryParams.contractType = "S";
      listPurchase(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        
        this.purchaseList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
      // 取消按钮
    cancelDetail() {
      this.openDetail = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        orderId: null,
        purchaseType: null,
        contractId: null,
        handledBy: null,
        belongDept: null,
        businessDate: null,
        materialName: null,
        purchaseQuantity: null,
        supplierName: null,
        unitPrice: null,
        meteringUnit: null,
        arrivalDate: null,
        requiredDeliveryDate: null,
        accountPeriod: null,
        arrivalTerms: null,
        arrivalTermsValue: null,
        settlementMethod: null,
        isInvoicing: null,
        orderRemark: null
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
      // 重置查询条件
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        contractId: null,
        belongDept: null,
        handledBy: null,
        materialName: null,
        checkMoneyMin: null,
        checkMoneyMax: null
      };

      // // 重置业务日期范围查询条件
      this.dateRange = null;

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
      this.title = "添加销售订单数据";
      this.form.isInvoicing = "1";
      this.isUpdate = false;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const contractId = row.contractId || this.ids
      getPurchase(contractId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改销售订单数据";
        this.isUpdate = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.form.purchaseType = "S";
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.isUpdate) {
            updatePurchase(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPurchase(this.form).then(response => {
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
      const orderIds = row.orderId || this.ids;
      this.$modal.confirm('是否确认删除销售订单数据项？').then(function() {
        return delPurchase(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('purchase/mgr/export', {
        ...this.queryParams
      }, `销售管理_${new Date().getFullYear()}年${new Date().getMonth()+1}月${new Date().getDate()}日 ${new Date().getHours()}:${new Date().getMinutes()}:${new Date().getSeconds()}.xlsx`)
    },
     /** 查看合同数据 */ 
    handleView(row) {
      this.formDetail = row;
      this.openDetail = true;
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data;
      });
    }
  }
};
</script>
