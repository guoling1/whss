package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerRatePolicy;
import com.jkm.hss.dealer.service.DealerRatePolicyService;
import com.jkm.hss.product.entity.ProductRatePolicy;
import com.jkm.hss.product.servcie.ProductRatePolicyService;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.dao.UserTradeRateDao;
import com.jkm.hsy.user.entity.UserTradeRate;
import com.jkm.hsy.user.help.requestparam.UserTradeRateResponse;
import com.jkm.hsy.user.service.UserTradeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Service
public class UserTradeRateServiceImpl implements UserTradeRateService {
    @Autowired
    private UserTradeRateDao userTradeRateDao;
    @Autowired
    private ProductRatePolicyService productRatePolicyService;
    @Autowired
    private DealerRatePolicyService dealerRatePolicyService;
    /**
     * 新增
     *
     * @param userTradeRate
     */
    @Override
    public void insert(UserTradeRate userTradeRate) {
        userTradeRateDao.insert(userTradeRate);
    }

    /**
     * 查询T1结算费率
     *
     * @param dealerId
     * @return
     */
    @Override
    public UserTradeRateResponse getRateRang(long dealerId) {
        UserTradeRateResponse userTradeRateResponse = new UserTradeRateResponse();
        if(dealerId>0){
            Optional<DealerRatePolicy> wxPolicy = dealerRatePolicyService.selectByDealerIdAndPolicyType(dealerId,EnumPolicyType.WECHAT.getId());
            if(wxPolicy.isPresent()){
                userTradeRateResponse.setWxMinT1Trade(wxPolicy.get().getMerchantMinRateT1().multiply(new BigDecimal("100")).setScale(2));
                userTradeRateResponse.setWxMaxT1Trade(wxPolicy.get().getMerchantMaxRateT1().multiply(new BigDecimal("100")).setScale(2));
            }
            Optional<DealerRatePolicy> aliPolicy =  dealerRatePolicyService.selectByDealerIdAndPolicyType(dealerId,EnumPolicyType.ALIPAY.getId());
            if(aliPolicy.isPresent()){
                userTradeRateResponse.setZfbMinT1Trade(aliPolicy.get().getMerchantMinRateT1().multiply(new BigDecimal("100")).setScale(2));
                userTradeRateResponse.setZfbMaxT1Trade(aliPolicy.get().getMerchantMaxRateT1().multiply(new BigDecimal("100")).setScale(2));
            }
        }else{
            Optional<ProductRatePolicy> wxPolicy =  productRatePolicyService.selectByPolicyType(EnumPolicyType.WECHAT.getId());
            if(wxPolicy.isPresent()){
                userTradeRateResponse.setWxMinT1Trade(wxPolicy.get().getMerchantMinRateT1().multiply(new BigDecimal("100")).setScale(2));
                userTradeRateResponse.setWxMaxT1Trade(wxPolicy.get().getMerchantMaxRateT1().multiply(new BigDecimal("100")).setScale(2));
            }
            Optional<ProductRatePolicy> aliPolicy =  productRatePolicyService.selectByPolicyType(EnumPolicyType.ALIPAY.getId());
            if(aliPolicy.isPresent()){
                userTradeRateResponse.setZfbMinT1Trade(aliPolicy.get().getMerchantMinRateT1().multiply(new BigDecimal("100")).setScale(2));
                userTradeRateResponse.setZfbMaxT1Trade(aliPolicy.get().getMerchantMaxRateT1().multiply(new BigDecimal("100")).setScale(2));
            }
        }
        return userTradeRateResponse;
    }


}
