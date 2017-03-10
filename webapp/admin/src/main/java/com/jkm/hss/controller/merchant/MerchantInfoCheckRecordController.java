package com.jkm.hss.controller.merchant;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.MerchantInfoCheckRecord;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.notifier.service.SmsAuthService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/24.
 */
@Controller
@RequestMapping(value = "/admin/merchantInfoCheckRecord")
public class MerchantInfoCheckRecordController extends BaseController {

    @Autowired
    private MerchantInfoCheckRecordService merchantInfoCheckRecordService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private VerifyIdService verifyIdService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SendMsgService sendMsgService;

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private SmsAuthService smsAuthService;

    @Autowired
    private AccountBankService accountBankService;

    @ResponseBody
    @RequestMapping(value = "/record",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> record(@RequestBody final RequestMerchantInfo requestMerchantInfo){

        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(requestMerchantInfo.getMerchantId());
        if (!merchantInfoOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "商户不存在");
        }
        MerchantInfo merchantInfo = merchantInfoOptional.get();
        requestMerchantInfo.setStatus(EnumMerchantStatus.PASSED.getId());
        this.merchantInfoCheckRecordService.save(requestMerchantInfo);
        final MerchantInfo merchant = merchantInfoOptional.get();
        final long accountId = this.accountService.initAccount(merchant.getMerchantName());
        merchant.setAccountId(accountId);
        merchant.setStatus(EnumMerchantStatus.PASSED.getId());
        merchant.setCheckedTime(new Date());
        this.merchantInfoService.addAccountId(accountId, EnumMerchantStatus.PASSED.getId(), merchant.getId(),merchant.getCheckedTime());
        //插入银行卡账户表
        accountBankService.initAccountBank(merchant.getId(),accountId);
        Optional<UserInfo> toUer = userInfoService.selectByMerchantId(requestMerchantInfo.getMerchantId());
        String toUsers = toUer.get().getOpenId();
        Date date = new Date();
        sendMsgService.sendAuditThroughMessage(EnumMerchantStatus.PASSED.getName(),date,toUsers);
        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(MerchantSupport.decryptMobile(merchantInfo.getMobile()), EnumVerificationCodeType.MERCHANT_AUDIT);
        if (1 == verifyCode.getLeft()) {
            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
            this.sendMessageService.sendMessage(SendMessageParams.builder()
                    .mobile(MerchantSupport.decryptMobile(merchantInfo.getMobile()))
                    .uid("")
                    .data(params)
                    .userType(EnumUserType.BACKGROUND_USER)
                    .noticeType(EnumNoticeType.MERCHANT_AUDIT)
                    .build()
            );
        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"审核通过");
    }

        @ResponseBody
        @RequestMapping(value = "/auditFailure",method = RequestMethod.POST)
        public CommonResponse<BaseEntity> auditFailure(@RequestBody final RequestMerchantInfo requestMerchantInfo){

            final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(requestMerchantInfo.getMerchantId());
            final MerchantInfo merchantInfo = merchantInfoOptional.get();
            requestMerchantInfo.setStatus(EnumMerchantStatus.UNPASSED.getId());
            this.merchantInfoCheckRecordService.save(requestMerchantInfo);


            this.merchantInfoCheckRecordService.updateStatus(requestMerchantInfo);
            this.verifyIdService.markToIneffective(MerchantSupport.decryptMobile(merchantInfo.getMobile()));
            Optional<UserInfo> toUer = userInfoService.selectByMerchantId(requestMerchantInfo.getMerchantId());
            String toUsers = toUer.get().getOpenId();
            MerchantInfoCheckRecord desr = userInfoService.selectDesr(requestMerchantInfo.getMerchantId());
            sendMsgService.sendAuditNoThroughMessage(EnumMerchantStatus.UNPASSED.getName(),desr.getDescr(),toUsers);
            final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(MerchantSupport.decryptMobile(merchantInfo.getMobile()), EnumVerificationCodeType.MERCHANT_NO_AUDIT);
            if (1 == verifyCode.getLeft()) {
                final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
                this.sendMessageService.sendMessage(SendMessageParams.builder()
                        .mobile(MerchantSupport.decryptMobile(merchantInfo.getMobile()))
                        .uid("")
                        .data(params)
                        .userType(EnumUserType.BACKGROUND_USER)
                        .noticeType(EnumNoticeType.MERCHANT_NO_AUDIT)
                        .build()
                );
            }
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"审核未通过");

        }


//    @ResponseBody
//    @RequestMapping(value = "/test",method = RequestMethod.POST)
//    public CommonResponse<BaseEntity> test(){
//        String mobile="13301129906";
//        final Pair<Integer, String> verifyCode = this.smsAuthService.getVerifyCode(mobile, EnumVerificationCodeType.MERCHANT_NO_AUDIT);
//        if (1 == verifyCode.getLeft()) {
//            final Map<String, String> params = ImmutableMap.of("code", verifyCode.getRight());
//            this.sendMessageService.sendMessage(SendMessageParams.builder()
//                    .mobile(mobile)
//                    .uid("")
//                    .data(params)
//                    .userType(EnumUserType.BACKGROUND_USER)
//                    .noticeType(EnumNoticeType.MERCHANT_NO_AUDIT)
//                    .build()
//            );
//        }
//        return null;
//    }

}
