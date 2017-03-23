package com.jkm.hss.controller.bill;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.enums.EnumTradeType;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.request.QueryInfoByOrderNoRequest;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yulong.zhang on 2017/1/4.
 */
@Controller
@RequestMapping(value = "/admin/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private SettlementRecordService settlementRecordService;
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
        long accountId;
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(orderNo);
        if (orderOptional.isPresent()) {
            final Order order = orderOptional.get();
            if (EnumTradeType.WITHDRAW.getId() != order.getTradeType()) {
                return CommonResponse.simpleResponse(-1, "交易单[提现单]异常");
            }
            accountId = order.getPayer();
        } else {
            final SettlementRecord settlementRecord = this.settlementRecordService.getBySettleNo(orderNo).get();
            accountId = settlementRecord.getAccountId();
        }
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.getByAccountId(accountId);
        if (merchantInfoOptional.isPresent()) {
            final MerchantInfo merchantInfo = merchantInfoOptional.get();
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                    .addParam("accountId", merchantInfo.getAccountId())
                    .addParam("userName", merchantInfo.getMerchantName())
                    .addParam("userType", "商户")
                    .build();
        }
        final Optional<Dealer> dealerOptional = this.dealerService.getByAccountId(accountId);
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

}
