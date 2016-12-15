package com.jkm.hss.controller.orderRecord;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.OrderRecordAndMerchant;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.OrderRecordAndMerchantRequest;
import com.jkm.hss.merchant.helper.request.UnfreezeRequest;
import com.jkm.hss.merchant.helper.request.UnpassRequest;
import com.jkm.hss.merchant.helper.request.WithDrawRequest;
import com.jkm.hss.merchant.service.OrderRecordService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-14 16:20
 */
@Controller
@RequestMapping(value = "/admin/withdraw")
public class WithDrawController extends BaseController {
    @Autowired
    private OrderRecordService orderRecordService;
    /**
     * 提现列表
     * @param request
     * @param response
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/withdrawListByContions", method = RequestMethod.POST)
    public CommonResponse withdrawListByContions(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final OrderRecordAndMerchantRequest req) {
        final PageModel<OrderRecordAndMerchant> pageModel = new PageModel<>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getMobile()!=null&&!"".equals(req.getMobile())){
            req.setMobile(MerchantSupport.passwordDigest(req.getMobile(),"JKM"));
        }
        List<OrderRecordAndMerchant> orderList =  orderRecordService.selectDrawWithRecordByPage(req);
        long count = orderRecordService.selectDrawWithCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 审核不通过
     * @param request
     * @param response
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unPass", method = RequestMethod.POST)
    public CommonResponse unPass(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final UnpassRequest req) {
        JSONObject jo = orderRecordService.unPass(req.getId(),req.getMessage());
        return CommonResponse.simpleResponse(jo.getInt("code"), jo.getString("message"));
    }

    /**
     * 提现失败，退款
     * @param request
     * @param response
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unfreeze", method = RequestMethod.POST)
    public CommonResponse unfreeze(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final UnfreezeRequest req) {
        JSONObject jo = orderRecordService.unfreeze(req.getId());
        return CommonResponse.simpleResponse(jo.getInt("code"), jo.getString("message"));
    }

    /**
     * 通过审核
     * @param request
     * @param response
     * @param req
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkWithdraw", method = RequestMethod.POST)
    public CommonResponse checkWithdraw(final HttpServletRequest request, final HttpServletResponse response, @RequestBody final WithDrawRequest req) {
        JSONObject jo = orderRecordService.checkWithdraw(req);
        return CommonResponse.simpleResponse(jo.getInt("code"), jo.getString("message"));
    }
}
