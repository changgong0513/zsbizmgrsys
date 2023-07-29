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
          @click="handleExport"
          v-hasPermi="['transportdocuments:detail:export']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="detailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="运输单号" align="center" prop="transportdocumentsId" />
      <el-table-column label="经办人姓名" align="center" prop="handledByName" />
      <el-table-column label="物料名称" align="center" prop="materialName" />
      <el-table-column label="业务日期" align="center" prop="businessDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.businessDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单据类型" align="center" prop="documentsType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.transportdocuments_documents_type" :value="scope.row.documentsType"/>
        </template>
      </el-table-column>
      <el-table-column label="运输单状态" align="center" prop="transportdocumentsState">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.transportdocuments_state" :value="scope.row.transportdocumentsState"/>
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
              <el-input v-model="form.transportdocumentsId" placeholder="请输入运输单号" style="width: 200px;" />
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
            <el-form-item label="发货地名称" prop="sourcePlaceId">
              <el-cascader
                size="large"
                :options="regionOptions"
                v-model="form.sourcePlaceId"
                filterable
                @change="handleSourcePlaceRegionChange"
                style="width: 200px;">
              </el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="卸货地名称" prop="targetPlaceId">
              <el-cascader
                size="large"
                :options="regionOptions"
                v-model="form.targetPlaceId"
                filterable
                @change="handleTargetPlaceRegionChange"
                style="width: 200px;">
              </el-cascader>
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
              <el-input v-model="form.unitPrice" placeholder="请输入单价" style="width: 200px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="运输单状态" prop="transportdocumentsState">
              <el-select v-model="form.transportdocumentsState" placeholder="请选择运输单状态" style="width: 200px;" >
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
            <el-form-item label="关联合同名称" prop="relatedContractName">
              <el-input v-model="form.relatedContractName" placeholder="请输入关联合同名称" style="width: 200px;" />
            </el-form-item>
            <el-input v-model="form.relatedContractId" v-show="false" />
          </el-col>
          <el-col :span="8">
            <el-form-item label="卸货数量" prop="landedQuantity">
              <el-input v-model="form.landedQuantity" placeholder="请输入卸货数量" style="width: 200px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="核算数量" prop="accountingQuantity">
              <el-input v-model="form.accountingQuantity" placeholder="请输入核算数量" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结算单价" prop="settlementUnitPrice">
              <el-input v-model="form.settlementUnitPrice" placeholder="请输入结算单价" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="运费单价" prop="freightUnitPrice">
              <el-input v-model="form.freightUnitPrice" placeholder="请输入运费单价" style="width: 200px;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="扣款金额" prop="deductionAmount">
              <el-input v-model="form.deductionAmount" placeholder="请输入扣款金额" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="压车费" prop="followUpFare">
              <el-input v-model="form.followUpFare" placeholder="请输入压车费" style="width: 200px;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="卸货日期" prop="landedDate">
              <el-date-picker clearable
                v-model="form.landedDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择卸货日期"
                style="width: 200px;">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDetail, getDetail, delDetail, addDetail, updateDetail } from "@/api/transportdocuments/detail";
import { regionData, CodeToText, TextToCode } from "element-china-area-data"
import { listMaterialData } from "@/api/masterdata/material";

export default {
  name: "Detail",
  dicts: ['transportdocuments_state', 'transportdocuments_documents_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 运输单详细信息表格数据
      detailList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        transportdocumentsId: null,
        businessDate: null,
        documentsType: null,
        transportdocumentsState: null,
      },
      // 表单参数
      form: {},
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
        relatedOrderId: [
          { required: true, message: "关联订单编号不能为空", trigger: "blur" }
        ],
        transportdocumentsState: [
          { required: true, message: "运输单状态不能为空", trigger: "change" }
        ],
        createBy: [
          { required: true, message: "创建者不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateBy: [
          { required: true, message: "更新者不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        bizVersion: [
          { required: true, message: "版本号不能为空", trigger: "blur" }
        ]
      },
      // 省市区级联数据
      regionOptions: regionData,
      // 物料名称选择用
      optionsMaterialName: [],
      listMaterialName: [],
      remoteLoadingMaterialName: false,
    };
  },
  created() {
    this.getList();
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加运输单详细信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDetail(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改运输单详细信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      // 发货地省市区级联选择器数组转字符串
      let changgedSourcePlaceId = this.form.sourcePlaceId;
      if (changgedSourcePlaceId) {
        this.form.sourcePlaceId = changgedSourcePlaceId.join('-');
      }

       // 省市区级联选择器数组转字符串
      let changgedTargetPlaceId = this.form.targetPlaceId;
      if (changgedTargetPlaceId) {
        this.form.targetPlaceId = changgedTargetPlaceId.join('-');
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
    /** 卸货地省市区级联选择器变更后 */
    handleTargetPlaceRegionChange (value) {
      this.form.targetPlaceName = this.getCodeToText(null, value)
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
  }
};
</script>
