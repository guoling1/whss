package com.jkm.hss.controller.admin;

import com.aliyun.oss.OSSClient;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.entity.JkmProfitResponse;
import com.jkm.hss.bill.service.ShareProfitService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.request.JkmProfitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
public class JkmProfitController extends BaseController{

    @Autowired
    private ShareProfitService shareProfitService;

    @Autowired
    private OSSClient ossClient;

    /**
     * 金开门分润
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/accountList",method = RequestMethod.POST)
    public CommonResponse accountList(@RequestBody JkmProfitRequest req) throws ParseException {
        final PageModel<JkmProfitResponse> pageModel = new PageModel<JkmProfitResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());

        List<JkmProfitResponse> orderList =  shareProfitService.selectAccountList(req);
        long count = shareProfitService.selectAccountListCount(req);
        if (orderList.size()==0){
            return CommonResponse.simpleResponse(-1,"未查询到相关数据");
        }
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }



}

