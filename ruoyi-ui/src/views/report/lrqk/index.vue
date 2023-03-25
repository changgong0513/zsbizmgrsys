<!-- 利润情况表 -->
<template>
    <div class="app-container" style="padding: auto;">
        <el-row :gutter="10" class="mb8" style="margin-bottom: 25px;">
            <el-col :span="1.5">
                <el-button
                type="primary"
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
        <table border="1px" style="border-collapse: collapse;">
            <thead>
                <tr>
                    <td colspan="6" align="center">利润情况表-（湖南版块（22年第一批）（玉米））   2022年01-08月</td>
                </tr>
                <tr><td colspan="6" align="center">单位：吨、元</td></tr>
                <tr>  
                    <td align="center">核算项目</td>  
                    <td align="center">数量</td>
                    <td align="center">单价</td>  
                    <td align="center">金额</td> 
                    <td align="center">总金额</td>  
                    <td align="center">备注</td>   
                </tr>
            </thead>
            <tbody>
                <!-- 采购成本 start -->
                <tr>
                    <!-- 核算项目 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td rowspan="5" style="border-collapse: collapse; border-right: 1px solid; text-align: center; width: 100px;">采购成本</td>
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: left; width: 600px;">中粟自营终端拍卖牡丹江直属库粮款</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">深圳盛泰源龙溪自采粮款</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">大连象屿农产龙溪自采粮款</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">徐彩芸龙溪自采粮款</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: left; border-bottom: 0px; width: 600px;"><b>小计</b></td></tr>       
                       
                    </td>
                    <!-- 数量 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;">996.42</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">34.74</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">2245.54</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">73.48</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"><b>3350.18</b></td></tr>
                    </td>
                    <!-- 单价 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;">2554.00</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">2854.00</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">2903.43</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">2924.33</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"></td></tr>
                    </td> 
                    <!-- 金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;">2535888.90</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">99147.96</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">6519761.23</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">214880.00</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"><b>9369678.09</b></td></tr>
                    </td> 
                    <!-- 总金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td rowspan="5" style="border-top:0px; border-bottom: 0px; text-align: center; width: 200px;"><b>9369678.09</b></td>
                        </tr>
                    </td>
                    <!-- 备注 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="text-align: center; border-bottom: 0px; width: 200px;"></td>
                        </tr>
                    </td>
                </tr>
                <!-- 采购成本 end -->
                <!-- 费用 start -->
                <tr>
                    <!-- 核算项目 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td rowspan="9" 
                                style="border-collapse: collapse; border-right: 1px solid; text-align: center; width: 100px; 
                                border-top: 0px; border-bottom: 0px;">费用</td>
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: left; width: 600px;">铁路费用</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">采购运费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">中介费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">酒水费用</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">差旅费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">工资和保险（1-7月）</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">开票费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">开票费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: left; border-bottom: 0px; width: 600px;"><b>小计</b></td></tr>       
                       
                    </td>
                    <!-- 数量 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">3348.05</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">0.45</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"><b>3348.50</b></td></tr>
                    </td>
                    <!-- 单价 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">6.00</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">1.00</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"><b></b></td></tr>
                    </td>
                    <!-- 金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;">356183.00</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">193034.57</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">0.00</td></tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">4600.00</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">0.00</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">50133.75</td></tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">20088.30</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">0.45</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="text-align: center; border-bottom: 0px; width: 200px;"><b>624040.07</b></td>
                        </tr>
                    </td>
                    <!-- 总金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td rowspan="5" style="border-top:0px; border-bottom: 0px; text-align: center; width: 200px;"><b>624040.07</b></td>
                        </tr>
                    </td>
                    <!-- 备注 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="text-align: center; border-bottom: 0px; width: 200px;"></td>
                        </tr>
                    </td>
                </tr>
                <!-- 费用 end -->
                <!-- 财务费用 start -->
                <tr>
                    <!-- 核算项目 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td 
                                style="border-collapse: collapse; border-right: 1px solid; text-align: center; width: 100px; 
                                border-top: 0px; border-bottom: 0px;">财务费用</td>
                            <td style="border-top:0px; text-align: left; width: 600px;">资金占用利息（2022年01月-08月）</td>
                        </tr>           
                    </td>
                    <!-- 数量 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                    <!-- 单价 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                    <!-- 金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;">40510.92</td>
                        </tr>
                    </td>
                    <!-- 总金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; border-bottom: 0px; text-align: center; width: 200px;"><b>40510.92</b></td>
                        </tr>
                    </td>
                    <!-- 备注 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                </tr>
                <!-- 财务费用 end -->
                <!-- 总成本 start -->
                <tr>
                    <!-- 核算项目 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td 
                                style="border-collapse: collapse; border-right: 1px solid; text-align: center; width: 100px; 
                                border-top: 0px; border-bottom: 0px;">总成本</td>
                            <td style="border-top:0px; text-align: left; width: 600px;"></td>
                        </tr>           
                    </td>
                    <!-- 数量 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                    <!-- 单价 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                    <!-- 金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;">10034229.08</td>
                        </tr>
                    </td>
                    <!-- 总金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; border-bottom: 0px; text-align: center; width: 200px;"><b>10034229.08</b></td>
                        </tr>
                    </td>
                    <!-- 备注 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                </tr>
                <!-- 总成本 end -->
                <!-- 销售收入 start -->
                <tr>
                    <!-- 核算项目 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td rowspan="9" 
                                style="border-collapse: collapse; border-right: 1px solid; text-align: center; width: 100px; 
                                border-top: 0px; border-bottom: 0px;">销售收入</td>
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: left; width: 600px;">铁路费用</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">采购运费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">中介费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">酒水费用</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">差旅费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">工资和保险（1-7月）</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">开票费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: left; width: 600px;">开票费</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: left; border-bottom: 0px; width: 600px;"><b>小计</b></td></tr>       
                       
                    </td>
                    <!-- 数量 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">3348.05</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">0.45</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"><b>3348.50</b></td></tr>
                    </td>
                    <!-- 单价 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">6.00</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; text-align: center; width: 200px;">1.00</td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"><b></b></td></tr>
                    </td>
                    <!-- 金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;">356183.00</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">193034.57</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">0.00</td></tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">4600.00</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">0.00</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">50133.75</td></tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">20088.30</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;">0.45</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="text-align: center; border-bottom: 0px; width: 200px;"><b>624040.07</b></td>
                        </tr>
                    </td>
                    <!-- 总金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td rowspan="5" style="border-top:0px; border-bottom: 0px; text-align: center; width: 200px;"><b>624040.07</b></td>
                        </tr>
                    </td>
                    <!-- 备注 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="text-align: center; border-bottom: 0px; width: 200px;"></td>
                        </tr>
                    </td>
                </tr>
                <!-- 销售收入 end -->
                <!-- 总收入 start -->
                <tr>
                    <!-- 核算项目 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td 
                                style="border-collapse: collapse; border-right: 1px solid; text-align: center; width: 100px; 
                                border-top: 0px; border-bottom: 0px;">总收入</td>
                            <td style="border-top:0px; text-align: left; width: 600px;"></td>
                        </tr>           
                    </td>
                    <!-- 数量 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;">3348.50</td>
                        </tr>
                    </td>
                    <!-- 单价 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                    <!-- 金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;">10034229.08</td>
                        </tr>
                    </td>
                    <!-- 总金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; border-bottom: 0px; text-align: center; width: 200px;"><b>10034229.08</b></td>
                        </tr>
                    </td>
                    <!-- 备注 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                </tr>
                <!-- 总收入 end -->
                <!-- 净利润 start -->
                <tr>
                    <!-- 核算项目 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 600px;">净利润</td>
                        </tr>           
                    </td>
                    <!-- 数量 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;">3348.50</td>
                        </tr>
                    </td>
                    <!-- 单价 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                    <!-- 金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;">75436.86</td>
                        </tr>
                    </td>
                    <!-- 总金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; border-bottom: 0px; text-align: center; width: 200px;"><b>75436.86</b></td>
                        </tr>
                    </td>
                    <!-- 备注 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                    </td>
                </tr>
                <!-- 净利润 end -->
                <!-- 利润分配 start -->
                <tr>
                    <!-- 核算项目 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td rowspan="2" style="border-collapse: collapse; border-right: 1px solid; text-align: center; width: 100px;">利润分配</td>
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: left; width: 600px;">中粟利润（50%）</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="text-align: left; border-bottom: 0px; width: 600px;">湖南版块利润（50%）</td>
                        </tr>       
                    </td>
                    <!-- 数量 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;">996.42</td>
                        </tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"></td></tr>
                    </td>
                    <!-- 单价 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"></td></tr>
                    </td> 
                    <!-- 金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;"><td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td></tr>
                        <tr style="border: 0px; height: 25px;"><td style="text-align: center; border-bottom: 0px; width: 200px;"><b></b></td></tr>
                    </td> 
                    <!-- 总金额 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; border-bottom: 1px solid;; text-align: center; width: 200px;"><b>37718.43</b></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-top:0px; border-bottom: 0px; text-align: center; width: 200px;"><b>37718.43</b></td>
                        </tr>
                    </td>
                    <!-- 备注 -->
                    <td style="padding: 0px; margin: 0px;" align="center">
                        <tr style="border: 0px; height: 25px;">
                            <td style="border-bottom: 1px solid; border-top:0px; text-align: center; width: 200px;"></td>
                        </tr>
                        <tr style="border: 0px; height: 25px;">
                            <td style="text-align: center; border-bottom: 0px; width: 200px;"></td>
                        </tr>
                    </td>
                </tr>
                <!-- 采购成本 end -->
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