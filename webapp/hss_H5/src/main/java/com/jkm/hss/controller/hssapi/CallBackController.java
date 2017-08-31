package com.jkm.hss.controller.hssapi;

import com.google.common.base.Optional;
import com.jkm.api.enums.EnumOpenCardStatus;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.dao.OpenCardRecordDao;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.OpenCardRecord;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Slf4j
@Controller
@RequestMapping(value = "/api/callback")
public class CallBackController extends BaseController {
    @Autowired
    private OpenCardRecordDao openCardRecordDao;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private BankCardBinService bankCardBinService;
    /**
     * 开卡回调
     * @return
     */
    @RequestMapping(value = "openCard", method = RequestMethod.GET)
    public Object openCard(@RequestParam(value = "orderId", required = true) String orderId,
                           @RequestParam(value = "activateStatus", required = true) String activateStatus,
                           @RequestParam(value = "token", required = true) String token,
                           @RequestParam(value = "msg", required = true) String msg) {
        final OpenCardRecord openCardRecord = openCardRecordDao.selectByBindCardReqNo(orderId);
        if(openCardRecord == null){
            log.error("开卡流水号[{}]-开卡回调没有开卡记录!!!!!!!!", orderId);
            return null;
        }
        int realStatus = 0;
        if("1".equals(activateStatus)){
            realStatus = 1;
            if(openCardRecord.getStatus()== EnumOpenCardStatus.SUBMIT.getId()){
                openCardRecordDao.updateStatusByBindCardReqNo(orderId,realStatus);
                Optional<MerchantInfo> mci =  merchantInfoService.getByMarkCode(openCardRecord.getMerchantNo());
                Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(openCardRecord.getCardNo());
                accountBankService.bindCard(mci.get().getAccountId(),openCardRecord.getCardNo(),bankCardBinOptional.get().getBankName(),
                        mci.get().getReserveMobile(),bankCardBinOptional.get().getShorthand(),token);
            }
        }else{
            realStatus = 2;
        }

        String htmlTemp = openCardRecord.getFrontUrl();
        String[] arrTemp = htmlTemp.split("\\?");
        String returnUrl = "";
        if(arrTemp.length>1){
            returnUrl = htmlTemp+"?orderId="+orderId+"&activateStatus="+realStatus+"&msg="+msg;
        }else{
            returnUrl = htmlTemp+"?orderId="+orderId+"&activateStatus="+realStatus+"&msg="+msg;
        }
        return "redirect:"+ returnUrl;
    }
}
