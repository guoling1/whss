package com.jkm.hss.controller.orderRecord;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.OrderRecordConditions;
import com.jkm.hss.merchant.helper.ValidateOrderRecord;
import com.jkm.hss.merchant.service.OrderRecordService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by lt on 2016/12/9.
 */
@Controller
@RequestMapping(value = "/admin/export")
public class ExportOrderRecordController extends BaseController {
    @Autowired
    private OrderRecordService orderRecordService;

    @ResponseBody
    @RequestMapping(value = "exportOrderRecord",method = RequestMethod.POST)
    public CommonResponse exportOrderRecord(@RequestBody OrderRecordConditions orderRecordConditions) throws Exception {
        orderRecordConditions = ValidateOrderRecord.validateOrderRecord(orderRecordConditions);  //验证传入的参数
        orderRecordConditions.setPageNo(-1);  //导出  不需要分页
        List<OrderRecordConditions> list = orderRecordService.selectOrderRecordByConditions(orderRecordConditions);
        if (list.size() == 0 || list == null){
            return CommonResponse.simpleResponse(-1,"未找到相应数据！");
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("订单");
        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        //创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        //样式字体居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //给表头第一行一次创建单元格
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("订单号");
        cell.setCellStyle(style);
        cell = row.createCell( (short) 1);
        cell.setCellValue("交易日期");
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);
        cell.setCellValue("商户编号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);
        cell.setCellValue("商户名称");
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);
        cell.setCellValue("商户手机号");
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);
        cell.setCellValue("交易金额");
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);
        cell.setCellValue("订单状态");
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);
        cell.setCellValue("结算状态");
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);
        cell.setCellValue("业务");
        cell.setCellStyle(style);

        //向单元格里填充数据
        for (short i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(list.get(i).getOrderId());
            row.createCell(1).setCellValue(DateFormatUtil.format(list.get(i).getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
            row.createCell(2).setCellValue(list.get(i).getMerchantId());
            row.createCell(3).setCellValue(list.get(i).getSubName());
            row.createCell(4).setCellValue(list.get(i).getMdMobile());
            row.createCell(5).setCellValue(list.get(i).getTotalFee().doubleValue());
            //支付结果 N-待支付 S-成功 F-失败
            String payResult = list.get(i).getPayResult();
            if (payResult.equals("N")){
                payResult = "待支付";
            }
            if (payResult.equals("S")){
                payResult = "成功";
            }
            if (payResult.equals("F")){
                payResult = "失败";
            }
            row.createCell(6).setCellValue(payResult);
            //0 正常   1 删除
            int status = list.get(i).getStatus();
            String s = "";
            if (status == 0){
                s = "正常";
            }
            if (status == 1){
                s = "删除";
            }
            row.createCell(7).setCellValue(s);
            //业务(101.微信扫码 102.支付宝扫码 103.银联)
            int payChannel = list.get(i).getPayChannel();
            String pay = "";
            if (payChannel == 101){
                pay = "微信扫码";
            }
            if (payChannel == 102){
                pay = "支付宝扫码";
            }
            if (payChannel == 103){
                pay = "银联";
            }
            row.createCell(8).setCellValue(pay);
        }
        try {
            //文件导出位置
            FileOutputStream out = new FileOutputStream(orderRecordConditions.getSaveUrl() + "//订单查询.xls");
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResponse.simpleResponse(1,"success");
    }
}

