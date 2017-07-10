package com.jkm.hss.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.enums.EnumSplitAccountUserType;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.service.ProfitService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.ProfitDetailsRequest;
import com.jkm.hss.product.enums.EnumPayChannelSign;
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
@RequestMapping(value = "/admin/queryProfit")
public class ProfitDetailsController extends BaseController{

    @Autowired
    private ProfitService profitService;

    @Autowired
    private OSSClient ossClient;

    /**
     * 分润明细
     * @param req
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/profitDetails",method = RequestMethod.POST)
    public CommonResponse profitDetails(@RequestBody ProfitDetailsRequest req) throws ParseException {
        final PageModel<JkmProfitDetailsResponse> pageModel = new PageModel<JkmProfitDetailsResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<JkmProfitDetailsResponse> orderList =  profitService.selectProfitDetails(req);
        if (orderList.size()>0){
            for (int i=0;i<orderList.size();i++){
                if (orderList.get(i).getPayChannelSign()!=0){
                    orderList.get(i).setRemark(EnumPayChannelSign.idOf(orderList.get(i).getPayChannelSign()).getUpperChannel().getValue());
                }
                if (orderList.get(i).getAccountUserType()==1){
                    orderList.get(i).setProfitType(EnumSplitAccountUserType.JKM.getValue());
                }
                if (orderList.get(i).getAccountUserType()==2){
                    orderList.get(i).setProfitType(EnumSplitAccountUserType.MERCHANT.getValue());
                }
                if (orderList.get(i).getAccountUserType()==3){
                    orderList.get(i).setProfitType(EnumSplitAccountUserType.FIRST_DEALER.getValue());
                }
                if (orderList.get(i).getAccountUserType()==4){
                    orderList.get(i).setProfitType(EnumSplitAccountUserType.SECOND_DEALER.getValue());
                }


            }
        }
//        if (orderList.size()==0){
//            return  CommonResponse.simpleResponse(-1,"未查询到相关数据");
//        }
        int count = profitService.selectProfitDetailsCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }


    /**
     * 分润金额统计
     * @param req
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/profitAmount",method = RequestMethod.POST)
    public CommonResponse profitAmount(@RequestBody ProfitDetailsRequest req) throws ParseException {
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        JkmProfitDetailsResponse profitAmount =  profitService.profitAmount(req);

        JkmProfitDetailsResponse res = new JkmProfitDetailsResponse();
        if (profitAmount==null){
            res.setSplitAmount("0");
            res.setSplitTotalAmount("0");
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", res);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", profitAmount);
    }

    /**
     * 导出公司分润
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/downLoad",method = RequestMethod.POST)
    private CommonResponse downLoad(@RequestBody ProfitDetailsRequest req) throws ParseException {
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        final String fileZip = this.profitService.downloadExcel(req, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/csv;charset=utf-8");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "profitDetail.csv";
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url = null;
        JSONObject jsonObject = new JSONObject();
        List list = new ArrayList();
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
            jsonObject.put("url",url.getHost() + url.getFile());
            list.add(jsonObject);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "导出成功", list);
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }

}

