<!-- 利润情况表 -->
<template>
  <div class="app-container" style="padding: auto;">
    <el-row :gutter="10" class="mb8" style="margin-bottom: 15px;">
        <el-col :span="1.5">
            <el-button
            type="warning"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            >预览</el-button>
        </el-col>
        <el-col :span="1.5">
            <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            >打印</el-button>
        </el-col>
    </el-row>
    <el-row :gutter="10" class="mb8" 
      style="margin-bottom: 15px; border-bottom: 1px solid #409EFF; width: 365px; padding: 5px;"><b>采购品种</b></el-row>
      <el-row :gutter="10" class="mb8" style="margin-bottom: 25px;">
          <el-col :span="1.5">
              <el-button
              type="primary"
              plain
              size="mini"
              @click="handleAdd"
              >标粉</el-button>
          </el-col>
          <el-col :span="1.5">
              <el-button
              type="primary"
              plain
              size="mini"
              @click="handleAdd"
              >次粉</el-button>
          </el-col>
          <el-col :span="1.5">
              <el-button
              type="primary"
              plain
              size="mini"
              @click="handleAdd"
              >麸皮</el-button>
          </el-col>
          <el-col :span="1.5">
              <el-button
              type="primary"
              plain
              size="mini"
              @click="handleAdd"
              >卸车费</el-button>
          </el-col>
          <el-col :span="1.5">
              <el-button
              type="primary"
              plain
              size="mini"
              @click="handleAdd"
              >（空白）</el-button>
          </el-col>
      </el-row>
      <table border="1px" style="border-collapse: collapse;">
          <thead>
              <tr style="background-color: #409EFF;">  
                  <td align="center">采购品种</td>  
                  <td align="center">供货方</td>
                  <td align="center">结算数量</td>  
                  <td align="center">结算金额</td> 
                  <td align="center">已付款</td>  
                  <td align="center">未付款</td>   
              </tr>
          </thead>
          <tbody>
              <tr style="height: 25px;">
                  <!-- 采购品种 -->
                  <td style="padding: 0px; margin: 0px; width: 300px;" align="left" colspan="2">标粉</td>
                  <!-- 结算数量 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>1180.08</b></td>
                  <!-- 结算金额 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>3998187.6</b></td>
                  <!-- 已付款 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>3048368</b></td>
                  <!-- 未付款 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>949819.6</b></td>
              </tr>
              <tr style="height: 25px;">
                  <!-- 采购品种 -->
                  <td></td>
                  <!-- 供货方 -->
                  <td>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">柏乡县东方面业</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">柏乡县九谷面业</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">柏乡县沙龙面业</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">柏乡县沙龙面业退款</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">柏乡县天甲面业</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">山东永明粮油食品</td></tr>
                  </td>
                  <!-- 结算数量 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">70</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">35</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">140</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">-1</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">35</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">175</td></tr>
                  </td>
                  <!-- 结算金额 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">238000</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">119700</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">468230</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">-3420</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">117600</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">607600</td></tr>
                  </td>
                  <!-- 已付款 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">671416</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;"></td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;"></td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">-3420</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;"></td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">591850</td></tr>
                  </td>
                  <!-- 未付款 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">-433416</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">119700</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">468230</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">0</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">117600</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">15750</td></tr>
                  </td>
              </tr>
              <tr style="height: 25px;">
                  <!-- 采购品种 -->
                  <td style="padding: 0px; margin: 0px; width: 300px;" align="left" colspan="2">次粉</td>
                  <!-- 结算数量 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>4478.61</b></td>
                  <!-- 结算金额 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>12783220.9</b></td>
                  <!-- 已付款 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>14827026.2</b></td>
                  <!-- 未付款 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>-2043805.3</b></td>
              </tr>
              <tr style="height: 25px;">
                  <!-- 采购品种 -->
                  <td></td>
                  <!-- 供货方 -->
                  <td>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">河北创远面业</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">河北华龙农庄面粉</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">河北金硕松面业</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">河北未兆贸易</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">焦作巨人商贸</td></tr>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">平顶山市双伟饲料</td></tr>
                  </td>
                  <!-- 结算数量 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">115.86</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">170</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">999.68</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">101.97</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">44.4</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">160</td></tr>
                  </td>
                  <!-- 结算金额 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">338311.2</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">479400</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">2882208.6</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">287215.1</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">135864</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">489600</td></tr>
                  </td>
                  <!-- 已付款 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">38311.2</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;"></td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">1750972.6</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;"></td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">135864</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">489600</td></tr>
                  </td>
                  <!-- 未付款 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">0</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">479400</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">1131236</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">287215.1</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 1px solid; border-left: 0px;">0</td></tr>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">0</td></tr>
                  </td>
              </tr>
              <tr style="height: 25px;">
                  <!-- 采购品种 -->
                  <td style="padding: 0px; margin: 0px; width: 300px;" align="left" colspan="2">麸皮</td>
                  <!-- 结算数量 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>1774.54</b></td>
                  <!-- 结算金额 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>4417283.2</b></td>
                  <!-- 已付款 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>3332611.5</b></td>
                  <!-- 未付款 -->
                  <td style="padding: 0px; margin: 0px; width: 200px;" align="right"><b>1084671.7</b></td>
              </tr>
              <tr style="height: 25px;">
                  <!-- 采购品种 -->
                  <td></td>
                  <!-- 供货方 -->
                  <td>
                    <tr><td style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">河北未兆贸易</td></tr>
                  </td>
                  <!-- 结算数量 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">680.81</td></tr>
                  </td>
                  <!-- 结算金额 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">1724468.8</td></tr>
                  </td>
                  <!-- 已付款 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">1191265.1</td></tr>
                  </td>
                  <!-- 未付款 -->
                  <td>
                    <tr><td align="right" style="width: 300px; height: 25px; border-top: 0px; border-right: 0px; border-bottom: 0px solid; border-left: 0px;">533203.7</td></tr>
                  </td>
              </tr>
          </tbody>
      </table>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        tableData: [{
          date: '2016-05-03',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-02',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-04',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-01',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-08',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-06',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-07',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }]
      }
    },
    methods: {
      objectSpanMethod({ row, column, rowIndex, columnIndex }) {
          if (columnIndex === 0) {
              if (rowIndex % 2 === 0) {
                  return {
                      rowspan: 2,
                      colspan: 1
                  };
              } else {
                  return {
                      rowspan: 0,
                      colspan: 0
                  };
              }
          }
      }
    } 
  }
</script>