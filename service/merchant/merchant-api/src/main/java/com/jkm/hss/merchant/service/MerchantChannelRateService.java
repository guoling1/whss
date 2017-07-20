package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantEnterInRequest;
import com.jkm.hss.merchant.helper.request.MerchantGetRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantUpgradeRequest;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by xingliujie on 2017/2/27.
 */
public interface MerchantChannelRateService {
    /**
     * 费率初始化
     * @param merchantChannelRate
     * @return
     */
    int initMerchantChannelRate(MerchantChannelRate merchantChannelRate);

    /**
     *根据商户编码、通道标示、产品编码查询商户费用
     * @param merchantChannelRateRequest
     * @return
     */
    Optional<MerchantChannelRate> selectByChannelTypeSignAndProductIdAndMerchantId(MerchantChannelRateRequest merchantChannelRateRequest);

    /**
     * 查询入网中商户的信息
     * @return
     */
    List<Long> selectIngMerchantInfo();

    /**
     * 更新商户入网状态
     * @param merchantId
     * @param enumEnterNet
     */
    void updateEnterNetStatus(long merchantId, EnumEnterNet enumEnterNet, String msg);


    /**
     * 根据商户id查询 通道列表
     * @param merchantId
     * @return
     */
    List<MerchantChannelRate> selectByMerchantId(long merchantId);
    /**
     *根据商户编码、通道标示、产品编码查询商户费用
     * @param merchantGetRateRequest
     * @return
     */
    List<MerchantChannelRate> selectByThirdCompanyAndProductIdAndMerchantId(MerchantGetRateRequest merchantGetRateRequest);

    /**
     *根据公司名、通道标示、产品编码查询商户费用
     * @param merchantEnterInRequest
     * @return
     */
    List<MerchantChannelRate> selectByChannelCompanyAndProductIdAndMerchantId(MerchantEnterInRequest merchantEnterInRequest);

    /**
     * 商户入网
     */
    void enterInterNet(long productId,long merchantId,String channelCompany);

    /**
     * 升级降费率
     * @param merchantUpgradeRequest
     */
    void toUpgrade(MerchantUpgradeRequest merchantUpgradeRequest);

    /**
     * 查询失败
     * @return
     */
    List<Long> selectFailMerchantInfo();

    /**
     * 商户入网
     */
    JSONObject enterInterNet1(long accountId,long productId, long merchantId, String channelCompany);
    /**
     * 修改商户入网
     */
    void updateInterNet(long accountId,long merchantId);
    /**
     * 修改银行卡信息
     */
    JSONObject updateKmBankInfo(long accountId,long merchantId,String oriBankNo);
    /**
     * 修改卡盟联行号
     */
    void updateKmBranchInfo(long accountId,long merchantId);
}
