package com.jkm.hss.controller.merchant;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.helper.requestparam.ChangeBankCardRequest;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumAccountBank;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xingliujie on 2017/3/7.
 */
@Controller
@RequestMapping(value = "/admin/accountBank")
public class AccountBankController extends BaseController {
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;


    /**
     * 更改银行卡信息
     * @param changeBankCardRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeBankCard", method = RequestMethod.POST)
    public CommonResponse changeBankCard (@RequestBody ChangeBankCardRequest changeBankCardRequest) {
        if (changeBankCardRequest.getMerchantId()<=0) {
            return CommonResponse.simpleResponse(-1, "商户编码不能为空");
        }
        if (StringUtils.isEmpty(changeBankCardRequest.getBankNo())) {
            return CommonResponse.simpleResponse(-1, "银行卡号不能为空");
        }
        if (StringUtils.isEmpty(changeBankCardRequest.getReserveMobile())) {
            return CommonResponse.simpleResponse(-1, "银行预留手机号不能为空");
        }
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(changeBankCardRequest.getBankNo());
        if (!bankCardBinOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "银行卡格式错误");
        }
        if("1".equals(bankCardBinOptional.get().getCardTypeCode())){
            return CommonResponse.simpleResponse(-1, "只能输入储蓄卡");
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(changeBankCardRequest.getMerchantId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "商户不存在");
        }
//        Optional<AccountBank> accountBankOptional = accountBankService.isExistBankNo(merchantInfoOptional.get().getAccountId(), MerchantSupport.encryptBankCard(changeBankCardRequest.getBankNo()), EnumAccountBank.DEBITCARD.getId());
//        if(accountBankOptional.isPresent()){
//            return CommonResponse.simpleResponse(-1, "银行卡号已存在");
//        }
        final JSONObject jsonObject = merchantChannelRateService.updateKmBankInfo(merchantInfoOptional.get().getAccountId(), merchantInfoOptional.get().getId(), changeBankCardRequest.getBankNo());
        final int code = jsonObject.getInt("code");
        if (code==1) {
            accountBankService.changeBankCard(merchantInfoOptional.get(), changeBankCardRequest.getBankNo(), changeBankCardRequest.getReserveMobile(),
                    changeBankCardRequest.getBranchCityCode(),changeBankCardRequest.getBranchCityName(),
                    changeBankCardRequest.getBranchCountyCode(),changeBankCardRequest.getBranchCountyName(),
                    changeBankCardRequest.getBranchProvinceCode(),changeBankCardRequest.getBranchProvinceName(),changeBankCardRequest.getBranchName());
        }else {
            final String msg = jsonObject.getString("msg");
            return CommonResponse.simpleResponse(-1, msg);

        }
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "更改成功");
    }

}
