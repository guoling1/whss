package com.jkm.hss.controller.orderRecord;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantAndOrderRecord;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.merchant.entity.OrderRecordAndMerchant;
import com.jkm.hss.merchant.entity.OrderRecordConditions;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.PageUtils;
import com.jkm.hss.merchant.helper.ValidateOrderRecord;
import com.jkm.hss.merchant.helper.request.OrderListRequest;
import com.jkm.hss.merchant.service.OrderRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * Created by lt on 2016/12/7.
 */
@Controller
@RequestMapping(value = "/admin/queryOrderRecord")
public class OrderRecordController extends BaseController{

    @Autowired
    private OrderRecordService orderRecordService;

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
    public CommonResponse orderList(@RequestBody OrderListRequest req){
        final PageModel<MerchantAndOrderRecord> pageModel = new PageModel<MerchantAndOrderRecord>(req.getPage(), req.getSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getMdMobile()!=null&&!"".equals(req.getMdMobile())){
            req.setMdMobile(MerchantSupport.passwordDigest(req.getMdMobile(),"JKM"));
        }
        List<MerchantAndOrderRecord> orderList =  orderRecordService.selectOrderListByPage(req);
        long count = orderRecordService.selectOrderListCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(orderList);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }
}

