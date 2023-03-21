<template>
    <div class="app-container">
      <!-- 查询表单 -->
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
        <!-- 仓库编码 -->
        <el-form-item label="仓库编码" prop="warehouseCode">
            <el-input v-model="queryParams.warehouseCode" placeholder="请输入仓库编码" clearable style="width: 240px;" />
        </el-form-item>
        <!-- 仓库名称 -->
        <el-form-item label="仓库名称" prop="warehouseName">
            <el-input v-model="queryParams.warehouseName" placeholder="请输入仓库名称" clearable style="width: 240px;" />
        </el-form-item>
        <!-- 区划 -->
        <el-form-item label="区划" prop="warehouseRegion">
            <el-select v-model="queryParams.warehouseRegion" placeholder="请输入区划" style="width: 240px;">
                <el-option
                  v-for="dict in dict.type.masterdata_warehouse_region"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                />
            </el-select>
        </el-form-item>
        <!-- 仓库地址 -->
        <el-form-item label="仓库地址" prop="warehouseAddress">
            <el-input v-model="queryParams.warehouseName" placeholder="请输入仓库地址" clearable style="width: 240px;" />
        </el-form-item>
        <!-- 仓库类别 -->
        <el-form-item label="仓库类别" prop="warehouseCategory">
            <el-select v-model="queryParams.warehouseCategory" placeholder="请输入仓库类别" style="width: 240px;">
            <el-option
                v-for="dict in dict.type.masterdata_warehouse_category"
                :key="dict.value"
                :label="dict.label"
                :value="parseInt(dict.value)"
            />
            </el-select>
        </el-form-item>
        <el-form-item style="margin-left: 30px;">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
  
      <el-row :gutter="10" class="mb8">
        <!-- <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['purchasesale:purchasesale:export']"
          >导出</el-button>
        </el-col> -->
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <!-- 列表 -->
      <el-table v-loading="loading" :data="kcList">
        <el-table-column label="仓库编码" align="center" prop="warehouseCode" width="225" />
        <el-table-column label="仓库名称" align="center" prop="warehouseName" width="150" :show-overflow-tooltip="true" />
        <el-table-column label="区划" align="center" prop="warehouseRegion" width="150">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.masterdata_warehouse_region" :value="scope.row.warehouseRegion"/>
          </template>
        </el-table-column>
        <el-table-column label="仓库地址" align="center" prop="warehouseAddress" width="300" :show-overflow-tooltip="true" />
        <el-table-column label="仓库类别" align="center" prop="warehouseCategory" width="150">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.masterdata_warehouse_category" :value="scope.row.warehouseCategory"/>
          </template>
        </el-table-column>
        <el-table-column label="最大容量" align="center" prop="maximumCapacity" width="150" />
        <el-table-column label="可用容量" align="center" prop="availableCapacity" width="150" />
        <el-table-column label="总收货量" align="center" prop="sumReceiptQuantity" width="150" />
        <el-table-column label="总发货量" align="center" prop="sumSaleQuantity" class-name="small-padding fixed-width" />
      </el-table>
      
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </div>
  </template>
  
  <script>
  
  import { listKc } from "@/api/purchasesale/receipt";

  export default {
    name: "kcck",
    dicts: ['masterdata_warehouse_region', 'masterdata_warehouse_category'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 1,
        // 库存表格数据
        kcList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          warehouseCode: null,
          warehouseName: null,
          warehouseRegion: null,
          warehouseCategory: null
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询采购收货销售发货管理列表 */
      getList() {
        listKc(this.queryParams).then(response => {
          this.kcList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
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
          warehouseCode: null,
          warehouseName: null,
          warehouseRegion: null,
          warehouseCategory: null
        };

        this.handleQuery();
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('purchase/mgr/kc/export', {
          ...this.queryParams
        }, `库存_${new Date().getFullYear()}年${new Date().getMonth()+1}月${new Date().getDate()}日 ${new Date().getHours()}:${new Date().getMinutes()}:${new Date().getSeconds()}.xlsx`)
      }
    }
  };
  </script>
  