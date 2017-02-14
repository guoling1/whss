package com.jkm.hss.controller.MyProfitController;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.SplitAccountRecord;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.request.ProfitDetailsSelectRequest;
import com.jkm.hss.helper.response.ProfitDetailsSelectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-02-13.
 */
@RestController
@RequestMapping(value = "/profit")
public class ProfitController extends BaseController{

    @Autowired
    private SplitAccountRecordService splitAccountRecordService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DealerService dealerService;

    @ResponseBody
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public CommonResponse getShallProfitDetails(@RequestBody final ProfitDetailsSelectRequest request){

        final Dealer dealer = this.getDealer().get();
        final long accountId = dealer.getAccountId();
        PageModel<SplitAccountRecord> pageModel = this.splitAccountRecordService.selectByParam(request.getPageNo(), request.getPageSize(), accountId, request.getOrderNo(),
                request.getBusinessType(), request.getBeginDate(), request.getEndDate());

        final List<SplitAccountRecord> records = pageModel.getRecords();
        List<String> orderNos = Lists.transform(records, new Function<SplitAccountRecord, String>() {
            @Override
            public String apply(SplitAccountRecord input) {
                return input.getOrderNo();
            }
        });
        List<Order> orderList = this.orderService.getByOrderNos(orderNos);
        final Map<String, Order> map = Maps.uniqueIndex(orderList, new Function<Order, String>() {
            @Override
            public String apply(Order input) {
                return input.getOrderNo();
            }
        });

        List<ProfitDetailsSelectResponse> responseList = Lists.transform(records, new Function<SplitAccountRecord, ProfitDetailsSelectResponse>() {
            @Override
            public ProfitDetailsSelectResponse apply(SplitAccountRecord input) {
                final Order order = map.get(input.getOrderNo());

                ProfitDetailsSelectResponse response = new ProfitDetailsSelectResponse();
                response.setSplitOrderNo(input.getSplitOrderNo());
                response.setBusinessType(input.getBusinessType());
                response.setSplitCreateTime(input.getCreateTime());
                response.setOrderNo(input.getOrderNo());
                response.setSplitSettlePeriod(getSplitSettlePeriod(order));
                response.setSettleTime(order.getSettleTime());
                response.setDealerName(getDealerName(dealer));
                response.setSplitAmount(input.getSplitAmount().toString());
                response.setRemark(getRemark(input.getBusinessType()));
                return response;
            }
        });

        PageModel<ProfitDetailsSelectResponse> model = new PageModel<>(pageModel.getPageNO(), pageModel.getPageSize());
        model.setCount(pageModel.getCount());
        model.setRecords(responseList);

        return CommonResponse.objectResponse(0, "SUCCESS", model);
    }


    private String getSplitSettlePeriod(Order order){
        if (order.getAppId().equals(EnumAppType.HSS.getId())){
            return "D0";
        }else {

            return "T1";
        }
    }

    private String getDealerName(Dealer dealer){

        if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
            return  dealer.getProxyName();
        }

        return this.dealerService.getById(dealer.getFirstLevelDealerId()).get().getProxyName();
    }

    private String getRemark(String businessType){

        switch (businessType){
            case "hssPay":
                return "收单分润";
            case "hssWithdraw":
                return "提现分润";
            case "hssPromote":
                return "升级费";
            case "hsyPay":
                return "收单分润";
            default:
                return "";
        }
    }
}
