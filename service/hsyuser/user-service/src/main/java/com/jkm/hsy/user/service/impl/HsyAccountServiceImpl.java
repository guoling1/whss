package com.jkm.hsy.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.service.HsyAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by yulong.zhang on 2017/1/19.
 */
@Slf4j
@Service(value = "hsyAccountService")
public class HsyAccountServiceImpl implements HsyAccountService {

    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private SendMessageService sendMessageService;

    /**
     * {@inheritDoc}
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    @Transactional
    public String getAccount(final String dataParam, final AppParam appParam) {
        final JSONObject dataJo = JSONObject.parseObject(dataParam);
        final JSONObject result = new JSONObject();
        final long accountId = dataJo.getLongValue("accountId");
        final Optional<Account> accountOptional = this.accountService.getById(accountId);
        final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(accountId).get(0);
        if (accountOptional.isPresent()) {
            final Account account = accountOptional.get();
            result.put("accountId", account.getId());
            result.put("totalAmount", account.getTotalAmount().toPlainString());
            result.put("available", account.getAvailable().toPlainString());
            result.put("dueSettleAmount", account.getDueSettleAmount().toPlainString());
            result.put("frozenAmount", account.getFrozenAmount().toPlainString());
            result.put("isBindCode", !StringUtils.isEmpty(appAuUser.getDealerID() + ""));
        }
        return result.toJSONString();
    }

    /**
     * {@inheritDoc}
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    public String getVerifyCode(final String dataParam, final AppParam appParam) {
        final JSONObject dataJo = JSONObject.parseObject(dataParam);
        final JSONObject result = new JSONObject();
        final long accountId = dataJo.getLongValue("accountId");
        final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(accountId).get(0);
        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(appAuUser.getCellphone(), EnumVerificationCodeType.WITHDRAW_MERCHANT);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(appAuUser.getCellphone())
                    .uid(appAuUser.getId() + "")
                    .data(params)
                    .userType(EnumUserType.FOREGROUND_USER)
                    .noticeType(EnumNoticeType.WITHDRAW_CODE_MERCHANT)
                    .build()
            );
            result.put("code", 0);
//            result.put("verifyCode", verifyCode.getValue());
            result.put("msg", "发送成功");
        } else {
            result.put("code", -1);
            result.put("msg", verifyCode.getValue());
        }
        return result.toJSONString();
    }


}
