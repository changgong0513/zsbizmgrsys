<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="运输单号" prop="transportdocumentsId">
        <el-input
          v-model="queryParams.transportdocumentsId"
          placeholder="请输入运输单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="批次号" prop="pch">
        <el-input
          v-model="queryParams.pch"
          placeholder="请输入批次号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车号" prop="wagonNumber">
        <el-input
          v-model="queryParams.wagonNumber"
          placeholder="请输入车号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="发货地名称" prop="sourcePlaceName">
        <el-input
          v-model="queryParams.sourcePlaceName"
          placeholder="请输入发货地名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="卸货地名称" prop="targetPlaceName">
        <el-input
          v-model="queryParams.targetPlaceName"
          placeholder="请输入卸货地名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经办人姓名" prop="handledByName">
        <el-input
          v-model="queryParams.handledByName"
          placeholder="请输入经办人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="物料名称" prop="materialName">
        <el-input
          v-model="queryParams.materialName"
          placeholder="请输入物料名称"
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
      <el-form-item label="关联订单编号" prop="relatedOrderId">
        <el-input
          v-model="queryParams.relatedOrderId"
          placeholder="请输入关联订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['transportdocuments:base:add']"
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
          v-hasPermi="['transportdocuments:base:edit']"
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
          v-hasPermi="['transportdocuments:base:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['transportdocuments:base:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="baseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="运输单号" align="center" prop="transportdocumentsId" />
      <el-table-column label="运输单类型" align="center" prop="transportdocumentsType" />
      <el-table-column label="批次号" align="center" prop="pch" />
      <el-table-column label="车号" align="center" prop="wagonNumber" />
      <el-table-column label="发货地名称" align="center" prop="sourcePlaceName" />
      <el-table-column label="卸货地名称" align="center" prop="targetPlaceName" />
      <el-table-column label="装车数量" align="center" prop="loadingQuantity" />
      <el-table-column label="经办人姓名" align="center" prop="handledByName" />
      <el-table-column label="物料名称" align="center" prop="materialName" />
      <el-table-column label="业务日期" align="center" prop="businessDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.businessDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单据类型" align="center" prop="documentsType" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['transportdocuments:base:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['transportdocuments:base:remove']"
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

    <!-- 添加或修改运输单基本信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="运输单号" prop="transportdocumentsId">
          <el-input v-model="form.transportdocumentsId" placeholder="请输入运输单号" />
        </el-form-item>
        <el-form-item label="批次号" prop="pch">
          <el-input v-model="form.pch" placeholder="请输入批次号" />
        </el-form-item>
        <el-form-item label="车号" prop="wagonNumber">
          <el-input v-model="form.wagonNumber" placeholder="请输入车号" />
        </el-form-item>
        <el-form-item label="发货地编号" prop="sourcePlaceId">
          <el-input v-model="form.sourcePlaceId" placeholder="请输入发货地编号" />
        </el-form-item>
        <el-form-item label="发货地名称" prop="sourcePlaceName">
          <el-input v-model="form.sourcePlaceName" placeholder="请输入发货地名称" />
        </el-form-item>
        <el-form-item label="卸货地编号" prop="targetPlaceId">
          <el-input v-model="form.targetPlaceId" placeholder="请输入卸货地编号" />
        </el-form-item>
        <el-form-item label="卸货地名称" prop="targetPlaceName">
          <el-input v-model="form.targetPlaceName" placeholder="请输入卸货地名称" />
        </el-form-item>
        <el-form-item label="装车数量" prop="loadingQuantity">
          <el-input v-model="form.loadingQuantity" placeholder="请输入装车数量" />
        </el-form-item>
        <el-form-item label="经办人编号" prop="handledById">
          <el-input v-model="form.handledById" placeholder="请输入经办人编号" />
        </el-form-item>
        <el-form-item label="经办人姓名" prop="handledByName">
          <el-input v-model="form.handledByName" placeholder="请输入经办人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="telephone">
          <el-input v-model="form.telephone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="物料名称" prop="materialName">
          <el-input v-model="form.materialName" placeholder="请输入物料名称" />
        </el-form-item>
        <el-form-item label="业务日期" prop="businessDate">
          <el-date-picker clearable
            v-model="form.businessDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择业务日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input v-model="form.unitPrice" placeholder="请输入单价" />
        </el-form-item>
        <el-form-item label="关联订单编号" prop="relatedOrderId">
          <el-input v-model="form.relatedOrderId" placeholder="请输入关联订单编号" />
        </el-form-item>
        <el-form-item label="关联合同编号" prop="relatedContractId">
          <el-input v-model="form.relatedContractId" placeholder="请输入关联合同编号" />
        </el-form-item>
        <el-form-item label="关联合同名称" prop="relatedContractName">
          <el-input v-model="form.relatedContractName" placeholder="请输入关联合同名称" />
        </el-form-item>
        <el-form-item label="版本号" prop="bizVersion">
          <el-input v-model="form.bizVersion" placeholder="请输入版本号" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBase, getBase, delBase, addBase, updateBase } from "@/api/transportdocuments/base";

export default {
  name: "Base",
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
      // 运输单基本信息表格数据
      baseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        transportdocumentsId: null,
        transportdocumentsType: null,
        pch: null,
        wagonNumber: null,
        sourcePlaceName: null,
        targetPlaceName: null,
        handledByName: null,
        materialName: null,
        businessDate: null,
        documentsType: null,
        relatedOrderId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        transportdocumentsId: [
          { required: true, message: "运输单号不能为空", trigger: "blur" }
        ],
        transportdocumentsType: [
          { required: true, message: "运输单类型不能为空", trigger: "change" }
        ],
        pch: [
          { required: true, message: "批次号不能为空", trigger: "blur" }
        ],
        wagonNumber: [
          { required: true, message: "车号不能为空", trigger: "blur" }
        ],
        sourcePlaceName: [
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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询运输单基本信息列表 */
    getList() {
      this.loading = true;
      listBase(this.queryParams).then(response => {
        this.baseList = response.rows;
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
      this.title = "添加运输单基本信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getBase(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改运输单基本信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBase(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBase(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除运输单基本信息编号为"' + ids + '"的数据项？').then(function() {
        return delBase(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('transportdocuments/base/export', {
        ...this.queryParams
      }, `base_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
