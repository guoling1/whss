package com.jkm.hsy.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.service.HsyAccountService;
import com.jkm.hsy.user.service.UserCurrentChannelPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
    @Autowired
    private UserCurrentChannelPolicyService userCurrentChannelPolicyService;
    @Autowired
    private OrderService orderService;
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
        final AppBizShop priShop = this.hsyShopDao.findPrimaryAppBizShopByAccountID(accountId).get(0);
        final AppBizCard appBizCard = new AppBizCard();
        appBizCard.setSid(priShop.getId());
        final AppBizCard card = this.hsyShopDao.findAppBizCardByParam(appBizCard).get(0);
        final String cardNO = card.getCardNO();
        final String cardBank = card.getCardBank();
        final UserCurrentChannelPolicy userCurrentChannelPolicy = this.userCurrentChannelPolicyService.selectByUserId(appAuUser.getId()).get();
        if (accountOptional.isPresent()) {
            final Account account = accountOptional.get();
            result.put("accountId", account.getId());
            result.put("totalAmount", account.getTotalAmount().toPlainString());
            result.put("available", account.getAvailable().toPlainString());
            result.put("dueSettleAmount", account.getDueSettleAmount().toPlainString());
            result.put("frozenAmount", account.getFrozenAmount().toPlainString());
            result.put("isBindCode", !StringUtils.isEmpty(appAuUser.getDealerID() + ""));
            if (appAuUser.getIsOpenD0() == EnumBoolean.TRUE.getCode())
                if (userCurrentChannelPolicy.getWechatChannelTypeSign() == EnumPayChannelSign.SYJ_WECHAT.getId() ||
                        userCurrentChannelPolicy.getAlipayChannelTypeSign() == EnumPayChannelSign.SYJ_ALIPAY.getId()) {
                    JSONObject jsonObject = this.orderService.d0WithDrawImpl(account, appAuUser.getId());
                    result.put("canWithdraw", EnumBoolean.TRUE.getCode());
                    result.put("cardNo", cardNO.substring(cardNO.length() - 4, cardNO.length()));
                    result.put("bankName", cardBank);
                    result.put("avaWithdraw", jsonObject.getString("avaWithdraw"));
                    result.put("fee", jsonObject.getString("fee"));
                    final BigDecimal receiveAmount = new BigDecimal(jsonObject.getString("avaWithdraw")).compareTo(new BigDecimal(jsonObject.getString("fee"))) == -1 ?
                            new BigDecimal("0") : new BigDecimal(jsonObject.getString("avaWithdraw")).subtract(new BigDecimal(jsonObject.getString("fee")));
                    result.put("receiveAmount", receiveAmount);
                    result.put("withDrawOrderId",jsonObject.getString("withDrawOrderId"));
                } else {
                    result.put("canWithdraw", EnumBoolean.FALSE.getCode());
                    result.put("phone", appAuUser.getCellphone());
                }
            else{
                result.put("canWithdraw", EnumBoolean.FALSE.getCode());
                result.put("phone", appAuUser.getCellphone());
            }


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

    /**
     *  {@inheritDoc}
     * @param dataParam
     * @param appParam
     * @return
     */
    @Transactional
    @Override
    public String withdraw(String dataParam, AppParam appParam) {

        final JSONObject dataJo = JSONObject.parseObject(dataParam);
        final JSONObject result = new JSONObject();
        final long withDrawOrderId = dataJo.getLongValue(dataJo.getString("withDrawOrderId"));
        Pair<Integer, String> pair =  this.orderService.confirmWithdraw(withDrawOrderId);
    }


}
