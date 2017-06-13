package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.dealer.entity.DealerRatePolicy;
import com.jkm.hss.dealer.service.DealerRatePolicyService;
import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.product.entity.ProductRatePolicy;
import com.jkm.hss.product.servcie.ProductRatePolicyService;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.dao.UserTradeRateDao;
import com.jkm.hsy.user.entity.UserTradeRate;
import com.jkm.hsy.user.entity.UserWithdrawRate;
import com.jkm.hsy.user.help.requestparam.UserTradeRateListResponse;
import com.jkm.hsy.user.help.requestparam.UserTradeRateResponse;
import com.jkm.hsy.user.service.UserTradeRateService;
import com.jkm.hsy.user.service.UserWithdrawRateService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private UserWithdrawRateService userWithdrawRateService;
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
     * 更新
     *
     * @param userTradeRate
     */
    @Override
    public void update(UserTradeRate userTradeRate) {
        userTradeRateDao.update(userTradeRate);
    }

    /**
     * 根据法人编码和政策类型查询
     *
     * @param userId
     * @param policyType
     * @return
     */
    @Override
    public Optional<UserTradeRate> selectByUserIdAndPolicyType(long userId, String policyType) {
        return Optional.fromNullable(userTradeRateDao.selectByUserIdAndPolicyType(userId,policyType));
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

    /**
     * 获取当前商户T1费率
     *
     * @param userId
     * @return
     */
    @Override
    public Pair<BigDecimal, BigDecimal> getCurrentUserRate(long userId) {
        BigDecimal wxRate = null;
        BigDecimal zfbRate = null;
        UserTradeRate wxTradeRate = userTradeRateDao.selectByUserIdAndPolicyType(userId, EnumPolicyType.WECHAT.getId());
        if(wxTradeRate!=null){
            wxRate = wxTradeRate.getTradeRateT1();
        }
        UserTradeRate zfbTradeRate = userTradeRateDao.selectByUserIdAndPolicyType(userId, EnumPolicyType.ALIPAY.getId());
        if(zfbTradeRate!=null){
            wxRate = zfbTradeRate.getTradeRateT1();
        }
        return Pair.of(wxRate,zfbRate);
    }

    /**
     * 保存商户费率
     * @param userId
     * @param wxRate
     * @param zfbRate
     * @param isOpenWx
     * @param isOpenZfb
     */
    @Override
    public  void saveUserRate(long userId,long dealerId,BigDecimal wxRate,BigDecimal zfbRate,int isOpenWx,int isOpenZfb) {
        UserTradeRate wx = new UserTradeRate();
        UserTradeRate wxUserTradeRate = userTradeRateDao.selectByUserIdAndPolicyType(userId,EnumPolicyType.WECHAT.getId());
        if(wxUserTradeRate==null){
            wx.setUserId(userId);
            wx.setPolicyType(EnumPolicyType.WECHAT.getId());
            wx.setIsOpen(isOpenWx);
            wx.setTradeRateT1(wxRate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
            wx.setTradeRateD0(wx.getTradeRateT1().add(new BigDecimal("0.0004")));
            wx.setStatus(EnumStatus.NORMAL.getId());
            this.insert(wx);
        }else{
            wx.setId(wxUserTradeRate.getId());
            wx.setUserId(userId);
            wx.setPolicyType(EnumPolicyType.WECHAT.getId());
            wx.setIsOpen(isOpenWx);
            wx.setTradeRateT1(wxRate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
            wx.setTradeRateD0(wx.getTradeRateT1().add(new BigDecimal("0.0004")));
            wx.setStatus(EnumStatus.NORMAL.getId());
            this.insert(wx);
        }


        UserTradeRate zfb = new UserTradeRate();
        UserTradeRate zfbUserTradeRate = userTradeRateDao.selectByUserIdAndPolicyType(userId,EnumPolicyType.ALIPAY.getId());
        if(zfbUserTradeRate==null){
            zfb.setUserId(userId);
            zfb.setPolicyType(EnumPolicyType.ALIPAY.getId());
            zfb.setIsOpen(isOpenZfb);
            zfb.setTradeRateT1(zfbRate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
            zfb.setTradeRateD0(zfb.getTradeRateT1().add(new BigDecimal("0.0004")));
            zfb.setStatus(EnumStatus.NORMAL.getId());
            this.insert(zfb);
        }else{
            zfb.setId(zfbUserTradeRate.getId());
            zfb.setUserId(userId);
            zfb.setPolicyType(EnumPolicyType.ALIPAY.getId());
            zfb.setIsOpen(isOpenZfb);
            zfb.setTradeRateT1(zfbRate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
            zfb.setTradeRateD0(zfb.getTradeRateT1().add(new BigDecimal("0.0004")));
            zfb.setStatus(EnumStatus.NORMAL.getId());
            this.insert(zfb);
        }


        UserWithdrawRate wd = new UserWithdrawRate();
        Optional<UserWithdrawRate> userWithdrawRateOptional = userWithdrawRateService.selectByUserId(userId);
        if(!userWithdrawRateOptional.isPresent()){
            wd.setUserId(userId);
            wd.setWithdrawRateT1(new BigDecimal("0"));
            wd.setWithdrawRateD0(new BigDecimal("3"));
            wd.setStatus(EnumStatus.NORMAL.getId());
            userWithdrawRateService.insert(wd);
        }else{
            wd.setId(userWithdrawRateOptional.get().getId());
            wd.setUserId(userId);
            wd.setWithdrawRateT1(new BigDecimal("0"));
            wd.setWithdrawRateD0(new BigDecimal("3"));
            wd.setStatus(EnumStatus.NORMAL.getId());
            userWithdrawRateService.update(wd);
        }

    }

    /**
     * 商户费率信息
     *
     * @return
     */
    @Override
    public List<UserTradeRateListResponse> getUserRate(long userId) {
        List<UserTradeRateListResponse> userTradeRateListResponses = new ArrayList<UserTradeRateListResponse>();
        List<UserTradeRate> userTradeRateList = userTradeRateDao.selectAllByUserId(userId);
        if(userTradeRateList.size()>0){
            for(int i=0;i<userTradeRateList.size();i++){
                UserTradeRateListResponse rateListResponse = new UserTradeRateListResponse();
                if((EnumPolicyType.WECHAT.getId()).equals(userTradeRateList.get(i).getPolicyType())){
                    rateListResponse.setRateName(EnumPolicyType.WECHAT.getName());
                }
                if((EnumPolicyType.ALIPAY.getId()).equals(userTradeRateList.get(i).getPolicyType())){
                    rateListResponse.setRateName(EnumPolicyType.ALIPAY.getName());
                }
                rateListResponse.setPolicyType(userTradeRateList.get(i).getPolicyType());
                if(userTradeRateList.get(i).getTradeRateT1()!=null&&!"".equals(userTradeRateList.get(i).getTradeRateT1())){
                    rateListResponse.setTradeRateT1(userTradeRateList.get(i).getTradeRateT1().multiply(new BigDecimal("100")));
                }
                if(userTradeRateList.get(i).getTradeRateD1()!=null&&!"".equals(userTradeRateList.get(i).getTradeRateD1())){
                    rateListResponse.setTradeRateD1(userTradeRateList.get(i).getTradeRateD1().multiply(new BigDecimal("100")));
                }
                if(userTradeRateList.get(i).getTradeRateD0()!=null&&!"".equals(userTradeRateList.get(i).getTradeRateD0())){
                    rateListResponse.setTradeRateD0(userTradeRateList.get(i).getTradeRateD0().multiply(new BigDecimal("100")));
                }
                userTradeRateListResponses.add(rateListResponse);
            }
        }else{
            UserTradeRateListResponse wx = new UserTradeRateListResponse();
            wx.setRateName(EnumPolicyType.WECHAT.getName());
            wx.setPolicyType(EnumPolicyType.WECHAT.getId());
            userTradeRateListResponses.add(wx);
            UserTradeRateListResponse zfb = new UserTradeRateListResponse();
            zfb.setRateName(EnumPolicyType.ALIPAY.getName());
            zfb.setPolicyType(EnumPolicyType.ALIPAY.getId());
            userTradeRateListResponses.add(zfb);
        }
        Optional<UserWithdrawRate> userWithdrawRateOptional = userWithdrawRateService.selectByUserId(userId);
        if(userWithdrawRateOptional.isPresent()){
            UserTradeRateListResponse withdraw = new UserTradeRateListResponse();
            withdraw.setRateName(EnumPolicyType.WITHDRAW.getName());
            withdraw.setPolicyType(EnumPolicyType.WITHDRAW.getId());
            withdraw.setTradeRateT1(userWithdrawRateOptional.get().getWithdrawRateT1());
            withdraw.setTradeRateD1(userWithdrawRateOptional.get().getWithdrawRateD1());
            withdraw.setTradeRateD0(userWithdrawRateOptional.get().getWithdrawRateD0());
            userTradeRateListResponses.add(withdraw);
        }else{
            UserTradeRateListResponse withdraw = new UserTradeRateListResponse();
            withdraw.setRateName(EnumPolicyType.WITHDRAW.getName());
            withdraw.setPolicyType(EnumPolicyType.WITHDRAW.getId());
            userTradeRateListResponses.add(withdraw);
        }
        return userTradeRateListResponses;
    }

    /**
     * 商户费率
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserTradeRate> selectAllByUserId(long userId) {
        return userTradeRateDao.selectAllByUserId(userId);
    }


}
