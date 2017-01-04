package com.jkm.hss.controller.merchant;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoCheckRecordService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.VerifyIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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

    @ResponseBody
    @RequestMapping(value = "/record",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> record(@RequestBody final RequestMerchantInfo requestMerchantInfo){

        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(requestMerchantInfo.getMerchantId());
        if (!merchantInfoOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "商户不存在");
        }
        this.merchantInfoCheckRecordService.save(requestMerchantInfo);
        final MerchantInfo merchant = merchantInfoOptional.get();
        final long accountId = this.accountService.initAccount(merchant.getMerchantName());
        merchant.setAccountId(accountId);
        merchant.setStatus(EnumMerchantStatus.PASSED.getId());
        merchant.setCheckedTime(new Date());
        this.merchantInfoService.addAccountId(accountId, EnumMerchantStatus.PASSED.getId(), merchant.getId(),merchant.getCheckedTime());
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"审核通过");
    }

        @ResponseBody
        @RequestMapping(value = "/auditFailure",method = RequestMethod.POST)
        public CommonResponse<BaseEntity> auditFailure(@RequestBody final RequestMerchantInfo requestMerchantInfo){

            final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(requestMerchantInfo.getMerchantId());
            final MerchantInfo merchantInfo = merchantInfoOptional.get();

            this.merchantInfoCheckRecordService.save(requestMerchantInfo);

            requestMerchantInfo.setStatus(EnumMerchantStatus.UNPASSED.getId());
            int i = this.merchantInfoCheckRecordService.updateStatus(requestMerchantInfo);
            this.verifyIdService.markToIneffective(MerchantSupport.decryptMobile(merchantInfo.getMobile()));
            return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"审核未通过");

//            return "";
        }




}
