package com.jkm.hss.controller.admin;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.service.ProfitService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.ProfitDetailsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
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
@RequestMapping(value = "/admin/queryProfit")
public class ProfitDetailsController extends BaseController{

    @Autowired
    private ProfitService profitService;

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
                if (orderList.get(i).getLevel()==1){
                    orderList.get(i).setProfitType("一级代理商");
                }
                if (orderList.get(i).getLevel()==2){
                    orderList.get(i).setProfitType("二级代理商");
                }if (orderList.get(i).getLevel()==0){
                    orderList.get(i).setProfitType("金开门");
                }
            }
        }
        if (orderList==null){
            return  CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
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
        BigDecimal x = new BigDecimal("0.0");
        JkmProfitDetailsResponse res = new JkmProfitDetailsResponse();
        if (profitAmount==null){
            res.setSplitAmount(x);
            res.setSplitTotalAmount(x);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", res);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "统计完成", profitAmount);
    }

}

