package com.jkm.hss.controller.trade;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.MerchantTradeResponse;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2017/3/27.
 */
@Slf4j
@Controller
@RequestMapping(value = "/daili/tradeQuery")
public class TradeQueryController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OSSClient ossClient;

    @ResponseBody
    @RequestMapping(value = "/tradeList", method = RequestMethod.POST)
    public CommonResponse tradeList(@RequestBody OrderTradeRequest req) throws ParseException {
        final PageModel<MerchantTradeResponse> pageModel = new PageModel<MerchantTradeResponse>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        long dealerId = super.getDealerId();
        int level = super.getDealer().get().getLevel();
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        req.setDealerId(dealerId);
        if (level==2){
            List<MerchantTradeResponse> list = orderService.getTrade(req);
            int count = orderService.listCount(req);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            String downLoadHsyMerchant = downLoadHsyMerchant(req);
            pageModel.setExt(downLoadHsyMerchant);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
        }
        if (level==1){
            List<MerchantTradeResponse> list = orderService.getTradeFirst(req);
            int count = orderService.listFirstCount(req);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            String downLoadHsyMerchant = downLoadHsyMerchant(req);
            pageModel.setExt(downLoadHsyMerchant);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
        }

        return CommonResponse.simpleResponse(-1, "查询异常");
    }

    /**
     * 统计
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/amountCount",method = RequestMethod.POST)
    public CommonResponse amountCount(@RequestBody OrderTradeRequest req) throws ParseException {
        long dealerId = super.getDealerId();
        int level = super.getDealer().get().getLevel();
        req.setDealerId(dealerId);
        if (level==2){
            String totalPayment = this.orderService.getAmountCount(req);
            String totalPoundage = this.orderService.getAmountCount1(req);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totalPayment",totalPayment);
            jsonObject.put("totalPoundage",totalPoundage);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", jsonObject);
        }
        if (level==1){
            String totalPayment = this.orderService.getAmountCount(req);
            String totalPoundage = this.orderService.getAmountCount1(req);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totalPayment",totalPayment);
            jsonObject.put("totalPoundage",totalPoundage);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", jsonObject);
        }
        return CommonResponse.simpleResponse(-1, "查询异常");
    }

    /**
     * 导出全部
     * @return
     */
    private String downLoadHsyMerchant(@RequestBody final OrderTradeRequest req) throws ParseException {
        final String fileZip = this.orderService.downloadExcel(req, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "hsyMerchant.xls";
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
}
