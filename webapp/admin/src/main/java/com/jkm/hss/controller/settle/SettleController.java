package com.jkm.hss.controller.settle;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.BatchSettleRequest;
import com.jkm.hss.helper.request.SettleRequest;
import com.jkm.hss.helper.response.ListSettleAuditRecordResponse;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.enums.EnumAccountCheckStatus;
import com.jkm.hss.settle.enums.EnumSettleOptionType;
import com.jkm.hss.settle.enums.EnumSettleStatus;
import com.jkm.hss.settle.helper.requestparam.ListSettleAuditRecordRequest;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/16.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/settle")
public class SettleController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    @Qualifier("accountSettleAuditRecordService")
    private AccountSettleAuditRecordService accountSettleAuditRecordService;

    @ResponseBody
    @RequestMapping(value = "settleTest")
    public CommonResponse settleTest() {
        log.info("结算审核定时任务--start--test");
        this.accountSettleAuditRecordService.handleT1SettleTask();
        log.info("结算审核定时任务--end--test");
        return CommonResponse.simpleResponse(0, "success");
    }


    /**
     *  结算
     *
     * @param settleRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "singleSettle", method = RequestMethod.POST)
    public CommonResponse normalSettle(@RequestBody final SettleRequest settleRequest) {
        final long recordId = settleRequest.getRecordId();
        final int option = settleRequest.getOption();
        final Optional<AccountSettleAuditRecord> recordOptional = this.accountSettleAuditRecordService.getById(recordId);
        if (!recordOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "结算审核记录不存在");
        }
        final AccountSettleAuditRecord accountSettleAuditRecord = recordOptional.get();

        if (EnumSettleOptionType.SETTLE_FORCE_ALL.getId() == option) {
            final Pair<Integer, String> result = this.accountSettleAuditRecordService.forceSettleAll(recordId);
            if (0 != result.getLeft()) {
                return CommonResponse.simpleResponse(-1, result.getRight());
            }
        } else if (EnumSettleOptionType.SETTLE_ACCOUNT_CHECKED.getId() == option && accountSettleAuditRecord.isSideException()) {

            final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(recordId);
            if (CollectionUtils.isEmpty(flows)) {
                return CommonResponse.simpleResponse(-1, "不存在待结算流水");
            }
            final HashSet<String> orderNos = new HashSet<>();
            for (SettleAccountFlow settleAccountFlow : flows) {
                orderNos.add(settleAccountFlow.getOrderNo());
                if (EnumAccountFlowType.INCREASE.getId() != settleAccountFlow.getType()) {
                    return CommonResponse.simpleResponse(-1, "待结算流水异常");
                }
            }
            final List<String> checkedOrderNos = this.orderService.getCheckedOrderNosByOrderNos(new ArrayList<>(orderNos));
            if (CollectionUtils.isEmpty(checkedOrderNos)) {
                return CommonResponse.simpleResponse(-1, "不存在已经对账的待结算流水");
            }
            final Pair<Integer, String> result = this.accountSettleAuditRecordService.settleCheckedPart(recordId, checkedOrderNos);
            if (0 != result.getLeft()) {
                return CommonResponse.simpleResponse(-1, result.getRight());
            }
        } else if (EnumSettleOptionType.SETTLE_NORMAL.getId() == option && accountSettleAuditRecord.isSuccessAccountCheck()) {
            final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(recordId);
            if (CollectionUtils.isEmpty(flows)) {
                return CommonResponse.simpleResponse(-1, "不存在待结算流水");
            }
            final HashSet<String> orderNos = new HashSet<>();
            for (SettleAccountFlow settleAccountFlow : flows) {
                orderNos.add(settleAccountFlow.getOrderNo());
                if (EnumAccountFlowType.INCREASE.getId() != settleAccountFlow.getType()) {
                    return CommonResponse.simpleResponse(-1, "待结算流水异常");
                }
            }
            final List<String> checkedOrderNos = this.orderService.getCheckedOrderNosByOrderNos(new ArrayList<>(orderNos));
            if (flows.size() != checkedOrderNos.size() || flows.size() != accountSettleAuditRecord.getTradeNumber()) {
                log.error("结算审核记录[{}], 结算时，查询到未对账的交易", recordId);
                return CommonResponse.simpleResponse(-1, "查询到未对账的交易");
            }
            final Pair<Integer, String> result = this.accountSettleAuditRecordService.normalSettle(recordId);
            if (0 != result.getLeft()) {
                return CommonResponse.simpleResponse(-1, result.getRight());
            }
        } else {
            return CommonResponse.simpleResponse(-1, "结算选择异常");
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "success");
    }

    /**
     * 批量结算
     *
     * @param batchSettleRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "batchSettle", method = RequestMethod.POST)
    public CommonResponse batchSettle(@RequestBody final BatchSettleRequest batchSettleRequest) {
        final List<Long> recordIds = batchSettleRequest.getRecordIds();
        if (CollectionUtils.isEmpty(recordIds)) {
            return CommonResponse.simpleResponse(-1, "参数错误");
        }
        final List<AccountSettleAuditRecord> records = this.accountSettleAuditRecordService.getByIds(recordIds);
        if (records.size() != recordIds.size()) {
            return CommonResponse.simpleResponse(-1, "参数错误");
        }
        for (AccountSettleAuditRecord record : records) {
            if (!record.isSuccessAccountCheck()) {
                return CommonResponse.simpleResponse(-1, "存在未对账的记录");
            }

            final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(record.getId());
            if (CollectionUtils.isEmpty(flows)) {
                return CommonResponse.simpleResponse(-1, "不存在待结算流水");
            }
            final HashSet<String> orderNos = new HashSet<>();
            for (SettleAccountFlow settleAccountFlow : flows) {
                orderNos.add(settleAccountFlow.getOrderNo());
                if (EnumAccountFlowType.INCREASE.getId() != settleAccountFlow.getType()) {
                    return CommonResponse.simpleResponse(-1, "待结算流水异常");
                }
            }
            final List<String> checkedOrderNos = this.orderService.getCheckedOrderNosByOrderNos(new ArrayList<>(orderNos));
            if (flows.size() != checkedOrderNos.size() || flows.size() != record.getTradeNumber()) {
                log.error("结算审核记录[{}], 结算时，查询到未对账的交易", record.getId());
                return CommonResponse.simpleResponse(-1, "查询到未对账的交易");
            }
        }
        final Pair<Integer, String> result = this.accountSettleAuditRecordService.batchSettle(recordIds);
        if (0 != result.getLeft()) {
            return CommonResponse.simpleResponse(-1, result.getRight());
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "success");
    }

    /**
     * 结算审核记录list
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse listSettleAuditRecord(@RequestBody ListSettleAuditRecordRequest settleAuditRecordRequest) {
        final PageModel<ListSettleAuditRecordResponse> result = new PageModel<>(settleAuditRecordRequest.getPageNo(), settleAuditRecordRequest.getPageSize());
        if (StringUtils.isEmpty(settleAuditRecordRequest.getMerchantNo())) {
            settleAuditRecordRequest.setMerchantNo(null);
        }
        if (StringUtils.isEmpty(settleAuditRecordRequest.getMerchantName())) {
            settleAuditRecordRequest.setMerchantName(null);
        }
        if (StringUtils.isEmpty(settleAuditRecordRequest.getStartSettleDate()) || StringUtils.isEmpty(settleAuditRecordRequest.getEndSettleDate())) {
            settleAuditRecordRequest.setStartSettleDate(null);
            settleAuditRecordRequest.setEndSettleDate(null);
        }
        if (EnumAccountCheckStatus.DUE_ACCOUNT_CHECK.getId() != settleAuditRecordRequest.getCheckedStatus()
                && EnumAccountCheckStatus.SIDE_EXCEPTION.getId() != settleAuditRecordRequest.getCheckedStatus()
                && EnumAccountCheckStatus.SUCCESS_ACCOUNT_CHECK.getId() != settleAuditRecordRequest.getCheckedStatus()) {
            settleAuditRecordRequest.setCheckedStatus(0);
        }
        if (EnumSettleStatus.DUE_SETTLE.getId() != settleAuditRecordRequest.getSettleStatus()
                && EnumSettleStatus.SETTLE_PART.getId() != settleAuditRecordRequest.getSettleStatus()
                && EnumSettleStatus.SETTLED_ALL.getId() != settleAuditRecordRequest.getSettleStatus()) {
            settleAuditRecordRequest.setSettleStatus(0);
        }
        final PageModel<AccountSettleAuditRecord> page = this.accountSettleAuditRecordService.listByParam(settleAuditRecordRequest);
        final List<AccountSettleAuditRecord> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            result.setCount(0);
            result.setRecords(Collections.<ListSettleAuditRecordResponse>emptyList());
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
        }
        final List<ListSettleAuditRecordResponse> responses = Lists.transform(records, new Function<AccountSettleAuditRecord, ListSettleAuditRecordResponse>() {
            @Override
            public ListSettleAuditRecordResponse apply(AccountSettleAuditRecord input) {
                final ListSettleAuditRecordResponse response = new ListSettleAuditRecordResponse();
                response.setId(input.getId());
                response.setMerchantNo(input.getMerchantNo());
                response.setMerchantName(input.getMerchantName());
                response.setDealerNo(input.getDealerNo());
                response.setDealerName(input.getDealerName());
                response.setAppId(input.getAppId());
                response.setAppName(StringUtils.isEmpty(input.getAppId()) ? "" : EnumProductType.of(input.getAppId()).getName());
                response.setTradeDate(input.getTradeDate());
                response.setTradeNumber(input.getTradeNumber());
                response.setSettleAmount(input.getSettleAmount());
                response.setCheckedStatus(input.getAccountCheckStatus());
                response.setCheckedStatusValue(input.getAccountCheckStatus() > 0 ? EnumAccountCheckStatus.of(input.getAccountCheckStatus()).getValue() : "");
                response.setSettleStatus(input.getSettleStatus());
                response.setSettleStatusValue(input.getSettleStatus() > 0 ? EnumSettleStatus.of(input.getSettleStatus()).getValue() : "");
                response.setSettleDate(input.getSettleDate());
                return response;
            }
        });
        result.setRecords(responses);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "success", result);
    }
}
