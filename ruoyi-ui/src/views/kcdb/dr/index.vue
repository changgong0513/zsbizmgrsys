<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!-- 收货部门（库存调入） -->
      <el-form-item label="收货部门" prop="shbm">
        <el-select v-model="queryParams.shbm" placeholder="请选择收货部门" style="width: 240px;">
          <el-option
            v-for="item in deptOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!-- 收货仓库名称（库存调入） -->
      <el-form-item label="仓库名称" prop="shck">
        <el-input
          v-model="queryParams.shck"
          placeholder="请输入仓库名称"
          clearable
          style="width: 240px"
        />
      </el-form-item>
      <!-- 卸货数量（库存调入） -->
      <el-form-item label="数量" prop="xhsl">
        <el-input
          v-model="queryParams.xhsl"
          placeholder="请输入数量"
          clearable
          style="width: 240px"
        />
      </el-form-item>
      <!-- 结算单价（库存调入） -->
      <el-form-item label="单价" prop="jsdj">
        <el-input
          v-model="queryParams.jsdj"
          placeholder="请输入单价"
          clearable
          style="width: 240px"
        />
      </el-form-item>
      <!-- 调拨金额（库存调入） -->
      <el-form-item label="金额" prop="dbje">
        <el-input
          v-model="queryParams.dbje"
          placeholder="请输入金额"
          clearable
          style="width: 240px"
        />
      </el-form-item>
      <el-form-item label="调拨日期" prop="dbrq">
        <el-date-picker clearable
          v-model="queryParams.dbrq"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择调拨日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="调拨类型" prop="lx">
        <el-select
          v-model="queryParams.lx"
          placeholder="请输入调拨类型"
          clearable
        >
          <el-option
            v-for="dict in dict.type.kcdb_db_type"
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
        >修改1</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="kcdbList" @selection-change="handleSelectionChange"
      @row-dblclick="handleView">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="调拨单号" align="center" prop="dh" />
      <el-table-column label="调拨类型" align="center" prop="lx">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.kcdb_db_type" :value="scope.row.lx"/>
        </template>
      </el-table-column>
      <el-table-column label="所属部门" align="center" prop="deptName" />
      <el-table-column label="仓库名称" align="center" prop="fhckmc" />
      <el-table-column label="数量" align="center" prop="dbsl" />
      <el-table-column label="单价" align="center" prop="jsdj" />
      <el-table-column label="金额" align="center" prop="dbje" />
      <el-table-column label="调拨日期" align="center" prop="dbrq" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dbrq, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="内勤人员" align="center" prop="nqry" />
      <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope" v-if="scope.row.userId !== 1">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >确认</el-button>
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

    <!-- 添加或修改存库调拨对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1150px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <!-- 收货部门 -->
          <el-col :span="8">
            <el-form-item label="收货部门" prop="shbm">
              <el-select v-model="queryParams.shbm" placeholder="请选择收货部门" style="width: 240px;">
                <el-option
                  v-for="item in deptOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- 调拨类型 -->
          <el-col :span="8">
            <el-form-item label="调拨类型" prop="lx">
              <el-select
                v-model="form.lx"
                placeholder="请输入调拨类型"
                clearable
                style="width: 240px"
              >
                <el-option
                  v-for="dict in dict.type.kcdb_db_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- 仓库名称 -->
          <el-col :span="8">
            <el-form-item label="仓库名称" prop="shck">
              <el-select
                v-model="form.fhck"
                filterable
                remote
                clearable
                reserve-keyword
                placeholder="请输入仓库名称关键字"
                style="width: 240px"
                :remote-method="remoteWarehouseName"
                :loading="remoteLoadingWarehouseName">
                <el-option
                  v-for="item in optionsWarehouseName"
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
            <el-form-item label="物料名称" prop="wlmc">
              <el-select
                v-model="form.wlmc"
                filterable
                remote
                clearable
                reserve-keyword
                style="width: 240px"
                placeholder="请输入物料名称关键字"
                :remote-method="remoteMethodMaterialName"
                :loading="remoteLoadingMaterialName">
                <el-option
                  v-for="item in optionsMaterialName"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- 运输方式 -->
          <el-col :span="8">
            <el-form-item label="运输方式" prop="ysfs">
              <el-select
                v-model="form.ysfs"
                placeholder="请输入运输方式"
                clearable
                style="width: 240px"
              >
                <el-option
                  v-for="dict in dict.type.purchasesale_transport_mode"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- 结算方式 -->
          <el-col :span="8">
            <el-form-item label="结算方式" prop="jsfs">
              <el-select
                v-model="form.jsfs"
                placeholder="结算方式"
                clearable
                style="width: 240px"
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
          <!-- 调拨数量 -->
          <el-col :span="8">
            <el-form-item label="调拨数量" prop="dbsl">
              <el-input v-model="form.dbsl" placeholder="请输入调拨数量" style="width: 240px" />
            </el-form-item>
          </el-col>
          <!-- 结算单价 -->
          <el-col :span="8">
            <el-form-item label="结算单价" prop="jsdj">
              <el-input v-model="form.jsdj" placeholder="请输入结算单价" style="width: 240px" />
            </el-form-item>
          </el-col>
          <!-- 金额 -->
          <el-col :span="8">
            <el-form-item label="金额" prop="dbje" style="width: 240px">{{calDrje}}</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 卸货数量 -->
          <el-col :span="8">
            <el-form-item label="卸货数量" prop="xhsl">
              <el-input v-model="form.xhsl" placeholder="请输入卸货数量" style="width: 240px" />
            </el-form-item>
          </el-col>
          <!-- 调拨日期 -->
          <el-col :span="8">
            <el-form-item label="调拨日期" prop="dbrq">
              <el-date-picker clearable
                v-model="form.dbrq"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择调拨日期" style="width: 240px">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <!-- 内勤人员 -->
          <el-col :span="8">
            <el-form-item label="内勤人员" prop="nqry">
              <el-input v-model="form.nqry" placeholder="请输入内勤人员" style="width: 240px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <!-- 备注 -->
          <el-col :span="24">
            <el-form-item label="备注" prop="bz">
              <el-input v-model="form.bz" placeholder="请输入备注" style="width: 95%" 
                maxlength="256" show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看存库调入详细对话框 -->
    <el-dialog :title="title" :visible.sync="openDetail" width="1150px" append-to-body :close-on-click-modal="false">
      <el-form ref="formDetail" :model="formDetail" label-width="80px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="调拨单号" prop="dh">
              <el-input v-model="formDetail.dh" placeholder="请输入调拨单号" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="调拨类型" prop="lx">
              <el-select
                v-model="formDetail.lx"
                placeholder="请输入调拨类型"
                clearable
                style="width: 200px"
                :disabled="true"
              >
                <el-option
                  v-for="dict in dict.type.kcdb_db_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="物料名称" prop="materialName">
              <el-input v-model="formDetail.materialName" placeholder="请输入物料名称" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="调拨数量" prop="dbsl">
              <el-input v-model="formDetail.dbsl" placeholder="请输入调拨数量" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结算单价" prop="jsdj">
              <el-input v-model="formDetail.jsdj" placeholder="请输入结算单价" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="金额" prop="dbje" style="width: 200px">{{formDetail.dbje}}</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="结算方式" prop="jsfs">
              <el-select
                v-model="formDetail.jsfs"
                placeholder="结算方式"
                clearable
                style="width: 200px"
                :disabled="true"
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
          <el-col :span="8">
            <el-form-item label="运输方式" prop="ysfs">
              <el-select
                v-model="formDetail.ysfs"
                placeholder="请输入运输方式"
                clearable
                style="width: 200px"
                :disabled="true"
              >
                <el-option
                  v-for="dict in dict.type.purchasesale_transport_mode"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="调拨日期" prop="dbrq">
              <el-date-picker clearable
                v-model="formDetail.dbrq"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择调拨日期" :disabled="true" style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="收货部门" prop="shbm">
              <el-select v-model="queryParams.shbm" placeholder="请选择收货部门" style="width: 200px;">
                <el-option
                  v-for="item in deptOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="收货仓库" prop="shck">
              <el-select
                v-model="formDetail.shck"
                filterable
                remote
                clearable
                reserve-keyword
                placeholder="请输入仓库名称关键字"
                :disabled="true"
                style="width: 200px"
                :remote-method="remoteWarehouseName"
                :loading="remoteLoadingWarehouseName">
                <el-option
                  v-for="item in optionsWarehouseName"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="卸货数量" prop="xhsl">
              <el-input v-model="formDetail.xhsl" placeholder="请输入卸货数量" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="内勤人员" prop="nqry">
              <el-input v-model="formDetail.nqry" placeholder="请输入内勤人员" :disabled="true" style="width: 200px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="bz">
              <el-input v-model="formDetail.bz" placeholder="请输入备注" :disabled="true" style="width: 760px" 
                maxlength="256" show-word-limit />
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
import { listKcdb, getKcdb, delKcdb, addKcdb, updateKcdb } from "@/api/kcdb/kcdb";
import { listWarehouse } from "@/api/masterdata/warehouse";
import { listMaterialData } from "@/api/masterdata/material";
import { deptSelect } from "@/api/system/user";

export default {
  name: "Kcdb",
  dicts: ['kcdb_db_type', 'purchasesale_belong_dept', 'purchasesale_transport_mode' ,'purchasesale_settlement_method'],
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
      // 存库调拨表格数据
      kcdbList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        dh: null,
        lx: null,
        fhbm: null,
        shbm: null,
        dbrq: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        shbm: [
          { required: true, message: "收货部门不能为空", trigger: "change" }
        ],
        shck: [
          { required: true, message: "仓库名称不能为空", trigger: "change" }
        ],
        wlmc: [
          { required: true, message: "物料名称不能为空", trigger: "change" }
        ],
        ysfs: [
          { required: true, message: "运输方式不能为空", trigger: "change" }
        ],
        dbsl: [
          { required: true, message: "调拨数据不能为空", trigger: "blur" }
        ],
        jsdj: [
          { required: true, message: "结算单价不能为空", trigger: "blur" }
        ],
        nqry: [
          { required: true, message: "内勤人员不能为空", trigger: "blur" }
        ]
      },
      isUpdate: false,
      formDetail: {},
      openDetail: false,
      // 仓库名称选择用
      optionsWarehouseName: [],
      ListWarehouseName: [],
      remoteLoadingWarehouseName: false,
      // 物料名称选择用
      optionsMaterialName: [],
      listMaterialName: [],
      remoteLoadingMaterialName: false,
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
  computed: {
    /** 计算调拨金额（调入） */
    calDrje: function () {
      if (this.form.dbsl && this.form.jsdj) {
        this.form.dbje = Number(this.form.dbsl) * Number(this.form.jsdj);
        return Number(this.form.dbsl) * Number(this.form.jsdj);
      }
      
      return 0;
    }
  },
  methods: {
    /** 根据输入仓库名称关键字，取得仓库名称列表 */
    remoteWarehouseName(query) {
      if (query !== '') {
        this.remoteLoadingWarehouseName = true;
        this.queryParams.warehouseName = query;
        listWarehouse(this.queryParams).then(response => {
          this.remoteLoadingWarehouseName = false;
          console.log(JSON.stringify(response.rows));
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
    /** 根据输入物料名称关键字，取得物料名称列表 */
    remoteMethodMaterialName(query) {
      if (query !== '') {
        this.remoteLoadingMaterialName = true;
        this.queryParams.materialName = query;
        console.log("取得物料名称远程方法调用查询参数：" + JSON.stringify(this.queryParams));
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
    /** 查询存库调拨列表 */
    getList() {
      this.loading = true;
      this.queryParams.recordFlag = "dr";
      listKcdb(this.queryParams).then(response => {
        this.kcdbList = response.rows;
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
        dh: null,
        lx: null,
        fhck: null,
        shck: null,
        fhbm: null,
        shbm: null,
        wlmc: null,
        dbsl: null,
        ysfs: null,
        jsfs: null,
        jsdj: null,
        xhsl: null,
        dbrq: null,
        bz: null,
        dbje: null,
        recordFlag: null,
        nqry: null,
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
      this.queryParams = {
        shbm: null,
        shck: null,
        xhsl: null,
        jsdj: null,
        dbje: null,
        dbrq: null,
        lx: null,
      };
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.dbId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加存库调入";
      this.isUpdate = false;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const dbId = row.dbId || this.ids
      getKcdb(dbId).then(response => {
        this.form = response.data;
        this.remoteWarehouseName(this.form.fhckmc);
        this.open = true;
        this.title = "修改库存调入";
        this.isUpdate = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.isUpdate) {
            this.form.recordFlag = 'dr';
            updateKcdb(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.recordFlag = 'dr';
            addKcdb(this.form).then(response => {
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
      const dhs = row.dh || this.ids;
      this.$modal.confirm('是否确认删除存库调拨编号为"' + dhs + '"的数据项？').then(function() {
        return delKcdb(dhs);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('kcdb/mgr/export', {
        ...this.queryParams
      }, `库存调入_${new Date().getFullYear()}年${new Date().getMonth()+1}月${new Date().getDate()}日 ${new Date().getHours()}:${new Date().getMinutes()}:${new Date().getSeconds()}.xlsx`)
    },
    /** 查看存库调出详细数据 */ 
    handleView(row) {
      this.title = "查看存库调入详情"
      this.formDetail = row;
      this.openDetail = true;
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptSelect().then(response => {
        this.deptOptions = response.data.map(item => {
            return { value: `${item.deptId}`, label: `${item.deptName}` };
          }).filter(item => {
            return item.value != 100 && item.value != 103;
          });
      });
    }
  }
};
</script>
