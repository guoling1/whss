package com.jkm.hss.controller.orderRecord;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.MerchantAndOrderRecord;
import com.jkm.hss.merchant.entity.OrderRecordConditions;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.PageUtils;
import com.jkm.hss.merchant.helper.ValidateOrderRecord;
import com.jkm.hss.merchant.helper.request.OrderListRequest;
import com.jkm.hss.merchant.service.OrderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lt on 2016/12/7.
 */
@Slf4j

@Controller
@RequestMapping(value = "/admin/queryOrderRecord")
public class OrderRecordController extends BaseController{

    @Autowired
    private OrderRecordService orderRecordService;

    @Autowired
    private OSSClient ossClient;

    @ResponseBody
    @RequestMapping(value = "/selectOrderRecordByConditions",method = RequestMethod.POST)
    public CommonResponse selectOrderRecordByConditions(@RequestBody OrderRecordConditions orderRecordConditions){
        int pageNo = orderRecordConditions.getPageNo();
        System.out.println("startTime : " + orderRecordConditions.getStartTime());
        System.out.println("endTime : " + orderRecordConditions.getEndTime());
        orderRecordConditions = ValidateOrderRecord.validateOrderRecord(orderRecordConditions); //验证传入的参数
        orderRecordConditions.setPageNo((pageNo - 1) * orderRecordConditions.getPageSize());
        List<OrderRecordConditions> list = orderRecordService.selectOrderRecordByConditions(orderRecordConditions);
        int count = orderRecordService.selectOrderRecordByConditionsCount(orderRecordConditions);
        PageUtils<OrderRecordConditions> page = new PageUtils<>(); //分页
        page.setPageNo(pageNo);
        page.setPageSize(orderRecordConditions.getPageSize());
        page.setRecord(list);
        page.setTotalCount(count);
        return CommonResponse.objectResponse(1,"success",page);
    }

    /**
     * 交易记录
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderList",method = RequestMethod.POST)
    public CommonResponse orderList(@RequestBody OrderListRequest req) throws ParseException {
        final PageModel<MerchantAndOrderRecord> pageModel = new PageModel<MerchantAndOrderRecord>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        if(req.getMdMobile()!=null&&!"".equals(req.getMdMobile())){
            req.setMdMobile(MerchantSupport.passwordDigest(req.getMdMobile(),"JKM"));
        }
        List<MerchantAndOrderRecord> orderList =  orderRecordService.selectOrderListByPage(req);
        long count = orderRecordService.selectOrderListCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        String downLoadExcel = downLoad();
        pageModel.setExt(downLoadExcel);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 交易记录详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderListAll",method = RequestMethod.POST)
    public CommonResponse orderListAll(@RequestBody OrderListRequest req) throws ParseException {
        MerchantAndOrderRecord orderList =  orderRecordService.selectOrderListByPageAll(req);


        if (orderList.getBankNo()!= null){
            orderList.setBankNo(MerchantSupport.decryptBankCard(orderList.getBankNo()));
        }
        if (orderList.getMobile()!=null){
            orderList.setMobile(MerchantSupport.decryptMobile(orderList.getMobile()));
        }
        if (orderList.getReserveMobile()!=null) {
            orderList.setReserveMobile(MerchantSupport.decryptMobile(orderList.getReserveMobile()));
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", orderList);
    }

    /**
     * 导出全部
     * @return
     */
    private String downLoad(){
        final String fileZip = this.orderRecordService.downloadExcel(ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        Date date = new Date();
        long nousedate =  date.getTime();
        String fileName = "hss/"+  nowDate + "/" + "trade.xls";
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url = null;
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
            return url.getHost() + url.getFile();
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }

    /**
     * 导出 Excel
     *
     * @return
     */
//    @ResponseBody
//    @RequestMapping(value = "downloadExcel", method = RequestMethod.POST)
//    public CommonResponse downloadExcel() {
//        final String fileZip = this.orderRecordService.downloadExcel(ApplicationConsts.getApplicationConfig().ossBucke());
//
//        final ObjectMetadata meta = new ObjectMetadata();
//        meta.setCacheControl("public, max-age=31536000");
//        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
//        meta.setContentType("application/x-xls");
//        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
//        String nowDate = sdf.format(new Date());
//        Date date = new Date();
//        long nousedate =  date.getTime();
//        String fileName = "hss/"+  nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) +".xls";
//        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
//        URL url;
//        try {
//            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
//            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
//        } catch (IOException e) {
//            log.error("上传文件失败", e);
//            return CommonResponse.simpleResponse(-1, "文件上传失败");
//        }
//        FileUtils.deleteQuietly(new File(fileZip));
//        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
//                .addParam("url", url.getHost() + url.getFile()).build();
//    }


}

