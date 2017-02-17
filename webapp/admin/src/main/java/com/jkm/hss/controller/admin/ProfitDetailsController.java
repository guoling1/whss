package com.jkm.hss.controller.admin;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.service.ProfitService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.request.ProfitDetailsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

/**
 * Created by lt on 2016/12/7.
 */
@Slf4j

@Controller
@RequestMapping(value = "/admin/queryJkmProfit")
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
    public CommonResponse profitDetails(ProfitDetailsRequest req) throws ParseException {
        final PageModel<JkmProfitDetailsResponse> pageModel = new PageModel<JkmProfitDetailsResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        List<JkmProfitDetailsResponse> orderList =  profitService.selectProfitDetails(req);
        int count = profitService.selectProfitDetailsCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);

        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }



}

