package com.jkm.hss.controller.bill;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.WithdrawRequest;
import com.jkm.hss.bill.entity.WithdrawResponse;
import com.jkm.hss.bill.enums.EnumTradeType;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.request.QueryInfoByOrderNoRequest;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/4.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    /**
     * 提现审核时（查询用户信息）
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryInfoByOrderNo", method = RequestMethod.POST)
    public CommonResponse queryInfoByOrderNo(@RequestBody QueryInfoByOrderNoRequest queryInfoByOrderNoRequest) {
        final String orderNo = queryInfoByOrderNoRequest.getOrderNo();
        if (StringUtils.isEmpty(orderNo)) {
            return CommonResponse.simpleResponse(-1, "交易订单号不能空");
        }
        final Order order = this.orderService.getByOrderNo(orderNo).get();
        if (EnumTradeType.WITHDRAW.getId() != order.getTradeType()) {
            return CommonResponse.simpleResponse(-1, "交易单[提现单]异常");
        }
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.getByAccountId(order.getPayer());
        if (merchantInfoOptional.isPresent()) {
            final MerchantInfo merchantInfo = merchantInfoOptional.get();
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("accountId", merchantInfo.getAccountId())
                    .addParam("userName", merchantInfo.getMerchantName())
                    .addParam("userType", "商户")
                    .build();
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getByAccountId(order.getPayer());
        if (dealerOptional.isPresent()) {
            final Dealer dealer = dealerOptional.get();
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("accountId", dealer.getAccountId())
                    .addParam("userName", dealer.getProxyName())
                    .addParam("userType", "代理商")
                    .build();
        }
        return CommonResponse.simpleResponse(-1, "没有查到打款用户信息");
    }


    @ResponseBody
    @RequestMapping(value = "/withdrawList", method = RequestMethod.POST)
    public CommonResponse withdrawList(@RequestBody WithdrawRequest req) throws ParseException {
//        final PageModel<WithdrawResponse> pageModel = new PageModel<WithdrawResponse>(req.getPageNo(), req.getPageSize());
//        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
            }
        List<WithdrawResponse> list = this.orderService.withdrawList(req);
        long count = this.orderService.getNo(req);
//        pageModel.setCount(count);
//        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1,"success",list);

        }



}
