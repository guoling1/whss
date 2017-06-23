package com.jkm.hss.controller.orderRecord;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.SplitAccountRefundRecord;
import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.bill.entity.ProfitRefundResponse;
import com.jkm.hss.bill.helper.responseparam.PaymentSdkQueryPayOrderByOrderNoResponse;
import com.jkm.hss.bill.helper.responseparam.PaymentSdkQueryRefundOrderByOrderNoResponse;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lt on 2016/12/7.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/queryOrder")
public class OrderTradeController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private OSSClient ossClient;


    /**
     * 交易记录
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderList",method = RequestMethod.POST)
    public CommonResponse orderList(@RequestBody OrderTradeRequest req) throws ParseException {
        final PageModel<MerchantTradeResponse> pageModel = new PageModel<MerchantTradeResponse>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        if (req.getAppId().equals("好收收")){
            req.setAppId("hss");
        }
        if (req.getAppId().equals("好收银")){
            req.setAppId("hsy");
        }

        List<MerchantTradeResponse> orderList =  orderService.selectOrderListByPage(req);
        int count = orderService.selectOrderListCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
//        String downLoadExcel = downLoad(req);
//        pageModel.setExt(downLoadExcel);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }


    /**
     * 统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/amountCount ",method = RequestMethod.POST)
    public CommonResponse amountCount(@RequestBody OrderTradeRequest req) throws ParseException {
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        if (req.getAppId().equals("好收收")){
            req.setAppId("hss");
        }
        if (req.getAppId().equals("好收银")){
            req.setAppId("hsy");
        }
        String orderList =  orderService.amountCount(req);
        if (orderList==null||("").equals(orderList)){
            int res = 0;
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", res);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", orderList);
    }

    /**
     * 交易记录详情
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderListAll",method = RequestMethod.POST)
    public JSONObject orderListAll(@RequestBody OrderTradeRequest req) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        MerchantTradeResponse orderList =  orderService.selectOrderListByPageAll(req);

        List<PaymentSdkQueryPayOrderByOrderNoResponse> payList = this.orderService.queryPayOrderByOrderNo(req.getOrderNo());
        String refundOrder = this.orderService.getRefundOrder(req.getOrderNo());
        List<PaymentSdkQueryRefundOrderByOrderNoResponse> refundList = this.orderService.queryRefundOrderByOrderNo(refundOrder);
        List<ProfitRefundResponse> profitRefundList = this.orderService.getProfitRefundList(req.getOrderNo());
        List<SplitAccountRefundRecord> splitAccountRefundList = this.orderService.splitAccountRefundList(req.getOrderNo());
        jsonObject.put("msg","ok");
        jsonObject.put("code",1);
        JSONObject jo = new JSONObject();
        jo.put("orderList",orderList);
        jo.put("payList",payList);
        jo.put("refundList",refundList);
        jo.put("profitRefundList",profitRefundList);
        jo.put("splitAccountRefundList",splitAccountRefundList);
        jsonObject.put("result",jo);

        return jsonObject;
    }

    /**
     * 导出全部
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/downLoad",method = RequestMethod.POST)
    private CommonResponse downLoad(@RequestBody OrderTradeRequest req){
        final String fileZip = this.orderService.downloadExcel(req, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "trade.xls";
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url = null;
        JSONObject jsonObject = new JSONObject();
        List list = new ArrayList();
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
            jsonObject.put("url",url.getHost() + url.getFile());
            list.add(jsonObject);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", list);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }




}

