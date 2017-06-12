package com.jkm.hsy.user.service.impl;

import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.dao.UserChannelPolicyDao;
import com.jkm.hsy.user.dao.UserCurrentChannelPolicyDao;
import com.jkm.hsy.user.entity.UserChannelPolicy;
import com.jkm.hsy.user.entity.UserCurrentChannelPolicy;
import com.jkm.hsy.user.help.requestparam.UserChannelPolicyResponse;
import com.jkm.hsy.user.service.UserChannelPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Slf4j
@Service
public class UserChannelPolicyServiceImpl implements UserChannelPolicyService{
    @Autowired
    private UserChannelPolicyDao userChannelPolicyDao;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private UserCurrentChannelPolicyDao userCurrentChannelPolicyDao;

    /**
     * 新增
     *
     * @param userChannelPolicy
     */
    @Override
    public void insert(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.insert(userChannelPolicy);
    }
    /**
     * 修改
     *
     * @param userChannelPolicy
     */
    @Override
    public void updateInit(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateInit(userChannelPolicy);
    }

    /**
     * 初始化商户通道
     *
     * @param userId
     */
    @Override
    public void initChannel(long userId) {
        List<BasicChannel> basicChannels =  basicChannelService.selectHsyAll();
        if(basicChannels.size()>0){
            for(int i=0;i<basicChannels.size();i++){
                UserChannelPolicy uc =  userChannelPolicyDao.selectByChannelTypeSign(basicChannels.get(i).getChannelTypeSign());
                if(uc==null){
                    UserChannelPolicy userChannelPolicy = new UserChannelPolicy();
                    userChannelPolicy.setUserId(userId);
                    if("微信".equals(basicChannels.get(i).getThirdCompany())){
                        userChannelPolicy.setPolicyType(EnumPolicyType.WECHAT.getId());
                    }
                    if("支付宝".equals(basicChannels.get(i).getThirdCompany())){
                        userChannelPolicy.setPolicyType(EnumPolicyType.ALIPAY.getId());
                    }
                    userChannelPolicy.setChannelName(basicChannels.get(i).getChannelName());
                    userChannelPolicy.setChannelTypeSign(basicChannels.get(i).getChannelTypeSign());
                    userChannelPolicy.setSettleType(basicChannels.get(i).getBasicBalanceType());
                    userChannelPolicy.setNetStatus(0);
                    userChannelPolicy.setOpenProductStatus(0);
                    userChannelPolicy.setStatus(EnumStatus.NORMAL.getId());
                    this.insert(userChannelPolicy);
                }else{
                    UserChannelPolicy userChannelPolicy = new UserChannelPolicy();
                    userChannelPolicy.setId(uc.getId());
                    userChannelPolicy.setUserId(userId);
                    if("微信".equals(basicChannels.get(i).getThirdCompany())){
                        userChannelPolicy.setPolicyType(EnumPolicyType.WECHAT.getId());
                    }
                    if("支付宝".equals(basicChannels.get(i).getThirdCompany())){
                        userChannelPolicy.setPolicyType(EnumPolicyType.ALIPAY.getId());
                    }
                    userChannelPolicy.setChannelName(basicChannels.get(i).getChannelName());
                    userChannelPolicy.setChannelTypeSign(basicChannels.get(i).getChannelTypeSign());
                    userChannelPolicy.setSettleType(basicChannels.get(i).getBasicBalanceType());
                    userChannelPolicy.setNetStatus(0);
                    userChannelPolicy.setOpenProductStatus(0);
                    userChannelPolicy.setStatus(EnumStatus.NORMAL.getId());
                    this.updateInit(userChannelPolicy);
                }
            }
        }
        UserCurrentChannelPolicy ucc = userCurrentChannelPolicyDao.selectByUserId(userId);
        if(ucc==null){
            UserCurrentChannelPolicy userCurrentChannelPolicy = new UserCurrentChannelPolicy();
            userCurrentChannelPolicy.setUserId(userId);
            userCurrentChannelPolicy.setStatus(EnumStatus.NORMAL.getId());
            userCurrentChannelPolicy.setWechatChannelTypeSign(EnumPayChannelSign.SYJ_WECHAT.getId());
            userCurrentChannelPolicy.setAlipayChannelTypeSign(EnumPayChannelSign.SYJ_ALIPAY.getId());
            userCurrentChannelPolicyDao.insert(userCurrentChannelPolicy);
        }
    }

    /**
     * 根据用户编码和通道编码修改
     * @param userChannelPolicy
     */
    @Override
    public void updateByUserIdAndChannelTypeSign(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateByUserIdAndChannelTypeSign(userChannelPolicy);
    }

    /**
     * 修改华夏入网信息
     *
     * @param userChannelPolicy
     */
    @Override
    public void updateHxNetInfo(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateHxNetInfo(userChannelPolicy);
    }

    /**
     * 修改华夏开通产品信息
     *
     * @param userChannelPolicy
     */
    @Override
    public void updateHxOpenProduct(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateHxOpenProduct(userChannelPolicy);
    }

    /**
     * 用户通道列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserChannelPolicyResponse> getUserChannelList(long userId) {
        return null;
    }


}
