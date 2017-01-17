package com.jkm.hss.controller.settle;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.BatchSettleRequest;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.enums.EnumSettleOptionType;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/16.
 */
@Slf4j
@Controller
@RequestMapping(value = "/settle")
public class SettleController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private AccountSettleAuditRecordService accountSettleAuditRecordService;

    /**
     *  结算
     *
     * @param recordId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/${recordId}/${option}")
    public CommonResponse normalSettle(@PathVariable final long recordId, @PathVariable final int option) {
        final Optional<AccountSettleAuditRecord> recordOptional = this.accountSettleAuditRecordService.getById(recordId);
        if (!recordOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "结算审核记录不存在");
        }
        final AccountSettleAuditRecord accountSettleAuditRecord = recordOptional.get();
        if (accountSettleAuditRecord.isDueAccountCheck()) {
            return CommonResponse.simpleResponse(-1, "未对账");
        }
        if (accountSettleAuditRecord.isSideException() &&
                !(EnumSettleOptionType.SETTLE_FORCE_ALL.getId() == option || EnumSettleOptionType.SETTLE_ACCOUNT_CHECKED.getId() == option)) {
            return CommonResponse.simpleResponse(-1, "对账结果有单边,不能正常结算");
        }
        if (accountSettleAuditRecord.isSuccessAccountCheck() && EnumSettleOptionType.SETTLE_NORMAL.getId() != option) {
            return CommonResponse.simpleResponse(-1, "对账结果无异常,只能正常结算");
        }
        if (accountSettleAuditRecord.isSideException()) {
            if (EnumSettleOptionType.SETTLE_FORCE_ALL.getId() == option) {
                final Pair<Integer, String> result = this.accountSettleAuditRecordService.forceSettleAll(recordId);
                if (0 != result.getLeft()) {
                    return CommonResponse.simpleResponse(-1, result.getRight());
                }
            } else if (EnumSettleOptionType.SETTLE_ACCOUNT_CHECKED.getId() == option) {
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
            } else {
                return CommonResponse.simpleResponse(-1, "对账结果有单边");
            }
        } else if (accountSettleAuditRecord.isSuccessAccountCheck()) {
            if (EnumSettleOptionType.SETTLE_NORMAL.getId() == option) {
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
                return CommonResponse.simpleResponse(-1, "对账结果无异常，请选择正常结算");
            }
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
}
