package com.jkm.hss.controller.settle;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.enums.EnumSettleDestinationType;
import com.jkm.hss.bill.enums.EnumSettleModeType;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import com.jkm.hss.bill.helper.requestparam.QuerySettlementRecordParams;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.response.ListSettlementRecordResponse;
import com.jkm.hss.settle.entity.SettleExceptionRecord;
import com.jkm.hss.settle.service.SettleExceptionRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/1.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/settlementRecord")
public class SettlementRecordController extends BaseController {

    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private SettleExceptionRecordService settleExceptionRecordService;
    /**
     * 结算记录list
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse listSettlementRecord(@RequestBody QuerySettlementRecordParams querySettlementRecordParams) {
        final PageModel<ListSettlementRecordResponse> result = new PageModel<>(querySettlementRecordParams.getPageNo(), querySettlementRecordParams.getPageSize());
        if (StringUtils.isEmpty(querySettlementRecordParams.getUserNo())) {
            querySettlementRecordParams.setUserNo(null);
        }
        if (StringUtils.isEmpty(querySettlementRecordParams.getUserName())) {
            querySettlementRecordParams.setUserName(null);
        }
        if (EnumAccountUserType.MERCHANT.getId() != querySettlementRecordParams.getUserType()
                && EnumAccountUserType.DEALER.getId() != querySettlementRecordParams.getUserType()){
            return CommonResponse.simpleResponse(-1, "参数错误");
        }
        if (StringUtils.isEmpty(querySettlementRecordParams.getSettleNo())) {
            querySettlementRecordParams.setSettleNo(null);
        }
        if (StringUtils.isEmpty(querySettlementRecordParams.getStartDate()) || StringUtils.isEmpty(querySettlementRecordParams.getEndDate())) {
            querySettlementRecordParams.setStartDate(null);
            querySettlementRecordParams.setEndDate(null);
        }

        final PageModel<SettlementRecord> page = this.settlementRecordService.listSettlementRecordByParam(querySettlementRecordParams);
        final List<SettlementRecord> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            result.setCount(0);
            result.setRecords(Collections.<ListSettlementRecordResponse>emptyList());
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
        }
        final List<ListSettlementRecordResponse> responses = Lists.transform(records, new Function<SettlementRecord, ListSettlementRecordResponse>() {
            @Override
            public ListSettlementRecordResponse apply(SettlementRecord input) {
                final ListSettlementRecordResponse listSettlementRecordResponse = new ListSettlementRecordResponse();
                listSettlementRecordResponse.setId(input.getId());
                listSettlementRecordResponse.setSettleNo(input.getSettleNo());
                listSettlementRecordResponse.setUserNo(input.getUserNo());
                listSettlementRecordResponse.setUserName(input.getUserName());
                listSettlementRecordResponse.setAppId(input.getAppId());
                listSettlementRecordResponse.setSettleDate(input.getSettleDate());
                listSettlementRecordResponse.setTradeNumber(input.getTradeNumber());
                listSettlementRecordResponse.setSettleAmount(input.getSettleAmount().toPlainString());
                listSettlementRecordResponse.setSettleDestination(input.getSettleDestination());
                listSettlementRecordResponse.setSettleDestinationValue(input.getSettleDestination() > 0 ? EnumSettleDestinationType.of(input.getSettleDestination()).getValue() : "");
                listSettlementRecordResponse.setSettleMode(input.getSettleMode());
                listSettlementRecordResponse.setSettleModeValue(input.getSettleMode() > 0 ? EnumSettleModeType.of(input.getSettleMode()).getValue() : "");
                listSettlementRecordResponse.setSettleStatus(input.getSettleStatus());
                listSettlementRecordResponse.setSettleStatusValue(input.getSettleStatus() > 0 ? EnumSettleStatus.of(input.getSettleStatus()).getValue() : "");
                return listSettlementRecordResponse;
            }
        });

        result.setCount(page.getCount());
        result.setRecords(responses);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
    }

    /**
     * 结算挂起记录list
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "exceptionList", method = RequestMethod.POST)
    public CommonResponse listSettlementExceptionRecord(@RequestBody QuerySettlementRecordParams querySettlementRecordParams) {

        final PageModel<SettleExceptionRecord> result = new PageModel<>(querySettlementRecordParams.getPageNo(), querySettlementRecordParams.getPageSize());
        if (StringUtils.isEmpty(querySettlementRecordParams.getUserNo())) {
            querySettlementRecordParams.setUserNo(null);
        }
        if (StringUtils.isEmpty(querySettlementRecordParams.getUserName())) {
            querySettlementRecordParams.setUserName(null);
        }
        if (StringUtils.isEmpty(querySettlementRecordParams.getStartDate()) || StringUtils.isEmpty(querySettlementRecordParams.getEndDate())) {
            querySettlementRecordParams.setStartDate(null);
            querySettlementRecordParams.setEndDate(null);
        }

        final PageModel<SettleExceptionRecord> page = this.settleExceptionRecordService.listSettlementRecordByParam(querySettlementRecordParams);
        final List<SettleExceptionRecord> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            result.setCount(0);
            result.setRecords(Collections.<SettleExceptionRecord>emptyList());
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
        }

        result.setCount(page.getCount());
        result.setRecords(records);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
    }

    /**
     * 处理挂起记录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "handle", method = RequestMethod.POST)
    public CommonResponse handle(@RequestBody QuerySettlementRecordParams querySettlementRecordParams) {
        try{
            Pair<Integer, String> pair =
                    this.settleExceptionRecordService.handleRecord(querySettlementRecordParams.getSettleExceptionId());
            if (pair.getLeft() > 0){
                return CommonResponse.simpleResponse(1, pair.getRight());
            }
            return CommonResponse.simpleResponse(-1, pair.getRight());
        }catch (final Throwable throwable){
            log.info("处理异常，异常信息：" + throwable.getMessage());
            return CommonResponse.simpleResponse(-1, "处理异常");
        }

    }
}
