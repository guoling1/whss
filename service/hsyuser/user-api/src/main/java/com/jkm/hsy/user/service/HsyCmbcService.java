package com.jkm.hsy.user.service;

import com.jkm.hsy.user.help.requestparam.CmbcProductResponse;
import com.jkm.hsy.user.help.requestparam.CmbcResponse;
import com.jkm.hsy.user.help.requestparam.XmmsResponse;

/**
 * Created by xingliujie on 2017/4/17.
 */
public interface HsyCmbcService {

    /**
     * 修改商户费率
     * @param userId
     * @param shopId
     * @return
     */
    boolean merchantInfoModify(long userId,long shopId,long adminId,String opt);
    /**
     * 民生银行商户基础信息注册
     * @param userId //用户编码
     * @param shopId //主店编码
     * @return
     */
    CmbcResponse merchantBaseInfoReg(long userId,long shopId);
    /**
     * 民生银行商户基础信息修改
     * @param userId //用户编码
     * @param shopId //主店编码
     * @return
     */
    CmbcResponse merchantBaseInfoModify(long userId,long shopId);
    /**
     * 收银家开通产品
     * @return
     */
    CmbcProductResponse merchantBindChannel(long userId, long shopId);
    /**
     * 民生银行商户支付修改通道绑定
     * @return
     */
    CmbcResponse merchantUpdateBindChannel(long userId);

    /**
     * 厦门民生入网
     * @param userId //用户编码
     * @param shopId //主店编码
     */
    XmmsResponse merchantIn(long userId, long shopId);
    /**
     * 厦门民生修改
     * @param userId //用户编码
     * @param shopId //主店编码
     */
    XmmsResponse.BaseResponse merchantModify(long userId, long shopId,int channelTypeSign);

    /**
     * 厦门民生入网
     * @param userId //用户编码
     * @param shopId //主店编码
     */
    XmmsResponse.BaseResponse merchantIn(long userId, long shopId,int channelTypeSign);
}
